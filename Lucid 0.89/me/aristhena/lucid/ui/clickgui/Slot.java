/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import java.util.List;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.SlotList;
import me.aristhena.lucid.ui.clickgui.Vector;

public class Slot
extends Interactable {
    public Interactable parent;
    public boolean selected;
    public boolean active;
    public boolean extended;
    public SlotList slotList;
    public final int fill = -13421773;
    public final int outline = -15658735;

    public Slot(Interactable parent, float x, float y, float width, float height) {
        this.parent = parent;
        this.pos = new Vector(x, y);
        this.size = new Vector(width, height);
        this.slotList = new SlotList(this, this.pos.x + 4.0f, this.pos.y + 4.0f, this.size.x - 8.0f, this.size.y - 8.0f);
    }

    public void update(int mouseX, int mouseY) {
        this.hover = this.mouseOver(mouseX, mouseY) && this.parent.hover;
        this.size.y = !this.extended ? 40.0f : 40.0f + this.slotList.size.y + 4.0f;
        this.slotList.pos = this.pos.offset(4.0f, 32.0f);
        this.slotList.size.x = this.size.x - 8.0f;
        this.slotList.update(mouseX, mouseY);
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        if (this.parent.hover) {
            boolean select = true;
            if (!this.slotList.slots.isEmpty()) {
                if (this.extended) {
                    for (Slot slot : this.slotList.slots) {
                        if (!slot.hover) continue;
                        select = false;
                    }
                    this.slotList.mousePress(mouseX, mouseY, button);
                }
                if (button == 1 && select && this.mouseOver(mouseX, mouseY)) {
                    this.extended = !this.extended;
                    select = false;
                }
            }
            if (select) {
                this.selected = this.hover;
                this.active = this.hover;
            }
        }
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        this.active = false;
        if (this.extended) {
            this.slotList.mouseRelease(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseScroll(int amount) {
        if (this.parent.hover && this.extended) {
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
        if (this.extended) {
            this.slotList.keyRelease(key, keyChar);
        }
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if ((float)mouseX >= this.pos.x && (float)mouseY > this.pos.y && (float)mouseX < this.pos.x + this.size.x && (float)mouseY <= this.pos.y + this.size.y) {
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

    public void render() {
        this.slotList.slots.isEmpty();
        if (this.extended) {
            this.slotList.render();
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

