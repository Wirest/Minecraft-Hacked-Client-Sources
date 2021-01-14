package com.etb.client.utils.value;

public abstract class Value<O> {
	
    protected O value;
    private O defaultValue;
    private String label;
    
    public Value(String label, O value) {
        this.value = value;
        this.defaultValue = value;
        this.label = label;
    }
    public O getValue() {
    	return this.value;
    }
    public O getDefaultValue() {
    	return this.defaultValue;
    }
    public String getLabel() {
    	return this.label;
    }
    public void setValue(O value) {
        this.value = value;
    }
    
    public abstract void setValue(String value);
}
