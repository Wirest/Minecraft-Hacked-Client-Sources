package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class AutoEat extends Module {
	private int oldSlot;
	   private int bestSlot;

	   public AutoEat() {
	      super("AutoEat", 0, Category.PLAYER);
	   }

	   @EventTarget
	   public void onUpdate(EventUpdate event) {
	      if (this.oldSlot == -1) {
	         if (!this.canEat()) {
	            return;
	         }

	         float bestSaturation = 0.0F;

	         for(int i = 0; i < 9; ++i) {
	            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
	            if (this.isFood(stack)) {
	               ItemFood food = (ItemFood)stack.getItem();
	               float saturation = food.getSaturationModifier(stack);
	               if (saturation > bestSaturation) {
	                  bestSaturation = saturation;
	                  this.bestSlot = i;
	               }
	            }
	         }

	         if (this.bestSlot != -1) {
	            this.oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
	         }
	      } else {
	         if (!this.canEat()) {
	            this.stop();
	            return;
	         }

	         if (!this.isFood(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(this.bestSlot))) {
	            this.stop();
	            return;
	         }

	         Minecraft.getMinecraft().thePlayer.inventory.currentItem = this.bestSlot;
	         Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed = true;
	      }

	   }

	   private boolean canEat() {
	      if (!Minecraft.getMinecraft().thePlayer.canEat(false)) {
	         return false;
	      } else {
	         if (Minecraft.getMinecraft().objectMouseOver != null) {
	            Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
	            if (entity instanceof EntityVillager || entity instanceof EntityTameable) {
	               return false;
	            }

	            BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
	            if (pos != null) {
	               Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	               if (block instanceof BlockContainer || block instanceof BlockWorkbench) {
	                  return false;
	               }
	            }
	         }

	         return true;
	      }
	   }

	   private boolean isFood(ItemStack stack) {
	      return stack.getItem() instanceof ItemFood;
	   }

	   private void stop() {
	      Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed = false;
	      Minecraft.getMinecraft().thePlayer.inventory.currentItem = this.oldSlot;
	      this.oldSlot = -1;
	   }
	   
	   @Override
	   public void onEnable() {
	      this.oldSlot = -1;
	      this.bestSlot = -1;
	      super.onEnable();
	   }
	   
	   @Override
	public void onDisable() {
		super.onDisable();
	}
}

