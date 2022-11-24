package com.project.paymebuddy.backend.controllers;


import com.project.paymebuddy.backend.config.jwt.JwtUtils;
import com.project.paymebuddy.backend.dtos.PayMyBuddyDto;
import com.project.paymebuddy.backend.entities.Account;
import com.project.paymebuddy.backend.entities.AppRole;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.entities.Role;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.AccountRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import com.project.paymebuddy.backend.repositories.RoleRepository;
import com.project.paymebuddy.backend.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.project.paymebuddy.backend.utils.Constants.AUTH_PATH;
import static com.project.paymebuddy.backend.utils.Constants.BEARER_TOKEN;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AUTH_PATH)
@AllArgsConstructor
public class AuthController {

    final AuthenticationManager authenticationManager;

    final PayMyBuddyUserRepository userRepository;

    final AccountRepository accountRepository;

    final RoleRepository roleRepository;

    final JwtUtils jwtUtils;

    final PasswordEncoder encoder;


    @PostMapping("/signin")
    public ResponseEntity<PayMyBuddyDto.JwtResponse> authenticateUser(@RequestBody PayMyBuddyDto.LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        PayMyBuddyUser user = userRepository.findById(userDetails.id()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(new PayMyBuddyDto.JwtResponse(jwt, BEARER_TOKEN, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<PayMyBuddyUser> registerUser(@RequestBody PayMyBuddyDto.SignupRequest signUpRequest) {
        Optional<PayMyBuddyUser> existUser = userRepository.findByUsername(signUpRequest.username());

        if (existUser.isPresent()) {
            throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Resource not found!");
        }

        PayMyBuddyUser user = new PayMyBuddyUser();
        user.setName(signUpRequest.name());
        user.setUsername(signUpRequest.username());
        user.setPassword(encoder.encode(signUpRequest.password()));

        Set<Role> roles = new HashSet<>();

        if(signUpRequest.appRole() != null) {
            roles.add(new Role(signUpRequest.appRole()));
        } else roles.add(new Role(AppRole.USER));

        user.setRoles(roles);
        PayMyBuddyUser savedUser = userRepository.save(user);
        Account account = new Account(new BigDecimal(BigInteger.ZERO), LocalDateTime.now());
        account.setUser(savedUser);
        accountRepository.save(account);

        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
