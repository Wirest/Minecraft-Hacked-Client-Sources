package saint.modstuff.modules.addons;

public final class Macro {
   private final String command;
   private final int key;

   public Macro(String command, int key) {
      this.command = command;
      this.key = key;
   }

   public String getCommand() {
      return this.command;
   }

   public int getKey() {
      return this.key;
   }
}
