package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.BlockPos;

public abstract interface ICommandManager
{
  public abstract int executeCommand(ICommandSender paramICommandSender, String paramString);
  
  public abstract List<String> getTabCompletionOptions(ICommandSender paramICommandSender, String paramString, BlockPos paramBlockPos);
  
  public abstract List<ICommand> getPossibleCommands(ICommandSender paramICommandSender);
  
  public abstract Map<String, ICommand> getCommands();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\ICommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */