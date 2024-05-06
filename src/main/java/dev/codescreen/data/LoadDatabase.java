package dev.codescreen.data;

import dev.codescreen.model.User;
import dev.codescreen.model.UserAccount;
import dev.codescreen.model.UserDetails;
import dev.codescreen.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * This class represents the LoadDatabase object.
 * The LoadDatabase object is a Configuration class that initializes the database with predefined UserAccount objects.
 * The purpose of this class is to populate the database with UserAccount objects when the application starts.
 * It also prints our the users that were saved to the database for easy reference.
 */
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserAccountService service) {
        return args -> {
            // Create and save predefined users
            for (int i = 1; i <= 10; i++) {
                UserDetails userDetails = new UserDetails(
                    "User" + i, "LastName", "user" + i + "@example.com", "1234567890"
                );
                User user = new User(userDetails);
                UserAccount userAccount = new UserAccount(user, "USD");
                service.saveUserAccount(userAccount);
                System.out.println("Saved UserAccount with userId: " + userAccount.getUser().getUserId());
            }
        };
    }
}