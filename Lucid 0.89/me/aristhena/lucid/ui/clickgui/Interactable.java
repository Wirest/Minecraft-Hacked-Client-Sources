/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.aristhena.lucid.ui.clickgui;

import me.aristhena.lucid.ui.clickgui.Vector;
import net.minecraft.client.Minecraft;

public abstract class Interactable {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public boolean hover;
    public Vector pos;
    public Vector size;

    public abstract void mousePress(int var1, int var2, int var3);

    public abstract void mouseRelease(int var1, int var2, int var3);

    public abstract void mouseScroll(int var1);

    public abstract void keyPress(int var1, int var2);

    public abstract void keyRelease(int var1, int var2);

    public boolean mouseOver(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x && (float)mouseY > this.pos.y && (float)mouseX < this.pos.x + this.size.x && (float)mouseY <= this.pos.y + this.size.y) {
            return true;
        }
        return false;
    }
}

