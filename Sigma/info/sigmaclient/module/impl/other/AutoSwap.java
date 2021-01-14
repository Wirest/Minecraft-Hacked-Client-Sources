package info.sigmaclient.module.impl.other;

import org.lwjgl.input.Keyboard;

import info.sigmaclient.management.command.Command;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;

public class AutoSwap extends Module {

    public static int multiSwap;
    public static boolean isSwapping;
    public static boolean settingKey;
    public static boolean keysSet;
    private static final String MULTI = "MULTI";
    private static final String SINGLE = "SINGLE";
    public int multiKey;
    public int single;
    Timer timer = new Timer();

    public AutoSwap(ModuleData data) {
        super(data);

    }

    @Override
    public void onEnable() {
        isSwapping = false;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        if (em.isPre()) {
            if (settingKey && timer.delay(1000) && !keysSet) {
                ChatUtil.printChat(Command.chatPrefix
                        + "press your key you'd like to set for Multi Swap.");
                this.keysSet = true;
            } else if (!settingKey && !keysSet) {

            }
            if (keysSet && Keyboard.getEventKey() != 0) {
                this.multiKey = Keyboard.getEventKey();
            }
        }
        if (em.isPost()) {
            if (!settingKey) {

            }
        }
    }
}
