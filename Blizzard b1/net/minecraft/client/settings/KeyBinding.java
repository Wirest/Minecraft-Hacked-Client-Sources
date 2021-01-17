package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

public class KeyBinding implements Comparable
{
    private static final List keybindArray = Lists.newArrayList();
    private static final IntHashMap hash = new IntHashMap();
    private static final Set keybindSet = Sets.newHashSet();
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;

    /** because _303 wanted me to call it that(Caironater) */
    public boolean pressed;
    private int pressTime;
    private static final String __OBFID = "CL_00000628";

    public static void onTick(int keyCode)
    {
        if (keyCode != 0)
        {
            KeyBinding var1 = (KeyBinding)hash.lookup(keyCode);

            if (var1 != null)
            {
                ++var1.pressTime;
            }
        }
    }

    public static void setKeyBindState(int keyCode, boolean pressed)
    {
        if (keyCode != 0)
        {
            KeyBinding var2 = (KeyBinding)hash.lookup(keyCode);

            if (var2 != null)
            {
                var2.pressed = pressed;
            }
        }
    }

    public static void unPressAllKeys()
    {
        Iterator var0 = keybindArray.iterator();

        while (var0.hasNext())
        {
            KeyBinding var1 = (KeyBinding)var0.next();
            var1.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash()
    {
        hash.clearMap();
        Iterator var0 = keybindArray.iterator();

        while (var0.hasNext())
        {
            KeyBinding var1 = (KeyBinding)var0.next();
            hash.addKey(var1.keyCode, var1);
        }
    }

    public static Set getKeybinds()
    {
        return keybindSet;
    }

    public KeyBinding(String description, int keyCode, String category)
    {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        keybindArray.add(this);
        hash.addKey(keyCode, this);
        keybindSet.add(category);
    }

    public boolean getIsKeyPressed()
    {
        return this.pressed;
    }

    public String getKeyCategory()
    {
        return this.keyCategory;
    }

    public boolean isPressed()
    {
        if (this.pressTime == 0)
        {
            return false;
        }
        else
        {
            --this.pressTime;
            return true;
        }
    }

    private void unpressKey()
    {
        this.pressTime = 0;
        this.pressed = false;
    }

    public String getKeyDescription()
    {
        return this.keyDescription;
    }

    public int getKeyCodeDefault()
    {
        return this.keyCodeDefault;
    }

    public int getKeyCode()
    {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode)
    {
        this.keyCode = keyCode;
    }

    public int compareTo(KeyBinding p_compareTo_1_)
    {
        int var2 = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));

        if (var2 == 0)
        {
            var2 = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
        }

        return var2;
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((KeyBinding)p_compareTo_1_);
    }
}
