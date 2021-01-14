package info.sigmaclient.util;

import java.io.*;
import java.util.*;

import info.sigmaclient.Client;
import net.minecraft.client.Minecraft;

public final class FileUtils {

    public static List<String> read(final File inputFile) {

        final List<String> readContent = new ArrayList<String>();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }

    public static void write(final File outputFile, final List<String> writeContent, final boolean overrideContent) {
        try {
            final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (final String outputLine : writeContent) {
                out.write(String.valueOf(outputLine) + System.getProperty("line.separator"));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getConfigDir() {
        final File file = new File(Minecraft.getMinecraft().mcDataDir, Client.clientName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
    private static File getConfigFolder() {
        final File Sigma = new File(Minecraft.getMinecraft().mcDataDir, Client.clientName);
        final File file = new File(Sigma, "Configs");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
    public static File getConfig(final String name){
    	final File file = new File(getConfigFolder(), String.format("%s.txt", name));
        if (!file.exists()) {
            return null;
        }
        return file;
    }
    public static File getConfigFile(final String name) {
        final File file = new File(getConfigDir(), String.format("%s.txt", name));
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

