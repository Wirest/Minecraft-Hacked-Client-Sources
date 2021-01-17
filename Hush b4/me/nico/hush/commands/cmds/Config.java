// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import me.nico.hush.Client;
import me.nico.hush.commands.Command;

public class Config extends Command
{
    public Config() {
        super("config", "to load a config.");
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            Command.messageWithPrefix(String.valueOf(Client.instance.commandManager.Chat_Prefix) + "config §f<config> §7/ §flist");
            return;
        }
        if (args[0].equalsIgnoreCase("CubeCraft") || args[0].equalsIgnoreCase("MC-Central") || args[0].equalsIgnoreCase("MinePlex") || args[0].equalsIgnoreCase("NeruxVace") || args[0].equalsIgnoreCase("HiveMC") || args[0].equalsIgnoreCase("AAC")) {
            Command.messageWithPrefix(" ");
            Command.messageWithPrefix("The §f" + args[0] + " §7config has been loaded.");
            Command.messageWithPrefix(" ");
        }
        else if (!args[0].equalsIgnoreCase("list")) {
            Command.messageWithPrefix("§cUnknown config.");
        }
        if (args[0].equalsIgnoreCase("list")) {
            Command.messageWithPrefix("§fLast updated on§7: §a10/17/20");
            Command.messageWithPrefix("");
            Command.messageWithPrefix("§cOffline §9- §fGommeHD");
            Command.messageWithPrefix("§aOnline §9- §fCubeCraft");
            Command.messageWithPrefix("§aOnline §9- §fMC-Central");
            Command.messageWithPrefix("§aOnline §9- §fNeruxVace");
            Command.messageWithPrefix("§aOnline §9- §fMinePlex");
            Command.messageWithPrefix("§aOnline §9- §fHiveMC");
            Command.messageWithPrefix("§aOnline §9- §fAAC");
        }
        else if (args[0].equalsIgnoreCase("CubeCraft")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("CubeCraft");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(3.48);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(12.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("CriticalsMode").setValString("Jump");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("CC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("NCP");
            Client.instance.settingManager.getSettingByName("NoFallMode").setValString("CC");
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Egg");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("CC");
            Client.instance.settingManager.getSettingByName("GlideMode").setValString("CC");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("CC-OnGround");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("StepMode").setValString("CC");
            Client.instance.settingManager.getSettingByName("SpeedTimer").setValDouble(1.0);
            Client.instance.settingManager.getSettingByName("BhopMotion").setValDouble(1.0);
            Client.instance.settingManager.getSettingByName("MotionY").setValDouble(1.0);
            Client.instance.settingManager.getSettingByName("StepHeight").setValDouble(3.5);
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(0.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoJoin").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("AntiKnockBack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (!Client.instance.moduleManager.getModuleName("Criticals").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("Step").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("Killaura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Killaura");
            }
            if (Client.instance.moduleManager.getModuleName("Velocity").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
        }
        else if (args[0].equalsIgnoreCase("Test")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("GommeHD");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(3.65);
            Client.instance.settingManager.getSettingByName("KillAuraTicks").setValDouble(4.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("VelocityMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("Bypass");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("Custom");
            Client.instance.settingManager.getSettingByName("NoFallMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("StepMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Bed");
            Client.instance.settingManager.getSettingByName("GlideMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("SlowDownValue").setValDouble(0.5);
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(2.0);
            Client.instance.settingManager.getSettingByName("SpeedTimer").setValDouble(1.0);
            Client.instance.settingManager.getSettingByName("BhopMotion").setValDouble(1.0);
            Client.instance.settingManager.getSettingByName("MotionY").setValDouble(1.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("Velocity").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("Step").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("AntiKnockBack").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (Client.instance.moduleManager.getModuleName("Killaura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Killaura");
            }
            if (Client.instance.moduleManager.getModuleName("Criticals").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("FastUse").isEnabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (Client.instance.moduleManager.getModuleName("FastLadder").isEnabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
        }
        else if (args[0].equalsIgnoreCase("MC-Central")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(4.3);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(8.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("NCP");
            Client.instance.settingManager.getSettingByName("NoFallMode").setValString("Intave");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Cake");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("GlideMode").setValString("MC-Central");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("Strafe");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Motion");
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(1.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("AntiKnockBack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("IceSpeed").isDisabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("KillAura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle KillAura");
            }
            if (Client.instance.moduleManager.getModuleName("Criticals").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (Client.instance.moduleManager.getModuleName("Velocity").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
        }
        else if (args[0].equalsIgnoreCase("NeruxVace")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(6.0);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(5.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Shop").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("CriticalsMode").setValString("Jump");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("VelocityMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Bed");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(0.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("Velocity").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (!Client.instance.moduleManager.getModuleName("Criticals").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("KillAura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle KillAura");
            }
            if (Client.instance.moduleManager.getModuleName("AntiKnockBack").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
            if (Client.instance.moduleManager.getModuleName("InvCleaner").isEnabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
        }
        else if (args[0].equalsIgnoreCase("MinePlex")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(3.75);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(8.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("CriticalsMode").setValString("Jump");
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("NCP");
            Client.instance.settingManager.getSettingByName("NoFallMode").setValString("New");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Cake");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(0.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("AntiKnockBack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (!Client.instance.moduleManager.getModuleName("Criticals").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("NoFall").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("Velocity").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (Client.instance.moduleManager.getModuleName("Killaura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Killaura");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
        }
        else if (args[0].equalsIgnoreCase("HiveMC")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("Normal");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(5.0);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(125.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("CriticalsMode").setValString("Jump");
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Slow");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Bed");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("MC-Central");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(1.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("AntiKnockBack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (!Client.instance.moduleManager.getModuleName("Criticals").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("FastPlace").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("Velocity").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (Client.instance.moduleManager.getModuleName("Killaura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Killaura");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
        }
        else if (args[0].equalsIgnoreCase("AAC")) {
            Client.instance.settingManager.getSettingByName("KillAuraMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("KillAuraRange").setValDouble(4.2);
            Client.instance.settingManager.getSettingByName("KillAuraDelay").setValDouble(5.0);
            Client.instance.settingManager.getSettingByName("AutoBlock").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Walls").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("AntiBots").setValBoolean(false);
            Client.instance.settingManager.getSettingByName("Mobs").setValBoolean(true);
            Client.instance.settingManager.getSettingByName("ChestStealerMode").setValString("Slow");
            Client.instance.settingManager.getSettingByName("AutoArmorMode").setValString("OpenInv");
            Client.instance.settingManager.getSettingByName("VelocityMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("FastUseMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SlowDownMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("DestroyerMode").setValString("Bed");
            Client.instance.settingManager.getSettingByName("FastLadderMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("SpeedMode").setValString("AAC");
            Client.instance.settingManager.getSettingByName("BhopMode").setValString("Legit");
            Client.instance.settingManager.getSettingByName("Delay").setValDouble(2.0);
            if (!Client.instance.moduleManager.getModuleName("AutoConfig").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoConfig");
            }
            if (!Client.instance.moduleManager.getModuleName("NameProtect").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NameProtect");
            }
            if (!Client.instance.moduleManager.getModuleName("Velocity").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Velocity");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoArmor").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoArmor");
            }
            if (!Client.instance.moduleManager.getModuleName("SlowDown").isDisabled()) {
                Client.instance.commandManager.execute(".toggle SlowDown");
            }
            if (!Client.instance.moduleManager.getModuleName("FastUse").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastUse");
            }
            if (!Client.instance.moduleManager.getModuleName("Sprint").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Sprint");
            }
            if (!Client.instance.moduleManager.getModuleName("FastLadder").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FastLadder");
            }
            if (!Client.instance.moduleManager.getModuleName("InvCleaner").isDisabled()) {
                Client.instance.commandManager.execute(".toggle InvCleaner");
            }
            if (!Client.instance.moduleManager.getModuleName("TNTBlock").isDisabled()) {
                Client.instance.commandManager.execute(".toggle TNTBlock");
            }
            if (!Client.instance.moduleManager.getModuleName("AutoRespawn").isDisabled()) {
                Client.instance.commandManager.execute(".toggle AutoRespawn");
            }
            if (!Client.instance.moduleManager.getModuleName("ESP").isDisabled()) {
                Client.instance.commandManager.execute(".toggle ESP");
            }
            if (!Client.instance.moduleManager.getModuleName("WallHack").isDisabled()) {
                Client.instance.commandManager.execute(".toggle WallHack");
            }
            if (!Client.instance.moduleManager.getModuleName("FullBright").isDisabled()) {
                Client.instance.commandManager.execute(".toggle FullBright");
            }
            if (!Client.instance.moduleManager.getModuleName("NoBob").isDisabled()) {
                Client.instance.commandManager.execute(".toggle NoBob");
            }
            if (!Client.instance.moduleManager.getModuleName("Items").isDisabled()) {
                Client.instance.commandManager.execute(".toggle Items");
            }
            if (Client.instance.moduleManager.getModuleName("AntiKnockBack").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AntiKnockBack");
            }
            if (Client.instance.moduleManager.getModuleName("Killaura").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Killaura");
            }
            if (Client.instance.moduleManager.getModuleName("Criticals").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Criticals");
            }
            if (Client.instance.moduleManager.getModuleName("ChestStealer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ChestStealer");
            }
            if (Client.instance.moduleManager.getModuleName("NoFall").isEnabled()) {
                Client.instance.commandManager.execute(".toggle NoFall");
            }
            if (Client.instance.moduleManager.getModuleName("Speed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Speed");
            }
            if (Client.instance.moduleManager.getModuleName("Bhop").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Bhop");
            }
            if (Client.instance.moduleManager.getModuleName("IceSpeed").isEnabled()) {
                Client.instance.commandManager.execute(".toggle IceSpeed");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
            if (Client.instance.moduleManager.getModuleName("LongJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle LongJump");
            }
            if (Client.instance.moduleManager.getModuleName("HighJump").isEnabled()) {
                Client.instance.commandManager.execute(".toggle HighJump");
            }
            if (Client.instance.moduleManager.getModuleName("Glide").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Glide");
            }
            if (Client.instance.moduleManager.getModuleName("Fly").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Fly");
            }
            if (Client.instance.moduleManager.getModuleName("BugUp").isEnabled()) {
                Client.instance.commandManager.execute(".toggle BugUp");
            }
            if (Client.instance.moduleManager.getModuleName("ScaffoldWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle ScaffoldWalk");
            }
            if (Client.instance.moduleManager.getModuleName("SafeWalk").isEnabled()) {
                Client.instance.commandManager.execute(".toggle SafeWalk");
            }
            if (Client.instance.moduleManager.getModuleName("Tower").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Tower");
            }
            if (Client.instance.moduleManager.getModuleName("FastPlace").isEnabled()) {
                Client.instance.commandManager.execute(".toggle FastPlace");
            }
            if (Client.instance.moduleManager.getModuleName("Spammer").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Spammer");
            }
            if (Client.instance.moduleManager.getModuleName("AutoJoin").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoJoin");
            }
        }
    }
}
