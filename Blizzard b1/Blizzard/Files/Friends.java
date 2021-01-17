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

import Blizzard.Files.FileManager;
import Blizzard.Friends.FriendManager;

public class Friends
extends FileManager.CustomFile {
    public Friends(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        while ((line = variable9.readLine()) != null) {
            int i2 = line.indexOf(":");
            if (i2 < 0) continue;
            String name = line.substring(0, i2).trim();
            String alias = line.substring(i2 + 1).trim();
            FriendManager.addFriend(name, alias);
        }
        System.out.println("Loaded " + this.getName() + " File!");
        variable9.close();
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (FriendManager.Friend frend : FriendManager.friends) {
            variable9.println(String.valueOf(String.valueOf(frend.getName())) + ":" + frend.getAlias());
        }
        variable9.close();
    }
}

