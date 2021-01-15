package VenusClient.online.Module.impl.Player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Event.impl.EventReceivePacket;
import VenusClient.online.Event.impl.EventSendPacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0DPacketCloseWindow;


public class InvMove extends Module{

	public InvMove() {
		super("InvMove", "InvMove", Category.PLAYER, Keyboard.KEY_NONE);
	}
	
	boolean inInventory = false;
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
		options.add("Normal");
		Client.instance.setmgr.rSetting(new Setting("InvMove Mode", this, "Normal", options));
		Client.instance.setmgr.rSetting(new Setting("InvMove Carry", this, false));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
			EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
	  		return;
		}
		String mode = Client.instance.setmgr.getSettingByName("InvMove Mode").getValString();
		if (mc.currentScreen != null) {
			if(!Client.instance.moduleManager.getModuleByName("Fly").isEnabled()) {
        		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
        			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
        		}
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
					//if(mode.equalsIgnoreCase("AACP")){
            		//	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
            		//}
					inInventory = !inInventory;
				}
			}
			if(mc.currentScreen instanceof GuiInventory){
        	if(inInventory){
        		
        		inInventory = !inInventory;
        		
        	}
				//inInventory = !inInventory;
			
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
        }
		}
			
    }

	@EventTarget
	public void onMove(EventMove event) {
		
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
	}
	
}
