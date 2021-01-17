// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsEditBox;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsInviteScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private RealmsEditBox profileName;
    private RealmsServer serverData;
    private final RealmsConfigureWorldScreen configureScreen;
    private final RealmsScreen lastScreen;
    private final int BUTTON_INVITE_ID = 0;
    private final int BUTTON_CANCEL_ID = 1;
    private RealmsButton inviteButton;
    private final int PROFILENAME_EDIT_BOX = 2;
    private String errorMsg;
    private boolean showError;
    
    public RealmsInviteScreen(final RealmsConfigureWorldScreen configureScreen, final RealmsScreen lastScreen, final RealmsServer serverData) {
        this.configureScreen = configureScreen;
        this.lastScreen = lastScreen;
        this.serverData = serverData;
    }
    
    @Override
    public void tick() {
        this.profileName.tick();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(this.inviteButton = RealmsScreen.newButton(0, this.width() / 2 - 100, RealmsConstants.row(10), RealmsScreen.getLocalizedString("mco.configure.world.buttons.invite")));
        this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 - 100, RealmsConstants.row(12), RealmsScreen.getLocalizedString("gui.cancel")));
        (this.profileName = this.newEditBox(2, this.width() / 2 - 100, RealmsConstants.row(2), 200, 20)).setFocus(true);
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
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 0: {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (this.profileName.getValue() == null || this.profileName.getValue().isEmpty()) {
                    return;
                }
                try {
                    final RealmsServer realmsServer = client.invite(this.serverData.id, this.profileName.getValue());
                    if (realmsServer != null) {
                        this.serverData.players = realmsServer.players;
                        Realms.setScreen(new RealmsPlayerScreen(this.configureScreen, this.serverData));
                    }
                    else {
                        this.showError(RealmsScreen.getLocalizedString("mco.configure.world.players.error"));
                    }
                }
                catch (Exception e) {
                    RealmsInviteScreen.LOGGER.error("Couldn't invite user");
                    this.showError(RealmsScreen.getLocalizedString("mco.configure.world.players.error"));
                }
                break;
            }
            default: {}
        }
    }
    
    private void showError(final String errorMsg) {
        this.showError = true;
        this.errorMsg = errorMsg;
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        this.profileName.keyPressed(ch, eventKey);
        if (eventKey == 15) {
            if (this.profileName.isFocused()) {
                this.profileName.setFocus(false);
            }
            else {
                this.profileName.setFocus(true);
            }
        }
        if (eventKey == 28 || eventKey == 156) {
            this.buttonClicked(this.inviteButton);
        }
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.profileName.mouseClicked(x, y, buttonNum);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.invite.profile.name"), this.width() / 2 - 100, RealmsConstants.row(1), 10526880);
        if (this.showError) {
            this.drawCenteredString(this.errorMsg, this.width() / 2, RealmsConstants.row(5), 16711680);
        }
        this.profileName.render();
        super.render(xm, ym, a);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
