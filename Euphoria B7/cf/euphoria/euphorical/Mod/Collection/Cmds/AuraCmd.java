// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Utils.ModeUtils;

@Info(name = "aura", syntax = { "<players/mobs/animals/blockhit>" }, help = "Change Aura settings")
public class AuraCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (p0.length > 1) {
            this.syntaxError();
        }
        else if (p0.length == 1) {
            if (p0[0].equalsIgnoreCase("blockhit")) {
                ModeUtils.bHit = !ModeUtils.bHit;
                ChatUtils.sendMessageToPlayer("Block Hit set to: " + ModeUtils.bHit);
            }
            else if (p0[0].equalsIgnoreCase("players")) {
                ModeUtils.auraP = !ModeUtils.auraP;
                ChatUtils.sendMessageToPlayer("Players set to: " + ModeUtils.auraP);
            }
            else if (p0[0].equalsIgnoreCase("mobs")) {
                ModeUtils.auraM = !ModeUtils.auraM;
                ChatUtils.sendMessageToPlayer("Mobs set to: " + ModeUtils.auraM);
            }
            else if (p0[0].equalsIgnoreCase("animals")) {
                ModeUtils.auraA = !ModeUtils.auraA;
                ChatUtils.sendMessageToPlayer("Animals set to: " + ModeUtils.auraA);
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
