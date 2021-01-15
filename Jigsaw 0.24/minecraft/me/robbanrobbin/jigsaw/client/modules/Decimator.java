package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.sun.jna.platform.unix.X11.XPoint;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Decimator extends Module {

	RenderManager renderManager = mc.getRenderManager();
	WaitTimer timer = new WaitTimer();

	public Decimator() {
		super("Decimator", Keyboard.KEY_NONE, Category.WORLD, "idk");
	}
	@Override
	public void onLeftClick() {
		if(!mc.thePlayer.capabilities.isCreativeMode) {
			Jigsaw.chatMessage("§cYou must be in creativemode!");
			return;
		}
		if(false) {
			ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
			int tpCount = 999;
			ArrayList<Double> xPoses = new ArrayList<Double>();
			ArrayList<Double> zPoses = new ArrayList<Double>();
			double angleA = Math.toRadians(Utils.normalizeAngle(mc.thePlayer.rotationYawHead + (90)));
			double valX = Math.cos(angleA);
			double valZ = Math.sin(angleA);
			for (int i = 6; i < 150; i += 2) {
				double x = valX * (double) i;
				double z = valZ * (double) i;
				x += mc.thePlayer.posX;
				z += mc.thePlayer.posZ;
				for (int xx = -7; xx < 7; xx++) {
					for (int yy = 7; yy > -7; yy--) {
						for (int zz = -7; zz < 7; zz++) {
							double xBlock = (xx + x);
							double yBlock = (mc.thePlayer.posY + yy);
							double zBlock = (zz + z);

							BlockPos blockPos1 = new BlockPos(xBlock, yBlock, zBlock);
							Block block1 = mc.theWorld.getBlockState(blockPos1).getBlock();

							if (block1.getMaterial() != Material.air) {
								mineBlock(blockPos1, hitBlocks);
							}

						}
					}
				}
				if (i > 0 && tpCount > 1) {
					double xr = valX * ((double) i - 6.0);
					double zr = valZ * ((double) i - 6.0);
					xr += mc.thePlayer.posX;
					zr += mc.thePlayer.posZ;
					xPoses.add(xr);
					zPoses.add(zr);
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xr, mc.thePlayer.posY, zr, false));
					tpCount = 0;
				}
				tpCount++;
			}
			for (int i = xPoses.size() - 2; i > -1; i -= 2) {
				if (i < 0) {
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xPoses.get(0), mc.thePlayer.posY,
							zPoses.get(0), false));
					break;
				}
				sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xPoses.get(i), mc.thePlayer.posY, zPoses.get(i),
						false));
			}
		}
		else {
			ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
			int tpCount = 999;
			ArrayList<Double> xPoses = new ArrayList<Double>();
			ArrayList<Double> yPoses = new ArrayList<Double>();
			ArrayList<Double> zPoses = new ArrayList<Double>();
			double valX = mc.thePlayer.getLookVec().xCoord;
			double valZ = mc.thePlayer.getLookVec().zCoord;
			double valY = mc.thePlayer.getLookVec().yCoord;
			for (int i = 6; i < 150; i += 2) {
				double x = valX * (double) i;
				double z = valZ * (double) i;
				double y = valY * (double) i;
				x += mc.thePlayer.posX;
				z += mc.thePlayer.posZ;
				y += mc.thePlayer.posY;
				for (int xx = -7; xx < 7; xx++) {
					for (int yy = 8; yy > -6; yy--) {
						for (int zz = -7; zz < 7; zz++) {
							double xBlock = (xx + x);
							double yBlock = (yy + y);
							double zBlock = (zz + z);

							BlockPos blockPos1 = new BlockPos(xBlock, yBlock, zBlock);
							Block block1 = mc.theWorld.getBlockState(blockPos1).getBlock();

							if (block1.getMaterial() != Material.air) {
								mineBlock(blockPos1, hitBlocks);
							}

						}
					}
				}
				if (i > 0 && tpCount > 1) {
					double xr = valX * ((double) i - 6.0);
					double zr = valZ * ((double) i - 6.0);
					double yr = valY * ((double) i - 6.0);
					xr += mc.thePlayer.posX;
					zr += mc.thePlayer.posZ;
					yr += mc.thePlayer.posY;
					xPoses.add(xr);
					yPoses.add(yr);
					zPoses.add(zr);
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xr, yr, zr, false));
					tpCount = 0;
				}
				tpCount++;
			}
			for (int i = xPoses.size() - 2; i > -1; i -= 2) {
				if (i < 0) {
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xPoses.get(0), yPoses.get(0),
							zPoses.get(0), false));
					break;
				}
				sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(xPoses.get(i), yPoses.get(i), zPoses.get(i),
						false));
			}
		}
		
		super.onLeftClick();
	}
	private void mineBlock(BlockPos pos, ArrayList<BlockPos> minedblocks) {
//		for(BlockPos pos1 : minedblocks) {
//			if(pos.getX() == pos1.getX() && pos.getY() == pos1.getY() && pos.getZ() == pos1.getZ()) {
//				return;
//			}
//		}
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK,
				pos, EnumFacing.NORTH));
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK,
				pos, EnumFacing.NORTH));
		//minedblocks.add(pos);
	}
}
