/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.util.ChatUtils;
import me.aristhena.lucid.util.FileUtils;

@Com(names = { "cleaner, ic" })
public class InventoryCleaner extends Command
{
    private static final File INV_CLEANER_DIR;
    
    static {
        INV_CLEANER_DIR = FileUtils.getConfigFile("InventoryCleaner List");
    }
    
    @Override
    public void runCommand(final String[] args) {
        try {
            System.out.println("OHHH");
            if (args.length < 2) {
                ChatUtils.sendClientMessage(this.getHelp());
                return;
            }
            if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("a")) {
                if (args.length < 3) {
                    ChatUtils.sendClientMessage(this.getHelp());
                    return;
                }
                final int id = Integer.parseInt(args[2]);
                if (me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.contains(id)) {
                    ChatUtils.sendClientMessage(String.valueOf(id) + " is already on the InventoryCleaner list.");
                }
                else {
                    me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.add(id);
                    ChatUtils.sendClientMessage("Added " + id + " to InventoryCleaner list.");
                }
            }
            else if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("d")) {
                if (args.length < 3) {
                    ChatUtils.sendClientMessage(this.getHelp());
                    return;
                }
                final int id = Integer.parseInt(args[2]);
                if (me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.contains(id)) {
                    if (me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.remove((Object)id)) {
                        ChatUtils.sendClientMessage("Deleted " + id + " from InventoryCleaner list.");
                    }
                }
                else {
                    ChatUtils.sendClientMessage(String.valueOf(id) + " is not on the InventoryCleaner list.");
                }
                save();
            }
            else if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("l")) {
                boolean hasItems = false;
                for (final int i : me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs) {
                    ChatUtils.sendClientMessage(new StringBuilder(String.valueOf(i)).toString());
                    hasItems = true;
                }
                if (!hasItems) {
                    ChatUtils.sendClientMessage("There are currently no items on the InventoryCleaner list.");
                }
            }
            else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("c")) {
                me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.clear();
                ChatUtils.sendClientMessage("Cleared InventoryCleaner list.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getHelp() {
        return "Cleaner - cleaner <ic>  (add <a> | del <d> | list <l> | clear <c>) [id].";
    }
    
    public static void save() {
        final File file = InventoryCleaner.INV_CLEANER_DIR;
        final List<String> content = new ArrayList<String>();
        for (final int i : me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs) {
            final int id = i;
            content.add(String.format("%s", id));
        }
        FileUtils.write(file, (ArrayList)content, true);
    }
    
    public static void load() {
        final File file = InventoryCleaner.INV_CLEANER_DIR;
        final List<String> content = (List<String>)(ArrayList)FileUtils.read(file);
        for (final String intS : content) {
            final int i = Integer.parseInt(intS);
            me.aristhena.lucid.modules.misc.InventoryCleaner.cleanIDs.add(i);
        }
    }
}