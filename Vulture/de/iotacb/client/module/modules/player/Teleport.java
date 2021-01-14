package de.iotacb.client.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.input.MouseEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.render.Render3D;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Teleport", description = "Teleport to a position", category = Category.PLAYER)
public class Teleport extends Module {

	private BlockPos targetPosition;
	private BlockPos raycastPosition;
	
	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			raycastPosition = Client.RAYCAST_UTIL.raycastPosition(100);
			if (raycastPosition == null) return;
			Client.RENDER3D.drawBox(raycastPosition, Client.INSTANCE.getClientColor().setAlpha(100), false);
			Client.RENDER3D.drawLine(new Vec3(0, 0, 0), new Vec3(raycastPosition.getX(), raycastPosition.getY(), raycastPosition.getZ()), Client.INSTANCE.getClientColor());
		}
	}
	
	@EventTarget
	public void onMouse(MouseEvent event) {
		if (event.getMouseButton() == 0) {
			if (raycastPosition == null) return;
			targetPosition = raycastPosition;
			
			for (int i = 0; i < 5; i++) {
				getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(targetPosition.getX(), targetPosition.getY() + 99, targetPosition.getZ(), true));
				getMc().thePlayer.setPosition(targetPosition.getVec3());
				getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(targetPosition.getX(), targetPosition.getY() + 1, targetPosition.getZ(), true));
			}
		}
	}
	
}
