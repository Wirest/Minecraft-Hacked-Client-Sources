// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Spambot extends Module
{
    public static boolean bp;
    float delaytime;
    int mCount;
    String r1;
    String r2;
    
    static {
        Spambot.bp = true;
    }
    
    public Spambot() {
        super("Spambot", 0, Category.OTHER);
        this.delaytime = 0.0f;
        this.mCount = 1;
    }
    
    @Override
    public void onUpdate() {
        ++this.delaytime;
        final float delay = this.delaytime / 20.0f;
        if (this.getState()) {
            if (!Spambot.bp && delay > FusionX.td) {
                Wrapper.mc.thePlayer.sendChatMessage(FusionX.spammessage);
                this.delaytime = 0.0f;
            }
            if (Spambot.bp) {
                this.r1 = RandomStringUtils.randomAlphanumeric(20).toLowerCase();
                this.r2 = UUID.randomUUID().toString();
                if (delay > FusionX.td && this.mCount <= 3) {
                    if (this.mCount == 1) {
                        Wrapper.mc.thePlayer.sendChatMessage(FusionX.spammessage);
                    }
                    else if (this.mCount == 2) {
                        Wrapper.mc.thePlayer.sendChatMessage(this.r1);
                    }
                    else if (this.mCount == 3) {
                        Wrapper.mc.thePlayer.sendChatMessage(this.r2);
                    }
                    ++this.mCount;
                    this.delaytime = 0.0f;
                }
                if (this.mCount > 3) {
                    this.mCount = 0;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
