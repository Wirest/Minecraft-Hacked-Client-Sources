package me.ihaq.iClient.modules.Player;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedBreaker extends Module {

	private int xPos;
	private int yPos;
	private int zPos;
	private static int radius = 5;

	public BedBreaker() {
		super("BedBreaker", Keyboard.KEY_NONE, Category.PLAYER, "");
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!this.isToggled()) {
			return;
		}
		for (int x = -radius; x < radius; ++x) {
			for (int y = radius; y > -radius; --y) {
				for (int z = -radius; z < radius; ++z) {
					this.xPos = (int) Minecraft.thePlayer.posX + x;
					this.yPos = (int) Minecraft.thePlayer.posY + y;
					this.zPos = (int) Minecraft.thePlayer.posZ + z;
					final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
					final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
					if (block.getBlockState().getBlock() == Block.getBlockById(26)) {
						Minecraft.thePlayer.sendQueue.addToSendQueue((Packet) new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
						Minecraft.thePlayer.sendQueue.addToSendQueue((Packet) new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
					}
				}
			}
		}
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {

	}

}
