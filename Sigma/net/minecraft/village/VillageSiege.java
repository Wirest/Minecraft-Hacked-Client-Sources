package net.minecraft.village;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;

public class VillageSiege {
    private World worldObj;
    private boolean field_75535_b;
    private int field_75536_c = -1;
    private int field_75533_d;
    private int field_75534_e;

    /**
     * Instance of Village.
     */
    private Village theVillage;
    private int field_75532_g;
    private int field_75538_h;
    private int field_75539_i;
    private static final String __OBFID = "CL_00001634";

    public VillageSiege(World worldIn) {
        worldObj = worldIn;
    }

    /**
     * Runs a single tick for the village siege
     */
    public void tick() {
        if (worldObj.isDaytime()) {
            field_75536_c = 0;
        } else if (field_75536_c != 2) {
            if (field_75536_c == 0) {
                float var1 = worldObj.getCelestialAngle(0.0F);

                if (var1 < 0.5D || var1 > 0.501D) {
                    return;
                }

                field_75536_c = worldObj.rand.nextInt(10) == 0 ? 1 : 2;
                field_75535_b = false;

                if (field_75536_c == 2) {
                    return;
                }
            }

            if (field_75536_c != -1) {
                if (!field_75535_b) {
                    if (!func_75529_b()) {
                        return;
                    }

                    field_75535_b = true;
                }

                if (field_75534_e > 0) {
                    --field_75534_e;
                } else {
                    field_75534_e = 2;

                    if (field_75533_d > 0) {
                        spawnZombie();
                        --field_75533_d;
                    } else {
                        field_75536_c = 2;
                    }
                }
            }
        }
    }

    private boolean func_75529_b() {
        List var1 = worldObj.playerEntities;
        Iterator var2 = var1.iterator();

        while (var2.hasNext()) {
            EntityPlayer var3 = (EntityPlayer) var2.next();

            if (!var3.func_175149_v()) {
                theVillage = worldObj.getVillageCollection().func_176056_a(new BlockPos(var3), 1);

                if (theVillage != null && theVillage.getNumVillageDoors() >= 10 && theVillage.getTicksSinceLastDoorAdding() >= 20 && theVillage.getNumVillagers() >= 20) {
                    BlockPos var4 = theVillage.func_180608_a();
                    float var5 = theVillage.getVillageRadius();
                    boolean var6 = false;
                    int var7 = 0;

                    while (true) {
                        if (var7 < 10) {
                            float var8 = worldObj.rand.nextFloat() * (float) Math.PI * 2.0F;
                            field_75532_g = var4.getX() + (int) (MathHelper.cos(var8) * var5 * 0.9D);
                            field_75538_h = var4.getY();
                            field_75539_i = var4.getZ() + (int) (MathHelper.sin(var8) * var5 * 0.9D);
                            var6 = false;
                            Iterator var9 = worldObj.getVillageCollection().getVillageList().iterator();

                            while (var9.hasNext()) {
                                Village var10 = (Village) var9.next();

                                if (var10 != theVillage && var10.func_179866_a(new BlockPos(field_75532_g, field_75538_h, field_75539_i))) {
                                    var6 = true;
                                    break;
                                }
                            }

                            if (var6) {
                                ++var7;
                                continue;
                            }
                        }

                        if (var6) {
                            return false;
                        }

                        Vec3 var11 = func_179867_a(new BlockPos(field_75532_g, field_75538_h, field_75539_i));

                        if (var11 != null) {
                            field_75534_e = 0;
                            field_75533_d = 20;
                            return true;
                        }

                        break;
                    }
                }
            }
        }

        return false;
    }

    private boolean spawnZombie() {
        Vec3 var1 = func_179867_a(new BlockPos(field_75532_g, field_75538_h, field_75539_i));

        if (var1 == null) {
            return false;
        } else {
            EntityZombie var2;

            try {
                var2 = new EntityZombie(worldObj);
                var2.func_180482_a(worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData) null);
                var2.setVillager(false);
            } catch (Exception var4) {
                var4.printStackTrace();
                return false;
            }

            var2.setLocationAndAngles(var1.xCoord, var1.yCoord, var1.zCoord, worldObj.rand.nextFloat() * 360.0F, 0.0F);
            worldObj.spawnEntityInWorld(var2);
            BlockPos var3 = theVillage.func_180608_a();
            var2.func_175449_a(var3, theVillage.getVillageRadius());
            return true;
        }
    }

    private Vec3 func_179867_a(BlockPos p_179867_1_) {
        for (int var2 = 0; var2 < 10; ++var2) {
            BlockPos var3 = p_179867_1_.add(worldObj.rand.nextInt(16) - 8, worldObj.rand.nextInt(6) - 3, worldObj.rand.nextInt(16) - 8);

            if (theVillage.func_179866_a(var3) && SpawnerAnimals.func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, worldObj, var3)) {
                return new Vec3(var3.getX(), var3.getY(), var3.getZ());
            }
        }

        return null;
    }
}
