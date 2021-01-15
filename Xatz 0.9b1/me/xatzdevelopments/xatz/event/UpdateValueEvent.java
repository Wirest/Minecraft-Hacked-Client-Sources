package me.xatzdevelopments.xatz.event;

import me.xatzdevelopments.xatz.utils.Property;

/**
 * @author antja03
 */
public class UpdateValueEvent {
    private Property value;

    private Object oldValue;
    private Object newValue;

    public UpdateValueEvent(Property value, Object oldValue, Object newValue) {
        this.value = value;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Property getValue() {
        return value;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
