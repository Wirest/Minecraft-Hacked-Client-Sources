// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGuardian;

public class GuardianSound extends MovingSound
{
    private final EntityGuardian guardian;
    
    public GuardianSound(final EntityGuardian guardian) {
        super(new ResourceLocation("minecraft:mob.guardian.attack"));
        this.guardian = guardian;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }
    
    @Override
    public void update() {
        if (!this.guardian.isDead && this.guardian.hasTargetedEntity()) {
            this.xPosF = (float)this.guardian.posX;
            this.yPosF = (float)this.guardian.posY;
            this.zPosF = (float)this.guardian.posZ;
            final float f = this.guardian.func_175477_p(0.0f);
            this.volume = 0.0f + 1.0f * f * f;
            this.pitch = 0.7f + 0.5f * f;
        }
        else {
            this.donePlaying = true;
        }
    }
}
