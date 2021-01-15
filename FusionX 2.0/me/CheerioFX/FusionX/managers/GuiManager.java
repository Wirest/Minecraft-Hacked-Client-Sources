// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.managers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.darkstorm.minecraft.gui.AbstractGuiManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.CheckButton;
import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.ComboBoxListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import org.lwjgl.input.Keyboard;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.module.modules.HideKeyBinds;
import me.CheerioFX.FusionX.values.BooleanValue;
import me.CheerioFX.FusionX.values.Value;
import net.minecraft.client.Minecraft;

public final class GuiManager extends AbstractGuiManager {
	private final AtomicBoolean setup;
	public static Value bgTransparency;
	public static Value highlightDarkness;
	public static Value red;
	public static Value green;
	public static Value blue;

	static {
		GuiManager.bgTransparency = new Value("BackGround Transparency", 69.0, 0.0, 100.0,
				BoundedRangeComponent.ValueDisplay.PERCENTAGE, "gui");
		GuiManager.highlightDarkness = new Value("Highlight Darkness", 33.0, 0.0, 100.0,
				BoundedRangeComponent.ValueDisplay.PERCENTAGE, "gui");
		GuiManager.red = new Value("Red", 100.0, 0.0, 100.0, BoundedRangeComponent.ValueDisplay.PERCENTAGE, "gui");
		GuiManager.green = new Value("Green", 5.0, 0.0, 100.0, BoundedRangeComponent.ValueDisplay.PERCENTAGE, "gui");
		GuiManager.blue = new Value("Blue", 5.0, 0.0, 100.0, BoundedRangeComponent.ValueDisplay.PERCENTAGE, "gui");
	}

	public static int getRBG() {
		return new Color((int) (GuiManager.red.getValueI() * 2.55), (int) (GuiManager.green.getValueI() * 2.55),
				(int) (GuiManager.blue.getValueI() * 2.55)).getRGB();
	}

	public static Color getRBGA() {
		return new Color((int) (GuiManager.red.getValueI() * 2.55), (int) (GuiManager.green.getValueI() * 2.55),
				(int) (GuiManager.blue.getValueI() * 2.55), getBGTransparency());
	}

	public static int getBGTransparency() {
		final int value = 255 - (int) (GuiManager.bgTransparency.getValueI() * 0.1);
		return value;
	}

	public static int getBGTransparencyALittleLighter() {
		final int value = 255 - GuiManager.bgTransparency.getValueI();
		return value;
	}

	public static float getBGTransparencyOutOfOne() {
		final int value = 255 - (int) (GuiManager.bgTransparency.getValueI() * 0.3);
		return value / 2.55f * 0.01f;
	}

	public static int getBGTransparency(final boolean tabGui) {
		int value = 255 - (int) (GuiManager.bgTransparency.getValueI() * 2.55);
		if (value < 50) {
			value += (int) 25.5;
		}
		return value;
	}

	public static int getHighlightDarkness() {
		return 255 - (int) (GuiManager.highlightDarkness.getValueI() * 2.55);
	}

	public GuiManager() {
		this.setup = new AtomicBoolean();
	}

