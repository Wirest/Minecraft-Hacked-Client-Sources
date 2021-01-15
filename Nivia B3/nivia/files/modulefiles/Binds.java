package nivia.files.modulefiles;

import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.managers.FileManager.CustomFile;
import nivia.modules.Module;

import java.io.*;

public class Binds extends CustomFile{
    public Binds(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {   	
    	BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
    	String line;
    	while ((line = variable9.readLine()) != null){
    		int i = line.indexOf(":");
    		if (i >= 0){
				final String[] arguments = line.split(":");
    			String module = arguments[0];
    			String key = arguments[1];
    			Module m = Pandora.getModManager().getModbyName(module);
                if(!key.isEmpty() && m != null)
    			    m.setKeybind(Keyboard.getKeyIndex(key.toUpperCase()));
    		}
    	}                       
    	variable9.close();
    	System.out.println("Loaded " + this.getName() + " File!");        
    }
    @Override
    public void saveFile() throws IOException {        
    	PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
    	for (Module m : Pandora.getModManager().mods) {
    		variable9.println(m.getName() + ":" + Keyboard.getKeyName(m.getKeybind()));
    	}
    	variable9.close();          
    }
}
