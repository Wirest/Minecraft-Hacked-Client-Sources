package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Main {
   public static void main(String[] p_main_0_) {
      System.setProperty("java.net.preferIPv4Stack", "true");
      OptionParser optionparser = new OptionParser();
      optionparser.allowsUnrecognizedOptions();
      optionparser.accepts("demo");
      optionparser.accepts("fullscreen");
      optionparser.accepts("checkGlErrors");
      OptionSpec optionspec = optionparser.accepts("server").withRequiredArg();
      OptionSpec optionspec1 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565, new Integer[0]);
      OptionSpec optionspec2 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
      OptionSpec optionspec3 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
      OptionSpec optionspec4 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
      OptionSpec optionspec5 = optionparser.accepts("proxyHost").withRequiredArg();
      OptionSpec optionspec6 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
      OptionSpec optionspec7 = optionparser.accepts("proxyUser").withRequiredArg();
      OptionSpec optionspec8 = optionparser.accepts("proxyPass").withRequiredArg();
      OptionSpec optionspec9 = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
      OptionSpec optionspec10 = optionparser.accepts("uuid").withRequiredArg();
      OptionSpec optionspec11 = optionparser.accepts("accessToken").withRequiredArg().required();
      OptionSpec optionspec12 = optionparser.accepts("version").withRequiredArg().required();
      OptionSpec optionspec13 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854, new Integer[0]);
      OptionSpec optionspec14 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480, new Integer[0]);
      OptionSpec optionspec15 = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}", new String[0]);
      OptionSpec optionspec16 = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", new String[0]);
      OptionSpec optionspec17 = optionparser.accepts("assetIndex").withRequiredArg();
      OptionSpec optionspec18 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
      OptionSpec optionspec19 = optionparser.nonOptions();
      OptionSet optionset = optionparser.parse(p_main_0_);
      List list = optionset.valuesOf(optionspec19);
      if (!list.isEmpty()) {
         System.out.println("Completely ignored arguments: " + list);
      }

      String s = (String)optionset.valueOf(optionspec5);
      Proxy proxy = Proxy.NO_PROXY;
      if (s != null) {
         try {
            proxy = new Proxy(Type.SOCKS, new InetSocketAddress(s, (Integer)optionset.valueOf(optionspec6)));
         } catch (Exception var46) {
         }
      }

      final String s1 = (String)optionset.valueOf(optionspec7);
      final String s2 = (String)optionset.valueOf(optionspec8);
      if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(s1) && isNullOrEmpty(s2)) {
         Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(s1, s2.toCharArray());
            }
         });
      }

      int i = (Integer)optionset.valueOf(optionspec13);
      int j = (Integer)optionset.valueOf(optionspec14);
      boolean flag = optionset.has("fullscreen");
      boolean flag1 = optionset.has("checkGlErrors");
      boolean flag2 = optionset.has("demo");
      String s3 = (String)optionset.valueOf(optionspec12);
      Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();
      PropertyMap propertymap = (PropertyMap)gson.fromJson((String)optionset.valueOf(optionspec15), PropertyMap.class);
      PropertyMap propertymap1 = (PropertyMap)gson.fromJson((String)optionset.valueOf(optionspec16), PropertyMap.class);
      File file1 = (File)optionset.valueOf(optionspec2);
      File file2 = optionset.has(optionspec3) ? (File)optionset.valueOf(optionspec3) : new File(file1, "assets/");
      File file3 = optionset.has(optionspec4) ? (File)optionset.valueOf(optionspec4) : new File(file1, "resourcepacks/");
      String s4 = optionset.has(optionspec10) ? (String)optionspec10.value(optionset) : (String)optionspec9.value(optionset);
      String s5 = optionset.has(optionspec17) ? (String)optionspec17.value(optionset) : null;
      String s6 = (String)optionset.valueOf(optionspec);
      Integer integer = (Integer)optionset.valueOf(optionspec1);
      Session session = new Session((String)optionspec9.value(optionset), s4, (String)optionspec11.value(optionset), (String)optionspec18.value(optionset));
      GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag1), new GameConfiguration.FolderInformation(file1, file3, file2, s5), new GameConfiguration.GameInformation(flag2, s3), new GameConfiguration.ServerInformation(s6, integer));
      Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
         public void run() {
            Minecraft.stopIntegratedServer();
         }
      });
      Thread.currentThread().setName("Client thread");
      (new Minecraft(gameconfiguration)).run();
   }

   private static boolean isNullOrEmpty(String str) {
      return str != null && !str.isEmpty();
   }
}
