/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package Blizzard.Commands.commands;

import org.lwjgl.input.Keyboard;

import Blizzard.Blizzard;
import Blizzard.Commands.Command;
import Blizzard.Files.FileManager;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;

public class Bind
extends Command {
    @Override
    public String getAlias() {
        return "bind";
    }

    @Override
    public String getSyntax() {
        return "-bind mod key | -bind del mod";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        args[1] = args[1].toUpperCase();
        int key = Keyboard.getKeyIndex((String)args[1]);
        for (Mod m : ModManager.getMods()) {
            if (!args[0].equalsIgnoreCase(m.getName())) continue;
            m.setKey(Keyboard.getKeyIndex((String)Keyboard.getKeyName((int)key)));
            Blizzard.addChatMessage(String.valueOf(args[0]) + "\u00a77 has been binded to \u00a77" + args[1]);
        }
        if (args[0].equalsIgnoreCase("del")) {
            for (Mod m : ModManager.getMods()) {
                if (!m.getName().equalsIgnoreCase(args[1])) continue;
                m.setKey(0);
                Blizzard.addChatMessage(String.valueOf(args[1]) + " has been unbinded");
            }
        }
        Blizzard.getInstance();
        Blizzard.fileManager.saveFiles();
    }
}

