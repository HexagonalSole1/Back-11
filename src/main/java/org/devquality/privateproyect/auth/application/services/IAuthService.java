package org.devquality.privateproyect.auth.application.services;

import org.devquality.privateproyect.auth.infrastructure.dtos.request.LoginRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.response.AuthResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;

public interface IAuthService {
    BaseResponse<AuthResponse> login(LoginRequest loginRequest);
    BaseResponse<AuthResponse> register(UserRequest userRequest);
    BaseResponse<AuthResponse> bootstrapAdmin(UserRequest userRequest);
}
