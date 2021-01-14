package cn.kody.debug;

import cn.kody.debug.command.CommandManager;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.ui.ClickGUI.ClickMenu;
import cn.kody.debug.ui.ClickGUI.UIClick;
import cn.kody.debug.ui.font.FontManager;
import cn.kody.debug.utils.file.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

public class Client {
    public static Client instance;
    public static String CLIENT_NAME = "Debug";
    public static String CLIENT_VERSION = "1.2.6";
    public static String CLIENT_FILEMANAGER_FOLDERNAME = "Debug";
    public FileUtils fileMgr;
    public CommandManager cmdMgr;
    public ModManager modMgr;
    public FontManager fontMgr;
    public UIClick crink;
    

    public Client() {
        Display.setTitle(CLIENT_NAME + " v" + CLIENT_VERSION);
        instance = this;
        this.modMgr = new ModManager();
        this.fontMgr = new FontManager();
        this.fileMgr = new FileUtils();
        this.cmdMgr = new CommandManager();
        this.crink = new UIClick();
    }
    
    
    public static String sendGet(String url, String param) throws Exception {
        String line;
        String result = "";
        BufferedReader in = null;
        InetSocketAddress is22323232322 = new InetSocketAddress("hanabi.alphaantileak.cn", 893);
        if (!is22323232322.getAddress().getHostAddress().equals("183.136.132.173")) {
            Minecraft.getMinecraft().shutdownMinecraftApplet();
        }
        if (!ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].contains("\\lib\\") || ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].replace("l", "I").contains("\\lib\\")) {
            Minecraft.getMinecraft().shutdownMinecraftApplet();
        }
        Properties properties = System.getProperties();
        Set set = properties.entrySet();
//        for (Map.Entry objectObjectEntry : set) {
//            if (!objectObjectEntry.getKey().toString().contains("http.proxy")) continue;
//            Minecraft.getMinecraft().shutdownMinecraftApplet();
//        }
        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        connection.setDoOutput(true);
        connection.setReadTimeout(99781);
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        connection.connect();
        Map<String, List<String>> map = connection.getHeaderFields();
        for (String line2 : map.keySet()) {
        }
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = in.readLine()) != null) {
            result = result + line;
        }
        try {
            in.close();
            return result;
        }
        catch (Exception e) {
            return result;
        }
    }

    public static String sigma() {
        String hwid = null;
        try {
            hwid = Client.hug(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name") + OpenGlHelper.getCpu());
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
        return hwid;
    }

    private static String hug(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return Client.zerodayisaminecraftcheat(sha1hash);
    }

    private static String zerodayisaminecraftcheat(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 15;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = data[i] & 15;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String encode(String string) {
        String retObj = Base64.getUrlEncoder().encodeToString(string.getBytes());
        return retObj.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(0, 8) + retObj.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(retObj.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length() - 8, retObj.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length());
    }

    public static String sendPost(String url, String param) throws UnsupportedEncodingException, IOException {
        return Client.sendPost(url, param, null);
    }

    public static String sendPost(String url, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        String line;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        URLConnection conn = realUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        out = new PrintWriter(conn.getOutputStream());
        out.print(param);
        out.flush();
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf8"));
        while ((line = in.readLine()) != null) {
            result = result + line;
        }
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        return result;
    }

}

