// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import me.nico.hush.modules.Module;
import me.nico.hush.Client;
import me.nico.hush.commands.Command;

public class Bind extends Command
{
    public Bind() {
        super("bind", "to bind a module.");
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            Command.messageWithPrefix(String.valueOf(Client.instance.commandManager.Chat_Prefix) + "bind §f<Module> <KeyBind> §f");
            Command.messageWithPrefix(String.valueOf(Client.instance.commandManager.Chat_Prefix) + "bind §f<Module> §8reset §f- §7To remove the keybind.");
            return;
        }
        final Module mod = Client.instance.moduleManager.getModuleName(args[0]);
        final String key = args[1];
        final Integer intKey = 0;
        if (mod != null) {
            if (key.equalsIgnoreCase("reset")) {
                mod.setKeyBind(0);
                Command.messageWithPrefix("§7The Keybind for §f" + mod.getDisplayname() + "§r§7 has been removed.");
                Client.instance.FileManager().saveKeyBinds();
            }
            else if (this.keyToInt(key) != 0) {
                mod.setKeyBind(this.keyToInt(key));
                Command.messageWithPrefix("§7The Module §f" + mod.getDisplayname() + "§r§7 was placed on the §f" + key + " §7Button.");
                Client.instance.FileManager().saveKeyBinds();
            }
            else {
                Command.messageWithPrefix("§cThis Button does not exist!");
            }
        }
        else {
            Command.messageWithPrefix("§cThe Module §f" + args[0] + " §r§ccould not be found!");
        }
    }
    
    public int keyToInt(final String keyCode) {
        if (keyCode.equalsIgnoreCase("a")) {
            return 30;
        }
        if (keyCode.equalsIgnoreCase("b")) {
            return 48;
        }
        if (keyCode.equalsIgnoreCase("c")) {
            return 46;
        }
        if (keyCode.equalsIgnoreCase("d")) {
            return 32;
        }
        if (keyCode.equalsIgnoreCase("e")) {
            return 18;
        }
        if (keyCode.equalsIgnoreCase("f")) {
            return 33;
        }
        if (keyCode.equalsIgnoreCase("g")) {
            return 34;
        }
        if (keyCode.equalsIgnoreCase("h")) {
            return 35;
        }
        if (keyCode.equalsIgnoreCase("i")) {
            return 23;
        }
        if (keyCode.equalsIgnoreCase("j")) {
            return 36;
        }
        if (keyCode.equalsIgnoreCase("k")) {
            return 37;
        }
        if (keyCode.equalsIgnoreCase("l")) {
            return 38;
        }
        if (keyCode.equalsIgnoreCase("m")) {
            return 50;
        }
        if (keyCode.equalsIgnoreCase("n")) {
            return 49;
        }
        if (keyCode.equalsIgnoreCase("o")) {
            return 24;
        }
        if (keyCode.equalsIgnoreCase("p")) {
            return 25;
        }
        if (keyCode.equalsIgnoreCase("q")) {
            return 16;
        }
        if (keyCode.equalsIgnoreCase("r")) {
            return 19;
        }
        if (keyCode.equalsIgnoreCase("s")) {
            return 31;
        }
        if (keyCode.equalsIgnoreCase("t")) {
            return 20;
        }
        if (keyCode.equalsIgnoreCase("u")) {
            return 22;
        }
        if (keyCode.equalsIgnoreCase("v")) {
            return 47;
        }
        if (keyCode.equalsIgnoreCase("w")) {
            return 17;
        }
        if (keyCode.equalsIgnoreCase("x")) {
            return 45;
        }
        if (keyCode.equalsIgnoreCase("y")) {
            return 21;
        }
        if (keyCode.equalsIgnoreCase("z")) {
            return 44;
        }
        if (keyCode.equalsIgnoreCase("0")) {
            return 11;
        }
        if (keyCode.equalsIgnoreCase("1")) {
            return 2;
        }
        if (keyCode.equalsIgnoreCase("2")) {
            return 3;
        }
        if (keyCode.equalsIgnoreCase("3")) {
            return 4;
        }
        if (keyCode.equalsIgnoreCase("4")) {
            return 5;
        }
        if (keyCode.equalsIgnoreCase("5")) {
            return 6;
        }
        if (keyCode.equalsIgnoreCase("6")) {
            return 7;
        }
        if (keyCode.equalsIgnoreCase("7")) {
            return 8;
        }
        if (keyCode.equalsIgnoreCase("8")) {
            return 9;
        }
        if (keyCode.equalsIgnoreCase("9")) {
            return 10;
        }
        if (keyCode.equalsIgnoreCase("f1")) {
            return 59;
        }
        if (keyCode.equalsIgnoreCase("f2")) {
            return 60;
        }
        if (keyCode.equalsIgnoreCase("f3")) {
            return 61;
        }
        if (keyCode.equalsIgnoreCase("f4")) {
            return 62;
        }
        if (keyCode.equalsIgnoreCase("f5")) {
            return 63;
        }
        if (keyCode.equalsIgnoreCase("f6")) {
            return 64;
        }
        if (keyCode.equalsIgnoreCase("f7")) {
            return 65;
        }
        if (keyCode.equalsIgnoreCase("f8")) {
            return 66;
        }
        if (keyCode.equalsIgnoreCase("f9")) {
            return 67;
        }
        if (keyCode.equalsIgnoreCase("f10")) {
            return 68;
        }
        if (keyCode.equalsIgnoreCase("f11")) {
            return 87;
        }
        if (keyCode.equalsIgnoreCase("f12")) {
            return 88;
        }
        if (keyCode.equalsIgnoreCase("numpad0")) {
            return 82;
        }
        if (keyCode.equalsIgnoreCase("numpad1")) {
            return 79;
        }
        if (keyCode.equalsIgnoreCase("numpad2")) {
            return 80;
        }
        if (keyCode.equalsIgnoreCase("numpad3")) {
            return 81;
        }
        if (keyCode.equalsIgnoreCase("numpad4")) {
            return 75;
        }
        if (keyCode.equalsIgnoreCase("numpad5")) {
            return 76;
        }
        if (keyCode.equalsIgnoreCase("numpad6")) {
            return 77;
        }
        if (keyCode.equalsIgnoreCase("numpad7")) {
            return 71;
        }
        if (keyCode.equalsIgnoreCase("numpad8")) {
            return 72;
        }
        if (keyCode.equalsIgnoreCase("numpad9")) {
            return 73;
        }
        if (keyCode.equalsIgnoreCase("up")) {
            return 200;
        }
        if (keyCode.equalsIgnoreCase("down")) {
            return 208;
        }
        if (keyCode.equalsIgnoreCase("left")) {
            return 203;
        }
        if (keyCode.equalsIgnoreCase("right")) {
            return 205;
        }
        if (keyCode.equalsIgnoreCase("del")) {
            return 211;
        }
        if (keyCode.equalsIgnoreCase("insert")) {
            return 210;
        }
        if (keyCode.equalsIgnoreCase("end")) {
            return 207;
        }
        if (keyCode.equalsIgnoreCase("home")) {
            return 199;
        }
        if (keyCode.equalsIgnoreCase("rshift")) {
            return 54;
        }
        if (keyCode.equalsIgnoreCase("lshift")) {
            return 42;
        }
        if (keyCode.equalsIgnoreCase("tab")) {
            return 15;
        }
        if (keyCode.equalsIgnoreCase(".")) {
            return 52;
        }
        if (keyCode.equalsIgnoreCase("strg")) {
            return 29;
        }
        if (keyCode.equalsIgnoreCase("alt")) {
            return 56;
        }
        if (keyCode.equalsIgnoreCase("hashtag")) {
            return 53;
        }
        return 0;
    }
}
