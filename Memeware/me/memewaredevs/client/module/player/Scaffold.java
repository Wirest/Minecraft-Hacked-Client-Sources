package me.memewaredevs.client.module.player;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.blocks.ScaffoldUtils;
import me.memewaredevs.client.util.blocks.misc.BlockInfo;
import me.memewaredevs.client.util.blocks.position.Vec3d;
import me.memewaredevs.client.util.combat.RotationUtils;
import me.memewaredevs.client.util.misc.Timer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Scaffold extends Module {

	private int block;
	private static List<Block> blockBlacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.chest,
			Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.tnt, Blocks.enchanting_table, Blocks.carpet,
			Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice,
			Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch,
			Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore,
			Blocks.iron_ore, Blocks.lapis_ore, Blocks.sand, Blocks.lit_redstone_ore, Blocks.quartz_ore,
			Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
			Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
			Blocks.wooden_button, Blocks.lever, Blocks.enchanting_table, Blocks.red_flower, Blocks.double_plant,
			Blocks.yellow_flower);
	private BlockInfo data;
	private Timer timer = new Timer();
	private int slot, newSlot, oldSlot;

	public Scaffold() {
		super("Scaffold", Keyboard.KEY_G, Category.PLAYER);
		this.slot = -1;
	}

	@Handler
	public Consumer<UpdateEvent> sexoButthole = (event) -> {
		if (event.isPre()) {
			for (block = 0; block < 45; ++block)
				mc.thePlayer.inventoryContainer.getSlot(block).getStack();
			int tempSlot = ScaffoldUtils.getBlockSlot();
			if (ScaffoldUtils.invCheck()) {
				for (int i = 9; i < 36; ++i) {
					Item item;
					if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
							|| !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack()
									.getItem()) instanceof ItemBlock)
							|| blockBlacklist.contains(((ItemBlock) item).getBlock())
							|| ((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest"))
						continue;
					ScaffoldUtils.swap(i, 7);
					break;
				}
			}
			data = null;
			slot = -1;
			if (tempSlot != -1) {
				newSlot = ScaffoldUtils.getBlockSlot();
				oldSlot = mc.thePlayer.inventory.currentItem;
				mc.thePlayer.inventory.currentItem = newSlot;
				BlockPos blockBelow1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.9, mc.thePlayer.posZ);
				mc.thePlayer.inventory.currentItem = oldSlot;
				if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
					data = ScaffoldUtils.getBlockData(blockBelow1);
					slot = tempSlot;
					if (data == null) {
						data = ScaffoldUtils.getBlockData(blockBelow1.offsetDown());
					}
					if (data == null)
						return;
					float[] rots = RotationUtils.doScaffoldRotations(new Vec3d(blockBelow1));
					mc.thePlayer.rotationYawHead = rots[0];
					mc.thePlayer.rotationPitchHead = rots[1];
					mc.thePlayer.renderYawOffset = rots[0];
					event.setYaw(rots[0]);
					event.setPitch(rots[1]);
				}
			}
		} else {
			if (this.data != null && this.timer.delay(75L) && this.slot != -1) {
				mc.thePlayer.inventory.currentItem = newSlot;
				if (mc.playerController.onPlayerRightClick3d(mc.thePlayer, mc.theWorld,
						mc.thePlayer.inventoryContainer.getSlot(36 + slot).getStack(), data.position, data.face,
						ScaffoldUtils.getVec3d(data.position, data.face))) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				}
				mc.thePlayer.inventory.currentItem = oldSlot;
				if (mc.gameSettings.keyBindJump.pressed) {
					if (!mc.thePlayer.isMoving() && !mc.gameSettings.keyBindForward.pressed) {
						mc.thePlayer.motionZ *= 0.0;
						mc.thePlayer.motionX *= 0.0;
						mc.thePlayer.motionY = 0.41999998688697815;
					}
				}
			}
		}
	};
}
