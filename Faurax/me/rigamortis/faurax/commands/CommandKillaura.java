package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.combat.*;
import me.rigamortis.faurax.*;

public class CommandKillaura extends Command
{
    public CommandKillaura() {
        super("killaura", new String[] { "aura", "ka" });
    }
    
    @Argument
    protected String killauraHelp() {
        final String help = "AttackDelay §a<§fInt§a>§f, AimType §a<§f0 - 1§a>§f, AimSpeed §a<§fFloat§a>§f, AutoBlock §a<§f0 - 1§a>§f, RayTrace §a<§f0 - 1§a>§f, Teleport §a<§f0 - 1§a>§f, Range §a<§fFloat§a>§f, Fov §a<§fFloat§a>§f, Invisibles §a<§f0 - 1§a>§f, Players §a<§f0 - 1§a>§f, Mobs §a<§f0 - 1§a>§f, Animals §a<§f0 - 1§a>§f, Target §a<§f0 - 1§a>§f, Silent §a<§f0 - 1§a>§f, AutoAttack §a<§f0 - 1§a>§f, TicksExisted §a<§fInt§a>§f";
        return help;
    }
    
    @Argument(handles = { "attackdelay", "ad" })
    protected String killauraAttackDelay(final int delay) {
        KillAura.attackDelay.setFloatValue(delay);
        Client.getConfig().saveConfig();
        return "Killaura §bAttack Delay §fset to §6" + delay + "§f";
    }
    
    @Argument(handles = { "aimtype", "at" })
    protected String killauraAimType(final int type) {
        if (type > 1 || type < 0) {
            return "Error, value can only be 0 or 1";
        }
        final String name = (type == 0) ? "Instant" : "Smooth";
        KillAura.aimType.setSelectedOption(name);
        Client.getConfig().saveConfig();
        return "Killaura §bAimtype §fset to §6" + name + "§f";
    }
    
    @Argument(handles = { "aimspeed", "as" })
    protected String killauraAimSpeed(final float speed) {
        KillAura.aimSpeed.setFloatValue(speed);
        Client.getConfig().saveConfig();
        return "Killaura §bSmooth Aim Speed §fset to §6" + speed + "§f";
    }
    
    @Argument(handles = { "autoblock", "ab" })
    protected String killauraAutoBlock(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.autoBlock.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bAuto Block §fmode set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "raytrace", "rt" })
    protected String killauraRayTrace(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.rayTrace.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bRay Trace §fmode set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "teleport", "tp" })
    protected String killauraTeleport(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.tpAura.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bTeleport §fmode set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "range", "r", "reach" })
    protected String killauraRange(final float dist) {
        KillAura.range.setFloatValue(dist);
        Client.getConfig().saveConfig();
        return "Killaura §bRange §fset to §6" + dist + "§f";
    }
    
    @Argument(handles = { "fieldofview", "fov" })
    protected String killauraFieldOfView(final int fov) {
        if (fov > 360 || fov < 1) {
            return "Error, value can only be 360 - 1";
        }
        KillAura.fov.setFloatValue(fov);
        Client.getConfig().saveConfig();
        return "Killaura §bFov §fset to §6" + fov + "§f";
    }
    
    @Argument(handles = { "invisibles", "invis" })
    protected String killauraInvisibles(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.invisibles.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bInvisibles §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "players", "p" })
    protected String killauraPlayers(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.players.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bPlayers §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "mobs", "m" })
    protected String killauraMobs(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.mobs.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bMobs §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "animals", "a" })
    protected String killauraAnimals(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.animals.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bAnimals §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "target", "t" })
    protected String killauraTarget(final int target) {
        if (target > 1 || target < 0) {
            return "Error, value can only be 0 or 1";
        }
        final String targetType = (target == 0) ? "Single" : "Multi";
        KillAura.target.setSelectedOption(targetType);
        Client.getConfig().saveConfig();
        return "Killaura §bTarget §fset to §6" + targetType + "§f";
    }
    
    @Argument(handles = { "silent", "s" })
    protected String killauraSilent(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.silent.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bSilent §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "autoattack", "aa" })
    protected String killauraAutoAttack(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        KillAura.autoAttack.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Killaura §bAuto Attack §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "ticksexisted", "te" })
    protected String killauraTicksExisted(final int ticks) {
        KillAura.ticksExisted.setIntValue(ticks);
        Client.getConfig().saveConfig();
        return "Killaura §bTicks Existed §fset to §6" + ticks + "§f";
    }
}
