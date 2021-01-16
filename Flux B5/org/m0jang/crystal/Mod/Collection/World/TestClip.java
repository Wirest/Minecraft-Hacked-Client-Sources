package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class TestClip extends Module {
   public double y = 3.0D;

   public TestClip() {
      super("Clip", Category.World, false);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      MovementInput var10000 = Minecraft.thePlayer.movementInput;
      if (MovementInput.sneak) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + Minecraft.thePlayer.motionX, Minecraft.thePlayer.posY - this.y, Minecraft.thePlayer.posZ + Minecraft.thePlayer.motionZ, false));
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + Minecraft.thePlayer.motionX, (double)Minecraft.theWorld.getHeight(), Minecraft.thePlayer.posZ + Minecraft.thePlayer.motionZ, false));
      }

      var10000 = Minecraft.thePlayer.movementInput;
      if (MovementInput.jump) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + Minecraft.thePlayer.motionX, Minecraft.thePlayer.posY + this.y, Minecraft.thePlayer.posZ + Minecraft.thePlayer.motionZ, false));
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + Minecraft.thePlayer.motionX, (double)Minecraft.theWorld.getHeight(), Minecraft.thePlayer.posZ + Minecraft.thePlayer.motionZ, false));
      }

   }
}
