package dev.codescreen.service;

import dev.codescreen.dto.AuthorizationRequest;
import dev.codescreen.dto.LoadRequest;
import dev.codescreen.exceptions.IncorrectCurrencyException;
import dev.codescreen.exceptions.InsufficientBalanceException;
import dev.codescreen.model.*;
import dev.codescreen.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount getUserAccount(Integer userId) throws IllegalArgumentException, NullPointerException {
        UserAccount userAccount = userAccountRepository.getUserAccount(userId);
        if (userAccount == null) {
            throw new NullPointerException("User account not found for userId: " + userId);
        }
        return userAccount;
    }

    @Override
    public void saveUserAccount(UserAccount userAccount) {
        userAccountRepository.saveUserAccount(userAccount);
    }

    @Override
    public void authorizeTransaction(
        AuthorizationRequest request
    ) throws IllegalArgumentException, InsufficientBalanceException, NullPointerException, IncorrectCurrencyException {
        UserAccount userAccount = getUserAccount(request.getUserId());
        if (!userAccount.getCurrency().equals(request.getTransactionAmount().getCurrency())) {
            throw new IncorrectCurrencyException("User account currency does not match transaction currency");
        }
        if (! request.getTransactionAmount().getTransactionType().equals(TransactionType.DEBIT.toString())) {
            throw new IllegalArgumentException("Invalid transaction type");
        }
        userAccount.recalculateBalance();
        TransactionEvent event = new TransactionEvent(
            System.currentTimeMillis(),
            TransactionType.DEBIT,
            request.getTransactionAmount().getAmount(),
            request.getMessageId(),
            request.getTransactionAmount().getCurrency()
        );
        userAccount.addTransactionEvent(event);
    }

    @Override
    public void loadTransaction(
        LoadRequest request
    ) throws IllegalArgumentException, InsufficientBalanceException, NullPointerException, IncorrectCurrencyException {
        UserAccount userAccount = getUserAccount(request.getUserId());
        if (!userAccount.getCurrency().equals(request.getTransactionAmount().getCurrency())) {
            throw new IncorrectCurrencyException("User account currency does not match transaction currency");
        }
        if (! request.getTransactionAmount().getTransactionType().equals(TransactionType.CREDIT.toString())) {
            throw new IllegalArgumentException("Invalid transaction type");
        }
        userAccount.recalculateBalance();
        TransactionEvent event = new TransactionEvent(
            System.currentTimeMillis(),
            TransactionType.CREDIT,
            request.getTransactionAmount().getAmount(),
            request.getMessageId(),
            request.getTransactionAmount().getCurrency()
        );
        userAccount.addTransactionEvent(event);
    }
}