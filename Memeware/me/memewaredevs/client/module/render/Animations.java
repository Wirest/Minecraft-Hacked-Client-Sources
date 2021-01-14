package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class Animations extends Module {

    public static int anim;

    public Animations() {
        super("Animations", 0, Category.RENDER);
        addModes("Vanilla", "Push", "Warped", "Spin", "Slide", "Down");
    }

    @Handler
    public Consumer<UpdateEvent> onEvent = (event) -> {
        if (isMode("Vanilla")) // have to use an int to change blockanim for some reason, checking mode normally not workin
            this.anim = 1;

        if (isMode("Push"))
            this.anim = 2;

        if (isMode("Warped"))
            this.anim = 3;

        if (isMode("Spin"))
            this.anim = 4;

        if (isMode("Slide"))
            this.anim = 5;

        if (isMode("Down"))
            this.anim = 6;
    };
}
