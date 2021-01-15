package info.spicyclient.modules.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.ClickGUI.ClickGUI;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.settings.SettingChangeEvent.type;
import info.spicyclient.util.InventoryUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;

public class InventoryManager extends Module {
	
	public BooleanSetting purge = new BooleanSetting("Purge items", false);
	public BooleanSetting sort = new BooleanSetting("Sort items", false);
	
	// Different settings for when purge is enabled
	public BooleanSetting armor = new BooleanSetting("Purge armor", false);
	public BooleanSetting tools = new BooleanSetting("Purge tools", false);
	public NumberSetting blocks = new NumberSetting("Max amount of blocks", 64, 0, 512, 16);
	public NumberSetting food = new NumberSetting("Max amount of food", 64, 0, 64, 1);
	
	// Different settings for when sort is enabled
	public NumberSetting swordSlot = new NumberSetting("Sword slot", 1, 1, 9, 1);
	public NumberSetting pickaxeSlot = new NumberSetting("Pickaxe slot", 2, 1, 9, 1);
	public NumberSetting axeSlot = new NumberSetting("Axe slot", 3, 1, 9, 1);
	
	public static transient boolean isInventoryOpen = false;
	
	public InventoryManager() {
		super("Inventory Manager", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(purge, armor, tools, blocks, food, sort, swordSlot);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
		if (e.settingType.equals(type.BOOLEAN)) {
			
			if (e.setting == null) {
				
			}else {
				
				if (purge == null) {
					
				}
				else if (e.setting.equals(purge)) {
					
					if (purge.isEnabled()) {
						if (!settings.contains(armor)) {
							settings.add(armor);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (!settings.contains(tools)) {
							settings.add(tools);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (!settings.contains(blocks)) {
							settings.add(blocks);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (!settings.contains(food)) {
							settings.add(food);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
					}
					else if (!purge.enabled){
						if (settings.contains(armor)) {
							settings.remove(armor);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (settings.contains(tools)) {
							settings.remove(tools);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (settings.contains(blocks)) {
							settings.remove(blocks);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (settings.contains(food)) {
							settings.remove(food);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						
					}
					
				}
				
				if (sort == null) {
					
				}
				else if (e.setting.equals(sort)) {
					
					if (sort.isEnabled()) {
						if (!settings.contains(swordSlot)) {
							settings.add(swordSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (!settings.contains(pickaxeSlot)) {
							settings.add(pickaxeSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (!settings.contains(axeSlot)) {
							settings.add(axeSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
					}
					else if (!sort.enabled){
						if (settings.contains(swordSlot)) {
							settings.remove(swordSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (settings.contains(pickaxeSlot)) {
							settings.remove(pickaxeSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						if (settings.contains(axeSlot)) {
							settings.remove(axeSlot);
							this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						}
						
					}
					
				}else {
					
				}
				
			}
			
		}
		
		this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
		
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket && e.isPre()) {
			
			EventSendPacket event = (EventSendPacket) e;
			
			if (event.packet instanceof C02PacketUseEntity || event.packet instanceof C07PacketPlayerDigging) {
				
				SpicyClient.config.autoArmor.timer.reset();
				
			}
			
		}
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) {
				return;
			}
			
			this.additionalInformation = "Hypixel";
			
			if (!SpicyClient.config.autoArmor.timer.hasTimeElapsed(250 + new Random().nextInt(40), false)) {
				return;
			}
			
			boolean packetSent = false;
			int blockAmount = 0, foodAmount = 0;
			
			boolean sword = false;
			boolean pickaxe = false;
			boolean axe = false;
			
			for (int i = 9; i < 45; i++) {
    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
    				
    				ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
    				
    				if (sort.isEnabled()) {
    					
	    				if(InventoryUtils.isBestWeapon(item) && item.getItem() instanceof ItemSword){
	    					
	        				if (!(mc.currentScreen instanceof GuiInventory)) {
	        					packetSent = true;
	            				
	            				
	        				}
	        				
	        				openInv();
	    					InventoryUtils.swap(i, (int)swordSlot.getValue() - 1);
	    					sword = true;
	    					
	    				}
	    				
	    				if(InventoryUtils.isBestPickaxe(item) && item.getItem() instanceof ItemPickaxe){
	        				if (!(mc.currentScreen instanceof GuiInventory)) {
	        					packetSent = true;
	            				
	            				
	        				}
	        				
	        				openInv();
	    					InventoryUtils.swap(i, (int)pickaxeSlot.getValue() - 1);
	    					pickaxe = true;
	    					
	    				}
	    				
	    				if(InventoryUtils.isBestAxe(item) && item.getItem() instanceof ItemAxe){
	        				if (!(mc.currentScreen instanceof GuiInventory)) {
	        					packetSent = true;
	            				
	            				
	        				}
	        				
	        				openInv();
	    					InventoryUtils.swap(i, (int)axeSlot.getValue() - 1);
	    					axe = true;
	    					
	    				}
	    				
    				}
    				
    			}
    			
            }
			
			for (int i = 9; i < 45; i++) {
    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
    				
    				ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
    				
    				if (purge.isEnabled()) {
    					
	    				if(item.getItem() instanceof ItemBlock){
	    					
	    					blockAmount += item.stackSize;
	    					
	    					if (blockAmount > blocks.getValue()) {
	    						
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
	    					}
	    					
	    				}
	    				
	    				if(item.getItem() instanceof ItemFood){
	    					
	    					if (foodAmount > blocks.getValue()) {
	    						
	    						foodAmount += item.stackSize;
	    						
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
	    					}
	    					
	    				}
	    				
	    				if (tools.isEnabled()) {
	    					
		    				if (sword) {
		    					
		    					if(item.getItem() instanceof ItemSword && i - 36 != swordSlot.getValue() - 1){
		    						
			        				if (!(mc.currentScreen instanceof GuiInventory)) {
			        					packetSent = true;
			            				
			            				
			        				}
			        				
			        				openInv();
			    					InventoryUtils.drop(i);
			    					SpicyClient.config.autoArmor.timer.reset();
			    					closeInv();
			    					return;
			    					
		    					}
		    					
		    				}else if (item.getItem() instanceof ItemSword) {
		    					
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
		    				}
		    				
		    				if (pickaxe) {
		    					
		    					if(item.getItem() instanceof ItemPickaxe && i - 36 != pickaxeSlot.getValue() - 1){
		    						
			        				if (!(mc.currentScreen instanceof GuiInventory)) {
			        					packetSent = true;
			            				
			            				
			        				}
			        				
			        				openInv();
			    					InventoryUtils.drop(i);
			    					SpicyClient.config.autoArmor.timer.reset();
			    					closeInv();
			    					return;
			    					
		    					}
		    					
		    				}else if (item.getItem() instanceof ItemPickaxe && !(item.getItem() instanceof ItemSword)) {
		    					
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
		    				}
		    				
		    				if (axe) {
		    					
		    					if(item.getItem() instanceof ItemAxe && i - 36 != axeSlot.getValue() - 1){
		    						
			        				if (!(mc.currentScreen instanceof GuiInventory)) {
			        					packetSent = true;
			            				
			            				
			        				}
			        				
			        				openInv();
			    					InventoryUtils.drop(i);
			    					SpicyClient.config.autoArmor.timer.reset();
			    					closeInv();
			    					return;
			    					
		    					}
		    					
		    				}else if (item.getItem() instanceof ItemAxe) {
		    					
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
		    				}
		    				
		    				if (item.getItem() instanceof ItemSpade || item.getItem() instanceof ItemHoe) {
		    					
		        				if (!(mc.currentScreen instanceof GuiInventory)) {
		        					packetSent = true;
		            				
		            				
		        				}
		        				
		        				openInv();
		    					InventoryUtils.drop(i);
		    					SpicyClient.config.autoArmor.timer.reset();
		    					closeInv();
		    					return;
		    					
		    				}
		    				
		    				if (armor.isEnabled()) {
		    					
		    					for(int type = 1; type < 5; type++){
		    						
		    						if (SpicyClient.config.autoArmor.isEnabled()) {
		    							SpicyClient.config.autoArmor.getBestArmor();
		    						}
		    						
				    				if (item.getItem() instanceof ItemArmor && !AutoArmor.isBestArmor(item, type)) {
				    					
				        				if (!(mc.currentScreen instanceof GuiInventory)) {
				        					packetSent = true;
				            				
				            				
				        				}
				        				
				        				openInv();
				    					InventoryUtils.drop(i);
				    					SpicyClient.config.autoArmor.timer.reset();
				    					closeInv();
				    					return;
				    					
				    				}
		    						
		    			        }
		    					
		    				}
		    				
	    				}
	    				
    				}
    				
    			}
    			
            }
			
			if (packetSent) {
				SpicyClient.config.autoArmor.timer.reset();
			}
			
			closeInv();
			
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
			Packet packet = ((EventPacket)e).packet;
			
            if (packet instanceof C0DPacketCloseWindow) {
                isInventoryOpen = false;
            }
            else if (packet instanceof C16PacketClientStatus) {
                C16PacketClientStatus open = (C16PacketClientStatus)packet;
                if (open.getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    isInventoryOpen = true;
                }
            }
            
		}
		
	}
	
	@Override
	public void onEventWhenDisabled(Event e) {
		
		if (e instanceof EventPacket && e.isPre()) {
			
			Packet packet = ((EventPacket)e).packet;
			
            if (packet instanceof C0DPacketCloseWindow) {
                isInventoryOpen = false;
            }
            else if (packet instanceof C16PacketClientStatus) {
                C16PacketClientStatus open = (C16PacketClientStatus)packet;
                if (open.getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    isInventoryOpen = true;
                }
            }
            
		}
		
	}
	
	public void openInv() {
		
        if (!this.isInventoryOpen) {
            mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }

        this.isInventoryOpen = true;
        
	}
	
	public void closeInv() {
		
		if (this.isInventoryOpen) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
        }

        this.isInventoryOpen = false;
		
	}
	
}