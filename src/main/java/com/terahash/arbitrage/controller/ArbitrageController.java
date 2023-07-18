package com.terahash.arbitrage.controller;

import com.terahash.arbitrage.model.LiveArbitrage;
import com.terahash.arbitrage.service.DecisionService;
import com.terahash.arbitrage.service.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

}
