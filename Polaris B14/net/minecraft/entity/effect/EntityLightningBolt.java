/*     */ package net.minecraft.entity.effect;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityLightningBolt
/*     */   extends EntityWeatherEffect
/*     */ {
/*     */   private int lightningState;
/*     */   public long boltVertex;
/*     */   private int boltLivingTime;
/*     */   
/*     */   public EntityLightningBolt(World worldIn, double posX, double posY, double posZ)
/*     */   {
/*  32 */     super(worldIn);
/*  33 */     setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
/*  34 */     this.lightningState = 2;
/*  35 */     this.boltVertex = this.rand.nextLong();
/*  36 */     this.boltLivingTime = (this.rand.nextInt(3) + 1);
/*  37 */     BlockPos blockpos = new BlockPos(this);
/*     */     
/*  39 */     if ((!worldIn.isRemote) && (worldIn.getGameRules().getBoolean("doFireTick")) && ((worldIn.getDifficulty() == EnumDifficulty.NORMAL) || (worldIn.getDifficulty() == EnumDifficulty.HARD)) && (worldIn.isAreaLoaded(blockpos, 10)))
/*     */     {
/*  41 */       if ((worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) && (Blocks.fire.canPlaceBlockAt(worldIn, blockpos)))
/*     */       {
/*  43 */         worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */       }
/*     */       
/*  46 */       for (int i = 0; i < 4; i++)
/*     */       {
/*  48 */         BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
/*     */         
/*  50 */         if ((worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) && (Blocks.fire.canPlaceBlockAt(worldIn, blockpos1)))
/*     */         {
/*  52 */           worldIn.setBlockState(blockpos1, Blocks.fire.getDefaultState());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  63 */     super.onUpdate();
/*     */     
/*  65 */     if (this.lightningState == 2)
/*     */     {
/*  67 */       this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
/*  68 */       this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
/*     */     }
/*     */     
/*  71 */     this.lightningState -= 1;
/*     */     
/*  73 */     if (this.lightningState < 0)
/*     */     {
/*  75 */       if (this.boltLivingTime == 0)
/*     */       {
/*  77 */         setDead();
/*     */       }
/*  79 */       else if (this.lightningState < -this.rand.nextInt(10))
/*     */       {
/*  81 */         this.boltLivingTime -= 1;
/*  82 */         this.lightningState = 1;
/*  83 */         this.boltVertex = this.rand.nextLong();
/*  84 */         BlockPos blockpos = new BlockPos(this);
/*     */         
/*  86 */         if ((!this.worldObj.isRemote) && (this.worldObj.getGameRules().getBoolean("doFireTick")) && (this.worldObj.isAreaLoaded(blockpos, 10)) && (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air) && (Blocks.fire.canPlaceBlockAt(this.worldObj, blockpos)))
/*     */         {
/*  88 */           this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     if (this.lightningState >= 0)
/*     */     {
/*  95 */       if (this.worldObj.isRemote)
/*     */       {
/*  97 */         this.worldObj.setLastLightningBolt(2);
/*     */       }
/*     */       else
/*     */       {
/* 101 */         double d0 = 3.0D;
/* 102 */         List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));
/*     */         
/* 104 */         for (int i = 0; i < list.size(); i++)
/*     */         {
/* 106 */           Entity entity = (Entity)list.get(i);
/* 107 */           entity.onStruckByLightning(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void entityInit() {}
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\effect\EntityLightningBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */