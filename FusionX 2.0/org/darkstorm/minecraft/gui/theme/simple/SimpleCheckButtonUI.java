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
import org.darkstorm.minecraft.gui.component.CheckButton;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;

public class SimpleCheckButtonUI extends AbstractComponentUI<CheckButton>
{
    private final SimpleTheme theme;
    
    SimpleCheckButtonUI(final SimpleTheme theme) {
        super(CheckButton.class);
        this.theme = theme;
        this.foreground = Color.WHITE;
        this.background = new Color(128, 128, 128, 128);
    }
    
    @Override
    protected void renderComponent(final CheckButton button) {
        this.translateComponent(button, false);
        final Rectangle area = button.getArea();
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        RenderUtil.setColor(button.getBackgroundColor());
        final int size = area.height - 4;
        GL11.glBegin(7);
        GL11.glVertex2d(2.0, 2.0);
        GL11.glVertex2d((double)(size + 2), 2.0);
        GL11.glVertex2d((double)(size + 2), (double)(size + 2));
        GL11.glVertex2d(2.0, (double)(size + 2));
        GL11.glEnd();
        if (button.isSelected()) {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            GL11.glBegin(7);
            GL11.glVertex2d(3.0, 3.5);
            GL11.glVertex2d(size + 0.5, 3.5);
            GL11.glVertex2d(size + 0.5, (double)(size + 1));
            GL11.glVertex2d(3.0, (double)(size + 1));
            GL11.glEnd();
        }
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(2);
        GL11.glVertex2d(2.0, 2.0);
        GL11.glVertex2d((double)(size + 2), 2.0);
        GL11.glVertex2d((double)(size + 2), (double)(size + 2));
        GL11.glVertex2d(1.5, (double)(size + 2));
        GL11.glEnd();
        final Point mouse = RenderUtil.calculateMouseLocation();
        for (Component parent = button.getParent(); parent != null; parent = parent.getParent()) {
            final Point point = mouse;
            point.x -= parent.getX();
            final Point point2 = mouse;
            point2.y -= parent.getY();
        }
        if (area.contains(mouse)) {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.5f : 0.3f);
            GL11.glBegin(7);
            GL11.glVertex2d(0.0, 0.0);
            GL11.glVertex2d((double)area.width, 0.0);
            GL11.glVertex2d((double)area.width, (double)area.height);
            GL11.glVertex2d(0.0, (double)area.height);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        final String text = button.getText();
        this.theme.getFontRenderer().drawString(text, size + 4, area.height / 2 - this.theme.getFontRenderer().FONT_HEIGHT / 2, RenderUtil.toRGBA(button.getForegroundColor()));
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        this.translateComponent(button, true);
    }
    
    @Override
    protected Dimension getDefaultComponentSize(final CheckButton component) {
        return new Dimension(this.theme.getFontRenderer().getStringWidth(component.getText()) + this.theme.getFontRenderer().FONT_HEIGHT + 6, this.theme.getFontRenderer().FONT_HEIGHT + 4);
    }
    
    @Override
    protected Rectangle[] getInteractableComponentRegions(final CheckButton component) {
        return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), component.getHeight()) };
    }
    
    @Override
    protected void handleComponentInteraction(final CheckButton component, final Point location, final int button) {
        if (location.x <= component.getWidth() && location.y <= component.getHeight() && button == 0) {
            component.press();
        }
    }
}
