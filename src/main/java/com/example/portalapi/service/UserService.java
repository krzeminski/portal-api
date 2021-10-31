package com.example.portalapi.service;

import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.entity.dto.mapper.UserEntityToDTOMapper;
import com.example.portalapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserEntityToDTOMapper userEntityToDTOMapper;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, UserEntityToDTOMapper userEntityToDTOMapper) {
        this.userRepository = userRepository;
        this.userEntityToDTOMapper = userEntityToDTOMapper;
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
}
