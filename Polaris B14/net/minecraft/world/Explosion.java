/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentProtection;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ public class Explosion
/*     */ {
/*     */   private final boolean isFlaming;
/*     */   private final boolean isSmoking;
/*     */   private final Random explosionRNG;
/*     */   private final World worldObj;
/*     */   private final double explosionX;
/*     */   private final double explosionY;
/*     */   private final double explosionZ;
/*     */   private final Entity exploder;
/*     */   private final float explosionSize;
/*     */   private final List<BlockPos> affectedBlockPositions;
/*     */   private final Map<EntityPlayer, Vec3> playerKnockbackMap;
/*     */   
/*     */   public Explosion(World worldIn, Entity p_i45752_2_, double p_i45752_3_, double p_i45752_5_, double p_i45752_7_, float p_i45752_9_, List<BlockPos> p_i45752_10_)
/*     */   {
/*  45 */     this(worldIn, p_i45752_2_, p_i45752_3_, p_i45752_5_, p_i45752_7_, p_i45752_9_, false, true, p_i45752_10_);
/*     */   }
/*     */   
/*     */   public Explosion(World worldIn, Entity p_i45753_2_, double p_i45753_3_, double p_i45753_5_, double p_i45753_7_, float p_i45753_9_, boolean p_i45753_10_, boolean p_i45753_11_, List<BlockPos> p_i45753_12_)
/*     */   {
/*  50 */     this(worldIn, p_i45753_2_, p_i45753_3_, p_i45753_5_, p_i45753_7_, p_i45753_9_, p_i45753_10_, p_i45753_11_);
/*  51 */     this.affectedBlockPositions.addAll(p_i45753_12_);
/*     */   }
/*     */   
/*     */   public Explosion(World worldIn, Entity p_i45754_2_, double p_i45754_3_, double p_i45754_5_, double p_i45754_7_, float size, boolean p_i45754_10_, boolean p_i45754_11_)
/*     */   {
/*  56 */     this.explosionRNG = new Random();
/*  57 */     this.affectedBlockPositions = Lists.newArrayList();
/*  58 */     this.playerKnockbackMap = Maps.newHashMap();
/*  59 */     this.worldObj = worldIn;
/*  60 */     this.exploder = p_i45754_2_;
/*  61 */     this.explosionSize = size;
/*  62 */     this.explosionX = p_i45754_3_;
/*  63 */     this.explosionY = p_i45754_5_;
/*  64 */     this.explosionZ = p_i45754_7_;
/*  65 */     this.isFlaming = p_i45754_10_;
/*  66 */     this.isSmoking = p_i45754_11_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doExplosionA()
/*     */   {
/*  74 */     Set<BlockPos> set = Sets.newHashSet();
/*  75 */     int i = 16;
/*     */     
/*  77 */     for (int j = 0; j < 16; j++)
/*     */     {
/*  79 */       for (int k = 0; k < 16; k++)
/*     */       {
/*  81 */         for (int l = 0; l < 16; l++)
/*     */         {
/*  83 */           if ((j == 0) || (j == 15) || (k == 0) || (k == 15) || (l == 0) || (l == 15))
/*     */           {
/*  85 */             double d0 = j / 15.0F * 2.0F - 1.0F;
/*  86 */             double d1 = k / 15.0F * 2.0F - 1.0F;
/*  87 */             double d2 = l / 15.0F * 2.0F - 1.0F;
/*  88 */             double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
/*  89 */             d0 /= d3;
/*  90 */             d1 /= d3;
/*  91 */             d2 /= d3;
/*  92 */             float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
/*  93 */             double d4 = this.explosionX;
/*  94 */             double d6 = this.explosionY;
/*  95 */             double d8 = this.explosionZ;
/*     */             
/*  97 */             for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
/*     */             {
/*  99 */               BlockPos blockpos = new BlockPos(d4, d6, d8);
/* 100 */               IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/*     */               
/* 102 */               if (iblockstate.getBlock().getMaterial() != Material.air)
/*     */               {
/* 104 */                 float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
/* 105 */                 f -= (f2 + 0.3F) * 0.3F;
/*     */               }
/*     */               
/* 108 */               if ((f > 0.0F) && ((this.exploder == null) || (this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))))
/*     */               {
/* 110 */                 set.add(blockpos);
/*     */               }
/*     */               
/* 113 */               d4 += d0 * 0.30000001192092896D;
/* 114 */               d6 += d1 * 0.30000001192092896D;
/* 115 */               d8 += d2 * 0.30000001192092896D;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     this.affectedBlockPositions.addAll(set);
/* 123 */     float f3 = this.explosionSize * 2.0F;
/* 124 */     int k1 = MathHelper.floor_double(this.explosionX - f3 - 1.0D);
/* 125 */     int l1 = MathHelper.floor_double(this.explosionX + f3 + 1.0D);
/* 126 */     int i2 = MathHelper.floor_double(this.explosionY - f3 - 1.0D);
/* 127 */     int i1 = MathHelper.floor_double(this.explosionY + f3 + 1.0D);
/* 128 */     int j2 = MathHelper.floor_double(this.explosionZ - f3 - 1.0D);
/* 129 */     int j1 = MathHelper.floor_double(this.explosionZ + f3 + 1.0D);
/* 130 */     List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
/* 131 */     Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);
/*     */     
/* 133 */     for (int k2 = 0; k2 < list.size(); k2++)
/*     */     {
/* 135 */       Entity entity = (Entity)list.get(k2);
/*     */       
/* 137 */       if (!entity.isImmuneToExplosions())
/*     */       {
/* 139 */         double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / f3;
/*     */         
/* 141 */         if (d12 <= 1.0D)
/*     */         {
/* 143 */           double d5 = entity.posX - this.explosionX;
/* 144 */           double d7 = entity.posY + entity.getEyeHeight() - this.explosionY;
/* 145 */           double d9 = entity.posZ - this.explosionZ;
/* 146 */           double d13 = MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);
/*     */           
/* 148 */           if (d13 != 0.0D)
/*     */           {
/* 150 */             d5 /= d13;
/* 151 */             d7 /= d13;
/* 152 */             d9 /= d13;
/* 153 */             double d14 = this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
/* 154 */             double d10 = (1.0D - d12) * d14;
/* 155 */             entity.attackEntityFrom(DamageSource.setExplosionSource(this), (int)((d10 * d10 + d10) / 2.0D * 8.0D * f3 + 1.0D));
/* 156 */             double d11 = EnchantmentProtection.func_92092_a(entity, d10);
/* 157 */             entity.motionX += d5 * d11;
/* 158 */             entity.motionY += d7 * d11;
/* 159 */             entity.motionZ += d9 * d11;
/*     */             
/* 161 */             if (((entity instanceof EntityPlayer)) && (!((EntityPlayer)entity).capabilities.disableDamage))
/*     */             {
/* 163 */               this.playerKnockbackMap.put((EntityPlayer)entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doExplosionB(boolean spawnParticles)
/*     */   {
/* 176 */     this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
/*     */     
/* 178 */     if ((this.explosionSize >= 2.0F) && (this.isSmoking))
/*     */     {
/* 180 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     else
/*     */     {
/* 184 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     
/* 187 */     if (this.isSmoking)
/*     */     {
/* 189 */       for (BlockPos blockpos : this.affectedBlockPositions)
/*     */       {
/* 191 */         Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */         
/* 193 */         if (spawnParticles)
/*     */         {
/* 195 */           double d0 = blockpos.getX() + this.worldObj.rand.nextFloat();
/* 196 */           double d1 = blockpos.getY() + this.worldObj.rand.nextFloat();
/* 197 */           double d2 = blockpos.getZ() + this.worldObj.rand.nextFloat();
/* 198 */           double d3 = d0 - this.explosionX;
/* 199 */           double d4 = d1 - this.explosionY;
/* 200 */           double d5 = d2 - this.explosionZ;
/* 201 */           double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
/* 202 */           d3 /= d6;
/* 203 */           d4 /= d6;
/* 204 */           d5 /= d6;
/* 205 */           double d7 = 0.5D / (d6 / this.explosionSize + 0.1D);
/* 206 */           d7 *= (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
/* 207 */           d3 *= d7;
/* 208 */           d4 *= d7;
/* 209 */           d5 *= d7;
/* 210 */           this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5, new int[0]);
/* 211 */           this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
/*     */         }
/*     */         
/* 214 */         if (block.getMaterial() != Material.air)
/*     */         {
/* 216 */           if (block.canDropFromExplosion(this))
/*     */           {
/* 218 */             block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
/*     */           }
/*     */           
/* 221 */           this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState(), 3);
/* 222 */           block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 227 */     if (this.isFlaming)
/*     */     {
/* 229 */       for (BlockPos blockpos1 : this.affectedBlockPositions)
/*     */       {
/* 231 */         if ((this.worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) && (this.worldObj.getBlockState(blockpos1.down()).getBlock().isFullBlock()) && (this.explosionRNG.nextInt(3) == 0))
/*     */         {
/* 233 */           this.worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<EntityPlayer, Vec3> getPlayerKnockbackMap()
/*     */   {
/* 241 */     return this.playerKnockbackMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityLivingBase getExplosivePlacedBy()
/*     */   {
/* 249 */     return (this.exploder instanceof EntityLivingBase) ? (EntityLivingBase)this.exploder : (this.exploder instanceof EntityTNTPrimed) ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : this.exploder == null ? null : null;
/*     */   }
/*     */   
/*     */   public void func_180342_d()
/*     */   {
/* 254 */     this.affectedBlockPositions.clear();
/*     */   }
/*     */   
/*     */   public List<BlockPos> getAffectedBlockPositions()
/*     */   {
/* 259 */     return this.affectedBlockPositions;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\Explosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */