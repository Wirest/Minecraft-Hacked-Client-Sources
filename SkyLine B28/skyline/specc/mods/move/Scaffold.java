package skyline.specc.mods.move;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Mineman;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventSafewalk;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventKeyPress;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.utils.AimUtils;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.RotationUtils;
import skyline.specc.utils.TimerUtils;
import skyline.specc.utils.Wrapper;

public class Scaffold extends Module {
	private TimerUtils timerMotion = new TimerUtils();
	public boolean safewalk = true;

	public Scaffold() {
		super(new ModData("Scaffold", 0, new Color(165, 103, 157)), ModType.PLAYER);
	}
	
	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
	}


	boolean placing;
	private int original;
	private int block;
	private TimerUtils timer = new TimerUtils();
	private bdat bdat;
	private long lastPlace;
	private int slot;
	private List<Block> invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
            Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
            Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
            Blocks.chest, Blocks.ender_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
            Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
            Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
            Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.furnace, Blocks.lit_furnace, Blocks.crafting_table,
            Blocks.acacia_fence, Blocks.acacia_fence_gate, Blocks.birch_fence, Blocks.birch_fence_gate, Blocks.dark_oak_fence, Blocks.dark_oak_fence_gate, Blocks.jungle_fence, Blocks.jungle_fence_gate, Blocks.oak_fence, Blocks.oak_fence_gate,
            Blocks.acacia_door, Blocks.birch_door, Blocks.dark_oak_door, Blocks.iron_door, Blocks.jungle_door, Blocks.oak_door, Blocks.spruce_door,
            Blocks.rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.brewing_stand);
	
	public void onPacket(EventPacket e) {
		if (bdat == null)
			return;
		final String direction = bdat.face.name();
		if (e.getPacket() instanceof C03PacketPlayer) {
			final C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
			packet.pitch = 81.6f;
			if (direction.equalsIgnoreCase("NORTH")) {
				packet.yaw = 360.0f;
			} else if (direction.equalsIgnoreCase("SOUTH")) {
				packet.yaw = 180.0f;
			} else if (direction.equalsIgnoreCase("WEST")) {
				packet.yaw = 270.0f;
			} else if (direction.equalsIgnoreCase("EAST")) {
				packet.yaw = 90.0f;
			} else {
				packet.pitch = 90.0f;
			}
			e.setPacket(packet);
		}
	}
	@EventListener
	public void onSafewalk(EventSafewalk e) {
		e.setCancelled(true);
	}
	
	public static class bdat {
		public BlockPos position;
		public EnumFacing face;

		public bdat(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}	
	}
	public static float getDirection() {
        float yaw = Mineman.thePlayer.rotationYawHead;
        float forward = Mineman.thePlayer.moveForward;
        float strafe = Mineman.thePlayer.moveStrafing;
        yaw += (forward < 0.0F ? 180 : 0);
        if (strafe < 0.0F) {
            yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        if (strafe > 0.0F) {
            yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        return yaw * 0.017453292F;
    }
	
	public static int lpt;
	
	@EventListener
	public void JumpUpScaffold(EventMotion Event) {
		if (Mineman.getMinecraft().gameSettings.keyBindJump.pressed && mc.thePlayer.motionY > 0.0) {
			mc.thePlayer.setSpeed((float) (getBaseMoveSpeed() - 0.18));
			Event.setPitch(90F);
			Event.setYaw(98);
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		} else {
			lpt = (int) MathUtil.getRandomInRange(80F, 120F);
		}
	}
	private double getBaseMoveSpeed() {
        double baseSpeed = 0.2870;
        if (p.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = p.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
	
	public static Block getBlock(int x, int y, int z) {
		return Wrapper.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
	}
	private int getBlockSlot() {
		for (int i = 36; i < 46; i++) {
			ItemStack itemStack = p.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock)
				return i - 36;
		}
		return -1;
	}
	@EventListener
	public void onPre(EventMotion event) {
		if (event.getType() == EventType.PRE) {
			mc.timer.timerSpeed = 1.085F;
			int tempSlot = getBlockSlot();
			bdat = null;
			slot = -1;
            bdat = null;
            original = mc.thePlayer.inventory.currentItem;
			mc.thePlayer.inventory.currentItem = getBlockSlot();
			double x1 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 93.0f));
			double z1 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 93.0f));
			if (!p.isSneaking() && tempSlot != -1) {
				double xOffset = mc.thePlayer.movementInput.moveForward * 0.30 * x1
				+ mc.thePlayer.movementInput.moveStrafe * 0.30 * z1;
				double zOffset = mc.thePlayer.movementInput.moveForward * 0.30 * z1
				- mc.thePlayer.movementInput.moveStrafe * 0.30 * x1;
				double x = mc.thePlayer.posX + xOffset, y = mc.thePlayer.posY - 1, z = mc.thePlayer.posZ + zOffset;
				BlockPos blockBelow1 = new BlockPos(x, y, z);
				if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
					bdat = getbdat(blockBelow1, invalid);
					slot = tempSlot;
					if (bdat != null ) {
						event.getLocation()
								.setYaw(AimUtils.getBlockRotations(bdat.position.getX(),
										bdat.position.getY(), bdat.position.getZ(),
										bdat.face)[0]);
						event.getLocation()
								.setPitch(AimUtils.getBlockRotations(bdat.position.getX(),
										bdat.position.getY(), bdat.position.getZ(),
										bdat.face)[1]);
					}
				}
			}
		}
	}
public static bdat getbdat(final BlockPos pos, final List list) {
   return list.contains(Mineman.getMinecraft().theWorld.getBlockState(pos.add(0, -1, 0))
	.getBlock())
			? (list.contains(Mineman.getMinecraft().theWorld.getBlockState(pos.add(-1, 0, 0))
						.getBlock())
								? (list
									.contains(Mineman.getMinecraft().theWorld
										.getBlockState(pos.add(1, 0, 0))
											.getBlock())
													? (list.contains(
														Mineman.getMinecraft().theWorld
																.getBlockState(pos.add(0, 0, -1))
																	.getBlock())
																			? (list.contains(Mineman
																					.getMinecraft().theWorld
																							.getBlockState(
																								pos
																								.add(0, 0,
																										1))
																								.getBlock())
																								? null
																								: new bdat(
																								pos.add(0,
																								0,
																								1),
																				EnumFacing.NORTH))
																			: new bdat(
																		pos.add(0, 0, -1),
																	 EnumFacing.SOUTH))
										: new bdat(pos.add(1, 0, 0), EnumFacing.WEST))
							: new bdat(pos.add(-1, 0, 0), EnumFacing.EAST))
				: new bdat(pos.add(0, -1, 0), EnumFacing.UP);
	}
	@EventListener
	public void onPost(EventPlayerUpdate post) {
		post.getType();
		if ((post.getType() == EventType.POST) && (bdat != null)
			&& slot != -1) {
			mc.rightClickDelayTimer = 0;
			
			boolean dohax = p.inventory.currentItem != slot;
			if (System.currentTimeMillis() - lastPlace > lpt) {
				if (mc.playerController.func_178890_a(p, mc.theWorld, p.inventoryContainer.getSlot(36 + slot).getStack(),
					bdat.position, bdat.face, new Vec3(bdat.position.getX(),
					bdat.position.getY(), bdat.position.getZ()))) {
					mc.thePlayer.swingItem();
					lastPlace = System.currentTimeMillis();
				}
			}
		}
	}

	
	public static Mineman mc() {
	      return Mineman.getMinecraft();
	   }
	
	public static void sendPacket(Packet p) {
	      mc().getNetHandler().addToSendQueue(p);
	   }
}