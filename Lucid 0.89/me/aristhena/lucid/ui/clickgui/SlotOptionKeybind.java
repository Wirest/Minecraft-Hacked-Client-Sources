/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.aristhena.lucid.ui.clickgui;

import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Gui;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.Option;
import me.aristhena.lucid.ui.clickgui.Slot;
import me.aristhena.lucid.ui.clickgui.SlotList;
import me.aristhena.lucid.ui.clickgui.Vector;
import me.aristhena.lucid.util.ColorUtil;
import org.lwjgl.input.Keyboard;

public class SlotOptionKeybind
extends Slot {
    public Option option;
    public boolean setting;

    public SlotOptionKeybind(Interactable parent, Option option, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        this.option = option;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        this.hover = this.mouseOver(mouseX, mouseY) && this.parent.hover;
        this.size.y = !this.extended ? 32.0f : 32.0f + this.slotList.size.y + 4.0f;
        this.slotList.pos = this.pos.offset(4.0f, 32.0f);
        this.slotList.size.x = this.size.x - 8.0f;
        this.slotList.update(mouseX, mouseY);
    }

    @Override
    public void mousePress(int mouseX, int mouseY, int button) {
        super.mousePress(mouseX, mouseY, button);
        if (this.active) {
            Gui.instance.binding = true;
            this.setting = true;
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
        if (this.setting) {
            Gui.instance.binding = false;
            this.setting = false;
            this.option.value = Keyboard.getKeyName((int)(key == 211 || key == 1 ? 0 : key));
            this.option.parent.keyBind = key == 211 || key == 1 ? 0 : key;
        }
    }

    @Override
    public void keyRelease(int key, int keyChar) {
        super.keyRelease(key, keyChar);
    }

    @Override
    public void render() {
        Draw.rectBordered(this.pos.x, this.pos.y, this.pos.x + this.size.x, this.pos.y + this.size.y, ColorUtil.blend(-13421773, -16777216, this.setting ? (this.hover ? 0.6f : 0.7f) : (this.hover ? 0.8f : 1.0f)), -15658735, 1.0f);
        Draw.rect(this.pos.x + 2.0f, this.pos.y + 1.0f, this.pos.x + this.size.x - 2.0f, this.pos.y + 2.0f, this.setting ? 536870912 : 553648127);
        Draw.string(this.option.title, this.pos.x + 8.0f, this.pos.y + 16.0f + (float)(this.setting ? 2 : 0), -1, 1, 0);
        Draw.string(this.option.value.toString(), this.pos.x + this.size.x - 8.0f, this.pos.y + 16.0f + (float)(this.setting ? 2 : 0), -1, -1, 0);
        super.render();
    }
}

