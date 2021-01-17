/*
 * Decompiled with CFR 0_122.
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
import java.util.ArrayList;

import Blizzard.Alts.Alt;
import Blizzard.Alts.AltManager;
import Blizzard.Files.FileManager;

public class Alts
extends FileManager.CustomFile {
    public Alts(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        while ((line = variable9.readLine()) != null) {
            String[] arguments = line.split(":");
            int i2 = 0;
            while (i2 < 2) {
                arguments[i2].replace(" ", "");
                ++i2;
            }
            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2]));
                continue;
            }
            AltManager.registry.add(new Alt(arguments[0], arguments[1]));
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
        for (Alt alt2 : AltManager.registry) {
            if (alt2.getMask().equals("")) {
                alts.println(String.valueOf(String.valueOf(alt2.getUsername())) + ":" + alt2.getPassword());
                continue;
            }
            alts.println(String.valueOf(String.valueOf(alt2.getUsername())) + ":" + alt2.getPassword() + ":" + alt2.getMask());
        }
        alts.close();
    }
}

