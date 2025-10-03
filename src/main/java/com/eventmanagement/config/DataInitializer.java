package com.eventmanagement.config;

import com.eventmanagement.entity.Role;
import com.eventmanagement.entity.User;
import com.eventmanagement.repository.RoleRepository;
import com.eventmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

    }

    private void initializeRoles(){

        if(roleRepository.count() == 0){

            logger.info("Initialisation des rôles...");

            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            Role organizerRole = new Role("ORGANIZER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(organizerRole);

            logger.info("Rôles initialisés avec succès");
        }
    }

    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@eventmanagement.com")) {
            logger.info("Création de l'utilisateur administrateur...");

            User admin = new User();
            admin.setFullname("Administrateur");
            admin.setEmail("admin@eventmanagement.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rôle ADMIN introuvable"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            logger.info("Utilisateur administrateur créé avec succès");
            logger.info("Email: admin@eventmanagement.com");
            logger.info("Mot de passe: admin123");
        }
    }
}
