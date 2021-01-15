package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.*;
import me.xatzdevelopments.xatz.client.darkmagician6.EventTarget;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class Scaffold3 extends Module {
	
	private Timer placeDelay;

	private Object[] placeInfo;

	private int slot;

	private int stage;
	
	private boolean canPlace;

    
	public ModSetting[] getModSettings() {
		CheckBtnSetting scaffoldsprint = new CheckBtnSetting("Sprint", "scaffoldsprint");
		CheckBtnSetting scaffoldsneak = new CheckBtnSetting("Sneak", "scaffoldsneak");
		CheckBtnSetting scaffoldsilent = new CheckBtnSetting("Silent", "scaffoldsilent");
		CheckBtnSetting scaffoldjitter = new CheckBtnSetting("Jitter", "scaffoldjitter");
		CheckBtnSetting scaffoldtimer = new CheckBtnSetting("Timer", "scaffoldtimer");
		CheckBtnSetting scaffoldnoswing = new CheckBtnSetting("No Swing", "scaffoldnoswing");
		SliderSetting<Number> scaffolddelay = new SliderSetting<Number>("Delay", ClientSettings.scaffolddelay, 0, 2000, 0.0, ValueFormat.INT);
		SliderSetting<Number> scaffoldtimerspeed = new SliderSetting<Number>("Delay", ClientSettings.scaffoldtimerspeed, 1, 20, 0.0, ValueFormat.INT);
		SliderSetting<Number> scaffoldjitterfactor = new SliderSetting<Number>("Delay", ClientSettings.scaffoldjitterfactor, 0.1, 1, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] {scaffoldsprint, scaffoldsneak, scaffoldsilent, scaffoldtimer, scaffoldnoswing, scaffolddelay, scaffoldtimerspeed, scaffoldjitterfactor };
    }

    public Scaffold3() {
        super("Scaffold3", Keyboard.KEY_NONE, Category.PLAYER, "test");
    }

    @Override
    public void onDisable() {
    	if (mc.thePlayer == null)
			return;
		Xatz.MOVEMENT_UTIL.doStrafe(.2);
		canPlace = false;
		mc.gameSettings.keyBindSneak.pressed = false;
		if (slot != mc.thePlayer.inventory.currentItem) {
			mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		}
        super.onDisable();
    }
    
    @Override
	public void onToggle() {
		placeInfo = null;
		stage = 0;
		mc.timer.timerSpeed = 1;
	}
    
	@EventTarget
	public void onMove(MoveEvent event) {
		
	}

	@EventTarget
	public void onRender(RenderEvent event) {
		
	}

	

	@EventTarget
	public void onJump(JumpEvent event) {
		
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		
	}

	
    @Override
    public void onEnable() {
        
        super.onEnable();
    }

    @Override
    public void onUpdate(UpdateEvent event) {
    
			
			if (mc.thePlayer.getHeldItem() == null) {
				if (ClientSettings.scaffoldsilent) {
					if (!Xatz.INVENTORY_UTIL.hasBlockInHotbar()) {
						placeInfo = null;
						return;
					}
				} else {
					placeInfo = null;
					return;
				}
			}
			
			placeInfo = getPlacingPosition(mc.thePlayer.getPositionVector().getBlockPos().add(0, -1, 0), 1);
			canPlace = false;

			if (placeInfo == null)
				return;

			final Vec3 blockSidePosition = getBlockSide((BlockPos) placeInfo[0], (EnumFacing) placeInfo[1]);
			float pitch = mc.thePlayer.onGround ? 80F : 90F;

			final float[] rotations = Xatz.ROTATION_UTIL.getRotations(blockSidePosition, false, 1);
			
			switch (this.currentMode) {
			case "NCP":
				if (rotations == null)
					return;

				mc.thePlayer.rotationYawHead = rotations[0];
				mc.thePlayer.renderYawOffset = rotations[0];
				mc.thePlayer.rotationPitchHead = rotations[1];
				
				canPlace = rayTrace(rotations[0], rotations[1]);

				event.setRotation(rotations[0], rotations[1]);
				break;
			
			case "AAC":
				final float yaw = (float) (getAACYaw() + ThreadLocalRandom.current().nextDouble(-1, 1));
				pitch = 81;
				pitch += ThreadLocalRandom.current().nextDouble(-1, 1);
				mc.thePlayer.rotationYawHead = yaw;
				mc.thePlayer.renderYawOffset = yaw;
				mc.thePlayer.rotationPitchHead = pitch;
				
				canPlace = rayTrace(yaw, pitch);
				
				event.setRotation(yaw, pitch);
				break;

			case "Redesky":
				final float yaw2 = (float) (getAACYaw() + ThreadLocalRandom.current().nextDouble(-1, 1));
				pitch += ThreadLocalRandom.current().nextDouble(-1, 1);

				mc.thePlayer.rotationYawHead = yaw2;
				mc.thePlayer.renderYawOffset = yaw2;
				mc.thePlayer.rotationPitchHead = pitch;
				
				canPlace = rayTrace(yaw2, pitch);
				
				event.setRotation(yaw2, pitch);
				break;
			}
		
        super.onUpdate();
    }
    
    public String[] getModes() {
		return new String[] { "NCP", "AAC", "Redesky"};
	}
	
	public String getModeName() {
		return "Mode: ";
	}
	
	private float getAACYaw() {
		switch (mc.getRenderViewEntity().getHorizontalFacing()) {
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
	
	 private boolean rayTrace(float yaw, float pitch) {
	        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
	        Vec3 vec31 = Xatz.ROTATION_UTIL.getVectorForRotation(yaw, pitch);
	        Vec3 vec32 = vec3.addVector(vec31.xCoord * 5, vec31.yCoord * 5, vec31.zCoord * 5);

	        MovingObjectPosition result = mc.theWorld.rayTraceBlocks(vec3, vec32, false);

	        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((BlockPos) placeInfo[0]).equals(result.getBlockPos());
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
    
}