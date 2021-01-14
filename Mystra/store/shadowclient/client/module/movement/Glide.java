package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;

public class Glide extends Module{
	
	public Glide() {
		super("Glide", Keyboard.KEY_P, Category.MOVEMENT);
		
		ArrayList<String> options = new ArrayList<>();
        options.add("AAC");
        options.add("NCP");
        options.add("Matrix");
        options.add("Vanilla");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Glide Mode", this, "AAC", options));
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		String mode = Shadow.instance.settingsManager.getSettingByName("Glide Mode").getValString();
        this.setDisplayName("Glide §7| " + mode);
        
		if(mode.equalsIgnoreCase("Vanilla")) {
			double oldY = mc.thePlayer.motionY;
			float oldJ = mc.thePlayer.jumpMovementFactor;

			if((mc.thePlayer.motionY < 0.0d) && (mc.thePlayer.isAirBorne) && (!mc.thePlayer.isInWater()) && (!mc.thePlayer.isOnLadder())){
				if(!mc.thePlayer.isInsideOfMaterial(Material.lava)){
					mc.thePlayer.motionY = -.125d;
					mc.thePlayer.jumpMovementFactor *= 1.12337f;
				}
			}else {
				mc.thePlayer.motionY = oldY;
				mc.thePlayer.jumpMovementFactor = oldJ;
			}
		}
		
		if(mode.equalsIgnoreCase("AAC")) {
			if(mc.gameSettings.keyBindJump.pressed == true) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01f, mc.thePlayer.posZ);
			}
			mc.thePlayer.motionY = -0.1f;
			{
				if(mc.gameSettings.keyBindSneak.pressed==true) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.5f, mc.thePlayer.posZ);
				}
			}
		}
		
		if(mode.equalsIgnoreCase("NCP")) {
			mc.thePlayer.onGround = true;
			mc.thePlayer.capabilities.isFlying = true;
			tpRel(mc.thePlayer.motionX, mc.thePlayer.motionY=-0.0222, mc.thePlayer.motionZ);
			tpPacket(mc.thePlayer.motionX, mc.thePlayer.motionY-9, mc.thePlayer.motionZ);
		}
		
		if(mode.equalsIgnoreCase("Matrix")) {
			if (mc.thePlayer.fallDistance > 3) {
	            if (mc.thePlayer.ticksExisted % 3 == 3) mc.thePlayer.motionY = - 0.1;
	            if (mc.thePlayer.ticksExisted % 4 == 0) mc.thePlayer.motionY = - 0.2;
	        }
		}
	}
	
	public static void setSpeed(double speed) {
	      Minecraft mc = Minecraft.getMinecraft();
	      MovementInput movementInput = mc.thePlayer.movementInput;
	      float forward = movementInput.moveForward;
	      float strafe = movementInput.moveStrafe;
	      float yaw = mc.thePlayer.rotationYaw;
	      if (forward == 0.0F && strafe == 0.0F) {
	         mc.thePlayer.motionX = 0.0D;
	         mc.thePlayer.motionZ = 0.0D;
	      } else if (forward != 0.0F) {
	         if (strafe >= 1.0F) {
	            yaw += (float)(forward > 0.0F ? -45 : 45);
	            strafe = 0.0F;
	         } else if (strafe <= -1.0F) {
	            yaw += (float)(forward > 0.0F ? 45 : -45);
	            strafe = 0.0F;
	         }

	         if (forward > 0.0F) {
	            forward = 1.0F;
	         } else if (forward < 0.0F) {
	            forward = -1.0F;
	         }
	     }
	}
	
	public static void tpRel(double x, double y, double z)
	  {
	    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
	    player.setPosition(player.posX + x, player.posY + y, player.posZ + z);
	  }
	
	public static void tpPacket(double x, double y, double z)
	  {
	    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
	    player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(player.posX + x, player.posY + y, player.posZ + z, false));
	    player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY, player.posZ, false));
	    player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
	  }
	
	@Override
    public void onEnable() {
		super.onEnable();
    }

	@Override
	public void onDisable() {
		super.onDisable();
	}
}