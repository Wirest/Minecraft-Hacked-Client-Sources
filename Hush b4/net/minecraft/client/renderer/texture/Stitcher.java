// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.StitcherException;
import java.util.Arrays;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set setStitchHolders;
    private final List stitchSlots;
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final boolean forcePowerOf2;
    private final int maxTileDimension;
    private static final String __OBFID = "CL_00001054";
    
    public Stitcher(final int maxTextureWidth, final int maxTextureHeight, final boolean p_i45095_3_, final int p_i45095_4_, final int mipmapLevel) {
        this.setStitchHolders = Sets.newHashSetWithExpectedSize(256);
        this.stitchSlots = Lists.newArrayListWithCapacity(256);
        this.mipmapLevelStitcher = mipmapLevel;
        this.maxWidth = maxTextureWidth;
        this.maxHeight = maxTextureHeight;
        this.forcePowerOf2 = p_i45095_3_;
        this.maxTileDimension = p_i45095_4_;
    }
    
    public int getCurrentWidth() {
        return this.currentWidth;
    }
    
    public int getCurrentHeight() {
        return this.currentHeight;
    }
    
    public void addSprite(final TextureAtlasSprite p_110934_1_) {
        final Holder stitcher$holder = new Holder(p_110934_1_, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            stitcher$holder.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add(stitcher$holder);
    }
    
    public void doStitch() {
        final Holder[] astitcher$holder = this.setStitchHolders.toArray(new Holder[this.setStitchHolders.size()]);
        Arrays.sort(astitcher$holder);
        Holder[] array;
        for (int length = (array = astitcher$holder).length, i = 0; i < length; ++i) {
            final Holder stitcher$holder = array[i];
            if (!this.allocateSlot(stitcher$holder)) {
                final String s = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", stitcher$holder.getAtlasSprite().getIconName(), stitcher$holder.getAtlasSprite().getIconWidth(), stitcher$holder.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight);
                throw new StitcherException(stitcher$holder, s);
            }
        }
        if (this.forcePowerOf2) {
            this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
        }
    }
    
    public List getStichSlots() {
        final ArrayList arraylist = Lists.newArrayList();
        for (final Object stitcher$slot : this.stitchSlots) {
            ((Slot)stitcher$slot).getAllStitchSlots(arraylist);
        }
        final ArrayList arraylist2 = Lists.newArrayList();
        for (final Object stitcher$slot2 : arraylist) {
            final Slot stitcher$slot3 = (Slot)stitcher$slot2;
            final Holder stitcher$holder = stitcher$slot3.getStitchHolder();
            final TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
            textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot3.getOriginX(), stitcher$slot3.getOriginY(), stitcher$holder.isRotated());
            arraylist2.add(textureatlassprite);
        }
        return arraylist2;
    }
    
    private static int getMipmapDimension(final int p_147969_0_, final int p_147969_1_) {
        return (p_147969_0_ >> p_147969_1_) + (((p_147969_0_ & (1 << p_147969_1_) - 1) != 0x0) ? 1 : 0) << p_147969_1_;
    }
    
    private boolean allocateSlot(final Holder p_94310_1_) {
        for (int i = 0; i < this.stitchSlots.size(); ++i) {
            if (this.stitchSlots.get(i).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
            if (this.stitchSlots.get(i).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
        }
        return this.expandAndAllocateSlot(p_94310_1_);
    }
    
    private boolean expandAndAllocateSlot(final Holder p_94311_1_) {
        final int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        final boolean flag = this.currentWidth == 0 && this.currentHeight == 0;
        boolean flag6;
        if (this.forcePowerOf2) {
            final int j = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            final int k = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
            final int l = MathHelper.roundUpToPowerOfTwo(this.currentWidth + i);
            final int i2 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + i);
            final boolean flag2 = l <= this.maxWidth;
            final boolean flag3 = i2 <= this.maxHeight;
            if (!flag2 && !flag3) {
                return false;
            }
            final boolean flag4 = j != l;
            final boolean flag5 = k != i2;
            if (flag4 ^ flag5) {
                flag6 = !flag4;
            }
            else {
                flag6 = (flag2 && j <= k);
            }
        }
        else {
            final boolean flag7 = this.currentWidth + i <= this.maxWidth;
            final boolean flag8 = this.currentHeight + i <= this.maxHeight;
            if (!flag7 && !flag8) {
                return false;
            }
            flag6 = (flag7 && (flag || this.currentWidth <= this.currentHeight));
        }
        final int j2 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        if (MathHelper.roundUpToPowerOfTwo((flag6 ? this.currentWidth : this.currentHeight) + j2) > (flag6 ? this.maxWidth : this.maxHeight)) {
            return false;
        }
        Slot stitcher$slot;
        if (flag6) {
            if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
                p_94311_1_.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = p_94311_1_.getHeight();
            }
            stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
            this.currentWidth += p_94311_1_.getWidth();
        }
        else {
            stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
            this.currentHeight += p_94311_1_.getHeight();
        }
        stitcher$slot.addSlot(p_94311_1_);
        this.stitchSlots.add(stitcher$slot);
        return true;
    }
    
    public static class Holder implements Comparable
    {
        private final TextureAtlasSprite theTexture;
        private final int width;
        private final int height;
        private final int mipmapLevelHolder;
        private boolean rotated;
        private float scaleFactor;
        private static final String __OBFID = "CL_00001055";
        
        public Holder(final TextureAtlasSprite p_i45094_1_, final int p_i45094_2_) {
            this.scaleFactor = 1.0f;
            this.theTexture = p_i45094_1_;
            this.width = p_i45094_1_.getIconWidth();
            this.height = p_i45094_1_.getIconHeight();
            this.mipmapLevelHolder = p_i45094_2_;
            this.rotated = (getMipmapDimension(this.height, p_i45094_2_) > getMipmapDimension(this.width, p_i45094_2_));
        }
        
        public TextureAtlasSprite getAtlasSprite() {
            return this.theTexture;
        }
        
        public int getWidth() {
            return this.rotated ? getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder) : getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public int getHeight() {
            return this.rotated ? getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder) : getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public void rotate() {
            this.rotated = !this.rotated;
        }
        
        public boolean isRotated() {
            return this.rotated;
        }
        
        public void setNewDimension(final int p_94196_1_) {
            if (this.width > p_94196_1_ && this.height > p_94196_1_) {
                this.scaleFactor = p_94196_1_ / (float)Math.min(this.width, this.height);
            }
        }
        
        @Override
        public String toString() {
            return "Holder{width=" + this.width + ", height=" + this.height + ", name=" + this.theTexture.getIconName() + '}';
        }
        
        public int compareTo(final Holder p_compareTo_1_) {
            int i;
            if (this.getHeight() == p_compareTo_1_.getHeight()) {
                if (this.getWidth() == p_compareTo_1_.getWidth()) {
                    if (this.theTexture.getIconName() == null) {
                        return (p_compareTo_1_.theTexture.getIconName() == null) ? 0 : -1;
                    }
                    return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
                }
                else {
                    i = ((this.getWidth() < p_compareTo_1_.getWidth()) ? 1 : -1);
                }
            }
            else {
                i = ((this.getHeight() < p_compareTo_1_.getHeight()) ? 1 : -1);
            }
            return i;
        }
        
        @Override
        public int compareTo(final Object p_compareTo_1_) {
            return this.compareTo((Holder)p_compareTo_1_);
        }
    }
    
    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List subSlots;
        private Holder holder;
        private static final String __OBFID = "CL_00001056";
        
        public Slot(final int p_i1277_1_, final int p_i1277_2_, final int widthIn, final int heightIn) {
            this.originX = p_i1277_1_;
            this.originY = p_i1277_2_;
            this.width = widthIn;
            this.height = heightIn;
        }
        
        public Holder getStitchHolder() {
            return this.holder;
        }
        
        public int getOriginX() {
            return this.originX;
        }
        
        public int getOriginY() {
            return this.originY;
        }
        
        public boolean addSlot(final Holder holderIn) {
            if (this.holder != null) {
                return false;
            }
            final int i = holderIn.getWidth();
            final int j = holderIn.getHeight();
            if (i > this.width || j > this.height) {
                return false;
            }
            if (i == this.width && j == this.height) {
                this.holder = holderIn;
                return true;
            }
            if (this.subSlots == null) {
                (this.subSlots = Lists.newArrayListWithCapacity(1)).add(new Slot(this.originX, this.originY, i, j));
                final int k = this.width - i;
                final int l = this.height - j;
                if (l > 0 && k > 0) {
                    final int i2 = Math.max(this.height, k);
                    final int j2 = Math.max(this.width, l);
                    if (i2 >= j2) {
                        this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
                        this.subSlots.add(new Slot(this.originX + i, this.originY, k, this.height));
                    }
                    else {
                        this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
                        this.subSlots.add(new Slot(this.originX, this.originY + j, this.width, l));
                    }
                }
                else if (k == 0) {
                    this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
                }
                else if (l == 0) {
                    this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
                }
            }
            for (final Object stitcher$slot : this.subSlots) {
                if (((Slot)stitcher$slot).addSlot(holderIn)) {
                    return true;
                }
            }
            return false;
        }
        
        public void getAllStitchSlots(final List p_94184_1_) {
            if (this.holder != null) {
                p_94184_1_.add(this);
            }
            else if (this.subSlots != null) {
                for (final Object stitcher$slot : this.subSlots) {
                    ((Slot)stitcher$slot).getAllStitchSlots(p_94184_1_);
                }
            }
        }
        
        @Override
        public String toString() {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
        }
    }
}
