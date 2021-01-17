// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.main;

import com.google.gson.Gson;
import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.util.Session;
import joptsimple.OptionSpec;
import java.lang.reflect.Type;
import com.mojang.authlib.properties.PropertyMap;
import com.google.gson.GsonBuilder;
import java.net.PasswordAuthentication;
import java.net.Authenticator;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import java.io.File;
import joptsimple.OptionParser;

public class Main
{
    public static void main(final String[] p_main_0_) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        final OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        final OptionSpec<String> optionspec = optionparser.accepts("server").withRequiredArg();
        final OptionSpec<Integer> optionspec2 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565, new Integer[0]);
        final OptionSpec<File> optionspec3 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
        final OptionSpec<File> optionspec4 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        final OptionSpec<File> optionspec5 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        final OptionSpec<String> optionspec6 = optionparser.accepts("proxyHost").withRequiredArg();
        final OptionSpec<Integer> optionspec7 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        final OptionSpec<String> optionspec8 = optionparser.accepts("proxyUser").withRequiredArg();
        final OptionSpec<String> optionspec9 = optionparser.accepts("proxyPass").withRequiredArg();
        final OptionSpec<String> optionspec10 = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
        final OptionSpec<String> optionspec11 = optionparser.accepts("uuid").withRequiredArg();
        final OptionSpec<String> optionspec12 = optionparser.accepts("accessToken").withRequiredArg().required();
        final OptionSpec<String> optionspec13 = optionparser.accepts("version").withRequiredArg().required();
        final OptionSpec<Integer> optionspec14 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854, new Integer[0]);
        final OptionSpec<Integer> optionspec15 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480, new Integer[0]);
        final OptionSpec<String> optionspec16 = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}", new String[0]);
        final OptionSpec<String> optionspec17 = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", new String[0]);
        final OptionSpec<String> optionspec18 = optionparser.accepts("assetIndex").withRequiredArg();
        final OptionSpec<String> optionspec19 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
        final OptionSpec<String> optionspec20 = optionparser.nonOptions();
        final OptionSet optionset = optionparser.parse(p_main_0_);
        final List<String> list = optionset.valuesOf(optionspec20);
        if (!list.isEmpty()) {
            System.out.println("Completely ignored arguments: " + list);
        }
        final String s = optionset.valueOf(optionspec6);
        Proxy proxy = Proxy.NO_PROXY;
        if (s != null) {
            try {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(s, optionset.valueOf(optionspec7)));
            }
            catch (Exception ex) {}
        }
        final String s2 = optionset.valueOf(optionspec8);
        final String s3 = optionset.valueOf(optionspec9);
        if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(s2) && isNullOrEmpty(s3)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(s2, s3.toCharArray());
                }
            });
        }
        final int i = optionset.valueOf(optionspec14);
        final int j = optionset.valueOf(optionspec15);
        final boolean flag = optionset.has("fullscreen");
        final boolean flag2 = optionset.has("checkGlErrors");
        final boolean flag3 = optionset.has("demo");
        final String s4 = optionset.valueOf(optionspec13);
        final Gson gson = new GsonBuilder().registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
        final PropertyMap propertymap = gson.fromJson(optionset.valueOf(optionspec16), PropertyMap.class);
        final PropertyMap propertymap2 = gson.fromJson(optionset.valueOf(optionspec17), PropertyMap.class);
        final File file1 = optionset.valueOf(optionspec3);
        final File file2 = optionset.has(optionspec4) ? optionset.valueOf(optionspec4) : new File(file1, "assets/");
        final File file3 = optionset.has(optionspec5) ? optionset.valueOf(optionspec5) : new File(file1, "resourcepacks/");
        final String s5 = optionset.has(optionspec11) ? optionspec11.value(optionset) : optionspec10.value(optionset);
        final String s6 = optionset.has(optionspec18) ? optionspec18.value(optionset) : null;
        final String s7 = optionset.valueOf(optionspec);
        final Integer integer = optionset.valueOf(optionspec2);
        final Session session = new Session(optionspec10.value(optionset), s5, optionspec12.value(optionset), optionspec19.value(optionset));
        final GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap2, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag2), new GameConfiguration.FolderInformation(file1, file3, file2, s6), new GameConfiguration.GameInformation(flag3, s4), new GameConfiguration.ServerInformation(s7, integer));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            @Override
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(gameconfiguration).run();
    }
    
    private static boolean isNullOrEmpty(final String str) {
        return str != null && !str.isEmpty();
    }
}
