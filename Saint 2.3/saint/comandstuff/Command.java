package saint.comandstuff;

import net.minecraft.client.Minecraft;

public abstract class Command {
   protected final String[] aliases;
   protected final String command;
   protected final String arguments;
   protected static final Minecraft mc = Minecraft.getMinecraft();

   public Command(String command, String arguments, String... aliases) {
      this.command = command;
      this.arguments = arguments;
      this.aliases = aliases;
   }

   public String[] getAliases() {
      return this.aliases;
   }

   public String getArguments() {
      return this.arguments;
   }

   public String getCommand() {
      return this.command;
   }

   public abstract void run(String var1);
}
