package org.devquality.privateproyect.auth.application.services;

import org.devquality.privateproyect.auth.domain.entities.User;

public interface IJwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, User user);
    String extractUserId(String token);
}
