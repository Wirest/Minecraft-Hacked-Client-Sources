package info.sigmaclient.management.command;

import info.sigmaclient.management.command.impl.*;

import java.util.Collection;
import java.util.HashMap;

public class CommandManager {

    public void addCommand(String name, Command command) {
        commandMap.put(name, command);
    }

    public Collection<Command> getCommands() {
        return commandMap.values();
    }

    public Command getCommand(String name) {
        return commandMap.get(name.toLowerCase());
    }

    public static final HashMap<String, Command> commandMap = new HashMap<String, Command>();

    public void setup() {
        //new ClaimFinder(new String[] {"cf","finder","claimfinder"}, "Finds claim in a X chunk radius.").register(this);
        new Waypoint(new String[]{"Waypoints", "waypoint", "wp", "marker", "mark", "w"}, "Add/Remove waypoints.").register(this);
        new PluginFinder(new String[]{"PluginFinder", "pf", "plugins"}, "Discovers the plugins on a server.").register(this);
        new Xray(new String[]{"Xray", "x-ray", "xr"}, "Add/Remove items from the blacklist.").register(this);
        new Color(new String[]{"Color", "c", "colors"}, "Change customizable colors.").register(this);
        new Target(new String[]{"Target", "foc", "targ", "focus"}, "Target someone with killaura.").register(this);
        new Save(new String[]{"Save", "sv"}, "Save config").register(this);
        new Insult(new String[]{"Insult", "i"}, "Insult those faggot nodus users.").register(this);
        new Damage(new String[]{"Damage", "dmg", "kys", "suicide", "amandatodd"}, "Damages exploit.").register(this);
        new LoadConfig(new String[]{"Load", "load"}, "Loads config").register(this);
        new Toggle(new String[]{"Toggle", "t"}, "Toggles the module.").register(this);
        new Settings(new String[]{"Setting", "set", "s"}, "Changing and listing settings for modules.").register(this);
        new Help(new String[]{"Help", "halp", "h"}, "Help for commands.").register(this);
        new Say(new String[]{"Say", "talk", "chat"}, "Send a message with your chat prefix.").register(this);
        new HClip(new String[]{"HClip", "hc"}, "Clips you horizontally.").register(this);
        new Bind(new String[]{"Bind", "key", "b"}, "Send a message with your chat prefix.").register(this);
        new Friend(new String[]{"Friend", "fr", "f"}, "Add and remove friends.").register(this);
        new Clear(new String[]{"Clear", "cl"}, "Clears chat for you.").register(this);
        new Panic(new String[]{"Panic"}, "Disable all modules.").register(this);
        new NotificationTest(new String[]{"Test", "nt"}, "Notifications test command.").register(this);
        new VClip(new String[]{"VClip", "vc", "clip"}, "Clips you vertically.").register(this);
        new Config(new String[]{"Config", "cf", "con"}, "Applies configuration settings.").register(this);
        new Hidden(new String[]{"Visible", "vis", "v", "hide", "hidden", "show"}, "Sets the modules hidden state.").register(this);
       // new ChatFaker(new String[]{"ChatFaker", "Chatfake", "cf"}, "Fake a chat message.").register(this);
       // new FakeHackerTarget(new String[]{"FakeHacker", "FakeHack", "fh"}, "Set FakeHacker target.").register(this);
       // new ForceOP(new String[]{"ForceOP"}, "Force OP OMEGALUL.").register(this);
        new AutoSay(new String[]{"AutoSay", "SpamBot"}, "Set message for autosay.").register(this);
        new Sigmeme(new String[]{"Sigmeme"}, "Set a stupid message in the chat").register(this);
    }

}
