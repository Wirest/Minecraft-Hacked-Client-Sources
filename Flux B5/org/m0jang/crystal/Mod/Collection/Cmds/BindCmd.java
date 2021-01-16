package org.m0jang.crystal.Mod.Collection.Cmds;

import org.lwjgl.input.Keyboard;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "bind",
   syntax = {"<mod> <key>"},
   help = "Binds a mod to a key."
)
public class BindCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length < 2) {
         this.syntaxError();
      } else {
         Module mod = Crystal.INSTANCE.getMods().get(args[0]);
         if (mod != null) {
            int key = Keyboard.getKeyIndex(args[1].toUpperCase());
            if (key != -1) {
               mod.setBind(key);
               Crystal.INSTANCE.getConfig().saveBinds();
               ChatUtils.sendMessageToPlayer(mod.getName() + " has been set to: " + Keyboard.getKeyName(mod.getBind()));
            } else {
               ChatUtils.sendMessageToPlayer("Key not found!");
            }
         } else {
            ChatUtils.sendMessageToPlayer("Module not found! (Hint: Don't include spaces or '.')");
         }
      }

   }
}
