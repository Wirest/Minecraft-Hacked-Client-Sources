package net.minecraft.client.audio;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class GuardianSound extends MovingSound
{
    private final EntityGuardian guardian;
    private static final String __OBFID = "CL_00002381";

    public GuardianSound(EntityGuardian guardian)
    {
        super(new ResourceLocation("minecraft:mob.guardian.attack"));
        this.guardian = guardian;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (!this.guardian.isDead && this.guardian.func_175474_cn())
        {
            this.xPosF = (float)this.guardian.posX;
            this.yPosF = (float)this.guardian.posY;
            this.zPosF = (float)this.guardian.posZ;
            float var1 = this.guardian.func_175477_p(0.0F);
            this.volume = 0.0F + 1.0F * var1 * var1;
            this.pitch = 0.7F + 0.5F * var1;
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
