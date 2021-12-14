package com.terahash.arbitrage.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.util.ArrayList;

public interface DecisionService {
    void addTicker(Exchange exchange, String pair, Ticker ticker ) throws InterruptedException;
    ArbiTicker getLow( String pair );
    ArbiTicker getHigh( String pair );
    ArrayList<Candidate> getCandidates() throws InterruptedException;
    void dump(ArrayList<Candidate> l) throws InterruptedException;
}
