/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  club.minnced.discord.rpc.DiscordEventHandlers
 *  club.minnced.discord.rpc.DiscordRPC
 *  club.minnced.discord.rpc.DiscordRichPresence
 *  club.minnced.discord.rpc.DiscordUser
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.GuiScreen
 */
package delta.utils;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;
import delta.Class23;
import delta.client.DeltaClient;
import delta.utils.Wrapper;
import java.io.IOException;
import java.net.URL;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.GuiScreen;

public class RpcUtils {
    private DiscordRPC rpc;
    private DiscordRichPresence richPresence;
    private long rpcRunTime;
    private DiscordUser discordUser;
    private int teams$;
    private DiscordEventHandlers handles$;
    private GuiScreen nerve$;

    public void _cooling(String string, String string2) {
        if (this.richPresence == null) {
            return;
        }
        this.richPresence.largeImageKey = "delta-title2";
        this.richPresence.smallImageKey = "delta-logo2";
        this.richPresence.largeImageText = "Delta b3.7";
        this.richPresence.smallImageText = "http://idelta.fr/";
        this.richPresence.startTimestamp = this.rpcRunTime;
        this.richPresence.details = string;
        this.richPresence.state = string2;
        this.rpc.Discord_UpdatePresence(this.richPresence);
    }

    public DiscordUser _vendor() {
        return this.discordUser;
    }

    public RpcUtils() {
        DeltaClient.instance.managers.eventManager.register((Object)this);
        this.rpc = DiscordRPC.INSTANCE;
        String string = null;
        try {
            string = Class23._diamond(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/d_id")).replace("\n", "");
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            string = "688377297015013486";
        }
        String string2 = "";
        this.handles$ = new DiscordEventHandlers();
        this.handles$.ready = this::_question;
        this.rpc.Discord_Initialize(string, this.handles$, true, string2);
        this.richPresence = new DiscordRichPresence();
        this._cooling("In menus", "Loading Delta...");
        Thread thread = new Thread(this::_ground, "Delta RPC");
        thread.setDaemon(true);
        thread.start();
    }

    private void _question(DiscordUser discordUser) {
        this.discordUser = discordUser;
    }

    private void _ground() {
        while (!Thread.currentThread().isInterrupted()) {
            this.rpc.Discord_RunCallbacks();
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException interruptedException) {
                this.rpc.Discord_Shutdown();
            }
        }
    }

    @EventTarget
    public void _villages(EventUpdate eventUpdate) {
        if (Wrapper.mc.currentScreen != this.nerve$) {
            this.nerve$ = Wrapper.mc.currentScreen;
            if (this.nerve$ == null) {
                this.teams$ = -999;
            }
        }
        int n = 0;
        int n2 = 0;
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            ++n;
            if (!iModule.isEnabled()) continue;
            ++n2;
        }
        if (this.teams$ != n2) {
            this.teams$ = n2;
            boolean bl = Wrapper.mc.isSingleplayer();
            this._cooling("En jeu - " + (bl ? "Solo" : Wrapper.mc.func_147104_D().serverIP), n2 + "/" + n + " modules activ\u00e9s.");
        }
    }
}

