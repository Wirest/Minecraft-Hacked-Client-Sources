package me.Corbis.Execution.Command.Commands;

import me.Corbis.Execution.Command.Command;
import me.Corbis.Execution.Execution;

public class PlayMusic extends Command {

    public PlayMusic(){
        super("playmusic", ".play musicname", 0);
    }

    @Override
    public void onCommand(String command, String[] args){
        Execution.instance.soundHandler.playSound("stiletostateofmind.wav");
        //if(Execution.instance.musicManager.getSongByName(args[0]) == null) {
          //  Execution.instance.addChatMessage("Song Not Found!");
         //   return;
        //}
       // Execution.instance.musicManager.getSongByName(args[0]).play();
        super.onCommand(command, args);

    }
}
