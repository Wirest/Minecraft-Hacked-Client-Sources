/*      */ package net.minecraft.item;
/*      */ 
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.collect.HashMultimap;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDirt.DirtType;
/*      */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*      */ import net.minecraft.block.BlockFlower.EnumFlowerColor;
/*      */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*      */ import net.minecraft.block.BlockPlanks.EnumType;
/*      */ import net.minecraft.block.BlockPrismarine.EnumType;
/*      */ import net.minecraft.block.BlockRedSandstone.EnumType;
/*      */ import net.minecraft.block.BlockSand.EnumType;
/*      */ import net.minecraft.block.BlockSandStone.EnumType;
/*      */ import net.minecraft.block.BlockSilverfish.EnumType;
/*      */ import net.minecraft.block.BlockStone.EnumType;
/*      */ import net.minecraft.block.BlockStoneBrick.EnumType;
/*      */ import net.minecraft.block.BlockWall.EnumType;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.entity.item.EntityItemFrame;
/*      */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.RegistryNamespaced;
/*      */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class Item
/*      */ {
/*   49 */   public static final RegistryNamespaced<ResourceLocation, Item> itemRegistry = new RegistryNamespaced();
/*   50 */   private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.newHashMap();
/*   51 */   protected static final UUID itemModifierUUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
/*      */   
/*      */   private CreativeTabs tabToDisplayOn;
/*      */   
/*   55 */   protected static Random itemRand = new Random();
/*      */   
/*      */ 
/*   58 */   protected int maxStackSize = 64;
/*      */   
/*      */ 
/*      */   private int maxDamage;
/*      */   
/*      */ 
/*      */   protected boolean bFull3D;
/*      */   
/*      */ 
/*      */   protected boolean hasSubtypes;
/*      */   
/*      */ 
/*      */   private Item containerItem;
/*      */   
/*      */ 
/*      */   private String potionEffect;
/*      */   
/*      */ 
/*      */   private String unlocalizedName;
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getIdFromItem(Item itemIn)
/*      */   {
/*   82 */     return itemIn == null ? 0 : itemRegistry.getIDForObject(itemIn);
/*      */   }
/*      */   
/*      */   public static Item getItemById(int id)
/*      */   {
/*   87 */     return (Item)itemRegistry.getObjectById(id);
/*      */   }
/*      */   
/*      */   public static Item getItemFromBlock(Block blockIn)
/*      */   {
/*   92 */     return (Item)BLOCK_TO_ITEM.get(blockIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Item getByNameOrId(String id)
/*      */   {
/*  101 */     Item item = (Item)itemRegistry.getObject(new ResourceLocation(id));
/*      */     
/*  103 */     if (item == null)
/*      */     {
/*      */       try
/*      */       {
/*  107 */         return getItemById(Integer.parseInt(id));
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  115 */     return item;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean updateItemStackNBT(NBTTagCompound nbt)
/*      */   {
/*  123 */     return false;
/*      */   }
/*      */   
/*      */   public Item setMaxStackSize(int maxStackSize)
/*      */   {
/*  128 */     this.maxStackSize = maxStackSize;
/*  129 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*      */   {
/*  137 */     return false;
/*      */   }
/*      */   
/*      */   public float getStrVsBlock(ItemStack stack, Block block)
/*      */   {
/*  142 */     return 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*      */   {
/*  150 */     return itemStackIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*      */   {
/*  159 */     return stack;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getItemStackLimit()
/*      */   {
/*  167 */     return this.maxStackSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMetadata(int damage)
/*      */   {
/*  176 */     return 0;
/*      */   }
/*      */   
/*      */   public boolean getHasSubtypes()
/*      */   {
/*  181 */     return this.hasSubtypes;
/*      */   }
/*      */   
/*      */   protected Item setHasSubtypes(boolean hasSubtypes)
/*      */   {
/*  186 */     this.hasSubtypes = hasSubtypes;
/*  187 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxDamage()
/*      */   {
/*  195 */     return this.maxDamage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Item setMaxDamage(int maxDamageIn)
/*      */   {
/*  203 */     this.maxDamage = maxDamageIn;
/*  204 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isDamageable()
/*      */   {
/*  209 */     return (this.maxDamage > 0) && (!this.hasSubtypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
/*      */   {
/*  218 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
/*      */   {
/*  226 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canHarvestBlock(Block blockIn)
/*      */   {
/*  234 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
/*      */   {
/*  242 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Item setFull3D()
/*      */   {
/*  250 */     this.bFull3D = true;
/*  251 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFull3D()
/*      */   {
/*  259 */     return this.bFull3D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean shouldRotateAroundWhenRendering()
/*      */   {
/*  268 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Item setUnlocalizedName(String unlocalizedName)
/*      */   {
/*  276 */     this.unlocalizedName = unlocalizedName;
/*  277 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUnlocalizedNameInefficiently(ItemStack stack)
/*      */   {
/*  286 */     String s = getUnlocalizedName(stack);
/*  287 */     return s == null ? "" : StatCollector.translateToLocal(s);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUnlocalizedName()
/*      */   {
/*  295 */     return "item." + this.unlocalizedName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUnlocalizedName(ItemStack stack)
/*      */   {
/*  304 */     return "item." + this.unlocalizedName;
/*      */   }
/*      */   
/*      */   public Item setContainerItem(Item containerItem)
/*      */   {
/*  309 */     this.containerItem = containerItem;
/*  310 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getShareTag()
/*      */   {
/*  318 */     return true;
/*      */   }
/*      */   
/*      */   public Item getContainerItem()
/*      */   {
/*  323 */     return this.containerItem;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasContainerItem()
/*      */   {
/*  331 */     return this.containerItem != null;
/*      */   }
/*      */   
/*      */   public int getColorFromItemStack(ItemStack stack, int renderPass)
/*      */   {
/*  336 */     return 16777215;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMap()
/*      */   {
/*  359 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EnumAction getItemUseAction(ItemStack stack)
/*      */   {
/*  367 */     return EnumAction.NONE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxItemUseDuration(ItemStack stack)
/*      */   {
/*  375 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Item setPotionEffect(String potionEffect)
/*      */   {
/*  390 */     this.potionEffect = potionEffect;
/*  391 */     return this;
/*      */   }
/*      */   
/*      */   public String getPotionEffect(ItemStack stack)
/*      */   {
/*  396 */     return this.potionEffect;
/*      */   }
/*      */   
/*      */   public boolean isPotionIngredient(ItemStack stack)
/*      */   {
/*  401 */     return getPotionEffect(stack) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public String getItemStackDisplayName(ItemStack stack)
/*      */   {
/*  413 */     return StatCollector.translateToLocal(new StringBuilder(String.valueOf(getUnlocalizedNameInefficiently(stack))).append(".name").toString()).trim();
/*      */   }
/*      */   
/*      */   public boolean hasEffect(ItemStack stack)
/*      */   {
/*  418 */     return stack.isItemEnchanted();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EnumRarity getRarity(ItemStack stack)
/*      */   {
/*  426 */     return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isItemTool(ItemStack stack)
/*      */   {
/*  434 */     return (getItemStackLimit() == 1) && (isDamageable());
/*      */   }
/*      */   
/*      */   protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids)
/*      */   {
/*  439 */     float f = playerIn.rotationPitch;
/*  440 */     float f1 = playerIn.rotationYaw;
/*  441 */     double d0 = playerIn.posX;
/*  442 */     double d1 = playerIn.posY + playerIn.getEyeHeight();
/*  443 */     double d2 = playerIn.posZ;
/*  444 */     Vec3 vec3 = new Vec3(d0, d1, d2);
/*  445 */     float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
/*  446 */     float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
/*  447 */     float f4 = -MathHelper.cos(-f * 0.017453292F);
/*  448 */     float f5 = MathHelper.sin(-f * 0.017453292F);
/*  449 */     float f6 = f3 * f4;
/*  450 */     float f7 = f2 * f4;
/*  451 */     double d3 = 5.0D;
/*  452 */     Vec3 vec31 = vec3.addVector(f6 * d3, f5 * d3, f7 * d3);
/*  453 */     return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getItemEnchantability()
/*      */   {
/*  461 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*      */   {
/*  469 */     subItems.add(new ItemStack(itemIn, 1, 0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CreativeTabs getCreativeTab()
/*      */   {
/*  477 */     return this.tabToDisplayOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Item setCreativeTab(CreativeTabs tab)
/*      */   {
/*  485 */     this.tabToDisplayOn = tab;
/*  486 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canItemEditBlocks()
/*      */   {
/*  495 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
/*      */   {
/*  503 */     return false;
/*      */   }
/*      */   
/*      */   public Multimap<String, AttributeModifier> getItemAttributeModifiers()
/*      */   {
/*  508 */     return HashMultimap.create();
/*      */   }
/*      */   
/*      */   public static void registerItems()
/*      */   {
/*  513 */     registerItemBlock(Blocks.stone, new ItemMultiTexture(Blocks.stone, Blocks.stone, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  517 */         return BlockStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  519 */     }).setUnlocalizedName("stone"));
/*  520 */     registerItemBlock(Blocks.grass, new ItemColored(Blocks.grass, false));
/*  521 */     registerItemBlock(Blocks.dirt, new ItemMultiTexture(Blocks.dirt, Blocks.dirt, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  525 */         return BlockDirt.DirtType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  527 */     }).setUnlocalizedName("dirt"));
/*  528 */     registerItemBlock(Blocks.cobblestone);
/*  529 */     registerItemBlock(Blocks.planks, new ItemMultiTexture(Blocks.planks, Blocks.planks, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  533 */         return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  535 */     }).setUnlocalizedName("wood"));
/*  536 */     registerItemBlock(Blocks.sapling, new ItemMultiTexture(Blocks.sapling, Blocks.sapling, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  540 */         return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  542 */     }).setUnlocalizedName("sapling"));
/*  543 */     registerItemBlock(Blocks.bedrock);
/*  544 */     registerItemBlock(Blocks.sand, new ItemMultiTexture(Blocks.sand, Blocks.sand, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  548 */         return BlockSand.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  550 */     }).setUnlocalizedName("sand"));
/*  551 */     registerItemBlock(Blocks.gravel);
/*  552 */     registerItemBlock(Blocks.gold_ore);
/*  553 */     registerItemBlock(Blocks.iron_ore);
/*  554 */     registerItemBlock(Blocks.coal_ore);
/*  555 */     registerItemBlock(Blocks.log, new ItemMultiTexture(Blocks.log, Blocks.log, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  559 */         return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  561 */     }).setUnlocalizedName("log"));
/*  562 */     registerItemBlock(Blocks.log2, new ItemMultiTexture(Blocks.log2, Blocks.log2, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  566 */         return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata() + 4).getUnlocalizedName();
/*      */       }
/*  568 */     }).setUnlocalizedName("log"));
/*  569 */     registerItemBlock(Blocks.leaves, new ItemLeaves(Blocks.leaves).setUnlocalizedName("leaves"));
/*  570 */     registerItemBlock(Blocks.leaves2, new ItemLeaves(Blocks.leaves2).setUnlocalizedName("leaves"));
/*  571 */     registerItemBlock(Blocks.sponge, new ItemMultiTexture(Blocks.sponge, Blocks.sponge, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  575 */         return (p_apply_1_.getMetadata() & 0x1) == 1 ? "wet" : "dry";
/*      */       }
/*  577 */     }).setUnlocalizedName("sponge"));
/*  578 */     registerItemBlock(Blocks.glass);
/*  579 */     registerItemBlock(Blocks.lapis_ore);
/*  580 */     registerItemBlock(Blocks.lapis_block);
/*  581 */     registerItemBlock(Blocks.dispenser);
/*  582 */     registerItemBlock(Blocks.sandstone, new ItemMultiTexture(Blocks.sandstone, Blocks.sandstone, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  586 */         return BlockSandStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  588 */     }).setUnlocalizedName("sandStone"));
/*  589 */     registerItemBlock(Blocks.noteblock);
/*  590 */     registerItemBlock(Blocks.golden_rail);
/*  591 */     registerItemBlock(Blocks.detector_rail);
/*  592 */     registerItemBlock(Blocks.sticky_piston, new ItemPiston(Blocks.sticky_piston));
/*  593 */     registerItemBlock(Blocks.web);
/*  594 */     registerItemBlock(Blocks.tallgrass, new ItemColored(Blocks.tallgrass, true).setSubtypeNames(new String[] { "shrub", "grass", "fern" }));
/*  595 */     registerItemBlock(Blocks.deadbush);
/*  596 */     registerItemBlock(Blocks.piston, new ItemPiston(Blocks.piston));
/*  597 */     registerItemBlock(Blocks.wool, new ItemCloth(Blocks.wool).setUnlocalizedName("cloth"));
/*  598 */     registerItemBlock(Blocks.yellow_flower, new ItemMultiTexture(Blocks.yellow_flower, Blocks.yellow_flower, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  602 */         return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  604 */     }).setUnlocalizedName("flower"));
/*  605 */     registerItemBlock(Blocks.red_flower, new ItemMultiTexture(Blocks.red_flower, Blocks.red_flower, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  609 */         return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  611 */     }).setUnlocalizedName("rose"));
/*  612 */     registerItemBlock(Blocks.brown_mushroom);
/*  613 */     registerItemBlock(Blocks.red_mushroom);
/*  614 */     registerItemBlock(Blocks.gold_block);
/*  615 */     registerItemBlock(Blocks.iron_block);
/*  616 */     registerItemBlock(Blocks.stone_slab, new ItemSlab(Blocks.stone_slab, Blocks.stone_slab, Blocks.double_stone_slab).setUnlocalizedName("stoneSlab"));
/*  617 */     registerItemBlock(Blocks.brick_block);
/*  618 */     registerItemBlock(Blocks.tnt);
/*  619 */     registerItemBlock(Blocks.bookshelf);
/*  620 */     registerItemBlock(Blocks.mossy_cobblestone);
/*  621 */     registerItemBlock(Blocks.obsidian);
/*  622 */     registerItemBlock(Blocks.torch);
/*  623 */     registerItemBlock(Blocks.mob_spawner);
/*  624 */     registerItemBlock(Blocks.oak_stairs);
/*  625 */     registerItemBlock(Blocks.chest);
/*  626 */     registerItemBlock(Blocks.diamond_ore);
/*  627 */     registerItemBlock(Blocks.diamond_block);
/*  628 */     registerItemBlock(Blocks.crafting_table);
/*  629 */     registerItemBlock(Blocks.farmland);
/*  630 */     registerItemBlock(Blocks.furnace);
/*  631 */     registerItemBlock(Blocks.lit_furnace);
/*  632 */     registerItemBlock(Blocks.ladder);
/*  633 */     registerItemBlock(Blocks.rail);
/*  634 */     registerItemBlock(Blocks.stone_stairs);
/*  635 */     registerItemBlock(Blocks.lever);
/*  636 */     registerItemBlock(Blocks.stone_pressure_plate);
/*  637 */     registerItemBlock(Blocks.wooden_pressure_plate);
/*  638 */     registerItemBlock(Blocks.redstone_ore);
/*  639 */     registerItemBlock(Blocks.redstone_torch);
/*  640 */     registerItemBlock(Blocks.stone_button);
/*  641 */     registerItemBlock(Blocks.snow_layer, new ItemSnow(Blocks.snow_layer));
/*  642 */     registerItemBlock(Blocks.ice);
/*  643 */     registerItemBlock(Blocks.snow);
/*  644 */     registerItemBlock(Blocks.cactus);
/*  645 */     registerItemBlock(Blocks.clay);
/*  646 */     registerItemBlock(Blocks.jukebox);
/*  647 */     registerItemBlock(Blocks.oak_fence);
/*  648 */     registerItemBlock(Blocks.spruce_fence);
/*  649 */     registerItemBlock(Blocks.birch_fence);
/*  650 */     registerItemBlock(Blocks.jungle_fence);
/*  651 */     registerItemBlock(Blocks.dark_oak_fence);
/*  652 */     registerItemBlock(Blocks.acacia_fence);
/*  653 */     registerItemBlock(Blocks.pumpkin);
/*  654 */     registerItemBlock(Blocks.netherrack);
/*  655 */     registerItemBlock(Blocks.soul_sand);
/*  656 */     registerItemBlock(Blocks.glowstone);
/*  657 */     registerItemBlock(Blocks.lit_pumpkin);
/*  658 */     registerItemBlock(Blocks.trapdoor);
/*  659 */     registerItemBlock(Blocks.monster_egg, new ItemMultiTexture(Blocks.monster_egg, Blocks.monster_egg, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  663 */         return BlockSilverfish.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  665 */     }).setUnlocalizedName("monsterStoneEgg"));
/*  666 */     registerItemBlock(Blocks.stonebrick, new ItemMultiTexture(Blocks.stonebrick, Blocks.stonebrick, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  670 */         return BlockStoneBrick.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  672 */     }).setUnlocalizedName("stonebricksmooth"));
/*  673 */     registerItemBlock(Blocks.brown_mushroom_block);
/*  674 */     registerItemBlock(Blocks.red_mushroom_block);
/*  675 */     registerItemBlock(Blocks.iron_bars);
/*  676 */     registerItemBlock(Blocks.glass_pane);
/*  677 */     registerItemBlock(Blocks.melon_block);
/*  678 */     registerItemBlock(Blocks.vine, new ItemColored(Blocks.vine, false));
/*  679 */     registerItemBlock(Blocks.oak_fence_gate);
/*  680 */     registerItemBlock(Blocks.spruce_fence_gate);
/*  681 */     registerItemBlock(Blocks.birch_fence_gate);
/*  682 */     registerItemBlock(Blocks.jungle_fence_gate);
/*  683 */     registerItemBlock(Blocks.dark_oak_fence_gate);
/*  684 */     registerItemBlock(Blocks.acacia_fence_gate);
/*  685 */     registerItemBlock(Blocks.brick_stairs);
/*  686 */     registerItemBlock(Blocks.stone_brick_stairs);
/*  687 */     registerItemBlock(Blocks.mycelium);
/*  688 */     registerItemBlock(Blocks.waterlily, new ItemLilyPad(Blocks.waterlily));
/*  689 */     registerItemBlock(Blocks.nether_brick);
/*  690 */     registerItemBlock(Blocks.nether_brick_fence);
/*  691 */     registerItemBlock(Blocks.nether_brick_stairs);
/*  692 */     registerItemBlock(Blocks.enchanting_table);
/*  693 */     registerItemBlock(Blocks.end_portal_frame);
/*  694 */     registerItemBlock(Blocks.end_stone);
/*  695 */     registerItemBlock(Blocks.dragon_egg);
/*  696 */     registerItemBlock(Blocks.redstone_lamp);
/*  697 */     registerItemBlock(Blocks.wooden_slab, new ItemSlab(Blocks.wooden_slab, Blocks.wooden_slab, Blocks.double_wooden_slab).setUnlocalizedName("woodSlab"));
/*  698 */     registerItemBlock(Blocks.sandstone_stairs);
/*  699 */     registerItemBlock(Blocks.emerald_ore);
/*  700 */     registerItemBlock(Blocks.ender_chest);
/*  701 */     registerItemBlock(Blocks.tripwire_hook);
/*  702 */     registerItemBlock(Blocks.emerald_block);
/*  703 */     registerItemBlock(Blocks.spruce_stairs);
/*  704 */     registerItemBlock(Blocks.birch_stairs);
/*  705 */     registerItemBlock(Blocks.jungle_stairs);
/*  706 */     registerItemBlock(Blocks.command_block);
/*  707 */     registerItemBlock(Blocks.beacon);
/*  708 */     registerItemBlock(Blocks.cobblestone_wall, new ItemMultiTexture(Blocks.cobblestone_wall, Blocks.cobblestone_wall, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  712 */         return BlockWall.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  714 */     }).setUnlocalizedName("cobbleWall"));
/*  715 */     registerItemBlock(Blocks.wooden_button);
/*  716 */     registerItemBlock(Blocks.anvil, new ItemAnvilBlock(Blocks.anvil).setUnlocalizedName("anvil"));
/*  717 */     registerItemBlock(Blocks.trapped_chest);
/*  718 */     registerItemBlock(Blocks.light_weighted_pressure_plate);
/*  719 */     registerItemBlock(Blocks.heavy_weighted_pressure_plate);
/*  720 */     registerItemBlock(Blocks.daylight_detector);
/*  721 */     registerItemBlock(Blocks.redstone_block);
/*  722 */     registerItemBlock(Blocks.quartz_ore);
/*  723 */     registerItemBlock(Blocks.hopper);
/*  724 */     registerItemBlock(Blocks.quartz_block, new ItemMultiTexture(Blocks.quartz_block, Blocks.quartz_block, new String[] { "default", "chiseled", "lines" }).setUnlocalizedName("quartzBlock"));
/*  725 */     registerItemBlock(Blocks.quartz_stairs);
/*  726 */     registerItemBlock(Blocks.activator_rail);
/*  727 */     registerItemBlock(Blocks.dropper);
/*  728 */     registerItemBlock(Blocks.stained_hardened_clay, new ItemCloth(Blocks.stained_hardened_clay).setUnlocalizedName("clayHardenedStained"));
/*  729 */     registerItemBlock(Blocks.barrier);
/*  730 */     registerItemBlock(Blocks.iron_trapdoor);
/*  731 */     registerItemBlock(Blocks.hay_block);
/*  732 */     registerItemBlock(Blocks.carpet, new ItemCloth(Blocks.carpet).setUnlocalizedName("woolCarpet"));
/*  733 */     registerItemBlock(Blocks.hardened_clay);
/*  734 */     registerItemBlock(Blocks.coal_block);
/*  735 */     registerItemBlock(Blocks.packed_ice);
/*  736 */     registerItemBlock(Blocks.acacia_stairs);
/*  737 */     registerItemBlock(Blocks.dark_oak_stairs);
/*  738 */     registerItemBlock(Blocks.slime_block);
/*  739 */     registerItemBlock(Blocks.double_plant, new ItemDoublePlant(Blocks.double_plant, Blocks.double_plant, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  743 */         return BlockDoublePlant.EnumPlantType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  745 */     }).setUnlocalizedName("doublePlant"));
/*  746 */     registerItemBlock(Blocks.stained_glass, new ItemCloth(Blocks.stained_glass).setUnlocalizedName("stainedGlass"));
/*  747 */     registerItemBlock(Blocks.stained_glass_pane, new ItemCloth(Blocks.stained_glass_pane).setUnlocalizedName("stainedGlassPane"));
/*  748 */     registerItemBlock(Blocks.prismarine, new ItemMultiTexture(Blocks.prismarine, Blocks.prismarine, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  752 */         return BlockPrismarine.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  754 */     }).setUnlocalizedName("prismarine"));
/*  755 */     registerItemBlock(Blocks.sea_lantern);
/*  756 */     registerItemBlock(Blocks.red_sandstone, new ItemMultiTexture(Blocks.red_sandstone, Blocks.red_sandstone, new Function()
/*      */     {
/*      */       public String apply(ItemStack p_apply_1_)
/*      */       {
/*  760 */         return BlockRedSandstone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
/*      */       }
/*  762 */     }).setUnlocalizedName("redSandStone"));
/*  763 */     registerItemBlock(Blocks.red_sandstone_stairs);
/*  764 */     registerItemBlock(Blocks.stone_slab2, new ItemSlab(Blocks.stone_slab2, Blocks.stone_slab2, Blocks.double_stone_slab2).setUnlocalizedName("stoneSlab2"));
/*  765 */     registerItem(256, "iron_shovel", new ItemSpade(ToolMaterial.IRON).setUnlocalizedName("shovelIron"));
/*  766 */     registerItem(257, "iron_pickaxe", new ItemPickaxe(ToolMaterial.IRON).setUnlocalizedName("pickaxeIron"));
/*  767 */     registerItem(258, "iron_axe", new ItemAxe(ToolMaterial.IRON).setUnlocalizedName("hatchetIron"));
/*  768 */     registerItem(259, "flint_and_steel", new ItemFlintAndSteel().setUnlocalizedName("flintAndSteel"));
/*  769 */     registerItem(260, "apple", new ItemFood(4, 0.3F, false).setUnlocalizedName("apple"));
/*  770 */     registerItem(261, "bow", new ItemBow().setUnlocalizedName("bow"));
/*  771 */     registerItem(262, "arrow", new Item().setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat));
/*  772 */     registerItem(263, "coal", new ItemCoal().setUnlocalizedName("coal"));
/*  773 */     registerItem(264, "diamond", new Item().setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials));
/*  774 */     registerItem(265, "iron_ingot", new Item().setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials));
/*  775 */     registerItem(266, "gold_ingot", new Item().setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials));
/*  776 */     registerItem(267, "iron_sword", new ItemSword(ToolMaterial.IRON).setUnlocalizedName("swordIron"));
/*  777 */     registerItem(268, "wooden_sword", new ItemSword(ToolMaterial.WOOD).setUnlocalizedName("swordWood"));
/*  778 */     registerItem(269, "wooden_shovel", new ItemSpade(ToolMaterial.WOOD).setUnlocalizedName("shovelWood"));
/*  779 */     registerItem(270, "wooden_pickaxe", new ItemPickaxe(ToolMaterial.WOOD).setUnlocalizedName("pickaxeWood"));
/*  780 */     registerItem(271, "wooden_axe", new ItemAxe(ToolMaterial.WOOD).setUnlocalizedName("hatchetWood"));
/*  781 */     registerItem(272, "stone_sword", new ItemSword(ToolMaterial.STONE).setUnlocalizedName("swordStone"));
/*  782 */     registerItem(273, "stone_shovel", new ItemSpade(ToolMaterial.STONE).setUnlocalizedName("shovelStone"));
/*  783 */     registerItem(274, "stone_pickaxe", new ItemPickaxe(ToolMaterial.STONE).setUnlocalizedName("pickaxeStone"));
/*  784 */     registerItem(275, "stone_axe", new ItemAxe(ToolMaterial.STONE).setUnlocalizedName("hatchetStone"));
/*  785 */     registerItem(276, "diamond_sword", new ItemSword(ToolMaterial.EMERALD).setUnlocalizedName("swordDiamond"));
/*  786 */     registerItem(277, "diamond_shovel", new ItemSpade(ToolMaterial.EMERALD).setUnlocalizedName("shovelDiamond"));
/*  787 */     registerItem(278, "diamond_pickaxe", new ItemPickaxe(ToolMaterial.EMERALD).setUnlocalizedName("pickaxeDiamond"));
/*  788 */     registerItem(279, "diamond_axe", new ItemAxe(ToolMaterial.EMERALD).setUnlocalizedName("hatchetDiamond"));
/*  789 */     registerItem(280, "stick", new Item().setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials));
/*  790 */     registerItem(281, "bowl", new Item().setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials));
/*  791 */     registerItem(282, "mushroom_stew", new ItemSoup(6).setUnlocalizedName("mushroomStew"));
/*  792 */     registerItem(283, "golden_sword", new ItemSword(ToolMaterial.GOLD).setUnlocalizedName("swordGold"));
/*  793 */     registerItem(284, "golden_shovel", new ItemSpade(ToolMaterial.GOLD).setUnlocalizedName("shovelGold"));
/*  794 */     registerItem(285, "golden_pickaxe", new ItemPickaxe(ToolMaterial.GOLD).setUnlocalizedName("pickaxeGold"));
/*  795 */     registerItem(286, "golden_axe", new ItemAxe(ToolMaterial.GOLD).setUnlocalizedName("hatchetGold"));
/*  796 */     registerItem(287, "string", new ItemReed(Blocks.tripwire).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials));
/*  797 */     registerItem(288, "feather", new Item().setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials));
/*  798 */     registerItem(289, "gunpowder", new Item().setUnlocalizedName("sulphur").setPotionEffect("+14&13-13").setCreativeTab(CreativeTabs.tabMaterials));
/*  799 */     registerItem(290, "wooden_hoe", new ItemHoe(ToolMaterial.WOOD).setUnlocalizedName("hoeWood"));
/*  800 */     registerItem(291, "stone_hoe", new ItemHoe(ToolMaterial.STONE).setUnlocalizedName("hoeStone"));
/*  801 */     registerItem(292, "iron_hoe", new ItemHoe(ToolMaterial.IRON).setUnlocalizedName("hoeIron"));
/*  802 */     registerItem(293, "diamond_hoe", new ItemHoe(ToolMaterial.EMERALD).setUnlocalizedName("hoeDiamond"));
/*  803 */     registerItem(294, "golden_hoe", new ItemHoe(ToolMaterial.GOLD).setUnlocalizedName("hoeGold"));
/*  804 */     registerItem(295, "wheat_seeds", new ItemSeeds(Blocks.wheat, Blocks.farmland).setUnlocalizedName("seeds"));
/*  805 */     registerItem(296, "wheat", new Item().setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials));
/*  806 */     registerItem(297, "bread", new ItemFood(5, 0.6F, false).setUnlocalizedName("bread"));
/*  807 */     registerItem(298, "leather_helmet", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 0).setUnlocalizedName("helmetCloth"));
/*  808 */     registerItem(299, "leather_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 1).setUnlocalizedName("chestplateCloth"));
/*  809 */     registerItem(300, "leather_leggings", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 2).setUnlocalizedName("leggingsCloth"));
/*  810 */     registerItem(301, "leather_boots", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 3).setUnlocalizedName("bootsCloth"));
/*  811 */     registerItem(302, "chainmail_helmet", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 0).setUnlocalizedName("helmetChain"));
/*  812 */     registerItem(303, "chainmail_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 1).setUnlocalizedName("chestplateChain"));
/*  813 */     registerItem(304, "chainmail_leggings", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 2).setUnlocalizedName("leggingsChain"));
/*  814 */     registerItem(305, "chainmail_boots", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 3).setUnlocalizedName("bootsChain"));
/*  815 */     registerItem(306, "iron_helmet", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 0).setUnlocalizedName("helmetIron"));
/*  816 */     registerItem(307, "iron_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 1).setUnlocalizedName("chestplateIron"));
/*  817 */     registerItem(308, "iron_leggings", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 2).setUnlocalizedName("leggingsIron"));
/*  818 */     registerItem(309, "iron_boots", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 3).setUnlocalizedName("bootsIron"));
/*  819 */     registerItem(310, "diamond_helmet", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 0).setUnlocalizedName("helmetDiamond"));
/*  820 */     registerItem(311, "diamond_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 1).setUnlocalizedName("chestplateDiamond"));
/*  821 */     registerItem(312, "diamond_leggings", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 2).setUnlocalizedName("leggingsDiamond"));
/*  822 */     registerItem(313, "diamond_boots", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 3).setUnlocalizedName("bootsDiamond"));
/*  823 */     registerItem(314, "golden_helmet", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 0).setUnlocalizedName("helmetGold"));
/*  824 */     registerItem(315, "golden_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 1).setUnlocalizedName("chestplateGold"));
/*  825 */     registerItem(316, "golden_leggings", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 2).setUnlocalizedName("leggingsGold"));
/*  826 */     registerItem(317, "golden_boots", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 3).setUnlocalizedName("bootsGold"));
/*  827 */     registerItem(318, "flint", new Item().setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials));
/*  828 */     registerItem(319, "porkchop", new ItemFood(3, 0.3F, true).setUnlocalizedName("porkchopRaw"));
/*  829 */     registerItem(320, "cooked_porkchop", new ItemFood(8, 0.8F, true).setUnlocalizedName("porkchopCooked"));
/*  830 */     registerItem(321, "painting", new ItemHangingEntity(net.minecraft.entity.item.EntityPainting.class).setUnlocalizedName("painting"));
/*  831 */     registerItem(322, "golden_apple", new ItemAppleGold(4, 1.2F, false).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 1, 1.0F).setUnlocalizedName("appleGold"));
/*  832 */     registerItem(323, "sign", new ItemSign().setUnlocalizedName("sign"));
/*  833 */     registerItem(324, "wooden_door", new ItemDoor(Blocks.oak_door).setUnlocalizedName("doorOak"));
/*  834 */     Item item = new ItemBucket(Blocks.air).setUnlocalizedName("bucket").setMaxStackSize(16);
/*  835 */     registerItem(325, "bucket", item);
/*  836 */     registerItem(326, "water_bucket", new ItemBucket(Blocks.flowing_water).setUnlocalizedName("bucketWater").setContainerItem(item));
/*  837 */     registerItem(327, "lava_bucket", new ItemBucket(Blocks.flowing_lava).setUnlocalizedName("bucketLava").setContainerItem(item));
/*  838 */     registerItem(328, "minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.RIDEABLE).setUnlocalizedName("minecart"));
/*  839 */     registerItem(329, "saddle", new ItemSaddle().setUnlocalizedName("saddle"));
/*  840 */     registerItem(330, "iron_door", new ItemDoor(Blocks.iron_door).setUnlocalizedName("doorIron"));
/*  841 */     registerItem(331, "redstone", new ItemRedstone().setUnlocalizedName("redstone").setPotionEffect("-5+6-7"));
/*  842 */     registerItem(332, "snowball", new ItemSnowball().setUnlocalizedName("snowball"));
/*  843 */     registerItem(333, "boat", new ItemBoat().setUnlocalizedName("boat"));
/*  844 */     registerItem(334, "leather", new Item().setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials));
/*  845 */     registerItem(335, "milk_bucket", new ItemBucketMilk().setUnlocalizedName("milk").setContainerItem(item));
/*  846 */     registerItem(336, "brick", new Item().setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials));
/*  847 */     registerItem(337, "clay_ball", new Item().setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials));
/*  848 */     registerItem(338, "reeds", new ItemReed(Blocks.reeds).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials));
/*  849 */     registerItem(339, "paper", new Item().setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc));
/*  850 */     registerItem(340, "book", new ItemBook().setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc));
/*  851 */     registerItem(341, "slime_ball", new Item().setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc));
/*  852 */     registerItem(342, "chest_minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.CHEST).setUnlocalizedName("minecartChest"));
/*  853 */     registerItem(343, "furnace_minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.FURNACE).setUnlocalizedName("minecartFurnace"));
/*  854 */     registerItem(344, "egg", new ItemEgg().setUnlocalizedName("egg"));
/*  855 */     registerItem(345, "compass", new Item().setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools));
/*  856 */     registerItem(346, "fishing_rod", new ItemFishingRod().setUnlocalizedName("fishingRod"));
/*  857 */     registerItem(347, "clock", new Item().setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools));
/*  858 */     registerItem(348, "glowstone_dust", new Item().setUnlocalizedName("yellowDust").setPotionEffect("+5-6-7").setCreativeTab(CreativeTabs.tabMaterials));
/*  859 */     registerItem(349, "fish", new ItemFishFood(false).setUnlocalizedName("fish").setHasSubtypes(true));
/*  860 */     registerItem(350, "cooked_fish", new ItemFishFood(true).setUnlocalizedName("fish").setHasSubtypes(true));
/*  861 */     registerItem(351, "dye", new ItemDye().setUnlocalizedName("dyePowder"));
/*  862 */     registerItem(352, "bone", new Item().setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc));
/*  863 */     registerItem(353, "sugar", new Item().setUnlocalizedName("sugar").setPotionEffect("-0+1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabMaterials));
/*  864 */     registerItem(354, "cake", new ItemReed(Blocks.cake).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.tabFood));
/*  865 */     registerItem(355, "bed", new ItemBed().setMaxStackSize(1).setUnlocalizedName("bed"));
/*  866 */     registerItem(356, "repeater", new ItemReed(Blocks.unpowered_repeater).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.tabRedstone));
/*  867 */     registerItem(357, "cookie", new ItemFood(2, 0.1F, false).setUnlocalizedName("cookie"));
/*  868 */     registerItem(358, "filled_map", new ItemMap().setUnlocalizedName("map"));
/*  869 */     registerItem(359, "shears", new ItemShears().setUnlocalizedName("shears"));
/*  870 */     registerItem(360, "melon", new ItemFood(2, 0.3F, false).setUnlocalizedName("melon"));
/*  871 */     registerItem(361, "pumpkin_seeds", new ItemSeeds(Blocks.pumpkin_stem, Blocks.farmland).setUnlocalizedName("seeds_pumpkin"));
/*  872 */     registerItem(362, "melon_seeds", new ItemSeeds(Blocks.melon_stem, Blocks.farmland).setUnlocalizedName("seeds_melon"));
/*  873 */     registerItem(363, "beef", new ItemFood(3, 0.3F, true).setUnlocalizedName("beefRaw"));
/*  874 */     registerItem(364, "cooked_beef", new ItemFood(8, 0.8F, true).setUnlocalizedName("beefCooked"));
/*  875 */     registerItem(365, "chicken", new ItemFood(2, 0.3F, true).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw"));
/*  876 */     registerItem(366, "cooked_chicken", new ItemFood(6, 0.6F, true).setUnlocalizedName("chickenCooked"));
/*  877 */     registerItem(367, "rotten_flesh", new ItemFood(4, 0.1F, true).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh"));
/*  878 */     registerItem(368, "ender_pearl", new ItemEnderPearl().setUnlocalizedName("enderPearl"));
/*  879 */     registerItem(369, "blaze_rod", new Item().setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials).setFull3D());
/*  880 */     registerItem(370, "ghast_tear", new Item().setUnlocalizedName("ghastTear").setPotionEffect("+0-1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  881 */     registerItem(371, "gold_nugget", new Item().setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials));
/*  882 */     registerItem(372, "nether_wart", new ItemSeeds(Blocks.nether_wart, Blocks.soul_sand).setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4"));
/*  883 */     registerItem(373, "potion", new ItemPotion().setUnlocalizedName("potion"));
/*  884 */     registerItem(374, "glass_bottle", new ItemGlassBottle().setUnlocalizedName("glassBottle"));
/*  885 */     registerItem(375, "spider_eye", new ItemFood(2, 0.8F, false).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setUnlocalizedName("spiderEye").setPotionEffect("-0-1+2-3&4-4+13"));
/*  886 */     registerItem(376, "fermented_spider_eye", new Item().setUnlocalizedName("fermentedSpiderEye").setPotionEffect("-0+3-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  887 */     registerItem(377, "blaze_powder", new Item().setUnlocalizedName("blazePowder").setPotionEffect("+0-1-2+3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  888 */     registerItem(378, "magma_cream", new Item().setUnlocalizedName("magmaCream").setPotionEffect("+0+1-2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  889 */     registerItem(379, "brewing_stand", new ItemReed(Blocks.brewing_stand).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing));
/*  890 */     registerItem(380, "cauldron", new ItemReed(Blocks.cauldron).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing));
/*  891 */     registerItem(381, "ender_eye", new ItemEnderEye().setUnlocalizedName("eyeOfEnder"));
/*  892 */     registerItem(382, "speckled_melon", new Item().setUnlocalizedName("speckledMelon").setPotionEffect("+0-1+2-3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  893 */     registerItem(383, "spawn_egg", new ItemMonsterPlacer().setUnlocalizedName("monsterPlacer"));
/*  894 */     registerItem(384, "experience_bottle", new ItemExpBottle().setUnlocalizedName("expBottle"));
/*  895 */     registerItem(385, "fire_charge", new ItemFireball().setUnlocalizedName("fireball"));
/*  896 */     registerItem(386, "writable_book", new ItemWritableBook().setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc));
/*  897 */     registerItem(387, "written_book", new ItemEditableBook().setUnlocalizedName("writtenBook").setMaxStackSize(16));
/*  898 */     registerItem(388, "emerald", new Item().setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials));
/*  899 */     registerItem(389, "item_frame", new ItemHangingEntity(EntityItemFrame.class).setUnlocalizedName("frame"));
/*  900 */     registerItem(390, "flower_pot", new ItemReed(Blocks.flower_pot).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations));
/*  901 */     registerItem(391, "carrot", new ItemSeedFood(3, 0.6F, Blocks.carrots, Blocks.farmland).setUnlocalizedName("carrots"));
/*  902 */     registerItem(392, "potato", new ItemSeedFood(1, 0.3F, Blocks.potatoes, Blocks.farmland).setUnlocalizedName("potato"));
/*  903 */     registerItem(393, "baked_potato", new ItemFood(5, 0.6F, false).setUnlocalizedName("potatoBaked"));
/*  904 */     registerItem(394, "poisonous_potato", new ItemFood(2, 0.3F, false).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous"));
/*  905 */     registerItem(395, "map", new ItemEmptyMap().setUnlocalizedName("emptyMap"));
/*  906 */     registerItem(396, "golden_carrot", new ItemFood(6, 1.2F, false).setUnlocalizedName("carrotGolden").setPotionEffect("-0+1+2-3+13&4-4").setCreativeTab(CreativeTabs.tabBrewing));
/*  907 */     registerItem(397, "skull", new ItemSkull().setUnlocalizedName("skull"));
/*  908 */     registerItem(398, "carrot_on_a_stick", new ItemCarrotOnAStick().setUnlocalizedName("carrotOnAStick"));
/*  909 */     registerItem(399, "nether_star", new ItemSimpleFoiled().setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials));
/*  910 */     registerItem(400, "pumpkin_pie", new ItemFood(8, 0.3F, false).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood));
/*  911 */     registerItem(401, "fireworks", new ItemFirework().setUnlocalizedName("fireworks"));
/*  912 */     registerItem(402, "firework_charge", new ItemFireworkCharge().setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc));
/*  913 */     registerItem(403, "enchanted_book", new ItemEnchantedBook().setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
/*  914 */     registerItem(404, "comparator", new ItemReed(Blocks.unpowered_comparator).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone));
/*  915 */     registerItem(405, "netherbrick", new Item().setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials));
/*  916 */     registerItem(406, "quartz", new Item().setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials));
/*  917 */     registerItem(407, "tnt_minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.TNT).setUnlocalizedName("minecartTnt"));
/*  918 */     registerItem(408, "hopper_minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.HOPPER).setUnlocalizedName("minecartHopper"));
/*  919 */     registerItem(409, "prismarine_shard", new Item().setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.tabMaterials));
/*  920 */     registerItem(410, "prismarine_crystals", new Item().setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.tabMaterials));
/*  921 */     registerItem(411, "rabbit", new ItemFood(3, 0.3F, true).setUnlocalizedName("rabbitRaw"));
/*  922 */     registerItem(412, "cooked_rabbit", new ItemFood(5, 0.6F, true).setUnlocalizedName("rabbitCooked"));
/*  923 */     registerItem(413, "rabbit_stew", new ItemSoup(10).setUnlocalizedName("rabbitStew"));
/*  924 */     registerItem(414, "rabbit_foot", new Item().setUnlocalizedName("rabbitFoot").setPotionEffect("+0+1-2+3&4-4+13").setCreativeTab(CreativeTabs.tabBrewing));
/*  925 */     registerItem(415, "rabbit_hide", new Item().setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.tabMaterials));
/*  926 */     registerItem(416, "armor_stand", new ItemArmorStand().setUnlocalizedName("armorStand").setMaxStackSize(16));
/*  927 */     registerItem(417, "iron_horse_armor", new Item().setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/*  928 */     registerItem(418, "golden_horse_armor", new Item().setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/*  929 */     registerItem(419, "diamond_horse_armor", new Item().setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
/*  930 */     registerItem(420, "lead", new ItemLead().setUnlocalizedName("leash"));
/*  931 */     registerItem(421, "name_tag", new ItemNameTag().setUnlocalizedName("nameTag"));
/*  932 */     registerItem(422, "command_block_minecart", new ItemMinecart(EntityMinecart.EnumMinecartType.COMMAND_BLOCK).setUnlocalizedName("minecartCommandBlock").setCreativeTab(null));
/*  933 */     registerItem(423, "mutton", new ItemFood(2, 0.3F, true).setUnlocalizedName("muttonRaw"));
/*  934 */     registerItem(424, "cooked_mutton", new ItemFood(6, 0.8F, true).setUnlocalizedName("muttonCooked"));
/*  935 */     registerItem(425, "banner", new ItemBanner().setUnlocalizedName("banner"));
/*  936 */     registerItem(427, "spruce_door", new ItemDoor(Blocks.spruce_door).setUnlocalizedName("doorSpruce"));
/*  937 */     registerItem(428, "birch_door", new ItemDoor(Blocks.birch_door).setUnlocalizedName("doorBirch"));
/*  938 */     registerItem(429, "jungle_door", new ItemDoor(Blocks.jungle_door).setUnlocalizedName("doorJungle"));
/*  939 */     registerItem(430, "acacia_door", new ItemDoor(Blocks.acacia_door).setUnlocalizedName("doorAcacia"));
/*  940 */     registerItem(431, "dark_oak_door", new ItemDoor(Blocks.dark_oak_door).setUnlocalizedName("doorDarkOak"));
/*  941 */     registerItem(2256, "record_13", new ItemRecord("13").setUnlocalizedName("record"));
/*  942 */     registerItem(2257, "record_cat", new ItemRecord("cat").setUnlocalizedName("record"));
/*  943 */     registerItem(2258, "record_blocks", new ItemRecord("blocks").setUnlocalizedName("record"));
/*  944 */     registerItem(2259, "record_chirp", new ItemRecord("chirp").setUnlocalizedName("record"));
/*  945 */     registerItem(2260, "record_far", new ItemRecord("far").setUnlocalizedName("record"));
/*  946 */     registerItem(2261, "record_mall", new ItemRecord("mall").setUnlocalizedName("record"));
/*  947 */     registerItem(2262, "record_mellohi", new ItemRecord("mellohi").setUnlocalizedName("record"));
/*  948 */     registerItem(2263, "record_stal", new ItemRecord("stal").setUnlocalizedName("record"));
/*  949 */     registerItem(2264, "record_strad", new ItemRecord("strad").setUnlocalizedName("record"));
/*  950 */     registerItem(2265, "record_ward", new ItemRecord("ward").setUnlocalizedName("record"));
/*  951 */     registerItem(2266, "record_11", new ItemRecord("11").setUnlocalizedName("record"));
/*  952 */     registerItem(2267, "record_wait", new ItemRecord("wait").setUnlocalizedName("record"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void registerItemBlock(Block blockIn)
/*      */   {
/*  960 */     registerItemBlock(blockIn, new ItemBlock(blockIn));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static void registerItemBlock(Block blockIn, Item itemIn)
/*      */   {
/*  968 */     registerItem(Block.getIdFromBlock(blockIn), (ResourceLocation)Block.blockRegistry.getNameForObject(blockIn), itemIn);
/*  969 */     BLOCK_TO_ITEM.put(blockIn, itemIn);
/*      */   }
/*      */   
/*      */   private static void registerItem(int id, String textualID, Item itemIn)
/*      */   {
/*  974 */     registerItem(id, new ResourceLocation(textualID), itemIn);
/*      */   }
/*      */   
/*      */   private static void registerItem(int id, ResourceLocation textualID, Item itemIn)
/*      */   {
/*  979 */     itemRegistry.register(id, textualID, itemIn);
/*      */   }
/*      */   
/*      */   public static enum ToolMaterial
/*      */   {
/*  984 */     WOOD(0, 59, 2.0F, 0.0F, 15), 
/*  985 */     STONE(1, 131, 4.0F, 1.0F, 5), 
/*  986 */     IRON(2, 250, 6.0F, 2.0F, 14), 
/*  987 */     EMERALD(3, 1561, 8.0F, 3.0F, 10), 
/*  988 */     GOLD(0, 32, 12.0F, 0.0F, 22);
/*      */     
/*      */     private final int harvestLevel;
/*      */     private final int maxUses;
/*      */     private final float efficiencyOnProperMaterial;
/*      */     private final float damageVsEntity;
/*      */     private final int enchantability;
/*      */     
/*      */     private ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability)
/*      */     {
/*  998 */       this.harvestLevel = harvestLevel;
/*  999 */       this.maxUses = maxUses;
/* 1000 */       this.efficiencyOnProperMaterial = efficiency;
/* 1001 */       this.damageVsEntity = damageVsEntity;
/* 1002 */       this.enchantability = enchantability;
/*      */     }
/*      */     
/*      */     public int getMaxUses()
/*      */     {
/* 1007 */       return this.maxUses;
/*      */     }
/*      */     
/*      */     public float getEfficiencyOnProperMaterial()
/*      */     {
/* 1012 */       return this.efficiencyOnProperMaterial;
/*      */     }
/*      */     
/*      */     public float getDamageVsEntity()
/*      */     {
/* 1017 */       return this.damageVsEntity;
/*      */     }
/*      */     
/*      */     public int getHarvestLevel()
/*      */     {
/* 1022 */       return this.harvestLevel;
/*      */     }
/*      */     
/*      */     public int getEnchantability()
/*      */     {
/* 1027 */       return this.enchantability;
/*      */     }
/*      */     
/*      */     public Item getRepairItem()
/*      */     {
/* 1032 */       return this == EMERALD ? Items.diamond : this == IRON ? Items.iron_ingot : this == GOLD ? Items.gold_ingot : this == STONE ? Item.getItemFromBlock(Blocks.cobblestone) : this == WOOD ? Item.getItemFromBlock(Blocks.planks) : null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */