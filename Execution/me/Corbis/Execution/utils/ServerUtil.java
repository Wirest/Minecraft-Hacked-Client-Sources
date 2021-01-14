package me.Corbis.Execution.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;

import java.util.Iterator;

public class ServerUtil {
    public static ServerUtil.ServerResult currentServer;
    public static boolean isInPit;

    public ServerUtil() {
    }
    static Minecraft mc = Minecraft.getMinecraft();
    public static ServerData serverData;

    public static void connectToLastServer() {
        if(serverData == null)
            return;

        mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, serverData));
    }

    public static String getRemoteIp() {
        String serverIp = "Singleplayer";

        if (mc.theWorld.isRemote) {
            final ServerData serverData = mc.getCurrentServerData();
            if(serverData != null)
                serverIp = serverData.serverIP;
        }

        return serverIp;
    }


    public static enum ServerResult {
        HYPIXEL,
        CUBECRAFT,
        MINEPLEX,
        OTHER;

        private static final ServerUtil.ServerResult[] VALUES = new ServerUtil.ServerResult[]{HYPIXEL, CUBECRAFT, MINEPLEX, OTHER};

        private ServerResult() {
        }
    }
}
