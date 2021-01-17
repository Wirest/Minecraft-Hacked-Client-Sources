package me.rigamortis.faurax.gui.tabui;

import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;
import me.rigamortis.faurax.*;
import net.minecraft.client.gui.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.utils.*;
import java.util.*;

public class TabUI implements RenderHelper
{
    private Minecraft mc;
    private int currentType;
    private boolean isTypeSelected;
    private int currentLength;
    private int currentModuleLength;
    private int currentModule;
    private boolean right;
    private boolean down;
    private boolean up;
    private boolean left;
    private boolean enter;
    private Module currentMod;
    public float maxX;
    
    public TabUI() {
        this.mc = Minecraft.getMinecraft();
        this.currentType = 0;
        this.right = false;
        this.down = false;
        this.up = false;
        this.left = false;
        this.enter = false;
    }
    
    public void draw() {
        if (Minecraft.getMinecraft().inGameHasFocus && !(this.mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown(200)) {
                if (!this.up) {
                    if (!this.isTypeSelected) {
                        --this.currentType;
                    }
                    else {
                        --this.currentModule;
                    }
                }
                this.up = true;
            }
            else {
                this.up = false;
            }
            if (Keyboard.isKeyDown(208)) {
                if (!this.down) {
                    if (!this.isTypeSelected) {
                        ++this.currentType;
                    }
                    else {
                        ++this.currentModule;
                    }
                }
                this.down = true;
            }
            else {
                this.down = false;
            }
            if (Keyboard.isKeyDown(205)) {
                if (!this.right) {
                    this.isTypeSelected = true;
                }
                if (this.isTypeSelected && !this.right && this.currentMod != null) {
                    this.currentMod.toggle();
                    Client.getConfig().saveMods();
                }
                this.right = true;
            }
            else {
                this.right = false;
            }
            if (Keyboard.isKeyDown(203)) {
                if (!this.left) {
                    this.isTypeSelected = false;
                }
                this.left = true;
            }
            else {
                this.left = false;
            }
            if (Keyboard.isKeyDown(28)) {
                if (!this.enter && this.isTypeSelected && this.currentMod != null) {
                    this.currentMod.toggle();
                    Client.getConfig().saveMods();
                }
                this.enter = true;
            }
            else {
                this.enter = false;
            }
            if (this.currentType < 0) {
                this.currentType = 5;
            }
            if (this.currentType > 5) {
                this.currentType = 0;
            }
            if (this.currentModule < 0) {
                this.currentModule = this.currentModuleLength;
            }
            if (this.currentModule > this.currentModuleLength) {
                this.currentModule = 0;
            }
        }
        final GuiUtils guiUtils = TabUI.guiUtils;
        Gui.drawRect(2, 22, 80, 33, (this.currentType == 0) ? -871279258 : -1442840576);
        final GuiUtils guiUtils2 = TabUI.guiUtils;
        Gui.drawRect(2, 33, 80, 44, (this.currentType == 1) ? -871279258 : -1442840576);
        final GuiUtils guiUtils3 = TabUI.guiUtils;
        Gui.drawRect(2, 44, 80, 55, (this.currentType == 2) ? -871279258 : -1442840576);
        final GuiUtils guiUtils4 = TabUI.guiUtils;
        Gui.drawRect(2, 55, 80, 66, (this.currentType == 3) ? -871279258 : -1442840576);
        final GuiUtils guiUtils5 = TabUI.guiUtils;
        Gui.drawRect(2, 66, 80, 77, (this.currentType == 4) ? -871279258 : -1442840576);
        final GuiUtils guiUtils6 = TabUI.guiUtils;
        Gui.drawRect(2, 77, 80, 88, (this.currentType == 5) ? -871279258 : -1442840576);
        this.mc.fontRendererObj.func_175063_a("Combat", 5.0f, 24.0f, -1);
        this.mc.fontRendererObj.func_175063_a("World", 5.0f, 35.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Render", 5.0f, 46.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Player", 5.0f, 57.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Misc", 5.0f, 68.0f, -1);
        this.mc.fontRendererObj.func_175063_a("Movement", 5.0f, 79.0f, -1);
        if (this.isTypeSelected) {
            if (this.currentType == 0) {
                this.getList(ModuleType.COMBAT);
            }
            if (this.currentType == 1) {
                this.getList(ModuleType.WORLD);
            }
            if (this.currentType == 2) {
                this.getList(ModuleType.RENDER);
            }
            if (this.currentType == 3) {
                this.getList(ModuleType.PLAYER);
            }
            if (this.currentType == 4) {
                this.getList(ModuleType.MISC);
            }
            if (this.currentType == 5) {
                this.getList(ModuleType.MOVEMENT);
            }
        }
        else {
            this.maxX = 81.0f;
            this.currentLength = 0;
            this.currentModule = 0;
            this.currentMod = null;
        }
    }
    
    public void getList(final ModuleType current) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getType() == current) {
                final int textLength = this.mc.fontRendererObj.getStringWidth(mod.getName());
                if (textLength <= this.currentLength) {
                    continue;
                }
                this.currentLength = textLength;
            }
        }
        int count = 0;
        for (final Module mod2 : Client.getModules().mods) {
            if (mod2.getType() == current) {
                count += 11;
            }
        }
        this.maxX += 2.0f;
        if (this.maxX >= this.currentLength + 86) {
            this.maxX = this.currentLength + 86;
        }
        if (count != 0) {
            final GuiUtils guiUtils = TabUI.guiUtils;
            GuiUtils.drawRect(81.0, 22.0, this.maxX, 22 + count, -1442840576);
            this.currentModuleLength = count / 10 - 1;
        }
        else {
            this.isTypeSelected = false;
            this.currentModuleLength = 0;
        }
        int count2 = 0;
        for (final Module mod3 : Client.getModules().mods) {
            if (mod3.getType() == current) {
                if (mod3.isToggled()) {
                    if (this.maxX >= this.currentLength + 86) {
                        this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -15628135);
                    }
                }
                else if (this.maxX >= this.currentLength + 86) {
                    this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -864585865);
                }
                if (this.currentMod == mod3) {
                    final GuiUtils guiUtils2 = TabUI.guiUtils;
                    GuiUtils.drawRect(81.0, 22 + count2, this.maxX, 33 + count2, -871279258);
                    if (this.maxX >= this.currentLength + 86) {
                        this.mc.fontRendererObj.func_175063_a(mod3.getName(), 84.0f, 24 + count2, -1);
                    }
                }
                if (this.currentModule == count2 / 10) {
                    this.currentMod = mod3;
                }
                count2 += 11;
            }
        }
    }
}
