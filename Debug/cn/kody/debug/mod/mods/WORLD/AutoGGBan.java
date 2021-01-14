package cn.kody.debug.mod.mods.WORLD;

import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;

public class AutoGGBan extends Mod
{
    public static Value ad;
    
    public AutoGGBan() {
        super("AutoGG", Category.WORLD);
    }
    
    static {
        AutoGGBan.ad = new Value("AutoGG_AD", (Boolean)true);
    }
}
