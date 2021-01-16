/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module;

import darkmagician6.EventManager;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.ToggledMods;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class Module {
    private String name;
    private Category category;
    private String description;
    private String displayname;
    private int key;
    private boolean toggled;
    private boolean visible;

    public Module() {
        this.name = this.getClass().getAnnotation(Mod.class).name();
        this.category = this.getClass().getAnnotation(Mod.class).category();
        this.description = this.getClass().getAnnotation(Mod.class).description();
        this.displayname = this.getClass().getAnnotation(Mod.class).name();
        this.key = 0;
        this.visible = true;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayname;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAlias() {
        return this.getName().toLowerCase().replace(" ", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String name) {
        this.displayname = name;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void toggle() {
        this.toggled = !this.toggled;
        boolean bl = this.toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        ToggledMods.save();
        try {
            Wrapper.mc().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void togglenosave() {
        this.toggled = !this.toggled;
        boolean bl = this.toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        try {
            Wrapper.mc().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void setToggled(boolean toggled) {
        if (this.toggled != toggled) {
            this.toggle();
        }
    }

    public ArrayList<Value> getValues() {
        ArrayList<Value> vals = new ArrayList<Value>();
        for (Value val : Value.modes) {
            if (!val.name.startsWith(this.getName().toLowerCase())) continue;
            vals.add(val);
        }
        return vals;
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public static Module getMod(String name) {
        for (Module mod : Weepcraft.getMods()) {
            if (!mod.getName().equalsIgnoreCase(name)) continue;
            return mod;
        }
        return null;
    }

    public static Module getModByAlias(String alias) {
        for (Module mod : Weepcraft.getMods()) {
            if (!mod.getAlias().equalsIgnoreCase(alias)) continue;
            return mod;
        }
        return null;
    }

    public static Module getModByClass(Object mod) {
        for (Module m : Weepcraft.getMods()) {
            if (!m.getClass().equals(mod)) continue;
            return m;
        }
        return null;
    }

    public static enum Category {

        COMBAT("COMBAT", 0, "COMBAT", 0), 
        AUTO("AUTO", 1, "AUTO", 1), 
        MOVEMENT("MOVEMENT", 2, "MOVEMENT", 2), 
        RENDER("RENDER", 3, "RENDER", 3), 
        PLAYER("PLAYER", 4, "PLAYER", 4), 
        WORLD("WORLD", 5, "WORLD", 5), 
        MISC("MISC", 6, "MISC", 6), 
        HIDDEN("HIDDEN", 7, "HIDDEN", 7);

        
        private static final Category[] ENUMVALUES;

        static {
            ENUMVALUES = new Category[]{COMBAT, AUTO, MOVEMENT, RENDER, PLAYER, WORLD, MISC, HIDDEN};
        }

        private Category(String string2, int n2, String string3, int n3) {
        }
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    public static @interface Mod {
        public String name();

        public int key();

        public Category category();

        public String description();
    }

}

