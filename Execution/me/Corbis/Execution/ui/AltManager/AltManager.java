package me.Corbis.Execution.ui.AltManager;

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
