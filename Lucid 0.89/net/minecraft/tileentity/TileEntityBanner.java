package net.minecraft.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityBanner extends TileEntity
{
    private int baseColor;

    /** A list of all the banner patterns. */
    private NBTTagList patterns;
    private boolean field_175119_g;

    /** A list of all patterns stored on this banner. */
    private List patternList;

    /** A list of all the color values stored on this banner. */
    private List colorList;

    /**
     * This is a String representation of this banners pattern and color lists, used for texture caching.
     */
    private String patternResourceLocation;

    public void setItemValues(ItemStack stack)
    {
        this.patterns = null;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound var2 = stack.getTagCompound().getCompoundTag("BlockEntityTag");

            if (var2.hasKey("Patterns"))
            {
                this.patterns = (NBTTagList)var2.getTagList("Patterns", 10).copy();
            }

            if (var2.hasKey("Base", 99))
            {
                this.baseColor = var2.getInteger("Base");
            }
            else
            {
                this.baseColor = stack.getMetadata() & 15;
            }
        }
        else
        {
            this.baseColor = stack.getMetadata() & 15;
        }

        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }

    @Override
	public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Base", this.baseColor);

        if (this.patterns != null)
        {
            compound.setTag("Patterns", this.patterns);
        }
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.baseColor = compound.getInteger("Base");
        this.patterns = compound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.field_175119_g = true;
    }

    /**
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    @Override
	public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 6, var1);
    }

    public int getBaseColor()
    {
        return this.baseColor;
    }

    public static int getBaseColor(ItemStack stack)
    {
        NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);
        return var1 != null && var1.hasKey("Base") ? var1.getInteger("Base") : stack.getMetadata();
    }

    /**
     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
     *  
     * @param stack The ItemStack which contains the NBTTagCompound data for banner patterns.
     */
    public static int getPatterns(ItemStack stack)
    {
        NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);
        return var1 != null && var1.hasKey("Patterns") ? var1.getTagList("Patterns", 10).tagCount() : 0;
    }

    /**
     * Retrieves the list of patterns for this tile entity. The banner data will be initialized/refreshed before this
     * happens.
     */
    public List getPatternList()
    {
        this.initializeBannerData();
        return this.patternList;
    }

    /**
     * Retrieves the list of colors for this tile entity. The banner data will be initialized/refreshed before this
     * happens.
     */
    public List getColorList()
    {
        this.initializeBannerData();
        return this.colorList;
    }

    public String func_175116_e()
    {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    /**
     * Establishes all of the basic properties for the banner. This will also apply the data from the tile entities nbt
     * tag compounds.
     */
    private void initializeBannerData()
    {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
        {
            if (!this.field_175119_g)
            {
                this.patternResourceLocation = "";
            }
            else
            {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(TileEntityBanner.EnumBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
                this.patternResourceLocation = "b" + this.baseColor;

                if (this.patterns != null)
                {
                    for (int var1 = 0; var1 < this.patterns.tagCount(); ++var1)
                    {
                        NBTTagCompound var2 = this.patterns.getCompoundTagAt(var1);
                        TileEntityBanner.EnumBannerPattern var3 = TileEntityBanner.EnumBannerPattern.getPatternByID(var2.getString("Pattern"));

                        if (var3 != null)
                        {
                            this.patternList.add(var3);
                            int var4 = var2.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(var4));
                            this.patternResourceLocation = this.patternResourceLocation + var3.getPatternID() + var4;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all the banner related data from a provided instance of ItemStack.
     *  
     * @param stack The instance of an ItemStack which will have the relevant nbt tags removed.
     */
    public static void removeBannerData(ItemStack stack)
    {
        NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);

        if (var1 != null && var1.hasKey("Patterns", 9))
        {
            NBTTagList var2 = var1.getTagList("Patterns", 10);

            if (var2.tagCount() > 0)
            {
                var2.removeTag(var2.tagCount() - 1);

                if (var2.hasNoTags())
                {
                    stack.getTagCompound().removeTag("BlockEntityTag");

                    if (stack.getTagCompound().hasNoTags())
                    {
                        stack.setTagCompound((NBTTagCompound)null);
                    }
                }
            }
        }
    }

    public static enum EnumBannerPattern
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

        private EnumBannerPattern(String p_i45670_1_, int p_i45670_2_, String name, String id)
        {
            this.craftingLayers = new String[3];
            this.patternName = name;
            this.patternID = id;
        }

        private EnumBannerPattern(String p_i45671_1_, int p_i45671_2_, String name, String id, ItemStack craftingItem)
        {
            this(p_i45671_1_, p_i45671_2_, name, id);
            this.patternCraftingStack = craftingItem;
        }

        private EnumBannerPattern(String p_i45672_1_, int p_i45672_2_, String name, String id, String craftingTop, String craftingMid, String craftingBot)
        {
            this(p_i45672_1_, p_i45672_2_, name, id);
            this.craftingLayers[0] = craftingTop;
            this.craftingLayers[1] = craftingMid;
            this.craftingLayers[2] = craftingBot;
        }

        public String getPatternName()
        {
            return this.patternName;
        }

        public String getPatternID()
        {
            return this.patternID;
        }

        public String[] getCraftingLayers()
        {
            return this.craftingLayers;
        }

        public boolean hasValidCrafting()
        {
            return this.patternCraftingStack != null || this.craftingLayers[0] != null;
        }

        public boolean hasCraftingStack()
        {
            return this.patternCraftingStack != null;
        }

        public ItemStack getCraftingStack()
        {
            return this.patternCraftingStack;
        }

        public static TileEntityBanner.EnumBannerPattern getPatternByID(String id)
        {
            TileEntityBanner.EnumBannerPattern[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                TileEntityBanner.EnumBannerPattern var4 = var1[var3];

                if (var4.patternID.equals(id))
                {
                    return var4;
                }
            }

            return null;
        }
    }
}
