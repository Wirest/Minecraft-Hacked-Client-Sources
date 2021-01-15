/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.option;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.aristhena.lucid.commands.OptionCommand;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.util.FileUtils;
import me.aristhena.lucid.util.StringUtil;

public class OptionManager {
    private static final File OPTION_DIR = FileUtils.getConfigFile("Options");
    public static List<Option> optionList = new ArrayList<Option>();

    public static void init() throws IllegalArgumentException, IllegalAccessException {
        for (Module mod : ModuleManager.moduleList) {
            Field[] arrfield = mod.getClass().getDeclaredFields();
            int n = arrfield.length;
            int n2 = 0;
            while (n2 < n) {
                Field field = arrfield[n2];
                field.setAccessible(true);
                if (field.isAnnotationPresent(Op.class)) {
                    boolean optionValue;
                    try {
                        optionValue = field.getBoolean(mod);
                    }
                    catch (IllegalArgumentException e) {
                        optionValue = false;
                    }
                    Op opAnnotation = (Op)field.getAnnotation(Op.class);
                    String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtil.capitalize(field.getName()) : StringUtil.capitalize(opAnnotation.name());
                    Option option = new Option(StringUtil.capitalize(name), optionValue, mod);
                    optionList.add(option);
                }
                ++n2;
            }
            ArrayList<String> nameList = new ArrayList<String>();
            if (CommandManager.optionCommand.names != null) {
                String[] optionValue = CommandManager.optionCommand.names;
                int n3 = optionValue.length;
                n = 0;
                while (n < n3) {
                    String name = optionValue[n];
                    nameList.add(name);
                    ++n;
                }
            }
            nameList.add(mod.name);
            CommandManager.optionCommand.names = nameList.toArray(new String[0]);
        }
        optionList.sort(new Comparator<Option>(){

            @Override
            public int compare(Option o1, Option o2) {
                String s1 = o1.name;
                String s2 = o2.name;
                return s1.compareTo(s2);
            }
        });
        OptionManager.load();
        OptionManager.save();
    }

    public static void load() {
        List<String> fileContent = FileUtils.read(OPTION_DIR);
        for (String line : fileContent) {
            try {
                String[] split = line.split(":");
                String optionName = split[0];
                String optionValAsString = split[1];
                boolean optionVal = Boolean.parseBoolean(optionValAsString);
                Module mod = ModuleManager.getModule(split[2]);
                Option option = OptionManager.getOption(optionName, mod);
                if (option == null) continue;
                option.setValue(optionVal);
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        ArrayList<String> fileContent = new ArrayList<String>();
        for (Option option : optionList) {
            String optionName = option.name;
            String optionVal = String.valueOf(option.value);
            Module mod = option.mod;
            fileContent.add(String.format("%s:%s:%s", optionName, optionVal, mod.name));
        }
        FileUtils.write(OPTION_DIR, fileContent, true);
    }

    public static Option getOption(String optionName, Module mod) {
        for (Option option : optionList) {
            if (!option.name.equalsIgnoreCase(optionName) || option.mod != mod) continue;
            return option;
        }
        return null;
    }

}

