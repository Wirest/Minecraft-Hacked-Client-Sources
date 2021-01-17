// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.awt.datatransfer.Clipboard;
import com.mojang.realmsclient.util.RealmsUtil;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.dto.RealmsState;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsBuyRealmsScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private RealmsScreen lastScreen;
    private static int BUTTON_BACK_ID;
    private volatile RealmsState realmsStatus;
    private boolean onLink;
    
    public RealmsBuyRealmsScreen(final RealmsScreen lastScreen) {
        this.onLink = false;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        final int buttonLength = 212;
        this.buttonsAdd(RealmsScreen.newButton(RealmsBuyRealmsScreen.BUTTON_BACK_ID, this.width() / 2 - buttonLength / 2, RealmsConstants.row(12), buttonLength, 20, RealmsScreen.getLocalizedString("gui.back")));
        this.fetchMessage();
    }
    
    private void fetchMessage() {
        final RealmsClient client = RealmsClient.createRealmsClient();
        new Thread("Realms-stat-message") {
            @Override
            public void run() {
                try {
                    RealmsBuyRealmsScreen.this.realmsStatus = client.fetchRealmsState();
                }
                catch (RealmsServiceException e) {
                    RealmsBuyRealmsScreen.LOGGER.error("Could not get state");
                    Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsBuyRealmsScreen.this.lastScreen));
                }
            }
        }.start();
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
        if (button.id() == RealmsBuyRealmsScreen.BUTTON_BACK_ID) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        if (this.onLink) {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(this.realmsStatus.getBuyLink()), null);
            RealmsUtil.browseTo(this.realmsStatus.getBuyLink());
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.buy.realms.title"), this.width() / 2, 17, 16777215);
        if (this.realmsStatus == null) {
            return;
        }
        final String[] lines = this.realmsStatus.getStatusMessage().split("\n");
        int i = 1;
        for (final String line : lines) {
            this.drawCenteredString(line, this.width() / 2, RealmsConstants.row(i), 10526880);
            i += 2;
        }
        if (this.realmsStatus.getBuyLink() != null) {
            final String buyLink = this.realmsStatus.getBuyLink();
            final int height = RealmsConstants.row(i + 1);
            final int textWidth = this.fontWidth(buyLink);
            final int x1 = this.width() / 2 - textWidth / 2 - 1;
            final int y1 = height - 1;
            final int x2 = x1 + textWidth + 1;
            final int y2 = height + 1 + this.fontLineHeight();
            if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
                this.onLink = true;
                this.drawString(buyLink, this.width() / 2 - textWidth / 2, height, 7107012);
            }
            else {
                this.onLink = false;
                this.drawString(buyLink, this.width() / 2 - textWidth / 2, height, 3368635);
            }
        }
        super.render(xm, ym, a);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RealmsBuyRealmsScreen.BUTTON_BACK_ID = 0;
    }
}
