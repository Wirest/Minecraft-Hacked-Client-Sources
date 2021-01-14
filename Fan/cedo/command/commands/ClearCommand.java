package cedo.command.commands;

import cedo.command.Command;
import cedo.util.Logger;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("Clear", "clear");
    }

    @Override
    public void execute(String[] args) {
        Thread clearChatThread = new Thread(() -> {
            Logger.ingameWarn("Clearing the chat!");
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mc.ingameGUI.getChatGUI().clearChatMessages();
        });
        clearChatThread.start();
    }
}