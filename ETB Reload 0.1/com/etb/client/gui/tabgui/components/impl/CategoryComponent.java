package com.etb.client.gui.tabgui.components.impl;

import com.etb.client.Client;
import com.etb.client.gui.tabgui.TabMain;
import com.etb.client.gui.tabgui.components.Component;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/28/2019
 **/
public class CategoryComponent extends Component {
    private Module.Category category;
    private TabMain tabMain;
    private Module selectedModule;
    private ArrayList<Component> components = new ArrayList();
    private float largestString;

    public CategoryComponent(TabMain tabMain, Module.Category category, String label, float x, float y, float width, float height) {
        super(label, x, y, width, height);
        this.tabMain = tabMain;
        this.category = category;
    }

    public void init() {
        float moduleY = getY();
        largestString = mc.fontRendererObj.getStringWidth(StringUtils.capitalize((Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(0).getRenderlabel() != null ? Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(0).getRenderlabel() : Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(0).getLabel())));
        for (int i = 0; i < Client.INSTANCE.getModuleManager().getCategoryCheats(category).size(); i++) {
            if (mc.fontRendererObj.getStringWidth(StringUtils.capitalize((Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getRenderlabel() != null ? Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getRenderlabel() : Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel()))) > largestString) {
                largestString = mc.fontRendererObj.getStringWidth(StringUtils.capitalize((Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getRenderlabel() != null ? Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getRenderlabel() : Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel())));
            }
        }
        ArrayList<Module> mods = new ArrayList(Client.INSTANCE.getModuleManager().getCategoryCheats(category));
        mods.sort(Comparator.comparing(m -> m.getLabel()));
        for (Module module : mods) {
            components.add(new ModuleComponent(this, module, StringUtils.capitalize(module.getLabel()), getX() + getWidth() + 6, moduleY, largestString + 18, 12));
            moduleY += 12;
        }
        selectedModule = mods.get(0);
        components.forEach(component -> component.init());
    }

    @Override
    public void onRender(ScaledResolution sr) {
        super.onRender(sr);
        if (tabMain.getSelectedCategory() == category)
            RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), new Color(0xff4d4c).getRGB());
        mc.fontRendererObj.drawStringWithShadow(getLabel(), tabMain.getSelectedCategory() == category ? 7 : 5, getY() + 2, tabMain.getSelectedCategory() == category ? -1 : new Color(180, 180, 180).getRGB());
        if (tabMain.getSelectedCategory() == category && tabMain.isExtended()) {
            RenderUtil.drawBorderedRect(getX() + getWidth() + 5, getY() - 1, largestString + 20, (Client.INSTANCE.getModuleManager().getCategoryCheats(category).size() * 12) + 2, 1, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
            components.forEach(component -> component.onRender(sr));
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        ArrayList<Module> mods = new ArrayList(Client.INSTANCE.getModuleManager().getCategoryCheats(category));
        mods.sort(Comparator.comparing(m -> m.getLabel()));
        components.forEach(component -> {
            if (tabMain.isExtendedValue()) {
                if (component instanceof ModuleComponent)
                    ((ModuleComponent) component).getComponents().forEach(component1 -> component1.onKeyPress(key));
            }
        });
        if (tabMain.getSelectedCategory() == category && tabMain.isExtended()) {
            switch (key) {
                case Keyboard.KEY_RIGHT:
                    if (tabMain.isExtended() && !tabMain.isExtendedValue() && !selectedModule.getValues().isEmpty()) {
                        tabMain.setExtendedvalue(true);
                    }
                    break;
                case Keyboard.KEY_DOWN:
                    if (!tabMain.isExtendedValue()) {
                        if (mods.indexOf(selectedModule) + 1 >= mods.size()) {
                            selectedModule = mods.get(0);
                            return;
                        }
                        selectedModule = mods.get(mods.indexOf(selectedModule) + 1);
                    }
                    break;
                case Keyboard.KEY_UP:
                    if (!tabMain.isExtendedValue()) {
                        if (mods.indexOf(selectedModule) <= 0) {
                            selectedModule = mods.get(mods.size() - 1);
                            return;
                        }
                        selectedModule = mods.get(mods.indexOf(selectedModule) - 1);
                    }
                    break;
                case Keyboard.KEY_RETURN:
                    if (!tabMain.isExtendedValue()) {
                        selectedModule.toggle();
                    } else {
                        tabMain.setExtendedvalue(false);
                    }
                    break;
            }
            components.forEach(component -> component.onKeyPress(key));
        }
    }

    public Module.Category getCategory() {
        return category;
    }

    public TabMain getTabMain() {
        return tabMain;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
