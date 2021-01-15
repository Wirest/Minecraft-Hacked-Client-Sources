package me.robbanrobbin.jigsaw.client.tools;

import java.util.ArrayList;
import java.util.Random;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.AutoBlock;
import me.robbanrobbin.jigsaw.client.modules.Criticals;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandEntityData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Utils {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static Random rand = new Random();
	
	public static boolean spectator;
	
	public static ArrayList<Integer> blackList = new ArrayList<Integer>();
	
	static double x;
	static double y;
	static double z;
	static double xPreEn;
	static double yPreEn;
	static double zPreEn;
	static double xPre;
	static double yPre;
	static double zPre;
	
//	static ArrayList<Vec3> positions = new ArrayList<Vec3>();
//	static ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	
	private static void preInfiniteReach(double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions, Vec3 targetPos, boolean tpStraight, boolean up, boolean attack, boolean tpUpOneBlock, boolean sneaking) {
		
	}
	
	private static void postInfiniteReach() {
		
	}
	
	public static boolean infiniteReach(double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions, EntityLivingBase en) {
		
		int ind = 0;
		xPreEn = en.posX;
		yPreEn = en.posY;
		zPreEn = en.posZ;
		xPre = mc.thePlayer.posX;
		yPre = mc.thePlayer.posY;
		zPre = mc.thePlayer.posZ;
		boolean attack = true;
		boolean up = false;
		boolean tpUpOneBlock = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		
		boolean sneaking = mc.thePlayer.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();

		positions.clear();
		positionsBack.clear();
		
		//preInfiniteReach(range, maxXZTP, maxYTP, positionsBack, positions, new Vec3(en.posX, en.posY, en.posZ), tpStraight, up, attack, tpUpOneBlock, sneaking);
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		MovingObjectPosition rayTrace = null;
		MovingObjectPosition rayTrace1 = null;
		MovingObjectPosition rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(en.posX, en.posY, en.posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
					new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ),
							new Vec3(en.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
							true)) != null) {
				MovingObjectPosition trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.thePlayer.posY;
					// yPreEn = mc.thePlayer.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							MovingObjectPosition tr = rayTracePos(
									new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
									new Vec3(en.posX, target.getY() + i, en.posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							Block block = mc.theWorld.getBlockState(blockPos).getBlock();
							if (block.getMaterial() != Material.air) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.thePlayer.posX - xPreEn;
						double difZ = mc.thePlayer.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				MovingObjectPosition ent = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY, en.posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.thePlayer.posY;
					yPreEn = mc.thePlayer.posY;
				} else {
					y = mc.thePlayer.posY;
					yPreEn = en.posY;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.thePlayer.posX;
				y = yPreEn;
				z = mc.thePlayer.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - en.posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistanceToEntity(en) >= 4) {
					x = xPreEn;
					y = en.posY;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					if (Jigsaw.getModuleByName("MegaKnockback").isToggled() && en.onGround) {
						for (int ii = 0; ii < 300; ii++) {
							mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
						}
					}
					attackInf(en);
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}
	
	public static boolean infiniteReach(double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions, BlockPos targetBlockPos) {
		positions.clear();
		positionsBack.clear();
		boolean tpUpOneBlock = false;
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		int ind = 0;
		double posX = ((double)targetBlockPos.getX()) + 0.5;
		double posY = ((double)targetBlockPos.getY()) + 1.0;
		double posZ = ((double)targetBlockPos.getZ()) + 0.5;
		xPreEn = posX;
		yPreEn = posY;
		zPreEn = posZ;
		xPre = mc.thePlayer.posX;
		yPre = mc.thePlayer.posY;
		zPre = mc.thePlayer.posZ;
		boolean attack = true;
		boolean up = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		boolean sneaking = mc.thePlayer.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		MovingObjectPosition rayTrace = null;
		MovingObjectPosition rayTrace1 = null;
		MovingObjectPosition rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(posX, posY, posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(posX, posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
					new Vec3(posX, mc.thePlayer.posY, posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ),
							new Vec3(posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
							true)) != null) {
				MovingObjectPosition trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.thePlayer.posY;
					// yPreEn = mc.thePlayer.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							MovingObjectPosition tr = rayTracePos(
									new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
									new Vec3(posX, target.getY() + i, posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							Block block = mc.theWorld.getBlockState(blockPos).getBlock();
							if (block.getMaterial() != Material.air) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.thePlayer.posX - xPreEn;
						double difZ = mc.thePlayer.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				MovingObjectPosition ent = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
						new Vec3(posX, posY, posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.thePlayer.posY;
					yPreEn = mc.thePlayer.posY;
				} else {
					y = mc.thePlayer.posY;
					yPreEn = posY;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.thePlayer.posX;
				y = yPreEn;
				z = mc.thePlayer.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistance(posX, posY, posZ) >= 4) {
					x = xPreEn;
					y = posY;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					mc.thePlayer.swingItem();
					mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
					mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}

	public static boolean infiniteReach(Vec3 src, Vec3 dest, double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions) {
		positions.clear();
		positionsBack.clear();
		boolean tpUpOneBlock = false;
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		int ind = 0;
		xPreEn = dest.xCoord;
		yPreEn = dest.yCoord;
		zPreEn = dest.zCoord;
		xPre = mc.thePlayer.posX;
		yPre = mc.thePlayer.posY;
		zPre = mc.thePlayer.posZ;
		boolean attack = true;
		boolean up = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		boolean sneaking = mc.thePlayer.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		MovingObjectPosition rayTrace = null;
		MovingObjectPosition rayTrace1 = null;
		MovingObjectPosition rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(dest.xCoord, dest.yCoord, dest.zCoord), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(dest.xCoord, dest.yCoord + mc.thePlayer.getEyeHeight(), dest.zCoord), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
					new Vec3(dest.xCoord, mc.thePlayer.posY, dest.zCoord), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ),
							new Vec3(dest.xCoord, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), dest.zCoord), false, false,
							true)) != null) {
				MovingObjectPosition trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.thePlayer.posY;
					// yPreEn = mc.thePlayer.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							MovingObjectPosition tr = rayTracePos(
									new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
									new Vec3(dest.xCoord, target.getY() + i, dest.zCoord), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							Block block = mc.theWorld.getBlockState(blockPos).getBlock();
							if (block.getMaterial() != Material.air) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.thePlayer.posX - xPreEn;
						double difZ = mc.thePlayer.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				MovingObjectPosition ent = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
						new Vec3(dest.xCoord, dest.yCoord, dest.zCoord), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.thePlayer.posY;
					yPreEn = mc.thePlayer.posY;
				} else {
					y = mc.thePlayer.posY;
					yPreEn = dest.yCoord;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.thePlayer.posX;
				y = yPreEn;
				z = mc.thePlayer.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - dest.yCoord;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP) {
					x = xPreEn;
					y = dest.yCoord;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					//Attack / interact
					
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getNetHandler().getNetworkManager().sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}
	
	private static void attackInf(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		mc.getNetHandler().getNetworkManager().sendPacketFinal(new C02PacketUseEntity(en, C02PacketUseEntity.Action.ATTACK));
		Criticals.disable = false;
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}
	
	public static void sendPacket(boolean goingBack, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions) {
		C04PacketPlayerPosition playerPacket = new C04PacketPlayerPosition(x, y, z, true);
		mc.getNetHandler().getNetworkManager().sendPacketFinal(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3(x, y, z));
			return;
		}
		positions.add(new Vec3(x, y, z));
	}
	
	@SuppressWarnings("unused")
	public static MovingObjectPosition rayTracePos(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		float[] rots = getFacePosRemote(vec32, vec31);
		float yaw = rots[0];
		double angleA = Math.toRadians(Utils.normalizeAngle(yaw));
		double angleB = Math.toRadians(Utils.normalizeAngle(yaw) + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		if (false) {
			MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
					ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
			return trace2;
		}
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		// positionsBack.add(rightA);
		// positionsBack.add(right2A);
		// positionsBack.add(leftA);
		// positionsBack.add(left2A);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace5 != null) {
			if (returnLastUncollidableBlock) {
				if (trace5 != null && (Utils.getBlock(trace5.getBlockPos()).getMaterial() != Material.air
						|| trace5.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace5;
				}
				if (trace4 != null && (Utils.getBlock(trace4.getBlockPos()).getMaterial() != Material.air
						|| trace4.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace4;
				}
				if (trace3 != null && (Utils.getBlock(trace3.getBlockPos()).getMaterial() != Material.air
						|| trace3.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null && (Utils.getBlock(trace1.getBlockPos()).getMaterial() != Material.air
						|| trace1.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null && (Utils.getBlock(trace2.getBlockPos()).getMaterial() != Material.air
						|| trace2.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			} else {
				if (trace5 != null) {
					return trace5;
				}
				if (trace4 != null) {
					return trace4;
				}
				if (trace3 != null) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			}
		}
		if (trace2 == null) {
			if (trace3 == null) {
				if (trace1 == null) {
					if (trace5 == null) {
						if (trace4 == null) {
							return null;
						}
						return trace4;
					}
					return trace5;
				}
				return trace1;
			}
			return trace3;
		}
		return trace2;
	}
	
	public static boolean rayTraceWide(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox,
			boolean returnLastUncollidableBlock) {
		float yaw = getFacePosRemote(vec32, vec31)[0];
		yaw = Utils.normalizeAngle(yaw);
		yaw += 180;
		yaw = MathHelper.wrapAngleTo180_float(yaw);
		double angleA = Math.toRadians(yaw);
		double angleB = Math.toRadians(yaw + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (returnLastUncollidableBlock) {
			return (trace1 != null && Utils.getBlock(trace1.getBlockPos()).getMaterial() != Material.air)
					|| (trace2 != null && Utils.getBlock(trace2.getBlockPos()).getMaterial() != Material.air)
					|| (trace3 != null && Utils.getBlock(trace3.getBlockPos()).getMaterial() != Material.air)
					|| (trace4 != null && Utils.getBlock(trace4.getBlockPos()).getMaterial() != Material.air)
					|| (trace5 != null && Utils.getBlock(trace5.getBlockPos()).getMaterial() != Material.air);
		} else {
			return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
		}

	}
	
	public static void blinkToPosFromPos(Vec3 src, Vec3 dest, double maxTP) {
		double range = 0;
		double xDist = src.xCoord - dest.xCoord;
		double yDist = src.yCoord - dest.yCoord;
		double zDist = src.zCoord - dest.zCoord;
		double x1 = src.xCoord;
		double y1 = src.yCoord;
		double z1 = src.zCoord;
		double x2 = dest.xCoord;
		double y2 = dest.yCoord;
		double z2 = dest.zCoord;
		range = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
		double step = maxTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			if (maxTP * steps > range) {
				break;
			}
		}
		for (int i = 0; i < steps; i++) {
			double difX = x1 - x2;
			double difY = y1 - y2;
			double difZ = z1 - z2;
			double divider = step * i;
			double x = x1 - difX * divider;
			double y = y1 - difY * divider;
			double z = z1 - difZ * divider;
			//Jigsaw.chatMessage(y);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
		}
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2, z2, true));
	}
	
	public static boolean isBlacklisted(Entity en) {
		for(int i : blackList) {
			if(en.getEntityId() == i) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<EntityLivingBase> getClosestEntitiesToEntity(float range, Entity ent) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (Utils.isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (ent.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
	/**
	 * Returns the distance to the entity. Args: entity
	 */
	public float getDistanceToEntityFromEntity(Entity entityIn, Entity entityIn2) {
		float f = (float) (entityIn.posX - entityIn2.posX);
		float f1 = (float) (entityIn.posY - entityIn2.posY);
		float f2 = (float) (entityIn.posZ - entityIn2.posZ);
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public static ArrayList<EntityLivingBase> getClosestEntities(float range) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
//	public static boolean checkEntity(boolean friends, boolean invisible, boolean players) {
//		if (en.isEntityEqual(Minecraft.getMinecraft().thePlayer)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
//			return false;
//		}
//		if (en.isDead) {
//			return false;
//		}
//		if (en.getHealth() <= 0) {
//			return false;
//		}
//		if (!(en instanceof EntityLivingBase)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
//			if (!Jigsaw.getModuleByName("Friends").isToggled()) {
//				return false;
//			}
//		}
//		if (en.isInvisible()) {
//			if (!Jigsaw.getModuleByName("Invisible").isToggled()) {
//				return false;
//			}
//		}
//		if (en instanceof EntityPlayer) {
//			if (!Jigsaw.getModuleByName("Players").isToggled() || en.height < 0.21f) {
//				return false;
//			}
//		}
//		if (Team.isOnTeam(en)) {
//			if (!Jigsaw.getModuleByName("Team").isToggled()) {
//				return false;
//			}
//		}
//		if (!(en instanceof EntityPlayer)) {
//			if (!Jigsaw.getModuleByName("NonPlayers").isToggled()) {
//				return false;
//			}
//		}
//		if ((en instanceof EntityPlayer)) {
//			if (Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiGwen")) {
//				if (!((EntityPlayer) en).didSwingItem) {
//					if (en.onGround) {
//						if (en.isSprinting()) {
//							return true;
//						}
//					} else {
//						if (en.hurtResistantTime == 0) {
//							return false;
//						}
//					}
//				}
//			}
//			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog")) {
//				if(en.ticksExisted < 139) {
//					return false;
//				}
//			}
//		}
//		if(isBlacklisted(en)) {
//			return false;
//		}
//		// if(en.hurtTime > 12 &&
//		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
//		// return false;
//		// }
//		return true;
//	}

	public static boolean validEntity(EntityLivingBase en) {
		if (en.isEntityEqual(Minecraft.getMinecraft().thePlayer)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
			return false;
		}
		if (en.isDead) {
			return false;
		}
		if (en.getHealth() <= 0) {
			return false;
		}
		if (!(en instanceof EntityLivingBase)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
			if (!Jigsaw.getModuleByName("Friends").isToggled()) {
				return false;
			}
		}
		if (en.isInvisible()) {
			if (!Jigsaw.getModuleByName("Invisible").isToggled()) {
				return false;
			}
		}
		if (en instanceof EntityPlayer) {
			if (!Jigsaw.getModuleByName("Players").isToggled() || en.height < 0.21f) {
				return false;
			}
		}
		if (Team.isOnTeam(en)) {
			if (!Jigsaw.getModuleByName("Team").isToggled()) {
				return false;
			}
		}
		if (!(en instanceof EntityPlayer)) {
			if (!Jigsaw.getModuleByName("NonPlayers").isToggled()) {
				return false;
			}
		}
		if (!(en instanceof EntityPlayer) && en instanceof EntityLiving && Jigsaw.getModuleByName("Skip Unarmored Mobs").isToggled()) {
			EntityLiving living = (EntityLiving)en;
			boolean armor = false;
			if(!armor && living.getCurrentArmor(0) != null && living.getCurrentArmor(0).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(1) != null && living.getCurrentArmor(1).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(2) != null && living.getCurrentArmor(2).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(3) != null && living.getCurrentArmor(3).getItem() != null) {
				armor = true;
			}
			if(armor == false) {
				return false;
			}
		}
		if ((en instanceof EntityPlayer) && Jigsaw.getModuleByName("Skip Unarmored Players").isToggled()) {
			EntityPlayer living = (EntityPlayer)en;
			boolean armor = false;
			if(!armor && living.inventory.armorInventory[0] != null && living.inventory.armorInventory[0].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[1] != null && living.inventory.armorInventory[1].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[2] != null && living.inventory.armorInventory[2].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[3] != null && living.inventory.armorInventory[3].getItem() != null) {
				armor = true;
			}
			if(armor == false) {
				return false;
			}
		}
		if ((en instanceof EntityPlayer)) {
			if (Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiGwen") || Jigsaw.getModuleByName("AntiBot(Gwen)").isToggled()) {
				if (!((EntityPlayer) en).didSwingItem) {
					if (en.onGround) {
						if (en.isSprinting()) {
							return true;
						}
					} else {
						if (en.hurtResistantTime == 0) {
							return false;
						}
					}
				}
			}
//			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog") || Jigsaw.getModuleByName("AntiBot(Watchdog)").isToggled()) {
//				if(en.ticksExisted < 139) {
//					return false;
//				}
//			}
		}
		if(isBlacklisted(en)) {
			return false;
		}
		// if(en.hurtTime > 12 &&
		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
		// return false;
		// }
		return true;
	}

	public static EntityLivingBase getClosestEntity(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}
	
	public static EntityLivingBase getClosestEntitySkipValidCheck(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static EntityLivingBase getClosestEntityToEntity(float range, Entity ent) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (ent.getDistanceToEntity(en) < mindistance) {
					mindistance = ent.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static boolean isNotItem(Object o) {
		if (!(o instanceof EntityLivingBase)) {
			return false;
		}
		return true;
	}

	public static void faceEntity(Entity en) {
		facePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));

	}

	public static void faceBlock(BlockPos blockPos) {
		facePos(getVec3(blockPos));
	}

	public static Vec3 getVec3(BlockPos blockPos) {
		return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3 vec) {
		return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public static void facePos(Vec3 vec) {
		if (ClientSettings.smoothAim) {
			smoothFacePos(vec);
			return;
		}
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Minecraft.getMinecraft().thePlayer.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw);
		Minecraft.getMinecraft().thePlayer.rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch);
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePos(Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	/**
	 * 
	 * 
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosRemote(Vec3 src, Vec3 dest) {
		double diffX = dest.xCoord - src.xCoord;
		double diffY = dest.yCoord - (src.yCoord);
		double diffZ = dest.zCoord - src.zCoord;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {MathHelper.wrapAngleTo180_float(yaw),
				MathHelper.wrapAngleTo180_float(pitch) };
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntity(Entity en) {
		if (en == null) {
			return new float[] { Minecraft.getMinecraft().thePlayer.rotationYawHead,
					Minecraft.getMinecraft().thePlayer.rotationPitch };
		}
		return getFacePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
		if (en == null) {
			return new float[] { facing.rotationYawHead, facing.rotationPitch };
		}
		return getFacePosRemote(new Vec3(facing.posX, facing.posY + en.getEyeHeight(), facing.posZ),
				new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ));
	}

	public static void smoothFacePos(Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		boolean aim = false;
		float max = 5;
		float yawChange = 0;
		if ((MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw)) > max * 2) {
			aim = true;
			yawChange = max;
		} else if ((MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw)) < -max * 2) {
			aim = true;
			yawChange = -max;
		}
		float pitchChange = 0;
		if ((MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)) > max * 4) {
			aim = true;
			pitchChange = max;
		} else if ((MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)) < -max
				* 4) {
			aim = true;
			pitchChange = -max;
		}
		// Minecraft.getMinecraft().thePlayer.rotationYaw += yawChange;
		// Minecraft.getMinecraft().thePlayer.rotationPitch += pitchChange;
		if (aim) {
			Minecraft.getMinecraft().thePlayer.rotationYaw += (MathHelper
					.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
			Minecraft.getMinecraft().thePlayer.rotationPitch += (MathHelper
					.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
		}

	}

	public static void smoothFacePos(Vec3 vec, double addSmoothing) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		Minecraft.getMinecraft().thePlayer.rotationYaw += (MathHelper
				.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
		Minecraft.getMinecraft().thePlayer.rotationPitch += (MathHelper
				.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
	}

	// public static int getDistanceFromMouse(Entity entity)
	// {
	// float[] neededRotations = getRotationsNeeded(entity);
	// if(neededRotations != null)
	// {
	// float neededYaw =
	// Minecraft.getMinecraft().thePlayer.rotationYaw
	// - neededRotations[0], neededPitch =
	// Minecraft.getMinecraft().thePlayer.rotationPitch
	// - neededRotations[1];
	// float distanceFromMouse =
	// MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch
	// * neededPitch);
	// return (int)distanceFromMouse;
	// }
	// return -1;
	// }
	// public static float[] getRotationsNeeded(Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
	// double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapAngleTo180_float(yaw
	// - Minecraft.getMinecraft().thePlayer.rotationYaw) /
	// AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapAngleTo180_float(pitch
	// - Minecraft.getMinecraft().thePlayer.rotationPitch) /
	// AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// Minecraft.getMinecraft().thePlayer.rotationYaw
	// + MathHelper.wrapAngleTo180_float(yaw
	// - Minecraft.getMinecraft().thePlayer.rotationYaw),
	// Minecraft.getMinecraft().thePlayer.rotationPitch
	// + MathHelper.wrapAngleTo180_float(pitch
	// - Minecraft.getMinecraft().thePlayer.rotationPitch)};
	//
	// }
	// public static float[] getRotationsNeededRemote(EntityLivingBase remote,
	// Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - remote.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (remote.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (remote.posY + remote.getEyeHeight());
	// double diffZ = entity.posZ - remote.posZ;
	// double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapAngleTo180_float(yaw
	// - remote.rotationYaw) / AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapAngleTo180_float(pitch
	// - remote.rotationPitch) / AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// remote.rotationYaw
	// + MathHelper.wrapAngleTo180_float(yaw
	// - remote.rotationYaw),
	// remote.rotationPitch
	// + MathHelper.wrapAngleTo180_float(pitch
	// - remote.rotationPitch)};
	//
	// }
	public static float getPlayerBlockDistance(BlockPos blockPos) {
		return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
		float xDiff = (float) (Minecraft.getMinecraft().thePlayer.posX - posX);
		float yDiff = (float) (Minecraft.getMinecraft().thePlayer.posY - posY);
		float zDiff = (float) (Minecraft.getMinecraft().thePlayer.posZ - posZ);
		return getBlockDistance(xDiff, yDiff, zDiff);
	}

	public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
		return MathHelper.sqrt_float(
				(xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}

	public static ArrayList<EntityItem> getNearbyItems(int range) {
		ArrayList<EntityItem> eList = new ArrayList<EntityItem>();
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= range) {
				continue;
			}

			eList.add(e);
		}
		return eList;
	}

	public static EntityItem getClosestItem(float range) {
		float mindistance = range;
		EntityItem ee = null;
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	public static Entity getClosestItemOrXPOrb(float range) {
		float mindistance = range;
		Entity ee = null;
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem) && !(o instanceof EntityXPOrb)) {
				continue;
			}
			Entity e = (Entity) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	private final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange)
			change = maxChange;
		else if (change < -maxChange)
			change = -maxChange;
		return current + change;
	}

	public static double normalizeAngle(double angle) {
		return (angle + 360) % 360;
	}

	public static float normalizeAngle(float angle) {
		return (angle + 360) % 360;
	}

	public static int getItemIndexHotbar(int itemID) {
		for (int i = 0; i < 9; i++) {
			ItemStack stackInSlot = mc.thePlayer.inventory.getStackInSlot(i);
			if (stackInSlot == null) {
				continue;
			}
			if (itemID == Item.getIdFromItem(stackInSlot.getItem())) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isBlockPosAir(BlockPos blockPos) {
		return mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
	}

	public static Block getBlockRelativeToEntity(Entity en, double d) {
		return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static BlockPos getBlockPosRelativeToEntity(Entity en, double d) {
		return new BlockPos(en.posX, en.posY + d, en.posZ);
	}
	
	public static Block getBlock(BlockPos pos) {
		return mc.theWorld.getBlockState(pos).getBlock();
	}
	
	private static Vec3 lastLoc = null;
	
	public static Vec3 getLastGroundLocation() {
		return lastLoc;
		
	}
	
	public static void updateLastGroundLocation() {
		if(mc.thePlayer.onGround) {
			lastLoc = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		}
	}
	
	public static double getXZDist(Vec3 loc1, Vec3 loc2) {
		double xDist = loc1.getX() - loc2.getX();
		double zDist = loc1.getZ() - loc2.getZ();
		return Math.abs(Math.sqrt(xDist * xDist + zDist * zDist));
	}
	
	public static IBlockState getBlockState(BlockPos blockPos) {
		return mc.theWorld.getBlockState(blockPos);
	}
}
