package info.spicyclient.modules.world;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventOnLadder;
import info.spicyclient.events.listeners.EventOpenChest;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class ChestStealer extends Module {
	
	public static Thread chestStealerThread = null;
	
	NumberSetting delay = new NumberSetting("Delay", 250, 0, 2000, 10);
	
	public ChestStealer() {
		super("Chest Stealer", Keyboard.KEY_NONE, Category.WORLD);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		settings.clear();
		addSettings(delay);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventOpenChest) {
			
			EventOpenChest chestEvent = (EventOpenChest) e;
			
			
			if (chestStealerThread == null) {
				
			}else {
				chestStealerThread.stop();
			}
			
			chestStealerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						
						for (int i = 0; i < (chestEvent.chest.inventoryRows * 9); i++) {
							Slot slot = (Slot) chestEvent.chest.inventorySlots.inventorySlots.get(i);
							if (slot.getStack() != null) {
								
								try {
									Thread.sleep((long) delay.getValue() + new Random().nextInt(40));
								}catch (ThreadDeath e) {
									e.printStackTrace();
								}
								
				                if (!(mc.currentScreen instanceof GuiInventory)) {
				                    //mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
				                }
								
								chestEvent.chest.spicyHandleMouseInput(slot, slot.slotNumber, 0, 1);
								
							}
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
				}
			});
			
			if (e.isPre()) {
				chestStealerThread.start();
			}
			
		}
		
	}
	
}
