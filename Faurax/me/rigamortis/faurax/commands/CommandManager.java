package me.rigamortis.faurax.commands;

import me.cupboard.command.executor.*;
import me.rigamortis.faurax.values.*;
import me.cupboard.command.*;

public class CommandManager extends CommandExecutor
{
    public static Value prefix;
    
    static {
        CommandManager.prefix = new Value("CMD", String.class, "Prefix", ".", new String[] { "!", "@" });
    }
    
    public CommandManager() {
        super("-");
        ValueManager.values.add(CommandManager.prefix);
        this.register(new CommandFlight());
        this.register(new CommandKillaura());
        this.register(new CommandFriend());
        this.register(new CommandCrits());
        this.register(new CommandSpeed());
        this.register(new CommandChestStealer());
        this.register(new CommandAutoFarm());
        this.register(new CommandXray());
        this.register(new CommandToggle());
        this.register(new CommandArrayList());
        this.register(new CommandPrefix());
        this.register(new CommandBind());
        this.register(new CommandVclip());
        this.register(new CommandAntiaim());
        this.register(new CommandConfig());
        this.register(new CommandTeleport());
        this.register(new CommandPhase());
        this.register(new CommandChams());
        this.register(new CommandForward());
        this.register(new CommandControl());
        this.register(new CommandWayPoint());
        this.register(new CommandSearch());
        this.register(new CommandGodmode());
        this.register(new CommandSpectate());
        this.register(new CommandRegen());
        this.register(new CommandEnchant());
        this.register(new CommandSpeedmine());
        this.register(new CommandJesus());
        this.register(new CommandMeme());
        this.register(new CommandSpam());
        this.register(new CommandESP());
    }
}
