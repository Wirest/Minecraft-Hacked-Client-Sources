package nivia.files;

import nivia.Pandora;
import nivia.managers.FileManager.CustomFile;

import java.io.*;

public class Misc extends CustomFile{
    public Misc(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        {
            String line;
            while ((line = variable9.readLine()) != null){
                int i = line.indexOf(":");
                if (i >= 0){
                    String value = line.substring(i + 1).trim();
                    Pandora.prefix = value;
                }
            }
        }
        System.out.println("Loaded " + this.getName() + " File!");
        variable9.close();
    }
    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        variable9.println("Prefix" + ":" + Pandora.prefix);
        variable9.close();
    }
}
