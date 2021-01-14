/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.value;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Value<T> {
    private T value;
    private T valueMin;
    private T valueMax;
    private double step;
    private final T defaultValue;
    private final String name;
    public boolean isValueBoolean = false;
    public boolean isValueInteger = false;
    public boolean isValueFloat = false;
    public boolean isValueDouble = false;
    public boolean isValueMode;
    public boolean isValueLong = false;
    public boolean isValueByte = false;
    private int current;
    public ArrayList<String> mode;
    public double sliderX;
    public boolean set = false;
    public static final List<Value> list = new ArrayList<Value>();
    public boolean isSettingMode;
    public boolean openMods;
    public double maxSliderSize;
    public final int RADIUS = 4;
    public float currentRadius = 4.0f;
    public boolean disabled;
    private String modeTitle;

    public Value(String classname, String modeTitle, int current) {
        this.defaultValue = this.value;
        this.isValueMode = true;
        this.step = 0.1;
        this.mode = new ArrayList();
        this.current = current;
        this.name = String.valueOf(classname) + "_Mode";
        this.modeTitle = modeTitle;
        list.add(this);
    }

    public Value(String name, T defaultValue, T valueMin, T valueMax) {
        this.defaultValue = this.value;
        this.name = name;
        this.value = defaultValue;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.step = 0.1;
        if (this.value instanceof Double) {
            this.isValueDouble = true;
        }
        list.add(this);
    }

    public Value(String name, T value, T valueMin, T valueMax, double steps) {
        this.defaultValue = value;
        this.name = name;
        this.value = value;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.step = steps;
        if (value instanceof Double) {
            this.isValueDouble = true;
        }
        list.add(this);
    }

    public Value(String name, T value) {
        this.defaultValue = value;
        this.name = name;
        this.value = value;
        if (value instanceof Boolean) {
            this.isValueBoolean = true;
        } else if (value instanceof Integer) {
            this.isValueInteger = true;
        } else if (value instanceof Float) {
            this.isValueFloat = true;
        } else if (value instanceof Long) {
            this.isValueLong = true;
        } else if (value instanceof Byte) {
            this.isValueByte = true;
        }
        list.add(this);
    }

    public Value(String name, String name2, String nam3, T value, T value2, T value3) {
        this.defaultValue = value;
        this.name = name;
        this.value = value;
        if (value instanceof Boolean) {
            this.isValueBoolean = true;
        } else if (value instanceof Integer) {
            this.isValueInteger = true;
        } else if (value instanceof Float) {
            this.isValueFloat = true;
        } else if (value instanceof Double) {
            this.isValueDouble = true;
        } else if (value instanceof Long) {
            this.isValueLong = true;
        } else if (value instanceof Byte) {
            this.isValueByte = true;
        }
        list.add(this);
    }

    public void addValue(String valueName) {
        this.mode.add(valueName);
    }

    public void setCurrentMode(int current) {
        if (current > this.mode.size() - 1) {
            System.out.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
            return;
        }
        this.current = current;
    }

    public int getCurrentMode() {
        return this.current;
    }

    public ArrayList<String> listModes() {
        return this.mode;
    }

    public String getModeTitle() {
        return this.modeTitle;
    }

    public String getModeAt(int index) {
        return this.mode.get(index);
    }

    public String getModeAt(String modeName) {
        for (int i = 0; i < this.mode.size(); ++i) {
            if (!this.mode.get(i).equalsIgnoreCase(modeName)) continue;
            return this.mode.get(i);
        }
        return "NULL";
    }

    public int getModeInt(String modeName) {
        for (int i = 0; i < this.mode.size(); ++i) {
            if (!this.mode.get(i).equalsIgnoreCase(modeName)) continue;
            return i;
        }
        return 0;
    }

    public boolean isCurrentMode(String modeName) {
        return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
    }

    public String getAllModes() {
        String all = "";
        for (int i = 0; i < this.mode.size(); ++i) {
            all = String.valueOf(all) + this.mode.get(i).toString();
        }
        return all;
    }

    public final String getValueName() {
        return this.name;
    }

    public String getDisplayTitle() {
        if (this.isValueMode) {
            return this.getModeTitle();
        }
        return this.getValueName().split("_")[1];
    }

    public final T getValueMin() {
        if (this.value instanceof Double) {
            return this.valueMin;
        }
        return null;
    }

    public final double getSteps() {
        return this.step;
    }

    public final T getValueMax() {
        if (this.value instanceof Double) {
            return this.valueMax;
        }
        return null;
    }

    public final T getDefaultValue() {
        return this.defaultValue;
    }

    public final T getValueState() {
        return this.value;
    }

    public final void setValueState(T value) {
        this.value = value;
    }

    public static Value getBooleanValueByName(String name) {
        for (Value value : list) {
            if (!value.isValueBoolean || !value.getValueName().equalsIgnoreCase(name)) continue;
            return value;
        }
        return null;
    }

    public static Value getDoubleValueByName(String name) {
        for (Value value : list) {
            if (!value.isValueDouble || !value.getValueName().equalsIgnoreCase(name)) continue;
            return value;
        }
        return null;
    }

    public static Value getModeValue(String valueName, String title) {
        for (Value value : list) {
            if (!value.isValueMode || !value.getValueName().equalsIgnoreCase(valueName) || !value.getModeTitle().equalsIgnoreCase(title)) continue;
            return value;
        }
        return null;
    }
}

