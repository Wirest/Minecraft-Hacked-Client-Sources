/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package Blizzard.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import org.lwjgl.input.Keyboard;

import Blizzard.Blizzard;
import Blizzard.Files.FileManager;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;

public class Binds
extends FileManager.CustomFile {
    public Binds(String name, boolean Mod2, boolean loadOnStart) {
        super(name, Mod2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        while ((line = variable9.readLine()) != null) {
            int i2 = line.indexOf(":");
            if (i2 < 0) continue;
            String Mod2 = line.substring(0, i2).trim();
            String key = line.substring(i2 + 1).trim();
            Blizzard.getInstance();
            Mod m2 = Blizzard.modManager.getModbyName(Mod2);
            m2.setKey(Keyboard.getKeyIndex((String)key.toUpperCase()));
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (Mod m2 : ModManager.getMods()) {
            variable9.println(String.valueOf(String.valueOf(String.valueOf(m2.getName()))) + ":" + Keyboard.getKeyName((int)m2.getKey()));
        }
        variable9.close();
    }
}

