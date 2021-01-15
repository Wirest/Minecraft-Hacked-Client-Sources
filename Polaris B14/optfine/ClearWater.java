/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ 
/*     */ public class ClearWater
/*     */ {
/*     */   public static void updateWaterOpacity(GameSettings p_updateWaterOpacity_0_, World p_updateWaterOpacity_1_)
/*     */   {
/*  18 */     if (p_updateWaterOpacity_0_ != null)
/*     */     {
/*  20 */       int i = 3;
/*     */       
/*  22 */       if (p_updateWaterOpacity_0_.ofClearWater)
/*     */       {
/*  24 */         i = 1;
/*     */       }
/*     */       
/*  27 */       BlockUtils.setLightOpacity(Blocks.water, i);
/*  28 */       BlockUtils.setLightOpacity(Blocks.flowing_water, i);
/*     */     }
/*     */     
/*  31 */     if (p_updateWaterOpacity_1_ != null)
/*     */     {
/*  33 */       IChunkProvider ichunkprovider = p_updateWaterOpacity_1_.getChunkProvider();
/*     */       
/*  35 */       if (ichunkprovider != null)
/*     */       {
/*  37 */         Entity entity = Config.getMinecraft().getRenderViewEntity();
/*     */         
/*  39 */         if (entity != null)
/*     */         {
/*  41 */           int j = (int)entity.posX / 16;
/*  42 */           int k = (int)entity.posZ / 16;
/*  43 */           int l = j - 512;
/*  44 */           int i1 = j + 512;
/*  45 */           int j1 = k - 512;
/*  46 */           int k1 = k + 512;
/*  47 */           int l1 = 0;
/*     */           
/*  49 */           for (int i2 = l; i2 < i1; i2++)
/*     */           {
/*  51 */             for (int j2 = j1; j2 < k1; j2++)
/*     */             {
/*  53 */               if (ichunkprovider.chunkExists(i2, j2))
/*     */               {
/*  55 */                 Chunk chunk = ichunkprovider.provideChunk(i2, j2);
/*     */                 
/*  57 */                 if ((chunk != null) && (!(chunk instanceof net.minecraft.world.chunk.EmptyChunk)))
/*     */                 {
/*  59 */                   int k2 = i2 << 4;
/*  60 */                   int l2 = j2 << 4;
/*  61 */                   int i3 = k2 + 16;
/*  62 */                   int j3 = l2 + 16;
/*  63 */                   BlockPosM blockposm = new BlockPosM(0, 0, 0);
/*  64 */                   BlockPosM blockposm1 = new BlockPosM(0, 0, 0);
/*     */                   
/*  66 */                   for (int k3 = k2; k3 < i3; k3++)
/*     */                   {
/*  68 */                     for (int l3 = l2; l3 < j3; l3++)
/*     */                     {
/*  70 */                       blockposm.setXyz(k3, 0, l3);
/*  71 */                       BlockPos blockpos = p_updateWaterOpacity_1_.getPrecipitationHeight(blockposm);
/*     */                       
/*  73 */                       for (int i4 = 0; i4 < blockpos.getY(); i4++)
/*     */                       {
/*  75 */                         blockposm1.setXyz(k3, i4, l3);
/*  76 */                         IBlockState iblockstate = p_updateWaterOpacity_1_.getBlockState(blockposm1);
/*     */                         
/*  78 */                         if (iblockstate.getBlock().getMaterial() == net.minecraft.block.material.Material.water)
/*     */                         {
/*  80 */                           p_updateWaterOpacity_1_.markBlocksDirtyVertical(k3, l3, blockposm1.getY(), blockpos.getY());
/*  81 */                           l1++;
/*  82 */                           break;
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*  92 */           if (l1 > 0)
/*     */           {
/*  94 */             String s = "server";
/*     */             
/*  96 */             if (Config.isMinecraftThread())
/*     */             {
/*  98 */               s = "client";
/*     */             }
/*     */             
/* 101 */             Config.dbg("ClearWater (" + s + ") relighted " + l1 + " chunks");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ClearWater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */