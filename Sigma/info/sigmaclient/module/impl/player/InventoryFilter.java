package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;

/**
 * Created by Arithmo on 5/19/2017 at 5:18 PM.
 */
public class InventoryFilter extends Module {

    private String MODE = "MODE";
    private String BETTER = "BETTER";

    public InventoryFilter(ModuleData data) {
        super(data);
        settings.put(BETTER, new Setting<>(BETTER, false, "Only pickup item if it's better."));
        settings.put(MODE, new Setting<>(MODE, new Options("Filter Mode", "Armor", new String[]{"Weapons", "Blocks", "Armor", "Tools"}), "Filters out items to pickup."));
    }

    @Override
    public void onEvent(Event event) {

    }

}
