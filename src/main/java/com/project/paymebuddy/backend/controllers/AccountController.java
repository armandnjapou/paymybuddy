package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.dtos.PayMyBuddyDto;
import com.project.paymebuddy.backend.entities.Account;
import com.project.paymebuddy.backend.entities.Operation;
import com.project.paymebuddy.backend.entities.OperationType;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.AccountRepository;
import com.project.paymebuddy.backend.repositories.OperationRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.paymebuddy.backend.utils.Constants.API_V1_PATH;

@RestController
@RequestMapping(API_V1_PATH + "/accounts")
@SecurityRequirement(name = "backendapi")
public record AccountController(AccountRepository accountRepository,
                                PayMyBuddyUserRepository userRepository,
                                OperationRepository operationRepository) {

    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccountByUserId(@PathVariable Integer userId) {

        PayMyBuddyUser user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found..."));
        Account account = accountRepository.findByUser(user).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Account not found."));
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Integer userId, @RequestBody PayMyBuddyDto.AccountOperation accountOperation) {
        return proceedAccountOperation(OperationType.DEPOSIT, userId, accountOperation);
    }

    @PostMapping("/{userId}/withdrawal")
    public ResponseEntity<Account> withdraw(@PathVariable Integer userId, @RequestBody PayMyBuddyDto.AccountOperation accountOperation) {
        return proceedAccountOperation(OperationType.WITHDRAWAL, userId, accountOperation);
    }

    public ResponseEntity<Account> proceedAccountOperation(OperationType operationType, Integer userId, PayMyBuddyDto.AccountOperation accountOperation) {
        PayMyBuddyUser user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found..."));
        Account userAccount = accountRepository.findByUser(user).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Account not found with userId: " + user.getId()));

        if(operationType.equals(OperationType.WITHDRAWAL)) {
            validateAccountForWithdraw(userAccount, BigDecimal.valueOf(accountOperation.amount()));
            userAccount.setBalance(userAccount.getBalance().subtract(BigDecimal.valueOf(accountOperation.amount())));
        } else {
            userAccount.setBalance(userAccount.getBalance().add(BigDecimal.valueOf(accountOperation.amount())));
        }

        Operation operation = new Operation();
        operation.setAccount(userAccount);
        operation.setAmount(BigDecimal.valueOf(accountOperation.amount()));
        operation.setOperationType(operationType);
        operation.setExecutionDate(LocalDateTime.now());
        operation.setDescription(accountOperation.description());
        operationRepository.save(operation);

        userAccount.getOperations().add(operation);
        Account updatedAccount = accountRepository.save(userAccount);

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    public void validateAccountForWithdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0)
            throw new DomainException(DomainException.Severity.FATAL, DomainException.Code.INTERNAL_SERVER_ERROR, "The current balance doesn't allow this operation");
    }
}
