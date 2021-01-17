package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.movement.*;
import me.rigamortis.faurax.*;

public class CommandFlight extends Command
{
    public CommandFlight() {
        super("fly", new String[] { "f", "flight" });
    }
    
    @Argument
    protected String flyHelp() {
        final String help = "Speed §a<§fFloat§a>§f, Antikick, §a<§f0 - 1§a>§f Bypass §a<§f0 - 2§a>§f";
        return help;
    }
    
    @Argument(handles = { "speed", "s" })
    protected String flySpeed(final float speed) {
        Flight.speed.setFloatValue(speed);
        Client.getConfig().saveConfig();
        return "Fly §bSpeed §fset to §6" + speed + "§f";
    }
    
    @Argument(handles = { "antikick", "ak" })
    protected String flyAntiKick(final int noKick) {
        if (noKick > 1 || noKick < 0) {
            return "Error, value can only be 0 or 1";
        }
        Flight.antiKick.setBooleanValue(noKick == 1);
        Client.getConfig().saveConfig();
        return "Fly §bAnti kick §fmode set to §6" + noKick + "§f";
    }
    
    @Argument(handles = { "bypass", "b" })
    protected String flyNocheat(final int bypass) {
        if (bypass > 2 || bypass < 0) {
            return "Error, value can only be 0 - 2";
        }
        String type = "";
        if (bypass == 0) {
            type = "Normal";
        }
        if (bypass == 1) {
            type = "MotionY";
        }
        if (bypass == 2) {
            type = "AAC";
        }
        Flight.types.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Fly §bBypass §fmode set to §6" + type + "§f";
    }
}
