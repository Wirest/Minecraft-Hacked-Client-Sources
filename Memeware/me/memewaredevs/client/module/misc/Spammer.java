package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.ChatUtil;
import me.memewaredevs.client.util.misc.Timer;

import java.util.function.Consumer;

public class Spammer extends Module {

    private Timer timer = new Timer();

    private int currentMessage;

    public Spammer() {
        super("Spammer", 0, Category.MISC);
        this.addModes("Aids", "Memeware");
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        if (timer.delay(1000L)) {
            if (isMode("Aids")) {
                ++this.currentMessage;
                ChatUtil.sendChat(SpamUtil.getAids("Jake." + this.currentMessage));
                if (this.currentMessage >= 121) {
                    this.currentMessage = 0;
                }
                timer.reset();
            }
            if (isMode("Memeware")) {
                if (this.currentMessage > 9) {
                    this.currentMessage = 0;
                }
                ChatUtil.sendChat(SpamUtil.getMemeware("memeware." + this.currentMessage));
                ++this.currentMessage;
                timer.reset();
            }
        }
    };

    @Override
    public void onEnable() {
        this.currentMessage = 0;
    }
}
