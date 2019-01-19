package io.coster.expense_svc.repositories;

import io.coster.expense_svc.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserId(String userId);

    @Query("SELECT e from Expense e WHERE e.userId = ?1 AND YEAR(e.date) = ?2 AND MONTH(e.date) = ?3")
    List<Expense> findAllByUserIdAndYearAndMonth(String userId, int year, int month);
}
