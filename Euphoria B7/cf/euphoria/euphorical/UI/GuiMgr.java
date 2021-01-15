// 
 // Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.UI;

import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.component.CheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import cf.euphoria.euphorical.Mod.BoolOption;
import cf.euphoria.euphorical.Utils.MathUtils;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import cf.euphoria.euphorical.Mod.NumValue;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import java.awt.Color;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.layout.LayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Category;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.darkstorm.minecraft.gui.AbstractGuiManager;

public final class GuiMgr extends AbstractGuiManager
{
    private final AtomicBoolean setup;
    
    public GuiMgr() {
        this.setup = new AtomicBoolean();
    }
    
    @Override
    public void setup() {
        if (!this.setup.compareAndSet(false, true)) {
            return;
        }
        this.createSettingsFrame();
        final Map<Category, ModuleFrame> categoryFrames = new HashMap<Category, ModuleFrame>();
        for (final Mod mod : Euphoria.getEuphoria().theMods.getMods()) {
            ModuleFrame frame = categoryFrames.get(mod.getCategory());
            if (frame == null) {
                String name = mod.getCategory().name().toLowerCase();
                name = String.valueOf(Character.toUpperCase(name.charAt(0))) + name.substring(1);
                frame = new ModuleFrame(name);
                frame.setTheme(this.theme);
                frame.setLayoutManager(new GridLayoutManager(1, 0));
                frame.setVisible(true);
                frame.setClosable(false);
                frame.setMinimized(true);
                frame.setPinnable(false);
                this.addFrame(frame);
                categoryFrames.put(mod.getCategory(), frame);
            }
            final Mod updateMod = mod;
            final Button button = new BasicButton(mod.getModName()) {
                @Override
                public void update() {
                    final Color green = new Color(0.0f, 1.0f, 0.4f, 1.0f);
                    final Color gray = new Color(0.3f, 0.3f, 0.3f, 1.0f);
                    this.setBackgroundColor(updateMod.isEnabled() ? green : gray);
                }
            };
            button.addButtonListener(new ButtonListener() {
                @Override
                public void onButtonPress(final Button button) {
                    updateMod.toggle();
                }
            });
            frame.add(button, GridLayoutManager.HorizontalGridConstraint.FILL);
        }
        this.resizeComponents();
        final Minecraft minecraft = Minecraft.getMinecraft();
        final Dimension maxSize = this.recalculateSizes();
        int offsetX = 5;
        int offsetY = 5;
        int scale = Minecraft.gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        int scaleFactor;
        for (scaleFactor = 0; scaleFactor < scale && Minecraft.displayWidth / (scaleFactor + 1) >= 320 && Minecraft.displayHeight / (scaleFactor + 1) >= 240; ++scaleFactor) {}
        Frame[] frames;
        for (int length = (frames = this.getFrames()).length, i = 0; i < length; ++i) {
            final Frame frame2 = frames[i];
            frame2.setX(offsetX);
            frame2.setY(offsetY);
            offsetX += maxSize.width + 5;
            if (offsetX + maxSize.width + 5 > Minecraft.displayWidth / scaleFactor) {
                offsetX = 5;
                offsetY += maxSize.height + 5;
            }
        }
    }
    
