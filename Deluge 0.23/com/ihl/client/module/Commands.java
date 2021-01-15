package com.ihl.client.module;

import com.ihl.client.commands.Command;
import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.commands.exceptions.CommandException;
import com.ihl.client.commands.exceptions.SyntaxException;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventChatSend;
import com.ihl.client.event.EventHandler;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueString;
import com.ihl.client.util.ChatUtil;

@EventHandler(events = {EventChatSend.class})
public class Commands extends Module {

    public Commands(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("prefix", new Option("Prefix", "Chat command prefix", new ValueString("."), Option.Type.STRING));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        String prefix = Option.get(options, "prefix").STRING();

        if (event instanceof EventChatSend) {
            EventChatSend e = (EventChatSend) event;
            if (e.type == Event.Type.PRE) {
                String message = e.message;
                if (message.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
                    message = message.substring(prefix.length(), message.length());
                    String[] args = message.split(" ");
                    String base = args[0].toLowerCase();
                    args = Command.dropFirst(args);

                    Command.run(base, args);

                    e.cancel();
                }
            }
        }
    }

}
