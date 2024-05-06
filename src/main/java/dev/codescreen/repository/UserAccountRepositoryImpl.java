package dev.codescreen.repository;

import dev.codescreen.model.UserAccount;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {
    private final Map<Integer, UserAccount> userAccounts = new HashMap<>();

    @Override
    public UserAccount getUserAccount(Integer userId) {
        return userAccounts.get(userId);
    }

    @Override
    public void saveUserAccount(UserAccount userAccount) {
        userAccounts.put(userAccount.getUser().getUserId(), userAccount);
    }
}