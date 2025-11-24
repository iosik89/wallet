package ru.iosofatov.wallet.client.api.controllers;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iosofatov.wallet.client.api.dto.WalletDto;
import ru.iosofatov.wallet.client.api.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class WalletController {

    WalletService walletService;

    @GetMapping("api/v1/wallets/{WALLET_UUID}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("WALLET_UUID") UUID wallet){
        BigDecimal balance = walletService.getBalance(wallet);
        return ResponseEntity.ok(balance); //.plus()
    }

    @PostMapping("api/v1/wallet")
    public ResponseEntity<String> processTransaction (@Valid @RequestBody WalletDto dto){
        walletService.processOperation(dto);
        return ResponseEntity.ok("Transaction Success!");
    }
}
