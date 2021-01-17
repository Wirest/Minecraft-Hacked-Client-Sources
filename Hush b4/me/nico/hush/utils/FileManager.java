// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import java.util.Iterator;
import me.nico.hush.modules.Module;
import de.Hero.settings.Setting;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import me.nico.hush.Client;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import net.minecraft.client.Minecraft;

public class FileManager
{
    public Minecraft mc;
    
    public FileManager() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void load() {
        final File folder = new File(this.mc.mcDataDir + "\\Z\u03a6diac");
        if (!folder.exists()) {
            folder.mkdir();
        }
        this.loadKeyBinds();
    }
    
    public void saveSettings() {
        final File file = new File(this.mc.mcDataDir + "\\Z\u03a6diac\\settings.txt");
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(file));
            final String setting = Client.instance.settingManager.getSettingByName("CriticalsMode").getValString();
            final String setting2 = Client.instance.settingManager.getSettingByName("KillAuraMode").getValString();
            final String setting3 = Client.instance.settingManager.getSettingByName("VelocityMode").getValString();
            final String setting4 = Client.instance.settingManager.getSettingByName("SpammerMode").getValString();
            final String setting5 = Client.instance.settingManager.getSettingByName("BhopMode").getValString();
            final String setting6 = Client.instance.settingManager.getSettingByName("FastLadderMode").getValString();
            final String setting7 = Client.instance.settingManager.getSettingByName("GlideMode").getValString();
            final String setting8 = Client.instance.settingManager.getSettingByName("SpeedMode").getValString();
            final String setting9 = Client.instance.settingManager.getSettingByName("StepMode").getValString();
            final String setting10 = Client.instance.settingManager.getSettingByName("AutoArmorMode").getValString();
            final String setting11 = Client.instance.settingManager.getSettingByName("ChestStealerMode").getValString();
            final String setting12 = Client.instance.settingManager.getSettingByName("FastUseMode").getValString();
            final String setting13 = Client.instance.settingManager.getSettingByName("NoFallMode").getValString();
            final String setting14 = Client.instance.settingManager.getSettingByName("HUDMode").getValString();
            final String setting15 = Client.instance.settingManager.getSettingByName("DestroyerMode").getValString();
            final String endstring = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting;
            final String endstring2 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting2;
            final String endstring3 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting3;
            final String endstring4 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting4;
            final String endstring5 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting5;
            final String endstring6 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting6;
            final String endstring7 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting7;
            final String endstring8 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting8;
            final String endstring9 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting9;
            final String endstring10 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting10;
            final String endstring11 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting11;
            final String endstring12 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting12;
            final String endstring13 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting13;
            final String endstring14 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting14;
            final String endstring15 = String.valueOf(String.valueOf(String.valueOf("Mode"))) + ":" + setting15;
            writer.println(endstring);
            writer.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadSettings() {
        try {
            final File file = new File(this.mc.mcDataDir + "\\Z\u03a6diac\\settings.txt");
            if (!file.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            else if (file.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString = "";
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(":");
                    final Setting s = Client.instance.settingManager.getSettingByName(split[0]);
                    final String setting = split[1];
                    final String setting2 = split[1];
                    final String setting3 = split[1];
                    final String setting4 = split[1];
                    final String setting5 = split[1];
                    final String setting6 = split[1];
                    final String setting7 = split[1];
                    final String setting8 = split[1];
                    final String setting9 = split[1];
                    final String setting10 = split[1];
                    final String setting11 = split[1];
                    final String setting12 = split[1];
                    final String setting13 = split[1];
                    final String setting14 = split[1];
                    final String setting15 = split[1];
                    Client.instance.settingManager.getSettingByName("CriticalsMode").setValString(setting);
                    Client.instance.settingManager.getSettingByName("KillAuraMode").setValString(setting2);
                    Client.instance.settingManager.getSettingByName("VelocityMode").setValString(setting3);
                    Client.instance.settingManager.getSettingByName("SpammerMode").setValString(setting4);
                    Client.instance.settingManager.getSettingByName("BhopMode").setValString(setting5);
                    Client.instance.settingManager.getSettingByName("FastLadderMode").setValString(setting6);
                    Client.instance.settingManager.getSettingByName("GlideMode").setValString(setting7);
                    Client.instance.settingManager.getSettingByName("SpeedMode").setValString(setting8);
                    Client.instance.settingManager.getSettingByName("StepMode").setValString(setting9);
                    Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString(setting10);
                    Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString(setting11);
                    Client.instance.settingManager.getSettingByName("FastUseMode").setValString(setting12);
                    Client.instance.settingManager.getSettingByName("NoFallMode").setValString(setting13);
                    Client.instance.settingManager.getSettingByName("HUDMode").setValString(setting14);
                    Client.instance.settingManager.getSettingByName("DestroyerMode").setValString(setting15);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadKeyBinds() {
        try {
            final File file = new File(this.mc.mcDataDir + "\\Z\u03a6diac\\Binds.txt");
            if (!file.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
            else if (file.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString = "";
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(":");
                    final Module mod = Client.instance.moduleManager.getModuleName(split[0]);
                    final int keyBind = Integer.parseInt(split[1]);
                    mod.setKeyBind(keyBind);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveKeyBinds() {
        final File file = new File(this.mc.mcDataDir + "\\Z\u03a6diac\\Binds.txt");
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (final Module module : Client.instance.moduleManager.getModules()) {
                final String modulename = module.getName();
                final int modulekey = module.getKeyBind();
                final String endstring = String.valueOf(String.valueOf(String.valueOf(modulename))) + ":" + modulekey;
                writer.println(endstring);
            }
            writer.close();
        }
        catch (Exception ex) {}
    }
}
