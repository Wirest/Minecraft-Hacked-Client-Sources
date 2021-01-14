package me.Corbis.Execution.Command;

import me.Corbis.Execution.Command.Commands.*;
import me.Corbis.Execution.Execution;

import java.util.ArrayList;

public class CommandManager {

    public ArrayList<Command> commands = new ArrayList<>();

    public CommandManager(){
        commands.add(new Toggle());
        commands.add(new Bind());
        commands.add(new PlayMusic());
        commands.add(new Hypixel());
        commands.add(new Replay());
    }

    public void call(String input){
        String[] split = input.split(" ");
        String commandName = split[0];
        String args = input.substring(commandName.length()).trim();
        for(Command c : commands){
            if(c.getName().equalsIgnoreCase(commandName)){
                c.onCommand(args, args.split(" "));
                return;
            }
        }
        Execution.instance.addChatMessage("Command Not Found!");
    }
}
