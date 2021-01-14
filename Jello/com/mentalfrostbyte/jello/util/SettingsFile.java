package com.mentalfrostbyte.jello.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class SettingsFile {
    public static ArrayList<CustomSettingsFile> SettingFiles = new ArrayList();
    private static File dir = null;
    private static File modDir = null;

    public SettingsFile() {
        if (Util.getOSType() == Util.EnumOS.LINUX) {
        	dir = new File(Minecraft.getMinecraft().mcDataDir, "/Jello/");
            modDir = new File(Minecraft.getMinecraft().mcDataDir, "/Jello/");
        } else {
        	dir = new File(Minecraft.getMinecraft().mcDataDir + "\\Jello\\");
            modDir = new File(Minecraft.getMinecraft().mcDataDir + "\\Jello\\");
        }
        this.makeDirectories();
        SettingFiles.add(new Settings("Config", true, true));
        SettingFiles.add(new Modules("Modules", true, true));
    }

    public void loadFiles() {
        for (CustomSettingsFile f2 : SettingFiles) {
            try {
                if (!f2.loadOnStart()) continue;
                f2.loadFile();
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFiles() {
        for (CustomSettingsFile f2 : SettingFiles) {
            try {
                f2.saveFile();
               
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomSettingsFile getSettingsFile(Class<? extends CustomSettingsFile> clazz) {
        for (CustomSettingsFile file : SettingFiles) {
            if (file.getClass() != clazz) continue;
            return file;
        }
        return null;
    }
    

    public void makeDirectories() {
        if (!dir.exists()) {
            if (dir.mkdir()) {
            } else {
            }
        }
        if (!modDir.exists()) {
            if (modDir.mkdir()) {
            } else {
            }
        }
    }
    public static abstract class CustomSettingsFile {
        private final File file;
        private final String name;
        private boolean load;

        public CustomSettingsFile(String name, boolean Module2, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = Module2 ? new File(modDir, String.valueOf(String.valueOf(name)) + ".txt") : new File(dir, String.valueOf(String.valueOf(name)) + ".txt");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public <T> T getValue(T value) {
            return null;
        }

        public final File getFile() {
            return this.file;
        }

        private boolean loadOnStart() {
            return this.load;
        }

        public final String getName() {
            return this.name;
        }

        public abstract void loadFile() throws IOException;

        public abstract void saveFile() throws IOException;
    }
    

}

