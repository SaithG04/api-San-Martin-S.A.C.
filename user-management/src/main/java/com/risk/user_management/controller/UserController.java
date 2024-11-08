package com.risk.user_management.controller;

import com.risk.user_management.domain.User;
import com.risk.user_management.domain.UserRole;
import com.risk.user_management.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(tags = "User Management", description = "Operations related to user management")
public class UserController {
    @Autowired
    private UserService userService;

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

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/assign-role")
    public ResponseEntity<User> assignRoleToUser(@PathVariable Long id, @RequestParam UserRole role) {
        User updatedUser = userService.assignRoleToUser(id, role);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<UserRole[]> getAllRoles() {
        return new ResponseEntity<>(UserRole.values(), HttpStatus.OK);
    }
}
