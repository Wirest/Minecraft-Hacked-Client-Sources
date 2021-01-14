package net.minecraft.init;

import com.mojang.authlib.GameProfile;

import java.io.PrintStream;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
    private static final PrintStream SYSOUT = System.out;

    /**
     * Whether the blocks, items, etc have already been registered
     */
    private static boolean alreadyRegistered = false;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String __OBFID = "CL_00001397";

    /**
     * Is Bootstrap registration already done?
     */
    public static boolean isRegistered() {
        return alreadyRegistered;
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001398";

            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                EntityArrow var3 = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
                var3.canBePickedUp = 1;
                return var3;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001404";

            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001405";

            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001406";

            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }

            protected float func_82498_a() {
                return super.func_82498_a() * 0.5F;
            }

            protected float func_82500_b() {
                return super.func_82500_b() * 1.25F;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001407";

            public ItemStack dispense(IBlockSource source, final ItemStack stack) {
                return ItemPotion.isSplash(stack.getMetadata()) ? (new BehaviorProjectileDispense() {
                    private static final String __OBFID = "CL_00001408";

                    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }

                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5F;
                    }

                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25F;
                    }
                }).dispense(source, stack) : this.field_150843_b.dispense(source, stack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001410";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                double var4 = source.getX() + (double) var3.getFrontOffsetX();
                double var6 = (double) ((float) source.getBlockPos().getY() + 0.2F);
                double var8 = source.getZ() + (double) var3.getFrontOffsetZ();
                Entity var10 = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), var4, var6, var8);

                if (var10 instanceof EntityLivingBase && stack.hasDisplayName()) {
                    ((EntityLiving) var10).setCustomNameTag(stack.getDisplayName());
                }

                stack.splitStack(1);
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001411";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                double var4 = source.getX() + (double) var3.getFrontOffsetX();
                double var6 = (double) ((float) source.getBlockPos().getY() + 0.2F);
                double var8 = source.getZ() + (double) var3.getFrontOffsetZ();
                EntityFireworkRocket var10 = new EntityFireworkRocket(source.getWorld(), var4, var6, var8, stack);
                source.getWorld().spawnEntityInWorld(var10);
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001412";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                IPosition var4 = BlockDispenser.getDispensePosition(source);
                double var5 = var4.getX() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
                double var7 = var4.getY() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
                double var9 = var4.getZ() + (double) ((float) var3.getFrontOffsetZ() * 0.3F);
                World var11 = source.getWorld();
                Random var12 = var11.rand;
                double var13 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetX();
                double var15 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetY();
                double var17 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetZ();
                var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001413";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                World var4 = source.getWorld();
                double var5 = source.getX() + (double) ((float) var3.getFrontOffsetX() * 1.125F);
                double var7 = source.getY() + (double) ((float) var3.getFrontOffsetY() * 1.125F);
                double var9 = source.getZ() + (double) ((float) var3.getFrontOffsetZ() * 1.125F);
                BlockPos var11 = source.getBlockPos().offset(var3);
                Material var12 = var4.getBlockState(var11).getBlock().getMaterial();
                double var13;

                if (Material.water.equals(var12)) {
                    var13 = 1.0D;
                } else {
                    if (!Material.air.equals(var12) || !Material.water.equals(var4.getBlockState(var11.offsetDown()).getBlock().getMaterial())) {
                        return this.field_150842_b.dispense(source, stack);
                    }

                    var13 = 0.0D;
                }

                EntityBoat var15 = new EntityBoat(var4, var5, var7 + var13, var9);
                var4.spawnEntityInWorld(var15);
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        });
        BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001399";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                ItemBucket var3 = (ItemBucket) stack.getItem();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.func_180616_a(source.getWorld(), var4)) {
                    stack.setItem(Items.bucket);
                    stack.stackSize = 1;
                    return stack;
                } else {
                    return this.field_150841_b.dispense(source, stack);
                }
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001400";

            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                IBlockState var5 = var3.getBlockState(var4);
                Block var6 = var5.getBlock();
                Material var7 = var6.getMaterial();
                Item var8;

                if (Material.water.equals(var7) && var6 instanceof BlockLiquid && ((Integer) var5.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                    var8 = Items.water_bucket;
                } else {
                    if (!Material.lava.equals(var7) || !(var6 instanceof BlockLiquid) || ((Integer) var5.getValue(BlockLiquid.LEVEL)).intValue() != 0) {
                        return super.dispenseStack(source, stack);
                    }

                    var8 = Items.lava_bucket;
                }

                var3.setBlockToAir(var4);

                if (--stack.stackSize == 0) {
                    stack.setItem(var8);
                    stack.stackSize = 1;
                } else if (((TileEntityDispenser) source.getBlockTileEntity()).func_146019_a(new ItemStack(var8)) < 0) {
                    this.field_150840_b.dispense(source, new ItemStack(var8));
                }

                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
            private boolean field_150839_b = true;
            private static final String __OBFID = "CL_00001401";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.isAirBlock(var4)) {
                    var3.setBlockState(var4, Blocks.fire.getDefaultState());

                    if (stack.attemptDamageItem(1, var3.rand)) {
                        stack.stackSize = 0;
                    }
                } else if (var3.getBlockState(var4).getBlock() == Blocks.tnt) {
                    Blocks.tnt.onBlockDestroyedByPlayer(var3, var4, Blocks.tnt.getDefaultState().withProperty(BlockTNT.field_176246_a, Boolean.valueOf(true)));
                    var3.setBlockToAir(var4);
                } else {
                    this.field_150839_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_150839_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
            private boolean field_150838_b = true;
            private static final String __OBFID = "CL_00001402";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                if (EnumDyeColor.WHITE == EnumDyeColor.func_176766_a(stack.getMetadata())) {
                    World var3 = source.getWorld();
                    BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                    if (ItemDye.func_179234_a(stack, var3, var4)) {
                        if (!var3.isRemote) {
                            var3.playAuxSFX(2005, var4, 0);
                        }
                    } else {
                        this.field_150838_b = false;
                    }

                    return stack;
                } else {
                    return super.dispenseStack(source, stack);
                }
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_150838_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001403";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                EntityTNTPrimed var5 = new EntityTNTPrimed(var3, (double) var4.getX() + 0.5D, (double) var4.getY(), (double) var4.getZ() + 0.5D, (EntityLivingBase) null);
                var3.spawnEntityInWorld(var5);
                var3.playSoundAtEntity(var5, "game.tnt.primed", 1.0F, 1.0F);
                --stack.stackSize;
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
            private boolean field_179240_b = true;
            private static final String __OBFID = "CL_00002278";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                EnumFacing var4 = BlockDispenser.getFacing(source.getBlockMetadata());
                BlockPos var5 = source.getBlockPos().offset(var4);
                BlockSkull var6 = Blocks.skull;

                if (var3.isAirBlock(var5) && var6.func_176415_b(var3, var5, stack)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var5, var6.getDefaultState().withProperty(BlockSkull.field_176418_a, EnumFacing.UP), 3);
                        TileEntity var7 = var3.getTileEntity(var5);

                        if (var7 instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile var8 = null;

                                if (stack.hasTagCompound()) {
                                    NBTTagCompound var9 = stack.getTagCompound();

                                    if (var9.hasKey("SkullOwner", 10)) {
                                        var8 = NBTUtil.readGameProfileFromNBT(var9.getCompoundTag("SkullOwner"));
                                    } else if (var9.hasKey("SkullOwner", 8)) {
                                        var8 = new GameProfile((UUID) null, var9.getString("SkullOwner"));
                                    }
                                }

                                ((TileEntitySkull) var7).setPlayerProfile(var8);
                            } else {
                                ((TileEntitySkull) var7).setType(stack.getMetadata());
                            }

                            ((TileEntitySkull) var7).setSkullRotation(var4.getOpposite().getHorizontalIndex() * 4);
                            Blocks.skull.func_180679_a(var3, var5, (TileEntitySkull) var7);
                        }

                        --stack.stackSize;
                    }
                } else {
                    this.field_179240_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_179240_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem() {
            private boolean field_179241_b = true;
            private static final String __OBFID = "CL_00002277";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                BlockPumpkin var5 = (BlockPumpkin) Blocks.pumpkin;

                if (var3.isAirBlock(var4) && var5.func_176390_d(var3, var4)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var4, var5.getDefaultState(), 3);
                    }

                    --stack.stackSize;
                } else {
                    this.field_179241_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_179241_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.command_block), new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00002276";

            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.isAirBlock(var4)) {
                    if (!var3.isRemote) {
                        IBlockState var5 = Blocks.command_block.getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf(false));
                        var3.setBlockState(var4, var5, 3);
                        ItemBlock.setTileEntityNBT(var3, var4, stack);
                        var3.notifyNeighborsOfStateChange(source.getBlockPos(), source.getBlock());
                    }

                    --stack.stackSize;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
            }

            protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
            }
        });
    }

    /**
     * Registers blocks, items, stats, etc.
     */
    public static void register() {
        if (!alreadyRegistered) {
            alreadyRegistered = true;

            if (LOGGER.isDebugEnabled()) {
                redirectOutputToLog();
            }

            Block.registerBlocks();
            BlockFire.func_149843_e();
            Item.registerItems();
            StatList.func_151178_a();
            registerDispenserBehaviors();
        }
    }

    /**
     * redirect standard streams to logger
     */
    private static void redirectOutputToLog() {
        System.setErr(new LoggingPrintStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
    }

    public static void func_179870_a(String p_179870_0_) {
        SYSOUT.println(p_179870_0_);
    }
}
