package com.risk.user_management.service;

import com.risk.user_management.domain.User;
import com.risk.user_management.domain.UserRole;
import com.risk.user_management.exception.InvalidCurrentPasswordException;
import com.risk.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Registrar un usuario
    public User registerUser(User user) {
        // Verificar si el nombre de usuario ya existe
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already taken");
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

    // Listar todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Actualizar información completa de un usuario (solo para administradores)
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }



    // Actualizar solo la información de perfil del usuario autenticado
    public User updateUserProfile(String username, User updatedProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Permitir solo la actualización de campos personales
        user.setFirstName(updatedProfile.getFirstName());
        user.setLastName(updatedProfile.getLastName());
        user.setPhone(updatedProfile.getPhone());

        return userRepository.save(user);
    }

    // Eliminar un usuario
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public User assignRoleToUser(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        return userRepository.save(user);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
