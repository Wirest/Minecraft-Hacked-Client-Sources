package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.BlockPos;

public interface ICommandManager
{
    int executeCommand(ICommandSender sender, String command);

    List getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos);

    /**
     * returns all commands that the commandSender can use
     */
    List getPossibleCommands(ICommandSender sender);

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    Map getCommands();
}
