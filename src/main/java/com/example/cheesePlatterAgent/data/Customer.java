package com.example.cheesePlatterAgent.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;

    @Size(max = 5)
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cheese> cheeses = new HashSet<>();

    public Customer(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer() {

    }

    public void addCheese(Cheese cheese) {
        cheese.setCustomer(this);
        this.cheeses.add(cheese);
    }

    public void removeCheese(Cheese cheese) {
        this.cheeses.remove(cheese);
    }
}
