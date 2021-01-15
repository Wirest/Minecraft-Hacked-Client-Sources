package com.ihl.client.module.option;

import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.ColorUtil;
import joptsimple.internal.Strings;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class Option {

    public static enum Type {
        BOOLEAN("<true|false>"),
        CHOICE("<%s>"),
        KEYBIND("<key>"),
        LIST("<value>"),
        NUMBER("<number>"),
        OTHER("<value>"),
        STRING("<text>");

        public String usage;

        Type(String usage) {
            this.usage = usage;
        }
    }

    public static Option get(Map<String, Option> options, String key) {
        return options.get(key);
    }

    public static Option get(Map<String, Option> options, String key, String key2) {
        return options.get(key).options.get(key2);
    }

    public static void setOptionValue(Option option, String arg) throws Exception {
        Object value = null;
        String message = String.format("[v]%s [t]set to [v]%s", option.name, arg);
        switch (option.type) {
            case BOOLEAN:
                try {
                    if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
                        value = Boolean.parseBoolean(arg);
                    } else {
                        throw new ArgumentException();
                    }
                } catch (Exception e) {
                    throw new ArgumentException();
                }
                break;
            case CHOICE:
                int is = -1;
                String[] list = ((ValueChoice) option.value).list;
                for (int i = 0; i < list.length; i++) {
                    if (list[i].equalsIgnoreCase(arg)) {
                        is = i;
                    }
                }
                if (is != -1) {
                    value = list[is];
                } else {
                    throw new ArgumentException();
                }
                break;
            case KEYBIND:
                value = arg.toUpperCase();
                break;
            case NUMBER:
                try {
                    value = Double.parseDouble(arg);
                } catch (Exception e) {
                    throw new ArgumentException();
                }
                break;
            case OTHER:
                value = arg;
                break;
            case STRING:
                value = arg;
                break;
            case LIST:
                List<String> l = new ArrayList();
                l.addAll((List<String>) option.value.value);

                if (l.contains(arg)) {
                    l.remove(arg);
                    message = String.format("[v]%s [t]removed from [v]%s", arg, option.name);
                } else {
                    l.add(arg);
                    message = String.format("[v]%s [t]added to [v]%s", arg, option.name);
                }

                value = l;
                break;
        }

        if (value != null) {
            option.value.value = value;
            ChatUtil.send(message);
        }
    }

    public String name, desc;
    public Value value;
    public Type type;
    public ResourceLocation icon, typeIcon;
    public Map<String, Option> options = new LinkedHashMap();
    public int color;

    public Option(String name, String desc, Value value, Type type) {
        this(name, desc, value, type, (List) null);
    }

    public Option(String name, String desc, Value value, Type type, Option[] options) {
        this(name, desc, value, type, Arrays.asList(options));
    }

    public Option(String name, String desc, Value value, Type type, List<Option> options) {
        this.name = name;
        this.desc = desc;
        this.value = value;
        this.type = type;
        if (options != null) {
            for(Option option : options) {
                this.options.put(option.name.toLowerCase().replaceAll(" ", ""), option);
            }
        }
        icon = new ResourceLocation("client/icons/option/" + (name.toLowerCase().replaceAll(" ", "")) + ".png");
        color = ColorUtil.rainbow((long) (Math.random() * 10000000000D), 1f).getRGB();
    }

    public boolean BOOLEAN() {
        if (value instanceof ValueBoolean) {
            return (boolean) value.value;
        }
        return false;
    }

    public double DOUBLE() {
        if (value instanceof ValueDouble) {
            return (double) value.value;
        }
        return 0;
    }

    public int INTEGER() {
        return (int) DOUBLE();
    }

    public String STRING() {
        if (type == Type.LIST) {
            return Strings.join(LIST(), ",");
        }
        return value.value.toString();
    }

    public String CHOICE() {
        return STRING();
    }

    public List<String> LIST() { return (List<String>) value.value; }

}
