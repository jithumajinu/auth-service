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
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractCoreUtilController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
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
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        List<String> roleNames = currentUser.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .toList();

        responseBuilder.data(
                JwtAuthenticationResponse.builder()
                        .accessToken(jwt)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer")
                        .name(currentUser.getName())
                        .email(currentUser.getEmail())
                        .claims(roleNames)
                        .build());

        return responseBuilder.build();
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtAuthenticationResponse> refreshToken(@RequestBody String refreshToken) {
        var responseBuilder = ApiResponse.<JwtAuthenticationResponse>builder();
        
        if (!tokenProvider.validateToken(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
            return responseBuilder.flag(false)
                    .error(ApiResponse.ApiError.builder()
                            .errorCode(ApiErrorCode.UNAUTHORIZED)
                            .message("Invalid refresh token")
                            .build())
                    .build();
        }
        
        Long userId = tokenProvider.getUserIdFromJWT(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));
        
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
        
        String newAccessToken = tokenProvider.generateToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);
        
        List<String> roleNames = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .toList();
        
        return responseBuilder.flag(true)
                .data(JwtAuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .tokenType("Bearer")
                        .name(userPrincipal.getName())
                        .email(userPrincipal.getEmail())
                        .claims(roleNames)
                        .build())
                .build();
    }

}
