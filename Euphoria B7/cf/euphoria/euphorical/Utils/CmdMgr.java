// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Utils;

import java.util.Collection;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Mod.Collection.Misc.Commands;
import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Events.EventChatSend;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Collection.Cmds.VClipCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.TracersCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.ToggleCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.SpeedCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.ServerInfoCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.PhaseCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.PDCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.HelpCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.FriendCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.ESPCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.ConnectCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.BindCmd;
import cf.euphoria.euphorical.Mod.Collection.Cmds.AuraCmd;
import cf.euphoria.euphorical.Mod.Cmd;
import java.util.TreeMap;

public class CmdMgr
{
    private final TreeMap<String, Cmd> cmds;
    
    public CmdMgr() {
        this.cmds = new TreeMap<String, Cmd>();
        this.add(new AuraCmd());
        this.add(new BindCmd());
        this.add(new ConnectCmd());
        this.add(new ESPCmd());
        this.add(new FriendCmd());
        this.add(new HelpCmd());
        this.add(new PDCmd());
        this.add(new PhaseCmd());
        this.add(new ServerInfoCmd());
        this.add(new SpeedCmd());
        this.add(new ToggleCmd());
        this.add(new TracersCmd());
        this.add(new VClipCmd());
        EventManager.register(this);
    }
    
    private void add(final Cmd cmd) {
        this.cmds.put(cmd.getCmdName(), cmd);
    }
    
    @EventTarget
    public void onChatSend(final EventChatSend event) {
        if (Euphoria.getEuphoria().theMods.getMod(Commands.class).isEnabled()) {
            final String message = event.message;
            if (message.startsWith(".")) {
                event.setCancelled(true);
                final String input = message.substring(1);
                final String commandName = input.split(" ")[0];
                String[] args;
                if (input.contains(" ")) {
                    args = input.substring(input.indexOf(" ") + 1).split(" ");
                }
                else {
                    args = new String[0];
                }
                final Cmd cmd = this.getCommandByName(commandName);
                if (cmd != null) {
                    try {
                        cmd.execute(args);
                    }
                    catch (Cmd.SyntaxError e) {
                        if (e.getMessage() != null) {
                            ChatUtils.sendMessageToPlayer("§4Syntax error:§r " + e.getMessage());
                        }
                        else {
                            ChatUtils.sendMessageToPlayer("§4Syntax error!§r");
                        }
                        cmd.printSyntax();
                    }
                    catch (Cmd.Error e2) {
                        ChatUtils.sendMessageToPlayer(e2.getMessage());
                    }
                    catch (Exception ex) {}
                }
                else {
                    ChatUtils.sendMessageToPlayer("\"." + commandName + "\" is not a valid command.");
                }
            }
        }
    }
    
    public Cmd getCommandByName(final String name) {
        return this.cmds.get(name);
    }
    
    public Collection<Cmd> getCmds() {
        return this.cmds.values();
    }
    
    public int countCommands() {
        return this.cmds.size();
    }
}
