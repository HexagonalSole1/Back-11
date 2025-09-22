package org.devquality.privateproyect.auth.infrastructure.config;

import org.devquality.privateproyect.auth.domain.entities.Role;
import org.devquality.privateproyect.auth.domain.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles por defecto si no existen
        createRoleIfNotExists(Role.RoleName.ADMIN);
        createRoleIfNotExists(Role.RoleName.USER);
        createRoleIfNotExists(Role.RoleName.MODERATOR);
    }

    private void createRoleIfNotExists(Role.RoleName roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
        }
    }
}
