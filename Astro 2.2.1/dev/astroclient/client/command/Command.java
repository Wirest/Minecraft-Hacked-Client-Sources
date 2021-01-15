package dev.astroclient.client.command;

public class Command {

    public String[] getUsages() {
        return usages;
    }

    private String[] usages;

    public String getDescription() {
        return description;
    }

    private String description;

    public Command(String[] usages, String description) {
        this.usages = usages;
        this.description = description;
    }

    public void execute(String[] args) {
    }

}
