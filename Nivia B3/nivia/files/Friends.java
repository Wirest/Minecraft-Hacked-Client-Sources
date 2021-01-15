package nivia.files;

import nivia.managers.FileManager.CustomFile;
import nivia.managers.FriendManager;

import java.io.*;

public class Friends extends CustomFile{
    public Friends(String name, boolean Module, boolean loadOnStart) {
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
                            String name = line.substring(0, i).trim();
                            String alias = line.substring(i + 1).trim();
                           FriendManager.addFriend(name, alias);
                            
                        }
                    }               
            }
            System.out.println("Loaded " + this.getName() + " File!");
            variable9.close();       
    }
    @Override
    public void saveFile() throws IOException { 
    	PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
    	FriendManager.friends.forEach(f -> variable9.println(f.getName() + ":" + f.getAlias()));
            variable9.close();          
    }
}
