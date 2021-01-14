package info.sigmaclient.module.impl.movement;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.PlayerUtil;

public class Sprint extends Module {
	String omni = "OMNIDIR";
	public static boolean backward = true;
    public Sprint(ModuleData data) {
        super(data);
        settings.put(omni, new Setting<>(omni, true, "Sprint backward."));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
    	backward = (boolean)settings.get(omni).getValue();
        EventUpdate em = (EventUpdate) event;
        if (em.isPre() && canSprint() && Scaffold.shouldSprint()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    private boolean canSprint() {
    	if(!(Boolean) settings.get(omni).getValue() &&!mc.gameSettings.keyBindForward.pressed)
    		return false;
        return PlayerUtil.isMoving() && mc.thePlayer.getFoodStats().getFoodLevel() > 6;
    }

}
