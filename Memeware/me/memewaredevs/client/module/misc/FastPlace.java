package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class FastPlace extends Module {

    public FastPlace() {
        super("Fast Place", 0, Category.MISC);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        mc.rightClickDelayTimer = 0;
    };
}
