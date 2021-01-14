package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;

public class Hitbox extends Mod{
    public static Value<Double> size = new Value<Double>("Hitbox_Size", 0.1, 0.1, 1.0, 0.01);
    public Hitbox() {
        super("Hitbox", "Hitbox", Category.COMBAT);
    }
}
