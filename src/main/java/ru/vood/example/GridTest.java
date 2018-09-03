package ru.vood.example;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "g")
public class GridTest extends VerticalLayout {


    private CustomerRepository repo;

    public GridTest(CustomerRepository repo) {


        this.repo = repo;
        VerticalLayout vbox = new VerticalLayout();
        System.out.println(repo);
        Grid<Customer> grid = new Grid<>(Customer.class);
        System.out.println(repo.findAll());
        grid.setItems(repo.findAll());

        grid.addColumn(Customer::getFirstName).setHeader("First Name");
        grid.addColumn(Customer::getLastName).setHeader("Last Name");

        add(grid);
    }
}
