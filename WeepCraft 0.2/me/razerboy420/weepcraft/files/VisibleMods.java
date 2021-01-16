/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.FileUtils;

public class VisibleMods {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Visible.weep");
        for (String s : file) {
            String name;
            Module mod;
            if (s.startsWith("#") || (mod = Module.getModByAlias(name = s.split(":")[0])) == null) continue;
            boolean toggle = Boolean.valueOf(s.split(":")[1]);
            mod.setVisible(toggle);
        }
    }

    public static void save() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Visible.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Module m : Weepcraft.getMods()) {
            newfile.add(String.valueOf(String.valueOf(m.getAlias())) + ":" + m.isVisible());
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Visible.weep", newfile);
    }

    public static void resetBinds() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Visible.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        for (Module m : Weepcraft.getMods()) {
            newfile.add(String.valueOf(String.valueOf(m.getAlias())) + ":" + m.isToggled());
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Visible.weep", newfile);
    }

    public static void bindKey(Module mod, boolean toggle) {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Visible.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        boolean exists = false;
        Iterator<String> iterator = file.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            String modname = s.split(":")[0];
            if (modname.equalsIgnoreCase(mod.getAlias())) {
                exists = true;
                s = String.valueOf(String.valueOf(mod.getAlias())) + ":" + toggle;
            }
            newfile.add(s);
        }
        if (!exists) {
            newfile.add(String.valueOf(String.valueOf(mod.getAlias())) + ":" + toggle);
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Visible.weep", newfile);
        VisibleMods.save();
    }
}

