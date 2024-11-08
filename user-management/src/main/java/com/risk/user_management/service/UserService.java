package com.risk.user_management.service;

import com.risk.user_management.domain.User;
import com.risk.user_management.domain.UserRole;
import com.risk.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Verificar si el nombre de usuario ya existe
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Establecer un rol predeterminado si no se especifica uno
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }
}
