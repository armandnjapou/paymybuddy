package com.project.paymebuddy.backend.services;

import com.project.paymebuddy.backend.dtos.PayMyBuddyDto;
import com.project.paymebuddy.backend.entities.*;
import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.repositories.AccountRepository;
import com.project.paymebuddy.backend.repositories.OperationRepository;
import com.project.paymebuddy.backend.repositories.PayMyBuddyUserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public record AccountServiceImpl(AccountRepository accountRepository,
                                 PayMyBuddyUserRepository userRepository,
                                 OperationRepository operationRepository,
                                 ConnectionServiceImpl connectionService) implements AccountService {

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        PayMyBuddyUser user = userRepository.findById(id).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "User not found..."));
        return accountRepository.findByUser(user).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Account not found."));
    }

    @Override
    public Account getAccountByUser(PayMyBuddyUser user) {
        return accountRepository.findByUser(user).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Account not found with userId: " + user.getId()));
    }

    @Override
    public Account proceedAccountOperation(OperationType operationType, Account userAccount, PayMyBuddyDto.AccountOperation accountOperation) {
        updateBalance(userAccount, operationType, BigDecimal.valueOf(accountOperation.amount()));

        userAccount.getOperations().add(buildAndSaveOperation(userAccount, operationType, accountOperation));

        return accountRepository.save(userAccount);
    }

    @Override
    public Pair<Account, Account> proceedTransfer(PayMyBuddyUser sender, PayMyBuddyUser target, PayMyBuddyDto.AccountOperation accountOperation) {

        Set<Connection> senderConnections = connectionService.getConnections(sender);

        if(ConnectionServiceImpl.isUserAlreadyConnected(senderConnections, target)) {
            Account senderAccount = accountRepository.findByUser(sender).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Sender account not found"));
            Account targetAccount = accountRepository.findByUser(target).orElseThrow(() -> new DomainException(DomainException.Severity.LOGIC, DomainException.Code.NOT_FOUND, "Target account not found"));

            updateBalance(targetAccount, OperationType.DEPOSIT, BigDecimal.valueOf(accountOperation.amount()));
            updateBalance(senderAccount, OperationType.WITHDRAWAL, BigDecimal.valueOf(accountOperation.amount()));

            senderAccount.getOperations().add(buildAndSaveOperation(senderAccount, OperationType.TRANSFER, accountOperation));
            targetAccount.getOperations().add(buildAndSaveOperation(targetAccount, OperationType.DEPOSIT, accountOperation));

            return Pair.of(senderAccount, targetAccount);
        } else throw new DomainException(DomainException.Severity.LOGIC, DomainException.Code.BAD_REQUEST, "Sender not connected to target");
    }

    @Override
    public void validateAccountForWithdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0)
            throw new DomainException(DomainException.Severity.FATAL, DomainException.Code.INTERNAL_SERVER_ERROR, "The current balance doesn't allow this operation");
    }

    @Override
    public Operation buildAndSaveOperation(Account account, OperationType operationType, PayMyBuddyDto.AccountOperation accountOperation) {
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(BigDecimal.valueOf(accountOperation.amount()));
        operation.setOperationType(operationType);
        operation.setExecutionDate(LocalDateTime.now());
        operation.setDescription(accountOperation.description());
        operationRepository.save(operation);

        return operation;
    }

    @Override
    public void updateBalance(Account userAccount, OperationType operationType, BigDecimal amount) {
        if (operationType.equals(OperationType.WITHDRAWAL)) {
            validateAccountForWithdraw(userAccount, amount);
            userAccount.setBalance(userAccount.getBalance().subtract(amount));
        } else if (operationType.equals(OperationType.DEPOSIT)) {
            userAccount.setBalance(userAccount.getBalance().add(amount));
        }
    }
}
