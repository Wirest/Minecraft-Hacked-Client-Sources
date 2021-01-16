/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.razerboy420.weepcraft.util;

import me.razerboy420.weepcraft.util.RenderUtils2D;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class MouseUtils {
    public static int getMouseX() {
        return Mouse.getX() * RenderUtils2D.newScaledResolution().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return RenderUtils2D.newScaledResolution().getScaledHeight() - Mouse.getY() * RenderUtils2D.newScaledResolution().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }
}

