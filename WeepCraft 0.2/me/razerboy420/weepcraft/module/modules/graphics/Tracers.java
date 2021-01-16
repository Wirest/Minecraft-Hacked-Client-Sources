/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import darkmagician6.EventTarget;
import darkmagician6.events.EventRender3D;
import java.util.List;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Module.Mod(category=Module.Category.RENDER, description="Draw a line to a player", key=0, name="Tracers")
public class Tracers
extends Module {
    @EventTarget
    public void onRender3D(EventRender3D event) {
        for (Object o : Wrapper.mc().world.loadedEntityList) {
            Entity e = (Entity)o;
            if (!(o instanceof EntityOtherPlayerMP) || o == Wrapper.mc().player || e == null || e.isInvisibleToPlayer(Wrapper.mc().player)) continue;
            me.razerboy420.weepcraft.util.esp.Tracers.tracerLine(e, (EntityOtherPlayerMP)o != null ? 0 : 1);
        }
    }
}

