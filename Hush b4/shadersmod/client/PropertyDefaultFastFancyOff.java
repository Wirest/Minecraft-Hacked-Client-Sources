// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.Config;

public class PropertyDefaultFastFancyOff extends Property
{
    public static final String[] PROPERTY_VALUES;
    public static final String[] USER_VALUES;
    
    static {
        PROPERTY_VALUES = new String[] { "default", "fast", "fancy", "off" };
        USER_VALUES = new String[] { "Default", "Fast", "Fancy", "OFF" };
    }
    
    public PropertyDefaultFastFancyOff(final String propertyName, final String userName, final int defaultValue) {
        super(propertyName, PropertyDefaultFastFancyOff.PROPERTY_VALUES, userName, PropertyDefaultFastFancyOff.USER_VALUES, defaultValue);
    }
    
    public boolean isDefault() {
        return this.getValue() == 0;
    }
    
    public boolean isFast() {
        return this.getValue() == 1;
    }
    
    public boolean isFancy() {
        return this.getValue() == 2;
    }
    
    public boolean isOff() {
        return this.getValue() == 3;
    }
    
    @Override
    public boolean setPropertyValue(String propVal) {
        if (Config.equals(propVal, "none")) {
            propVal = "off";
        }
        return super.setPropertyValue(propVal);
    }
}
