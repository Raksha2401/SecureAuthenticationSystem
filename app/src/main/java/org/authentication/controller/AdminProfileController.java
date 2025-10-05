package org.authentication.controller;

import org.authentication.entities.UserInfo;
import org.authentication.entities.UserRole;
import org.authentication.repository.UserRepository;
import org.authentication.service.RefreshTokenService;
import org.authentication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminProfileController {

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDashboard(Authentication authentication) {
        String adminName = authentication.getName();

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            roles.add(auth.getAuthority());
        }

        long totalUsers = userService.countAllUsers();

        long activeTokens = refreshTokenService.countActiveTokens();

        Map<String, Long> usersPerRole = new HashMap<>();
        Iterable<UserInfo> allUsers = userRepository.findAll();
        for (UserInfo user : allUsers) {
            for (UserRole role : user.getRoles()) {
                String roleName = role.getName();
                usersPerRole.put(roleName, usersPerRole.getOrDefault(roleName, 0L) + 1);
            }
        }

        List<String> dashboardLines = new ArrayList<>();
        dashboardLines.add("Welcome, Admin " + adminName + "!");
        dashboardLines.add("You have the following roles: " + String.join(", ", roles) + ".");
        dashboardLines.add("Total registered users: " + totalUsers);
        dashboardLines.add("Active login sessions: " + activeTokens);
        dashboardLines.add("Users per role:");
        for (Map.Entry<String, Long> entry : usersPerRole.entrySet()) {
            dashboardLines.add("  - " + entry.getKey() + ": " + entry.getValue());
        }
        dashboardLines.add("Report generated at: " + Instant.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("admin", adminName);
        response.put("roles", roles);
        response.put("dashboard", dashboardLines);

        return ResponseEntity.ok(response);
    }



}
