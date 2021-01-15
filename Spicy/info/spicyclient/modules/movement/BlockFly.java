package info.spicyclient.modules.movement;

import java.util.Comparator;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.tests.xml.ItemContainer;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventRender3D;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventSneaking;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.settings.SettingChangeEvent.type;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.RenderUtils;
import info.spicyclient.util.RotationUtils;
import info.spicyclient.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BlockFly extends Module {
	
	public BlockFly() {
		super("Block Fly", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {
		
		lastYaw = mc.thePlayer.rotationYaw;
		lastPitch = mc.thePlayer.rotationPitch;
		
	}
	
	public void onDisable() {
		
	}
	
	public static transient float lastYaw = 0, lastPitch = 0;
	public static transient Timer timer = new Timer();
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSneaking) {
			
			if (e.isPre()) {
				
				if (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) {
					return;
				}
				
				EventSneaking sneak = (EventSneaking) e;
				
				if (sneak.entity.onGround && sneak.entity instanceof EntityPlayer) {
					sneak.sneaking = true;
				}else {
					sneak.sneaking = false;
				}
				sneak.offset = -1D;
				sneak.revertFlagAfter = true;
				
			}
			
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
			if (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) {
				return;
			}
			
			if (((EventPacket)e).packet instanceof S2FPacketSetSlot) {
				e.setCanceled(true);
			}
			
		}
		
		if (e instanceof EventSendPacket & e.isPre()) {
			
			if (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) {
				return;
			}
			
			if (((EventSendPacket)e).packet instanceof C03PacketPlayer) {
				((C03PacketPlayer)((EventSendPacket)e).packet).setYaw(lastYaw);
				((C03PacketPlayer)((EventSendPacket)e).packet).setPitch(lastPitch);
			}
			
		}
		
		if (e instanceof EventRender3D && e.isPre()) {
			
			BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
			
			for (int i = 0; i < 5; i++) {
				
				RenderUtils.drawLine(below.getX(), below.getY(), below.getZ(), below.getX() + 1, below.getY(), below.getZ());
				RenderUtils.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX() + 1, below.getY() + 1, below.getZ());
				RenderUtils.drawLine(below.getX(), below.getY(), below.getZ(), below.getX(), below.getY(), below.getZ() + 1);
				RenderUtils.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX(), below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX(), below.getY(), below.getZ(), below.getX(), below.getY() + 1, below.getZ());
				RenderUtils.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX(), below.getY() + 1, below.getZ());
				RenderUtils.drawLine(below.getX() + 1, below.getY(), below.getZ(), below.getX() + 1, below.getY() + 1, below.getZ());
				RenderUtils.drawLine(below.getX() + 1, below.getY() + 1, below.getZ(), below.getX() + 1, below.getY() + 1, below.getZ());
				RenderUtils.drawLine(below.getX(), below.getY(), below.getZ() + 1, below.getX(), below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX(), below.getY() + 1, below.getZ() + 1, below.getX(), below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX() + 1, below.getY(), below.getZ() + 1, below.getX(), below.getY(), below.getZ() + 1);
				RenderUtils.drawLine(below.getX() + 1, below.getY() + 1, below.getZ() + 1, below.getX(), below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX() + 1, below.getY(), below.getZ() + 1, below.getX() + 1, below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX() + 1, below.getY() + 1, below.getZ(), below.getX() + 1, below.getY() + 1, below.getZ() + 1);
				RenderUtils.drawLine(below.getX() + 1, below.getY(), below.getZ(), below.getX() + 1, below.getY(), below.getZ() + 1);
				
			}
			
		}
		
		if (e instanceof EventUpdate && e.isPre() && MovementUtils.isOnGround(0.4)) {
			
			if (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) {
				return;
			}
			
			//mc.thePlayer.onGround = false;
			
			EventUpdate update = (EventUpdate)e;
			
			double motionX = mc.thePlayer.motionX, motionZ = mc.thePlayer.motionZ;
			
			if (motionX < 0)
				motionX *= -1;
			
			if (motionZ < 0)
				motionZ *= -1;
			
			if (motionX < 0.05 && motionZ < 0.05 && MovementUtils.isOnGround(0.3) && mc.gameSettings.keyBindJump.isKeyDown()) {
				if (mc.gameSettings.keyBindJump.isKeyDown() && MovementUtils.isOnGround(0.25)) {
					
		            //mc.thePlayer.motionX = 0.0D;
		            //mc.thePlayer.motionZ = 0.0D;
		            //mc.thePlayer.motionY = 0.375;
		            if (timer.hasTimeElapsed(1500, true)) {
		               //mc.thePlayer.motionY = -0.28D;
		            }
					
				}
			}
			
			
		}
		
		if (e instanceof EventMotion && e.isPost()) {
			
			if (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) {
				return;
			}
			
			EventMotion event = (EventMotion) e;
			
			//mc.thePlayer.setSprinting(false);
			
			event.setYaw(lastYaw);
			event.setYaw(lastPitch);
			
			ItemStack i = mc.thePlayer.getCurrentEquippedItem();
			BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
			
			if (mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
				
				for (EnumFacing facing : EnumFacing.VALUES) {
					
					switch (facing) {
					case UP:
						
						BlockPos underBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2D, mc.thePlayer.posZ);
						
						if (mc.theWorld.getBlockState(underBelow).getBlock() != Blocks.air) {
							
							ItemStack block = mc.thePlayer.getCurrentEquippedItem();
							
							for (int g = 0; g < 9; g++) {
								
								if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize > 0 && !((ItemBlock)mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock().getLocalizedName().toLowerCase().contains("chest") && !((ItemBlock)mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock().getLocalizedName().toLowerCase().contains("table")) {
									
									InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
									inventoryplayer.setCurrentItem(mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem(), 0, false, true);
									
								}
								
							}
							
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, block, underBelow, EnumFacing.UP, RotationUtils.getVectorForRotation(getRotationsHypixel(underBelow, EnumFacing.UP)[1], getRotationsHypixel(underBelow, EnumFacing.UP)[1])) || true) {
								
								mc.thePlayer.swingItem();
								event.setYaw(getRotationsHypixel(underBelow, facing)[0]);
								event.setPitch(getRotationsHypixel(underBelow, facing)[1]);
								lastYaw = event.yaw;
								lastPitch = event.pitch;
								return;
								
							}
							return;
							
						}
						
						break;
					case NORTH:
					case EAST:
					case SOUTH:
					case WEST:
						
						BlockInfo defaultPos = findFacingAndBlockPosForBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ + mc.thePlayer.motionZ));
						
						if (defaultPos == null)
							return;
						
						if (mc.theWorld.getBlockState(defaultPos.pos).getBlock() != Blocks.air) {
							
							ItemStack block = mc.thePlayer.getCurrentEquippedItem();
							
							for (int g = 0; g < 9; g++) {
								
								if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize > 0 && !((ItemBlock)mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock().getLocalizedName().toLowerCase().contains("chest") && !((ItemBlock)mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock().getLocalizedName().toLowerCase().contains("table")) {
									
									InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
									inventoryplayer.setCurrentItem(mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem(), 0, false, true);
									
								}
								
							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, block, defaultPos.pos, defaultPos.facing, RotationUtils.getVectorForRotation(getRotationsHypixel(defaultPos.targetPos, defaultPos.facing)[0], getRotationsHypixel(defaultPos.targetPos, defaultPos.facing)[1])) || true) {
								
								mc.thePlayer.swingItem();
								event.setYaw(getRotationsHypixel(defaultPos.pos.offset(defaultPos.facing), defaultPos.facing)[0]);
								event.setPitch(getRotationsHypixel(defaultPos.pos.offset(defaultPos.facing), defaultPos.facing)[1]);
								lastYaw = event.yaw;
								lastPitch = event.pitch;
								return;
								
							}
							return;
							
						}
						
						break;
						
					}
					
				}
				
			}
			
		}
		
	}
	
	private BlockInfo findFacingAndBlockPosForBlock(BlockPos input) {
		
		BlockInfo output = new BlockInfo();
		output.pos = input;		
		
		for (EnumFacing face : EnumFacing.VALUES) {
			
			if (mc.theWorld.getBlockState(output.pos.offset(face)).getBlock() != Blocks.air) {
				
				output.pos = output.pos.offset(face);
				output.facing = face.getOpposite();
				output.targetPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
				return output;
				
			}
			
		}
		
		for (EnumFacing face : EnumFacing.VALUES) {
			
			if (mc.theWorld.getBlockState(output.pos.offset(face)).getBlock() == Blocks.air) {
				
				for (EnumFacing face1 : EnumFacing.VALUES) {
					
					if (mc.theWorld.getBlockState(output.pos.offset(face).offset(face1)).getBlock() != Blocks.air) {
						
						output.pos = output.pos.offset(face).offset(face1);
						output.facing = face.getOpposite();
						output.targetPos = output.pos.offset(face);
						return output;
						
					}
					
				}
				
			}
			
		}
		
		return null;
		
	}
	
	public float[] getRotationsHypixel(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
		double offset = 0.5;
		offset = 1;
        double d1 = (double)paramBlockPos.getX() + offset - mc.thePlayer.posX + (double)paramEnumFacing.getFrontOffsetX() / 2.0D;
        double d2 = (double)paramBlockPos.getZ() + offset - mc.thePlayer.posZ + (double)paramEnumFacing.getFrontOffsetZ() / 2.0D;
        double d3 = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - ((double)paramBlockPos.getY() + 0.5D);
        double d4 = (double)MathHelper.sqrt_double(d1 * d1 + d2 * d2);
        float f1 = (float)(Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);
        if (f1 < 0.0F) {
            f1 += 360.0F;
        }
        
        //f1 += 180;
        f1 = MathHelper.wrapAngleTo180_float(f1);
        f2 += 30;
        
        if (f2 > 90)
        	f2 = 90;
        
        if (f2 < -90)
        	f2 = -90;
        
        //Command.sendPrivateChatMessage(f1 + " : " + f2);
        
        return new float[]{f1, f2};
    }
	
	private class BlockInfo {
		
		BlockPos pos, targetPos;
		EnumFacing facing;
		
	}
	
}
