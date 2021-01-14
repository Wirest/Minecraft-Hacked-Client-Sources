package cn.kody.debug.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Mod
{
    private Value<String> mode;
    public float oldGammaSetting;
    
    public Fullbright() {
        super("Fullbright", Category.RENDER);
        this.mode = new Value<String>("Fullbright", "Mode", 0);
        this.oldGammaSetting = 1.0f;
        this.mode.addValue("Gamma");
        this.mode.addValue("Potion");
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (this.mode.isCurrentMode("Gamma")) {
            this.oldGammaSetting = this.mc.gameSettings.gammaSetting;
            this.mc.gameSettings.gammaSetting = 1000.0f;
        }
        if (this.mode.isCurrentMode("Potion")) {
            this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.oldGammaSetting;
        if (this.mode.isCurrentMode("Potion")) {
            this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
        }
        super.onDisable();
    }
}
