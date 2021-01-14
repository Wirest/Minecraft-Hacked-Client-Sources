package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.player.Scaffold;

public class HypixelScaffoldAddon extends Module {
    public HypixelScaffoldAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));

        ModuleManager m = Client.getModuleManager();
        Scaffold scaffoldModule = (Scaffold) m.get(Scaffold.class);
        scaffoldModule.setPremiumAddon(this);
        //scaffoldModule.getSettings().put(Scaffold.BYPASS, new Setting<>(Scaffold.BYPASS, false, "Bypass method for hypixel. (Upgraded Only)"));
    }

    @Override
    public void onEvent(Event event) {
        mc.thePlayer.motionX *= 0;
        mc.thePlayer.motionZ *= 0;
        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
            MoveUtils.setMotion(0.14);
        } else {
            MoveUtils.setMotion(0.2);
        }
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
