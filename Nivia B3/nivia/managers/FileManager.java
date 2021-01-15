	package nivia.managers;


    import net.minecraft.client.Minecraft;
    import net.minecraft.util.EnumChatFormatting;
    import net.minecraft.util.Util;
    import nivia.Pandora;
    import nivia.files.Alts;
    import nivia.files.Friends;
    import nivia.files.Misc;
    import nivia.files.modulefiles.*;
    import nivia.managers.PropertyManager.DoubleProperty;
    import nivia.managers.PropertyManager.Property;
    import nivia.modules.ModuleMode;
    import nivia.modules.movement.Jesus;
    import nivia.modules.movement.Speed;
    import nivia.modules.movement.Step;
    import nivia.modules.render.ESP;
    import nivia.utils.Logger;

    import java.io.File;
    import java.io.IOException;
    import java.util.ArrayList;

public class FileManager {
    public static ArrayList<CustomFile> Files = new ArrayList<CustomFile>();
    private static final String xd = Util.getOSType() == Util.EnumOS.LINUX ? "//" : "\\";
    private static File directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + xd + Pandora.getClientName());
    private static File moduleDirectory = new File(Minecraft.getMinecraft().mcDataDir.toString() + xd + Pandora.getClientName() + xd + "Modules");
    public FileManager(){
    	makeDirectories();
        Files.add(new Friends("Friends", false, true));
    	Files.add(new Alts("alts", false, true));
    	Files.add(new Modules("Modules", true , false));
    	Files.add(new Binds("Binds", true ,true));
        Files.add(new Movement("Movement", true, true));
    	Files.add(new Exploits("Exploits", true, true));
        Files.add(new Player("Player", true, true));
    	Files.add(new Render("Render", true, true));
    	Files.add(new Miscellaneous("Miscellaneous", true,true));
        Files.add(new Misc("Misc", false, true));
        Files.add(new Waypoints("waypoints", true,true));
        Files.add(new Combat("Combat", true, true));
        Files.add(new Colors("colors", true, true));
        Files.add(new Ghost("Ghost", true, true));
    }
    public void loadFiles(){
    	Files.forEach(f -> {
    		try {
    			if (f.loadOnStart())
    				f.loadFile();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    }
    public void saveFiles(){
    	Files.forEach(f -> {
    		try {
    			f.saveFile();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    }
    public CustomFile getFile(Class<? extends CustomFile> clazz){
		return Files.stream().filter(f -> f.getClass() == clazz).findFirst().orElse(null);	
    }
    public void makeDirectories(){
    	if (!directory.exists()){
    		if (directory.mkdir()) 
    			System.out.println("Directory is created!");
    		else 
    			System.out.println("Failed to create directory!");			
		}
    	if (!moduleDirectory.exists()){
    		if (moduleDirectory.mkdir()) 
    			System.out.println("Directory is created!");
    		else 
    			System.out.println("Failed to create directory!");			
		}
    }
    public static abstract class CustomFile {
        private final java.io.File file;
        private final String name;
        private boolean load;
        public CustomFile(String name, boolean Module, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            if(Module) file = new java.io.File(moduleDirectory, name + ".txt");
            else file = new java.io.File(directory, name + ".txt");
            if (!file.exists()){
            	 try { saveFile(); } catch(Exception e){ e.printStackTrace(); }
            }
        }
        public <T> T getValue(T value) {
			return null;
        }
        public void setValue(Property p, String value) {
        	if(p instanceof DoubleProperty){     
        		((DoubleProperty) p).setValue(Double.valueOf(value));
                return;
        	}  
            if (p.value instanceof Integer)
                p.value = (Integer.valueOf(value));
            else if (p.value instanceof Boolean)
                p.value = (Boolean.valueOf(value));
            else if (p.value instanceof Double)
                p.value = (Double.valueOf(value));
            else if (p.value instanceof Float)
                p.value = (Float.valueOf(value));
            else if (p.value instanceof Long)
                p.value = (Long.parseLong(value));
            else if (p.value instanceof String)
                p.value = value;
            else if (p.value instanceof Speed.speedMode)
                p.value = (Speed.speedMode.valueOf(value));
            else if (p.value instanceof Step.StepMode)
                p.value = (Step.StepMode.valueOf(value));
            else if (p.value instanceof ESP.ESPMode)
                p.value = (ESP.ESPMode.valueOf(value));     
            else if(p.value instanceof Jesus.JesusMode)
               p.value = Jesus.JesusMode.valueOf(value);
            else if(p.value instanceof ModuleMode) {
                Logger.logChat(((ModuleMode) p.value).getName() + " " + p.getModule().getMode(value) + " " + value + "YO<--------------------------------------------------------");
                p.value = p.getModule().getMode(value);
                if(p.value == null) {
                    Logger.logChat(EnumChatFormatting.RED + "Grats you broke it bitch ass shit dev");
                    p.reset();
                }
            }
            else p.reset();
        }
        public final java.io.File getFile() {
            return file;
        }
        private boolean loadOnStart(){
        	return load;
        }
        public final String getName() {
            return name;
        }
        public abstract void loadFile() throws IOException;
        public abstract void saveFile() throws IOException;
            
    }   
}
