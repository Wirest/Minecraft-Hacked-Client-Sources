package info.sigmaclient.management.agora;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.client.gui.GuiChat;






public class AgoraSwitchModule extends Module {

	private String oldMode = "Minecraft";
    public AgoraSwitchModule(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
    	if(GuiChat.mode.equalsIgnoreCase("Minecraft")){
    		GuiChat.mode = "Agora";
    		mc.displayGuiScreen(new GuiAgoraIngame());
    		mc.displayGuiScreen(null);
    			
    	}else{
    		GuiChat.mode = "Minecraft";
    	}
    	this.toggle();
    	this.setHidden(true);
    }
    @Override
    public void onDisable() {
    }

    @Override
    @RegisterEvent(events = {})
    public void onEvent(Event event) {
    	
    }
    
    
}
