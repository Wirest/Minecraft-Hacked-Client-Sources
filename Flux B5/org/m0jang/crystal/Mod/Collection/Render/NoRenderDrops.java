package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class NoRenderDrops extends Module {
   public NoRenderDrops() {
      super("NoRenderDrops", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         if (o instanceof EntityItem) {
            EntityItem i = (EntityItem)o;
            Minecraft.theWorld.removeEntity(i);
         }
      }

   }
}
