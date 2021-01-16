/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.FileUtils;
import me.razerboy420.weepcraft.value.Value;

public class ValuesFile {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Config.weep");
        for (String s : file) {
            try {
                if (s == null) {
                    return;
                }
                if (s.startsWith("#")) continue;
                String valueName = s.split(":")[0];
                String stringBool = s.split(":")[1];
                boolean bool = Boolean.parseBoolean(stringBool);
                Value val = ValuesFile.find(valueName);
                float fl = 0.0f;
                if (ValuesFile.isFloat(stringBool)) {
                    fl = Float.parseFloat(stringBool);
                }
                if (val != null && val.isaboolean) {
                    val.boolvalue = bool;
                }
                if (val != null && val.isamode) {
                    val.stringvalue = stringBool;
                }
                if (val == null || !val.isafloat) continue;
                val.value = Float.valueOf(fl);
            }
            catch (Exception e) {
                System.out.println("Looks like the values file is empty. Don't worry, this usually means this is the first time starting the game.");
            }
        }
    }

    public static Value find(String name) {
        for (Value v : Value.modes) {
            if (!v.name.equalsIgnoreCase(name)) continue;
            return v;
        }
        return null;
    }

    public static void save() {
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Value val : Value.modes) {
            String name;
            if (val.isaboolean) {
                name = val.name;
                boolean option = val.boolvalue;
                newfile.add(String.valueOf(String.valueOf(name)) + ":" + option);
            }
            if (val.isafloat) {
                name = val.name;
                String option = val.value.toString();
                newfile.add(String.valueOf(String.valueOf(name)) + ":" + option);
            }
            if (!val.isamode) continue;
            name = val.name;
            String option = val.stringvalue;
            newfile.add(String.valueOf(String.valueOf(name)) + ":" + option);
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Config.weep", newfile);
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

