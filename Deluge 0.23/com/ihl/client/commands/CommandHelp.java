package com.ihl.client.commands;

import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.commands.exceptions.CommandException;
import com.ihl.client.commands.exceptions.SyntaxException;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.util.ChatUtil;

import java.util.List;

public class CommandHelp extends Command {

    public CommandHelp(String base, List<String> usages) {
        super(base, usages);
    }

    public void execute(String[] args) throws CommandException {
        if (args.length == 0) {
            ChatUtil.send("%n%Commands:");
            ChatUtil.send(String.format("[v]%s[n]help [module] [option] [sub-option]", Option.get(Module.get("commands").options, "prefix").STRING()));
            ChatUtil.send(String.format("[v]%s[n]login <username> [password]", Option.get(Module.get("commands").options, "prefix").STRING()));
            ChatUtil.send(String.format("[v]%s[n]<option> [option <value>]", Option.get(Module.get("commands").options, "prefix").STRING()));
            ChatUtil.send(String.format("[v]%s[n]<module> [option <value>] [sub-option <value>]", Option.get(Module.get("commands").options, "prefix").STRING()));
        } else if (args.length == 1) {
            Module module = Module.get(args[0]);
            if (module != null) {
                ChatUtil.send(String.format("[n]%s [t]Description: [v]%s", module.name, module.desc));
            } else {
                throw new ArgumentException();
            }
        } else if (args.length == 2) {
            Module module = Module.get(args[0]);
            if (module != null) {
                Option option = Option.get(module.options, args[1]);
                if (option != null) {
                    ChatUtil.send(String.format("[n]%s %s [t]Description: [v]%s", module.name, option.name, option.desc));
                    ChatUtil.send(String.format("[n]%s %s [t]Value: [v]%s", module.name, option.name, option.value.value));
                } else {
                    throw new ArgumentException();
                }
            } else {
                throw new ArgumentException();
            }
        } else if (args.length == 3) {
            Module module = Module.get(args[0]);
            if (module != null) {
                Option option = Option.get(module.options, args[1]);
                if (option != null) {
                    Option subOption = Option.get(module.options, args[1], args[2]);
                    if (subOption != null) {
                        ChatUtil.send(String.format("[n]%s %s %s [t]Description: [v]%s", module.name, option.name, subOption.name, subOption.desc));
                        ChatUtil.send(String.format("[n]%s %s %s [t]Value: [v]%s", module.name, option.name, subOption.name, subOption.value.value));
                    } else {
                        throw new ArgumentException();
                    }
                } else {
                    throw new ArgumentException();
                }
            } else {
                throw new ArgumentException();
            }
        } else {
            throw new SyntaxException();
        }
    }
}
