package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.dtos.PayMyBuddyDto;
import com.project.paymebuddy.backend.entities.Account;
import com.project.paymebuddy.backend.entities.OperationType;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import com.project.paymebuddy.backend.services.AccountServiceImpl;
import com.project.paymebuddy.backend.services.ConnectionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/accounts")
@SecurityRequirement(name = "backendapi")
public record AccountController(PayMyBuddyUserRepository userRepository,
                                ConnectionServiceImpl connectionService,
                                AccountServiceImpl accountService) {

    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccountByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(accountService.getAccountById(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Integer userId, @RequestBody PayMyBuddyDto.AccountOperation accountOperation) {
        PayMyBuddyUser user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found..."));
        Account userAccount = accountService.getAccountByUser(user);
        Account updatedAccount = accountService.proceedAccountOperation(OperationType.DEPOSIT, userAccount, accountOperation);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/{userId}/withdrawal")
    public ResponseEntity<Account> withdraw(@PathVariable Integer userId, @RequestBody PayMyBuddyDto.AccountOperation accountOperation) {
        PayMyBuddyUser user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found..."));
        Account userAccount = accountService.getAccountByUser(user);
        Account updatedAccount = accountService.proceedAccountOperation(OperationType.WITHDRAWAL, userAccount, accountOperation);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/{userId}/transfer")
    public ResponseEntity<Pair<Account, Account>> transfer(@PathVariable Integer userId, @RequestBody PayMyBuddyDto.AccountOperation accountOperation) {
        PayMyBuddyUser sender = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Sender not found..."));
        PayMyBuddyUser target = userRepository.findByUsername(accountOperation.email()).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Target not found..."));
        return new ResponseEntity<>(accountService.proceedTransfer(sender, target, accountOperation), HttpStatus.OK);
    }
}
