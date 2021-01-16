/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.keybinds;

import java.util.ArrayList;
import me.razerboy420.weepcraft.files.Keybinds;
import me.razerboy420.weepcraft.module.Module;

public class Keybind {
    public Module mod;
    public int key;
    public static ArrayList<Keybind> keybinds = new ArrayList();

    public Keybind(Module mod, int key) {
        this.mod = mod;
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
        Keybinds.save();
    }

    public Module getMod() {
        return this.mod;
    }
}

