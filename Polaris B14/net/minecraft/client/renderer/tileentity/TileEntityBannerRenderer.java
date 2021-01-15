/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBanner;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.tileentity.TileEntityBanner.EnumBannerPattern;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
/*     */ {
/*  21 */   private static final Map<String, TimedBannerTexture> DESIGNS = ;
/*  22 */   private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
/*  23 */   private ModelBanner bannerModel = new ModelBanner();
/*     */   
/*     */   public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/*  27 */     boolean flag = te.getWorld() != null;
/*  28 */     boolean flag1 = (!flag) || (te.getBlockType() == net.minecraft.init.Blocks.standing_banner);
/*  29 */     int i = flag ? te.getBlockMetadata() : 0;
/*  30 */     long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
/*  31 */     GlStateManager.pushMatrix();
/*  32 */     float f = 0.6666667F;
/*     */     
/*  34 */     if (flag1)
/*     */     {
/*  36 */       GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
/*  37 */       float f1 = i * 360 / 16.0F;
/*  38 */       GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
/*  39 */       this.bannerModel.bannerStand.showModel = true;
/*     */     }
/*     */     else
/*     */     {
/*  43 */       float f2 = 0.0F;
/*     */       
/*  45 */       if (i == 2)
/*     */       {
/*  47 */         f2 = 180.0F;
/*     */       }
/*     */       
/*  50 */       if (i == 4)
/*     */       {
/*  52 */         f2 = 90.0F;
/*     */       }
/*     */       
/*  55 */       if (i == 5)
/*     */       {
/*  57 */         f2 = -90.0F;
/*     */       }
/*     */       
/*  60 */       GlStateManager.translate((float)x + 0.5F, (float)y - 0.25F * f, (float)z + 0.5F);
/*  61 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/*  62 */       GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
/*  63 */       this.bannerModel.bannerStand.showModel = false;
/*     */     }
/*     */     
/*  66 */     BlockPos blockpos = te.getPos();
/*  67 */     float f3 = blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13 + (float)j + partialTicks;
/*  68 */     this.bannerModel.bannerSlate.rotateAngleX = ((-0.0125F + 0.01F * MathHelper.cos(f3 * 3.1415927F * 0.02F)) * 3.1415927F);
/*  69 */     GlStateManager.enableRescaleNormal();
/*  70 */     ResourceLocation resourcelocation = func_178463_a(te);
/*     */     
/*  72 */     if (resourcelocation != null)
/*     */     {
/*  74 */       bindTexture(resourcelocation);
/*  75 */       GlStateManager.pushMatrix();
/*  76 */       GlStateManager.scale(f, -f, -f);
/*  77 */       this.bannerModel.renderBanner();
/*  78 */       GlStateManager.popMatrix();
/*     */     }
/*     */     
/*  81 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  82 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private ResourceLocation func_178463_a(TileEntityBanner bannerObj)
/*     */   {
/*  87 */     String s = bannerObj.func_175116_e();
/*     */     
/*  89 */     if (s.isEmpty())
/*     */     {
/*  91 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  95 */     TimedBannerTexture tileentitybannerrenderer$timedbannertexture = (TimedBannerTexture)DESIGNS.get(s);
/*     */     
/*  97 */     if (tileentitybannerrenderer$timedbannertexture == null) {
/*     */       TimedBannerTexture tileentitybannerrenderer$timedbannertexture1;
/*  99 */       if (DESIGNS.size() >= 256)
/*     */       {
/* 101 */         long i = System.currentTimeMillis();
/* 102 */         Iterator<String> iterator = DESIGNS.keySet().iterator();
/*     */         
/* 104 */         while (iterator.hasNext())
/*     */         {
/* 106 */           String s1 = (String)iterator.next();
/* 107 */           tileentitybannerrenderer$timedbannertexture1 = (TimedBannerTexture)DESIGNS.get(s1);
/*     */           
/* 109 */           if (i - tileentitybannerrenderer$timedbannertexture1.systemTime > 60000L)
/*     */           {
/* 111 */             Minecraft.getMinecraft().getTextureManager().deleteTexture(tileentitybannerrenderer$timedbannertexture1.bannerTexture);
/* 112 */             iterator.remove();
/*     */           }
/*     */         }
/*     */         
/* 116 */         if (DESIGNS.size() >= 256)
/*     */         {
/* 118 */           return null;
/*     */         }
/*     */       }
/*     */       
/* 122 */       List<TileEntityBanner.EnumBannerPattern> list1 = bannerObj.getPatternList();
/* 123 */       List<EnumDyeColor> list = bannerObj.getColorList();
/* 124 */       List<String> list2 = com.google.common.collect.Lists.newArrayList();
/*     */       
/* 126 */       for (TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : list1)
/*     */       {
/* 128 */         list2.add("textures/entity/banner/" + tileentitybanner$enumbannerpattern.getPatternName() + ".png");
/*     */       }
/*     */       
/* 131 */       tileentitybannerrenderer$timedbannertexture = new TimedBannerTexture(null);
/* 132 */       tileentitybannerrenderer$timedbannertexture.bannerTexture = new ResourceLocation(s);
/* 133 */       Minecraft.getMinecraft().getTextureManager().loadTexture(tileentitybannerrenderer$timedbannertexture.bannerTexture, new LayeredColorMaskTexture(BANNERTEXTURES, list2, list));
/* 134 */       DESIGNS.put(s, tileentitybannerrenderer$timedbannertexture);
/*     */     }
/*     */     
/* 137 */     tileentitybannerrenderer$timedbannertexture.systemTime = System.currentTimeMillis();
/* 138 */     return tileentitybannerrenderer$timedbannertexture.bannerTexture;
/*     */   }
/*     */   
/*     */   static class TimedBannerTexture
/*     */   {
/*     */     public long systemTime;
/*     */     public ResourceLocation bannerTexture;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityBannerRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */