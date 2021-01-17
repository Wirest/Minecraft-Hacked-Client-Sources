package me.ihaq.iClient.modules.Player;

import java.awt.List;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import net.java.games.input.Mouse;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventPostMotionUpdates;
import me.ihaq.iClient.event.events.EventTick;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import me.ihaq.iClient.utils.BlockUtils;
import me.ihaq.iClient.utils.ChestUtil;
import me.ihaq.iClient.utils.timer.TimerUtils;

public class ChestSteal extends Module {

	int delay = 20;

	public ChestSteal() {
		super("ChestSteal", Keyboard.KEY_NONE, Category.PLAYER, "");
	}

	@EventTarget
	public void onUpdate(EventPostMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		if ((mc.thePlayer.openContainer != null) && ((mc.thePlayer.openContainer instanceof ContainerChest))) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;

			for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
				if ((container.getLowerChestInventory().getStackInSlot(i) != null)
						&& (TimerUtils.hasReached((delay)))) {
					mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
					TimerUtils.rt();
				}

			}
			if (ChestUtil.isChestEmpty(container)) {
				mc.thePlayer.closeScreen();
			}
		}
	}

}
