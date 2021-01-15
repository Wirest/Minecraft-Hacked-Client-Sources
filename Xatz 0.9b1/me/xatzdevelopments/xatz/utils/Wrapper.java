package me.xatzdevelopments.xatz.utils;

import java.awt.Font;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import org.lwjgl.opengl.GL11;

import me.xatzdevelopments.xatz.client.NahrFont;

public final class Wrapper {
	private static long currentMS = 0L;
	protected static long lastMS = -1L;
	public static float yaw;
	public static float pitch;
	private static NahrFont guiFont;
	private static NahrFont arrayFont;
	private static NahrFont chatFont;
	private static String IRCChatPrefix = "§7[§3" + "IRC" + "§7] §7";
    
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

	public static WorldClient getWorld() {
		return Wrapper.mc().theWorld;
	}

	public static void drawBetterCenteredString(String text, int x, int y, int color) {
		//Wrapper.drawBetterString(text, (float) (x - Wrapper.fr().getStringWidth(text) / 2), (float) y, color);
	}

	public final static NahrFont getGuiFont() {
		if (guiFont == null) {
			guiFont = new NahrFont(new Font("Lucida Console", 1, 14), 14.0F);
		}
		return guiFont;
	}
	public final static NahrFont getChatFontTotallyNotTakenFromInnocentJustLikeTheIRCWasnt() {
		if (chatFont == null) {
			chatFont = new NahrFont(new Font("Verdana Bold", 1, 18), 14.0F);
		}
		return chatFont;
	}

	public final static NahrFont getArrayFont() {
		if (arrayFont == null) {
			arrayFont = new NahrFont(new Font("Lucida Console", 1, 11), 14.0F);
		}
		return arrayFont;
	}

	public static Block getBlockAtPos(BlockPos inBlockPos) {
		BlockPos currentPos = inBlockPos;
		IBlockState s = Wrapper.getWorld().getBlockState(currentPos);
		return s.getBlock();
	}

	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}

	public static Block getBlock(int x, int y, int z) {
		return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public static EntityPlayerSP getPlayer() {
		return Wrapper.mc().thePlayer;
	}

	public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double blocks) {
		blocks += inPlayer.height;
		return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + blocks, inPlayer.posZ));
	}

	public static String getBlockName(int block) {
		return new ItemStack(Block.getBlockById(block)).getDisplayName();
	}

	public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0D, inPlayer.posZ));
	}

	public static void tellPlayer(String message) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("\2478[§9Xatz\2478]\247r: " + message));
	}
	public static void addIRCChatMessage(String chatMessage) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(IRCChatPrefix + chatMessage));
	}

	public static void invalidCommand(String s) {
		Wrapper.tellPlayer("\2477Invalid command for " + "\247a"+ s);
	}

	public static FontRenderer fr() {
		return mc().fontRendererObj;
	}

	public static FontRenderer bf() {
		return mc().fontRendererObj;
	}

	public static void rapecpu() {
		Wrapper.mc().gameSettings.thirdPersonView = 1;
		for (Object o : Wrapper.getWorld().loadedEntityList) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				entity.setArrowCountInEntity(999999999);
			}
		}
	}

	public static void customString(String text, int x, int y, int color, float size) {
		GL11.glPushMatrix();
		GlStateManager.scale(size, size, size);
		Gui.drawString(Wrapper.bf(), text, x, y, color);
		GlStateManager.scale(1, 1, 1);
		GL11.glPopMatrix();
	}

	public static void sendPacketQueue(Packet packet) {
		NetHandlerPlayClient.get().netManager.sendPacket(packet);
	}

	public static void sendPacket(Packet packet) {
		Wrapper.getPlayer().sendQueue.addToSendQueue(packet);
	}

	public final static void updateMS() {
		currentMS = System.currentTimeMillis();
	}

	public final static void updateLastMS() {
		lastMS = System.currentTimeMillis();
	}

	public final static boolean hasTimePassedM(long MS) {
		return currentMS >= lastMS + MS;
	}

	public final static boolean hasTimePassedS(float speed) {
		return currentMS >= lastMS + (long) (1000 / speed);
	}
	public static void blinkToPos(double[] startPos, BlockPos endPos, double slack) {
		double curX = startPos[0];
		double curY = startPos[1];
		double curZ = startPos[2];
		double endX = endPos.getX() + 0.5D;
		double endY = endPos.getY() + 1.0D;
		double endZ = endPos.getZ() + 0.5D;

		double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
		int count = 0;
		while (distance > slack) {
			distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
			if (count > 120) {
				break;
			}
			boolean next = false;
			double diffX = curX - endX;
			double diffY = curY - endY;
			double diffZ = curZ - endZ;

			double offset = (count & 0x1) == 0 ? 0.4D : 0.1D;
			if (diffX < 0.0D) {
				if (Math.abs(diffX) > offset) {
					curX += offset;
				} else {
					curX += Math.abs(diffX);
				}
			}
			if (diffX > 0.0D) {
				if (Math.abs(diffX) > offset) {
					curX -= offset;
				} else {
					curX -= Math.abs(diffX);
				}
			}
			if (diffY < 0.0D) {
				if (Math.abs(diffY) > 0.25D) {
					curY += 0.25D;
				} else {
					curY += Math.abs(diffY);
				}
			}
			if (diffY > 0.0D) {
				if (Math.abs(diffY) > 0.25D) {
					curY -= 0.25D;
				} else {
					curY -= Math.abs(diffY);
				}
			}
			if (diffZ < 0.0D) {
				if (Math.abs(diffZ) > offset) {
					curZ += offset;
				} else {
					curZ += Math.abs(diffZ);
				}
			}
			if (diffZ > 0.0D) {
				if (Math.abs(diffZ) > offset) {
					curZ -= offset;
				} else {
					curZ -= Math.abs(diffZ);
				}
			}
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
			count++;
		}
	}
	 public static int getPlayerPing(final String name) {
	        final EntityPlayer player = mc().theWorld.getPlayerEntityByName(name);
	        if (player instanceof EntityOtherPlayerMP) {
	            return ((EntityOtherPlayerMP)player).playerInfo.getResponseTime();
	        }
	        return 0;
	    }
	public static boolean isMoving() {
		if ((Wrapper.getPlayer().lastTickPosX != Wrapper.getPlayer().posX) || (Wrapper.getPlayer().lastTickPosZ != Wrapper.getPlayer().posZ) || (Wrapper.getPlayer().posY != Wrapper.getPlayer().lastTickPosY)) {
			return true;
		} else
			return false;
	}

	public static void drawBorderRect(int left, int top, int right, int bottom, int bcolor, int icolor, int f) {
		Gui.drawRect(left + f, top + f, right - f, bottom - f, icolor);
		Gui.drawRect(left, top, left + f, bottom, bcolor);
		Gui.drawRect(left + f, top, right, top + f, bcolor);
		Gui.drawRect(left + f, bottom - f, right, bottom, bcolor);
		Gui.drawRect(right - f, top + f, right, bottom - f, bcolor);
	}
}