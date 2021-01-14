package net.minecraft.creativetab;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
    public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks") {
        private static final String __OBFID = "CL_00000006";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.brick_block);
        }
    };
    public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations") {
        private static final String __OBFID = "CL_00000010";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.double_plant);
        }

        @Override
        public int getIconItemDamage() {
            return BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a();
        }
    };
    public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone") {
        private static final String __OBFID = "CL_00000011";

        @Override
        public Item getTabIconItem() {
            return Items.redstone;
        }
    };
    public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation") {
        private static final String __OBFID = "CL_00000012";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.golden_rail);
        }
    };
    public static final CreativeTabs tabMisc = (new CreativeTabs(4, "misc") {
        private static final String __OBFID = "CL_00000014";

        @Override
        public Item getTabIconItem() {
            return Items.lava_bucket;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL});
    public static final CreativeTabs tabAllSearch = (new CreativeTabs(5, "search") {
        private static final String __OBFID = "CL_00000015";

        @Override
        public Item getTabIconItem() {
            return Items.compass;
        }
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabs(6, "food") {
        private static final String __OBFID = "CL_00000016";

        @Override
        public Item getTabIconItem() {
            return Items.apple;
        }
    };
    public static final CreativeTabs tabTools = (new CreativeTabs(7, "tools") {
        private static final String __OBFID = "CL_00000017";

        @Override
        public Item getTabIconItem() {
            return Items.iron_axe;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs tabCombat = (new CreativeTabs(8, "combat") {
        private static final String __OBFID = "CL_00000018";

        @Override
        public Item getTabIconItem() {
            return Items.golden_sword;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
    public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing") {
        private static final String __OBFID = "CL_00000007";

        @Override
        public Item getTabIconItem() {
            return Items.potionitem;
        }
    };
    public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials") {
        private static final String __OBFID = "CL_00000008";

        @Override
        public Item getTabIconItem() {
            return Items.stick;
        }
    };
    public static final CreativeTabs tabInventory = (new CreativeTabs(11, "inventory") {
        private static final String __OBFID = "CL_00000009";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.chest);
        }
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;

    /**
     * Texture to use.
     */
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;

    /**
     * Whether to draw the title in the foreground of the creative GUI
     */
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    private static final String __OBFID = "CL_00000005";

    public CreativeTabs(int index, String label) {
        tabIndex = index;
        tabLabel = label;
        CreativeTabs.creativeTabArray[index] = this;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public String getTabLabel() {
        return tabLabel;
    }

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel() {
        return "itemGroup." + getTabLabel();
    }

    public ItemStack getIconItemStack() {
        if (iconItemStack == null) {
            iconItemStack = new ItemStack(getTabIconItem(), 1, getIconItemDamage());
        }

        return iconItemStack;
    }

    public abstract Item getTabIconItem();

    public int getIconItemDamage() {
        return 0;
    }

    public String getBackgroundImageName() {
        return theTexture;
    }

    public CreativeTabs setBackgroundImageName(String texture) {
        theTexture = texture;
        return this;
    }

    public boolean drawInForegroundOfTab() {
        return drawTitle;
    }

    public CreativeTabs setNoTitle() {
        drawTitle = false;
        return this;
    }

    public boolean shouldHidePlayerInventory() {
        return hasScrollbar;
    }

    public CreativeTabs setNoScrollbar() {
        hasScrollbar = false;
        return this;
    }

    /**
     * returns index % 6
     */
    public int getTabColumn() {
        return tabIndex % 6;
    }

    /**
     * returns tabIndex < 6
     */
    public boolean isTabInFirstRow() {
        return tabIndex < 6;
    }

    /**
     * Returns the enchantment types relevant to this tab
     */
    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return enchantmentTypes;
    }

    /**
     * Sets the enchantment types for populating this tab with enchanting books
     */
    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types) {
        enchantmentTypes = types;
        return this;
    }

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType p_111226_1_) {
        if (enchantmentTypes == null) {
            return false;
        } else {
            EnumEnchantmentType[] var2 = enchantmentTypes;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                EnumEnchantmentType var5 = var2[var4];

                if (var5 == p_111226_1_) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * only shows items which have tabToDisplayOn == this
     */
    public void displayAllReleventItems(List p_78018_1_) {
        Iterator var2 = Item.itemRegistry.iterator();

        while (var2.hasNext()) {
            Item var3 = (Item) var2.next();

            if (var3 != null && var3.getCreativeTab() == this) {
                var3.getSubItems(var3, this, p_78018_1_);
            }
        }

        if (getRelevantEnchantmentTypes() != null) {
            addEnchantmentBooksToList(p_78018_1_, getRelevantEnchantmentTypes());
        }
    }

    /**
     * Adds the enchantment books from the supplied EnumEnchantmentType to the
     * given list.
     */
    public void addEnchantmentBooksToList(List p_92116_1_, EnumEnchantmentType... p_92116_2_) {
        Enchantment[] var3 = Enchantment.enchantmentsList;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Enchantment var6 = var3[var5];

            if (var6 != null && var6.type != null) {
                boolean var7 = false;

                for (int var8 = 0; var8 < p_92116_2_.length && !var7; ++var8) {
                    if (var6.type == p_92116_2_[var8]) {
                        var7 = true;
                    }
                }

                if (var7) {
                    p_92116_1_.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var6, var6.getMaxLevel())));
                }
            }
        }
    }
}
