package dev.codescreen.repository;

import dev.codescreen.dto.AuthorizationRequest;
import dev.codescreen.exceptions.InsufficientBalanceException;
import dev.codescreen.model.UserAccount;

public interface UserAccountRepository {
    /**
     * This method returns the UserAccount object for the given userId.
     *
     * @param userId The Integer userId for which the UserAccount object is to be retrieved.
     * @return The UserAccount object for the given userId.
     */
    UserAccount getUserAccount(Integer userId);

    /**
     * This method saves the given UserAccount object.
     *
     * @param userAccount The UserAccount object to be saved.
     */
    void saveUserAccount(UserAccount userAccount);
}