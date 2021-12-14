package com.terahash.arbitrage.service.impl;

import com.terahash.arbitrage.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value="DecisionService")
public class DecisionServerImpl implements DecisionService {
    private static final Logger log = LogManager.getLogger("arbitrage");

    @Autowired
    ConfigService configService;

    @Autowired
    ArbiSymbolTableService arbiSymbolTableService;

    @Override
    public void addTicker(Exchange exchange, String key, Ticker ticker) throws InterruptedException {

        ArrayList<ArbiTicker> v = null;
        ArbiTicker a = new ArbiTicker( key, exchange, ticker );

        if( !arbiSymbolTableService.containsKey(key) || (arbiSymbolTableService.containsKey(key) && arbiSymbolTableService.get(key) == null )  ) {
            v = new ArrayList<>();
            v.add(a);
            arbiSymbolTableService.put(key, v);
        }
        else {
            v = arbiSymbolTableService.get(key);
            if( v != null ) {
                v.remove(a);
                v.add(a);
            }
        }
        v.sort(Comparator.comparingDouble(ArbiTicker::getLastQuotation));

    }

    @Override
    public ArbiTicker getLow( String pair ) {
        ArrayList<ArbiTicker> v = arbiSymbolTableService.get(pair);
        return v.get(0);
    }

    @Override
    public ArbiTicker getHigh( String pair ) {
        ArrayList<ArbiTicker> v = arbiSymbolTableService.get(pair);
        return v.get(v.size()-1);
    }

    @Override
    public ArrayList<Candidate> getCandidates() throws InterruptedException {

        ArrayList<Candidate> candidates = new ArrayList<Candidate>();

        Boolean isCandidate = false;
        Double minimalPercentage = configService.getPropertyAsDouble("decision.percentage", "decision");

        log.debug("minimalPercentage from conf: {}", minimalPercentage);

        arbiSymbolTableService.acquire();

        Set<String> x = arbiSymbolTableService.keySet();

        for( String coin:  x ) {
            ArbiTicker high = getHigh(coin);
            ArbiTicker low = getLow(coin);
            Candidate candidate = new Candidate(low, high);

            isCandidate = low.getLastQuotation() > 0;
            isCandidate = isCandidate && (high.getLastQuotation()-low.getLastQuotation()) > 1;
            isCandidate = isCandidate && !high.getExchange().getExchangeSpecification().getExchangeName().equalsIgnoreCase( low.getExchange().getExchangeSpecification().getExchangeName());
            isCandidate = isCandidate && (candidate.getMarginPerc() > minimalPercentage);

            if( isCandidate ) candidates.add( candidate );
        }

        arbiSymbolTableService.release();

        return candidates;
    }


    @Override
    public void dump(ArrayList<Candidate> cc) throws InterruptedException {

        log.debug( "Candidates list has {} itmes", cc.size());
        for( Candidate c: cc ) {
            log.debug( "Candidate Arbitrage: {} DeltaPerc: {} Delta: {} . Purchase from {} at {} and sell to {} at {}",
                    c.getLow().getCoin(), String.format("%.2f",c.getMarginPerc()) , String.format("%.2f",c.getMargin()),
                    c.getLow().getExchange().getExchangeSpecification().getExchangeName(), String.format("%.2f",c.getLow().getLastQuotation()),
                    c.getHigh().getExchange().getDefaultExchangeSpecification().getExchangeName(), String.format("%.2f",c.getHigh().getLastQuotation()) );
        }
    }
}
