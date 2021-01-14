package cn.kody.debug.mod.mods.RENDER;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class BlockAnimation
extends Mod {
    public static Value<String> mode = new Value("BlockAnimation", "Mode", 0);

    public BlockAnimation() {
        super("BlockAnimation", Category.RENDER);
        mode.addValue("Swing");
        mode.addValue("Swang");
        mode.addValue("Swong");
        mode.addValue("Swank");
        mode.addValue("Swaing");
        mode.addValue("Vanilla");
    }

    @Override
    public void onEnable() {
//        this.set(false);
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.isEnabled()) {
//            this.set(false);
        }
    }
}

