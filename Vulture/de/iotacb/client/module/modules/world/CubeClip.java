package de.iotacb.client.module.modules.world;

import org.apache.commons.collections4.functors.ForClosure;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGlass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "CubeClip", description = "Clip through the spawn box in skywars on cubecraft", category = Category.WORLD)
public class CubeClip extends Module {
	
	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
		if (getMc().thePlayer.getPositionVector().getBlockPos().down().getBlock() != Blocks.air) {
			if (getMc().gameSettings.keyBindSneak.pressed) {
				getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + getClipDistanceUp(), getMc().thePlayer.posZ);
			} else {
				if (isSpaceDown()) {
					getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY - getClipDistanceDown(), getMc().thePlayer.posZ);
				}
			}
		}
		setEnabled(false);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	private boolean isSpaceDown() {
		for (int i = 0; i < getMc().thePlayer.getPositionVector().getBlockPos().getY(); i++) {
			if (getMc().thePlayer.getPositionVector().getBlockPos().down(i).getBlock() == Blocks.air) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSpaceUp() {
		for (int i = 0; i < 256 - getMc().thePlayer.getPositionVector().getBlockPos().getY(); i++) {
			if (getMc().thePlayer.getPositionVector().getBlockPos().up(i).getBlock() != Blocks.air) {
				return true;
			}
		}
		return false;
	}
	
	private int getClipDistanceDown() {
		for (int i = 0; i < getMc().thePlayer.getPositionVector().getBlockPos().getY(); i++) {
			final BlockPos pos = getMc().thePlayer.getPositionVector().getBlockPos().down(i);
			if (pos.getBlock() == Blocks.air && pos.down().getBlock() == Blocks.air && pos.down(2).getBlock() != Blocks.air) {
				return i + 1;
			}
		}
		return 0;
	}
	
	private int getClipDistanceUp() {
		for (int i = 0; i < 256 - getMc().thePlayer.getPositionVector().getBlockPos().getY(); i++) {
			final BlockPos pos = getMc().thePlayer.getPositionVector().getBlockPos().up(i);
			if (pos.getBlock() != Blocks.air && pos.up().getBlock() == Blocks.air && pos.up(2).getBlock() == Blocks.air) {
				return i + 1;
			}
		}
		return 0;
	}

}
