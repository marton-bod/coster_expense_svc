package io.coster.expense_svc.services;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpenseByUserIdAndExpenseId(String userId, Long expenseId) {
        expenseRepository.deleteByUserIdAndId(userId, expenseId);
    }

    public Expense modifyExpense(Expense expense) {
        Optional<Expense> byId = expenseRepository.findById(expense.getId());
        Expense old = byId.orElseThrow(() -> new IllegalArgumentException("Expense does not exist!"));
        updateExpense(old, expense);
        expenseRepository.saveAndFlush(old);
        return old;
    }

    private void updateExpense(Expense oldExpense, Expense newExpense) {
        oldExpense.setAmount(newExpense.getAmount());
        oldExpense.setLocation(newExpense.getLocation());
        oldExpense.setDate(newExpense.getDate());
        oldExpense.setCategory(newExpense.getCategory());
        oldExpense.setUserId(newExpense.getUserId());
    }

}
