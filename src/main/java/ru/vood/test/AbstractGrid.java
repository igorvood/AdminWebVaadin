package ru.vood.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.StringUtils;
import ru.vood.amdWeb.util.FieldForView;

import java.util.Map;
import java.util.Set;

public abstract class AbstractGrid<T extends Customer, R extends JpaRepository> extends VerticalLayout {
    final com.vaadin.flow.component.grid.Grid<T> grid;
    final TextField filter;

    //    private final CustomerEditor editor;
    private final R repo;
    private final Button addNewBtn;
    private Class<T> type;

    public AbstractGrid(R repo, AbstractEditor editor, Class<T> type) {
        this.type = type;
        this.repo = repo;
        //this.editor = editor;
        this.grid = new Grid<>(type);
        grid.setPageSize(2);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("500px");
        final FieldForView<T> tFieldForView = new FieldForView<>(type);
        final Map<String, FieldForView.FieldPropertyView> mapFields = tFieldForView.getFields();
        final Set<String> fieldsList = mapFields.keySet();
        final String[] fields = fieldsList.toArray(new String[fieldsList.size()]);

/*
        final List<String> fieldsList = new FieldForView<T>(type)
                .getFields()
                .values()
                .stream()
                .map(o -> o.getDisplayName())
                .collect(Collectors.toList());

        final String[] fields = fieldsList.toArray(new String[fieldsList.size()]);
*/

        grid.setColumns(fields);
        //grid.setColumns("id", "firstName", "lastName");
        grid.setPageSize(2);

        //для каждой колонки
        mapFields.values()
                .stream()
                .forEach(r -> {
                    // добавить сортировку
                    grid.getColumnByKey(r.getFieldName()).setSortable(true);
                });

        //grid.getColumnByKey("id").setWidth("150px").setFlexGrow(0);

        //filter.setPlaceholder("Filter by last name dsadasdasdas");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        final T instanceT = tFieldForView.getInstanceT();
        addNewBtn.addClickListener(e -> editor.editCustomer(instanceT));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);
    }

    // tag::listCustomers[]
    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {

            grid.setItems(repo.findAll());
        }
/*
        else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
*/
    }
    // end::listCustomers[]


}
