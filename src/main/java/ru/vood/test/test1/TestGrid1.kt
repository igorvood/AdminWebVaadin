package ru.vood.test.test1

import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import ru.vood.amdWeb.util.abstraction.AbstractGridKT
import ru.vood.test.Customer
import ru.vood.test.CustomerRepository

@Route(value = "testGrid1")
@UIScope
//class TestGrid1(repo: CustomerRepository) : AbstractGrid<Customer, CustomerRepository>(repo, Customer::class.java, editor) {

class TestGrid1(repo: CustomerRepository, editor: TestEditor1) : AbstractGridKT<Customer, CustomerRepository, TestEditor1>(repo, Customer::class.java, editor) {

/*
    override fun getListColumns(): HashMap<String, FieldPropertyForView<Customer>> {
        val retColumns = super.getListColumns()
        retColumns.put("1 value", FieldPropertyForView("first val display", ValueProvider { source -> source.firstName }))
        retColumns.put("2 value", FieldPropertyForView("first val display___2", ValueProvider { source -> source.lastName }))
        return retColumns
    }
*/
}