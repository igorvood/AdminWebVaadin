package ru.vood.amdWeb.util.abstraction;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;
import ru.vood.amdWeb.util.FieldForEditor;

public class TuneBinder {
    public static void tune(Binder binder, FieldForEditor.FieldPropertyEditor propertyEditorMap) {
        final Binder.BindingBuilder bindingBuilder = binder.forField((HasValue) propertyEditorMap.getMappedField());
        bindingBuilder.withNullRepresentation(propertyEditorMap.getNullRepresentation());
        if (propertyEditorMap.getConverter() != null) {
            bindingBuilder.withConverter(propertyEditorMap.getConverter());
        }
        if (propertyEditorMap.getSetter() != null && propertyEditorMap.getValueProvider() != null) {
            bindingBuilder.bind(propertyEditorMap.getValueProvider(), propertyEditorMap.getSetter());
        } else {
            bindingBuilder.bind(propertyEditorMap.getFieldName());
        }
//        if (propertyEditorMap.getDataFromRepo()!=null){
//            ((ComboBox)propertyEditorMap.getMappedField()).setItems(propertyEditorMap.getDataFromRepo());
//        }
        //bindingBuilder.bind(propertyEditorMap.getFieldName());
    }
}
