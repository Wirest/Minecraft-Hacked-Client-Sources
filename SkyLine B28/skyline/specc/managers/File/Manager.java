package skyline.specc.managers.File;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.util.file.FileUtils;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.util.file.KeyBind;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.SkyLine;

public class Manager<T> {

    private List<T> contents = new ArrayList<T>();
    private ManagerFileHandler fileHandler = null;

    public List<T> getContents() {
        return contents;
    }

    public void addContent(T content) {
        this.contents.add(content);
    }

    public void removeContent(T content) {
        this.contents.remove(content);
    }

    public boolean hasContent(T content) {
        return this.contents.contains(content);
    }

    public void setFileHandler(ManagerFileHandler fileHandler, String fileName) {
        this.fileHandler = fileHandler;
    }

    private static final File MODULE_DIR = FileUtils.getConfigFile("Modules");
    private static final File SETS = FileUtils.getConfigFile("Settings");

    public void save() {
        final List<String> fileContent = new ArrayList();
        for (final Module module : SkyLine.getManagers().mods().getContents()) {
            final String moduleName = module.getName();
            final String visible = Boolean.toString(module.getData().isVisible());
            final String moduleKey = Integer.toString(module.getData().getKeyBind().getKey());
            final String enabled = Boolean.toString(module.getState());
            fileContent.add(String.format("%s:%s:%s:%s", moduleName, visible, moduleKey, enabled));
        }
        Manager.saveSettings();
        FileUtils.write(MODULE_DIR, fileContent, true);
//        System.out.println("Saving Module Data!");
    }

    public static void saveSettings() {
        final List<String> fileContent = new ArrayList();
        for (final Module module : SkyLine.getManagers().mods().getContents()) {
            for (Value value : module.getData().getValues()) {
                fileContent.add(String.format("%s:%s:%s:%s", module.getName(), value.getName(), value.getValue(), value.getGenericClass()));
            }
        }
        FileUtils.write(SETS, fileContent, true);
//        System.out.println("Saving Module Settings!");
    }


    public void load() {
        try {
            final List<String> fileContent = FileUtils.read(MODULE_DIR);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                final String id = split[0];
                final String shown = split[1];
                final String keybind = split[2];
                final String strEnabled = split[3];
                final Module module = getModule(id);
                final boolean enabled = Boolean.parseBoolean(strEnabled);
                final boolean visible = Boolean.parseBoolean(shown);
                module.setState(enabled);
                module.getData().setVisible(visible);
                module.getData().setKeybind(new KeyBind(module.getName(), Integer.parseInt(keybind)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Loading Module Data!");
        loadSettings();
    }


    public void loadSettings() {
        try {
            final List<String> fileContent = FileUtils.read(SETS);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                String moduleName = split[0];
                Module module = SkyLine.getManagers().getModuleManager().getModule(moduleName);
                String valueName = split[1];
                String setValue = split[2];
                String valueType = split[3];
                if (valueName.equalsIgnoreCase("mode")) {
                    for (ModMode mode : module.getModes()) {
                        if (mode.getName().equalsIgnoreCase(setValue)) {
                            module.setMode(mode);
                        }
                    }
                } else {
                    for (Value value : module.getData().getValues()) {
                        if (value.getName().equalsIgnoreCase(valueName)) {
                            if (valueType.contains("Float")) {
                                value.setValue(Float.parseFloat(setValue));
                            } else if (valueType.contains("Double")) {
                                value.setValue(Double.parseDouble(setValue));
                            } else if (valueType.contains("Boolean")) {
                                value.setValue(Boolean.valueOf(setValue));
                            } else if (valueType.contains("Integer")) {
                                value.setValue(Integer.parseInt(setValue));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
//            System.out.println("Loading Module Settings!");
            e.printStackTrace();
        }
    }



    public Module getModule(String moduleName) {
        for (Module module : SkyLine.getManagers().getModuleManager().getContents()) {
            if (module.getName().equalsIgnoreCase(moduleName)) {
                return module;
            }
        }
        return null;
    }


    public ManagerFileHandler getFileHandler() {
        return this.fileHandler;
    }

    public boolean usesFileHandler() {
        return this.fileHandler != null;
    }


}