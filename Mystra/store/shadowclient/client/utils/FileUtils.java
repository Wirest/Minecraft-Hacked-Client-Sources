package store.shadowclient.client.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class FileUtils {
    public static List<String> read(File inputFile) {
        ArrayList<String> readContent = new ArrayList<String>();
        try {
            String str;
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(inputFile), "UTF8"));
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }

    public static void write(File outputFile, List<String> writeContent, boolean overrideContent) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(outputFile), "UTF-8"));
            for (String outputLine : writeContent) {
                out.write(String.valueOf(outputLine) + System.getProperty("line.separator"));
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getConfigDir() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "Sigma");
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

