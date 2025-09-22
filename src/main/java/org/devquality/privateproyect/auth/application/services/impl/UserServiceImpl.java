package org.devquality.privateproyect.auth.application.services.impl;

import org.devquality.privateproyect.auth.application.mappers.UserMapper;
import org.devquality.privateproyect.auth.application.services.IUserService;
import org.devquality.privateproyect.auth.domain.entities.User;
import org.devquality.privateproyect.auth.domain.repositories.IUserRepository;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public BaseResponse<Object> createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        try {
            User savedUser = userRepository.save(user);
            return BaseResponse.builder()
                    .message("Usuario creado exitosamente")
                    .data(savedUser)
                    .status(org.springframework.http.HttpStatus.OK)
                    .success(true)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .message("Error al crear el usuario: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }
}