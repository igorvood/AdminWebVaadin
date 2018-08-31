package ru.vood.amdWeb.util.abstraction

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.ValueProvider
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.repository.JpaRepository
import ru.vood.amdWeb.util.FieldForView
import java.math.BigDecimal
import java.util.*

@Deprecated("Буду считать что не получилось, но пока оставлю")
abstract class AbstractGrid2<T /*: Customer*/, R : JpaRepository<T, BigDecimal>> : VerticalLayout {
    lateinit var fields: HashMap<String, FieldPropertyForView<T>>//()

    var grid: com.vaadin.flow.component.grid.Grid<T>

    var repo: R
    var addNewBtn: Button
    var type: Class<T>
    //lateinit var editor: AbstractEditorKT<T, R>


    constructor(repo: R, type: Class<T>/*, editor: AbstractEditorKT<T, R>*/) : super() {
        this.repo = repo
        this.type = type
        //this.editor = editor
        this.addNewBtn = Button("New Entity - add here name of Entity", VaadinIcon.PLUS.create())

        fields = initialization()


        // если этот метод не перекрыт. то надо вернуть стандартный набор колонок
        if (fields.size == 0) {
            this.grid = Grid<T>(type)

            val tFieldForView = FieldForView<T>(type)
            val mapFields = tFieldForView.getFields()
            val fieldsList = mapFields.keys
            val fields = fieldsList.toTypedArray()

            // грид создается с указанием класса. от туда ваадин вычитает все поля.
            grid.setColumns(*fields)

            fieldsList.asSequence().forEach { field ->
                val column = grid.getColumnByKey(field)
                column.isSortable = true
                column.setHeader(mapFields.get(field)?.displayName)
            }
            grid.setItems(repo.findAll())
        } else {
            // поля заданы надо их использовать
            this.grid = Grid<T>()
            val dataProvider = ListDataProvider<T>(repo.findAll())
            grid.dataProvider = dataProvider
            val columns: HashMap<String, FieldPropertyForView<T>> = fields
            val valueProviders = ArrayList<ValueProvider<T, String>>()
            val filterRow = grid.appendHeaderRow()
            columns.asSequence()
                    .forEach { entry ->
                        valueProviders.add(entry.value.getterForView)
                        val column: Grid.Column<T> = grid.addColumn(entry.value.getterForView)
                                .setSortable(true)
                                .setHeader(entry.value.displayName)
                        val field = TextField()
                        field.addValueChangeListener { event ->
                            dataProvider.addFilter { entity ->
                                StringUtils.containsIgnoreCase(entry.value.getterForView.apply(entity), field.value)
                            }
                        }
                        field.valueChangeMode = ValueChangeMode.EAGER

                        filterRow.getCell(column).setComponent(field)
                        field.setSizeFull()
                        field.placeholder = "Filter"
                    }
        }
        val actions = HorizontalLayout(addNewBtn)
        add(actions, grid/*, editor*/)
        grid.height = "500px"

    }

    open protected fun initialization(): HashMap<String, AbstractGrid2.FieldPropertyForView<T>> {
        val retVal = HashMap<String, FieldPropertyForView<T>>();

        val tFieldForView = FieldForView<T>(type)
        val mapFields = tFieldForView.getFields()

        return retVal
    }

/*
    open fun getListColumns() = fields

    open fun setListCoumms(fields: HashMap<String, FieldPropertyForView<T>>) {
        this.fields = fields
    }
*/

    class FieldPropertyForView<T>(var displayName: String = ""
            //, var fieldName: String = ""
                                  , var getterForView: ValueProvider<T, String>
    )
}


