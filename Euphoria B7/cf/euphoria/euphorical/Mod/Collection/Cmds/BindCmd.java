// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import org.lwjgl.input.Keyboard;

import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Utils.ChatUtils;

@Info(name = "bind", syntax = { "<mod> <key>" }, help = "Binds a mod to a key")
public class BindCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (p0.length < 2) {
            this.syntaxError();
        }
        else {
            final Mod mod = Euphoria.getEuphoria().theMods.getMod(p0[0]);
            if (mod != null) {
                final int key = Keyboard.getKeyIndex(p0[1].toUpperCase());
                if (key != -1) {
                    mod.setBind(key);
                    ChatUtils.sendMessageToPlayer(String.valueOf(mod.getModName()) + " has been set to: " + Keyboard.getKeyName(mod.getBind()));
                }
                else {
                    ChatUtils.sendMessageToPlayer("Key not found!");
                }
            }
            else {
                ChatUtils.sendMessageToPlayer("Mod not found! (Hint: Dont include spaces or '.')");
            }
        }
    }
}
