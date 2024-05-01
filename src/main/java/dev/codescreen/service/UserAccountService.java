package dev.codescreen.service;

import dev.codescreen.model.UserAccount;

public interface UserAccountService {
    /**
     * This method returns the UserAccount object for the given userId.
     *
     * @param userId The Integer userId for which the UserAccount object is to be retrieved.
     *               This userId is unique for each user.
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