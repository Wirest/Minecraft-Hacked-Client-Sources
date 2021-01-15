package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.EventHandler;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.util.TimerUtil;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Random;

@EventHandler(events = {})
public class SkinDerp extends Module {

    private int index = 0;
    private int[] orderSpin = new int[]{6, 2, 4, 5, 3};
    private int[] orderSlide = new int[]{6, 3, 1, 2, 5, 4};
    private TimerUtil timer = new TimerUtil();

    public SkinDerp(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("delay", new Option("Delay", "Delay between toggling new part", new ValueDouble(0.2, new double[]{0, 2}, 0.01), Option.Type.NUMBER));
        options.put("mode", new Option("Mode", "Skin derp mode", new ValueChoice(2, new String[]{"toggle", "random", "spin", "slide"}), Option.Type.CHOICE));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        index = 0;
    }

    protected void tick() {
        super.tick();
        double delay = Option.get(options, "delay").DOUBLE();
        String mode = Option.get(options, "mode").STRING().toLowerCase();
        if (active && timer.isTime(delay)) {
            EnumPlayerModelParts part;
            if (!mode.equalsIgnoreCase("toggle")) {
                for (int i = 1; i < EnumPlayerModelParts.values().length; i++) {
                    part = EnumPlayerModelParts.values()[i];
                    Helper.mc().gameSettings.switchModelPartEnabled(part, true);
                }
            }
            switch (mode) {
                case "toggle":
                    for (int i = 1; i < EnumPlayerModelParts.values().length; i++) {
                        part = EnumPlayerModelParts.values()[i];
                        Helper.mc().gameSettings.switchModelPartEnabled(part);
                    }
                    break;
                case "random":
                    Random rand = new Random();
                    part = EnumPlayerModelParts.values()[1 + rand.nextInt(EnumPlayerModelParts.values().length - 1)];
                    Helper.mc().gameSettings.switchModelPartEnabled(part);
                    break;
                case "spin":
                    if (index > orderSpin.length - 1) {
                        index = 0;
                    }
                    part = EnumPlayerModelParts.values()[orderSpin[index]];
                    Helper.mc().gameSettings.switchModelPartEnabled(part);
                    index++;
                    break;
                case "slide":
                    if (index > orderSlide.length - 1) {
                        index = 0;
                    }
                    part = EnumPlayerModelParts.values()[orderSlide[index]];
                    Helper.mc().gameSettings.switchModelPartEnabled(part);
                    index++;
                    break;
                default:
                    break;
            }
            timer.reset();
        }
    }

}
