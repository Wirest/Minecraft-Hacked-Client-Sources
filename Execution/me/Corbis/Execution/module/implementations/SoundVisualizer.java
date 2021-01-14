package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event2D;
import me.Corbis.Execution.event.events.EventSoundPlay;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class SoundVisualizer extends Module {
    float volume = 0;
    public SoundVisualizer(){
        super("SoundVisualizer", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onSound(EventSoundPlay event){
        volume = event.getVolume();

    }

    @EventTarget
    public void onRenderGui(Event2D event){
       // Execution.instance.soundVisualiser.draw(volume);
        for(int i = 0; i < 20; i++){
            Gui.drawRect(0 + i * 3 + 1, GuiScreen.height - volume * 100, 3 + i * 3, GuiScreen.height, 0xFFFFFFFF);
        }
    }
}
