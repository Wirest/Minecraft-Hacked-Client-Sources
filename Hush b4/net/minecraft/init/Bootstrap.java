// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.init;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemDye;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockTNT;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.IProjectile;
import net.minecraft.dispenser.IPosition;
import net.minecraft.world.World;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.block.BlockDispenser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class Bootstrap
{
    private static final PrintStream SYSOUT;
    private static boolean alreadyRegistered;
    private static final Logger LOGGER;
    
    static {
        SYSOUT = System.out;
        Bootstrap.alreadyRegistered = false;
        LOGGER = LogManager.getLogger();
    }
    
    public static boolean isRegistered() {
        return Bootstrap.alreadyRegistered;
    }
    
    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                final EntityArrow entityarrow = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.canBePickedUp = 1;
                return entityarrow;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }
            
            @Override
            protected float func_82498_a() {
                return super.func_82498_a() * 0.5f;
            }
            
            @Override
            protected float func_82500_b() {
                return super.func_82500_b() * 1.25f;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
            
            @Override
            public ItemStack dispense(final IBlockSource source, final ItemStack stack) {
                return ItemPotion.isSplash(stack.getMetadata()) ? new BehaviorProjectileDispense() {
                    @Override
                    protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }
                    
                    @Override
                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5f;
                    }
                    
                    @Override
                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25f;
                    }
                }.dispense(source, stack) : this.field_150843_b.dispense(source, stack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final double d0 = source.getX() + enumfacing.getFrontOffsetX();
                final double d2 = source.getBlockPos().getY() + 0.2f;
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ();
                final Entity entity = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), d0, d2, d3);
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
                }
                stack.splitStack(1);
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final double d0 = source.getX() + enumfacing.getFrontOffsetX();
                final double d2 = source.getBlockPos().getY() + 0.2f;
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ();
                final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d2, d3, stack);
                source.getWorld().spawnEntityInWorld(entityfireworkrocket);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final IPosition iposition = BlockDispenser.getDispensePosition(source);
                final double d0 = iposition.getX() + enumfacing.getFrontOffsetX() * 0.3f;
                final double d2 = iposition.getY() + enumfacing.getFrontOffsetY() * 0.3f;
                final double d3 = iposition.getZ() + enumfacing.getFrontOffsetZ() * 0.3f;
                final World world = source.getWorld();
                final Random random = world.rand;
                final double d4 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetX();
                final double d5 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetY();
                final double d6 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetZ();
                world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d2, d3, d4, d5, d6));
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final World world = source.getWorld();
                final double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125f;
                final double d2 = source.getY() + enumfacing.getFrontOffsetY() * 1.125f;
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125f;
                final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                final Material material = world.getBlockState(blockpos).getBlock().getMaterial();
                double d4;
                if (Material.water.equals(material)) {
                    d4 = 1.0;
                }
                else {
                    if (!Material.air.equals(material) || !Material.water.equals(world.getBlockState(blockpos.down()).getBlock().getMaterial())) {
                        return this.field_150842_b.dispense(source, stack);
                    }
                    d4 = 0.0;
                }
                final EntityBoat entityboat = new EntityBoat(world, d0, d2 + d4, d3);
                world.spawnEntityInWorld(entityboat);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        });
        final IBehaviorDispenseItem ibehaviordispenseitem = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final ItemBucket itembucket = (ItemBucket)stack.getItem();
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                if (itembucket.tryPlaceContainedLiquid(source.getWorld(), blockpos)) {
                    stack.setItem(Items.bucket);
                    stack.stackSize = 1;
                    return stack;
                }
                return this.field_150841_b.dispense(source, stack);
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, ibehaviordispenseitem);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, ibehaviordispenseitem);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                final Material material = block.getMaterial();
                Item item;
                if (Material.water.equals(material) && block instanceof BlockLiquid && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                    item = Items.water_bucket;
                }
                else {
                    if (!Material.lava.equals(material) || !(block instanceof BlockLiquid) || iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(source, stack);
                    }
                    item = Items.lava_bucket;
                }
                world.setBlockToAir(blockpos);
                final int stackSize = stack.stackSize - 1;
                stack.stackSize = stackSize;
                if (stackSize == 0) {
                    stack.setItem(item);
                    stack.stackSize = 1;
                }
                else if (source.getBlockTileEntity().addItemStack(new ItemStack(item)) < 0) {
                    this.field_150840_b.dispense(source, new ItemStack(item));
                }
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
            private boolean field_150839_b = true;
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                if (world.isAirBlock(blockpos)) {
                    world.setBlockState(blockpos, Blocks.fire.getDefaultState());
                    if (stack.attemptDamageItem(1, world.rand)) {
                        stack.stackSize = 0;
                    }
                }
                else if (world.getBlockState(blockpos).getBlock() == Blocks.tnt) {
                    Blocks.tnt.onBlockDestroyedByPlayer(world, blockpos, Blocks.tnt.getDefaultState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
                    world.setBlockToAir(blockpos);
                }
                else {
                    this.field_150839_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_150839_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
            private boolean field_150838_b = true;
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(stack.getMetadata())) {
                    final World world = source.getWorld();
                    final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                    if (ItemDye.applyBonemeal(stack, world, blockpos)) {
                        if (!world.isRemote) {
                            world.playAuxSFX(2005, blockpos, 0);
                        }
                    }
                    else {
                        this.field_150838_b = false;
                    }
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_150838_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5, null);
                world.spawnEntityInWorld(entitytntprimed);
                world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
                --stack.stackSize;
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
            private boolean field_179240_b = true;
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                final BlockSkull blockskull = Blocks.skull;
                if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, stack)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockskull.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.UP), 3);
                        final TileEntity tileentity = world.getTileEntity(blockpos);
                        if (tileentity instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile gameprofile = null;
                                if (stack.hasTagCompound()) {
                                    final NBTTagCompound nbttagcompound = stack.getTagCompound();
                                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                    }
                                    else if (nbttagcompound.hasKey("SkullOwner", 8)) {
                                        final String s = nbttagcompound.getString("SkullOwner");
                                        if (!StringUtils.isNullOrEmpty(s)) {
                                            gameprofile = new GameProfile(null, s);
                                        }
                                    }
                                }
                                ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
                            }
                            else {
                                ((TileEntitySkull)tileentity).setType(stack.getMetadata());
                            }
                            ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
                            Blocks.skull.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
                        }
                        --stack.stackSize;
                    }
                }
                else {
                    this.field_179240_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_179240_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem() {
            private boolean field_179241_b = true;
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.pumpkin;
                if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
                    }
                    --stack.stackSize;
                }
                else {
                    this.field_179241_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_179241_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
    }
    
    public static void register() {
        if (!Bootstrap.alreadyRegistered) {
            Bootstrap.alreadyRegistered = true;
            if (Bootstrap.LOGGER.isDebugEnabled()) {
                redirectOutputToLog();
            }
            Block.registerBlocks();
            BlockFire.init();
            Item.registerItems();
            StatList.init();
            registerDispenserBehaviors();
        }
    }
    
    private static void redirectOutputToLog() {
        System.setErr(new LoggingPrintStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
    }
    
    public static void printToSYSOUT(final String p_179870_0_) {
        Bootstrap.SYSOUT.println(p_179870_0_);
    }
}
