// 
// Decompiled by Procyon v0.5.30
// 

package info.sigmaclient.gui.altmanager;

import java.util.ArrayList;

public class AltManager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;

    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }

    public void setLastAlt(final Alt alt) {
        AltManager.lastAlt = alt;
    }

    static {
        AltManager.registry = new ArrayList<Alt>();
    }
}
