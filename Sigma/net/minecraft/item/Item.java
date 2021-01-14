package net.minecraft.item;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Item {
    public static final RegistryNamespaced itemRegistry = new RegistryNamespaced();
    private static final Map BLOCK_TO_ITEM = Maps.newHashMap();
    protected static final UUID itemModifierUUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private CreativeTabs tabToDisplayOn;

    /**
     * The RNG used by the Item subclasses.
     */
    protected static Random itemRand = new Random();

    /**
     * Maximum size of the stack.
     */
    protected int maxStackSize = 64;

    /**
     * Maximum damage an item can handle.
     */
    private int maxDamage;

    /**
     * If true, render the object in full 3D, like weapons and tools.
     */
    protected boolean bFull3D;

    /**
     * Some items (like dyes) have multiple subtypes on same item, this is field
     * define this behavior
     */
    protected boolean hasSubtypes;
    private Item containerItem;

    /**
     * The string representing this item's effect on a potion when used as an
     * ingredient.
     */
    private String potionEffect;

    /**
     * The unlocalized name of this item.
     */
    private String unlocalizedName;
    private static final String __OBFID = "CL_00000041";

    public static int getIdFromItem(Item itemIn) {
        return itemIn == null ? 0 : Item.itemRegistry.getIDForObject(itemIn);
    }

    public static Item getItemById(int id) {
        return (Item) Item.itemRegistry.getObjectById(id);
    }

    public static Item getItemFromBlock(Block blockIn) {
        return (Item) Item.BLOCK_TO_ITEM.get(blockIn);
    }

    /**
     * Tries to get an Item by it's name (e.g. minecraft:apple) or a String
     * representation of a numerical ID. If both fail, null is returned.
     */
    public static Item getByNameOrId(String id) {
        Item var1 = (Item) Item.itemRegistry.getObject(new ResourceLocation(id));

        if (var1 == null) {
            try {
                return Item.getItemById(Integer.parseInt(id));
            } catch (NumberFormatException var3) {
                ;
            }
        }

        return var1;
    }

    /**
     * Called when an ItemStack with NBT data is read to potentially that
     * ItemStack's NBT data
     */
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        return false;
    }

    public Item setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
        return 1.0F;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is
     * pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        return itemStackIn;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.).
     * Not called when the player stops using the Item before the action is
     * complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        return stack;
    }

    /**
     * Returns the maximum size of the stack for a specific item. *Isn't this
     * more a Set than a Get?*
     */
    public int getItemStackLimit() {
        return maxStackSize;
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be
     * placed in the world when this Item is placed as a Block (mostly used with
     * ItemBlocks).
     */
    public int getMetadata(int damage) {
        return 0;
    }

    public boolean getHasSubtypes() {
        return hasSubtypes;
    }

    protected Item setHasSubtypes(boolean hasSubtypes) {
        this.hasSubtypes = hasSubtypes;
        return this;
    }

    /**
     * Returns the maximum damage an item can take.
     */
    public int getMaxDamage() {
        return maxDamage;
    }

    /**
     * set max damage of an Item
     */
    protected Item setMaxDamage(int maxDurability) {
        maxDamage = maxDurability;
        return this;
    }

    public boolean isDamageable() {
        return maxDamage > 0 && !hasSubtypes;
    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside ev. They just raise the damage on the stack.
     *
     * @param target   The Entity being hit
     * @param attacker the attacking entity
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger
     * the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        return false;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(Block blockIn) {
        return false;
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on
     * sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        return false;
    }

    /**
     * Sets bFull3D to True and return the object.
     */
    public Item setFull3D() {
        bFull3D = true;
        return this;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D() {
        return bFull3D;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y
     * axis when being held in an entities hands.
     */
    public boolean shouldRotateAroundWhenRendering() {
        return false;
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the
     * parameter, prefixed by "item."
     */
    public Item setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    /**
     * Translates the unlocalized name of this item, but without the .name
     * suffix, so the translation fails and the unlocalized name itself is
     * returned.
     */
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        String var2 = this.getUnlocalizedName(stack);
        return var2 == null ? "" : StatCollector.translateToLocal(var2);
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getUnlocalizedName() {
        return "item." + unlocalizedName;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + unlocalizedName;
    }

    public Item setContainerItem(Item containerItem) {
        this.containerItem = containerItem;
        return this;
    }

    /**
     * If this function returns true (or the item is damageable), the
     * ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag() {
        return true;
    }

    public Item getContainerItem() {
        return containerItem;
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean hasContainerItem() {
        return containerItem != null;
    }

    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return 16777215;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps
     * to check if is on a player hand and update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    }

    /**
     * false for all Items except sub-classes of ItemMapBase
     */
    public boolean isMap() {
        return false;
    }

    /**
     * returns the action that specifies what animation to play when the items
     * is being used
     */
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse
     * button).
     *
     * @param timeLeft The amount of ticks left before the using would have been
     *                 complete
     */
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
    }

    /**
     * Sets the string representing this item's effect on a potion when used as
     * an ingredient.
     */
    protected Item setPotionEffect(String potionEffect) {
        this.potionEffect = potionEffect;
        return this;
    }

    public String getPotionEffect(ItemStack stack) {
        return potionEffect;
    }

    public boolean isPotionIngredient(ItemStack stack) {
        return getPotionEffect(stack) != null;
    }

    /**
     * allows items to add custom lines of information to the mouseover
     * description
     *
     * @param tooltip  All lines to display in the Item's tooltip. This is a List of
     *                 Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return ("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name")).trim();
    }

    public boolean hasEffect(ItemStack stack) {
        return stack.isItemEnchanted();
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isItemTool(ItemStack stack) {
        return getItemStackLimit() == 1 && isDamageable();
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float var4 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch);
        float var5 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw);
        double var6 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX);
        double var8 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) + playerIn.getEyeHeight();
        double var10 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ);
        Vec3 var12 = new Vec3(var6, var8, var10);
        float var13 = MathHelper.cos(-var5 * 0.017453292F - (float) Math.PI);
        float var14 = MathHelper.sin(-var5 * 0.017453292F - (float) Math.PI);
        float var15 = -MathHelper.cos(-var4 * 0.017453292F);
        float var16 = MathHelper.sin(-var4 * 0.017453292F);
        float var17 = var14 * var15;
        float var19 = var13 * var15;
        double var20 = 5.0D;
        Vec3 var22 = var12.addVector(var17 * var20, var16 * var20, var19 * var20);
        return worldIn.rayTraceBlocks(var12, var22, useLiquids, !useLiquids, false);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based
     * on material.
     */
    public int getItemEnchantability() {
        return 0;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items)
     *
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
    }

    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getCreativeTab() {
        return tabToDisplayOn;
    }

    /**
     * returns this;
     */
    public Item setCreativeTab(CreativeTabs tab) {
        tabToDisplayOn = tab;
        return this;
    }

    /**
     * Returns true if players can use this item to affect the world (e.g.
     * placing blocks, placing ender eyes in portal) when not in creative
     */
    public boolean canItemEditBlocks() {
        return false;
    }

    /**
     * Return whether this item is repairable in an anvil.
     *
     * @param toRepair The ItemStack to be repaired
     * @param repair   The ItemStack that should repair this Item (leather for
     *                 leather armor, etc.)
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    public Multimap getItemAttributeModifiers() {
        return HashMultimap.create();
    }

    public static void registerItems() {
        Item.registerItemBlock(Blocks.stone, (new ItemMultiTexture(Blocks.stone, Blocks.stone, new Function() {
            private static final String __OBFID = "CL_00002178";

            public String apply(ItemStack stack) {
                return BlockStone.EnumType.getStateFromMeta(stack.getMetadata()).func_176644_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("stone"));
        Item.registerItemBlock(Blocks.grass, new ItemColored(Blocks.grass, false));
        Item.registerItemBlock(Blocks.dirt, (new ItemMultiTexture(Blocks.dirt, Blocks.dirt, new Function() {
            private static final String __OBFID = "CL_00002169";

            public String apply(ItemStack stack) {
                return BlockDirt.DirtType.byMetadata(stack.getMetadata()).getUnlocalizedName();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("dirt"));
        Item.registerItemBlock(Blocks.cobblestone);
        Item.registerItemBlock(Blocks.planks, (new ItemMultiTexture(Blocks.planks, Blocks.planks, new Function() {
            private static final String __OBFID = "CL_00002168";

            public String apply(ItemStack stack) {
                return BlockPlanks.EnumType.func_176837_a(stack.getMetadata()).func_176840_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("wood"));
        Item.registerItemBlock(Blocks.sapling, (new ItemMultiTexture(Blocks.sapling, Blocks.sapling, new Function() {
            private static final String __OBFID = "CL_00002167";

            public String apply(ItemStack stack) {
                return BlockPlanks.EnumType.func_176837_a(stack.getMetadata()).func_176840_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("sapling"));
        Item.registerItemBlock(Blocks.bedrock);
        Item.registerItemBlock(Blocks.sand, (new ItemMultiTexture(Blocks.sand, Blocks.sand, new Function() {
            private static final String __OBFID = "CL_00002166";

            public String apply(ItemStack stack) {
                return BlockSand.EnumType.func_176686_a(stack.getMetadata()).func_176685_d();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("sand"));
        Item.registerItemBlock(Blocks.gravel);
        Item.registerItemBlock(Blocks.gold_ore);
        Item.registerItemBlock(Blocks.iron_ore);
        Item.registerItemBlock(Blocks.coal_ore);
        Item.registerItemBlock(Blocks.log, (new ItemMultiTexture(Blocks.log, Blocks.log, new Function() {
            private static final String __OBFID = "CL_00002165";

            public String apply(ItemStack stack) {
                return BlockPlanks.EnumType.func_176837_a(stack.getMetadata()).func_176840_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("log"));
        Item.registerItemBlock(Blocks.log2, (new ItemMultiTexture(Blocks.log2, Blocks.log2, new Function() {
            private static final String __OBFID = "CL_00002164";

            public String apply(ItemStack stack) {
                return BlockPlanks.EnumType.func_176837_a(stack.getMetadata() + 4).func_176840_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("log"));
        Item.registerItemBlock(Blocks.leaves, (new ItemLeaves(Blocks.leaves)).setUnlocalizedName("leaves"));
        Item.registerItemBlock(Blocks.leaves2, (new ItemLeaves(Blocks.leaves2)).setUnlocalizedName("leaves"));
        Item.registerItemBlock(Blocks.sponge, (new ItemMultiTexture(Blocks.sponge, Blocks.sponge, new Function() {
            private static final String __OBFID = "CL_00002163";

            public String apply(ItemStack stack) {
                return (stack.getMetadata() & 1) == 1 ? "wet" : "dry";
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("sponge"));
        Item.registerItemBlock(Blocks.glass);
        Item.registerItemBlock(Blocks.lapis_ore);
        Item.registerItemBlock(Blocks.lapis_block);
        Item.registerItemBlock(Blocks.dispenser);
        Item.registerItemBlock(Blocks.sandstone, (new ItemMultiTexture(Blocks.sandstone, Blocks.sandstone, new Function() {
            private static final String __OBFID = "CL_00002162";

            public String apply(ItemStack stack) {
                return BlockSandStone.EnumType.func_176673_a(stack.getMetadata()).func_176676_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("sandStone"));
        Item.registerItemBlock(Blocks.noteblock);
        Item.registerItemBlock(Blocks.golden_rail);
        Item.registerItemBlock(Blocks.detector_rail);
        Item.registerItemBlock(Blocks.sticky_piston, new ItemPiston(Blocks.sticky_piston));
        Item.registerItemBlock(Blocks.web);
        Item.registerItemBlock(Blocks.tallgrass, (new ItemColored(Blocks.tallgrass, true)).func_150943_a(new String[]{"shrub", "grass", "fern"}));
        Item.registerItemBlock(Blocks.deadbush);
        Item.registerItemBlock(Blocks.piston, new ItemPiston(Blocks.piston));
        Item.registerItemBlock(Blocks.wool, (new ItemCloth(Blocks.wool)).setUnlocalizedName("cloth"));
        Item.registerItemBlock(Blocks.yellow_flower, (new ItemMultiTexture(Blocks.yellow_flower, Blocks.yellow_flower, new Function() {
            private static final String __OBFID = "CL_00002177";

            public String apply(ItemStack stack) {
                return BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.YELLOW, stack.getMetadata()).func_176963_d();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("flower"));
        Item.registerItemBlock(Blocks.red_flower, (new ItemMultiTexture(Blocks.red_flower, Blocks.red_flower, new Function() {
            private static final String __OBFID = "CL_00002176";

            public String apply(ItemStack stack) {
                return BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.RED, stack.getMetadata()).func_176963_d();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("rose"));
        Item.registerItemBlock(Blocks.brown_mushroom);
        Item.registerItemBlock(Blocks.red_mushroom);
        Item.registerItemBlock(Blocks.gold_block);
        Item.registerItemBlock(Blocks.iron_block);
        Item.registerItemBlock(Blocks.stone_slab, (new ItemSlab(Blocks.stone_slab, Blocks.stone_slab, Blocks.double_stone_slab)).setUnlocalizedName("stoneSlab"));
        Item.registerItemBlock(Blocks.brick_block);
        Item.registerItemBlock(Blocks.tnt);
        Item.registerItemBlock(Blocks.bookshelf);
        Item.registerItemBlock(Blocks.mossy_cobblestone);
        Item.registerItemBlock(Blocks.obsidian);
        Item.registerItemBlock(Blocks.torch);
        Item.registerItemBlock(Blocks.mob_spawner);
        Item.registerItemBlock(Blocks.oak_stairs);
        Item.registerItemBlock(Blocks.chest);
        Item.registerItemBlock(Blocks.diamond_ore);
        Item.registerItemBlock(Blocks.diamond_block);
        Item.registerItemBlock(Blocks.crafting_table);
        Item.registerItemBlock(Blocks.farmland);
        Item.registerItemBlock(Blocks.furnace);
        Item.registerItemBlock(Blocks.lit_furnace);
        Item.registerItemBlock(Blocks.ladder);
        Item.registerItemBlock(Blocks.rail);
        Item.registerItemBlock(Blocks.stone_stairs);
        Item.registerItemBlock(Blocks.lever);
        Item.registerItemBlock(Blocks.stone_pressure_plate);
        Item.registerItemBlock(Blocks.wooden_pressure_plate);
        Item.registerItemBlock(Blocks.redstone_ore);
        Item.registerItemBlock(Blocks.redstone_torch);
        Item.registerItemBlock(Blocks.stone_button);
        Item.registerItemBlock(Blocks.snow_layer, new ItemSnow(Blocks.snow_layer));
        Item.registerItemBlock(Blocks.ice);
        Item.registerItemBlock(Blocks.snow);
        Item.registerItemBlock(Blocks.cactus);
        Item.registerItemBlock(Blocks.clay);
        Item.registerItemBlock(Blocks.jukebox);
        Item.registerItemBlock(Blocks.oak_fence);
        Item.registerItemBlock(Blocks.spruce_fence);
        Item.registerItemBlock(Blocks.birch_fence);
        Item.registerItemBlock(Blocks.jungle_fence);
        Item.registerItemBlock(Blocks.dark_oak_fence);
        Item.registerItemBlock(Blocks.acacia_fence);
        Item.registerItemBlock(Blocks.pumpkin);
        Item.registerItemBlock(Blocks.netherrack);
        Item.registerItemBlock(Blocks.soul_sand);
        Item.registerItemBlock(Blocks.glowstone);
        Item.registerItemBlock(Blocks.lit_pumpkin);
        Item.registerItemBlock(Blocks.trapdoor);
        Item.registerItemBlock(Blocks.monster_egg, (new ItemMultiTexture(Blocks.monster_egg, Blocks.monster_egg, new Function() {
            private static final String __OBFID = "CL_00002175";

            public String apply(ItemStack stack) {
                return BlockSilverfish.EnumType.func_176879_a(stack.getMetadata()).func_176882_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("monsterStoneEgg"));
        Item.registerItemBlock(Blocks.stonebrick, (new ItemMultiTexture(Blocks.stonebrick, Blocks.stonebrick, new Function() {
            private static final String __OBFID = "CL_00002174";

            public String apply(ItemStack stack) {
                return BlockStoneBrick.EnumType.getStateFromMeta(stack.getMetadata()).getVariantName();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("stonebricksmooth"));
        Item.registerItemBlock(Blocks.brown_mushroom_block);
        Item.registerItemBlock(Blocks.red_mushroom_block);
        Item.registerItemBlock(Blocks.iron_bars);
        Item.registerItemBlock(Blocks.glass_pane);
        Item.registerItemBlock(Blocks.melon_block);
        Item.registerItemBlock(Blocks.vine, new ItemColored(Blocks.vine, false));
        Item.registerItemBlock(Blocks.oak_fence_gate);
        Item.registerItemBlock(Blocks.spruce_fence_gate);
        Item.registerItemBlock(Blocks.birch_fence_gate);
        Item.registerItemBlock(Blocks.jungle_fence_gate);
        Item.registerItemBlock(Blocks.dark_oak_fence_gate);
        Item.registerItemBlock(Blocks.acacia_fence_gate);
        Item.registerItemBlock(Blocks.brick_stairs);
        Item.registerItemBlock(Blocks.stone_brick_stairs);
        Item.registerItemBlock(Blocks.mycelium);
        Item.registerItemBlock(Blocks.waterlily, new ItemLilyPad(Blocks.waterlily));
        Item.registerItemBlock(Blocks.nether_brick);
        Item.registerItemBlock(Blocks.nether_brick_fence);
        Item.registerItemBlock(Blocks.nether_brick_stairs);
        Item.registerItemBlock(Blocks.enchanting_table);
        Item.registerItemBlock(Blocks.end_portal_frame);
        Item.registerItemBlock(Blocks.end_stone);
        Item.registerItemBlock(Blocks.dragon_egg);
        Item.registerItemBlock(Blocks.redstone_lamp);
        Item.registerItemBlock(Blocks.wooden_slab, (new ItemSlab(Blocks.wooden_slab, Blocks.wooden_slab, Blocks.double_wooden_slab)).setUnlocalizedName("woodSlab"));
        Item.registerItemBlock(Blocks.sandstone_stairs);
        Item.registerItemBlock(Blocks.emerald_ore);
        Item.registerItemBlock(Blocks.ender_chest);
        Item.registerItemBlock(Blocks.tripwire_hook);
        Item.registerItemBlock(Blocks.emerald_block);
        Item.registerItemBlock(Blocks.spruce_stairs);
        Item.registerItemBlock(Blocks.birch_stairs);
        Item.registerItemBlock(Blocks.jungle_stairs);
        Item.registerItemBlock(Blocks.command_block);
        Item.registerItemBlock(Blocks.beacon);
        Item.registerItemBlock(Blocks.cobblestone_wall, (new ItemMultiTexture(Blocks.cobblestone_wall, Blocks.cobblestone_wall, new Function() {
            private static final String __OBFID = "CL_00002173";

            public String apply(ItemStack stack) {
                return BlockWall.EnumType.func_176660_a(stack.getMetadata()).func_176659_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("cobbleWall"));
        Item.registerItemBlock(Blocks.wooden_button);
        Item.registerItemBlock(Blocks.anvil, (new ItemAnvilBlock(Blocks.anvil)).setUnlocalizedName("anvil"));
        Item.registerItemBlock(Blocks.trapped_chest);
        Item.registerItemBlock(Blocks.light_weighted_pressure_plate);
        Item.registerItemBlock(Blocks.heavy_weighted_pressure_plate);
        Item.registerItemBlock(Blocks.daylight_detector);
        Item.registerItemBlock(Blocks.redstone_block);
        Item.registerItemBlock(Blocks.quartz_ore);
        Item.registerItemBlock(Blocks.hopper);
        Item.registerItemBlock(Blocks.quartz_block, (new ItemMultiTexture(Blocks.quartz_block, Blocks.quartz_block, new String[]{"default", "chiseled", "lines"})).setUnlocalizedName("quartzBlock"));
        Item.registerItemBlock(Blocks.quartz_stairs);
        Item.registerItemBlock(Blocks.activator_rail);
        Item.registerItemBlock(Blocks.dropper);
        Item.registerItemBlock(Blocks.stained_hardened_clay, (new ItemCloth(Blocks.stained_hardened_clay)).setUnlocalizedName("clayHardenedStained"));
        Item.registerItemBlock(Blocks.barrier);
        Item.registerItemBlock(Blocks.iron_trapdoor);
        Item.registerItemBlock(Blocks.hay_block);
        Item.registerItemBlock(Blocks.carpet, (new ItemCloth(Blocks.carpet)).setUnlocalizedName("woolCarpet"));
        Item.registerItemBlock(Blocks.hardened_clay);
        Item.registerItemBlock(Blocks.coal_block);
        Item.registerItemBlock(Blocks.packed_ice);
        Item.registerItemBlock(Blocks.acacia_stairs);
        Item.registerItemBlock(Blocks.dark_oak_stairs);
        Item.registerItemBlock(Blocks.slime_block);
        Item.registerItemBlock(Blocks.double_plant, (new ItemDoublePlant(Blocks.double_plant, Blocks.double_plant, new Function() {
            private static final String __OBFID = "CL_00002172";

            public String apply(ItemStack stack) {
                return BlockDoublePlant.EnumPlantType.func_176938_a(stack.getMetadata()).func_176939_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("doublePlant"));
        Item.registerItemBlock(Blocks.stained_glass, (new ItemCloth(Blocks.stained_glass)).setUnlocalizedName("stainedGlass"));
        Item.registerItemBlock(Blocks.stained_glass_pane, (new ItemCloth(Blocks.stained_glass_pane)).setUnlocalizedName("stainedGlassPane"));
        Item.registerItemBlock(Blocks.prismarine, (new ItemMultiTexture(Blocks.prismarine, Blocks.prismarine, new Function() {
            private static final String __OBFID = "CL_00002171";

            public String apply(ItemStack stack) {
                return BlockPrismarine.EnumType.func_176810_a(stack.getMetadata()).func_176809_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("prismarine"));
        Item.registerItemBlock(Blocks.sea_lantern);
        Item.registerItemBlock(Blocks.red_sandstone, (new ItemMultiTexture(Blocks.red_sandstone, Blocks.red_sandstone, new Function() {
            private static final String __OBFID = "CL_00002170";

            public String apply(ItemStack stack) {
                return BlockRedSandstone.EnumType.func_176825_a(stack.getMetadata()).func_176828_c();
            }

            @Override
            public Object apply(Object p_apply_1_) {
                return this.apply((ItemStack) p_apply_1_);
            }
        })).setUnlocalizedName("redSandStone"));
        Item.registerItemBlock(Blocks.red_sandstone_stairs);
        Item.registerItemBlock(Blocks.stone_slab2, (new ItemSlab(Blocks.stone_slab2, Blocks.stone_slab2, Blocks.double_stone_slab2)).setUnlocalizedName("stoneSlab2"));
        Item.registerItem(256, "iron_shovel", (new ItemSpade(Item.ToolMaterial.IRON)).setUnlocalizedName("shovelIron"));
        Item.registerItem(257, "iron_pickaxe", (new ItemPickaxe(Item.ToolMaterial.IRON)).setUnlocalizedName("pickaxeIron"));
        Item.registerItem(258, "iron_axe", (new ItemAxe(Item.ToolMaterial.IRON)).setUnlocalizedName("hatchetIron"));
        Item.registerItem(259, "flint_and_steel", (new ItemFlintAndSteel()).setUnlocalizedName("flintAndSteel"));
        Item.registerItem(260, "apple", (new ItemFood(4, 0.3F, false)).setUnlocalizedName("apple"));
        Item.registerItem(261, "bow", (new ItemBow()).setUnlocalizedName("bow"));
        Item.registerItem(262, "arrow", (new Item()).setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat));
        Item.registerItem(263, "coal", (new ItemCoal()).setUnlocalizedName("coal"));
        Item.registerItem(264, "diamond", (new Item()).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(265, "iron_ingot", (new Item()).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(266, "gold_ingot", (new Item()).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(267, "iron_sword", (new ItemSword(Item.ToolMaterial.IRON)).setUnlocalizedName("swordIron"));
        Item.registerItem(268, "wooden_sword", (new ItemSword(Item.ToolMaterial.WOOD)).setUnlocalizedName("swordWood"));
        Item.registerItem(269, "wooden_shovel", (new ItemSpade(Item.ToolMaterial.WOOD)).setUnlocalizedName("shovelWood"));
        Item.registerItem(270, "wooden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood"));
        Item.registerItem(271, "wooden_axe", (new ItemAxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hatchetWood"));
        Item.registerItem(272, "stone_sword", (new ItemSword(Item.ToolMaterial.STONE)).setUnlocalizedName("swordStone"));
        Item.registerItem(273, "stone_shovel", (new ItemSpade(Item.ToolMaterial.STONE)).setUnlocalizedName("shovelStone"));
        Item.registerItem(274, "stone_pickaxe", (new ItemPickaxe(Item.ToolMaterial.STONE)).setUnlocalizedName("pickaxeStone"));
        Item.registerItem(275, "stone_axe", (new ItemAxe(Item.ToolMaterial.STONE)).setUnlocalizedName("hatchetStone"));
        Item.registerItem(276, "diamond_sword", (new ItemSword(Item.ToolMaterial.EMERALD)).setUnlocalizedName("swordDiamond"));
        Item.registerItem(277, "diamond_shovel", (new ItemSpade(Item.ToolMaterial.EMERALD)).setUnlocalizedName("shovelDiamond"));
        Item.registerItem(278, "diamond_pickaxe", (new ItemPickaxe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("pickaxeDiamond"));
        Item.registerItem(279, "diamond_axe", (new ItemAxe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("hatchetDiamond"));
        Item.registerItem(280, "stick", (new Item()).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(281, "bowl", (new Item()).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(282, "mushroom_stew", (new ItemSoup(6)).setUnlocalizedName("mushroomStew"));
        Item.registerItem(283, "golden_sword", (new ItemSword(Item.ToolMaterial.GOLD)).setUnlocalizedName("swordGold"));
        Item.registerItem(284, "golden_shovel", (new ItemSpade(Item.ToolMaterial.GOLD)).setUnlocalizedName("shovelGold"));
        Item.registerItem(285, "golden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold"));
        Item.registerItem(286, "golden_axe", (new ItemAxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hatchetGold"));
        Item.registerItem(287, "string", (new ItemReed(Blocks.tripwire)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(288, "feather", (new Item()).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(289, "gunpowder", (new Item()).setUnlocalizedName("sulphur").setPotionEffect(PotionHelper.gunpowderEffect).setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(290, "wooden_hoe", (new ItemHoe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hoeWood"));
        Item.registerItem(291, "stone_hoe", (new ItemHoe(Item.ToolMaterial.STONE)).setUnlocalizedName("hoeStone"));
        Item.registerItem(292, "iron_hoe", (new ItemHoe(Item.ToolMaterial.IRON)).setUnlocalizedName("hoeIron"));
        Item.registerItem(293, "diamond_hoe", (new ItemHoe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("hoeDiamond"));
        Item.registerItem(294, "golden_hoe", (new ItemHoe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hoeGold"));
        Item.registerItem(295, "wheat_seeds", (new ItemSeeds(Blocks.wheat, Blocks.farmland)).setUnlocalizedName("seeds"));
        Item.registerItem(296, "wheat", (new Item()).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(297, "bread", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("bread"));
        Item.registerItem(298, "leather_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 0)).setUnlocalizedName("helmetCloth"));
        Item.registerItem(299, "leather_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 1)).setUnlocalizedName("chestplateCloth"));
        Item.registerItem(300, "leather_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 2)).setUnlocalizedName("leggingsCloth"));
        Item.registerItem(301, "leather_boots", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 3)).setUnlocalizedName("bootsCloth"));
        Item.registerItem(302, "chainmail_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 0)).setUnlocalizedName("helmetChain"));
        Item.registerItem(303, "chainmail_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 1)).setUnlocalizedName("chestplateChain"));
        Item.registerItem(304, "chainmail_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 2)).setUnlocalizedName("leggingsChain"));
        Item.registerItem(305, "chainmail_boots", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 3)).setUnlocalizedName("bootsChain"));
        Item.registerItem(306, "iron_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 0)).setUnlocalizedName("helmetIron"));
        Item.registerItem(307, "iron_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 1)).setUnlocalizedName("chestplateIron"));
        Item.registerItem(308, "iron_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 2)).setUnlocalizedName("leggingsIron"));
        Item.registerItem(309, "iron_boots", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 3)).setUnlocalizedName("bootsIron"));
        Item.registerItem(310, "diamond_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 0)).setUnlocalizedName("helmetDiamond"));
        Item.registerItem(311, "diamond_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 1)).setUnlocalizedName("chestplateDiamond"));
        Item.registerItem(312, "diamond_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 2)).setUnlocalizedName("leggingsDiamond"));
        Item.registerItem(313, "diamond_boots", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 3)).setUnlocalizedName("bootsDiamond"));
        Item.registerItem(314, "golden_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 0)).setUnlocalizedName("helmetGold"));
        Item.registerItem(315, "golden_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 1)).setUnlocalizedName("chestplateGold"));
        Item.registerItem(316, "golden_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 2)).setUnlocalizedName("leggingsGold"));
        Item.registerItem(317, "golden_boots", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 3)).setUnlocalizedName("bootsGold"));
        Item.registerItem(318, "flint", (new Item()).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(319, "porkchop", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("porkchopRaw"));
        Item.registerItem(320, "cooked_porkchop", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("porkchopCooked"));
        Item.registerItem(321, "painting", (new ItemHangingEntity(EntityPainting.class)).setUnlocalizedName("painting"));
        Item.registerItem(322, "golden_apple", (new ItemAppleGold(4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 1, 1.0F).setUnlocalizedName("appleGold"));
        Item.registerItem(323, "sign", (new ItemSign()).setUnlocalizedName("sign"));
        Item.registerItem(324, "wooden_door", (new ItemDoor(Blocks.oak_door)).setUnlocalizedName("doorOak"));
        Item var0 = (new ItemBucket(Blocks.air)).setUnlocalizedName("bucket").setMaxStackSize(16);
        Item.registerItem(325, "bucket", var0);
        Item.registerItem(326, "water_bucket", (new ItemBucket(Blocks.flowing_water)).setUnlocalizedName("bucketWater").setContainerItem(var0));
        Item.registerItem(327, "lava_bucket", (new ItemBucket(Blocks.flowing_lava)).setUnlocalizedName("bucketLava").setContainerItem(var0));
        Item.registerItem(328, "minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.RIDEABLE)).setUnlocalizedName("minecart"));
        Item.registerItem(329, "saddle", (new ItemSaddle()).setUnlocalizedName("saddle"));
        Item.registerItem(330, "iron_door", (new ItemDoor(Blocks.iron_door)).setUnlocalizedName("doorIron"));
        Item.registerItem(331, "redstone", (new ItemRedstone()).setUnlocalizedName("redstone").setPotionEffect(PotionHelper.redstoneEffect));
        Item.registerItem(332, "snowball", (new ItemSnowball()).setUnlocalizedName("snowball"));
        Item.registerItem(333, "boat", (new ItemBoat()).setUnlocalizedName("boat"));
        Item.registerItem(334, "leather", (new Item()).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(335, "milk_bucket", (new ItemBucketMilk()).setUnlocalizedName("milk").setContainerItem(var0));
        Item.registerItem(336, "brick", (new Item()).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(337, "clay_ball", (new Item()).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(338, "reeds", (new ItemReed(Blocks.reeds)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(339, "paper", (new Item()).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(340, "book", (new ItemBook()).setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(341, "slime_ball", (new Item()).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(342, "chest_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.CHEST)).setUnlocalizedName("minecartChest"));
        Item.registerItem(343, "furnace_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.FURNACE)).setUnlocalizedName("minecartFurnace"));
        Item.registerItem(344, "egg", (new ItemEgg()).setUnlocalizedName("egg"));
        Item.registerItem(345, "compass", (new Item()).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools));
        Item.registerItem(346, "fishing_rod", (new ItemFishingRod()).setUnlocalizedName("fishingRod"));
        Item.registerItem(347, "clock", (new Item()).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools));
        Item.registerItem(348, "glowstone_dust", (new Item()).setUnlocalizedName("yellowDust").setPotionEffect(PotionHelper.glowstoneEffect).setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(349, "fish", (new ItemFishFood(false)).setUnlocalizedName("fish").setHasSubtypes(true));
        Item.registerItem(350, "cooked_fish", (new ItemFishFood(true)).setUnlocalizedName("fish").setHasSubtypes(true));
        Item.registerItem(351, "dye", (new ItemDye()).setUnlocalizedName("dyePowder"));
        Item.registerItem(352, "bone", (new Item()).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(353, "sugar", (new Item()).setUnlocalizedName("sugar").setPotionEffect(PotionHelper.sugarEffect).setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(354, "cake", (new ItemReed(Blocks.cake)).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.tabFood));
        Item.registerItem(355, "bed", (new ItemBed()).setMaxStackSize(1).setUnlocalizedName("bed"));
        Item.registerItem(356, "repeater", (new ItemReed(Blocks.unpowered_repeater)).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.tabRedstone));
        Item.registerItem(357, "cookie", (new ItemFood(2, 0.1F, false)).setUnlocalizedName("cookie"));
        Item.registerItem(358, "filled_map", (new ItemMap()).setUnlocalizedName("map"));
        Item.registerItem(359, "shears", (new ItemShears()).setUnlocalizedName("shears"));
        Item.registerItem(360, "melon", (new ItemFood(2, 0.3F, false)).setUnlocalizedName("melon"));
        Item.registerItem(361, "pumpkin_seeds", (new ItemSeeds(Blocks.pumpkin_stem, Blocks.farmland)).setUnlocalizedName("seeds_pumpkin"));
        Item.registerItem(362, "melon_seeds", (new ItemSeeds(Blocks.melon_stem, Blocks.farmland)).setUnlocalizedName("seeds_melon"));
        Item.registerItem(363, "beef", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("beefRaw"));
        Item.registerItem(364, "cooked_beef", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("beefCooked"));
        Item.registerItem(365, "chicken", (new ItemFood(2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw"));
        Item.registerItem(366, "cooked_chicken", (new ItemFood(6, 0.6F, true)).setUnlocalizedName("chickenCooked"));
        Item.registerItem(367, "rotten_flesh", (new ItemFood(4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh"));
        Item.registerItem(368, "ender_pearl", (new ItemEnderPearl()).setUnlocalizedName("enderPearl"));
        Item.registerItem(369, "blaze_rod", (new Item()).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials).setFull3D());
        Item.registerItem(370, "ghast_tear", (new Item()).setUnlocalizedName("ghastTear").setPotionEffect(PotionHelper.ghastTearEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(371, "gold_nugget", (new Item()).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(372, "nether_wart", (new ItemSeeds(Blocks.nether_wart, Blocks.soul_sand)).setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4"));
        Item.registerItem(373, "potion", (new ItemPotion()).setUnlocalizedName("potion"));
        Item.registerItem(374, "glass_bottle", (new ItemGlassBottle()).setUnlocalizedName("glassBottle"));
        Item.registerItem(375, "spider_eye", (new ItemFood(2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setUnlocalizedName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect));
        Item.registerItem(376, "fermented_spider_eye", (new Item()).setUnlocalizedName("fermentedSpiderEye").setPotionEffect(PotionHelper.fermentedSpiderEyeEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(377, "blaze_powder", (new Item()).setUnlocalizedName("blazePowder").setPotionEffect(PotionHelper.blazePowderEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(378, "magma_cream", (new Item()).setUnlocalizedName("magmaCream").setPotionEffect(PotionHelper.magmaCreamEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(379, "brewing_stand", (new ItemReed(Blocks.brewing_stand)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(380, "cauldron", (new ItemReed(Blocks.cauldron)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(381, "ender_eye", (new ItemEnderEye()).setUnlocalizedName("eyeOfEnder"));
        Item.registerItem(382, "speckled_melon", (new Item()).setUnlocalizedName("speckledMelon").setPotionEffect(PotionHelper.speckledMelonEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(383, "spawn_egg", (new ItemMonsterPlacer()).setUnlocalizedName("monsterPlacer"));
        Item.registerItem(384, "experience_bottle", (new ItemExpBottle()).setUnlocalizedName("expBottle"));
        Item.registerItem(385, "fire_charge", (new ItemFireball()).setUnlocalizedName("fireball"));
        Item.registerItem(386, "writable_book", (new ItemWritableBook()).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(387, "written_book", (new ItemEditableBook()).setUnlocalizedName("writtenBook").setMaxStackSize(16));
        Item.registerItem(388, "emerald", (new Item()).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(389, "item_frame", (new ItemHangingEntity(EntityItemFrame.class)).setUnlocalizedName("frame"));
        Item.registerItem(390, "flower_pot", (new ItemReed(Blocks.flower_pot)).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations));
        Item.registerItem(391, "carrot", (new ItemSeedFood(3, 0.6F, Blocks.carrots, Blocks.farmland)).setUnlocalizedName("carrots"));
        Item.registerItem(392, "potato", (new ItemSeedFood(1, 0.3F, Blocks.potatoes, Blocks.farmland)).setUnlocalizedName("potato"));
        Item.registerItem(393, "baked_potato", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("potatoBaked"));
        Item.registerItem(394, "poisonous_potato", (new ItemFood(2, 0.3F, false)).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous"));
        Item.registerItem(395, "map", (new ItemEmptyMap()).setUnlocalizedName("emptyMap"));
        Item.registerItem(396, "golden_carrot", (new ItemFood(6, 1.2F, false)).setUnlocalizedName("carrotGolden").setPotionEffect(PotionHelper.goldenCarrotEffect).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(397, "skull", (new ItemSkull()).setUnlocalizedName("skull"));
        Item.registerItem(398, "carrot_on_a_stick", (new ItemCarrotOnAStick()).setUnlocalizedName("carrotOnAStick"));
        Item.registerItem(399, "nether_star", (new ItemSimpleFoiled()).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(400, "pumpkin_pie", (new ItemFood(8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood));
        Item.registerItem(401, "fireworks", (new ItemFirework()).setUnlocalizedName("fireworks"));
        Item.registerItem(402, "firework_charge", (new ItemFireworkCharge()).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(403, "enchanted_book", (new ItemEnchantedBook()).setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
        Item.registerItem(404, "comparator", (new ItemReed(Blocks.unpowered_comparator)).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone));
        Item.registerItem(405, "netherbrick", (new Item()).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(406, "quartz", (new Item()).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(407, "tnt_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.TNT)).setUnlocalizedName("minecartTnt"));
        Item.registerItem(408, "hopper_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.HOPPER)).setUnlocalizedName("minecartHopper"));
        Item.registerItem(409, "prismarine_shard", (new Item()).setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(410, "prismarine_crystals", (new Item()).setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(411, "rabbit", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("rabbitRaw"));
        Item.registerItem(412, "cooked_rabbit", (new ItemFood(5, 0.6F, true)).setUnlocalizedName("rabbitCooked"));
        Item.registerItem(413, "rabbit_stew", (new ItemSoup(10)).setUnlocalizedName("rabbitStew"));
        Item.registerItem(414, "rabbit_foot", (new Item()).setUnlocalizedName("rabbitFoot").setPotionEffect(PotionHelper.field_179538_n).setCreativeTab(CreativeTabs.tabBrewing));
        Item.registerItem(415, "rabbit_hide", (new Item()).setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.tabMaterials));
        Item.registerItem(416, "armor_stand", (new ItemArmorStand()).setUnlocalizedName("armorStand").setMaxStackSize(16));
        Item.registerItem(417, "iron_horse_armor", (new Item()).setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(418, "golden_horse_armor", (new Item()).setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(419, "diamond_horse_armor", (new Item()).setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
        Item.registerItem(420, "lead", (new ItemLead()).setUnlocalizedName("leash"));
        Item.registerItem(421, "name_tag", (new ItemNameTag()).setUnlocalizedName("nameTag"));
        Item.registerItem(422, "command_block_minecart", (new ItemMinecart(EntityMinecart.EnumMinecartType.COMMAND_BLOCK)).setUnlocalizedName("minecartCommandBlock").setCreativeTab((CreativeTabs) null));
        Item.registerItem(423, "mutton", (new ItemFood(2, 0.3F, true)).setUnlocalizedName("muttonRaw"));
        Item.registerItem(424, "cooked_mutton", (new ItemFood(6, 0.8F, true)).setUnlocalizedName("muttonCooked"));
        Item.registerItem(425, "banner", (new ItemBanner()).setUnlocalizedName("banner"));
        Item.registerItem(427, "spruce_door", (new ItemDoor(Blocks.spruce_door)).setUnlocalizedName("doorSpruce"));
        Item.registerItem(428, "birch_door", (new ItemDoor(Blocks.birch_door)).setUnlocalizedName("doorBirch"));
        Item.registerItem(429, "jungle_door", (new ItemDoor(Blocks.jungle_door)).setUnlocalizedName("doorJungle"));
        Item.registerItem(430, "acacia_door", (new ItemDoor(Blocks.acacia_door)).setUnlocalizedName("doorAcacia"));
        Item.registerItem(431, "dark_oak_door", (new ItemDoor(Blocks.dark_oak_door)).setUnlocalizedName("doorDarkOak"));
        Item.registerItem(2256, "record_13", (new ItemRecord("13")).setUnlocalizedName("record"));
        Item.registerItem(2257, "record_cat", (new ItemRecord("cat")).setUnlocalizedName("record"));
        Item.registerItem(2258, "record_blocks", (new ItemRecord("blocks")).setUnlocalizedName("record"));
        Item.registerItem(2259, "record_chirp", (new ItemRecord("chirp")).setUnlocalizedName("record"));
        Item.registerItem(2260, "record_far", (new ItemRecord("far")).setUnlocalizedName("record"));
        Item.registerItem(2261, "record_mall", (new ItemRecord("mall")).setUnlocalizedName("record"));
        Item.registerItem(2262, "record_mellohi", (new ItemRecord("mellohi")).setUnlocalizedName("record"));
        Item.registerItem(2263, "record_stal", (new ItemRecord("stal")).setUnlocalizedName("record"));
        Item.registerItem(2264, "record_strad", (new ItemRecord("strad")).setUnlocalizedName("record"));
        Item.registerItem(2265, "record_ward", (new ItemRecord("ward")).setUnlocalizedName("record"));
        Item.registerItem(2266, "record_11", (new ItemRecord("11")).setUnlocalizedName("record"));
        Item.registerItem(2267, "record_wait", (new ItemRecord("wait")).setUnlocalizedName("record"));
    }

    /**
     * Register a default ItemBlock for the given Block.
     */
    private static void registerItemBlock(Block blockIn) {
        Item.registerItemBlock(blockIn, new ItemBlock(blockIn));
    }

    /**
     * Register the given Item as the ItemBlock for the given Block.
     */
    protected static void registerItemBlock(Block blockIn, Item itemIn) {
        Item.registerItem(Block.getIdFromBlock(blockIn), (ResourceLocation) Block.blockRegistry.getNameForObject(blockIn), itemIn);
        Item.BLOCK_TO_ITEM.put(blockIn, itemIn);
    }

    private static void registerItem(int id, String textualID, Item itemIn) {
        Item.registerItem(id, new ResourceLocation(textualID), itemIn);
    }

    private static void registerItem(int id, ResourceLocation textualID, Item itemIn) {
        Item.itemRegistry.register(id, textualID, itemIn);
    }

    public static enum ToolMaterial {
        WOOD("WOOD", 0, 0, 59, 2.0F, 0.0F, 15), STONE("STONE", 1, 1, 131, 4.0F, 1.0F, 5), IRON("IRON", 2, 2, 250, 6.0F, 2.0F, 14), EMERALD("EMERALD", 3, 3, 1561, 8.0F, 3.0F, 10), GOLD("GOLD", 4, 0, 32, 12.0F, 0.0F, 22);
        private final int harvestLevel;
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final float damageVsEntity;
        private final int enchantability;

        private static final Item.ToolMaterial[] $VALUES = new Item.ToolMaterial[]{WOOD, STONE, IRON, EMERALD, GOLD};
        private static final String __OBFID = "CL_00000042";

        private ToolMaterial(String p_i1874_1_, int p_i1874_2_, int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability) {
            this.harvestLevel = harvestLevel;
            this.maxUses = maxUses;
            efficiencyOnProperMaterial = efficiency;
            this.damageVsEntity = damageVsEntity;
            this.enchantability = enchantability;
        }

        public int getMaxUses() {
            return maxUses;
        }

        public float getEfficiencyOnProperMaterial() {
            return efficiencyOnProperMaterial;
        }

        public float getDamageVsEntity() {
            return damageVsEntity;
        }

        public int getHarvestLevel() {
            return harvestLevel;
        }

        public int getEnchantability() {
            return enchantability;
        }

        public Item getBaseItemForRepair() {
            return this == WOOD ? Item.getItemFromBlock(Blocks.planks) : (this == STONE ? Item.getItemFromBlock(Blocks.cobblestone) : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == EMERALD ? Items.diamond : null))));
        }
    }
}
