package me.ihaq.iClient.modules.Player;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.Event;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventMouse;
import me.ihaq.iClient.event.events.EventPacketSend;
import me.ihaq.iClient.event.events.EventPostMotionUpdates;
import me.ihaq.iClient.event.events.EventPreMotionUpdates;
import me.ihaq.iClient.event.events.EventRender2D;
import me.ihaq.iClient.event.events.EventSafe;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.EntityUtils;
import me.ihaq.iClient.utils.MathUtils;
import me.ihaq.iClient.utils.R2DUtils;
import me.ihaq.iClient.utils.timer.TimeUtil;
import me.ihaq.iClient.utils.values.BooleanValue;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

	private BooleanValue swing = new BooleanValue(this, "Swing", "tabgui", Boolean.valueOf(true));
	private BooleanValue safewalk = new BooleanValue(this, "SafeWalk", "arraylist", Boolean.valueOf(true));
	private TimeUtil timer;
	private List<Block> invalid;
	private MathUtils.BlockData blockData;
	private boolean placing;
	private int slot;
	private float lastYaw;
	private float lastPitch;
	private int startSlot;

	public Scaffold() {
		super("Scaffold", Keyboard.KEY_NONE, Category.PLAYER, "");
		this.invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava,
				Blocks.flowing_lava, Blocks.anvil,Blocks.chest,Blocks.ender_chest,Blocks.enchanting_table);
		this.timer = new TimeUtil();
		this.blockData = null;
	}

	@Override
	public void onEnable() {
		this.startSlot = mc.thePlayer.inventory.currentItem;
	}

	@Override
	public void onDisable() {
		mc.thePlayer.inventory.currentItem = this.startSlot;
	}

	private int getBlockSlot() {
		for (int i = 36; i < 45; ++i) {
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
				return i - 36;
			}
		}
		return -1;
	}

	public static MathUtils.BlockData getBlockData(final BlockPos pos, final List list) {
		return list.contains(Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()) ? (list.contains(
				Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())
						? (list.contains(Scaffold.mc.theWorld
								.getBlockState(pos.add(1, 0, 0)).getBlock())
										? (list.contains(
												Scaffold.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())
														? (list.contains(
																Scaffold.mc.theWorld.getBlockState(pos.add(0, -1, 0))
																		.getBlock()) ? null
																				: new MathUtils.BlockData(
																						pos.add(0, -1, 0),
																						EnumFacing.UP))
														: new MathUtils.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST))
										: new MathUtils.BlockData(pos.add(1, 0, 0), EnumFacing.WEST))
						: new MathUtils.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH))
				: new MathUtils.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
	}

	@EventTarget
	public void onPreMotion(EventPreMotionUpdates event) {
		if(!this.isToggled()){
			return;
		}
		int tempSlot = getBlockSlot();
		this.blockData = null;
		this.slot = -1;
		if (!mc.thePlayer.isSneaking() && tempSlot != -1) {
			double x2 = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
			double z2 = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
			double xOffset = this.mc.thePlayer.movementInput.moveForward * 0.4 * x2
					+ this.mc.thePlayer.movementInput.moveStrafe * 0.4 * z2;
			double zOffset = this.mc.thePlayer.movementInput.moveForward * 0.4 * z2
					- this.mc.thePlayer.movementInput.moveStrafe * 0.4 * x2;
			double x = mc.thePlayer.posX + xOffset, y = mc.thePlayer.posY - 1, z = mc.thePlayer.posZ + zOffset;
			BlockPos blockBelow1 = new BlockPos(x, y, z);
			if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
				this.blockData = this.getBlockData(blockBelow1, invalid);
				this.slot = tempSlot;
				if (this.blockData != null) {
					this.lastPitch = Minecraft.thePlayer.rotationPitch;
					this.lastYaw = Minecraft.thePlayer.rotationYaw;
					final float[] values = EntityUtils.getBlockRotations(this.blockData.position.getX(),
							this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
					Minecraft.thePlayer.rotationYaw = values[0];
					Minecraft.thePlayer.rotationPitch = values[1];
				}

			}
		}
	}

	@EventTarget
	public void onPostMotion(EventPostMotionUpdates event) {
		if(!this.isToggled()){
			return;
		}
		if (this.blockData != null && this.timer.hasReached(75L) && this.slot != -1) {
			Scaffold.mc.rightClickDelayTimer = 3;
			//mc.thePlayer.inventory.setInventorySlotContents(index, stack);
			mc.thePlayer.inventory.currentItem = this.slot;
			final boolean dohax = Minecraft.thePlayer.inventory.currentItem != this.slot;
			if (dohax) {
				Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
			}
			if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
					Minecraft.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), this.blockData.position,
					this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(),
							this.blockData.position.getZ()))) {
				Minecraft.thePlayer.swingItem();
				if (dohax) {
					Minecraft.thePlayer.sendQueue
							.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
				}
				if (this.blockData != null) {
					if (this.timer.hasReached(75L)) {
						mc.rightClickDelayTimer = 0;
						if (mc.gameSettings.keyBindJump.isKeyDown()) {
							mc.thePlayer.motionY = 0.42;
							if (timer.hasReached(1500)) {
								mc.thePlayer.motionY = -0.28;
								timer.reset();
								if (timer.hasReached(2L)) {
									mc.thePlayer.motionY = 0.42;
								}
							}
						}
					}
				}
			}
		}
	}

	@EventTarget
	public void onPacketSend(EventPacketSend event){
		if(!this.isToggled()){
			return;
		}
		if (event.getPacket() instanceof C03PacketPlayer) {
			final C03PacketPlayer packet = (C03PacketPlayer) ((EventPacketSend) event).getPacket();
			if (packet.getRotating() && this.blockData != null) {
				final float[] values2 = EntityUtils.getBlockRotations(this.blockData.position.getX(),
						this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face);
				packet.yaw = values2[0];
				packet.pitch = values2[1];
				Minecraft.thePlayer.rotationYaw = this.lastYaw;
				Minecraft.thePlayer.rotationPitch = this.lastPitch;
			}
		}
		
	}

	@EventTarget
	public void onUdate(EventUpdate event) {
		if(!this.isToggled()){
			return;
		}
		if (mc.thePlayer.swingProgress <= 0.0f && this.swing.getValue()) {
			if (!mc.thePlayer.isEating()) {
				mc.thePlayer.swingProgressInt = 5;
			}
		}
	}

	@EventTarget
	public void onSafe(EventSafe event) {
		if(!this.isToggled()){
			return;
		}
		if (this.safewalk.getValue()) {
			event.setCancelled(true);
		}
	}

	@EventTarget
	public void onMouse(EventMouse event) {
		if(!this.isToggled()){
			return;
		}
		event.getKey();
		mc.gameSettings.keyBindJump.getKeyCode();
	}

	@EventTarget
	public void onRender(EventRender2D event) {
		if(!this.isToggled()){
			return;
		}
		int n = 0;
		try {
			for (int k = 0; k < 9; ++k) {
				final ItemStack stackInSlot3 = mc.thePlayer.inventory.getStackInSlot(k);
				if (stackInSlot3 != null && stackInSlot3.getItem() instanceof ItemBlock) {
					n += stackInSlot3.stackSize;
				}
			}
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
		Gui.drawCenteredString(mc.getMinecraft().fontRendererObj, (n > 10) ? ("§2" + n) : ("§4" + n),
				(int) (new ScaledResolution(mc).getScaledWidth() / 2
						- mc.fontRendererObj.getStringWidth(new StringBuilder(String.valueOf(n)).toString()) / 2.0f),
				new ScaledResolution(mc).getScaledHeight() / 2 - 100, -1);
		setMode(": \u00A7f"+n);
	}

}
