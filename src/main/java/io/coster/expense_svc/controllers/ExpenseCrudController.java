package io.coster.expense_svc.controllers;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.domain.ExpenseCategory;
import io.coster.expense_svc.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseCrudController {

    private ExpenseService expenseService;

    public ExpenseCrudController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/list")
    public List<Expense> listExpenses(@RequestParam(value = "month", required = false) String month) {
        return expenseService.getAllExpensesByUserId("test@test.co.uk");
    }


}
