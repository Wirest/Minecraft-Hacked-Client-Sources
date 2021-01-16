package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;

public class NoSlow extends Module {
   public NoSlow() {
      super("NoSlow", Category.Movement, false);
   }

   @EventTarget(3)
   public void onUpdate(EventUpdate event) {
      if (this.auraBlocking() || Minecraft.thePlayer.isBlocking()) {
         if (event.state == EventState.PRE) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, Minecraft.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
         } else {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
         }
      }

   }

   public boolean auraBlocking() {
      return Crystal.INSTANCE.getMods().get(Aura.class).isEnabled() && Aura.autoBlock.getBooleanValue() && Minecraft.thePlayer.inventory.getCurrentItem() != null && Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && Aura.isEntityValid(Aura.currentTarget, false);
   }
}
