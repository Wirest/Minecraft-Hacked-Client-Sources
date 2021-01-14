package info.sigmaclient.management.command.impl;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.keybinding.Keybind;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

import info.sigmaclient.management.keybinding.KeyMask;
import net.minecraft.util.EnumChatFormatting;

public class Bind extends Command {

    public Bind(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        // Intended arguements
        // 1 - Module
        // 2 - Key
        // 3 - Mask
        if (args == null) {
            printUsage();
            return;
        }
        // Make sure the user inputs a valid Module
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            printUsage();
            return;
        }
        if (args.length == 1) {
            Keybind key = module.getKeybind();
            ChatUtil.printChat(chatPrefix + module.getName() + ": " + (key.getMask() == KeyMask.None ? "" : key.getMask().name() + " + ") + key.getKeyStr());
        } else if (args.length == 2) {
            int keyIndex = Keyboard.getKeyIndex(args[1].toUpperCase());
            Keybind keybind = new Keybind(module, keyIndex);
            module.setKeybind(keybind);
            Keybind key = module.getKeybind();
            ChatUtil.printChat(chatPrefix + "Set " + module.getName() + " to " + (key.getMask() == KeyMask.None ? "" : key.getMask().name() + " + ") + key.getKeyStr());
        } else if (args.length == 3) {
            int keyIndex = Keyboard.getKeyIndex(args[1].toUpperCase());
            KeyMask mask = KeyMask.getMask(args[2]);
            Keybind keybind = new Keybind(module, keyIndex, mask);
            module.setKeybind(keybind);
            Keybind key = module.getKeybind();
            ChatUtil.printChat(chatPrefix + "Set " + module.getName() + " to " + (key.getMask() == KeyMask.None ? "" : key.getMask().name() + " + ") + key.getKeyStr());
        }
        Module.saveStatus();
    }

    @Override
    public String getUsage() {
        return "bind <Module> [Key] [Mask]";
    }

    @Override
    public void onEvent(Event event) {

    }
}
