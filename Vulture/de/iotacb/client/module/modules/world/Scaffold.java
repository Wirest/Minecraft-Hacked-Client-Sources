package de.iotacb.client.module.modules.world;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.JumpEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.MoveEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Scaffold", description = "Automatically builds blocks when moving", category = Category.WORLD)
public class Scaffold extends Module {

	private Timer placeDelay;

	private Object[] placeInfo;

	private int slot;

	private int stage;
	
	private boolean canPlace;

	@Override
	public void onInit() {
		this.placeDelay = new Timer();

		addValue(new Value("ScaffoldSprint", true));
		addValue(new Value("ScaffoldSneak", true));
		addValue(new Value("ScaffoldSilent", true));
		addValue(new Value("ScaffoldJitter", true));
		addValue(new Value("ScaffoldTimer", false));
		addValue(new Value("ScaffoldNo swing", false));
		addValue(new Value("ScaffoldDisplay block count", true));
		addValue(new Value("ScaffoldDelay", 200, new ValueMinMax(0, 2000, 1)));
		addValue(new Value("ScaffoldTimer speed", 2, new ValueMinMax(.1, 20, .1)));
		addValue(new Value("ScaffoldJitter factor", .1, new ValueMinMax(.1, 1, .1)));
		addValue(new Value("ScaffoldModes", "NCP", "AAC", "Redesky", "Hypixel"));
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("ScaffoldTimer speed").setEnabled(getValueByName("ScaffoldTimer").isEnabled());
		super.updateValueStates();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null)
			return;
		Client.MOVEMENT_UTIL.doStrafe(.2);
		canPlace = false;
		getMc().gameSettings.keyBindSneak.pressed = false;
		if (slot != getMc().thePlayer.inventory.currentItem) {
			getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
		}
	}

	@Override
	public void onToggle() {
		placeInfo = null;
		stage = 0;
		getMc().timer.timerSpeed = 1;
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled()) {
			if (getMc().gameSettings.keyBindJump.pressed) {
				return;
			}
		}
		setSettingInfo(getValueByName("ScaffoldModes").getComboValue());
		if (event.getState() == UpdateState.PRE) {
			
			if (getMc().thePlayer.getHeldItem() == null) {
				if (getValueByName("ScaffoldSilent").getBooleanValue()) {
					if (!Client.INVENTORY_UTIL.hasBlockInHotbar()) {
						placeInfo = null;
						return;
					}
				} else {
					placeInfo = null;
					return;
				}
			}
			
			placeInfo = getPlacingPosition(getMc().thePlayer.getPositionVector().getBlockPos().add(0, -1, 0), 1);
			canPlace = false;

			if (placeInfo == null)
				return;

			final Vec3 blockSidePosition = getBlockSide((BlockPos) placeInfo[0], (EnumFacing) placeInfo[1]);
			float pitch = getMc().thePlayer.onGround ? 80F : 90F;

			final float[] rotations = Client.ROTATION_UTIL.getRotations(blockSidePosition, false, 1);
			
			switch (getValueByName("ScaffoldModes").getComboValue()) {
			case "NCP":
				if (rotations == null)
					return;

				getMc().thePlayer.rotationYawHead = rotations[0];
				getMc().thePlayer.renderYawOffset = rotations[0];
				getMc().thePlayer.rotationPitchHead = rotations[1];
				
				canPlace = rayTrace(rotations[0], rotations[1]);

				event.setRotation(rotations[0], rotations[1]);
				break;
			case "Hypixel":
				if (rotations == null)
					return;

				getMc().thePlayer.rotationYawHead = rotations[0];
				getMc().thePlayer.renderYawOffset = rotations[0];
				getMc().thePlayer.rotationPitchHead = rotations[1];
				
				canPlace = rayTrace(rotations[0], 80);

				event.setPitch(80);
				break;
			case "AAC":
				final float yaw = (float) (getAACYaw() + ThreadLocalRandom.current().nextDouble(-1, 1));
				pitch = 81;
				pitch += ThreadLocalRandom.current().nextDouble(-1, 1);
				getMc().thePlayer.rotationYawHead = yaw;
				getMc().thePlayer.renderYawOffset = yaw;
				getMc().thePlayer.rotationPitchHead = pitch;
				
				canPlace = rayTrace(yaw, pitch);
				
				event.setRotation(yaw, pitch);
				break;

			case "Redesky":
				final float yaw2 = (float) (getAACYaw() + ThreadLocalRandom.current().nextDouble(-1, 1));
				pitch += ThreadLocalRandom.current().nextDouble(-1, 1);

				getMc().thePlayer.rotationYawHead = yaw2;
				getMc().thePlayer.renderYawOffset = yaw2;
				getMc().thePlayer.rotationPitchHead = pitch;
				
				canPlace = rayTrace(yaw2, pitch);
				
				event.setRotation(yaw2, pitch);
				break;
			}
		}
	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled()) {
			if (getMc().gameSettings.keyBindJump.pressed) {
				return;
			}
		}
		if (placeInfo == null) {
			placeDelay.reset();
			return;
		}

		ItemStack stack = getMc().thePlayer.getHeldItem();

		int blockSlot = -1;

		if (getValueByName("ScaffoldSneak").getBooleanValue()) {
			getMc().gameSettings.keyBindSneak.pressed = false;
		}
		if (getValueByName("ScaffoldSilent").getBooleanValue()) {
			if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
				blockSlot = Client.INVENTORY_UTIL.findBlock(true);

				if (blockSlot == -1)
					return;

				getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
				stack = getMc().thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
			}
		}
		if (Client.ENTITY_UTIL.getBlockBelowPlayer() == Blocks.air) {
			if (getValueByName("ScaffoldTimer").getBooleanValue() && !getMc().gameSettings.keyBindJump.pressed) {
				getMc().timer.timerSpeed = (float) getValueByName("ScaffoldTimer speed").getNumberValue();
			}
		} else {
			if (getValueByName("ScaffoldTimer").getBooleanValue()) {
				getMc().timer.timerSpeed = 1;
			}
		}
		if (!hasNeighbour(getMc().thePlayer.getPositionVector().getBlockPos().add(0, -1, 0))) return;
		if ((placeDelay.delay2((long) getValueByName("ScaffoldDelay").getNumberValue())) && canPlace) {
			if (getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, stack, (BlockPos) placeInfo[0], (EnumFacing) placeInfo[1], blockDataToVec3((BlockPos) placeInfo[0], (EnumFacing) placeInfo[1]))) {
				if (!getValueByName("ScaffoldNo swing").getBooleanValue()) {
					getMc().thePlayer.swingItem();
				} else {
					getMc().getNetHandler().addToSendQueue(new C0APacketAnimation());
				}

				if (getValueByName("ScaffoldSneak").getBooleanValue()) {
					getMc().gameSettings.keyBindSneak.pressed = true;
				}
			}
			placeDelay.reset();
		}
		
		if (Client.MOVEMENT_UTIL.isMoving() && getValueByName("ScaffoldJitter").getBooleanValue()) {
			final double jitter = ThreadLocalRandom.current().nextDouble(-1, 1) * (getValueByName("ScaffoldJitter factor").getNumberValue() / 10.0);
			getMc().thePlayer.motionX += jitter;
			getMc().thePlayer.motionZ -= jitter;
		}
	}

	@EventTarget
	public void onMove(MoveEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled()) {
			if (getMc().gameSettings.keyBindJump.pressed) {
				return;
			}
		}
		if (!getValueByName("ScaffoldModes").isCombo("Redesky") || placeInfo == null)
			return;

		if (stage > 10)
			stage = 0;
		stage++;
		if (Client.MOVEMENT_UTIL.isMoving()) {
			if (stage > 6) {
				Client.MOVEMENT_UTIL.doStrafe(ThreadLocalRandom.current().nextDouble(.4, .6));
			} else {
				Client.MOVEMENT_UTIL.doStrafe(0);
			}
		} else {
			stage = 0;
		}
	}

	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.TWOD) {
			if (!getValueByName("ScaffoldDisplay block count").getBooleanValue())
				return;
			final String blockCount = "Blocks: " + Client.INSTANCE.getClientColorCode() + getBlocksInHotbar();
			final double width = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(blockCount) + 5;
			final double height = Client.INSTANCE.getFontManager().getDefaultFont().getHeight(blockCount) + 5;
			final double posX = event.getSr().getScaledWidth() / 2 - width / 2;
			final double posY = event.getSr().getScaledHeight() - height / 2 - 80;
			Client.RENDER2D.rect(posX, posY, width, height, new Color(20, 20, 20));
			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(blockCount, posX + width / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(blockCount) / 2, posY + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(blockCount) / 2, Color.white);
		} else if (event.getState() == RenderState.THREED) {
			if (placeInfo == null)
				return;
			Client.RENDER3D.drawBox(getMc().thePlayer.getPositionVector().getBlockPos().down(), Client.INSTANCE.getClientColor().setAlpha(100), false);
		}
	}

	@EventTarget
	public void onSafe(SafewalkEvent event) {
		if (getMc().thePlayer.onGround) {
			event.setSafe(true);
		}
	}

	@EventTarget
	public void onJump(JumpEvent event) {
		event.setCancelled(Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled());
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (getMc().thePlayer == null)
			return;

		if (event.getPacket() instanceof C09PacketHeldItemChange) {
			slot = ((C09PacketHeldItemChange) event.getPacket()).getSlotId();
		}
	}

	private Object[] getPlacingPosition(BlockPos pos, int expansion) {
		BlockPos blockPos = pos;
		EnumFacing facing = EnumFacing.UP;

		if (pos.add(0, 0, expansion).getBlock() != Blocks.air) {
			blockPos = pos.add(0, 0, expansion);
			facing = EnumFacing.NORTH;
		}
		if (pos.add(0, 0, -expansion).getBlock() != Blocks.air) {
			blockPos = pos.add(0, 0, -expansion);
			facing = EnumFacing.SOUTH;
		}
		if (pos.add(expansion, 0, 0).getBlock() != Blocks.air) {
			blockPos = pos.add(expansion, 0, 0);
			facing = EnumFacing.WEST;
		}
		if (pos.add(-expansion, 0, 0).getBlock() != Blocks.air) {
			blockPos = pos.add(-expansion, 0, 0);
			facing = EnumFacing.EAST;
		}
		if (pos.add(0, -expansion, 0).getBlock() != Blocks.air) {
			blockPos = pos.add(0, -expansion, 0);
			facing = EnumFacing.UP;
		}
		return new Object[] { blockPos, facing, new Vec3(0, 0, 0) };
	}

	private Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.NORTH) {
			return new Vec3(pos.getX(), pos.getY(), pos.getZ() - .5);
		}
		if (face == EnumFacing.EAST) {
			return new Vec3(pos.getX() + .5, pos.getY(), pos.getZ());
		}
		if (face == EnumFacing.SOUTH) {
			return new Vec3(pos.getX(), pos.getY(), pos.getZ() + .5);
		}
		if (face == EnumFacing.WEST) {
			return new Vec3(pos.getX() - .5, pos.getY(), pos.getZ());
		}
		return new Vec3(pos.getX(), pos.getY(), pos.getZ());
	}
	
	private boolean hasNeighbour(BlockPos pos) {
		return (pos.add(-1, 0, 0).getBlock() != Blocks.air || pos.add(1, 0, 0).getBlock() != Blocks.air || pos.add(0, 0, 1).getBlock() != Blocks.air || pos.add(0, 0, -1).getBlock() != Blocks.air || pos.add(0, -1, 0).getBlock() != Blocks.air);
	}
	
    private boolean rayTrace(float yaw, float pitch) {
        Vec3 vec3 = getMc().thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = Client.ROTATION_UTIL.getVectorForRotation(yaw, pitch);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * 5, vec31.yCoord * 5, vec31.zCoord * 5);

        MovingObjectPosition result = getMc().theWorld.rayTraceBlocks(vec3, vec32, false);

        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((BlockPos) placeInfo[0]).equals(result.getBlockPos());
    }

	private Vec3 blockDataToVec3(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
		double x = paramBlockPos.getX() + 0.5D;
		double y = paramBlockPos.getY() + 0.5D;
		double z = paramBlockPos.getZ() + 0.5D;
		x += paramEnumFacing.getFrontOffsetX() / 2.0D;
		y += paramEnumFacing.getFrontOffsetZ() / 2.0D;
		z += paramEnumFacing.getFrontOffsetY() / 2.0D;
        if (paramEnumFacing == EnumFacing.UP || paramEnumFacing == EnumFacing.DOWN) {
            x += this.randomNumber(-0.3, 0.3);
            z += this.randomNumber(-0.3, 0.3);
        }
        else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (paramEnumFacing == EnumFacing.WEST || paramEnumFacing == EnumFacing.EAST) {
            z += this.randomNumber(-0.3, 0.3);
        }
        if (paramEnumFacing == EnumFacing.SOUTH || paramEnumFacing == EnumFacing.NORTH) {
            x += this.randomNumber(-0.3, 0.3);
        }
		return new Vec3(x, y, z);
	}
	
	private double randomNumber(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	private float getAACYaw() {
		switch (getMc().getRenderViewEntity().getHorizontalFacing()) {
		case EAST:
			return 90;

		case SOUTH:
			return 180;

		case WEST:
			return 270;

		default:
			return 0;
		}
	}

	private int getBlocksInHotbar() {
		int amount = 0;
		for (int i = 36; i < 45; i++) {
			final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack == null)
				continue;
			if (stack.getItem() instanceof ItemBlock) {
				amount += stack.stackSize;
			}
		}
		return amount;
	}
}
