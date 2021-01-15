
package me.tireman.hexa.alts;

import java.util.ArrayList;


public class AltManager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;

    static {
        registry = new ArrayList();
    }

    public ArrayList<Alt> getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt2) {
        lastAlt = alt2;
    }
}

