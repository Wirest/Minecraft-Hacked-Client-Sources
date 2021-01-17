/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.IChatComponent;

public class Teams
extends Mod {
    public Teams() {
        super("Teams", Mod.Category.MISCELLANEOUS, Colors.BLUE.c);
    }

    public static boolean isEnemy(EntityPlayer e) {
        if (ModManager.getModByName("Teams").isEnabled() && (Minecraft.getMinecraft().thePlayer.getTeam().equals(e.getTeam()) || Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().startsWith("\u00a7") && e.getDisplayName().getFormattedText().startsWith("\u00a7") && Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0, 2).equals(e.getDisplayName().getFormattedText().substring(0, 2)))) {
            return true;
        }
        return false;
    }

    public static String getPrefix(EntityPlayer p) {
        String tname = p.getDisplayName().getFormattedText();
        String name = p.getName();
        if (tname.length() > 1 + name.length()) {
            return tname.substring(0, tname.indexOf(name));
        }
        return "";
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Teams Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Teams Enable", ClientNotification.Type.SUCCESS);
    }
}

