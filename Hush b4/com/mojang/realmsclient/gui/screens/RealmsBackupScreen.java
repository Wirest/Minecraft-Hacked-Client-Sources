// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import com.mojang.realmsclient.exception.RetryCallException;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.dto.RealmsOptions;
import java.util.Date;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.Realms;
import java.text.DateFormat;
import java.util.Iterator;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import org.lwjgl.input.Keyboard;
import java.util.Collections;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.Backup;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsBackupScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final String PLUS_ICON_LOCATION = "realms:textures/gui/realms/plus_icon.png";
    private static final String RESTORE_ICON_LOCATION = "realms:textures/gui/realms/restore_icon.png";
    private static int lastScrollPosition;
    private final RealmsConfigureWorldScreen lastScreen;
    private List<Backup> backups;
    private String toolTip;
    private BackupSelectionList backupSelectionList;
    private int selectedBackup;
    private static final int BACK_BUTTON_ID = 0;
    private static final int RESTORE_BUTTON_ID = 1;
    private static final int DOWNLOAD_BUTTON_ID = 2;
    private RealmsButton downloadButton;
    private Boolean noBackups;
    private RealmsServer serverData;
    private static final String UPLOADED_KEY = "Uploaded";
    
    public RealmsBackupScreen(final RealmsConfigureWorldScreen lastscreen, final RealmsServer serverData) {
        this.backups = Collections.emptyList();
        this.toolTip = null;
        this.selectedBackup = -1;
        this.noBackups = false;
        this.lastScreen = lastscreen;
        this.serverData = serverData;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.backupSelectionList.mouseEvent();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.backupSelectionList = new BackupSelectionList();
        if (RealmsBackupScreen.lastScrollPosition != -1) {
            this.backupSelectionList.scroll(RealmsBackupScreen.lastScrollPosition);
        }
        new Thread("Realms-fetch-backups") {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsBackupScreen.this.backups = client.backupsFor(RealmsBackupScreen.this.serverData.id).backups;
                    RealmsBackupScreen.this.noBackups = (RealmsBackupScreen.this.backups.size() == 0);
                    RealmsBackupScreen.this.generateChangeList();
                }
                catch (RealmsServiceException e) {
                    RealmsBackupScreen.LOGGER.error("Couldn't request backups", e);
                }
            }
        }.start();
        this.postInit();
    }
    
    private void generateChangeList() {
        if (this.backups.size() <= 1) {
            return;
        }
        for (int i = 0; i < this.backups.size() - 1; ++i) {
            final Backup backup = this.backups.get(i);
            final Backup olderBackup = this.backups.get(i + 1);
            if (!backup.metadata.isEmpty()) {
                if (!olderBackup.metadata.isEmpty()) {
                    for (final String key : backup.metadata.keySet()) {
                        if (!key.contains("Uploaded") && olderBackup.metadata.containsKey(key)) {
                            if (backup.metadata.get(key).equals(olderBackup.metadata.get(key))) {
                                continue;
                            }
                            this.addToChangeList(backup, key);
                        }
                        else {
                            this.addToChangeList(backup, key);
                        }
                    }
                }
            }
        }
    }
    
    private void addToChangeList(final Backup backup, final String key) {
        if (key.contains("Uploaded")) {
            final String uploadedTime = DateFormat.getDateTimeInstance(3, 3).format(backup.lastModifiedDate);
            backup.changeList.put(key, uploadedTime);
            backup.setUploadedVersion(true);
        }
        else {
            backup.changeList.put(key, backup.metadata.get(key));
        }
    }
    
    private void postInit() {
        this.buttonsAdd(this.downloadButton = RealmsScreen.newButton(2, this.width() - 125, 32, 100, 20, RealmsScreen.getLocalizedString("mco.backup.button.download")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() - 125, this.height() - 35, 85, 20, RealmsScreen.getLocalizedString("gui.back")));
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
        else if (button.id() == 2) {
            this.downloadClicked();
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    private void restoreClicked(final int selectedBackup) {
        if (selectedBackup >= 0 && selectedBackup < this.backups.size() && !this.serverData.expired) {
            this.selectedBackup = selectedBackup;
            final Date backupDate = this.backups.get(selectedBackup).lastModifiedDate;
            final String datePresentation = DateFormat.getDateTimeInstance(3, 3).format(backupDate);
            final String age = RealmsUtil.convertToAgePresentation(System.currentTimeMillis() - backupDate.getTime());
            final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.restore.question.line1", datePresentation, age);
            final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.restore.question.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line2, line3, true, 1));
        }
    }
    
    private void downloadClicked() {
        final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.restore.download.question.line1");
        final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.restore.download.question.line2");
        Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 2));
    }
    
    private void downloadWorldData() {
        final RealmsClient client = RealmsClient.createRealmsClient();
        try {
            final String downloadLink = client.download(this.serverData.id);
            Realms.setScreen(new RealmsDownloadLatestWorldScreen(this, downloadLink, this.serverData.name + " (" + this.serverData.slots.get(this.serverData.activeSlot).getSlotName(this.serverData.activeSlot) + ")"));
        }
        catch (RealmsServiceException e) {
            RealmsBackupScreen.LOGGER.error("Couldn't download world data");
            Realms.setScreen(new RealmsGenericErrorScreen(e, this));
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (result && id == 1) {
            this.restore();
        }
        else if (result && id == 2) {
            this.downloadWorldData();
        }
        else {
            Realms.setScreen(this);
        }
    }
    
    private void restore() {
        final Backup backup = this.backups.get(this.selectedBackup);
        final RestoreTask restoreTask = new RestoreTask(backup);
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen.getNewScreen(), restoreTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.renderBackground();
        this.backupSelectionList.render(xm, ym, a);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.backup"), this.width() / 2, 12, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.backup"), (this.width() - 150) / 2 - 90, 20, 10526880);
        if (this.noBackups) {
            this.drawString(RealmsScreen.getLocalizedString("mco.backup.nobackups"), 20, this.height() / 2 - 10, 16777215);
        }
        this.downloadButton.active(!this.noBackups);
        super.render(xm, ym, a);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        final int rx = x + 12;
        final int ry = y - 12;
        final int width = this.fontWidth(msg);
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 16777215);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RealmsBackupScreen.lastScrollPosition = -1;
    }
    
    private class RestoreTask extends LongRunningTask
    {
        private final Backup backup;
        
        private RestoreTask(final Backup backup) {
            this.backup = backup;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.backup.restoring"));
            for (int i = 0; i < 6; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    client.restoreWorld(RealmsBackupScreen.this.serverData.id, this.backup.backupId);
                    this.pause(1);
                    if (this.aborted()) {
                        return;
                    }
                    Realms.setScreen(RealmsBackupScreen.this.lastScreen.getNewScreen());
                    return;
                }
                catch (RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    this.pause(e.delaySeconds);
                }
                catch (RealmsServiceException e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsBackupScreen.LOGGER.error("Couldn't restore backup");
                    Realms.setScreen(new RealmsGenericErrorScreen(e2, RealmsBackupScreen.this.lastScreen));
                    return;
                }
                catch (Exception e3) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsBackupScreen.LOGGER.error("Couldn't restore backup");
                    this.error(e3.getLocalizedMessage());
                    return;
                }
            }
        }
        
        private void pause(final int pauseSeconds) {
            try {
                Thread.sleep(pauseSeconds * 1000);
            }
            catch (InterruptedException e) {
                RealmsBackupScreen.LOGGER.error(e);
            }
        }
    }
    
    private class BackupSelectionList extends RealmsClickableScrolledSelectionList
    {
        public BackupSelectionList() {
            super(RealmsBackupScreen.this.width() - 150, RealmsBackupScreen.this.height(), 32, RealmsBackupScreen.this.height() - 15, 36);
        }
        
        @Override
        public int getItemCount() {
            return RealmsBackupScreen.this.backups.size() + 1;
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public void renderBackground() {
            RealmsBackupScreen.this.renderBackground();
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = this.width() / 2 - 92;
                final int x2 = this.width();
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                }
            }
        }
        
        @Override
        public void renderItem(final int i, int x, final int y, final int h, final int mouseX, final int mouseY) {
            x += 16;
            if (i < RealmsBackupScreen.this.backups.size()) {
                this.renderBackupItem(i, x, y, h, RealmsBackupScreen.this.width);
            }
        }
        
        @Override
        public int getScrollbarPosition() {
            return this.width() - 5;
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, final int slot, final int xm, final int ym, final int width) {
            final int infox = this.width() - 40;
            final int infoy = clickSlotPos + 30 - this.getScroll();
            final int mx = infox + 10;
            final int my = infoy - 3;
            if (xm >= infox && xm <= infox + 9 && ym >= infoy && ym <= infoy + 9) {
                if (!RealmsBackupScreen.this.backups.get(slot).changeList.isEmpty()) {
                    RealmsBackupScreen.lastScrollPosition = this.getScroll();
                    Realms.setScreen(new RealmsBackupInfoScreen(RealmsBackupScreen.this, RealmsBackupScreen.this.backups.get(slot)));
                }
            }
            else if (xm >= mx && xm <= mx + 9 && ym >= my && ym <= my + 9) {
                RealmsBackupScreen.lastScrollPosition = this.getScroll();
                RealmsBackupScreen.this.restoreClicked(slot);
            }
        }
        
        private void renderBackupItem(final int i, final int x, final int y, final int h, final int width) {
            final Backup backup = RealmsBackupScreen.this.backups.get(i);
            final int color = backup.isUploadedVersion() ? -8388737 : 16777215;
            RealmsBackupScreen.this.drawString("Backup (" + RealmsUtil.convertToAgePresentation(System.currentTimeMillis() - backup.lastModifiedDate.getTime()) + ")", x + 2, y + 1, color);
            RealmsBackupScreen.this.drawString(this.getMediumDatePresentation(backup.lastModifiedDate), x + 2, y + 12, 5000268);
            final int dx = this.width() - 30;
            final int dy = -3;
            final int infox = dx - 10;
            final int infoy = dy + 3;
            if (!RealmsBackupScreen.this.serverData.expired) {
                this.drawRestore(dx, y + dy, this.xm(), this.ym());
            }
            if (!backup.changeList.isEmpty()) {
                this.drawInfo(infox, y + infoy, this.xm(), this.ym());
            }
        }
        
        private String getMediumDatePresentation(final Date lastModifiedDate) {
            return DateFormat.getDateTimeInstance(3, 3).format(lastModifiedDate);
        }
        
        private void drawRestore(final int x, final int y, final int xm, final int ym) {
            final boolean hovered = xm >= x && xm <= x + 12 && ym >= y && ym <= y + 14 && ym < RealmsBackupScreen.this.height() - 15 && ym > 32;
            RealmsScreen.bind("realms:textures/gui/realms/restore_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            RealmsScreen.blit(x * 2, y * 2, 0.0f, hovered ? 28.0f : 0.0f, 23, 28, 23.0f, 56.0f);
            GL11.glPopMatrix();
            if (hovered) {
                RealmsBackupScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.backup.button.restore");
            }
        }
        
        private void drawInfo(final int x, final int y, final int xm, final int ym) {
            final boolean hovered = xm >= x && xm <= x + 8 && ym >= y && ym <= y + 8 && ym < RealmsBackupScreen.this.height() - 15 && ym > 32;
            RealmsScreen.bind("realms:textures/gui/realms/plus_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            RealmsScreen.blit(x * 2, y * 2, 0.0f, hovered ? 15.0f : 0.0f, 15, 15, 15.0f, 30.0f);
            GL11.glPopMatrix();
            if (hovered) {
                RealmsBackupScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.backup.changes.tooltip");
            }
        }
    }
}
