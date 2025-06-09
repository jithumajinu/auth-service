package com.openapi.cloud.core.controller;

import com.acid.core.web.constants.ApiErrorCode;
import com.acid.core.web.model.ApiResponse;
import com.acid.core.web.model.JwtAuthenticationResponse;

import com.openapi.cloud.core.exception.AppException;
import com.openapi.cloud.core.model.dto.request.LoginRequest;
import com.openapi.cloud.core.model.dto.request.SignUpRequest;
import com.openapi.cloud.core.model.entities.Role;
import com.openapi.cloud.core.model.entities.RoleName;
import com.openapi.cloud.core.model.entities.User;
import com.openapi.cloud.core.repository.RoleRepository;
import com.openapi.cloud.core.repository.UserRepository;
import com.openapi.cloud.core.security.JwtTokenProvider;
import com.openapi.cloud.core.security.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractCoreUtilController {

        @Autowired
        AuthenticationManager authenticationManager;

        UserRepository userRepository;

        @Autowired
        RoleRepository roleRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        JwtTokenProvider tokenProvider;

        @PostMapping("/signup")
        public String registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

                User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                                signUpRequest.getEmail(), signUpRequest.getPassword());
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new AppException("User Role not set."));
                user.setRoles(Collections.singleton(userRole));
                return "success";
        }

        @PostMapping("/login")
        public ApiResponse<JwtAuthenticationResponse> loginUser(
                        @Validated @RequestBody LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpServletResponse httpServletResponse) {

                log.info("API: /login");
                log.info("Request: {}", loginRequest);
                var responseBuilder = ApiResponse.<JwtAuthenticationResponse>builder()
                                .flag(true);

                if (bindingResult.hasErrors()) {
                        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                        return responseBuilder
                                        .error(ApiResponse.ApiError.builder()
                                                        .errorCode(ApiErrorCode.INPUT_ERROR)
                                                        .errors(formatInputErrors(bindingResult))
                                                        .build())
                                        .build();
                }

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsernameOrEmail(),
                                                loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();
                String jwt = tokenProvider.generateToken(authentication);

                List<String> roleNames = currentUser.getAuthorities().stream()
                                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                                .toList();

                responseBuilder.data(
                                JwtAuthenticationResponse.builder()
                                                .accessToken(jwt)
                                                .tokenType("Bearer")
                                                .name(currentUser.getName())
                                                .email(currentUser.getEmail())
                                                .claims(roleNames)
                                                .build());

                return responseBuilder.build();
        }

}
