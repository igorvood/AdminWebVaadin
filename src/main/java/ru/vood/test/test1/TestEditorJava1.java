package ru.vood.test.test1;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vood.amdWeb.util.FieldForEditor;
import ru.vood.amdWeb.util.abstraction.AbstractEditorKT;
import ru.vood.test.Customer;
import ru.vood.test.CustomerRepository;
import ru.vood.test.TypeCustomerRepository;

import java.util.Map;

@SpringComponent
@UIScope
public class TestEditorJava1 extends AbstractEditorKT<Customer, CustomerRepository> {
    //  @Autowired
    private TypeCustomerRepository typeCustomerRepository;

    /*
        @Autowired
        private CustomerRepository repository;
    */
    @Autowired
    public TestEditorJava1(@NotNull CustomerRepository repository, @NotNull TypeCustomerRepository typeCustomerRepository) {
        super(Customer.class, repository);
        this.typeCustomerRepository = typeCustomerRepository;
    }

    @Override
    protected Map<String, FieldForEditor.FieldPropertyEditor> getFields() {
        final Map<String, FieldForEditor.FieldPropertyEditor> fields = super.getFields();
        final FieldForEditor.FieldPropertyEditor typeCustomer = fields.get("typeCustomer");

/*

        typeCustomer.setValueProvider(o -> {
            if (((Customer) o).getTypeCustomer() != null) {
                return ((Customer) o).getTypeCustomer().getCode();
            } else {
                return null;
            }
        });
        typeCustomer.setSetter((o, o2) -> ((Customer) o).setTypeCustomer((TypeCustomer) o2));
*/


        typeCustomer.setDataFromRepo(typeCustomerRepository.findAll());
        ((ComboBox) typeCustomer.getMappedField()).setItems(typeCustomer.getDataFromRepo());
        return fields;
    }
}
