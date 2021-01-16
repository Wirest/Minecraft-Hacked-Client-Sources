package org.m0jang.crystal.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;

public class ModeUtils {
   public static boolean noslowSneak = false;
   public static Integer longJumpDist = Integer.valueOf(20);
   public static boolean espP = true;
   public static boolean espM = false;
   public static boolean espA = false;
   public static boolean tracerP = true;
   public static boolean tracerM = false;
   public static boolean tracerA = false;

   public static boolean isValidForESP(EntityLivingBase entity) {
      if (entity instanceof EntityPlayer) {
         return espP;
      } else if (!(entity instanceof EntityMob) && !(entity instanceof EntitySlime)) {
         return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && espA;
      } else {
         return espM;
      }
   }

   public static boolean isValidForTracers(EntityLivingBase entity) {
      if (entity.isInvisible()) {
         return false;
      } else if (entity instanceof EntityPlayer && !entity.isPlayerSleeping()) {
         return tracerP;
      } else if (!(entity instanceof EntityMob) && !(entity instanceof EntitySlime)) {
         return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && tracerA;
      } else {
         return tracerM;
      }
   }

   public static boolean isValidForAura(Entity entity) {
      Minecraft.getMinecraft();
      if (entity != Minecraft.thePlayer && !Crystal.INSTANCE.friendManager.isFriend(entity.getName()) && entity.isEntityAlive()) {
         if (Aura.team.getBooleanValue()) {
            FriendManager var10000 = Crystal.INSTANCE.friendManager;
            if (FriendManager.isTeam((EntityLivingBase)entity)) {
               return false;
            }
         }

         if (!Aura.AttackInvisible.getBooleanValue() && entity.isInvisible()) {
            return false;
         } else if (entity instanceof EntityPlayer) {
            return Aura.AttackPlayer.getBooleanValue();
         } else if (!(entity instanceof EntityMob) && !(entity instanceof EntitySlime)) {
            return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat) && Aura.AttackMonster.getBooleanValue();
         } else {
            return Aura.AttackAnimal.getBooleanValue();
         }
      } else {
         return false;
      }
   }
}
