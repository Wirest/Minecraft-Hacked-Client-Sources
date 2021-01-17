package me.rigamortis.faurax.gui.click.theme.themes;

import me.rigamortis.faurax.gui.click.theme.*;
import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.gui.font.*;
import me.rigamortis.faurax.*;
import java.util.*;
import me.rigamortis.faurax.utils.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.gui.click.components.*;
import java.text.*;
import me.rigamortis.faurax.gui.click.*;

public class Vanity extends Theme implements RenderHelper
{
    private Minecraft mc;
    public CustomFont font;
    
    public Vanity() {
        this.mc = Minecraft.getMinecraft();
        this.setName("Vanity");
        this.setVisible(false);
        this.font = new CustomFont("Verdana", 18.0f);
    }
    
    @Override
    public void panelContructor(final Panel panel, final float mouseX, final float mouseY) {
    }
    
    @Override
    public void panelMouseClicked(final Panel panel, final int mouseX, final int mouseY, final int button) {
        if (panel.isVisible()) {
            for (final Button b : panel.buttons) {
                b.mouseClicked(mouseX, mouseY, button);
            }
            for (final Slider s : panel.sliders) {
                s.mouseClicked(mouseX, mouseY, button);
            }
            for (final CheckBox cb : panel.checkBoxes) {
                cb.mouseClicked(mouseX, mouseY, button);
            }
            for (final DropDown dd : panel.dropDowns) {
                dd.mouseClicked(mouseX, mouseY, button);
            }
            for (final ScrollBar sb : panel.scrollBars) {
                sb.mouseClicked(mouseX, mouseY, button);
            }
            if (mouseX >= panel.x + panel.dragX && mouseY >= panel.dragY + panel.y - 2.0f && mouseX <= panel.dragX + panel.x + 140.0f + 11 * panel.scrollBars.size() && mouseY <= panel.dragY + panel.y + 12.0f && button == 0) {
                panel.dragging = true;
                Client.getGUI().getTopPanel(panel);
                panel.lastDragX = mouseX - panel.dragX;
                panel.lastDragY = mouseY - panel.dragY;
            }
            if (mouseX >= panel.x + panel.dragX + 128.0f + 9 * panel.scrollBars.size() && mouseY >= panel.dragY + panel.y - 1.0f && mouseX <= panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size() - 2.0f && mouseY <= panel.dragY + panel.y + 9.0f && button == 0 && !panel.name.equalsIgnoreCase("GUI")) {
                panel.setVisible(panel.dragging = false);
                Client.getGUI().getTopPanel(panel);
            }
        }
    }
    
    @Override
    public void panelMouseMovedOrUp(final Panel panel, final int mouseX, final int mouseY, final int button) {
        if (panel.isVisible()) {
            for (final Slider s : panel.sliders) {
                s.mouseReleased(mouseX, mouseY, button);
            }
            for (final ScrollBar sb : panel.scrollBars) {
                sb.mouseReleased(mouseX, mouseY, button);
            }
        }
        if (button == 0) {
            panel.dragging = false;
        }
    }
    
    @Override
    public void panelDraw(final Panel panel, final int mouseX, final int mouseY) {
        if (panel.isVisible()) {
            final GuiUtils guiUtils = Vanity.guiUtils;
            GuiUtils.drawRect(panel.x + panel.dragX, panel.y + panel.dragY - 3.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size(), panel.dragY + panel.y + 11.0f, -1155061977);
            Vanity.guiUtils.drawOutlineRect(panel.x + panel.dragX, panel.y + panel.dragY - 3.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size(), panel.dragY + panel.y + 11.0f, -1153088187);
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(panel.x + panel.dragX, panel.y + panel.dragY - 2.0f + 14.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size(), panel.dragY + panel.y + 15.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size() + 100 * panel.radar.size(), -1155061977);
            Vanity.guiUtils.drawOutlineRect(panel.x + panel.dragX, panel.y + panel.dragY - 2.0f + 14.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size(), panel.dragY + panel.y + 15.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size() + 100 * panel.radar.size(), -1153088187);
            if (!panel.name.equalsIgnoreCase("GUI")) {
                final GuiUtils guiUtils3 = Vanity.guiUtils;
                GuiUtils.drawRect(panel.x + panel.dragX + 128.0f + 9 * panel.scrollBars.size(), panel.y + panel.dragY - 1.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size() - 2.0f, panel.dragY + panel.y + 9.0f, -1153417152);
                Vanity.guiUtils.drawOutlineRect(panel.x + panel.dragX + 128.0f + 9 * panel.scrollBars.size(), panel.y + panel.dragY - 1.0f, panel.dragX + panel.x + 140.0f + 9 * panel.scrollBars.size() - 2.0f, panel.dragY + panel.y + 9.0f, -1153088187);
                this.font.drawString("-", panel.x + panel.dragX + 131.5f, panel.y + panel.dragY - 4.0f, CustomFont.FontType.NORMAL, -1);
            }
            this.font.drawString(panel.name, panel.x + panel.dragX + 4.0f, panel.y + panel.dragY - 4.0f, CustomFont.FontType.NORMAL, -1);
            GL11.glEnable(3089);
            GuiUtils.scissorBox((int)(panel.x + panel.dragX), (int)(panel.y + panel.dragY + 14.0f), (int)(panel.dragX + panel.x + 140.0f + 11 * panel.scrollBars.size()), (int)(panel.dragY + panel.y + 15.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size()) + 100 * panel.radar.size());
            for (final ScrollBar sb : panel.scrollBars) {
                sb.draw(mouseX, mouseY);
            }
            GL11.glDisable(3089);
            for (final Button b : panel.buttons) {
                b.draw(mouseX, mouseY);
            }
            for (final Slider s : panel.sliders) {
                s.draw(mouseX, mouseY);
            }
            for (final CheckBox cb : panel.checkBoxes) {
                cb.draw(mouseX, mouseY);
            }
            for (final DropDown dd : panel.dropDowns) {
                dd.draw(mouseX, mouseY);
            }
        }
        if (panel.dragging) {
            panel.dragX = mouseX - panel.lastDragX;
            panel.dragY = mouseY - panel.lastDragY;
        }
    }
    
    @Override
    public void buttonContructor(final Button button, final Panel panel) {
    }
    
