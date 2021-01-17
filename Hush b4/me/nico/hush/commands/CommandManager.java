// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands;

import java.util.Iterator;
import java.util.Arrays;
import me.nico.hush.commands.cmds.Settings;
import me.nico.hush.commands.cmds.Config;
import me.nico.hush.commands.cmds.Ign;
import me.nico.hush.commands.cmds.Clip;
import me.nico.hush.commands.cmds.Bind;
import me.nico.hush.commands.cmds.Help;
import me.nico.hush.commands.cmds.Toggle;
import java.util.ArrayList;
import java.util.List;

public class CommandManager
{
    private List<Command> commands;
    public String Chat_Prefix;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.Chat_Prefix = ".";
        this.addCommand(new Toggle());
        this.addCommand(new Help());
        this.addCommand(new Bind());
        this.addCommand(new Clip());
        this.addCommand(new Ign());
        this.addCommand(new Config());
        this.addCommand(new Settings());
    }
    
    public void addCommand(final Command cmd) {
        this.commands.add(cmd);
    }
    
    public boolean execute(String text) {
        if (!text.startsWith(this.Chat_Prefix)) {
            return false;
        }
        text = text.substring(1);
        final String[] arguments = text.split(" ");
        for (final Command cmd : this.commands) {
            if (cmd.getName().equalsIgnoreCase(arguments[0])) {
                final String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
                cmd.execute(args);
                return true;
            }
        }
        return false;
    }
}
