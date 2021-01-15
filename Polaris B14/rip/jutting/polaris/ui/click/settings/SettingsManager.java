/*    */ package rip.jutting.polaris.ui.click.settings;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.utils.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SettingsManager
/*    */ {
/*    */   public static ArrayList<Setting> settings;
/*    */   private static File SETTING_DIR;
/*    */   
/*    */   public SettingsManager()
/*    */   {
/* 24 */     SETTING_DIR = FileUtils.getConfigFile("Settings");
/* 25 */     settings = new ArrayList();
/*    */   }
/*    */   
/*    */   public void rSetting(Setting in) {
/* 29 */     settings.add(in);
/*    */   }
/*    */   
/*    */   public ArrayList<Setting> getSettings() {
/* 33 */     return settings;
/*    */   }
/*    */   
/*    */   public ArrayList<Setting> getSettingsByMod(Module mod) {
/* 37 */     ArrayList<Setting> out = new ArrayList();
/* 38 */     for (Setting s : getSettings()) {
/* 39 */       if (s.getParentMod().equals(mod)) {
/* 40 */         out.add(s);
/*    */       }
/*    */     }
/* 43 */     if (out.isEmpty()) {
/* 44 */       return null;
/*    */     }
/* 46 */     return out;
/*    */   }
/*    */   
/*    */   public static Setting setting(String set) {
/* 50 */     return Polaris.instance.settingsManager.getSettingByName(set);
/*    */   }
/*    */   
/*    */   public Setting getSettingByName(String name) {
/* 54 */     for (Setting set : getSettings()) {
/* 55 */       if (set.getName().equalsIgnoreCase(name)) {
/* 56 */         return set;
/*    */       }
/*    */     }
/* 59 */     System.err.println("[" + Polaris.instance.name + "] Error Setting NOT found: '" + name + "'!");
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public static void save() {
/* 64 */     List<String> fileContent = new ArrayList();
/* 65 */     for (Setting s : settings) {
/* 66 */       fileContent.add(s.getParentMod().getName() + ":" + s.getName() + ":" + s.getValString() + ":" + s.getValDouble() + ":" + s.getValBoolean());
/*    */     }
/* 68 */     FileUtils.write(SETTING_DIR, fileContent, true);
/* 69 */     System.out.println("Saved Settings!");
/*    */   }
/*    */   
/*    */   public static void load() {
/* 73 */     List<String> fileContent = FileUtils.read(SETTING_DIR);
/* 74 */     for (String line : fileContent) {
/*    */       try {
/* 76 */         String[] split = line.split(":");
/* 77 */         String name = split[0];
/* 78 */         String settingname = split[1];
/* 79 */         String mstring = split[2];
/* 80 */         String vdouble = split[3];
/* 81 */         String booolean = split[4];
/* 82 */         boolean meme = Boolean.parseBoolean(booolean);
/* 83 */         double meme2 = Double.parseDouble(vdouble);
/* 84 */         for (Setting s : settings) {
/* 85 */           setting(settingname).setValBoolean(meme);
/* 86 */           setting(settingname).setValString(mstring);
/* 87 */           setting(settingname).setValDouble(meme2);
/*    */         }
/*    */       }
/*    */       catch (Exception localException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\settings\SettingsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */