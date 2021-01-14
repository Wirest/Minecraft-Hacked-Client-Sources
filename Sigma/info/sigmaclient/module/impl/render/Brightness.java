/**
 * Time: 5:30:04 AM
 * Date: Jan 7, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author cool1
 */
public class Brightness extends Module {

    public Brightness(ModuleData data) {
        super(data);

    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
    }

}
