
package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.Render3DEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.function.Consumer;

public class ESP extends Module {
    public ESP() {
        super("Player ESP", 0, Module.Category.RENDER);
    }

    @Handler
    public Consumer<Render3DEvent> onEvent = (event) -> {
        for (final Entity o : this.mc.theWorld.loadedEntityList) {
            if (o == this.mc.thePlayer || !(o instanceof EntityPlayer)) {
                continue;
            }
            RenderUtils.drawEsp(o, new Color(0, 0, 0, 21).getRGB());
        }
    };

}
