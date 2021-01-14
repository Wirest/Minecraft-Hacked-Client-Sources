package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class Fullbright extends Module {

    private float lastBrightness;

    public Fullbright() {
        super("Fullbright", 0, Category.RENDER);
    }

    @Handler
    public Consumer<UpdateEvent> onEvent = (event) -> {
        mc.gameSettings.gammaSetting = 1000000L;
    };

    @Override
    public void onEnable() {
        lastBrightness = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000000L;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = lastBrightness;
    }
}
