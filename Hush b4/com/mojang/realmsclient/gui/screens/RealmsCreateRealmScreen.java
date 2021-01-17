// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsScreen;

public class RealmsCreateRealmScreen extends RealmsScreen
{
    private final RealmsServer server;
    private RealmsMainScreen lastScreen;
    private RealmsEditBox nameBox;
    private RealmsEditBox descriptionBox;
    private static int CREATE_BUTTON;
    private static int CANCEL_BUTTON;
    private static int NAME_BOX_ID;
    private static int DESCRIPTION_BOX_ID;
    private RealmsButton createButton;
    
    public RealmsCreateRealmScreen(final RealmsServer server, final RealmsMainScreen lastScreen) {
        this.server = server;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void tick() {
        this.nameBox.tick();
        this.descriptionBox.tick();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(this.createButton = RealmsScreen.newButton(RealmsCreateRealmScreen.CREATE_BUTTON, this.width() / 2 - 100, this.height() / 4 + 120 + 17, 97, 20, RealmsScreen.getLocalizedString("mco.create.world")));
        this.buttonsAdd(RealmsScreen.newButton(RealmsCreateRealmScreen.CANCEL_BUTTON, this.width() / 2 + 5, this.height() / 4 + 120 + 17, 95, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        this.createButton.active(false);
        (this.nameBox = this.newEditBox(RealmsCreateRealmScreen.NAME_BOX_ID, this.width() / 2 - 100, 65, 200, 20)).setFocus(true);
        this.descriptionBox = this.newEditBox(RealmsCreateRealmScreen.DESCRIPTION_BOX_ID, this.width() / 2 - 100, 115, 200, 20);
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
        if (button.id() == RealmsCreateRealmScreen.CANCEL_BUTTON) {
            Realms.setScreen(this.lastScreen);
        }
        else if (button.id() == RealmsCreateRealmScreen.CREATE_BUTTON) {
            this.createWorld();
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        this.nameBox.keyPressed(ch, eventKey);
        this.descriptionBox.keyPressed(ch, eventKey);
        this.createButton.active(this.valid());
        switch (eventKey) {
            case 15: {
                this.nameBox.setFocus(!this.nameBox.isFocused());
                this.descriptionBox.setFocus(!this.descriptionBox.isFocused());
                break;
            }
            case 28:
            case 156: {
                this.buttonClicked(this.createButton);
                break;
            }
            case 1: {
                Realms.setScreen(this.lastScreen);
                break;
            }
        }
    }
    
    private void createWorld() {
        if (this.valid()) {
            final RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this.lastScreen, this.server, this.lastScreen.newScreen(), RealmsScreen.getLocalizedString("mco.selectServer.create"), RealmsScreen.getLocalizedString("mco.create.world.subtitle"), 10526880, RealmsScreen.getLocalizedString("mco.create.world.skip"));
            resetWorldScreen.setResetTitle(RealmsScreen.getLocalizedString("mco.create.world.reset.title"));
            final RealmsTasks.WorldCreationTask worldCreationTask = new RealmsTasks.WorldCreationTask(this.server.id, this.nameBox.getValue(), this.descriptionBox.getValue(), resetWorldScreen);
            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, worldCreationTask);
            longRunningMcoTaskScreen.start();
            Realms.setScreen(longRunningMcoTaskScreen);
        }
    }
    
    private boolean valid() {
        return this.nameBox.getValue() != null && !this.nameBox.getValue().trim().equals("");
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        this.nameBox.mouseClicked(x, y, buttonNum);
        this.descriptionBox.mouseClicked(x, y, buttonNum);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.create"), this.width() / 2, 11, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.name"), this.width() / 2 - 100, 52, 10526880);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.description"), this.width() / 2 - 100, 102, 10526880);
        this.nameBox.render();
        this.descriptionBox.render();
        super.render(xm, ym, a);
    }
    
    static {
        RealmsCreateRealmScreen.CREATE_BUTTON = 0;
        RealmsCreateRealmScreen.CANCEL_BUTTON = 1;
        RealmsCreateRealmScreen.NAME_BOX_ID = 3;
        RealmsCreateRealmScreen.DESCRIPTION_BOX_ID = 4;
    }
}
