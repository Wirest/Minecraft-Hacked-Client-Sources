// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Utils.ChatUtils;

@Info(name = "t", syntax = { "<mod>" }, help = "Toggles mods")
public class ToggleCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {	
        if (p0.length < 1) {
            this.syntaxError();
        }
        else {
            final Mod mod = Euphoria.getEuphoria().theMods.getMod(p0[0]);
            if (mod != null) {
                mod.toggle();
            }
            else {
                ChatUtils.sendMessageToPlayer("Mod not found! (Hint: Dont include spaces or '.')");
            }
        }
    }
}
