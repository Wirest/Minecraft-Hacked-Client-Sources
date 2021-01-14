package de.iotacb.client.module.modules.movement;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Blink", description = "Pretends lagging to other players", category = Category.MOVEMENT)
public class Blink extends Module {

	private final List<Packet> packets = new ArrayList<Packet>();
	private final List<Vector3d> positions = new ArrayList<Vector3d>();
	
	private BlockPos startPos;

	private boolean doCancel;
	
	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
		if (getMc().theWorld == null)
			return;
		doCancel = true;
		startPos = getMc().thePlayer.getPosition();
		final EntityOtherPlayerMP clone = new EntityOtherPlayerMP(getMc().theWorld, getMc().thePlayer.getGameProfile());
		clone.setPositionAndRotation(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch);
		clone.rotationYawHead = getMc().thePlayer.rotationYawHead;
		getMc().theWorld.addEntityToWorld(-13371337, clone);
	}

	@Override
	public void onDisable() {
		if (getMc().theWorld == null)
			return;
		doCancel = false;
		packets.forEach(getMc().thePlayer.sendQueue::addToSendQueue);
		getMc().theWorld.removeEntityFromWorld(-13371337);
	}

	@Override
	public void onToggle() {
		packets.clear();
		positions.clear();
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.SEND) {
			event.setCancelled(doCancel);
			if (moved())
				positions.add(new Vector3d(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ));
			if (doCancel) {
				if (event.getPacket() instanceof C04PacketPlayerPosition)
					packets.add(event.getPacket());
			}
		}
	}

	@EventTarget
	public void onRender(RenderEvent event) {
		setSettingInfo(""+(int)getMc().thePlayer.getDistanceToBlockPos(startPos));
		if (event.getState() == RenderState.THREED) {
			if (positions.isEmpty())
				return;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			Client.RENDER2D.color(Client.INSTANCE.getClientColor());
			GL11.glLineWidth(1.5F);
			GL11.glBegin(GL11.GL_LINE_STRIP);
	        for (final Vector3d vec : positions) {
	        		GL11.glVertex3d(vec.x - getMc().getRenderManager().renderPosX, vec.y - getMc().getRenderManager().renderPosY, vec.z - getMc().getRenderManager().renderPosZ);
	        }
			GL11.glEnd();
			GlStateManager.enableTexture2D();
			GlStateManager.enableDepth();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.resetColor();
		}
	}

	private boolean moved() {
		return getMc().thePlayer.prevPosX != getMc().thePlayer.posX || getMc().thePlayer.prevPosY != getMc().thePlayer.posY || getMc().thePlayer.prevPosZ != getMc().thePlayer.posZ;
	}

}
