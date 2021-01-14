package me.Corbis.Execution.Configuration;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

public class SaveLoad {
    public File dir;
    public File configs;
    public File dataFile;

    public SaveLoad() {
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Execution");
        if (!dir.exists()) {
            dir.mkdir();
        }
        configs = new File(Minecraft.getMinecraft().mcDataDir, "Execution/configs");
        if (!configs.exists()) {
            configs.mkdir();
        }
        dataFile = new File(dir, "data.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {e.printStackTrace();}
        }

        this.load();
    }


    public void save() {

        ArrayList<String> toSave = new ArrayList<String>();

        for (Module mod : Execution.instance.moduleManager.getModules()) {
            toSave.add("MOD:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKey());
        }

        for (Setting set : Execution.instance.settingsManager.getSettings()) {
            if (set.isCheck()) {
                toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValBoolean());
            }
            if (set.isCombo()) {
                toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValString());
            }
            if (set.isSlider()) {
                toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValDouble());
            }
        }

        try {
            PrintWriter pw = new PrintWriter(this.dataFile);
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {


        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            String[] args = s.split(":");
            if (s.toLowerCase().startsWith("mod:")) {
                Module m = Execution.instance.moduleManager.getModuleByName(args[1]);
                if (m != null) {
                    m.setEnabled(Boolean.parseBoolean(args[2]));
                    m.setKey(Integer.parseInt(args[3]));
                }
            } else if (s.toLowerCase().startsWith("set:")) {
                Module m = Execution.instance.moduleManager.getModuleByName(args[2]);
                if (m != null) {
                    Setting set = Execution.instance.settingsManager.getSettingByName(args[1]);
                    if (set != null) {
                        if (set.isCheck()) {
                            set.setValBoolean(Boolean.parseBoolean(args[3]));
                        }
                        if (set.isCombo()) {
                            set.setValString(args[3]);
                        }
                        if (set.isSlider()) {
                            set.setValDouble(Double.parseDouble(args[3]));
                        }
                    }
                }
            }
        }
    }
}
