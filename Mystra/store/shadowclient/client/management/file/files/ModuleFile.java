package store.shadowclient.client.management.file.files;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.file.ClientFile;
import store.shadowclient.client.module.Module;

public class ModuleFile extends ClientFile {

	public ModuleFile(String path) {
		super(path);
	}
	
	public void saveModules() {
		String content = "";
		for (Module module : Shadow.instance.moduleManager.getModules()) {
			String key = module.isMultiBinded() ? "[" + module.getMultiBindKey() + "," + module.getKey() + "]" : ""+module.getKey();
			content += module.getName() + ":" + key + ":" + module.isToggled() + "\n";
		}
		saveFile(content);
	}
	
	public void readModules() {
		final String content = loadFile();
		if (content.isEmpty()) return;
		final String[] lines = content.split("\n");
		for (String line : lines) {
			if (line.contains(":") && line.split(":").length > 2) {
				final String name = line.split(":")[0];
				String key = line.split(":")[1];
				final Module module = Shadow.instance.moduleManager.getModuleByName(name);
				final boolean enabled = Boolean.valueOf(line.split(":")[2]); 
				if (key.contains("[")) {
					key = key.substring(1, key.length() - 1);
					final int mKey = Integer.valueOf(key.split(",")[0]);
					final int nKey = Integer.valueOf(key.split(",")[1]);
					if (module != null) {
						module.setKey(nKey);
					}
				} else {
					final int nKey = Integer.valueOf(key);
					if (module != null) {
						module.setKey(nKey);
					}
				}
			}
		}
	}

}
