package cn.kody.debug.command.commands;

import cn.kody.debug.command.Command;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.PlayerUtil;

public class CommandToggle
extends Command {
    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs(".toggle <mod>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
            PlayerUtil.tellDebugPlayer(this.getArgs());
        } else {
            boolean found = false;
            for (Mod mod : ModManager.getModList()) {
                if (!args[1].equalsIgnoreCase(mod.getName())) continue;
                try {
                    mod.set(!mod.isEnabled());
                }
                catch (Exception var6) {
                    var6.printStackTrace();
                }
                found = true;
                PlayerUtil.tellDebugPlayer(mod.getName() + " was toggled");
                break;
            }
            if (!found) {
                PlayerUtil.tellDebugPlayer("Cannot find Module : " + args[1]);
            }
        }
    }
}

