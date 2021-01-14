package com.mentalfrostbyte.jello.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
public class Modules
extends SettingsFile.CustomSettingsFile {
    public Modules(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    public void loadFile()
    	    throws IOException
    	  {
    	
    	
    	List<String> fileContent = FileUtils.read(this.getFile());
		for (String line : fileContent) {
			try {
				String[] split = line.split(":");
				String name = split[0];
				String keyCode = split[1];
				String toggled = split[2];
				int key = Integer.parseInt(keyCode);
				for (Module m : Jello.getModules()) {
					if (!name.equalsIgnoreCase(m.getName()))
						continue;
					m.keyCode = key;
					if (!toggled.equalsIgnoreCase("true") || m.isToggled())
						continue;
					m.toggle();
				}
				continue;
			} catch (Exception split) {
				// empty catch block
			}
		}
    	
       
    	  }

    public void saveFile()
    	    throws IOException
    	  {
    	
    		ArrayList<String> fileContent = new ArrayList<String>();
    		for (Module m : Jello.getModules()) {
    			fileContent.add(String.valueOf(m.getName()) + ":" + m.getKeyCode() + ":" + m.isToggled());

    		}
    		
    		FileUtils.write(getFile(), fileContent, true);
    	
    	
    	  }
}

