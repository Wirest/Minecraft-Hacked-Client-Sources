/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.client.model.ModelChest;
/*     */ import net.minecraft.client.model.ModelLargeChest;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
/*     */ {
/*  14 */   private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
/*  15 */   private static final ResourceLocation textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
/*  16 */   private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
/*  17 */   private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
/*  18 */   private static final ResourceLocation textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
/*  19 */   private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
/*  20 */   private ModelChest simpleChest = new ModelChest();
/*  21 */   private ModelChest largeChest = new ModelLargeChest();
/*     */   private boolean isChristams;
/*     */   
/*     */   public TileEntityChestRenderer()
/*     */   {
/*  26 */     Calendar calendar = Calendar.getInstance();
/*     */     
/*  28 */     if ((calendar.get(2) + 1 == 12) && (calendar.get(5) >= 24) && (calendar.get(5) <= 26))
/*     */     {
/*  30 */       this.isChristams = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderTileEntityAt(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/*  36 */     GlStateManager.enableDepth();
/*  37 */     GlStateManager.depthFunc(515);
/*  38 */     GlStateManager.depthMask(true);
/*     */     int i;
/*     */     int i;
/*  41 */     if (!te.hasWorldObj())
/*     */     {
/*  43 */       i = 0;
/*     */     }
/*     */     else
/*     */     {
/*  47 */       Block block = te.getBlockType();
/*  48 */       i = te.getBlockMetadata();
/*     */       
/*  50 */       if (((block instanceof BlockChest)) && (i == 0))
/*     */       {
/*  52 */         ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
/*  53 */         i = te.getBlockMetadata();
/*     */       }
/*     */       
/*  56 */       te.checkForAdjacentChests();
/*     */     }
/*     */     
/*  59 */     if ((te.adjacentChestZNeg == null) && (te.adjacentChestXNeg == null))
/*     */     {
/*     */       ModelChest modelchest;
/*     */       
/*  63 */       if ((te.adjacentChestXPos == null) && (te.adjacentChestZPos == null))
/*     */       {
/*  65 */         ModelChest modelchest = this.simpleChest;
/*     */         
/*  67 */         if (destroyStage >= 0)
/*     */         {
/*  69 */           bindTexture(DESTROY_STAGES[destroyStage]);
/*  70 */           GlStateManager.matrixMode(5890);
/*  71 */           GlStateManager.pushMatrix();
/*  72 */           GlStateManager.scale(4.0F, 4.0F, 1.0F);
/*  73 */           GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/*  74 */           GlStateManager.matrixMode(5888);
/*     */         }
/*  76 */         else if (te.getChestType() == 1)
/*     */         {
/*  78 */           bindTexture(textureTrapped);
/*     */         }
/*  80 */         else if (this.isChristams)
/*     */         {
/*  82 */           bindTexture(textureChristmas);
/*     */         }
/*     */         else
/*     */         {
/*  86 */           bindTexture(textureNormal);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  91 */         modelchest = this.largeChest;
/*     */         
/*  93 */         if (destroyStage >= 0)
/*     */         {
/*  95 */           bindTexture(DESTROY_STAGES[destroyStage]);
/*  96 */           GlStateManager.matrixMode(5890);
/*  97 */           GlStateManager.pushMatrix();
/*  98 */           GlStateManager.scale(8.0F, 4.0F, 1.0F);
/*  99 */           GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/* 100 */           GlStateManager.matrixMode(5888);
/*     */         }
/* 102 */         else if (te.getChestType() == 1)
/*     */         {
/* 104 */           bindTexture(textureTrappedDouble);
/*     */         }
/* 106 */         else if (this.isChristams)
/*     */         {
/* 108 */           bindTexture(textureChristmasDouble);
/*     */         }
/*     */         else
/*     */         {
/* 112 */           bindTexture(textureNormalDouble);
/*     */         }
/*     */       }
/*     */       
/* 116 */       GlStateManager.pushMatrix();
/* 117 */       GlStateManager.enableRescaleNormal();
/*     */       
/* 119 */       if (destroyStage < 0)
/*     */       {
/* 121 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 124 */       GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
/* 125 */       GlStateManager.scale(1.0F, -1.0F, -1.0F);
/* 126 */       GlStateManager.translate(0.5F, 0.5F, 0.5F);
/* 127 */       int j = 0;
/*     */       
/* 129 */       if (i == 2)
/*     */       {
/* 131 */         j = 180;
/*     */       }
/*     */       
/* 134 */       if (i == 3)
/*     */       {
/* 136 */         j = 0;
/*     */       }
/*     */       
/* 139 */       if (i == 4)
/*     */       {
/* 141 */         j = 90;
/*     */       }
/*     */       
/* 144 */       if (i == 5)
/*     */       {
/* 146 */         j = -90;
/*     */       }
/*     */       
/* 149 */       if ((i == 2) && (te.adjacentChestXPos != null))
/*     */       {
/* 151 */         GlStateManager.translate(1.0F, 0.0F, 0.0F);
/*     */       }
/*     */       
/* 154 */       if ((i == 5) && (te.adjacentChestZPos != null))
/*     */       {
/* 156 */         GlStateManager.translate(0.0F, 0.0F, -1.0F);
/*     */       }
/*     */       
/* 159 */       GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
/* 160 */       GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/* 161 */       float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
/*     */       
/* 163 */       if (te.adjacentChestZNeg != null)
/*     */       {
/* 165 */         float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
/*     */         
/* 167 */         if (f1 > f)
/*     */         {
/* 169 */           f = f1;
/*     */         }
/*     */       }
/*     */       
/* 173 */       if (te.adjacentChestXNeg != null)
/*     */       {
/* 175 */         float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
/*     */         
/* 177 */         if (f2 > f)
/*     */         {
/* 179 */           f = f2;
/*     */         }
/*     */       }
/*     */       
/* 183 */       f = 1.0F - f;
/* 184 */       f = 1.0F - f * f * f;
/* 185 */       modelchest.chestLid.rotateAngleX = (-(f * 3.1415927F / 2.0F));
/* 186 */       modelchest.renderAll();
/* 187 */       GlStateManager.disableRescaleNormal();
/* 188 */       GlStateManager.popMatrix();
/* 189 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 191 */       if (destroyStage >= 0)
/*     */       {
/* 193 */         GlStateManager.matrixMode(5890);
/* 194 */         GlStateManager.popMatrix();
/* 195 */         GlStateManager.matrixMode(5888);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */