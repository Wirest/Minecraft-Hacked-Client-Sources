package me.rigamortis.faurax.values;

public class Value<T>
{
    private String name;
    private float minFloatValue;
    private float floatValue;
    private float maxFloat;
    private boolean booleanValue;
    private String stringValue;
    private int minIntValue;
    private int intValue;
    private int maxInt;
    private String[] options;
    private String selectedOption;
    private String valType;
    private T type;
    
    public Value(final String valType, final T type, final String name, final float value, final float minValue, final float maxValue) {
        this.setName(name);
        this.setType(type);
        this.setValType(valType);
        this.setFloatValue(value);
        this.setMinFloatValue(minValue);
        this.setMaxFloat(maxValue);
    }
    
    public Value(final String valType, final T type, final String name, final String selected, final String[] options) {
        this.setName(name);
        this.setSelectedOption(selected);
        this.setOptions(options);
        this.setType(type);
        this.setValType(valType);
    }
    
    public Value(final String valType, final T type, final String name, final int value, final int minValue, final int maxValue) {
        this.setName(name);
        this.setIntValue(value);
        this.setMinIntValue(minValue);
        this.setMaxInt(maxValue);
        this.setType(type);
        this.setValType(valType);
    }
    
    public Value(final String valType, final T type, final String name, final boolean value) {
        this.setName(name);
        this.setBooleanValue(value);
        this.setType(type);
        this.setValType(valType);
    }
    
    public Value(final T type, final String name, final String value) {
        this.setName(name);
        this.setStringValue(value);
        this.setType(type);
        this.setValType(this.valType);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public float getFloatValue() {
        return this.floatValue;
    }
    
    public void setFloatValue(final float floatValue) {
        this.floatValue = floatValue;
    }
    
    public boolean getBooleanValue() {
        return this.booleanValue;
    }
    
    public void setBooleanValue(final boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
    
    public String getStringValue() {
        return this.stringValue;
    }
    
    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }
    
    public float getMaxFloat() {
        return this.maxFloat;
    }
    
    public void setMaxFloat(final float maxFloat) {
        this.maxFloat = maxFloat;
    }
    
    public int getIntValue() {
        return this.intValue;
    }
    
    public void setIntValue(final int intValue) {
        this.intValue = intValue;
    }
    
    public int getMaxInt() {
        return this.maxInt;
    }
    
    public void setMaxInt(final int maxInt) {
        this.maxInt = maxInt;
    }
    
    public float getMinFloatValue() {
        return this.minFloatValue;
    }
    
    public void setMinFloatValue(final float minFloatValue) {
        this.minFloatValue = minFloatValue;
    }
    
    public int getMinIntValue() {
        return this.minIntValue;
    }
    
    public void setMinIntValue(final int minIntValue) {
        this.minIntValue = minIntValue;
    }
    
    public String[] getOptions() {
        return this.options;
    }
    
    public void setOptions(final String[] options) {
        this.options = options;
    }
    
    public String getSelectedOption() {
        return this.selectedOption;
    }
    
    public void setSelectedOption(final String selectedOption) {
        this.selectedOption = selectedOption;
    }
    
    public T getType() {
        return this.type;
    }
    
    public void setType(final T type) {
        this.type = type;
    }
    
    public String getValType() {
        return this.valType;
    }
    
    public void setValType(final String valType) {
        this.valType = valType;
    }
}
