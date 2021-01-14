package info.sigmaclient.management.command.impl;

import net.minecraft.util.MathHelper;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.impl.hud.Enabled;
import info.sigmaclient.module.impl.hud.TabGUI;
import info.sigmaclient.module.impl.other.ChatCommands;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;

public class Panic extends Command {

    public Panic(String[] names, String description) {
        super(names, description);

    }



	@Override
    public void fire(String[] args) {
        for (Module module : Client.getModuleManager().getArray()) {
        	if(!(module instanceof ChatCommands) && !(module instanceof Enabled) && !(module instanceof TabGUI))
        	if(module.isEnabled()){
        		module.toggle();
        	}
        }
    }

    @Override
    public String getUsage() {
        return "Panic";
    }

    @Override
    public void onEvent(Event event) {

    }
}
