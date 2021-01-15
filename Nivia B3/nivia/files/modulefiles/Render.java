package nivia.files.modulefiles;

import nivia.Pandora;
import nivia.managers.FileManager.CustomFile;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module.Category;
import nivia.modules.ModuleMode;
import nivia.utils.Logger;

import java.io.*;


public class Render extends CustomFile{
    public Render(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = variable9.readLine()) != null) {
            int i = line.indexOf(":");
            if (i >= 0) {
                final String[] arguments = line.split(":");
                String modName = arguments[0];
                String pName = arguments[1];
                String value = arguments[2];
                for (Property p : PropertyManager.properties) {
                    if (p.getModule() == null) continue;
                    if (p.value == null) continue;
                    if (p.getModule().getCategory().equals(Category.RENDER)) {
                        if (p.getModule().getName().equalsIgnoreCase(modName) && p.getName().equalsIgnoreCase(pName))
                            setValue(p, value);
                    }
                }
            }
        }
        System.out.println("Loaded " + this.getName() + " File!");
        variable9.close();
    }
    @Override
    public void saveFile() throws IOException {

        BufferedWriter variable9 = new BufferedWriter(new FileWriter(this.getFile(), false));
    	for (Property p : Pandora.getPropertyManager().properties) {
            if(p.getModule() == null)
                continue;
            if (p.getModule().getCategory() == Category.RENDER) {
                if(p instanceof PropertyManager.DoubleProperty)
                    variable9.write(p.getModule().getName() + ":" + p.getName()+ ":" + ((PropertyManager.DoubleProperty) p).getValue());
                else if(p.value instanceof ModuleMode) {
                    variable9.write(p.getModule().getName() + ":" + p.getName() + ":" + ((ModuleMode) p.value).getName());
                } else
                    variable9.write(p.getModule().getName() + ":" + p.getName()+ ":" + p.value);
                variable9.newLine();
            }
    	}
    	variable9.close();     
    }
}
