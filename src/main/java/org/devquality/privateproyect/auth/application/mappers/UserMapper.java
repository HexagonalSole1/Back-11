package org.devquality.privateproyect.auth.application.mappers;

import org.devquality.privateproyect.auth.domain.entities.User;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    /**
     * Convierte un UserRequest a una entidad User
     * @param userRequest DTO de entrada
     * @return Entidad User
     */
    public User toEntity(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .lastName(userRequest.getLastName())
                .secondLastName(userRequest.getSecondLastName())
                .phone(userRequest.getPhone())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convierte una entidad User a un UserRequest
     * @param user Entidad User
     * @return DTO UserRequest
     */
    public UserRequest toDto(User user) {
        return UserRequest.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .lastName(user.getLastName())
                .secondLastName(user.getSecondLastName())
                .phone(user.getPhone())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }
}
