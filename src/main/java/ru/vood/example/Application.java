package ru.vood.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Calendar;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner loadData(CustomerRepository repository) {
        return (args) -> {

            TypeCustomerRepository bean = applicationContext.getBean(TypeCustomerRepository.class);

            final TypeCustomer type_1 = bean.save(TypeCustomer.builder().code("type_1").build());
            final TypeCustomer type_2 = bean.save(TypeCustomer.builder().code("type_2").build());


            Customer customer1 = Customer.builder()
                    .firstName("Игорь")
                    .lastName("Вуд")
                    .salary(1000)
                    .typeCustomer(type_2)
                    .dateBirth(Calendar.getInstance().getTime())
                    .married(true)
                    .build();

            // save a couple of customers
            repository.save(customer1);
            repository.save(new Customer(null, "Jack", "Bauer", 0, null, null, false));
            repository.save(new Customer(null, "Chloe", "O'Brian", 0, null, null, false));
            repository.save(new Customer(null, "Kim", "Bauer", 0, null, null, false));
            repository.save(new Customer(null, "David", "Palmer", 0, null, null, false));
            repository.save(new Customer(null, "Michelle", "Dessler", 0, null, null, false));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
/*
            Customer customer = repository.findById(1L).get();
            log.info("Customer found with findOne(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");
*/

            // fetch customers by last name
            log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
            log.info("--------------------------------------------");
            for (Customer bauer : repository
                    .findByLastNameStartsWithIgnoreCase("Bauer")) {
                log.info(bauer.toString());
            }
            log.info("");
        };
    }

}