package ru.iosofatov.wallet.client.api.controllers.errors;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(UUID id) {
        super("Wallet with id" + id + "not found");
    }
}
