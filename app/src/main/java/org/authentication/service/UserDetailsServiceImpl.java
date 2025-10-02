package org.authentication.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.authentication.entities.UserInfo;
import org.authentication.model.UserInfoDto;
import org.authentication.repository.UserRepository;
import org.authentication.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Could not find user!");
        }
        return new CustomerUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfo) {
        return userRepository.findByUsername(userInfo.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        ValidationUtils.validateUser(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
        return true;
    }

}
