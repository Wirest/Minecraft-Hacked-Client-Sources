/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import java.util.ArrayList;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.alts.YggdrasilLoginBridge;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;

public class CommandRelog
extends Command {
    public CommandRelog() {
        super(new String[]{"relog"}, "Relog with a random alt from your alt list!", ".relog");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            Wrapper.getWorld().sendQuittingDisconnectingPacket();
            Wrapper.mc().loadWorld(null);
            if (YggdrasilLoginBridge.loginWithAlt(Alt.alts.get((int)((double)(Alt.alts.size() - 1) * Math.random()))) != null) {
                Wrapper.mc().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), Wrapper.mc(), Weepcraft.ip, Weepcraft.port));
            } else {
                Wrapper.tellPlayer("Error while attempting to connect.");
            }
            return true;
        }
        catch (Exception e) {
            Wrapper.tellPlayer("Error while attempting to connect.");
            return false;
        }
    }
}

