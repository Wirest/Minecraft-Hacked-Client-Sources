package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.data.SettingsMap;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.Client;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.StringConversions;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class Settings extends Command {

    public Settings(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        // Intended arguements
        // 1 - Module
        // 2 - Setting
        // 3 - Value
        if (args == null) {
            printUsage();
            return;
        }
        // Make sure the user inputs a valid Module
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            printUsage();
            return;
        }
        // .setting Module
        if (args.length == 1) {
            SettingsMap moduleSettings = module.getSettings();
            ChatUtil.printChat(
                    chatPrefix + "[" + EnumChatFormatting.WHITE + module.getName() + EnumChatFormatting.DARK_GRAY
                            + "] - Settings: " + EnumChatFormatting.WHITE + moduleSettings.size());
            for (Setting setting : moduleSettings.values()) {
                if (setting != null) {
                    printSetting(setting);
                }
            }
        } else if (args.length >= 2) {
            // If there are two or more arguments, get the second arg as a
            // setting
            Setting setting = getSetting(module.getSettings(), args[1]);
            if (setting == null) {
                printUsage();
                return;
            }
            // .setting Module Setting
            if (args.length == 2) {
                // Print the information of the given setting
                printSetting(setting);
            } // .setting Module Setting Value
            else if (args.length >= 3) {
                String objText = args[2];
                try {
                    // If the setting is supposed to be numeric
                    if (setting.getValue() instanceof Number) {
                        Object newValue = (StringConversions.castNumber(objText, setting.getValue()));
                        if (newValue != null) {
                            ChatUtil.printChat(chatPrefix + module.getName() + "'s " + setting.getName().toLowerCase()
                                    + " has been changed to: " + EnumChatFormatting.DARK_AQUA + newValue);
                            if (((Number) newValue).doubleValue() < setting.getMin()) {
                                newValue = (MathUtils.isInteger(setting.getMax()) ? (int) setting.getMin() : setting.getMin());
                            } else if (((Number) newValue).doubleValue() > setting.getMax()) {
                                newValue = (MathUtils.isInteger(setting.getMax()) ? (int) setting.getMax() : setting.getMax());
                            }
                            setting.setValue(newValue);
                            module.getSettings();
                            module.save();
                            return;
                        }
                    } // If the setting is supposed to be a string
                    else if (setting.getValue().getClass().equals(String.class)) {
                        String str = objText;

                        if (args.length > 3) {
                            for (int i = 3; i < args.length; i++) {
                                str = str + " " + args[i];
                            }
                        }
                        ChatUtil.printChat(chatPrefix + module.getName() + "'s " + setting.getName().toLowerCase()
                                + " has been changed to: " + EnumChatFormatting.DARK_RED + "\"" + str + "\"");
                        setting.setValue(str);
                        module.getSettings();
                        module.save();
                        return;
                    } // If the setting is supposed to be a boolean
                    else if (setting.getValue().getClass().equals(Boolean.class)) {
                        ChatUtil.printChat(chatPrefix + module.getName() + "'s " + setting.getName().toLowerCase()
                                + " has been changed to: " + EnumChatFormatting.DARK_RED + objText);
                        setting.setValue(Boolean.parseBoolean(objText));
                        module.getSettings();
                        module.save();
                        return;
                    } else if (setting.getValue() instanceof Options) {
                        for (String string : ((Options) setting.getValue()).getOptions()) {
                            if (string.toLowerCase().equalsIgnoreCase(objText)) {
                                ChatUtil.printChat(chatPrefix + module.getName() + "'s " + ((Options) setting.getValue()).getName()
                                        + " has been changed to: " + EnumChatFormatting.DARK_RED + string);
                                ((Options) setting.getValue()).setSelected(string);
                                return;
                            }
                        }
                    }
                    // Non-numeric, non-string, non boolean setting values
                    else {
                        // Possibly an arraylist or something
                    }
                } catch (Exception e) {
                }
                // Setting could not be applied, therefore print error
                ChatUtil.printChat(chatPrefix + "ERROR" + ": Could not apply the value '" + objText + "' to "
                        + module.getName() + "'s " + setting.getName());
            }
        }
    }

    /**
     * Gets the setting with the given name. If a full name is not provided, it
     * guesses what was intended by checking if the given text is the beginning
     * of an existing setting's name.
     *
     * @param map
     * @param settingText
     * @return
     */
    private Setting getSetting(SettingsMap map, String settingText) {
        settingText = settingText.toUpperCase();
        if (map.containsKey(settingText)) {
            return map.get(settingText);
        } else {
            for (String key : map.keySet()) {
                if (key.startsWith(settingText)) {
                    return map.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Print out the information of a given Setting.
     */
    private void printSetting(Setting setting) {
        // Print usage if the setting is not found
        if (setting == null) {
            printUsage();
            return;
        }
        // Get the type as a string
        String typeStr = setting.getType() == null ? setting.getValue().getClass().getSimpleName()
                : setting.getType().getTypeName();
        if (typeStr.contains(".")) {
            typeStr = typeStr.substring(typeStr.lastIndexOf(".") + 1);
        }

        String settingText = EnumChatFormatting.GRAY + setting.getName().toLowerCase() + "\2478: "
                + EnumChatFormatting.RESET + EnumChatFormatting.RED + setting.getValue();
        if (setting.getValue().getClass().equals(Boolean.class)) {
            settingText = EnumChatFormatting.GRAY + setting.getName().toLowerCase() + "\2478: "
                    + EnumChatFormatting.RESET + EnumChatFormatting.RED + (Boolean.parseBoolean(setting.getValue().toString()) ? "True" : "False");
        }
        // Print formatted string with information
        if (setting.getValue() instanceof Options) {
            settingText = EnumChatFormatting.GRAY + setting.getName().toLowerCase() + "\2478: "
                    + EnumChatFormatting.RESET + EnumChatFormatting.RED + ((Options) setting.getValue()).getSelected() + EnumChatFormatting.DARK_GRAY + " " + Arrays.toString(((Options) setting.getValue()).getOptions());
        }
        ChatUtil.printChat(settingText);
    }

    @Override
    public String getUsage() {
        return "set <Module> " + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Option> "
                + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Value>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
