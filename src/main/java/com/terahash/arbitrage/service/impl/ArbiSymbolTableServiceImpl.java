package com.terahash.arbitrage.service.impl;

import com.terahash.arbitrage.model.LiveArbitrage;
import com.terahash.arbitrage.repository.LiveArbitrageRepository;
import com.terahash.arbitrage.service.ArbiSymbolTableService;
import com.terahash.arbitrage.service.ArbiTicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service("ArbiSymbolTableService")
public class ArbiSymbolTableServiceImpl implements ArbiSymbolTableService {
    private static final Logger log = LogManager.getLogger("arbitrage");

    Semaphore semaphore = new Semaphore(1);
    ConcurrentHashMap<String, ArrayList<ArbiTicker>> symbolTable = new ConcurrentHashMap<>();

    @Autowired
    LiveArbitrageRepository liveArbitrageRepository;

    @Override
    public synchronized Set<String> keySet() {
        return symbolTable.keySet();
    }

    @Override
    public synchronized void put(String key, ArrayList<ArbiTicker> arbiTickerList) {
        symbolTable.put(key, arbiTickerList);
    }

    @Override
    public synchronized ArrayList<ArbiTicker> get(String key) {
        return symbolTable.get(key);
    }

    @Override
    public synchronized boolean containsKey(String key) {
        return symbolTable.containsKey(key);
    }

    @Override
    public void acquire() throws InterruptedException {
        //semaphore.acquire();
    }

    @Override
    public void release() {
        //semaphore.release();
    }

    @Override
    @Transactional
    public void save() {

        List<LiveArbitrage> l = new ArrayList<>();

        liveArbitrageRepository.deleteAll();

        for( String key: symbolTable.keySet() ) {

            LiveArbitrage liveArbitrage = new LiveArbitrage();

            liveArbitrage.setPair(key);

            ArrayList<ArbiTicker> tickerList = symbolTable.get(key);
            for( ArbiTicker arbiTicker: tickerList ) {
                switch(arbiTicker.getExchange().getExchangeSpecification().getExchangeName().toLowerCase()) {
                    case "kraken":
                        liveArbitrage.setKraken(arbiTicker.getLastQuotation());
                        break;
                    case "poloniex":
                        liveArbitrage.setPoloniex(arbiTicker.getLastQuotation());
                        break;
                    case "binance":
                        liveArbitrage.setBinance(arbiTicker.getLastQuotation());
                        break;
                    case "bitfinex":
                        liveArbitrage.setBitfinex(arbiTicker.getLastQuotation());
                        break;
                    case "bleutrade":
                        liveArbitrage.setBleutrade(arbiTicker.getLastQuotation());
                        break;
                    case "cex.it":
                        liveArbitrage.setCexIt(arbiTicker.getLastQuotation());
                        break;
                    case "coinbase":
                        liveArbitrage.setCoinbase(arbiTicker.getLastQuotation());
                        break;
                    case "therock":
                        liveArbitrage.setTherock(arbiTicker.getLastQuotation());
                        break;
                    case "coindirect":
                        liveArbitrage.setCoindirect(arbiTicker.getLastQuotation());
                        break;
                    default:
                        log.error("Exchange {} not implemented", arbiTicker.getExchange().getExchangeSpecification().getExchangeName().toLowerCase());
                        break;
                }
            }
            l.add(liveArbitrage);
        }
        liveArbitrageRepository.saveAll(l);
    }
}
