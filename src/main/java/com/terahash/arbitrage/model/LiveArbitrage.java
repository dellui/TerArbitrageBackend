package com.terahash.arbitrage.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "live_arbitrage")
@Entity
@Data
public class LiveArbitrage {
    @Id
    @Column(name = "pair", nullable = false)
    private String pair;

    @Column(name = "kraken")
    private Double kraken;

    @Column(name = "poloniex")
    private Double poloniex;

    @Column(name = "binance")
    private Double binance;

    @Column(name = "bitfinex")
    private Double bitfinex;

    @Column(name = "bleutrade")
    private Double bleutrade;

    @Column(name = "`cex_it`")
    private Double cexIt;

    @Column(name = "coinbase")
    private Double coinbase;

    @Column(name = "therock")
    private Double therock;

    @Column(name = "coindirect")
    private Double coindirect;

}
