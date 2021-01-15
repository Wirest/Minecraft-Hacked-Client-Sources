/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiUtilRenderComponents;
/*     */ import net.minecraft.client.model.ModelSign;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign>
/*     */ {
/*  17 */   private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
/*     */   
/*     */ 
/*  20 */   private final ModelSign model = new ModelSign();
/*     */   
/*     */   public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/*  24 */     Block block = te.getBlockType();
/*  25 */     GlStateManager.pushMatrix();
/*  26 */     float f = 0.6666667F;
/*     */     
/*  28 */     if (block == Blocks.standing_sign)
/*     */     {
/*  30 */       GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
/*  31 */       float f1 = te.getBlockMetadata() * 360 / 16.0F;
/*  32 */       GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
/*  33 */       this.model.signStick.showModel = true;
/*     */     }
/*     */     else
/*     */     {
/*  37 */       int k = te.getBlockMetadata();
/*  38 */       float f2 = 0.0F;
/*     */       
/*  40 */       if (k == 2)
/*     */       {
/*  42 */         f2 = 180.0F;
/*     */       }
/*     */       
/*  45 */       if (k == 4)
/*     */       {
/*  47 */         f2 = 90.0F;
/*     */       }
/*     */       
/*  50 */       if (k == 5)
/*     */       {
/*  52 */         f2 = -90.0F;
/*     */       }
/*     */       
/*  55 */       GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
/*  56 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/*  57 */       GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
/*  58 */       this.model.signStick.showModel = false;
/*     */     }
/*     */     
/*  61 */     if (destroyStage >= 0)
/*     */     {
/*  63 */       bindTexture(DESTROY_STAGES[destroyStage]);
/*  64 */       GlStateManager.matrixMode(5890);
/*  65 */       GlStateManager.pushMatrix();
/*  66 */       GlStateManager.scale(4.0F, 2.0F, 1.0F);
/*  67 */       GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/*  68 */       GlStateManager.matrixMode(5888);
/*     */     }
/*     */     else
/*     */     {
/*  72 */       bindTexture(SIGN_TEXTURE);
/*     */     }
/*     */     
/*  75 */     GlStateManager.enableRescaleNormal();
/*  76 */     GlStateManager.pushMatrix();
/*  77 */     GlStateManager.scale(f, -f, -f);
/*  78 */     this.model.renderSign();
/*  79 */     GlStateManager.popMatrix();
/*  80 */     FontRenderer fontrenderer = getFontRenderer();
/*  81 */     float f3 = 0.015625F * f;
/*  82 */     GlStateManager.translate(0.0F, 0.5F * f, 0.07F * f);
/*  83 */     GlStateManager.scale(f3, -f3, f3);
/*  84 */     GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
/*  85 */     GlStateManager.depthMask(false);
/*  86 */     int i = 0;
/*     */     
/*  88 */     if (destroyStage < 0)
/*     */     {
/*  90 */       for (int j = 0; j < te.signText.length; j++)
/*     */       {
/*  92 */         if (te.signText[j] != null)
/*     */         {
/*  94 */           IChatComponent ichatcomponent = te.signText[j];
/*  95 */           List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
/*  96 */           String s = (list != null) && (list.size() > 0) ? ((IChatComponent)list.get(0)).getFormattedText() : "";
/*     */           
/*  98 */           if (j == te.lineBeingEdited)
/*     */           {
/* 100 */             s = "> " + s + " <";
/* 101 */             fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
/*     */           }
/*     */           else
/*     */           {
/* 105 */             fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 111 */     GlStateManager.depthMask(true);
/* 112 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 113 */     GlStateManager.popMatrix();
/*     */     
/* 115 */     if (destroyStage >= 0)
/*     */     {
/* 117 */       GlStateManager.matrixMode(5890);
/* 118 */       GlStateManager.popMatrix();
/* 119 */       GlStateManager.matrixMode(5888);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntitySignRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */