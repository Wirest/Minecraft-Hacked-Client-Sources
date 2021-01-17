package me.rigamortis.faurax.gui.click.theme.themes;

import me.rigamortis.faurax.gui.click.theme.*;
import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.gui.font.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.*;
import java.util.*;
import me.rigamortis.faurax.utils.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.gui.click.components.*;
import java.text.*;
import me.rigamortis.faurax.gui.click.*;

public class Reliant extends Theme implements RenderHelper
{
    private Minecraft mc;
    private CustomFont font;
    private ResourceLocation texture;
    
    public Reliant() {
        this.mc = Minecraft.getMinecraft();
        this.setName("Reliant");
        this.setVisible(false);
        this.font = new CustomFont("Verdana", 18.0f);
        this.texture = new ResourceLocation("GuiRedux.png");
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
            if (mouseX >= panel.x + panel.dragX && mouseY >= panel.dragY + panel.y && mouseX <= panel.dragX + panel.x + 110.0f + 11 * panel.scrollBars.size() && mouseY <= panel.dragY + panel.y + 12.0f && button == 0) {
                panel.dragging = true;
                Client.getGUI().getTopPanel(panel);
                panel.lastDragX = mouseX - panel.dragX;
                panel.lastDragY = mouseY - panel.dragY;
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
            final GuiUtils guiUtils = Reliant.guiUtils;
            GuiUtils.drawRect(panel.x + panel.dragX, panel.y + panel.dragY, panel.dragX + panel.x + 129.0f + 11 * panel.scrollBars.size(), panel.dragY + panel.y + 14.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size() + 100 * panel.radar.size(), 1713907752);
            final GuiUtils guiUtils2 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(panel.x + panel.dragX, panel.y + panel.dragY, panel.dragX + panel.x + 129.0f + 11 * panel.scrollBars.size(), panel.dragY + panel.y + 12.0f, -8619663, -10658725);
            Reliant.guiUtils.drawOutlineRect(panel.x + panel.dragX, panel.y + panel.dragY, panel.dragX + panel.x + 129.0f + 11 * panel.scrollBars.size(), panel.dragY + panel.y + 14.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size() + 100 * panel.radar.size(), -16777216);
            this.font.drawString(panel.name, panel.x + panel.dragX + 2.0f, panel.y + panel.dragY - 1.7f, CustomFont.FontType.NORMAL, -4473925);
            this.font.drawString(panel.name, panel.x + panel.dragX + 2.0f, panel.y + panel.dragY - 2.0f, CustomFont.FontType.NORMAL, -16777216);
            GL11.glEnable(3089);
            GuiUtils.scissorBox((int)(panel.x + panel.dragX), (int)(panel.y + panel.dragY + 14.0f), (int)(panel.dragX + panel.x + 110.0f + 11 * panel.scrollBars.size()), (int)(panel.dragY + panel.y + 14.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size()) + 100 * panel.radar.size());
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
        if (mouseX >= panel.x + panel.dragX + button.x && mouseY >= panel.dragY + panel.y + button.y && mouseX <= panel.dragX + panel.x + button.x + 110.0f && mouseY <= panel.dragY + panel.y + button.y + 12.0f && mouse == 0) {
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
        Reliant.guiUtils.drawTexturedRectangle(this.texture, button.x + panel.dragX + panel.x + 1.0f, button.y + panel.dragY + panel.y, 25.0f, 12.0f, 1.0f, 1.0f, 50.0f, 25.0f);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + panel.x + 40.0f, button.y + panel.dragY + panel.y + 2.5f, -6710887);
        if (x >= panel.x + panel.dragX + button.x && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 110.0f && y <= panel.dragY + panel.y + button.y + 12.0f) {
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + panel.x + 40.0f, button.y + panel.dragY + panel.y + 2.5f, -1);
        }
        if (button.enabled) {
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + panel.x + 40.0f, button.y + panel.dragY + panel.y + 2.5f, -1);
        }
        if (x >= panel.x + panel.dragX + button.x && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 110.0f && y <= panel.dragY + panel.y + button.y + 12.0f && button.enabled) {
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + panel.x + 40.0f, button.y + panel.dragY + panel.y + 2.5f, -1);
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
        if (mouseX >= panel.x + panel.dragX + checkBox.x + 95.0f && mouseY >= panel.dragY + panel.y + checkBox.y && mouseX <= panel.dragX + panel.x + checkBox.x + 110.0f && mouseY <= panel.dragY + panel.y + checkBox.y + 12.0f && mouse == 0) {
            checkBox.value.setBooleanValue(!checkBox.value.getBooleanValue());
        }
    }
    
    @Override
    public void checkBoxDraw(final CheckBox checkBox, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1999975733, 1999646768);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y + 2.5f, -6710887);
        if (x >= panel.x + panel.dragX + checkBox.x && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 110.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f) {
            final GuiUtils guiUtils2 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1715486784, 1715618370);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y + 2.5f, -1);
        }
        if (checkBox.enabled) {
            final GuiUtils guiUtils3 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1712403776, 1712403776);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y + 2.5f, -1);
            final GuiUtils guiUtils4 = Reliant.guiUtils;
            GuiUtils.drawCheck((int)checkBox.x + (int)panel.dragX + (int)panel.x + (int)99.0f, (int)checkBox.y + (int)panel.dragY + (int)panel.y + (int)1.5f, -1);
        }
        if (x >= panel.x + panel.dragX + checkBox.x && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 110.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f && checkBox.enabled) {
            final GuiUtils guiUtils5 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1712406597, 1712406597);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y + 2.5f, -1);
            final GuiUtils guiUtils6 = Reliant.guiUtils;
            GuiUtils.drawCheck((int)checkBox.x + (int)panel.dragX + (int)panel.x + (int)99.0f, (int)checkBox.y + (int)panel.dragY + (int)panel.y + (int) 1.5f, -1);
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
        if (mouseX >= panel.x + panel.dragX + dropDown.x && mouseY >= panel.dragY + panel.y + dropDown.y && mouseX <= panel.dragX + panel.x + dropDown.x + 110.0f && mouseY <= panel.dragY + panel.y + dropDown.y + 12.0f && mouse == 0) {
            dropDown.open = !dropDown.open;
            Client.getGUI().getTopPanel(panel);
        }
    }
    
    @Override
    public void dropDownDraw(final DropDown dropDown, final Panel panel, final float x, final float y) {
        final int state = dropDown.open ? 0 : 180;
        final float height = dropDown.open ? (dropDown.y + panel.dragY + panel.y + 13.0f + 13 * dropDown.buttons.size()) : (dropDown.y + panel.dragY + panel.y);
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, height, -14211289, -14145496);
        final GuiUtils guiUtils2 = Reliant.guiUtils;
        GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, dropDown.y + panel.dragY + panel.y + 12.0f, 1999975733, 1999646768);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDown.name, dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y + 2.5f, -6710887);
        final GuiUtils guiUtils3 = Reliant.guiUtils;
        GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.5f, dropDown.y + panel.dragY + panel.y + 7.0f, 3.5f, state, 1.0f, -871296751);
        final GuiUtils guiUtils4 = Reliant.guiUtils;
        GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.0f, dropDown.y + panel.dragY + panel.y + 6.5f, 3.5f, state, 1.0f, -6710887);
        if (x >= panel.x + panel.dragX + dropDown.x && y >= panel.dragY + panel.y + dropDown.y && x <= panel.dragX + panel.x + dropDown.x + 110.0f && y <= panel.dragY + panel.y + dropDown.y + 12.0f) {
            final GuiUtils guiUtils5 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, dropDown.y + panel.dragY + panel.y + 12.0f, 2000699456, 2000831042);
            final GuiUtils guiUtils6 = Reliant.guiUtils;
            GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.5f, dropDown.y + panel.dragY + panel.y + 7.0f, 3.5f, state, 1.0f, -871296751);
            final GuiUtils guiUtils7 = Reliant.guiUtils;
            GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.0f, dropDown.y + panel.dragY + panel.y + 6.5f, 3.5f, state, 1.0f, -1);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDown.name, dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y + 2.5f, -1);
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
        if (mouseX >= panel.x + panel.dragX + dropDownButton.x && mouseY >= panel.dragY + panel.y + dropDownButton.y && mouseX <= panel.dragX + panel.x + dropDownButton.x + 110.0f && mouseY <= panel.dragY + panel.y + dropDownButton.y + 12.0f && mouse == 0) {
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
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1999975733, 1999646768);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY + 2.5f, -6710887);
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 110.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f) {
            final GuiUtils guiUtils2 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 2000699456, 2000831042);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY + 2.5f, -1);
        }
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 110.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f && dropDownButton.enabled) {
            final GuiUtils guiUtils3 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1997619269, 1997619269);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY + 2.5f, -1);
        }
        if (dropDownButton.enabled) {
            final GuiUtils guiUtils4 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1997616448, 1997616448);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY + 2.5f, -1);
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
        if (mouseX >= panel.x + panel.dragX + scrollBar.x + 1.0f && mouseY >= panel.dragY + panel.y + scrollBar.y + 1.0f && mouseX <= panel.dragX + panel.x + scrollBar.x + 109.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f) {
            for (final ScrollBarButton sbb : scrollBar.buttons) {
                sbb.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.x + panel.dragX + scrollBar.x + 110.0f && mouseY >= panel.dragY + panel.y + 1.0f + scrollBar.y && mouseX <= panel.dragX + panel.x + scrollBar.x + 120.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f && mouse == 0) {
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
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(scrollBar.x + panel.dragX + panel.x + 110.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 119.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, 1999975733, 1999646768);
        if (x >= panel.x + panel.dragX + scrollBar.x && y >= panel.dragY + panel.y + scrollBar.y + scrollBar.dragY + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 120.0f && y <= panel.dragY + panel.y + scrollBar.y + 39.0f + scrollBar.dragY) {
            final GuiUtils guiUtils2 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(scrollBar.x + panel.dragX + panel.x + 110.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 119.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, 1999975733, 1999646768);
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
        if (mouseX >= scrollBar.x + panel.dragX + button.x && mouseY >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && mouseX <= panel.dragX + scrollBar.x + button.x + 110.0f && mouseY <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && mouse == 0) {
            for (final Panel p : Client.getGUI().panels) {
                if (p.name.equalsIgnoreCase(button.name)) {
                    p.setVisible(!p.isVisible());
                }
            }
        }
    }
    
    @Override
    public void ScrollBarButtonDraw(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1999975733, 1999646768);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY + 2.5f + scrollBar.y - scrollBar.dragY, -6710887);
        if (x >= panel.x + panel.dragX + scrollBar.x + 1.0f && y >= panel.dragY + panel.y + scrollBar.y + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 109.0f && y <= panel.dragY + panel.y + scrollBar.y + 156.0f) {
            if (x >= scrollBar.x + panel.dragX + button.x && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 110.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY) {
                final GuiUtils guiUtils2 = Reliant.guiUtils;
                GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 2000699456, 2000831042);
                Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY + 2.5f + scrollBar.y - scrollBar.dragY, -6710887);
            }
            if (x >= scrollBar.x + panel.dragX + button.x && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 110.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && button.enabled) {
                final GuiUtils guiUtils3 = Reliant.guiUtils;
                GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1997616448, 1997616448);
                Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY + 2.5f + scrollBar.y - scrollBar.dragY, -1);
            }
        }
        if (button.enabled) {
            final GuiUtils guiUtils4 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1997616448, 1997616448);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY + 2.5f + scrollBar.y - scrollBar.dragY, -1);
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
        if (mouseX >= panel.x + panel.dragX + slider.x && mouseY >= panel.dragY + panel.y + slider.y && mouseX <= panel.dragX + panel.x + slider.x + 110.0f && mouseY <= panel.dragY + panel.y + slider.y + 12.0f && mouse == 0) {
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
        final float fraction = slider.dragX / 99.0f;
        final float value = fraction * slider.value.getMaxFloat();
        final DecimalFormat decimal = new DecimalFormat("#.#");
        final float percent = Float.valueOf(decimal.format(value));
        final GuiUtils guiUtils = Reliant.guiUtils;
        GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + 109.0f, slider.y + panel.dragY + panel.y + 12.0f, 1714763061, 1714434096);
        final GuiUtils guiUtils2 = Reliant.guiUtils;
        GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + slider.dragX + 10.0f, slider.y + panel.dragY + panel.y + 12.0f, 1712403776, 1712403776);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(slider.name, slider.x + panel.dragX + panel.x + 2.0f, slider.y + panel.dragY + panel.y + 2.5f, -1);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 110.0f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) - 2.0f, slider.y + panel.dragY + panel.y + 2.5f, -1);
        if (x >= panel.x + panel.dragX + slider.x && y >= panel.dragY + panel.y + slider.y && x <= panel.dragX + panel.x + slider.x + 110.0f && y <= panel.dragY + panel.y + slider.y + 12.0f) {
            final GuiUtils guiUtils3 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + 109.0f, slider.y + panel.dragY + panel.y + 12.0f, 1715486784, 1715618370);
            final GuiUtils guiUtils4 = Reliant.guiUtils;
            GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + slider.dragX + 10.0f, slider.y + panel.dragY + panel.y + 12.0f, 1712406597, 1712406597);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(slider.name, slider.x + panel.dragX + panel.x + 2.0f, slider.y + panel.dragY + panel.y + 2.5f, -1);
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 110.0f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) - 2.0f, slider.y + panel.dragY + panel.y + 2.5f, -1);
        }
        if (slider.dragging) {
            slider.dragX = x - slider.lastDragX;
            slider.value.setFloatValue(value);
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
        if (slider.dragX >= 99.0f) {
            slider.dragX = 99.0f;
        }
    }
    
    @Override
    public void mainConstructor(final ClickUI ui) {
    }
}
