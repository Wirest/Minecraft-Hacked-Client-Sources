package net.minecraft.command;

public interface IAdminCommand
{
    /**
     * Send an informative message to the server operators
     *  
     * @param sender The command sender
     * @param command The command that was executed
     * @param msgFormat The message, optionally with formatting wildcards
     * @param msgParams The formatting arguments for the {@code msgFormat}
     */
    void notifyOperators(ICommandSender var1, ICommand var2, int var3, String var4, Object ... var5);
}
