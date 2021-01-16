/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.FileUtils;
import org.lwjgl.input.Keyboard;

public class Keybinds {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Keybinds.weep");
        for (String s : file) {
            String name;
            Module mod;
            if (s.startsWith("#") || (mod = Module.getModByAlias(name = s.split(":")[0])) == null) continue;
            Integer bind = Keyboard.getKeyIndex((String)s.split(":")[1]);
            Weepcraft.keybinds.add(new Keybind(mod, bind));
        }
    }

    public static void save() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Keybinds.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Keybind k : Weepcraft.keybinds) {
            newfile.add(String.valueOf(String.valueOf(k.getMod().getAlias())) + ":" + Keyboard.getKeyName((int)k.getKey()));
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Keybinds.weep", newfile);
    }
}

