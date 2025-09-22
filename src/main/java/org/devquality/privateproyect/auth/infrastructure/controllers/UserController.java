package org.devquality.privateproyect.auth.infrastructure.controllers;

import org.devquality.privateproyect.auth.application.services.IAuthService;
import org.devquality.privateproyect.auth.application.services.IUserService;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.LoginRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.response.AuthResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final IUserService userService;
    private final IAuthService authService;

    @Autowired
    public UserController(IUserService userService, IAuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthResponse>> register(@Valid @RequestBody UserRequest userRequest) {
        BaseResponse<AuthResponse> response = authService.register(userRequest);
        return response.toResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse<AuthResponse> response = authService.login(loginRequest);
        return response.toResponseEntity();
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<Object>> createUser(@Valid @RequestBody UserRequest user) {
        BaseResponse<Object> response = userService.createUser(user);
        return response.toResponseEntity();
    }

    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<AuthResponse>> createUserWithRole(@Valid @RequestBody UserRequest userRequest) {
        BaseResponse<AuthResponse> response = authService.register(userRequest);
        return response.toResponseEntity();
    }

    @PostMapping("/bootstrap-admin")
    public ResponseEntity<BaseResponse<AuthResponse>> bootstrapAdmin(@Valid @RequestBody UserRequest userRequest) {
        BaseResponse<AuthResponse> response = authService.bootstrapAdmin(userRequest);
        return response.toResponseEntity();
    }
}
