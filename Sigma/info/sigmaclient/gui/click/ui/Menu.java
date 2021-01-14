package info.sigmaclient.gui.click.ui;

import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.gui.click.components.*;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.Client;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cool1 on 1/21/2017.
 */
public class Menu extends UI {

    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void mainConstructor(ClickGui p0, MainPanel panel) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
        RenderingUtil.rectangleBordered(panel.x + panel.dragX, panel.y + panel.dragY, panel.x + 400 + panel.dragX, panel.y + 230 + panel.dragY, 3, Colors.getColor(14, 14, 14), Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        mc.fontRendererObj.drawStringWithShadow(panel.headerString + " Hook | " + dateFormat.format(date), (panel.x + 8 + panel.dragX) / 0.5f, (panel.y + 8 + panel.dragY) / 0.5f, -1);

        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
        for (CategoryButton button : panel.typeButton) {
            button.draw(p0, p1);
        }
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 57, panel.y + panel.dragY + 16, panel.x + 390 + panel.dragX, panel.y + 220 + panel.dragY, 0.5, Colors.getColor(0, 0, 0, 0), Colors.getColor(28, 28, 28));
        if (panel.dragging) {
            panel.dragX = p0 - panel.lastDragX;
            panel.dragY = p1 - panel.lastDragY;
        }
        RenderingUtil.rectangle(p0 - 1, p1 - 1, p0 + 2, p1 - 0.5, -1);
        RenderingUtil.rectangle(p0 - 1, p1 - 1, p0 - 0.5, p1 + 2, -1);
    }

    @Override
    public void mainPanelKeyPress(MainPanel panel, int key) {

    }

    @Override
    public void panelConstructor(MainPanel mainPanel, float x, float y) {
        int y1 = 16;
        for (ModuleData.Type types : ModuleData.Type.values()) {
            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x, y + y1));
            y += 14;
        }
    }

    @Override
    public void panelMouseClicked(MainPanel mainPanel, int x, int y, int z) {
        if (x >= mainPanel.x + mainPanel.dragX && y >= mainPanel.dragY + mainPanel.y && x <= mainPanel.dragX + mainPanel.x + 400 && y <= mainPanel.dragY + mainPanel.y + 12.0f && z == 0) {
            mainPanel.dragging = true;
            mainPanel.lastDragX = x - mainPanel.dragX;
            mainPanel.lastDragY = y - mainPanel.dragY;
        }
        for (CategoryButton c : mainPanel.typeButton) {
            c.mouseClicked(x, y, z);
            c.categoryPanel.mouseClicked(x, y, z);
        }
    }

    @Override
    public void panelMouseMovedOrUp(MainPanel mainPanel, int x, int y, int z) {
        if (z == 0) {
            mainPanel.dragging = false;
        }
        for (CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(x, y, z);
        }
    }

    @Override
    public void categoryButtonConstructor(CategoryButton p0, MainPanel p1) {
        p0.categoryPanel = new CategoryPanel(p0.name, p0, 0, 0);
    }

    @Override
    public void categoryButtonMouseClicked(CategoryButton p0, MainPanel p1, int p2, int p3, int p4) {
        if (p2 >= p0.x + p1.dragX && p3 >= p1.dragY + p0.y && p2 <= p1.dragX + p0.x + 50 && p3 <= p1.dragY + p0.y + 12 && p4 == 0) {
            int i = 0;
            for (CategoryButton button : p1.typeButton) {
                if (button == p0) {
                    p0.enabled = true;
                    p0.categoryPanel.visible = true;
                } else {
                    button.enabled = false;
                    button.categoryPanel.visible = false;
                }
                i++;
            }
        }
    }

    @Override
    public void categoryButtonDraw(CategoryButton p0, float p2, float p3) {
        RenderingUtil.rectangle(p0.x + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 50 + p0.panel.dragX, p0.y + 12 + p0.panel.dragY, Colors.getColor(28, 28, 28));
        if (p2 >= p0.x + p0.panel.dragX && p3 >= p0.y + p0.panel.dragY && p2 <= p0.x + p0.panel.dragX + 50 && p3 <= p0.y + p0.panel.dragY + 12) {
            RenderingUtil.rectangle(p0.x + 3 + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 50 + p0.panel.dragX, p0.y + 12 + p0.panel.dragY, Colors.getColor(255, 255, 255, 50));
        }
        if (p0.enabled) {
            RenderingUtil.rectangle(p0.x + 3 + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 6 + p0.panel.dragX, p0.y + 12 + p0.panel.dragY, Colors.getColor(165, 241, 165));
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        mc.fontRendererObj.drawStringWithShadow(p0.name, (p0.x + 10 + p0.panel.dragX) * 2, (p0.y + 4 + p0.panel.dragY) * 2, -1);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
        p0.categoryPanel.draw(p2, p3);
    }

    private List<Setting> getSettings(Module mod) {
        List<Setting> settings = new ArrayList();
        for (Setting set : mod.getSettings().values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }

    @Override
    public void categoryPanelConstructor(CategoryPanel categoryPanel, CategoryButton categoryButton, float x, float y) {
        float xOff = 62 + categoryButton.panel.x;
        float yOff = 20 + categoryButton.panel.y;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            float biggestY = yOff + 8;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Combat) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8;
                    if (getSettings(module) != null) {
                        for (Setting setting : getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
                                y += 12;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                        }
                    }
                    xOff += 40;
                    if (xOff > 20 + categoryButton.panel.y + 360) {
                        xOff = 62 + categoryButton.panel.x;
                        yOff = biggestY + 8;
                    }
                }
            }
            //categoryPanel.dropdownBoxes.add(new DropdownBox(Targeting.mode, 62 + categoryButton.panel.x + (40 * 6), 20 + categoryButton.panel.y + (8 * 17), categoryPanel));
        }
        if (categoryButton.name == "Player") {
            float biggestY = yOff + 8;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Player) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8;
                    if (getSettings(module) != null) {
                        for (Setting setting : getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
                                y += 12;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                        }
                    }
                    xOff += 45;
                    if (xOff > 20 + categoryButton.panel.y + 315) {
                        xOff = 62 + categoryButton.panel.x;
                        yOff = biggestY + 8;
                    }
                }
            }
        }
        if (categoryButton.name == "Movement") {
            float biggestY = yOff + 8;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Movement) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8;
                    if (getSettings(module) != null) {
                        for (Setting setting : getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
                                y += 12;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                        }
                    }
                    xOff += 45;
                    if (xOff > 20 + categoryButton.panel.y + 315) {
                        xOff = 62 + categoryButton.panel.x;
                        yOff = biggestY + 8;
                    }
                }
            }
        }
        if (categoryButton.name == "Visuals") {
            float biggestY = yOff + 8;
            int row = 0;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Visuals) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8;
                    if (getSettings(module) != null) {
                        for (Setting setting : getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
                                y += 12;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                        }
                    }
                    xOff += 40;
                    if (xOff > 20 + categoryButton.panel.y + 360) {
                        row++;
                        xOff = 62 + categoryButton.panel.x;
                        yOff = biggestY + 8;
                        if (row == 2) {
                            yOff = 20 + categoryButton.panel.y + (24) + (8 * 10);
                        }
                    }
                }
            }
        }
        if (categoryButton.name == "Other") {
            float biggestY = yOff + 8;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Other) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8;
                    if (getSettings(module) != null) {
                        for (Setting setting : getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6, setting));
                                y += 12;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                        }
                    }
                    xOff += 40;
                    if (xOff > 20 + categoryButton.panel.y + 360) {
                        xOff = 62 + categoryButton.panel.x;
                        yOff = biggestY + 8;
                    }
                }
            }
        }
    }

    @Override
    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
        for (Button button : categoryPanel.buttons) {
            button.mouseClicked(p1, p2, p3);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.mouseClicked(p1, p2, p3);
        }
        for (Slider slider : categoryPanel.sliders) {
            slider.mouseClicked(p1, p2, p3);
        }
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            db.mouseClicked(p1, p2, p3);
        }
    }

    @Override
    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {
        for (Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.draw(x, y);
        }
        for (Slider slider : categoryPanel.sliders) {
            slider.draw(x, y);
        }
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            db.draw(x, y);
        }
    }

    @Override
    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
        for (Slider slider : categoryPanel.sliders) {
            slider.mouseReleased(x, y, button);
        }
    }

    @Override
    public void buttonContructor(Button p0, CategoryPanel panel) {

    }

    @Override
    public void buttonMouseClicked(Button p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + xOff + 6 && p3 <= p0.y + yOff + 6) {
                if (p4 == 0) {
                    p0.module.toggle();
                    p0.enabled = p0.module.isEnabled();
                }
            }
        }
    }

    @Override
    public void buttonDraw(Button p0, float p2, float p3, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.drawRect(p0.x + xOff, p0.y + yOff, p0.x + 6 + xOff, p0.y + 6 + yOff, Colors.getColor(28, 28, 28));
            p0.enabled = p0.module.isEnabled();
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 6 + xOff && p3 <= p0.y + 6 + yOff) {
                RenderingUtil.rectangle(p0.x + +xOff, p0.y + yOff, p0.x + xOff + 6, p0.y + yOff + 6, Colors.getColor(255, 255, 255, 50));
            }
            if (p0.enabled) {
                RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + 6 + xOff, p0.y + 6 + yOff, 0.3, Colors.getColor(0, 0, 0, 0), Colors.getColor(165, 241, 165));
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            mc.fontRendererObj.drawStringWithShadow(p0.module.getName(), (p0.x + 8 + xOff) * 2, (p0.y + 1 + yOff) * 2, -1);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void buttonKeyPressed(Button button, int key) {

    }

    @Override
    public void checkBoxMouseClicked(Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + xOff + 6 && p3 <= p0.y + yOff + 6) {
                if (p4 == 0) {
                    boolean xd = ((Boolean) p0.setting.getValue()).booleanValue();
                    p0.setting.setValue(!xd);
                    Module.saveSettings();
                }
            }
        }
    }

    @Override
    public void checkBoxDraw(Checkbox p0, float p2, float p3, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.drawRect(p0.x + xOff, p0.y + yOff, p0.x + 6 + xOff, p0.y + 6 + yOff, Colors.getColor(28, 28, 28));
            p0.enabled = ((Boolean) p0.setting.getValue()).booleanValue();
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 6 + xOff && p3 <= p0.y + 6 + yOff) {
                RenderingUtil.rectangle(p0.x + +xOff, p0.y + yOff, p0.x + xOff + 6, p0.y + yOff + 6, Colors.getColor(255, 255, 255, 50));
            }
            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + 6 + xOff, p0.y + 6 + yOff, 0.5, Colors.getColor(0, 0, 0, 0), p0.enabled ? Colors.getColor(165, 241, 165) : Colors.getColor(55, 55, 55));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
            mc.fontRendererObj.drawStringWithShadow(xd, (p0.x + 8 + xOff) * 2, (p0.y + 1 + yOff) * 2, -1);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void dropDownContructor(DropdownBox p0, float p2, float p3, CategoryPanel panel) {

    }

    @Override
    public void dropDownMouseClicked(DropdownBox dropDown, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (dropDown.active) {
            for (DropdownButton b : dropDown.buttons) {
                b.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if ((mouseX >= panel.categoryButton.panel.dragX + dropDown.x) && (mouseY >= panel.categoryButton.panel.dragY + dropDown.y) && (mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40) && (mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8) &&
                (mouse == 0)) {
            dropDown.active = (!dropDown.active);
        }
    }

    @Override
    public void dropDownDraw(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40 + xOff, p0.y + 6 + yOff, Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        mc.fontRendererObj.drawStringWithShadow("Target Mode", (p0.x + xOff) * 2, (p0.y - 7 + yOff) * 2, -1);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
        boolean hovering = (p2 >= panel.categoryButton.panel.dragX + p0.x) && (p3 >= panel.categoryButton.panel.dragY + p0.y) && (p2 <= panel.categoryButton.panel.dragX + p0.x + 40) && (p3 <= panel.categoryButton.panel.dragY + p0.y + 6);
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40 + xOff, p0.y + 6 + yOff, Colors.getColor(255, 255, 255, 50));
        }
        if (p0.active) {
            for (DropdownButton buttons : p0.buttons) {
                buttons.draw(p2, p3);
            }
        }
    }


    @Override
    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
        if ((x >= p1.panel.categoryButton.panel.dragX + p0.x) && (y >= p1.panel.categoryButton.panel.dragY + p0.y) && (x <= p1.panel.categoryButton.panel.dragX + p0.x + 40) && (y <= p1.panel.categoryButton.panel.dragY + p0.y + 8) && (mouse == 0)) {


        }
    }

    @Override
    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
        float xOff = p1.panel.categoryButton.panel.dragX;
        float yOff = p1.panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40 + xOff, p0.y + 6 + yOff, Colors.getColor(28, 28, 28));
        boolean hovering = (x >= xOff + p0.x) && (y >= yOff + p0.y) && (x <= xOff + p0.x + 40) && (y <= yOff + p0.y + 6);
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40 + xOff, p0.y + 6 + yOff, Colors.getColor(255, 255, 255, 50));
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        int offSet = mc.fontRendererObj.getStringWidth(p0.name) / 2;
        mc.fontRendererObj.drawStringWithShadow(p0.name, (p0.x + 20 - offSet / 2 + xOff) * 2, (p0.y + 1 + yOff) * 2, -1);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
    }


    @Override
    public void SliderContructor(Slider p0, CategoryPanel panel) {
        p0.dragX = ((Number) p0.setting.getValue()).doubleValue() * 40 / p0.setting.getMax();
    }

    @Override
    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        if (panel.visible && mouseX >= panel.x + xOff + slider.x && mouseY >= yOff + panel.y + slider.y && mouseX <= xOff + panel.x + slider.x + 40.0f && mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }

    @Override
    public void SliderMouseMovedOrUp(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }

    @Override
    public void SliderDraw(Slider slider, float x, float y, CategoryPanel panel) {
        if (panel.visible) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            final double fraction = slider.dragX / 40;
            final double value = MathUtils.roundToPlace(fraction * slider.setting.getMax(), 2);
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38, slider.y + yOff + 3, Colors.getColor(28, 28, 28));
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, slider.x + xOff + (38 * fraction), slider.y + yOff + 3, Colors.getColor(165, 241, 165));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            String xd = slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
            mc.fontRendererObj.drawStringWithShadow(xd, (slider.x + xOff) * 2, (slider.y - 6 + yOff) * 2, -1);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
            if ((x >= xOff + slider.x && y >= yOff + slider.y && x <= xOff + slider.x + 38 && y <= yOff + slider.y + 3) || slider.dragging) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5, 0.5, 0.5);
                int strWidth = mc.fontRendererObj.getStringWidth(xd + " ");
                mc.fontRendererObj.drawStringWithShadow(Double.toString(((Number) slider.setting.getValue()).doubleValue()), (slider.x + strWidth / 2 + xOff) * 2, (slider.y - 6 + yOff) * 2, -1);
                GlStateManager.scale(1, 1, 1);
                GlStateManager.popMatrix();
            }
            if (slider.dragging) {
                slider.dragX = x - slider.lastDragX;
                Object newValue = (StringConversions.castNumber(Double.toString(value), slider.setting.getValue()));
                slider.setting.setValue(newValue);
            }
            if (((Number) slider.setting.getValue()).doubleValue() <= slider.setting.getMin()) {
                Object newValue = (StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getValue()));
                slider.setting.setValue(newValue);
            }
            if (((Number) slider.setting.getValue()).doubleValue() >= slider.setting.getMax()) {
                Object newValue = (StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getValue()));
                slider.setting.setValue(newValue);
            }
            if (slider.dragX <= 0.0f) {
                slider.dragX = 0.0f;
            }
            if (slider.dragX >= 40) {
                slider.dragX = 40;
            }
        }
    }

    @Override
    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }

    @Override
    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {

    }

    @Override
    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {

    }

    @Override
    public void colorConstructor(ColorPreview colorPreview, float x, float y) {

    }

    @Override
    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {

    }

    @Override
    public void rgbSliderDraw(RGBSlider slider, float x, float y) {

    }

    @Override
    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {

    }

    @Override
    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {

    }

}
