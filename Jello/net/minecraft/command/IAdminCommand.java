package net.minecraft.command;

public interface IAdminCommand
{
    void notifyOperators(ICommandSender var1, ICommand var2, int var3, String var4, Object ... var5);
}
