// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Date;
import net.minecraft.realms.Tezzelator;
import net.minecraft.realms.RealmsScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import java.util.Collections;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import net.minecraft.realms.RealmsLevelSummary;
import java.util.List;
import java.text.DateFormat;
import net.minecraft.realms.RealmsButton;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSelectFileToUploadScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final int CANCEL_BUTTON = 1;
    private static final int UPLOAD_BUTTON = 2;
    private final RealmsResetWorldScreen lastScreen;
    private final long worldId;
    private int slotId;
    private RealmsButton uploadButton;
    private final DateFormat DATE_FORMAT;
    private List<RealmsLevelSummary> levelList;
    private int selectedWorld;
    private WorldSelectionList worldSelectionList;
    private String worldLang;
    private String conversionLang;
    private String[] gameModesLang;
    
    public RealmsSelectFileToUploadScreen(final long worldId, final int slotId, final RealmsResetWorldScreen lastScreen) {
        this.DATE_FORMAT = new SimpleDateFormat();
        this.levelList = new ArrayList<RealmsLevelSummary>();
        this.selectedWorld = -1;
        this.gameModesLang = new String[4];
        this.lastScreen = lastScreen;
        this.worldId = worldId;
        this.slotId = slotId;
    }
    
    private void loadLevelList() throws Exception {
        final RealmsAnvilLevelStorageSource levelSource = this.getLevelStorageSource();
        Collections.sort(this.levelList = levelSource.getLevelList());
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        try {
            this.loadLevelList();
        }
        catch (Exception e) {
            RealmsSelectFileToUploadScreen.LOGGER.error("Couldn't load level list", e);
            Realms.setScreen(new RealmsGenericErrorScreen("Unable to load worlds", e.getMessage(), this.lastScreen));
            return;
        }
        this.worldLang = RealmsScreen.getLocalizedString("selectWorld.world");
        this.conversionLang = RealmsScreen.getLocalizedString("selectWorld.conversion");
        this.gameModesLang[Realms.survivalId()] = RealmsScreen.getLocalizedString("gameMode.survival");
        this.gameModesLang[Realms.creativeId()] = RealmsScreen.getLocalizedString("gameMode.creative");
        this.gameModesLang[Realms.adventureId()] = RealmsScreen.getLocalizedString("gameMode.adventure");
        this.gameModesLang[Realms.spectatorId()] = RealmsScreen.getLocalizedString("gameMode.spectator");
        this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 + 6, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString("gui.back")));
        this.buttonsAdd(this.uploadButton = RealmsScreen.newButton(2, this.width() / 2 - 154, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString("mco.upload.button.name")));
        this.uploadButton.active(this.selectedWorld >= 0 && this.selectedWorld < this.levelList.size());
        this.worldSelectionList = new WorldSelectionList();
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
        if (button.id() == 1) {
            Realms.setScreen(this.lastScreen);
        }
        else if (button.id() == 2) {
            this.upload();
        }
    }
    
    private void upload() {
        if (this.selectedWorld != -1 && !this.levelList.get(this.selectedWorld).isHardcore()) {
            final RealmsLevelSummary selectedLevel = this.levelList.get(this.selectedWorld);
            Realms.setScreen(new RealmsUploadScreen(this.worldId, this.slotId, this.lastScreen, selectedLevel));
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.worldSelectionList.render(xm, ym, a);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.upload.select.world.title"), this.width() / 2, 13, 16777215);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.upload.select.world.subtitle"), this.width() / 2, RealmsConstants.row(-1), 10526880);
        if (this.levelList.size() == 0) {
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.upload.select.world.none"), this.width() / 2, this.height() / 2 - 20, 16777215);
        }
        super.render(xm, ym, a);
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.worldSelectionList.mouseEvent();
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class WorldSelectionList extends RealmsScrolledSelectionList
    {
        public WorldSelectionList() {
            super(RealmsSelectFileToUploadScreen.this.width(), RealmsSelectFileToUploadScreen.this.height(), RealmsConstants.row(0), RealmsSelectFileToUploadScreen.this.height() - 40, 36);
        }
        
        @Override
        public int getItemCount() {
            return RealmsSelectFileToUploadScreen.this.levelList.size();
        }
        
        @Override
        public void selectItem(final int item, final boolean doubleClick, final int xMouse, final int yMouse) {
            RealmsSelectFileToUploadScreen.this.selectedWorld = item;
            RealmsSelectFileToUploadScreen.this.uploadButton.active(RealmsSelectFileToUploadScreen.this.selectedWorld >= 0 && RealmsSelectFileToUploadScreen.this.selectedWorld < this.getItemCount() && !RealmsSelectFileToUploadScreen.this.levelList.get(RealmsSelectFileToUploadScreen.this.selectedWorld).isHardcore());
            if (doubleClick) {
                RealmsSelectFileToUploadScreen.this.upload();
            }
        }
        
        @Override
        public boolean isSelectedItem(final int item) {
            return item == RealmsSelectFileToUploadScreen.this.selectedWorld;
        }
        
        @Override
        public int getMaxPosition() {
            return RealmsSelectFileToUploadScreen.this.levelList.size() * 36;
        }
        
        @Override
        public void renderBackground() {
            RealmsSelectFileToUploadScreen.this.renderBackground();
        }
        
        @Override
        protected void renderItem(final int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            final RealmsLevelSummary levelSummary = RealmsSelectFileToUploadScreen.this.levelList.get(i);
            String name = levelSummary.getLevelName();
            if (name == null || name.isEmpty()) {
                name = RealmsSelectFileToUploadScreen.this.worldLang + " " + (i + 1);
            }
            String id = levelSummary.getLevelId();
            id = id + " (" + RealmsSelectFileToUploadScreen.this.DATE_FORMAT.format(new Date(levelSummary.getLastPlayed()));
            id += ")";
            String info = "";
            if (levelSummary.isRequiresConversion()) {
                info = RealmsSelectFileToUploadScreen.this.conversionLang + " " + info;
            }
            else {
                info = RealmsSelectFileToUploadScreen.this.gameModesLang[levelSummary.getGameMode()];
                if (levelSummary.isHardcore()) {
                    info = ChatFormatting.DARK_RED + RealmsScreen.getLocalizedString("mco.upload.hardcore") + ChatFormatting.RESET;
                }
                if (levelSummary.hasCheats()) {
                    info = info + ", " + RealmsScreen.getLocalizedString("selectWorld.cheats");
                }
            }
            RealmsSelectFileToUploadScreen.this.drawString(name, x + 2, y + 1, 16777215);
            RealmsSelectFileToUploadScreen.this.drawString(id, x + 2, y + 12, 8421504);
            RealmsSelectFileToUploadScreen.this.drawString(info, x + 2, y + 12 + 10, 8421504);
        }
    }
}
