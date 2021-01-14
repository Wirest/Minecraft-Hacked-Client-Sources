package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSafeWalk;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	private static boolean cooldown = false;
	public static float yaw;
    public static float pitch;

	public Scaffold() {
		super("Scaffold", Keyboard.KEY_G, Category.MOVEMENT);

		ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("New");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Scaffold Mode", this, "Hypixel", options));
		
		Shadow.instance.settingsManager.rSetting(new Setting("Tower Speed", this, 0.42, 0.1, 1, false));
		Shadow.instance.settingsManager.rSetting(new Setting("Tower", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("Silent", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("KeepY", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("SafeWalk", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("SRotations", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("BlockCount", this, true));
	}
	@EventTarget
	public void onUpdate(EventUpdate event) {
		String mode = Shadow.instance.settingsManager.getSettingByName("Scaffold Mode").getValString();
		this.setSuffix("§f" + this.getBlockCount() + " §fBlocks left");
		BlockPos playerBlock = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
		if(mc.theWorld.isAirBlock(playerBlock.add(0, -1, 0))) {
			if(mode.equalsIgnoreCase("Hypixel")) {
			if(isValidBlock(playerBlock.add(0, -2, 0))) {
				if(Shadow.instance.settingsManager.getSettingByName("Tower").getValBoolean()) {
					if(mc.gameSettings.keyBindJump.pressed) {
						mc.thePlayer.motionY = Shadow.instance.settingsManager.getSettingByName("Tower Speed").getValDouble();;
						mc.thePlayer.motionX = 0F;
						mc.thePlayer.motionZ = 0F;
						this.setDisplayName("Scaffold §7| Hypixel");
					}
				}
				
				setName("Scaffold");
				this.setDisplayName("Scaffold §7| Hypixel");
				//place(playerBlock.add(0, -2, 0), EnumFacing.UP);
	                place(playerBlock.add(0, -1, 0), EnumFacing.UP);
				}else if(isValidBlock(playerBlock.add(-1, -1, 0))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
				}else if(isValidBlock(playerBlock.add(1, -1, 0))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
				}else if(isValidBlock(playerBlock.add(0, -1, -1))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
				}else if(isValidBlock(playerBlock.add(0, -1, 1))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
				}else if(isValidBlock(playerBlock.add(1, -1, 1))) {
					if(isValidBlock(playerBlock.add(0, -1, 1))) {
						place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
					}
					place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
				}else if(isValidBlock(playerBlock.add(-1, -1, 1))) {
					if(isValidBlock(playerBlock.add(-1, -1, 0))) {
						place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
					}
					place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
				}else if(isValidBlock(playerBlock.add(-1, -1, -1))) {
					if(isValidBlock(playerBlock.add(0, -1, -1))) {
						place(playerBlock.add(0, -1, -1), EnumFacing.SOUTH);
					}
					place(playerBlock.add(-1, -1, 1), EnumFacing.WEST);
				}else if(isValidBlock(playerBlock.add(1, -1, -1))) {
					if(isValidBlock(playerBlock.add(1, -1, 0))) {
						place(playerBlock.add(1, -1, 0), EnumFacing.EAST);
					}
					place(playerBlock.add(1, -1, -1), EnumFacing.NORTH);
				}
			}
			
			if(mode.equalsIgnoreCase("New")) {
				if(isValidBlock(playerBlock.add(0, -2, 0))) {
					if (Shadow.instance.settingsManager.getSettingByName("Tower").getValBoolean()) {
						if(mc.gameSettings.keyBindJump.pressed) {
							mc.thePlayer.motionX = 0.0D;
					        mc.thePlayer.motionZ = 0.0D;
					        mc.thePlayer.motionY = Shadow.instance.settingsManager.getSettingByName("Tower Speed").getValDouble();
					        this.setDisplayName("Scaffold §7| New");
						}
					}
					if (Shadow.instance.settingsManager.getSettingByName("Tower").getValBoolean()) {
						//place(playerBlock.add(0, -2, 0), EnumFacing.UP);
						place(playerBlock.add(0, -1, 0), EnumFacing.UP);
					} else {
						place(playerBlock.add(0, -1, 0), EnumFacing.UP);
					}
					this.setDisplayName("Scaffold §7| New");
				} else if(isValidBlock(playerBlock.add(-1, -1, 0))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
				} else if(isValidBlock(playerBlock.add(1, -1, 0))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
				} else if(isValidBlock(playerBlock.add(0, -1, -1))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
				} else if(isValidBlock(playerBlock.add(0, -1, 1))) {
					place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
				} else if(isValidBlock(playerBlock.add(1, -1, 1))) {
					if(isValidBlock(playerBlock.add(0, -1, 1))) {
						place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
					}
					place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
					
				}else if(isValidBlock(playerBlock.add(-1, -1, 1))) {
					if(isValidBlock(playerBlock.add(-1, -1, 0))) {
						place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
					}
					place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
					
				}/*else if(isValidBlock(playerBlock.add(-1, -1, -1))) {
					if(isValidBlock(playerBlock.add(0, -1, -1))) {
						place(playerBlock.add(0, -1, -1), EnumFacing.SOUTH);
					}
					place(playerBlock.add(-1, -1, 1), EnumFacing.WEST);
					
				}*/else if(isValidBlock(playerBlock.add(-1, -1, -1))) {
					if(isValidBlock(playerBlock.add(0, -1, -1))) {
						place(playerBlock.add(0, -1, -1), EnumFacing.SOUTH);
					}
					place(playerBlock.add(-2, -1, 0), EnumFacing.WEST);
					
				} else if(isValidBlock(playerBlock.add(1, -1, -1))) {
					if(isValidBlock(playerBlock.add(1, -1, 0))) {
						place(playerBlock.add(1, -1, 0), EnumFacing.EAST);
					}
					place(playerBlock.add(1, -1, -1), EnumFacing.NORTH);
				}
			}
		}
	}

	private boolean isValidBlock(BlockPos pos) {
		Block b = mc.theWorld.getBlockState(pos).getBlock();
		return (!(b instanceof BlockLiquid)) && (b.getMaterial() != Material.air);
	}
	
	private void place(BlockPos pos, EnumFacing face) {
		cooldown = true;
		if(face == EnumFacing.UP) {
			pos = pos.add(0, -1, 0);
		}else if(face == EnumFacing.NORTH) {
			pos = pos.add(0, 0, 1);
		}else if(face == EnumFacing.EAST) {
			pos = pos.add(-1, 0, 0);
		}else if(face == EnumFacing.SOUTH) {
			pos = pos.add(0, 0, -1);
		}else if(face == EnumFacing.WEST) {
			pos = pos.add(1, 0, 0);
		}
		if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
			for (int i = 0; i < 9; i++) {
				ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
				if (item != null && item.getItem() instanceof ItemBlock) {
					int last = mc.thePlayer.inventory.currentItem;
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 0));
		            mc.thePlayer.inventory.currentItem = i - 0;
		            mc.playerController.updateController();
		            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, face, new Vec3(0.5D, 0.5D, 0.5D));
		            if(!Shadow.instance.settingsManager.getSettingByName("Silent").getValBoolean()) {
						mc.thePlayer.swingItem();
					}
		            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		            mc.thePlayer.inventory.currentItem = last;
		            mc.playerController.updateController();
        	}
		}
			double var4 = pos.getX() + 0.25D - mc.thePlayer.posX;
			double var6 = pos.getZ() + 0.25D - mc.thePlayer.posZ;
			double var8 = pos.getY() + 0.25D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
			double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
			this.yaw = (float)(Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
			this.pitch = (float)-(Math.atan2(var8, var14) * 180.0D / Math.PI);
			int ticks = 0;
			ticks++;
			if(ticks >= 1000) {
				ticks = 0;
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, mc.thePlayer.onGround));
			}
		}
		if((mc.thePlayer.getHeldItem() != null) && ((mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))) {
			mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, face, new Vec3(0.5D, 0.5D, 0.5D));
			if(!Shadow.instance.settingsManager.getSettingByName("Silent").getValBoolean()) {
				mc.thePlayer.swingItem();
			}
			double var4 = pos.getX() + 0.25D - mc.thePlayer.posX;
			double var6 = pos.getZ() + 0.25D - mc.thePlayer.posZ;
			double var8 = pos.getY() + 0.25D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
			double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
			this.yaw = (float)(Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
			this.pitch = (float)-(Math.atan2(var8, var14) * 180.0D / Math.PI);
			int ticks = 0;
			ticks++;
			if(ticks >= 1000) {
				ticks = 0;
				//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, mc.thePlayer.onGround));
			}
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		Packet packet = event.getPacket();
		if(packet instanceof C03PacketPlayer)
            if(Shadow.instance.settingsManager.getSettingByName("SRotations").getValBoolean()) {
                C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
                packetPlayer.yaw = this.yaw;
                packetPlayer.pitch = this.pitch;
                getMc().thePlayer.renderYawOffset = yaw;
                mc.thePlayer.rotationYawHead = yaw;
                //mc.thePlayer.rotationYaw = yaw;
                //mc.thePlayer.rotationYawHead = yaw;
                //mc.thePlayer.rotationPitch = pitch;
            }
	}
	
	@EventTarget
	public void onSafewalk(EventSafeWalk event) {
		if(Shadow.instance.settingsManager.getSettingByName("SafeWalk").getValBoolean()) {
			if (getMc().thePlayer != null)
				event.setCancelled(Shadow.instance.settingsManager.getSettingByName("KeepY").getValBoolean() ? (!getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.onGround) : getMc().thePlayer.onGround);
		}
	}
	
	public static int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
        	if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock)) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
	@Override
    public void onEnable() {
		EventManager.register(this);
        super.onEnable();
    }
    @Override
    public void onDisable() {
    	EventManager.unregister(this);
        super.onDisable();
    }
}