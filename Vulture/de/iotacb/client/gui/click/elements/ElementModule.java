package de.iotacb.client.gui.click.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.ClickConfig;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.gui.click.ElementValue;
import de.iotacb.client.gui.click.GuiBindScreen;
import de.iotacb.client.gui.click.GuiClick;
import de.iotacb.client.gui.click.elements.values.ElementCombo;
import de.iotacb.client.gui.click.elements.values.ElementSlider;
import de.iotacb.client.gui.click.elements.values.ElementToggle;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueType;
import net.minecraft.util.ResourceLocation;

public class ElementModule extends Element {

	Module module;

	double posYOffset;

	boolean extended;

	List<ElementValue> values;

	boolean useWidestWidth;
	
	int rotation;

	public ElementModule(double posX, double posY, double width, double height, Element parent, Module module) {
		super(posX, posY, width, height, parent);

		this.module = module;

		this.values = new ArrayList<ElementValue>();

		addValues();
	}

	@Override
	public void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)) {
			if (mouseButton == 0) {
				getModule().toggle();
			} else if (mouseButton == 1) {
				if (module.getValues().size() > 0) {
					for (ElementModule elementModule : ((ElementPanel) getParent()).getModules()) {
						if (elementModule.module != module) {
							elementModule.setExtended(false);
						}
					}
					setExtended(!isExtended());
				}
			} else if (mouseButton == 2) {
				getMc().displayGuiScreen(new GuiBindScreen(getModule()));
			}
		}

		if (isExtended())
			getValues().forEach(value -> {
				if (value.getValue().isEnabled())
					value.clickElement(mouseX, mouseY, mouseButton);
			});

	}

	@Override
	public void updateElement(int mouseX, int mouseY) {
		if (isHovered(mouseX, mouseY)) {
			if (((ElementPanel) getParent()).getHoveredModule() != this) {
				((ElementPanel) getParent()).setHoveredModule(this);
				((ElementPanel) getParent()).animUtil.getProgression(0).setValue(0);
				((ElementPanel) getParent()).hoveringTicks = 0;
				((ElementPanel) getParent()).tooltipAlpha = 0;
			} else {
				((ElementPanel) getParent()).hoveringTicks += Client.DELTA_UTIL.deltaTime;
			}
		}
		if (getParent() instanceof ElementPanel) {
			if (((ElementPanel) getParent()).isDragging()) {
				updatePositions();
			}
		}
		rotation = (int) AnimationUtil.slide(rotation, 0, 90, .03, isExtended());
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		Client.RENDER2D.rect(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT);
		String bindString = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? "[" + (getModule().isMultiBinded() ? Keyboard.getKeyName(getModule().getMultiBindKey()) + "," : "") + Keyboard.getKeyName(getModule().getKey()) + "] " : "";
		if (Keyboard.getKeyName(getModule().getKey()).equalsIgnoreCase("none")) {
			bindString = "";
		}
		getFontRenderer().drawStringWithShadow(bindString + getModule().getName(), getPosX() + 5, getPosY() + getHeight() / 2 - getFontRenderer().getHeight(getModule().getName()) / 2, module.isEnabled() ? isDarkMode() ? Client.INSTANCE.getClientColor().addColoring(40) : Client.INSTANCE.getClientColor() : isDarkMode() ? ClickConfig.MODULE_TITLE_DARK : ClickConfig.MODULE_TITLE_LIGHT);

		if (isExtended())
			drawValues(mouseX, mouseY);

		if (module.getValues().size() > 1) {
//			Client.RENDER2D.circle(getPosX() + getWidth() - 11.5, getPosY() + getHeight() / 2 - 2.5, 6, Color.black);
//			Client.RENDER2D.circle(getPosX() + getWidth() - 12, getPosY() + getHeight() / 2 - 3, 6, isExtended() ? Client.INSTANCE.getClientColor() : isDarkMode() ? Color.white : new Color(80, 80, 80));
			Client.RENDER2D.push();
			final int size = 10;
			Client.RENDER2D.translate(getPosX() + getWidth() - size / 2 - 2, getPosY() + getHeight() - size / 2 - 2);
			Client.RENDER2D.rotate(0, 0, 1, rotation);
			Client.RENDER2D.image(new ResourceLocation("client/designs/default/sub_buttons/wheel-lonely.png"), -size / 2, -size / 2, size, size);
			Client.RENDER2D.pop();
		}

	}

	public Module getModule() {
		return module;
	}

	public boolean isExtended() {
		return extended;
	}

	public double getPosYOffset() {
		return posYOffset;
	}

	public List<ElementValue> getValues() {
		return values;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public void setPosYOffset(double posYOffset) {
		this.posYOffset = posYOffset;
	}

	void drawValues(int mouseX, int mouseY) {
		getValues().forEach(value -> {
			if (value.getValue().isEnabled())
				value.updateElement(mouseX, mouseY);
			value.drawElement(mouseX, mouseY);
		});
	}

	public void updatePositions() {
		getValues().forEach(value -> {
			value.setPosition(getPosX(), getPosY());
		});
	}

	void addValues() {
		int valuePosY = 0;
		double width = getWidestWidth();
		int size = module.getValues().size();
		for (int i = 0; i < size; i++) {
			Value value = module.getValues().get(i);
			if (value.getValueType() == ValueType.NUMBER) {
				ElementValue elementValue = new ElementSlider(getPosX(), getPosY(), width, getHeight(), this);
				elementValue.setPosOffsets(width + getWidth() - getWidestWidth(), valuePosY);
				elementValue.setValue(value);
				if (size == 1) {
					elementValue.setLonely(true);
				} else {
					elementValue.setFirst(i == 0);
					if (size > 1)
						elementValue.setLast(i == size - 1);
				}
				this.values.add(elementValue);
			} else if (value.getValueType() == ValueType.COMBO) {
				ElementValue elementValue = new ElementCombo(getPosX(), getPosY(), width, getHeight(), this);
				elementValue.setPosOffsets(width + getWidth() - getWidestWidth(), valuePosY);
				elementValue.setValue(value);
				if (size == 1) {
					elementValue.setLonely(true);
				} else {
					elementValue.setFirst(i == 0);
					if (size > 1)
						elementValue.setLast(i == size - 1);
				}
				this.values.add(elementValue);
			} else if (value.getValueType() == ValueType.BOOL) {
				ElementValue elementValue = new ElementToggle(getPosX(), getPosY(), width, getHeight(), this);
				elementValue.setPosOffsets(width + getWidth() - getWidestWidth(), valuePosY);
				elementValue.setValue(value);
				if (size == 1) {
					elementValue.setLonely(true);
				} else {
					elementValue.setFirst(i == 0);
					if (size > 1)
						elementValue.setLast(i == size - 1);
				}
				this.values.add(elementValue);

			}
			valuePosY += getHeight();
		}
	}

	double getWidestWidth() {
		double widestWidth = 100;
		for (Value value : module.getValues()) {
			double width = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(value.getValueName().substring(module.getName().length()));
			if (width > widestWidth) {
				widestWidth = width;
			}
		}
		return widestWidth;
	}

}
