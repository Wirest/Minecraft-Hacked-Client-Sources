// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import net.minecraft.entity.player.EnumPlayerModelParts;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.EventTick;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class SkinDerp extends Module
{
    public SkinDerp() {
        super("SkinDerp", 0, Category.OTHER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.HAT, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.JACKET, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.LEFT_PANTS_LEG, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.RIGHT_PANTS_LEG, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.LEFT_SLEEVE, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.RIGHT_SLEEVE, new Random().nextBoolean());
    }
}
