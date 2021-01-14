package shadersmod.client;

import optifine.Config;

public class PropertyDefaultFastFancyOff
        extends Property {
    public static final String[] PROPERTY_VALUES = {"default", "fast", "fancy", "off"};
    public static final String[] USER_VALUES = {"Default", "Fast", "Fancy", "OFF"};

    public PropertyDefaultFastFancyOff(String paramString1, String paramString2, int paramInt) {
        super(paramString1, PROPERTY_VALUES, paramString2, USER_VALUES, paramInt);
    }

    public boolean isDefault() {
        return getValue() == 0;
    }

    public boolean isFast() {
        return getValue() == 1;
    }

    public boolean isFancy() {
        return getValue() == 2;
    }

    public boolean isOff() {
        return getValue() == 3;
    }

    public boolean setPropertyValue(String paramString) {
        if (Config.equals(paramString, "none")) {
            paramString = "off";
        }
        return super.setPropertyValue(paramString);
    }
}




