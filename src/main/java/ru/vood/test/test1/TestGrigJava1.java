package ru.vood.test.test1;


import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jetbrains.annotations.NotNull;
import ru.vood.amdWeb.util.abstraction.AbstractGridKT;
import ru.vood.test.Customer;
import ru.vood.test.CustomerRepository;

@Route(value = "testGrigJava1")
@UIScope
public class TestGrigJava1 extends AbstractGridKT<Customer, CustomerRepository, TestEditorJava1> {
    public TestGrigJava1(@NotNull CustomerRepository repo, @NotNull TestEditorJava1 editor) {
        super(repo, Customer.class, editor);
    }
    /*
    public TestGrigJava1(@NotNull CustomerRepository repo) {
        super(repo, Customer.class);
    }
*/

/*
    @NotNull
    @Override
    public HashMap<String, FieldPropertyForView<Customer>> getListColumns() {
        final HashMap<String, AbstractGridKT.FieldPropertyForView<Customer>> mapColumns = super.getListColumns();
        mapColumns.put("first val"
                , new AbstractGridKT.FieldPropertyForView<>("first val display", customer ->
                        customer.getLastName()
                ));
        mapColumns.put("first val2"
                , new AbstractGridKT.FieldPropertyForView<>("first val display2", customer ->
                        customer.getFirstName()
                ));


        return mapColumns;
    }
*/
}
