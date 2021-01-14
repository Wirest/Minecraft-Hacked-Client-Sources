package info.sigmaclient.module.impl.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.Timer;
import info.sigmaclient.event.RegisterEvent;
public class AutoArmor extends Module {

	public static final String DELAY = "DELAY";
	public static final String MODE = "MODE";
	private Timer timer = new Timer();
    public AutoArmor(ModuleData data) {
        super(data);
        settings.put(DELAY, new Setting<>(DELAY, 1, "Inventory clicks delay.", 1, 0, 10));
		settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Basic",new String[] {"Basic", "OpenInv", "FakeInv"}), "InvManager method."));
    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        String mode = ((Options) settings.get(MODE).getValue()).getSelected();
        this.setSuffix(mode);
    	if(Client.getModuleManager().isEnabled(InventoryManager.class))
    		return;
    	long delay = ((Number) settings.get(DELAY).getValue()).longValue()*50;
        if(mode.equalsIgnoreCase("OpenInv") && !(mc.currentScreen instanceof GuiInventory)){
        	return;
        }
        if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
        	if(timer.check(delay)){
        		getBestArmor();
        	}
        }
    }
    
    public void getBestArmor(){
    	for(int type = 1; type < 5; type++){
    		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
    			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
    			if(isBestArmor(is, type)){
    				continue;
    			}else{
    				if( ((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("FakeInv")){
        				C16PacketClientStatus p = new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT);
        				mc.thePlayer.sendQueue.addToSendQueue(p);
    				}
    				drop(4 + type);
    			}
    		}
    		for (int i = 9; i < 45; i++) {
    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
    				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
    				if(isBestArmor(is, type) && getProtection(is) > 0){
    					shiftClick(i);
    					timer.reset();
    					if(((Number) settings.get(DELAY).getValue()).longValue() > 0)
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
    public void shiftClick(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public void drop(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
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
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack)/100d;   	
    	}
	    return prot;
    }
}
