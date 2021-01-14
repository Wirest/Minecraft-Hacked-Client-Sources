package de.iotacb.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "TerrainSpeed", description = "Enables faster movement when walking on specific terrain.", category = Category.MOVEMENT)
public class TerrainSpeed extends Module {

	private double speedFactor;

	@Override
	public void onInit() {
		addValue(new Value("TerrainSpeedKeep speed", true));
		addValue(new Value("TerrainSpeedFactor", .8, new ValueMinMax(.1, 10, .1)));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		speedFactor = .2;
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (getMc().thePlayer.onGround) {
				if (Client.ENTITY_UTIL.getBlockBelowPlayer() instanceof BlockStairs || Client.ENTITY_UTIL.getBlockBelowPlayer(-.01) instanceof BlockSlab) {
					if (speedFactor < getValueByName("TerrainSpeedFactor").getNumberValue()) {
						speedFactor += .2;
					}
				}
				if (speedFactor > .2) {
					Client.MOVEMENT_UTIL.doStrafe(getValueByName("TerrainSpeedKeep speed").getBooleanValue() ? speedFactor : getValueByName("TerrainSpeedFactor").getNumberValue());
					speedFactor *= .95;
				}
				speedFactor = MathHelper.clamp_double(speedFactor, .2, getValueByName("TerrainSpeedFactor").getMinMax().getMax());
			} else {
				speedFactor = .2;
			}
			if (!Client.MOVEMENT_UTIL.isMoving()) {
				speedFactor = .2;
			}
		}
	}

}
