package com.terahash.arbitrage.service.impl;

import com.terahash.arbitrage.model.ArbiExchange;
import com.terahash.arbitrage.model.LiveArbitrage;
import com.terahash.arbitrage.repository.ArbiExchangeRepository;
import com.terahash.arbitrage.repository.CoinRepository;
import com.terahash.arbitrage.service.ArbiSymbolTableService;
import com.terahash.arbitrage.service.ConfigService;
import com.terahash.arbitrage.service.DecisionService;
import com.terahash.arbitrage.service.ExchangeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bitbay.v3.BitbayExchange;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitmex.BitmexExchange;
import org.knowm.xchange.bleutrade.BleutradeExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.coinbase.v2.CoinbaseExchange;
import org.knowm.xchange.coindirect.CoindirectExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.OptionsContract;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Fee;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.params.CurrencyPairsParam;
import org.knowm.xchange.therock.TheRockExchange;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *
 * @author l.dellamonica
 */
@Service(value="exchangeService")
public class ExchangeServiceImpl implements ExchangeService {
    private static final Logger log = LogManager.getLogger("arbitrage");

    HashMap<String, org.knowm.xchange.Exchange> exchangeCache = new HashMap<>();

    @Autowired
    ArbiExchangeRepository arbiExchangeRepository;
    @Autowired
    CoinRepository coinRepository;
    @Autowired
    DecisionService decisionService;
    @Autowired
    ConfigService configService;
    @Autowired
    ArbiSymbolTableService arbiSymbolTableService;

    private boolean isEnableQuotation = false;

    @Override
    public org.knowm.xchange.Exchange getExchange(String userName, String exchange) throws IOException {
        try {
            ArbiExchange a = arbiExchangeRepository.findByUsernameAndExchangeAndEnabled(userName, exchange, 1);
            return getExchange(a);
        }
        catch (Exception e ) {
            return null;
        }
    }

    @Override
    public org.knowm.xchange.Exchange getExchange(ArbiExchange a) throws IOException {

        try {
            ExchangeSpecification exSpec = null;

            if (a == null) throw new IOException("Account is null");

            String k = a.getUsername().toLowerCase() + "_" + a.getExchange().toLowerCase();

            if (exchangeCache.containsKey(k)) return exchangeCache.get(k);

            switch (a.getExchange()) {
                case "kraken":
                    exSpec = new KrakenExchange().getDefaultExchangeSpecification();
                    break;
                case "coinbase":
                    exSpec = new CoinbaseExchange().getDefaultExchangeSpecification();
                    break;
                case "binance":
                    exSpec = new BinanceExchange().getDefaultExchangeSpecification();
                    //exSpec.setExchangeSpecificParametersItem("nonce");
                    break;
                case "bitfinex":
                    exSpec = new BitfinexExchange().getDefaultExchangeSpecification();
                    break;
                case "bitbay":
                    exSpec = new BitbayExchange().getDefaultExchangeSpecification();
                    break;
                case "bitstamp":
                    exSpec = new BitbayExchange().getDefaultExchangeSpecification();
                    break;
                case "therock":
                    exSpec = new TheRockExchange().getDefaultExchangeSpecification();
                    break;
                case "bitmex":
                    exSpec = new BitmexExchange().getDefaultExchangeSpecification();
                    break;
                case "bleutrade":
                    exSpec = new BleutradeExchange().getDefaultExchangeSpecification();
                    break;
                case "cex.io":
                    exSpec = new CexIOExchange().getDefaultExchangeSpecification();
                    break;
                case "coindirect":
                    exSpec = new CoindirectExchange().getDefaultExchangeSpecification();
                    break;
                case "poloniex":
                    exSpec = new PoloniexExchange().getDefaultExchangeSpecification();
                    break;
                default:
                    throw new IOException("Exchange [" + a.getExchange() + "] not found or disabled. Please check getExchange Factory method");
            }

            exSpec.setUserName(a.getUsername());
            exSpec.setApiKey(a.getPublicKey());
            exSpec.setSecretKey(a.getPrivateKey());
            exchangeCache.put(k, org.knowm.xchange.ExchangeFactory.INSTANCE.createExchange(exSpec));
            return exchangeCache.get(k);
            // TODO: verificare la gestione dell'invalidate della chache degli exchange.
        }
        catch (Exception e ){
            log.error(e);
            return null;
        }
    }

