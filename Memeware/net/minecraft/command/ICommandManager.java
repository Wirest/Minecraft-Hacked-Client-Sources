package net.minecraft.command;

import java.util.List;
import java.util.Map;

import net.minecraft.util.BlockPos;

public interface ICommandManager {
    int executeCommand(ICommandSender var1, String var2);

    List getTabCompletionOptions(ICommandSender var1, String var2, BlockPos var3);

    /**
     * returns all commands that the commandSender can use
     */
    List getPossibleCommands(ICommandSender var1);

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    Map getCommands();
}
