package com.terahash.arbitrage.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "group")
    private String group;

    @Column(name = "description")
    private String description;

}
