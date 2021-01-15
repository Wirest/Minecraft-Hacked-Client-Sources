/*    */ package net.minecraft.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import net.minecraft.entity.boss.EntityDragon;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.entity.monster.EntityBlaze;
/*    */ import net.minecraft.entity.monster.EntityCaveSpider;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ import net.minecraft.entity.monster.EntityEnderman;
/*    */ import net.minecraft.entity.monster.EntityEndermite;
/*    */ import net.minecraft.entity.monster.EntityGhast;
/*    */ import net.minecraft.entity.monster.EntityGiantZombie;
/*    */ import net.minecraft.entity.monster.EntityGuardian;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.entity.monster.EntityMagmaCube;
/*    */ import net.minecraft.entity.monster.EntityPigZombie;
/*    */ import net.minecraft.entity.monster.EntitySilverfish;
/*    */ import net.minecraft.entity.monster.EntitySkeleton;
/*    */ import net.minecraft.entity.monster.EntitySlime;
/*    */ import net.minecraft.entity.monster.EntitySnowman;
/*    */ import net.minecraft.entity.monster.EntitySpider;
/*    */ import net.minecraft.entity.monster.EntityWitch;
/*    */ import net.minecraft.entity.monster.EntityZombie;
/*    */ import net.minecraft.entity.passive.EntityBat;
/*    */ import net.minecraft.entity.passive.EntityChicken;
/*    */ import net.minecraft.entity.passive.EntityCow;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.entity.passive.EntityMooshroom;
/*    */ import net.minecraft.entity.passive.EntityOcelot;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.entity.passive.EntityRabbit;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ import net.minecraft.entity.passive.EntitySquid;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.entity.passive.EntityWolf;
/*    */ 
/*    */ public class EntitySpawnPlacementRegistry
/*    */ {
/* 40 */   private static final HashMap<Class, EntityLiving.SpawnPlacementType> ENTITY_PLACEMENTS = ;
/*    */   
/*    */   public static EntityLiving.SpawnPlacementType getPlacementForEntity(Class entityClass)
/*    */   {
/* 44 */     return (EntityLiving.SpawnPlacementType)ENTITY_PLACEMENTS.get(entityClass);
/*    */   }
/*    */   
/*    */   static
/*    */   {
/* 49 */     ENTITY_PLACEMENTS.put(EntityBat.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 50 */     ENTITY_PLACEMENTS.put(EntityChicken.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 51 */     ENTITY_PLACEMENTS.put(EntityCow.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 52 */     ENTITY_PLACEMENTS.put(EntityHorse.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 53 */     ENTITY_PLACEMENTS.put(EntityMooshroom.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 54 */     ENTITY_PLACEMENTS.put(EntityOcelot.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 55 */     ENTITY_PLACEMENTS.put(EntityPig.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 56 */     ENTITY_PLACEMENTS.put(EntityRabbit.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 57 */     ENTITY_PLACEMENTS.put(EntitySheep.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 58 */     ENTITY_PLACEMENTS.put(EntitySnowman.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 59 */     ENTITY_PLACEMENTS.put(EntitySquid.class, EntityLiving.SpawnPlacementType.IN_WATER);
/* 60 */     ENTITY_PLACEMENTS.put(EntityIronGolem.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 61 */     ENTITY_PLACEMENTS.put(EntityWolf.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 62 */     ENTITY_PLACEMENTS.put(EntityVillager.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 63 */     ENTITY_PLACEMENTS.put(EntityDragon.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 64 */     ENTITY_PLACEMENTS.put(EntityWither.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 65 */     ENTITY_PLACEMENTS.put(EntityBlaze.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 66 */     ENTITY_PLACEMENTS.put(EntityCaveSpider.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 67 */     ENTITY_PLACEMENTS.put(EntityCreeper.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 68 */     ENTITY_PLACEMENTS.put(EntityEnderman.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 69 */     ENTITY_PLACEMENTS.put(EntityEndermite.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 70 */     ENTITY_PLACEMENTS.put(EntityGhast.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 71 */     ENTITY_PLACEMENTS.put(EntityGiantZombie.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 72 */     ENTITY_PLACEMENTS.put(EntityGuardian.class, EntityLiving.SpawnPlacementType.IN_WATER);
/* 73 */     ENTITY_PLACEMENTS.put(EntityMagmaCube.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 74 */     ENTITY_PLACEMENTS.put(EntityPigZombie.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 75 */     ENTITY_PLACEMENTS.put(EntitySilverfish.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 76 */     ENTITY_PLACEMENTS.put(EntitySkeleton.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 77 */     ENTITY_PLACEMENTS.put(EntitySlime.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 78 */     ENTITY_PLACEMENTS.put(EntitySpider.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 79 */     ENTITY_PLACEMENTS.put(EntityWitch.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/* 80 */     ENTITY_PLACEMENTS.put(EntityZombie.class, EntityLiving.SpawnPlacementType.ON_GROUND);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntitySpawnPlacementRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */