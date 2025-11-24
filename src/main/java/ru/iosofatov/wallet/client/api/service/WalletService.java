package ru.iosofatov.wallet.client.api.service;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iosofatov.wallet.client.api.controllers.errors.InsufficientFundsException;
import ru.iosofatov.wallet.client.api.controllers.errors.WalletNotFoundException;
import ru.iosofatov.wallet.client.api.dto.WalletDto;
import ru.iosofatov.wallet.client.api.store.entityes.WalletEntity;
import ru.iosofatov.wallet.client.api.store.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    /**
     * Главный метод — обрабатывает любую операцию.
     * Автоматически перезапускается при OptimisticLockException до 5 раз.
     */
    @Retryable(
            retryFor = OptimisticLockException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 10) // 10ms задержка между попытками
    )
    @Transactional
    public void processOperation(WalletDto request) {

        WalletEntity wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new WalletNotFoundException(request.walletId()));

        switch (request.operationType()) {

            case DEPOSIT -> deposit(wallet, request.amount());

            case WITHDRAW -> withdraw(wallet, request.amount());
        }
    }

    private void deposit(WalletEntity wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
    }

    private void withdraw(WalletEntity wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId))
                .getBalance();
    }
}
