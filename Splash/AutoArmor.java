package splash.client.modules.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.player.InventoryUtils;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 21:54, 13-Jun-20
 * Project: Client
 */
public class AutoArmor extends Module {
    private Stopwatch stopwatch = new Stopwatch();
    public NumberValue<Integer> equipDelayValue = new NumberValue<>("Delay", 200, 1, 1000, this);

    public AutoArmor() {
        super("AutoArmor", "Auto equips armor", ModuleCategory.COMBAT);
    }

    @Collect
    public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (mc.currentScreen != null) return;
        if(mc.thePlayer != null) {
            InventoryUtils.equipArmor(stopwatch, equipDelayValue.getValue());
        }
    }
}
