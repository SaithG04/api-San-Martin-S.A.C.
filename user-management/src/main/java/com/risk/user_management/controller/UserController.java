package com.risk.user_management.controller;

import com.risk.user_management.domain.PasswordResetToken;
import com.risk.user_management.domain.User;
import com.risk.user_management.domain.UserRole;
import com.risk.user_management.exception.UserNotFoundException;
import com.risk.user_management.service.PasswordResetService;
import com.risk.user_management.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(tags = "User Management", description = "Operations related to user management")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetService passwordResetService;

    @ApiOperation(value = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all users", notes = "Requires ADMIN role")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("Usuario autenticado: " + userDetails.getUsername() + ", Roles: " + userDetails.getAuthorities());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/assign-role/{id}")
    public ResponseEntity<User> assignRoleToUser(@PathVariable Long id, @RequestParam UserRole role) {
        User updatedUser = userService.assignRoleToUser(id, role);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<UserRole[]> getAllRoles() {
        return new ResponseEntity<>(UserRole.values(), HttpStatus.OK);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        PasswordResetToken token = passwordResetService.createPasswordResetToken(email);
        return new ResponseEntity<>("Password reset token sent to email", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Principal principal,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.changePassword(user.getId(), currentPassword, newPassword);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody User updatedProfile) {

        User user = userService.updateUserProfile(userDetails.getUsername(), updatedProfile);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Long id, @RequestParam boolean isActive) {
        User updatedUser = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        updatedUser.setActive(isActive);
        userService.updateUser(id, updatedUser);

        String status = isActive ? "activated" : "deactivated";
        return ResponseEntity.ok("User " + status + " successfully");
    }

}
