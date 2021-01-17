// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsScreen;

public class RealmsSettingsScreen extends RealmsScreen
{
    private RealmsConfigureWorldScreen configureWorldScreen;
    private RealmsServer serverData;
    private static final int BUTTON_CANCEL_ID = 0;
    private static final int BUTTON_DONE_ID = 1;
    private static final int NAME_EDIT_BOX = 2;
    private static final int DESC_EDIT_BOX = 3;
    private static final int BUTTON_OPEN_CLOSE_ID = 5;
    private final int COMPONENT_WIDTH = 212;
    private RealmsButton doneButton;
    private RealmsEditBox descEdit;
    private RealmsEditBox nameEdit;
    
    public RealmsSettingsScreen(final RealmsConfigureWorldScreen configureWorldScreen, final RealmsServer serverData) {
        this.configureWorldScreen = configureWorldScreen;
        this.serverData = serverData;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.descEdit.tick();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        final int center = this.width() / 2 - 106;
        this.buttonsAdd(this.doneButton = RealmsScreen.newButton(1, center - 2, RealmsConstants.row(12), 106, 20, RealmsScreen.getLocalizedString("mco.configure.world.buttons.done")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 + 2, RealmsConstants.row(12), 106, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        this.buttonsAdd(RealmsScreen.newButton(5, this.width() / 2 - 53, RealmsConstants.row(0), 106, 20, this.serverData.state.equals(RealmsServer.State.OPEN) ? RealmsScreen.getLocalizedString("mco.configure.world.buttons.close") : RealmsScreen.getLocalizedString("mco.configure.world.buttons.open")));
        (this.nameEdit = this.newEditBox(2, center, RealmsConstants.row(4), 212, 20)).setFocus(true);
        this.nameEdit.setMaxLength(32);
        if (this.serverData.getName() != null) {
            this.nameEdit.setValue(this.serverData.getName());
        }
        (this.descEdit = this.newEditBox(3, center, RealmsConstants.row(8), 212, 20)).setMaxLength(32);
        if (this.serverData.getDescription() != null) {
            this.descEdit.setValue(this.serverData.getDescription());
        }
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
            case 0: {
                Realms.setScreen(this.configureWorldScreen);
                break;
            }
            case 1: {
                this.save();
                break;
            }
            case 5: {
                if (this.serverData.state.equals(RealmsServer.State.OPEN)) {
                    final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.close.question.line1");
                    final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.close.question.line2");
                    Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 5));
                    break;
                }
                this.configureWorldScreen.openTheWorld(false, this);
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        switch (id) {
            case 5: {
                if (result) {
                    this.configureWorldScreen.closeTheWorld(this);
                    break;
                }
                Realms.setScreen(this);
                break;
            }
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        this.nameEdit.keyPressed(ch, eventKey);
        this.descEdit.keyPressed(ch, eventKey);
        Label_0122: {
            switch (eventKey) {
                case 15: {
                    this.nameEdit.setFocus(!this.nameEdit.isFocused());
                    this.descEdit.setFocus(!this.descEdit.isFocused());
                    break Label_0122;
                }
                case 28:
                case 156: {
                    this.save();
                    break Label_0122;
                }
                case 1: {
                    Realms.setScreen(this.configureWorldScreen);
                    break;
                }
            }
            return;
        }
        this.doneButton.active(this.nameEdit.getValue() != null && !this.nameEdit.getValue().trim().equals(""));
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.descEdit.mouseClicked(x, y, buttonNum);
        this.nameEdit.mouseClicked(x, y, buttonNum);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.settings.title"), this.width() / 2, 17, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.name"), this.width() / 2 - 106, RealmsConstants.row(3), 10526880);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.description"), this.width() / 2 - 106, RealmsConstants.row(7), 10526880);
        this.nameEdit.render();
        this.descEdit.render();
        super.render(xm, ym, a);
    }
    
    public void save() {
        this.configureWorldScreen.saveSettings(this.nameEdit.getValue(), this.descEdit.getValue());
    }
}
