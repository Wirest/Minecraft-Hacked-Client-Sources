//package me.arithmo.gui.click.ui;
//
//import Client;
//import ClickGui;
//import me.arithmo.gui.click.components.*;
//import ColorManager;
//import ColorObject;
//import Color;
//import Keybind;
//import Module;
//import ModuleData;
//import Options;
//import Setting;
//import MathUtils;
//import RenderingUtil;
//import StringConversions;
//import ChatUtil;
//import Colors;
//import NharFont;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GlStateManager;
//import org.lwjgl.input.Keyboard;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * Created by cool1 on 1/21/2017.
// */
//public class Zeus extends UI {
//
//    private Minecraft mc = Minecraft.getMinecraft();
//
//    @Override
//    public void mainConstructor(ClickGui p0, MainPanel panel) {
//
//    }
//
//    @Override
//    public void onClose() {
//
//    }
//
//    private float hue;
//
//    @Override
//    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
//        GlStateManager.pushMatrix();
//        GlStateManager.popMatrix();
//        RenderingUtil.rectangleBordered(panel.x + panel.dragX - 0.3, panel.y + panel.dragY - 0.3, panel.x + 400 + panel.dragX + 0.3, panel.y + 270 + panel.dragY + 0.3, 0.5, Colors.getColor(60), Colors.getColor(10));
//        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 0.6, panel.y + panel.dragY + 0.6, panel.x + 400 + panel.dragX - 0.6, panel.y + 270 + panel.dragY - 0.6, 1.3, Colors.getColor(60), Colors.getColor(40));
//        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 2.5, panel.y + panel.dragY + 2.5, panel.x + 400 + panel.dragX - 2.5, panel.y + 270 + panel.dragY - 2.5, 0.5, Colors.getColor(20), Colors.getColor(10));
//
//        if (hue > 255) {
//            hue = 0;
//        }
//        float h = hue;
//        float h2 = hue + 100;
//        float h3 = hue + 200;
//        if (h > 255) {
//            h = 0;
//        }
//        if (h2 > 255) {
//            h2 -= 255;
//        }
//        if (h3 > 255) {
//            h3 -= 255;
//        }
//        final java.awt.Color color33 = java.awt.Color.getHSBColor(h / 255.0f, 0.9f, 1);
//        final java.awt.Color color332 = java.awt.Color.getHSBColor(h2 / 255.0f, 0.9f, 1);
//        final java.awt.Color color333 = java.awt.Color.getHSBColor(h3 / 255.0f, 0.9f, 1);
//        int color = color33.getRGB();
//        int color2 = color332.getRGB();
//        int color3 = color333.getRGB();
//        hue += 0.15;
//        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 3, panel.y + panel.dragY + 3, panel.x + 202 + panel.dragX - 3, panel.dragY + panel.y + 4, color, color2);
//        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 199, panel.y + panel.dragY + 3, panel.x + 400 + panel.dragX - 3, panel.dragY + panel.y + 4, color2, color3);
//
//        RenderingUtil.rectangle(panel.x + panel.dragX + 3, panel.y + panel.dragY + 3.3, panel.x + 400 + panel.dragX - 3, panel.dragY + panel.y + 4, Colors.getColor(0, 120));
//        boolean isOff = false;
//        for (float y = 5f; y < 268; y += 4) {
//            for (int x = isOff ? 6 : 4; x < 397; x += 4) {
//                RenderingUtil.rectangle(panel.x + panel.dragX + x, panel.y + panel.dragY + y, panel.x + panel.dragX + x + 1.3, panel.y + panel.dragY + y + 1.6, Colors.getColor(12));
//            }
//            isOff = !isOff;
//        }
///*        for(int x = 5; x < 397; x+=2) {
//            RenderingUtil.rectangle(panel.x + panel.dragX + x, panel.y + panel.dragY + 5.3,panel.x + panel.dragX + x + 0.3, panel.y + panel.dragY + 7, -1);
//        }*/
//        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 57, panel.y + panel.dragY + 16, panel.x + 390 + panel.dragX, panel.y + 250 + panel.dragY, 0.5, Colors.getColor(46), Colors.getColor(10));
//        RenderingUtil.rectangle(panel.x + panel.dragX + 58, panel.y + panel.dragY + 17, panel.x + 390 + panel.dragX - 1, panel.y + 250 + panel.dragY - 1, Colors.getColor(17));
//
//
//        for (SLButton button : panel.slButtons) {
//            button.draw(p0, p1);
//        }
//        for (CategoryButton button : panel.typeButton) {
//            button.draw(p0, p1);
//        }
//        if (panel.dragging) {
//            panel.dragX = p0 - panel.lastDragX;
//            panel.dragY = p1 - panel.lastDragY;
//        }
//    }
//
//    @Override
//    public void mainPanelKeyPress(MainPanel panel, int key) {
//        for (CategoryButton cbutton : panel.typeButton) {
//            for (Button button : cbutton.categoryPanel.buttons) {
//                button.keyPressed(key);
//            }
//        }
//    }
//
//    @Override
//    public void panelConstructor(MainPanel mainPanel, float x, float y) {
//        int y1 = 16;
//        for (ModuleData.Type types : ModuleData.Type.values()) {
//            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x, y + y1));
//            y += 14;
//        }
//        mainPanel.typeButton.add(new CategoryButton(mainPanel, "Colors", x, y + y1));
//        y += 14;
//        mainPanel.typeButton.add(new CategoryButton(mainPanel, "Extra", x, y + y1));
//        mainPanel.slButtons.add(new SLButton(mainPanel, "Save", 5, 250 - 49, false));
//        mainPanel.slButtons.add(new SLButton(mainPanel, "Load", 5, 250 - 35, true));
//    }
//
//    @Override
//    public void panelMouseClicked(MainPanel mainPanel, int x, int y, int z) {
//        if (x >= mainPanel.x + mainPanel.dragX && y >= mainPanel.dragY + mainPanel.y && x <= mainPanel.dragX + mainPanel.x + 400 && y <= mainPanel.dragY + mainPanel.y + 12.0f && z == 0) {
//            mainPanel.dragging = true;
//            mainPanel.lastDragX = x - mainPanel.dragX;
//            mainPanel.lastDragY = y - mainPanel.dragY;
//        }
//        for (CategoryButton c : mainPanel.typeButton) {
//            c.mouseClicked(x, y, z);
//            c.categoryPanel.mouseClicked(x, y, z);
//        }
//        for (SLButton button : mainPanel.slButtons) {
//            button.mouseClicked(x, y, z);
//        }
//    }
//
//    @Override
//    public void panelMouseMovedOrUp(MainPanel mainPanel, int x, int y, int z) {
//        if (z == 0) {
//            mainPanel.dragging = false;
//        }
//        for (CategoryButton button : mainPanel.typeButton) {
//            button.mouseReleased(x, y, z);
//        }
//    }
//
//    @Override
//    public void categoryButtonConstructor(CategoryButton p0, MainPanel p1) {
//        p0.categoryPanel = new CategoryPanel(p0.name, p0, 0, 0);
//    }
//
//    @Override
//    public void categoryButtonMouseClicked(CategoryButton p0, MainPanel p1, int p2, int p3, int p4) {
//        if (p2 >= p0.x + p1.dragX && p3 >= p1.dragY + p0.y && p2 <= p1.dragX + p0.x + Client.f.getStringWidth(p0.name.toUpperCase()) + 5 && p3 <= p1.dragY + p0.y + 12 && p4 == 0) {
//            int i = 0;
//            for (CategoryButton button : p1.typeButton) {
//                if (button == p0) {
//                    p0.enabled = true;
//                    p0.categoryPanel.visible = true;
//                } else {
//                    button.enabled = false;
//                    button.categoryPanel.visible = false;
//                }
//                i++;
//            }
//        }
//    }
//
//    @Override
//    public void categoryButtonDraw(CategoryButton p0, float p2, float p3) {
//        //RenderingUtil.rectangle(p0.x + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + Client.f.getStringWidth(p0.name) + 5 + p0.panel.dragX, p0.y + 12 + p0.panel.dragY, Colors.getColor(010);
//        int color = p0.enabled ? -1 : Colors.getColor(75);
//        if (p2 >= p0.x + p0.panel.dragX && p3 >= p0.y + p0.panel.dragY && p2 <= p0.x + p0.panel.dragX + Client.f.getStringWidth(p0.name.toUpperCase()) + 5 && p3 <= p0.y + p0.panel.dragY + 12 && !p0.enabled) {
//            color = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
//        }
///*            if (p0.enabled) {
//                RenderingUtil.rectangle(p0.x + 3 + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 6 + p0.panel.dragX, p0.y + 12 + p0.panel.dragY, Colors.getColor(165,241,165));
//            }*/
//        GlStateManager.pushMatrix();
//        Client.f.drawStringWithShadow(p0.name.toUpperCase(), (p0.x + 4 + p0.panel.dragX), (p0.y + p0.panel.dragY), color);
//        GlStateManager.popMatrix();
//        p0.categoryPanel.draw(p2, p3);
//    }
//
//    private List<Setting> getSettings(Module mod) {
//        List<Setting> settings = new ArrayList();
//        for (Setting set : mod.getSettings().values()) {
//            settings.add(set);
//        }
//        if (settings.isEmpty()) {
//            return null;
//        }
//        return settings;
//    }
//
//    @Override
//    public void categoryPanelConstructor(CategoryPanel categoryPanel, CategoryButton categoryButton, float x, float y) {
//        float xOff = 62 + categoryButton.panel.x;
//        float yOff = 20 + categoryButton.panel.y;
//        if (categoryButton.name.equalsIgnoreCase("Combat")) {
//            float biggestY = yOff + 8;
//            for (Module module : Client.getModuleManager().getArray()) {
//                if (module.getType() == ModuleData.Type.Combat) {
//                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10, module));
//                    y = 16 + 16;
//                    if (getSettings(module) != null) {
//                        y = 18;
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Boolean) {
//                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
//                                y += 8;
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        List<Setting> sliders = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Number) {
//                                sliders.add(setting);
//                            }
//                        }
//                        sliders.sort(Comparator.comparing(Setting::getName));
//                        for(Setting setting : sliders) {
//                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
//                            y += 12;
//                            if (yOff + y >= biggestY) {
//                                biggestY = 20 + categoryButton.panel.y + 16;
//                            }
//                        }
//                        List<Options> listToAdd = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Options) {
//                                Options option = (Options) setting.getValue();
//                                listToAdd.add(option);
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        y += 6 + (listToAdd.size() * 16);
//                        if (!listToAdd.isEmpty()) {
//                            for (Options option : listToAdd) {
//                                y -= 16;
//                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff, yOff + y, categoryPanel));
//                            }
//                        }
//                    }
//                    xOff += 45;
//                    if (xOff > 20 + categoryButton.panel.y + 320) {
//                        xOff = 62 + categoryButton.panel.x;
//                        yOff = biggestY + 16;
//                    }
//                }
//            }
//            //categoryPanel.dropdownBoxes.add(new DropdownBox(Targeting.mode, 62 + categoryButton.panel.x + (45 * 6), 20 + categoryButton.panel.y + (9 * 17), categoryPanel));
//        }
//        if (categoryButton.name == "Player") {
//            float biggestY = yOff + 8;
//            for (Module module : Client.getModuleManager().getArray()) {
//                if (module.getType() == ModuleData.Type.Player) {
//                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10, module));
//                    y = 16 + 16;
//                    if (getSettings(module) != null) {
//                        y = 18;
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Boolean) {
//                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
//                                y += 8;
//                                if (yOff + y >= biggestY) {
//                                    biggestY = yOff + y;
//                                }
//                            }
//                        }
//                        List<Setting> sliders = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Number) {
//                                sliders.add(setting);
//                            }
//                        }
//                        sliders.sort(Comparator.comparing(Setting::getName));
//                        for(Setting setting : sliders) {
//                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
//                            y += 12;
//                            if (yOff + y >= biggestY) {
//                                biggestY = yOff + y;
//                            }
//                        }
//                        List<Options> listToAdd = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Options) {
//                                Options option = (Options) setting.getValue();
//                                listToAdd.add(option);
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        y += 6 + (listToAdd.size() * 16);
//                        if (!listToAdd.isEmpty()) {
//                            for (Options option : listToAdd) {
//                                y -= 16;
//                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff, yOff + y, categoryPanel));
//                            }
//                        }
//                    }
//                    xOff += 50;
//                    if (xOff > 20 + categoryButton.panel.y + 315) {
//                        xOff = 62 + categoryButton.panel.x;
//                        yOff = biggestY + 8;
//                    }
//                }
//            }
//            //categoryPanel.dropdownBoxes.add(new DropdownBox(AntiAim.pitch, 62 + categoryButton.panel.x + (50 * 1), 20 + categoryButton.panel.y + (32 + 8 + 8), categoryPanel));
//            //categoryPanel.dropdownBoxes.add(new DropdownBox(AntiAim.yaw, 62 + categoryButton.panel.x + (50 * 1), 20 + categoryButton.panel.y + (32), categoryPanel));
//        }
//        if (categoryButton.name == "Movement") {
//            float biggestY = yOff + 8;
//            for (Module module : Client.getModuleManager().getArray()) {
//                if (module.getType() == ModuleData.Type.Movement) {
//                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10, module));
//                    y = 16 + 16;
//                    if (getSettings(module) != null) {
//                        y = 18;
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Boolean) {
//                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
//                                y += 8;
//                                if (yOff + y >= biggestY) {
//                                    biggestY = yOff + y;
//                                }
//                            }
//                        }
//                        List<Setting> sliders = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Number) {
//                                sliders.add(setting);
//                            }
//                        }
//                        sliders.sort(Comparator.comparing(Setting::getName));
//                        for(Setting setting : sliders) {
//                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
//                            y += 12;
//                            if (yOff + y >= biggestY) {
//                                biggestY = yOff + y;
//                            }
//                        }
//                        List<Options> listToAdd = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Options) {
//                                Options option = (Options) setting.getValue();
//                                listToAdd.add(option);
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        y += 6 + (listToAdd.size() * 16);
//                        if (!listToAdd.isEmpty()) {
//                            for (Options option : listToAdd) {
//                                y -= 16;
//                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff, yOff + y, categoryPanel));
//                            }
//                        }
//                    }
//                    xOff += 50;
//                    if (xOff > 20 + categoryButton.panel.y + 315) {
//                        xOff = 62 + categoryButton.panel.x;
//                        yOff = biggestY + 8;
//                    }
//                }
//            }
//        }
//        if (categoryButton.name == "Visuals") {
//            float biggestY = yOff + 8;
//            int row = 0;
//            for (Module module : Client.getModuleManager().getArray()) {
//                if (module.getType() == ModuleData.Type.Visuals) {
//                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10, module));
//                    y = 16 + 16;
//                    if (getSettings(module) != null) {
//                        y = 18;
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Boolean) {
//                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
//                                y += 8;
//                                if (yOff + y >= biggestY) {
//                                    biggestY = yOff + y;
//                                }
//                            }
//                        }
//                        List<Setting> sliders = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Number) {
//                                sliders.add(setting);
//                            }
//                        }
//                        sliders.sort(Comparator.comparing(Setting::getName));
//                        for(Setting setting : sliders) {
//                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
//                            y += 12;
//                            if (yOff + y >= biggestY) {
//                                biggestY = yOff + y;
//                            }
//                        }
//                        List<Options> listToAdd = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Options) {
//                                Options option = (Options) setting.getValue();
//                                listToAdd.add(option);
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        y += 6 + (listToAdd.size() * 16);
//                        if (!listToAdd.isEmpty()) {
//                            for (Options option : listToAdd) {
//                                y -= 16;
//                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff, yOff + y, categoryPanel));
//                            }
//                        }
//                    }
//                    xOff += 40;
//                    if (xOff > 20 + categoryButton.panel.y + 320) {
//                        row++;
//                        xOff = 62 + categoryButton.panel.x;
//                        yOff = biggestY + 8;
//                        if (row == 2) {
//                            yOff = 20 + categoryButton.panel.y + (24) + (8 * 10);
//                        }
//                    }
//                }
//            }
//        }
//        if (categoryButton.name == "Other") {
//            float biggestY = yOff + 8;
//            for (Module module : Client.getModuleManager().getArray()) {
//                if (module.getType() == ModuleData.Type.Other) {
//                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10, module));
//                    y = 16 + 16;
//                    if (getSettings(module) != null) {
//                        y = 18;
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Boolean) {
//                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
//                                y += 8;
//                                if (yOff + y >= biggestY) {
//                                    biggestY = yOff + y;
//                                }
//                            }
//                        }
//                        List<Setting> sliders = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Number) {
//                                sliders.add(setting);
//                            }
//                        }
//                        sliders.sort(Comparator.comparing(Setting::getName));
//                        for(Setting setting : sliders) {
//                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
//                            y += 12;
//                            if (yOff + y >= biggestY) {
//                                biggestY = yOff + y;
//                            }
//                        }
//                        List<Options> listToAdd = new ArrayList<>();
//                        for (Setting setting : getSettings(module)) {
//                            if (setting.getValue() instanceof Options) {
//                                Options option = (Options) setting.getValue();
//                                listToAdd.add(option);
//                                if (yOff + y >= biggestY) {
//                                    biggestY = 20 + categoryButton.panel.y + 16;
//                                }
//                            }
//                        }
//                        y += 6 + (listToAdd.size() * 16);
//                        if (!listToAdd.isEmpty()) {
//                            for (Options option : listToAdd) {
//                                y -= 16;
//                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff, yOff + y, categoryPanel));
//                            }
//                        }
//                    }
//                    xOff += 47;
//                    if (xOff > 20 + categoryButton.panel.y + 320) {
//                        xOff = 62 + categoryButton.panel.x;
//                        yOff = biggestY + 8;
//                    }
//                }
//            }
//        }
//        if (categoryButton.name == "Colors") {
//            float biggestY = yOff + 8;
//            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getHudColor(), "Hud Color", xOff + 300, y + 5, categoryButton));
//        }
//    }
//
//    @Override
//    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
//        boolean active = false;
//        for (DropdownBox db : categoryPanel.dropdownBoxes) {
//            if (db.active) {
//                db.mouseClicked(p1, p2, p3);
//                active = true;
//                break;
//            }
//        }
//        if (!active) {
//            for (DropdownBox db : categoryPanel.dropdownBoxes) {
//                db.mouseClicked(p1, p2, p3);
//            }
//            for (Button button : categoryPanel.buttons) {
//                button.mouseClicked(p1, p2, p3);
//            }
//            for (Checkbox checkbox : categoryPanel.checkboxes) {
//                checkbox.mouseClicked(p1, p2, p3);
//            }
//            for (Slider slider : categoryPanel.sliders) {
//                slider.mouseClicked(p1, p2, p3);
//            }
//            for (ColorPreview cp : categoryPanel.colorPreviews) {
//                for (RGBSlider slider : cp.sliders) {
//                    slider.mouseClicked(p1, p2, p3);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {
//        if (categoryPanel.visible) {
//            RenderingUtil.rectangle(114 + categoryPanel.categoryButton.panel.dragX, 65 + categoryPanel.categoryButton.panel.dragY, 115 + categoryPanel.categoryButton.panel.dragX + Client.fs.getStringWidth(categoryPanel.headerString) + 2, 65 + categoryPanel.categoryButton.panel.dragY + Client.fs.getStringHeight(categoryPanel.headerString), Colors.getColor(17));
//            Client.fs.drawStringWithShadow(categoryPanel.headerString, 115 + categoryPanel.categoryButton.panel.dragX, 66 + categoryPanel.categoryButton.panel.dragY, -1);
//        }
//        for (ColorPreview cp : categoryPanel.colorPreviews) {
//            cp.draw(x, y);
//        }
//        for (Button button : categoryPanel.buttons) {
//            button.draw(x, y);
//        }
//        for (Checkbox checkbox : categoryPanel.checkboxes) {
//            checkbox.draw(x, y);
//        }
//        for (Slider slider : categoryPanel.sliders) {
//            slider.draw(x, y);
//        }
//        for (DropdownBox db : categoryPanel.dropdownBoxes) {
//            db.draw(x, y);
//        }
//        for (DropdownBox db : categoryPanel.dropdownBoxes) {
//            if (db.active) {
//                for (DropdownButton b : db.buttons) {
//                    b.draw(x, y);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
//        for (Slider slider : categoryPanel.sliders) {
//            slider.mouseReleased(x, y, button);
//        }
//        for (ColorPreview cp : categoryPanel.colorPreviews) {
//            for (RGBSlider slider : cp.sliders) {
//                slider.mouseReleased(x, y, button);
//            }
//        }
//    }
//
//    @Override
//    public void buttonContructor(Button p0, CategoryPanel panel) {
//
//    }
//
//    @Override
//    public void buttonMouseClicked(Button p0, int p2, int p3, int p4, CategoryPanel panel) {
//        if (panel.categoryButton.enabled) {
//            float xOff = panel.categoryButton.panel.dragX;
//            float yOff = panel.categoryButton.panel.dragY;
//            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35 + xOff && p3 <= p0.y + 6 + yOff;
//            if (hovering) {
//                if (p4 == 0) {
//                    p0.module.toggle();
//                    p0.enabled = p0.module.isEnabled();
//                } else if (p4 == 1) {
//                    if (p0.isBinding) {
//                        p0.module.setKeybind(new Keybind(p0.module, Keyboard.getKeyIndex("NONE")));
//                        p0.isBinding = false;
//                    } else {
//                        p0.isBinding = true;
//                    }
//                }
//            } else if (p0.isBinding) {
//                p0.isBinding = false;
//            }
//        }
//    }
//
//    @Override
//    public void buttonDraw(Button p0, float p2, float p3, CategoryPanel panel) {
//        if (panel.categoryButton.enabled) {
//            float xOff = panel.categoryButton.panel.dragX;
//            float yOff = panel.categoryButton.panel.dragY;
//            RenderingUtil.rectangle(p0.x + xOff + 0.6, p0.y + yOff + 0.6, p0.x + 6 + xOff + -0.6, p0.y + 6 + yOff + -0.6, Colors.getColor(10));
//            RenderingUtil.drawGradient(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + 6 + xOff + -1, p0.y + 6 + yOff + -1, Colors.getColor(76), Colors.getColor(51));
//            p0.enabled = p0.module.isEnabled();
//            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35 + xOff && p3 <= p0.y + 6 + yOff;
//            GlStateManager.pushMatrix();
//            Client.fs.drawStringWithShadow("", 0, 0, Colors.getColor(0, 0));
//            Client.fs.drawString(p0.module.getName(), (p0.x + xOff + 1), (p0.y + 2.5f + yOff - 7), NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//            String meme = !p0.module.getKeybind().getKeyStr().equalsIgnoreCase("None") ? "Enable [" + p0.module.getKeybind().getKeyStr() + "]" : "Enable";
//            Client.fss.drawString(meme, (p0.x + 7.6f + xOff), (p0.y + 2.7f + yOff), NharFont.FontType.OUTLINE_THIN, p0.isBinding ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : p0.enabled ? -1 : Colors.getColor(180), Colors.getColor(10));
//
//            GlStateManager.popMatrix();
//            if (p0.enabled) {
//                RenderingUtil.drawGradient(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + xOff + 5, p0.y + yOff + 5, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
//            }
//            if (hovering && !p0.enabled) {
//                RenderingUtil.rectangle(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + xOff + 5, p0.y + yOff + 5, Colors.getColor(255, 40));
//            }
//
//            if (hovering) {
//                Client.fs.drawStringWithShadow("Desc: ", (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//                float width = Client.fs.getStringWidth("Desc: ");
//                Client.fss.drawStringWithShadow(p0.module.getDescription(), (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55 + width, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//            }
//        }
//    }
//
//    @Override
//    public void buttonKeyPressed(Button button, int key) {
//        if (button.isBinding && key != 0) {
//            int keyToBind = key;
//            if (key == 1) {
//                keyToBind = Keyboard.getKeyIndex("NONE");
//            }
//            Keybind keybind = new Keybind(button.module, keyToBind);
//            button.module.setKeybind(keybind);
//            button.isBinding = false;
//        }
//    }
//
//    @Override
//    public void checkBoxMouseClicked(Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
//        if (panel.categoryButton.enabled) {
//            float xOff = panel.categoryButton.panel.dragX;
//            float yOff = panel.categoryButton.panel.dragY;
//            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35 + xOff && p3 <= p0.y + 6 + yOff;
//            if (hovering) {
//                if (p4 == 0) {
//                    boolean xd = ((Boolean) p0.setting.getValue());
//                    p0.setting.setValue(!xd);
//                    Module.saveSettings();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void checkBoxDraw(Checkbox p0, float p2, float p3, CategoryPanel panel) {
//        if (panel.categoryButton.enabled) {
//            float xOff = panel.categoryButton.panel.dragX;
//            float yOff = panel.categoryButton.panel.dragY;
//            RenderingUtil.rectangle(p0.x + xOff + 0.6, p0.y + yOff + 0.6, p0.x + 6 + xOff + -0.6, p0.y + 6 + yOff + -0.6, Colors.getColor(10));
//            RenderingUtil.drawGradient(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + 6 + xOff + -1, p0.y + 6 + yOff + -1, Colors.getColor(76), Colors.getColor(51));
//            p0.enabled = ((Boolean) p0.setting.getValue());
//            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35 + xOff && p3 <= p0.y + 6 + yOff;
//            GlStateManager.pushMatrix();
//            String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
//            Client.fs.drawStringWithShadow("", 0, 0, Colors.getColor(0, 0));
//            Client.fss.drawString(xd, (p0.x + 7.5f + xOff), (p0.y + 2.6f + yOff), NharFont.FontType.OUTLINE_THIN, (hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : p0.enabled ? -1 : Colors.getColor(180)), Colors.getColor(10));
//            GlStateManager.popMatrix();
//            if (p0.enabled) {
//                RenderingUtil.drawGradient(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + xOff + 5, p0.y + yOff + 5, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
//            }
//            if (hovering && !p0.enabled) {
//                RenderingUtil.rectangle(p0.x + xOff + 1, p0.y + yOff + 1, p0.x + xOff + 5, p0.y + yOff + 5, Colors.getColor(255, 40));
//            }
//
//            if (hovering) {
//                Client.fs.drawStringWithShadow("Desc: ", (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//                float width = Client.fs.getStringWidth("Desc: ");
//                Client.fss.drawStringWithShadow(p0.setting.getDesc(), (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55 + width, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//            }
//        }
//    }
//
//    @Override
//    public void dropDownContructor(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
//        int y = 7;
//        for (String value : p0.option.getOptions()) {
//            p0.buttons.add(new DropdownButton(value, p2, p3 + y, p0));
//            y += 6;
//        }
//    }
//
//    @Override
//    public void dropDownMouseClicked(DropdownBox dropDown, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
//        for (DropdownButton db : dropDown.buttons) {
//            if (dropDown.active && dropDown.panel.visible) {
//                db.mouseClicked(mouseX, mouseY, mouse);
//            }
//        }
//        if ((mouseX >= panel.categoryButton.panel.dragX + dropDown.x) && (mouseY >= panel.categoryButton.panel.dragY + dropDown.y) && (mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40) && (mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8) &&
//                (mouse == 0) && dropDown.panel.visible) {
//            dropDown.active = (!dropDown.active);
//        } else {
//            dropDown.active = false;
//        }
//    }
//
//    @Override
//    public void dropDownDraw(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
//        float xOff = panel.categoryButton.panel.dragX;
//        float yOff = panel.categoryButton.panel.dragY;//40
//        boolean hovering = (p2 >= panel.categoryButton.panel.dragX + p0.x) && (p3 >= panel.categoryButton.panel.dragY + p0.y) && (p2 <= panel.categoryButton.panel.dragX + p0.x + 40) && (p3 <= panel.categoryButton.panel.dragY + p0.y + 6);
//
//        RenderingUtil.rectangle(p0.x + xOff - 0.3, p0.y + yOff - 0.3, p0.x + xOff + 40 + 0.3, p0.y + yOff + 6 + 0.3, Colors.getColor(10));
//
//        RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40, p0.y + yOff + 6, Colors.getColor(31), Colors.getColor(36));
//        if (hovering) {
//            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40, p0.y + yOff + 6, 0.3, Colors.getColor(0, 0), Colors.getColor(90));
//        }
//        Client.fs.drawStringWithShadow("", 0, 0, Colors.getColor(0, 0));
//        Client.fss.drawString(p0.option.getName(), (p0.x + xOff), (p0.y - 5 + yOff), NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//        GlStateManager.pushMatrix();
//        GlStateManager.translate((p0.x + xOff + 36), (p0.y + (p0.active ? 3.5 : 2.5) + yOff), 0);
//        GlStateManager.rotate(p0.active ? 270 : 90, 0, 0, 90);
//        RenderingUtil.drawCircle(0, 0, 2.5f, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
//        RenderingUtil.drawCircle(0, 0, 1.5f, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
//        RenderingUtil.drawCircle(0, 0, 1, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
//        GlStateManager.popMatrix();
//        Client.fss.drawString(p0.option.getSelected(), (p0.x + 2 + xOff) - 1, (p0.y + 2.5f + yOff), NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//        if (p0.active) {
//            int i = p0.buttons.size();
//            RenderingUtil.rectangle(p0.x + xOff - 0.3, p0.y + 7 + yOff - 0.3, p0.x + xOff + 40 + 0.3, p0.y + yOff + 7 + (6 * i) + 0.3, Colors.getColor(10));
//            RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff + 7, p0.x + xOff + 40, p0.y + yOff + 7 + (6 * i), Colors.getColor(31), Colors.getColor(36));
//        }
//    }
//
//
//    @Override
//    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
//        if ((x >= p1.panel.categoryButton.panel.dragX + p0.x) && (y >= p1.panel.categoryButton.panel.dragY + p0.y) && (x <= p1.panel.categoryButton.panel.dragX + p0.x + 40) && (y <= p1.panel.categoryButton.panel.dragY + p0.y + 5.5) && (mouse == 0)) {
//            p1.option.setSelected(p0.name);
//            p1.active = false;
//        }
//    }
//
//    @Override
//    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
//        float xOff = p1.panel.categoryButton.panel.dragX;
//        float yOff = p1.panel.categoryButton.panel.dragY;
////        RenderingUtil.rectangle(p0.x +  xOff - 0.3, p0.y + yOff - 0.3, p0.x  + xOff + 40 + 0.3, p0.y + yOff + 6 + 0.3, Colors.getColor(010);
////        RenderingUtil.drawGradient(p0.x +  xOff, p0.y + yOff, p0.x  + xOff + 40, p0.y + yOff + 6, Colors.getColor(46), Colors.getColor(27));
//        boolean hovering = (x >= xOff + p0.x) && (y >= yOff + p0.y) && (x <= xOff + p0.x + 40) && (y <= yOff + p0.y + 5.5);
//        GlStateManager.pushMatrix();
//
//        Client.fs.drawStringWithShadow("", 0, 0, Colors.getColor(0, 0));
//        Client.fss.drawString(p0.name, (p0.x + 1 + xOff), (p0.y + 2.5f + yOff), NharFont.FontType.OUTLINE_THIN, hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : -1, Colors.getColor(10));
//        GlStateManager.scale(1, 1, 1);
//        GlStateManager.popMatrix();
//    }
//
//
//    @Override
//    public void SliderContructor(Slider p0, CategoryPanel panel) {
//        p0.dragX = ((Number) p0.setting.getValue()).doubleValue() * 40 / p0.setting.getMax();
//    }
//
//
//    @Override
//    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
//        categoryButton.categoryPanel.mouseReleased(x, y, button);
//    }
//
//    @Override
//    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {
//        float xOff = panel.dragX;
//        float yOff = panel.dragY + 75;
//        boolean hovering = (x >= 55 + slButton.x + xOff && y >= slButton.y + yOff - 2 && x <= 55 + slButton.x + xOff + 40 && y <= slButton.y + 8 + yOff + 2);
//        RenderingUtil.rectangleBordered(slButton.x + xOff + 55 - 0.3, slButton.y + yOff - 0.3 - 2, slButton.x + xOff + 40 + 55 + 0.3, slButton.y + 8 + yOff + 0.3 + 2, 0.3, Colors.getColor(10), Colors.getColor(10));
//
//        RenderingUtil.drawGradient(slButton.x + xOff + 55, slButton.y + yOff - 2, slButton.x + xOff + 40 + 55, slButton.y + 8 + yOff + 2, Colors.getColor(46), Colors.getColor(27));
//        if (hovering) {
//            RenderingUtil.rectangleBordered(slButton.x + xOff + 55, slButton.y + yOff - 2, slButton.x + xOff + 40 + 55, slButton.y + 8 + yOff + 2, 0.6, Colors.getColor(0, 0), Colors.getColor(90));
//        }
//        float xOffset = Client.fs.getStringWidth(slButton.name) / 2;
//        //(p0.x + 20 - offSet/2 + xOff)
//        Client.fs.drawStringWithShadow(slButton.name, (xOff + 25 + 55) - xOffset, slButton.y + yOff + 3.5f, -1);
//    }
//
//    @Override
//    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {
//        float xOff = panel.dragX;
//        float yOff = panel.dragY + 75;
//        if (button == 0 &&
//                x >= 55 + slButton.x + xOff &&
//                y >= slButton.y + yOff - 2 &&
//                x <= 55 + slButton.x + xOff + 40 &&
//                y <= slButton.y + 8 + yOff + 2) {
//
//            if (slButton.load) {
//                ChatUtil.printChat("Settings have been loaded.");
//                Module.loadSettings();
//                Color.loadStatus();
//                for (CategoryPanel xd : panel.typePanel) {
//                    for (Slider slider : xd.sliders) {
//                        slider.dragX = slider.lastDragX = ((Number) slider.setting.getValue()).doubleValue() * 40 / slider.setting.getMax();
//                    }
//                }
//            } else {
//                ChatUtil.printChat("Settings have been saved.");
//                Color.saveStatus();
//                Module.saveSettings();
//                for (CategoryPanel xd : panel.typePanel) {
//                    for (Slider slider : xd.sliders) {
//                        slider.dragX = slider.lastDragX = ((Number) slider.setting.getValue()).doubleValue() * 40 / slider.setting.getMax();
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void colorConstructor(ColorPreview colorPreview, float x, float y) {
//        int i = 0;
//        for (RGBSlider.Colors xd : RGBSlider.Colors.values()) {
//            colorPreview.sliders.add(new RGBSlider(x + 10, y + i, colorPreview, xd));
//            i += 12;
//        }
//    }
//
//    @Override
//    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {
//        float xOff = colorPreview.x + colorPreview.categoryPanel.panel.dragX;
//        float yOff = colorPreview.y + colorPreview.categoryPanel.panel.dragY + 75;
//        RenderingUtil.rectangleBordered(xOff - 80, yOff - 6, xOff + 1, yOff + 46, 0.3, Colors.getColor(48), Colors.getColor(10));
//        RenderingUtil.rectangle(xOff - 79, yOff - 5, xOff, yOff + 45, Colors.getColor(17));
//
//        RenderingUtil.rectangle(xOff - 74, yOff - 6, xOff - 73 + Client.fs.getStringWidth(colorPreview.colorName) + 1, yOff - 4, Colors.getColor(17));
//        Client.fs.drawStringWithShadow(colorPreview.colorName, xOff - 73, yOff - 6, -1);
//
//        for (RGBSlider slider : colorPreview.sliders) {
//            slider.draw(x, y);
//        }
//
//    }
//
//    @Override
//    public void rgbSliderDraw(RGBSlider slider, float x, float y) {
//        float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75;
//        float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74;
//        final double fraction = slider.dragX / 60;
//        final double value = MathUtils.getIncremental(fraction * 255, 1);
//        ColorObject cO = slider.colorPreview.colorObject;
//        int faggotNiggerColor = Colors.getColor(cO.red, cO.green, cO.blue, 255);
//        int faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, 120);
//        //boolean hovered = x >= xOff && y >= yOff && x <= xOff + 60.0f && y <= yOff + 6.0f;
//        RenderingUtil.rectangle(xOff, yOff, xOff + 60, yOff + 6, Colors.getColor(32));
//        switch (slider.rgba) {
//            case ALPHA:
//                faggotNiggerColor = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
//                faggotNiggerColor2 = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120);
//                break;
//        }
//
//        mc.fontRendererObj.drawString("", 100, 100, -1);
//        RenderingUtil.rectangle(xOff, yOff, xOff + (60 * fraction), yOff + 6, Colors.getColor(0));
//        RenderingUtil.drawGradient(xOff, yOff, xOff + (60 * fraction), yOff + 6, faggotNiggerColor, faggotNiggerColor2);
//        Client.fss.drawStringWithShadow("", 0, 0, Colors.getColor(0, 0));
//        String current = "R";
//        switch (slider.rgba) {
//            case BLUE:
//                current = "B";
//                break;
//            case GREEN:
//                current = "G";
//                break;
//            case ALPHA:
//                current = "A";
//                break;
//        }
//        Client.fs.drawString(current, xOff - 7, yOff + 3, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//
//        float textX = (xOff + 30 - Client.fs.getStringWidth(Integer.toString((int) value)) / 2);
//        Client.fs.drawString(Integer.toString((int) value), textX, yOff + 5, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//        double newValue = 0;
//        if (slider.dragging) {
//            slider.dragX = x - slider.lastDragX;
//            if (value <= 255 && value >= 0) {
//                newValue = value;
//            }
//            switch (slider.rgba) {
//                case RED:
//                    slider.colorPreview.colorObject.setRed((int) newValue);
//                    break;
//                case GREEN:
//                    slider.colorPreview.colorObject.setGreen((int) newValue);
//                    break;
//                case BLUE:
//                    slider.colorPreview.colorObject.setBlue((int) newValue);
//                    break;
//                case ALPHA:
//                    slider.colorPreview.colorObject.setAlpha((int) newValue);
//                    break;
//            }
//
//        }
//        if (slider.dragX <= 0.0f) {
//            slider.dragX = 0.0f;
//        }
//        if (slider.dragX >= 60) {
//            slider.dragX = 60;
//        }
//    }
//
//    @Override
//    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {
//        float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75;
//        float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74;
//        if (slider.colorPreview.categoryPanel.enabled && x >= xOff && y >= yOff && x <= xOff + 60.0f && y <= yOff + 6.0f && mouse == 0) {
//            slider.dragging = true;
//            slider.lastDragX = x - slider.dragX;
//        }
//    }
//
//    @Override
//    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {
//        if (mouse == 0) {
//            Color.saveStatus();
//            slider.dragging = false;
//        }
//    }
//
//    @Override
//    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
//        float xOff = panel.categoryButton.panel.dragX;
//        float yOff = panel.categoryButton.panel.dragY;
//        if (panel.visible && mouseX >= panel.x + xOff + slider.x && mouseY >= yOff + panel.y + slider.y - 6 && mouseX <= xOff + panel.x + slider.x + 40.0f && mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
//            slider.dragging = true;
//            slider.lastDragX = mouseX - slider.dragX;
//        }
//    }
//
//    @Override
//    public void SliderMouseMovedOrUp(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
//        if (mouse == 0) {
//            slider.dragging = false;
//        }
//    }
//
//    @Override
//    public void SliderDraw(Slider slider, float x, float y, CategoryPanel panel) {
//        if (panel.visible) {
//            float xOff = panel.categoryButton.panel.dragX;
//            float yOff = panel.categoryButton.panel.dragY;
//            final double fraction = slider.dragX / 40;
//            final double value = MathUtils.getIncremental(fraction * slider.setting.getMax(), slider.setting.getInc());
//            float sliderX = (float) (((Number) slider.setting.getValue()).doubleValue() * 38 / slider.setting.getMax());
//            RenderingUtil.rectangle(slider.x + xOff - 0.3, slider.y + yOff - 0.3, slider.x + xOff + 38 + 0.3, slider.y + yOff + 3 + 0.3, Colors.getColor(10));
//            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38, slider.y + yOff + 3, Colors.getColor(46), Colors.getColor(27));
//            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + sliderX, slider.y + yOff + 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
//            Client.fs.drawStringWithShadow("", (slider.x + xOff), (slider.y - 5 + yOff), -1);
//
//            String xd = slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
//            double setting = ((Number) slider.setting.getValue()).doubleValue();
//            GlStateManager.pushMatrix();
//            String valu2e = MathUtils.isInteger(setting) ? (int) setting + "" : setting + "";
//            float strWidth = Client.fs.getStringWidth(valu2e);
//            float textX = sliderX + strWidth > 42 ? sliderX - strWidth : sliderX - strWidth / 2;
//            Client.fs.drawString(valu2e, slider.x + xOff + textX, (slider.y + yOff + 2.6f), NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//            GlStateManager.scale(1, 1, 1);
//            GlStateManager.popMatrix();
//            Client.fss.drawString(xd, (slider.x + xOff), (slider.y - 5 + yOff), NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
//
//
//            if (slider.dragging) {
//                slider.dragX = x - slider.lastDragX;
//                Object newValue = (StringConversions.castNumber(Double.toString(value), slider.setting.getInc()));
//                slider.setting.setValue(newValue);
//            }
//            if (((Number) slider.setting.getValue()).doubleValue() <= slider.setting.getMin()) {
//                Object newValue = (StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getInc()));
//                slider.setting.setValue(newValue);
//            }
//            if (((Number) slider.setting.getValue()).doubleValue() >= slider.setting.getMax()) {
//                Object newValue = (StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getInc()));
//                slider.setting.setValue(newValue);
//            }
//            if (slider.dragX <= 0.0f) {
//                slider.dragX = 0.0f;
//            }
//            if (slider.dragX >= 40) {
//                slider.dragX = 40;
//            }
//            if ((x >= xOff + slider.x && y >= yOff + slider.y - 6 && x <= xOff + slider.x + 38 && y <= yOff + slider.y + 3) || slider.dragging) {
//                Client.fs.drawStringWithShadow("Desc: ", (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//                float width = Client.fs.getStringWidth("Desc: ");
//                Client.fss.drawStringWithShadow(slider.setting.getDesc() + " Min: " + slider.setting.getMin() + " " + "Max: " + slider.setting.getMax(), (panel.categoryButton.panel.x + 4 + panel.categoryButton.panel.dragX) + 55 + width, (panel.categoryButton.panel.y + 8 + panel.categoryButton.panel.dragY), -1);
//            }
//        }
//    }
//}
