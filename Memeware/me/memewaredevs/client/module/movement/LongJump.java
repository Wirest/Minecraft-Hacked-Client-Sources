
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class LongJump extends Module {

    private double currentMotion, previousDistance, currentPhase;

    public LongJump() {
        super("LongJump", 0, Module.Category.MOVEMENT);
        this.addModes("NCP", "Mineplex", "Hypixel");
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = event -> {

    };

    @Handler
    public Consumer<MovementEvent> eventConsumer1 = (event) -> {

    };

}
