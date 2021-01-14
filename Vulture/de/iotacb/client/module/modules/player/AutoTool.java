package de.iotacb.client.module.modules.player;

import org.lwjgl.input.Mouse;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.player.InventoryUtil;
import de.iotacb.client.utilities.values.Value;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.texture.Stitcher.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "AutoTool", description = "Automatically switches to the best tool", category = Category.PLAYER)
public class AutoTool extends Module {

	int slot;
	
	@Override
	public void onInit() {
		addValue(new Value("AutoToolSilent", true));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null) return;
		if (slot != getMc().thePlayer.inventory.currentItem) {
			getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
		}
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getMc().objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !Mouse.isButtonDown(0)) {
			if (slot != getMc().thePlayer.inventory.currentItem) {
				getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
				Client.INVENTORY_UTIL.itemSlot = getMc().thePlayer.inventory.currentItem;
			}
			return;
		}
		final BlockPos hoveredBlock = getMc().objectMouseOver.getBlockPos();
		
		if (hoveredBlock.getBlock() instanceof BlockAir || hoveredBlock == null) return;
		
		final int bestToolSlot = Client.INVENTORY_UTIL.findBestTool(hoveredBlock);
		
		if (bestToolSlot == -1) return;
		
		if (!getValueByName("AutoToolSilent").getBooleanValue()) {
			getMc().thePlayer.inventory.currentItem = bestToolSlot;
		} else {
			Client.INVENTORY_UTIL.itemSlot = bestToolSlot;
			getMc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(bestToolSlot));
		}
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (getMc().thePlayer == null) return;
		
		if (event.getPacket() instanceof C09PacketHeldItemChange) {
			slot = ((C09PacketHeldItemChange) event.getPacket()).getSlotId();
		}
	}

}
