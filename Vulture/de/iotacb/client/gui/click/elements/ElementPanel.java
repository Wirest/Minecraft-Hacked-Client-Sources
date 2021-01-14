package de.iotacb.client.gui.click.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.ClickConfig;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Cubic;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.cu.core.math.MathUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class ElementPanel extends Element {

	String panelTitle;

	List<ElementModule> modules;

	boolean dragging;
	boolean extended;
	boolean useWidestWidth;

	double startMouseX, startMouseY, scissorValue;

	int clickIndex;

	ElementModule hoveredModule;
	ElementColorPicker colorPicker;

	public AnimationUtil animUtil;

	public double tooltipAlpha, rotation;
	public int hoveringTicks;

	public ElementPanel(double posX, double posY, double width, double height, Element parent, String panelTitle) {
		super(posX, posY, width, height, parent);
		this.panelTitle = panelTitle;

		this.modules = new ArrayList<ElementModule>();

		this.animUtil = new AnimationUtil(Cubic.class);
		animUtil.addProgression(0);

		this.setWidth(getWidesWidth() < 100 ? 100 : getWidesWidth());

		if (panelTitle.equalsIgnoreCase("color")) {
			this.setWidth(getWidth() + 20);
			colorPicker = new ElementColorPicker(getPosX(), getPosY() + getHeight(), getWidth(), 100, this);
		} else {
			addModules();
		}
	}

	@Override
	public void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)) {
			if (mouseButton == 0) {
				startMouseX = mouseX - getPosX();
				startMouseY = mouseY - getPosY();
				dragging = true;
				clickIndex = 1;
			} else if (mouseButton == 1) {
				if (modules.size() > 0 || colorPicker != null) {
					setExtended(!isExtended());
				}
			}
		}

		if (isExtended()) {
			if (colorPicker != null) {
				colorPicker.clickElement(mouseX, mouseY, mouseButton);
			}
			this.modules.forEach(module -> module.clickElement(mouseX, mouseY, mouseButton));
		}
	}

	double targetPosX, targetPosY;
	double posXSmoothed, posYSmoothed;

	@Override
	public void updateElement(int mouseX, int mouseY) {
		rotation = AnimationUtil.slide(rotation, 0, 90, .1, !isExtended());
		if (!Mouse.isButtonDown(0)) {
			dragging = false;
			clickIndex = 0;
		}
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {
			targetPosX = 0;
			targetPosY = 0;
		}
		if (isDragging() || (targetPosX != 0 || targetPosY != 0)) {
			if (isDragging()) {
				targetPosX = mouseX;
				targetPosY = mouseY;
			}

			double distanceX = Math.abs(targetPosX - posXSmoothed) / 8;
			double stepX = (distanceX) / 2 * (Client.DELTA_UTIL.deltaTime * .2);

			if (posXSmoothed < targetPosX) {
				if (targetPosX - posXSmoothed >= stepX) {
					posXSmoothed += stepX;
				} else {
					posXSmoothed = targetPosX;
				}
			} else if (posXSmoothed > targetPosX) {
				if (posXSmoothed - targetPosX >= stepX) {
					posXSmoothed -= stepX;
				} else {
					posXSmoothed = targetPosX;
				}
			}

			double distanceY = Math.abs(targetPosY - posYSmoothed) / 8;
			double stepY = (distanceY) / 2 * (Client.DELTA_UTIL.deltaTime * .2);

			if (posYSmoothed < targetPosY) {
				if (targetPosY - posYSmoothed >= stepY) {
					posYSmoothed += stepY;
				} else {
					posYSmoothed = targetPosY;
				}
			} else if (posYSmoothed > targetPosY) {
				if (posYSmoothed - targetPosY >= stepY) {
					posYSmoothed -= stepY;
				} else {
					posYSmoothed = targetPosY;
				}
			}

			if (Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {
				updatePositions(posXSmoothed, posYSmoothed);
			} else {
				updatePositions(mouseX, mouseY);
			}
		} else {
			posXSmoothed = mouseX;
			posYSmoothed = mouseY;
		}
	}

	double prevMouseX;
	double swingTargetX;
	double animatedSwingX;

	@Override
	public void drawElement(int mouseX, int mouseY) {
		double distance = Math.abs(swingTargetX - animatedSwingX) / 12;
		double step = (distance) / 2 * (Client.DELTA_UTIL.deltaTime * .2);
		
		swingTargetX = (mouseX - prevMouseX);
		swingTargetX = MathHelper.clamp_double(swingTargetX, -10, 10);
		
		if (isDragging() && Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {

			if (animatedSwingX < swingTargetX) {
				if (swingTargetX - animatedSwingX >= step) {
					animatedSwingX += step;
				} else {
					animatedSwingX = swingTargetX;
				}
			} else if (animatedSwingX > swingTargetX) {
				if (animatedSwingX - swingTargetX >= step) {
					animatedSwingX -= step;
				} else {
					animatedSwingX = swingTargetX;
				}
			}

			Client.RENDER2D.push();
			Client.RENDER2D.translate(getPosX() + startMouseX, getPosY() + startMouseY);
			Client.RENDER2D.rotate(0, 0, 1, animatedSwingX * Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow factor").getNumberValue());
			Client.RENDER2D.translate(-getPosX() - startMouseX, -getPosY() - startMouseY);
		} else {
			if (Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {
				if (Math.abs(animatedSwingX) < .01)
					animatedSwingX = 0;
				if (animatedSwingX > 0) {
					animatedSwingX *= 1 - (Client.DELTA_UTIL.deltaTime * .01);
					Client.RENDER2D.push();
					Client.RENDER2D.translate(getPosX() + startMouseX, getPosY() + startMouseY);
					Client.RENDER2D.rotate(0, 0, 1, animatedSwingX * Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow factor").getNumberValue());
					Client.RENDER2D.translate(-getPosX() - startMouseX, -getPosY() - startMouseY);
				} else if (animatedSwingX < 0) {
					animatedSwingX *= 1 - (Client.DELTA_UTIL.deltaTime * .01);
					Client.RENDER2D.push();
					Client.RENDER2D.translate(getPosX() + startMouseX, getPosY() + startMouseY);
					Client.RENDER2D.rotate(0, 0, 1, animatedSwingX * Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow factor").getNumberValue());
					Client.RENDER2D.translate(-getPosX() - startMouseX, -getPosY() - startMouseY);
				}
			}
		}

		if (isExtended()) {
			if (colorPicker != null) {
				colorPicker.updateElement(mouseX, mouseY);
				colorPicker.drawElement(mouseX, mouseY);
			}
			drawModules(mouseX, mouseY);
			Client.RENDER2D.rect(getPosX(), getPosY() + (colorPicker != null ? 100 + getHeight() : getHeight() + (getHeight() * getModules().size())), getWidth(), getHeight() / 3, Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDColoring").getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(0).addColoring(-60) : Client.INSTANCE.getClientColor().addColoring(-10));

		} else {
			Client.RENDER2D.rect(getPosX(), getPosY() + getHeight(), getWidth(), getHeight() / 3, Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDColoring").getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(0).addColoring(-60) : Client.INSTANCE.getClientColor().addColoring(-10));
			Client.RENDER2D.rect(getPosX(), getPosY() + getHeight(), getWidth(), 1, Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDColoring").getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(0).darker() : Client.INSTANCE.getClientColor().darker());
		}
		Client.RENDER2D.gradient(getPosX(), getPosY() + getHeight() - 5, getWidth(), getHeight(), Color.black, new Color(0, 0, 0, 0));
		Client.RENDER2D.rect(getPosX() - 2.5, getPosY(), getWidth() + 5, getHeight(), Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDColoring").getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(0) : Client.INSTANCE.getClientColor());
		getFontRenderer().drawStringWithShadow(getPanelTitle(), getPosX() + 5, getPosY() + getHeight() / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(getPanelTitle()) / 2, Color.white);

		Client.RENDER2D.push();
		final int size = 8;
		Client.RENDER2D.translate(getPosX() + getWidth() - size / 2 - 4, getPosY() + getHeight() - size / 2 - 2);
		Client.RENDER2D.rect(-size / 2 + 1, 0, size, 1, Color.black);
		Client.RENDER2D.rect(-size / 2 + .5, -.5, size, 1);
		Client.RENDER2D.rotate(0, 0, 1, rotation);
		Client.RENDER2D.rect(-size / 2 + .5, -.5, size, 1, Color.black);
		Client.RENDER2D.rect(-size / 2, -1, size, 1, Color.white);
		Client.RENDER2D.pop();

		if (isDragging() && Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {
			Client.RENDER2D.pop();
		} else {
			if (Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiFlow").getBooleanValue()) {
				if (animatedSwingX > 0) {
					Client.RENDER2D.pop();
				} else if (animatedSwingX < 0) {
					Client.RENDER2D.pop();
				}
			}
		}
		if (isExtended() && isHovered(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight() + (getHeight() * getModules().size()))) {
			if (getHoveredModule() != null && hoveringTicks > 1000) {
				tooltipAlpha = animUtil.easeOut(0, 0, 255, 1);
				tooltipAlpha = MathHelper.clamp_int((int) tooltipAlpha, 0, 255);
				String text = getHoveredModule().getModule().getDescription();
				Client.RENDER2D.rect(getPosX() + getWidth() + 5, mouseY - getFontRenderer().getHeight(text), getFontRenderer().getWidth(text) + 5, getFontRenderer().getHeight(text) * 2, new Color(20, 20, 20, (int) tooltipAlpha));
				getFontRenderer().drawStringWithShadow(text, (getPosX() + getWidth() + 5) + 2.5, mouseY - getFontRenderer().getHeight(text) / 2, new Color(255, 255, 255, (int) tooltipAlpha));
			}
		} else {
			setHoveredModule(null);
			animUtil.getProgression(0).setValue(0);
			tooltipAlpha = 0;
		}

		prevMouseX = mouseX;
	}

	public String getPanelTitle() {
		return panelTitle;
	}

	public int getClickIndex() {
		return clickIndex;
	}

	public ElementModule getHoveredModule() {
		return hoveredModule;
	}

	public boolean isExtended() {
		return extended;
	}

	public boolean isDragging() {
		return dragging;
	}

	public List<ElementModule> getModules() {
		return modules;
	}

	public void setPanelTitle(String panelTitle) {
		this.panelTitle = panelTitle;
	}

	public void setExtended(boolean isExtended) {
		this.extended = isExtended;
	}

	public void setHoveredModule(ElementModule hoveredModule) {
		this.hoveredModule = hoveredModule;
	}

	void addModules() {
		double modulePosY = getHeight();
		List<Module> modules = Client.INSTANCE.getModuleManager().getModulesByCategory(getPanelTitle());
		modules.sort(Comparator.comparingDouble(mod -> -Client.INSTANCE.getFontManager().getDefaultFont().getWidth(mod.getName())));
		for (Module module : modules) {
			ElementModule element = new ElementModule(getPosX(), getPosY() + modulePosY, getWidth(), getHeight(), this, module);
			element.setPosYOffset(modulePosY);
			this.modules.add(element);
			modulePosY += getHeight();
		}
	}

	void drawModules(int mouseX, int mouseY) {
		modules.forEach(module -> {
			module.updateElement(mouseX, mouseY);
			module.drawElement(mouseX, mouseY);
		});
	}

	public void updatePositions(double mouseX, double mouseY) {
		if (mouseX != -1 && mouseY != -1)
			setPosition(mouseX - startMouseX, mouseY - startMouseY);

		if (colorPicker != null) {
			colorPicker.setPosition(getPosX(), getPosY() + getHeight());
		}
		getModules().forEach(module -> {
			module.setPosition(getPosX(), getPosY() + module.getPosYOffset());
			module.updatePositions();
		});
	}

	double getWidesWidth() {
		double widestWidth = 0;
		for (Module module : Client.INSTANCE.getModuleManager().getModulesByCategory(getPanelTitle())) {
			double moduleWidth = module.getName().length() * 6;
			if (!(widestWidth > 0)) {
				widestWidth = moduleWidth + (module.getValues().size() > 0 ? 15 : 0);
			} else {
				if (moduleWidth > widestWidth) {
					widestWidth = moduleWidth + (module.getValues().size() > 0 ? 15 : 0);
				}
			}
		}
		return widestWidth;
	}

	public ElementModule getElementModuleByName(String moduleName) {
		for (ElementModule module : getModules()) {
			if (module.getModule().getName().equalsIgnoreCase(moduleName)) {
				return module;
			}
		}
		return null;
	}

	public ElementColorPicker getColorPicker() {
		return colorPicker;
	}
}
