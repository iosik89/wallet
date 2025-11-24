package ru.iosofatov.wallet.client.api.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iosofatov.wallet.client.api.store.entityes.WalletEntity;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity,UUID> {

}
