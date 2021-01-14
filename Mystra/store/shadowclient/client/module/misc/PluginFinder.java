package store.shadowclient.client.module.misc;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventReceivePacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.network.play.server.S3APacketTabComplete;

public class PluginFinder extends Module {

	boolean isListening;
    TimeHelper timer = new TimeHelper();
	
	public PluginFinder() {
		super("PluginFinder", 0, Category.MISC);
	}
	
	@EventTarget
	public void onUpdate(EventReceivePacket event) {
        EventReceivePacket ep = (EventReceivePacket)event;
        if (ep.getPacket() instanceof S3APacketTabComplete && this.isListening && this.timer.delay(20000.0f)) {
            S3APacketTabComplete packet = (S3APacketTabComplete)ep.getPacket();
            String[] pluginsFound = packet.func_149630_c();
            Shadow.instance.addChatMessage("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a73Found \u00a77[\u00a73" + pluginsFound.length + "\u00a77] \u00a73plugin(s): \u00a78" + pluginsFound + "\u00a73.");
            toggle();
        }
    }
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
}
