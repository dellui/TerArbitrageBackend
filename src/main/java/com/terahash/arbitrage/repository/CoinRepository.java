package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String>, JpaSpecificationExecutor<Coin> {
    List<Coin> findAllByEnabled(int enabled );
}