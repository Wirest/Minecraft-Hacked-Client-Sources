/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.util.List;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.Session;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public static void main(String[] p_main_0_)
/*     */   {
/*  24 */     System.setProperty("java.net.preferIPv4Stack", "true");
/*  25 */     OptionParser optionparser = new OptionParser();
/*  26 */     optionparser.allowsUnrecognizedOptions();
/*  27 */     optionparser.accepts("demo");
/*  28 */     optionparser.accepts("fullscreen");
/*  29 */     optionparser.accepts("checkGlErrors");
/*  30 */     OptionSpec<String> optionspec = optionparser.accepts("server").withRequiredArg();
/*  31 */     OptionSpec<Integer> optionspec1 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), new Integer[0]);
/*  32 */     OptionSpec<File> optionspec2 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
/*  33 */     OptionSpec<File> optionspec3 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
/*  34 */     OptionSpec<File> optionspec4 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
/*  35 */     OptionSpec<String> optionspec5 = optionparser.accepts("proxyHost").withRequiredArg();
/*  36 */     OptionSpec<Integer> optionspec6 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
/*  37 */     OptionSpec<String> optionspec7 = optionparser.accepts("proxyUser").withRequiredArg();
/*  38 */     OptionSpec<String> optionspec8 = optionparser.accepts("proxyPass").withRequiredArg();
/*  39 */     OptionSpec<String> optionspec9 = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
/*  40 */     OptionSpec<String> optionspec10 = optionparser.accepts("uuid").withRequiredArg();
/*  41 */     OptionSpec<String> optionspec11 = optionparser.accepts("accessToken").withRequiredArg().required();
/*  42 */     OptionSpec<String> optionspec12 = optionparser.accepts("version").withRequiredArg().required();
/*  43 */     OptionSpec<Integer> optionspec13 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), new Integer[0]);
/*  44 */     OptionSpec<Integer> optionspec14 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), new Integer[0]);
/*  45 */     OptionSpec<String> optionspec15 = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}", new String[0]);
/*  46 */     OptionSpec<String> optionspec16 = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", new String[0]);
/*  47 */     OptionSpec<String> optionspec17 = optionparser.accepts("assetIndex").withRequiredArg();
/*  48 */     OptionSpec<String> optionspec18 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
/*  49 */     OptionSpec<String> optionspec19 = optionparser.nonOptions();
/*  50 */     OptionSet optionset = optionparser.parse(p_main_0_);
/*  51 */     List<String> list = optionset.valuesOf(optionspec19);
/*     */     
/*  53 */     if (!list.isEmpty())
/*     */     {
/*  55 */       System.out.println("Completely ignored arguments: " + list);
/*     */     }
/*     */     
/*  58 */     String s = (String)optionset.valueOf(optionspec5);
/*  59 */     Proxy proxy = Proxy.NO_PROXY;
/*     */     
/*  61 */     if (s != null)
/*     */     {
/*     */       try
/*     */       {
/*  65 */         proxy = new Proxy(java.net.Proxy.Type.SOCKS, new java.net.InetSocketAddress(s, ((Integer)optionset.valueOf(optionspec6)).intValue()));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  73 */     String s1 = (String)optionset.valueOf(optionspec7);
/*  74 */     final String s2 = (String)optionset.valueOf(optionspec8);
/*     */     
/*  76 */     if ((!proxy.equals(Proxy.NO_PROXY)) && (isNullOrEmpty(s1)) && (isNullOrEmpty(s2)))
/*     */     {
/*  78 */       Authenticator.setDefault(new Authenticator()
/*     */       {
/*     */         protected PasswordAuthentication getPasswordAuthentication()
/*     */         {
/*  82 */           return new PasswordAuthentication(Main.this, s2.toCharArray());
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  87 */     int i = ((Integer)optionset.valueOf(optionspec13)).intValue();
/*  88 */     int j = ((Integer)optionset.valueOf(optionspec14)).intValue();
/*  89 */     boolean flag = optionset.has("fullscreen");
/*  90 */     boolean flag1 = optionset.has("checkGlErrors");
/*  91 */     boolean flag2 = optionset.has("demo");
/*  92 */     String s3 = (String)optionset.valueOf(optionspec12);
/*  93 */     Gson gson = new GsonBuilder().registerTypeAdapter(PropertyMap.class, new com.mojang.authlib.properties.PropertyMap.Serializer()).create();
/*  94 */     PropertyMap propertymap = (PropertyMap)gson.fromJson((String)optionset.valueOf(optionspec15), PropertyMap.class);
/*  95 */     PropertyMap propertymap1 = (PropertyMap)gson.fromJson((String)optionset.valueOf(optionspec16), PropertyMap.class);
/*  96 */     File file1 = (File)optionset.valueOf(optionspec2);
/*  97 */     File file2 = optionset.has(optionspec3) ? (File)optionset.valueOf(optionspec3) : new File(file1, "assets/");
/*  98 */     File file3 = optionset.has(optionspec4) ? (File)optionset.valueOf(optionspec4) : new File(file1, "resourcepacks/");
/*  99 */     String s4 = optionset.has(optionspec10) ? (String)optionspec10.value(optionset) : (String)optionspec9.value(optionset);
/* 100 */     String s5 = optionset.has(optionspec17) ? (String)optionspec17.value(optionset) : null;
/* 101 */     String s6 = (String)optionset.valueOf(optionspec);
/* 102 */     Integer integer = (Integer)optionset.valueOf(optionspec1);
/* 103 */     Session session = new Session((String)optionspec9.value(optionset), s4, (String)optionspec11.value(optionset), (String)optionspec18.value(optionset));
/* 104 */     GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag1), new GameConfiguration.FolderInformation(file1, file3, file2, s5), new GameConfiguration.GameInformation(flag2, s3), new GameConfiguration.ServerInformation(s6, integer.intValue()));
/* 105 */     Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
/*     */     {
/*     */ 
/*     */       public void run() {}
/*     */ 
/*     */ 
/* 111 */     });
/* 112 */     Thread.currentThread().setName("Client thread");
/* 113 */     new Minecraft(gameconfiguration).run();
/*     */   }
/*     */   
/*     */   private static boolean isNullOrEmpty(String str)
/*     */   {
/* 118 */     return (str != null) && (!str.isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\main\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */