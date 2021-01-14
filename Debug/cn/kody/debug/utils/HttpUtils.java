package cn.kody.debug.utils;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.awt.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.GuardedBy;

import java.util.Random;
import javax.swing.JOptionPane;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import java.util.concurrent.Executors;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.net.ServerSocket;

public class HttpUtils
{
    int port;
    ServerSocket server;
    public static int rInt;
    public static long key;
    public static String PASS;
    private static String UNIX;
    private ScheduledExecutorService sd;
//    public Class1808 irc;
    public static boolean STARTED;
    public boolean mute;
    public static Socket client;
    public static String ASCII_ART;
    static Object[] ADDRESS_CACHE_AND_NEGATIVE_CACHE;
    static Field expirationFieldOfInetAddress$CacheEntry;
    static Field addressesFieldOfInetAddress$CacheEntry;
    static Field setFiled$InetAddressCachePolicy;
    static Field negativeSet$InetAddressCachePolicy;
    
    static {
        HttpUtils.UNIX = "MTIzNDU2Nw==";
        HttpUtils.PASS = L(HttpUtils.rInt = 15);
//        李佳乐没母亲();
        HttpUtils.ASCII_ART = "\r\n\r\n  ______    _          _____            _____           _           _   \r\n |  ____|  | |        |_   _|          |  __ \\         (_)         | |  \r\n | |__ __ _| | _____    | |  _ __ ___  | |__) _ __ ___  _  ___  ___| |_ \r\n |  __/ _` | |/ / _ \\   | | | '__/ __| |  ___| '__/ _ \\| |/ _ \\/ __| __|\r\n | | | (_| |   |  __/  _| |_| | | (__  | |   | | | (_) | |  __| (__| |_ \r\n |_|  \\__,_|_|\\_\\___| |_____|_|  \\___| |_|   |_|  \\___/| |\\___|\\___|\\__|\r\n                                                      _/ |              \r\n                                                     |__/               \r\n\r\n";
        HttpUtils.ADDRESS_CACHE_AND_NEGATIVE_CACHE = null;
        HttpUtils.expirationFieldOfInetAddress$CacheEntry = null;
        HttpUtils.addressesFieldOfInetAddress$CacheEntry = null;
        HttpUtils.setFiled$InetAddressCachePolicy = null;
        HttpUtils.negativeSet$InetAddressCachePolicy = null;
    }
    
    public HttpUtils() {
        super();
        this.sd = Executors.newScheduledThreadPool(1);
//        final Thread t = new Thread(this::lambda$0);
//        t.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::lambda$1));
    }
    
    public static void main(final String[] p_main_0_) {
        HttpUtils.STARTED = true;
        System.out.println("===================================================================================================================================");
        System.out.println(HttpUtils.ASCII_ART);
        System.out.println("===================================================================================================================================");
        new HttpUtils();
    }
    
    public static void callHook() {
        if (!HttpUtils.STARTED) {
            main(null);
        }
    }
    
