package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.ArbiWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<ArbiWallet, String>, JpaSpecificationExecutor<ArbiWallet> {
}