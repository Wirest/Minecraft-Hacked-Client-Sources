
package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.Render3DEvent;
import me.memewaredevs.client.event.events.RenderPlayerNameEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.render.TagUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Consumer;

public class Tags extends Module {
    public Tags() {
        super("Better Tags", 0, Module.Category.RENDER);
    }

    @Handler
    public Consumer<Render3DEvent> update = e -> {
        for (final Object o : this.mc.theWorld.playerEntities) {
            final EntityPlayer player = (EntityPlayer) o;
            if (player.isInvisible())
                continue;
            final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * e.getRenderTick()
                    - RenderManager.renderPosX;
            final double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * e.getRenderTick()
                    - RenderManager.renderPosY;
            final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.getRenderTick()
                    - RenderManager.renderPosZ;
            TagUtils.renderNametag(player, x, y2, z);
        }
    };

    @Handler
    public Consumer<RenderPlayerNameEvent> update2 = e -> {
        e.cancel();
    };

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
