// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.managers.fileManager;

import me.CheerioFX.FusionX.AltManager.Alt;
import me.CheerioFX.FusionX.AltManager.AltManager;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.FusionX;

public class FileManager
{
    private Filer states;
    private Filer binds;
    private Filer values;
    private Filer booleanValues;
    private Filer alts;
    private boolean stopSaving;
    
    public FileManager() {
        this.states = new Filer("Hacks", FusionX.theClient.getClient_Name());
        this.binds = new Filer("Binds", FusionX.theClient.getClient_Name());
        this.values = new Filer("Values", FusionX.theClient.getClient_Name());
        this.booleanValues = new Filer("CheckBoxes", FusionX.theClient.getClient_Name());
        this.alts = new Filer("Alts", FusionX.theClient.getClient_Name());
        this.stopSaving = false;
        try {
            this.loadAll();
        }
        catch (Exception ex) {}
    }
    
    public void clearAll() {
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        for (final Module m : ModuleManager.getModules()) {
            if (m.getState()) {
                m.setState(false);
            }
            m.setBind(m.defaultBind);
        }
        this.states.clear();
        this.binds.clear();
        this.values.clear();
        this.booleanValues.clear();
        this.alts.clear();
    }
    
    public void loadAll() throws Exception {
        this.loadKeybinds();
        this.loadAlts();
    }
    
    public void saveMods() throws Exception {
    }
    
    private void loadMods() throws Exception {
    }
    
    public void saveKeybinds() throws Exception {
        if (this.stopSaving) {
            return;
        }
        this.binds.clear();
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        for (final Module m : ModuleManager.getModules()) {
            final String line = (String.valueOf(m.getName()) + ":" + Keyboard.getKeyName(m.getBind())).toLowerCase();
            this.binds.write(line);
        }
    }
    
    private void loadKeybinds() throws Exception {
        for (final String s : this.binds.read()) {
            final ModuleManager moduleManager = FusionX.theClient.moduleManager;
            for (final Module m : ModuleManager.getModules()) {
                final String name = s.split(":")[0];
                final String keybind = s.split(":")[1];
                if (m.getName().equalsIgnoreCase(name)) {
                    m.setBind(keybind, true);
                }
            }
        }
    }
    
    public void saveAlts() throws Exception {
        if (this.stopSaving) {
            return;
        }
        this.alts.clear();
        for (final Alt alt : AltManager.registry) {
            if (alt.getMask().equals("")) {
                this.alts.write(String.valueOf(alt.getUsername()) + ":" + alt.getPassword());
            }
            else {
                this.alts.write(String.valueOf(alt.getUsername()) + ":" + alt.getPassword() + ":" + alt.getMask());
            }
        }
    }
    
    public void loadAlts() throws Exception {
        AltManager.registry.clear();
        for (final String s : this.alts.read()) {
            final String[] args = s.split(":");
            for (int i = 0; i < 2; ++i) {
                args[i].replace(" ", "");
            }
            if (args.length > 2) {
                AltManager.registry.add(new Alt(args[0], args[1], args[2]));
            }
            else {
                AltManager.registry.add(new Alt(args[0], args[1]));
            }
        }
    }
    
    public void saveValues() throws Exception {
    }
    
    public void loadValues() throws Exception {
    }
    
    public void saveBooleanValues() throws Exception {
    }
    
    public void loadBooleanValues() throws Exception {
    }
}
