package dev.codescreen.controller;

import dev.codescreen.dto.AuthorizationRequest;
import dev.codescreen.dto.AuthorizationResponse;
import dev.codescreen.dto.LoadRequest;
import dev.codescreen.dto.LoadResponse;
import dev.codescreen.exceptions.InsufficientBalanceException;
import dev.codescreen.model.*;
import dev.codescreen.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {

    private final UserAccountService userAccountService;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);


    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * Ping the server to check if it is running.
     *
     * @return ResponseEntity object containing a String message "Pong! Current server time is:" & current server time
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Pong! Current server time is: " + System.currentTimeMillis());
    }

    /**
     * Authorize a transaction for a user account.
     *
     * @param request AuthorizationRequest object containing the user ID, message ID, and amount.
     * @return ResponseEntity object containing the AuthorizationResponse object.
     */
    @PutMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> authorizeTransaction(@RequestBody AuthorizationRequest request) {
        try {
            userAccountService.authorizeTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthorizationResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    "APPROVED",
                    userAccountService.getUserAccount(request.getUserId()).recalculateBalance()
                )
            );
        } catch (IllegalArgumentException | InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new AuthorizationResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    "DECLINED",
                    userAccountService.getUserAccount(request.getUserId()).recalculateBalance()
                )
            );
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new AuthorizationResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    "NOT_FOUND",
                    0.0
                )
            );
        }
    }

    /**
     * Load a transaction for a user account.
     *
     * @param request LoadRequest object containing the user ID, message ID, and amount.
     * @return ResponseEntity object containing the LoadResponse object.
     */
    @PutMapping("/load")
    public ResponseEntity<LoadResponse> loadTransaction(@RequestBody LoadRequest request) {
        try {
            userAccountService.loadTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new LoadResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    userAccountService.getUserAccount(request.getUserId()).recalculateBalance()
                )
            );
        } catch (IllegalArgumentException | InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new LoadResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    userAccountService.getUserAccount(request.getUserId()).recalculateBalance()
                )
            );
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new LoadResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    0.0
                )
            );
        }
    }
}
