package com.terahash.arbitrage.service;

import java.util.ArrayList;
import java.util.Set;

public interface ArbiSymbolTableService {
    Set<String> keySet();
    void put(String key, ArrayList<ArbiTicker> arbiTickerList);
    ArrayList<ArbiTicker> get(String key);
    boolean containsKey(String key );
    void acquire() throws InterruptedException;
    void release();

    void save();
}
