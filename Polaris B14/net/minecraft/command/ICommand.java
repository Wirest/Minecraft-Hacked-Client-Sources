package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public abstract interface ICommand
  extends Comparable<ICommand>
{
  public abstract String getCommandName();
  
  public abstract String getCommandUsage(ICommandSender paramICommandSender);
  
  public abstract List<String> getCommandAliases();
  
  public abstract void processCommand(ICommandSender paramICommandSender, String[] paramArrayOfString)
    throws CommandException;
  
  public abstract boolean canCommandSenderUseCommand(ICommandSender paramICommandSender);
  
  public abstract List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString, BlockPos paramBlockPos);
  
  public abstract boolean isUsernameIndex(String[] paramArrayOfString, int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\ICommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */