/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.Slot;
import me.aristhena.lucid.ui.clickgui.Vector;

public class SlotList
extends Interactable {
    public float scroll;
    public float scrollTo;
    public float slotsHeight;
    public Interactable parent;
    public List<Slot> slots = new CopyOnWriteArrayList<Slot>();

    public SlotList(Interactable parent, float x, float y, float width, float height) {
        this.parent = parent;
        this.pos = new Vector(x, y);
        this.size = new Vector(width, height);
    }

    public void update(int mouseX, int mouseY) {
        this.slotsHeight = 0.0f;
        for (Slot slot : this.slots) {
            this.slotsHeight += slot.size.y + (float)(this.slots.indexOf(slot) < this.slots.size() - 1 ? 2 : 0);
        }
        this.size.y = Math.min(this.parent instanceof Slot ? this.slotsHeight : 768.0f, this.slotsHeight);
        this.scrollTo = Math.min(Math.max(0.0f, this.scrollTo), Math.max(0.0f, this.slotsHeight - this.size.y));
        this.scroll += (this.scrollTo - this.scroll) / 5.0f;
        if (Math.abs(this.scroll - this.scrollTo) < 0.3f) {
            this.scroll = this.scrollTo;
        }
        float offset = 0.0f;
        for (Slot slot2 : this.slots) {
            slot2.size.x = this.size.x - 10.0f;
            slot2.pos = this.pos.offset((this.size.x - slot2.size.x) / 2.0f, offset - this.scroll);
            offset += slot2.size.y + 2.0f;
            slot2.update(mouseX, mouseY);
        }
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        if (this.parent.hover) {
            for (Slot slot : this.slots) {
                slot.mousePress(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        for (Slot slot : this.slots) {
            slot.mouseRelease(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseScroll(int amount) {
        if (this.parent.hover) {
            for (Slot slot : this.slots) {
                slot.mouseScroll(amount);
            }
            this.scrollTo -= (float)(amount / 120 * 28);
        }
    }

    @Override
    public void keyPress(int key, int keyChar) {
        for (Slot slot : this.slots) {
            slot.keyPress(key, keyChar);
        }
    }

    @Override
    public void keyRelease(int key, int keyChar) {
        for (Slot slot : this.slots) {
            slot.keyRelease(key, keyChar);
        }
    }

    public void render() {
        for (Slot slot : this.slots) {
            slot.render();
        }
        if (this.slotsHeight > this.size.y) {
            float s = this.scroll;
            float ext = this.slotsHeight - this.size.y;
            float ext2 = s / ext;
            float perc = this.size.y / this.slotsHeight;
            float sh = this.size.y * perc;
            float sy = ext2 * (this.size.y - sh);
            Draw.rect(this.pos.x + this.size.x - 4.0f, this.pos.y + sy, this.pos.x + this.size.x, this.pos.y + sy + sh, 1090519039);
        }
    }
}

