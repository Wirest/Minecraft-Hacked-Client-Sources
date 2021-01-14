package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;

public class Reach extends Mod
{
    public Reach() {
        super("Reach","Reach", Category.COMBAT);
    }
    
    public static Value<Double> reach = new Value<Double>("Reach_Range", 4.5, 3.0, 8.0, 0.1);

}
