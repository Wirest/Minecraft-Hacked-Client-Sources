/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Commands.commands;

import Blizzard.Blizzard;
import Blizzard.Commands.Command;

public class Help
extends Command {
    @Override
    public String getAlias() {
        return "help";
    }

    @Override
    public String getSyntax() {
        return "-help";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        Blizzard.addChatMessage("Add Bind: -bind [Mod] [Key]");
        Blizzard.addChatMessage("Remove Bind: -bind del [Key]");
    }
}

