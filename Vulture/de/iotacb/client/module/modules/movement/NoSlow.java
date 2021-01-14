package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.SlowDownEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "NoSlow", description = "Disables or lowers the movement slowdown when using items", category = Category.MOVEMENT)
public class NoSlow extends Module {
	
    private Timer packetTimer;
	
    private boolean doSendPackets;
	
	public float strafeFactor, forwardFactor;

	@Override
	public void onInit() {
		addValue(new Value("NoSlowStrafe factor", 1F, new ValueMinMax(.1, 1, .05)));
		addValue(new Value("NoSlowForward factor", 1F, new ValueMinMax(.1, 1, .05)));
		addValue(new Value("NoSlowPacket", true));
		packetTimer = new Timer();
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
		strafeFactor = (float) getValueByName("NoSlowStrafe factor").getNumberValue();
		forwardFactor = (float) getValueByName("NoSlowForward factor").getNumberValue();
		final Aura aura = (Aura) Client.INSTANCE.getModuleManager().getModuleByClass(Aura.class);
		if (Client.MOVEMENT_UTIL.isMoving() && (getValueByName("NoSlowPacket").getBooleanValue() && (getMc().thePlayer.isBlocking() || (aura.isEnabled() && aura.isBlocking())))) {
			if (event.getState() == UpdateState.PRE) {
				getMc().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			} else if (event.getState() == UpdateState.POST) {
				getMc().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.inventory.getCurrentItem()));
			}
		}
	}

}
