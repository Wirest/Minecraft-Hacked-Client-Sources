package cn.kody.debug.command.commands;

import cn.kody.debug.Client;
import cn.kody.debug.command.Command;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.utils.file.FileUtils;
import org.lwjgl.input.Keyboard;

public class CommandBind
extends Command {
    public CommandBind(String[] command) {
        super(command);
        this.setArgs(".bind <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            PlayerUtil.tellDebugPlayer(this.getArgs());
        } else {
            String mod = args[1];
            int key = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            for (Mod m : ModManager.getModList()) {
                if (!m.getName().equalsIgnoreCase(mod)) continue;
                m.setKey(key);
                PlayerUtil.tellDebugPlayer(String.valueOf(m.getName()) + " was bound to " + Keyboard.getKeyName((int)key));
                Client.instance.fileMgr.saveKeys();
                return;
            }
            PlayerUtil.tellDebugPlayer("Cannot find Module : " + mod);
        }
    }
}

