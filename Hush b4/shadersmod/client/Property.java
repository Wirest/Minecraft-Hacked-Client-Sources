// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.Config;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;

public class Property
{
    private int[] values;
    private int defaultValue;
    private String propertyName;
    private String[] propertyValues;
    private String userName;
    private String[] userValues;
    private int value;
    
    public Property(final String propertyName, final String[] propertyValues, final String userName, final String[] userValues, final int defaultValue) {
        this.values = null;
        this.defaultValue = 0;
        this.propertyName = null;
        this.propertyValues = null;
        this.userName = null;
        this.userValues = null;
        this.value = 0;
        this.propertyName = propertyName;
        this.propertyValues = propertyValues;
        this.userName = userName;
        this.userValues = userValues;
        this.defaultValue = defaultValue;
        if (propertyValues.length != userValues.length) {
            throw new IllegalArgumentException("Property and user values have different lengths: " + propertyValues.length + " != " + userValues.length);
        }
        if (defaultValue >= 0 && defaultValue < propertyValues.length) {
            this.value = defaultValue;
            return;
        }
        throw new IllegalArgumentException("Invalid default value: " + defaultValue);
    }
    
    public boolean setPropertyValue(final String propVal) {
        if (propVal == null) {
            this.value = this.defaultValue;
            return false;
        }
        this.value = ArrayUtils.indexOf(this.propertyValues, propVal);
        if (this.value >= 0 && this.value < this.propertyValues.length) {
            return true;
        }
        this.value = this.defaultValue;
        return false;
    }
    
    public void nextValue() {
        ++this.value;
        if (this.value < 0 || this.value >= this.propertyValues.length) {
            this.value = 0;
        }
    }
    
    public void setValue(final int val) {
        this.value = val;
        if (this.value < 0 || this.value >= this.propertyValues.length) {
            this.value = this.defaultValue;
        }
    }
    
    public int getValue() {
        return this.value;
    }
    
    public String getUserValue() {
        return this.userValues[this.value];
    }
    
    public String getPropertyValue() {
        return this.propertyValues[this.value];
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getPropertyName() {
        return this.propertyName;
    }
    
    public void resetValue() {
        this.value = this.defaultValue;
    }
    
    public boolean loadFrom(final Properties props) {
        this.resetValue();
        if (props == null) {
            return false;
        }
        final String s = props.getProperty(this.propertyName);
        return s != null && this.setPropertyValue(s);
    }
    
    public void saveTo(final Properties props) {
        if (props != null) {
            props.setProperty(this.getPropertyName(), this.getPropertyValue());
        }
    }
    
    @Override
    public String toString() {
        return this.propertyName + "=" + this.getPropertyValue() + " [" + Config.arrayToString(this.propertyValues) + "], value: " + this.value;
    }
}
