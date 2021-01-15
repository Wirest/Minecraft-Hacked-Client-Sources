package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.TimerS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;

public class AutoArmor extends Module {

	private TimerS timer = new TimerS();
	
	public ModSetting[] getModSettings() {
    	SliderSetting<Number> autoarmordelay = new SliderSetting<Number>("Delay", ClientSettings.autoarmordelay, 1, 10, 0.0, ValueFormat.DECIMAL);
		
		return new ModSetting[] { autoarmordelay };
    }
	
	long delay = ((Number) ClientSettings.autoarmordelay).longValue()*50;

	public AutoArmor() {
		super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER, "Equips armor instantly.");
	}

	@Override
	public void onUpdate() {
		if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
        	if(timer.check(delay)){
        		getBestArmor();
        	}
		}
		super.onUpdate();
	}
		
		public void getBestArmor(){
	    	for(int type = 1; type < 5; type++){
	    		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
	    			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
	    			if(isBestArmor(is, type)){
	    				continue;
	    			}else{
	    				
	        				C16PacketClientStatus p = new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT);
	        				mc.thePlayer.sendQueue.addToSendQueue(p);
	    				
	    				drop(4 + type);
	    			}
	    		}
	    		for (int i = 9; i < 45; i++) {
	    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	    				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	    				if(isBestArmor(is, type) && getProtection(is) > 0){
	    					shiftClick(i);
	    					timer.reset();
	    					if(((Number) ClientSettings.autoarmordelay).longValue() > 0)
	    						return;
	    				}
	    			}
	            }
	        }
	    }
	    public static boolean isBestArmor(ItemStack stack, int type){
	    	float prot = getProtection(stack);
	    	String strType = "";
	    	if(type == 1){
	    		strType = "helmet";
	    	}else if(type == 2){
	    		strType = "chestplate";
	    	}else if(type == 3){
	    		strType = "leggings";
	    	}else if(type == 4){
	    		strType = "boots";
	    	}
	    	if(!stack.getUnlocalizedName().contains(strType)){
	    		return false;
	    	}
	    	for (int i = 5; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
	                	return false;
	            }
	        }
	    	return true;
	    }
	    
	    public void drop(int slot){
	    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	    }
	    
	    public void shiftClick(int slot){
	    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	    }
	    
	    public static float getProtection(ItemStack stack){
	    	float prot = 0;
	    	if ((stack.getItem() instanceof ItemArmor)) {
	    		ItemArmor armor = (ItemArmor)stack.getItem();
	    		prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;   	
	    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)/100d;   	
	    	}
		    return prot;
	    }

}
