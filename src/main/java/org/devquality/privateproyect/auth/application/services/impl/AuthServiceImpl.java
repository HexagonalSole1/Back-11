package org.devquality.privateproyect.auth.application.services.impl;

import org.devquality.privateproyect.auth.application.mappers.UserMapper;
import org.devquality.privateproyect.auth.application.services.IAuthService;
import org.devquality.privateproyect.auth.application.services.IJwtService;
import org.devquality.privateproyect.auth.domain.entities.Role;
import org.devquality.privateproyect.auth.domain.entities.User;
import org.devquality.privateproyect.auth.domain.repositories.IUserRepository;
import org.devquality.privateproyect.auth.domain.repositories.IRoleRepository;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.LoginRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.request.UserRequest;
import org.devquality.privateproyect.auth.infrastructure.dtos.response.AuthResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, IJwtService jwtService, 
                          AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    /**
     * Obtiene un rol de la base de datos o lo crea si no existe
     */
    private Role getOrCreateRole(Role.RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(roleName)
                            .build();
                    return roleRepository.save(role);
                });
    }

    @Override
    public BaseResponse<AuthResponse> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList()))
                .build();

            return BaseResponse.<AuthResponse>builder()
                .success(true)
                .message("Login exitoso")
                .data(authResponse)
                .status(org.springframework.http.HttpStatus.OK)
                .build();

        } catch (Exception e) {
            return BaseResponse.<AuthResponse>builder()
                .success(false)
                .message("Credenciales inválidas")
                .status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                .build();
        }
    }

    @Override
    public BaseResponse<AuthResponse> register(UserRequest userRequest) {
        try {
            if (userRepository.existsByUsername(userRequest.getUsername())) {
                return BaseResponse.<AuthResponse>builder()
                    .success(false)
                    .message("El nombre de usuario ya existe")
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .build();
            }

            if (userRepository.existsByEmail(userRequest.getEmail())) {
                return BaseResponse.<AuthResponse>builder()
                    .success(false)
                    .message("El email ya está registrado")
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .build();
            }

            User user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            // Asignar rol según request o por defecto
            String roleName = userRequest.getRole() != null ? userRequest.getRole() : "USER";
            Role.RoleName roleEnum;
            
            // Verificar si es el primer usuario (se convierte en ADMIN automáticamente)
            boolean isFirstUser = userRepository.count() == 0;
            
            if (isFirstUser) {
                roleEnum = Role.RoleName.ADMIN; // El primer usuario siempre es ADMIN
            } else {
                try {
                    roleEnum = Role.RoleName.valueOf(roleName.toUpperCase());
                    // Solo permitir USER en registro público si no es el primer usuario
                    if (roleEnum != Role.RoleName.USER) {
                        roleEnum = Role.RoleName.USER;
                    }
                } catch (IllegalArgumentException e) {
                    roleEnum = Role.RoleName.USER; // Fallback a USER si el rol no es válido
                }
            }
            
            Role userRole = getOrCreateRole(roleEnum);
            user.setRoles(Collections.singletonList(userRole));

            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser);

            AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList()))
                .build();

            return BaseResponse.<AuthResponse>builder()
                .success(true)
                .message("Usuario registrado exitosamente")
                .data(authResponse)
                .status(org.springframework.http.HttpStatus.CREATED)
                .build();

        } catch (Exception e) {
            return BaseResponse.<AuthResponse>builder()
                .success(false)
                .message("Error al registrar usuario: " + e.getMessage())
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    @Override
    public BaseResponse<AuthResponse> bootstrapAdmin(UserRequest userRequest) {
        try {
            // Verificar si ya existe un admin
            if (userRepository.count() > 0) {
                return BaseResponse.<AuthResponse>builder()
                    .success(false)
                    .message("Ya existe un administrador en el sistema")
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .build();
            }

            if (userRepository.existsByUsername(userRequest.getUsername())) {
                return BaseResponse.<AuthResponse>builder()
                    .success(false)
                    .message("El nombre de usuario ya existe")
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .build();
            }

            if (userRepository.existsByEmail(userRequest.getEmail())) {
                return BaseResponse.<AuthResponse>builder()
                    .success(false)
                    .message("El email ya está registrado")
                    .status(org.springframework.http.HttpStatus.CONFLICT)
                    .build();
            }

            User user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            // Forzar rol ADMIN para bootstrap
            Role adminRole = getOrCreateRole(Role.RoleName.ADMIN);
            user.setRoles(Collections.singletonList(adminRole));

            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser);

            AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList()))
                .build();

            return BaseResponse.<AuthResponse>builder()
                .success(true)
                .message("Administrador inicial creado exitosamente")
                .data(authResponse)
                .status(org.springframework.http.HttpStatus.CREATED)
                .build();

        } catch (Exception e) {
            return BaseResponse.<AuthResponse>builder()
                .success(false)
                .message("Error al crear administrador inicial: " + e.getMessage())
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }
}
