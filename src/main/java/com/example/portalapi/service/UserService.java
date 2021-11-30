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
import static com.example.portalapi.constant.UserConstant.NO_USER_FOUND_BY_USERNAME;
import static com.example.portalapi.constant.UserConstant.USERNAME_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserEntityToDTOMapper userEntityToDTOMapper;
    private final UserRepository userRepository;
    private final AwardRepository awardRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginAttemptService loginAttemptService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        } else {
            validateLoginAttempt(user);
            return user;
        }
    }

    public String register(User user) {
        Optional<User> u = userRepository.findByEmail(user.getEmail());
        boolean userExists = u.isPresent();

        if (userExists) {
            Optional<ConfirmationToken> token = confirmationTokenService.getTokenByUser(user);
            if (token.isPresent() && (token.get().getConfirmedAt() == null)) {
                return token.get().getToken();
            } else {
                throw new IllegalStateException("email already taken");
            }
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(1), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public void activateUser(String email) {
        userRepository.activateUser(email);
    }

    public Optional<UserDTO> getUserDTO(Long id) {
        return userRepository.findById(id)
                .map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public Optional<UserDTO> getUserDTOByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // TODO: 30.11.2021 validate
    public User update(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            return userRepository.save(getUser(userDTO, user));
        } else {
            return null;
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

    public User addAwards(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());

        List<Award> awardsProxy = new ArrayList<>();

        for (Award awardObj : userDTO.getAwards()) {
            Award tempAward = awardRepository.getById(awardObj.getId());
            awardsProxy.add(tempAward);
        }

        user.setAwards(new HashSet<>(awardsProxy));

        return userRepository.save(user);
    }

    private User getUser(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // TODO: 30.11.2021 fix awards 
//        user.setAwards(userDTO.getAwards());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        if (userDTO.getRole() != null) {
            user.setRole(Role.fromName(userDTO.getRole()));
        } else {
            user.setRole(user.getRole());
        }
        user.setActive(userDTO.isActive());
        user.setLocked(userDTO.isLocked());
        return user;
    }

    private void validateLoginAttempt(User user) {
        if (!user.isLocked()) {
            user.setLocked(loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        User userByNewUsername = userRepository.findByUsername(newUsername);
        User userByNewEmail = userRepository.findByEmail(newEmail).orElse(null);
        if (!currentUsername.isBlank()) {
            User currentUser = userRepository.findByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    public void resetPassword(String email) {
        // TODO: 30.11.2021 add password reset
    }
}
