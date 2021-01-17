// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.FoodStats;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import java.util.Collection;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.scoreboard.Score;
import com.google.common.base.Predicate;
import net.minecraft.util.BlockPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import optifine.CustomColors;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import me.nico.hush.utils.UnicodeFontRenderer;
import me.nico.hush.utils.Fonts;
import java.util.Iterator;
import java.util.List;
import de.Hero.clickgui.util.ColorUtil;
import java.util.Collections;
import java.util.Comparator;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;
import java.util.ArrayList;
import me.nico.hush.Client;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;
import net.minecraft.potion.Potion;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class GuiIngame extends Gui
{
    private static final ResourceLocation vignetteTexPath;
    private static final ResourceLocation widgetsTexPath;
    private static final ResourceLocation pumpkinBlurTexPath;
    private final Random rand;
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying;
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int field_175195_w;
    private String field_175201_x;
    private String field_175200_y;
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;
    private String test;
    private int width;
    private int height;
    private static final String __OBFID = "CL_00000661";
    int fps;
    
    static {
        vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    }
    
    public GuiIngame(final Minecraft mcIn) {
        this.rand = new Random();
        this.recordPlaying = "";
        this.prevVignetteBrightness = 1.0f;
        this.field_175201_x = "";
        this.field_175200_y = "";
        this.playerHealth = 0;
        this.lastPlayerHealth = 0;
        this.lastSystemTime = 0L;
        this.healthUpdateCounter = 0L;
        this.fps = 18;
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.func_175177_a();
    }
    
    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }
    
    public void renderGameOverlay(final float partialTicks) {
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        this.renderArrayList();
        this.renderServer();
        this.renderName();
        this.renderFPS();
        this.renderHotbar();
        this.ZodiacName();
        this.ZodiacFps();
        this.ZodiacArrayList();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(Minecraft.thePlayer.getBrightness(partialTicks), scaledresolution);
        }
        else {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        final ItemStack itemstack = Minecraft.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
            final float f = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * partialTicks;
            if (f > 0.0f) {
                this.func_180474_b(f, scaledresolution);
            }
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        final boolean showDebugInfo = this.mc.gameSettings.showDebugInfo;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        GlStateManager.enableBlend();
        if (this.showCrosshair() && this.mc.gameSettings.thirdPersonView < 1) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }
        GlStateManager.disableBlend();
        if (Minecraft.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            final int l = Minecraft.thePlayer.getSleepTimer();
            float f2 = l / 100.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f - (l - 100) / 10.0f;
            }
            final int k = (int)(220.0f * f2) << 24 | 0x101020;
            Gui.drawRect(0.0, 0.0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i2 = i / 2 - 91;
        if (Minecraft.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, i2);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, i2);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.func_181551_a(scaledresolution);
        }
        else if (Minecraft.thePlayer.isSpectator()) {
            this.spectatorGui.func_175263_a(scaledresolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            final float f3 = this.recordPlayingUpFor - partialTicks;
            int k2 = (int)(f3 * 255.0f / 20.0f);
            if (k2 > 255) {
                k2 = 255;
            }
            if (k2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int i3 = 16777215;
                if (this.recordIsPlaying) {
                    i3 = (MathHelper.func_181758_c(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4.0, i3 + (k2 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            final float f4 = this.field_175195_w - partialTicks;
            int l2 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                final float f5 = this.field_175199_z + this.field_175192_A + this.field_175193_B - f4;
                l2 = (int)(f5 * 255.0f / this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                l2 = (int)(f4 * 255.0f / this.field_175193_B);
            }
            l2 = MathHelper.clamp_int(l2, 0, 255);
            if (l2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                final int j2 = l2 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.field_175201_x, (float)(-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.field_175200_y, (float)(-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        final Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(Minecraft.thePlayer.getName());
        if (scoreplayerteam != null) {
            final int j3 = scoreplayerteam.getChatFormat().getColorIndex();
            if (j3 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j3);
            }
        }
        ScoreObjective scoreobjective2 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective2 != null) {
            this.renderScoreboard(scoreobjective2, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, (float)(j - 48), 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective2 = scoreboard.getObjectiveInDisplaySlot(0);
        if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || (this.mc.isIntegratedServerRunning() && Minecraft.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective2 == null)) {
            this.overlayPlayerList.updatePlayerList(false);
        }
        else {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective2);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    private void ZodiacArrayList() {
        if (Client.instance.settingManager.getSettingByName("ArrayList").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            final int index = 0;
            final ScaledResolution sr = new ScaledResolution(this.mc);
            sr.getScaledWidth();
            sr.getScaledHeight();
            final FontRenderer fr = this.mc.fontRendererObj;
            final int i = 0;
            final List<String> display = new ArrayList<String>();
            for (final Module m : Client.instance.moduleManager.getModules()) {
                if (m.isEnabled() && m.visible && !m.isCategory(Category.CLIENT)) {
                    display.add(String.valueOf(m.getDisplayname()));
                }
            }
            Collections.sort(Client.instance.moduleManager.getModules(), new Comparator<Module>() {
                @Override
                public int compare(final Module mod1, final Module mod2) {
                    if (fr.getStringWidth(mod1.getDisplayname()) > fr.getStringWidth(mod2.getDisplayname())) {
                        return -1;
                    }
                    if (fr.getStringWidth(mod1.getDisplayname()) < fr.getStringWidth(mod2.getDisplayname())) {
                        return 1;
                    }
                    return 0;
                }
            });
            int yOffset = 2;
            final int heightOffset = 1;
            int count = 0;
            for (final Module j : Client.instance.moduleManager.getModules()) {
                if (j.isEnabled()) {
                    final int xDistance = sr.getScaledWidth() - fr.getStringWidth(j.getDisplayname()) - 1;
                    final int width = sr.getScaledWidth();
                    final int height = fr.FONT_HEIGHT;
                    fr.drawString(j.getDisplayname(), xDistance, yOffset, ColorUtil.getClickGUIColor().getRGB());
                    yOffset += fr.FONT_HEIGHT + 1;
                    ++count;
                }
            }
            display.clear();
        }
    }
    
    private void renderHotbar() {
        if (Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush") && Client.instance.settingManager.getSettingByName("Hotbar").getValBoolean()) {
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            final int i = scaledResolution.getScaledWidth() / 2;
            Gui.drawRect(0.0, scaledResolution.getScaledHeight() - 22, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), Integer.MIN_VALUE);
            Gui.drawRect(i - 89 - 1 + Minecraft.thePlayer.inventory.currentItem * 20, scaledResolution.getScaledHeight() - 22, i - 91 - 1 + Minecraft.thePlayer.inventory.currentItem * 20 + 24, scaledResolution.getScaledHeight(), ColorUtil.getClickGUIColor().getRGB());
            Gui.drawRect(0.0, scaledResolution.getScaledHeight() - 1, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ColorUtil.getClickGUIColor().getRGB());
            if (this.mc.isFullScreen()) {
                if (Client.instance.settingManager.getSettingByName("Name & Version").getValBoolean()) {
                    Fonts.smallHush2.drawString("hush b4", 3, 521, 16777215);
                }
                if (Client.instance.settingManager.getSettingByName("Cords").getValBoolean()) {
                    Fonts.smallHush2.drawString("X: " + Minecraft.thePlayer.getPosition().getX() + " Y: " + Minecraft.thePlayer.getPosition().getY() + " Z: " + Minecraft.thePlayer.getPosition().getZ(), 863, 521, 16777215);
                }
            }
        }
    }
    
    private void ZodiacName() {
        if (Client.instance.settingManager.getSettingByName("Watermark").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            this.drawString(this.getFontRenderer(), "Zodiac b4", 3, 3, ColorUtil.getClickGUIColor().getRGB());
        }
        else if (Client.instance.settingManager.getSettingByName("Fps").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            this.fps = 3;
            final FontRenderer fontRenderer = this.getFontRenderer();
            final StringBuilder sb = new StringBuilder("fps: ");
            Minecraft.getMinecraft();
            this.drawString(fontRenderer, sb.append(Minecraft.getDebugFPS()).toString(), 3, this.fps, ColorUtil.getClickGUIColor().getRGB());
        }
        if (Client.instance.settingManager.getSettingByName("Watermark").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            this.fps = 18;
        }
    }
    
    private void ZodiacFps() {
        if (Client.instance.settingManager.getSettingByName("Fps").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Zodiac")) {
            final FontRenderer fontRenderer = this.getFontRenderer();
            final StringBuilder sb = new StringBuilder("fps: ");
            Minecraft.getMinecraft();
            this.drawString(fontRenderer, sb.append(Minecraft.getDebugFPS()).toString(), 3, this.fps, ColorUtil.getClickGUIColor().getRGB());
        }
    }
    
    private void renderArrayList() {
        if (Client.instance.settingManager.getSettingByName("ArrayList").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            final int index = 0;
            final ScaledResolution sr = new ScaledResolution(this.mc);
            sr.getScaledWidth();
            sr.getScaledHeight();
            final FontRenderer fr = this.mc.fontRendererObj;
            final int i = 0;
            final List<String> display = new ArrayList<String>();
            for (final Module m : Client.instance.moduleManager.getModules()) {
                if (m.isEnabled() && m.visible && !m.isCategory(Category.CLIENT)) {
                    display.add(String.valueOf(m.getDisplayname()));
                }
            }
            Collections.sort(Client.instance.moduleManager.getModules(), new Comparator<Module>() {
                @Override
                public int compare(final Module mod1, final Module mod2) {
                    if (Fonts.smallHush2.getStringWidth(mod1.getDisplayname()) > Fonts.smallHush2.getStringWidth(mod2.getDisplayname())) {
                        return -1;
                    }
                    if (Fonts.smallHush2.getStringWidth(mod1.getDisplayname()) < Fonts.smallHush2.getStringWidth(mod2.getDisplayname())) {
                        return 1;
                    }
                    return 0;
                }
            });
            int yOffset = -2;
            final int heightOffset = 2;
            int count = 0;
            for (final Module j : Client.instance.moduleManager.getModules()) {
                if (j.isEnabled()) {
                    final int xDistance = sr.getScaledWidth() - Fonts.smallHush2.getStringWidth(j.getDisplayname()) - 1;
                    final int width = sr.getScaledWidth();
                    final int height = Fonts.smallHush2.FONT_HEIGHT;
                    Fonts.smallHush2.drawString(j.getDisplayname(), xDistance, yOffset, j.getColor());
                    yOffset += fr.FONT_HEIGHT + 2;
                    ++count;
                }
            }
            display.clear();
        }
    }
    
    private void renderName() {
        if (Client.instance.settingManager.getSettingByName("Watermark").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            Fonts.watermarkHush2.drawString("hush", 3, -2, 16777215);
        }
        else if (Client.instance.settingManager.getSettingByName("Fps").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            this.fps = 0;
            final UnicodeFontRenderer smallHush2 = Fonts.smallHush2;
            final StringBuilder sb = new StringBuilder("fps: ");
            Minecraft.getMinecraft();
            smallHush2.drawString(sb.append(Minecraft.getDebugFPS()).toString(), 3, this.fps, 16777215);
        }
        if (Client.instance.settingManager.getSettingByName("Watermark").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            this.fps = 28;
        }
    }
    
    private void renderFPS() {
        if (Client.instance.settingManager.getSettingByName("Fps").getValBoolean() && Client.instance.settingManager.getSettingByName("HUDMode").getValString().equalsIgnoreCase("Hush")) {
            final UnicodeFontRenderer smallHush2 = Fonts.smallHush2;
            final StringBuilder sb = new StringBuilder("fps: ");
            Minecraft.getMinecraft();
            smallHush2.drawString(sb.append(Minecraft.getDebugFPS()).toString(), 3, this.fps, 16777215);
        }
    }
    
    private void renderServer() {
        if (this.mc.isSingleplayer()) {
            final String server = "SinglePlayer";
        }
        else {
            final String server = this.mc.getCurrentServerData().serverIP;
        }
    }
    
    protected void renderTooltip(final ScaledResolution sr, final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.widgetsTexPath);
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = sr.getScaledWidth() / 2;
            final float f = this.zLevel;
            this.zLevel = -90.0f;
            this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                final int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                final int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    public void renderHorseJumpBar(final ScaledResolution p_175186_1_, final int p_175186_2_) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final float f = Minecraft.thePlayer.getHorseJumpPower();
        final short short1 = 182;
        final int i = (int)(f * (short1 + 1));
        final int j = p_175186_1_.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, j, 0, 84, short1, 5);
        if (i > 0) {
            this.drawTexturedModalRect(p_175186_2_, j, 0, 89, i, 5);
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void renderExpBar(final ScaledResolution p_175176_1_, final int p_175176_2_) {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final int i = Minecraft.thePlayer.xpBarCap();
        if (i > 0) {
            final short short1 = 182;
            final int k = (int)(Minecraft.thePlayer.experience * (short1 + 1));
            final int j = p_175176_1_.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, j, 0, 64, short1, 5);
            if (k > 0) {
                this.drawTexturedModalRect(p_175176_2_, j, 0, 69, k, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (Minecraft.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int j2 = 8453920;
            if (Config.isCustomColors()) {
                j2 = CustomColors.getExpBarTextColor(j2);
            }
            final String s = new StringBuilder().append(Minecraft.thePlayer.experienceLevel).toString();
            final int i2 = (p_175176_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            final int l = p_175176_1_.getScaledHeight() - 31 - 4;
            final boolean flag = false;
            this.getFontRenderer().drawString(s, i2 + 1, l, 0);
            this.getFontRenderer().drawString(s, i2 - 1, l, 0);
            this.getFontRenderer().drawString(s, i2, l + 1, 0);
            this.getFontRenderer().drawString(s, i2, l - 1, 0);
            this.getFontRenderer().drawString(s, i2, l, j2);
            this.mc.mcProfiler.endSection();
        }
    }
    
    public void func_181551_a(final ScaledResolution p_181551_1_) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }
            final int i = (p_181551_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = p_181551_1_.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }
            int k = (int)(this.remainingHighlightTicks * 256.0f / 10.0f);
            if (k > 255) {
                k = 255;
            }
            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(s, (float)i, (float)j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void renderDemo(final ScaledResolution p_175185_1_) {
        this.mc.mcProfiler.startSection("demo");
        String s = "";
        if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else {
            s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())));
        }
        final int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, (float)(p_175185_1_.getScaledWidth() - i - 10), 5.0f, 16777215);
        this.mc.mcProfiler.endSection();
    }
    
    protected boolean showCrosshair() {
        if (this.mc.gameSettings.showDebugInfo && !Minecraft.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            return false;
        }
        if (!this.mc.playerController.isSpectator()) {
            return true;
        }
        if (this.mc.pointedEntity != null) {
            return true;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            if (this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory) {
                return true;
            }
        }
        return false;
    }
    
    public void renderStreamIndicator(final ScaledResolution p_180478_1_) {
        this.streamIndicator.render(p_180478_1_.getScaledWidth() - 10, 10);
    }
    
    private void renderScoreboard(final ScoreObjective p_180475_1_, final ScaledResolution p_180475_2_) {
        final Scoreboard scoreboard = p_180475_1_.getScoreboard();
        final Collection collection = scoreboard.getSortedScores(p_180475_1_);
        final ArrayList arraylist = Lists.newArrayList((Iterable<?>)Iterables.filter((Iterable<? extends E>)collection, new Predicate() {
            private static final String __OBFID = "CL_00001958";
            
            public boolean apply(final Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
            
            @Override
            public boolean apply(final Object p_apply_1_) {
                return this.apply((Score)p_apply_1_);
            }
        }));
        ArrayList arraylist2;
        if (arraylist.size() > 15) {
            arraylist2 = Lists.newArrayList((Iterable<?>)Iterables.skip((Iterable<? extends E>)arraylist, collection.size() - 15));
        }
        else {
            arraylist2 = arraylist;
        }
        int i = this.getFontRenderer().getStringWidth(p_180475_1_.getDisplayName());
        for (final Object score : arraylist2) {
            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(((Score)score).getPlayerName());
            final String s = String.valueOf(ScorePlayerTeam.formatPlayerName(scoreplayerteam, ((Score)score).getPlayerName())) + ": " + EnumChatFormatting.RED + ((Score)score).getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        final int j1 = arraylist2.size() * this.getFontRenderer().FONT_HEIGHT;
        final int k1 = p_180475_2_.getScaledHeight() / 2 + j1 / 3;
        final byte b0 = 3;
        final int l = p_180475_2_.getScaledWidth() - i - b0;
        int m = 0;
        for (final Object score2 : arraylist2) {
            ++m;
            final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(((Score)score2).getPlayerName());
            final String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, ((Score)score2).getPlayerName());
            final String s3 = new StringBuilder().append(EnumChatFormatting.RED).append(((Score)score2).getScorePoints()).toString();
            final int l2 = k1 - m * this.getFontRenderer().FONT_HEIGHT;
            final int i2 = p_180475_2_.getScaledWidth() - b0 + 2;
            Gui.drawRect(l - 2, l2, i2, l2 + this.getFontRenderer().FONT_HEIGHT, 1342177280);
            this.getFontRenderer().drawString(s2, l, l2, 553648127);
            this.getFontRenderer().drawString(s3, i2 - this.getFontRenderer().getStringWidth(s3), l2, 553648127);
            if (m == arraylist2.size()) {
                final String s4 = p_180475_1_.getDisplayName();
                Gui.drawRect(l - 2, l2 - this.getFontRenderer().FONT_HEIGHT - 1, i2, l2 - 1, 1610612736);
                Gui.drawRect(l - 2, l2 - 1, i2, l2, 1342177280);
                this.getFontRenderer().drawString(s4, l + i / 2 - this.getFontRenderer().getStringWidth(s4) / 2, l2 - this.getFontRenderer().FONT_HEIGHT, 553648127);
            }
        }
    }
    
    private void renderPlayerStats(final ScaledResolution p_180477_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            final boolean flag = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L;
            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            }
            else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i;
            final int j = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            final boolean flag2 = false;
            final FoodStats foodstats = entityplayer.getFoodStats();
            final int k = foodstats.getFoodLevel();
            final int l = foodstats.getPrevFoodLevel();
            final IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            final int i2 = p_180477_1_.getScaledWidth() / 2 - 91;
            final int j2 = p_180477_1_.getScaledWidth() / 2 + 91;
            final int k2 = p_180477_1_.getScaledHeight() - 39;
            final float f = (float)iattributeinstance.getAttributeValue();
            final float f2 = entityplayer.getAbsorptionAmount();
            final int l2 = MathHelper.ceiling_float_int((f + f2) / 2.0f / 10.0f);
            final int i3 = Math.max(10 - (l2 - 2), 3);
            final int j3 = k2 - (l2 - 1) * i3 - 10;
            float f3 = f2;
            final int k3 = entityplayer.getTotalArmorValue();
            int l3 = -1;
            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l3 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            for (int i4 = 0; i4 < 10; ++i4) {
                if (k3 > 0) {
                    final int j4 = i2 + i4 * 8;
                    if (i4 * 2 + 1 < k3) {
                        this.drawTexturedModalRect(j4, j3, 34, 9, 9, 9);
                    }
                    if (i4 * 2 + 1 == k3) {
                        this.drawTexturedModalRect(j4, j3, 25, 9, 9, 9);
                    }
                    if (i4 * 2 + 1 > k3) {
                        this.drawTexturedModalRect(j4, j3, 16, 9, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int j5 = MathHelper.ceiling_float_int((f + f2) / 2.0f) - 1; j5 >= 0; --j5) {
                int k4 = 16;
                if (entityplayer.isPotionActive(Potion.poison)) {
                    k4 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither)) {
                    k4 += 72;
                }
                byte b0 = 0;
                if (flag) {
                    b0 = 1;
                }
                final int k5 = MathHelper.ceiling_float_int((j5 + 1) / 10.0f) - 1;
                final int l4 = i2 + j5 % 10 * 8;
                int i5 = k2 - k5 * i3;
                if (i <= 4) {
                    i5 += this.rand.nextInt(2);
                }
                if (j5 == l3) {
                    i5 -= 2;
                }
                byte b2 = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    b2 = 5;
                }
                this.drawTexturedModalRect(l4, i5, 16 + b0 * 9, 9 * b2, 9, 9);
                if (flag) {
                    if (j5 * 2 + 1 < j) {
                        this.drawTexturedModalRect(l4, i5, k4 + 54, 9 * b2, 9, 9);
                    }
                    if (j5 * 2 + 1 == j) {
                        this.drawTexturedModalRect(l4, i5, k4 + 63, 9 * b2, 9, 9);
                    }
                }
                if (f3 <= 0.0f) {
                    if (j5 * 2 + 1 < i) {
                        this.drawTexturedModalRect(l4, i5, k4 + 36, 9 * b2, 9, 9);
                    }
                    if (j5 * 2 + 1 == i) {
                        this.drawTexturedModalRect(l4, i5, k4 + 45, 9 * b2, 9, 9);
                    }
                }
                else {
                    if (f3 == f2 && f2 % 2.0f == 1.0f) {
                        this.drawTexturedModalRect(l4, i5, k4 + 153, 9 * b2, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(l4, i5, k4 + 144, 9 * b2, 9, 9);
                    }
                    f3 -= 2.0f;
                }
            }
            final Entity entity = entityplayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int l5 = 0; l5 < 10; ++l5) {
                    int i6 = k2;
                    int j6 = 16;
                    byte b3 = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        j6 += 36;
                        b3 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                        i6 = k2 + (this.rand.nextInt(3) - 1);
                    }
                    if (flag2) {
                        b3 = 1;
                    }
                    final int k6 = j2 - l5 * 8 - 9;
                    this.drawTexturedModalRect(k6, i6, 16 + b3 * 9, 27, 9, 9);
                    if (flag2) {
                        if (l5 * 2 + 1 < l) {
                            this.drawTexturedModalRect(k6, i6, j6 + 54, 27, 9, 9);
                        }
                        if (l5 * 2 + 1 == l) {
                            this.drawTexturedModalRect(k6, i6, j6 + 63, 27, 9, 9);
                        }
                    }
                    if (l5 * 2 + 1 < k) {
                        this.drawTexturedModalRect(k6, i6, j6 + 36, 27, 9, 9);
                    }
                    if (l5 * 2 + 1 == k) {
                        this.drawTexturedModalRect(k6, i6, j6 + 45, 27, 9, 9);
                    }
                }
            }
            else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                final int l6 = (int)Math.ceil(entitylivingbase.getHealth());
                final float f4 = entitylivingbase.getMaxHealth();
                int l7 = (int)(f4 + 0.5f) / 2;
                if (l7 > 30) {
                    l7 = 30;
                }
                int j7 = k2;
                int j8 = 0;
                while (l7 > 0) {
                    final int k7 = Math.min(l7, 10);
                    l7 -= k7;
                    for (int l8 = 0; l8 < k7; ++l8) {
                        final byte b4 = 52;
                        byte b5 = 0;
                        if (flag2) {
                            b5 = 1;
                        }
                        final int i7 = j2 - l8 * 8 - 9;
                        this.drawTexturedModalRect(i7, j7, b4 + b5 * 9, 9, 9, 9);
                        if (l8 * 2 + 1 + j8 < l6) {
                            this.drawTexturedModalRect(i7, j7, b4 + 36, 9, 9, 9);
                        }
                        if (l8 * 2 + 1 + j8 == l6) {
                            this.drawTexturedModalRect(i7, j7, b4 + 45, 9, 9, 9);
                        }
                    }
                    j7 -= 10;
                    j8 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                final int i8 = Minecraft.thePlayer.getAir();
                for (int j9 = MathHelper.ceiling_double_int((i8 - 2) * 10.0 / 300.0), k8 = MathHelper.ceiling_double_int(i8 * 10.0 / 300.0) - j9, i9 = 0; i9 < j9 + k8; ++i9) {
                    if (i9 < j9) {
                        this.drawTexturedModalRect(j2 - i9 * 8 - 9, j3, 16, 18, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(j2 - i9 * 8 - 9, j3, 25, 18, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }
    
    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            final FontRenderer fontrenderer = this.mc.fontRendererObj;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            final int i = scaledresolution.getScaledWidth();
            final short short1 = 182;
            final int j = i / 2 - short1 / 2;
            final int k = (int)(BossStatus.healthScale * (short1 + 1));
            final byte b0 = 12;
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            if (k > 0) {
                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }
            final String s = BossStatus.bossName;
            int l = 16777215;
            if (Config.isCustomColors()) {
                l = CustomColors.getBossTextColor(l);
            }
            this.getFontRenderer().drawStringWithShadow(s, (float)(i / 2 - this.getFontRenderer().getStringWidth(s) / 2), (float)(b0 - 10), l);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        }
    }
    
    private void renderPumpkinOverlay(final ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(GuiIngame.pumpkinBlurTexPath);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180476_1_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), p_180476_1_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderVignette(float p_180480_1_, final ScaledResolution p_180480_2_) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        else {
            p_180480_1_ = 1.0f - p_180480_1_;
            p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0f, 1.0f);
            final WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
            float f = (float)worldborder.getClosestDistance(Minecraft.thePlayer);
            final double d0 = Math.min(worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            final double d2 = Math.max(worldborder.getWarningDistance(), d0);
            if (f < d2) {
                f = 1.0f - (float)(f / d2);
            }
            else {
                f = 0.0f;
            }
            this.prevVignetteBrightness += (float)((p_180480_1_ - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            if (f > 0.0f) {
                GlStateManager.color(0.0f, f, f, 1.0f);
            }
            else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            this.mc.getTextureManager().bindTexture(GuiIngame.vignetteTexPath);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0, p_180480_2_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(p_180480_2_.getScaledWidth(), p_180480_2_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(p_180480_2_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }
    
    private void func_180474_b(float p_180474_1_, final ScaledResolution p_180474_2_) {
        if (p_180474_1_ < 1.0f) {
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_180474_1_);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMinV();
        final float f3 = textureatlassprite.getMaxU();
        final float f4 = textureatlassprite.getMaxV();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180474_2_.getScaledHeight(), -90.0).tex(f, f4).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), p_180474_2_.getScaledHeight(), -90.0).tex(f3, f4).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), 0.0, -90.0).tex(f3, f2).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(f, f2).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderHotbarItem(final int index, final int xPos, final int yPos, final float partialTicks, final EntityPlayer p_175184_5_) {
        final ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];
        if (itemstack != null) {
            final float f = itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                final float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
        }
    }
    
    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.field_175195_w > 0) {
            --this.field_175195_w;
            if (this.field_175195_w <= 0) {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }
        ++this.updateCounter;
        this.streamIndicator.func_152439_a();
        if (Minecraft.thePlayer != null) {
            final ItemStack itemstack = Minecraft.thePlayer.inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            }
            else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }
    
    public void setRecordPlayingMessage(final String p_73833_1_) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", p_73833_1_), true);
    }
    
    public void setRecordPlaying(final String p_110326_1_, final boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }
    
    public void displayTitle(final String p_175178_1_, final String p_175178_2_, final int p_175178_3_, final int p_175178_4_, final int p_175178_5_) {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        }
        else if (p_175178_1_ != null) {
            this.field_175201_x = p_175178_1_;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        }
        else if (p_175178_2_ != null) {
            this.field_175200_y = p_175178_2_;
        }
        else {
            if (p_175178_3_ >= 0) {
                this.field_175199_z = p_175178_3_;
            }
            if (p_175178_4_ >= 0) {
                this.field_175192_A = p_175178_4_;
            }
            if (p_175178_5_ >= 0) {
                this.field_175193_B = p_175178_5_;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }
    
    public void setRecordPlaying(final IChatComponent p_175188_1_, final boolean p_175188_2_) {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }
    
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }
    
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    public FontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }
    
    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }
    
    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }
    
    public void func_181029_i() {
        this.overlayPlayerList.func_181030_a();
    }
}
