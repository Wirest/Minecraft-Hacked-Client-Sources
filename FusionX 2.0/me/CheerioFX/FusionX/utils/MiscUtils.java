// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.io.DataOutputStream;
import java.net.Proxy;
import net.minecraft.server.MinecraftServer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.awt.Component;
import org.lwjgl.Sys;
import net.minecraft.util.Util;
import java.io.IOException;
import java.io.File;
import java.net.URI;
import java.awt.Desktop;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiscUtils
{
    private static final Logger logger;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    public static boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    public static int countMatches(final String string, final String regex) {
        final Matcher matcher = Pattern.compile(regex).matcher(string);
        int count = 0;
        while (matcher.find()) {
            ++count;
        }
        return count;
    }
    
    public static boolean openLink(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
            return true;
        }
        catch (Exception e) {
            MiscUtils.logger.error("Failed to open link", (Throwable)e);
            return false;
        }
    }
    
    public static void openFile(final File file) {
        openFile(file.getPath());
    }
    
    public static void openFile(final String path) {
        final File file = new File(path);
        final String apath = file.getAbsolutePath();
        try {
            Desktop.getDesktop().open(file);
        }
        catch (IOException e) {
            MiscUtils.logger.error("Failed to open file via Desktop.", (Throwable)e);
        }
        Label_0136: {
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                final String command = String.format("cmd.exe /C start \"Open file\" \"%s\"", apath);
                try {
                    Runtime.getRuntime().exec(command);
                    return;
                }
                catch (IOException var8) {
                    MiscUtils.logger.error("Failed to open file", (Throwable)var8);
                    break Label_0136;
                }
            }
            if (Util.getOSType() == Util.EnumOS.OSX) {
                try {
                    MiscUtils.logger.info(apath);
                    Runtime.getRuntime().exec(new String[] { "/usr/bin/open", apath });
                    return;
                }
                catch (IOException var9) {
                    MiscUtils.logger.error("Failed to open file", (Throwable)var9);
                }
            }
        }
        boolean browserFailed = false;
        try {
            Desktop.getDesktop().browse(file.toURI());
        }
        catch (Throwable var10) {
            MiscUtils.logger.error("Failed to open file", var10);
            browserFailed = true;
        }
        if (browserFailed) {
            MiscUtils.logger.info("Opening via system class!");
            Sys.openURL("file://" + apath);
        }
    }
    
    public static void simpleError(final Exception e, final Component parent) {
        final StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        final String message = writer.toString();
        JOptionPane.showMessageDialog(parent, message, "Error", 0);
    }
    
    public static String get(final URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        final BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }
        input.close();
        return buffer.toString();
    }
    
    public static String post(final URL url, final String content) throws IOException {
        return post(url, content, "application/x-www-form-urlencoded");
    }
    
    public static String post(final URL url, final String content, final String contentType) throws IOException {
        Proxy proxy = (MinecraftServer.getServer() == null) ? null : MinecraftServer.getServer().getServerProxy();
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Length", new StringBuilder().append(content.getBytes().length).toString());
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        final DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        output.writeBytes(content);
        output.flush();
        output.close();
        final BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = input.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }
        input.close();
        return buffer.toString();
    }
}
