package org.m0jang.crystal.Mod.Collection.Movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class SelfStuck extends Module {
   public SelfStuck() {
      super("SelfStuck", Category.Movement, false);
   }

   public void onEnable() {
      Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 10.0D, Minecraft.thePlayer.posZ, false));
      this.setEnabled(false);
   }

   public void onDisable() {
      super.onDisable();
   }
}
