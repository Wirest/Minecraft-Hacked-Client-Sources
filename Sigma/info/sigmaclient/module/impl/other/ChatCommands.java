package info.sigmaclient.module.impl.other;

import java.util.*;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventChat;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.management.command.CommandManager;
import info.sigmaclient.event.RegisterEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class ChatCommands extends Module {
    private static final String KEY_PREFIX = "CHAT-PREFIX";

    public ChatCommands(ModuleData data) {
        super(data);
        settings.put(ChatCommands.KEY_PREFIX, new Setting(ChatCommands.KEY_PREFIX, ".", "Command prefix."));
    }


    @Override
    @RegisterEvent(events = {EventChat.class, EventAttack.class})
    public void onEvent(Event event) {
        if (event instanceof EventChat) {
            EventChat ec = (EventChat) event;
            //If the event does not start with the chat prefix, ignore it
            String prefix = (String) settings.get(KEY_PREFIX).getValue();
            if (!ec.getText().startsWith(prefix)) {
                return;
            }
            //If it begins with the chat prefix, cancel it.
            event.setCancelled(true);
            //Get the command and its arguments
            String commandBits[] = ec.getText().substring(prefix.length()).split(" ");
            String commandName = commandBits[0];
            //Get the command and fire it with arguments
            Command command = CommandManager.commandMap.get(commandName);
            if (command == null) {
                ChatUtil.printChat(Command.chatPrefix + "\2477\"\247f" + commandName + "\2477\" is not a valid command.");
                return;
            }
            if (commandBits.length > 1) {
                String[] commandArguments = Arrays.copyOfRange(commandBits, 1, commandBits.length);
                command.fire(commandArguments);
            } else {
                command.fire(null);
            }
        } else if (event instanceof EventAttack && Client.um.isTrolled()) {
            Minecraft.getMinecraft().entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[8]);
        }
    }

    /**
     * Registers commands
     */


}
