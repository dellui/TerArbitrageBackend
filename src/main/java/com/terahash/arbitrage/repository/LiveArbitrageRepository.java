package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.LiveArbitrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveArbitrageRepository extends JpaRepository<LiveArbitrage, String> {
}
