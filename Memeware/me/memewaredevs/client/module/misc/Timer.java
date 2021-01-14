package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class Timer extends Module {

    public Timer() {
        super("Timer", 0, Category.MISC);
        this.addDouble("Timer", 1, 0.1, 10);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        mc.timer.timerSpeed = this.getDouble("Timer");
    };

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
    }
}
