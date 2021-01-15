/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.Display
 */
package me.aristhena.lucid.ui.clickgui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.aristhena.lucid.management.module.Category;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.Value;
import me.aristhena.lucid.management.value.ValueManager;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.GuiGrabber;
import me.aristhena.lucid.ui.clickgui.Interactable;
import me.aristhena.lucid.ui.clickgui.Option;
import me.aristhena.lucid.ui.clickgui.Slot;
import me.aristhena.lucid.ui.clickgui.SlotList;
import me.aristhena.lucid.ui.clickgui.SlotOptionBoolean;
import me.aristhena.lucid.ui.clickgui.SlotOptionDouble;
import me.aristhena.lucid.ui.clickgui.SlotOptionKeybind;
import me.aristhena.lucid.ui.clickgui.SlotToggle;
import me.aristhena.lucid.ui.clickgui.Vector;
import me.aristhena.lucid.ui.clickgui.Window;
import me.aristhena.lucid.util.FileUtils;
import me.aristhena.lucid.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Gui extends Interactable
{
    public static boolean active;
    public List<Window> windows;
    public boolean binding;
    public static Window topMost;
    private static final File GUI_DIR;
    public static Gui instance;
    
    static {
        GUI_DIR = FileUtils.getConfigFile("Gui");
    }
    
    public Gui() {
        this.windows = new CopyOnWriteArrayList<Window>();
        Gui.instance = this;
        this.windows.clear();
        final int columns = 3;
        for (int i = 0; i < Category.values().length; ++i) {
            final Category category = Category.values()[i];
            final int x = 16 + i % columns * 208;
            final int y = 16 + i / columns * 48;
            final Window window = new Window(StringUtil.capitalize(category.name().toLowerCase()), x, y, 220.0f, 256.0f);
            final List<Module> mods = new ArrayList<Module>();
            for (final Module mod : ModuleManager.moduleList) {
                if (mod.category == category) {
                    mods.add(mod);
                }
            }
            for (final Module mod : mods) {
                final Slot slot = new SlotToggle(window, mod, window.slotList.pos.x, window.slotList.pos.y, window.slotList.size.x, 40.0f);
                window.slotList.slots.add(slot);
                for (final Option option : mod.getConvertedOptions()) {
                    Slot slot2 = null;
                    switch (option.type) {
                        case bool: {
                            slot2 = new SlotOptionBoolean(slot, option, slot.slotList.pos.x, slot.slotList.pos.y, slot.slotList.size.x - 4.0f, 35.0f);
                            break;
                        }
                        case floa: {
                            slot2 = new SlotOptionDouble(slot, option, slot.slotList.pos.x, slot.slotList.pos.y, slot.slotList.size.x - 4.0f, 35.0f);
                            break;
                        }
                        case inte: {
                            slot2 = new SlotOptionDouble(slot, option, slot.slotList.pos.x, slot.slotList.pos.y, slot.slotList.size.x - 4.0f, 35.0f);
                            break;
                        }
                        case keyb: {
                            slot2 = new SlotOptionKeybind(slot, option, slot.slotList.pos.x, slot.slotList.pos.y, slot.slotList.size.x - 4.0f, 35.0f);
                            break;
                        }
                    }
                    if (slot2 != null) {
                        slot.slotList.slots.add(slot2);
                        for (final Option option2 : option.options) {
                            Slot slot3 = null;
                            switch (option2.type) {
                                case bool: {
                                    slot3 = new SlotOptionBoolean(slot2, option2, slot2.slotList.pos.x, slot2.slotList.pos.y, slot2.slotList.size.x - 4.0f, 35.0f);
                                    break;
                                }
                                case floa: {
                                    slot3 = new SlotOptionDouble(slot2, option2, slot2.slotList.pos.x, slot2.slotList.pos.y, slot2.slotList.size.x - 4.0f, 35.0f);
                                    break;
                                }
                                case inte: {
                                    slot3 = new SlotOptionDouble(slot2, option2, slot2.slotList.pos.x, slot2.slotList.pos.y, slot2.slotList.size.x - 4.0f, 35.0f);
                                    break;
                                }
                                case keyb: {
                                    slot3 = new SlotOptionKeybind(slot2, option2, slot2.slotList.pos.x, slot2.slotList.pos.y, slot2.slotList.size.x - 4.0f, 35.0f);
                                    break;
                                }
                            }
                            if (slot3 != null) {
                                slot2.slotList.slots.add(slot3);
                            }
                        }
                    }
                }
            }
            this.windows.add(window);
        }
        this.loadWindows();
        this.saveWindows();
    }
    
    public void reloadOptions() {
        try {
            for (final Window window : this.windows) {
                for (final Slot slot : window.slotList.slots) {
                    for (final Slot optionSlot : slot.slotList.slots) {
                        if (optionSlot instanceof SlotOptionBoolean) {
                            ((SlotOptionBoolean)optionSlot).option.value = OptionManager.getOption(((SlotOptionBoolean)optionSlot).option.title, ((SlotOptionBoolean)optionSlot).option.parent).value;
                        }
                        else if (optionSlot instanceof SlotOptionDouble) {
                            ((SlotOptionDouble)optionSlot).option.value = ValueManager.getValue(((SlotOptionDouble)optionSlot).option.title, ((SlotOptionDouble)optionSlot).option.parent).value;
                        }
                        else {
                            if (!(optionSlot instanceof SlotOptionKeybind)) {
                                continue;
                            }
                            final int key = ((SlotOptionKeybind)optionSlot).option.parent.keyBind;
                            ((SlotOptionKeybind)optionSlot).option.value = Keyboard.getKeyName((key == 211 || key == 1) ? 0 : key);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadWindows() {
        final List<String> fileContent = FileUtils.read(Gui.GUI_DIR);
        for (final String line : fileContent) {
            final String[] split = line.split(":");
            final String windowName = split[0];
            final String windowPosX = split[1];
            final String windowPosY = split[2];
            final String windowExtended = split[3];
            final Window window = this.getWindow(windowName);
            final int posX = Integer.parseInt(windowPosX);
            final int posY = Integer.parseInt(windowPosY);
            final Boolean extended = Boolean.parseBoolean(windowExtended);
            window.pos.x = posX;
            window.pos.y = posY;
            window.extended = extended;
        }
    }
    
    public void saveWindows() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Window window : this.windows) {
            final String windowName = window.title;
            final String windowPosX = new StringBuilder().append((int)window.pos.x).toString();
            final String windowPosY = new StringBuilder().append((int)window.pos.y).toString();
            final String windowExtended = Boolean.toString(window.extended);
            fileContent.add(String.format("%s:%s:%s:%s", windowName, windowPosX, windowPosY, windowExtended));
        }
        FileUtils.write(Gui.GUI_DIR, fileContent, true);
    }
    
    private Window getWindow(final String name) {
        for (final Window window : this.windows) {
            if (window.title.equalsIgnoreCase(name)) {
                return window;
            }
        }
        return null;
    }
    
    public void update(final int mouseX, final int mouseY) {
        Gui.active = (Gui.mc.currentScreen instanceof GuiGrabber);
        if (Gui.active) {
            Gui.topMost = this.getTop(mouseX, mouseY);
            for (final Window window : this.windows) {
                window.update(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mousePress(final int mouseX, final int mouseY, final int button) {
        if (Gui.active) {
            final Window top = this.getTopReOrder(mouseX, mouseY);
            if (top != null) {
                top.mousePress(mouseX, mouseY, button);
                this.saveWindows();
            }
        }
    }
    
    @Override
    public void mouseRelease(final int mouseX, final int mouseY, final int button) {
        if (Gui.active) {
            for (final Window window : this.windows) {
                window.mouseRelease(mouseX, mouseY, button);
                this.saveWindows();
            }
        }
    }
    
    @Override
    public void mouseScroll(final int amount) {
        if (Gui.active && Gui.topMost != null) {
            Gui.topMost.mouseScroll(amount);
        }
    }
    
    @Override
    public void keyPress(final int key, final int keyChar) {
        if (Gui.active) {
            if (key == 0) {
                return;
            }
            if (key == 1 && !this.binding) {
                Gui.mc.displayGuiScreen((GuiScreen)null);
                return;
            }
            for (final Window window : this.windows) {
                window.keyPress(key, keyChar);
            }
        }
    }
    
    @Override
    public void keyRelease(final int key, final int keyChar) {
        if (Gui.active) {
            for (final Window window : this.windows) {
                window.keyRelease(key, keyChar);
            }
        }
    }
    
    public Window getTop(final int mouseX, final int mouseY) {
        final List<Window> underMouse = new ArrayList<Window>();
        for (final Window w : this.windows) {
            if (w.mouseOver(mouseX, mouseY)) {
                underMouse.add(w);
            }
        }
        if (underMouse.isEmpty()) {
            return null;
        }
        return underMouse.get(underMouse.size() - 1);
    }
    
    public Window getTopReOrder(final int mouseX, final int mouseY) {
        final List<Window> underMouse = new ArrayList<Window>();
        for (final Window w : this.windows) {
            if (w.mouseOver(mouseX, mouseY)) {
                underMouse.add(w);
            }
        }
        if (underMouse.isEmpty()) {
            return null;
        }
        final Window temp = underMouse.get(underMouse.size() - 1);
        this.windows.remove(temp);
        this.windows.add(temp);
        return temp;
    }
    
    public void render() {
        if (Gui.active) {
            Draw.rect(0.0, 0.0, Display.getWidth(), Display.getHeight(), -2146365167);
            for (final Window window : this.windows) {
                window.render();
            }
        }
        else {
            for (final Window window : this.windows) {
                if (window.pinned) {
                    window.render();
                }
            }
        }
    }
}
