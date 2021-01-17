package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.events.EventPreMotionUpdates;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.BlockHelper;
import me.ihaq.iClient.utils.BlockUtils;
import me.ihaq.iClient.utils.timer.TimerUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "");
	}

	

}
