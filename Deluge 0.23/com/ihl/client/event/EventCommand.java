package com.ihl.client.event;

import com.ihl.client.commands.Command;

public class EventCommand extends Event {

    public Command command;
    public String[] args;

    public EventCommand(Type type, Command command, String[] args) {
        super(type);
        this.command = command;
        this.args = args;
    }

}
