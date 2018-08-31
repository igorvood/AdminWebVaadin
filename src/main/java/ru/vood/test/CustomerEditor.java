package ru.vood.test;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    private final CustomerRepository repository;
    /* Fields to edit properties in Customer entity */
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField salary = new TextField("Salary");
    //TextField dateBirth = new TextField("Date Birth");
    DatePicker dateBirth = new DatePicker("Date Birth");
    //TextField married = new TextField("Married");
    Checkbox married = new Checkbox("Married");
    ComboBox<TypeCustomer> typeCustomerComboBox = new ComboBox<>();
    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Customer> binder = new Binder<>(Customer.class);
    /**
     * The currently edited customer
     */
    private Customer customer;
    private ChangeHandler changeHandler;


    @Autowired
    public CustomerEditor(CustomerRepository repository, TypeCustomerRepository typeCustomerRepository) {
        this.repository = repository;

        add(firstName, lastName, salary, dateBirth, married, typeCustomerComboBox, actions);

        // bind using naming convention
        //binder.bindInstanceFields(this);

        binder.forField(firstName)
                .withNullRepresentation("")
                .bind(Customer::getFirstName, Customer::setFirstName);

        binder.forField(lastName)
                .withNullRepresentation("")
                .bind(Customer::getLastName, Customer::setLastName);

        binder.forField(salary)
                .withNullRepresentation("")
                .withConverter(
                        new StringToIntegerConverter(Integer.valueOf(0), "integers only"))
                .bind(Customer::getSalary, Customer::setSalary);

        binder.forField(dateBirth)
                .withNullRepresentation(LocalDate.MIN)
                .withConverter(
                        new LocalDateToDateConverter())
                .bind("dateBirth");
        //.bind(Customer::getDateBirth, Customer::setDateBirth);
//                .bind(customer2 -> customer2.getDateBirth()
//                        , (customer1, date) -> customer1.setDateBirth(date));

        binder.forField(married)
                .withNullRepresentation(false)
//                .withConverter(
//                        new StringToBooleanConverter("1 or 0 needed"))
                .bind(Customer::isMarried, Customer::setMarried);

        //typeCustomerComboBox.setItemLabelGenerator(TypeCustomer::getCode);
        //typeCustomerComboBox.setItems(typeCustomerRepository.findAll());
        binder.forField(typeCustomerComboBox)
                .withNullRepresentation(new TypeCustomer())
                .bind("typeCustomer");


        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        repository.save(customer);
        changeHandler.onChange();
    }

    public final void editCustomer(Customer c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = repository.findById(c.getId()).get();
        } else {
            customer = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // Focus first name initially
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }
}