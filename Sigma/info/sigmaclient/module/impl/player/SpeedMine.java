/**
 * Time: 5:28:19 AM
 * Date: Jan 7, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;

/**
 * @author cool1
 */
public class SpeedMine extends Module {

    /**
     * @param data
     */
    public SpeedMine(ModuleData data) {
        super(data);
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        mc.playerController.blockHitDelay = 0;
        if (mc.playerController.curBlockDamageMP >= 0.7F) {
            mc.playerController.curBlockDamageMP = 1.0F;
        }
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

}
