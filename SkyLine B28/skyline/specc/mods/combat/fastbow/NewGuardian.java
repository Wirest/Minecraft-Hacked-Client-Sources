package skyline.specc.mods.combat.fastbow;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.combat.FastBow;
import skyline.specc.mods.combat.Velocity;
import skyline.specc.utils.TimerUtils;

public class NewGuardian extends ModMode<FastBow> {
    
    TimerUtils timer = new TimerUtils();
    public NewGuardian(FastBow parent, String name) {
        super(parent, name);
    }
    
    @Override
    public void onDisable() {
	       mc.timer.timerSpeed = 1.0F;
	    }
    //TODO: Make this actually bypass. It lasts like 2 seconds before banning...
    //Make timer not as fast?
	@EventListener
    public void onTick(EventTick event) {
	      if (mc.thePlayer.isDead) {
	          mc.timer.timerSpeed = 1.0F;
	       }

	       if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
	          mc.timer.timerSpeed = 2.0F;
	          if (mc.thePlayer.getItemInUseDuration() >= 21) {
	             mc.playerController.onStoppedUsingItem(mc.thePlayer);
	          }

	          if (mc.thePlayer.ticksExisted % 1 == 0) {
	              for(int i = 0; i < 20; ++i) {
	                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-9D, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
	                 if (mc.thePlayer.ticksExisted % 2 == 0) {
	                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.5D, mc.thePlayer.posZ, false));
	                 }
	              }
	           }
	        }
	    }

	    @EventListener
	    public void onMove(EventMotion event) {
	       if (mc.timer.timerSpeed == 10.0D && !mc.thePlayer.isDead && mc.thePlayer.isEntityAlive()) {
	          event.setPosX(0.0D);
	          event.setPosZ(0.0D);
	       }

	  }
}
