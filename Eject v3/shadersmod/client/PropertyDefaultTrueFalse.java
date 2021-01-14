package shadersmod.client;

import optifine.Lang;

public class PropertyDefaultTrueFalse
        extends Property {
    public static final String[] PROPERTY_VALUES = {"default", "true", "false"};
    public static final String[] USER_VALUES = {"Default", "ON", "OFF"};

    public PropertyDefaultTrueFalse(String paramString1, String paramString2, int paramInt) {
        super(paramString1, PROPERTY_VALUES, paramString2, USER_VALUES, paramInt);
    }

    public String getUserValue() {
        return isFalse() ? Lang.getOff() : isTrue() ? Lang.getOn() : isDefault() ? Lang.getDefault() : super.getUserValue();
    }

    public boolean isDefault() {
        return getValue() == 0;
    }

    public boolean isTrue() {
        return getValue() == 1;
    }

    public boolean isFalse() {
        return getValue() == 2;
    }
}




