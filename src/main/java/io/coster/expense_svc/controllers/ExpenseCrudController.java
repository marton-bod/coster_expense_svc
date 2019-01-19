package io.coster.expense_svc.controllers;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.services.ExpenseService;
import io.coster.expense_svc.utilities.ParserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseCrudController {

    private ExpenseService expenseService;

    public ExpenseCrudController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Expense>> listExpenses(@RequestParam(value = "month", required = false) String month) {
        if (month == null) {
            List<Expense> expenses = expenseService.getAllExpensesByUserId("test@test.co.uk");
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }

        ParserUtil.YearMonthParseResult result = ParserUtil.parseYearAndMonth(month);
        if (result.isValid()) {
            List<Expense> expenses = expenseService.getAllExpensesByUserIdAndYearAndMonth("test@test.co.uk", result.getYear(), result.getMonth());
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(List.of(new Expense()), HttpStatus.BAD_REQUEST);
        }
    }


}
