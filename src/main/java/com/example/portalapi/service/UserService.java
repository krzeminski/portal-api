package com.example.portalapi.service;

import com.example.portalapi.entity.Award;
import com.example.portalapi.entity.ConfirmationToken;
import com.example.portalapi.enumeration.Role;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.CredentialsDTO;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.entity.dto.mapper.UserEntityToDTOMapper;
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

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserEntityToDTOMapper userEntityToDTOMapper;
    private final UserRepository userRepository;
    private final AwardRepository awardRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }


    public String signUpUser(User user) {
        Optional<User> u = userRepository
                .findByEmail(user.getEmail());
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

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                user
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;
    }


    public void activateUser(String email) {
        userRepository.activateUser(email);
    }

    public Optional<UserDTO> getUser(Long id) {
        return userRepository.findById(id)
                .map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

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
    public Page<UserDTO> getUsersWithAwards(Pageable paging) {
        return new PageImpl<>(
                userRepository.findAll()
                        .stream()
                        .map(UserEntityToDTOMapper::convertToUserDTO)
                        .collect(Collectors.toUnmodifiableList()));
    }


    public UserDTO authenticate(CredentialsDTO credentialsDTO) {
        User user = userRepository.findByEmail(credentialsDTO.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, credentialsDTO.getEmail())));
        UserDTO userDTO = UserEntityToDTOMapper.convertToUserDTO(user);
//        if (user.getPassword().equals(credentialsDTO.getPassword())) {
        return userDTO;
//        } else {
//            return null;
//        }
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


}
