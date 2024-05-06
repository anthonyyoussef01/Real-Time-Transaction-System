package dev.codescreen.service;

import dev.codescreen.dto.AuthorizationRequest;
import dev.codescreen.dto.LoadRequest;
import dev.codescreen.exceptions.IncorrectCurrencyException;
import dev.codescreen.exceptions.InsufficientBalanceException;
import dev.codescreen.model.UserAccount;

public interface UserAccountService {
    /**
     * This method returns the UserAccount object for the given userId.
     *
     * @param userId The Integer userId for which the UserAccount object is to be retrieved.
     *               This userId is unique for each user.
     * @return The UserAccount object for the given userId.
     */
    UserAccount getUserAccount(Integer userId) throws IllegalArgumentException, NullPointerException;

    /**
     * This method saves the given UserAccount object.
     *
     * @param userAccount The UserAccount object to be saved.
     */
    void saveUserAccount(UserAccount userAccount);

    /**
     * This method authorizes a transaction for a user account.
     *
     * @param request AuthorizationRequest object containing the user ID, message ID, and amount.
     * @throws IllegalArgumentException If the request is invalid.
     * @throws InsufficientBalanceException If the user account has insufficient balance.
     * @throws NullPointerException If the user account is not found.
     */
    void authorizeTransaction(
        AuthorizationRequest request
    ) throws IllegalArgumentException, InsufficientBalanceException, NullPointerException, IncorrectCurrencyException;

    /**
     * This method loads a transaction for a user account.
     *
     * @param request LoadRequest object containing the user ID, message ID, and amount.
     * @throws IllegalArgumentException If the request is invalid.
     * @throws InsufficientBalanceException If the user account has insufficient balance.
     * @throws NullPointerException If the user account is not found.
     */
    void loadTransaction(
        LoadRequest request
    ) throws IllegalArgumentException, InsufficientBalanceException, NullPointerException, IncorrectCurrencyException;
}