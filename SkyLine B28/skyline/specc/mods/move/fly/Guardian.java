package skyline.specc.mods.move.fly;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPreMotionUpdates;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Fly;
import net.minecraft.client.Mineman;


public class Guardian extends ModMode<Fly>
{

    public Guardian(Fly parent, String string) {
        super(parent, "Guardian");
    }
    
    @Override
    public void onDisable() {
    	mc.thePlayer.setSpeed(0F);
    	Timer.timerSpeed = 1.0F;
    }
    
     @EventListener
     public void onUpdate(EventMotion event) {
        if (event.getType() == EventType.POST) {
           int i;
           if (mc.gameSettings.keyBindJump.pressed) {
              if (mc.thePlayer.ticksExisted % 13 == 0) {
                 for(i = 0; i < 20; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.5E-6D, mc.thePlayer.posZ, true));
                 }
              }
              mc.thePlayer.motionY = 0.5D;
           } if (mc.gameSettings.keyBindSneak.pressed) {
              if (mc.thePlayer.ticksExisted % 13 == 0) {
                 for(i = 0; i < 20; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.5E-5D, mc.thePlayer.posZ, true));
                 }
              }
              mc.thePlayer.motionY = -0.5D;
           } if (mc.thePlayer.isMoving() && !mc.gameSettings.keyBindSneak.isPressed() && !mc.gameSettings.keyBindJump.isPressed()) {
              if (mc.thePlayer.motionY <= -0.42D) {
                 for(i = 0; i < 20; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.5E-5D, mc.thePlayer.posZ, true));
                 }
                 mc.thePlayer.motionY = 0.4D;
                 mc.thePlayer.setSpeed(5.0F);
              }
           }
        }
     }
  }
