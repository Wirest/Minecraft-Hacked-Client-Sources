/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class EntitySnowman extends EntityGolem implements IRangedAttackMob
/*     */ {
/*     */   public EntitySnowman(World worldIn)
/*     */   {
/*  28 */     super(worldIn);
/*  29 */     setSize(0.7F, 1.9F);
/*  30 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  31 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
/*  32 */     this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
/*  33 */     this.tasks.addTask(3, new EntityAIWatchClosest(this, net.minecraft.entity.player.EntityPlayer.class, 6.0F));
/*  34 */     this.tasks.addTask(4, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  35 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, net.minecraft.entity.EntityLiving.class, 10, true, false, IMob.mobSelector));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  40 */     super.applyEntityAttributes();
/*  41 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
/*  42 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  51 */     super.onLivingUpdate();
/*     */     
/*  53 */     if (!this.worldObj.isRemote)
/*     */     {
/*  55 */       int i = MathHelper.floor_double(this.posX);
/*  56 */       int j = MathHelper.floor_double(this.posY);
/*  57 */       int k = MathHelper.floor_double(this.posZ);
/*     */       
/*  59 */       if (isWet())
/*     */       {
/*  61 */         attackEntityFrom(DamageSource.drown, 1.0F);
/*     */       }
/*     */       
/*  64 */       if (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0F)
/*     */       {
/*  66 */         attackEntityFrom(DamageSource.onFire, 1.0F);
/*     */       }
/*     */       
/*  69 */       for (int l = 0; l < 4; l++)
/*     */       {
/*  71 */         i = MathHelper.floor_double(this.posX + (l % 2 * 2 - 1) * 0.25F);
/*  72 */         j = MathHelper.floor_double(this.posY);
/*  73 */         k = MathHelper.floor_double(this.posZ + (l / 2 % 2 * 2 - 1) * 0.25F);
/*  74 */         BlockPos blockpos = new BlockPos(i, j, k);
/*     */         
/*  76 */         if ((this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == net.minecraft.block.material.Material.air) && (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(blockpos) < 0.8F) && (Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos)))
/*     */         {
/*  78 */           this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/*  86 */     return Items.snowball;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/*  94 */     int i = this.rand.nextInt(16);
/*     */     
/*  96 */     for (int j = 0; j < i; j++)
/*     */     {
/*  98 */       dropItem(Items.snowball, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
/*     */   {
/* 107 */     EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, this);
/* 108 */     double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
/* 109 */     double d1 = p_82196_1_.posX - this.posX;
/* 110 */     double d2 = d0 - entitysnowball.posY;
/* 111 */     double d3 = p_82196_1_.posZ - this.posZ;
/* 112 */     float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
/* 113 */     entitysnowball.setThrowableHeading(d1, d2 + f, d3, 1.6F, 12.0F);
/* 114 */     playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
/* 115 */     this.worldObj.spawnEntityInWorld(entitysnowball);
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 120 */     return 1.7F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntitySnowman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */