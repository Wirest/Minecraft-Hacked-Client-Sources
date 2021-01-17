package me.slowly.client.util.command.cmds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import me.slowly.client.Client;
import me.slowly.client.util.command.Command;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.world.IRC;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CommandIRC extends Command {
    public CommandIRC(String[] commands) {
        super(commands);
        this.setArgs(".IRC <Text>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length == 1) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
            return;
        }else {
        	  String msg = "";
              int i = 1;
              while (i < args.length) {
                  msg = String.valueOf((Object)String.valueOf((Object)msg)) + args[i] + " ";
                  ++i;
        }
              if(ModManager.getModByName("IRC").isEnabled()) {
            	  IRC.sendIRCMessage(msg,true);
              }else {
            	  Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("¡ìd[Margele-IRC]¡ìcIRCÎ´¿ªÆô£¬ÇëÏÈ¿ªÆôIRC£¡"));
              }
      }
    }
}

