package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.misc.ChatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cool1 on 1/13/2017.
 */
public class Color extends Command {

    private static final File COLOR_DIR = FileUtils.getConfigFile("Colors");

    public static void saveStatus() {
        List<String> fileContent = new ArrayList<>();
        fileContent.add(String.format("%s:%s:%s:%s:%s", "hudColor", ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), ColorManager.hudColor.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "xhairColor", ColorManager.ch.getRed(), ColorManager.ch.getGreen(), ColorManager.ch.getBlue(), ColorManager.ch.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "espColor", ColorManager.esp.getRed(), ColorManager.esp.getGreen(), ColorManager.esp.getBlue(), ColorManager.esp.getAlpha()));
        FileUtils.write(COLOR_DIR, fileContent, true);
    }

    public static void loadStatus() {
        try {
            List<String> fileContent = FileUtils.read(COLOR_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String object = split[0];
                int red = Integer.parseInt(split[1]);
                int green = Integer.parseInt(split[2]);
                int blue = Integer.parseInt(split[3]);
      
                int alpha = Integer.parseInt(split[4]);
                switch (object) {
                    case "hudColor":
                        ColorManager.hudColor.updateColors(red, green, blue);
                        break;
                    case "xhairColor":
                        ColorManager.ch.updateColors(red, green, blue);
                        break;
                    case "espColor":
                        ColorManager.esp.updateColors(red, green, blue, alpha);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Color(String[] names, String description) {
        super(names, description);
        loadStatus();
    }


    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }

        if (args.length < 2) {
            printUsage();
            return;
        }
        String[] color = args[1].split(":");
        if (color.length < 4) {
            printUsage();
            return;
        }
        int red = Integer.parseInt(color[0]);
        int green = Integer.parseInt(color[1]);
        int blue = Integer.parseInt(color[2]);
        int alpha = Integer.parseInt(color[3]);
        switch (args[0]) {
            case "hc":
                ColorManager.hudColor.updateColors(red, green, blue, alpha);
                ChatUtil.printChat(chatPrefix + "Color set: \247c" + red + "  \247a" + green + "  \247b" + blue + "  \247f" + alpha);
                saveStatus();
                break;
            default:
                printUsage();
        }
    }

    @Override
    public String getUsage() {
        return "object <fv | ev | fi | ei | hc > color <r:g:b:a>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
