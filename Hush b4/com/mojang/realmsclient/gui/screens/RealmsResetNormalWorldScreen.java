// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;

public class RealmsResetNormalWorldScreen extends RealmsScreen
{
    private RealmsResetWorldScreen lastScreen;
    private RealmsEditBox seedEdit;
    private Boolean generateStructures;
    private Integer levelTypeIndex;
    String[] levelTypes;
    private final int BUTTON_CANCEL_ID = 0;
    private final int BUTTON_RESET_ID = 1;
    private static final int BUTTON_LEVEL_TYPE_ID = 2;
    private static final int BUTTON_GENERATE_STRUCTURES_ID = 3;
    private final int SEED_EDIT_BOX = 4;
    private RealmsButton resetButton;
    private RealmsButton levelTypeButton;
    private RealmsButton generateStructuresButton;
    
    public RealmsResetNormalWorldScreen(final RealmsResetWorldScreen lastScreen) {
        this.generateStructures = true;
        this.levelTypeIndex = 0;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void tick() {
        this.seedEdit.tick();
        super.tick();
    }
    
    @Override
    public void init() {
        this.levelTypes = new String[] { RealmsScreen.getLocalizedString("generator.default"), RealmsScreen.getLocalizedString("generator.flat"), RealmsScreen.getLocalizedString("generator.largeBiomes"), RealmsScreen.getLocalizedString("generator.amplified") };
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 + 8, RealmsConstants.row(12), 97, 20, RealmsScreen.getLocalizedString("gui.back")));
        this.buttonsAdd(this.resetButton = RealmsScreen.newButton(1, this.width() / 2 - 102, RealmsConstants.row(12), 97, 20, RealmsScreen.getLocalizedString("mco.backup.button.reset")));
        (this.seedEdit = this.newEditBox(4, this.width() / 2 - 100, RealmsConstants.row(2), 200, 20)).setFocus(true);
        this.seedEdit.setMaxLength(32);
        this.seedEdit.setValue("");
        this.buttonsAdd(this.levelTypeButton = RealmsScreen.newButton(2, this.width() / 2 - 102, RealmsConstants.row(4), 205, 20, this.levelTypeTitle()));
        this.buttonsAdd(this.generateStructuresButton = RealmsScreen.newButton(3, this.width() / 2 - 102, RealmsConstants.row(6) - 2, 205, 20, this.generateStructuresTitle()));
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        this.seedEdit.keyPressed(ch, eventKey);
        if (eventKey == 28 || eventKey == 156) {
            this.buttonClicked(this.resetButton);
        }
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 1: {
                this.lastScreen.resetWorld(new RealmsResetWorldScreen.ResetWorldInfo(this.seedEdit.getValue(), this.levelTypeIndex, this.generateStructures));
                break;
            }
            case 2: {
                this.levelTypeIndex = (this.levelTypeIndex + 1) % this.levelTypes.length;
                button.msg(this.levelTypeTitle());
                break;
            }
            case 3: {
                this.generateStructures = !this.generateStructures;
                button.msg(this.generateStructuresTitle());
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.seedEdit.mouseClicked(x, y, buttonNum);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.reset.world.generate"), this.width() / 2, 17, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.reset.world.seed"), this.width() / 2 - 100, RealmsConstants.row(1), 10526880);
        this.seedEdit.render();
        super.render(xm, ym, a);
    }
    
    private String levelTypeTitle() {
        final String levelType = RealmsScreen.getLocalizedString("selectWorld.mapType");
        return levelType + " " + this.levelTypes[this.levelTypeIndex];
    }
    
    private String generateStructuresTitle() {
        return RealmsScreen.getLocalizedString("selectWorld.mapFeatures") + " " + (this.generateStructures ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
}
