package ru.iosofatov.wallet.client.api.controllers.errors;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException() {
        super("Insufficient funds in the wallet");
    }

    public InsufficientFundsException(String message) {
        super("message");
    }
}
