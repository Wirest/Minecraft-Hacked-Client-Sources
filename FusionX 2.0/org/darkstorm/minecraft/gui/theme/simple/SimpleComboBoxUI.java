// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme.simple;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import org.lwjgl.input.Mouse;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.darkstorm.minecraft.gui.component.Component;
import java.awt.Color;
import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;

public class SimpleComboBoxUI extends AbstractComponentUI<ComboBox>
{
    private final SimpleTheme theme;
    
    SimpleComboBoxUI(final SimpleTheme theme) {
        super(ComboBox.class);
        this.theme = theme;
        this.foreground = Color.WHITE;
        this.background = new Color(128, 128, 128, 192);
    }
    
    @Override
    protected void renderComponent(final ComboBox component) {
        this.translateComponent(component, false);
        final Rectangle area = component.getArea();
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        int maxWidth = 0;
        String[] elements4;
        for (int length = (elements4 = component.getElements()).length, l = 0; l < length; ++l) {
            final String element = elements4[l];
            maxWidth = Math.max(maxWidth, this.theme.getFontRenderer().getStringWidth(element));
        }
        int extendedHeight = 0;
        if (component.isSelected()) {
            final String[] elements = component.getElements();
            for (int i = 0; i < elements.length - 1; ++i) {
                extendedHeight += this.theme.getFontRenderer().FONT_HEIGHT + 2;
            }
            extendedHeight += 2;
        }
        RenderUtil.setColor(component.getBackgroundColor());
        GL11.glBegin(7);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d((double)area.width, 0.0);
        GL11.glVertex2d((double)area.width, (double)(area.height + extendedHeight));
        GL11.glVertex2d(0.0, (double)(area.height + extendedHeight));
        GL11.glEnd();
        final Point mouse = RenderUtil.calculateMouseLocation();
        for (Component parent = component.getParent(); parent != null; parent = parent.getParent()) {
            final Point point = mouse;
            point.x -= parent.getX();
            final Point point2 = mouse;
            point2.y -= parent.getY();
        }
        GL11.glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.5f : 0.3f);
        if (area.contains(mouse)) {
            GL11.glBegin(7);
            GL11.glVertex2d(0.0, 0.0);
            GL11.glVertex2d((double)area.width, 0.0);
            GL11.glVertex2d((double)area.width, (double)area.height);
            GL11.glVertex2d(0.0, (double)area.height);
            GL11.glEnd();
        }
        else if (component.isSelected() && mouse.x >= area.x && mouse.x <= area.x + area.width) {
            int offset = component.getHeight();
            final String[] elements2 = component.getElements();
            for (int j = 0; j < elements2.length; ++j) {
                if (j != component.getSelectedIndex()) {
                    int height = this.theme.getFontRenderer().FONT_HEIGHT + 2;
                    Label_0494: {
                        Label_0491: {
                            if (component.getSelectedIndex() == 0) {
                                if (j == 1) {
                                    break Label_0491;
                                }
                            }
                            else if (j == 0) {
                                break Label_0491;
                            }
                            if (component.getSelectedIndex() == elements2.length - 1) {
                                if (j != elements2.length - 2) {
                                    break Label_0494;
                                }
                            }
                            else if (j != elements2.length - 1) {
                                break Label_0494;
                            }
                        }
                        ++height;
                    }
                    if (mouse.y >= area.y + offset && mouse.y <= area.y + offset + height) {
                        GL11.glBegin(7);
                        GL11.glVertex2d(0.0, (double)offset);
                        GL11.glVertex2d(0.0, (double)(offset + height));
                        GL11.glVertex2d((double)area.width, (double)(offset + height));
                        GL11.glVertex2d((double)area.width, (double)offset);
                        GL11.glEnd();
                        break;
                    }
                    offset += height;
                }
            }
        }
        final int height2 = this.theme.getFontRenderer().FONT_HEIGHT + 4;
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.3f);
        GL11.glBegin(4);
        if (component.isSelected()) {
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, 2.0 * height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, 2.0 * height2 / 3.0);
        }
        else {
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, 2.0 * height2 / 3.0);
        }
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        if (component.isSelected()) {
            GL11.glBegin(1);
            GL11.glVertex2d(2.0, (double)area.height);
            GL11.glVertex2d((double)(area.width - 2), (double)area.height);
            GL11.glEnd();
        }
        GL11.glBegin(1);
        GL11.glVertex2d((double)(maxWidth + 4), 2.0);
        GL11.glVertex2d((double)(maxWidth + 4), (double)(area.height - 2));
        GL11.glEnd();
        GL11.glBegin(2);
        if (component.isSelected()) {
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, 2.0 * height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, 2.0 * height2 / 3.0);
        }
        else {
            GL11.glVertex2d(maxWidth + 4 + height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + 2.0 * height2 / 3.0, height2 / 3.0);
            GL11.glVertex2d(maxWidth + 4 + height2 / 2.0, 2.0 * height2 / 3.0);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        final String text = component.getSelectedElement();
        this.theme.getFontRenderer().drawString(text, 2, area.height / 2 - this.theme.getFontRenderer().FONT_HEIGHT / 2, RenderUtil.toRGBA(component.getForegroundColor()));
        if (component.isSelected()) {
            int offset2 = area.height + 2;
            final String[] elements3 = component.getElements();
            for (int k = 0; k < elements3.length; ++k) {
                if (k != component.getSelectedIndex()) {
                    this.theme.getFontRenderer().drawString(elements3[k], 2, offset2, RenderUtil.toRGBA(component.getForegroundColor()));
                    offset2 += this.theme.getFontRenderer().FONT_HEIGHT + 2;
                }
            }
        }
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        this.translateComponent(component, true);
    }
    
    @Override
    protected Dimension getDefaultComponentSize(final ComboBox component) {
        int maxWidth = 0;
        String[] elements;
        for (int length = (elements = component.getElements()).length, i = 0; i < length; ++i) {
            final String element = elements[i];
            maxWidth = Math.max(maxWidth, this.theme.getFontRenderer().getStringWidth(element));
        }
        return new Dimension(maxWidth + 8 + this.theme.getFontRenderer().FONT_HEIGHT, this.theme.getFontRenderer().FONT_HEIGHT + 4);
    }
    
    @Override
    protected Rectangle[] getInteractableComponentRegions(final ComboBox component) {
        int height = component.getHeight();
        if (component.isSelected()) {
            final String[] elements = component.getElements();
            for (int i = 0; i < elements.length; ++i) {
                height += this.theme.getFontRenderer().FONT_HEIGHT + 2;
            }
            height += 2;
        }
        return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), height) };
    }
    
    @Override
    protected void handleComponentInteraction(final ComboBox component, final Point location, final int button) {
        if (button != 0) {
            return;
        }
        if (location.x <= component.getWidth() && location.y <= component.getHeight()) {
            component.setSelected(!component.isSelected());
        }
        else if (location.x <= component.getWidth() && component.isSelected()) {
            int offset = component.getHeight() + 2;
            final String[] elements = component.getElements();
            for (int i = 0; i < elements.length; ++i) {
                if (i != component.getSelectedIndex()) {
                    if (location.y >= offset && location.y <= offset + this.theme.getFontRenderer().FONT_HEIGHT) {
                        component.setSelectedIndex(i);
                        component.setSelected(false);
                        break;
                    }
                    offset += this.theme.getFontRenderer().FONT_HEIGHT + 2;
                }
            }
        }
    }
}
