package com.terahash.arbitrage.model;

import javax.persistence.*;

@Table(name = "arbi_exchange_coins")
@Entity
public class ArbiExchangeCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_exchange", nullable = false)
    private ArbiExchange idExchange;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pair", nullable = false)
    private Coin pair;

    @Column(name = "custom_purchase_fee")
    private Double purchaseFee;

    @Column(name = "custom_sell_fee")
    private Double sellFee;

    @Column(name = "withdraw_fee")
    private Double withdrawnFee;

    @Column(name = "enabled")
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Double getWithdrawnFee() {
        return withdrawnFee;
    }

    public void setWithdrawnFee(Double withdrawnFee) {
        this.withdrawnFee = withdrawnFee;
    }

    public Double getSellFee() {
        return sellFee;
    }

    public void setSellFee(Double sellFee) {
        this.sellFee = sellFee;
    }

    public Double getPurchaseFee() {
        return purchaseFee;
    }

    public void setPurchaseFee(Double purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public Coin getPair() {
        return pair;
    }

    public void setPair(Coin pair) {
        this.pair = pair;
    }

    public ArbiExchange getIdExchange() {
        return idExchange;
    }

    public void setIdExchange(ArbiExchange idExchange) {
        this.idExchange = idExchange;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
