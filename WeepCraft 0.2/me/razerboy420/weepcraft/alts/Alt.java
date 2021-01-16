/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.alts;

import java.util.ArrayList;
import net.minecraft.util.Session;

public class Alt {
    public String name;
    public String email;
    public String password;
    public static ArrayList<Alt> alts = new ArrayList();
    public static Session savedsession;

    public Alt(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isCracked() {
        return this.password.equalsIgnoreCase("[NULL]");
    }

    public boolean isMigrated() {
        return !this.email.equalsIgnoreCase("[NULL]");
    }
}

