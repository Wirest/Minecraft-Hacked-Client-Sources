package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;
import org.m0jang.crystal.Events.EventBBSet;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class AntiCactus extends Module {
   public AntiCactus() {
      super("AntiCactus", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onBB(EventBBSet event) {
      if (event.block instanceof BlockCactus) {
         event.boundingBox = new AxisAlignedBB((double)event.pos.getX(), (double)event.pos.getY(), (double)event.pos.getZ(), (double)(event.pos.getX() + 1), (double)(event.pos.getY() + 1), (double)(event.pos.getZ() + 1));
      }

   }
}
