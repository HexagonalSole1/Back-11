package org.devquality.privateproyect.auth.application.services;

import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;

public interface IUserService {
    BaseResponse<Object> createUser(UserRequest user);
}