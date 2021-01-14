package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;


/**
 * @author LeakedPvP
 */
public class Animations extends Module {

	private static String BLOCK = "BLOCK";
	private static String HIT = "HIT";
	public static String hit,block = "";
    public Animations(ModuleData data) {
        super(data);
        settings.put(BLOCK, new Setting<>(BLOCK, new Options("BlockAnimation", "Sigma", new String[]{"Sigma", "Slide","Avatar", "Vanilla", "Tap", "Tap2"}), "Blocking animation."));
        settings.put(HIT, new Setting<>(HIT, new Options("HitAnimation", "Vanilla", new String[]{"Vanilla", "Smooth"}), "Hiting animation."));
    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
    	hit = ((Options)settings.get(HIT).getValue()).getSelected();
    	block = ((Options)settings.get(BLOCK).getValue()).getSelected();
    }

   

}
