/*
 * Decompiled with CFR 0_114.
 */
package info.sigmaclient.gui.altmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.ssl.internal.ssl.Provider;
import info.sigmaclient.Client;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.util.security.HTTPUtils;
import info.sigmaclient.util.security.SSLUtilities;
import net.minecraft.client.Minecraft;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class FileManager {
    public static ArrayList<CustomFile> Files = new ArrayList();
    private static File directory = new File(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString()) + File.separator + Client.clientName);

    public FileManager() {
        this.makeDirectories();
        Files.add(new Alts("alts", false, true));
    }

    public void loadFiles() {
        for (CustomFile f : Files) {
            try {
                if (!f.loadOnStart()) continue;
                f.loadFile();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFiles() {
        for (CustomFile f : Files) {
            try {
                f.saveFile();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class<? extends CustomFile> clazz) {
        for (CustomFile file : Files) {
            if (file.getClass() != clazz) continue;
            return file;
        }
        return null;
    }

    public void makeDirectories() {
        try {
            //TODO: VERSION CHECK
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public static abstract class CustomFile {
        private final File file;
        private final String name;
        private boolean load;

        public CustomFile(String name, boolean Module2, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(directory, String.valueOf(name) + ".txt");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

