package org.devquality.privateproyect.auth.infrastructure.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String username;

    private String name;

    private String surname;

    private String lastName;

    private String secondLastName;

    private String phone;

    private String password;

    private String email;

    @Builder.Default
    private String role = "USER"; // Rol por defecto
}
