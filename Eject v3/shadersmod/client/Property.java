package shadersmod.client;

import optifine.Config;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Properties;

public class Property {
    private int[] values = null;
    private int defaultValue = 0;
    private String propertyName = null;
    private String[] propertyValues = null;
    private String userName = null;
    private String[] userValues = null;
    private int value = 0;

    public Property(String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, int paramInt) {
        this.propertyName = paramString1;
        this.propertyValues = paramArrayOfString1;
        this.userName = paramString2;
        this.userValues = paramArrayOfString2;
        this.defaultValue = paramInt;
        if (paramArrayOfString1.length != paramArrayOfString2.length) {
            throw new IllegalArgumentException("Property and user values have different lengths: " + paramArrayOfString1.length + " != " + paramArrayOfString2.length);
        }
        if ((paramInt >= 0) && (paramInt < paramArrayOfString1.length)) {
            this.value = paramInt;
        } else {
            throw new IllegalArgumentException("Invalid default value: " + paramInt);
        }
    }

    public boolean setPropertyValue(String paramString) {
        if (paramString == null) {
            this.value = this.defaultValue;
            return false;
        }
        this.value = ArrayUtils.indexOf(this.propertyValues, paramString);
        if ((this.value >= 0) && (this.value < this.propertyValues.length)) {
            return true;
        }
        this.value = this.defaultValue;
        return false;
    }

    public void nextValue() {
        this.value |= 0x1;
        if ((this.value < 0) || (this.value >= this.propertyValues.length)) {
            this.value = 0;
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int paramInt) {
        this.value = paramInt;
        if ((this.value < 0) || (this.value >= this.propertyValues.length)) {
            this.value = this.defaultValue;
        }
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

    public boolean loadFrom(Properties paramProperties) {
        resetValue();
        if (paramProperties == null) {
            return false;
        }
        String str = paramProperties.getProperty(this.propertyName);
        return str == null ? false : setPropertyValue(str);
    }

    public void saveTo(Properties paramProperties) {
        if (paramProperties != null) {
            paramProperties.setProperty(getPropertyName(), getPropertyValue());
        }
    }

    public String toString() {
        return "" + this.propertyName + "=" + getPropertyValue() + " [" + Config.arrayToString((Object[]) this.propertyValues) + "], value: " + this.value;
    }
}




