/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Gui;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.SlotList;
import me.aristhena.lucid.ui.clickgui.Vector;
import me.aristhena.lucid.util.ColorUtil;

public class Window
extends Interactable {
    public String title;
    public boolean drag;
    public boolean extended;
    public boolean pinned;
    public Vector dragOffset;
    public SlotList slotList;
    public final float handleHeight = 32.0f;
    public final int fill = -14540254;
    public final int outline = -15658735;

    public Window(String title, float x, float y, float width, float height) {
        this.title = title;
        this.pos = new Vector(x, y);
        this.size = new Vector(width, height);
        this.slotList = new SlotList(this, this.pos.x + 4.0f, this.pos.y + 32.0f + 4.0f, this.size.x - 8.0f, this.size.y - 32.0f - 8.0f);
    }

    public void update(int mouseX, int mouseY) {
        this.hover = Gui.topMost == null || Gui.topMost == this ? this.mouseOver(mouseX, mouseY) : false;
        this.size.y = !this.extended ? 0.0f : this.slotList.size.y + 12.0f;
        float xGrid = 12.0f;
        float yGrid = 12.0f;
        if (this.drag) {
            this.pos.x = (float)mouseX - this.dragOffset.x;
            this.pos.y = (float)mouseY - this.dragOffset.y;
        }
        this.pos.x = (float)Math.round(this.pos.x / xGrid) * xGrid;
        this.pos.y = (float)Math.round(this.pos.y / yGrid) * yGrid;
        if (this.drag) {
            Gui.instance.saveWindows();
        }
        this.slotList.pos = this.pos.offset(4.0f, 36.0f);
        this.slotList.update(mouseX, mouseY);
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        if (this.mouseOverHandle(mouseX, mouseY)) {
            boolean oldExtended = this.extended;
            boolean oldPinned = this.pinned;
            if (button == 1) {
                this.extended = !this.extended;
                Gui.instance.saveWindows();
            }
            if (this.extended == oldExtended && this.pinned == oldPinned) {
                this.drag = true;
                this.dragOffset = new Vector((float)mouseX - this.pos.x, (float)mouseY - this.pos.y);
            }
        }
        this.slotList.mousePress(mouseX, mouseY, button);
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        this.drag = false;
        this.slotList.mouseRelease(mouseX, mouseY, button);
    }

    @Override
    public void mouseScroll(int amount) {
        if (this.extended) {
            this.slotList.mouseScroll(amount);
        }
    }

    @Override
    public void keyPress(int key, int keyChar) {
        if (this.extended) {
            this.slotList.keyPress(key, keyChar);
        }
    }

    @Override
    public void keyRelease(int key, int keyChar) {
        this.slotList.keyRelease(key, keyChar);
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x && (float)mouseY > this.pos.y && (float)mouseX < this.pos.x + this.size.x && (float)mouseY <= this.pos.y + this.size.y + 32.0f) {
            return true;
        }
        return false;
    }

    public boolean mouseOverHandle(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x && (float)mouseY > this.pos.y && (float)mouseX < this.pos.x + this.size.x && (float)mouseY <= this.pos.y + 32.0f) {
            return true;
        }
        return false;
    }

    public boolean mouseOverExtended(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x + this.size.x - 24.0f && (float)mouseY > this.pos.y + 8.0f && (float)mouseX < this.pos.x + this.size.x - 8.0f && (float)mouseY <= this.pos.y + 24.0f) {
            return true;
        }
        return false;
    }

    public boolean mouseOverPinned(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x + 8.0f && (float)mouseY > this.pos.y + 8.0f && (float)mouseX < this.pos.x + 24.0f && (float)mouseY <= this.pos.y + 24.0f) {
            return true;
        }
        return false;
    }

    public void render() {
        int[] fillGradient = new int[]{-14540254, -14540254, ColorUtil.blend(-14540254, -16777216, 0.95f), ColorUtil.blend(-14540254, -16777216, 0.95f)};
        int[] outlineGradient = new int[]{ColorUtil.blend(-15658735, -16777216, 0.95f), ColorUtil.blend(-15658735, -16777216, 0.95f), -15658735, -15658735};
        Draw.rectGradientBordered(this.pos.x, this.pos.y, this.pos.x + this.size.x, this.pos.y + this.size.y + 32.0f, fillGradient, outlineGradient, 1.0f);
        Draw.rect(this.pos.x + 2.0f, this.pos.y + 1.0f, this.pos.x + this.size.x - 2.0f, this.pos.y + 2.0f, 553648127);
        Draw.string(this.title, this.pos.x + this.size.x / 2.0f, this.pos.y + 16.0f, -1, 0, 0);
        if (this.extended) {
            Draw.startClip(this.slotList.pos.x, this.slotList.pos.y, this.slotList.pos.x + this.slotList.size.x, this.slotList.pos.y + this.slotList.size.y);
            this.slotList.render();
            Draw.endClip();
            if (this.slotList.slotsHeight > this.slotList.size.y) {
                int[] arrn = new int[4];
                arrn[0] = Integer.MIN_VALUE;
                arrn[1] = Integer.MIN_VALUE;
                Draw.rectGradient(this.slotList.pos.x - 1.0f, this.slotList.pos.y + 1.0f, this.slotList.pos.x + this.slotList.size.x, this.slotList.pos.y + 5.0f, arrn);
                int[] arrn2 = new int[4];
                arrn2[2] = Integer.MIN_VALUE;
                arrn2[3] = 134217728;
                Draw.rectGradient(this.slotList.pos.x - 1.0f, this.slotList.pos.y + this.slotList.size.y - 5.0f, this.slotList.pos.x + this.slotList.size.x, this.slotList.pos.y + this.slotList.size.y, arrn2);
                Draw.rect(this.slotList.pos.x - 1.0f, this.slotList.pos.y, this.slotList.pos.x + this.slotList.size.x + 1.0f, this.slotList.pos.y + 1.0f, -15658735);
                Draw.rect(this.slotList.pos.x - 1.0f, this.slotList.pos.y + this.slotList.size.y - 1.0f, this.slotList.pos.x + this.slotList.size.x + 1.0f, this.slotList.pos.y + this.slotList.size.y, -15658735);
            }
        }
    }
}

