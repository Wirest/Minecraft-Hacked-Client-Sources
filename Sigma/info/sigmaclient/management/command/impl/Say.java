package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.misc.ChatUtil;

public class Say extends Command {

    public Say(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            return;
        }
        if (args.length > 0) {
            StringBuilder out = new StringBuilder();
            for (String word : args) {
                out.append(word + " ");
            }
            // TODO: Find a fix for sendChat(s) duplicating the message
            String message = out.substring(0, out.length() - 1);
            message = message.replaceAll("&", "\u00A7");
            ChatUtil.printChat(message);
        }
    }

    @Override
    public String getUsage() {
        return "Say <Message>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
