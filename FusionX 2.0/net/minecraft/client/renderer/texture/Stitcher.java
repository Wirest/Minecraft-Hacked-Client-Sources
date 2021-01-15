package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.MathHelper;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set setStitchHolders = Sets.newHashSetWithExpectedSize(256);
    private final List stitchSlots = Lists.newArrayListWithCapacity(256);
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final boolean forcePowerOf2;

    /** Max size (width or height) of a single tile */
    private final int maxTileDimension;
    private static final String __OBFID = "CL_00001054";

    public Stitcher(int p_i45095_1_, int p_i45095_2_, boolean p_i45095_3_, int p_i45095_4_, int p_i45095_5_)
    {
        this.mipmapLevelStitcher = p_i45095_5_;
        this.maxWidth = p_i45095_1_;
        this.maxHeight = p_i45095_2_;
        this.forcePowerOf2 = p_i45095_3_;
        this.maxTileDimension = p_i45095_4_;
    }

    public int getCurrentWidth()
    {
        return this.currentWidth;
    }

    public int getCurrentHeight()
    {
        return this.currentHeight;
    }

    public void addSprite(TextureAtlasSprite p_110934_1_)
    {
        Stitcher.Holder var2 = new Stitcher.Holder(p_110934_1_, this.mipmapLevelStitcher);

        if (this.maxTileDimension > 0)
        {
            var2.setNewDimension(this.maxTileDimension);
        }

        this.setStitchHolders.add(var2);
    }

    public void doStitch()
    {
        Stitcher.Holder[] var1 = (Stitcher.Holder[])((Stitcher.Holder[])this.setStitchHolders.toArray(new Stitcher.Holder[this.setStitchHolders.size()]));
        Arrays.sort(var1);
        Stitcher.Holder[] var2 = var1;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            Stitcher.Holder var5 = var2[var4];

            if (!this.allocateSlot(var5))
            {
                String var6 = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] {var5.getAtlasSprite().getIconName(), Integer.valueOf(var5.getAtlasSprite().getIconWidth()), Integer.valueOf(var5.getAtlasSprite().getIconHeight()), Integer.valueOf(this.currentWidth), Integer.valueOf(this.currentHeight), Integer.valueOf(this.maxWidth), Integer.valueOf(this.maxHeight)});
                throw new StitcherException(var5, var6);
            }
        }

        if (this.forcePowerOf2)
        {
            this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
        }
    }

    public List getStichSlots()
    {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.stitchSlots.iterator();

        while (var2.hasNext())
        {
            Stitcher.Slot var7 = (Stitcher.Slot)var2.next();
            var7.getAllStitchSlots(var1);
        }

        ArrayList var71 = Lists.newArrayList();
        Iterator var8 = var1.iterator();

        while (var8.hasNext())
        {
            Stitcher.Slot var4 = (Stitcher.Slot)var8.next();
            Stitcher.Holder var5 = var4.getStitchHolder();
            TextureAtlasSprite var6 = var5.getAtlasSprite();
            var6.initSprite(this.currentWidth, this.currentHeight, var4.getOriginX(), var4.getOriginY(), var5.isRotated());
            var71.add(var6);
        }

        return var71;
    }

    private static int getMipmapDimension(int p_147969_0_, int p_147969_1_)
    {
        return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
    }

    /**
     * Attempts to find space for specified tile
     */
    private boolean allocateSlot(Stitcher.Holder p_94310_1_)
    {
        for (int var2 = 0; var2 < this.stitchSlots.size(); ++var2)
        {
            if (((Stitcher.Slot)this.stitchSlots.get(var2)).addSlot(p_94310_1_))
            {
                return true;
            }

            p_94310_1_.rotate();

            if (((Stitcher.Slot)this.stitchSlots.get(var2)).addSlot(p_94310_1_))
            {
                return true;
            }

            p_94310_1_.rotate();
        }

        return this.expandAndAllocateSlot(p_94310_1_);
    }

    /**
     * Expand stitched texture in order to make space for specified tile
     */
    private boolean expandAndAllocateSlot(Stitcher.Holder p_94311_1_)
    {
        int var2 = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        boolean var3 = this.currentWidth == 0 && this.currentHeight == 0;
        boolean var4;
        int var5;

        if (this.forcePowerOf2)
        {
            var5 = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            int var15 = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
            int var14 = MathHelper.roundUpToPowerOfTwo(this.currentWidth + var2);
            int var8 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + var2);
            boolean var9 = var14 <= this.maxWidth;
            boolean var10 = var8 <= this.maxHeight;

            if (!var9 && !var10)
            {
                return false;
            }

            boolean var11 = var5 != var14;
            boolean var12 = var15 != var8;

            if (var11 ^ var12)
            {
                var4 = !var11;
            }
            else
            {
                var4 = var9 && var5 <= var15;
            }
        }
        else
        {
            boolean var151 = this.currentWidth + var2 <= this.maxWidth;
            boolean var141 = this.currentHeight + var2 <= this.maxHeight;

            if (!var151 && !var141)
            {
                return false;
            }

            var4 = var151 && (var3 || this.currentWidth <= this.currentHeight);
        }

        var5 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());

        if (MathHelper.roundUpToPowerOfTwo((!var4 ? this.currentHeight : this.currentWidth) + var5) > (!var4 ? this.maxHeight : this.maxWidth))
        {
            return false;
        }
        else
        {
            Stitcher.Slot var152;

            if (var4)
            {
                if (p_94311_1_.getWidth() > p_94311_1_.getHeight())
                {
                    p_94311_1_.rotate();
                }

                if (this.currentHeight == 0)
                {
                    this.currentHeight = p_94311_1_.getHeight();
                }

                var152 = new Stitcher.Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
                this.currentWidth += p_94311_1_.getWidth();
            }
            else
            {
                var152 = new Stitcher.Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
                this.currentHeight += p_94311_1_.getHeight();
            }

            var152.addSlot(p_94311_1_);
            this.stitchSlots.add(var152);
            return true;
        }
    }

    public static class Holder implements Comparable
    {
        private final TextureAtlasSprite theTexture;
        private final int width;
        private final int height;
        private final int mipmapLevelHolder;
        private boolean rotated;
        private float scaleFactor = 1.0F;
        private static final String __OBFID = "CL_00001055";

        public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_)
        {
            this.theTexture = p_i45094_1_;
            this.width = p_i45094_1_.getIconWidth();
            this.height = p_i45094_1_.getIconHeight();
            this.mipmapLevelHolder = p_i45094_2_;
            this.rotated = Stitcher.getMipmapDimension(this.height, p_i45094_2_) > Stitcher.getMipmapDimension(this.width, p_i45094_2_);
        }

        public TextureAtlasSprite getAtlasSprite()
        {
            return this.theTexture;
        }

        public int getWidth()
        {
            return this.rotated ? Stitcher.getMipmapDimension((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder);
        }

        public int getHeight()
        {
            return this.rotated ? Stitcher.getMipmapDimension((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder);
        }

        public void rotate()
        {
            this.rotated = !this.rotated;
        }

        public boolean isRotated()
        {
            return this.rotated;
        }

        public void setNewDimension(int p_94196_1_)
        {
            if (this.width > p_94196_1_ && this.height > p_94196_1_)
            {
                this.scaleFactor = (float)p_94196_1_ / (float)Math.min(this.width, this.height);
            }
        }

        public String toString()
        {
            return "Holder{width=" + this.width + ", height=" + this.height + ", name=" + this.theTexture.getIconName() + '}';
        }

        public int compareTo(Stitcher.Holder p_compareTo_1_)
        {
            int var2;

            if (this.getHeight() == p_compareTo_1_.getHeight())
            {
                if (this.getWidth() == p_compareTo_1_.getWidth())
                {
                    if (this.theTexture.getIconName() == null)
                    {
                        return p_compareTo_1_.theTexture.getIconName() == null ? 0 : -1;
                    }

                    return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
                }

                var2 = this.getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
            }
            else
            {
                var2 = this.getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
            }

            return var2;
        }

        public int compareTo(Object p_compareTo_1_)
        {
            return this.compareTo((Stitcher.Holder)p_compareTo_1_);
        }
    }

    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List subSlots;
        private Stitcher.Holder holder;
        private static final String __OBFID = "CL_00001056";

        public Slot(int p_i1277_1_, int p_i1277_2_, int p_i1277_3_, int p_i1277_4_)
        {
            this.originX = p_i1277_1_;
            this.originY = p_i1277_2_;
            this.width = p_i1277_3_;
            this.height = p_i1277_4_;
        }

        public Stitcher.Holder getStitchHolder()
        {
            return this.holder;
        }

        public int getOriginX()
        {
            return this.originX;
        }

        public int getOriginY()
        {
            return this.originY;
        }

        public boolean addSlot(Stitcher.Holder p_94182_1_)
        {
            if (this.holder != null)
            {
                return false;
            }
            else
            {
                int var2 = p_94182_1_.getWidth();
                int var3 = p_94182_1_.getHeight();

                if (var2 <= this.width && var3 <= this.height)
                {
                    if (var2 == this.width && var3 == this.height)
                    {
                        this.holder = p_94182_1_;
                        return true;
                    }
                    else
                    {
                        if (this.subSlots == null)
                        {
                            this.subSlots = Lists.newArrayListWithCapacity(1);
                            this.subSlots.add(new Stitcher.Slot(this.originX, this.originY, var2, var3));
                            int var8 = this.width - var2;
                            int var9 = this.height - var3;

                            if (var9 > 0 && var8 > 0)
                            {
                                int var6 = Math.max(this.height, var8);
                                int var7 = Math.max(this.width, var9);

                                if (var6 >= var7)
                                {
                                    this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + var3, var2, var9));
                                    this.subSlots.add(new Stitcher.Slot(this.originX + var2, this.originY, var8, this.height));
                                }
                                else
                                {
                                    this.subSlots.add(new Stitcher.Slot(this.originX + var2, this.originY, var8, var3));
                                    this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + var3, this.width, var9));
                                }
                            }
                            else if (var8 == 0)
                            {
                                this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + var3, var2, var9));
                            }
                            else if (var9 == 0)
                            {
                                this.subSlots.add(new Stitcher.Slot(this.originX + var2, this.originY, var8, var3));
                            }
                        }

                        Iterator var81 = this.subSlots.iterator();

                        while (var81.hasNext())
                        {
                            Stitcher.Slot var91 = (Stitcher.Slot)var81.next();

                            if (var91.addSlot(p_94182_1_))
                            {
                                return true;
                            }
                        }

                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }

        public void getAllStitchSlots(List p_94184_1_)
        {
            if (this.holder != null)
            {
                p_94184_1_.add(this);
            }
            else if (this.subSlots != null)
            {
                Iterator var2 = this.subSlots.iterator();

                while (var2.hasNext())
                {
                    Stitcher.Slot var3 = (Stitcher.Slot)var2.next();
                    var3.getAllStitchSlots(p_94184_1_);
                }
            }
        }

        public String toString()
        {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
        }
    }
}
