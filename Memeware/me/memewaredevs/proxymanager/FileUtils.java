/*
 * Decompiled with CFR 0_122.
 */
package me.memewaredevs.proxymanager;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
    public static List<String> read(File inputFile) {
        ArrayList<String> readContent = new ArrayList<String>();
        try {
            String str;
            while ((str = new BufferedReader(new InputStreamReader((InputStream) new FileInputStream(inputFile), "UTF8")).readLine()) != null) {
                readContent.add(str);
            }
            new BufferedReader(new InputStreamReader((InputStream) new FileInputStream(inputFile), "UTF8")).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }

    public static void write(File outputFile, List<String> writeContent, boolean overrideContent) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			for (String outputLine : writeContent) {
                new BufferedWriter(new OutputStreamWriter((OutputStream) fileOutputStream, "UTF-8")).write(String.valueOf(outputLine) + System.getProperty("line.separator"));
            }
			fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getConfigDir() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "MemeClient");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getConfigFile(String name) {
        File file = new File(FileUtils.getConfigDir(), String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

