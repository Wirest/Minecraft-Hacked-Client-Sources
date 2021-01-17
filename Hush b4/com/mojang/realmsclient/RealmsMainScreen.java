// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.net.UnknownHostException;
import net.minecraft.realms.Tezzelator;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import net.minecraft.realms.RealmsMth;
import com.mojang.realmsclient.util.RealmsUtil;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import java.util.concurrent.TimeUnit;
import com.mojang.realmsclient.gui.screens.RealmsBuyRealmsScreen;
import java.util.ArrayList;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.Ping;
import java.util.Iterator;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.Realms;
import com.google.common.collect.Lists;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import net.minecraft.realms.RealmsButton;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.realms.RealmsServerStatusPinger;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.dto.RealmsServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsMainScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static boolean overrideConfigure;
    private static boolean stageEnabled;
    private boolean dontSetConnectedToRealms;
    protected static final int BUTTON_BACK_ID = 0;
    protected static final int BUTTON_PLAY_ID = 1;
    protected static final int BUTTON_CONFIGURE_ID = 2;
    protected static final int BUTTON_LEAVE_ID = 3;
    protected static final int BUTTON_BUY_ID = 4;
    protected static final int RESOURCEPACK_ID = 100;
    private RealmsServer resourcePackServer;
    private static final String ON_ICON_LOCATION = "realms:textures/gui/realms/on_icon.png";
    private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
    private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
    private static final String INVITATION_ICONS_LOCATION = "realms:textures/gui/realms/invitation_icons.png";
    private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
    private static final String WORLDICON_LOCATION = "realms:textures/gui/realms/world_icon.png";
    private static final String LOGO_LOCATION = "realms:textures/gui/title/realms.png";
    private static RealmsDataFetcher realmsDataFetcher;
    private static RealmsServerStatusPinger statusPinger;
    private static final ThreadPoolExecutor THREAD_POOL;
    private static int lastScrollYPosition;
    private RealmsScreen lastScreen;
    private volatile ServerSelectionList serverSelectionList;
    private long selectedServerId;
    private RealmsButton configureButton;
    private RealmsButton leaveButton;
    private RealmsButton playButton;
    private RealmsButton buyButton;
    private String toolTip;
    private List<RealmsServer> realmsServers;
    private static final String mcoInfoUrl = "https://minecraft.net/realms";
    private volatile int numberOfPendingInvites;
    private int animTick;
    private static volatile boolean mcoEnabled;
    private static volatile boolean mcoEnabledCheck;
    private static boolean checkedMcoAvailability;
    private static volatile boolean trialsAvailable;
    private static volatile boolean createdTrial;
    private static final ReentrantLock trialLock;
    private static RealmsScreen realmsGenericErrorScreen;
    private static boolean regionsPinged;
    private boolean onLink;
    private int mindex;
    private char[] mchars;
    private int sindex;
    private char[] schars;
    
    public RealmsMainScreen(final RealmsScreen lastScreen) {
        this.dontSetConnectedToRealms = false;
        this.selectedServerId = -1L;
        this.realmsServers = (List<RealmsServer>)Lists.newArrayList();
        this.numberOfPendingInvites = 0;
        this.onLink = false;
        this.mindex = 0;
        this.mchars = new char[] { '3', '2', '1', '4', '5', '6' };
        this.sindex = 0;
        this.schars = new char[] { '9', '8', '7', '1', '2', '3' };
        this.lastScreen = lastScreen;
        this.checkIfMcoEnabled();
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.serverSelectionList.mouseEvent();
    }
    
    @Override
    public void init() {
        if (!this.dontSetConnectedToRealms) {
            Realms.setConnectedToRealms(false);
        }
        if (RealmsMainScreen.realmsGenericErrorScreen != null) {
            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen);
            return;
        }
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.postInit();
        if (this.isMcoEnabled()) {
            RealmsMainScreen.realmsDataFetcher.init();
        }
    }
    
    public void postInit() {
        this.buttonsAdd(this.playButton = RealmsScreen.newButton(1, this.width() / 2 - 154, this.height() - 52, 154, 20, RealmsScreen.getLocalizedString("mco.selectServer.play")));
        this.buttonsAdd(this.configureButton = RealmsScreen.newButton(2, this.width() / 2 + 6, this.height() - 52, 154, 20, RealmsScreen.getLocalizedString("mco.selectServer.configure")));
        this.buttonsAdd(this.leaveButton = RealmsScreen.newButton(3, this.width() / 2 - 154, this.height() - 28, 102, 20, RealmsScreen.getLocalizedString("mco.selectServer.leave")));
        this.buttonsAdd(this.buyButton = RealmsScreen.newButton(4, this.width() / 2 - 48, this.height() - 28, 102, 20, RealmsScreen.getLocalizedString("mco.selectServer.buy")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 + 58, this.height() - 28, 102, 20, RealmsScreen.getLocalizedString("gui.back")));
        this.serverSelectionList = new ServerSelectionList();
        if (RealmsMainScreen.lastScrollYPosition != -1) {
            this.serverSelectionList.scroll(RealmsMainScreen.lastScrollYPosition);
        }
        final RealmsServer server = this.findServer(this.selectedServerId);
        this.playButton.active(server != null && server.state == RealmsServer.State.OPEN && !server.expired);
        this.configureButton.active(RealmsMainScreen.overrideConfigure || (server != null && server.state != RealmsServer.State.ADMIN_LOCK && server.ownerUUID.equals(Realms.getUUID())));
        this.leaveButton.active(server != null && !server.ownerUUID.equals(Realms.getUUID()));
    }
    
    @Override
    public void tick() {
        ++this.animTick;
        if (this.noParentalConsent()) {
            Realms.setScreen(new RealmsParentalConsentScreen(this.lastScreen));
        }
        if (this.isMcoEnabled()) {
            RealmsMainScreen.realmsDataFetcher.init();
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.SERVER_LIST)) {
                final List<RealmsServer> newServers = RealmsMainScreen.realmsDataFetcher.getServers();
                boolean ownsNonExpiredRealmServer = false;
                for (final RealmsServer retrievedServer : newServers) {
                    if (this.isSelfOwnedNonExpiredServer(retrievedServer)) {
                        ownsNonExpiredRealmServer = true;
                    }
                    for (final RealmsServer oldServer : this.realmsServers) {
                        if (retrievedServer.id == oldServer.id) {
                            retrievedServer.latestStatFrom(oldServer);
                            break;
                        }
                    }
                }
                this.realmsServers = newServers;
                if (!RealmsMainScreen.regionsPinged && ownsNonExpiredRealmServer) {
                    RealmsMainScreen.regionsPinged = true;
                    this.pingRegions();
                }
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
                this.numberOfPendingInvites = RealmsMainScreen.realmsDataFetcher.getPendingInvitesCount();
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE) && !RealmsMainScreen.createdTrial) {
                RealmsMainScreen.trialsAvailable = RealmsMainScreen.realmsDataFetcher.isTrialAvailable();
            }
            RealmsMainScreen.realmsDataFetcher.markClean();
        }
    }
    
    private void pingRegions() {
        new Thread() {
            @Override
            public void run() {
                final List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
                final RealmsClient client = RealmsClient.createRealmsClient();
                final PingResult pingResult = new PingResult();
                pingResult.pingResults = regionPingResultList;
                pingResult.worldIds = RealmsMainScreen.this.getOwnedNonExpiredWorldIds();
                try {
                    client.sendPingResults(pingResult);
                }
                catch (Throwable t) {
                    RealmsMainScreen.LOGGER.warn("Could not send ping result to Realms: ", t);
                }
            }
        }.start();
    }
    
    private List<Long> getOwnedNonExpiredWorldIds() {
        final List<Long> ids = new ArrayList<Long>();
        for (final RealmsServer server : this.realmsServers) {
            if (this.isSelfOwnedNonExpiredServer(server)) {
                ids.add(server.id);
            }
        }
        return ids;
    }
    
    private boolean isMcoEnabled() {
        return RealmsMainScreen.mcoEnabled;
    }
    
    private boolean noParentalConsent() {
        return RealmsMainScreen.mcoEnabledCheck && !RealmsMainScreen.mcoEnabled;
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 1: {
                this.play(this.findServer(this.selectedServerId));
                break;
            }
            case 2: {
                this.configureClicked();
                break;
            }
            case 3: {
                this.leaveClicked();
                break;
            }
            case 4: {
                this.saveListScrollPosition();
                this.stopRealmsFetcherAndPinger();
                Realms.setScreen(new RealmsBuyRealmsScreen(this));
                break;
            }
            case 0: {
                this.stopRealmsFetcherAndPinger();
                Realms.setScreen(this.lastScreen);
                break;
            }
            default: {}
        }
    }
    
    private void createTrial() {
        if (RealmsMainScreen.createdTrial) {
            RealmsMainScreen.trialsAvailable = false;
            return;
        }
        final RealmsScreen mainScreen = this;
        new Thread("Realms-create-trial") {
            @Override
            public void run() {
                try {
                    if (!RealmsMainScreen.trialLock.tryLock(10L, TimeUnit.MILLISECONDS)) {
                        return;
                    }
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    RealmsMainScreen.trialsAvailable = false;
                    if (client.createTrial()) {
                        RealmsMainScreen.createdTrial = true;
                        RealmsMainScreen.realmsDataFetcher.forceUpdate();
                    }
                    else {
                        Realms.setScreen(new RealmsGenericErrorScreen(RealmsScreen.getLocalizedString("mco.trial.unavailable"), mainScreen));
                    }
                }
                catch (RealmsServiceException e) {
                    RealmsMainScreen.LOGGER.error("Trials wasn't available: " + e.toString());
                    Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this));
                }
                catch (IOException e2) {
                    RealmsMainScreen.LOGGER.error("Couldn't parse response when trying to create trial: " + e2.toString());
                    RealmsMainScreen.trialsAvailable = false;
                }
                catch (InterruptedException e3) {
                    RealmsMainScreen.LOGGER.error("Trial Interrupted exception: " + e3.toString());
                }
                finally {
                    if (RealmsMainScreen.trialLock.isHeldByCurrentThread()) {
                        RealmsMainScreen.trialLock.unlock();
                    }
                }
            }
        }.start();
    }
    
    private void checkIfMcoEnabled() {
        if (!RealmsMainScreen.checkedMcoAvailability) {
            RealmsMainScreen.checkedMcoAvailability = true;
            new Thread("MCO Availability Checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final RealmsClient.CompatibleVersionResponse versionResponse = client.clientCompatible();
                        if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OUTDATED)) {
                            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, true));
                            return;
                        }
                        if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OTHER)) {
                            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, false));
                            return;
                        }
                    }
                    catch (RealmsServiceException e) {
                        RealmsMainScreen.checkedMcoAvailability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e.toString());
                        if (e.httpResultCode == 401) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen);
                        }
                        Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen));
                        return;
                    }
                    catch (IOException e2) {
                        RealmsMainScreen.checkedMcoAvailability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e2.getMessage());
                        Realms.setScreen(new RealmsGenericErrorScreen(e2.getMessage(), RealmsMainScreen.this.lastScreen));
                        return;
                    }
                    boolean retry = false;
                    for (int i = 0; i < 3; ++i) {
                        try {
                            final Boolean result = client.mcoEnabled();
                            if (result) {
                                RealmsMainScreen.LOGGER.info("Realms is available for this user");
                                RealmsMainScreen.mcoEnabled = true;
                            }
                            else {
                                RealmsMainScreen.LOGGER.info("Realms is not available for this user");
                                RealmsMainScreen.mcoEnabled = false;
                            }
                            RealmsMainScreen.mcoEnabledCheck = true;
                        }
                        catch (RetryCallException e5) {
                            retry = true;
                        }
                        catch (RealmsServiceException e3) {
                            RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e3.toString());
                        }
                        catch (IOException e4) {
                            RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e4.getMessage());
                        }
                        if (!retry) {
                            break;
                        }
                        try {
                            Thread.sleep(5000L);
                        }
                        catch (InterruptedException e6) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }.start();
        }
    }
    
    private void switchToStage() {
        if (!RealmsMainScreen.stageEnabled) {
            new Thread("MCO Stage Availability Checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final Boolean result = client.stageAvailable();
                        if (result) {
                            RealmsMainScreen.this.stopRealmsFetcherAndPinger();
                            RealmsClient.switchToStage();
                            RealmsMainScreen.LOGGER.info("Switched to stage");
                            RealmsMainScreen.realmsDataFetcher.init();
                            RealmsMainScreen.stageEnabled = true;
                        }
                        else {
                            RealmsMainScreen.stageEnabled = false;
                        }
                    }
                    catch (RealmsServiceException e) {
                        RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e.toString());
                    }
                    catch (IOException e2) {
                        RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e2.getMessage());
                    }
                }
            }.start();
        }
    }
    
    private void switchToProd() {
        if (RealmsMainScreen.stageEnabled) {
            RealmsMainScreen.stageEnabled = false;
            this.stopRealmsFetcherAndPinger();
            RealmsClient.switchToProd();
            RealmsMainScreen.realmsDataFetcher.init();
        }
    }
    
    private void stopRealmsFetcherAndPinger() {
        if (this.isMcoEnabled()) {
            RealmsMainScreen.realmsDataFetcher.stop();
            RealmsMainScreen.statusPinger.removeAll();
        }
    }
    
    private void configureClicked() {
        final RealmsServer selectedServer = this.findServer(this.selectedServerId);
        if (selectedServer != null && (Realms.getUUID().equals(selectedServer.ownerUUID) || RealmsMainScreen.overrideConfigure)) {
            this.stopRealmsFetcherAndPinger();
            this.saveListScrollPosition();
            Realms.setScreen(new RealmsConfigureWorldScreen(this, selectedServer.id));
        }
    }
    
    private void leaveClicked() {
        final RealmsServer selectedServer = this.findServer(this.selectedServerId);
        if (selectedServer != null && !Realms.getUUID().equals(selectedServer.ownerUUID)) {
            this.saveListScrollPosition();
            final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.leave.question.line1");
            final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.leave.question.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 3));
        }
    }
    
    private void saveListScrollPosition() {
        RealmsMainScreen.lastScrollYPosition = this.serverSelectionList.getScroll();
    }
    
    private RealmsServer findServer(final long id) {
        for (final RealmsServer server : this.realmsServers) {
            if (server.id == id) {
                return server;
            }
        }
        return null;
    }
    
    private int findIndex(final long serverId) {
        for (int i = 0; i < this.realmsServers.size(); ++i) {
            if (this.realmsServers.get(i).id == serverId) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 3) {
            if (result) {
                new Thread("Realms-leave-server") {
                    @Override
                    public void run() {
                        try {
                            final RealmsServer server = RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId);
                            if (server != null) {
                                final RealmsClient client = RealmsClient.createRealmsClient();
                                RealmsMainScreen.realmsDataFetcher.removeItem(server);
                                RealmsMainScreen.this.realmsServers.remove(server);
                                client.uninviteMyselfFrom(server.id);
                                RealmsMainScreen.realmsDataFetcher.removeItem(server);
                                RealmsMainScreen.this.realmsServers.remove(server);
                                RealmsMainScreen.this.updateSelectedItemPointer();
                            }
                        }
                        catch (RealmsServiceException e) {
                            RealmsMainScreen.LOGGER.error("Couldn't configure world");
                            Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this));
                        }
                    }
                }.start();
            }
            Realms.setScreen(this);
        }
        else if (id == 100) {
            if (!result) {
                Realms.setScreen(this);
            }
            else {
                this.connectToServer(this.resourcePackServer);
            }
        }
    }
    
    private void updateSelectedItemPointer() {
        int originalIndex = this.findIndex(this.selectedServerId);
        if (this.realmsServers.size() - 1 == originalIndex) {
            --originalIndex;
        }
        if (this.realmsServers.size() == 0) {
            originalIndex = -1;
        }
        if (originalIndex >= 0 && originalIndex < this.realmsServers.size()) {
            this.selectedServerId = this.realmsServers.get(originalIndex).id;
        }
    }
    
    public void removeSelection() {
        this.selectedServerId = -1L;
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        switch (eventKey) {
            case 28:
            case 156: {
                this.mindex = 0;
                this.sindex = 0;
                this.buttonClicked(this.playButton);
                break;
            }
            case 1: {
                this.mindex = 0;
                this.sindex = 0;
                this.stopRealmsFetcherAndPinger();
                Realms.setScreen(this.lastScreen);
                break;
            }
            default: {
                if (this.mchars[this.mindex] == ch) {
                    ++this.mindex;
                    if (this.mindex == this.mchars.length) {
                        this.mindex = 0;
                        RealmsMainScreen.overrideConfigure = true;
                    }
                }
                else {
                    this.mindex = 0;
                }
                if (this.schars[this.sindex] == ch) {
                    ++this.sindex;
                    if (this.sindex == this.schars.length) {
                        this.sindex = 0;
                        if (!RealmsMainScreen.stageEnabled) {
                            this.switchToStage();
                        }
                        else {
                            this.switchToProd();
                        }
                    }
                    return;
                }
                this.sindex = 0;
                break;
            }
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.renderBackground();
        this.serverSelectionList.render(xm, ym, a);
        this.drawRealmsLogo(this.width() / 2 - 50, 7);
        this.renderLink(xm, ym);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
        this.drawInvitationPendingIcon(xm, ym);
        if (RealmsMainScreen.stageEnabled) {
            this.renderStage();
        }
        super.render(xm, ym, a);
    }
    
    private void drawRealmsLogo(final int x, final int y) {
        RealmsScreen.bind("realms:textures/gui/title/realms.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2 - 5, 0.0f, 0.0f, 200, 50, 200.0f, 50.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        if (this.inPendingInvitationArea(x, y)) {
            this.stopRealmsFetcherAndPinger();
            final RealmsPendingInvitesScreen pendingInvitationScreen = new RealmsPendingInvitesScreen(this.lastScreen);
            Realms.setScreen(pendingInvitationScreen);
        }
        if (this.onLink) {
            RealmsUtil.browseTo("https://minecraft.net/realms");
        }
    }
    
    private void drawInvitationPendingIcon(final int xm, final int ym) {
        final int pendingInvitesCount = this.numberOfPendingInvites;
        final boolean hovering = this.inPendingInvitationArea(xm, ym);
        final int baseX = this.width() / 2 + 50;
        final int baseY = 8;
        if (pendingInvitesCount != 0) {
            final float scale = 0.25f + (1.0f + RealmsMth.sin(this.animTick * 0.5f)) * 0.25f;
            int color = 0xFF000000 | (int)(scale * 64.0f) << 16 | (int)(scale * 64.0f) << 8 | (int)(scale * 64.0f) << 0;
            this.fillGradient(baseX - 2, 6, baseX + 18, 26, color, color);
            color = (0xFF000000 | (int)(scale * 255.0f) << 16 | (int)(scale * 255.0f) << 8 | (int)(scale * 255.0f) << 0);
            this.fillGradient(baseX - 2, 6, baseX + 18, 7, color, color);
            this.fillGradient(baseX - 2, 6, baseX - 1, 26, color, color);
            this.fillGradient(baseX + 17, 6, baseX + 18, 26, color, color);
            this.fillGradient(baseX - 2, 25, baseX + 18, 26, color, color);
        }
        RealmsScreen.bind("realms:textures/gui/realms/invite_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(baseX, 2, hovering ? 16.0f : 0.0f, 0.0f, 15, 25, 31.0f, 25.0f);
        GL11.glPopMatrix();
        if (pendingInvitesCount != 0) {
            final int spritePos = (Math.min(pendingInvitesCount, 6) - 1) * 8;
            final int yOff = (int)(Math.max(0.0f, Math.max(RealmsMth.sin((10 + this.animTick) * 0.57f), RealmsMth.cos(this.animTick * 0.35f))) * -6.0f);
            RealmsScreen.bind("realms:textures/gui/realms/invitation_icons.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(baseX + 4, 12 + yOff, (float)spritePos, hovering ? 8.0f : 0.0f, 8, 8, 48.0f, 16.0f);
            GL11.glPopMatrix();
        }
        if (hovering) {
            final int rx = xm + 12;
            final int ry = ym - 12;
            final String message = (pendingInvitesCount == 0) ? RealmsScreen.getLocalizedString("mco.invites.nopending") : RealmsScreen.getLocalizedString("mco.invites.pending");
            final int width = this.fontWidth(message);
            this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
            this.fontDrawShadow(message, rx, ry, -1);
        }
    }
    
    private boolean inPendingInvitationArea(final int xm, final int ym) {
        final int x1 = this.width() / 2 + 50;
        final int x2 = this.width() / 2 + 66;
        final int y1 = 13;
        final int y2 = 27;
        return x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2;
    }
    
    public void play(final RealmsServer server) {
        if (server != null) {
            this.stopRealmsFetcherAndPinger();
            this.dontSetConnectedToRealms = true;
            if (server.resourcePackUrl != null && server.resourcePackHash != null) {
                this.resourcePackServer = server;
                this.saveListScrollPosition();
                final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.resourcepack.question.line1");
                final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.resourcepack.question.line2");
                Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 100));
            }
            else {
                this.connectToServer(server);
            }
        }
    }
    
    private void connectToServer(final RealmsServer server) {
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this, new RealmsTasks.RealmsConnectTask(this, server));
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    private boolean isSelfOwnedServer(final RealmsServer serverData) {
        return serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID());
    }
    
    private boolean isSelfOwnedNonExpiredServer(final RealmsServer serverData) {
        return serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID()) && !serverData.expired;
    }
    
    private void drawExpired(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/expired_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2, 0.0f, 0.0f, 15, 15, 15.0f, 15.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 64 && ym > 32) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expired");
        }
    }
    
    private void drawExpiring(final int x, final int y, final int xm, final int ym, final int daysLeft) {
        if (this.animTick % 20 < 10) {
            RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            RealmsScreen.blit(x * 2, y * 2, 0.0f, 0.0f, 15, 15, 15.0f, 15.0f);
            GL11.glPopMatrix();
        }
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 64 && ym > 32) {
            if (daysLeft == 0) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.soon");
            }
            else if (daysLeft == 1) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.day");
            }
            else {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.days", daysLeft);
            }
        }
    }
    
    private void drawOpen(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2, 0.0f, 0.0f, 15, 15, 15.0f, 15.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 64 && ym > 32) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.open");
        }
    }
    
    private void drawClose(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2, 0.0f, 0.0f, 15, 15, 15.0f, 15.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 64 && ym > 32) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.closed");
        }
    }
    
    private void drawLocked(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2, 0.0f, 0.0f, 15, 15, 15.0f, 15.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 64 && ym > 32) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.locked");
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        final int rx = x + 12;
        final int ry = y - 12;
        int index = 0;
        int width = 0;
        for (final String s : msg.split("\n")) {
            final int the_width = this.fontWidth(s);
            if (the_width > width) {
                width = the_width;
            }
        }
        for (final String s : msg.split("\n")) {
            this.fillGradient(rx - 3, ry - ((index == 0) ? 3 : 0) + index, rx + width + 3, ry + 8 + 3 + index, -1073741824, -1073741824);
            this.fontDrawShadow(s, rx, ry + index, 16777215);
            index += 10;
        }
    }
    
    private void renderLink(final int xm, final int ym) {
        final String text = RealmsScreen.getLocalizedString("mco.selectServer.whatisrealms");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        final int textWidth = this.fontWidth(text);
        final int leftPadding = 10;
        final int topPadding = 12;
        final int x1 = leftPadding;
        final int x2 = x1 + textWidth + 1;
        final int y1 = topPadding;
        final int y2 = y1 + this.fontLineHeight();
        GL11.glTranslatef((float)x1, (float)y1, 0.0f);
        if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
            this.onLink = true;
            this.drawString(text, 0, 0, 7107012);
        }
        else {
            this.onLink = false;
            this.drawString(text, 0, 0, 3368635);
        }
        GL11.glPopMatrix();
    }
    
    private void renderStage() {
        final String text = "STAGE!";
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width() / 2 - 25), 20.0f, 0.0f);
        GL11.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        this.drawString(text, 0, 0, -256);
        GL11.glPopMatrix();
    }
    
    public RealmsScreen newScreen() {
        return new RealmsMainScreen(this.lastScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RealmsMainScreen.overrideConfigure = false;
        RealmsMainScreen.stageEnabled = false;
        RealmsMainScreen.realmsDataFetcher = new RealmsDataFetcher();
        RealmsMainScreen.statusPinger = new RealmsServerStatusPinger();
        THREAD_POOL = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
        RealmsMainScreen.lastScrollYPosition = -1;
        RealmsMainScreen.createdTrial = false;
        trialLock = new ReentrantLock();
        RealmsMainScreen.realmsGenericErrorScreen = null;
        RealmsMainScreen.regionsPinged = false;
        final String version = RealmsVersion.getVersion();
        if (version != null) {
            RealmsMainScreen.LOGGER.info("Realms library version == " + version);
        }
    }
    
    private class ServerSelectionList extends RealmsScrolledSelectionList
    {
        public ServerSelectionList() {
            super(RealmsMainScreen.this.width(), RealmsMainScreen.this.height(), 32, RealmsMainScreen.this.height() - 64, 36);
        }
        
        @Override
        public int getItemCount() {
            if (RealmsMainScreen.trialsAvailable) {
                return RealmsMainScreen.this.realmsServers.size() + 1;
            }
            return RealmsMainScreen.this.realmsServers.size();
        }
        
        @Override
        public void selectItem(int item, final boolean doubleClick, final int xMouse, final int yMouse) {
            if (RealmsMainScreen.trialsAvailable) {
                if (item == 0) {
                    RealmsMainScreen.this.createTrial();
                    return;
                }
                --item;
            }
            if (item >= RealmsMainScreen.this.realmsServers.size()) {
                return;
            }
            final RealmsServer server = RealmsMainScreen.this.realmsServers.get(item);
            if (server.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.selectedServerId = -1L;
                RealmsMainScreen.this.stopRealmsFetcherAndPinger();
                Realms.setScreen(new RealmsCreateRealmScreen(server, RealmsMainScreen.this));
            }
            else {
                RealmsMainScreen.this.selectedServerId = server.id;
            }
            RealmsMainScreen.this.configureButton.active(RealmsMainScreen.overrideConfigure || (RealmsMainScreen.this.isSelfOwnedServer(server) && server.state != RealmsServer.State.ADMIN_LOCK && server.state != RealmsServer.State.UNINITIALIZED));
            RealmsMainScreen.this.leaveButton.active(!RealmsMainScreen.this.isSelfOwnedServer(server));
            RealmsMainScreen.this.playButton.active(server.state == RealmsServer.State.OPEN && !server.expired);
            if (doubleClick && RealmsMainScreen.this.playButton.active()) {
                RealmsMainScreen.this.play(RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId));
            }
        }
        
        @Override
        public boolean isSelectedItem(int item) {
            if (RealmsMainScreen.trialsAvailable) {
                if (item == 0) {
                    return false;
                }
                --item;
            }
            return item == RealmsMainScreen.this.findIndex(RealmsMainScreen.this.selectedServerId);
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public void renderBackground() {
            RealmsMainScreen.this.renderBackground();
        }
        
        @Override
        protected void renderItem(int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            if (RealmsMainScreen.trialsAvailable) {
                if (i == 0) {
                    this.renderTrialItem(i, x, y);
                    return;
                }
                --i;
            }
            if (i < RealmsMainScreen.this.realmsServers.size()) {
                this.renderMcoServerItem(i, x, y);
            }
        }
        
        private void renderTrialItem(final int i, final int x, final int y) {
            final int ry = y + 12;
            int index = 0;
            final String msg = RealmsScreen.getLocalizedString("mco.trial.message");
            boolean hovered = false;
            if (x <= this.xm() && this.xm() <= this.getScrollbarPosition() && y <= this.ym() && this.ym() <= y + 32) {
                hovered = true;
            }
            final float scale = 0.5f + (1.0f + RealmsMth.sin(RealmsMainScreen.this.animTick * 0.25f)) * 0.25f;
            int textColor;
            if (hovered) {
                textColor = (0xFF | (int)(127.0f * scale) << 16 | (int)(255.0f * scale) << 8 | (int)(127.0f * scale));
            }
            else {
                textColor = (0xFF000000 | (int)(127.0f * scale) << 16 | (int)(255.0f * scale) << 8 | (int)(127.0f * scale));
            }
            for (final String s : msg.split("\\\\n")) {
                RealmsMainScreen.this.drawCenteredString(s, RealmsMainScreen.this.width() / 2, ry + index, textColor);
                index += 10;
            }
        }
        
        private void renderMcoServerItem(final int i, final int x, final int y) {
            final RealmsServer serverData = RealmsMainScreen.this.realmsServers.get(i);
            final int nameColor = RealmsMainScreen.this.isSelfOwnedServer(serverData) ? 8388479 : 16777215;
            if (serverData.state == RealmsServer.State.UNINITIALIZED) {
                RealmsScreen.bind("realms:textures/gui/realms/world_icon.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(3008);
                GL11.glPushMatrix();
                RealmsScreen.blit(x + 10, y + 6, 0.0f, 0.0f, 40, 20, 40.0f, 20.0f);
                GL11.glPopMatrix();
                final float scale = 0.5f + (1.0f + RealmsMth.sin(RealmsMainScreen.this.animTick * 0.25f)) * 0.25f;
                final int textColor = 0xFF000000 | (int)(127.0f * scale) << 16 | (int)(255.0f * scale) << 8 | (int)(127.0f * scale);
                RealmsMainScreen.this.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.uninitialized"), x + 10 + 40 + 75, y + 12, textColor);
                return;
            }
            if (serverData.shouldPing(Realms.currentTimeMillis())) {
                serverData.serverPing.lastPingSnapshot = Realms.currentTimeMillis();
                RealmsMainScreen.THREAD_POOL.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RealmsMainScreen.statusPinger.pingServer(serverData.ip, serverData.serverPing);
                        }
                        catch (UnknownHostException e) {
                            RealmsMainScreen.LOGGER.error("Pinger: Could not resolve host");
                        }
                    }
                });
            }
            RealmsMainScreen.this.drawString(serverData.getName(), x + 2, y + 1, nameColor);
            final int dx = 207;
            final int dy = 1;
            if (serverData.expired) {
                RealmsMainScreen.this.drawExpired(x + dx, y + dy, this.xm(), this.ym());
            }
            else if (serverData.state == RealmsServer.State.CLOSED) {
                RealmsMainScreen.this.drawClose(x + dx, y + dy, this.xm(), this.ym());
            }
            else if (RealmsMainScreen.this.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
                this.showStatus(x - 14, y, serverData);
                RealmsMainScreen.this.drawExpiring(x + dx, y + dy, this.xm(), this.ym(), serverData.daysLeft);
            }
            else if (serverData.state == RealmsServer.State.OPEN) {
                RealmsMainScreen.this.drawOpen(x + dx, y + dy, this.xm(), this.ym());
                this.showStatus(x - 14, y, serverData);
            }
            else if (serverData.state == RealmsServer.State.ADMIN_LOCK) {
                RealmsMainScreen.this.drawLocked(x + dx, y + dy, this.xm(), this.ym());
            }
            final String noPlayers = "0";
            if (!serverData.serverPing.nrOfPlayers.equals(noPlayers)) {
                final String coloredNumPlayers = ChatFormatting.GRAY + "" + serverData.serverPing.nrOfPlayers;
                RealmsMainScreen.this.drawString(coloredNumPlayers, x + 200 - RealmsMainScreen.this.fontWidth(coloredNumPlayers), y + 1, 8421504);
                if (this.xm() >= x + 200 - RealmsMainScreen.this.fontWidth(coloredNumPlayers) && this.xm() <= x + 200 && this.ym() >= y + 1 && this.ym() <= y + 9 && this.ym() < RealmsMainScreen.this.height() - 64 && this.ym() > 32) {
                    RealmsMainScreen.this.toolTip = serverData.serverPing.playerList;
                }
            }
            if (serverData.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
                int motdColor = 9206892;
                if (RealmsMainScreen.this.animTick % 10 < 5) {
                    motdColor = 13413468;
                }
                final String miniGameStr = RealmsScreen.getLocalizedString("mco.selectServer.minigame") + " ";
                final int mgWidth = RealmsMainScreen.this.fontWidth(miniGameStr);
                RealmsMainScreen.this.drawString(miniGameStr, x + 2, y + 12, motdColor);
                RealmsMainScreen.this.drawString(serverData.getMinigameName(), x + 2 + mgWidth, y + 12, 7105644);
            }
            else {
                RealmsMainScreen.this.drawString(serverData.getDescription(), x + 2, y + 12, 7105644);
            }
            RealmsMainScreen.this.drawString(serverData.owner, x + 2, y + 12 + 11, 5000268);
            RealmsScreen.bindFace(serverData.ownerUUID, serverData.owner);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x - 36, y, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
            RealmsScreen.blit(x - 36, y, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
        }
        
        private void showStatus(final int x, final int y, final RealmsServer serverData) {
            if (serverData.ip == null) {
                return;
            }
            if (serverData.status != null) {
                RealmsMainScreen.this.drawString(serverData.status, x + 215 - RealmsMainScreen.this.fontWidth(serverData.status), y + 1, 8421504);
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.bind("textures/gui/icons.png");
        }
    }
}
