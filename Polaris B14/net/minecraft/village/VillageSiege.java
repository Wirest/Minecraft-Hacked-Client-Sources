/*     */ package net.minecraft.village;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityLiving.SpawnPlacementType;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.SpawnerAnimals;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class VillageSiege
/*     */ {
/*     */   private World worldObj;
/*     */   private boolean field_75535_b;
/*  19 */   private int field_75536_c = -1;
/*     */   
/*     */   private int field_75533_d;
/*     */   
/*     */   private int field_75534_e;
/*     */   private Village theVillage;
/*     */   private int field_75532_g;
/*     */   private int field_75538_h;
/*     */   private int field_75539_i;
/*     */   
/*     */   public VillageSiege(World worldIn)
/*     */   {
/*  31 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tick()
/*     */   {
/*  39 */     if (this.worldObj.isDaytime())
/*     */     {
/*  41 */       this.field_75536_c = 0;
/*     */     }
/*  43 */     else if (this.field_75536_c != 2)
/*     */     {
/*  45 */       if (this.field_75536_c == 0)
/*     */       {
/*  47 */         float f = this.worldObj.getCelestialAngle(0.0F);
/*     */         
/*  49 */         if ((f < 0.5D) || (f > 0.501D))
/*     */         {
/*  51 */           return;
/*     */         }
/*     */         
/*  54 */         this.field_75536_c = (this.worldObj.rand.nextInt(10) == 0 ? 1 : 2);
/*  55 */         this.field_75535_b = false;
/*     */         
/*  57 */         if (this.field_75536_c == 2)
/*     */         {
/*  59 */           return;
/*     */         }
/*     */       }
/*     */       
/*  63 */       if (this.field_75536_c != -1)
/*     */       {
/*  65 */         if (!this.field_75535_b)
/*     */         {
/*  67 */           if (!func_75529_b())
/*     */           {
/*  69 */             return;
/*     */           }
/*     */           
/*  72 */           this.field_75535_b = true;
/*     */         }
/*     */         
/*  75 */         if (this.field_75534_e > 0)
/*     */         {
/*  77 */           this.field_75534_e -= 1;
/*     */         }
/*     */         else
/*     */         {
/*  81 */           this.field_75534_e = 2;
/*     */           
/*  83 */           if (this.field_75533_d > 0)
/*     */           {
/*  85 */             spawnZombie();
/*  86 */             this.field_75533_d -= 1;
/*     */           }
/*     */           else
/*     */           {
/*  90 */             this.field_75536_c = 2;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean func_75529_b()
/*     */   {
/*  99 */     List<EntityPlayer> list = this.worldObj.playerEntities;
/* 100 */     Iterator iterator = list.iterator();
/*     */     Vec3 vec3;
/*     */     do {
/*     */       do { EntityPlayer entityplayer;
/* 104 */         do { if (!iterator.hasNext())
/*     */           {
/* 106 */             return false;
/*     */           }
/*     */           
/* 109 */           entityplayer = (EntityPlayer)iterator.next();
/*     */         }
/* 111 */         while (entityplayer.isSpectator());
/*     */         
/* 113 */         this.theVillage = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(entityplayer), 1);
/*     */       }
/* 115 */       while ((this.theVillage == null) || (this.theVillage.getNumVillageDoors() < 10) || (this.theVillage.getTicksSinceLastDoorAdding() < 20) || (this.theVillage.getNumVillagers() < 20));
/*     */       
/* 117 */       BlockPos blockpos = this.theVillage.getCenter();
/* 118 */       float f = this.theVillage.getVillageRadius();
/* 119 */       boolean flag = false;
/*     */       
/* 121 */       for (int i = 0; i < 10; i++)
/*     */       {
/* 123 */         float f1 = this.worldObj.rand.nextFloat() * 3.1415927F * 2.0F;
/* 124 */         this.field_75532_g = (blockpos.getX() + (int)(MathHelper.cos(f1) * f * 0.9D));
/* 125 */         this.field_75538_h = blockpos.getY();
/* 126 */         this.field_75539_i = (blockpos.getZ() + (int)(MathHelper.sin(f1) * f * 0.9D));
/* 127 */         flag = false;
/*     */         
/* 129 */         for (Village village : this.worldObj.getVillageCollection().getVillageList())
/*     */         {
/* 131 */           if ((village != this.theVillage) && (village.func_179866_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i))))
/*     */           {
/* 133 */             flag = true;
/* 134 */             break;
/*     */           }
/*     */         }
/*     */         
/* 138 */         if (!flag) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 144 */       if (flag)
/*     */       {
/* 146 */         return false;
/*     */       }
/*     */       
/* 149 */       vec3 = func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));
/*     */     }
/* 151 */     while (vec3 == null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */     this.field_75534_e = 0;
/* 160 */     this.field_75533_d = 20;
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   private boolean spawnZombie()
/*     */   {
/* 166 */     Vec3 vec3 = func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));
/*     */     
/* 168 */     if (vec3 == null)
/*     */     {
/* 170 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 178 */       EntityZombie entityzombie = new EntityZombie(this.worldObj);
/* 179 */       entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
/* 180 */       entityzombie.setVillager(false);
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 184 */       exception.printStackTrace();
/* 185 */       return false;
/*     */     }
/*     */     EntityZombie entityzombie;
/* 188 */     entityzombie.setLocationAndAngles(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
/* 189 */     this.worldObj.spawnEntityInWorld(entityzombie);
/* 190 */     BlockPos blockpos = this.theVillage.getCenter();
/* 191 */     entityzombie.setHomePosAndDistance(blockpos, this.theVillage.getVillageRadius());
/* 192 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private Vec3 func_179867_a(BlockPos p_179867_1_)
/*     */   {
/* 198 */     for (int i = 0; i < 10; i++)
/*     */     {
/* 200 */       BlockPos blockpos = p_179867_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
/*     */       
/* 202 */       if ((this.theVillage.func_179866_a(blockpos)) && (SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.worldObj, blockpos)))
/*     */       {
/* 204 */         return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */       }
/*     */     }
/*     */     
/* 208 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\VillageSiege.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */