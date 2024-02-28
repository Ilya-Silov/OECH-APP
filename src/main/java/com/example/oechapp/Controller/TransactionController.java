package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.ReplenishBalanceRequest;
import com.example.oechapp.Entity.Transaction;
import com.example.oechapp.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Transaction>> doTransaction(@RequestParam Long userId) {

        return new ResponseEntity<>(transactionService.getUsersTransactions(userId), HttpStatus.CREATED);
    }

    @PostMapping("replanishes")
    public ResponseEntity<?> replanish(@RequestBody ReplenishBalanceRequest replenishBalanceRequest)
    {
        transactionService.replanishBalance(replenishBalanceRequest.getAmount(), replenishBalanceRequest.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Другие методы для работы с транзакциями
}