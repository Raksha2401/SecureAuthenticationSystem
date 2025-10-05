package org.authentication.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.authentication.entities.UserInfo;
import org.authentication.model.UserInfoDto;
import org.authentication.repository.UserRepository;
import org.authentication.util.ValidationUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.authentication.entities.UserRole;
import org.authentication.repository.RoleRepository;
import java.util.Set;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomerUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        ValidationUtils.validateUser(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
            return false;
        }
        String userId = UUID.randomUUID().toString();
        Set<UserRole> roles = new HashSet<>();

        if (userInfoDto.getRoles() != null && !userInfoDto.getRoles().isEmpty()) {
            for (String roleName : userInfoDto.getRoles()) {
                UserRole role = roleRepository.findByName(roleName.toUpperCase());
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        if (roles.isEmpty()) {
            UserRole defaultRole = roleRepository.findByName("USER");
            if (defaultRole != null) roles.add(defaultRole);
        }

        UserInfo newUser = new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), roles);
        userRepository.save(newUser);
        return true;
    }

    public UserInfo loadUserEntityByUsername(String username) {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public long countAllUsers() {
        return userRepository.count();
    }

}
