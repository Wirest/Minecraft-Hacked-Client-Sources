package de.iotacb.client.file.files;

import de.iotacb.client.Client;
import de.iotacb.client.file.ClientFile;
import de.iotacb.client.gui.click.GuiClick;
import de.iotacb.client.gui.click.elements.ElementModule;
import de.iotacb.client.gui.click.elements.ElementPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class ClickGuiFile extends ClientFile {

	public ClickGuiFile(String path) {
		super(path);
	}
	
	public void saveClick() {
		final GuiClick click = Client.INSTANCE.getClickGui();
		final StringBuilder content = new StringBuilder();
		for (ElementPanel panel : click.getPanels()) {
			content.append(panel.getPanelTitle().concat("#").concat(String.valueOf(panel.isExtended()).concat(":").concat(String.valueOf(panel.getPosX()).concat("'").concat(String.valueOf(panel.getPosY()).concat("\n")))));
			for (ElementModule module : panel.getModules()) {
				content.append("-".concat(panel.getPanelTitle()).concat(":").concat(module.getModule().getName()).concat("#").concat(String.valueOf(module.isExtended()).concat("\n")));
			}
			if (panel.getColorPicker() != null) {
				content.append("+Color:".concat(String.valueOf(panel.getColorPicker().getHueBarDotX()).concat("'").concat(String.valueOf(panel.getColorPicker().getHueBarDotY()).concat("#").concat(String.valueOf(panel.getColorPicker().getColorFieldDotX()).concat("'").concat(String.valueOf(panel.getColorPicker().getColorFieldDotY()).concat("\n"))))));
			}
		}
		saveFile(content.toString());
	}
	
	public void readClick() {
		if (Minecraft.getMinecraft().thePlayer == null) return;
		final String content = loadFile();
		if (content.isEmpty()) return;
		final GuiClick click = Client.INSTANCE.getClickGui();
		final String[] lines = content.split("\n");
		for (String line : lines) {
			if (line.startsWith("-")) {
				if (!line.contains(":")) {
					continue;
				}
				final String panelTitle = line.substring(1).split(":")[0];
				final ElementPanel panel = click.getPanelByTitle(panelTitle);
				if (panel != null) {
					if (!line.contains("#")) {
						continue;
					}
					final String moduleName = line.split(":")[1].split("#")[0];
					final ElementModule module = panel.getElementModuleByName(moduleName);
					if (module != null) {
						if (!line.contains("#")) {
							continue;
						}
						final String moduleExtended = line.split("#")[1];
						module.setExtended(Boolean.valueOf(moduleExtended));
					}
				}
			} else if (line.startsWith("+")) {
				if (!line.contains(":")) {
					continue;
				}
				final String panelTitle = line.substring(1).split(":")[0];
				final ElementPanel panel = click.getPanelByTitle(panelTitle);
				if (panel != null) {
					if (!line.contains("#")) {
						continue;
					}
					final String hueDotPosition = line.split(":")[1].split("#")[0];
					final String colorFieldDotPosition = line.split("#")[1];
					
					if (!line.contains("'")) {
						continue;
					}
					final double hueDotX = Double.valueOf(hueDotPosition.split("'")[0]);
					final double hueDotY = Double.valueOf(hueDotPosition.split("'")[1]);
					
					final double colorFieldDotX = Double.valueOf(colorFieldDotPosition.split("'")[0]);
					final double colorFieldDotY = Double.valueOf(colorFieldDotPosition.split("'")[1]);
					
					panel.getColorPicker().setDotPosition(hueDotX, hueDotY, colorFieldDotX, colorFieldDotY);
				}
			} else {
				if (!line.contains("#")) {
					continue;
				}
				final String panelTitle = line.split("#")[0];
				final ElementPanel panel = click.getPanelByTitle(panelTitle);
				if (panel != null) {
					if (!line.contains(":")) {
						continue;
					}
					final String panelExtended = line.split("#")[1].split(":")[0];
					final String panelPosX = line.split(":")[1].split("'")[0];
					final String panelPosY = line.split(":")[1].split("'")[1];
					
					final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

					final Double posX = MathHelper.clamp_double(Double.valueOf(panelPosX), 0, sr.getScaledWidth() - 100);
					final Double posY = MathHelper.clamp_double(Double.valueOf(panelPosY), 0, sr.getScaledHeight() - 100);
					
					panel.setExtended(Boolean.valueOf(panelExtended));
					panel.setPosition(posX, posY);
					panel.updatePositions(-1, -1);
				}
			}
			
		}
	}

}
