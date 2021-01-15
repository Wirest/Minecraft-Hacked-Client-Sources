/*     */ package net.minecraft.world.chunk;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EmptyChunk extends Chunk
/*     */ {
/*     */   public EmptyChunk(World worldIn, int x, int z)
/*     */   {
/*  19 */     super(worldIn, x, z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAtLocation(int x, int z)
/*     */   {
/*  27 */     return (x == this.xPosition) && (z == this.zPosition);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHeightValue(int x, int z)
/*     */   {
/*  35 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateHeightMap() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateSkylightMap() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block getBlock(BlockPos pos)
/*     */   {
/*  54 */     return Blocks.air;
/*     */   }
/*     */   
/*     */   public int getBlockLightOpacity(BlockPos pos)
/*     */   {
/*  59 */     return 255;
/*     */   }
/*     */   
/*     */   public int getBlockMetadata(BlockPos pos)
/*     */   {
/*  64 */     return 0;
/*     */   }
/*     */   
/*     */   public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos)
/*     */   {
/*  69 */     return p_177413_1_.defaultLightValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {}
/*     */   
/*     */ 
/*     */   public int getLightSubtracted(BlockPos pos, int amount)
/*     */   {
/*  78 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntity(Entity entityIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEntity(Entity entityIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEntityAtIndex(Entity entityIn, int p_76608_2_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSeeSky(BlockPos pos)
/*     */   {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_)
/*     */   {
/* 109 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTileEntity(TileEntity tileEntityIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTileEntity(BlockPos pos) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onChunkLoad() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onChunkUnload() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChunkModified() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean needsSaving(boolean p_76601_1_)
/*     */   {
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   public Random getRandomWithSeed(long seed)
/*     */   {
/* 166 */     return new Random(getWorld().getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 171 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAreLevelsEmpty(int startY, int endY)
/*     */   {
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\EmptyChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */