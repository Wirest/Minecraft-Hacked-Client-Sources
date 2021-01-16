/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.util.FileUtils;

public class AltsFile {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.accDir + File.separator + "Accounts.weep");
        for (String s : file) {
            try {
                if (s.startsWith("#")) continue;
                String username = s.split(":")[0];
                String email = s.split(":")[1];
                String password = s.split(":")[2];
                Alt alt = new Alt(username, email, password);
                Alt.alts.add(alt);
            }
            catch (Exception e) {
                System.out.println("Looks like the alts file is empty. Don't worry, this usually means this is the first time starting the game.");
            }
        }
    }

    public static void save() {
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Alt alt : Alt.alts) {
            newfile.add(String.valueOf(String.valueOf(alt.name)) + ":" + alt.email + ":" + alt.password);
        }
        FileUtils.writeFile(Weepcraft.accDir + File.separator + "Accounts.weep", newfile);
    }
}

