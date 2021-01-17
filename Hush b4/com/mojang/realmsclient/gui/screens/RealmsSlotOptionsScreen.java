// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsSliderButton;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsOptions;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;

public class RealmsSlotOptionsScreen extends RealmsScreen
{
    private static final int BUTTON_CANCEL_ID = 0;
    private static final int BUTTON_DONE_ID = 1;
    private static final int BUTTON_DIFFICULTY_ID = 2;
    private static final int BUTTON_GAMEMODE_ID = 3;
    private static final int BUTTON_PVP_ID = 4;
    private static final int BUTTON_SPAWN_ANIMALS_ID = 5;
    private static final int BUTTON_SPAWN_MONSTERS_ID = 6;
    private static final int BUTTON_SPAWN_NPCS_ID = 7;
    private static final int BUTTON_SPAWN_PROTECTION_ID = 8;
    private static final int BUTTON_COMMANDBLOCKS_ID = 9;
    private static final int BUTTON_FORCE_GAMEMODE_ID = 10;
    private static final int NAME_EDIT_BOX = 11;
    private RealmsEditBox nameEdit;
    protected final RealmsConfigureWorldScreen parent;
    private int column1_x;
    private int column_width;
    private int column2_x;
    private RealmsOptions options;
    private RealmsServer.WorldType worldType;
    private int activeSlot;
    private int difficultyIndex;
    private int gameModeIndex;
    private Boolean pvp;
    private Boolean spawnNPCs;
    private Boolean spawnAnimals;
    private Boolean spawnMonsters;
    private Integer spawnProtection;
    private Boolean commandBlocks;
    private Boolean forceGameMode;
    private RealmsButton pvpButton;
    private RealmsButton spawnAnimalsButton;
    private RealmsButton spawnMonstersButton;
    private RealmsButton spawnNPCsButton;
    private RealmsSliderButton spawnProtectionButton;
    private RealmsButton commandBlocksButton;
    private RealmsButton forceGameModeButton;
    private boolean notNormal;
    String[] difficulties;
    String[] gameModes;
    String[][] gameModeHints;
    
