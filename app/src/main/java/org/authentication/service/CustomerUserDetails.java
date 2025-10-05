package org.authentication.service;

import org.authentication.entities.UserInfo;
import org.authentication.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomerUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomerUserDetails(UserInfo user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(UserRole::getName)
                .map(String::toUpperCase)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r)) // Spring expects "ROLE_*"
                .collect(Collectors.toList());

        System.out.println("Authorities in CustomerUserDetails: " + this.authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}