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
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsTermsScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final int BUTTON_AGREE_ID = 1;
    private static final int BUTTON_DISAGREE_ID = 2;
    private final RealmsScreen lastScreen;
    private final RealmsServer realmsServer;
    private RealmsButton agreeButton;
    private boolean onLink;
    private String realmsToSUrl;
    
    public RealmsTermsScreen(final RealmsScreen lastScreen, final RealmsServer realmsServer) {
        this.onLink = false;
        this.realmsToSUrl = "https://minecraft.net/realms/terms";
        this.lastScreen = lastScreen;
        this.realmsServer = realmsServer;
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        final int column1_x = this.width() / 4;
        final int column_width = this.width() / 4 - 2;
        final int column2_x = this.width() / 2 + 4;
        this.buttonsAdd(this.agreeButton = RealmsScreen.newButton(1, column1_x, RealmsConstants.row(12), column_width, 20, RealmsScreen.getLocalizedString("mco.terms.buttons.agree")));
        this.buttonsAdd(RealmsScreen.newButton(2, column2_x, RealmsConstants.row(12), column_width, 20, RealmsScreen.getLocalizedString("mco.terms.buttons.disagree")));
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
            case 2: {
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 1: {
                this.agreedToTos();
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    private void agreedToTos() {
        final RealmsClient client = RealmsClient.createRealmsClient();
        try {
            client.agreeToTos();
            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, new RealmsTasks.RealmsConnectTask(this.lastScreen, this.realmsServer));
            longRunningMcoTaskScreen.start();
            Realms.setScreen(longRunningMcoTaskScreen);
        }
        catch (RealmsServiceException e) {
            RealmsTermsScreen.LOGGER.error("Couldn't agree to TOS");
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        if (this.onLink) {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(this.realmsToSUrl), null);
            RealmsUtil.browseTo(this.realmsToSUrl);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.terms.title"), this.width() / 2, 17, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.terms.sentence.1"), this.width() / 2 - 120, RealmsConstants.row(5), 16777215);
        final int firstPartWidth = this.fontWidth(RealmsScreen.getLocalizedString("mco.terms.sentence.1"));
        final int x1 = this.width() / 2 - 121 + firstPartWidth;
        final int y1 = RealmsConstants.row(5);
        final int x2 = x1 + this.fontWidth("mco.terms.sentence.2") + 1;
        final int y2 = y1 + 1 + this.fontLineHeight();
        if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
            this.onLink = true;
            this.drawString(" " + RealmsScreen.getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + firstPartWidth, RealmsConstants.row(5), 7107012);
        }
        else {
            this.onLink = false;
            this.drawString(" " + RealmsScreen.getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + firstPartWidth, RealmsConstants.row(5), 3368635);
        }
        super.render(xm, ym, a);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
