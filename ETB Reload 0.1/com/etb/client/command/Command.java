package com.etb.client.command;


public class Command {
    private String label;
    private String[] handles;
    private String description;

    public Command(String label, String[] handles, String description) {
        this.label = label;
        this.handles = handles;
        this.description = description;
    }

    public String[] getHandles() {
        return handles;
    }

    public String getDescription() {
        return description;
    }

    public void onRun(final String[] s) {
    }

    public String getLabel() {
        return this.label;
    }
}
