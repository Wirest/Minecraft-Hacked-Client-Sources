/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.value.ValueManager;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.Option;
import me.aristhena.lucid.ui.clickgui.Slot;
import me.aristhena.lucid.ui.clickgui.Vector;

public class SlotOptionDouble
extends Slot {
    public Option option;
    public boolean dragging;

    public SlotOptionDouble(Interactable parent, Option option, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        this.option = option;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        if (this.dragging) {
            double[] limit = this.option.limit;
            double inc = this.option.inc;
            double valAbs = (float)mouseX - (this.pos.x + 1.0f);
            double perc = valAbs / (double)(this.size.x - 2.0f);
            perc = Math.min(Math.max(0.0, perc), 1.0);
            double valRel = (limit[1] - limit[0]) * perc;
            double val = limit[0] + valRel;
            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
            this.option.value = val;
            ValueManager.getValue(this.option.title, this.option.parent).setValue(val);
        }
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        super.mousePress(mouseX, mouseY, button);
        this.dragging = this.active;
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        super.mouseRelease(mouseX, mouseY, button);
        this.dragging = false;
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
        Draw.rectBordered(this.pos.x, this.pos.y, this.pos.x + this.size.x, this.pos.y + this.size.y, -13421773, -15658735, 1.0f);
        Draw.rect(this.pos.x + 2.0f, this.pos.y + 1.0f, this.pos.x + this.size.x - 2.0f, this.pos.y + 2.0f, this.option.valBool() ? 536870912 : 553648127);
        double[] limit = this.option.limit;
        double valRel = (double)this.option.valFloat() - limit[0];
        double perc = valRel / (limit[1] - limit[0]);
        double valAbs = (double)(this.size.x - 2.0f) * perc;
        Draw.rect(this.pos.x + 1.0f, this.pos.y + this.size.y - 6.0f, this.pos.x + this.size.x - 1.0f, this.pos.y + this.size.y - 4.0f, 1090519039);
        Draw.rect(this.pos.x + 1.0f, this.pos.y + this.size.y - 6.0f, (double)(this.pos.x + 1.0f) + valAbs, this.pos.y + this.size.y - 4.0f, -2130706433);
        Draw.string(this.option.title, this.pos.x + 8.0f, this.pos.y + this.size.y / 2.0f + (float)(this.option.valBool() ? 2 : 0), -1, 1, 0);
        Draw.string(String.valueOf(this.option.valFloat()), this.pos.x + this.size.x - 8.0f, this.pos.y + this.size.y / 2.0f + (float)(this.option.valBool() ? 2 : 0), -1, -1, 0);
        super.render();
    }
}

