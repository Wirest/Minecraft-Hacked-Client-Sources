package nivia.files.modulefiles;

import nivia.Pandora;
import nivia.managers.FileManager.CustomFile;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.io.*;

public class Colors extends CustomFile{
    public Colors(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = variable9.readLine()) != null){
            int i = line.indexOf(":");
            if (i >= 0){
                String[] arguments = line.split(":");
                String module = line.substring(0, i).trim();
                    for (Module m : Pandora.getModManager().mods) {
                        if (module.equalsIgnoreCase(m.getName())) {
                            boolean sub = arguments[1].length() == 10;
                            int value = Integer.parseInt(arguments[1].substring(sub ? 4 : 2), 16);
                            try {
                                m.setColor(value);
                            } catch (Exception e){Logger.logChat("Check the values, one of them is not in proper format.");}
                        }
                    }
            }
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }
    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (Module m : Pandora.getModManager().mods) {
            variable9.println(m.getName() + ":" + "0x" + Integer.toHexString(m.getColor()));
        }
        variable9.close();
    }
}
