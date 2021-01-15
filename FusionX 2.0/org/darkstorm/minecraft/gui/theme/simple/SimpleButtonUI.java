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
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;

public class SimpleButtonUI extends AbstractComponentUI<Button>
{
    private final SimpleTheme theme;
    
    SimpleButtonUI(final SimpleTheme theme) {
        super(Button.class);
        this.theme = theme;
        this.foreground = Color.WHITE;
        this.background = new Color(105, 105, 105, 192);
    }
    
    @Override
    protected void renderComponent(final Button button) {
        this.translateComponent(button, false);
        final Rectangle area = button.getArea();
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        RenderUtil.setColor(button.getBackgroundColor());
        GL11.glBegin(7);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d((double)area.width, 0.0);
        GL11.glVertex2d((double)area.width, (double)area.height);
        GL11.glVertex2d(0.0, (double)area.height);
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
        this.theme.getFontRenderer().drawString(text, area.width / 2 - this.theme.getFontRenderer().getStringWidth(text) / 2, area.height / 2 - this.theme.getFontRenderer().FONT_HEIGHT / 2, RenderUtil.toRGBA(button.getForegroundColor()));
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        this.translateComponent(button, true);
    }
    
    @Override
    protected Dimension getDefaultComponentSize(final Button component) {
        return new Dimension(this.theme.getFontRenderer().getStringWidth(component.getText()) + 4, this.theme.getFontRenderer().FONT_HEIGHT + 4);
    }
    
    @Override
    protected Rectangle[] getInteractableComponentRegions(final Button component) {
        return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), component.getHeight()) };
    }
    
    @Override
    protected void handleComponentInteraction(final Button component, final Point location, final int button) {
        if (location.x <= component.getWidth() && location.y <= component.getHeight() && button == 0) {
            component.press();
        }
    }
}
