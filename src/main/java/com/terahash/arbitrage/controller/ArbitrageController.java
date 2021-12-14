package com.terahash.arbitrage.controller;

import com.terahash.arbitrage.model.LiveArbitrage;
import com.terahash.arbitrage.service.DecisionService;
import com.terahash.arbitrage.service.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Luigi Della Monica
 */
@Api(value = "API per gestione Arbitraggio")
@RestController
@RequestMapping("/arbitrage")
public class ArbitrageController {
    private static final Logger log = LogManager.getLogger("arbitrage");

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    DecisionService decisionService;

    @ApiOperation(value = "Restituisce il live arbitrage")
    @GetMapping(value = "/live", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataTablesOutput<LiveArbitrage>> getLiveArbitrage(
            @RequestBody DataTablesInput input,
            @RequestHeader(name = "user-name", required = true) String username) throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        DataTablesOutput<LiveArbitrage> liveArbitrages = exchangeService.getLiveArbitrage(username, input);
        return ResponseEntity.ok(liveArbitrages);
    }
}
