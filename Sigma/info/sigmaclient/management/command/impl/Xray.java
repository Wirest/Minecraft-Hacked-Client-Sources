package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.item.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arithmo on 5/3/2017 at 1:02 AM.
 */
public class Xray extends Command {

    public Xray(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                if (args.length == 2) {
                    if (StringConversions.isNumeric(args[1])) {
                        info.sigmaclient.module.impl.render.Xray.blockIDs.add(Integer.parseInt(args[1]));
                        ChatUtil.printChat(chatPrefix + "Added p" + args[1] + "] to X-Ray list.");
                    }
                } else if (mc.thePlayer.inventory.getCurrentItem() != null) {
                    Item item = mc.thePlayer.inventory.getCurrentItem().getItem();
                    int i = Item.getIdFromItem(item);
                    info.sigmaclient.module.impl.render.Xray.blockIDs.add(i);
                    ChatUtil.printChat(chatPrefix + "Added [" + Item.getIdFromItem(item) + "] to X-Ray list.");
                }
                Xray.saveIDs();
                return;
            } else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("d")) {
                if (args.length == 2) {
                    if (StringConversions.isNumeric(args[1]) && info.sigmaclient.module.impl.render.Xray.blockIDs.contains(Integer.parseInt(args[1]))) {
                        info.sigmaclient.module.impl.render.Xray.blockIDs.remove(new Integer(Integer.parseInt(args[1])));
                        ChatUtil.printChat(chatPrefix + "Removed [" + args[1] + "] from X-Ray list.");
                    }
                } else if (mc.thePlayer.inventory.getCurrentItem() != null && info.sigmaclient.module.impl.render.Xray.blockIDs.contains(Item.getIdFromItem(mc.thePlayer.inventory.getCurrentItem().getItem()))) {
                    Item item = mc.thePlayer.inventory.getCurrentItem().getItem();
                    int i = Item.getIdFromItem(item);
                    info.sigmaclient.module.impl.render.Xray.blockIDs.remove(new Integer(i));
                    ChatUtil.printChat(chatPrefix + "Removed [" + Item.getIdFromItem(item) + "] from X-Ray list.");
                    return;
                }
                Xray.saveIDs();
                return;
            } else if (args[0].equalsIgnoreCase("clear")) {
                if (!info.sigmaclient.module.impl.render.Xray.blockIDs.isEmpty()) {
                    info.sigmaclient.module.impl.render.Xray.blockIDs.clear();
                    ChatUtil.printChat(chatPrefix + "Cleared X-Ray list!");
                    Xray.saveIDs();
                }
            }
        }
        printUsage();
    }

    private static final File CLEAN_DIR = FileUtils.getConfigFile("Xray");

    public static void saveIDs() {
        List<String> fileContent = new ArrayList<>();
        for (Integer integ : info.sigmaclient.module.impl.render.Xray.blockIDs) {
            fileContent.add(integ.toString());
        }
        FileUtils.write(CLEAN_DIR, fileContent, true);
    }

    public static void loadIDs() {
        try {
            List<String> fileContent = FileUtils.read(CLEAN_DIR);
            for (String line : fileContent) {
                if (StringConversions.isNumeric(line) && !info.sigmaclient.module.impl.render.Xray.blockIDs.contains(Integer.parseInt(line))) {
                    info.sigmaclient.module.impl.render.Xray.blockIDs.add(Integer.parseInt(line));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUsage() {
        return "<add/del/clear> [Block ID]";
    }

    @Override
    public void onEvent(Event event) {

    }
}
