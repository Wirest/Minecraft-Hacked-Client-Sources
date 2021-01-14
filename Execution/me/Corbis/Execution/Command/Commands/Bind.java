package me.Corbis.Execution.Command.Commands;

import me.Corbis.Execution.Command.Command;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind(){
        super("Bind", ".bind <Module Name> <Key>", 0);
    }
    @Override
    public void onCommand(String command, String[] args){
        super.onCommand(command, args);
        for(Module m : Execution.instance.getModuleManager().getModules()){
            if(m.getName().equalsIgnoreCase(args[0])){
                if(args.length < 2){
                    Execution.instance.addChatMessage(this.getSyntax());
                }else {
                    if(!(args[1].equalsIgnoreCase("none"))) {
                        m.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                    }else {
                        m.setKey(Keyboard.KEY_NONE);
                    }
                    Execution.instance.addChatMessage("Successfully Bound " + m.getName() + " to Key " + args[1]);
                    return;
                }
            }
        }
        Execution.instance.addChatMessage("Module Not Found!");
    }
}
