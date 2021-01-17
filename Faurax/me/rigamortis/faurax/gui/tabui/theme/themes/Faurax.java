package me.rigamortis.faurax.gui.tabui.theme.themes;

import me.rigamortis.faurax.gui.tabui.theme.*;
import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.utils.*;
import me.rigamortis.faurax.*;
import java.util.*;

public class Faurax extends Theme implements RenderHelper
{
    private Minecraft mc;
    
    public Faurax() {
        this.mc = Minecraft.getMinecraft();
        Faurax.name = "Faurax";
        Faurax.active = true;
    }
    
    @Override
    public void draw(final int currentType, final boolean isTypeSelected, float maxX, int currentLength, int currentModule, Module currentMod, final int currentModuleLength) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        Gui.drawRect(2, 22, 80, 33, (currentType == 0) ? -871279258 : -1442840576);
        final GuiUtils guiUtils2 = Faurax.guiUtils;
        Gui.drawRect(2, 33, 80, 44, (currentType == 1) ? -871279258 : -1442840576);
        final GuiUtils guiUtils3 = Faurax.guiUtils;
        Gui.drawRect(2, 44, 80, 55, (currentType == 2) ? -871279258 : -1442840576);
        final GuiUtils guiUtils4 = Faurax.guiUtils;
        Gui.drawRect(2, 55, 80, 66, (currentType == 3) ? -871279258 : -1442840576);
        final GuiUtils guiUtils5 = Faurax.guiUtils;
        Gui.drawRect(2, 66, 80, 77, (currentType == 4) ? -871279258 : -1442840576);
        final GuiUtils guiUtils6 = Faurax.guiUtils;
        Gui.drawRect(2, 77, 80, 88, (currentType == 5) ? -871279258 : -1442840576);
        this.mc.fontRendererObj.func_175063_a("Combat", 5.0f, 24.0f, -1);
        this.mc.fontRendererObj.func_175063_a("World", 5.0f, 35.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Render", 5.0f, 46.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Player", 5.0f, 57.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Misc", 5.0f, 68.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Movement", 5.0f, 79.0f, -1);
        if (isTypeSelected) {
            if (currentType == 0) {
                this.getList(ModuleType.COMBAT, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
            if (currentType == 1) {
                this.getList(ModuleType.WORLD, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
            if (currentType == 2) {
                this.getList(ModuleType.RENDER, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
            if (currentType == 3) {
                this.getList(ModuleType.PLAYER, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
            if (currentType == 4) {
                this.getList(ModuleType.PLAYER, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
            if (currentType == 5) {
                this.getList(ModuleType.MOVEMENT, currentLength, maxX, isTypeSelected, currentModuleLength, currentMod, currentModule);
            }
        }
        else {
            maxX = 81.0f;
            currentLength = 0;
            currentModule = 0;
            currentMod = null;
        }
    }
    
    private void getList(final ModuleType current, int currentLength, float maxX, boolean isTypeSelected, int currentModuleLength, Module currentMod, final int currentModule) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getType() == current) {
                final int textLength = this.mc.fontRendererObj.getStringWidth(mod.getName());
                if (textLength <= currentLength) {
                    continue;
                }
                currentLength = textLength;
            }
        }
        int count = 0;
        for (final Module mod2 : Client.getModules().mods) {
            if (mod2.getType() == current) {
                count += 11;
            }
        }
        maxX += 2.0f;
        if (maxX >= currentLength + 86) {
            maxX = currentLength + 86;
        }
        if (count != 0) {
            final GuiUtils guiUtils = Faurax.guiUtils;
            GuiUtils.drawRect(81.0, 22.0, maxX, 22 + count, -1442840576);
            currentModuleLength = count / 10 - 1;
        }
        else {
            isTypeSelected = false;
            currentModuleLength = 0;
        }
        int count2 = 0;
        for (final Module mod3 : Client.getModules().mods) {
            if (mod3.getType() == current) {
                if (mod3.isToggled()) {
                    if (maxX >= currentLength + 86) {
                        this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -15628135);
                    }
                }
                else if (maxX >= currentLength + 86) {
                    this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -864585865);
                }
                if (currentMod == mod3) {
                    final GuiUtils guiUtils2 = Faurax.guiUtils;
                    GuiUtils.drawRect(81.0, 22 + count2, maxX, 33 + count2, -871279258);
                    if (maxX >= currentLength + 86) {
                        this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -1);
                    }
                }
                if (currentModule == count2 / 10) {
                    currentMod = mod3;
                }
                count2 += 11;
            }
        }
    }
}
