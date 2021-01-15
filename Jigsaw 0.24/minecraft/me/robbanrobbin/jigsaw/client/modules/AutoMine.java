package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Slider;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderTask;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AutoMine extends Module {
	
	private EnumFacing facing = EnumFacing.EAST;
	private boolean skipCheck = false;
	private WaitTimer skipCheckTimer = new WaitTimer();

	public AutoMine() {
		super("AutoMine", 0, Category.WORLD, "Mines automatically so you can take a shit and when you come back you got tons of diamonds and you have hopefuly not fallen in lava.");
	}
	
	@Override
	public ModSetting[] getModSettings() {
		double max = (mc.theWorld == null ? 256 : mc.theWorld.getHeight());
		return new ModSetting[]{new SliderSetting<Double>("Block Height", 
				ClientSettings.autoMineBlockLimit, 0.0, max, 0.0, ValueFormat.DECIMAL)};
	}
	
	@Override
	public void onEnable() {
		facing = EnumFacing.fromAngle(mc.thePlayer.rotationYaw);
		skipCheckTimer.time = 5000;
		super.onEnable();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		BlockPos toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ).offset(facing);
		boolean foundItem = false;
		boolean foundOre = false;
		if(skipCheckTimer.hasTimeElapsed(5000, false)) {
			Block under = Utils.getBlockRelativeToEntity(mc.thePlayer, -0.01d);
			IBlockState stateUnder = Utils.getBlockState(Utils.getBlockPosRelativeToEntity(mc.thePlayer, -0.01d));
			
			EntityItem theItem = null;
			for (EntityItem item : Utils.getNearbyItems(5)) {
				if (mc.thePlayer.canEntityBeSeen(item) && item.ticksExisted > 20 && item.ticksExisted < 150) {
					foundItem = true;
					theItem = item;
				}
			}
			if (foundItem) {
				Utils.faceEntity(theItem);
				mc.thePlayer.moveFlying(0, 1f, 0.1f);
				if (theItem.posY > mc.thePlayer.posY && mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				}
			} 
			else {
				for (int x = -3; x <= 3; x++) {
					for (int y = -3; y <= 5; y++) {
						for (int z = -3; z <= 3; z++) {
							BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y,
									mc.thePlayer.posZ + z);
							Block block = Utils.getBlock(blockPos);
							IBlockState state = Utils.getBlockState(blockPos);
							if (state.getBlock().getMaterial() == Material.air) {
								continue;
							}
							if (block instanceof BlockLiquid) {
								MovingObjectPosition trace0 = mc.theWorld.rayTraceBlocks(
										new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
										new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), true,
										false, true);
								if (trace0.getBlockPos() == null) {
									continue;
								}
								BlockPos blockPosTrace = trace0.getBlockPos();
								Block blockTrace = Utils.getBlock(blockPosTrace);
								if(blockTrace instanceof BlockLiquid) {
									facing = facing.getOpposite();
									toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
											mc.thePlayer.posZ).offset(facing);
									if (Utils.isBlockPosAir(toFace)) {
										toFace = toFace.down();
									}
									skipCheckTimer.reset();
									break;
								}
							}
							if (block instanceof BlockOre
									 || block instanceof BlockRedstoneOre) {
								MovingObjectPosition trace0 = mc.theWorld.rayTraceBlocks(
										new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
										new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), true,
										false, true);
								if (trace0.getBlockPos() == null) {
									continue;
								}
								BlockPos blockPosTrace = trace0.getBlockPos();
								Block blockTrace = Utils.getBlock(blockPosTrace);
								double dist = Utils.getVec3(blockPos).distanceTo(Utils.getVec3(blockPosTrace));
								double xx = mc.thePlayer.posX - (blockPosTrace.getX() + 0.5);
								double yy = mc.thePlayer.posY - (blockPosTrace.getY() + 0.5 - mc.thePlayer.getEyeHeight());
								double zz = mc.thePlayer.posZ - (blockPosTrace.getZ() + 0.5);
								double dist0 = Math.sqrt(x * x + y * y + z * z);
								if(dist0 >= 1.5) {
									float yaw = Utils.getFacePos(Utils.getVec3(blockPosTrace))[0];
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
								if (dist <= 0) {
									toFace = blockPosTrace;
									foundOre = true;
									break;
								}
							}
						}
					}
				}
				if (foundOre) {
					Utils.faceBlock(toFace);
					if (!Utils.isBlockPosAir(toFace)) {
						mineBlock(toFace);
					}
				} 
				else {
					if (mc.thePlayer.posY > ClientSettings.autoMineBlockLimit) {
						if (stateUnder.getBlock().getMaterial() != Material.air) {
							mineBlockUnderPlayer();
						} 
						else if (mc.thePlayer.onGround) {
							mc.thePlayer.moveFlying(0, 0.5f, 0.1f);
						}
					} 
					else {
						if (Utils.isBlockPosAir(toFace)) {
							toFace = toFace.down();
							if (isBlockPosSafe(toFace)) {
								if (Utils.isBlockPosAir(toFace) && mc.thePlayer.onGround) {
									mc.thePlayer.moveFlying(0, 1f, 0.1f);
								}
							} 
							else {
								facing = facing.fromAngle(mc.thePlayer.rotationYaw + 90);
								toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
										mc.thePlayer.posZ).offset(facing);
								if (!isBlockPosSafe(toFace)) {
									facing = facing.fromAngle(mc.thePlayer.rotationYaw - 90);
									toFace = new BlockPos(mc.thePlayer.posX,
											mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
													.offset(facing);
									if (!isBlockPosSafe(toFace.down())) {
										facing = facing.fromAngle(mc.thePlayer.rotationYaw + 180);
										toFace = new BlockPos(mc.thePlayer.posX,
												mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
														.offset(facing);
									}
								}
							}
						}
						if (Utils.isBlockPosAir(toFace)) { //Stop bobbing (up and down is irritating)
							toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ).offset(facing);
						}
						Utils.faceBlock(toFace);
						if (!Utils.isBlockPosAir(toFace)) {
							mineBlock(toFace);
						}
					}
				}
			}
		}
		else {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.moveFlying(0, 1f, 0.1f);
			}
			if (Utils.isBlockPosAir(toFace)) {
				toFace = toFace.down();
			}
			Utils.faceBlock(toFace);
			if (!Utils.isBlockPosAir(toFace)) {
				mineBlock(toFace);
			}
		}
		
		super.onUpdate(event);
	}
	
	public void mineBlockUnderPlayer() {
		BlockPos pos = Utils.getBlockPosRelativeToEntity(mc.thePlayer, -0.01d);
		mineBlock(pos);
	}
	
	public void mineBlock(BlockPos pos) {
		Block block = Utils.getBlock(pos);
		Utils.faceBlock(pos);
		mc.thePlayer.swingItem();
		mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
	}
	
	public boolean isBlockPosSafe(BlockPos pos) {
		return checkBlockPos(pos, 10);
	}
	
	public boolean checkBlockPos(BlockPos pos, int checkHeight) {
		boolean safe = true;
		boolean blockInWay = false;
		int fallDist = 0;
		if(Utils.getBlock(pos).getMaterial() == Material.lava
				|| Utils.getBlock(pos).getMaterial() == Material.water) {
			return false;
		}
		if(Utils.getBlock(pos.up(1)).getMaterial() == Material.lava
				|| Utils.getBlock(pos.up(1)).getMaterial() == Material.water) {
			return false;
		}
		if(Utils.getBlock(pos.up(2)).getMaterial() == Material.lava
				|| Utils.getBlock(pos.up(2)).getMaterial() == Material.water) {
			return false;
		}
		for(int i = 1; i < checkHeight + 1; i++) {
			BlockPos pos2 = pos.down(i);
			Block block = Utils.getBlock(pos2);
			if(block.getMaterial() == Material.air) {
				if(!blockInWay) {
					fallDist++;
				}
				continue;
			}
			if(!blockInWay) {
				if(block.getMaterial() == Material.lava
						|| block.getMaterial() == Material.water) {
					return false;
				}
			}
			if(!blockInWay) {
				blockInWay = true;
			}
		}
		if(fallDist > 2) {
			safe = false;
		}
		return safe;
	}
	
}
