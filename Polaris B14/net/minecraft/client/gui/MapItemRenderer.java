/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ public class MapItemRenderer
/*     */ {
/*  17 */   private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
/*     */   private final TextureManager textureManager;
/*  19 */   private final Map<String, Instance> loadedMaps = Maps.newHashMap();
/*     */   
/*     */   public MapItemRenderer(TextureManager textureManagerIn) {
/*  22 */     this.textureManager = textureManagerIn;
/*     */   }
/*     */   
/*     */   public void updateMapTexture(MapData mapdataIn) {
/*  26 */     getMapRendererInstance(mapdataIn).updateMapTexture();
/*     */   }
/*     */   
/*     */   public void renderMap(MapData mapdataIn, boolean p_148250_2_) {
/*  30 */     getMapRendererInstance(mapdataIn).render(p_148250_2_);
/*     */   }
/*     */   
/*     */   private Instance getMapRendererInstance(MapData mapdataIn) {
/*  34 */     Instance mapitemrenderer$instance = (Instance)this.loadedMaps.get(mapdataIn.mapName);
/*  35 */     if (mapitemrenderer$instance == null) {
/*  36 */       mapitemrenderer$instance = new Instance(mapdataIn, null);
/*  37 */       this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
/*     */     }
/*     */     
/*  40 */     return mapitemrenderer$instance;
/*     */   }
/*     */   
/*     */   public void clearLoadedMaps() {
/*  44 */     for (Instance mapitemrenderer$instance : this.loadedMaps.values()) {
/*  45 */       this.textureManager.deleteTexture(mapitemrenderer$instance.location);
/*     */     }
/*     */     
/*  48 */     this.loadedMaps.clear();
/*     */   }
/*     */   
/*     */   class Instance {
/*     */     private final MapData mapData;
/*     */     private final DynamicTexture mapTexture;
/*     */     private final ResourceLocation location;
/*     */     private final int[] mapTextureData;
/*     */     
/*     */     private Instance(MapData mapdataIn) {
/*  58 */       this.mapData = mapdataIn;
/*  59 */       this.mapTexture = new DynamicTexture(128, 128);
/*  60 */       this.mapTextureData = this.mapTexture.getTextureData();
/*  61 */       this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
/*     */       
/*  63 */       for (int i = 0; i < this.mapTextureData.length; i++) {
/*  64 */         this.mapTextureData[i] = 0;
/*     */       }
/*     */     }
/*     */     
/*     */     private void updateMapTexture() {
/*  69 */       for (int i = 0; i < 16384; i++) {
/*  70 */         int j = this.mapData.colors[i] & 0xFF;
/*  71 */         if (j / 4 == 0) {
/*  72 */           this.mapTextureData[i] = ((i + i / 128 & 0x1) * 8 + 16 << 24);
/*     */         } else {
/*  74 */           this.mapTextureData[i] = net.minecraft.block.material.MapColor.mapColorArray[(j / 4)].func_151643_b(j & 0x3);
/*     */         }
/*     */       }
/*     */       
/*  78 */       this.mapTexture.updateDynamicTexture();
/*     */     }
/*     */     
/*     */     private void render(boolean noOverlayRendering) {
/*  82 */       int i = 0;
/*  83 */       int j = 0;
/*  84 */       Tessellator tessellator = Tessellator.getInstance();
/*  85 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  86 */       float f = 0.0F;
/*  87 */       MapItemRenderer.this.textureManager.bindTexture(this.location);
/*  88 */       GlStateManager.enableBlend();
/*  89 */       GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
/*  90 */       GlStateManager.disableAlpha();
/*  91 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  92 */       worldrenderer.pos(i + 0 + f, j + 128 - f, -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
/*  93 */       worldrenderer.pos(i + 128 - f, j + 128 - f, -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
/*  94 */       worldrenderer.pos(i + 128 - f, j + 0 + f, -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
/*  95 */       worldrenderer.pos(i + 0 + f, j + 0 + f, -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
/*  96 */       tessellator.draw();
/*  97 */       GlStateManager.enableAlpha();
/*  98 */       GlStateManager.disableBlend();
/*  99 */       MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
/* 100 */       int k = 0;
/*     */       
/* 102 */       for (Vec4b vec4b : this.mapData.mapDecorations.values()) {
/* 103 */         if ((!noOverlayRendering) || (vec4b.func_176110_a() == 1)) {
/* 104 */           GlStateManager.pushMatrix();
/* 105 */           GlStateManager.translate(i + vec4b.func_176112_b() / 2.0F + 64.0F, j + vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
/* 106 */           GlStateManager.rotate(vec4b.func_176111_d() * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
/* 107 */           GlStateManager.scale(4.0F, 4.0F, 3.0F);
/* 108 */           GlStateManager.translate(-0.125F, 0.125F, 0.0F);
/* 109 */           byte b0 = vec4b.func_176110_a();
/* 110 */           float f1 = (b0 % 4 + 0) / 4.0F;
/* 111 */           float f2 = (b0 / 4 + 0) / 4.0F;
/* 112 */           float f3 = (b0 % 4 + 1) / 4.0F;
/* 113 */           float f4 = (b0 / 4 + 1) / 4.0F;
/* 114 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 115 */           float f5 = -0.001F;
/* 116 */           worldrenderer.pos(-1.0D, 1.0D, k * -0.001F).tex(f1, f2).endVertex();
/* 117 */           worldrenderer.pos(1.0D, 1.0D, k * -0.001F).tex(f3, f2).endVertex();
/* 118 */           worldrenderer.pos(1.0D, -1.0D, k * -0.001F).tex(f3, f4).endVertex();
/* 119 */           worldrenderer.pos(-1.0D, -1.0D, k * -0.001F).tex(f1, f4).endVertex();
/* 120 */           tessellator.draw();
/* 121 */           GlStateManager.popMatrix();
/* 122 */           k++;
/*     */         }
/*     */       }
/*     */       
/* 126 */       GlStateManager.pushMatrix();
/* 127 */       GlStateManager.translate(0.0F, 0.0F, -0.04F);
/* 128 */       GlStateManager.scale(1.0F, 1.0F, 1.0F);
/* 129 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\MapItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */