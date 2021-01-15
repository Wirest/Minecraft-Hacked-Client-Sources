package nivia.files;

import nivia.gui.altmanager.Alt;
import nivia.gui.altmanager.AltManager;
import nivia.managers.FileManager.CustomFile;

import java.io.*;

/**
 * Created by JohnSwagster on 7/07/2015.
 */
public class Alts extends CustomFile{
    public Alts(String name, boolean Module, boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }
    @Override
    public void loadFile() throws IOException {  
 		   BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
 		   String line;
 		  while ((line = variable9.readLine()) != null) {
 		      String[] arguments = line.split(":");
 		      for(int i = 0; i < 2; i++)
                  arguments[i].replace(" ", "");
 		      if (arguments.length > 2) 		    	
 		        AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2]));		    	
 		      else 
 		    	AltManager.registry.add(new Alt(arguments[0], arguments[1]));		      		 
 		    }	    
 	    variable9.close();
 	    System.out.println("Loaded " + this.getName() + " File!");	    	     	             
 	  }
 	  @Override
 	  public void saveFile() throws IOException{		 
 	            PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
 	            AltManager.registry.forEach(a -> {
 	 	              if (a.getMask().equals(""))
 	  	                alts.println(a.getUsername() + ":" + a.getPassword());
 	  	               else
 	  	                alts.println(a.getUsername() + ":" + a.getPassword() + ":" + a.getMask());
 	            });
 	           alts.close();	       
 	  }
}
