/*     */ package rip.jutting.polaris.command.commands;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.Command;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.protection.GuiAuth;
/*     */ import rip.jutting.polaris.utils.FileUtils2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigCommand
/*     */   implements Command
/*     */ {
/*     */   public static File CONFIG_DIR;
/*     */   
/*     */   public boolean run(final String[] args)
/*     */   {
/*  43 */     if (args.length < 2) {
/*  44 */       return false;
/*     */     }
/*  46 */     if ((args.length == 2) || (args.length == 3) || (args.length == 4))
/*     */     {
/*     */       String name;
/*     */       
/*     */ 
/*  51 */       if ((args[1].equalsIgnoreCase("load")) && (args.length == 3)) {
/*  52 */         for (Module m : Polaris.instance.moduleManager.getModules()) {
/*  53 */           if (m.isToggled()) {
/*  54 */             m.toggle();
/*     */           }
/*     */         }
/*  57 */         File CONFIG_DIR = FileUtils2.getConfigFile(args[2].toString().toUpperCase());
/*  58 */         Polaris.sendMessage("Successfully loaded the config §f" + args[2].toUpperCase());
/*  59 */         Object fileContent = FileUtils2.read(CONFIG_DIR);
/*  60 */         for (String line : (List)fileContent) {
/*     */           try {
/*  62 */             String[] split = line.split(":");
/*  63 */             name = split[0];
/*  64 */             String bind = split[1];
/*  65 */             String enable = split[2];
/*  66 */             String sname = split[3];
/*  67 */             String settingname = split[4];
/*  68 */             String mstring = split[5];
/*  69 */             String vdouble = split[6];
/*  70 */             String booolean = split[7];
/*  71 */             boolean meme = Boolean.parseBoolean(booolean);
/*  72 */             double meme2 = Double.parseDouble(vdouble);
/*  73 */             int key = Integer.parseInt(bind);
/*  74 */             for (Module m : ModuleManager.modules) {
/*  75 */               if (name.equalsIgnoreCase(m.getName())) {
/*  76 */                 if (!"FrozenPvPs".equalsIgnoreCase(GuiAuth.username.getText()))
/*     */                 {
/*  78 */                   m.setKey(key);
/*     */                 }
/*  80 */                 if ((enable.equalsIgnoreCase("true")) && (!m.isToggled())) {
/*  81 */                   m.toggle();
/*     */                 }
/*     */               }
/*     */             }
/*  85 */             for (Setting s : SettingsManager.settings) {
/*  86 */               setting(settingname).setValBoolean(meme);
/*  87 */               setting(settingname).setValString(mstring);
/*  88 */               setting(settingname).setValDouble(meme2);
/*     */             }
/*     */           }
/*     */           catch (Exception localException) {}
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         Object fileContent;
/*  97 */         if ((args[1].equalsIgnoreCase("create")) || ((args[1].equalsIgnoreCase("save")) && (args.length == 3))) {
/*  98 */           File CONFIG_DIR = FileUtils2.getConfigFile(args[2].toString().toUpperCase());
/*  99 */           Polaris.sendMessage("Successfully created the config §f" + args[2].toUpperCase());
/* 100 */           fileContent = new ArrayList();
/* 101 */           for (??? = ModuleManager.modules.iterator(); ???.hasNext(); 
/* 102 */               name.hasNext())
/*     */           {
/* 101 */             Module m = (Module)???.next();
/* 102 */             name = SettingsManager.settings.iterator(); continue;Setting s = (Setting)name.next();
/* 103 */             ((List)fileContent).add(m.getName() + ":" + m.getKey() + ":" + m.isToggled() + ":" + s.getParentMod().getName() + ":" + s.getName() + ":" + s.getValString() + ":" + s.getValDouble() + ":" + s.getValBoolean());
/*     */           }
/*     */           
/* 106 */           FileUtils2.write(CONFIG_DIR, (List)fileContent, true);
/* 107 */           System.out.println("Saved Config!");
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 112 */         else if ((args[1].equalsIgnoreCase("del")) || ((args[1].equalsIgnoreCase("delete")) && (args.length == 3))) {
/* 113 */           File CONFIG_DIR = FileUtils2.getConfigFile(args[2].toString().toUpperCase());
/* 114 */           CONFIG_DIR.delete();
/* 115 */           Polaris.sendMessage("Successfully deleted the config §a" + args[2].toUpperCase());
/*     */         }
/*     */         else
/*     */         {
/*     */           Object sprint;
/* 120 */           if (args[1].equalsIgnoreCase("clear")) {
/* 121 */             for (fileContent = Polaris.instance.moduleManager.getModules().iterator(); ((Iterator)fileContent).hasNext();) { Module m = (Module)((Iterator)fileContent).next();
/* 122 */               if (m.isToggled()) {
/* 123 */                 m.toggle();
/*     */               }
/*     */             }
/* 126 */             Module hud = Polaris.instance.moduleManager.getModuleByName("Hud");
/* 127 */             sprint = Polaris.instance.moduleManager.getModuleByName("Sprint");
/* 128 */             hud.toggle();
/* 129 */             ((Module)sprint).toggle();
/* 130 */             Polaris.sendMessage("Successfully cleared the current config");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 136 */           else if ((args[1].equalsIgnoreCase("download")) || ((args[1].equalsIgnoreCase("dl")) && (args.length == 4))) {
/* 137 */             URL url = verify(args[2]);
/* 138 */             saveFile(url, System.getenv("APPDATA") + "/.minecraft/Polaris/" + args[3] + ".txt");
/* 139 */             Polaris.sendMessage("Successfully downloaded the config as " + args[3]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 145 */           else if ((args[1].equalsIgnoreCase("share")) || ((args[1].equalsIgnoreCase("upload")) && (args.length == 3)))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */             new Thread(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/*     */                 try
/*     */                 {
/* 150 */                   Thread.sleep(100L);
/*     */                   try {
/* 152 */                     Socket socket = null;
/* 153 */                     String host = "51.38.119.240";
/* 154 */                     socket = new Socket(host, 1234);
/* 155 */                     File file = FileUtils2.getConfigFile(args[2].toString().toUpperCase());
/* 156 */                     byte[] bytes = new byte['Ѐ'];
/* 157 */                     InputStream in = new FileInputStream(file);
/* 158 */                     OutputStream out = socket.getOutputStream();
/* 159 */                     long length = file.length();
/* 160 */                     DataOutputStream d = new DataOutputStream(out);
/* 161 */                     d.writeUTF(args[2].toString().toUpperCase() + " by " + GuiAuth.username.getText() + ".txt");
/* 162 */                     d.writeLong(length);
/*     */                     int count;
/* 164 */                     while ((count = in.read(bytes)) > 0) { int count;
/* 165 */                       out.write(bytes, 0, count);
/*     */                     }
/* 167 */                     out.close();
/* 168 */                     in.close();
/* 169 */                     socket.close();
/*     */                   } catch (UnknownHostException e) {
/* 171 */                     e.printStackTrace();
/*     */                   } catch (IOException e) {
/* 173 */                     e.printStackTrace();
/*     */                   }
/* 175 */                   Polaris.sendMessage("Successfully uploaded the config §f" + args[2].toUpperCase());
/*     */ 
/*     */                 }
/*     */                 catch (InterruptedException localInterruptedException) {}
/*     */               }
/*     */             })
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */               .start();
/*     */           }
/* 182 */           else if (args[1].equalsIgnoreCase("list")) {
/* 183 */             Polaris.sendMessage("List of configs:");
/* 184 */             for (sprint = getAllConfigs().iterator(); ((Iterator)sprint).hasNext();) { String string = (String)((Iterator)sprint).next();
/* 185 */               Polaris.sendMessage(string.replace(".txt", ""));
/*     */             }
/*     */           }
/*     */         }
/*     */       } }
/* 190 */     return true;
/*     */   }
/*     */   
/*     */   private ArrayList<String> getAllConfigs() {
/* 194 */     ArrayList<String> xd = new ArrayList();
/* 195 */     File folder = new File(Minecraft.getMinecraft().mcDataDir + "/" + Polaris.instance.name);
/* 196 */     File[] listOfFiles = folder.listFiles();
/* 197 */     for (int i = 0; i < listOfFiles.length; i++) {
/* 198 */       if ((listOfFiles[i].isFile()) && (!listOfFiles[i].getName().equalsIgnoreCase("Mods.txt")) && (!listOfFiles[i].getName().equalsIgnoreCase("Friends.txt")) && (!listOfFiles[i].getName().equalsIgnoreCase("Settings.txt"))) {
/* 199 */         xd.add(listOfFiles[i].getName());
/*     */       }
/*     */     }
/* 202 */     return xd;
/*     */   }
/*     */   
/*     */   private static URL verify(String url) {
/* 206 */     if (!url.toLowerCase().startsWith("https://")) {
/* 207 */       return null;
/*     */     }
/* 209 */     URL verifyUrl = null;
/*     */     try
/*     */     {
/* 212 */       verifyUrl = new URL(url);
/*     */     } catch (Exception e) {
/* 214 */       e.printStackTrace();
/*     */     }
/* 216 */     return verifyUrl;
/*     */   }
/*     */   
/*     */   public static boolean saveFile(URL fileURL, String fileSavePath)
/*     */   {
/* 221 */     boolean isSucceed = true;
/*     */     
/* 223 */     CloseableHttpClient httpClient = HttpClients.createDefault();
/*     */     
/* 225 */     HttpGet httpGet = new HttpGet(fileURL.toString());
/* 226 */     httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
/* 227 */     httpGet.addHeader("Referer", "https://www.google.com");
/*     */     try
/*     */     {
/* 230 */       CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
/* 231 */       HttpEntity fileEntity = httpResponse.getEntity();
/*     */       
/* 233 */       if (fileEntity != null) {
/* 234 */         FileUtils.copyInputStreamToFile(fileEntity.getContent(), new File(fileSavePath));
/* 235 */         System.out.println("Success!");
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 239 */       isSucceed = false;
/* 240 */       System.out.println("Failed");
/*     */     }
/*     */     
/* 243 */     httpGet.releaseConnection();
/*     */     
/* 245 */     return isSucceed;
/*     */   }
/*     */   
/*     */   public String usage()
/*     */   {
/* 250 */     return "-config [load/create/del/clear/upload/download/list] [name]";
/*     */   }
/*     */   
/*     */   public static Setting setting(String set) {
/* 254 */     return Polaris.instance.settingsManager.getSettingByName(set);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\ConfigCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */