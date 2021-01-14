package me.Corbis.Execution.Command.Commands;

import me.Corbis.Execution.Command.Command;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Module;

public class Toggle extends Command {

    public Toggle(){
        super("t", ".t <Module Name>", 1);
    }

    @Override
    public void onCommand(String command, String[] args){
        super.onCommand(command, args);
        for(Module m : Execution.instance.getModuleManager().getModules()){
            if(m.getName().equalsIgnoreCase(args[0])){
                m.toggle();
                Execution.instance.addChatMessage("Toggled " + m.getName() + "!");
                return;
            }
        }
        Execution.instance.addChatMessage("Module Not Found!");

    }
}
