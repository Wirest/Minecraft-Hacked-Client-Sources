/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands;

import java.util.ArrayList;
import me.razerboy420.weepcraft.commands.CommandManager;
import me.razerboy420.weepcraft.module.Module;

public abstract class Command {
    public String[] names;
    public String desc;
    public String syntax;
    public Module mod;

    public Command(String[] names, String desc, String syntax) {
        this.names = names;
        this.desc = desc;
        this.syntax = syntax;
        CommandManager.cmds.add(this);
    }

    public boolean runCommand(String command, String[] args) {
        return false;
    }

    public boolean runCommandMod(String command, String[] args, Module mod) {
        return false;
    }
}

