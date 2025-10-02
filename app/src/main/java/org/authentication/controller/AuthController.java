package org.authentication.controller;

import org.authentication.entities.RefreshToken;
import org.authentication.model.UserInfoDto;
import org.authentication.response.JwtResponseDto;
import org.authentication.service.JwtService;
import org.authentication.service.RefreshTokenService;
import org.authentication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        try {
            Boolean isSigned = userDetailsService.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSigned)) {
                return new ResponseEntity<> ("User Exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<> (JwtResponseDto.builder().accessToken(jwtToken)
                    .token(refreshToken.getToken()).build(), HttpStatus.OK);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity("Exception in User Service!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
