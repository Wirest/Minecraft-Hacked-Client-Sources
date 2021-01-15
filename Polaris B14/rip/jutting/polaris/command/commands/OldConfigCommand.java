/*     */ package rip.jutting.polaris.command.commands;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.Command;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.FileUtils;
/*     */ 
/*     */ public class OldConfigCommand implements Command
/*     */ {
/*     */   public static File CONFIG_DIR;
/*     */   
/*     */   public boolean run(String[] args)
/*     */   {
/*  22 */     if (args.length < 2) {
/*  23 */       return false;
/*     */     }
/*  25 */     if ((args.length == 2) || (args.length == 3))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  30 */       if ((args[1].equalsIgnoreCase("load")) && (args.length == 3)) {
/*  31 */         for (Module m : Polaris.instance.moduleManager.getModules()) {
/*  32 */           if (m.isToggled()) {
/*  33 */             m.toggle();
/*     */           }
/*     */         }
/*  36 */         File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Mods");
/*  37 */         Polaris.sendMessage("Successfully loaded the config §f" + args[2].toUpperCase());
/*  38 */         Object fileContent = FileUtils.read(CONFIG_DIR);
/*  39 */         for (String line : (List)fileContent) {
/*     */           try {
/*  41 */             String[] split = line.split(":");
/*  42 */             String name = split[0];
/*  43 */             String bind = split[1];
/*  44 */             String enable = split[2];
/*  45 */             int key = Integer.parseInt(bind);
/*  46 */             for (Module m : ModuleManager.modules) {
/*  47 */               if (name.equalsIgnoreCase(m.getName())) {
/*  48 */                 m.setKey(key);
/*  49 */                 if ((enable.equalsIgnoreCase("true")) && (!m.isToggled())) {
/*  50 */                   m.toggle();
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (Exception localException) {}
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         Object fileContent;
/*  61 */         if ((args[1].equalsIgnoreCase("create")) || ((args[1].equalsIgnoreCase("save")) && (args.length == 3))) {
/*  62 */           File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Mods");
/*  63 */           Polaris.sendMessage("Successfully created the config §f" + args[2].toUpperCase());
/*  64 */           fileContent = new ArrayList();
/*  65 */           for (Module m : ModuleManager.modules) {
/*  66 */             ((List)fileContent).add(m.getName() + ":" + m.getKey() + ":" + m.isToggled());
/*     */           }
/*  68 */           FileUtils.write(CONFIG_DIR, (List)fileContent, true);
/*  69 */           System.out.println("Saved Modules!");
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*  74 */         else if ((args[1].equalsIgnoreCase("del")) || ((args[1].equalsIgnoreCase("delete")) && (args.length == 3))) {
/*  75 */           File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Mods");
/*  76 */           CONFIG_DIR.delete();
/*  77 */           Polaris.sendMessage("Successfully deleted the config §a" + args[2].toUpperCase());
/*     */         }
/*  79 */         else if (args[1].equalsIgnoreCase("clear")) {
/*  80 */           for (fileContent = Polaris.instance.moduleManager.getModules().iterator(); ((Iterator)fileContent).hasNext();) { Module m = (Module)((Iterator)fileContent).next();
/*  81 */             if (m.isToggled()) {
/*  82 */               m.toggle();
/*     */             }
/*     */           }
/*  85 */           Module hud = Polaris.instance.moduleManager.getModuleByName("Hud");
/*  86 */           Module sprint = Polaris.instance.moduleManager.getModuleByName("Sprint");
/*  87 */           hud.toggle();
/*  88 */           sprint.toggle();
/*  89 */           Polaris.sendMessage("Successfully cleared the current config");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */       if ((args[1].equalsIgnoreCase("load")) && (args.length == 3)) {
/* 102 */         File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Settings");
/* 103 */         Object fileContent = FileUtils.read(CONFIG_DIR);
/* 104 */         for (String line : (List)fileContent) {
/*     */           try {
/* 106 */             String[] split = line.split(":");
/* 107 */             String name = split[0];
/* 108 */             String settingname = split[1];
/* 109 */             String mstring = split[2];
/* 110 */             String vdouble = split[3];
/* 111 */             String booolean = split[4];
/* 112 */             boolean meme = Boolean.parseBoolean(booolean);
/* 113 */             double meme2 = Double.parseDouble(vdouble);
/* 114 */             for (Setting s : SettingsManager.settings) {
/* 115 */               setting(settingname).setValBoolean(meme);
/* 116 */               setting(settingname).setValString(mstring);
/* 117 */               setting(settingname).setValDouble(meme2);
/* 118 */               System.out.println("Loaded Setting!");
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           catch (Exception localException1) {}
/*     */         }
/*     */         
/*     */       }
/* 127 */       else if ((args[1].equalsIgnoreCase("create")) || ((args[1].equalsIgnoreCase("save")) && (args.length == 3))) {
/* 128 */         File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Settings");
/* 129 */         Object fileContent = new ArrayList();
/* 130 */         for (Setting s : SettingsManager.settings) {
/* 131 */           ((List)fileContent).add(s.getParentMod().getName() + ":" + s.getName() + ":" + s.getValString() + ":" + s.getValDouble() + ":" + s.getValBoolean());
/*     */         }
/* 133 */         FileUtils.write(CONFIG_DIR, (List)fileContent, true);
/* 134 */         System.out.println("Saved Settings!");
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 139 */       else if ((args[1].equalsIgnoreCase("del")) || ((args[1].equalsIgnoreCase("delete")) && (args.length == 3))) {
/* 140 */         File CONFIG_DIR = FileUtils.getConfigFile(args[2].toString().toUpperCase() + " Settings");
/* 141 */         CONFIG_DIR.delete();
/*     */       }
/*     */     }
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public String usage()
/*     */   {
/* 150 */     return "-config [load/create/del/clear] [name]";
/*     */   }
/*     */   
/*     */   public static Setting setting(String set) {
/* 154 */     return Polaris.instance.settingsManager.getSettingByName(set);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\OldConfigCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */