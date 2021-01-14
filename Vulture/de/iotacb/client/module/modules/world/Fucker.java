package de.iotacb.client.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "Fucker", description = "Destroys blocks when in a specific range (Even through walls)", category = Category.WORLD)
public class Fucker extends Module {

	@Override
	public void onInit() {
		addValue(new Value("FuckerModes", "Bed", "Egg", "Cake"));
		addValue(new Value("FuckerRange", 4, new ValueMinMax(1, 10, 1)));
		this.timer = new Timer();
	}
	
	private Timer timer;

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
	public void onUpdateEvent(UpdateEvent event) {
		setSettingInfo(getValueByName("FuckerModes").getComboValue());
		final int range = (int) getValueByName("FuckerRange").getNumberValue();
		for (int offsetY = range; offsetY >= -range; --offsetY) {
			for (int offsetX = -range; offsetX <= range; ++offsetX) {
				for (int offsetZ = -range; offsetZ <= range; ++offsetZ) {

					final double x = getMc().thePlayer.posX + offsetX;
					final double y = getMc().thePlayer.posY + offsetY;
					final double z = getMc().thePlayer.posZ + offsetZ;

					final BlockPos blockPos = new BlockPos(x, y, z);

					final int id = Block.getIdFromBlock(blockPos.getBlock());

					if (id == currentBlockTargetId()) {
						final float[] rotations = Client.ROTATION_UTIL.getRotations(blockPos.getVec3(), false, 1);
						getMc().thePlayer.rotationYawHead = rotations[0];
						getMc().thePlayer.rotationPitchHead = rotations[1];
						if (timer.delay(200)) {
							getMc().thePlayer.swingItem();
							getMc().getNetHandler().addToSendQueue(
									new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
							getMc().getNetHandler().addToSendQueue(
									new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
						}
					}
				}
			}
		}
	}
	
	private int currentBlockTargetId() {
		int id = -1;
		switch (getValueByName("FuckerModes").getComboValue()) {
		case "Bed":
			id = Block.getIdFromBlock(Blocks.bed);
			break;
			
		case "Egg":
			id = Block.getIdFromBlock(Blocks.dragon_egg);
			break;
		
		case "Cake":
			id = Block.getIdFromBlock(Blocks.cake);
			break;
		}
		return id;
	}

}