    @Override
    public void buttonMouseClicked(final Button button, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + button.x + 98.0f && mouseY >= panel.dragY + panel.y + button.y && mouseX <= panel.dragX + panel.x + button.x + 140.0f && mouseY <= panel.dragY + panel.y + button.y + 12.0f && mouse == 0) {
            for (final Module mod : Client.getModules().mods) {
                if (mod.getName().equalsIgnoreCase(button.name)) {
                    mod.toggle();
                }
            }
            Client.getConfig().saveMods();
        }
    }
    
    @Override
    public void buttonDraw(final Button button, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(button.x + panel.dragX + panel.x + 98.0f, button.y + panel.dragY + panel.y + 0.5f, button.x + panel.dragX + panel.x + 138.0f, button.y + panel.dragY + panel.y + 12.0f, -1153417152);
        Vanity.guiUtils.drawOutlineRect(button.x + panel.dragX + panel.x + 98.0f, button.y + panel.dragY + panel.y + 1.0f, button.x + panel.dragX + panel.x + 138.0f, button.y + panel.dragY + panel.y + 12.0f, -1152364464);
        this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -65536);
        this.font.drawString(button.enabled ? "Disable" : "Enable", button.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(button.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -3355444);
        if (x >= panel.x + panel.dragX + button.x + 98.0f && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 140.0f && y <= panel.dragY + panel.y + button.y + 12.0f) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(button.x + panel.dragX + panel.x + 98.0f, button.y + panel.dragY + panel.y + 0.5f, button.x + panel.dragX + panel.x + 138.0f, button.y + panel.dragY + panel.y + 12.0f, -297779136);
            Vanity.guiUtils.drawOutlineRect(button.x + panel.dragX + panel.x + 98.0f, button.y + panel.dragY + panel.y + 1.0f, button.x + panel.dragX + panel.x + 138.0f, button.y + panel.dragY + panel.y + 12.0f, -297450171);
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -65536);
            this.font.drawString(button.enabled ? "Disable" : "Enable", button.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(button.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -2236963);
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -43691);
        }
        if (button.enabled) {
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -16711936);
        }
        if (x >= panel.x + panel.dragX + button.x && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 140.0f && y <= panel.dragY + panel.y + button.y + 12.0f && button.enabled) {
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -11141291);
        }
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(button.name)) {
                if (mod.isToggled()) {
                    button.enabled = true;
                }
                else {
                    button.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void checkBoxContructor(final CheckBox checkBox, final Panel panel) {
    }
    
    @Override
    public void checkBoxMouseClicked(final CheckBox checkBox, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + checkBox.x + 98.0f && mouseY >= panel.dragY + panel.y + checkBox.y && mouseX <= panel.dragX + panel.x + checkBox.x + 140.0f && mouseY <= panel.dragY + panel.y + checkBox.y + 12.0f && mouse == 0) {
            checkBox.value.setBooleanValue(!checkBox.value.getBooleanValue());
        }
    }
    
    @Override
    public void checkBoxDraw(final CheckBox checkBox, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(checkBox.x + panel.dragX + panel.x + 98.0f, checkBox.y + panel.dragY + panel.y + 0.5f, checkBox.x + panel.dragX + panel.x + 138.0f, checkBox.y + panel.dragY + panel.y + 12.0f, -1153417152);
        Vanity.guiUtils.drawOutlineRect(checkBox.x + panel.dragX + panel.x + 98.0f, checkBox.y + panel.dragY + panel.y + 1.0f, checkBox.x + panel.dragX + panel.x + 138.0f, checkBox.y + panel.dragY + panel.y + 12.0f, -1152364464);
        this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 3.0f, checkBox.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -65536);
        this.font.drawString(checkBox.enabled ? "Disable" : "Enable", checkBox.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(checkBox.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -3355444);
        if (x >= panel.x + panel.dragX + checkBox.x + 98.0f && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 140.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(checkBox.x + panel.dragX + panel.x + 98.0f, checkBox.y + panel.dragY + panel.y + 0.5f, checkBox.x + panel.dragX + panel.x + 138.0f, checkBox.y + panel.dragY + panel.y + 12.0f, -297779136);
            Vanity.guiUtils.drawOutlineRect(checkBox.x + panel.dragX + panel.x + 98.0f, checkBox.y + panel.dragY + panel.y + 1.0f, checkBox.x + panel.dragX + panel.x + 138.0f, checkBox.y + panel.dragY + panel.y + 12.0f, -297450171);
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 3.0f, checkBox.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -65536);
            this.font.drawString(checkBox.enabled ? "Disable" : "Enable", checkBox.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(checkBox.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -2236963);
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 3.0f, checkBox.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -43691);
        }
        if (checkBox.enabled) {
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 3.0f, checkBox.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -16711936);
        }
        if (x >= panel.x + panel.dragX + checkBox.x && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 140.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f && checkBox.enabled) {
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 3.0f, checkBox.y + panel.dragY + panel.y - 1.0f, CustomFont.FontType.NORMAL, -11141291);
        }
        if (checkBox.value.getBooleanValue()) {
            checkBox.enabled = true;
        }
        else {
            checkBox.enabled = false;
        }
    }
    
    @Override
    public void dropDownContructor(final DropDown dropDown, final Panel panel, final float x, final float y) {
    }
    
    @Override
    public void dropDownMouseClicked(final DropDown dropDown, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (dropDown.open) {
            for (final DropDownButton b : dropDown.buttons) {
                b.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.x + panel.dragX + dropDown.x && mouseY >= panel.dragY + panel.y + dropDown.y && mouseX <= panel.dragX + panel.x + dropDown.x + 140.0f && mouseY <= panel.dragY + panel.y + dropDown.y + 12.0f && mouse == 0) {
            dropDown.open = !dropDown.open;
            Client.getGUI().getTopPanel(panel);
        }
    }
    
    @Override
    public void dropDownDraw(final DropDown dropDown, final Panel panel, final float x, final float y) {
        final float height = dropDown.open ? (dropDown.y + panel.dragY + panel.y + 13.0f + 13 * dropDown.buttons.size()) : (dropDown.y + panel.dragY + panel.y + 12.0f);
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(dropDown.x + panel.dragX + panel.x + 2.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 138.0f, height, dropDown.open ? -1155061977 : -1153417152);
        Vanity.guiUtils.drawOutlineRect(dropDown.x + panel.dragX + panel.x + 2.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 138.0f, height, -1153088187);
        this.font.drawString(String.valueOf(dropDown.name) + " (" + dropDown.value.getSelectedOption() + ")", dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        if (x >= panel.x + panel.dragX + dropDown.x && y >= panel.dragY + panel.y + dropDown.y && x <= panel.dragX + panel.x + dropDown.x + 130.0f && y <= panel.dragY + panel.y + dropDown.y + 12.0f) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(dropDown.x + panel.dragX + panel.x + 2.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 138.0f, height, dropDown.open ? -1155061977 : -1153417152);
            Vanity.guiUtils.drawOutlineRect(dropDown.x + panel.dragX + panel.x + 2.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 138.0f, height, -1153088187);
            this.font.drawString(String.valueOf(dropDown.name) + " (" + dropDown.value.getSelectedOption() + ")", dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -1118482);
        }
        if (dropDown.open) {
            for (final DropDownButton b : dropDown.buttons) {
                b.draw(x, y);
            }
        }
    }
    
    @Override
    public void dropDownButtonContructor(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel) {
    }
    
    @Override
    public void dropDownButtonMouseClicked(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + dropDownButton.x && mouseY >= panel.dragY + panel.y + dropDownButton.y && mouseX <= panel.dragX + panel.x + dropDownButton.x + 140.0f && mouseY <= panel.dragY + panel.y + dropDownButton.y + 12.0f && mouse == 0) {
            for (final DropDownButton ddb : dropDown.buttons) {
                if (!ddb.name.equalsIgnoreCase(dropDownButton.name)) {
                    ddb.enabled = false;
                }
            }
            for (final Theme theme : Client.getThemes().themes) {
                if (dropDownButton.name.equalsIgnoreCase(theme.getName())) {
                    for (final Theme t : Client.getThemes().themes) {
                        t.setVisible(false);
                    }
                    theme.setVisible(true);
                }
            }
            dropDownButton.enabled = true;
            dropDownButton.value.setSelectedOption(dropDownButton.name);
        }
    }
    
    @Override
    public void dropDownButtonDraw(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(dropDownButton.x + panel.dragX + 98.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 137.0f, dropDownButton.y + panel.dragY + 12.0f, -1153417152);
        Vanity.guiUtils.drawOutlineRect(dropDownButton.x + panel.dragX + 98.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 137.0f, dropDownButton.y + panel.dragY + 12.0f, -1153088187);
        this.font.drawString(dropDownButton.enabled ? "Disable" : "Enable", dropDownButton.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(dropDownButton.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, dropDownButton.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -3355444);
        this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 5.0f, dropDownButton.y + panel.dragY - 2.0f, CustomFont.FontType.NORMAL, -65536);
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 140.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(dropDownButton.x + panel.dragX + 98.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 137.0f, dropDownButton.y + panel.dragY + 12.0f, -297779136);
            Vanity.guiUtils.drawOutlineRect(dropDownButton.x + panel.dragX + 98.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 137.0f, dropDownButton.y + panel.dragY + 12.0f, -297450171);
            this.font.drawString(dropDownButton.enabled ? "Disable" : "Enable", dropDownButton.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(dropDownButton.enabled ? "Disable" : "Enable") / 2.0f + 48.0f, dropDownButton.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.NORMAL, -3355444);
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 5.0f, dropDownButton.y + panel.dragY - 2.0f, CustomFont.FontType.NORMAL, -43691);
        }
        if (dropDownButton.enabled) {
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 5.0f, dropDownButton.y + panel.dragY - 2.0f, CustomFont.FontType.NORMAL, -16711936);
        }
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 140.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f && dropDownButton.enabled) {
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 5.0f, dropDownButton.y + panel.dragY - 2.0f, CustomFont.FontType.NORMAL, -11141291);
        }
        Client.getValues();
        for (final Value val : ValueManager.values) {
            if (dropDownButton.value == val) {
                if (dropDownButton.value.getSelectedOption().equalsIgnoreCase(dropDownButton.name)) {
                    dropDownButton.enabled = true;
                }
                else {
                    dropDownButton.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void ScrollBarContructor(final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
    }
    
    @Override
    public void ScrollBarMouseClicked(final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + scrollBar.x && mouseY >= panel.dragY + panel.y + scrollBar.y + 1.0f && mouseX <= panel.dragX + panel.x + scrollBar.x + 137.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f) {
            for (final ScrollBarButton sbb : scrollBar.buttons) {
                sbb.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.x + panel.dragX + scrollBar.x + 140.0f && mouseY >= panel.dragY + panel.y + 1.0f + scrollBar.y && mouseX <= panel.dragX + panel.x + scrollBar.x + 147.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f && mouse == 0) {
            scrollBar.dragging = true;
            scrollBar.lastDragY = mouseY - scrollBar.dragY;
        }
    }
    
    @Override
    public void ScrollBarMouseMovedOrUp(final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouse == 0) {
            scrollBar.dragging = false;
        }
    }
    
    @Override
    public void ScrollBarDraw(final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(scrollBar.x + panel.dragX + panel.x + 140.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 147.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, -1153417152);
        Vanity.guiUtils.drawOutlineRect(scrollBar.x + panel.dragX + panel.x + 140.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 147.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, -1153088187);
        if (x >= panel.x + panel.dragX + scrollBar.x + 140.0f && y >= panel.dragY + panel.y + scrollBar.y + scrollBar.dragY + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 147.0f && y <= panel.dragY + panel.y + scrollBar.y + 39.0f + scrollBar.dragY) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(scrollBar.x + panel.dragX + panel.x + 140.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 147.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, -297779136);
            Vanity.guiUtils.drawOutlineRect(scrollBar.x + panel.dragX + panel.x + 140.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 147.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, -297450171);
        }
        if (scrollBar.dragging) {
            scrollBar.dragY = y - scrollBar.lastDragY;
        }
        if (scrollBar.dragY <= 0.0f) {
            scrollBar.dragY = 0.0f;
        }
        if (scrollBar.dragY >= scrollBar.height) {
            scrollBar.dragY = scrollBar.height;
        }
        for (final ScrollBarButton sbb : scrollBar.buttons) {
            sbb.draw(x, y);
        }
    }
    
    @Override
    public void ScrollBarButtonContructor(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel) {
    }
    
    @Override
    public void ScrollBarButtonMouseClicked(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= scrollBar.x + panel.dragX + button.x + 98.0f && mouseY >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && mouseX <= panel.dragX + scrollBar.x + button.x + 142.0f && mouseY <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && mouse == 0) {
            for (final Panel p : Client.getGUI().panels) {
                if (p.name.equalsIgnoreCase(button.name)) {
                    p.setVisible(!p.isVisible());
                }
            }
        }
    }
    
    @Override
    public void ScrollBarButtonDraw(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(button.x + panel.dragX + 2.0f + scrollBar.x + 98.0f, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 138.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, -1153417152);
        Vanity.guiUtils.drawOutlineRect(button.x + panel.dragX + 2.0f + scrollBar.x + 98.0f, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 138.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, -1153088187);
        this.font.drawString(button.name, button.x + panel.dragX + 4.0f + scrollBar.x, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.NORMAL, -65536);
        this.font.drawString(button.enabled ? "Disable" : "Enable", scrollBar.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(button.enabled ? "Disable" : "Enable") / 2.0f + 49.0f, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY - 1.0f, CustomFont.FontType.NORMAL, -3355444);
        if (x >= panel.x + panel.dragX + scrollBar.x + 1.0f && y >= panel.dragY + panel.y + scrollBar.y + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 139.0f && y <= panel.dragY + panel.y + scrollBar.y + 170.0f && x >= scrollBar.x + panel.dragX + button.x + 98.0f && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 140.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY) {
            final GuiUtils guiUtils2 = Vanity.guiUtils;
            GuiUtils.drawRect(button.x + panel.dragX + 2.0f + scrollBar.x + 98.0f, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 138.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, -1153417152);
            Vanity.guiUtils.drawOutlineRect(button.x + panel.dragX + 2.0f + scrollBar.x + 98.0f, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 138.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, -1153088187);
            this.font.drawString(button.name, button.x + panel.dragX + 4.0f + scrollBar.x, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.NORMAL, -43691);
            this.font.drawString(button.enabled ? "Disable" : "Enable", scrollBar.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(button.enabled ? "Disable" : "Enable") / 2.0f + 49.0f, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY - 1.0f, CustomFont.FontType.NORMAL, -3355444);
        }
        if (button.enabled) {
            this.font.drawString(button.name, button.x + panel.dragX + 4.0f + scrollBar.x, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.NORMAL, -16711936);
        }
        if (x >= scrollBar.x + panel.dragX + button.x + 98.0f && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 140.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && button.enabled) {
            this.font.drawString(button.name, button.x + panel.dragX + 4.0f + scrollBar.x, button.y + panel.dragY - 1.0f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.NORMAL, -11141291);
        }
        for (final Panel p : Client.getGUI().panels) {
            if (p.name.equalsIgnoreCase(button.name)) {
                if (p.isVisible()) {
                    button.enabled = true;
                }
                else {
                    button.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void SliderContructor(final Slider slider, final Panel panel) {
    }
    
    @Override
    public void SliderMouseClicked(final Slider slider, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + slider.x + 61.0f && mouseY >= panel.dragY + panel.y + slider.y && mouseX <= panel.dragX + panel.x + slider.x + 140.0f && mouseY <= panel.dragY + panel.y + slider.y + 12.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }
    
    @Override
    public void SliderMouseMovedOrUp(final Slider slider, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }
    
    @Override
    public void SliderDraw(final Slider slider, final Panel panel, final float x, final float y) {
        final float fraction = slider.dragX / 140.0f;
        final float value = fraction * slider.value.getMaxFloat();
        final DecimalFormat decimal = new DecimalFormat("#.#");
        final float percent = Float.valueOf(decimal.format(value));
        final GuiUtils guiUtils = Vanity.guiUtils;
        GuiUtils.drawRect(slider.x + panel.dragX + panel.x + 61.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 72.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153417152);
        Vanity.guiUtils.drawOutlineRect(slider.x + panel.dragX + panel.x + 61.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 72.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153088187);
        this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 4.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        this.font.drawString("<", slider.x + panel.dragX + panel.x + 63.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        final GuiUtils guiUtils2 = Vanity.guiUtils;
        GuiUtils.drawRect(slider.x + panel.dragX + panel.x + 128.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 138.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153417152);
        Vanity.guiUtils.drawOutlineRect(slider.x + panel.dragX + panel.x + 128.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 138.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153088187);
        this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 4.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        this.font.drawString(">", slider.x + panel.dragX + panel.x + 130.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        this.font.drawString(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) / 2.0f + 32.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        if (x >= panel.x + panel.dragX + slider.x + 61.0f && y >= panel.dragY + panel.y + slider.y && x <= panel.dragX + panel.x + slider.x + 140.0f && y <= panel.dragY + panel.y + slider.y + 12.0f) {
            final GuiUtils guiUtils3 = Vanity.guiUtils;
            GuiUtils.drawRect(slider.x + panel.dragX + panel.x + 61.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 72.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153417152);
            Vanity.guiUtils.drawOutlineRect(slider.x + panel.dragX + panel.x + 61.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 72.0f, slider.y + panel.dragY + panel.y + 11.5f, -1153088187);
            this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 4.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
            this.font.drawString("<", slider.x + panel.dragX + panel.x + 63.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
            final GuiUtils guiUtils4 = Vanity.guiUtils;
            GuiUtils.drawRect(slider.x + panel.dragX + panel.x + 128.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 138.0f, slider.y + panel.dragY + panel.y + 11.5f, -297779136);
            Vanity.guiUtils.drawOutlineRect(slider.x + panel.dragX + panel.x + 128.0f, slider.y + panel.dragY + panel.y + 0.5f, slider.x + panel.dragX + panel.x + 138.0f, slider.y + panel.dragY + panel.y + 11.5f, -297450171);
            this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 4.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
            this.font.drawString(">", slider.x + panel.dragX + panel.x + 130.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
            this.font.drawString(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 70.0f - this.font.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) / 2.0f + 32.0f, slider.y + panel.dragY + panel.y - 2.0f, CustomFont.FontType.NORMAL, -3355444);
        }
        if (slider.dragging) {
            slider.dragX = x - slider.lastDragX;
            slider.value.setFloatValue(value);
            Client.getConfig().saveConfig();
        }
        if (slider.value.getFloatValue() <= slider.value.getMinFloatValue()) {
            slider.value.setFloatValue(slider.value.getMinFloatValue());
        }
        if (slider.value.getFloatValue() >= slider.value.getMaxFloat()) {
            slider.value.setFloatValue(slider.value.getMaxFloat());
        }
        if (slider.dragX <= 0.0f) {
            slider.dragX = 0.0f;
        }
        if (slider.dragX >= 140.0f) {
            slider.dragX = 140.0f;
        }
    }
    
    @Override
    public void mainConstructor(final ClickUI ui) {
    }
}
