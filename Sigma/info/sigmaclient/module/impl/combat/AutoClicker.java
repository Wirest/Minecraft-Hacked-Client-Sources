package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;

import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
	
    public AutoClicker(ModuleData data) {
        super(data);
        settings.put(CPS, new Setting<>(CPS, 8, "Base click per seconds.", 1,1, 20));
        settings.put(RANDOM, new Setting<>(RANDOM, true, "Randomize click delay."));
        settings.put(MIN, new Setting<>(MIN, 7, "Minimum click randomization.", 1, 1, 20));
        settings.put(MAX, new Setting<>(MAX, 10, "Maximum click randomization.", 1, 1, 20));
        settings.put(MOUSE, new Setting<>(MOUSE, true, "Click when mouse is held down."));
    }

    private final String CPS = "CPS";
    private final String RANDOM = "RANDOM";
    private final String MIN = "MINRAND";
    private final String MAX = "MAXRAND";
    private final String MOUSE = "ON-MOUSE";
    Timer timer = new Timer();

    public static int randomNumber(int max, int min) {
        int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
        return ii;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && mc.currentScreen == null && mc.thePlayer.isEntityAlive()) {
                if ((Boolean) settings.get(MOUSE).getValue() && !Mouse.isButtonDown(0)) {
                    return;
                }
                int cps = ((Number) settings.get(CPS).getValue()).intValue();
                int minran = ((Number) settings.get(MIN).getValue()).intValue();
                int maxran = ((Number) settings.get(MAX).getValue()).intValue();
                boolean random = ((Boolean) settings.get(RANDOM).getValue());
                int rand = random ? randomNumber(minran, maxran) : 0;
                int cpsdel = cps+rand <= 0? 1:cps+rand;
                long del = 1000/(cpsdel) ;
                if (timer.delay(del)) {
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    mc.thePlayer.swingItem();
                    mc.clickMouse();
                    timer.reset();
                }
            }
        }
    }
}
