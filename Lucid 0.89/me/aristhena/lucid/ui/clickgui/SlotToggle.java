/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.Slot;
import me.aristhena.lucid.ui.clickgui.SlotList;
import me.aristhena.lucid.ui.clickgui.Vector;
import me.aristhena.lucid.util.ColorUtil;

public class SlotToggle
extends Slot {
    public Module mod;

    public SlotToggle(Interactable parent, Module mod, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        this.mod = mod;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        this.hover = this.mouseOver(mouseX, mouseY) && this.parent.hover;
        this.size.y = !this.extended ? 40.0f : 40.0f + this.slotList.size.y + 16.0f;
        this.slotList.pos = this.pos.offset(4.0f, 44.0f);
        this.slotList.size.x = this.size.x - 8.0f;
        this.slotList.update(mouseX, mouseY);
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        super.mousePress(mouseX, mouseY, button);
        if (this.active) {
            this.mod.toggle();
        }
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        super.mouseRelease(mouseX, mouseY, button);
    }

    @Override
    public void mouseScroll(int amount) {
        super.mouseScroll(amount);
    }

    @Override
    public void keyPress(int key, int keyChar) {
        super.keyPress(key, keyChar);
    }

    @Override
    public void keyRelease(int key, int keyChar) {
        super.keyRelease(key, keyChar);
    }

    @Override
    public void render() {
        Draw.rectBordered(this.pos.x, this.pos.y, this.pos.x + this.size.x, this.pos.y + this.size.y, ColorUtil.blend(-13421773, -16777216, this.mod.enabled ? (this.hover ? 0.6f : 0.7f) : (this.hover ? 0.8f : 1.0f)), -15658735, 1.0f);
        Draw.rect(this.pos.x + 2.0f, this.pos.y + 1.0f, this.pos.x + this.size.x - 2.0f, this.pos.y + 2.0f, this.mod.enabled ? 536870912 : 553648127);
        Draw.string(this.mod.name, this.pos.x + this.size.x / 2.0f, this.pos.y + 20.0f + (float)(this.mod.enabled ? 2 : 0), -1, 0, 0);
        super.render();
    }
}

