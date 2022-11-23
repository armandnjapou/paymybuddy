package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.repositories.AccountRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/accounts")
@AllArgsConstructor
@SecurityRequirement(name = "backendapi")
public class AccountController {

    final AccountRepository accountRepository;

    final PayMyBuddyUserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllAccounts(){
        return ResponseEntity.ok().body(accountRepository.findAll());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer accountId) {
        return ResponseEntity.ok().body(accountRepository.findById(accountId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAccountByUserId(@PathVariable Integer userId) {

        Optional<PayMyBuddyUser> user = userRepository.findById(Long.valueOf(userId));
        if(user.isPresent()){
            return ResponseEntity.ok().body(accountRepository.findByUser(user.get()));
        } else throw new UsernameNotFoundException("User not found with id: " + userId);
    }
}
