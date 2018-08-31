package ru.vood.test;

import com.vaadin.flow.router.Route;

@Route(value = "gg")
public class ConcretGrid extends AbstractGrid<Customer, CustomerRepository> {

    public ConcretGrid(CustomerRepository repo, ConcreteEditor editor) {
        super(repo, editor, Customer.class);
    }
}
