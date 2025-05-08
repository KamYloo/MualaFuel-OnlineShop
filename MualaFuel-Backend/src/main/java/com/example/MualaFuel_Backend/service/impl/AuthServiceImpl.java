package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.RoleDao;
import com.example.MualaFuel_Backend.dao.UserDao;
import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.dto.request.LoginRequest;
import com.example.MualaFuel_Backend.dto.request.RegisterRequest;
import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.AuthService;
import com.example.MualaFuel_Backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Mapper<User, UserDto> mapper;
    private final JwtService jwtService;

    @Override
    public UserDto createUser(RegisterRequest registerRequest) {
        userDao.findByEmail(registerRequest.getEmail())
                .ifPresent(email -> {
                    throw new CustomException(BusinessErrorCodes.EMAIL_IS_USED);
                });

        Role role = roleDao.findByName("USER").orElseThrow(
                () -> new RuntimeException("Role not found"));

        User user = User.builder()
                .firstName(registerRequest.getFirstname())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Set.of(role))
                .build();
        User savedUser = userDao.save(user);
        return mapper.mapTo(savedUser);
    }

    @Override
    public String verify(LoginRequest loginRequest){
        User user = userDao.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.BAD_CREDENTIALS));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String email = loginRequest.getEmail();
            return jwtService.generateToken(email);
        }
        throw new CustomException(BusinessErrorCodes.BAD_CREDENTIALS);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return mapper.mapTo(userDao.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User not found")));
    }

    @Override
    public UserDto verifyToken(String token) {
        if(!jwtService.validateJwtToken(token)){
            throw new CustomException(BusinessErrorCodes.INVALID_TOKEN);
        }
        String userName = jwtService.extractUserName(token);

        User user = userDao.findByEmail(userName).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return mapper.mapTo(user);
    }
}
