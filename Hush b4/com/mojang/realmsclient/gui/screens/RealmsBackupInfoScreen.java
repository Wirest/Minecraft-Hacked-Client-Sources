// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Tezzelator;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.mojang.realmsclient.dto.Backup;
import net.minecraft.realms.RealmsScreen;

public class RealmsBackupInfoScreen extends RealmsScreen
{
    private final RealmsScreen lastScreen;
    private final int BUTTON_BACK_ID = 0;
    private final Backup backup;
    private List<String> keys;
    private BackupInfoList backupInfoList;
    String[] difficulties;
    String[] gameModes;
    
    public RealmsBackupInfoScreen(final RealmsScreen lastScreen, final Backup backup) {
        this.keys = new ArrayList<String>();
        this.difficulties = new String[] { RealmsScreen.getLocalizedString("options.difficulty.peaceful"), RealmsScreen.getLocalizedString("options.difficulty.easy"), RealmsScreen.getLocalizedString("options.difficulty.normal"), RealmsScreen.getLocalizedString("options.difficulty.hard") };
        this.gameModes = new String[] { RealmsScreen.getLocalizedString("selectWorld.gameMode.survival"), RealmsScreen.getLocalizedString("selectWorld.gameMode.creative"), RealmsScreen.getLocalizedString("selectWorld.gameMode.adventure") };
        this.lastScreen = lastScreen;
        this.backup = backup;
        if (backup.changeList != null) {
            for (final Map.Entry<String, String> entry : backup.changeList.entrySet()) {
                this.keys.add(entry.getKey());
            }
        }
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.backupInfoList.mouseEvent();
    }
    
    @Override
    public void tick() {
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 24, RealmsScreen.getLocalizedString("gui.back")));
        this.backupInfoList = new BackupInfoList();
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
        if (button.id() == 0) {
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
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString("Changes from last backup", this.width() / 2, 10, 16777215);
        this.backupInfoList.render(xm, ym, a);
        super.render(xm, ym, a);
    }
    
    private String checkForSpecificMetadata(final String key, final String value) {
        final String k = key.toLowerCase();
        if (k.contains("game") && k.contains("mode")) {
            return this.gameModeMetadata(value);
        }
        if (k.contains("game") && k.contains("difficulty")) {
            return this.gameDifficultyMetadata(value);
        }
        return value;
    }
    
    private String gameDifficultyMetadata(final String value) {
        try {
            return this.difficulties[Integer.parseInt(value)];
        }
        catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    private String gameModeMetadata(final String value) {
        try {
            return this.gameModes[Integer.parseInt(value)];
        }
        catch (Exception e) {
            return "UNKNOWN";
        }
    }
    
    private class BackupInfoList extends RealmsSimpleScrolledSelectionList
    {
        public BackupInfoList() {
            super(RealmsBackupInfoScreen.this.width(), RealmsBackupInfoScreen.this.height(), 32, RealmsBackupInfoScreen.this.height() - 64, 36);
        }
        
        @Override
        public int getItemCount() {
            return RealmsBackupInfoScreen.this.backup.changeList.size();
        }
        
        @Override
        public void selectItem(final int item, final boolean doubleClick, final int xMouse, final int yMouse) {
        }
        
        @Override
        public boolean isSelectedItem(final int item) {
            return false;
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public void renderBackground() {
        }
        
        @Override
        protected void renderItem(final int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            final String key = RealmsBackupInfoScreen.this.keys.get(i);
            RealmsBackupInfoScreen.this.drawString(key, this.width() / 2 - 40, y, 10526880);
            final String metadataValue = RealmsBackupInfoScreen.this.backup.changeList.get(key);
            RealmsBackupInfoScreen.this.drawString(RealmsBackupInfoScreen.this.checkForSpecificMetadata(key, metadataValue), this.width() / 2 - 40, y + 12, 16777215);
        }
    }
}
