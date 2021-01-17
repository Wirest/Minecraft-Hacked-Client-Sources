package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final EntityPlayer player;
    private final EntityMinecart minecart;
    private static final String __OBFID = "CL_00001119";

    public MovingSoundMinecartRiding(EntityPlayer p_i45106_1_, EntityMinecart minecart)
    {
        super(new ResourceLocation("minecraft:minecart.inside"));
        this.player = p_i45106_1_;
        this.minecart = minecart;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (!this.minecart.isDead && this.player.isRiding() && this.player.ridingEntity == this.minecart)
        {
            float var1 = MathHelper.sqrt_double(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);

            if ((double)var1 >= 0.01D)
            {
                this.volume = 0.0F + MathHelper.clamp_float(var1, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                this.volume = 0.0F;
            }
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
