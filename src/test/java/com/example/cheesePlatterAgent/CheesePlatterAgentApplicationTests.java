package com.example.cheesePlatterAgent;

import com.example.cheesePlatterAgent.client.CheesePlatterService;
import com.example.cheesePlatterAgent.data.Cheese;
import com.example.cheesePlatterAgent.data.CheesePlatterRepository;
import com.example.cheesePlatterAgent.data.CheeseType;
import com.example.cheesePlatterAgent.data.Customer;
import com.example.cheesePlatterAgent.exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CheesePlatterAgentApplicationTests {
	@Autowired
	CheesePlatterRepository cheesePlatterRepository;

	@Autowired
	CheesePlatterService cheesePlatterService;

	@AfterEach
	public void cleanUpEach() {
		cheesePlatterRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testCreateCustomers() {
		var customer1 = new Customer("Username1", "FirstName1", "LastName1");
		var customer2 = new Customer("Username2", "FirstName2", "LastName2");

		cheesePlatterRepository.saveAll(List.of(customer1, customer2));

		var fullName1 = cheesePlatterService.getFullNameOfUser(customer1.getId());
		var fullName2 =  cheesePlatterService.getFullNameOfUser(customer2.getId());

		assertThat(fullName1).isEqualTo(customer1.getFirstName() + " " + customer1.getLastName());
		assertThat(fullName2).isEqualTo(customer2.getFirstName() + " " + customer2.getLastName());
	}

	@Test
	void customerNotFound() {
		assertThatThrownBy(() -> cheesePlatterService.getFullNameOfUser(1000000L))
				.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	void testAddAndRemoveCheeses() {
		var customer1 = new Customer("Username1", "FirstName1", "LastName1");
		var customer2 = new Customer("Username2", "FirstName2", "LastName2");

		cheesePlatterRepository.saveAll(List.of(customer1, customer2));

		cheesePlatterService.addCheese(customer1.getId(), "cheese1", CheeseType.HARD.description, "Producer1", "Description1", 100L);
		cheesePlatterService.addCheese(customer1.getId(), "cheese2", CheeseType.BLUE.description, "Producer2", "Description2", 200L);
		cheesePlatterService.addCheese(customer2.getId(), "cheese3", CheeseType.GOAT_SHEEP.description, "Producer3", "Description3", 300L);

		var cheeses1 = cheesePlatterService.getCheesePlatter(customer1.getId());
		var cheeses2 = cheesePlatterService.getCheesePlatter(customer2.getId());

		assertThat(cheeses1.size()).isEqualTo(2);
		assertThat(cheeses2.size()).isEqualTo(1);

		cheesePlatterService.removeCheese(customer1.getId(), "cheese2");
		cheesePlatterService.addCheese(customer2.getId(), "cheese4", CheeseType.RED_WHITE_RIND.description, "Producer4", "Description4", 400L);

		cheeses1 = cheesePlatterService.getCheesePlatter(customer1.getId());
		cheeses2 = cheesePlatterService.getCheesePlatter(customer2.getId());

		assertThat(cheeses1.size()).isEqualTo(1);
		assertThat(cheeses2.size()).isEqualTo(2);
	}

	@Test
	void testCheeseNotFound() {
		var customer1 = new Customer("Username1", "FirstName1", "LastName1");
		cheesePlatterRepository.save(customer1);

		assertThatThrownBy(() -> cheesePlatterService.removeCheese(customer1.getId(), "notExisting"))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Error while trying to remove cheese from cheese platter, cheese with name notExisting not found");
	}

	@Test
	void testUnsafeQuery() {
		var customers = cheesePlatterService.findCustomersUnsafe("notExisting or 1=1");
		assertThat(customers.size()).isEqualTo(2);

		var firstname = customers.get(0).getFirstName();
		assertThat(firstname).isNotNull();
	}
}
