package com.example.portalapi.service;

import com.example.portalapi.entity.Award;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.entity.dto.mapper.UserEntityToDTOMapper;
import com.example.portalapi.repository.AwardRepository;
import com.example.portalapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserEntityToDTOMapper userEntityToDTOMapper;
    private final UserRepository userRepository;
    private final AwardRepository awardRepository;

    public UserService(UserRepository userRepository, UserEntityToDTOMapper userEntityToDTOMapper, AwardRepository awardRepository) {
        this.userRepository = userRepository;
        this.userEntityToDTOMapper = userEntityToDTOMapper;
        this.awardRepository = awardRepository;
    }

    @Transactional
    public Page<UserDTO> getUsersWithAwards(Pageable paging) {
        return new PageImpl<>(
                userRepository.findAll()
                        .stream()
                        .map(UserEntityToDTOMapper::convertToUserDTO)
                        .collect(Collectors.toUnmodifiableList()));
    }

    public Optional<UserDTO> getUser(Long id){
        return userRepository.findById(id)
                .map(UserEntityToDTOMapper::convertToUserDTO);
    }

    public User save(UserDTO userDTO){
        User user = new User();
        return userRepository.save(getUser(userDTO, user));
    }

    public User update(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        return userRepository.save(getUser(userDTO, user));
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User addAwards(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());

        List<Award> awardsProxy = new ArrayList<>();

        for (Award awardObj : userDTO.getAwards()) {
            Award tempAward = awardRepository.getById(awardObj.getId());
            awardsProxy.add(tempAward);
        }

        user.setAwards(new HashSet<> (awardsProxy));

        return userRepository.save(user);
    }

    private User getUser(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setAwards(userDTO.getAwards());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        user.setRole(User.Role.fromName(userDTO.getRole()));
        user.setActive(userDTO.isActive());
        user.setLocked(userDTO.isLocked());
        user.setRole(User.Role.fromName(userDTO.getRole()));
        return user;
    }
}
