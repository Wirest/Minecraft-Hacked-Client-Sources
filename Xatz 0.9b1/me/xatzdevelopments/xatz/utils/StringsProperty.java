package me.xatzdevelopments.xatz.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.xatzdevelopments.xatz.event.UpdateValueEvent;
import me.xatzdevelopments.xatz.utils.Parser;

/**
 * @author antja03
 */
public class StringsProperty extends Property<LinkedHashMap<String, Boolean>> {

    private boolean canMultiselect;
    private boolean needsSelected;

    public StringsProperty(String id, String description, me.xatzdevelopments.xatz.utils.Dependency dependency, boolean canMultiselect, boolean needsSelected, String[] keys, Boolean[] defaultvalues) {
        super(id, description, dependency);
        this.canMultiselect = canMultiselect;
        this.needsSelected = needsSelected;
        this.value = new LinkedHashMap<String, Boolean>() {
            {
                for (int index = 0; index < keys.length; index++) {
                    this.put(keys[index], defaultvalues[index]);
                }
            }
        };
    }

    public List<String> getSelectedStrings() {
        List<String> selectedStrings = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : this.value.entrySet()) {
            if (entry.getValue().equals(true)) {
                selectedStrings.add(entry.getKey());
            }
        }
        return selectedStrings;
    }

    public boolean isSelected(String string) {
        for (Map.Entry<String, Boolean> entry : this.value.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(string) && entry.getValue().equals(true)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setValue(String input) {
        LinkedHashMap<String, Boolean> oldValue = getValue();

        String[] entries = input.split(",");
        for (String option : entries) {
            String[] optionSplit = option.split(":");
            String key = optionSplit[0];
            String value = optionSplit[1];

            if (!getValue().containsKey(key))
                continue;

            if (Parser.parseBool(value) == null)
                continue;

            boolean booleanValue = Boolean.valueOf(value);
            if (!booleanValue) {
                if (needsSelected && getSelectedStrings().size() >= 2) {
                    if (this.value.get(key) != booleanValue) {
                        this.value.replace(key, booleanValue);
                    }
                } else if (!needsSelected) {
                    if (this.value.get(key) != booleanValue) {
                        this.value.replace(key, booleanValue);
                    }
                }
            } else if (booleanValue){
                if (this.value.get(key) != booleanValue) {
                    this.value.replace(key, booleanValue);
                }

                if (!canMultiselect) {
                    for (Map.Entry<String, Boolean> entryInValue : this.value.entrySet()) {
                        if (!entryInValue.getKey().equalsIgnoreCase(key) && entryInValue.getValue().equals(true)) {
                            entryInValue.setValue(false);
                        }
                    }
                }
            }
        }

    }

    public void setOption(String input) {
    	LinkedHashMap<String, Boolean> oldValue = getValue();

        String[] splitEntry = input.split(":");
        String key = splitEntry[0];
        if (this.value.keySet().contains(key)) {
            String value = splitEntry[1];

            if (Parser.parseBool(value) == null) return;

            boolean booleanValue = Boolean.valueOf(value);
            if (!booleanValue) {
                if (needsSelected && getSelectedStrings().size() >= 2) {
                    if (this.value.get(key) != booleanValue) {
                        this.value.replace(key, booleanValue);
                    }
                } else if (!needsSelected) {
                    if (this.value.get(key) != booleanValue) {
                        this.value.replace(key, booleanValue);
                    }
                }
            } else if (booleanValue){
                if (this.value.get(key) != booleanValue) {
                    this.value.replace(key, booleanValue);
                }

                if (!canMultiselect) {
                    for (Map.Entry<String, Boolean> entryInValue : this.value.entrySet()) {
                        if (!entryInValue.getKey().equalsIgnoreCase(key) && entryInValue.getValue().equals(true)) {
                            entryInValue.setValue(false);
                        }
                    }
                }
            }
        }

    }

    @Override
    public String getValueAsString() {
        String value = "";
        for (Map.Entry<String, Boolean> entry : getValue().entrySet()) {
            if (value == "") {
                value = entry.getKey() + ":" + String.valueOf(entry.getValue());
            } else {
                value += "," + entry.getKey() + ":" + String.valueOf(entry.getValue());
            }
        }
        return value;
    }

    public boolean canMultiselect() {
        return this.canMultiselect;
    }

    public boolean needsSelected() { return this.needsSelected; }
}
