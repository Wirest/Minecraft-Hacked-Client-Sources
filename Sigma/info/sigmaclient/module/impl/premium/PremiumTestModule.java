package info.sigmaclient.module.impl.premium;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventChat;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;

public class PremiumTestModule extends Module {
    public PremiumTestModule() {
        super(new ModuleData(ModuleData.Type.Other, "PremiumTest", "A simple module to test premium modules"));
    }

    @Override
    @RegisterEvent(events = {EventChat.class})
    public void onEvent(Event event) {
        ChatUtil.printChat("Chat event");
    }
}
