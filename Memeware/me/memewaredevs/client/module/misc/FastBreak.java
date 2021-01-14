package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class FastBreak extends Module {

    public FastBreak() {
        super("Fast Break", 0, Category.MISC);
        this.addDouble("Break", 0.7, 0, 1);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        mc.playerController.blockHitDelay = 0;
        if (mc.playerController.curBlockDamageMP >= this.getDouble("Break"))
            mc.playerController.curBlockDamageMP = 1F;
    };
}
