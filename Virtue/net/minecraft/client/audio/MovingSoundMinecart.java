package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecart extends MovingSound
{
    private final EntityMinecart minecart;
    private float field_147669_l = 0.0F;
    private static final String __OBFID = "CL_00001118";

    public MovingSoundMinecart(EntityMinecart p_i45105_1_)
    {
        super(new ResourceLocation("minecraft:minecart.base"));
        this.minecart = p_i45105_1_;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (this.minecart.isDead)
        {
            this.donePlaying = true;
        }
        else
        {
            this.xPosF = (float)this.minecart.posX;
            this.yPosF = (float)this.minecart.posY;
            this.zPosF = (float)this.minecart.posZ;
            float var1 = MathHelper.sqrt_double(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);

            if ((double)var1 >= 0.01D)
            {
                this.field_147669_l = MathHelper.clamp_float(this.field_147669_l + 0.0025F, 0.0F, 1.0F);
                this.volume = 0.0F + MathHelper.clamp_float(var1, 0.0F, 0.5F) * 0.7F;
            }
            else
            {
                this.field_147669_l = 0.0F;
                this.volume = 0.0F;
            }
        }
    }
}
