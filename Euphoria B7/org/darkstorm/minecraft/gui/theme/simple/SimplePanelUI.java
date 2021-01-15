// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme.simple;

import org.darkstorm.minecraft.gui.layout.Constraint;
import java.awt.Dimension;
import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.darkstorm.minecraft.gui.component.Component;
import java.awt.Color;
import org.darkstorm.minecraft.gui.component.Panel;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;

public class SimplePanelUI extends AbstractComponentUI<Panel>
{
    private final SimpleTheme theme;
    
    SimplePanelUI(final SimpleTheme theme) {
        super(Panel.class);
        this.theme = theme;
        this.foreground = Color.WHITE;
        this.background = new Color(128, 128, 128, 128);
    }
    
    @Override
    protected void renderComponent(final Panel component) {
        if (component.getParent() != null) {
            return;
        }
        final Rectangle area = component.getArea();
        this.translateComponent(component, false);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glBlendFunc(770, 771);
        RenderUtil.setColor(component.getBackgroundColor());
        GL11.glBegin(7);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d((double)area.width, 0.0);
        GL11.glVertex2d((double)area.width, (double)area.height);
        GL11.glVertex2d(0.0, (double)area.height);
        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        this.translateComponent(component, true);
    }
    
    @Override
    protected Dimension getDefaultComponentSize(final Panel component) {
        final Component[] children = component.getChildren();
        final Rectangle[] areas = new Rectangle[children.length];
        final Constraint[][] constraints = new Constraint[children.length][];
        for (int i = 0; i < children.length; ++i) {
            final Component child = children[i];
            final Dimension size = child.getTheme().getUIForComponent(child).getDefaultSize(child);
            areas[i] = new Rectangle(0, 0, size.width, size.height);
            constraints[i] = component.getConstraints(child);
        }
        return component.getLayoutManager().getOptimalPositionedSize(areas, constraints);
    }
}
