package ru.vood.test.test1

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import ru.vood.amdWeb.util.FieldForEditor
import ru.vood.amdWeb.util.abstraction.AbstractEditorKT
import ru.vood.test.Customer
import ru.vood.test.CustomerRepository
import ru.vood.test.TypeCustomerRepository

@SpringComponent
@UIScope
/*
class TestEditor1(@Autowired repository: CustomerRepository, @Autowired typeCustomerRepository: TypeCustomerRepository)
    : AbstractEditorKT<Customer, CustomerRepository>(Customer::class.java, repository) {
*/
class TestEditor1 : AbstractEditorKT<Customer, CustomerRepository> {
    private val typeCustomerRepository: TypeCustomerRepository

    @Autowired
    constructor(repository: CustomerRepository, typeCustomerRepository: TypeCustomerRepository)
            : super(Customer::class.java, repository) {
        this.typeCustomerRepository = typeCustomerRepository
    }

    override fun getFields(): Map<String, FieldForEditor.FieldPropertyEditor> {
        val fields = super.getFields()
        val typeCustomer = fields["typeCustomer"]

        typeCustomer!!.setDataFromRepo(typeCustomerRepository.findAll())
        //(typeCustomer.getMappedField() as ComboBox<TypeCustomer>).setItems(typeCustomer.getDataFromRepo())
        //(typeCustomer.mappedField as ComboBox<*>).setItems(typeCustomer.dataFromRepo)
        return fields
    }
}