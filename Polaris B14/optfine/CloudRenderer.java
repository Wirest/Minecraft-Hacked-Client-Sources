/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CloudRenderer
/*     */ {
/*     */   private Minecraft mc;
/*  12 */   private boolean updated = false;
/*  13 */   private boolean renderFancy = false;
/*     */   int cloudTickCounter;
/*     */   float partialTicks;
/*  16 */   private int glListClouds = -1;
/*  17 */   private int cloudTickCounterUpdate = 0;
/*  18 */   private double cloudPlayerX = 0.0D;
/*  19 */   private double cloudPlayerY = 0.0D;
/*  20 */   private double cloudPlayerZ = 0.0D;
/*     */   
/*     */   public CloudRenderer(Minecraft p_i25_1_)
/*     */   {
/*  24 */     this.mc = p_i25_1_;
/*  25 */     this.glListClouds = net.minecraft.client.renderer.GLAllocation.generateDisplayLists(1);
/*     */   }
/*     */   
/*     */   public void prepareToRender(boolean p_prepareToRender_1_, int p_prepareToRender_2_, float p_prepareToRender_3_)
/*     */   {
/*  30 */     if (this.renderFancy != p_prepareToRender_1_)
/*     */     {
/*  32 */       this.updated = false;
/*     */     }
/*     */     
/*  35 */     this.renderFancy = p_prepareToRender_1_;
/*  36 */     this.cloudTickCounter = p_prepareToRender_2_;
/*  37 */     this.partialTicks = p_prepareToRender_3_;
/*     */   }
/*     */   
/*     */   public boolean shouldUpdateGlList()
/*     */   {
/*  42 */     if (!this.updated)
/*     */     {
/*  44 */       return true;
/*     */     }
/*  46 */     if (this.cloudTickCounter >= this.cloudTickCounterUpdate + 20)
/*     */     {
/*  48 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  52 */     Entity entity = this.mc.getRenderViewEntity();
/*  53 */     boolean flag = this.cloudPlayerY + entity.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F;
/*  54 */     boolean flag1 = entity.prevPosY + entity.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F;
/*  55 */     return flag1 ^ flag;
/*     */   }
/*     */   
/*     */ 
/*     */   public void startUpdateGlList()
/*     */   {
/*  61 */     GL11.glNewList(this.glListClouds, 4864);
/*     */   }
/*     */   
/*     */   public void endUpdateGlList()
/*     */   {
/*  66 */     GL11.glEndList();
/*  67 */     this.cloudTickCounterUpdate = this.cloudTickCounter;
/*  68 */     this.cloudPlayerX = this.mc.getRenderViewEntity().prevPosX;
/*  69 */     this.cloudPlayerY = this.mc.getRenderViewEntity().prevPosY;
/*  70 */     this.cloudPlayerZ = this.mc.getRenderViewEntity().prevPosZ;
/*  71 */     this.updated = true;
/*  72 */     GlStateManager.resetColor();
/*     */   }
/*     */   
/*     */   public void renderGlList()
/*     */   {
/*  77 */     Entity entity = this.mc.getRenderViewEntity();
/*  78 */     double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * this.partialTicks;
/*  79 */     double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * this.partialTicks;
/*  80 */     double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * this.partialTicks;
/*  81 */     double d3 = this.cloudTickCounter - this.cloudTickCounterUpdate + this.partialTicks;
/*  82 */     float f = (float)(d0 - this.cloudPlayerX + d3 * 0.03D);
/*  83 */     float f1 = (float)(d1 - this.cloudPlayerY);
/*  84 */     float f2 = (float)(d2 - this.cloudPlayerZ);
/*  85 */     GlStateManager.pushMatrix();
/*     */     
/*  87 */     if (this.renderFancy)
/*     */     {
/*  89 */       GlStateManager.translate(-f / 12.0F, -f1, -f2 / 12.0F);
/*     */     }
/*     */     else
/*     */     {
/*  93 */       GlStateManager.translate(-f, -f1, -f2);
/*     */     }
/*     */     
/*  96 */     GlStateManager.callList(this.glListClouds);
/*  97 */     GlStateManager.popMatrix();
/*  98 */     GlStateManager.resetColor();
/*     */   }
/*     */   
/*     */   public void reset()
/*     */   {
/* 103 */     this.updated = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CloudRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */