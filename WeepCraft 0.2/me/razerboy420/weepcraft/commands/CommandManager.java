/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands;

import java.util.ArrayList;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.commands.cmds.CommandAllSettings;
import me.razerboy420.weepcraft.commands.cmds.CommandForward;
import me.razerboy420.weepcraft.commands.cmds.CommandFriend;
import me.razerboy420.weepcraft.commands.cmds.CommandNCPClip;
import me.razerboy420.weepcraft.commands.cmds.CommandRelog;
import me.razerboy420.weepcraft.commands.cmds.CommandSay;
import me.razerboy420.weepcraft.commands.cmds.CommandScrape;
import me.razerboy420.weepcraft.commands.cmds.CommandSkinSteal;
import me.razerboy420.weepcraft.commands.cmds.CommandVClip;

public class CommandManager {
    public static ArrayList<Command> cmds = new ArrayList();

    public CommandManager() {
        CommandManager.init();
    }

    public static void init() {
        cmds.clear();
        cmds.add(new CommandAllSettings());
        cmds.add(new CommandFriend());
        cmds.add(new CommandRelog());
        cmds.add(new CommandSay());
        cmds.add(new CommandSkinSteal());
        cmds.add(new CommandVClip());
        cmds.add(new CommandForward());
        cmds.add(new CommandNCPClip());
        cmds.add(new CommandScrape());
    }
}

