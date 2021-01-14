package me.memewaredevs.client.module.render;

import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.potion.Potion;

import java.util.function.Consumer;

public class AntiBlind extends Module {
    public AntiBlind() {
        super("AntiBlind", 0, Category.RENDER);
    }
    public Consumer<UpdateEvent> eventConsumer0 = (updateEvent) -> {
        if (mc.thePlayer.isPotionActive(Potion.blindness)) {
            mc.thePlayer.removePotionEffect(Potion.blindness.id);
        }
    };
}