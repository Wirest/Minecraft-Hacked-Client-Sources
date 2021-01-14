package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.*;
import splash.utilities.math.MathUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.time.Stopwatch;

public class Phase extends Module {
	private ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.ARIS, this);
	private enum Mode {
		ARIS, FALCON, VANILLA
	}
	private double distance = 2.5;
	private Stopwatch timer;
	private int moveUnder;
	
	public Phase() {
		super("Phase", "Lets you go through blocks.", ModuleCategory.MOVEMENT);
		timer = new Stopwatch();
	}
	public static String getDirection() {
		return Minecraft.getMinecraft().thePlayer.getHorizontalFacing().getName();
	}
	public static void setMoveSpeedAris(EventMove event, double speed) {

		/*
		 * Okay first off, lemme get one goddamn thing straight, everyone says ``OH U SKID ARIS SPEED CODE`` but yet again, EVERYONE FUCKING USES IT
		 *
		 * If you can name 1 person with a hypixel damage fly/bhop or a nocheatplus bhop faster than 1.2% vanilla bhopping that isnt using aris speed code, id be genuinly impressed
		 * */

		double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward,
				strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
				yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {

		} else {
			if (forward != 0) {
				if (strafe > 0) {
					yaw += forward > 0 ? -45 : 45;
				} else if (strafe < 0) {
					yaw += forward > 0 ? 45 : -45;
				}
				strafe = 0;
				if (forward > 0) {
					forward = 1;
				} else if (forward < 0) {
					forward = -1;
				}
			}
			if (Minecraft.getMinecraft().thePlayer.isMoving()) {
				event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0F))
						+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9F)));
				event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0F))
						- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9F)));
			}
		}
	}

	@Collect
	public void onTick(EventTick eventTick) {
		if(mode.getValue() == Mode.VANILLA) {
			if (mc.thePlayer != null && moveUnder == 1) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
				moveUnder = 0;
			}
			if (mc.thePlayer != null && moveUnder == 1488) {
				double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
				double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
				double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
				double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
				moveUnder = 0;
			}
		}
	}

	public boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(
				mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int y = MathHelper.floor_double(
					mc.thePlayer.boundingBox.minY + 1.0D); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY)
					+ 2; ++y) {
				for (int z = MathHelper.floor_double(
						mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ)
						+ 1; ++z) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block != null && !(block instanceof BlockAir)) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
								mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						if (block instanceof BlockHopper) {
							boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						}

						if (boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox))
							return true;
					}
				}
			}
		}
		return false;
	}

	@Collect
	public void onBB(EventBoundingBox event) {
		if (mode.getValue().equals(Mode.VANILLA)) {
			if (mc.thePlayer.isCollidedHorizontally && !isInsideBlock()) {
				double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
				double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
				double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
				double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
				event.setBoundingBox(null);
				mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				moveUnder = 69;
			}
			if (isInsideBlock()) event.setBoundingBox(null);
		}


		if ((mode.getValue().equals(Mode.ARIS))
				&& (isInsideBlock() && mc.gameSettings.keyBindJump.pressed || !isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY)) {
			if (mc.thePlayer.isMoving()) {
				mc.thePlayer.setSpeed(0.625F);
			}
			if (mc.thePlayer.isCollidedVertically) {
				mc.thePlayer.motionY = 0;
			}
			event.setBoundingBox(null);
		} else {
			mc.thePlayer.setSpeed(mc.thePlayer.isMoving() ? .15 : 0);
		}

		if (mode.getValue().equals(Mode.FALCON)) {
			if (isInsideBlock() && !Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown()) {
				if (event.getBlockPos().getY() > mc.thePlayer.getEntityBoundingBox().minY - 0.4) {
					if (event.getBlockPos().getY() < mc.thePlayer.getEntityBoundingBox().maxY + 1.0) {
						event.setBoundingBox(null);
					}
				}
			}
			event.setBoundingBox(null);
		}
	}

	@Collect
	public void onCollision(EventBlockPush e) {
		if (mode.getValue().equals(Mode.FALCON) && isInsideBlock() || mode.getValue().equals(Mode.ARIS) || mode.getValue().equals(Mode.VANILLA)) {
			e.setCancelled(true);
			if (mc.thePlayer.isCollidedVertically) {
				mc.thePlayer.motionY = 0;
			}
		}
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (mode.getValue().equals(Mode.VANILLA) && mc.gameSettings.keyBindSneak.isPressed() && !isInsideBlock()) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
			moveUnder = 2;
		}
	}
	
	@Collect
	public void onRenderInside(EventRenderInsideBlock e) {
		e.setCancelled(true);
	}
	
	@Collect
	public void onMove(EventMove event) {
		if(mode.getValue().equals(Mode.VANILLA)) {
			if (isInsideBlock()) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					event.setY(mc.thePlayer.motionY = 1.2);
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					event.setY(mc.thePlayer.motionY = -1.2);
				} else {
					event.setY(mc.thePlayer.motionY = 0.0);
				}
				event.setMoveSpeed(0.3);
			}
		}

		if (isInsideBlock() && !mode.getValue().equals(Mode.ARIS)) {
			mc.thePlayer.motionY = 0;
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY = 0.16;
				event.setY(mc.thePlayer.motionY + .4);
			} else {
				if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.motionY = -0.16;
					event.setY(mc.thePlayer.motionY - .1);
				} else {
					mc.thePlayer.motionY = 0;
					event.setY(0.0);
				}
			}
			setMoveSpeedAris(event, 0.25);
		}
	} 
	
	@Collect
	public void onPacketReceive(EventPacketReceive event) {

		if (event.getReceivedPacket() instanceof S02PacketChat) {
			S02PacketChat packet = (S02PacketChat) event.getReceivedPacket();
			if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
				event.setCancelled(true);
			}
		}

		if(mode.getValue().equals(Mode.VANILLA)) {
			if (event.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getReceivedPacket();
				packet.setPitch(mc.thePlayer.rotationPitch);
				packet.setYaw(mc.thePlayer.rotationYaw);

				if (moveUnder == 2) {
					moveUnder = 1;
				}
			}
		}

		if (mode.getValue().equals(Mode.VANILLA) && event.getReceivedPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
			moveUnder = 1;
		}

		if (mode.getValue().equals(Mode.VANILLA) && event.getReceivedPacket() instanceof S08PacketPlayerPosLook && moveUnder == 69) {
			moveUnder = 1488;
		}

		if (mode.getValue().equals(Mode.FALCON)) {
			if (event.getReceivedPacket() instanceof S02PacketChat) {
				S02PacketChat packet = (S02PacketChat) event.getReceivedPacket();
				if (packet.getChatComponent().getUnformattedText().contains("border")) {
					event.setCancelled(true);
				}
			}
		}
		if (event.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getReceivedPacket();
			packet.setPitch(mc.thePlayer.rotationPitch);
			packet.setYaw(mc.thePlayer.rotationYaw);

			if (moveUnder == 2) {
				moveUnder = 1;
			}
		}
	}

	@Collect
	public void onTick(EventPlayerUpdate event) {
		if (mode.getValue().equals(Mode.VANILLA)) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
			moveUnder = 2;
		}


		if (event.getStage().equals(Stage.PRE)) { 
			if (mode.getValue().equals(Mode.FALCON)) {
				if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - .5, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					moveUnder = 2;
				}
				if (moveUnder == 1) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - .1, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					moveUnder = 0;
					if (mc.thePlayer.isCollidedHorizontally) {
						if (mc.gameSettings.keyBindSprint.isKeyDown()) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, true));
						}
					}
				}
			}
		} else {
			if (mode.getValue().equals(Mode.ARIS)) {
				if (!event.getStage().equals(Stage.PRE)) {

					if (mc.thePlayer.isSneaking()) {
						if (!mc.thePlayer.isOnLadder()) {
							mc.thePlayer.setSpeed(mc.thePlayer.isCollidedHorizontally ? .3 : .05);
							mc.thePlayer.boundingBox.offset(
									1.2 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)), 0.0,
									1.2 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)));

							if (mc.getCurrentServerData() != null
									&& !mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
								double offset = 1.35;
								Number playerYaw = mc.thePlayer.getDirection();
								mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
										mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
										mc.thePlayer.posY + .3,
										mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
								mc.thePlayer.setPositionAndUpdate(
										mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
										mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
							} else {
								double offset = 1.2;
								Number playerYaw = mc.thePlayer.getDirection();
								mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
										mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
										mc.thePlayer.posY + .3,
										mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
								mc.thePlayer.setPositionAndUpdate(
										mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
										mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
								event.setX(mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset);
								event.setZ(mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
							}
						}

					}
				} else {
					event.setYaw(event.getYaw() + MathUtils.getRandomInRange(-.5F, .5F));
				}
			} 
		}
	}
}
