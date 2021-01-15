// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command;

import java.util.Iterator;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.commands.Bind;
import me.CheerioFX.FusionX.command.commands.Script;
import me.CheerioFX.FusionX.command.commands.SaveLoc;
import me.CheerioFX.FusionX.command.commands.Damage;
import me.CheerioFX.FusionX.command.commands.GhostClientSettings;
import me.CheerioFX.FusionX.command.commands.Vclip;
import me.CheerioFX.FusionX.command.commands.InstantStopSettings;
import me.CheerioFX.FusionX.command.commands.SpamBot;
import me.CheerioFX.FusionX.command.commands.Toggle;
import me.CheerioFX.FusionX.command.commands.Prefix;
import me.CheerioFX.FusionX.command.commands.Say;
import me.CheerioFX.FusionX.command.commands.Help;
import java.util.ArrayList;

public class CommandManager
{
    private ArrayList<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.addCommand(new Help());
        this.addCommand(new Say());
        this.addCommand(new Prefix());
        this.addCommand(new Toggle());
        this.addCommand(new SpamBot());
        this.addCommand(new InstantStopSettings());
        this.addCommand(new Vclip());
        this.addCommand(new GhostClientSettings());
        this.addCommand(new Damage());
        this.addCommand(new SaveLoc());
        this.addCommand(new Script());
        this.addCommand(new Bind());
    }
    
    public void addCommand(final Command c) {
        this.commands.add(c);
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
    
    public void callCommand(final String input) {
        final String[] split = input.split(" ");
        final String command = split[0];
        final String args = input.substring(command.length()).trim();
        for (final Command c : this.getCommands()) {
            if (c.getAlias().equalsIgnoreCase(command)) {
                try {
                    c.onCommand(args, args.split(" "));
                }
                catch (Exception e) {
                    FusionX.addChatMessage("Wrong Command Usage!");
                    FusionX.addChatMessage(c.getSyntax());
                }
                return;
            }
        }
        FusionX.addChatMessage("Unknown Command! Try " + FusionX.prefix + "help For A List Of Commands.");
    }
}