    private void createSettingsFrame() {
        final Frame valuesFrame = new BasicFrame("Value Manager");
        valuesFrame.setTheme(this.getTheme());
        valuesFrame.setLayoutManager(new GridLayoutManager(1, 0));
        valuesFrame.setVisible(true);
        valuesFrame.setClosable(false);
        valuesFrame.setMinimized(true);
        valuesFrame.setPinnable(false);
        for (final NumValue v : NumValue.getVals()) {
            if (v.getValueDisplay() == null) {
                continue;
            }
            final Slider slider = new BasicSlider(v.getName());
            slider.setMinimumValue(v.getMin());
            slider.setMaximumValue(v.getMax());
            slider.setValue(v.getValue());
            slider.setValueDisplay(v.getValueDisplay());
            slider.setIncrement(1.0);
            if (v.getValueDisplay() == BoundedRangeComponent.ValueDisplay.DECIMAL) {
                slider.setIncrement(0.5);
            }
            slider.setEnabled(true);
            slider.addSliderListener(new SliderListener() {
                @Override
                public void onSliderValueChanged(final Slider slider) {
                    if (slider.getValueDisplay() != BoundedRangeComponent.ValueDisplay.DECIMAL) {
                        v.setValue(MathUtils.round(slider.getValue(), 0));
                    }
                    else {
                        v.setValue(MathUtils.round(slider.getValue(), 0));
                    }
                }
            });
            valuesFrame.add(slider, GridLayoutManager.HorizontalGridConstraint.FILL);
        }
        this.addFrame(valuesFrame);
        final Frame optionsFrame = new BasicFrame("Settings/Modes");
        optionsFrame.setTheme(this.getTheme());
        optionsFrame.setLayoutManager(new GridLayoutManager(1, 0));
        optionsFrame.setVisible(true);
        optionsFrame.setClosable(false);
        optionsFrame.setMinimized(true);
        optionsFrame.setPinnable(false);
        for (final BoolOption op : BoolOption.getVals()) {
            final CheckButton cb = new BasicCheckButton(op.getName());
            cb.addButtonListener(new ButtonListener() {
                @Override
                public void onButtonPress(final Button button) {
                    op.setEnabled(!op.isEnabled());
                }
            });
            optionsFrame.add(cb, GridLayoutManager.HorizontalGridConstraint.FILL);
        }
        this.addFrame(optionsFrame);
    }
    
    @Override
    protected void resizeComponents() {
        final Theme theme = this.getTheme();
        final Frame[] frames = this.getFrames();
        final Button enable = new BasicButton("Enable");
        final Button disable = new BasicButton("Disable");
        final Dimension enableSize = theme.getUIForComponent(enable).getDefaultSize(enable);
        final Dimension disableSize = theme.getUIForComponent(disable).getDefaultSize(disable);
        final int buttonWidth = Math.max(enableSize.width, disableSize.width);
        final int buttonHeight = Math.max(enableSize.height, disableSize.height);
        Frame[] array;
        for (int length = (array = frames).length, i = 0; i < length; ++i) {
            final Frame frame = array[i];
            if (frame instanceof ModuleFrame) {
                Component[] children;
                for (int length2 = (children = frame.getChildren()).length, j = 0; j < length2; ++j) {
                    final Component component = children[j];
                    if (component instanceof Button) {
                        component.setWidth(buttonWidth);
                        component.setHeight(buttonHeight);
                    }
                }
            }
        }
        this.recalculateSizes();
    }
    
    private Dimension recalculateSizes() {
        final Frame[] frames = this.getFrames();
        int maxWidth = 0;
        int maxHeight = 0;
        Frame[] array;
        for (int length = (array = frames).length, i = 0; i < length; ++i) {
            final Frame frame = array[i];
            final Dimension defaultDimension = frame.getTheme().getUIForComponent(frame).getDefaultSize(frame);
            maxWidth = Math.max(maxWidth, defaultDimension.width);
            frame.setHeight(defaultDimension.height);
            if (frame.isMinimized()) {
                Rectangle[] interactableRegions;
                for (int length2 = (interactableRegions = frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame)).length, j = 0; j < length2; ++j) {
                    final Rectangle area = interactableRegions[j];
                    maxHeight = Math.max(maxHeight, area.height);
                }
            }
            else {
                maxHeight = Math.max(maxHeight, defaultDimension.height);
            }
        }
        Frame[] array2;
        for (int length3 = (array2 = frames).length, k = 0; k < length3; ++k) {
            final Frame frame = array2[k];
            frame.setWidth(maxWidth);
            frame.layoutChildren();
        }
        return new Dimension(maxWidth, maxHeight);
    }
    
    private class ModuleFrame extends BasicFrame
    {
        private ModuleFrame() {
        }
        
        private ModuleFrame(final String title) {
            super(title);
        }
    }
}
