package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.commands.Command;
import com.ihl.client.commands.CommandModule;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.module.option.ValueString;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.MathUtil;
import joptsimple.internal.Strings;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Module {

    public static Map<String, Module> modules = new LinkedHashMap();
    protected static int currentId = 1;

    public static void init() {
        new AntiCactus("Anti Cactus",   "Don't get hurt when standing on a cactus",         Category.PLAYER,    "NONE");
        new Aura("Aura",                "Kill everything near you",                         Category.COMBAT,    "F");
        new AutoPotion("Auto Potion",   "Splash health potions when needed",                Category.COMBAT,    "I");
        new Blink("Blink",              "Simulate lag",                                     Category.PLAYER,    "B");
        new CameraClip("Camera Clip",   "Allow third person camera to clip into blocks",    Category.RENDER,    "NONE");
        new Chams("Chams",              "Render entities through blocks",                   Category.RENDER,    "NONE");
        new Commands("Commands",        "Enable in-game chat commands",                     Category.MISC,      "NONE");
        new Console("Console",          "Enable GUI console for command input",             Category.MISC,      "NONE");
        new CPS("CPS",                  "Monitor your clicks-per-second",                   Category.MISC,      "NONE");
        new Criticals("Criticals",      "Deal critical hits",                               Category.COMBAT,    "NONE");
        new Damage("Damage",            "Force yourself to take damage",                    Category.PLAYER,    "NONE");
        new Distance("Distance",        "Change the third person camera distance",          Category.RENDER,    "NONE");
        new ESP("ESP",                  "Render outlines around entities",                  Category.RENDER,    "NONE");
        new FastClimb("FastClimb",      "Climb ladders and vines faster",                   Category.MOVEMENT,  "NONE");
        new FastUse("FastUse",          "Finish using items faster",                        Category.PLAYER,    "NONE");
        new Fly("Fly",                  "Enable creative fly",                              Category.MOVEMENT,  "R");
        new Freecam("Freecam",          "Ghost through blocks client-side",                 Category.MOVEMENT,  "C");
        new Friends("Friends",          "Whitelist friends for combat mods",                Category.MISC,      "NONE");
        new Fullbright("Fullbright",    "Brighten up the world",                            Category.RENDER,    "H");
        new Glide("Glide",              "Slowly decent to the ground",                      Category.MOVEMENT,  "NONE");
        new GUI("GUI",                  "Open the radial GUI",                              Category.RENDER,    "RSHIFT");
        new Jesus("Jesus",              "Walk on water",                                    Category.MOVEMENT,  "NONE");
        new MenuWalk("Menu Walk",       "Walk when in a GUI",                               Category.MOVEMENT,  "NONE");
        new Nametags("Nametags",        "Render player nametags through blocks",            Category.RENDER,    "NONE");
        new Noclip("Noclip",            "Clip through all blocks",                          Category.MOVEMENT,  "NONE");
        new NoSlow("No Slow",           "Prevent slowing down when using items",            Category.MOVEMENT,  "NONE");
        new NoView("No View",           "Prevent the server changing your view direction",  Category.PLAYER,    "NONE");
        new Plugins("Plugins",          "Get a list of server plugins",                     Category.MISC,      "NONE");
        new Phase("Phase",              "Clip through non-solid blocks",                    Category.MOVEMENT,  "NONE");
        new Pinger("Pinger",            "Spoof a perfect ping of 0",                        Category.MISC,      "NONE");
        new Sneak("Sneak",              "Enable server-side sneaking",                      Category.MOVEMENT,  "NONE");
        new Speed("Speed",              "Apply a movement multiplier",                      Category.MOVEMENT,  "G");
        new SkinDerp("Skin Derp",       "Make your clothing layers toggle",                 Category.MISC,      "NONE");
        new Sprint("Sprint",            "Automatically force sprinting",                    Category.MOVEMENT,  "G");
        new Step("Step",                "Step up blocks like stairs",                       Category.MOVEMENT,  "NONE");
        new StorageESP("Storage ESP",   "Render outlines around storage blocks",            Category.RENDER,    "NONE");
        new Swing("Swing",              "Reset the swing animation faster",                 Category.PLAYER,    "NONE");
        new Tracer("Tracer",            "Render lines to entities",                         Category.RENDER,    "NONE");
        new VClip("VClip",              "Clip vertically down through blocks",              Category.MOVEMENT,  "NONE");
        new Velocity("Velocity",        "Change the knockback velocity",                    Category.COMBAT,    "V");
    }

    public static Module get(String key) {
        return modules.get(key);
    }

    public static List<String> enabled() {
        List<String> list = new ArrayList();
        for (String s : modules.keySet()) {
            Module module = get(s);
            if (module.active) {
                list.add(s);
            }
        }
        return list;
    }

    public static List<String> category(Category category) {
        List<String> list = new ArrayList();
        for (String s : modules.keySet()) {
            Module module = get(s);
            if (module.category == category) {
                list.add(s);
            }
        }
        return list;
    }

    public static void tickMods() {
        for(String key : modules.keySet()) {
            Module module = modules.get(key);
            module.tick();
        }
    }

    public static void event(Event event, boolean reverse) {
        if (Helper.player() == null) {
            return;
        }
        List<String> enabled = enabled();
        for (int i = reverse ? enabled.size() - 1 : 0; reverse ? i >= 0 : i < enabled.size(); i += reverse ? -1 : 1) {
            Module module = get(enabled.get(i));
            Class clazz = module.getClass();
            if (clazz.isAnnotationPresent(EventHandler.class)) {
                Annotation annotation = clazz.getAnnotation(EventHandler.class);
                EventHandler handler = (EventHandler) annotation;
                for (Class type : handler.events()) {
                    if (type == event.getClass()) {
                        module.onEvent(event);
                        break;
                    }
                }
            }
        }
    }

    public int id;
    public String name, desc;
    public Category category;
    public int color;
    public boolean active;
    public Map<String, Option> options = new LinkedHashMap();
    public ResourceLocation icon;
    protected Class[] events = new Class[]{};

    public Module(String name, String desc, Category category, String keybind) {
        this.id = currentId++;
        this.name = name;
        this.desc = desc;
        this.category = category;

        options.put("keybind", new Option("Keybind", "Module toggle keybind", new ValueString(keybind), Option.Type.KEYBIND));

        String base = name.toLowerCase().replaceAll(" ", "");
        modules.put(base, this);

        icon = new ResourceLocation("client/icons/module/" + base + ".png");
    }

    protected void initCommands(String base) {
        List<String> usages = new ArrayList();
        usages.add(base);
        for (String key : options.keySet()) {
            Option option = Option.get(options, key);
            for (String key2 : option.options.keySet()) {
                Option option2 = Option.get(options, key, key2);
                usages.add(String.format("%s %s %s %s", name.replaceAll(" ", ""), option.name.replaceAll(" ", ""), option2.name.replaceAll(" ", ""), option2.type == Option.Type.CHOICE ? String.format(option2.type.usage, Strings.join(((ValueChoice) option2.value).list, "|")) : option2.type.usage).toLowerCase());
            }
            usages.add(String.format("%s %s %s", name.replaceAll(" ", ""), option.name.replaceAll(" ", ""), option.type == Option.Type.CHOICE ? String.format(option.type.usage, Strings.join(((ValueChoice) option.value).list, "|")) : option.type.usage).toLowerCase());
        }

        Command.commands.put(base, new CommandModule(base, usages));
    }

    public void enable() {
        color = ColorUtil.rainbow((long) (Math.random() * 10000000000D), 1f).getRGB();
        active = true;
    }

    public void disable() {
        active = false;
    }

    public void toggle() {
        if (active) {
            disable();
        } else {
            enable();
        }
    }

    public String getDisplay() {
        return name;
    }

    protected void tick() {
        for (String key : options.keySet()) {
            Option option = Option.get(options, key);
            limitOption(option);
            if (!option.options.isEmpty()) {
                for(String subKey : option.options.keySet()) {
                    Option subOption = Option.get(options, key, subKey);
                    limitOption(subOption);
                }
            }
        }
    }

    protected void onEvent(Event event) {
    }

    private void limitOption(Option option) {
        switch (option.type) {
            case BOOLEAN:
                break;
            case CHOICE:
                break;
            case KEYBIND:
                break;
            case NUMBER:
                ValueDouble value = (ValueDouble) option.value;
                double val = Math.min(Math.max(value.limit[0], (double) value.value), value.limit[1]);
                val = MathUtil.roundInc(val, value.step);
                value.value = val;
                break;
            case OTHER:
                break;
            case STRING:
                break;
            case LIST:
                break;
        }
    }
}
