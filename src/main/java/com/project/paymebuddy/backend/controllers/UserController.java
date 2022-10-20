package com.project.paymebuddy.backend.controllers;


import com.project.paymebuddy.backend.config.jwt.JwtUtils;
import com.project.paymebuddy.backend.dtos.UserDTO;
import com.project.paymebuddy.backend.entities.AppRole;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.entities.Role;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import com.project.paymebuddy.backend.repositories.RoleRepository;
import com.project.paymebuddy.backend.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.project.paymebuddy.backend.utils.Constants.AUTH_PATH;
import static com.project.paymebuddy.backend.utils.Constants.BEARER_TOKEN;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AUTH_PATH)
@AllArgsConstructor
public class UserController {

    final AuthenticationManager authenticationManager;

    final PayMyBuddyUserRepository userRepository;

    final RoleRepository roleRepository;

    final JwtUtils jwtUtils;

    final PasswordEncoder encoder;


    @PostMapping("/signin")
    public ResponseEntity<UserDTO.JwtResponse> authenticateUser(@RequestBody UserDTO.LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new UserDTO.JwtResponse(
                jwt,
                BEARER_TOKEN,
                userDetails.id(),
                userDetails.name(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO.MessageResponse> registerUser(@RequestBody UserDTO.SignupRequest signUpRequest) {
        Optional<PayMyBuddyUser> existUser = userRepository.findByUsername(signUpRequest.username());

        if (existUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserDTO.MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        PayMyBuddyUser user = new PayMyBuddyUser();
        user.setName(signUpRequest.name());
        user.setUsername(signUpRequest.username());
        user.setPassword(encoder.encode(signUpRequest.password()));

        Set<Role> roles = new HashSet<>();

        if(signUpRequest.appRole() != null) {
            roles.add(new Role(signUpRequest.appRole()));
        } else roles.add(new Role(AppRole.USER));

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new UserDTO.MessageResponse("User registered successfully!"));
    }
}
