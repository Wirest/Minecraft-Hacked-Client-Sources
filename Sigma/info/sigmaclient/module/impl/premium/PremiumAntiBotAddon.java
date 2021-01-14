package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.combat.AntiBot;

public class PremiumAntiBotAddon extends Module {
    public PremiumAntiBotAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));
        ModuleManager m = Client.getModuleManager();
        AntiBot antiBotModule = (AntiBot) m.get(AntiBot.class);
        antiBotModule.setPremiumAddon(this);
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
