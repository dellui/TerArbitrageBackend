package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.LiveArbitrage;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveArbitrageRepository extends DataTablesRepository<LiveArbitrage, String> {
}
