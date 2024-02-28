package com.example.oechapp.Repository;

import com.example.oechapp.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.fromUser.id = :userId or t.toUser.id = :userId")
    public List<Transaction> getUsersTransactions(Long userId);
}
