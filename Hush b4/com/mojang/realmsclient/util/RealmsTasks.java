// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

import java.io.UnsupportedEncodingException;
import com.google.common.util.concurrent.Futures;
import javax.annotation.Nullable;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.google.common.util.concurrent.FutureCallback;
import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.realms.RealmsConnect;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.RealmsScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.LongRunningTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsTasks
{
    private static final Logger LOGGER;
    private static final int NUMBER_OF_RETRIES = 25;
    
    private static void pause(final int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e) {
            RealmsTasks.LOGGER.error("", e);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class OpenServerTask extends LongRunningTask
    {
        private final RealmsServer serverData;
        private final RealmsConfigureWorldScreen configureScreen;
        private final boolean join;
        private final RealmsScreen lastScreen;
        
        public OpenServerTask(final RealmsServer realmsServer, final RealmsConfigureWorldScreen configureWorldScreen, final RealmsScreen lastScreen, final boolean join) {
            this.serverData = realmsServer;
            this.configureScreen = configureWorldScreen;
            this.join = join;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.configure.world.opening"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                if (this.aborted()) {
                    return;
                }
                try {
                    final boolean openResult = client.open(this.serverData.id);
                    if (openResult) {
                        this.configureScreen.stateChanged();
                        this.serverData.state = RealmsServer.State.OPEN;
                        if (this.join) {
                            ((RealmsMainScreen)this.lastScreen).play(this.serverData);
                            break;
                        }
                        Realms.setScreen(this.configureScreen);
                        break;
                    }
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Failed to open server", e2);
                    this.error("Failed to open the server");
                }
            }
        }
    }
    
    public static class CloseServerTask extends LongRunningTask
    {
        private final RealmsServer serverData;
        private final RealmsConfigureWorldScreen configureScreen;
        
        public CloseServerTask(final RealmsServer realmsServer, final RealmsConfigureWorldScreen configureWorldScreen) {
            this.serverData = realmsServer;
            this.configureScreen = configureWorldScreen;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.configure.world.closing"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                if (this.aborted()) {
                    return;
                }
                try {
                    final boolean closeResult = client.close(this.serverData.id);
                    if (closeResult) {
                        this.configureScreen.stateChanged();
                        this.serverData.state = RealmsServer.State.CLOSED;
                        Realms.setScreen(this.configureScreen);
                        break;
                    }
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Failed to close server", e2);
                    this.error("Failed to close the server");
                }
            }
        }
    }
    
    public static class SwitchSlotTask extends LongRunningTask
    {
        private final long worldId;
        private final int slot;
        private final RealmsScreen lastScreen;
        private final int confirmId;
        
        public SwitchSlotTask(final long worldId, final int slot, final RealmsScreen lastScreen, final int confirmId) {
            this.worldId = worldId;
            this.slot = slot;
            this.lastScreen = lastScreen;
            this.confirmId = confirmId;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            final String title = RealmsScreen.getLocalizedString("mco.minigame.world.slot.screen.title");
            this.setTitle(title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (client.switchSlot(this.worldId, this.slot)) {
                        this.lastScreen.confirmResult(true, this.confirmId);
                        break;
                    }
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't switch world!");
                    this.error(e2.toString());
                }
            }
        }
    }
    
    public static class SwitchMinigameTask extends LongRunningTask
    {
        private final long worldId;
        private final WorldTemplate worldTemplate;
        private final RealmsConfigureWorldScreen lastScreen;
        
        public SwitchMinigameTask(final long worldId, final WorldTemplate worldTemplate, final RealmsConfigureWorldScreen lastScreen) {
            this.worldId = worldId;
            this.worldTemplate = worldTemplate;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            final String title = RealmsScreen.getLocalizedString("mco.minigame.world.starting.screen.title");
            this.setTitle(title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (client.putIntoMinigameMode(this.worldId, this.worldTemplate.id)) {
                        Realms.setScreen(this.lastScreen);
                        break;
                    }
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't start mini game!");
                    this.error(e2.toString());
                }
            }
        }
    }
    
    public static class ResettingWorldTask extends LongRunningTask
    {
        private final String seed;
        private final WorldTemplate worldTemplate;
        private final int levelType;
        private final boolean generateStructures;
        private final long serverId;
        private final RealmsScreen lastScreen;
        private String title;
        
        public ResettingWorldTask(final long serverId, final RealmsScreen lastScreen, final WorldTemplate worldTemplate) {
            this.title = RealmsScreen.getLocalizedString("mco.reset.world.resetting.screen.title");
            this.seed = null;
            this.worldTemplate = worldTemplate;
            this.levelType = -1;
            this.generateStructures = true;
            this.serverId = serverId;
            this.lastScreen = lastScreen;
        }
        
        public ResettingWorldTask(final long serverId, final RealmsScreen lastScreen, final String seed, final int levelType, final boolean generateStructures) {
            this.title = RealmsScreen.getLocalizedString("mco.reset.world.resetting.screen.title");
            this.seed = seed;
            this.worldTemplate = null;
            this.levelType = levelType;
            this.generateStructures = generateStructures;
            this.serverId = serverId;
            this.lastScreen = lastScreen;
        }
        
        public void setResetTitle(final String title) {
            this.title = title;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            this.setTitle(this.title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (this.worldTemplate != null) {
                        client.resetWorldWithTemplate(this.serverId, this.worldTemplate.id);
                    }
                    else {
                        client.resetWorldWithSeed(this.serverId, this.seed, this.levelType, this.generateStructures);
                    }
                    if (this.aborted()) {
                        return;
                    }
                    Realms.setScreen(this.lastScreen);
                    return;
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't reset world");
                    this.error(e2.toString());
                    return;
                }
            }
        }
    }
    
    public static class RealmsConnectTask extends LongRunningTask
    {
        private final RealmsConnect realmsConnect;
        private final RealmsServer data;
        private final RealmsScreen onlineScreen;
        
        public RealmsConnectTask(final RealmsScreen onlineScreen, final RealmsServer server) {
            this.onlineScreen = onlineScreen;
            this.realmsConnect = new RealmsConnect(onlineScreen);
            this.data = server;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.connect.connecting"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            boolean addressRetrieved = false;
            boolean hasError = false;
            int sleepTime = 5;
            RealmsServerAddress a = null;
            boolean tosNotAccepted = false;
            for (int i = 0; i < 20; ++i) {
                if (this.aborted()) {
                    break;
                }
                try {
                    a = client.join(this.data.id);
                    addressRetrieved = true;
                }
                catch (RetryCallException e) {
                    sleepTime = e.delaySeconds;
                }
                catch (RealmsServiceException e2) {
                    if (e2.errorCode == 6002) {
                        tosNotAccepted = true;
                        break;
                    }
                    hasError = true;
                    this.error(e2.toString());
                    RealmsTasks.LOGGER.error("Couldn't connect to world", e2);
                    break;
                }
                catch (IOException e3) {
                    RealmsTasks.LOGGER.error("Couldn't parse response connecting to world", e3);
                }
                catch (Exception e4) {
                    hasError = true;
                    RealmsTasks.LOGGER.error("Couldn't connect to world", e4);
                    this.error(e4.getLocalizedMessage());
                }
                if (addressRetrieved) {
                    break;
                }
                this.sleep(sleepTime);
            }
            if (tosNotAccepted) {
                Realms.setScreen(new RealmsTermsScreen(this.onlineScreen, this.data));
            }
            else if (!this.aborted() && !hasError) {
                if (addressRetrieved) {
                    if (this.data.resourcePackUrl != null && this.data.resourcePackHash != null) {
                        try {
                            final RealmsServerAddress finalA = a;
                            Futures.addCallback(Realms.downloadResourcePack(this.data.resourcePackUrl, this.data.resourcePackHash), new FutureCallback<Object>() {
                                @Override
                                public void onSuccess(@Nullable final Object result) {
                                    final net.minecraft.realms.RealmsServerAddress address = net.minecraft.realms.RealmsServerAddress.parseString(finalA.address);
                                    RealmsConnectTask.this.realmsConnect.connect(address.getHost(), address.getPort());
                                }
                                
                                @Override
                                public void onFailure(final Throwable t) {
                                    RealmsTasks.LOGGER.error(t);
                                    RealmsConnectTask.this.error("Failed to download resource pack!");
                                }
                            });
                        }
                        catch (Exception e5) {
                            Realms.clearResourcePack();
                            RealmsTasks.LOGGER.error(e5);
                            this.error("Failed to download resource pack!");
                        }
                    }
                    else {
                        final net.minecraft.realms.RealmsServerAddress address = net.minecraft.realms.RealmsServerAddress.parseString(a.address);
                        this.realmsConnect.connect(address.getHost(), address.getPort());
                    }
                }
                else {
                    this.error(RealmsScreen.getLocalizedString("mco.errorMessage.connectionFailure"));
                }
            }
        }
        
        private void sleep(final int sleepTimeSeconds) {
            try {
                Thread.sleep(sleepTimeSeconds * 1000);
            }
            catch (InterruptedException e1) {
                RealmsTasks.LOGGER.warn(e1.getLocalizedMessage());
            }
        }
        
        @Override
        public void abortTask() {
            this.realmsConnect.abort();
        }
        
        @Override
        public void tick() {
            this.realmsConnect.tick();
        }
    }
    
    public static class WorldCreationTask extends LongRunningTask
    {
        private final String name;
        private final String motd;
        private final long worldId;
        private final RealmsScreen lastScreen;
        
        public WorldCreationTask(final long worldId, final String name, final String motd, final RealmsScreen lastScreen) {
            this.worldId = worldId;
            this.name = name;
            this.motd = motd;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            final String title = RealmsScreen.getLocalizedString("mco.create.world.wait");
            this.setTitle(title);
            final RealmsClient client = RealmsClient.createRealmsClient();
            try {
                client.initializeWorld(this.worldId, this.name, this.motd);
                Realms.setScreen(this.lastScreen);
            }
            catch (RealmsServiceException e) {
                RealmsTasks.LOGGER.error("Couldn't create world");
                this.error(e.toString());
            }
            catch (UnsupportedEncodingException e2) {
                RealmsTasks.LOGGER.error("Couldn't create world");
                this.error(e2.getLocalizedMessage());
            }
            catch (IOException e3) {
                RealmsTasks.LOGGER.error("Could not parse response creating world");
                this.error(e3.getLocalizedMessage());
            }
            catch (Exception e4) {
                RealmsTasks.LOGGER.error("Could not create world");
                this.error(e4.getLocalizedMessage());
            }
        }
    }
}
