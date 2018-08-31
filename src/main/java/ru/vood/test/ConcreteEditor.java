package ru.vood.test;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ConcreteEditor extends AbstractEditor<Customer, CustomerRepository> {

    public ConcreteEditor(Class<Customer> type, CustomerRepository repository) {
        super(type, repository);
    }
}