    @Override
    public Boolean getBalance(org.knowm.xchange.Exchange exchange) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {

        AccountService accountService = exchange.getAccountService();
        AccountInfo accountInfo = accountService.getAccountInfo();
        String accountInfoStr = accountInfo.toString();
        log.debug("GetBalance: {}", accountInfoStr );
        Map<String, org.knowm.xchange.dto.account.Wallet> wallets = accountInfo.getWallets();
        for( String k: wallets.keySet() ) {
            if( k == null ) continue;
            org.knowm.xchange.dto.account.Wallet w = (org.knowm.xchange.dto.account.Wallet)wallets.get(k);
            for( Currency currency: w.getBalances().keySet() ) {
                Balance b = w.getBalance( currency );
                log.debug( "Wallet {} - Coin {}: Available {} - Borrowed {} Total {} - Withdrawing {} - AvailableForWithdrawal {} - " +
                        "Depositing {} - Frozen {} - Loaned {}",
                        w.getId(), currency.getCurrencyCode(),
                        b.getAvailable(), b.getBorrowed(), b.getTotal(), b.getWithdrawing(), b.getAvailableForWithdrawal(),
                        b.getDepositing(), b.getFrozen(), b.getLoaned() );
            }
        }
        return true;
    }

    @Override
    public Long updateQuotations(String startOrStop) throws IOException {
        try {
            if( startOrStop.equalsIgnoreCase( "start" ) ) {
                enableQuotationRetrive();
                updateQuotationsAsync();
            }
            else {
                disableQuotationRetrive();
            }
        } catch (InterruptedException e) {
            disableQuotationRetrive();
            e.printStackTrace();
        }
        return 1L; // TODO: ritornare requestId generata in qualche modo
    }

    @Override
    @Async
    public void updateQuotationsAsync() throws IOException, InterruptedException {

        int sleepTime = configService.getPropertyAsInteger("quotation.sleep", "*" );

        while( isEnabledQuotation() ) {

            arbiSymbolTableService.acquire();

            log.info( "Main loop iteration start" );

            List<ArbiExchange> arbiExchanges = arbiExchangeRepository.findAllByEnabled(1);

            for( ArbiExchange arbiExchange: arbiExchanges ) {
                Currency pair = null;
                Set<Currency> pairs = new HashSet<>();
                Exchange exchange = getExchange(arbiExchange);

                try {
                    for (Currency cp : exchange.getExchangeMetaData().getCurrencies().keySet()) {
                        pair = cp;
                        pairs.add(cp);
                    }
                    List<Ticker> tickerList = exchange.getMarketDataService().getTickers(null); // TODO: passare solo le coppie di valuta richiesta
                    for (Ticker ticker : tickerList) {
                        decisionService.addTicker(exchange, ticker.getInstrument().getBase().toString(), ticker);
                    }
                } catch (Exception e) {
                    log.debug("Exchange Name: {} - Pair: {} - Last: {}", exchange != null ? exchange.getDefaultExchangeSpecification().getExchangeName() : "Unknown", pair != null ? pair.toString() : "unknown", "");
                }
            }

            arbiSymbolTableService.save();

            log.info( "Main loop iteration completed. Sleep {}. Release sem ", sleepTime );
            arbiSymbolTableService.release();

            Thread.sleep( sleepTime );

        }
    }

    @Override
    public void disableQuotationRetrive() {
        isEnableQuotation = false;
    }

    @Override
    public void enableQuotationRetrive() {
        isEnableQuotation = true;
    }
    private boolean isEnabledQuotation() {
        return isEnableQuotation;
    }

    @Override
    public void dumpExchageWithdrawFee(Exchange x) throws IOException {

        List<ArbiExchange> arbiExchanges = arbiExchangeRepository.findAllByEnabled(1);

        for (ArbiExchange arbiExchange : arbiExchanges) {
            CurrencyPair pair = null;
            Set<CurrencyPair> pairs = new HashSet<>();
            Exchange exchange = getExchange(arbiExchange);
            if( arbiExchange == null || exchange == null ) {
                log.error("{} is null", arbiExchange.getExchange());
            }
            try {
                OptionsContract c;
                Map<Instrument, Fee> df = exchange.getAccountService().getDynamicTradingFeesByInstrument();

                for (Instrument cp : df.keySet()) {
                    Fee f = df.get(cp);
                    log.debug("{} - {}: MakerFee: {} TakerFee: {}", exchange.getExchangeSpecification().getExchangeName(), cp.toString(), f.getMakerFee(), f.getTakerFee());
                }
            } catch (Exception e) {
                log.debug("{} - getDynamicTradingFees: Unsopported ", exchange != null ? exchange.getExchangeSpecification().getExchangeName() : "Unknown");
            }
        }
    }

}
