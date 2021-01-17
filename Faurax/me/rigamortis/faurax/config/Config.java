package me.rigamortis.faurax.config;

import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import java.util.*;
import java.io.*;
import me.rigamortis.faurax.gui.click.components.*;
import me.rigamortis.faurax.module.modules.render.*;
import me.rigamortis.faurax.friends.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.gui.click.theme.*;

public class Config
{
    public static String dir;
    
    static {
        Config.dir = "Faurax 3.6";
    }
    
    public Config() {
        try {
            this.createNewDir(Config.dir);
            this.createNewDir(String.valueOf(Config.dir) + "/Plugins");
            this.createNewDir(String.valueOf(Config.dir) + "/Commands");
            this.createNewFile("/Mods");
            this.createNewFile("/Friends");
            this.createNewFile("/Keys");
            this.createNewFile("/Xray");
            this.createNewFile("/Config");
            this.createNewFile("/GUI");
            this.loadMods();
            this.loadFriends();
            this.loadBinds();
            this.loadXray();
            this.loadConfig();
            this.loadGUI();
            this.loadGUITheme();
        }
        catch (IOException ex) {
            System.out.println("Error writing to file " + Config.dir);
        }
    }
    
    public void createNewDir(final String name) throws IOException {
        final File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    public void createNewFile(final String name) throws IOException {
        final File file = new File(String.valueOf(Config.dir) + name + ".faurax");
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    public void loadConfig() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/Config.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String s = line;
                s = s.substring(0, s.indexOf(":"));
                Client.getValues();
                for (final Value value : ValueManager.values) {
                    final String val = String.valueOf(value.getValType()) + "-" + value.getName();
                    if (s.equalsIgnoreCase(val)) {
                        if (value.getType() == Boolean.TYPE) {
                            value.setBooleanValue(Boolean.parseBoolean(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() == Float.TYPE) {
                            value.setFloatValue(Float.parseFloat(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() == Integer.TYPE) {
                            value.setIntValue(Integer.parseInt(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() != String.class) {
                            continue;
                        }
                        value.setSelectedOption(line.substring(line.lastIndexOf(":") + 1));
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveConfig() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/Config.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            Client.getValues();
            for (final Value value : ValueManager.values) {
                if (value.getType() == Boolean.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getBooleanValue());
                }
                if (value.getType() == Float.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getFloatValue());
                }
                if (value.getType() == Integer.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getIntValue());
                }
                if (value.getType() == String.class) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getSelectedOption());
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadPresetConfig(final String fileName) {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/" + fileName + ".faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String s = line;
                s = s.substring(0, s.indexOf(":"));
                Client.getValues();
                for (final Value value : ValueManager.values) {
                    final String val = String.valueOf(value.getValType()) + "-" + value.getName();
                    if (s.equalsIgnoreCase(val)) {
                        if (value.getType() == Boolean.TYPE) {
                            value.setBooleanValue(Boolean.parseBoolean(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() == Float.TYPE) {
                            value.setFloatValue(Float.parseFloat(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() == Integer.TYPE) {
                            value.setIntValue(Integer.parseInt(line.substring(line.lastIndexOf(":") + 1)));
                        }
                        if (value.getType() != String.class) {
                            continue;
                        }
                        value.setSelectedOption(line.substring(line.lastIndexOf(":") + 1));
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void savePresetConfig(final String fileName) {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/" + fileName + ".faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            Client.getValues();
            for (final Value value : ValueManager.values) {
                if (value.getType() == Boolean.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getBooleanValue());
                }
                if (value.getType() == Float.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getFloatValue());
                }
                if (value.getType() == Integer.TYPE) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getIntValue());
                }
                if (value.getType() == String.class) {
                    bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getSelectedOption());
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadGUI() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/GUI.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String s = line;
                s = s.substring(0, s.indexOf("~"));
                for (final Panel panel : Client.getGUI().panels) {
                    if (s.equalsIgnoreCase(panel.name)) {
                        panel.dragX = Float.parseFloat(line.substring(line.lastIndexOf("~") + 1, line.indexOf(":")));
                        panel.dragY = Float.parseFloat(line.substring(line.lastIndexOf(":") + 1, line.indexOf("!")));
                        panel.visible = Boolean.parseBoolean(line.substring(line.indexOf("!") + 1));
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveGUI() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/GUI.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Panel panel : Client.getGUI().panels) {
                bufferedWriter.write(String.valueOf(panel.name) + "~" + panel.dragX + ":" + panel.dragY + "!" + panel.visible);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveXray() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/Xray.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < Xray.blocks.length; ++i) {
                if (Xray.blocks[i]) {
                    bufferedWriter.write(new StringBuilder().append(i).toString());
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadXray() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/Xray.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                Xray.add(Integer.parseInt(line));
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadBinds() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/Keys.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                for (final Module mod : Client.getModules().mods) {
                    if (mod.getName().equalsIgnoreCase(line.substring(0, line.indexOf(":")))) {
                        final String key = line.substring(line.lastIndexOf(":") + 1).toUpperCase();
                        mod.setKey(key);
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveBinds() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/Keys.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Module mod : Client.getModules().mods) {
                bufferedWriter.write(String.valueOf(mod.getName()) + ":" + Module.getKeyFromInt(mod.getKey()));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadFriends() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/Friends.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                FriendManager.addFriend(line.substring(0, line.indexOf(":")), line.substring(line.lastIndexOf(":") + 1));
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveFriends() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/Friends.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Friend friend : FriendManager.friends) {
                bufferedWriter.write(String.valueOf(friend.getName()) + ":" + friend.getAlias());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadModsPreset(final String file) {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/" + file + " Mods.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                for (final Module mod : Client.getModules().mods) {
                    if (mod.getType() != ModuleType.UI && mod.getName().equalsIgnoreCase(line)) {
                        mod.setToggled(true);
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveModsPreset(final String file) {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/" + file + " Mods.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Module mod : Client.getModules().mods) {
                if (mod.getType() != ModuleType.UI && mod.isToggled()) {
                    bufferedWriter.write(mod.getName());
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadMods() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/Mods.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                for (final Module mod : Client.getModules().mods) {
                    if (mod.getType() != ModuleType.UI) {
                        if (mod.getName().equalsIgnoreCase(line)) {
                            mod.setToggled(true);
                        }
                        if (mod.getName().equalsIgnoreCase(line) || !mod.isToggled()) {
                            continue;
                        }
                        mod.setToggled(false);
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveMods() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/Mods.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Module mod : Client.getModules().mods) {
                if (mod.getType() != ModuleType.UI && mod.isToggled()) {
                    bufferedWriter.write(mod.getName());
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadGUITheme() {
        try {
            String line = null;
            final FileReader fileReader = new FileReader(String.valueOf(Config.dir) + "/GUITheme.faurax");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                for (final Theme theme : Client.getThemes().themes) {
                    if (line.equalsIgnoreCase(theme.getName())) {
                        for (final Theme t : Client.getThemes().themes) {
                            t.setVisible(false);
                        }
                        theme.setVisible(true);
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveGUITheme() {
        try {
            final FileWriter fileWriter = new FileWriter(String.valueOf(Config.dir) + "/GUITheme.faurax");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (final Theme theme : Client.getThemes().themes) {
                if (theme.isVisible()) {
                    bufferedWriter.write(theme.getName());
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
}
