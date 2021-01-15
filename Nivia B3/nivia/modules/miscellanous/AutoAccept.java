package nivia.modules.miscellanous;

import net.minecraft.network.play.server.S02PacketChat;
import nivia.Pandora;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketReceive;
import nivia.managers.FriendManager.Friend;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;

public class AutoAccept extends Module {
	public AutoAccept() {
		super("AutoAccept", 0, 0x005C00, Category.MISCELLANEOUS, "Accepts friend's tpas automatically",
				new String[] { "aaccept", "accept" }, true);
	}
	public Property<Boolean> reverse = new Property<Boolean>(this, "Reverse", true);
	@EventTarget
	  public void onReceivePacket(EventPacketReceive e) {	   
	      S02PacketChat message = (S02PacketChat)e.getPacket();	
	      if (message.func_148915_c().getFormattedText().contains("has requested to teleport to you") || (message.func_148915_c().getFormattedText().contains("has requested that you teleport to them") && reverse.value)) {
	        for (Friend friend : Pandora.getFriendManager().friends) {
	          if (message.func_148915_c().getFormattedText().contains(friend.getName()) || message.func_148915_c().getFormattedText().contains(friend.getAlias())) {
	            mc.thePlayer.sendChatMessage("/tpaccept");
	            break;
	          }
	        }
	      }	    
	  }
}
