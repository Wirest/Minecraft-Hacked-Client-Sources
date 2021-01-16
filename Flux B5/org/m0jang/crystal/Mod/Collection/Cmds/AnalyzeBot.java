package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "fix",
   syntax = {""},
   help = "Fix Sound System Quickly."
)
public class AnalyzeBot extends Command {
   public void execute(String[] args) throws Command.Error {
      Wrapper.mc.getSoundHandler().getSndManager().reloadSoundSystem();
      ChatUtils.sendMessageToPlayer("Reloaded Sound System!");
   }
}
