package ru.vood.amdWeb.util.abstraction


import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyNotifier
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.Binder
import org.springframework.data.jpa.repository.JpaRepository
import ru.vood.amdWeb.util.EntityInterface
import ru.vood.amdWeb.util.FieldForEditor
import java.math.BigDecimal
import java.util.*

abstract class AbstractEditorKT<T : EntityInterface, R : JpaRepository<T, BigDecimal>> : VerticalLayout, KeyNotifier {
    private var repository: R
    /* Fields to edit properties in entity */
    private var fields: HashMap<String, Component> = HashMap()
    /* Action buttons */
    private val save = Button("Save", VaadinIcon.CHECK.create())
    private val cancel = Button("Cancel")
    private val delete = Button("Delete", VaadinIcon.TRASH.create())
    private val actions = HorizontalLayout(save, cancel, delete)

    private var binder: Binder<T>? = null
    private var type: Class<T>
    private lateinit var entity: T
    private lateinit var changeHandler: AbstractEditorKT.ChangeHandler

    private var fieldsProperty: Map<String, FieldForEditor.FieldPropertyEditor>? = null


    constructor(type: Class<T>, repository: R) : super() {
        this.type = type
        this.repository = repository

        // Configure and style components
        isSpacing = true
        save.element.themeList.add("primary")
        delete.element.themeList.add("error")

        //addKeyPressListener(Key.ENTER, { e -> save() })
        addKeyPressListener(Key.ENTER, ComponentEventListener { _ -> save() })

        // wire action buttons to save, delete and reset
        save.addClickListener { _ -> save() }
        delete.addClickListener { _ -> delete() }
        cancel.addClickListener { _ -> editEntity(entity) }

        isVisible = false

    }

    private fun initBinder() {
        if (this.binder == null) {
            this.binder = Binder<T>(type)
            //this.binder = Binder<T>()

            // получение по типу сущности type необходимой информации о полях
            val fieldsFor = getFields()//FieldForEditor(type).fields
//            if (fieldsFor != null) {
            for (field in fieldsFor) {
                //создание для каждого поля сущности соответствующего компонента для редактирования
//            if ((field.value.mappedField::class.java == ComboBox::class.java)) {
//                var c = field.value.mappedField as ComboBox
//                c.items = typeCustomerRepository.findAll()
//
//            }
                fields.put(field.key, field.value.mappedField)
                TuneBinder.tune(binder, field.value)
                //          }
            }
            //          }
            //помещеие компонентов на экранную форму
            val components = ArrayList<Component>(fields.size + 1)
            components.addAll(fields.values)
            components.add(actions)
            add(*components.toTypedArray())
        }
    }

    protected open fun getFields(): Map<String, FieldForEditor.FieldPropertyEditor> {
        if (fieldsProperty == null) {
            fieldsProperty = FieldForEditor(type).fields
        }
        return fieldsProperty!!
    }


    fun editEntity(entity: T?) {
        if (entity == null) {
            isVisible = false
            return
        }

        initBinder()

/*
        val toList = getFields()!!.values.asSequence()
                .filter { entity -> entity.mappedField is ComboBox<*> }
                .toList()
        //.forEach { propertyEditor -> (propertyEditor.mappedField as ComboBox<*>).setItems(propertyEditor.dataFromRepo)  }
        println(toList)
*/

        val persisted = entity.id != null
        if (persisted) {
            // Find fresh entity for editing
            this.entity = repository.findById(entity.id).get()
        } else {
            this.entity = entity
        }
        cancel.isVisible = persisted

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder!!.bean = entity

        isVisible = true

        // Focus first name initially
//        firstName.focus()
    }

    private fun delete() {
        repository.delete(entity)
        changeHandler.onChange()
    }

    private fun save() {
        repository.save<T>(entity)
        changeHandler.onChange()
    }


    fun setChangeHandler(h: ChangeHandler) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h
    }

    interface ChangeHandler {
        fun onChange()
    }
}

