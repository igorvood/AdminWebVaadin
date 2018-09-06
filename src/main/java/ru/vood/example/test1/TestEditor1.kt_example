package ru.vood.example.test1

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import ru.vood.amdWeb.util.FieldForEditor
import ru.vood.amdWeb.util.abstraction.AbstractEditorKT
import ru.vood.example.Customer
import ru.vood.example.CustomerRepository
import ru.vood.example.TypeCustomerRepository

@SpringComponent
@UIScope
class TestEditor1 : AbstractEditorKT<Customer, CustomerRepository> {
    private val typeCustomerRepository: TypeCustomerRepository

    @Autowired
    constructor(repository: CustomerRepository, typeCustomerRepository: TypeCustomerRepository)
            : super(Customer::class.java, repository) {
        this.typeCustomerRepository = typeCustomerRepository
    }

    override fun getFields(): Map<String, FieldForEditor.FieldPropertyEditor> {
        val fields = super.getFields()
        fields["typeCustomer"]!!.dataForComboBox = typeCustomerRepository.findAll()
        return fields
    }
}