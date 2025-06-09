package com.openapi.cloud.core.security;

import com.openapi.cloud.core.exception.ResourceNotFoundException;
import com.openapi.cloud.core.model.entities.User;
import com.openapi.cloud.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        UserRepository userRepository;

        @Override
        @Transactional
        @Cacheable(value = "userCache", key = "#usernameOrEmail")
        public UserDetails loadUserByUsername(String usernameOrEmail)
                        throws UsernameNotFoundException {
                User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with username or email : " + usernameOrEmail));

                return UserPrincipal.create(user);
        }

        @Transactional
        @Cacheable(value = "userCache", key = "#id")
        public UserDetails loadUserById(Long id) {
                User user = userRepository.findById(id).orElseThrow(
                                () -> new ResourceNotFoundException("User", "id", id));

                return UserPrincipal.create(user);
        }
}
