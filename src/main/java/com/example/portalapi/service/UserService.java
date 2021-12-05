package com.example.portalapi.service;

import com.example.portalapi.entity.Award;
import com.example.portalapi.entity.ConfirmationToken;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.entity.dto.mapper.UserEntityToDTOMapper;
import com.example.portalapi.enumeration.Role;
import com.example.portalapi.exception.EmailExistsException;
import com.example.portalapi.exception.UserNotFoundException;
import com.example.portalapi.exception.UsernameExistsException;
import com.example.portalapi.repository.AwardRepository;
import com.example.portalapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.portalapi.constant.UserConstant.EMAIL_ALREADY_EXISTS;
import static com.example.portalapi.constant.UserConstant.NO_USER_FOUND_BY_EMAIL;
import static com.example.portalapi.constant.UserConstant.NO_USER_FOUND_BY_ID;
import static com.example.portalapi.constant.UserConstant.USERNAME_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserEntityToDTOMapper userEntityToDTOMapper;
    private final UserRepository userRepository;
    private final AwardRepository awardRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        } else {
            validateLoginAttempt(user);
            userRepository.save(user);
            return user;
        }
    }

    public String register(User user) throws EmailExistsException, UsernameExistsException {
        validateNewUsernameAndEmail(user.getUsername(), user.getEmail());

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User newUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(1), newUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public void activateUser(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(NO_USER_FOUND_BY_EMAIL);
        }
    }

    public Optional<UserDTO> getUserDTO(Long id) {
        return userRepository.findById(id)
                .map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public Optional<UserDTO> getUserDTOByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User update(UserDTO userDTO) throws UserNotFoundException {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            return userRepository.save(getUser(userDTO, user));
        } else {
            throw new UserNotFoundException(NO_USER_FOUND_BY_ID);
        }
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public Page<UserDTO> getUsers(Pageable paging) {
        return new PageImpl<>(
                userRepository.findAll()
                        .stream()
                        .map(user -> userEntityToDTOMapper.convertToUserDTO(user))
                        .collect(Collectors.toUnmodifiableList()));
    }


    private User getUser(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        user.setActive(userDTO.isActive());
        user.setLocked(userDTO.isLocked());

        if (userDTO.getRole() != null) {
            user.setRole(Role.fromName(userDTO.getRole()));
        } else {
            user.setRole(user.getRole());
        }

        List<Award> awardsProxy = new ArrayList<>();
        for (Award awardObj : userDTO.getAwards()) {
            Award tempAward = awardRepository.getById(awardObj.getId());
            awardsProxy.add(tempAward);
        }
        user.setAwards(new HashSet<>(awardsProxy));

        return user;
    }

    private void validateLoginAttempt(User user) {
        if (!user.isLocked()) {
            user.setLocked(loginAttemptService.hasExceededMaxAttempts(user.getEmail()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }

    private User validateNewUsernameAndEmail(String newUsername, String newEmail) throws UsernameExistsException, EmailExistsException {
        User userByNewUsername = userRepository.findByUsername(newUsername);
        User userByNewEmail = userRepository.findByEmail(newEmail).orElse(null);
        if (userByNewUsername != null) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
        if (userByNewEmail != null) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
        return null;
    }

    public void resetPassword(String email) {
        // TODO: 30.11.2021 add password reset
    }
}
