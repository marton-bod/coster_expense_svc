package io.coster.expense_svc.services;

import io.coster.expense_svc.domain.Expense;
import io.coster.expense_svc.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private AuthenticationService authenticationService;

    public ExpenseService(ExpenseRepository expenseRepository, AuthenticationService authenticationService) {
        this.expenseRepository = expenseRepository;
        this.authenticationService = authenticationService;
    }

    public List<Expense> getAllExpensesByUserId(String userId) {
        return expenseRepository.findAllByUserId(userId);
    }

    public List<Expense> getAllExpensesByUserIdAndYearAndMonth(String userId, int year, int month) {
        return expenseRepository.findAllByUserIdAndYearAndMonth(userId, year, month);
    }

    public Expense saveExpense(Expense expense) {
        validateNewExpense(expense);
        return expenseRepository.save(expense);
    }

    private void validateNewExpense(Expense expense) {
        if (isBlank(expense.getLocation()) || isNull(expense.getAmount()) || isNull(expense.getDate())
                || isNull(expense.getCategory()) || isNull(expense.getUserId())) {
            throw new IllegalArgumentException("None of the fields can be null or empty!");
        }

        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Only positive amount values are allowed!");
        }
    }

    private boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
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
