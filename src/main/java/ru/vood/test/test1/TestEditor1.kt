package ru.vood.test.test1

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import ru.vood.amdWeb.util.abstraction.AbstractEditorKT
import ru.vood.test.Customer
import ru.vood.test.CustomerRepository

@SpringComponent
@UIScope
class TestEditor1(repository: CustomerRepository) : AbstractEditorKT<Customer, CustomerRepository>(Customer::class.java, repository) {

}