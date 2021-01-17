// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.creativetab;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class CreativeTabs
{
    public static final CreativeTabs[] creativeTabArray;
    public static final CreativeTabs tabBlock;
    public static final CreativeTabs tabDecorations;
    public static final CreativeTabs tabRedstone;
    public static final CreativeTabs tabTransport;
    public static final CreativeTabs tabMisc;
    public static final CreativeTabs tabAllSearch;
    public static final CreativeTabs tabFood;
    public static final CreativeTabs tabTools;
    public static final CreativeTabs tabCombat;
    public static final CreativeTabs tabBrewing;
    public static final CreativeTabs tabMaterials;
    public static final CreativeTabs tabInventory;
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture;
    private boolean hasScrollbar;
    private boolean drawTitle;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    
    static {
        creativeTabArray = new CreativeTabs[12];
        tabBlock = new CreativeTabs("buildingBlocks") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.brick_block);
            }
        };
        tabDecorations = new CreativeTabs("decorations") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.double_plant);
            }
            
            @Override
            public int getIconItemDamage() {
                return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
            }
        };
        tabRedstone = new CreativeTabs("redstone") {
            @Override
            public Item getTabIconItem() {
                return Items.redstone;
            }
        };
        tabTransport = new CreativeTabs("transportation") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.golden_rail);
            }
        };
        tabMisc = new CreativeTabs("misc") {
            @Override
            public Item getTabIconItem() {
                return Items.lava_bucket;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL);
        tabAllSearch = new CreativeTabs("search") {
            @Override
            public Item getTabIconItem() {
                return Items.compass;
            }
        }.setBackgroundImageName("item_search.png");
        tabFood = new CreativeTabs("food") {
            @Override
            public Item getTabIconItem() {
                return Items.apple;
            }
        };
        tabTools = new CreativeTabs("tools") {
            @Override
            public Item getTabIconItem() {
                return Items.iron_axe;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
        tabCombat = new CreativeTabs("combat") {
            @Override
            public Item getTabIconItem() {
                return Items.golden_sword;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON);
        tabBrewing = new CreativeTabs("brewing") {
            @Override
            public Item getTabIconItem() {
                return Items.potionitem;
            }
        };
        tabMaterials = new CreativeTabs("materials") {
            @Override
            public Item getTabIconItem() {
                return Items.stick;
            }
        };
        tabInventory = new CreativeTabs("inventory") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.chest);
            }
        }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    }
    
    public CreativeTabs(final int index, final String label) {
        this.theTexture = "items.png";
        this.hasScrollbar = true;
        this.drawTitle = true;
        this.tabIndex = index;
        this.tabLabel = label;
        CreativeTabs.creativeTabArray[index] = this;
    }
    
    public int getTabIndex() {
        return this.tabIndex;
    }
    
    public String getTabLabel() {
        return this.tabLabel;
    }
    
    public String getTranslatedTabLabel() {
        return "itemGroup." + this.getTabLabel();
    }
    
    public ItemStack getIconItemStack() {
        if (this.iconItemStack == null) {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }
        return this.iconItemStack;
    }
    
    public abstract Item getTabIconItem();
    
    public int getIconItemDamage() {
        return 0;
    }
    
    public String getBackgroundImageName() {
        return this.theTexture;
    }
    
    public CreativeTabs setBackgroundImageName(final String texture) {
        this.theTexture = texture;
        return this;
    }
    
    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }
    
    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }
    
    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }
    
    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }
    
    public int getTabColumn() {
        return this.tabIndex % 6;
    }
    
    public boolean isTabInFirstRow() {
        return this.tabIndex < 6;
    }
    
    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }
    
    public CreativeTabs setRelevantEnchantmentTypes(final EnumEnchantmentType... types) {
        this.enchantmentTypes = types;
        return this;
    }
    
    public boolean hasRelevantEnchantmentType(final EnumEnchantmentType enchantmentType) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        EnumEnchantmentType[] enchantmentTypes;
        for (int length = (enchantmentTypes = this.enchantmentTypes).length, i = 0; i < length; ++i) {
            final EnumEnchantmentType enumenchantmenttype = enchantmentTypes[i];
            if (enumenchantmenttype == enchantmentType) {
                return true;
            }
        }
        return false;
    }
    
    public void displayAllReleventItems(final List<ItemStack> p_78018_1_) {
        for (final Item item : Item.itemRegistry) {
            if (item != null && item.getCreativeTab() == this) {
                item.getSubItems(item, this, p_78018_1_);
            }
        }
        if (this.getRelevantEnchantmentTypes() != null) {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }
    
    public void addEnchantmentBooksToList(final List<ItemStack> itemList, final EnumEnchantmentType... enchantmentType) {
        Enchantment[] enchantmentsBookList;
        for (int length = (enchantmentsBookList = Enchantment.enchantmentsBookList).length, j = 0; j < length; ++j) {
            final Enchantment enchantment = enchantmentsBookList[j];
            if (enchantment != null && enchantment.type != null) {
                boolean flag = false;
                for (int i = 0; i < enchantmentType.length && !flag; ++i) {
                    if (enchantment.type == enchantmentType[i]) {
                        flag = true;
                    }
                }
                if (flag) {
                    itemList.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
        }
    }
}
