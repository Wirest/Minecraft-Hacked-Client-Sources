// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import optifine.Reflector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import optifine.Config;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class EffectRenderer
{
    private static final ResourceLocation particleTextures;
    protected World worldObj;
    private List[][] fxLayers;
    private List particleEmitters;
    private TextureManager renderer;
    private Random rand;
    private Map particleTypes;
    private static final String __OBFID = "CL_00000915";
    
    static {
        particleTextures = new ResourceLocation("textures/particle/particles.png");
    }
    
    public EffectRenderer(final World worldIn, final TextureManager rendererIn) {
        this.fxLayers = new List[4][];
        this.particleEmitters = Lists.newArrayList();
        this.rand = new Random();
        this.particleTypes = Maps.newHashMap();
        this.worldObj = worldIn;
        this.renderer = rendererIn;
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i] = new List[2];
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j] = Lists.newArrayList();
            }
        }
        this.registerVanillaParticles();
    }
    
    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
    }
    
    public void registerParticle(final int id, final IParticleFactory particleFactory) {
        this.particleTypes.put(id, particleFactory);
    }
    
    public void emitParticleAtEntity(final Entity entityIn, final EnumParticleTypes particleTypes) {
        this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
    }
    
    public EntityFX spawnEffectParticle(final int particleId, final double p_178927_2_, final double p_178927_4_, final double p_178927_6_, final double p_178927_8_, final double p_178927_10_, final double p_178927_12_, final int... p_178927_14_) {
        final IParticleFactory iparticlefactory = this.particleTypes.get(particleId);
        if (iparticlefactory != null) {
            final EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);
            if (entityfx != null) {
                this.addEffect(entityfx);
                return entityfx;
            }
        }
        return null;
    }
    
    public void addEffect(final EntityFX effect) {
        if (effect != null && (!(effect instanceof EntityFirework.SparkFX) || Config.isFireworkParticles())) {
            final int i = effect.getFXLayer();
            final int j = (effect.getAlpha() == 1.0f) ? 1 : 0;
            if (this.fxLayers[i][j].size() >= 4000) {
                this.fxLayers[i][j].remove(0);
            }
            if (!(effect instanceof Barrier) || !this.reuseBarrierParticle(effect, this.fxLayers[i][j])) {
                this.fxLayers[i][j].add(effect);
            }
        }
    }
    
    public void updateEffects() {
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        final ArrayList arraylist = Lists.newArrayList();
        for (final Object entityparticleemitter0 : this.particleEmitters) {
            final EntityParticleEmitter entityparticleemitter2 = (EntityParticleEmitter)entityparticleemitter0;
            entityparticleemitter2.onUpdate();
            if (entityparticleemitter2.isDead) {
                arraylist.add(entityparticleemitter2);
            }
        }
        this.particleEmitters.removeAll(arraylist);
    }
    
    private void updateEffectLayer(final int p_178922_1_) {
        for (int i = 0; i < 2; ++i) {
            this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][i]);
        }
    }
    
    private void updateEffectAlphaLayer(final List p_178925_1_) {
        final ArrayList arraylist = Lists.newArrayList();
        for (int i = 0; i < p_178925_1_.size(); ++i) {
            final EntityFX entityfx = p_178925_1_.get(i);
            this.tickParticle(entityfx);
            if (entityfx.isDead) {
                arraylist.add(entityfx);
            }
        }
        p_178925_1_.removeAll(arraylist);
    }
    
    private void tickParticle(final EntityFX p_178923_1_) {
        try {
            p_178923_1_.onUpdate();
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", new Callable() {
                private static final String __OBFID = "CL_00000916";
                
                @Override
                public String call() throws Exception {
                    return p_178923_1_.toString();
                }
            });
            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable() {
                private static final String __OBFID = "CL_00000917";
                
                @Override
                public String call() throws Exception {
                    return (i == 0) ? "MISC_TEXTURE" : ((i == 1) ? "TERRAIN_TEXTURE" : ((i == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i)));
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    public void renderParticles(final Entity entityIn, final float partialTicks) {
        final float f = ActiveRenderInfo.getRotationX();
        final float f2 = ActiveRenderInfo.getRotationZ();
        final float f3 = ActiveRenderInfo.getRotationYZ();
        final float f4 = ActiveRenderInfo.getRotationXY();
        final float f5 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        for (int i = 0; i < 3; ++i) {
            final int j = i;
            for (int k = 0; k < 2; ++k) {
                if (!this.fxLayers[j][k].isEmpty()) {
                    switch (k) {
                        case 0: {
                            GlStateManager.depthMask(false);
                            break;
                        }
                        case 1: {
                            GlStateManager.depthMask(true);
                            break;
                        }
                    }
                    switch (j) {
                        default: {
                            this.renderer.bindTexture(EffectRenderer.particleTextures);
                            break;
                        }
                        case 1: {
                            this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                            break;
                        }
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    for (int l = 0; l < this.fxLayers[j][k].size(); ++l) {
                        final EntityFX entityfx = this.fxLayers[j][k].get(l);
                        try {
                            entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f5, f2, f3, f4);
                        }
                        catch (Throwable throwable) {
                            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", new Callable() {
                                private static final String __OBFID = "CL_00000918";
                                
                                @Override
                                public String call() throws Exception {
                                    return entityfx.toString();
                                }
                            });
                            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable() {
                                private static final String __OBFID = "CL_00000919";
                                
                                @Override
                                public String call() throws Exception {
                                    return (j == 0) ? "MISC_TEXTURE" : ((j == 1) ? "TERRAIN_TEXTURE" : ((j == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + j)));
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public void renderLitParticles(final Entity entityIn, final float p_78872_2_) {
        final float f = 0.017453292f;
        final float f2 = MathHelper.cos(entityIn.rotationYaw * 0.017453292f);
        final float f3 = MathHelper.sin(entityIn.rotationYaw * 0.017453292f);
        final float f4 = -f3 * MathHelper.sin(entityIn.rotationPitch * 0.017453292f);
        final float f5 = f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292f);
        final float f6 = MathHelper.cos(entityIn.rotationPitch * 0.017453292f);
        for (int i = 0; i < 2; ++i) {
            final List list = this.fxLayers[3][i];
            if (!list.isEmpty()) {
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                for (int j = 0; j < list.size(); ++j) {
                    final EntityFX entityfx = list.get(j);
                    entityfx.renderParticle(worldrenderer, entityIn, p_78872_2_, f2, f6, f3, f4, f5);
                }
            }
        }
    }
    
    public void clearEffects(final World worldIn) {
        this.worldObj = worldIn;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j].clear();
            }
        }
        this.particleEmitters.clear();
    }
    
    public void addBlockDestroyEffects(final BlockPos pos, IBlockState state) {
        boolean flag;
        if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
            final Block block = state.getBlock();
            Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, this.worldObj, pos);
            flag = (!Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, this.worldObj, pos) && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, this.worldObj, pos, this));
        }
        else {
            flag = (state.getBlock().getMaterial() != Material.air);
        }
        if (flag) {
            state = state.getBlock().getActualState(state, this.worldObj, pos);
            final byte b0 = 4;
            for (int i = 0; i < b0; ++i) {
                for (int j = 0; j < b0; ++j) {
                    for (int k = 0; k < b0; ++k) {
                        final double d0 = pos.getX() + (i + 0.5) / b0;
                        final double d2 = pos.getY() + (j + 0.5) / b0;
                        final double d3 = pos.getZ() + (k + 0.5) / b0;
                        this.addEffect(new EntityDiggingFX(this.worldObj, d0, d2, d3, d0 - pos.getX() - 0.5, d2 - pos.getY() - 0.5, d3 - pos.getZ() - 0.5, state).func_174846_a(pos));
                    }
                }
            }
        }
    }
    
    public void addBlockHitEffects(final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = this.worldObj.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block.getRenderType() != -1) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            final float f = 0.1f;
            double d0 = i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0f) + f + block.getBlockBoundsMinX();
            double d2 = j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0f) + f + block.getBlockBoundsMinY();
            double d3 = k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0f) + f + block.getBlockBoundsMinZ();
            if (side == EnumFacing.DOWN) {
                d2 = j + block.getBlockBoundsMinY() - f;
            }
            if (side == EnumFacing.UP) {
                d2 = j + block.getBlockBoundsMaxY() + f;
            }
            if (side == EnumFacing.NORTH) {
                d3 = k + block.getBlockBoundsMinZ() - f;
            }
            if (side == EnumFacing.SOUTH) {
                d3 = k + block.getBlockBoundsMaxZ() + f;
            }
            if (side == EnumFacing.WEST) {
                d0 = i + block.getBlockBoundsMinX() - f;
            }
            if (side == EnumFacing.EAST) {
                d0 = i + block.getBlockBoundsMaxX() + f;
            }
            this.addEffect(new EntityDiggingFX(this.worldObj, d0, d2, d3, 0.0, 0.0, 0.0, iblockstate).func_174846_a(pos).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
        }
    }
    
    public void moveToAlphaLayer(final EntityFX effect) {
        this.moveToLayer(effect, 1, 0);
    }
    
    public void moveToNoAlphaLayer(final EntityFX effect) {
        this.moveToLayer(effect, 0, 1);
    }
    
    private void moveToLayer(final EntityFX effect, final int p_178924_2_, final int p_178924_3_) {
        for (int i = 0; i < 4; ++i) {
            if (this.fxLayers[i][p_178924_2_].contains(effect)) {
                this.fxLayers[i][p_178924_2_].remove(effect);
                this.fxLayers[i][p_178924_3_].add(effect);
            }
        }
    }
    
    public String getStatistics() {
        int i = 0;
        for (int j = 0; j < 4; ++j) {
            for (int k = 0; k < 2; ++k) {
                i += this.fxLayers[j][k].size();
            }
        }
        return new StringBuilder().append(i).toString();
    }
    
    private boolean reuseBarrierParticle(final EntityFX p_reuseBarrierParticle_1_, final List<EntityFX> p_reuseBarrierParticle_2_) {
        for (final EntityFX entityfx : p_reuseBarrierParticle_2_) {
            if (entityfx instanceof Barrier && p_reuseBarrierParticle_1_.posX == entityfx.posX && p_reuseBarrierParticle_1_.posY == entityfx.posY && p_reuseBarrierParticle_1_.posZ == entityfx.posZ) {
                entityfx.particleAge = 0;
                return true;
            }
        }
        return false;
    }
    
    public void addBlockHitEffects(final BlockPos p_addBlockHitEffects_1_, final MovingObjectPosition p_addBlockHitEffects_2_) {
        final Block block = this.worldObj.getBlockState(p_addBlockHitEffects_1_).getBlock();
        final boolean flag = Reflector.callBoolean(block, Reflector.ForgeBlock_addHitEffects, this.worldObj, p_addBlockHitEffects_2_, this);
        if (block != null && !flag) {
            this.addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
        }
    }
}
