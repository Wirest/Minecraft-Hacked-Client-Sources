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

import java.util.ArrayList;
import java.util.List;

public class CommandModule extends Command {

    public CommandModule(String base, List<String> usages) {
        super(base, usages);
    }

    public void execute(String[] args) throws CommandException {
        Module module = Module.get(base);
        if (args.length == 0) {
            module.toggle();
            ChatUtil.send(String.format("[n]%s[t]: [v]%s", module.name, module.active ? "On" : "Off"));
            return;
        } else if (args.length == 2) {
            Option option = null;
            for (String key : module.options.keySet()) {
                if (key.equalsIgnoreCase(args[0])) {
                    option = module.options.get(key);
                    break;
                }
            }
            if (option != null) {
                try {
                    setOptionValue(option, args[1]);
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
        } else if (args.length == 3) {
            Option option = null;
            for (String key : module.options.keySet()) {
                if (key.equalsIgnoreCase(args[0])) {
                    Option temp = module.options.get(key);
                    boolean is = false;
                    for (String key2 : temp.options.keySet()) {
                        if (key2.equalsIgnoreCase(args[1])) {
                            option = temp.options.get(key2);
                            is = true;
                            break;
                        }
                    }
                    if (is) {
                        break;
                    }
                }
            }
            if (option != null) {
                try {
                    setOptionValue(option, args[2]);
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

    private void setOptionValue(Option option, String arg) throws Exception {
        Object value = null;
        String message = String.format("[v]%s [t]set to [v]%s", option.name, arg);
        switch (option.type) {
            case BOOLEAN:
                try {
                    if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
                        value = Boolean.parseBoolean(arg);
                    } else {
                        throw new ArgumentException();
                    }
                } catch (Exception e) {
                    throw new ArgumentException();
                }
                break;
            case CHOICE:
                int is = -1;
                String[] list = ((ValueChoice) option.value).list;
                for (int i = 0; i < list.length; i++) {
                    if (list[i].equalsIgnoreCase(arg)) {
                        is = i;
                    }
                }
                if (is != -1) {
                    value = list[is];
                } else {
                    throw new ArgumentException();
                }
                break;
            case KEYBIND:
                value = arg.toUpperCase();
                break;
            case NUMBER:
                try {
                    value = Double.parseDouble(arg);
                } catch (Exception e) {
                    throw new ArgumentException();
                }
                break;
            case OTHER:
                value = arg;
                break;
            case STRING:
                value = arg;
                break;
            case LIST:
                List<String> l = new ArrayList();
                l.addAll((List<String>) option.value.value);

                if (l.contains(arg)) {
                    l.remove(arg);
                    message = String.format("[v]%s [t]removed from [v]%s", arg, option.name);
                } else {
                    l.add(arg);
                    message = String.format("[v]%s [t]added to [v]%s", arg, option.name);
                }

                value = l;
                break;
        }

        if (value != null) {
            option.value.value = value;
            ChatUtil.send(message);
        }
    }
}
