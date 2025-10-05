package org.authentication.controller;

import org.authentication.entities.UserInfo;
import org.authentication.entities.UserRole;
import org.authentication.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private final UserDetailsServiceImpl userDetailsService;

    public UserProfileController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {

        String username = authentication.getName();
        UserInfo user = userDetailsService.loadUserEntityByUsername(username);
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("Username", user.getUsername());
        profile.put("Roles", user.getRoles().stream()
                .map(UserRole::getName)
                .collect(Collectors.toList()));
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Welcome", "Hello " + user.getUsername() + "! Here is your profile.");
        response.put("Profile", profile);

        return ResponseEntity.ok(response);
    }
}
