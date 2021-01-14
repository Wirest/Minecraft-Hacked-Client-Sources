package com.mentalfrostbyte.jello.util;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class ServerHook {
    private static String currentServerIP = "127.0.0.1:25565";
    private static ServerListEntryNormal lastServer;

    

    

    public static void joinLastServer(GuiMultiplayer guiMultiplayer) {
        if (lastServer == null) {
            return;
        }
        currentServerIP = ServerHook.lastServer.getServerData().serverIP;
        if (!currentServerIP.contains(":")) {
            currentServerIP = String.valueOf(currentServerIP) + ":25565";
        }
        guiMultiplayer.connectToServer(lastServer.getServerData());
    }

    public static void reconnectToLastServer(GuiScreen prevScreen) {
        if (lastServer == null) {
            return;
        }
        currentServerIP = ServerHook.lastServer.getServerData().serverIP;
        if (!currentServerIP.contains(":")) {
            currentServerIP = String.valueOf(currentServerIP) + ":25565";
        }
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new GuiConnecting(prevScreen, mc, lastServer.getServerData()));
    }

    public static void updateLastServerFromServerlist(GuiListExtended.IGuiListEntry entry, GuiMultiplayer guiMultiplayer) {
        if (entry instanceof ServerListEntryNormal) {
            currentServerIP = ((ServerListEntryNormal)entry).getServerData().serverIP;
            if (!currentServerIP.contains(":")) {
                currentServerIP = String.valueOf(currentServerIP) + ":25565";
            }
            lastServer = (ServerListEntryNormal)(guiMultiplayer.serverListSelector.func_148193_k() < 0 ? null : guiMultiplayer.serverListSelector.getListEntry(guiMultiplayer.serverListSelector.func_148193_k()));
        } else if (entry instanceof ServerListEntryLanDetected) {
            currentServerIP = ((ServerListEntryLanDetected)entry).getLanServer().getServerIpPort();
            lastServer = new ServerListEntryNormal(guiMultiplayer, new ServerData("LAN-Server", currentServerIP));
        }
    }

    public static void updateLastServerFromDirectConnect(GuiMultiplayer guiMultiplayer, ServerData serverData) {
        currentServerIP = serverData.serverIP;
        if (!currentServerIP.contains(":")) {
            currentServerIP = String.valueOf(currentServerIP) + ":25565";
        }
        lastServer = new ServerListEntryNormal(guiMultiplayer, serverData);
    }

    public static boolean hasLastServer() {
        if (lastServer != null) {
            return true;
        }
        return false;
    }

    public static void setCurrentIpToSingleplayer() {
        currentServerIP = "127.0.0.1:25565";
    }

    public static void setCurrentIpToLanServer(String port) {
        currentServerIP = "127.0.0.1:" + port;
    }

    public static String getCurrentServerIP() {
        return currentServerIP;
    }

    public static ServerData getLastServerData() {
        return lastServer.getServerData();
    }

}

