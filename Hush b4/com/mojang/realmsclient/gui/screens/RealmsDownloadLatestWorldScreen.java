// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.client.FileDownload;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.realms.RealmsButton;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsDownloadLatestWorldScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private final RealmsScreen lastScreen;
    private final String downloadLink;
    private RealmsButton cancelButton;
    private final String worldName;
    private final DownloadStatus downloadStatus;
    private volatile String errorMessage;
    private volatile String status;
    private volatile String progress;
    private volatile boolean cancelled;
    private volatile boolean showDots;
    private volatile boolean finished;
    private volatile boolean extracting;
    private Long previousWrittenBytes;
    private Long previousTimeSnapshot;
    private long bytesPersSecond;
    private int animTick;
    private static final String[] DOTS;
    private int dotIndex;
    private final int WARNING_ID = 100;
    private boolean checked;
    private static final ReentrantLock downloadLock;
    
    public RealmsDownloadLatestWorldScreen(final RealmsScreen lastScreen, final String downloadLink, final String worldName) {
        this.errorMessage = null;
        this.status = null;
        this.progress = null;
        this.cancelled = false;
        this.showDots = true;
        this.finished = false;
        this.extracting = false;
        this.previousWrittenBytes = null;
        this.previousTimeSnapshot = null;
        this.bytesPersSecond = 0L;
        this.animTick = 0;
        this.dotIndex = 0;
        this.checked = false;
        this.lastScreen = lastScreen;
        this.worldName = worldName;
        this.downloadLink = downloadLink;
        this.downloadStatus = new DownloadStatus();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(this.cancelButton = RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() - 42, 200, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        this.checkDownloadSize();
    }
    
    private void checkDownloadSize() {
        if (this.finished) {
            return;
        }
        if (!this.checked && this.getContentLength(this.downloadLink) >= 1048576000L) {
            final String line1 = RealmsScreen.getLocalizedString("mco.download.confirmation.line1", humanReadableSize(1048576000L));
            final String line2 = RealmsScreen.getLocalizedString("mco.download.confirmation.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line1, line2, false, 100));
        }
        else {
            this.downloadSave();
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        this.checked = true;
        Realms.setScreen(this);
        this.downloadSave();
    }
    
    private long getContentLength(final String downloadLink) {
        final FileDownload fileDownload = new FileDownload();
        return fileDownload.contentLength(downloadLink);
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.animTick;
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 0) {
            this.cancelled = true;
            this.backButtonClicked();
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            this.cancelled = true;
            this.backButtonClicked();
        }
    }
    
    private void backButtonClicked() {
        Realms.setScreen(this.lastScreen);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        if (this.extracting && !this.finished) {
            this.status = RealmsScreen.getLocalizedString("mco.download.extracting");
        }
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.download.title"), this.width() / 2, 20, 16777215);
        this.drawCenteredString(this.status, this.width() / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots();
        }
        if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar();
            this.drawDownloadSpeed();
        }
        if (this.errorMessage != null) {
            this.drawCenteredString(this.errorMessage, this.width() / 2, 110, 16711680);
        }
        super.render(xm, ym, a);
    }
    
    private void drawDots() {
        final int statusWidth = this.fontWidth(this.status);
        if (this.animTick % 10 == 0) {
            ++this.dotIndex;
        }
        this.drawString(RealmsDownloadLatestWorldScreen.DOTS[this.dotIndex % RealmsDownloadLatestWorldScreen.DOTS.length], this.width() / 2 + statusWidth / 2 + 5, 50, 16777215);
    }
    
    private void drawProgressBar() {
        final double percentage = this.downloadStatus.bytesWritten / (double)this.downloadStatus.totalBytes * 100.0;
        this.progress = String.format("%.1f", percentage);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3553);
        final Tezzelator t = Tezzelator.instance;
        t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
        final double base = this.width() / 2 - 100;
        final double diff = 0.5;
        t.vertex(base - 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base - 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.end();
        GL11.glEnable(3553);
        this.drawCenteredString(this.progress + " %", this.width() / 2, 84, 16777215);
    }
    
    private void drawDownloadSpeed() {
        if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
            if (this.previousWrittenBytes != null) {
                long timeElapsed = System.currentTimeMillis() - this.previousTimeSnapshot;
                if (timeElapsed == 0L) {
                    timeElapsed = 1L;
                }
                this.drawDownloadSpeed0(this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / timeElapsed);
            }
            this.previousWrittenBytes = this.downloadStatus.bytesWritten;
            this.previousTimeSnapshot = System.currentTimeMillis();
        }
        else {
            this.drawDownloadSpeed0(this.bytesPersSecond);
        }
    }
    
    private void drawDownloadSpeed0(final long bytesPersSecond) {
        if (bytesPersSecond > 0L) {
            final int progressLength = this.fontWidth(this.progress);
            final String stringPresentation = "(" + humanReadableSpeed(bytesPersSecond) + ")";
            this.drawString(stringPresentation, this.width() / 2 + progressLength / 2 + 15, 84, 16777215);
        }
    }
    
    public static String humanReadableSpeed(final long bytes) {
        final int unit = 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        final int exp = (int)(Math.log((double)bytes) / Math.log(unit));
        final String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB/s", bytes / Math.pow(unit, exp), pre);
    }
    
    public static String humanReadableSize(final long bytes) {
        final int unit = 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        final int exp = (int)(Math.log((double)bytes) / Math.log(unit));
        final String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.0f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }
    
    private void downloadSave() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (!RealmsDownloadLatestWorldScreen.downloadLock.tryLock(1L, TimeUnit.SECONDS)) {
                        return;
                    }
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.preparing");
                    if (RealmsDownloadLatestWorldScreen.this.cancelled) {
                        RealmsDownloadLatestWorldScreen.this.downloadCancelled();
                        return;
                    }
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.downloading", RealmsDownloadLatestWorldScreen.this.worldName);
                    final FileDownload fileDownload = new FileDownload();
                    fileDownload.contentLength(RealmsDownloadLatestWorldScreen.this.downloadLink);
                    fileDownload.download(RealmsDownloadLatestWorldScreen.this.downloadLink, RealmsDownloadLatestWorldScreen.this.worldName, RealmsDownloadLatestWorldScreen.this.downloadStatus, RealmsDownloadLatestWorldScreen.this.getLevelStorageSource());
                    while (!fileDownload.isFinished()) {
                        if (fileDownload.isError()) {
                            fileDownload.cancel();
                            RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
                            RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                            return;
                        }
                        if (fileDownload.isExtracting()) {
                            RealmsDownloadLatestWorldScreen.this.extracting = true;
                        }
                        if (RealmsDownloadLatestWorldScreen.this.cancelled) {
                            fileDownload.cancel();
                            RealmsDownloadLatestWorldScreen.this.downloadCancelled();
                            return;
                        }
                        try {
                            Thread.sleep(500L);
                        }
                        catch (InterruptedException e2) {
                            RealmsDownloadLatestWorldScreen.LOGGER.error("Failed to check Realms backup download status");
                        }
                    }
                    RealmsDownloadLatestWorldScreen.this.finished = true;
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.done");
                    RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                }
                catch (InterruptedException e3) {
                    RealmsDownloadLatestWorldScreen.LOGGER.error("Could not acquire upload lock");
                }
                catch (Exception e) {
                    RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
                    e.printStackTrace();
                }
                finally {
                    if (!RealmsDownloadLatestWorldScreen.downloadLock.isHeldByCurrentThread()) {
                        return;
                    }
                    RealmsDownloadLatestWorldScreen.downloadLock.unlock();
                    RealmsDownloadLatestWorldScreen.this.showDots = false;
                    RealmsDownloadLatestWorldScreen.this.buttonsRemove(RealmsDownloadLatestWorldScreen.this.cancelButton);
                    RealmsDownloadLatestWorldScreen.this.finished = true;
                }
            }
        }.start();
    }
    
    private void downloadCancelled() {
        this.status = RealmsScreen.getLocalizedString("mco.download.cancelled");
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DOTS = new String[] { "", ".", ". .", ". . ." };
        downloadLock = new ReentrantLock();
    }
    
    public class DownloadStatus
    {
        public volatile Long bytesWritten;
        public volatile Long totalBytes;
        
        public DownloadStatus() {
            this.bytesWritten = 0L;
            this.totalBytes = 0L;
        }
    }
}
