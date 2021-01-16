package org.m0jang.crystal.Mod.Collection.Movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Chams extends Module {
   public static Value player;
   public static Value mob;

   static {
      player = new Value("Chams", Boolean.TYPE, "Player", true);
      mob = new Value("Chams", Boolean.TYPE, "Mob", false);
   }

   public Chams() {
      super("Chams", Category.Render, false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public static boolean isCorrect(EntityLivingBase entity) {
      if (player.getBooleanValue() && entity instanceof EntityPlayer && entity != Minecraft.thePlayer) {
         return true;
      } else {
         return mob.getBooleanValue() && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer);
      }
   }
}
