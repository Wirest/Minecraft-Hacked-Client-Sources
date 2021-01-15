// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.util;

import java.io.IOException;
import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.GuiManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiManagerDisplayScreen extends GuiScreen
{
    private final GuiManager guiManager;
    
    public GuiManagerDisplayScreen(final GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) throws IOException {
        super.mouseClicked(x, y, button);
        Frame[] frames;
        for (int length = (frames = this.guiManager.getFrames()).length, i = 0; i < length; ++i) {
            final Frame frame = frames[i];
            if (frame.isVisible()) {
                if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
                    Component[] children;
                    for (int length2 = (children = frame.getChildren()).length, j = 0; j < length2; ++j) {
                        final Component component = children[j];
                        Rectangle[] interactableRegions;
                        for (int length3 = (interactableRegions = component.getTheme().getUIForComponent(component).getInteractableRegions(component)).length, k = 0; k < length3; ++k) {
                            final Rectangle area = interactableRegions[k];
                            if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
                                frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
                                this.guiManager.bringForward(frame);
                                return;
                            }
                        }
                    }
                }
            }
        }
        Frame[] frames2;
        for (int length4 = (frames2 = this.guiManager.getFrames()).length, l = 0; l < length4; ++l) {
            final Frame frame = frames2[l];
            if (frame.isVisible()) {
                if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
                    frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
                    this.guiManager.bringForward(frame);
                    break;
                }
                if (frame.isMinimized()) {
                    Rectangle[] interactableRegions2;
                    for (int length5 = (interactableRegions2 = frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)).length, n = 0; n < length5; ++n) {
                        final Rectangle area2 = interactableRegions2[n];
                        if (area2.contains(x - frame.getX(), y - frame.getY())) {
                            frame.onMousePress(x - frame.getX(), y - frame.getY(), button);
                            this.guiManager.bringForward(frame);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        super.mouseReleased(x, y, button);
        Frame[] frames;
        for (int length = (frames = this.guiManager.getFrames()).length, i = 0; i < length; ++i) {
            final Frame frame = frames[i];
            if (frame.isVisible()) {
                if (!frame.isMinimized() && !frame.getArea().contains(x, y)) {
                    Component[] children;
                    for (int length2 = (children = frame.getChildren()).length, j = 0; j < length2; ++j) {
                        final Component component = children[j];
                        Rectangle[] interactableRegions;
                        for (int length3 = (interactableRegions = component.getTheme().getUIForComponent(component).getInteractableRegions(component)).length, k = 0; k < length3; ++k) {
                            final Rectangle area = interactableRegions[k];
                            if (area.contains(x - frame.getX() - component.getX(), y - frame.getY() - component.getY())) {
                                frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
                                this.guiManager.bringForward(frame);
                                return;
                            }
                        }
                    }
                }
            }
        }
        Frame[] frames2;
        for (int length4 = (frames2 = this.guiManager.getFrames()).length, l = 0; l < length4; ++l) {
            final Frame frame = frames2[l];
            if (frame.isVisible()) {
                if (!frame.isMinimized() && frame.getArea().contains(x, y)) {
                    frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
                    this.guiManager.bringForward(frame);
                    break;
                }
                if (frame.isMinimized()) {
                    Rectangle[] interactableRegions2;
                    for (int length5 = (interactableRegions2 = frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)).length, n = 0; n < length5; ++n) {
                        final Rectangle area2 = interactableRegions2[n];
                        if (area2.contains(x - frame.getX(), y - frame.getY())) {
                            frame.onMouseRelease(x - frame.getX(), y - frame.getY(), button);
                            this.guiManager.bringForward(frame);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int par2, final int par3, final float par4) {
        this.guiManager.render();
        super.drawScreen(par2, par3, par4);
    }
}
