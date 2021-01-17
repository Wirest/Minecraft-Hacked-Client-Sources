// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.RealmsConstants;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.Logger;
import com.mojang.realmsclient.dto.WorldTemplate;

public class RealmsResetWorldScreen extends RealmsScreenWithCallback<WorldTemplate>
{
    private static final Logger LOGGER;
    private RealmsScreen lastScreen;
    private RealmsServer serverData;
    private RealmsScreen returnScreen;
    private String title;
    private String subtitle;
    private String buttonTitle;
    private int subtitleColor;
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String UPLOAD_LOCATION = "realms:textures/gui/realms/upload.png";
    private final int BUTTON_CANCEL_ID = 0;
    private boolean loaded;
    private List<WorldTemplate> templates;
    private List<WorldTemplate> adventuremaps;
    private final Random random;
    private ResetType selectedType;
    private int templateId;
    private int adventureMapId;
    public int slot;
    private ResetType typeToReset;
    private ResetWorldInfo worldInfoToReset;
    private WorldTemplate worldTemplateToReset;
    private String resetTitle;
    
    public RealmsResetWorldScreen(final RealmsScreen lastScreen, final RealmsServer serverData, final RealmsScreen returnScreen) {
        this.title = RealmsScreen.getLocalizedString("mco.reset.world.title");
        this.subtitle = RealmsScreen.getLocalizedString("mco.reset.world.warning");
        this.buttonTitle = RealmsScreen.getLocalizedString("gui.cancel");
        this.subtitleColor = 16711680;
        this.loaded = false;
        this.templates = new ArrayList<WorldTemplate>();
        this.adventuremaps = new ArrayList<WorldTemplate>();
        this.random = new Random();
        this.selectedType = ResetType.NONE;
        this.slot = -1;
        this.typeToReset = ResetType.NONE;
        this.worldInfoToReset = null;
        this.worldTemplateToReset = null;
        this.resetTitle = null;
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.returnScreen = returnScreen;
    }
    
