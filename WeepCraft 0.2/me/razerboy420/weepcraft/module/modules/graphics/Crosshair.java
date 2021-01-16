/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import darkmagician6.EventTarget;
import darkmagician6.events.EventRender2D;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;

@Module.Mod(category=Module.Category.RENDER, description="Crosshair like in CSGO (Client Settings -> Crosshair) to change", key=0, name="Crosshair")
public class Crosshair
extends Module {
    public static Value color = new Value("crosshair_Color", Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(270.0f), Float.valueOf(0.5f));
    public static Value thickness = new Value("crosshair_Thickness", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(0.5f));
    public static Value size = new Value("crosshair_Size", Float.valueOf(7.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(0.5f));
    public static Value gap = new Value("crosshair_Gap", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), Float.valueOf(0.5f));
    public static Value outline = new Value("crosshair_Outline", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(3.0f), Float.valueOf(0.5f));

    @EventTarget
    public void onEvent(EventRender2D event) {
        Wrapper.drawCrosshair(RenderUtils2D.newScaledResolution().getScaledWidth() / 2, RenderUtils2D.newScaledResolution().getScaledHeight() / 2);
    }
}

