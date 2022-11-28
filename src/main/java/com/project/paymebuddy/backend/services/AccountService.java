package com.project.paymebuddy.backend.services;

import com.project.paymebuddy.backend.dtos.PayMyBuddyDto;
import com.project.paymebuddy.backend.entities.Account;
import com.project.paymebuddy.backend.entities.Operation;
import com.project.paymebuddy.backend.entities.OperationType;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<Account> getAccounts();

    Account getAccountById(Long id);

    Account getAccountByUser(PayMyBuddyUser user);

    Account proceedAccountOperation(OperationType operationType, Account userAccount, PayMyBuddyDto.AccountOperation accountOperation);

    Pair<Account, Account> proceedTransfer(PayMyBuddyUser sender, PayMyBuddyUser target, PayMyBuddyDto.AccountOperation accountOperation);

    void validateAccountForWithdraw(Account account, BigDecimal amount);

    Operation buildAndSaveOperation(Account account, OperationType operationType, PayMyBuddyDto.AccountOperation accountOperation);

    void updateBalance(Account userAccount, OperationType operationType, BigDecimal amount);
}
