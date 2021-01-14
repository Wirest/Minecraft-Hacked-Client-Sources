package de.iotacb.client.module.modules.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "ChestAura", description = "Opens chest in a specific range", category = Category.WORLD)
public class ChestAura extends Module {

	@Override
	public void onInit() {
		addValue(new Value("ChestAuraRange", 4, new ValueMinMax(.1, 10, .1)));
		addValue(new Value("ChestAuraThrough walls", false));
		timer = new Timer();
	}
	
	private final List<TileEntity> openedChests = new ArrayList<TileEntity>();
	
	private TileEntity entity;
	
	private Timer timer;

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		openedChests.clear();
		entity = null;
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() ||
			Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled() ||
			Client.INSTANCE.getModuleManager().getModuleByClass(Aura.class).isEnabled() || getMc().currentScreen != null) return;
		if (event.getState() == UpdateState.PRE) {
			
			entity = getNearestChest();
			
			if (entity == null) return;
			
			final float[] rotationsCurrent = new float[] {Client.ROTATION_UTIL.serverYaw, Client.ROTATION_UTIL.serverPitch};
			final float[] rotationsInstant = Client.ROTATION_UTIL.getRotations(entity.getPos().getVec3(), false, 1);
			
			event.setRotations(rotationsInstant);
			getMc().thePlayer.setHeadRotations(rotationsInstant);
			
		} else if (event.getState() == UpdateState.POST) {
			if (entity == null) return;
			final float[] rotationsInstant = Client.ROTATION_UTIL.getRotations(entity.getPos().getVec3(), false, 1);
			
			if (!getValueByName("ChestAuraThrough walls").getBooleanValue()) {
				if (!getMc().thePlayer.canBlockBeSeen(entity.getPos())) {
					return;
				}
			}
			
			if (timer.delay(200)) {
				if (getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem(), entity.getPos(), EnumFacing.UP, new Vec3(0, 0, 0))) {
					getMc().thePlayer.swingItem();
					openedChests.add(entity);
				}
			}
			
		}
	}
	
	private float calculateRotationSpeed() {
		return (float) ThreadLocalRandom.current().nextDouble(20, 40);
	}
	
	private TileEntity getNearestChest() {
		TileEntity entity = null;
		for (TileEntity chest : getMc().theWorld.loadedTileEntityList) {
			if (getMc().thePlayer.getDistanceToBlockPos(chest.getPos()) > getValueByName("ChestAuraRange").getNumberValue() || openedChests.contains(chest)) continue;
			if (chest instanceof TileEntityChest) {
				if (entity == null) {
					entity = chest;
				} else {
					if (getMc().thePlayer.getDistanceToBlockPos(chest.getPos()) < getMc().thePlayer.getDistanceToBlockPos(entity.getPos())) {
						entity = chest;
					}
				}
			}
		}
		return entity;
	}

}
