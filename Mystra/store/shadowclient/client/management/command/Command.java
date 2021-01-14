package store.shadowclient.client.management.command;

public abstract class Command {
	
	private final String[] names;
    private final String description;
	
    public Command(String[] names, String description) {
        this.names = names;
        this.description = description;
    }
    
    public String getName() {
        return names[0];
    }
    
	public abstract String getAlias();
	public abstract String getDescription();
	public abstract String getSyntax();
	public abstract String executeCommand(String line, String[] args);	
}
