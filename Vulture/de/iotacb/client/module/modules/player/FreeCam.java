package de.iotacb.client.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "FreeCam", description = "Lucid dreaming", category = Category.PLAYER)
public class FreeCam extends Module {
	
	private EntityOtherPlayerMP normalBody;
	
	private Vec3 startPos;

	@Override
	public void onInit() {
		addValue(new Value("FreeCamNoClip", true));
	}

	@Override
	public void onEnable() {
		if (getMc().thePlayer == null) return;
		
		this.startPos = getMc().thePlayer.getPositionVector();
		
		this.normalBody = new EntityOtherPlayerMP(getMc().theWorld, getMc().thePlayer.getGameProfile());
		normalBody.clonePlayer(getMc().thePlayer, true);
		
		normalBody.rotationYawHead = getMc().thePlayer.rotationYaw;
		normalBody.rotationPitchHead = getMc().thePlayer.rotationPitch;
		
		normalBody.copyLocationAndAnglesFrom(getMc().thePlayer);
		
		getMc().theWorld.addEntityToWorld(-1337, normalBody);
		
		if (getValueByName("FreeCamNoClip").getBooleanValue()) {
			getMc().thePlayer.noClip = true;
		}
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null || normalBody == null || startPos == null) return;
		
		getMc().thePlayer.setPositionAndRotation(startPos.xCoord, startPos.yCoord, startPos.zCoord, normalBody.rotationYaw, normalBody.rotationPitch);
		getMc().theWorld.removeEntityFromWorld(normalBody.getEntityId());
		
		normalBody = null;
		
		Client.MOVEMENT_UTIL.stop(true);
		
		getMc().thePlayer.noClip = false;
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getValueByName("FreeCamNoClip").getBooleanValue()) {
			getMc().thePlayer.noClip = true;
		}
		
		getMc().thePlayer.fallDistance = 0;
		
		Client.MOVEMENT_UTIL.stop(true);
		
		if (getMc().gameSettings.keyBindJump.pressed) {
			getMc().thePlayer.motionY = 1;
		} else if (getMc().gameSettings.keyBindSneak.pressed) {
			getMc().thePlayer.motionY = -1;
		}
		if (Client.MOVEMENT_UTIL.isMoving()) {
			Client.MOVEMENT_UTIL.doStrafe(1);
		}
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if(packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction)
            event.setCancelled(true);
	}

}
