package ru.vood.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@HtmlImport("styles.html")
@Theme(Lumo.class)
@Route(value = "mainView456")
public class MainView456 extends VerticalLayout {


    private CustomerRepository repo;

    private CustomerEditor editor;

    private Grid<Customer> grid;

    private Button addNewBtn;

    public MainView456(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        //------------------------------------------------
        ListDataProvider<Customer> dataProvider = new ListDataProvider<>(repo.findAll());

        grid.setDataProvider(dataProvider);

        List<ValueProvider<Customer, String>> valueProviders = new ArrayList<>();
        valueProviders.add(Customer::getFirstName);
        valueProviders.add(person -> String.valueOf(person.getLastName()));
        valueProviders.add(person -> String.valueOf(person.getSalary()));
        valueProviders.add(person -> String.valueOf(person.getDateBirth()));
        valueProviders.add(person -> {
            if (person.getTypeCustomer() != null)
                return person.getTypeCustomer().getCode();
            else return null;
        });
        //valueProviders.add(person -> String.valueOf(person.isMarried()));

        Iterator<ValueProvider<Customer, String>> iterator = valueProviders.iterator();

        grid.getColumns().stream().forEach(customerColumn -> {

            //customerColumn.setHeader()

        });
        grid.addColumn(iterator.next()).setHeader("First Name");
        grid.addColumn(iterator.next()).setHeader("Last Name");
        grid.addColumn(iterator.next()).setHeader("Salary");
        grid.addColumn(iterator.next()).setHeader("Date Birth");
        grid.addColumn(iterator.next()).setHeader("Type customer");
//        grid.addColumn(iterator.next()).setHeader("Married");

        HeaderRow filterRow = grid.appendHeaderRow();

        Iterator<ValueProvider<Customer, String>> iterator2 = valueProviders.iterator();

        grid.getColumns().forEach(column -> {
            column.setSortable(true);
            TextField field = new TextField();
            if (iterator2.hasNext()) {
                ValueProvider<Customer, String> valueProvider = iterator2.next();

                field.addValueChangeListener(event -> dataProvider
                        .addFilter(person -> StringUtils.containsIgnoreCase(
                                valueProvider.apply(person), field.getValue())));
                field.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(column).setComponent(field);
                field.setSizeFull();
                field.setPlaceholder("Filter");
            }
        });
        //---------------------------------------

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
        });

        // Initialize listing
        //listCustomers(null);
    }

    // tag::listCustomers[]
    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
    // end::listCustomers[]

}
