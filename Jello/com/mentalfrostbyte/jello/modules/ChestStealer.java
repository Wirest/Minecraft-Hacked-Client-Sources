package com.mentalfrostbyte.jello.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class ChestStealer extends Module {

	public Random rand = new Random();
	public TimerUtil timer = new TimerUtil();
	
	public ChestStealer() {
        super("Chest Stealer", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(1);
    }
   
	public void onEnable(){
	}
	
	public void onDisable(){
	}
	
    public void onUpdate(){
    	if(!this.isToggled())
    		return;
    	GuiChest chest;
        if (this.mc.currentScreen instanceof GuiChest && !Jello.inventoryUtil.inventoryIsFull() && !Jello.chestUtil.chestIsEmpty(chest = (GuiChest)this.mc.currentScreen)) {
        	Jello.chestUtil.chestSlots.clear();
        	Jello.chestUtil.findChestSlots(chest);
        	if (!Jello.chestUtil.chestSlots.isEmpty() && (chest.lowerChestInventory.getDisplayName().getUnformattedText().equals("Large Chest") || chest.lowerChestInventory.getDisplayName().getUnformattedText().equals("Chest") || chest.lowerChestInventory.getDisplayName().getUnformattedText().equals("LOW"))) {
            	
            	Random random = new Random();
                int randomSlot = random.nextInt(Jello.chestUtil.chestSlots.size());
                if (this.mc.thePlayer.ticksExisted % 4 == 0) {
                    this.mc.playerController.windowClick(chest.inventorySlots.windowId, Jello.chestUtil.chestSlots.get(randomSlot), 0, 1, this.mc.thePlayer);
                   
                }
               
            }
        }else if(this.mc.currentScreen instanceof GuiChest){
        	mc.thePlayer.closeScreen();
        	int keycode = mc.gameSettings.keyBindForward.getKeyCode();
        	int keycode1 = mc.gameSettings.keyBindBack.getKeyCode();
        	int keycode2 = mc.gameSettings.keyBindLeft.getKeyCode();
        	int keycode3 = mc.gameSettings.keyBindRight.getKeyCode();
    		
    		if(Keyboard.isKeyDown(keycode)){
    			mc.gameSettings.keyBindForward.pressed = true;
    		}
    		if(Keyboard.isKeyDown(keycode1)){
    			mc.gameSettings.keyBindBack.pressed = true;
    		}
    		if(Keyboard.isKeyDown(keycode2)){
    			mc.gameSettings.keyBindLeft.pressed = true;
    		}
    		if(Keyboard.isKeyDown(keycode3)){
    			mc.gameSettings.keyBindRight.pressed = true;
    		}
        }

    		
    }
	
	
}
