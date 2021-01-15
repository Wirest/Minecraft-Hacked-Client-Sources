package VenusClient.online.Module.impl.World;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventReceivePacket;
import VenusClient.online.Event.impl.EventSendPacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.MathUtils;

import com.mojang.realmsclient.gui.ChatFormatting;
import de.Hero.settings.Setting;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class Disabler extends Module {
    public Disabler() {
        super("Disabler", "Disabler", Keyboard.KEY_NONE, Category.WORLD);
    }

    @Override
    public void setup() {}
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    	C0FPacketConfirmTransaction c0f;
    	if(event.getPacket() instanceof C0FPacketConfirmTransaction && (c0f = (C0FPacketConfirmTransaction)event.getPacket()).getUid() < 0) {
    		event.setCancelled(true);
    	}
    	
    	if(event.getPacket() instanceof C00PacketKeepAlive) {
    		event.setCancelled(true);
    	}
    	
    }
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
    	
    }
    
    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
    	if ((mc.thePlayer.ticksExisted % 20 == 0)) { 
    	if(Client.instance.moduleManager.getModuleByName("Fly").isEnabled()) {
    		PlayerCapabilities cap = new PlayerCapabilities();
    		cap.isCreativeMode = true;
    		mc.thePlayer.sendQueue.addToSendQueueSilent(new C13PacketPlayerAbilities(cap));
    	}
    	
    }
   
    }
    
}