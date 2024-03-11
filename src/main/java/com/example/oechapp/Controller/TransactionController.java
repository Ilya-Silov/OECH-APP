package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.ReplenishBalanceRequest;
import com.example.oechapp.Entity.Transaction;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Security.UserDetailsImpl;
import com.example.oechapp.Service.TransactionService;
import com.example.oechapp.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Контроллер для работы с транзакциями")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Operation(summary = "Получить транзакции пользователя", description = "Получает список транзакций пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакции пользователя найдены"),
            @ApiResponse(responseCode = "404", description = "Транзакции пользователя не найдены")
    })
    @GetMapping("user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Transaction>> doTransaction(@Parameter(description = "Идентификатор пользователя", required = true) @RequestParam Long userId, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();

        if (!user.getId().equals(userId))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(transactionService.getUsersTransactions(userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Пополнить баланс", description = "Пополняет баланс пользователя.")
    @PostMapping("replanishes")
    public ResponseEntity<?> replanish(@Parameter(description = "Запрос на пополнение баланса", required = true) @RequestBody ReplenishBalanceRequest replenishBalanceRequest)
    {
        transactionService.replanishBalance(replenishBalanceRequest.getAmount(), replenishBalanceRequest.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Другие методы для работы с транзакциями
}