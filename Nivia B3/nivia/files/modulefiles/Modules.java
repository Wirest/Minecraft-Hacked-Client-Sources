package nivia.files.modulefiles;

import nivia.Pandora;
import nivia.managers.FileManager.CustomFile;
import nivia.modules.Module;
import nivia.modules.render.ESP;

import java.io.*;


public class Modules extends CustomFile{
    public Modules(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = variable9.readLine()) != null) {
                int i = line.indexOf(":");
                if (i >= 0) {
                    String[] arguments = line.split(":");
                    if (arguments.length != 4) {
                        System.out.println("Changed modules, adjusting values.");
                        continue;
                    }
                    String module = arguments[0];
                    Module m = Pandora.getModManager().getModbyName(module);
                    if (Boolean.valueOf(arguments[1]) && !m.getState() && (m != Pandora.getModManager().getModule(ESP.class)))
                        m.Toggle();
                    m.setVisible(Boolean.valueOf(arguments[2]));
                    m.setTag(arguments[3]);
                }
            }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }
    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (Module m : Pandora.getModManager().mods) {
            variable9.println(m.getName() + ":" + m.getState() + ":" + m.isVisible() + ":" + m.getTag());
        }
        variable9.close();
    }
}
