// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumDyeColor;
import java.util.List;
import net.minecraft.nbt.NBTTagList;

public class TileEntityBanner extends TileEntity
{
    private int baseColor;
    private NBTTagList patterns;
    private boolean field_175119_g;
    private List<EnumBannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;
    
    public void setItemValues(final ItemStack stack) {
        this.patterns = null;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if (nbttagcompound.hasKey("Patterns")) {
                this.patterns = (NBTTagList)nbttagcompound.getTagList("Patterns", 10).copy();
            }
            if (nbttagcompound.hasKey("Base", 99)) {
                this.baseColor = nbttagcompound.getInteger("Base");
            }
            else {
                this.baseColor = (stack.getMetadata() & 0xF);
            }
        }
        else {
            this.baseColor = (stack.getMetadata() & 0xF);
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        func_181020_a(compound, this.baseColor, this.patterns);
    }
    
    public static void func_181020_a(final NBTTagCompound p_181020_0_, final int p_181020_1_, final NBTTagList p_181020_2_) {
        p_181020_0_.setInteger("Base", p_181020_1_);
        if (p_181020_2_ != null) {
            p_181020_0_.setTag("Patterns", p_181020_2_);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.baseColor = compound.getInteger("Base");
        this.patterns = compound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.field_175119_g = true;
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }
    
    public int getBaseColor() {
        return this.baseColor;
    }
    
    public static int getBaseColor(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return (nbttagcompound != null && nbttagcompound.hasKey("Base")) ? nbttagcompound.getInteger("Base") : stack.getMetadata();
    }
    
    public static int getPatterns(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }
    
    public List<EnumBannerPattern> getPatternList() {
        this.initializeBannerData();
        return this.patternList;
    }
    
    public NBTTagList func_181021_d() {
        return this.patterns;
    }
    
    public List<EnumDyeColor> getColorList() {
        this.initializeBannerData();
        return this.colorList;
    }
    
    public String func_175116_e() {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }
    
    private void initializeBannerData() {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
            if (!this.field_175119_g) {
                this.patternResourceLocation = "";
            }
            else {
                this.patternList = (List<EnumBannerPattern>)Lists.newArrayList();
                this.colorList = (List<EnumDyeColor>)Lists.newArrayList();
                this.patternList.add(EnumBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
                this.patternResourceLocation = "b" + this.baseColor;
                if (this.patterns != null) {
                    for (int i = 0; i < this.patterns.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                        final EnumBannerPattern tileentitybanner$enumbannerpattern = EnumBannerPattern.getPatternByID(nbttagcompound.getString("Pattern"));
                        if (tileentitybanner$enumbannerpattern != null) {
                            this.patternList.add(tileentitybanner$enumbannerpattern);
                            final int j = nbttagcompound.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(j));
                            this.patternResourceLocation = String.valueOf(this.patternResourceLocation) + tileentitybanner$enumbannerpattern.getPatternID() + j;
                        }
                    }
                }
            }
        }
    }
    
