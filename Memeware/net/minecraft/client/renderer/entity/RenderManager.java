package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import optifine.PlayerItemsLayer;
import optifine.Reflector;

public class RenderManager {
    /**
     * A map of entity classes and the associated renderer.
     */
    private Map entityRenderMap = Maps.newHashMap();
    private Map field_178636_l = Maps.newHashMap();
    private RenderPlayer field_178637_m;

    /**
     * Renders fonts
     */
    private FontRenderer textRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;

    /**
     * Reference to the World object.
     */
    public World worldObj;

    /**
     * Rendermanager's variable for the player
     */
    public Entity livingPlayer;
    public Entity field_147941_i;
    public static float playerViewY;
    public static float playerViewX;

    /**
     * Reference to the GameSettings object.
     */
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean field_178639_r = false;
    private boolean field_178638_s = true;

    /**
     * whether bounding box should be rendered or not
     */
    private boolean debugBoundingBox = false;
    private static final String __OBFID = "CL_00000991";

    public RenderManager(TextureManager p_i46180_1_, RenderItem p_i46180_2_) {
        this.renderEngine = p_i46180_1_;
        this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
        this.entityRenderMap.put(EntityPig.class, new RenderPig(this, new ModelPig(), 0.7F));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this, new ModelSheep2(), 0.7F));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(this, new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this, new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this, new ModelWolf(), 0.5F));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this, new ModelChicken(), 0.3F));
        this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this, new ModelOcelot(), 0.4F));
        this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this, new ModelRabbit(), 0.3F));
        this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
        this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
        this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
        this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
        this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
        this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
        this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
        this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
        this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this, new ModelSlime(16), 0.25F));
        this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this, new ModelSquid(), 0.7F));
        this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
        this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
        this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
        this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
        this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
        this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
        this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
        this.entityRenderMap.put(Entity.class, new RenderEntity(this));
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
        this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, p_i46180_2_));
        this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow(this));
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.snowball, p_i46180_2_));
        this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ender_pearl, p_i46180_2_));
        this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ender_eye, p_i46180_2_));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.egg, p_i46180_2_));
        this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, p_i46180_2_));
        this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.experience_bottle, p_i46180_2_));
        this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.fireworks, p_i46180_2_));
        this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
        this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
        this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
        this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, p_i46180_2_));
        this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
        this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
        this.entityRenderMap.put(EntityArmorStand.class, new ArmorStandRenderer(this));
        this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
        this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
        this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
        this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this, new ModelHorse(), 0.75F));
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
        this.field_178637_m = new RenderPlayer(this);
        this.field_178636_l.put("default", this.field_178637_m);
        this.field_178636_l.put("slim", new RenderPlayer(this, true));
        PlayerItemsLayer.register(this.field_178636_l);

        if (Reflector.RenderingRegistry_loadEntityRenderers.exists()) {
            Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, new Object[]{this, this.entityRenderMap});
        }
    }

    public void func_178628_a(double p_178628_1_, double p_178628_3_, double p_178628_5_) {
        this.renderPosX = p_178628_1_;
        this.renderPosY = p_178628_3_;
        this.renderPosZ = p_178628_5_;
    }

    public Render getEntityClassRenderObject(Class p_78715_1_) {
        Render var2 = (Render) this.entityRenderMap.get(p_78715_1_);

        if (var2 == null && p_78715_1_ != Entity.class) {
            var2 = this.getEntityClassRenderObject(p_78715_1_.getSuperclass());
            this.entityRenderMap.put(p_78715_1_, var2);
        }

        return var2;
    }

    public Render getEntityRenderObject(Entity p_78713_1_) {
        if (p_78713_1_ instanceof AbstractClientPlayer) {
            String var2 = ((AbstractClientPlayer) p_78713_1_).func_175154_l();
            RenderPlayer var3 = (RenderPlayer) this.field_178636_l.get(var2);
            return var3 != null ? var3 : this.field_178637_m;
        } else {
            return this.getEntityClassRenderObject(p_78713_1_.getClass());
        }
    }

    public void func_180597_a(World worldIn, FontRenderer p_180597_2_, Entity p_180597_3_, Entity p_180597_4_, GameSettings p_180597_5_, float p_180597_6_) {
        this.worldObj = worldIn;
        this.options = p_180597_5_;
        this.livingPlayer = p_180597_3_;
        this.field_147941_i = p_180597_4_;
        this.textRenderer = p_180597_2_;

        if (p_180597_3_ instanceof EntityLivingBase && ((EntityLivingBase) p_180597_3_).isPlayerSleeping()) {
            IBlockState var7 = worldIn.getBlockState(new BlockPos(p_180597_3_));
            Block var8 = var7.getBlock();

            if (Reflector.callBoolean(Reflector.ForgeBlock_isBed, new Object[]{worldIn, new BlockPos(p_180597_3_), (EntityLivingBase) p_180597_3_})) {
                EnumFacing var9 = (EnumFacing) Reflector.call(var8, Reflector.ForgeBlock_getBedDirection, new Object[]{worldIn, new BlockPos(p_180597_3_)});
                int var91 = var9.getHorizontalIndex();
                this.playerViewY = (float) (var91 * 90 + 180);
                this.playerViewX = 0.0F;
            } else if (var8 == Blocks.bed) {
                int var92 = ((EnumFacing) var7.getValue(BlockBed.AGE)).getHorizontalIndex();
                this.playerViewY = (float) (var92 * 90 + 180);
                this.playerViewX = 0.0F;
            }
        } else {
            this.playerViewY = p_180597_3_.prevRotationYaw + (p_180597_3_.rotationYaw - p_180597_3_.prevRotationYaw) * p_180597_6_;
            this.playerViewX = p_180597_3_.prevRotationPitch + (p_180597_3_.rotationPitch - p_180597_3_.prevRotationPitch) * p_180597_6_;
        }

        if (p_180597_5_.thirdPersonView == 2) {
            this.playerViewY += 180.0F;
        }

        this.viewerPosX = p_180597_3_.lastTickPosX + (p_180597_3_.posX - p_180597_3_.lastTickPosX) * (double) p_180597_6_;
        this.viewerPosY = p_180597_3_.lastTickPosY + (p_180597_3_.posY - p_180597_3_.lastTickPosY) * (double) p_180597_6_;
        this.viewerPosZ = p_180597_3_.lastTickPosZ + (p_180597_3_.posZ - p_180597_3_.lastTickPosZ) * (double) p_180597_6_;
    }

    public void func_178631_a(float p_178631_1_) {
        this.playerViewY = p_178631_1_;
    }

    public boolean func_178627_a() {
        return this.field_178638_s;
    }

    public void func_178633_a(boolean p_178633_1_) {
        this.field_178638_s = p_178633_1_;
    }

    public void func_178629_b(boolean p_178629_1_) {
        this.debugBoundingBox = p_178629_1_;
    }

    public boolean func_178634_b() {
        return this.debugBoundingBox;
    }

    public boolean renderEntitySimple(Entity p_147937_1_, float p_147937_2_) {
        return this.renderEntityStatic(p_147937_1_, p_147937_2_, false);
    }

    public boolean func_178635_a(Entity p_178635_1_, ICamera p_178635_2_, double p_178635_3_, double p_178635_5_, double p_178635_7_) {
        Render var9 = this.getEntityRenderObject(p_178635_1_);
        return var9 != null && var9.func_177071_a(p_178635_1_, p_178635_2_, p_178635_3_, p_178635_5_, p_178635_7_);
    }

    public boolean renderEntityStatic(Entity p_147936_1_, float p_147936_2_, boolean p_147936_3_) {
        if (p_147936_1_.ticksExisted == 0) {
            p_147936_1_.lastTickPosX = p_147936_1_.posX;
            p_147936_1_.lastTickPosY = p_147936_1_.posY;
            p_147936_1_.lastTickPosZ = p_147936_1_.posZ;
        }

        double var4 = p_147936_1_.lastTickPosX + (p_147936_1_.posX - p_147936_1_.lastTickPosX) * (double) p_147936_2_;
        double var6 = p_147936_1_.lastTickPosY + (p_147936_1_.posY - p_147936_1_.lastTickPosY) * (double) p_147936_2_;
        double var8 = p_147936_1_.lastTickPosZ + (p_147936_1_.posZ - p_147936_1_.lastTickPosZ) * (double) p_147936_2_;
        float var10 = p_147936_1_.prevRotationYaw + (p_147936_1_.rotationYaw - p_147936_1_.prevRotationYaw) * p_147936_2_;
        int var11 = p_147936_1_.getBrightnessForRender(p_147936_2_);

        if (p_147936_1_.isBurning()) {
            var11 = 15728880;
        }

        int var12 = var11 % 65536;
        int var13 = var11 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var12 / 1.0F, (float) var13 / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        return this.doRenderEntity(p_147936_1_, var4 - this.renderPosX, var6 - this.renderPosY, var8 - this.renderPosZ, var10, p_147936_2_, p_147936_3_);
    }

    public void func_178630_b(Entity p_178630_1_, float p_178630_2_) {
        double var3 = p_178630_1_.lastTickPosX + (p_178630_1_.posX - p_178630_1_.lastTickPosX) * (double) p_178630_2_;
        double var5 = p_178630_1_.lastTickPosY + (p_178630_1_.posY - p_178630_1_.lastTickPosY) * (double) p_178630_2_;
        double var7 = p_178630_1_.lastTickPosZ + (p_178630_1_.posZ - p_178630_1_.lastTickPosZ) * (double) p_178630_2_;
        Render var9 = this.getEntityRenderObject(p_178630_1_);

        if (var9 != null && this.renderEngine != null) {
            int var10 = p_178630_1_.getBrightnessForRender(p_178630_2_);
            int var11 = var10 % 65536;
            int var12 = var10 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var11 / 1.0F, (float) var12 / 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            var9.func_177067_a(p_178630_1_, var3 - this.renderPosX, var5 - this.renderPosY, var7 - this.renderPosZ);
        }
    }

    public boolean renderEntityWithPosYaw(Entity p_147940_1_, double p_147940_2_, double p_147940_4_, double p_147940_6_, float p_147940_8_, float p_147940_9_) {
        return this.doRenderEntity(p_147940_1_, p_147940_2_, p_147940_4_, p_147940_6_, p_147940_8_, p_147940_9_, false);
    }

    public boolean doRenderEntity(Entity p_147939_1_, double p_147939_2_, double p_147939_4_, double p_147939_6_, float p_147939_8_, float p_147939_9_, boolean p_147939_10_) {
        Render var11 = null;

        try {
            var11 = this.getEntityRenderObject(p_147939_1_);

            if (var11 != null && this.renderEngine != null) {
                try {
                    if (var11 instanceof RendererLivingEntity) {
                        ((RendererLivingEntity) var11).func_177086_a(this.field_178639_r);
                    }

                    var11.doRender(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                } catch (Throwable var18) {
                    throw new ReportedException(CrashReport.makeCrashReport(var18, "Rendering entity in world"));
                }

                try {
                    if (!this.field_178639_r) {
                        var11.doRenderShadowAndFire(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    }
                } catch (Throwable var17) {
                    throw new ReportedException(CrashReport.makeCrashReport(var17, "Post-rendering entity in world"));
                }

                if (this.debugBoundingBox && !p_147939_1_.isInvisible() && !p_147939_10_) {
                    try {
                        this.renderDebugBoundingBox(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    } catch (Throwable var16) {
                        throw new ReportedException(CrashReport.makeCrashReport(var16, "Rendering entity hitbox in world"));
                    }
                }
            } else if (this.renderEngine != null) {
                return false;
            }

            return true;
        } catch (Throwable var19) {
            CrashReport var13 = CrashReport.makeCrashReport(var19, "Rendering entity in world");
            CrashReportCategory var14 = var13.makeCategory("Entity being rendered");
            p_147939_1_.addEntityCrashInfo(var14);
            CrashReportCategory var15 = var13.makeCategory("Renderer details");
            var15.addCrashSection("Assigned renderer", var11);
            var15.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(p_147939_2_, p_147939_4_, p_147939_6_));
            var15.addCrashSection("Rotation", Float.valueOf(p_147939_8_));
            var15.addCrashSection("Delta", Float.valueOf(p_147939_9_));
            throw new ReportedException(var13);
        }
    }

    /**
     * Renders the bounding box around an entity when F3+B is pressed
     */
    private void renderDebugBoundingBox(Entity p_85094_1_, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_) {
        GlStateManager.depthMask(false);
        GlStateManager.func_179090_x();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float var10 = p_85094_1_.width / 2.0F;
        AxisAlignedBB var11 = p_85094_1_.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - p_85094_1_.posX + p_85094_2_, var11.minY - p_85094_1_.posY + p_85094_4_, var11.minZ - p_85094_1_.posZ + p_85094_6_, var11.maxX - p_85094_1_.posX + p_85094_2_, var11.maxY - p_85094_1_.posY + p_85094_4_, var11.maxZ - p_85094_1_.posZ + p_85094_6_);
        RenderGlobal.drawOutlinedBoundingBox(var12, 16777215);

        if (p_85094_1_ instanceof EntityLivingBase) {
            float var16 = 0.01F;
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(p_85094_2_ - (double) var10, p_85094_4_ + (double) p_85094_1_.getEyeHeight() - 0.009999999776482582D, p_85094_6_ - (double) var10, p_85094_2_ + (double) var10, p_85094_4_ + (double) p_85094_1_.getEyeHeight() + 0.009999999776482582D, p_85094_6_ + (double) var10), 16711680);
        }

        Tessellator var161 = Tessellator.getInstance();
        WorldRenderer var14 = var161.getWorldRenderer();
        Vec3 var15 = p_85094_1_.getLook(p_85094_9_);
        var14.startDrawing(3);
        var14.func_178991_c(255);
        var14.addVertex(p_85094_2_, p_85094_4_ + (double) p_85094_1_.getEyeHeight(), p_85094_6_);
        var14.addVertex(p_85094_2_ + var15.xCoord * 2.0D, p_85094_4_ + (double) p_85094_1_.getEyeHeight() + var15.yCoord * 2.0D, p_85094_6_ + var15.zCoord * 2.0D);
        var161.draw();
        GlStateManager.func_179098_w();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(World worldIn) {
        this.worldObj = worldIn;
    }

    public double getDistanceToCamera(double p_78714_1_, double p_78714_3_, double p_78714_5_) {
        double var7 = p_78714_1_ - this.viewerPosX;
        double var9 = p_78714_3_ - this.viewerPosY;
        double var11 = p_78714_5_ - this.viewerPosZ;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer() {
        return this.textRenderer;
    }

    public void func_178632_c(boolean p_178632_1_) {
        this.field_178639_r = p_178632_1_;
    }

    public Map getEntityRenderMap() {
        return this.entityRenderMap;
    }

    public void setEntityRenderMap(Map entityRenderMap) {
        this.entityRenderMap = entityRenderMap;
    }

    public Map<String, RenderPlayer> getSkinMap() {
        return Collections.unmodifiableMap(this.field_178636_l);
    }
}
