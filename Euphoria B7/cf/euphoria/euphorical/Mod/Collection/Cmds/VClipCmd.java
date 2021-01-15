// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.MathUtils;

@Info(name = "vclip", syntax = { "<height>" }, help = "TPs you up or down depending on <height>")
public class VClipCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (p0.length > 1) {
            this.syntaxError();
        }
        else if (p0.length == 1) {
            if (MathUtils.isDouble(p0[0])) {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + Double.parseDouble(p0[0]), this.mc.thePlayer.posZ);
            }
            else {
                this.syntaxError("<height> must be a valid integer!");
            }
        }
        else {
            this.syntaxError();
        }
    }
}
