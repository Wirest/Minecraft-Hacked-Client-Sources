package me.Corbis.Execution.Command.Commands;

import me.Corbis.Execution.Command.Command;
import me.Corbis.Execution.Execution;
import net.minecraft.client.Minecraft;

public class Replay extends Command {

    public Replay(){
        super("replay", ".replay", 0);
    }

    @Override
    public void onCommand(String command, String[] args) {
        Execution.instance.replayManager.replays.add(new me.Corbis.Execution.Replay.Replay(Minecraft.getMinecraft().theWorld));
        Execution.instance.isRecording = true;

        super.onCommand(command, args);
    }
}