    public RealmsSlotOptionsScreen(final RealmsConfigureWorldScreen configureWorldScreen, final RealmsOptions options, final RealmsServer.WorldType worldType, final int activeSlot) {
        this.notNormal = false;
        this.parent = configureWorldScreen;
        this.options = options;
        this.worldType = worldType;
        this.activeSlot = activeSlot;
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 1: {
                this.saveSettings();
                break;
            }
            case 0: {
                Realms.setScreen(this.parent);
                break;
            }
            case 2: {
                this.difficultyIndex = (this.difficultyIndex + 1) % this.difficulties.length;
                button.msg(this.difficultyTitle());
                if (this.worldType.equals(RealmsServer.WorldType.NORMAL)) {
                    this.spawnMonstersButton.active(this.difficultyIndex != 0);
                    this.spawnMonstersButton.msg(this.spawnMonstersTitle());
                    break;
                }
                break;
            }
            case 3: {
                this.gameModeIndex = (this.gameModeIndex + 1) % this.gameModes.length;
                button.msg(this.gameModeTitle());
                break;
            }
            case 4: {
                this.pvp = !this.pvp;
                button.msg(this.pvpTitle());
                break;
            }
            case 5: {
                this.spawnAnimals = !this.spawnAnimals;
                button.msg(this.spawnAnimalsTitle());
                break;
            }
            case 7: {
                this.spawnNPCs = !this.spawnNPCs;
                button.msg(this.spawnNPCsTitle());
                break;
            }
            case 6: {
                this.spawnMonsters = !this.spawnMonsters;
                button.msg(this.spawnMonstersTitle());
                break;
            }
            case 9: {
                this.commandBlocks = !this.commandBlocks;
                button.msg(this.commandBlocksTitle());
                break;
            }
            case 10: {
                this.forceGameMode = !this.forceGameMode;
                button.msg(this.forceGameModeTitle());
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        this.nameEdit.keyPressed(eventCharacter, eventKey);
        switch (eventKey) {
            case 15: {
                this.nameEdit.setFocus(!this.nameEdit.isFocused());
                break;
            }
            case 1: {
                Realms.setScreen(this.parent);
                break;
            }
            case 28:
            case 156: {
                this.saveSettings();
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        this.nameEdit.mouseClicked(x, y, buttonNum);
    }
    
    @Override
    public void init() {
        this.column1_x = this.width() / 2 - 122;
        this.column_width = 122;
        this.column2_x = this.width() / 2 + 10;
        this.createDifficultyAndGameMode();
        this.difficultyIndex = this.options.difficulty;
        this.gameModeIndex = this.options.gameMode;
        if (!this.worldType.equals(RealmsServer.WorldType.NORMAL)) {
            this.notNormal = true;
            this.pvp = true;
            this.spawnProtection = 0;
            this.forceGameMode = false;
            this.spawnAnimals = true;
            this.spawnMonsters = true;
            this.spawnNPCs = true;
            this.commandBlocks = true;
        }
        else {
            this.pvp = this.options.pvp;
            this.spawnProtection = this.options.spawnProtection;
            this.forceGameMode = this.options.forceGameMode;
            this.spawnAnimals = this.options.spawnAnimals;
            this.spawnMonsters = this.options.spawnMonsters;
            this.spawnNPCs = this.options.spawnNPCs;
            this.commandBlocks = this.options.commandBlocks;
        }
        (this.nameEdit = this.newEditBox(11, this.column1_x + 2, RealmsConstants.row(2), this.column_width - 4, 20)).setFocus(true);
        this.nameEdit.setMaxLength(10);
        this.nameEdit.setValue(this.options.getSlotName(this.activeSlot));
        this.buttonsAdd(RealmsScreen.newButton(3, this.column2_x, RealmsConstants.row(2), this.column_width, 20, this.gameModeTitle()));
        this.buttonsAdd(this.pvpButton = RealmsScreen.newButton(4, this.column1_x, RealmsConstants.row(4), this.column_width, 20, this.pvpTitle()));
        this.buttonsAdd(this.spawnAnimalsButton = RealmsScreen.newButton(5, this.column2_x, RealmsConstants.row(4), this.column_width, 20, this.spawnAnimalsTitle()));
        this.buttonsAdd(RealmsScreen.newButton(2, this.column1_x, RealmsConstants.row(6), this.column_width, 20, this.difficultyTitle()));
        this.buttonsAdd(this.spawnMonstersButton = RealmsScreen.newButton(6, this.column2_x, RealmsConstants.row(6), this.column_width, 20, this.spawnMonstersTitle()));
        this.buttonsAdd(this.spawnProtectionButton = new SettingsSlider(8, this.column1_x, RealmsConstants.row(8), this.column_width, 17, this.spawnProtection, 0.0f, 16.0f));
        this.buttonsAdd(this.spawnNPCsButton = RealmsScreen.newButton(7, this.column2_x, RealmsConstants.row(8), this.column_width, 20, this.spawnNPCsTitle()));
        this.buttonsAdd(this.forceGameModeButton = RealmsScreen.newButton(10, this.column1_x, RealmsConstants.row(10), this.column_width, 20, this.forceGameModeTitle()));
        this.buttonsAdd(this.commandBlocksButton = RealmsScreen.newButton(9, this.column2_x, RealmsConstants.row(10), this.column_width, 20, this.commandBlocksTitle()));
        if (!this.worldType.equals(RealmsServer.WorldType.NORMAL)) {
            this.pvpButton.active(false);
            this.spawnAnimalsButton.active(false);
            this.spawnNPCsButton.active(false);
            this.spawnMonstersButton.active(false);
            this.spawnProtectionButton.active(false);
            this.commandBlocksButton.active(false);
            this.spawnProtectionButton.active(false);
            this.forceGameModeButton.active(false);
        }
        if (this.difficultyIndex == 0) {
            this.spawnMonstersButton.active(false);
        }
        this.buttonsAdd(RealmsScreen.newButton(1, this.column1_x, RealmsConstants.row(13), this.column_width, 20, RealmsScreen.getLocalizedString("mco.configure.world.buttons.done")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.column2_x, RealmsConstants.row(13), this.column_width, 20, RealmsScreen.getLocalizedString("gui.cancel")));
    }
    
    private void createDifficultyAndGameMode() {
        this.difficulties = new String[] { RealmsScreen.getLocalizedString("options.difficulty.peaceful"), RealmsScreen.getLocalizedString("options.difficulty.easy"), RealmsScreen.getLocalizedString("options.difficulty.normal"), RealmsScreen.getLocalizedString("options.difficulty.hard") };
        this.gameModes = new String[] { RealmsScreen.getLocalizedString("selectWorld.gameMode.survival"), RealmsScreen.getLocalizedString("selectWorld.gameMode.creative"), RealmsScreen.getLocalizedString("selectWorld.gameMode.adventure") };
        this.gameModeHints = new String[][] { { RealmsScreen.getLocalizedString("selectWorld.gameMode.survival.line1"), RealmsScreen.getLocalizedString("selectWorld.gameMode.survival.line2") }, { RealmsScreen.getLocalizedString("selectWorld.gameMode.creative.line1"), RealmsScreen.getLocalizedString("selectWorld.gameMode.creative.line2") }, { RealmsScreen.getLocalizedString("selectWorld.gameMode.adventure.line1"), RealmsScreen.getLocalizedString("selectWorld.gameMode.adventure.line2") } };
    }
    
    private String difficultyTitle() {
        final String difficulty = RealmsScreen.getLocalizedString("options.difficulty");
        return difficulty + ": " + this.difficulties[this.difficultyIndex];
    }
    
    private String gameModeTitle() {
        final String gameMode = RealmsScreen.getLocalizedString("selectWorld.gameMode");
        return gameMode + ": " + this.gameModes[this.gameModeIndex];
    }
    
    private String pvpTitle() {
        return RealmsScreen.getLocalizedString("mco.configure.world.pvp") + ": " + (this.pvp ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    private String spawnAnimalsTitle() {
        return RealmsScreen.getLocalizedString("mco.configure.world.spawnAnimals") + ": " + (this.spawnAnimals ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    private String spawnMonstersTitle() {
        if (this.difficultyIndex == 0) {
            return RealmsScreen.getLocalizedString("mco.configure.world.spawnMonsters") + ": " + RealmsScreen.getLocalizedString("mco.configure.world.off");
        }
        return RealmsScreen.getLocalizedString("mco.configure.world.spawnMonsters") + ": " + (this.spawnMonsters ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    private String spawnNPCsTitle() {
        return RealmsScreen.getLocalizedString("mco.configure.world.spawnNPCs") + ": " + (this.spawnNPCs ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    private String commandBlocksTitle() {
        return RealmsScreen.getLocalizedString("mco.configure.world.commandBlocks") + ": " + (this.commandBlocks ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    private String forceGameModeTitle() {
        return RealmsScreen.getLocalizedString("mco.configure.world.forceGameMode") + ": " + (this.forceGameMode ? RealmsScreen.getLocalizedString("mco.configure.world.on") : RealmsScreen.getLocalizedString("mco.configure.world.off"));
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        final String slotName = RealmsScreen.getLocalizedString("mco.configure.world.edit.slot.name");
        this.drawString(slotName, this.column1_x + this.fontWidth(slotName) / 2, RealmsConstants.row(0) + 5, 16777215);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.buttons.options"), this.width() / 2, 17, 16777215);
        if (this.notNormal) {
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.edit.subscreen.adventuremap"), this.width() / 2, 30, 16711680);
        }
        this.nameEdit.render();
        super.render(xm, ym, a);
    }
    
    public void renderHints() {
        this.drawString(this.gameModeHints[this.gameModeIndex][0], this.column2_x + 2, RealmsConstants.row(0), 10526880);
        this.drawString(this.gameModeHints[this.gameModeIndex][1], this.column2_x + 2, RealmsConstants.row(0) + this.fontLineHeight() + 2, 10526880);
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int buttonNum) {
        if (!this.spawnProtectionButton.active()) {
            return;
        }
        this.spawnProtectionButton.released(x, y);
    }
    
    @Override
    public void mouseDragged(final int x, final int y, final int buttonNum, final long delta) {
        if (!this.spawnProtectionButton.active()) {
            return;
        }
        if (x < this.column1_x + this.spawnProtectionButton.getWidth() && x > this.column1_x && y < this.spawnProtectionButton.y() + 20 && y > this.spawnProtectionButton.y()) {
            this.spawnProtectionButton.clicked(x, y);
        }
    }
    
    private String getSlotName() {
        if (this.nameEdit.getValue().equals(this.options.getDefaultSlotName(this.activeSlot))) {
            return "";
        }
        return this.nameEdit.getValue();
    }
    
    private void saveSettings() {
        if (this.worldType.equals(RealmsServer.WorldType.ADVENTUREMAP)) {
            this.parent.saveSlotSettings(new RealmsOptions(this.options.pvp, this.options.spawnAnimals, this.options.spawnMonsters, this.options.spawnNPCs, this.options.spawnProtection, this.options.commandBlocks, this.difficultyIndex, this.gameModeIndex, this.options.forceGameMode, this.getSlotName()));
        }
        else {
            this.parent.saveSlotSettings(new RealmsOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficultyIndex, this.gameModeIndex, this.forceGameMode, this.getSlotName()));
        }
    }
    
    private class SettingsSlider extends RealmsSliderButton
    {
        public SettingsSlider(final int id, final int x, final int y, final int width, final int steps, final int currentValue, final float minValue, final float maxValue) {
            super(id, x, y, width, steps, currentValue, minValue, maxValue);
        }
        
        @Override
        public String getMessage() {
            return RealmsScreen.getLocalizedString("mco.configure.world.spawnProtection") + ": " + ((RealmsSlotOptionsScreen.this.spawnProtection == 0) ? RealmsScreen.getLocalizedString("mco.configure.world.off") : RealmsSlotOptionsScreen.this.spawnProtection);
        }
        
        @Override
        public void clicked(final float value) {
            if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active()) {
                return;
            }
            RealmsSlotOptionsScreen.this.spawnProtection = (int)value;
        }
    }
}
