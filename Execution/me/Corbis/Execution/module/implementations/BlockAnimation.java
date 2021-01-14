package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class BlockAnimation extends Module {
    public BlockAnimation(){
        super("BlockAnimation", Keyboard.KEY_NONE, Category.RENDER);

        ArrayList<String> options = new ArrayList<>();
        options.add("1.7");
        options.add("Execution");
        options.add("Skidma");
        options.add("Leaked");
        options.add("Slide");
        options.add("Stab");
        options.add("Spin");
        options.add("Astolfo");
        options.add("Virtue");
        options.add("Slap");
        options.add("Sink");
        options.add("ETB");
        options.add("Exhibobo");
        options.add("Wax");
        options.add("Table");
        options.add("Remix");
        options.add("?");
        options.add("!");


        Execution.instance.settingsManager.rSetting(new Setting("BlockAnimation", this, "Slide", options));

        Execution.instance.settingsManager.rSetting(new Setting("BlockAnimation Speed", this, 3, 1, 50, true));
    }

    
}
