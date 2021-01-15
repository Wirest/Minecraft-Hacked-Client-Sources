/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.aristhena.lucid.ui.click.component;

import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.Lucid;
import me.aristhena.lucid.management.module.Category;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.ui.click.ClickGui;
import me.aristhena.lucid.ui.click.component.SliderButton;
import me.aristhena.lucid.ui.click.component.Type;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Option;
import me.aristhena.lucid.util.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Button
{
    private static final int MAX_FADE = 10;
    private static final int MIN_HEX = 1678576909;
    private static final int MAX_HEX = 2013265920;
    private static int noColor;
    public String name;
    public Type layerType;
    public int x;
    public int y;
    public int x1;
    public int y1;
    protected int mouseX;
    protected int mouseY;
    private int fadeState;
    private int fillColor;
    
    public Button(final String name, final Type layerType, final int x, final int y, final int x1, final int y1) {
        this.fadeState = 0;
        this.fillColor = 1678576909;
        this.name = name;
        this.layerType = layerType;
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if ((this.layerType == Type.MODULE && ModuleManager.getModule(this.name).enabled) || (this.layerType == Type.OPTION && OptionManager.getOption(this.name, ClickGui.instance.currentModule).value)) {
            Draw.rect(this.x, this.y, this.x1, this.y1, 1677721600);
        }
        Draw.rectBordered(this.x, this.y, this.x1, this.y1, this.isOver() ? 2013265920 : 1678576909, -16777216, 0.5f);
        this.drawName(this.name, 1.2f);
    }
    
    public void onTick() {
        this.updateFade();
    }
    
    public void onClick(final int mouseButton) {
        ClickGui.instance.backButton.fadeState = 0;
        ClickGui.instance.backButton.updateFade();
        for (final Button button : ClickGui.instance.categoryList) {
            button.fadeState = 0;
            button.updateFade();
        }
        for (final Button button : ClickGui.instance.moduleList) {
            button.fadeState = 0;
            button.updateFade();
        }
        for (final Button button : ClickGui.instance.optionList) {
            button.fadeState = 0;
            button.updateFade();
        }
        switch (this.layerType) {
            case BACK: {
                if (ClickGui.instance.currentLayerType == Type.MODULE) {
                    ClickGui.instance.currentLayerType = Type.CATEGORY;
                    break;
                }
                if (ClickGui.instance.currentLayerType == Type.OPTION) {
                    ClickGui.instance.currentLayerType = Type.MODULE;
                    break;
                }
                if (ClickGui.instance.currentLayerType == Type.SLIDER) {
                    ClickGui.instance.currentLayerType = Type.OPTION;
                    break;
                }
                break;
            }
            case CATEGORY: {
                ClickGui.instance.moduleList.clear();
                final List<Module> modList = new ArrayList<Module>();
                for (final Module mod : ModuleManager.moduleList) {
                    if (mod.category == Category.valueOf(this.name.toUpperCase())) {
                        modList.add(mod);
                    }
                }
                if (modList.size() > 0) {
                    final int modListSize = modList.size();
                    switch (modListSize) {
                        case 1: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 1, 1, 1));
                            break;
                        }
                        case 2: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 2, 1, 1));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 2, 1, 1));
                            break;
                        }
                        case 3: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 1));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 1));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 1));
                            break;
                        }
                        case 4: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 2, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 2, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 1, 2, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 2, 2, 2, 2));
                            break;
                        }
                        case 5: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 2, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 2, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 1, 3, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 2, 3, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 3, 3, 2, 2));
                            break;
                        }
                        case 6: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 1, 3, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 2, 3, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 3, 3, 2, 2));
                            break;
                        }
                        case 7: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 1, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 2, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 3, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 4, 4, 2, 2));
                            break;
                        }
                        case 8: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 2));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 2));
                            break;
                        }
                        case 9: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 1, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 2, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 3, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 1, 3, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 2, 3, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 3, 3, 3, 3));
                            break;
                        }
                        case 10: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 1, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 2, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 3, 3, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 1, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 2, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 3, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 4, 4, 3, 3));
                            break;
                        }
                        case 11: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 3, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 1, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 2, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 3, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 4, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 1, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 2, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 3, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 4, 4, 3, 3));
                            break;
                        }
                        case 12: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 1, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 2, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 3, 4, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 4, 4, 3, 3));
                            break;
                        }
                        case 13: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 1, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 2, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 3, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 4, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 5, 5, 3, 3));
                            break;
                        }
                        case 14: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 5, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 1, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 2, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 3, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 4, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 5, 5, 3, 3));
                            break;
                        }
                        case 15: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 5, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 5, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 5, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 5, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 5, 5, 1, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 1, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 2, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 3, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 4, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 5, 5, 2, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 1, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 2, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 3, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 4, 5, 3, 3));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 5, 5, 3, 3));
                            break;
                        }
                        case 16: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 1, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 2, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 3, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 4, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 1, 4, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 2, 4, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 3, 4, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(15).name, Type.MODULE, 4, 4, 4, 4));
                            break;
                        }
                        case 17: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 1, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 2, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 3, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 4, 4, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 1, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 2, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 3, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(15).name, Type.MODULE, 4, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(16).name, Type.MODULE, 5, 5, 4, 4));
                            break;
                        }
                        case 18: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 4, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 1, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 2, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 3, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 4, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 5, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 1, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 2, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(15).name, Type.MODULE, 3, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(16).name, Type.MODULE, 4, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(17).name, Type.MODULE, 5, 5, 4, 4));
                            break;
                        }
                        case 19: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 4, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 1, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 2, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 3, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 4, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 5, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 1, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 2, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 3, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 4, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 5, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 1, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(15).name, Type.MODULE, 2, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(16).name, Type.MODULE, 3, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(17).name, Type.MODULE, 4, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(18).name, Type.MODULE, 5, 5, 4, 4));
                            break;
                        }
                        case 20: {
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(0).name, Type.MODULE, 1, 5, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(1).name, Type.MODULE, 2, 5, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(2).name, Type.MODULE, 3, 5, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(3).name, Type.MODULE, 4, 5, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(4).name, Type.MODULE, 5, 5, 1, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(5).name, Type.MODULE, 1, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(6).name, Type.MODULE, 2, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(7).name, Type.MODULE, 3, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(8).name, Type.MODULE, 4, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(9).name, Type.MODULE, 5, 5, 2, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(10).name, Type.MODULE, 1, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(11).name, Type.MODULE, 2, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(12).name, Type.MODULE, 3, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(13).name, Type.MODULE, 4, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(14).name, Type.MODULE, 5, 5, 3, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(15).name, Type.MODULE, 1, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(16).name, Type.MODULE, 2, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(17).name, Type.MODULE, 3, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(18).name, Type.MODULE, 4, 5, 4, 4));
                            ClickGui.instance.moduleList.add(getButtonFromGridPosition(modList.get(19).name, Type.MODULE, 5, 5, 4, 4));
                            break;
                        }
                    }
                    ClickGui.instance.currentLayerType = Type.MODULE;
                    break;
                }
                break;
            }
            case MODULE: {
                if (mouseButton == 0) {
                    ModuleManager.getModule(this.name).toggle();
                    break;
                }
                ClickGui.instance.currentModule = ModuleManager.getModule(this.name);
                ClickGui.instance.optionList.clear();
                final List<Option> optionList = ModuleManager.getModule(this.name).getConvertedOptions();
                for (final Option option : optionList) {
                    if (option.title == "Bind") {
                        option.title = "Bind [" + Keyboard.getKeyName((ClickGui.instance.currentModule.keyBind == 211 || ClickGui.instance.currentModule.keyBind == 1) ? 0 : ClickGui.instance.currentModule.keyBind) + "]";
                    }
                }
                if (optionList.size() > 0) {
                    switch (optionList.size()) {
                        case 1: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 1, 1, 1));
                            break;
                        }
                        case 2: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 2, 1, 1));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 2, 1, 1));
                            break;
                        }
                        case 3: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 1));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 1));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 1));
                            break;
                        }
                        case 4: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 2, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 2, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 1, 2, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 2, 2, 2, 2));
                            break;
                        }
                        case 5: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 2, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 2, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 1, 3, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 2, 3, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 3, 3, 2, 2));
                            break;
                        }
                        case 6: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 1, 3, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 2, 3, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 3, 3, 2, 2));
                            break;
                        }
                        case 7: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 1, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 2, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 3, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 4, 4, 2, 2));
                            break;
                        }
                        case 8: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 2));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 2));
                            break;
                        }
                        case 9: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 1, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 2, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 3, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 1, 3, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 2, 3, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 3, 3, 3, 3));
                            break;
                        }
                        case 10: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 1, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 2, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 3, 3, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 1, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 2, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 3, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 4, 4, 3, 3));
                            break;
                        }
                        case 11: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 3, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 1, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 2, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 3, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 4, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 1, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 2, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 3, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 4, 4, 3, 3));
                            break;
                        }
                        case 12: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 1, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 2, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 3, 4, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 4, 4, 3, 3));
                            break;
                        }
                        case 13: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 1, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 2, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 3, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 4, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 5, 5, 3, 3));
                            break;
                        }
                        case 14: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 5, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 1, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 2, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 3, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 4, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 5, 5, 3, 3));
                            break;
                        }
                        case 15: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 5, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 5, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 5, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 5, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 5, 5, 1, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 1, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 2, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 3, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 4, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 5, 5, 2, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 1, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 2, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 3, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 4, 5, 3, 3));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 5, 5, 3, 3));
                            break;
                        }
                        case 16: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 1, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 2, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 3, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 4, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 1, 4, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 2, 4, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 3, 4, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(15).title, Type.OPTION, 4, 4, 4, 4));
                            break;
                        }
                        case 17: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 1, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 2, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 3, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 4, 4, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 1, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 2, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 3, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(15).title, Type.OPTION, 4, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(16).title, Type.OPTION, 5, 5, 4, 4));
                            break;
                        }
                        case 18: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 4, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 1, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 2, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 3, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 4, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 5, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 1, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 2, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(15).title, Type.OPTION, 3, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(16).title, Type.OPTION, 4, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(17).title, Type.OPTION, 5, 5, 4, 4));
                            break;
                        }
                        case 19: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 4, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 1, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 2, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 3, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 4, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 5, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 1, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 2, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 3, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 4, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 5, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 1, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(15).title, Type.OPTION, 2, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(16).title, Type.OPTION, 3, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(17).title, Type.OPTION, 4, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(18).title, Type.OPTION, 5, 5, 4, 4));
                            break;
                        }
                        case 20: {
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(0).title, Type.OPTION, 1, 5, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(1).title, Type.OPTION, 2, 5, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(2).title, Type.OPTION, 3, 5, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(3).title, Type.OPTION, 4, 5, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(4).title, Type.OPTION, 5, 5, 1, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(5).title, Type.OPTION, 1, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(6).title, Type.OPTION, 2, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(7).title, Type.OPTION, 3, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(8).title, Type.OPTION, 4, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(9).title, Type.OPTION, 5, 5, 2, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(10).title, Type.OPTION, 1, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(11).title, Type.OPTION, 2, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(12).title, Type.OPTION, 3, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(13).title, Type.OPTION, 4, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(14).title, Type.OPTION, 5, 5, 3, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(15).title, Type.OPTION, 1, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(16).title, Type.OPTION, 2, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(17).title, Type.OPTION, 3, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(18).title, Type.OPTION, 4, 5, 4, 4));
                            ClickGui.instance.optionList.add(getButtonFromGridPosition(optionList.get(19).title, Type.OPTION, 5, 5, 4, 4));
                            break;
                        }
                    }
                    ClickGui.instance.currentLayerType = Type.OPTION;
                    break;
                }
                break;
            }
            case OPTION: {
                OptionManager.getOption(this.name, ClickGui.instance.currentModule).toggle();
                break;
            }
            case BIND: {
                if (this.name.contains(" [...]")) {
                    this.name = "Bind [" + Keyboard.getKeyName((ClickGui.instance.currentModule.keyBind == 211 || ClickGui.instance.currentModule.keyBind == 1) ? 0 : ClickGui.instance.currentModule.keyBind) + "]";
                    break;
                }
                this.name = "Bind [...]";
                break;
            }
            case VALUE: {
                ClickGui.instance.currentLayerType = Type.SLIDER;
                ClickGui.slider = new SliderButton(this.name, 0, ClickGui.instance.backButton.y1, ClickGui.instance.width, ClickGui.instance.height);
                break;
            }
        }
    }
    
    public boolean onKeyTyped(final int key) {
        boolean binding = false;
        if (this.layerType == Type.BIND && this.name.contains(" [...]")) {
            binding = true;
            ClickGui.instance.currentModule.keyBind = ((key == 211 || key == 1) ? 0 : key);
            this.name = "Bind [" + Keyboard.getKeyName((ClickGui.instance.currentModule.keyBind == 211 || ClickGui.instance.currentModule.keyBind == 1) ? 0 : ClickGui.instance.currentModule.keyBind) + "]";
        }
        return binding;
    }
    
    private static Type getTypeFromOption(final Option option) {
        if (option.type == Option.Type.bool) {
            return Type.OPTION;
        }
        if (option.type == Option.Type.floa) {
            return Type.VALUE;
        }
        if (option.type == Option.Type.keyb) {
            return Type.BIND;
        }
        return Type.OPTION;
    }
    
    public static Button getButtonFromGridPosition(final String name, final Type type, int xPosition, final int xSpots, int yPosition, final int ySpots, final boolean backButtonVisible) {
        --xPosition;
        --yPosition;
        if (backButtonVisible) {
            final int x1 = xPosition * (ClickGui.instance.width / xSpots);
            final int x2 = (xPosition + 1) * (ClickGui.instance.width / xSpots);
            final int y1 = ClickGui.instance.backButton.y1 + yPosition * ((ClickGui.instance.height - ClickGui.instance.backButton.y1) / ySpots);
            int y2 = ClickGui.instance.backButton.y1 + (yPosition + 1) * ((ClickGui.instance.height - ClickGui.instance.backButton.y1) / ySpots);
            if (ClickGui.instance.height - y2 <= 4) {
                y2 = ClickGui.instance.height;
            }
            return new Button(name, type, x1, y1, x2, y2);
        }
        final int x1 = xPosition * (ClickGui.instance.width / xSpots);
        final int x2 = (xPosition + 1) * (ClickGui.instance.width / xSpots);
        final int y1 = yPosition * (ClickGui.instance.height / ySpots);
        int y2 = (yPosition + 1) * (ClickGui.instance.height / ySpots);
        if (ClickGui.instance.height - y2 <= 4) {
            y2 = ClickGui.instance.height;
        }
        return new Button(name, type, x1, y1, x2, y2);
    }
    
    public static Button getButtonFromGridPosition(final String name, Type type, int xPosition, final int xSpots, int yPosition, final int ySpots) {
        if (type == Type.OPTION) {
            for (final Option option : ClickGui.instance.currentModule.getConvertedOptions()) {
                if (option.title.equalsIgnoreCase(name) || name.contains("Bind")) {
                    type = getTypeFromOption(option);
                }
            }
        }
        --xPosition;
        --yPosition;
        final int x1 = xPosition * (ClickGui.instance.width / xSpots);
        final int x2 = (xPosition + 1) * (ClickGui.instance.width / xSpots);
        final int y1 = ClickGui.instance.backButton.y1 + yPosition * ((ClickGui.instance.height - ClickGui.instance.backButton.y1) / ySpots);
        int y2 = ClickGui.instance.backButton.y1 + (yPosition + 1) * ((ClickGui.instance.height - ClickGui.instance.backButton.y1) / ySpots);
        if (ClickGui.instance.height - y2 <= 4) {
            y2 = ClickGui.instance.height;
        }
        return new Button(name, type, x1, y1, x2, y2);
    }
    
    public boolean isOver() {
        return this.mouseX > this.x && this.mouseX < this.x1 && this.mouseY > this.y && this.mouseY < this.y1;
    }
    
    private void updateFade() {
        if (this.isOver() && this.fadeState < 10) {
            ++this.fadeState;
        }
        else if (!this.isOver() && this.fadeState > 0) {
            --this.fadeState;
        }
        final double ratio = this.fadeState / 10.0;
        this.fillColor = this.getFadeHex(1678576909, 2013265920, ratio);
    }
    
    protected void drawName(final String text, final float scale) {
        GL11.glScalef(scale, scale, scale);
        int strWidth = Lucid.font.getStringWidth(text);
        strWidth *= (int)scale;
        int x = this.x + (this.x1 - this.x) / 2 - strWidth / 2;
        int y = this.y + (this.y1 - this.y) / 2 - 4;
        x /= (int)scale;
        y /= (int)scale;
        Lucid.font.drawStringWithShadow(text, x, y, -1);
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
    }
    
    private int getFadeHex(final int hex1, final int hex2, final double ratio) {
        int r = hex1 >> 16;
        int g = hex1 >> 8 & 0xFF;
        int b = hex1 & 0xFF;
        r += (int)(((hex2 >> 16) - r) * ratio);
        g += (int)(((hex2 >> 8 & 0xFF) - g) * ratio);
        b += (int)(((hex2 & 0xFF) - b) * ratio);
        return r << 16 | g << 8 | b;
    }
}
