package skyline.specc.mods.player;

import java.awt.Color;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;

public class Regen extends Module
{
   public Regen() {
	   super(new ModData("Regen", 0, new Color(255, 50, 53)), ModType.OTHER);
   }
   
   @Override
   public void onDisable() {
       mc.timer.timerSpeed = 1.0F;
   }
   @Override
   public void onEnable() {
       mc.timer.timerSpeed = 1.9F;
       String message = "§4§lWARNING >>§6Do not §6MOVE§6, Eat§6 often!";
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (mc.thePlayer.onGround && (double)mc.thePlayer.getHealth() < 16.0D && mc.thePlayer.getFoodStats().getFoodLevel() > 17 && mc.thePlayer.isCollidedVertically) {
         for(int i = 0; i < 40; ++i) {
            mc.thePlayer.sendQueue.addToSendQueue(new C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
            if (Mineman.thePlayer.ticksExisted % 2 == 0) {
               mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ, false));
            }
         }
      }
   }
}

