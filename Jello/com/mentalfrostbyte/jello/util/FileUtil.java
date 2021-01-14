package com.mentalfrostbyte.jello.util;



import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.lwjgl.Sys;

public abstract class FileUtil {
    public static List<String> readFile(String file) {
        try {
            return Files.readAllLines(Paths.get(file, new String[0]));
        }
        catch (IOException e) {
            FileUtil.createConfigFile(file);
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public static void writeFile(String file, List<String> newcontent) {
        try {
            FileWriter fw = new FileWriter(file);
            for (String s : newcontent) {
                fw.append(String.valueOf(s) + "\r\n");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(String name) {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir, String.valueOf(name) + ".txt");
            if (!file.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            FileWriter fw = new FileWriter(file);
            fw.append(FileUtil.getDateString());
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createConfigFile(String name) {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir, String.valueOf(name.replace(".txt", "")) + ".txt");
            if (!file.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            FileWriter fw = new FileWriter(file);
            fw.append(FileUtil.getDateString());
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createAccFile(String name) {
        try {
            File file = new File(FileUtils.getConfigDir(), String.valueOf(name) + ".txt");
            if (!file.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            FileWriter fw = new FileWriter(file);
            fw.append(FileUtil.getDateString());
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openFile(File file) {
        FileUtil.openFile(file.getPath());
    }

    public static void openFile(String path) {
        File file = new File(path);
        String apath = file.getAbsolutePath();
        try {
            Desktop.getDesktop().open(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String command = String.format("cmd.exe /C start \"Open file\" \"%s\"", apath);
            try {
                Runtime.getRuntime().exec(command);
                return;
            }
            catch (IOException var4_7) {}
        } else if (Util.getOSType() == Util.EnumOS.OSX) {
            try {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", apath});
                return;
            }
            catch (IOException command) {
                // empty catch block
            }
        }
        boolean browserFailed = false;
        try {
            Desktop.getDesktop().browse(file.toURI());
        }
        catch (Throwable var7) {
            browserFailed = true;
        }
        if (browserFailed) {
            Sys.openURL((String)("file://" + apath));
        }
    }

    public static boolean deleteFile(File file) {
        System.out.println(file.delete());
        return file.delete();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        System.out.println(file.delete());
        return file.delete();
    }

    public static boolean deleteDir(File file) {
        try {
            File[] files = file.listFiles();
            if (files.length > 0) {
                File[] arrfile = files;
                int n = arrfile.length;
                int n2 = 0;
                while (n2 < n) {
                    File f = arrfile[n2];
                    File[] filesmore = f.listFiles();
                    if (filesmore.length > 0) {
                        File[] arrfile2 = filesmore;
                        int n3 = arrfile2.length;
                        int n4 = 0;
                        while (n4 < n3) {
                            File fi = arrfile2[n4];
                            File[] filesmore1 = fi.listFiles();
                            if (filesmore1.length > 0) {
                                File[] arrfile3 = filesmore1;
                                int n5 = arrfile3.length;
                                int n6 = 0;
                                while (n6 < n5) {
                                    File f1 = arrfile3[n6];
                                    f1.delete();
                                    ++n6;
                                }
                            }
                            fi.delete();
                            ++n4;
                        }
                    }
                    f.delete();
                    ++n2;
                }
            }
            return file.delete();
        }
        catch (Exception e) {
            return true;
        }
    }

    public static String getDateString() {
        return "#Last Updated: " + new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + " at " + new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}