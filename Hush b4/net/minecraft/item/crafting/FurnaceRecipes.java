// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import java.util.Map;

public class FurnaceRecipes
{
    private static final FurnaceRecipes smeltingBase;
    private Map<ItemStack, ItemStack> smeltingList;
    private Map<ItemStack, Float> experienceList;
    
    static {
        smeltingBase = new FurnaceRecipes();
    }
    
    public static FurnaceRecipes instance() {
        return FurnaceRecipes.smeltingBase;
    }
    
    private FurnaceRecipes() {
        this.smeltingList = (Map<ItemStack, ItemStack>)Maps.newHashMap();
        this.experienceList = (Map<ItemStack, Float>)Maps.newHashMap();
        this.addSmeltingRecipeForBlock(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0f);
        this.addSmeltingRecipeForBlock(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
        this.addSmelting(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35f);
        this.addSmelting(Items.beef, new ItemStack(Items.cooked_beef), 0.35f);
        this.addSmelting(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35f);
        this.addSmelting(Items.rabbit, new ItemStack(Items.cooked_rabbit), 0.35f);
        this.addSmelting(Items.mutton, new ItemStack(Items.cooked_mutton), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CRACKED_META), 0.1f);
        this.addSmelting(Items.clay_ball, new ItemStack(Items.brick), 0.3f);
        this.addSmeltingRecipeForBlock(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.cactus, new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
        this.addSmeltingRecipeForBlock(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0f);
        this.addSmelting(Items.potato, new ItemStack(Items.baked_potato), 0.35f);
        this.addSmeltingRecipeForBlock(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1f);
        this.addSmeltingRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.15f);
        ItemFishFood.FishType[] values;
        for (int length = (values = ItemFishFood.FishType.values()).length, i = 0; i < length; ++i) {
            final ItemFishFood.FishType itemfishfood$fishtype = values[i];
            if (itemfishfood$fishtype.canCook()) {
                this.addSmeltingRecipe(new ItemStack(Items.fish, 1, itemfishfood$fishtype.getMetadata()), new ItemStack(Items.cooked_fish, 1, itemfishfood$fishtype.getMetadata()), 0.35f);
            }
        }
        this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1f);
        this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7f);
        this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), 0.2f);
        this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2f);
    }
    
    public void addSmeltingRecipeForBlock(final Block input, final ItemStack stack, final float experience) {
        this.addSmelting(Item.getItemFromBlock(input), stack, experience);
    }
    
    public void addSmelting(final Item input, final ItemStack stack, final float experience) {
        this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }
    
    public void addSmeltingRecipe(final ItemStack input, final ItemStack stack, final float experience) {
        this.smeltingList.put(input, stack);
        this.experienceList.put(stack, experience);
    }
    
    public ItemStack getSmeltingResult(final ItemStack stack) {
        for (final Map.Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private boolean compareItemStacks(final ItemStack stack1, final ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
    
    public Map<ItemStack, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }
    
    public float getSmeltingExperience(final ItemStack stack) {
        for (final Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0f;
    }
}
