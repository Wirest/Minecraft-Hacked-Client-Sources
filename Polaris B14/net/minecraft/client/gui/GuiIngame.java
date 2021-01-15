/*      */ package net.minecraft.client.gui;
/*      */ 
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Random;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.RenderHelper;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.entity.RenderItem;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.boss.BossStatus;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import optfine.Config;
/*      */ import rip.jutting.polaris.Polaris;
/*      */ import rip.jutting.polaris.event.events.Event2D;
/*      */ import rip.jutting.polaris.module.Module;
/*      */ import rip.jutting.polaris.ui.click.settings.Setting;
/*      */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*      */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*      */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*      */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*      */ 
/*      */ public class GuiIngame extends Gui
/*      */ {
/*   56 */   private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
/*   57 */   private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
/*   58 */   private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
/*   59 */   private final Random rand = new Random();
/*      */   
/*      */   private final Minecraft mc;
/*      */   
/*      */   private final RenderItem itemRenderer;
/*      */   
/*      */   private final GuiNewChat persistantChatGUI;
/*      */   
/*      */   private final GuiStreamIndicator streamIndicator;
/*      */   private int updateCounter;
/*   69 */   private String recordPlaying = "";
/*      */   
/*      */ 
/*      */   private int recordPlayingUpFor;
/*      */   
/*      */   private boolean recordIsPlaying;
/*      */   
/*   76 */   public float prevVignetteBrightness = 1.0F;
/*      */   
/*      */   private int remainingHighlightTicks;
/*      */   
/*      */   private ItemStack highlightingItemStack;
/*      */   
/*      */   private final GuiOverlayDebug overlayDebug;
/*      */   
/*      */   private final GuiSpectator spectatorGui;
/*      */   
/*      */   private final GuiPlayerTabOverlay overlayPlayerList;
/*      */   
/*      */   private int field_175195_w;
/*   89 */   private String field_175201_x = "";
/*   90 */   private String field_175200_y = "";
/*      */   private int field_175199_z;
/*      */   private int field_175192_A;
/*      */   private int field_175193_B;
/*   94 */   private int playerHealth = 0;
/*   95 */   private int lastPlayerHealth = 0;
/*      */   
/*      */ 
/*   98 */   private long lastSystemTime = 0L;
/*      */   
/*      */ 
/*  101 */   private long healthUpdateCounter = 0L;
/*      */   private static final String __OBFID = "CL_00000661";
/*      */   
/*      */   public GuiIngame(Minecraft mcIn) {
/*  105 */     this.mc = mcIn;
/*  106 */     this.itemRenderer = mcIn.getRenderItem();
/*  107 */     this.overlayDebug = new GuiOverlayDebug(mcIn);
/*  108 */     this.spectatorGui = new GuiSpectator(mcIn);
/*  109 */     this.persistantChatGUI = new GuiNewChat(mcIn);
/*  110 */     this.streamIndicator = new GuiStreamIndicator(mcIn);
/*  111 */     this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
/*  112 */     func_175177_a();
/*      */   }
/*      */   
/*      */   public void func_175177_a() {
/*  116 */     this.field_175199_z = 10;
/*  117 */     this.field_175192_A = 70;
/*  118 */     this.field_175193_B = 20;
/*      */   }
/*      */   
/*      */   public void renderGameOverlay(float partialTicks) {
/*  122 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/*  123 */     int i = ScaledResolution.getScaledWidth();
/*  124 */     int j = ScaledResolution.getScaledHeight();
/*  125 */     this.mc.entityRenderer.setupOverlayRendering();
/*  126 */     GlStateManager.enableBlend();
/*      */     
/*  128 */     if (Config.isVignetteEnabled()) {
/*  129 */       renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
/*      */     } else {
/*  131 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */     }
/*      */     
/*  134 */     ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
/*      */     
/*  136 */     if ((this.mc.gameSettings.thirdPersonView == 0) && (itemstack != null) && 
/*  137 */       (itemstack.getItem() == net.minecraft.item.Item.getItemFromBlock(Blocks.pumpkin))) {
/*  138 */       renderPumpkinOverlay(scaledresolution);
/*      */     }
/*      */     
/*  141 */     if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
/*  142 */       float f = this.mc.thePlayer.prevTimeInPortal + 
/*  143 */         (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
/*      */       
/*  145 */       if (f > 0.0F) {
/*  146 */         func_180474_b(f, scaledresolution);
/*      */       }
/*      */     }
/*      */     
/*  150 */     if (this.mc.playerController.isSpectator()) {
/*  151 */       this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
/*      */     } else {
/*  153 */       renderTooltip(scaledresolution, partialTicks);
/*      */     }
/*      */     
/*  156 */     Event2D event2D = new Event2D(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
/*  157 */     event2D.call();
/*      */     
/*  159 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  160 */     this.mc.getTextureManager().bindTexture(icons);
/*  161 */     GlStateManager.enableBlend();
/*      */     
/*  163 */     if (showCrosshair()) {
/*  164 */       GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
/*  165 */       GlStateManager.enableAlpha();
/*  166 */       drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
/*      */     }
/*      */     
/*  169 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  170 */     this.mc.mcProfiler.startSection("bossHealth");
/*  171 */     renderBossHealth();
/*  172 */     this.mc.mcProfiler.endSection();
/*      */     
/*  174 */     if (this.mc.playerController.shouldDrawHUD()) {
/*  175 */       renderPlayerStats(scaledresolution);
/*      */     }
/*      */     
/*  178 */     GlStateManager.disableBlend();
/*      */     
/*  180 */     if (this.mc.thePlayer.getSleepTimer() > 0) {
/*  181 */       this.mc.mcProfiler.startSection("sleep");
/*  182 */       GlStateManager.disableDepth();
/*  183 */       GlStateManager.disableAlpha();
/*  184 */       int l = this.mc.thePlayer.getSleepTimer();
/*  185 */       float f2 = l / 100.0F;
/*      */       
/*  187 */       if (f2 > 1.0F) {
/*  188 */         f2 = 1.0F - (l - 100) / 10.0F;
/*      */       }
/*      */       
/*  191 */       int k = (int)(220.0F * f2) << 24 | 0x101020;
/*  192 */       drawRect(0.0D, 0.0D, i, j, k);
/*  193 */       GlStateManager.enableAlpha();
/*  194 */       GlStateManager.enableDepth();
/*  195 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */     
/*  198 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  199 */     int i2 = i / 2 - 91;
/*      */     
/*  201 */     if (this.mc.thePlayer.isRidingHorse()) {
/*  202 */       renderHorseJumpBar(scaledresolution, i2);
/*  203 */     } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
/*  204 */       renderExpBar(scaledresolution, i2);
/*      */     }
/*      */     
/*  207 */     if ((this.mc.gameSettings.heldItemTooltips) && (!this.mc.playerController.isSpectator())) {
/*  208 */       func_181551_a(scaledresolution);
/*  209 */     } else if (this.mc.thePlayer.isSpectator()) {
/*  210 */       this.spectatorGui.func_175263_a(scaledresolution);
/*      */     }
/*      */     
/*  213 */     if (this.mc.isDemo()) {
/*  214 */       renderDemo(scaledresolution);
/*      */     }
/*      */     
/*  217 */     if (this.mc.gameSettings.showDebugInfo) {
/*  218 */       this.overlayDebug.renderDebugInfo(scaledresolution);
/*      */     }
/*      */     
/*  221 */     if (this.recordPlayingUpFor > 0) {
/*  222 */       this.mc.mcProfiler.startSection("overlayMessage");
/*  223 */       float f3 = this.recordPlayingUpFor - partialTicks;
/*  224 */       int k1 = (int)(f3 * 255.0F / 20.0F);
/*      */       
/*  226 */       if (k1 > 255) {
/*  227 */         k1 = 255;
/*      */       }
/*      */       
/*  230 */       if (k1 > 8) {
/*  231 */         GlStateManager.pushMatrix();
/*  232 */         GlStateManager.translate(i / 2, j - 68, 0.0F);
/*  233 */         GlStateManager.enableBlend();
/*  234 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  235 */         int i1 = 16777215;
/*      */         
/*  237 */         if (this.recordIsPlaying) {
/*  238 */           i1 = MathHelper.func_181758_c(f3 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
/*      */         }
/*  240 */         CFontRenderer font = FontLoaders.vardana12;
/*  241 */         font.drawString(this.recordPlaying, -getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4.0F, 
/*  242 */           i1 + (k1 << 24 & 0xFF000000));
/*  243 */         GlStateManager.disableBlend();
/*  244 */         GlStateManager.popMatrix();
/*      */       }
/*      */       
/*  247 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */     
/*  250 */     if (this.field_175195_w > 0) {
/*  251 */       this.mc.mcProfiler.startSection("titleAndSubtitle");
/*  252 */       float f4 = this.field_175195_w - partialTicks;
/*  253 */       int l1 = 255;
/*      */       
/*  255 */       if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
/*  256 */         float f1 = this.field_175199_z + this.field_175192_A + this.field_175193_B - f4;
/*  257 */         l1 = (int)(f1 * 255.0F / this.field_175199_z);
/*      */       }
/*      */       
/*  260 */       if (this.field_175195_w <= this.field_175193_B) {
/*  261 */         l1 = (int)(f4 * 255.0F / this.field_175193_B);
/*      */       }
/*      */       
/*  264 */       l1 = MathHelper.clamp_int(l1, 0, 255);
/*      */       
/*  266 */       if (l1 > 8) {
/*  267 */         GlStateManager.pushMatrix();
/*  268 */         GlStateManager.translate(i / 2, j / 2, 0.0F);
/*  269 */         GlStateManager.enableBlend();
/*  270 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  271 */         GlStateManager.pushMatrix();
/*  272 */         GlStateManager.scale(4.0F, 4.0F, 4.0F);
/*  273 */         int j2 = l1 << 24 & 0xFF000000;
/*  274 */         getFontRenderer().drawString(this.field_175201_x, 
/*  275 */           -getFontRenderer().getStringWidth(this.field_175201_x) / 2, -10.0F, 
/*  276 */           0xFFFFFF | j2, true);
/*  277 */         GlStateManager.popMatrix();
/*  278 */         GlStateManager.pushMatrix();
/*  279 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*  280 */         getFontRenderer().drawString(this.field_175200_y, 
/*  281 */           -getFontRenderer().getStringWidth(this.field_175200_y) / 2, 5.0F, 0xFFFFFF | j2, 
/*  282 */           true);
/*  283 */         GlStateManager.popMatrix();
/*  284 */         GlStateManager.disableBlend();
/*  285 */         GlStateManager.popMatrix();
/*      */       }
/*      */       
/*  288 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */     
/*  291 */     Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
/*  292 */     ScoreObjective scoreobjective = null;
/*  293 */     ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
/*      */     
/*  295 */     if (scoreplayerteam != null) {
/*  296 */       int j1 = scoreplayerteam.getChatFormat().getColorIndex();
/*      */       
/*  298 */       if (j1 >= 0) {
/*  299 */         scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j1);
/*      */       }
/*      */     }
/*      */     
/*  303 */     ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : 
/*  304 */       scoreboard.getObjectiveInDisplaySlot(1);
/*      */     
/*  306 */     if (scoreobjective1 != null) {
/*  307 */       renderScoreboard(scoreobjective1, scaledresolution);
/*      */     }
/*      */     
/*  310 */     GlStateManager.enableBlend();
/*  311 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  312 */     GlStateManager.disableAlpha();
/*  313 */     GlStateManager.pushMatrix();
/*  314 */     GlStateManager.translate(0.0F, j - 48, 0.0F);
/*  315 */     this.mc.mcProfiler.startSection("chat");
/*  316 */     this.persistantChatGUI.drawChat(this.updateCounter);
/*  317 */     this.mc.mcProfiler.endSection();
/*  318 */     GlStateManager.popMatrix();
/*  319 */     scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
/*      */     
/*  321 */     if ((!this.mc.gameSettings.keyBindPlayerList.isKeyDown()) || ((this.mc.isIntegratedServerRunning()) && 
/*  322 */       (this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1) && (scoreobjective1 == null))) {
/*  323 */       this.overlayPlayerList.updatePlayerList(false);
/*      */     } else {
/*  325 */       this.overlayPlayerList.updatePlayerList(true);
/*  326 */       this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
/*      */     }
/*      */     
/*  329 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  330 */     GlStateManager.disableLighting();
/*  331 */     GlStateManager.enableAlpha();
/*      */   }
/*      */   
/*      */   private String getDirection() {
/*  335 */     return this.mc.getRenderViewEntity().getHorizontalFacing().name();
/*      */   }
/*      */   
/*      */   protected void renderTooltip(ScaledResolution sr, float partialTicks) {
/*  339 */     if ((this.mc.getRenderViewEntity() instanceof EntityPlayer)) {
/*  340 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  341 */       this.mc.getTextureManager().bindTexture(widgetsTexPath);
/*  342 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  343 */       int i = ScaledResolution.getScaledWidth() / 2;
/*  344 */       float f = this.zLevel;
/*  345 */       this.zLevel = -90.0F;
/*  346 */       if (Polaris.instance.moduleManager.getModuleByName("Hotbar").isToggled()) {
/*  347 */         GlStateManager.enableRescaleNormal();
/*  348 */         GlStateManager.enableBlend();
/*  349 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  350 */         RenderHelper.enableGUIStandardItemLighting();
/*      */         
/*  352 */         for (int j = 0; j < 9; j++) {
/*  353 */           int k = ScaledResolution.getScaledWidth() / 2 - 90 + j * 20 + 2;
/*  354 */           int l = ScaledResolution.getScaledHeight() - 16 - 3;
/*  355 */           renderHotbarItem(j, k, l, partialTicks, entityplayer);
/*      */         }
/*      */         
/*  358 */         RenderHelper.disableStandardItemLighting();
/*  359 */         GlStateManager.disableRescaleNormal();
/*  360 */         GlStateManager.disableBlend();
/*  361 */         CFontRenderer font = FontLoaders.vardana12;
/*  362 */         RenderUtils.R2DUtils.drawRect(0.0F, ScaledResolution.getScaledHeight() - 23, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 
/*  363 */           -1879048192);
/*  364 */         RenderUtils.R2DUtils.drawRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20 + 1, 
/*  365 */           ScaledResolution.getScaledHeight() - 24, 
/*  366 */           ScaledResolution.getScaledWidth() - i - 91 - 1 + entityplayer.inventory.currentItem * 20 + 22, 
/*  367 */           ScaledResolution.getScaledHeight() - 20, 
/*  368 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  369 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  370 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  371 */           .getRGB());
/*  372 */         font.drawStringWithShadow("FPS §7: " + Minecraft.debugFPS, 
/*  373 */           ScaledResolution.getScaledWidth() - font.getStringWidth("FPS §7: " + Minecraft.debugFPS) - 2, 
/*  374 */           ScaledResolution.getScaledHeight() - 10, 
/*  375 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  376 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  377 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  378 */           .getRGB());
/*  379 */         font.drawStringWithShadow("Direction§7 : " + getDirection(), 2.0D, ScaledResolution.getScaledHeight() - 20, 
/*  380 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  381 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  382 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  383 */           .getRGB());
/*  384 */         font.drawStringWithShadow("Version §7: " + Polaris.instance.version, 
/*  385 */           ScaledResolution.getScaledWidth() - font.getStringWidth("Version §7: " + Polaris.instance.version) - 2, 
/*  386 */           ScaledResolution.getScaledHeight() - 20, 
/*  387 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  388 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  389 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  390 */           .getRGB());
/*  391 */         font.drawStringWithShadow("XYZ§7 : " + 
/*  392 */           (int)this.mc.thePlayer.posX + ", " + (int)this.mc.thePlayer.posY + ", " + (int)this.mc.thePlayer.posZ, 2.0D, 
/*  393 */           ScaledResolution.getScaledHeight() - 10, 
/*  394 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  395 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  396 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  397 */           .getRGB());
/*      */       } else {
/*  399 */         drawTexturedModalRect(i - 91, ScaledResolution.getScaledHeight() - 22, 0, 0, 182, 22);
/*  400 */         drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, 
/*  401 */           ScaledResolution.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
/*      */       }
/*  403 */       this.zLevel = f;
/*  404 */       GlStateManager.enableRescaleNormal();
/*  405 */       GlStateManager.enableBlend();
/*  406 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  407 */       RenderHelper.enableGUIStandardItemLighting();
/*      */       
/*  409 */       for (int j = 0; j < 9; j++) {
/*  410 */         int k = ScaledResolution.getScaledWidth() / 2 - 90 + j * 20 + 2;
/*  411 */         int l = ScaledResolution.getScaledHeight() - 16 - 3;
/*  412 */         renderHotbarItem(j, k, l, partialTicks, entityplayer);
/*      */       }
/*      */       
/*  415 */       RenderHelper.disableStandardItemLighting();
/*  416 */       GlStateManager.disableRescaleNormal();
/*  417 */       GlStateManager.disableBlend();
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderHorseJumpBar(ScaledResolution p_175186_1_, int p_175186_2_) {
/*  422 */     this.mc.mcProfiler.startSection("jumpBar");
/*  423 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/*  424 */     float f = this.mc.thePlayer.getHorseJumpPower();
/*  425 */     short short1 = 182;
/*  426 */     int i = (int)(f * (short1 + 1));
/*  427 */     int j = ScaledResolution.getScaledHeight() - 32 + 3;
/*  428 */     drawTexturedModalRect(p_175186_2_, j, 0, 84, short1, 5);
/*      */     
/*  430 */     if (i > 0) {
/*  431 */       drawTexturedModalRect(p_175186_2_, j, 0, 89, i, 5);
/*      */     }
/*      */     
/*  434 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   public void renderExpBar(ScaledResolution p_175176_1_, int p_175176_2_) {
/*  438 */     this.mc.mcProfiler.startSection("expBar");
/*  439 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/*  440 */     int i = this.mc.thePlayer.xpBarCap();
/*      */     
/*  442 */     if (i > 0) {
/*  443 */       short short1 = 182;
/*  444 */       int k = (int)(this.mc.thePlayer.experience * (short1 + 1));
/*  445 */       int j = ScaledResolution.getScaledHeight() - 32 + 3;
/*  446 */       drawTexturedModalRect(p_175176_2_, j, 0, 64, short1, 5);
/*      */       
/*  448 */       if (k > 0) {
/*  449 */         drawTexturedModalRect(p_175176_2_, j, 0, 69, k, 5);
/*      */       }
/*      */     }
/*      */     
/*  453 */     this.mc.mcProfiler.endSection();
/*      */     
/*  455 */     if (this.mc.thePlayer.experienceLevel > 0) {
/*  456 */       this.mc.mcProfiler.startSection("expLevel");
/*  457 */       int j1 = 8453920;
/*  458 */       String s = this.mc.thePlayer.experienceLevel;
/*  459 */       int i1 = (ScaledResolution.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
/*  460 */       int l = ScaledResolution.getScaledHeight() - 31 - 4;
/*  461 */       boolean flag = false;
/*  462 */       getFontRenderer().drawString(s, i1 + 1, l, 0);
/*  463 */       getFontRenderer().drawString(s, i1 - 1, l, 0);
/*  464 */       getFontRenderer().drawString(s, i1, l + 1, 0);
/*  465 */       getFontRenderer().drawString(s, i1, l - 1, 0);
/*  466 */       getFontRenderer().drawString(s, i1, l, j1);
/*  467 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_181551_a(ScaledResolution p_181551_1_) {
/*  472 */     this.mc.mcProfiler.startSection("selectedItemName");
/*      */     
/*  474 */     if ((this.remainingHighlightTicks > 0) && (this.highlightingItemStack != null)) {
/*  475 */       String s = this.highlightingItemStack.getDisplayName();
/*      */       
/*  477 */       if (this.highlightingItemStack.hasDisplayName()) {
/*  478 */         s = EnumChatFormatting.ITALIC + s;
/*      */       }
/*      */       
/*  481 */       int i = (ScaledResolution.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
/*  482 */       int j = ScaledResolution.getScaledHeight() - 59;
/*      */       
/*  484 */       if (!this.mc.playerController.shouldDrawHUD()) {
/*  485 */         j += 14;
/*      */       }
/*      */       
/*  488 */       int k = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);
/*      */       
/*  490 */       if (k > 255) {
/*  491 */         k = 255;
/*      */       }
/*      */       
/*  494 */       if (k > 0) {
/*  495 */         GlStateManager.pushMatrix();
/*  496 */         GlStateManager.enableBlend();
/*  497 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  498 */         getFontRenderer().drawStringWithShadow(s, i, j, 16777215 + (k << 24));
/*  499 */         GlStateManager.disableBlend();
/*  500 */         GlStateManager.popMatrix();
/*      */       }
/*      */     }
/*      */     
/*  504 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   public void renderDemo(ScaledResolution p_175185_1_) {
/*  508 */     this.mc.mcProfiler.startSection("demo");
/*  509 */     String s = "";
/*      */     
/*  511 */     if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
/*  512 */       s = I18n.format("demo.demoExpired", new Object[0]);
/*      */     } else {
/*  514 */       s = I18n.format("demo.remainingTime", new Object[] {
/*  515 */         net.minecraft.util.StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())) });
/*      */     }
/*      */     
/*  518 */     int i = getFontRenderer().getStringWidth(s);
/*  519 */     getFontRenderer().drawStringWithShadow(s, ScaledResolution.getScaledWidth() - i - 10, 5.0F, 16777215);
/*  520 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   protected boolean showCrosshair() {
/*  524 */     if ((this.mc.gameSettings.showDebugInfo) && (!this.mc.thePlayer.hasReducedDebug()) && 
/*  525 */       (!this.mc.gameSettings.reducedDebugInfo))
/*  526 */       return false;
/*  527 */     if (this.mc.playerController.isSpectator()) {
/*  528 */       if (this.mc.pointedEntity != null) {
/*  529 */         return true;
/*      */       }
/*  531 */       if ((this.mc.objectMouseOver != null) && 
/*  532 */         (this.mc.objectMouseOver.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK)) {
/*  533 */         net.minecraft.util.BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/*      */         
/*  535 */         if ((this.mc.theWorld.getTileEntity(blockpos) instanceof net.minecraft.inventory.IInventory)) {
/*  536 */           return true;
/*      */         }
/*      */       }
/*      */       
/*  540 */       return false;
/*      */     }
/*      */     
/*  543 */     return true;
/*      */   }
/*      */   
/*      */   public void renderStreamIndicator(ScaledResolution p_180478_1_)
/*      */   {
/*  548 */     this.streamIndicator.render(ScaledResolution.getScaledWidth() - 10, 10);
/*      */   }
/*      */   
/*      */   private void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
/*  552 */     if (Polaris.instance.settingsManager.getSettingByName("Disable").getValBoolean()) {
/*  553 */       return;
/*      */     }
/*      */     
/*  556 */     CFontRenderer font = FontLoaders.vardana12;
/*  557 */     Scoreboard scoreboard = p_180475_1_.getScoreboard();
/*  558 */     Collection collection = scoreboard.getSortedScores(p_180475_1_);
/*  559 */     ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new com.google.common.base.Predicate() {
/*      */       private static final String __OBFID = "CL_00001958";
/*      */       
/*      */       public boolean apply(Score p_apply_1_) {
/*  563 */         return (p_apply_1_.getPlayerName() != null) && (!p_apply_1_.getPlayerName().startsWith("#"));
/*      */       }
/*      */       
/*      */       public boolean apply(Object p_apply_1_) {
/*  567 */         return apply((Score)p_apply_1_);
/*      */       }
/*      */     }));
/*      */     ArrayList arraylist1;
/*      */     ArrayList arraylist1;
/*  572 */     if (arraylist.size() > 15) {
/*  573 */       arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
/*      */     } else {
/*  575 */       arraylist1 = arraylist;
/*      */     }
/*      */     
/*  578 */     int i = getFontRenderer().getStringWidth(p_180475_1_.getDisplayName());
/*      */     
/*  580 */     for (Object score0 : arraylist1) {
/*  581 */       Score score = (Score)score0;
/*  582 */       ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
/*  583 */       String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + 
/*  584 */         EnumChatFormatting.RED + score.getScorePoints();
/*  585 */       if (Polaris.instance.settingsManager.getSettingByName("SB Font").getValBoolean()) {
/*  586 */         i = Math.max(i, font.getStringWidth(s));
/*      */       } else {
/*  588 */         i = Math.max(i, getFontRenderer().getStringWidth(s));
/*      */       }
/*      */     }
/*      */     
/*  592 */     int j1 = arraylist1.size() * (Polaris.instance.settingsManager.getSettingByName("SB Font").getValBoolean() ? font.getHeight() : getFontRenderer().FONT_HEIGHT);
/*  593 */     int k1 = ScaledResolution.getScaledHeight() / 2 + j1 / 3;
/*  594 */     byte b0 = 3;
/*  595 */     int j = ScaledResolution.getScaledWidth() - i - b0;
/*  596 */     int k = 0;
/*      */     
/*  598 */     for (Object score10 : arraylist1) {
/*  599 */       Score score1 = (Score)score10;
/*  600 */       k++;
/*  601 */       ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
/*  602 */       String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
/*  603 */       String s2 = EnumChatFormatting.RED + score1.getScorePoints();
/*  604 */       int l = k1 - k * getFontRenderer().FONT_HEIGHT + 
/*  605 */         (int)Polaris.instance.settingsManager.getSettingByName("Height").getValDouble();
/*  606 */       int i1 = ScaledResolution.getScaledWidth() - b0 + 2;
/*  607 */       drawRect(j - 2, l, i1, l + getFontRenderer().FONT_HEIGHT, 1342177280);
/*  608 */       if (Polaris.instance.settingsManager.getSettingByName("SB Font").getValBoolean()) {
/*  609 */         font.drawStringWithShadow(s1, j, l + 2, 553648127);
/*  610 */         if (Polaris.instance.settingsManager.getSettingByName("Numbers").getValBoolean()) {
/*  611 */           font.drawStringWithShadow(s2, i1 - font.getStringWidth(s2), l, 553648127);
/*      */         }
/*      */         
/*  614 */         if (k == arraylist1.size()) {
/*  615 */           String s3 = p_180475_1_.getDisplayName();
/*  616 */           drawRect(j - 2, l - getFontRenderer().FONT_HEIGHT, i1, l - 1, 1610612736);
/*  617 */           drawRect(j - 2, l - 1, i1, l, 1342177280);
/*  618 */           font.drawStringWithShadow(s3, j + i / 2 - font.getStringWidth(s3) / 2, l - font.getHeight(), 
/*  619 */             553648127);
/*      */         }
/*      */       } else {
/*  622 */         this.mc.fontRendererObj.drawStringWithShadow(s1, j, l + 1, 553648127);
/*  623 */         if (Polaris.instance.settingsManager.getSettingByName("Numbers").getValBoolean()) {
/*  624 */           this.mc.fontRendererObj.drawStringWithShadow(s2, i1 - this.mc.fontRendererObj.getStringWidth(s2), l, 553648127);
/*      */         }
/*      */         
/*  627 */         if (k == arraylist1.size()) {
/*  628 */           String s3 = p_180475_1_.getDisplayName();
/*  629 */           drawRect(j - 2, l - getFontRenderer().FONT_HEIGHT, i1, l - 1, 1610612736);
/*  630 */           drawRect(j - 2, l - 1, i1, l, 1342177280);
/*  631 */           this.mc.fontRendererObj.drawStringWithShadow(s3, j + i / 2 - this.mc.fontRendererObj.getStringWidth(s3) / 2, l + 1 - this.mc.fontRendererObj.getHeight(), 
/*  632 */             553648127);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderPlayerStats(ScaledResolution p_180477_1_) {
/*  639 */     if ((this.mc.getRenderViewEntity() instanceof EntityPlayer)) {
/*  640 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  641 */       int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
/*  642 */       boolean flag = (this.healthUpdateCounter > this.updateCounter) && 
/*  643 */         ((this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L);
/*      */       
/*  645 */       if ((i < this.playerHealth) && (entityplayer.hurtResistantTime > 0)) {
/*  646 */         this.lastSystemTime = Minecraft.getSystemTime();
/*  647 */         this.healthUpdateCounter = (this.updateCounter + 20);
/*  648 */       } else if ((i > this.playerHealth) && (entityplayer.hurtResistantTime > 0)) {
/*  649 */         this.lastSystemTime = Minecraft.getSystemTime();
/*  650 */         this.healthUpdateCounter = (this.updateCounter + 10);
/*      */       }
/*      */       
/*  653 */       if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
/*  654 */         this.playerHealth = i;
/*  655 */         this.lastPlayerHealth = i;
/*  656 */         this.lastSystemTime = Minecraft.getSystemTime();
/*      */       }
/*      */       
/*  659 */       this.playerHealth = i;
/*  660 */       int j = this.lastPlayerHealth;
/*  661 */       this.rand.setSeed(this.updateCounter * 312871);
/*  662 */       boolean flag1 = false;
/*  663 */       FoodStats foodstats = entityplayer.getFoodStats();
/*  664 */       int k = foodstats.getFoodLevel();
/*  665 */       int l = foodstats.getPrevFoodLevel();
/*  666 */       IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth);
/*  667 */       int i1 = ScaledResolution.getScaledWidth() / 2 - 91;
/*  668 */       int j1 = ScaledResolution.getScaledWidth() / 2 + 91;
/*  669 */       int k1 = ScaledResolution.getScaledHeight() - 39;
/*  670 */       float f = (float)iattributeinstance.getAttributeValue();
/*  671 */       float f1 = entityplayer.getAbsorptionAmount();
/*  672 */       int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
/*  673 */       int i2 = Math.max(10 - (l1 - 2), 3);
/*  674 */       int j2 = k1 - (l1 - 1) * i2 - 10;
/*  675 */       float f2 = f1;
/*  676 */       int k2 = entityplayer.getTotalArmorValue();
/*  677 */       int l2 = -1;
/*      */       
/*  679 */       if (entityplayer.isPotionActive(Potion.regeneration)) {
/*  680 */         l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
/*      */       }
/*      */       
/*  683 */       this.mc.mcProfiler.startSection("armor");
/*      */       
/*  685 */       for (int i3 = 0; i3 < 10; i3++) {
/*  686 */         if (k2 > 0) {
/*  687 */           int j3 = i1 + i3 * 8;
/*      */           
/*  689 */           if (i3 * 2 + 1 < k2) {
/*  690 */             drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
/*      */           }
/*      */           
/*  693 */           if (i3 * 2 + 1 == k2) {
/*  694 */             drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
/*      */           }
/*      */           
/*  697 */           if (i3 * 2 + 1 > k2) {
/*  698 */             drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  703 */       this.mc.mcProfiler.endStartSection("health");
/*      */       
/*  705 */       for (int j5 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; j5 >= 0; j5--) {
/*  706 */         int k5 = 16;
/*      */         
/*  708 */         if (entityplayer.isPotionActive(Potion.poison)) {
/*  709 */           k5 += 36;
/*  710 */         } else if (entityplayer.isPotionActive(Potion.wither)) {
/*  711 */           k5 += 72;
/*      */         }
/*      */         
/*  714 */         byte b0 = 0;
/*      */         
/*  716 */         if (flag) {
/*  717 */           b0 = 1;
/*      */         }
/*      */         
/*  720 */         int k3 = MathHelper.ceiling_float_int((j5 + 1) / 10.0F) - 1;
/*  721 */         int l3 = i1 + j5 % 10 * 8;
/*  722 */         int i4 = k1 - k3 * i2;
/*      */         
/*  724 */         if (i <= 4) {
/*  725 */           i4 += this.rand.nextInt(2);
/*      */         }
/*      */         
/*  728 */         if (j5 == l2) {
/*  729 */           i4 -= 2;
/*      */         }
/*      */         
/*  732 */         byte b1 = 0;
/*      */         
/*  734 */         if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
/*  735 */           b1 = 5;
/*      */         }
/*      */         
/*  738 */         drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);
/*      */         
/*  740 */         if (flag) {
/*  741 */           if (j5 * 2 + 1 < j) {
/*  742 */             drawTexturedModalRect(l3, i4, k5 + 54, 9 * b1, 9, 9);
/*      */           }
/*      */           
/*  745 */           if (j5 * 2 + 1 == j) {
/*  746 */             drawTexturedModalRect(l3, i4, k5 + 63, 9 * b1, 9, 9);
/*      */           }
/*      */         }
/*      */         
/*  750 */         if (f2 <= 0.0F) {
/*  751 */           if (j5 * 2 + 1 < i) {
/*  752 */             drawTexturedModalRect(l3, i4, k5 + 36, 9 * b1, 9, 9);
/*      */           }
/*      */           
/*  755 */           if (j5 * 2 + 1 == i) {
/*  756 */             drawTexturedModalRect(l3, i4, k5 + 45, 9 * b1, 9, 9);
/*      */           }
/*      */         } else {
/*  759 */           if ((f2 == f1) && (f1 % 2.0F == 1.0F)) {
/*  760 */             drawTexturedModalRect(l3, i4, k5 + 153, 9 * b1, 9, 9);
/*      */           } else {
/*  762 */             drawTexturedModalRect(l3, i4, k5 + 144, 9 * b1, 9, 9);
/*      */           }
/*      */           
/*  765 */           f2 -= 2.0F;
/*      */         }
/*      */       }
/*      */       
/*  769 */       Entity entity = entityplayer.ridingEntity;
/*      */       
/*  771 */       if (entity == null) {
/*  772 */         this.mc.mcProfiler.endStartSection("food");
/*      */         
/*  774 */         for (int l5 = 0; l5 < 10; l5++) {
/*  775 */           int i8 = k1;
/*  776 */           int j6 = 16;
/*  777 */           byte b4 = 0;
/*      */           
/*  779 */           if (entityplayer.isPotionActive(Potion.hunger)) {
/*  780 */             j6 += 36;
/*  781 */             b4 = 13;
/*      */           }
/*      */           
/*  784 */           if ((entityplayer.getFoodStats().getSaturationLevel() <= 0.0F) && 
/*  785 */             (this.updateCounter % (k * 3 + 1) == 0)) {
/*  786 */             i8 = k1 + (this.rand.nextInt(3) - 1);
/*      */           }
/*      */           
/*  789 */           if (flag1) {
/*  790 */             b4 = 1;
/*      */           }
/*      */           
/*  793 */           int k7 = j1 - l5 * 8 - 9;
/*  794 */           drawTexturedModalRect(k7, i8, 16 + b4 * 9, 27, 9, 9);
/*      */           
/*  796 */           if (flag1) {
/*  797 */             if (l5 * 2 + 1 < l) {
/*  798 */               drawTexturedModalRect(k7, i8, j6 + 54, 27, 9, 9);
/*      */             }
/*      */             
/*  801 */             if (l5 * 2 + 1 == l) {
/*  802 */               drawTexturedModalRect(k7, i8, j6 + 63, 27, 9, 9);
/*      */             }
/*      */           }
/*      */           
/*  806 */           if (l5 * 2 + 1 < k) {
/*  807 */             drawTexturedModalRect(k7, i8, j6 + 36, 27, 9, 9);
/*      */           }
/*      */           
/*  810 */           if (l5 * 2 + 1 == k) {
/*  811 */             drawTexturedModalRect(k7, i8, j6 + 45, 27, 9, 9);
/*      */           }
/*      */         }
/*  814 */       } else if ((entity instanceof EntityLivingBase)) {
/*  815 */         this.mc.mcProfiler.endStartSection("mountHealth");
/*  816 */         EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
/*  817 */         int l7 = (int)Math.ceil(entitylivingbase.getHealth());
/*  818 */         float f3 = entitylivingbase.getMaxHealth();
/*  819 */         int l6 = (int)(f3 + 0.5F) / 2;
/*      */         
/*  821 */         if (l6 > 30) {
/*  822 */           l6 = 30;
/*      */         }
/*      */         
/*  825 */         int j7 = k1;
/*      */         
/*  827 */         for (int j4 = 0; l6 > 0; j4 += 20) {
/*  828 */           int k4 = Math.min(l6, 10);
/*  829 */           l6 -= k4;
/*      */           
/*  831 */           for (int l4 = 0; l4 < k4; l4++) {
/*  832 */             byte b2 = 52;
/*  833 */             byte b3 = 0;
/*      */             
/*  835 */             if (flag1) {
/*  836 */               b3 = 1;
/*      */             }
/*      */             
/*  839 */             int i5 = j1 - l4 * 8 - 9;
/*  840 */             drawTexturedModalRect(i5, j7, b2 + b3 * 9, 9, 9, 9);
/*      */             
/*  842 */             if (l4 * 2 + 1 + j4 < l7) {
/*  843 */               drawTexturedModalRect(i5, j7, b2 + 36, 9, 9, 9);
/*      */             }
/*      */             
/*  846 */             if (l4 * 2 + 1 + j4 == l7) {
/*  847 */               drawTexturedModalRect(i5, j7, b2 + 45, 9, 9, 9);
/*      */             }
/*      */           }
/*      */           
/*  851 */           j7 -= 10;
/*      */         }
/*      */       }
/*      */       
/*  855 */       this.mc.mcProfiler.endStartSection("air");
/*      */       
/*  857 */       if (entityplayer.isInsideOfMaterial(net.minecraft.block.material.Material.water)) {
/*  858 */         int i6 = this.mc.thePlayer.getAir();
/*  859 */         int j8 = MathHelper.ceiling_double_int((i6 - 2) * 10.0D / 300.0D);
/*  860 */         int k6 = MathHelper.ceiling_double_int(i6 * 10.0D / 300.0D) - j8;
/*      */         
/*  862 */         for (int i7 = 0; i7 < j8 + k6; i7++) {
/*  863 */           if (i7 < j8) {
/*  864 */             drawTexturedModalRect(j1 - i7 * 8 - 9, j2, 16, 18, 9, 9);
/*      */           } else {
/*  866 */             drawTexturedModalRect(j1 - i7 * 8 - 9, j2, 25, 18, 9, 9);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  871 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void renderBossHealth()
/*      */   {
/*  879 */     if ((BossStatus.bossName != null) && (BossStatus.statusBarTime > 0)) {
/*  880 */       BossStatus.statusBarTime -= 1;
/*  881 */       FontRenderer fontrenderer = this.mc.fontRendererObj;
/*  882 */       ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, 
/*  883 */         Minecraft.getMinecraft().displayHeight);
/*  884 */       int i = ScaledResolution.getScaledWidth();
/*  885 */       short short1 = 182;
/*  886 */       int j = i / 2 - short1 / 2;
/*  887 */       int k = (int)(BossStatus.healthScale * (short1 + 1));
/*  888 */       byte b0 = 12;
/*  889 */       drawTexturedModalRect(j, b0, 0, 74, short1, 5);
/*  890 */       drawTexturedModalRect(j, b0, 0, 74, short1, 5);
/*      */       
/*  892 */       if (k > 0) {
/*  893 */         drawTexturedModalRect(j, b0, 0, 79, k, 5);
/*      */       }
/*      */       
/*  896 */       String s = BossStatus.bossName;
/*  897 */       getFontRenderer().drawStringWithShadow(s, 
/*  898 */         i / 2 - getFontRenderer().getStringWidth(s) / 2, b0 - 10, 16777215);
/*  899 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  900 */       this.mc.getTextureManager().bindTexture(icons);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderPumpkinOverlay(ScaledResolution p_180476_1_) {
/*  905 */     GlStateManager.disableDepth();
/*  906 */     GlStateManager.depthMask(false);
/*  907 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  908 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  909 */     GlStateManager.disableAlpha();
/*  910 */     this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
/*  911 */     Tessellator tessellator = Tessellator.getInstance();
/*  912 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  913 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  914 */     worldrenderer.pos(0.0D, ScaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
/*  915 */     worldrenderer.pos(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0D)
/*  916 */       .tex(1.0D, 1.0D).endVertex();
/*  917 */     worldrenderer.pos(ScaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
/*  918 */     worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
/*  919 */     tessellator.draw();
/*  920 */     GlStateManager.depthMask(true);
/*  921 */     GlStateManager.enableDepth();
/*  922 */     GlStateManager.enableAlpha();
/*  923 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void renderVignette(float p_180480_1_, ScaledResolution p_180480_2_)
/*      */   {
/*  930 */     if (Config.isVignetteEnabled()) {
/*  931 */       p_180480_1_ = 1.0F - p_180480_1_;
/*  932 */       p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0F, 1.0F);
/*  933 */       WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
/*  934 */       float f = (float)worldborder.getClosestDistance(this.mc.thePlayer);
/*  935 */       double d0 = Math.min(worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0D, 
/*  936 */         Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
/*  937 */       double d1 = Math.max(worldborder.getWarningDistance(), d0);
/*      */       
/*  939 */       if (f < d1) {
/*  940 */         f = 1.0F - (float)(f / d1);
/*      */       } else {
/*  942 */         f = 0.0F;
/*      */       }
/*      */       
/*  945 */       this.prevVignetteBrightness = 
/*  946 */         ((float)(this.prevVignetteBrightness + (p_180480_1_ - this.prevVignetteBrightness) * 0.01D));
/*  947 */       GlStateManager.disableDepth();
/*  948 */       GlStateManager.depthMask(false);
/*  949 */       GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
/*      */       
/*  951 */       if (f > 0.0F) {
/*  952 */         GlStateManager.color(0.0F, f, f, 1.0F);
/*      */       } else {
/*  954 */         GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, 
/*  955 */           this.prevVignetteBrightness, 1.0F);
/*      */       }
/*      */       
/*  958 */       this.mc.getTextureManager().bindTexture(vignetteTexPath);
/*  959 */       Tessellator tessellator = Tessellator.getInstance();
/*  960 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  961 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  962 */       worldrenderer.pos(0.0D, ScaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
/*  963 */       worldrenderer.pos(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0D)
/*  964 */         .tex(1.0D, 1.0D).endVertex();
/*  965 */       worldrenderer.pos(ScaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
/*  966 */       worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
/*  967 */       tessellator.draw();
/*  968 */       GlStateManager.depthMask(true);
/*  969 */       GlStateManager.enableDepth();
/*  970 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  971 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private void func_180474_b(float p_180474_1_, ScaledResolution p_180474_2_) {
/*  976 */     if (p_180474_1_ < 1.0F) {
/*  977 */       p_180474_1_ *= p_180474_1_;
/*  978 */       p_180474_1_ *= p_180474_1_;
/*  979 */       p_180474_1_ = p_180474_1_ * 0.8F + 0.2F;
/*      */     }
/*      */     
/*  982 */     GlStateManager.disableAlpha();
/*  983 */     GlStateManager.disableDepth();
/*  984 */     GlStateManager.depthMask(false);
/*  985 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  986 */     GlStateManager.color(1.0F, 1.0F, 1.0F, p_180474_1_);
/*  987 */     this.mc.getTextureManager().bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture);
/*  988 */     TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()
/*  989 */       .getTexture(Blocks.portal.getDefaultState());
/*  990 */     float f = textureatlassprite.getMinU();
/*  991 */     float f1 = textureatlassprite.getMinV();
/*  992 */     float f2 = textureatlassprite.getMaxU();
/*  993 */     float f3 = textureatlassprite.getMaxV();
/*  994 */     Tessellator tessellator = Tessellator.getInstance();
/*  995 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  996 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  997 */     worldrenderer.pos(0.0D, ScaledResolution.getScaledHeight(), -90.0D).tex(f, f3)
/*  998 */       .endVertex();
/*  999 */     worldrenderer.pos(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0D)
/* 1000 */       .tex(f2, f3).endVertex();
/* 1001 */     worldrenderer.pos(ScaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(f2, f1)
/* 1002 */       .endVertex();
/* 1003 */     worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
/* 1004 */     tessellator.draw();
/* 1005 */     GlStateManager.depthMask(true);
/* 1006 */     GlStateManager.enableDepth();
/* 1007 */     GlStateManager.enableAlpha();
/* 1008 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {
/* 1012 */     ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];
/*      */     
/* 1014 */     if (itemstack != null) {
/* 1015 */       float f = itemstack.animationsToGo - partialTicks;
/*      */       
/* 1017 */       if (f > 0.0F) {
/* 1018 */         GlStateManager.pushMatrix();
/* 1019 */         float f1 = 1.0F + f / 5.0F;
/* 1020 */         GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
/* 1021 */         GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
/* 1022 */         GlStateManager.translate(-(xPos + 8), -(yPos + 12), 0.0F);
/*      */       }
/*      */       
/* 1025 */       this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
/*      */       
/* 1027 */       if (f > 0.0F) {
/* 1028 */         GlStateManager.popMatrix();
/*      */       }
/*      */       
/* 1031 */       this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateTick()
/*      */   {
/* 1039 */     if (this.recordPlayingUpFor > 0) {
/* 1040 */       this.recordPlayingUpFor -= 1;
/*      */     }
/*      */     
/* 1043 */     if (this.field_175195_w > 0) {
/* 1044 */       this.field_175195_w -= 1;
/*      */       
/* 1046 */       if (this.field_175195_w <= 0) {
/* 1047 */         this.field_175201_x = "";
/* 1048 */         this.field_175200_y = "";
/*      */       }
/*      */     }
/*      */     
/* 1052 */     this.updateCounter += 1;
/* 1053 */     this.streamIndicator.func_152439_a();
/*      */     
/* 1055 */     if (this.mc.thePlayer != null) {
/* 1056 */       ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
/*      */       
/* 1058 */       if (itemstack == null) {
/* 1059 */         this.remainingHighlightTicks = 0;
/* 1060 */       } else if ((this.highlightingItemStack != null) && (itemstack.getItem() == this.highlightingItemStack.getItem()) && 
/* 1061 */         (ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack)) && (
/* 1062 */         (itemstack.isItemStackDamageable()) || 
/* 1063 */         (itemstack.getMetadata() == this.highlightingItemStack.getMetadata()))) {
/* 1064 */         if (this.remainingHighlightTicks > 0) {
/* 1065 */           this.remainingHighlightTicks -= 1;
/*      */         }
/*      */       } else {
/* 1068 */         this.remainingHighlightTicks = 40;
/*      */       }
/*      */       
/* 1071 */       this.highlightingItemStack = itemstack;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setRecordPlayingMessage(String p_73833_1_) {
/* 1076 */     setRecordPlaying(I18n.format("record.nowPlaying", new Object[] { p_73833_1_ }), true);
/*      */   }
/*      */   
/*      */   public void setRecordPlaying(String p_110326_1_, boolean p_110326_2_) {
/* 1080 */     this.recordPlaying = p_110326_1_;
/* 1081 */     this.recordPlayingUpFor = 60;
/* 1082 */     this.recordIsPlaying = p_110326_2_;
/*      */   }
/*      */   
/*      */   public void displayTitle(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_)
/*      */   {
/* 1087 */     if ((p_175178_1_ == null) && (p_175178_2_ == null) && (p_175178_3_ < 0) && (p_175178_4_ < 0) && (p_175178_5_ < 0)) {
/* 1088 */       this.field_175201_x = "";
/* 1089 */       this.field_175200_y = "";
/* 1090 */       this.field_175195_w = 0;
/* 1091 */     } else if (p_175178_1_ != null) {
/* 1092 */       this.field_175201_x = p_175178_1_;
/* 1093 */       this.field_175195_w = (this.field_175199_z + this.field_175192_A + this.field_175193_B);
/* 1094 */     } else if (p_175178_2_ != null) {
/* 1095 */       this.field_175200_y = p_175178_2_;
/*      */     } else {
/* 1097 */       if (p_175178_3_ >= 0) {
/* 1098 */         this.field_175199_z = p_175178_3_;
/*      */       }
/*      */       
/* 1101 */       if (p_175178_4_ >= 0) {
/* 1102 */         this.field_175192_A = p_175178_4_;
/*      */       }
/*      */       
/* 1105 */       if (p_175178_5_ >= 0) {
/* 1106 */         this.field_175193_B = p_175178_5_;
/*      */       }
/*      */       
/* 1109 */       if (this.field_175195_w > 0) {
/* 1110 */         this.field_175195_w = (this.field_175199_z + this.field_175192_A + this.field_175193_B);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setRecordPlaying(IChatComponent p_175188_1_, boolean p_175188_2_) {
/* 1116 */     setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public GuiNewChat getChatGUI()
/*      */   {
/* 1124 */     return this.persistantChatGUI;
/*      */   }
/*      */   
/*      */   public int getUpdateCounter() {
/* 1128 */     return this.updateCounter;
/*      */   }
/*      */   
/*      */   public FontRenderer getFontRenderer() {
/* 1132 */     return this.mc.fontRendererObj;
/*      */   }
/*      */   
/*      */   public GuiSpectator getSpectatorGui() {
/* 1136 */     return this.spectatorGui;
/*      */   }
/*      */   
/*      */   public GuiPlayerTabOverlay getTabList() {
/* 1140 */     return this.overlayPlayerList;
/*      */   }
/*      */   
/*      */   public void func_181029_i() {
/* 1144 */     this.overlayPlayerList.func_181030_a();
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiIngame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */