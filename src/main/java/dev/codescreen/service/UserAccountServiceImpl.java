package dev.codescreen.service;

import dev.codescreen.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private Map<Integer, UserAccount> userAccounts = new HashMap<>();

    @Override
    public UserAccount getUserAccount(Integer userId) {
        return userAccounts.get(userId);
    }

    @Override
    public void saveUserAccount(UserAccount userAccount) {
        userAccounts.put(userAccount.getUser().getUserId(), userAccount);
    }
}