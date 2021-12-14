
package com.terahash.arbitrage.model;

import com.google.j2objc.annotations.AutoreleasePool;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "arbi_exchange")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ArbiExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "exchange", nullable = false)
    private String exchange;

    @Column(name = "private_key", nullable = false)
    private String privateKey;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Column(name = "simulated")
    private Boolean simulated;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "default_purchase_fee")
    private Double purchaseMaxFee;

    @Column(name = "default_sell_fee")
    private Double sellMaxFee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArbiExchange that = (ArbiExchange) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
