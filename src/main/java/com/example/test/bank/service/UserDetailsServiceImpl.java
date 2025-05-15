package com.example.test.bank.service;

import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;
import com.example.test.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private static final String USERNAME_EMPTY_MESSAGE = "Username cannot be empty";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found: %s";
    private static final String USER_NOT_ACTIVE_MESSAGE = "User is not active: %s";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException(USERNAME_EMPTY_MESSAGE);
        }

        log.info("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User  not found with username: {}", username);
                    return new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
                });

        if (!user.isActive()) {
            throw new UsernameNotFoundException("User is not active: " + username);
        }

        log.info("User  found: {}", username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::name)
                        .toArray(String[]::new))
                .disabled(!user.getActive())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}
