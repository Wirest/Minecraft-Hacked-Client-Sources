package dev.astroclient.client.property.impl;

import dev.astroclient.client.property.Property;
import dev.astroclient.client.util.GlueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zane for PublicBase
 * @since 10/24/19
 */

public class MultiSelectableProperty extends Property {

    public List<String> getSelectedObjects() {
        return selectedObjects;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        if (selectedObjects != objects)
            this.selectedObjects = objects;
    }

    public boolean isSelected(String object) {
        return selectedObjects.contains(object);
    }

    public void addObject(String object) {
        if (objects.contains(object))
            if (!selectedObjects.contains(object))
                this.selectedObjects.add(object);
            else if ((selectedObjects.contains(object)))
                this.selectedObjects.remove(object);
    }

    public void removeObject(String object) {
        if (selectedObjects.contains(object))
            this.selectedObjects.remove(object);
    }

    private List<String> objects, selectedObjects;

    public MultiSelectableProperty(String name, boolean dependency, String[] selectedObjects, String[] objects) {
        super(name, dependency);
        this.objects = new ArrayList<>(Arrays.asList((objects)));
        this.selectedObjects = new ArrayList<>(Arrays.asList((selectedObjects)));
    }
}