	@Override
	public void setup() {
		if (!this.setup.compareAndSet(false, true)) {
			return;
		}
		this.createKillauraFrame();
		this.createiAuraFrame();
		this.createTargetSettingsFrame();
		this.createOtherSettingsFrame();
		this.createGuiFrame();
		final Map<Category, ModuleFrame> categoryFrames = new HashMap<Category, ModuleFrame>();
		for (final Module module : ModuleManager.getModules()) {
			if (module.isCategory(Category.GUI)) {
				continue;
			}
			ModuleFrame frame = categoryFrames.get(module.getCategory());
			if (frame == null) {
				String name = module.getCategory().name().toLowerCase();
				name = String.valueOf(Character.toUpperCase(name.charAt(0))) + name.substring(1);
				frame = new ModuleFrame(name);
				frame.setTheme(this.getTheme());
				frame.setLayoutManager(new GridLayoutManager(1, 0));
				frame.setVisible(true);
				frame.setClosable(false);
				frame.setMinimized(true);
				frame.setPinnable(false);
				final Dimension defaultDimension = this.theme.getUIForComponent(frame).getDefaultSize(frame);
				frame.setWidth(defaultDimension.width);
				frame.setHeight(defaultDimension.height);
				frame.layoutChildren();
				this.addFrame(frame);
				categoryFrames.put(module.getCategory(), frame);
			}
			final Module updateModule = module;
			final Button button = new BasicButton(module.getName()) {
				@Override
				public void update() {
					String string = updateModule.getName();
					if (updateModule.getExtraInfo() != null) {
						string = String.valueOf(string) + " " + updateModule.getExtraInfo();
					}
					if (!HideKeyBinds.enabled && updateModule.getBind() != 0) {
						string = String.valueOf(string) + " [" + Keyboard.getKeyName(updateModule.getBind()) + "]";
					}
					this.setText(string);
					this.setEnabled(updateModule.getState());
					if (updateModule.getState()) {
						this.setBackgroundColor(GuiManager.getRBGA());
					} else {
						this.setBackgroundColor(new Color(105, 105, 105, GuiManager.getBGTransparencyALittleLighter()));
					}
				}
			};
			button.addButtonListener(new ButtonListener() {
				@Override
				public void onButtonPress(final Button button) {
					updateModule.toggleModule();
					button.setText(updateModule.getName());
					button.setEnabled(updateModule.getState());
				}
			});
			frame.add(button, GridLayoutManager.HorizontalGridConstraint.FILL);
		}
		this.resizeComponents();
		final Minecraft minecraft = Minecraft.getMinecraft();
		final Dimension maxSize = this.recalculateSizes();
		int offsetX = 5;
		int offsetY = 5;
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0) {
			scale = 1000;
		}
		int scaleFactor;
		for (scaleFactor = 0; scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240; ++scaleFactor) {
		}
		Frame[] frames;
		for (int length = (frames = this.getFrames()).length, i = 0; i < length; ++i) {
			final Frame frame2 = frames[i];
			frame2.setX(offsetX);
			frame2.setY(offsetY);
			offsetX += maxSize.width + 5;
			if (offsetX + maxSize.width + 5 > minecraft.displayWidth / scaleFactor) {
				offsetX = 5;
				offsetY += maxSize.height + 5;
			}
		}
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
				for (int length2 = (interactableRegions = frame.getTheme().getUIForComponent(frame)
						.getInteractableRegions(frame)).length, j = 0; j < length2; ++j) {
					final Rectangle area = interactableRegions[j];
					maxHeight = Math.max(maxHeight, area.height);
				}
			} else {
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

	private void createTestFrame() {
		final Theme theme = this.getTheme();
		final Frame testFrame = new BasicFrame("Test Frame");
		testFrame.setTheme(theme);
		final ComboBox comboBox = new BasicComboBox(new String[] { "Simple Theme", "FusionX Theme" });
		comboBox.addComboBoxListener(new ComboBoxListener() {
			@Override
			public void onComboBoxSelectionChanged(final ComboBox comboBox) {
				Theme theme = null;
				switch (comboBox.getSelectedIndex()) {
				case 0: {
					theme = new SimpleTheme();
					break;
				}
				case 1: {
					break;
				}
				default: {
					return;
				}
				}
				GuiManager.this.setTheme(theme);
			}
		});
		testFrame.add(comboBox, new Constraint[0]);
		testFrame.setX(50);
		testFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(testFrame).getDefaultSize(testFrame);
		testFrame.setWidth(defaultDimension.width);
		testFrame.setHeight(defaultDimension.height);
		testFrame.setVisible(true);
		testFrame.setClosable(false);
		testFrame.setMinimized(true);
		testFrame.setPinnable(false);
		this.addFrame(testFrame);
	}

	private void createTargetSettingsFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("Target Settings");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "target") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "target") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						v2.setValue(checkbox.isSelected());
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		this.addFrame(valuesFrame);
	}

	private void createKillauraFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("KillAura Settings");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "killaura") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "killaura") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						v2.setValue(checkbox.isSelected());
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		this.addFrame(valuesFrame);
	}

	private void createCFlyFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("Cubecraft Fly Settings");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "cfly") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "cfly") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						v2.setValue(checkbox.isSelected());
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		this.addFrame(valuesFrame);
	}

	private void createOtherSettingsFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("Other Settings");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "other") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "other") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						v2.setValue(checkbox.isSelected());
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		this.addFrame(valuesFrame);
	}

	private void createiAuraFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("InfiniteAura Settings");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "iaura") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "iaura") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						v2.setValue(checkbox.isSelected());
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		valuesFrame.update();
		this.addFrame(valuesFrame);
	}

	private void createGuiFrame() {
		final Theme theme = this.getTheme();
		final Frame valuesFrame = new BasicFrame("Gui Manager");
		valuesFrame.setTheme(theme);
		valuesFrame.setX(50);
		valuesFrame.setY(50);
		final Dimension defaultDimension = theme.getUIForComponent(valuesFrame).getDefaultSize(valuesFrame);
		valuesFrame.setWidth(defaultDimension.width);
		valuesFrame.setHeight(defaultDimension.height);
		valuesFrame.layoutChildren();
		valuesFrame.setVisible(true);
		valuesFrame.setClosable(false);
		valuesFrame.setMinimized(true);
		valuesFrame.setPinnable(false);
		for (final Value v : Value.getVals()) {
			if (v.getId() == "gui") {
				if (v.getValueDisplay() == null) {
					continue;
				}
				final Slider slider = new BasicSlider(v.getName());
				slider.setValueDisplay(v.getValueDisplay());
				slider.setValue(v.getValue());
				slider.setMaximumValue(v.getMax());
				slider.setMinimumValue(v.getMin());
				slider.setIncrement(0.0);
				slider.setEnabled(true);
				slider.addSliderListener(new SliderListener() {
					@Override
					public void onSliderValueChanged(final Slider slider) {
						v.setValue(slider.getValue());
					}
				});
				slider.update();
				valuesFrame.add(slider, new Constraint[0]);
			}
			valuesFrame.update();
		}
		for (final BooleanValue v2 : BooleanValue.getVals()) {
			if (v2.getId() == "gui") {
				final CheckButton checkbox = new BasicCheckButton(v2.getName());
				checkbox.setSelected(v2.getValue());
				checkbox.setEnabled(true);
				checkbox.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(final Button button) {
						if (button.getText().equalsIgnoreCase("tabgui")) {
							v2.setValue(checkbox.isSelected());
							FusionX.theClient.tabGui.updateRegisteration();
						} else {
							v2.setValue(checkbox.isSelected());
						}
					}
				});
				checkbox.update();
				valuesFrame.add(checkbox, new Constraint[0]);
			}
			valuesFrame.update();
		}
		this.addFrame(valuesFrame);
	}

	private class ModuleFrame extends BasicFrame {
		private ModuleFrame() {
		}

		private ModuleFrame(final String title) {
			super(title);
		}
	}
}
