package ru.iosofatov.wallet.client.api.service;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void processOperation(WalletDto request) {

        switch (request.operationType()) {

            case DEPOSIT -> deposit(request.walletId(), request.amount());

            case WITHDRAW -> withdraw(request.walletId(), request.amount());
        }
    }

    public BigDecimal getBalance(UUID walletId){
        return walletRepository.getReferenceById(walletId).getBalance();
    }

    private void deposit(UUID walletId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    private void withdraw(UUID walletId, BigDecimal amount) {
        boolean success = false;
            while (!success) {
                try {
                    WalletEntity wallet = walletRepository.findById(walletId)
                            .orElseThrow(() -> new WalletNotFoundException(walletId));
                    if (wallet.getBalance().compareTo(amount) < 0) {
                        throw new InsufficientFundsException(String.valueOf(walletId));
                    }
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                    walletRepository.saveAndFlush(wallet);
                    success = true;
                } catch (OptimisticLockException e) {
                    throw new RuntimeException(e);
                }
            }
    }

}

