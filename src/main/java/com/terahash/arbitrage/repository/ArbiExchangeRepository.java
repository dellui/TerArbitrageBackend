package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.ArbiExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArbiExchangeRepository extends JpaRepository<ArbiExchange, String> {
    ArbiExchange findByUsernameAndExchangeAndEnabled(String userName, String exchange, int enabled);

    List<ArbiExchange> findAllByEnabled(int enabled);
}