    public RealmsResetWorldScreen(final RealmsScreen lastScreen, final RealmsServer serverData, final RealmsScreen returnScreen, final String title, final String subtitle, final int subtitleColor, final String buttonTitle) {
        this(lastScreen, serverData, returnScreen);
        this.title = title;
        this.subtitle = subtitle;
        this.subtitleColor = subtitleColor;
        this.buttonTitle = buttonTitle;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
    
    public void setResetTitle(final String title) {
        this.resetTitle = title;
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 40, RealmsConstants.row(14) - 10, 80, 20, this.buttonTitle));
        if (!this.loaded) {
            new Thread("Realms-reset-world-fetcher") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        for (final WorldTemplate wt : client.fetchWorldTemplates().templates) {
                            if (!wt.recommendedPlayers.equals("")) {
                                RealmsResetWorldScreen.this.adventuremaps.add(wt);
                            }
                            else {
                                RealmsResetWorldScreen.this.templates.add(wt);
                            }
                        }
                        RealmsResetWorldScreen.this.templateId = RealmsResetWorldScreen.this.random.nextInt(RealmsResetWorldScreen.this.templates.size());
                        RealmsResetWorldScreen.this.adventureMapId = RealmsResetWorldScreen.this.random.nextInt(RealmsResetWorldScreen.this.adventuremaps.size());
                        RealmsResetWorldScreen.this.loaded = true;
                    }
                    catch (RealmsServiceException e) {
                        RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world");
                    }
                }
            }.start();
        }
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
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
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        switch (this.selectedType) {
            case NONE: {
                break;
            }
            case GENERATE: {
                Realms.setScreen(new RealmsResetNormalWorldScreen(this));
                break;
            }
            case UPLOAD: {
                Realms.setScreen(new RealmsSelectFileToUploadScreen(this.serverData.id, (this.slot != -1) ? this.slot : this.serverData.activeSlot, this));
                break;
            }
            case ADVENTURE: {
                final RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen(this, null, false, false, this.adventuremaps);
                screen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.adventure"));
                Realms.setScreen(screen);
                break;
            }
            case SURVIVAL_SPAWN: {
                final RealmsSelectWorldTemplateScreen templateScreen = new RealmsSelectWorldTemplateScreen(this, null, false, false, this.templates);
                templateScreen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.template"));
                Realms.setScreen(templateScreen);
                break;
            }
            default: {}
        }
    }
    
    private int frame(final int i) {
        return this.width() / 2 - 80 + (i - 1) * 100;
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.selectedType = ResetType.NONE;
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, 7, 16777215);
        this.drawCenteredString(this.subtitle, this.width() / 2, 22, this.subtitleColor);
        if (this.loaded) {
            this.drawFrame(this.frame(1), RealmsConstants.row(0) + 10, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.generate"), -1L, "textures/gui/title/background/panorama_3.png", ResetType.GENERATE);
            this.drawFrame(this.frame(2), RealmsConstants.row(0) + 10, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.upload"), -1L, "realms:textures/gui/realms/upload.png", ResetType.UPLOAD);
            this.drawFrame(this.frame(1), RealmsConstants.row(6) + 20, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.adventure"), Long.valueOf(this.adventuremaps.get(this.adventureMapId).id), this.adventuremaps.get(this.adventureMapId).image, ResetType.ADVENTURE);
            this.drawFrame(this.frame(2), RealmsConstants.row(6) + 20, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.template"), Long.valueOf(this.templates.get(this.templateId).id), this.templates.get(this.templateId).image, ResetType.SURVIVAL_SPAWN);
        }
        super.render(xm, ym, a);
    }
    
    private void drawFrame(final int x, final int y, final int xm, final int ym, final String text, final long imageId, final String image, final ResetType resetType) {
        boolean hovered = false;
        if (xm >= x && xm <= x + 60 && ym >= y - 12 && ym <= y + 60) {
            hovered = true;
            this.selectedType = resetType;
        }
        if (imageId != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        }
        else {
            RealmsScreen.bind(image);
        }
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x + 2, y + 2, 0.0f, 0.0f, 56, 56, 56.0f, 56.0f);
        RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 60, 60, 60.0f, 60.0f);
        this.drawCenteredString(text, x + 30, y - 12, hovered ? 10526880 : 16777215);
    }
    
    @Override
    void callback(final WorldTemplate worldTemplate) {
        if (worldTemplate != null) {
            if (this.slot != -1) {
                this.typeToReset = (worldTemplate.recommendedPlayers.equals("") ? ResetType.SURVIVAL_SPAWN : ResetType.ADVENTURE);
                this.worldTemplateToReset = worldTemplate;
                this.switchSlot();
            }
            else {
                this.resetWorldWithTemplate(worldTemplate);
            }
        }
    }
    
    private void switchSlot() {
        this.switchSlot(this);
    }
    
    public void switchSlot(final RealmsScreen screen) {
        final RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, this.slot, screen, 100);
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 100 && result) {
            switch (this.typeToReset) {
                case ADVENTURE:
                case SURVIVAL_SPAWN: {
                    if (this.worldTemplateToReset != null) {
                        this.resetWorldWithTemplate(this.worldTemplateToReset);
                        break;
                    }
                    break;
                }
                case GENERATE: {
                    if (this.worldInfoToReset != null) {
                        this.triggerResetWorld(this.worldInfoToReset);
                        break;
                    }
                    break;
                }
                default: {}
            }
            return;
        }
        if (result) {
            Realms.setScreen(this.returnScreen);
        }
    }
    
    public void resetWorldWithTemplate(final WorldTemplate template) {
        final RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, template);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    public void resetWorld(final ResetWorldInfo resetWorldInfo) {
        if (this.slot != -1) {
            this.typeToReset = ResetType.GENERATE;
            this.worldInfoToReset = resetWorldInfo;
            this.switchSlot();
        }
        else {
            this.triggerResetWorld(resetWorldInfo);
        }
    }
    
    private void triggerResetWorld(final ResetWorldInfo resetWorldInfo) {
        final RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, resetWorldInfo.seed, resetWorldInfo.levelType, resetWorldInfo.generateStructures);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    enum ResetType
    {
        NONE, 
        GENERATE, 
        UPLOAD, 
        ADVENTURE, 
        SURVIVAL_SPAWN;
    }
    
    public static class ResetWorldInfo
    {
        String seed;
        int levelType;
        boolean generateStructures;
        
        public ResetWorldInfo(final String seed, final int levelType, final boolean generateStructures) {
            this.seed = seed;
            this.levelType = levelType;
            this.generateStructures = generateStructures;
        }
    }
}
