package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AreaMine extends Module {
	
	public BlockPos pos1;
	public BlockPos pos2;
	public ArrayList<BlockPos> blockPoses = new ArrayList<BlockPos>();
	public BlockPos currentPos;

	public AreaMine() {
		super("AreaMine", 0, Category.WORLD, "Select two blocks and it will mine it for you!");
	}
	
	@Override
	public ModSetting[] getModSettings() {
//		final BasicCheckButton box2 = new BasicCheckButton("Fastbreak");
//		box2.setSelected(ClientSettings.areaMineFastBreak);
//		box2.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.areaMineFastBreak = box2.isSelected();
//			}
//		});
		CheckBtnSetting box2 = new CheckBtnSetting("FastBreak", "areaMineFastBreak");
		return new ModSetting[]{box2};
	}
	
	@Override
	public void onEnable() {
		pos1 = null;
		pos2 = null;
		blockPoses.clear();
		currentPos = null;
		super.onEnable();
	}
	
	public float[] getRots(BlockPos pos) {
		float[] rots = Utils.getFacePos(Utils.getVec3(pos));
		
		rots = Utils.getFacePos(Utils.getVec3(pos));
		
		float yaw = rots[0];
		float pitch = rots[1];
		
		return new float[]{yaw, pitch};
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(currentPos != null && Utils.getBlock(currentPos).getMaterial() != Material.air) {
			float[] rots = getRots(currentPos);
			event.yaw = rots[0];
			event.pitch = rots[1];
			return;
		}
		currentPos = null;
		if(pos1 == null || pos2 == null) {
			return;
		}
		if(blockPoses.isEmpty()) {
			for(BlockPos pos : BlockPos.getAllInBox(pos1, pos2)) {
				blockPoses.add(pos);
			}
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Loaded " + blockPoses.size() + " blocks!"));
		}
		int valid = 0;
		for(BlockPos pos : blockPoses) {
			if(Utils.getBlock(pos).getMaterial() != Material.air) {
				valid++;
			}
		}
		if(valid == 0) {
			pos1 = null;
			pos2 = null;
			blockPoses.clear();
			currentPos = null;
		}
		for(BlockPos pos : blockPoses) {
			if(Utils.getBlock(pos).getMaterial() == Material.air) {
				continue;
			}
			double x = mc.thePlayer.posX - (pos.getX() + 0.5);
			double y = mc.thePlayer.posY - (pos.getY() + 0.5 - mc.thePlayer.getEyeHeight());
			double z = mc.thePlayer.posZ - (pos.getZ() + 0.5);
			double dist = Math.sqrt(x * x + y * y + z * z);
			if(dist < 5) {
				MovingObjectPosition trace = mc.theWorld.rayTraceBlocks(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), true,
						false, true);
				if(trace.getBlockPos().equals(pos)) {
					if(dist >= 1.5) {
						float yaw = getRots(trace.getBlockPos())[0];
						yaw = Utils.normalizeAngle(yaw);
						if (yaw == 45) {
							continue;
						}
						if (yaw == -45) {
							continue;
						}
						if (yaw == 135) {
							continue;
						}
						if (yaw == -135) {
							continue;
						}
					}
					currentPos = pos;
					float[] rots = getRots(currentPos);
					event.yaw = rots[0];
					event.pitch = rots[1];
					break;
				}
			}
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		if(currentPos != null) {
			double x = mc.thePlayer.posX - (currentPos.getX() + 0.5);
			double y = mc.thePlayer.posY - (currentPos.getY() + 0.5 - mc.thePlayer.getEyeHeight());
			double z = mc.thePlayer.posZ - (currentPos.getZ() + 0.5);
			double dist = Math.sqrt(x * x + y * y + z * z);
			if(dist >= 5) {
				currentPos = null;
				return;
			}
			mineBlock(currentPos);
		}
		super.onLateUpdate();
	}
	
	@Override
	public void onRender() {
		if(pos1 != null) {
			Vec3 pos = RenderTools.getRenderPos(pos1.getX(), pos1.getY(), pos1.getZ());
			RenderTools.drawBlockESP(pos.xCoord, pos.yCoord, pos.zCoord, 0.5f, 1f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		if(pos2 != null) {
			Vec3 pos = RenderTools.getRenderPos(pos2.getX(), pos2.getY(), pos2.getZ());
			RenderTools.drawBlockESP(pos.xCoord, pos.yCoord, pos.zCoord, 1f, 0.5f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		if(pos1 != null && pos2 != null) {
			for(BlockPos blockPos : blockPoses) {
				if(Utils.getBlock(blockPos).getMaterial() == Material.air) {
					continue;
				}
				Vec3 pos = RenderTools.getRenderPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				RenderTools.drawOutlinedBlockESP(pos.xCoord, pos.yCoord, pos.zCoord, 1, 1, 1, 0.2f, 1f);
			}
		}
		if(currentPos != null) {
			Vec3 pos = RenderTools.getRenderPos(currentPos.getX(), currentPos.getY(), currentPos.getZ());
			RenderTools.drawBlockESP(pos.xCoord, pos.yCoord, pos.zCoord, 0.5f, 0.5f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		super.onRender();
	}
	
	@Override
	public void onRightClick() {
		if(pos1 == null) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Set first position!"));
			pos1 = mc.objectMouseOver.getBlockPos();
		}
		else if(pos2 == null) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Set second position!"));
			pos2 = mc.objectMouseOver.getBlockPos();
		}
		super.onRightClick();
	}
	
	public void mineBlock(BlockPos pos) {
		if(ClientSettings.areaMineFastBreak) {
			mc.thePlayer.swingItem();
			mc.thePlayer.sendQueue.addToSendQueue(
					new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.NORTH));
			mc.thePlayer.sendQueue.addToSendQueue(
					new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.NORTH));
		}
		else {
			mc.thePlayer.swingItem();
			if(mc.thePlayer.capabilities.isCreativeMode) {
				mc.playerController.clickBlock(pos, EnumFacing.UP);
			}
			else {
				mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
			}
		}
		
		
		
		
	}
	
}
