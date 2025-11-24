package ru.iosofatov.wallet.client.api.store.entityes;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.iosofatov.wallet.client.api.controllers.errors.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public void deposit(BigDecimal amount) {
        balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount)<0) {
            throw new InsufficientFundsException();
        }
        balance.subtract(amount);
    }

}

