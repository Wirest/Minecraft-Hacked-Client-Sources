// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

@Info(name = "connect", syntax = { "<ip:port>" }, help = "Connects you to a server")
public class ConnectCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (p0.length > 0) {
            final ServerData theServer = new ServerData("server", p0[0]);
            if (!this.mc.isSingleplayer()) {
                this.mc.theWorld.sendQuittingDisconnectingPacket();
            }
            this.mc.displayGuiScreen(new GuiConnecting(null, this.mc, theServer));
        }
        else {
            this.syntaxError();
        }
    }
}
