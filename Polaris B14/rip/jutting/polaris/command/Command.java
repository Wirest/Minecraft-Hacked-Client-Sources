package rip.jutting.polaris.command;

public abstract interface Command
{
  public abstract boolean run(String[] paramArrayOfString);
  
  public abstract String usage();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */