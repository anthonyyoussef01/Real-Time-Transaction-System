package dev.codescreen.controller;

import dev.codescreen.dto.*;
import dev.codescreen.exceptions.IncorrectCurrencyException;
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
     * @return ResponseEntity object containing the AuthorizationResponse object or ErrorResponse object.
     */
    @PutMapping("/authorization")
    public ResponseEntity<?> authorizeTransaction(@RequestBody AuthorizationRequest request) {
        try {
            userAccountService.authorizeTransaction(request);
            ResponseBalance responseBalance = new ResponseBalance(
                userAccountService.getUserAccount(request.getUserId()).recalculateBalance(),
                request.getTransactionAmount().getCurrency(),
                TransactionType.DEBIT
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthorizationResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    "APPROVED",
                    responseBalance
                )
            );
        } catch (IllegalArgumentException | InsufficientBalanceException | IncorrectCurrencyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    request.getMessageId(),
                    "DECLINED"
                )
            );
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                    request.getMessageId(),
                    "DECLINED"
                )
            );
        }
    }

    /**
     * Load a transaction for a user account.
     *
     * @param request LoadRequest object containing the user ID, message ID, and amount.
     * @return ResponseEntity object containing the LoadResponse object or ErrorResponse object.
     */
    @PutMapping("/load")
    public ResponseEntity<?> loadTransaction(@RequestBody LoadRequest request) {
        try {
            userAccountService.loadTransaction(request);
            ResponseBalance responseBalance = new ResponseBalance(
                userAccountService.getUserAccount(request.getUserId()).recalculateBalance(),
                request.getTransactionAmount().getCurrency(),
                TransactionType.CREDIT
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new LoadResponse(
                    request.getUserId(),
                    request.getMessageId(),
                    responseBalance
                )
            );
        } catch (IllegalArgumentException | InsufficientBalanceException | IncorrectCurrencyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    request.getMessageId(),
                    "DECLINED"
                )
            );
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                    request.getMessageId(),
                    "DECLINED"
                )
            );
        }
    }
}
