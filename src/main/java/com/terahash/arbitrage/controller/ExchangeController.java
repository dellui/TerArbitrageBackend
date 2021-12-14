package com.terahash.arbitrage.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.terahash.arbitrage.service.Candidate;
import com.terahash.arbitrage.service.DecisionService;
import com.terahash.arbitrage.service.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author Luigi Della Monica
 */
@Api(value = "API per gestione Exchange")
@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private static final Logger log = LogManager.getLogger("arbitrage");

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    DecisionService decisionService;

    @ApiOperation(value = "Restituisce il balance dell'utente loggato")
    @GetMapping(value = "/{exchange}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getBalance(
        @PathVariable("exchange") String exchangeName,
        @RequestHeader(name = "user-name", required = true) String username) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        return ResponseEntity.ok(exchangeService.getBalance(exchangeService.getExchange(username, exchangeName)));
    }

    @ApiOperation(value = "Aggiorna le quotazioni di tutti gli exchange")
    @GetMapping(value = "/quotations/{startOrStop}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateQuotations(
            @PathVariable("startOrStop") String startOrStop,
            @RequestHeader(name = "user-name", required = true) String username) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        //Long requestId = exchangeService.updateQuotations(startOrStop);
        try {
            if( startOrStop.equalsIgnoreCase( "start" ) ) {
                log.debug("enable");
                exchangeService.enableQuotationRetrive();
                log.debug("start async call");
                exchangeService.updateQuotationsAsync();
                log.debug("end async call");
            }
            else {
                exchangeService.disableQuotationRetrive();
            }
        } catch (InterruptedException e) {
            exchangeService.disableQuotationRetrive();
            e.printStackTrace();
        }
        return ResponseEntity.ok(1L);
    }

    @ApiOperation(value = "Visualizza tutte i candidati")
    @GetMapping(value = "/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getCandidates(
            @RequestHeader(name = "user-name", required = true) String username) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        ArrayList<Candidate> l = decisionService.getCandidates();
        decisionService.dump(l);
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Dump withdraw fee su file di log")
    @GetMapping(value = "/fees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> dumpExchageWithdrawFee(
            @PathVariable("exchange") String exchangeName,
            @RequestHeader(name = "user-name", required = true) String username) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {

        exchangeService.dumpExchageWithdrawFee(exchangeService.getExchange(username,exchangeName));

        return ResponseEntity.ok(true);
    }



}
