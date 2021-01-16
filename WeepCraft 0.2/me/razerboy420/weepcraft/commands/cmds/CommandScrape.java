/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Ordering
 *  com.mojang.authlib.GameProfile
 */
package me.razerboy420.weepcraft.commands.cmds;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.FileUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;

public class CommandScrape
extends Command {
    public ArrayList<String> names = new ArrayList();

    public CommandScrape() {
        super(new String[]{"scrape"}, "Puts all player names on your server in a .txt file", "\".scrape\"");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            File s = new File(Weepcraft.weepcraftDir + File.separator + "Scrape");
            this.getAllPlayers();
            FileUtils.writeFile(s + File.separator + "Scrapes.txt", this.names);
            FileUtils.openFile(s);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void getAllPlayers() {
        this.names.clear();
        NetHandlerPlayClient nethandlerplayclient = Wrapper.mc().player.connection;
        GuiPlayerTabOverlay m = new GuiPlayerTabOverlay(null, null);
        List list = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        list = list.subList(0, Math.min(list.size(), 80));
        int l3 = list.size();
        boolean flag = Wrapper.mc().isIntegratedServerRunning() || Wrapper.mc().getConnection().getNetworkManager().isEncrypted();
        boolean bl = flag;
        if (!flag) {
            Wrapper.tellPlayer("You are in single player.");
        }
        Object list1 = null;
        int k4 = 0;
        while (k4 < l3) {
            if (k4 < list.size()) {
                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(k4);
                GameProfile gameprofile = networkplayerinfo1.getGameProfile();
                try {
                    this.names.add(gameprofile.getName());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++k4;
        }
    }
}

