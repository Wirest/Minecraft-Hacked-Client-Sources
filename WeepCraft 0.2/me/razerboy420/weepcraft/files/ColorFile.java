/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.FileUtils;

public class ColorFile {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Colors.weep");
        ArrayList<String> s1 = new ArrayList<String>();
        for (String s : file) {
            s1.add(s.split(":")[1]);
        }
        Weepcraft.normalColor = ColorFile.getColorViaName((String)s1.get(1));
        Weepcraft.enabledColor = ColorFile.getColorViaName((String)s1.get(2));
        Weepcraft.disabledColor = ColorFile.getColorViaName((String)s1.get(3));
        Weepcraft.primaryColor = ColorFile.getColorViaName((String)s1.get(4));
        Weepcraft.secondaryColor = ColorFile.getColorViaName((String)s1.get(5));
        Weepcraft.panlColor = ColorFile.getColorViaName((String)s1.get(6));
        Weepcraft.borderColor = ColorFile.getColorViaName((String)s1.get(7));
    }

    public static EnumColor getColorViaName(String name) {
        EnumColor[] arrenumColor = EnumColor.values();
        int n = arrenumColor.length;
        int n2 = 0;
        while (n2 < n) {
            EnumColor c = arrenumColor[n2];
            if (c.name().equalsIgnoreCase(name)) {
                return c;
            }
            ++n2;
        }
        return null;
    }

    public static void save() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Colors.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        newfile.add("normalColor:" + Weepcraft.normalColor.name());
        newfile.add("enabledColor:" + Weepcraft.enabledColor.name());
        newfile.add("disabledColor:" + Weepcraft.disabledColor.name());
        newfile.add("primaryColor:" + Weepcraft.primaryColor.name());
        newfile.add("secondaryColor:" + Weepcraft.secondaryColor.name());
        newfile.add("panelColor:" + Weepcraft.panlColor.name());
        newfile.add("borderColor:" + Weepcraft.borderColor.name());
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "Colors.weep", newfile);
    }
}

