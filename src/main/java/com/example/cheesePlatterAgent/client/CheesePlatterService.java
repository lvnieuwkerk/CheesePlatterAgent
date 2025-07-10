package com.example.cheesePlatterAgent.client;

import com.example.cheesePlatterAgent.data.Cheese;
import com.example.cheesePlatterAgent.data.CheeseDetails;
import com.example.cheesePlatterAgent.data.CheesePlatterRepository;
import com.example.cheesePlatterAgent.data.CheeseType;
import com.example.cheesePlatterAgent.data.Customer;
import com.example.cheesePlatterAgent.exceptions.CustomerNotFoundException;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@BrowserCallable
@AnonymousAllowed
public class CheesePlatterService {
    private final CheesePlatterRepository cheesePlatterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Getter
    private Long currentUserId;

    public CheesePlatterService(CheesePlatterRepository cheesePlatterRepository) {
        this.cheesePlatterRepository = cheesePlatterRepository;

        initDemoData();
    }

    public List<CheeseDetails> getCheesePlatter(Long userId) {
        if(userId == null) return null;
        Customer customer = cheesePlatterRepository.findCustomerById(userId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customer.getCheeses()
                .stream()
                .map(cheese ->
                        new CheeseDetails(cheese.getName(), cheese.getType().getDescription(), cheese.getProducer(), cheese.getDescription(), cheese.getPrice()))
                .toList();
    }

    public void addCheese(Long userId, String cheeseName, String type, String producer, String description, Long price) {
        Customer customer = cheesePlatterRepository.findCustomerById(userId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.addCheese(new Cheese(cheeseName, producer, description, price, CheeseType.valueOfDescription(type)));
        cheesePlatterRepository.save(customer);
    }

    public void removeCheese(Long userId, String cheeseName) {
        Customer customer = cheesePlatterRepository.findCustomerById(userId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.getCheeses().stream().filter(cheese -> cheese.getName().equals(cheeseName)).findFirst()
                .ifPresentOrElse(
                        customer::removeCheese,
                        () -> {
                            throw new RuntimeException(String.format("Error while trying to remove cheese from cheese platter, cheese with name %s not found", cheeseName));
                        }
                );
        cheesePlatterRepository.save(customer);
    }

    public void setCurrentUserId(String username) {
        Customer customer = cheesePlatterRepository.findCustomerByUserName(username).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        currentUserId = customer.getId();
    }

    public String getFullNameOfUser(Long userId) {
        if (userId == null) return null;
        Customer customer = cheesePlatterRepository.findCustomerById(userId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customer.getFirstName() + " " + customer.getLastName();
    }

    public String findCustomerFirstNameUnsafe(String userName, String param2, String param3, String param4, String param5, String param6) {
        String sql = "SELECT * FROM customer WHERE user_name = " + userName + "";
        Query query = entityManager.createNativeQuery(sql, Customer.class);

        try {
            var customers = (List<Customer>) query.getResultList();
            return customers.get(0).getFirstName();
        } catch (NoResultException e) {
            return null;
        }
    }

    private void initDemoData() {
        if (cheesePlatterRepository.findAll().isEmpty()) {
            var customer1 = new Customer("AdB", "Adam", "Black");
            var customer2 = new Customer("JoW", "Joey", "White");
            cheesePlatterRepository.saveAll(List.of(customer1, customer2));
        }
    }
}