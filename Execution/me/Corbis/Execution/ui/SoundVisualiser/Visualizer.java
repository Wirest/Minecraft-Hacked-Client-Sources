package me.Corbis.Execution.ui.SoundVisualiser;

import java.util.ArrayList;

public class Visualizer {

    public void draw(float volume){
        for(int i = 0; i < 20; i++){
            ArrayList<SoundBar> soundBars = new ArrayList<>();
            soundBars.add(new SoundBar(i, volume));
            for(SoundBar bar : soundBars){
                bar.draw();
            }
        }
    }

}
