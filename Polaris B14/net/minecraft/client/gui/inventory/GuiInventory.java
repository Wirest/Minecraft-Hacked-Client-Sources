/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.achievement.GuiAchievements;
/*     */ import net.minecraft.client.gui.achievement.GuiStats;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.InventoryEffectRenderer;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ 
/*     */ public class GuiInventory extends InventoryEffectRenderer
/*     */ {
/*     */   private float oldMouseX;
/*     */   private float oldMouseY;
/*     */   
/*     */   public GuiInventory(EntityPlayer p_i1094_1_)
/*     */   {
/*  27 */     super(p_i1094_1_.inventoryContainer);
/*  28 */     this.allowUserInput = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  36 */     if (this.mc.playerController.isInCreativeMode())
/*     */     {
/*  38 */       this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
/*     */     }
/*     */     
/*  41 */     updateActivePotionEffects();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  50 */     this.buttonList.clear();
/*     */     
/*  52 */     if (this.mc.playerController.isInCreativeMode())
/*     */     {
/*  54 */       this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
/*     */     }
/*     */     else
/*     */     {
/*  58 */       super.initGui();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*     */   {
/*  67 */     this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 86.0D, 16.0D, 4210752);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  75 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  76 */     this.oldMouseX = mouseX;
/*  77 */     this.oldMouseY = mouseY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*     */   {
/*  85 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  86 */     this.mc.getTextureManager().bindTexture(inventoryBackground);
/*  87 */     int i = this.guiLeft;
/*  88 */     int j = this.guiTop;
/*  89 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*  90 */     drawEntityOnScreen(i + 51, j + 75, 30, i + 51 - this.oldMouseX, j + 75 - 50 - this.oldMouseY, this.mc.thePlayer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
/*     */   {
/*  98 */     GlStateManager.enableColorMaterial();
/*  99 */     GlStateManager.pushMatrix();
/* 100 */     GlStateManager.translate(posX, posY, 50.0F);
/* 101 */     GlStateManager.scale(-scale, scale, scale);
/* 102 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 103 */     float f = ent.renderYawOffset;
/* 104 */     float f1 = ent.rotationYaw;
/* 105 */     float f2 = ent.rotationPitch;
/* 106 */     float f3 = ent.prevRotationYawHead;
/* 107 */     float f4 = ent.rotationYawHead;
/* 108 */     GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
/* 109 */     RenderHelper.enableStandardItemLighting();
/* 110 */     GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/* 111 */     GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0F) * 20.0F, 1.0F, 0.0F, 0.0F);
/* 112 */     ent.renderYawOffset = ((float)Math.atan(mouseX / 40.0F) * 20.0F);
/* 113 */     ent.rotationYaw = ((float)Math.atan(mouseX / 40.0F) * 40.0F);
/* 114 */     ent.rotationPitch = (-(float)Math.atan(mouseY / 40.0F) * 20.0F);
/* 115 */     ent.rotationYawHead = ent.rotationYaw;
/* 116 */     ent.prevRotationYawHead = ent.rotationYaw;
/* 117 */     GlStateManager.translate(0.0F, 0.0F, 0.0F);
/* 118 */     RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/* 119 */     rendermanager.setPlayerViewY(180.0F);
/* 120 */     rendermanager.setRenderShadow(false);
/* 121 */     rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
/* 122 */     rendermanager.setRenderShadow(true);
/* 123 */     ent.renderYawOffset = f;
/* 124 */     ent.rotationYaw = f1;
/* 125 */     ent.rotationPitch = f2;
/* 126 */     ent.prevRotationYawHead = f3;
/* 127 */     ent.rotationYawHead = f4;
/* 128 */     GlStateManager.popMatrix();
/* 129 */     RenderHelper.disableStandardItemLighting();
/* 130 */     GlStateManager.disableRescaleNormal();
/* 131 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 132 */     GlStateManager.disableTexture2D();
/* 133 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 141 */     if (button.id == 0)
/*     */     {
/* 143 */       this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */     
/* 146 */     if (button.id == 1)
/*     */     {
/* 148 */       this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */