/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSilverfish;
/*     */ import net.minecraft.block.BlockSilverfish.EnumType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntityDamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySilverfish extends EntityMob
/*     */ {
/*     */   private AISummonSilverfish summonSilverfish;
/*     */   
/*     */   public EntitySilverfish(World worldIn)
/*     */   {
/*  30 */     super(worldIn);
/*  31 */     setSize(0.4F, 0.3F);
/*  32 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  33 */     this.tasks.addTask(3, this.summonSilverfish = new AISummonSilverfish(this));
/*  34 */     this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  35 */     this.tasks.addTask(5, new AIHideInStone(this));
/*  36 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, true, new Class[0]));
/*  37 */     this.targetTasks.addTask(2, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYOffset()
/*     */   {
/*  45 */     return 0.2D;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/*  50 */     return 0.1F;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  55 */     super.applyEntityAttributes();
/*  56 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*  57 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*  58 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  75 */     return "mob.silverfish.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  83 */     return "mob.silverfish.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  91 */     return "mob.silverfish.kill";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  99 */     if (isEntityInvulnerable(source))
/*     */     {
/* 101 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 105 */     if (((source instanceof EntityDamageSource)) || (source == DamageSource.magic))
/*     */     {
/* 107 */       this.summonSilverfish.func_179462_f();
/*     */     }
/*     */     
/* 110 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 116 */     playSound("mob.silverfish.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 121 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 129 */     this.renderYawOffset = this.rotationYaw;
/* 130 */     super.onUpdate();
/*     */   }
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos)
/*     */   {
/* 135 */     return this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.stone ? 10.0F : super.getBlockPathWeight(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidLightLevel()
/*     */   {
/* 143 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 151 */     if (super.getCanSpawnHere())
/*     */     {
/* 153 */       EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
/* 154 */       return entityplayer == null;
/*     */     }
/*     */     
/*     */ 
/* 158 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 167 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */   
/*     */   static class AIHideInStone extends EntityAIWander
/*     */   {
/*     */     private final EntitySilverfish field_179485_a;
/*     */     private EnumFacing facing;
/*     */     private boolean field_179484_c;
/*     */     
/*     */     public AIHideInStone(EntitySilverfish p_i45827_1_)
/*     */     {
/* 178 */       super(1.0D, 10);
/* 179 */       this.field_179485_a = p_i45827_1_;
/* 180 */       setMutexBits(1);
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 185 */       if (this.field_179485_a.getAttackTarget() != null)
/*     */       {
/* 187 */         return false;
/*     */       }
/* 189 */       if (!this.field_179485_a.getNavigator().noPath())
/*     */       {
/* 191 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 195 */       Random random = this.field_179485_a.getRNG();
/*     */       
/* 197 */       if (random.nextInt(10) == 0)
/*     */       {
/* 199 */         this.facing = EnumFacing.random(random);
/* 200 */         BlockPos blockpos = new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D, this.field_179485_a.posZ).offset(this.facing);
/* 201 */         IBlockState iblockstate = this.field_179485_a.worldObj.getBlockState(blockpos);
/*     */         
/* 203 */         if (BlockSilverfish.canContainSilverfish(iblockstate))
/*     */         {
/* 205 */           this.field_179484_c = true;
/* 206 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 210 */       this.field_179484_c = false;
/* 211 */       return super.shouldExecute();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean continueExecuting()
/*     */     {
/* 217 */       return this.field_179484_c ? false : super.continueExecuting();
/*     */     }
/*     */     
/*     */     public void startExecuting()
/*     */     {
/* 222 */       if (!this.field_179484_c)
/*     */       {
/* 224 */         super.startExecuting();
/*     */       }
/*     */       else
/*     */       {
/* 228 */         World world = this.field_179485_a.worldObj;
/* 229 */         BlockPos blockpos = new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D, this.field_179485_a.posZ).offset(this.facing);
/* 230 */         IBlockState iblockstate = world.getBlockState(blockpos);
/*     */         
/* 232 */         if (BlockSilverfish.canContainSilverfish(iblockstate))
/*     */         {
/* 234 */           world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
/* 235 */           this.field_179485_a.spawnExplosionParticle();
/* 236 */           this.field_179485_a.setDead();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISummonSilverfish extends net.minecraft.entity.ai.EntityAIBase
/*     */   {
/*     */     private EntitySilverfish silverfish;
/*     */     private int field_179463_b;
/*     */     
/*     */     public AISummonSilverfish(EntitySilverfish p_i45826_1_)
/*     */     {
/* 249 */       this.silverfish = p_i45826_1_;
/*     */     }
/*     */     
/*     */     public void func_179462_f()
/*     */     {
/* 254 */       if (this.field_179463_b == 0)
/*     */       {
/* 256 */         this.field_179463_b = 20;
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 262 */       return this.field_179463_b > 0;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 267 */       this.field_179463_b -= 1;
/*     */       
/* 269 */       if (this.field_179463_b <= 0)
/*     */       {
/* 271 */         World world = this.silverfish.worldObj;
/* 272 */         Random random = this.silverfish.getRNG();
/* 273 */         BlockPos blockpos = new BlockPos(this.silverfish);
/*     */         
/* 275 */         for (int i = 0; (i <= 5) && (i >= -5); i = i <= 0 ? 1 - i : 0 - i)
/*     */         {
/* 277 */           for (int j = 0; (j <= 10) && (j >= -10); j = j <= 0 ? 1 - j : 0 - j)
/*     */           {
/* 279 */             for (int k = 0; (k <= 10) && (k >= -10); k = k <= 0 ? 1 - k : 0 - k)
/*     */             {
/* 281 */               BlockPos blockpos1 = blockpos.add(j, i, k);
/* 282 */               IBlockState iblockstate = world.getBlockState(blockpos1);
/*     */               
/* 284 */               if (iblockstate.getBlock() == Blocks.monster_egg)
/*     */               {
/* 286 */                 if (world.getGameRules().getBoolean("mobGriefing"))
/*     */                 {
/* 288 */                   world.destroyBlock(blockpos1, true);
/*     */                 }
/*     */                 else
/*     */                 {
/* 292 */                   world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue(BlockSilverfish.VARIANT)).getModelBlock(), 3);
/*     */                 }
/*     */                 
/* 295 */                 if (random.nextBoolean())
/*     */                 {
/* 297 */                   return;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntitySilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */