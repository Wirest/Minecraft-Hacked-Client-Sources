package me.Corbis.Execution.Command.Commands;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Command.Command;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.module.implementations.HUD;
import me.Corbis.Execution.utils.PlayMusic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Hypixel extends Command {

    public Hypixel(){
        super("config", ".config", 0);
    }

    @Override
    public void onCommand(String command, String[] args){
        File datas = new File(Execution.instance.saveLoad.configs.getPath() + "/" + args[0] + ".txt");
        if(datas != null){
            Execution.instance.moduleManager.getModule(HUD.class).setEnabled(false);
            load(datas);

        }
        PlayMusic.playSound("LoadIn.wav");
        super.onCommand(command, args);
    }

    public void load(File file) {

        Execution.instance.addChatMessage("loading Config: " + file.getAbsolutePath());

        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
                }
            } else if (s.toLowerCase().startsWith("set:")) {
                Module m = Execution.instance.moduleManager.getModuleByName(args[2]);
                if (m != null) {
                    Setting set = Execution.instance.settingsManager.getSettingByName(args[1]);
                    if (set != null) {
                        if (set.isCheck()) {
                            set.setValBoolean(Boolean.parseBoolean(args[3]));
                            Execution.instance.addChatMessage("Config: Bool Value set: " + args[1] + ", " + args[2] + ", " + args[3]);
                        }
                        if (set.isCombo()) {
                            set.setValString(args[3]);
                            Execution.instance.addChatMessage("Config: Combo Value set: " + args[1] + ", " + args[2] + ", " + args[3]);

                        }
                        if (set.isSlider()) {
                            set.setValDouble(Double.parseDouble(args[3]));
                            Execution.instance.addChatMessage("Config: Double Value set: " + args[1] + ", " + args[2] + ", " + args[3]);

                        }
                    }
                }
            }
        }
    }
}
