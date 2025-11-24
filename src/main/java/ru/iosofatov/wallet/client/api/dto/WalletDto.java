package ru.iosofatov.wallet.client.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;


public record WalletDto(

        @NotNull(message = "walletId is required")
        UUID walletId,

        @NotNull(message = "operationType is required")
        OperationType operationType,

        @Positive(message = "amount must be > 0")
        BigDecimal amount


) {
        public enum OperationType {
                DEPOSIT,
                WITHDRAW
        }
}