    public static void removeBannerData(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            if (nbttaglist.tagCount() > 0) {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);
                if (nbttaglist.hasNoTags()) {
                    stack.getTagCompound().removeTag("BlockEntityTag");
                    if (stack.getTagCompound().hasNoTags()) {
                        stack.setTagCompound(null);
                    }
                }
            }
        }
    }
    
    public enum EnumBannerPattern
    {
        BASE("BASE", 0, "base", "b"), 
        SQUARE_BOTTOM_LEFT("SQUARE_BOTTOM_LEFT", 1, "square_bottom_left", "bl", "   ", "   ", "#  "), 
        SQUARE_BOTTOM_RIGHT("SQUARE_BOTTOM_RIGHT", 2, "square_bottom_right", "br", "   ", "   ", "  #"), 
        SQUARE_TOP_LEFT("SQUARE_TOP_LEFT", 3, "square_top_left", "tl", "#  ", "   ", "   "), 
        SQUARE_TOP_RIGHT("SQUARE_TOP_RIGHT", 4, "square_top_right", "tr", "  #", "   ", "   "), 
        STRIPE_BOTTOM("STRIPE_BOTTOM", 5, "stripe_bottom", "bs", "   ", "   ", "###"), 
        STRIPE_TOP("STRIPE_TOP", 6, "stripe_top", "ts", "###", "   ", "   "), 
        STRIPE_LEFT("STRIPE_LEFT", 7, "stripe_left", "ls", "#  ", "#  ", "#  "), 
        STRIPE_RIGHT("STRIPE_RIGHT", 8, "stripe_right", "rs", "  #", "  #", "  #"), 
        STRIPE_CENTER("STRIPE_CENTER", 9, "stripe_center", "cs", " # ", " # ", " # "), 
        STRIPE_MIDDLE("STRIPE_MIDDLE", 10, "stripe_middle", "ms", "   ", "###", "   "), 
        STRIPE_DOWNRIGHT("STRIPE_DOWNRIGHT", 11, "stripe_downright", "drs", "#  ", " # ", "  #"), 
        STRIPE_DOWNLEFT("STRIPE_DOWNLEFT", 12, "stripe_downleft", "dls", "  #", " # ", "#  "), 
        STRIPE_SMALL("STRIPE_SMALL", 13, "small_stripes", "ss", "# #", "# #", "   "), 
        CROSS("CROSS", 14, "cross", "cr", "# #", " # ", "# #"), 
        STRAIGHT_CROSS("STRAIGHT_CROSS", 15, "straight_cross", "sc", " # ", "###", " # "), 
        TRIANGLE_BOTTOM("TRIANGLE_BOTTOM", 16, "triangle_bottom", "bt", "   ", " # ", "# #"), 
        TRIANGLE_TOP("TRIANGLE_TOP", 17, "triangle_top", "tt", "# #", " # ", "   "), 
        TRIANGLES_BOTTOM("TRIANGLES_BOTTOM", 18, "triangles_bottom", "bts", "   ", "# #", " # "), 
        TRIANGLES_TOP("TRIANGLES_TOP", 19, "triangles_top", "tts", " # ", "# #", "   "), 
        DIAGONAL_LEFT("DIAGONAL_LEFT", 20, "diagonal_left", "ld", "## ", "#  ", "   "), 
        DIAGONAL_RIGHT("DIAGONAL_RIGHT", 21, "diagonal_up_right", "rd", "   ", "  #", " ##"), 
        DIAGONAL_LEFT_MIRROR("DIAGONAL_LEFT_MIRROR", 22, "diagonal_up_left", "lud", "   ", "#  ", "## "), 
        DIAGONAL_RIGHT_MIRROR("DIAGONAL_RIGHT_MIRROR", 23, "diagonal_right", "rud", " ##", "  #", "   "), 
        CIRCLE_MIDDLE("CIRCLE_MIDDLE", 24, "circle", "mc", "   ", " # ", "   "), 
        RHOMBUS_MIDDLE("RHOMBUS_MIDDLE", 25, "rhombus", "mr", " # ", "# #", " # "), 
        HALF_VERTICAL("HALF_VERTICAL", 26, "half_vertical", "vh", "## ", "## ", "## "), 
        HALF_HORIZONTAL("HALF_HORIZONTAL", 27, "half_horizontal", "hh", "###", "###", "   "), 
        HALF_VERTICAL_MIRROR("HALF_VERTICAL_MIRROR", 28, "half_vertical_right", "vhr", " ##", " ##", " ##"), 
        HALF_HORIZONTAL_MIRROR("HALF_HORIZONTAL_MIRROR", 29, "half_horizontal_bottom", "hhb", "   ", "###", "###"), 
        BORDER("BORDER", 30, "border", "bo", "###", "# #", "###"), 
        CURLY_BORDER("CURLY_BORDER", 31, "curly_border", "cbo", new ItemStack(Blocks.vine)), 
        CREEPER("CREEPER", 32, "creeper", "cre", new ItemStack(Items.skull, 1, 4)), 
        GRADIENT("GRADIENT", 33, "gradient", "gra", "# #", " # ", " # "), 
        GRADIENT_UP("GRADIENT_UP", 34, "gradient_up", "gru", " # ", " # ", "# #"), 
        BRICKS("BRICKS", 35, "bricks", "bri", new ItemStack(Blocks.brick_block)), 
        SKULL("SKULL", 36, "skull", "sku", new ItemStack(Items.skull, 1, 1)), 
        FLOWER("FLOWER", 37, "flower", "flo", new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())), 
        MOJANG("MOJANG", 38, "mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));
        
        private String patternName;
        private String patternID;
        private String[] craftingLayers;
        private ItemStack patternCraftingStack;
        
        private EnumBannerPattern(final String name2, final int ordinal, final String name, final String id) {
            this.craftingLayers = new String[3];
            this.patternName = name;
            this.patternID = id;
        }
        
        private EnumBannerPattern(final String s, final int n, final String name, final String id, final ItemStack craftingItem) {
            this(s, n, name, id);
            this.patternCraftingStack = craftingItem;
        }
        
        private EnumBannerPattern(final String s, final int n, final String name, final String id, final String craftingTop, final String craftingMid, final String craftingBot) {
            this(s, n, name, id);
            this.craftingLayers[0] = craftingTop;
            this.craftingLayers[1] = craftingMid;
            this.craftingLayers[2] = craftingBot;
        }
        
        public String getPatternName() {
            return this.patternName;
        }
        
        public String getPatternID() {
            return this.patternID;
        }
        
        public String[] getCraftingLayers() {
            return this.craftingLayers;
        }
        
        public boolean hasValidCrafting() {
            return this.patternCraftingStack != null || this.craftingLayers[0] != null;
        }
        
        public boolean hasCraftingStack() {
            return this.patternCraftingStack != null;
        }
        
        public ItemStack getCraftingStack() {
            return this.patternCraftingStack;
        }
        
        public static EnumBannerPattern getPatternByID(final String id) {
            EnumBannerPattern[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumBannerPattern tileentitybanner$enumbannerpattern = values[i];
                if (tileentitybanner$enumbannerpattern.patternID.equals(id)) {
                    return tileentitybanner$enumbannerpattern;
                }
            }
            return null;
        }
    }
}
