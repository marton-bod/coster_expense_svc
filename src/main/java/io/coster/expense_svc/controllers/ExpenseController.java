package io.coster.expense_svc.controllers;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.services.AuthenticationService;
import io.coster.expense_svc.services.ExpenseService;
import io.coster.expense_svc.utilities.ParserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<List<Expense>> listExpenses(@RequestHeader(value = "auth_token") String token,
                                                      @RequestHeader(value = "auth_id") String userId,
                                                      @RequestParam(value = "month", required = false) String month) {
        checkAuthCredentials(token, userId);
        List<Expense> expenses;
        if (month == null) {
            expenses = expenseService.getAllExpensesByUserId(userId);
        } else {
            ParserUtil.YearAndMonth result = ParserUtil.parseYearAndMonth(month);
            expenses = expenseService.getAllExpensesByUserIdAndYearAndMonth(userId, result.getYear(), result.getMonth());
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    private void checkAuthCredentials(String token, String userId) {
        boolean isValid = authenticationService.isUserAndTokenValid(userId, token);
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials!");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Expense> createExpense(@RequestHeader(value = "auth_token") String token,
                                                 @RequestHeader(value = "auth_id") String userId,
                                                 @RequestBody Expense expense) {

        checkAuthCredentials(token, userId);
        Expense saved = expenseService.saveExpense(expense);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/delete")
    public ResponseEntity deleteExpense(@RequestHeader(value = "auth_token") String token,
                                        @RequestHeader(value = "auth_id") String userId,
                                        @RequestParam("id") Long expenseId) {

        checkAuthCredentials(token, userId);
        expenseService.deleteExpenseByUserIdAndExpenseId(userId, expenseId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<Expense> modifyExpense(@RequestHeader(value = "auth_token") String token,
                                                 @RequestHeader(value = "auth_id") String userId,
                                                 @RequestBody Expense expense) {

        checkAuthCredentials(token, userId);
        Expense modifiedExpense = expenseService.modifyExpense(expense);
        return new ResponseEntity<>(modifiedExpense, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpectedException(Exception e) {
        return e.getMessage();
    }
}
