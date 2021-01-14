package de.iotacb.client.file.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.iotacb.client.Client;
import de.iotacb.client.file.ClientFile;
import de.iotacb.client.gui.click.elements.values.ElementCombo;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueType;

public class ConfigFile extends ClientFile {

	public ConfigFile(String path) {
		super(path);
	}
	
	public boolean saveConfig() {
		String content = "";
		for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
			content += module.getName() + "~" + module.isEnabled() + "\n";
			for (Value value : module.getValues()) {
				content += module.getName() + ":" + value.getValueName() + ":" + value.getValue() + "\n";
			}
		}
		return saveFile(content);
	}
	
	public boolean saveConfig(String content) {
		return saveFile(content);
	}
	
	public boolean readConfig(boolean loadModules) {
		final String content = loadFile();
		loadConfig(content, true, loadModules);
		return true;
	}
	
	public boolean readConfig(boolean loadRender, boolean loadModules) {
		final String content = loadFile();
		loadConfig(content, loadRender, loadModules);
		return true;
	}
	
	public static boolean loadConfig(String config, boolean loadRender, boolean loadModules) {
		if (config.isEmpty()) return false;
		final String[] configLines = config.split("\n");
		for (String configLine : configLines) {
			if (configLine.contains("~") && loadModules) {
				final String moduleName = configLine.split("~")[0];
				
				if (moduleName.isEmpty()) continue;
				
				final Module module = Client.INSTANCE.getModuleManager().getModuleByName(moduleName);
				
				if (module == null) continue;
				
				try {
					final boolean enabled = Boolean.valueOf(configLine.split("~")[1]);
					if (module.isEnabled()) {
						if (!enabled) {
							module.setEnabled(false);
						}
					} else {
						if (enabled) {
							module.setEnabled(true);
						}
					}
				} catch (Exception e) {}
				continue;
			}
			if (!configLine.contains(":")) {
				continue;
			}
			final String[] line = configLine.split(":");
			
			final String moduleName = line[0];
			
			if (moduleName.isEmpty()) continue;
			
			final Module module = Client.INSTANCE.getModuleManager().getModuleByName(moduleName);
			
			if (module == null) continue;
			
			if (module.getCategory() == Category.RENDER && !loadRender) continue;
			
			final String valueName = line[1];
			
			if (valueName.isEmpty()) continue;
			
			final Value value = module.getValueByName(valueName);
			
			if (value == null) continue;
			
			final String valueValue = line[2];
			
			if (valueValue.isEmpty()) continue;
			
			Double doubleValue = null;
			Boolean booleanValue = null;
			String comboValue = null;
			
			try {
				if (valueValue.toLowerCase().contains("d")) {
					throw new Exception();
				} else {
					doubleValue = Double.valueOf(valueValue);
				}
			} catch (Exception e) {
				try {
					if (valueValue.equalsIgnoreCase("true") || valueValue.equalsIgnoreCase("false")) {
						booleanValue = Boolean.valueOf(valueValue);
					} else {
						throw new Exception();
					}
				} catch (Exception e2) {
					comboValue = valueValue;
				}
			}
			
			if (comboValue != null) {
				value.setComboValue(comboValue);
			} else {
				if (booleanValue != null) {
					value.setBooleanValue(booleanValue);
				} else {
					if (doubleValue != null) {
						value.setNumberValue(doubleValue);
					} else {
						Client.PRINTER.printMessage("Skipping setting '§5" + valueName + "§f'");
						continue;
					}
				}
			}
		}
		return true;
	}

}
