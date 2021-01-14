package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.player.Teleport;
import info.sigmaclient.util.PlayerUtil;

public class PremiumTeleportAddon extends Module {
    private Teleport teleportModule;

    public PremiumTeleportAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));
        ModuleManager m = Client.getModuleManager();
        teleportModule = (Teleport) m.get(Teleport.class);
        teleportModule.setPremiumAddon(this);
    }

    @Override
    public void onEvent(Event event) {
        final double[] startPos = {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
        PlayerUtil.hypixelTeleport(startPos, teleportModule.endPos);
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
