package me.xatzdevelopments.xatz.utils;

/**
 * @author antja03
 */
public class Parser {

    private static final String validBool[][] = {
            {"on","true"},
            {"off", "false"},
            {"enabled", "true"},
            {"disabled", "false"},
            {"1", "true"},
            {"0", "false"},
            {"true", "true"},
            {"false", "false"}
    };

    public static int getBoolIndex(String stringValue) {
        for (int i = 0; i < validBool.length; i++) {
            String[] vals = validBool[i];
            if (vals[0].equalsIgnoreCase(stringValue)) {
                return i;
            }
        }
        return -1;
    }

    public static Boolean parseBool(String stringValue) {
        int index = getBoolIndex(stringValue);
        if (index != -1) {
            return Boolean.valueOf(validBool[index][1]);
        }
        return null;
    }

}
