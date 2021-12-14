package com.terahash.arbitrage.service;

public abstract class AbstractExchangeService implements ExchangeService {

    String exchangeName = "";
    String currentUser = "";

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getExchangeName() {
        return exchangeName;
    }
    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

}
