package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.RotationUtils;
import me.xatzdevelopments.xatz.utils.TimerS;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPotion extends Module {


    public static boolean potting;
    TimerS timer = new TimerS();

    public ModSetting[] getModSettings() {
    	CheckBtnSetting speed = new CheckBtnSetting("Use Speed Pots", "autopotspeed");
		CheckBtnSetting regen = new CheckBtnSetting("Use Regen Pots", "autopotregen");
		CheckBtnSetting predict = new CheckBtnSetting("Predict your position", "autopotpredict");
		SliderSetting<Number> autopothealth = new SliderSetting<Number>("Health", ClientSettings.autopothealth, 0.5, 10, 0.0, ValueFormat.INT);
		return new ModSetting[]{speed, regen, predict, autopothealth};
    }
    
	public AutoPotion() {
		super("AutoPotion", Keyboard.KEY_NONE, Category.COMBAT, "Throws potions automatically");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		final boolean speed = (Boolean) ClientSettings.autopotspeed;
        final boolean regen = (Boolean) ClientSettings.autopotregen;
		if(timer.check(200)){
        	if(potting)
        		potting = false;
        }
        int spoofSlot = getBestSpoofSlot();
        int pots[] = {6,-1,-1};
        if(regen)
        	pots[1] = 10;
        if(speed)
        	pots[2] = 1;
        
        for(int i = 0; i < pots.length; i ++){
        	if(pots[i] == -1)
        		continue;
        	if(pots[i] == 6 || pots[i] == 10){
        		if(timer.check(900) && !mc.thePlayer.isPotionActive(pots[i])){
            		if(mc.thePlayer.getHealth() < ((Number)ClientSettings.autopothealth).doubleValue()*2){
            			getBestPot(spoofSlot, pots[i]);
            		}
        		}
        	}else
        	if(timer.check(1000) && !mc.thePlayer.isPotionActive(pots[i])){
        		getBestPot(spoofSlot, pots[i]);               		
        	}
        }
        
		super.onUpdate();
	}
	
	int getBestSpoofSlot(){  	
    	int spoofSlot = 5;
    	for (int i = 36; i < 45; i++) {       		
    		if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
     			spoofSlot = i - 36;
     			break;
            }else if(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
            	spoofSlot = i - 36;
     			break;
            }
        }
    	return spoofSlot;
    }
	
	 public void swap(int slot1, int hotbarSlot){
	    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	    }
	 
	 float[] getRotations(){    	
	        double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 26.0D;
	        double movedPosY = mc.thePlayer.boundingBox.minY - 3.6D;
	        double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 26.0D;	
	        if((Boolean) ClientSettings.autopotpredict)
	        	return RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
	        else
	        	return new float[]{mc.thePlayer.rotationYaw, 90};
	    }
	
	void getBestPot(int hotbarSlot, int potID){
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() &&(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
              	  ItemPotion pot = (ItemPotion)is.getItem();
              	  if(pot.getEffects(is).isEmpty())
              		  return;
              	  PotionEffect effect = (PotionEffect) pot.getEffects(is).get(0);              	  
                  int potionID = effect.getPotionID();
                  if(potionID == potID)
              	  if(ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)){
              		  if(36 + hotbarSlot != i)
              			  swap(i, hotbarSlot);
              		  timer.reset();
              		  boolean canpot = true;
              		  int oldSlot = mc.thePlayer.inventory.currentItem;
              		  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
          			  potting = true;
          			  break;
              	  }               	  
                }              
            }
        }
    }
	
	boolean isBestPot(ItemPotion potion, ItemStack stack){
    	if(potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1)
    		return false;
        PotionEffect effect = (PotionEffect) potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier(); 
        int duration = effect.getDuration();
    	for (int i = 9; i < 45; i++) {    		
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {           	
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                	ItemPotion pot = (ItemPotion)is.getItem();
                	 if (pot.getEffects(is) != null) {
                         for (Object o : pot.getEffects(is)) {
                             PotionEffect effects = (PotionEffect) o;
                             int id = effects.getPotionID();
                             int ampl = effects.getAmplifier(); 
                             int dur = effects.getDuration();
                             if (id == potionID && ItemPotion.isSplash(is.getItemDamage())){
                            	 if(ampl > amplifier){
                            		 return false;
                            	 }else if (ampl == amplifier && dur > duration){
                            		 return false;
                            	 }
                             }                            
                         }
                     }
                }
            }
        }
    	return true;
    }

	
}
