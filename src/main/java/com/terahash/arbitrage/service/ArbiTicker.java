package com.terahash.arbitrage.service;

import com.terahash.arbitrage.model.ArbiExchange;
import org.hibernate.Hibernate;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.util.Objects;

public class ArbiTicker {
    private String coin = "";
    private Exchange exchange;
    private Ticker ticker;

    public ArbiTicker(String coin, Exchange exchange, Ticker ticker) {
        this.coin = coin;
        this.exchange = exchange;
        this.ticker = ticker;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public String getCoin() {
        return  coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    @Override
    public String toString() {
        return exchange.getDefaultExchangeSpecification().getExchangeName();
    }
    @Override
    public boolean equals(Object o) {
        ArbiTicker that = (ArbiTicker) o;
        return getExchange().getDefaultExchangeSpecification().getExchangeName().equalsIgnoreCase( that.getExchange().getDefaultExchangeSpecification().getExchangeName());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public double getLastQuotation() {
        return ticker.getLast().doubleValue();
    }
}
