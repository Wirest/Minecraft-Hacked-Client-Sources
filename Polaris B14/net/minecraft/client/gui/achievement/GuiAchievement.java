/*     */ package net.minecraft.client.gui.achievement;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class GuiAchievement extends net.minecraft.client.gui.Gui
/*     */ {
/*  15 */   private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
/*     */   private Minecraft mc;
/*     */   private int width;
/*     */   private int height;
/*     */   private String achievementTitle;
/*     */   private String achievementDescription;
/*     */   private Achievement theAchievement;
/*     */   private long notificationTime;
/*     */   private RenderItem renderItem;
/*     */   private boolean permanentNotification;
/*     */   
/*     */   public GuiAchievement(Minecraft mc)
/*     */   {
/*  28 */     this.mc = mc;
/*  29 */     this.renderItem = mc.getRenderItem();
/*     */   }
/*     */   
/*     */   public void displayAchievement(Achievement ach)
/*     */   {
/*  34 */     this.achievementTitle = net.minecraft.client.resources.I18n.format("achievement.get", new Object[0]);
/*  35 */     this.achievementDescription = ach.getStatName().getUnformattedText();
/*  36 */     this.notificationTime = Minecraft.getSystemTime();
/*  37 */     this.theAchievement = ach;
/*  38 */     this.permanentNotification = false;
/*     */   }
/*     */   
/*     */   public void displayUnformattedAchievement(Achievement achievementIn)
/*     */   {
/*  43 */     this.achievementTitle = achievementIn.getStatName().getUnformattedText();
/*  44 */     this.achievementDescription = achievementIn.getDescription();
/*  45 */     this.notificationTime = (Minecraft.getSystemTime() + 2500L);
/*  46 */     this.theAchievement = achievementIn;
/*  47 */     this.permanentNotification = true;
/*     */   }
/*     */   
/*     */   private void updateAchievementWindowScale()
/*     */   {
/*  52 */     GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/*  53 */     GlStateManager.matrixMode(5889);
/*  54 */     GlStateManager.loadIdentity();
/*  55 */     GlStateManager.matrixMode(5888);
/*  56 */     GlStateManager.loadIdentity();
/*  57 */     this.width = this.mc.displayWidth;
/*  58 */     this.height = this.mc.displayHeight;
/*  59 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  60 */     this.width = ScaledResolution.getScaledWidth();
/*  61 */     this.height = ScaledResolution.getScaledHeight();
/*  62 */     GlStateManager.clear(256);
/*  63 */     GlStateManager.matrixMode(5889);
/*  64 */     GlStateManager.loadIdentity();
/*  65 */     GlStateManager.ortho(0.0D, this.width, this.height, 0.0D, 1000.0D, 3000.0D);
/*  66 */     GlStateManager.matrixMode(5888);
/*  67 */     GlStateManager.loadIdentity();
/*  68 */     GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/*     */   }
/*     */   
/*     */   public void updateAchievementWindow()
/*     */   {
/*  73 */     if ((this.theAchievement != null) && (this.notificationTime != 0L) && (Minecraft.getMinecraft().thePlayer != null))
/*     */     {
/*  75 */       double d0 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;
/*     */       
/*  77 */       if (!this.permanentNotification)
/*     */       {
/*  79 */         if ((d0 < 0.0D) || (d0 > 1.0D))
/*     */         {
/*  81 */           this.notificationTime = 0L;
/*     */         }
/*     */         
/*     */       }
/*  85 */       else if (d0 > 0.5D)
/*     */       {
/*  87 */         d0 = 0.5D;
/*     */       }
/*     */       
/*  90 */       updateAchievementWindowScale();
/*  91 */       GlStateManager.disableDepth();
/*  92 */       GlStateManager.depthMask(false);
/*  93 */       double d1 = d0 * 2.0D;
/*     */       
/*  95 */       if (d1 > 1.0D)
/*     */       {
/*  97 */         d1 = 2.0D - d1;
/*     */       }
/*     */       
/* 100 */       d1 *= 4.0D;
/* 101 */       d1 = 1.0D - d1;
/*     */       
/* 103 */       if (d1 < 0.0D)
/*     */       {
/* 105 */         d1 = 0.0D;
/*     */       }
/*     */       
/* 108 */       d1 *= d1;
/* 109 */       d1 *= d1;
/* 110 */       int i = this.width - 160;
/* 111 */       int j = 0 - (int)(d1 * 36.0D);
/* 112 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 113 */       GlStateManager.enableTexture2D();
/* 114 */       this.mc.getTextureManager().bindTexture(achievementBg);
/* 115 */       GlStateManager.disableLighting();
/* 116 */       drawTexturedModalRect(i, j, 96, 202, 160, 32);
/*     */       
/* 118 */       if (this.permanentNotification)
/*     */       {
/* 120 */         this.mc.fontRendererObj.drawSplitString(this.achievementDescription, i + 30, j + 7, 120, -1);
/*     */       }
/*     */       else
/*     */       {
/* 124 */         this.mc.fontRendererObj.drawString(this.achievementTitle, i + 30, j + 7, 65280);
/* 125 */         this.mc.fontRendererObj.drawString(this.achievementDescription, i + 30, j + 18, -1);
/*     */       }
/*     */       
/* 128 */       RenderHelper.enableGUIStandardItemLighting();
/* 129 */       GlStateManager.disableLighting();
/* 130 */       GlStateManager.enableRescaleNormal();
/* 131 */       GlStateManager.enableColorMaterial();
/* 132 */       GlStateManager.enableLighting();
/* 133 */       this.renderItem.renderItemAndEffectIntoGUI(this.theAchievement.theItemStack, i + 8, j + 8);
/* 134 */       GlStateManager.disableLighting();
/* 135 */       GlStateManager.depthMask(true);
/* 136 */       GlStateManager.enableDepth();
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearAchievements()
/*     */   {
/* 142 */     this.theAchievement = null;
/* 143 */     this.notificationTime = 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\achievement\GuiAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */