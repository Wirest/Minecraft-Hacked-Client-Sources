package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.Client;
import info.sigmaclient.module.Module;

/**
 * Created by Arithmo on 5/17/2017 at 4:24 PM.
 */
public class ClickGui extends Module {

    public ClickGui(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Client.getClickGui());
        toggle();
    }

    @Override
    public void onEvent(Event event) {

    }

}
