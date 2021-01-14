package store.shadowclient.client.management.command.variables;

public class Friend {
    private final String username;

    private String alias;

    public Friend(String username, String alias) {
        this.username = username;
        this.alias = alias;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}