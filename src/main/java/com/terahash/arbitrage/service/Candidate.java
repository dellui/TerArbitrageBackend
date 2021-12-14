package com.terahash.arbitrage.service;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
@Getter
@Setter
public class Candidate {
    ArbiTicker low;
    ArbiTicker high;
    double margin = 0;
    double marginPerc = 0;

    public Candidate(ArbiTicker low, ArbiTicker high) {
        this.low = low;
        this.high = high;
        margin = high.getLastQuotation() - low.getLastQuotation();
        marginPerc = (margin/high.getLastQuotation())*100;
    }

}
