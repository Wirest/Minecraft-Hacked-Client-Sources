package de.iotacb.client.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.JumpEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.movement.Speed;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Velocity", description = "Changes the effect of knockback", category = Category.COMBAT)
public class Velocity extends Module {

	private boolean jump, gotKnockback;

	@Override
	public void onInit() {
		addValue(new Value("VelocityModes", "NCP", "AAC Push", "AAC3", "AAC4", "Intave"));
		addValue(new Value("VelocityHorizontal", .8, new ValueMinMax(0, 1, .1)));
		addValue(new Value("VelocityVertical", .8, new ValueMinMax(0, 1, .1)));
	}

	@Override
	public void updateValueStates() {
		getValueByName("VelocityHorizontal").setEnabled(getValueByName("VelocityModes"), "AAC3");
		getValueByName("VelocityVertical").setEnabled(getValueByName("VelocityModes"), "AAC3");
		super.updateValueStates();
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
	public void onUpdate(UpdateEvent event) {
		setSettingInfo(getValueByName("VelocityModes").getComboValue());
		switch (getValueByName("VelocityModes").getComboValue()) {
		case "AAC Push":
			if (event.getState() != UpdateState.PRE)
				return;
			if (jump) {
				if (getMc().thePlayer.onGround)
					jump = false;
			} else {
				if (getMc().thePlayer.hurtTime > 0 && getMc().thePlayer.motionX != 0.0 && getMc().thePlayer.motionZ != 0.0)
					getMc().thePlayer.onGround = true;

				if (getMc().thePlayer.hurtResistantTime > 0)
					getMc().thePlayer.motionY -= 0.0145;
			}

			if (getMc().thePlayer.hurtResistantTime >= 19) {
				getMc().thePlayer.motionX /= 2;
				getMc().thePlayer.motionZ /= 2;
			}
			break;

		case "AAC3":
			if (getMc().thePlayer.hurtTime > 0) {
				getMc().thePlayer.motionX *= getValueByName("VelocityHorizontal").getNumberValue();
				getMc().thePlayer.motionY *= getValueByName("VelocityVertical").getNumberValue();
				getMc().thePlayer.motionZ *= getValueByName("VelocityHorizontal").getNumberValue();
			}
			break;

		case "AAC4":
			if (getMc().thePlayer.hurtTime > 0 && getMc().thePlayer.hurtTime <= 6) {
				getMc().thePlayer.motionX *= 0.4;
				getMc().thePlayer.motionZ *= 0.4;
			}
			break;

		case "Intave":
			if (getMc().thePlayer.hurtTime > 1 && getMc().thePlayer.hurtTime < 10) {
				getMc().thePlayer.motionX *= 0.75;
				getMc().thePlayer.motionZ *= 0.75;
				
				if (getMc().thePlayer.hurtTime < 4) {
					if (getMc().thePlayer.motionY > 0) {
						getMc().thePlayer.motionY *= .9;
					} else {
						getMc().thePlayer.motionY *= 1.1;
					}
				}
			}
			break;
		}

	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.RECEIVE) {
			final Packet packet = event.getPacket();
			if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
				if (getMc().thePlayer == null)
					return;

				switch (getValueByName("VelocityModes").getComboValue()) {
				case "NCP":
					event.setCancelled(true);
					break;

				}
			}
		}
	}

	@EventTarget
	public void onJump(JumpEvent event) {
		jump = true;

		if (!getMc().thePlayer.isCollidedVertically)
			event.setCancelled(true);
	}
}
