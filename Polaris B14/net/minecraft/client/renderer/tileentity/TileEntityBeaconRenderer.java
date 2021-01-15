/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.tileentity.TileEntityBeacon;
/*     */ import net.minecraft.tileentity.TileEntityBeacon.BeamSegment;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer<TileEntityBeacon>
/*     */ {
/*  15 */   private static final net.minecraft.util.ResourceLocation beaconBeam = new net.minecraft.util.ResourceLocation("textures/entity/beacon_beam.png");
/*     */   
/*     */   public void renderTileEntityAt(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/*  19 */     float f = te.shouldBeamRender();
/*  20 */     GlStateManager.alphaFunc(516, 0.1F);
/*     */     
/*  22 */     if (f > 0.0F)
/*     */     {
/*  24 */       Tessellator tessellator = Tessellator.getInstance();
/*  25 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  26 */       GlStateManager.disableFog();
/*  27 */       List<TileEntityBeacon.BeamSegment> list = te.getBeamSegments();
/*  28 */       int i = 0;
/*     */       
/*  30 */       for (int j = 0; j < list.size(); j++)
/*     */       {
/*  32 */         TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment)list.get(j);
/*  33 */         int k = i + tileentitybeacon$beamsegment.getHeight();
/*  34 */         bindTexture(beaconBeam);
/*  35 */         GL11.glTexParameterf(3553, 10242, 10497.0F);
/*  36 */         GL11.glTexParameterf(3553, 10243, 10497.0F);
/*  37 */         GlStateManager.disableLighting();
/*  38 */         GlStateManager.disableCull();
/*  39 */         GlStateManager.disableBlend();
/*  40 */         GlStateManager.depthMask(true);
/*  41 */         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/*  42 */         double d0 = te.getWorld().getTotalWorldTime() + partialTicks;
/*  43 */         double d1 = MathHelper.func_181162_h(-d0 * 0.2D - MathHelper.floor_double(-d0 * 0.1D));
/*  44 */         float f1 = tileentitybeacon$beamsegment.getColors()[0];
/*  45 */         float f2 = tileentitybeacon$beamsegment.getColors()[1];
/*  46 */         float f3 = tileentitybeacon$beamsegment.getColors()[2];
/*  47 */         double d2 = d0 * 0.025D * -1.5D;
/*  48 */         double d3 = 0.2D;
/*  49 */         double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
/*  50 */         double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
/*  51 */         double d6 = 0.5D + Math.cos(d2 + 0.7853981633974483D) * 0.2D;
/*  52 */         double d7 = 0.5D + Math.sin(d2 + 0.7853981633974483D) * 0.2D;
/*  53 */         double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
/*  54 */         double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
/*  55 */         double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
/*  56 */         double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
/*  57 */         double d12 = 0.0D;
/*  58 */         double d13 = 1.0D;
/*  59 */         double d14 = -1.0D + d1;
/*  60 */         double d15 = tileentitybeacon$beamsegment.getHeight() * f * 2.5D + d14;
/*  61 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*  62 */         worldrenderer.pos(x + d4, y + k, z + d5).tex(1.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  63 */         worldrenderer.pos(x + d4, y + i, z + d5).tex(1.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  64 */         worldrenderer.pos(x + d6, y + i, z + d7).tex(0.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  65 */         worldrenderer.pos(x + d6, y + k, z + d7).tex(0.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  66 */         worldrenderer.pos(x + d10, y + k, z + d11).tex(1.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  67 */         worldrenderer.pos(x + d10, y + i, z + d11).tex(1.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  68 */         worldrenderer.pos(x + d8, y + i, z + d9).tex(0.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  69 */         worldrenderer.pos(x + d8, y + k, z + d9).tex(0.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  70 */         worldrenderer.pos(x + d6, y + k, z + d7).tex(1.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  71 */         worldrenderer.pos(x + d6, y + i, z + d7).tex(1.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  72 */         worldrenderer.pos(x + d10, y + i, z + d11).tex(0.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  73 */         worldrenderer.pos(x + d10, y + k, z + d11).tex(0.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  74 */         worldrenderer.pos(x + d8, y + k, z + d9).tex(1.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  75 */         worldrenderer.pos(x + d8, y + i, z + d9).tex(1.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  76 */         worldrenderer.pos(x + d4, y + i, z + d5).tex(0.0D, d14).color(f1, f2, f3, 1.0F).endVertex();
/*  77 */         worldrenderer.pos(x + d4, y + k, z + d5).tex(0.0D, d15).color(f1, f2, f3, 1.0F).endVertex();
/*  78 */         tessellator.draw();
/*  79 */         GlStateManager.enableBlend();
/*  80 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  81 */         GlStateManager.depthMask(false);
/*  82 */         d2 = 0.2D;
/*  83 */         d3 = 0.2D;
/*  84 */         d4 = 0.8D;
/*  85 */         d5 = 0.2D;
/*  86 */         d6 = 0.2D;
/*  87 */         d7 = 0.8D;
/*  88 */         d8 = 0.8D;
/*  89 */         d9 = 0.8D;
/*  90 */         d10 = 0.0D;
/*  91 */         d11 = 1.0D;
/*  92 */         d12 = -1.0D + d1;
/*  93 */         d13 = tileentitybeacon$beamsegment.getHeight() * f + d12;
/*  94 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*  95 */         worldrenderer.pos(x + 0.2D, y + k, z + 0.2D).tex(1.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/*  96 */         worldrenderer.pos(x + 0.2D, y + i, z + 0.2D).tex(1.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/*  97 */         worldrenderer.pos(x + 0.8D, y + i, z + 0.2D).tex(0.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/*  98 */         worldrenderer.pos(x + 0.8D, y + k, z + 0.2D).tex(0.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/*  99 */         worldrenderer.pos(x + 0.8D, y + k, z + 0.8D).tex(1.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 100 */         worldrenderer.pos(x + 0.8D, y + i, z + 0.8D).tex(1.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 101 */         worldrenderer.pos(x + 0.2D, y + i, z + 0.8D).tex(0.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 102 */         worldrenderer.pos(x + 0.2D, y + k, z + 0.8D).tex(0.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 103 */         worldrenderer.pos(x + 0.8D, y + k, z + 0.2D).tex(1.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 104 */         worldrenderer.pos(x + 0.8D, y + i, z + 0.2D).tex(1.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 105 */         worldrenderer.pos(x + 0.8D, y + i, z + 0.8D).tex(0.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 106 */         worldrenderer.pos(x + 0.8D, y + k, z + 0.8D).tex(0.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 107 */         worldrenderer.pos(x + 0.2D, y + k, z + 0.8D).tex(1.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 108 */         worldrenderer.pos(x + 0.2D, y + i, z + 0.8D).tex(1.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 109 */         worldrenderer.pos(x + 0.2D, y + i, z + 0.2D).tex(0.0D, d12).color(f1, f2, f3, 0.125F).endVertex();
/* 110 */         worldrenderer.pos(x + 0.2D, y + k, z + 0.2D).tex(0.0D, d13).color(f1, f2, f3, 0.125F).endVertex();
/* 111 */         tessellator.draw();
/* 112 */         GlStateManager.enableLighting();
/* 113 */         GlStateManager.enableTexture2D();
/* 114 */         GlStateManager.depthMask(true);
/* 115 */         i = k;
/*     */       }
/*     */       
/* 118 */       GlStateManager.enableFog();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_181055_a()
/*     */   {
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityBeaconRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */