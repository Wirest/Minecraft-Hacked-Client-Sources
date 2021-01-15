/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEnderCrystal extends Entity
/*     */ {
/*     */   public int innerRotation;
/*     */   public int health;
/*     */   
/*     */   public EntityEnderCrystal(World worldIn)
/*     */   {
/*  20 */     super(worldIn);
/*  21 */     this.preventEntitySpawning = true;
/*  22 */     setSize(2.0F, 2.0F);
/*  23 */     this.health = 5;
/*  24 */     this.innerRotation = this.rand.nextInt(100000);
/*     */   }
/*     */   
/*     */   public EntityEnderCrystal(World worldIn, double p_i1699_2_, double p_i1699_4_, double p_i1699_6_)
/*     */   {
/*  29 */     this(worldIn);
/*  30 */     setPosition(p_i1699_2_, p_i1699_4_, p_i1699_6_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  39 */     return false;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  44 */     this.dataWatcher.addObject(8, Integer.valueOf(this.health));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  52 */     this.prevPosX = this.posX;
/*  53 */     this.prevPosY = this.posY;
/*  54 */     this.prevPosZ = this.posZ;
/*  55 */     this.innerRotation += 1;
/*  56 */     this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
/*  57 */     int i = MathHelper.floor_double(this.posX);
/*  58 */     int j = MathHelper.floor_double(this.posY);
/*  59 */     int k = MathHelper.floor_double(this.posZ);
/*     */     
/*  61 */     if (((this.worldObj.provider instanceof net.minecraft.world.WorldProviderEnd)) && (this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock() != Blocks.fire))
/*     */     {
/*  63 */       this.worldObj.setBlockState(new BlockPos(i, j, k), Blocks.fire.getDefaultState());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/*  86 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  94 */     if (isEntityInvulnerable(source))
/*     */     {
/*  96 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 100 */     if ((!this.isDead) && (!this.worldObj.isRemote))
/*     */     {
/* 102 */       this.health = 0;
/*     */       
/* 104 */       if (this.health <= 0)
/*     */       {
/* 106 */         setDead();
/*     */         
/* 108 */         if (!this.worldObj.isRemote)
/*     */         {
/* 110 */           this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 6.0F, true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 115 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */