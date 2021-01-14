package de.iotacb.client.module.modules.world;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.SafewalkEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.player.InventoryUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.renderer.BlockModelRenderer.EnumNeighborInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing.Plane;

@ModuleInfo(name = "Tower", description = "Automatically builds blocks under you", category = Category.WORLD)
public class Tower extends Module {
	
	private Timer placeDelay;
	
	private int slot;
	
	@Override
	public void onInit() {
		this.placeDelay = new Timer();
		
		addValue(new Value("TowerSilent", true));
		addValue(new Value("TowerTimer", false));
		addValue(new Value("TowerAuto centering", false));
		addValue(new Value("TowerNo swing", false));
		addValue(new Value("TowerDisplay block count", true));
		addValue(new Value("TowerDelay", 200, new ValueMinMax(0, 2000, 1)));
		addValue(new Value("TowerTimer speed", 2, new ValueMinMax(.1, 6, .1)));
		addValue(new Value("TowerModes", "Jump", "Motion", "Cubecraft"));
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("TowerTimer speed").setEnabled(getValueByName("TowerTimer").getBooleanValue());
		super.updateValueStates();
	}
	
	@Override
	public void onEnable() {
		if (getValueByName("TowerAuto centering").getBooleanValue()) {
			final Vec3 pos = getMc().thePlayer.getPositionVector();
			getMc().thePlayer.setPosition(pos.getBlockPos().getVec3().addVector(.5, 0, .5));
		}
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null) return;
		getMc().gameSettings.keyBindSneak.pressed = false;
		if (slot != getMc().thePlayer.inventory.currentItem) {
			getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
		}
		if (getValueByName("TowerModes").isCombo("Cubecraft")) {
			getMc().thePlayer.motionY = -.5;
		} else {
			getMc().thePlayer.motionY = .2;
		}
		getMc().timer.timerSpeed = 1F;
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
			if (!getMc().gameSettings.keyBindJump.pressed) return;
		}
		if (event.getState() == UpdateState.PRE) {
			
			if (getMc().thePlayer.getHeldItem() == null || !(getMc().thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
				if (getValueByName("TowerSilent").getBooleanValue()) {
					if (!Client.INVENTORY_UTIL.hasBlockInHotbar()) {
						return;
					}
				} else {
					return;
				}
			}
			
			final float pitch = 79.44F;
			
			getMc().thePlayer.rotationPitchHead = pitch;

			event.setPitch(pitch);
		}
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
			if (!getMc().gameSettings.keyBindJump.pressed) {
				return;
			}
		}
		if (getValueByName("TowerModes").isCombo("Motion")) {
			if (Client.MOVEMENT_UTIL.isMoving()) {
				return;
			}
		}
		ItemStack stack = getMc().thePlayer.getHeldItem();
		
		int blockSlot = -1;
		if (getValueByName("TowerSilent").getBooleanValue()) {
			if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
				blockSlot = Client.INVENTORY_UTIL.findBlock(true);
				
				if (blockSlot == -1) return;
				
				getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
				stack = getMc().thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
			}
		} else {
			if (getMc().thePlayer.getHeldItem() == null || !(getMc().thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
				return;
			}
		}
		
		if ((placeDelay.delay2((long) getValueByName("TowerDelay").getNumberValue()) || getValueByName("TowerModes").isCombo("Cubecraft"))) {
			if (Client.ENTITY_UTIL.getBlockBelowPlayer() == Blocks.air) {
				if (getValueByName("TowerTimer").getBooleanValue() && !getMc().gameSettings.keyBindJump.pressed) {
					getMc().timer.timerSpeed = (float) getValueByName("TowerTimer speed").getNumberValue();
				}
			} else {
				if (getValueByName("TowerTimer").getBooleanValue()) {
					getMc().timer.timerSpeed = 1;
				}
			}
			if (getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, stack, getMc().thePlayer.getPositionVector().getBlockPos().down(2), EnumFacing.UP, blockDataToVec3(getMc().thePlayer.getPositionVector().getBlockPos().down(2), EnumFacing.UP))) {
				if (!getValueByName("TowerNo swing").getBooleanValue()) {
					getMc().thePlayer.swingItem();
				} else {
					getMc().getNetHandler().addToSendQueue(new C0APacketAnimation());
				}
				placeDelay.reset();
			}
			
			if (!Client.INVENTORY_UTIL.hasBlockInHotbar() && getValueByName("TowerModes").isCombo("Cubecraft")) {
				getMc().thePlayer.motionY = -1;
			}
		}
		
		if (getValueByName("TowerModes").isCombo("Motion")) {
			getMc().thePlayer.motionY = .42;
		} else if (getValueByName("TowerModes").isCombo("Cubecraft")) {
			if (getMc().thePlayer.onGround) {
				getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 1.1,
						getMc().thePlayer.posZ);
			} else {
				getMc().thePlayer.motionY = -100;
			}
		} else {
			if (getMc().thePlayer.onGround) {
				getMc().thePlayer.jump();
			}
		}
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
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
			if (!getMc().gameSettings.keyBindJump.pressed) return;
		}
		if (event.getState() == RenderState.TWOD) {
			if (!getValueByName("TowerDisplay block count").getBooleanValue()) return;
			final String blockCount = "Blocks: " + Client.INSTANCE.getClientColorCode() + getBlocksInHotbar();
			final double width = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(blockCount) + 5;
			final double height = Client.INSTANCE.getFontManager().getDefaultFont().getHeight(blockCount) + 5;
			final double posX = event.getSr().getScaledWidth() / 2 - width / 2;
			final double posY = event.getSr().getScaledHeight() - height / 2 - 80;
			Client.RENDER2D.rect(posX, posY, width, height, new Color(20, 20, 20));
			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(blockCount, posX + width / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(blockCount) / 2, posY + height / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(blockCount) / 2, Color.white);
		} else if (event.getState() == RenderState.THREED) {
			Client.RENDER3D.drawBox(getMc().thePlayer.getPositionVector().getBlockPos().add(0, -1, 0), Client.INSTANCE.getClientColor().setAlpha(100), false);
		}
	}
	
	@EventTarget
	public void onSafe(SafewalkEvent event) {
		event.setSafe(true);
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (getMc().thePlayer == null) return;
		
		if (event.getPacket() instanceof C09PacketHeldItemChange) {
			slot = ((C09PacketHeldItemChange) event.getPacket()).getSlotId();
		}
	}
    
	private int getBlocksInHotbar() {
    	int amount = 0;
    	for (int i = 36; i < 45; i++) {
    		final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
    		if (stack == null) continue;
    		if (stack.getItem() instanceof ItemBlock) {
    			amount += stack.stackSize;
    		}
    	}
    	return amount;
    }
}
