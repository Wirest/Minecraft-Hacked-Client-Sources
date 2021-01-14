package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.agora.GuiAgoraIngame;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.gui.screen.GuiInvManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;

public class InventoryWalk extends Module {

    private String CARRY = "CARRY";
    public final static String MODE = "MODE";
    boolean inInventory = false;
    public InventoryWalk(ModuleData data) {
        super(data);
        settings.put(CARRY, new Setting(CARRY, false, "Carry items in crafting slots."));
        settings.put(MODE,new Setting<>(MODE,new Options("Mode", "Basic", new String[] {"Basic", "AACP", "Cubecraft"}),"InvMove method."));
    }

    @Override
    @RegisterEvent(events = {EventPacket.class, EventTick.class, EventUpdate.class})
    public void onEvent(Event event) {
    	String mode = ((Options) settings.get(MODE).getValue()).getSelected();
    	this.setSuffix(mode);
        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiAgoraIngame) {
            return;
        }
        if(event instanceof EventUpdate){
        	EventUpdate em = (EventUpdate)event;
        	if(em.isPre()){
        	}
        }
        if (event instanceof EventTick) {
            if (mc.currentScreen != null) {
    			if(!mode.equalsIgnoreCase("Cubecraft")){
    				KeyBinding[] moveKeys = new KeyBinding[]{
    					mc.gameSettings.keyBindForward,
    					mc.gameSettings.keyBindBack,
    					mc.gameSettings.keyBindLeft,
    					mc.gameSettings.keyBindRight,
    					mc.gameSettings.keyBindJump					
    				};
    				for (KeyBinding bind : moveKeys){
    					KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
    				}
    				if(!inInventory){
    					if(mode.equalsIgnoreCase("AACP")){
                			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
                		}
    					inInventory = !inInventory;
    				}
    			}
    			if(!(mc.currentScreen instanceof GuiInvManager) ){
                if (Keyboard.isKeyDown(200)) {
                    mc.thePlayer.rotationPitch -= 1;
                }
                if (Keyboard.isKeyDown(208)) {
                    mc.thePlayer.rotationPitch += 1;
                }
                if (Keyboard.isKeyDown(203)) {
                    mc.thePlayer.rotationYaw -= 3;
                }
                if (Keyboard.isKeyDown(205)) {
                    mc.thePlayer.rotationYaw += 3;
                }
    			}
            }else{
            	if(inInventory){
            		if(mode.equalsIgnoreCase("AACP")){
            			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
            		}
					inInventory = !inInventory;
				}
            }
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet packet = ep.getPacket();
            if(((Boolean) settings.get(CARRY).getValue()))
            if (ep.isOutgoing() && packet instanceof C0DPacketCloseWindow) {
                ep.setCancelled(true);
            }
            if(packet instanceof C0BPacketEntityAction){          	
            	C0BPacketEntityAction p = (C0BPacketEntityAction)packet;
            	if(p.getAction() == Action.START_SPRINTING && inInventory && mode.equalsIgnoreCase("AACP"))
            		ep.setCancelled(true);
            }
            if(ep.isPre() && mode.equalsIgnoreCase("Cubecraft")){
            	if(packet instanceof C0BPacketEntityAction){
            		C0BPacketEntityAction p =(C0BPacketEntityAction)packet;
            		Action act = p.getAction();
            		if(act == Action.OPEN_INVENTORY){
            			ep.setCancelled(true);
            		}
            	}
            	if(packet instanceof C16PacketClientStatus){
                	C16PacketClientStatus p = (C16PacketClientStatus)packet;
                	EnumState st = p.getStatus();
                	if(st == EnumState.OPEN_INVENTORY_ACHIEVEMENT){
                		ep.setCancelled(true);
                	}
                }
        	}
        }
    }
}
