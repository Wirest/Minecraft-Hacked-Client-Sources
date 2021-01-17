// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.Tezzelator;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.RealmsSharedConstants;
import org.lwjgl.input.Mouse;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import org.lwjgl.input.Keyboard;
import java.util.Collections;
import net.minecraft.realms.RealmsButton;
import java.util.List;
import com.mojang.realmsclient.dto.WorldTemplate;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSelectWorldTemplateScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final String LINK_ICON = "realms:textures/gui/realms/link_icons.png";
    private static final String TRAILER_ICON = "realms:textures/gui/realms/trailer_icons.png";
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private final RealmsScreenWithCallback<WorldTemplate> lastScreen;
    private WorldTemplate selectedWorldTemplate;
    private List<WorldTemplate> templates;
    private WorldTemplateSelectionList worldTemplateSelectionList;
    private int selectedTemplate;
    private String title;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_SELECT_ID = 1;
    private RealmsButton selectButton;
    private String toolTip;
    private String currentLink;
    private boolean isMiniGame;
    private boolean displayWarning;
    private int clicks;
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback<WorldTemplate> lastScreen, final WorldTemplate selectedWorldTemplate, final boolean isMiniGame) {
        this.templates = Collections.emptyList();
        this.selectedTemplate = -1;
        this.toolTip = null;
        this.currentLink = null;
        this.displayWarning = false;
        this.clicks = 0;
        this.lastScreen = lastScreen;
        this.selectedWorldTemplate = selectedWorldTemplate;
        this.isMiniGame = isMiniGame;
        this.title = (isMiniGame ? RealmsScreen.getLocalizedString("mco.template.title.minigame") : RealmsScreen.getLocalizedString("mco.template.title"));
    }
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback<WorldTemplate> lastScreen, final WorldTemplate selectedWorldTemplate, final boolean isMiniGame, final boolean displayWarning) {
        this(lastScreen, selectedWorldTemplate, isMiniGame);
        this.displayWarning = displayWarning;
    }
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback<WorldTemplate> lastScreen, final WorldTemplate selectedWorldTemplate, final boolean isMiniGame, final boolean displayWarning, final List<WorldTemplate> templates) {
        this(lastScreen, selectedWorldTemplate, isMiniGame, displayWarning);
        this.templates = templates;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.worldTemplateSelectionList.mouseEvent();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.worldTemplateSelectionList = new WorldTemplateSelectionList();
        if (this.templates.size() == 0) {
            final boolean isMiniGame = this.isMiniGame;
            new Thread("Realms-minigame-fetcher") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        if (isMiniGame) {
                            RealmsSelectWorldTemplateScreen.this.templates = client.fetchMinigames().templates;
                        }
                        else {
                            RealmsSelectWorldTemplateScreen.this.templates = client.fetchWorldTemplates().templates;
                        }
                    }
                    catch (RealmsServiceException e) {
                        RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates");
                    }
                }
            }.start();
        }
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 + 6, this.height() - 32, 153, 20, this.isMiniGame ? RealmsScreen.getLocalizedString("gui.cancel") : RealmsScreen.getLocalizedString("gui.back")));
        this.buttonsAdd(this.selectButton = RealmsScreen.newButton(1, this.width() / 2 - 154, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString("mco.template.button.select")));
        this.selectButton.active(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                this.backButtonClicked();
                break;
            }
            case 1: {
                this.selectTemplate();
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }
    
    private void backButtonClicked() {
        this.lastScreen.callback(null);
        Realms.setScreen(this.lastScreen);
    }
    
    private void selectTemplate() {
        if (this.selectedTemplate >= 0 && this.selectedTemplate < this.templates.size()) {
            final WorldTemplate template = this.templates.get(this.selectedTemplate);
            template.setMinigame(this.isMiniGame);
            this.lastScreen.callback(template);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.currentLink = null;
        this.renderBackground();
        this.worldTemplateSelectionList.render(xm, ym, a);
        this.drawCenteredString(this.title, this.width() / 2, 13, 16777215);
        if (this.displayWarning) {
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.minigame.world.info1"), this.width() / 2, RealmsConstants.row(-1), 10526880);
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.minigame.world.info2"), this.width() / 2, RealmsConstants.row(0), 10526880);
        }
        super.render(xm, ym, a);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        final int rx = x + 12;
        final int ry = y - 12;
        final int width = this.fontWidth(msg);
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 16777215);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class WorldTemplateSelectionList extends RealmsClickableScrolledSelectionList
    {
        public WorldTemplateSelectionList() {
            super(RealmsSelectWorldTemplateScreen.this.width(), RealmsSelectWorldTemplateScreen.this.height(), RealmsSelectWorldTemplateScreen.this.displayWarning ? RealmsConstants.row(1) : 32, RealmsSelectWorldTemplateScreen.this.height() - 40, 46);
        }
        
        @Override
        public int getItemCount() {
            return RealmsSelectWorldTemplateScreen.this.templates.size();
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = this.width() / 2 - 150;
                final int x2 = this.width();
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                    if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                        return;
                    }
                    RealmsSelectWorldTemplateScreen.this.selectButton.active(true);
                    RealmsSelectWorldTemplateScreen.this.selectedTemplate = slot;
                    RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate = null;
                    RealmsSelectWorldTemplateScreen.this.clicks += RealmsSharedConstants.TICKS_PER_SECOND / 3 + 1;
                    if (RealmsSelectWorldTemplateScreen.this.clicks >= RealmsSharedConstants.TICKS_PER_SECOND / 2) {
                        RealmsSelectWorldTemplateScreen.this.selectTemplate();
                    }
                }
            }
        }
        
        @Override
        public boolean isSelectedItem(final int item) {
            if (RealmsSelectWorldTemplateScreen.this.templates.size() == 0) {
                return false;
            }
            if (item >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                return false;
            }
            if (RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate != null) {
                final boolean same = RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate.name.equals(RealmsSelectWorldTemplateScreen.this.templates.get(item).name);
                if (same) {
                    RealmsSelectWorldTemplateScreen.this.selectedTemplate = item;
                }
                return same;
            }
            return item == RealmsSelectWorldTemplateScreen.this.selectedTemplate;
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, final int slot, final int xm, final int ym, final int width) {
            if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                return;
            }
            if (RealmsSelectWorldTemplateScreen.this.currentLink != null) {
                RealmsUtil.browseTo(RealmsSelectWorldTemplateScreen.this.currentLink);
            }
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 46;
        }
        
        @Override
        public void renderBackground() {
            RealmsSelectWorldTemplateScreen.this.renderBackground();
        }
        
        @Override
        public void renderItem(final int i, final int x, final int y, final int h, final int mouseX, final int mouseY) {
            if (i < RealmsSelectWorldTemplateScreen.this.templates.size()) {
                this.renderWorldTemplateItem(i, x, y, h);
            }
        }
        
        @Override
        public int getScrollbarPosition() {
            return super.getScrollbarPosition() + 30;
        }
        
        @Override
        public void renderSelected(final int width, final int y, final int h, final Tezzelator t) {
            final int x0 = this.getScrollbarPosition() - 290;
            final int x2 = this.getScrollbarPosition() - 10;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
            t.vertex(x0, y + h + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y + h + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0, y - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0 + 1, y + h + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y + h + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x0 + 1, y - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
        }
        
        private void renderWorldTemplateItem(final int i, final int x, final int y, final int h) {
            final WorldTemplate worldTemplate = RealmsSelectWorldTemplateScreen.this.templates.get(i);
            final int textStart = x + 20;
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.name, textStart, y + 2, 16777215);
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.author, textStart, y + 15, 7105644);
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.version, textStart + 227 - RealmsSelectWorldTemplateScreen.this.fontWidth(worldTemplate.version), y + 1, 7105644);
            if (!worldTemplate.link.equals("") || !worldTemplate.trailer.equals("") || !worldTemplate.recommendedPlayers.equals("")) {
                this.drawIcons(textStart - 1, y + 25, this.xm(), this.ym(), worldTemplate.link, worldTemplate.trailer, worldTemplate.recommendedPlayers);
            }
            this.drawImage(x - 25, y + 1, this.xm(), this.ym(), worldTemplate);
        }
        
        private void drawImage(final int x, final int y, final int xm, final int ym, final WorldTemplate worldTemplate) {
            RealmsTextureManager.bindWorldTemplate(worldTemplate.id, worldTemplate.image);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x + 1, y + 1, 0.0f, 0.0f, 38, 38, 38.0f, 38.0f);
            RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x, y, 0.0f, 0.0f, 40, 40, 40.0f, 40.0f);
        }
        
        private void drawIcons(final int x, final int y, final int xm, final int ym, final String link, final String trailerLink, final String recommendedPlayers) {
            boolean linkHovered = false;
            boolean trailerHovered = false;
            if (!recommendedPlayers.equals("")) {
                RealmsSelectWorldTemplateScreen.this.drawString(recommendedPlayers, x, y + 4, 5000268);
            }
            final int offset = recommendedPlayers.equals("") ? 0 : (RealmsSelectWorldTemplateScreen.this.fontWidth(recommendedPlayers) + 2);
            if (xm >= x + offset && xm <= x + offset + 32 && ym >= y && ym <= y + 15 && ym < RealmsSelectWorldTemplateScreen.this.height() - 15 && ym > 32) {
                if (xm <= x + 15 + offset && xm > offset) {
                    if (!link.equals("")) {
                        linkHovered = true;
                    }
                    else {
                        trailerHovered = true;
                    }
                }
                else if (!link.equals("")) {
                    trailerHovered = true;
                }
            }
            if (!link.equals("")) {
                RealmsScreen.bind("realms:textures/gui/realms/link_icons.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(x + offset, y, linkHovered ? 15.0f : 0.0f, 0.0f, 15, 15, 30.0f, 15.0f);
                GL11.glPopMatrix();
            }
            if (!trailerLink.equals("")) {
                RealmsScreen.bind("realms:textures/gui/realms/trailer_icons.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(x + offset + (link.equals("") ? 0 : 17), y, trailerHovered ? 15.0f : 0.0f, 0.0f, 15, 15, 30.0f, 15.0f);
                GL11.glPopMatrix();
            }
            if (linkHovered && !link.equals("")) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.info.tooltip");
                RealmsSelectWorldTemplateScreen.this.currentLink = link;
            }
            else if (trailerHovered && !trailerLink.equals("")) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.trailer.tooltip");
                RealmsSelectWorldTemplateScreen.this.currentLink = trailerLink;
            }
        }
    }
}