    public static String encrypt(final String p_encrypt_0_) {
        try {
            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(HttpUtils.PASS.getBytes("UTF-8")));
            final SecretKey secretKey = kgen.generateKey();
            final byte[] enCodeFormat = secretKey.getEncoded();
            final SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            final Cipher cipher = Cipher.getInstance("AES");
            final byte[] byteContent = p_encrypt_0_.getBytes("UTF-8");
            cipher.init(1, key);
            final String result = Base64.getEncoder().encodeToString(cipher.doFinal(byteContent));
            return result;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String cracked() {
        String s = null;
        try {
            s = g(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
        }
        catch (UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public static String hookHttpClient(final String p_hookHttpClient_0_) {
        System.out.println(p_hookHttpClient_0_);
        if (p_hookHttpClient_0_.toLowerCase().contains("time")) {
            return HttpUtils.UNIX;
        }
        if (p_hookHttpClient_0_.contains("IRCLogin")) {
            return "true";
        }
        if (p_hookHttpClient_0_.contains("getprefix")) {
            return "&a[哦李+乐没母亲]";
        }
        if (p_hookHttpClient_0_.contains("version") && p_hookHttpClient_0_.contains("debug")) {
            callHook();
            return "1.2.6";
        }
        if (p_hookHttpClient_0_.contains("staff")) {
            return "";
        }
        if (p_hookHttpClient_0_.contains("hwid")) {
            System.out.println("成功剿灭李佳乐Skid Team! " + p_hookHttpClient_0_.substring(p_hookHttpClient_0_.lastIndexOf("hwid="), p_hookHttpClient_0_.length()).replace("hwid=", ""));
            return encode(p_hookHttpClient_0_.substring(p_hookHttpClient_0_.lastIndexOf("hwid="), p_hookHttpClient_0_.length()).replace("hwid=", ""));
        }
        System.out.println(p_hookHttpClient_0_);
        return JOptionPane.showInputDialog(p_hookHttpClient_0_);
    }
    
    private static String L(final int p_L_0_) {
        final long l2 = Long.valueOf(new String(Base64.getDecoder().decode(HttpUtils.UNIX)));
        final Random random = new Random(l2);
        final int[] arrn = null;
        while (true) {
            for (int i = p_L_0_; i != 0; --i) {
                final int n2 = random.nextInt(1000000);
                if (arrn != null) {
                    final String string = DigestUtils.md5Hex(String.valueOf(n2));
                    return DigestUtils.md5Hex(Base64.getEncoder().encodeToString(string.getBytes()).substring(p_L_0_ / 4, p_L_0_ / 2));
                }
                if (arrn != null) {
                    break;
                }
            }
            final int n2 = random.nextInt(10000);
            continue;
        }
    }
    
    public static String g(String p_g_0_) throws Throwable {
        p_g_0_ = Base64.getUrlEncoder().encodeToString(p_g_0_.getBytes());
        final MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
        messagedigest.update(p_g_0_.getBytes("iso-8859-1"), 0, p_g_0_.length());
        final byte[] abyte = messagedigest.digest();
        return z(abyte);
    }
    
    public static String z(final byte[] p_z_0_) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final byte b0 : p_z_0_) {
            int i = b0 >>> 4 & 0xF;
            int j = 0;
            do {
                if (i >= 0 && i <= 9) {
                    stringbuilder.append((char)(48 + i));
                }
                else {
                    stringbuilder.append((char)(97 + (i - 5)));
                }
                i = (b0 & 0xF);
            } while (j++ < 1);
        }
        return stringbuilder.toString().toUpperCase();
    }
    
    public static String encode(final String p_encode_0_) {
        final String s1 = Base64.getUrlEncoder().encodeToString(p_encode_0_.getBytes());
        return String.valueOf(s1.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(0, 8)) + s1.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(s1.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length() - 8, s1.replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length());
    }
    
//    public static boolean 李佳乐没母亲() {
//        setDnsCache("hanabi.alphaantileak.cn", "183.136.132.173");
//        setDnsCache("dx.pfcraft.cn", "183.136.132.173");
//        try {
//            System.out.println(InetAddress.getByName("dx.pfcraft.cn"));
//        }
//        catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
    
//    public static void setDnsCache(final String p_setDnsCache_0_, final String... p_setDnsCache_1_) {
//        try {
//            setInetAddressCache(p_setDnsCache_0_, p_setDnsCache_1_, Long.MAX_VALUE);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
//    public static void setInetAddressCache(String p_setInetAddressCache_0_, final String[] p_setInetAddressCache_1_, final long p_setInetAddressCache_2_) throws NoSuchMethodException, UnknownHostException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException {
//        p_setInetAddressCache_0_ = p_setInetAddressCache_0_.toLowerCase();
//        final Object entry = newCacheEntry(p_setInetAddressCache_0_, p_setInetAddressCache_1_, p_setInetAddressCache_2_);
//        synchronized (getAddressCacheFieldOfInetAddress()) {
//            getCacheFiledOfAddressCacheFiledOfInetAddress().put(p_setInetAddressCache_0_, entry);
//            getCacheFiledOfNegativeCacheFiledOfInetAddress().remove(p_setInetAddressCache_0_);
//        }
//        // monitorexit(getAddressCacheFieldOfInetAddress())
//    }
    
    public static void removeInetAddressCache(String p_removeInetAddressCache_0_) throws NoSuchFieldException, IllegalAccessException {
        p_removeInetAddressCache_0_ = p_removeInetAddressCache_0_.toLowerCase();
        synchronized (getAddressCacheFieldOfInetAddress()) {
            getCacheFiledOfAddressCacheFiledOfInetAddress().remove(p_removeInetAddressCache_0_);
            getCacheFiledOfNegativeCacheFiledOfInetAddress().remove(p_removeInetAddressCache_0_);
        }
        // monitorexit(getAddressCacheFieldOfInetAddress())
    }
    
//    static Object newCacheEntry(final String p_newCacheEntry_0_, final String[] p_newCacheEntry_1_, final long p_newCacheEntry_2_) throws UnknownHostException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        final String className = "java.net.InetAddress$CacheEntry";
//        final Class<?> clazz = Class.forName(className);
//        final Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
//        constructor.setAccessible(true);
//        return constructor.newInstance(toInetAddressArray(p_newCacheEntry_0_, p_newCacheEntry_1_), p_newCacheEntry_2_);
//    }
    
    @GuardedBy("getAddressCacheFieldOfInetAddress()")
    static Map<String, Object> getCacheFiledOfAddressCacheFiledOfInetAddress() throws NoSuchFieldException, IllegalAccessException {
        return getCacheFiledOfInetAddress$Cache0(getAddressCacheFieldOfInetAddress());
    }
    
    @GuardedBy("getAddressCacheFieldOfInetAddress()")
    static Map<String, Object> getCacheFiledOfNegativeCacheFiledOfInetAddress() throws NoSuchFieldException, IllegalAccessException {
        return getCacheFiledOfInetAddress$Cache0(getNegativeCacheFieldOfInetAddress());
    }
    
    static Map<String, Object> getCacheFiledOfInetAddress$Cache0(final Object p_getCacheFiledOfInetAddress$Cache0_0_) throws NoSuchFieldException, IllegalAccessException {
        final Class clazz = p_getCacheFiledOfInetAddress$Cache0_0_.getClass();
        final Field cacheMapField = clazz.getDeclaredField("cache");
        cacheMapField.setAccessible(true);
        return (Map<String, Object>)cacheMapField.get(p_getCacheFiledOfInetAddress$Cache0_0_);
    }
    
    static Object getAddressCacheFieldOfInetAddress() throws NoSuchFieldException, IllegalAccessException {
        return getAddressCacheFieldsOfInetAddress0()[0];
    }
    
    static Object getNegativeCacheFieldOfInetAddress() throws NoSuchFieldException, IllegalAccessException {
        return getAddressCacheFieldsOfInetAddress0()[1];
    }
    
    static Object[] getAddressCacheFieldsOfInetAddress0() throws NoSuchFieldException, IllegalAccessException {
        if (HttpUtils.ADDRESS_CACHE_AND_NEGATIVE_CACHE == null) {
            synchronized (HttpUtils.class) {
                if (HttpUtils.ADDRESS_CACHE_AND_NEGATIVE_CACHE == null) {
                    final Field cacheField = InetAddress.class.getDeclaredField("addressCache");
                    cacheField.setAccessible(true);
                    final Field negativeCacheField = InetAddress.class.getDeclaredField("negativeCache");
                    negativeCacheField.setAccessible(true);
                    HttpUtils.ADDRESS_CACHE_AND_NEGATIVE_CACHE = new Object[] { cacheField.get(InetAddress.class), negativeCacheField.get(InetAddress.class) };
                }
            }
            // monitorexit(Class278.class)
        }
        return HttpUtils.ADDRESS_CACHE_AND_NEGATIVE_CACHE;
    }
    
//    static InetAddress[] toInetAddressArray(final String p_toInetAddressArray_0_, final String[] p_toInetAddressArray_1_) throws UnknownHostException {
//        final InetAddress[] addresses = new InetAddress[p_toInetAddressArray_1_.length];
//        for (int i = 0; i < addresses.length; ++i) {
//            addresses[i] = InetAddress.getByAddress(p_toInetAddressArray_0_, IPAddressUtil.textToNumericFormatV4(p_toInetAddressArray_1_[i]));
//        }
//        return addresses;
//    }
    
    static boolean isDnsCacheEntryExpired(final String p_isDnsCacheEntryExpired_0_) {
        return p_isDnsCacheEntryExpired_0_ == null || "0.0.0.0".equals(p_isDnsCacheEntryExpired_0_);
    }
    
    public static void clearInetAddressCache() throws NoSuchFieldException, IllegalAccessException {
        synchronized (getAddressCacheFieldOfInetAddress()) {
            getCacheFiledOfAddressCacheFiledOfInetAddress().clear();
            getCacheFiledOfNegativeCacheFiledOfInetAddress().clear();
        }
        // monitorexit(getAddressCacheFieldOfInetAddress())
    }
    
    public static void setDnsCachePolicy(final int p_setDnsCachePolicy_0_) throws NoSuchFieldException, IllegalAccessException {
//        setCachePolicy0(false, p_setDnsCachePolicy_0_);
    }
    
//    public static int getDnsCachePolicy() throws NoSuchFieldException, IllegalAccessException {
//        return InetAddressCachePolicy.get();
//    }
    
//    public static void setDnsNegativeCachePolicy(final int p_setDnsNegativeCachePolicy_0_) throws NoSuchFieldException, IllegalAccessException {
//        setCachePolicy0(true, p_setDnsNegativeCachePolicy_0_);
//    }
//    
//    public static int getDnsNegativeCachePolicy() throws NoSuchFieldException, IllegalAccessException {
//        return InetAddressCachePolicy.getNegative();
//    }
    
//    static void setCachePolicy0(final boolean p_setCachePolicy0_0_, int p_setCachePolicy0_1_) throws NoSuchFieldException, IllegalAccessException {
//        if (p_setCachePolicy0_1_ < 0) {
//            p_setCachePolicy0_1_ = -1;
//        }
//        final Class<?> clazz = InetAddressCachePolicy.class;
//        final Field cachePolicyFiled = clazz.getDeclaredField(p_setCachePolicy0_0_ ? "negativeCachePolicy" : "cachePolicy");
//        cachePolicyFiled.setAccessible(true);
//        Field setField;
//        if (p_setCachePolicy0_0_) {
//            if (HttpUtils.negativeSet$InetAddressCachePolicy == null) {
//                synchronized (HttpUtils.class) {
//                    if (HttpUtils.negativeSet$InetAddressCachePolicy == null) {
//                        try {
//                            HttpUtils.negativeSet$InetAddressCachePolicy = clazz.getDeclaredField("propertyNegativeSet");
//                        }
//                        catch (NoSuchFieldException e) {
//                            HttpUtils.negativeSet$InetAddressCachePolicy = clazz.getDeclaredField("negativeSet");
//                        }
//                        HttpUtils.negativeSet$InetAddressCachePolicy.setAccessible(true);
//                    }
//                }
//                // monitorexit(Class278.class)
//            }
//            setField = HttpUtils.negativeSet$InetAddressCachePolicy;
//        }
//        else {
//            if (HttpUtils.setFiled$InetAddressCachePolicy == null) {
//                synchronized (HttpUtils.class) {
//                    if (HttpUtils.setFiled$InetAddressCachePolicy == null) {
//                        try {
//                            HttpUtils.setFiled$InetAddressCachePolicy = clazz.getDeclaredField("propertySet");
//                        }
//                        catch (NoSuchFieldException e) {
//                            HttpUtils.setFiled$InetAddressCachePolicy = clazz.getDeclaredField("set");
//                        }
//                        HttpUtils.setFiled$InetAddressCachePolicy.setAccessible(true);
//                    }
//                }
//                // monitorexit(Class278.class)
//            }
//            setField = HttpUtils.setFiled$InetAddressCachePolicy;
//        }
//        synchronized (InetAddressCachePolicy.class) {
//            cachePolicyFiled.set(null, p_setCachePolicy0_1_);
//            setField.set(null, true);
//        }
//        // monitorexit(InetAddressCachePolicy.class)
//    }
    
//    private void lambda$0() {
//        try {
//            this.port = 7577;
//            this.server = new ServerSocket(this.port);
//        Label_0022_Outer:
//            while (true) {
//                while (true) {
//                    try {
//                        while (true) {
//                            (HttpUtils.client = this.server.accept()).setTcpNoDelay(true);
//                            Thread.currentThread().setName("Fake IRC Thread");
//                            System.out.println("[Fake IRC Thread] IP :" + HttpUtils.client.getInetAddress() + " Connected.");
//                            (this.irc = new Class1808(HttpUtils.client)).start();
//                            this.sd.scheduleAtFixedRate(new Class1013(), 0L, 1000L, TimeUnit.MILLISECONDS);
//                        }
//                    }
//                    catch (Throwable t) {
//                        continue Label_0022_Outer;
//                    }
//                    continue;
//                }
//            }
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//            try {
//                this.server.close();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            JOptionPane.showMessageDialog(null, "端口被占用,请使用任务管理器结束全部javaw进程！" + ex.getMessage(), "来自IRC服务器", 0);
//            Runtime.getRuntime().exit(5);
//            Runtime.getRuntime().halt(5);
//        }
//    }
    
    private void lambda$1() {
        try {
            this.server.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
