/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Alts;

import java.util.ArrayList;

import Blizzard.Alts.Alt;

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

