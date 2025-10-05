package org.authentication.controller;

import org.authentication.entities.RefreshToken;
import org.authentication.entities.UserInfo;
import org.authentication.entities.UserRole;
import org.authentication.request.AuthRequestDto;
import org.authentication.request.RefreshTokenRequestDto;
import org.authentication.response.JwtResponseDto;
import org.authentication.service.CustomerUserDetails;
import org.authentication.service.JwtService;
import org.authentication.service.RefreshTokenService;
import org.authentication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserInfo user = userDetailsService.loadUserEntityByUsername(request.getUsername());
            String jwtToken = jwtService.generateToken(new CustomerUserDetails(user));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
            return ResponseEntity.ok(
                    JwtResponseDto.builder()
                            .accessToken(jwtToken)
                            .token(refreshToken.getToken())
                            .roles(user.getRoles().stream().map(UserRole::getName).toList())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
        }
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                    String accessToken = jwtService.generateToken(userDetails);

                    return ResponseEntity.ok(
                            JwtResponseDto.builder()
                                    .accessToken(accessToken)
                                    .token(refreshTokenRequestDTO.getToken())
                                    .roles(user.getRoles().stream()
                                            .map(r -> r.getName())
                                            .toList())
                                    .build()
                    );
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB"));
    }
}
