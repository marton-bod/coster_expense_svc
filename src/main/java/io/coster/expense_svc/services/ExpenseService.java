package io.coster.expense_svc.services;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpensesByUserId(String userId) {
        return expenseRepository.findAllByUserId(userId);
    }

    public List<Expense> getAllExpensesByUserIdAndYearAndMonth(String userId, int year, int month) {
        return expenseRepository.findAllByUserIdAndYearAndMonth(userId, year, month);
    }

    public void saveExpense(Expense expense) {
        expenseRepository.save(expense);
    }
    // DELETE

    // UDPATE

}
