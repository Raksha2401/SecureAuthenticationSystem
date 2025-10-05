package org.authentication.controller;

import org.authentication.model.UserInfoDto;
import org.authentication.service.JwtService;
import org.authentication.service.RefreshTokenService;
import org.authentication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSigned = userDetailsService.signupUser(userInfoDto);

            if (Boolean.FALSE.equals(isSigned)) {
                return ResponseEntity.badRequest().body("User already exists!");
            }
            return ResponseEntity.ok("Signup successful! Please login to continue.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Exception during user registration");
        }
    }

}
