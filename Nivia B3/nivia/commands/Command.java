package nivia.commands;

import net.minecraft.client.Minecraft;

public abstract class Command {

    protected final Minecraft mc = Minecraft.getMinecraft();
    private String name, description, errorMessage;
    private String[] aliases;
    private boolean commandModule;
    
    public Command(String name, String description, String errorMessage, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.errorMessage = errorMessage;
        this.commandModule = true;
    }
    public Command(String name, String description, String errorMessage, boolean nonModule ,String... aliases) {
        this.name = name;
        this.errorMessage = errorMessage;
        this.description = description;
        this.aliases = aliases;
        this.commandModule = nonModule;
    }
    
    public String getName(){
    	return name;
    }
    public String getDescription(){
    	return this.description;
    }
    public boolean isModuleCommand(){
    	return this.commandModule;
    }
    public String getError(){
    	return this.errorMessage;
    }
    public String[] getAliases() {
        return this.aliases;
    }

    /**
     * @Author Anodise
     */
    public abstract void execute(String commandName ,String[] arguments);
}
