// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Utils.ModeUtils;

@Info(name = "speed", syntax = { "<yport/vhop>" }, help = "Change your speed mode.")
public class SpeedCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (p0.length > 1) {
            this.syntaxError();
        }
        else if (p0.length == 1) {
            if (p0[0].equalsIgnoreCase("yport")) {
                ModeUtils.speedMode = "yport";
                ChatUtils.sendMessageToPlayer("Speed mode changed to: §aY-Port!");
            }
            else if (p0[0].equalsIgnoreCase("vhop")) {
                ChatUtils.sendMessageToPlayer("Speed mode changed to: §aV-Hop!");
                ModeUtils.speedMode = "vhop";
            }
            else {
                this.syntaxError();
            }
        }
        else {
            this.syntaxError();
        }
    }
}
