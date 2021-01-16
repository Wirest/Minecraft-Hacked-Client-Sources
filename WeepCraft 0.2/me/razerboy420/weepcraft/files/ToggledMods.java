/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.FileUtils;

public class ToggledMods {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "ModuleEnabled.weep");
        System.out.println(String.valueOf(String.valueOf(file.size())) + " mods total");
        for (String s : file) {
            try {
                boolean toggle;
                Module mod;
                String name;
                if (s.startsWith("#") || (mod = Module.getModByAlias(name = s.split(":")[0])) == null || !(toggle = Boolean.valueOf(s.split(":")[1]).booleanValue()) || mod.isToggled()) continue;
                mod.togglenosave();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "ModuleEnabled.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Module m : Weepcraft.getMods()) {
            if (m.getAlias().equalsIgnoreCase("blink")) continue;
            newfile.add(String.valueOf(String.valueOf(m.getAlias())) + ":" + m.isToggled());
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "ModuleEnabled.weep", newfile);
    }
}

