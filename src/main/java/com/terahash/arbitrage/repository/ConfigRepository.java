package com.terahash.arbitrage.repository;

import com.terahash.arbitrage.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer>, JpaSpecificationExecutor<Config> {
}