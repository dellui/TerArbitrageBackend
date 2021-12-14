package com.terahash.arbitrage.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "wallet")
public class ArbiWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "currency")
    private BigDecimal currency;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "available")
    private BigDecimal available;

    @Column(name = "frozen")
    private BigDecimal frozen;

    @Column(name = "loaned")
    private BigDecimal loaned;

    @Column(name = "borrowed")
    private BigDecimal borrowed;

    @Column(name = "withdrawing")
    private BigDecimal withdrawing;

    @Column(name = "depositing")
    private BigDecimal depositing;

    @Column(name = "timestamp")
    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getCurrency() {
        return currency;
    }

    public void setCurrency(BigDecimal currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

    public BigDecimal getLoaned() {
        return loaned;
    }

    public void setLoaned(BigDecimal loaned) {
        this.loaned = loaned;
    }

    public BigDecimal getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(BigDecimal borrowed) {
        this.borrowed = borrowed;
    }

    public BigDecimal getWithdrawing() {
        return withdrawing;
    }

    public void setWithdrawing(BigDecimal withdrawing) {
        this.withdrawing = withdrawing;
    }

    public BigDecimal getDepositing() {
        return depositing;
    }

    public void setDepositing(BigDecimal depositing) {
        this.depositing = depositing;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
