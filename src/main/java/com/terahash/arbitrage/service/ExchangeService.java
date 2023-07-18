package com.terahash.arbitrage.service;

import com.terahash.arbitrage.model.ArbiExchange;
import com.terahash.arbitrage.model.LiveArbitrage;
import org.knowm.xchange.Exchange;

import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ExchangeService {

    Boolean getBalance(org.knowm.xchange.Exchange exchange) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException;

    Long updateQuotations(String startOrStop) throws IOException;
    void updateQuotationsAsync() throws IOException, InterruptedException;;
    public void enableQuotationRetrive();
    public void disableQuotationRetrive();

    org.knowm.xchange.Exchange getExchange(String userName, String exchange) throws IOException;
    org.knowm.xchange.Exchange getExchange(ArbiExchange arbiExchange) throws IOException;

    void dumpExchageWithdrawFee(Exchange exchange) throws IOException;
}
