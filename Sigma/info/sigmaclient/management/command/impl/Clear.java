package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import net.minecraft.client.Minecraft;

public class Clear extends Command {

    public Clear(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
    }

    @Override
    public String getUsage() {
        return "Clear";
    }

    @Override
    public void onEvent(Event event) {

    }
}
