package ru.vood.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vood.amdWeb.util.FieldForView;

import java.util.*;

/*
@SpringComponent
@UIScope
*/
public class AbstractEditor<T extends Customer, R extends JpaRepository> extends VerticalLayout implements KeyNotifier {
    private final R repository;
    /* Fields to edit properties in Customer entity */
    Map<String, TextField> fields = new HashMap<>();
    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    //    TextField firstName = new TextField("First name");
//    TextField lastName = new TextField("Last name");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<T> binder;
    private Class<T> type;
    private T customer;
    private AbstractEditor.ChangeHandler changeHandler;


    @Autowired
    public AbstractEditor(Class<T> type, R repository) {
        this.type = type;
        this.repository = repository;
        this.binder = new Binder<>(type);


        final Map<String, FieldForView.FieldPropertyView> fieldsForView = new FieldForView<>(type).getFields();
        final Set<String> fieldNameSet = fieldsForView.keySet();
        for (String fieldName : fieldNameSet) {
            fields.put(fieldName, new TextField(fieldsForView.get(fieldName).getDisplayName()));
        }


        List<Component> components = new ArrayList<>(fields.size() + 1);
        components.addAll(fields.values());
        components.add(actions);
        Component[] myArray = components.toArray(new Component[components.size()]);
        add(myArray);

        // bind using naming convention
        //binder.bindInstanceFields(this);
        for (String tf : fields.keySet()) {
            Binder.Binding<T, String> bind = binder.bind(fields.get(tf), tf);
            System.out.println(bind);
//


        }


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

    public final void editCustomer(T c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = (T) repository.findById(c.getId()).get();
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
        fields.values().iterator().next().focus();
    }

    public void setChangeHandler(AbstractEditor.ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }


}
