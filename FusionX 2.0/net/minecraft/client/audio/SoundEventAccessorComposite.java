package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class SoundEventAccessorComposite implements ISoundEventAccessor
{
    /** A composite (List) of ISoundEventAccessors */
    private final List soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;
    private static final String __OBFID = "CL_00001146";

    public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category)
    {
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }

    public int getWeight()
    {
        int var1 = 0;
        ISoundEventAccessor var3;

        for (Iterator var2 = this.soundPool.iterator(); var2.hasNext(); var1 += var3.getWeight())
        {
            var3 = (ISoundEventAccessor)var2.next();
        }

        return var1;
    }

    public SoundPoolEntry cloneEntry1()
    {
        int var1 = this.getWeight();

        if (!this.soundPool.isEmpty() && var1 != 0)
        {
            int var2 = this.rnd.nextInt(var1);
            Iterator var3 = this.soundPool.iterator();
            ISoundEventAccessor var4;

            do
            {
                if (!var3.hasNext())
                {
                    return SoundHandler.missing_sound;
                }

                var4 = (ISoundEventAccessor)var3.next();
                var2 -= var4.getWeight();
            }
            while (var2 >= 0);

            SoundPoolEntry var5 = (SoundPoolEntry)var4.cloneEntry();
            var5.setPitch(var5.getPitch() * this.eventPitch);
            var5.setVolume(var5.getVolume() * this.eventVolume);
            return var5;
        }
        else
        {
            return SoundHandler.missing_sound;
        }
    }

    public void addSoundToEventPool(ISoundEventAccessor p_148727_1_)
    {
        this.soundPool.add(p_148727_1_);
    }

    public ResourceLocation getSoundEventLocation()
    {
        return this.soundLocation;
    }

    public SoundCategory getSoundCategory()
    {
        return this.category;
    }

    public Object cloneEntry()
    {
        return this.cloneEntry1();
    }
}
