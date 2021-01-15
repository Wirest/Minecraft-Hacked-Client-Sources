package com.ihl.client.commands;

import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.commands.exceptions.CommandException;
import com.ihl.client.commands.exceptions.SyntaxException;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventCommand;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.part.Settings;

import java.util.ArrayList;
import java.util.List;

public class CommandOption extends Command {

    public CommandOption(String base, List<String> usages) {
        super(base, usages);
    }

    public void execute(String[] args) throws CommandException {
        Option option = Option.get(Settings.options, base);
        if (args.length == 1) {
            if (option != null) {
                try {
                    Option.setOptionValue(option, args[0]);
                } catch (Exception e) {
                    if (e instanceof ArgumentException) {
                        throw new ArgumentException();
                    } else if (e instanceof SyntaxException) {
                        throw new SyntaxException();
                    } else {
                        throw new CommandException();
                    }
                }
            } else {
                throw new ArgumentException();
            }
        } else if (args.length == 2) {
            Option subOption = null;
            for (String key : option.options.keySet()) {
                if (key.equalsIgnoreCase(args[0])) {
                    subOption = option.options.get(key);
                    break;
                }
            }
            if (subOption != null) {
                try {
                    Option.setOptionValue(subOption, args[1]);
                } catch (Exception e) {
                    if (e instanceof ArgumentException) {
                        throw new ArgumentException();
                    } else if (e instanceof SyntaxException) {
                        throw new SyntaxException();
                    } else {
                        throw new CommandException();
                    }
                }
            } else {
                throw new ArgumentException();
            }
        } else {
            throw new SyntaxException();
        }

        EventCommand e2 = new EventCommand(Event.Type.PRE, this, args);
        Module.event(e2, false);
    }
}
