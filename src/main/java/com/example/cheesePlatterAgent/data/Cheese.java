package com.example.cheesePlatterAgent.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class Cheese {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private CheeseType type;
    private String producer;
    private String description;
    private Long price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Setter
    private Customer customer;

    public Cheese(String name, String producer, String description, Long price, CheeseType type) {
        this.name = name;
        this.producer = producer;
        this.description = description;
        this.price = price;
        this.type = type;
    }
}
