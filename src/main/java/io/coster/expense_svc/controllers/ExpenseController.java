package io.coster.expense_svc.controllers;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.services.AuthenticationService;
import io.coster.expense_svc.services.ExpenseService;
import io.coster.expense_svc.utilities.ParserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private ExpenseService expenseService;
    private AuthenticationService authenticationService;

    public ExpenseController(ExpenseService expenseService, AuthenticationService authenticationService) {
        this.expenseService = expenseService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Expense>> listExpenses(@CookieValue(value = "auth_token") String token,
                                                      @CookieValue(value = "auth_id") String userId,
                                                      @RequestParam(value = "month", required = false) String month) {

        checkAuthCredentials(token, userId);
        if (month == null) {
            List<Expense> expenses = expenseService.getAllExpensesByUserId(userId);
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }

        try {
            ParserUtil.YearAndMonth result = ParserUtil.parseYearAndMonth(month);
            List<Expense> expenses = expenseService.getAllExpensesByUserIdAndYearAndMonth(userId, result.getYear(), result.getMonth());
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private void checkAuthCredentials(String token, String userId) {
        boolean isValid = authenticationService.isUserAndTokenValid(userId, token);
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials!");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Expense> createExpense(@CookieValue(value = "auth_token") String token,
                                                 @CookieValue(value = "auth_id") String userId,
                                                 @RequestBody Expense expense) {

        checkAuthCredentials(token, userId);
        try {
            Expense saved = expenseService.saveExpense(expense);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/delete")
    public ResponseEntity deleteExpense(@CookieValue(value = "auth_token") String token,
                                        @CookieValue(value = "auth_id") String userId,
                                        @RequestParam("id") Long expenseId) {

        checkAuthCredentials(token, userId);
        expenseService.deleteExpenseByUserIdAndExpenseId(userId, expenseId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<Expense> modifyExpense(@CookieValue(value = "auth_token") String token,
                                                 @CookieValue(value = "auth_id") String userId,
                                                 @RequestBody Expense expense) {

        checkAuthCredentials(token, userId);
        try {
            Expense modifiedExpense = expenseService.modifyExpense(expense);
            return new ResponseEntity<>(modifiedExpense, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
