package com.example.oechapp.Service;

import com.example.oechapp.Entity.Transaction;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Repository.TransactionRepository;
import com.example.oechapp.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> getUsersTransactions(Long userId)
    {
        return transactionRepository.getUsersTransactions(userId);
    }
    @Transactional
    public void replanishBalance(double amount, Long userId)
    {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
        {
            throw new IllegalArgumentException("Cant find user with user id " + userId);
        }
        user.get().setBalance(user.get().getBalance()+amount);
        userRepository.save(user.get());
        Transaction transaction = new Transaction();
        transaction.setToUser(user.get());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }
    @Transactional
    public void debitBalance(double amount, Long userId)
    {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
        {
            throw new IllegalArgumentException("Cant find user with user id " + userId);
        }
        user.get().setBalance(user.get().getBalance()-amount);
        userRepository.save(user.get());
        Transaction transaction = new Transaction();
        transaction.setFromUser(user.get());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction doTransaction(Transaction transaction)
    {
        Optional<User> fromUser = userRepository.findById(transaction.getFromUser().getId());
        Optional<User> toUser = userRepository.findById(transaction.getToUser().getId());
        if (fromUser.isEmpty() || toUser.isEmpty())
        {
            throw new IllegalArgumentException("Cant find user(s) participate in a transaction");
        }
        fromUser.get().setBalance(fromUser.get().getBalance()- transaction.getAmount());
        toUser.get().setBalance(toUser.get().getBalance() + transaction.getAmount());
        userRepository.save(fromUser.get());
        userRepository.save(toUser.get());
        transactionRepository.save(transaction);
        return transaction;
    }
}
