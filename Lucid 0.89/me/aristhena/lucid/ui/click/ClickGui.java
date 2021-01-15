/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package me.aristhena.lucid.ui.click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.eventapi.EventManager;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.management.module.Category;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.ui.click.component.Button;
import me.aristhena.lucid.ui.click.component.SliderButton;
import me.aristhena.lucid.ui.click.component.Type;
import me.aristhena.lucid.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class ClickGui
extends GuiScreen {
    public List<Button> categoryList = new ArrayList<Button>();
    public List<Button> moduleList = new ArrayList<Button>();
    public List<Button> optionList = new ArrayList<Button>();
    public Type currentLayerType = Type.CATEGORY;
    public Module currentModule;
    public Button backButton;
    public Button fadeButton;
    public static ClickGui instance;
    public static SliderButton slider;

    public void initGui() {
        instance = this;
        EventManager.register((Object)instance);
        this.backButton = new Button("Back", Type.BACK, 0, 0, this.width, this.height / 16);
        int categoryCount = Category.values().length;
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[0].name().toLowerCase()), Type.CATEGORY, 1, 3, 1, 2, false));
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[1].name().toLowerCase()), Type.CATEGORY, 2, 3, 1, 2, false));
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[2].name().toLowerCase()), Type.CATEGORY, 3, 3, 1, 2, false));
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[3].name().toLowerCase()), Type.CATEGORY, 1, 3, 2, 2, false));
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[4].name().toLowerCase()), Type.CATEGORY, 2, 3, 2, 2, false));
        this.categoryList.add(Button.getButtonFromGridPosition(StringUtil.capitalize(Category.values()[5].name().toLowerCase()), Type.CATEGORY, 3, 3, 2, 2, false));
    }

    public void drawScreen(int x, int y, float par3) {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        if (this.currentLayerType == Type.CATEGORY) {
            for (Button categoryButton : this.categoryList) {
                categoryButton.draw(mouseX, mouseY);
            }
        } else if (this.currentLayerType == Type.MODULE) {
            this.backButton.draw(mouseX, mouseY);
            for (Button moduleButton : this.moduleList) {
                moduleButton.draw(mouseX, mouseY);
            }
        } else if (this.currentLayerType == Type.OPTION) {
            this.backButton.draw(mouseX, mouseY);
            for (Button optionButton : this.optionList) {
                optionButton.draw(mouseX, mouseY);
            }
        } else if (this.currentLayerType == Type.SLIDER) {
            this.backButton.draw(mouseX, mouseY);
            slider.draw(mouseX, mouseY);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        boolean binding = false;
        if (this.currentLayerType == Type.OPTION) {
            for (Button optionButton : this.optionList) {
                binding = optionButton.onKeyTyped(keyCode);
            }
        }
        if (!binding) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        if (this.currentLayerType != Type.CATEGORY && this.backButton.isOver()) {
            this.backButton.onClick(mouseButton);
        } else if (this.currentLayerType == Type.CATEGORY) {
            for (Button categoryButton : this.categoryList) {
                if (!categoryButton.isOver()) continue;
                categoryButton.onClick(mouseButton);
            }
        } else if (this.currentLayerType == Type.MODULE) {
            for (Button moduleButton : this.moduleList) {
                if (!moduleButton.isOver()) continue;
                moduleButton.onClick(mouseButton);
            }
        } else if (this.currentLayerType == Type.OPTION) {
            for (Button optionButton : this.optionList) {
                if (!optionButton.isOver()) continue;
                optionButton.onClick(mouseButton);
            }
        } else if (this.currentLayerType == Type.SLIDER) {
            slider.onClick(mouseButton);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        this.backButton.onTick();
        for (Button categoryButton : this.categoryList) {
            categoryButton.onTick();
        }
        for (Button moduleButton : this.moduleList) {
            moduleButton.onTick();
        }
        for (Button optionButton : this.optionList) {
            optionButton.onTick();
        }
    }

    public void onGuiClosed() {
        EventManager.unregister((Object)instance);
        super.onGuiClosed();
    }
}

