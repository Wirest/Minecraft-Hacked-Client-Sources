package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FastUse extends Module {
   public boolean canBoost = false;

   public FastUse() {
      super("FastUse", Category.Misc, false);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.getItemInUseDuration() == 16) {
         this.canBoost = true;
      }

      if (event.state == EventState.PRE && Minecraft.thePlayer.onGround && this.canBoost && !(Minecraft.thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
         this.canBoost = false;

         for(int i = 0; i < 20; ++i) {
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
         }
      }

   }
}
