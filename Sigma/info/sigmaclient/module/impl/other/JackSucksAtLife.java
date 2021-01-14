package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;



public class JackSucksAtLife extends Module {

    public static String Mushroom = "MUSHROOM";
    public static String Scumbag = "SCUMBAG";
    public static String Advertisement = "AD";
    public JackSucksAtLife(ModuleData data) {
        super(data);
        settings.put(Scumbag, new Setting<>(Scumbag, true, "Scumbags everywhere."));
        settings.put(Mushroom, new Setting<>(Mushroom, true, "You everywhere."));
        settings.put(Advertisement, new Setting<>(Advertisement, true, "Draw \"mc.skycade.net\" on the top of the screen."));
    }

    @Override
    @RegisterEvent(events = {})
    public void onEvent(Event event) {
    }

}
