package net.minecraft.client.renderer;

import com.google.gson.JsonSyntaxException;

import info.sigmaclient.Client;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.gui.screen.impl.mainmenu.ClientMainMenu;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.other.Infinite;
import info.sigmaclient.module.impl.render.Brightness;
import info.sigmaclient.module.impl.render.NoHurtCam;
import info.sigmaclient.module.impl.render.Xray;
import info.sigmaclient.util.misc.ChatUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.optifine.Config;
import net.minecraft.optifine.CustomColorizer;
import net.minecraft.optifine.Lagometer;
import net.minecraft.optifine.RandomMobs;
import net.minecraft.optifine.Reflector;
import net.minecraft.optifine.RenderPlayerOF;
import net.minecraft.optifine.TextureUtils;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class EntityRenderer implements IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    public static boolean anaglyphEnable;

    /**
     * Anaglyph field (0=R, 1=GB)
     */
    public static int anaglyphField;

    /**
     * A reference to the Minecraft object.
     */
    private Minecraft mc;
    private final IResourceManager resourceManager;
    private Random random = new Random();
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer theMapItemRenderer;

    /**
     * Entity renderer update count
     */
    private int rendererUpdateCount;

    /**
     * Pointed entity
     */
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private float thirdPersonDistance = 4.0F;

    /**
     * Third person distance temp
     */
    private float thirdPersonDistanceTemp = 4.0F;

    /**
     * Smooth cam yaw
     */
    private float smoothCamYaw;

    /**
     * Smooth cam pitch
     */
    private float smoothCamPitch;

    /**
     * Smooth cam filter X
     */
    private float smoothCamFilterX;

    /**
     * Smooth cam filter Y
     */
    private float smoothCamFilterY;

    /**
     * Smooth cam partial ticks
     */
    private float smoothCamPartialTicks;

    /**
     * FOV modifier hand
     */
    private float fovModifierHand;

    /**
     * FOV modifier hand prev
     */
    private float fovModifierHandPrev;
    private float bossColorModifier;
    private float bossColorModifierPrev;

    /**
     * Cloud fog mode
     */
    private boolean cloudFog;
    private boolean field_175074_C = true;
    private boolean field_175073_D = true;

    /**
     * Previous frame time in milliseconds
     */
    private long prevFrameTime = Minecraft.getSystemTime();

    /**
     * End time of last render (ns)
     */
    private long renderEndNanoTime;

    /**
     * The texture id of the blocklight/skylight texture used for lighting
     * effects
     */
    private final DynamicTexture lightmapTexture;

    /**
     * Colors computed in updateLightmap() and loaded into the lightmap
     * emptyTexture
     */
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;

    /**
     * Is set, updateCameraAndRender() calls updateLightmap(); set by
     * updateTorchFlicker()
     */
    private boolean lightmapUpdateNeeded;

    /**
     * Torch flicker X
     */
    private float torchFlickerX;
    private float field_175075_L;

    /**
     * Rain sound counter
     */
    private int rainSoundCounter;
    private float[] field_175076_N = new float[1024];
    private float[] field_175077_O = new float[1024];

    /**
     * Fog color buffer
     */
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public float field_175080_Q;
    public float field_175082_R;
    public float field_175081_S;

    /**
     * Fog color 2
     */
    private float fogColor2;

    /**
     * Fog color 1
     */
    private float fogColor1;
    private int field_175079_V = 0;
    private boolean field_175078_W = false;
    private double cameraZoom = 1.0D;
    private double cameraYaw;
    private double cameraPitch;
    public ShaderGroup theShaderGroup;
    public static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"),
            new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
    public static final int shaderCount = shaderResourceLocations.length;
    private int shaderIndex;
    private boolean field_175083_ad;
    private int field_175084_ae;
    private static final String __OBFID = "CL_00000947";
    private boolean initialized = false;
    private World updatedWorld = null;
    private boolean showDebugInfo = false;
    public boolean fogStandard = false;
    private float clipDistance = 128.0F;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0F;
    private float avgServerTickDiff = 0.0F;

    public EntityRenderer(Minecraft mcIn, IResourceManager p_i45076_2_) {
        this.shaderIndex = shaderCount;
        this.field_175083_ad = false;
        this.field_175084_ae = 0;
        this.mc = mcIn;
        this.resourceManager = p_i45076_2_;
        this.itemRenderer = mcIn.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;

        for (int var3 = 0; var3 < 32; ++var3) {
            for (int var4 = 0; var4 < 32; ++var4) {
                float var5 = (float) (var4 - 16);
                float var6 = (float) (var3 - 16);
                float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
                this.field_175076_N[var3 << 5 | var4] = -var6 / var7;
                this.field_175077_O[var3 << 5 | var4] = var5 / var7;
            }
        }
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    public void func_175071_c() {
        this.field_175083_ad = !this.field_175083_ad;
    }

    public void func_175066_a(Entity p_175066_1_) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }

            this.theShaderGroup = null;

            if (p_175066_1_ instanceof EntityCreeper) {
                this.func_175069_a(new ResourceLocation("shaders/post/creeper.json"));
            } else if (p_175066_1_ instanceof EntitySpider) {
                this.func_175069_a(new ResourceLocation("shaders/post/spider.json"));
            } else if (p_175066_1_ instanceof EntityEnderman) {
                this.func_175069_a(new ResourceLocation("shaders/post/invert.json"));
            }
        }
    }

    public void activateNextShader() {
        if (OpenGlHelper.shadersSupported && this.mc.func_175606_aa() instanceof EntityPlayer) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }

            this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);

            if (this.shaderIndex != shaderCount) {
                this.func_175069_a(shaderResourceLocations[this.shaderIndex]);
            } else {
                this.theShaderGroup = null;
            }
        }
    }

    public void func_175069_a(ResourceLocation p_175069_1_) {
        try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), p_175069_1_);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.field_175083_ad = true;
        } catch (IOException var3) {
            logger.warn("Failed to load shader: " + p_175069_1_, var3);
            this.shaderIndex = shaderCount;
            this.field_175083_ad = false;
        } catch (JsonSyntaxException var4) {
            logger.warn("Failed to load shader: " + p_175069_1_, var4);
            this.shaderIndex = shaderCount;
            this.field_175083_ad = false;
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }

        this.theShaderGroup = null;

        if (this.shaderIndex != shaderCount) {
            this.func_175069_a(shaderResourceLocations[this.shaderIndex]);
        } else {
            this.func_175066_a(this.mc.func_175606_aa());
        }
    }

    /**
     * Updates the entity renderer
     */
    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }

        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        float var1;
        float var2;

        if (this.mc.gameSettings.smoothCamera) {
            var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            var2 = var1 * var1 * var1 * 8.0F;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
            this.smoothCamPartialTicks = 0.0F;
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
        } else {
            this.smoothCamFilterX = 0.0F;
            this.smoothCamFilterY = 0.0F;
            this.mouseFilterXAxis.func_180179_a();
            this.mouseFilterYAxis.func_180179_a();
        }

        if (this.mc.func_175606_aa() == null) {
            this.mc.func_175607_a(this.mc.thePlayer);
        }

        var1 = this.mc.theWorld.getLightBrightness(new BlockPos(this.mc.func_175606_aa()));
        var2 = (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
        float var3 = var1 * (1.0F - var2) + var2;
        this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;

        if (BossStatus.hasColorModifier) {
            this.bossColorModifier += 0.05F;

            if (this.bossColorModifier > 1.0F) {
                this.bossColorModifier = 1.0F;
            }

            BossStatus.hasColorModifier = false;
        } else if (this.bossColorModifier > 0.0F) {
            this.bossColorModifier -= 0.0125F;
        }
    }

    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }

    public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
            }

            this.mc.renderGlobal.checkOcclusionQueryResult(p_147704_1_, p_147704_2_);
        }
    }

    /**
     * Finds what block or object the mouse is over at the specified partial
     * tick time. Args: partialTickTime
     */
    public void getMouseOver(float partialTick) {
        Entity entity = this.mc.func_175606_aa();

        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double reach = this.mc.playerController.getBlockReachDistance();
            ModuleManager m = Client.getModuleManager();
            //Reach reachModule = (Reach) m.get(Reach.class);
            //if(reachModule.isEnabled()) {
                //reach = reachModule.getReachDistance();
            //}
            if(Client.getModuleManager().isEnabled(Infinite.class)){
            	reach = 60;
            }
            this.mc.objectMouseOver = entity.func_174822_a(reach, partialTick);
            double distanceToVec = reach;
            Vec3 vec1 = entity.func_174824_e(partialTick);

           // if(!reachModule.isEnabled()) {
                if (this.mc.playerController.extendedReach()) {
                    reach = 6.0D;
                    distanceToVec = 6.0D;
                } else {
                    if (reach > 3.0D) {
                        distanceToVec = 3.0D;
                    }

                    reach = distanceToVec;
                }
           // }

            if (this.mc.objectMouseOver != null) {
                distanceToVec = this.mc.objectMouseOver.hitVec.distanceTo(vec1);
            }

            Vec3 getLook = entity.getLook(partialTick);
            Vec3 lookTimesReach = vec1.addVector(getLook.xCoord * reach, getLook.yCoord * reach, getLook.zCoord * reach);
            this.pointedEntity = null;
            Vec3 var10 = null;
            float var11 = 1.0F;
            List var12 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(getLook.xCoord * reach, getLook.yCoord * reach, getLook.zCoord * reach).expand((double) var11, (double) var11, (double) var11));
            double var13 = distanceToVec;

            for (int index = 0; index < var12.size(); ++index) {
                Entity ent = (Entity) var12.get(index);

                if (ent.canBeCollidedWith()) {
                    float collisSize = ent.getCollisionBorderSize();
                    AxisAlignedBB bounds = ent.getEntityBoundingBox().expand((double) collisSize, (double) collisSize, (double) collisSize);

                    MovingObjectPosition objectCollis = bounds.calculateIntercept(vec1, lookTimesReach);

                    if (bounds.isVecInside(vec1)) {
                        if (0.0D < var13 || var13 == 0.0D) {
                            this.pointedEntity = ent;
                            var10 = objectCollis == null ? vec1 : objectCollis.hitVec;
                            var13 = 0.0D;
                        }
                    } else if (objectCollis != null) {
                        double var20 = vec1.distanceTo(objectCollis.hitVec);

                        if (var20 < var13 || var13 == 0.0D) {
                            boolean canRiderInteract = false;

                            if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                                canRiderInteract = Reflector.callBoolean(Integer.valueOf(index), Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                            }

                            if (ent == entity.ridingEntity && !canRiderInteract) {
                                if (var13 == 0.0D) {
                                    this.pointedEntity = ent;
                                    var10 = objectCollis.hitVec;
                                }
                            } else {
                                this.pointedEntity = ent;
                                var10 = objectCollis.hitVec;
                                var13 = var20;
                            }
                        }
                    }
                }
            }

            if (this.pointedEntity != null && (var13 < distanceToVec || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var10);

                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }

    /**
     * Update FOV modifier hand
     */
    private void updateFovModifierHand() {
        float var1 = 1.0F;

        if (this.mc.func_175606_aa() instanceof AbstractClientPlayer) {
            AbstractClientPlayer var2 = (AbstractClientPlayer) this.mc.func_175606_aa();
            var1 = var2.func_175156_o();
        } else {
            var1 = mc.thePlayer.func_175156_o();
        }

        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (var1 - this.fovModifierHand) * 0.5F;

        if (this.fovModifierHand > 1.5F) {
            this.fovModifierHand = 1.5F;
        }

        if (this.fovModifierHand < 0.1F) {
            this.fovModifierHand = 0.1F;
        }
    }

    /**
     * Changes the field of view of the player depending on if they are
     * underwater or not
     */
    private float getFOVModifier(float partialTicks, boolean p_78481_2_) {
        if (this.field_175078_W) {
            return 90.0F;
        } else {
            Entity var3 = this.mc.func_175606_aa();
            float var4 = 70.0F;

            if (p_78481_2_) {
                var4 = this.mc.gameSettings.fovSetting;
                var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }

            boolean zoomActive = false;

            if (this.mc.currentScreen == null) {
                GameSettings var10000 = this.mc.gameSettings;
                zoomActive = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
            }

            if (zoomActive) {
                if (!Config.zoomMode) {
                    Config.zoomMode = true;
                    this.mc.gameSettings.smoothCamera = true;
                }

                if (Config.zoomMode) {
                    var4 /= 4.0F;
                }
            } else if (Config.zoomMode) {
                Config.zoomMode = false;
                this.mc.gameSettings.smoothCamera = false;
                this.mouseFilterXAxis = new MouseFilter();
                this.mouseFilterYAxis = new MouseFilter();
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }

            if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).getHealth() <= 0.0F) {
                float var6 = (float) ((EntityLivingBase) var3).deathTime + partialTicks;
                var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
            }

            Block var61 = ActiveRenderInfo.func_180786_a(this.mc.theWorld, var3, partialTicks);

            if (var61.getMaterial() == Material.water) {
                var4 = var4 * 60.0F / 70.0F;
            }

            return var4;
        }
    }

    private void hurtCameraEffect(float p_78482_1_) {
        if (this.mc.func_175606_aa() instanceof EntityLivingBase) {     	
            if (Client.getModuleManager().isEnabled(NoHurtCam.class) || (Fly.fastFlew < 35 && Client.getModuleManager().isEnabled(Fly.class)))
                return;

            EntityLivingBase var2 = (EntityLivingBase) this.mc.func_175606_aa();
            float var3 = (float) var2.hurtTime - p_78482_1_;
            float var4;

            if (var2.getHealth() <= 0.0F) {
                var4 = (float) var2.deathTime + p_78482_1_;
                GlStateManager.rotate(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (var3 < 0.0F) {
                return;
            }

            var3 /= (float) var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float) Math.PI);
            var4 = var2.attackedAtYaw;
            GlStateManager.rotate(-var4, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var4, 0.0F, 1.0F, 0.0F);
        }
    }

    /**
     * Setups all the GL settings for view bobbing. Args: partialTickTime
     */
    private void setupViewBobbing(float p_78475_1_) {
        if (this.mc.func_175606_aa() instanceof EntityPlayer) {
            EntityPlayer var2 = (EntityPlayer) this.mc.func_175606_aa();
            float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
            float var4 = -(var2.distanceWalkedModified + var3 * p_78475_1_);
            float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * p_78475_1_;
            float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * p_78475_1_;
            GlStateManager.translate(MathHelper.sin(var4 * (float) Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float) Math.PI) * var5), 0.0F);
            GlStateManager.rotate(MathHelper.sin(var4 * (float) Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(var4 * (float) Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var6, 1.0F, 0.0F, 0.0F);
        }
    }

    /**
     * sets up player's eye (or camera in third person mode)
     */
    public void orientCamera(float p_78467_1_) {
        Entity var2 = this.mc.func_175606_aa();
        float var3 = var2.getEyeHeight();
        double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) p_78467_1_;
        double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) p_78467_1_ + (double) var3;
        double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) p_78467_1_;

        if (var2 instanceof EntityLivingBase && ((EntityLivingBase) var2).isPlayerSleeping()) {
            var3 = (float) ((double) var3 + 1.0D);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);

            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos var27 = new BlockPos(var2);
                IBlockState var11 = this.mc.theWorld.getBlockState(var27);
                Block var29 = var11.getBlock();

                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[]{this.mc, var2});
                } else if (var29 == Blocks.bed) {
                    int var30 = ((EnumFacing) var11.getValue(BlockBed.AGE)).getHorizontalIndex();
                    GlStateManager.rotate((float) (var30 * 90), 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double var28 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * p_78467_1_);

            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0F, 0.0F, (float) (-var28));
            } else {
                float var12 = var2.rotationYaw;
                float var13 = var2.rotationPitch;

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    var13 += 180.0F;
                }

                double var14 = (double) (-MathHelper.sin(var12 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI)) * var28;
                double var16 = (double) (MathHelper.cos(var12 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI)) * var28;
                double var18 = (double) (-MathHelper.sin(var13 / 180.0F * (float) Math.PI)) * var28;

                for (int var20 = 0; var20 < 8; ++var20) {
                    float var21 = (float) ((var20 & 1) * 2 - 1);
                    float var22 = (float) ((var20 >> 1 & 1) * 2 - 1);
                    float var23 = (float) ((var20 >> 2 & 1) * 2 - 1);
                    var21 *= 0.1F;
                    var22 *= 0.1F;
                    var23 *= 0.1F;
                    MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(new Vec3(var4 + (double) var21, var6 + (double) var22, var8 + (double) var23), new Vec3(var4 - var14 + (double) var21 + (double) var23, var6 - var18 + (double) var22, var8 - var16 + (double) var23));

                    if (var24 != null) {
                        double var25 = var24.hitVec.distanceTo(new Vec3(var4, var6, var8));

                        if (var25 < var28) {
                        	//TODO NOCLIP
                         //   var28 = var25;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.rotate(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(var2.rotationYaw - var12, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, (float) (-var28));
                GlStateManager.rotate(var12 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GlStateManager.translate(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);

            if (var2 instanceof EntityAnimal) {
                EntityAnimal var281 = (EntityAnimal) var2;
                GlStateManager.rotate(var281.prevRotationYawHead + (var281.rotationYawHead - var281.prevRotationYawHead) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.translate(0.0F, -var3, 0.0F);
        var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) p_78467_1_;
        var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) p_78467_1_ + (double) var3;
        var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) p_78467_1_;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, p_78467_1_);
    }

    /**
     * sets up projection, view effects, camera position/rotation
     */
    public void setupCameraTransform(float partialTicks, int pass) {
        this.farPlaneDistance = (float) (this.mc.gameSettings.renderDistanceChunks * 16);

        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95F;
        }

        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83F;
        }

        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float var3 = 0.07F;

        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float) (-(pass * 2 - 1)) * var3, 0.0F, 0.0F);
        }

        this.clipDistance = this.farPlaneDistance * MathHelper.field_180189_a;

        if (this.clipDistance < 173.0F) {
            this.clipDistance = 173.0F;
        }

        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.clipDistance = 256.0F;
        }

        if (this.cameraZoom != 1.0D) {
            GlStateManager.translate((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
            GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.clipDistance);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float) (pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        float var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;

        if (var4 > 0.0F) {
            byte var5 = 20;

            if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                var5 = 7;
            }

            float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
            var6 *= var6;
            GlStateManager.rotate(((float) this.rendererUpdateCount + partialTicks) * (float) var5, 0.0F, 1.0F, 1.0F);
            GlStateManager.scale(1.0F / var6, 1.0F, 1.0F);
            GlStateManager.rotate(-((float) this.rendererUpdateCount + partialTicks) * (float) var5, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(partialTicks);

        if (this.field_175078_W) {
            switch (this.field_175079_V) {
                case 0:
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 1:
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 2:
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 3:
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    break;

                case 4:
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    /**
     * Render player hand
     */
    private void renderHand(float p_78476_1_, int p_78476_2_) {
        if (!this.field_175078_W) {
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float var3 = 0.07F;

            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float) (-(p_78476_2_ * 2 - 1)) * var3, 0.0F, 0.0F);
            }

            Project.gluPerspective(this.getFOVModifier(p_78476_1_, false), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();

            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float) (p_78476_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }

            GlStateManager.pushMatrix();
            this.hurtCameraEffect(p_78476_1_);

            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(p_78476_1_);
            }

            boolean var4 = this.mc.func_175606_aa() instanceof EntityLivingBase && ((EntityLivingBase) this.mc.func_175606_aa()).isPlayerSleeping();

            if (this.mc.gameSettings.thirdPersonView == 0 && !var4 && !this.mc.gameSettings.hideGUI && !this.mc.playerController.enableEverythingIsScrewedUpMode()) {
                this.func_180436_i();
                this.itemRenderer.renderItemInFirstPerson(p_78476_1_);
                this.func_175072_h();
            }

            GlStateManager.popMatrix();

            if (this.mc.gameSettings.thirdPersonView == 0 && !var4) {
                this.itemRenderer.renderOverlays(p_78476_1_);
                this.hurtCameraEffect(p_78476_1_);
            }

            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(p_78476_1_);
            }
        }
    }

    public void func_175072_h() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void func_180436_i() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float var1 = 0.00390625F;
        GlStateManager.scale(var1, var1, var1);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Recompute a random value that is applied to block color in
     * updateLightmap()
     */
    private void updateTorchFlicker() {
        this.field_175075_L = (float) ((double) this.field_175075_L + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.field_175075_L = (float) ((double) this.field_175075_L * 0.9D);
        this.torchFlickerX += (this.field_175075_L - this.torchFlickerX) * 1.0F;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.mcProfiler.startSection("lightTex");
            WorldClient var2 = this.mc.theWorld;

            if (var2 != null) {
                if (CustomColorizer.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision))) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();
                    return;
                }

                for (int var3 = 0; var3 < 256; ++var3) {
                    float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
                    float var5 = var2.provider.getLightBrightnessTable()[var3 / 16] * var4;
                    float var6 = var2.provider.getLightBrightnessTable()[var3 % 16] * (this.torchFlickerX * 0.1F + 1.5F);

                    if (var2.func_175658_ac() > 0) {
                        var5 = var2.provider.getLightBrightnessTable()[var3 / 16];
                    }

                    float var7 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
                    float var8 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
                    float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
                    float var13 = var7 + var6;
                    float var14 = var8 + var11;
                    float var15 = var5 + var12;
                    var13 = var13 * 0.96F + 0.03F;
                    var14 = var14 * 0.96F + 0.03F;
                    var15 = var15 * 0.96F + 0.03F;
                    float var16;

                    if (this.bossColorModifier > 0.0F) {
                        var16 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                        var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
                        var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
                        var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
                    }

                    if (var2.provider.getDimensionId() == 1) {
                        var13 = 0.22F + var6 * 0.75F;
                        var14 = 0.28F + var11 * 0.75F;
                        var15 = 0.25F + var12 * 0.75F;
                    }

                    float var17;

                    if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                        var16 = this.func_180438_a(this.mc.thePlayer, partialTicks);
                        var17 = 1.0F / var13;

                        if (var17 > 1.0F / var14) {
                            var17 = 1.0F / var14;
                        }

                        if (var17 > 1.0F / var15) {
                            var17 = 1.0F / var15;
                        }

                        var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
                        var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
                        var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
                    }

                    if (var13 > 1.0F) {
                        var13 = 1.0F;
                    }

                    if (var14 > 1.0F) {
                        var14 = 1.0F;
                    }

                    if (var15 > 1.0F) {
                        var15 = 1.0F;
                    }

                    var16 = (Client.getModuleManager().isEnabled(Xray.class) || Client.getModuleManager().isEnabled(Brightness.class)) ? 10000f : this.mc.gameSettings.gammaSetting;
                    var17 = 1.0F - var13;
                    float var18 = 1.0F - var14;
                    float var19 = 1.0F - var15;
                    var17 = 1.0F - var17 * var17 * var17 * var17;
                    var18 = 1.0F - var18 * var18 * var18 * var18;
                    var19 = 1.0F - var19 * var19 * var19 * var19;
                    var13 = var13 * (1.0F - var16) + var17 * var16;
                    var14 = var14 * (1.0F - var16) + var18 * var16;
                    var15 = var15 * (1.0F - var16) + var19 * var16;
                    var13 = var13 * 0.96F + 0.03F;
                    var14 = var14 * 0.96F + 0.03F;
                    var15 = var15 * 0.96F + 0.03F;

                    if (var13 > 1.0F) {
                        var13 = 1.0F;
                    }

                    if (var14 > 1.0F) {
                        var14 = 1.0F;
                    }

                    if (var15 > 1.0F) {
                        var15 = 1.0F;
                    }

                    if (var13 < 0.0F) {
                        var13 = 0.0F;
                    }

                    if (var14 < 0.0F) {
                        var14 = 0.0F;
                    }

                    if (var15 < 0.0F) {
                        var15 = 0.0F;
                    }

                    short var20 = 255;
                    int var21 = (int) (var13 * 255.0F);
                    int var22 = (int) (var14 * 255.0F);
                    int var23 = (int) (var15 * 255.0F);
                    this.lightmapColors[var3] = var20 << 24 | var21 << 16 | var22 << 8 | var23;
                }

                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }

    private float func_180438_a(EntityLivingBase p_180438_1_, float partialTicks) {
        int var3 = p_180438_1_.getActivePotionEffect(Potion.nightVision).getDuration();
        return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin(((float) var3 - partialTicks) * (float) Math.PI * 0.2F) * 0.3F;
    }

    /**
     * Will update any inputs that effect the camera angle (mouse) and then
     * render the world and GUI
     */
    public void updateCameraAndRender(float partialTicks) {
        this.frameInit();
        boolean var2 = Display.isActive();

        if (!var2 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (var2 && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }

        if (this.mc.inGameHasFocus && var2) {
            this.mc.mouseHelper.mouseXYChange();
            float var13 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float var14 = var13 * var13 * var13 * 8.0F;
            float var15 = (float) this.mc.mouseHelper.deltaX * var14;
            float var16 = (float) this.mc.mouseHelper.deltaY * var14;
            byte var17 = 1;

            if (this.mc.gameSettings.invertMouse) {
                var17 = -1;
            }

            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += var15;
                this.smoothCamPitch += var16;
                float var18 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                var15 = this.smoothCamFilterX * var18;
                var16 = this.smoothCamFilterY * var18;
                this.mc.thePlayer.setAngles(var15, var16 * (float) var17);
            } else {
                this.smoothCamYaw = 0.0F;
                this.smoothCamPitch = 0.0F;
                this.mc.thePlayer.setAngles(var15, var16 * (float) var17);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution var131 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var141 = var131.getScaledWidth();
            int var151 = var131.getScaledHeight();
            final int var161 = Mouse.getX() * var141 / this.mc.displayWidth;
            final int var171 = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
            int var181 = this.mc.gameSettings.limitFramerate;

            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");
                int var12 = Math.max(Minecraft.func_175610_ah(), 30);
                this.renderWorld(partialTicks, this.renderEndNanoTime + (long) (1000000000 / var12));
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.func_174975_c();

                    if (this.theShaderGroup != null && this.field_175083_ad) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(partialTicks);
                        GlStateManager.popMatrix();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1F);
                    this.mc.ingameGUI.func_175180_a(partialTicks);
                }

                this.mc.mcProfiler.endSection();
            } else {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);

                try {
                    boolean var122 = false;

                    if (Reflector.EventBus_post.exists()) {
                        var122 = Reflector.postForgeBusEvent(Reflector.DrawScreenEvent_Pre_Constructor, new Object[]{this.mc.currentScreen, Integer.valueOf(var161), Integer.valueOf(var171), Float.valueOf(partialTicks)});
                    }

                    if (!var122) {
                        this.mc.currentScreen.drawScreen(var161, var171, partialTicks);
                    }

                    Reflector.postForgeBusEvent(Reflector.DrawScreenEvent_Post_Constructor, new Object[]{this.mc.currentScreen, Integer.valueOf(var161), Integer.valueOf(var171), Float.valueOf(partialTicks)});
                } catch (Throwable var121) {
                    CrashReport var10 = CrashReport.makeCrashReport(var121, "Rendering screen");
                    CrashReportCategory var11 = var10.makeCategory("Screen render details");
                    var11.addCrashSectionCallable("Screen name", new Callable() {
                        private static final String __OBFID = "CL_00000948";

                        public String call() {
                            return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    var11.addCrashSectionCallable("Mouse location", new Callable() {
                        private static final String __OBFID = "CL_00000950";

                        public String call() {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[]{Integer.valueOf(var161), Integer.valueOf(var171), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
                        }
                    });
                    var11.addCrashSectionCallable("Screen size", new Callable() {
                        private static final String __OBFID = "CL_00000951";

                        public String call() {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[]{Integer.valueOf(var131.getScaledWidth()), Integer.valueOf(var131.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(var131.getScaleFactor())});
                        }
                    });
                    throw new ReportedException(var10);
                }
            }
        }

        this.waitForServerThread();
        Lagometer.updateLagometer();

        if (this.mc.gameSettings.ofProfiler) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }

    public void func_152430_c(float p_152430_1_) {
        this.setupOverlayRendering();
        this.mc.ingameGUI.func_180478_c(new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight));
    }

    private boolean func_175070_n() {
        if (!this.field_175073_D) {
            return false;
        } else {
            Entity var1 = this.mc.func_175606_aa();
            boolean var2 = var1 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;

            if (var2 && !((EntityPlayer) var1).capabilities.allowEdit) {
                ItemStack var3 = ((EntityPlayer) var1).getCurrentEquippedItem();

                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos var4 = this.mc.objectMouseOver.getBlockPos();
                    Block var5 = this.mc.theWorld.getBlockState(var4).getBlock();

                    if (this.mc.playerController.func_178889_l() == WorldSettings.GameType.SPECTATOR) {
                        var2 = var5.hasTileEntity() && this.mc.theWorld.getTileEntity(var4) instanceof IInventory;
                    } else {
                        var2 = var3 != null && (var3.canDestroy(var5) || var3.canPlaceOn(var5));
                    }
                }
            }

            return var2;
        }
    }

    private void func_175067_i(float p_175067_1_) {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.func_175140_cp() && !this.mc.gameSettings.field_178879_v) {
            Entity var2 = this.mc.func_175606_aa();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTextures();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.orientCamera(p_175067_1_);
            GlStateManager.translate(0.0F, var2.getEyeHeight(), 0.0F);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), -65536);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), -16776961);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), -16711936);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.enableTextures();
            GlStateManager.disableBlend();
        }
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        this.updateLightmap(partialTicks);

        if (this.mc.func_175606_aa() == null) {
            this.mc.func_175607_a(this.mc.thePlayer);
        }

        this.getMouseOver(partialTicks);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        this.mc.mcProfiler.startSection("center");

        if (this.mc.gameSettings.anaglyph) {
            anaglyphField = 0;
            GlStateManager.colorMask(false, true, true, false);
            this.func_175068_a(0, partialTicks, finishTimeNano);
            anaglyphField = 1;
            GlStateManager.colorMask(true, false, false, false);
            this.func_175068_a(1, partialTicks, finishTimeNano);
            GlStateManager.colorMask(true, true, true, false);
        } else {
            this.func_175068_a(2, partialTicks, finishTimeNano);
        }

        this.mc.mcProfiler.endSection();
    }

    private void func_175068_a(int pass, float partialTicks, long finishTimeNano) {
        RenderGlobal var5 = this.mc.renderGlobal;
        EffectRenderer var6 = this.mc.effectRenderer;
        boolean var7 = this.func_175070_n();
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("clear");
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        this.updateFogColor(partialTicks);
        GlStateManager.clear(16640);
        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        Frustrum frust = new Frustrum();
        Entity player = this.mc.func_175606_aa();
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        frust.setPosition(px, py, pz);

        if (!Config.isSkyEnabled() && !Config.isSunMoonEnabled() && !Config.isStarsEnabled()) {
            GlStateManager.disableBlend();
        } else {
            this.setupFog(-1, partialTicks);
            this.mc.mcProfiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.clipDistance);
            GlStateManager.matrixMode(5888);
            var5.func_174976_a(partialTicks, pass);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.field_180189_a);
            GlStateManager.matrixMode(5888);
        }

        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);

        if (player.posY + (double) player.getEyeHeight() < 128.0D + (double) (this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
            this.func_180437_a(var5, partialTicks, pass);
        }

        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        var5.func_174970_a(player, (double) partialTicks, frust, this.field_175084_ae++, this.mc.thePlayer.func_175149_v());

        if (pass == 0 || pass == 2) {
            this.mc.mcProfiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.func_174967_a(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }

        this.mc.mcProfiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        var5.func_174977_a(EnumWorldBlockLayer.SOLID, (double) partialTicks, pass, player);
        GlStateManager.enableAlpha();
        var5.func_174977_a(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, pass, player);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
        var5.func_174977_a(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, pass, player);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174935_a();
        Lagometer.timerTerrain.end();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        EntityPlayer var16;

        if (!this.field_175078_W) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");

            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(0)});
            }

            var5.func_180446_a(player, frust, partialTicks);

            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
            }

            RenderHelper.disableStandardItemLighting();
            this.func_175072_h();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();

            if (this.mc.objectMouseOver != null && player.isInsideOfMaterial(Material.water) && var7) {
                var16 = (EntityPlayer) player;
                GlStateManager.disableAlpha();
                this.mc.mcProfiler.endStartSection("outline");
                var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
                GlStateManager.enableAlpha();
            }
        }

        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();

        if (var7 && this.mc.objectMouseOver != null && !player.isInsideOfMaterial(Material.water)) {
            var16 = (EntityPlayer) player;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
            GlStateManager.enableAlpha();
        }

        this.mc.mcProfiler.endStartSection("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        var5.func_174981_a(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), player, partialTicks);
        GlStateManager.disableBlend();

        if (!this.field_175078_W) {
            this.func_180436_i();
            this.mc.mcProfiler.endStartSection("litParticles");
            var6.renderLitParticles(player, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.mcProfiler.endStartSection("particles");
            var6.renderParticles(player, partialTicks);
            this.func_175072_h();
        }

        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("weather");
        this.renderRainSnow(partialTicks);
        GlStateManager.depthMask(true);
        var5.func_180449_a(player, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1F);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GlStateManager.shadeModel(7425);

        if (Config.isTranslucentBlocksFancy()) {
            this.mc.mcProfiler.endStartSection("translucent");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            var5.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, pass, player);
            GlStateManager.disableBlend();
        } else {
            this.mc.mcProfiler.endStartSection("translucent");
            var5.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, pass, player);
        }

        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();

        if (player.posY + (double) player.getEyeHeight() >= 128.0D + (double) (this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.func_180437_a(var5, partialTicks, pass);
        }
        fireRenderer(partialTicks, pass);

        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            this.mc.mcProfiler.endStartSection("FRenderLast");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[]{var5, Float.valueOf(partialTicks)});
        }

        this.mc.mcProfiler.endStartSection("hand");
        boolean renderFirstPersonHand = Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[]{this.mc.renderGlobal, Float.valueOf(partialTicks), Integer.valueOf(pass)});

        if (!renderFirstPersonHand) {
            GlStateManager.clear(256);
            this.renderHand(partialTicks, pass);
            this.func_175067_i(partialTicks);
        }
    }

    public static void enableGL3D() {

    }

    public static void disableGL3D() {

        /*
         * if (Settings.isAntiAlias()) { GL11.glDisable(2848); GL11.glHint(3154,
         * 4352); GL11.glHint(3155, 4352); }
         */
    }

    public void fireRenderer(float renderPartialTicks, int position) {
        boolean extras = true;
        GlStateManager.pushMatrix();
        if (extras) {
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableTextures();
            // GlStateManager.disableDepth();
        }
        ((EventRender3D) EventSystem.getInstance(EventRender3D.class)).fire(renderPartialTicks, 0, 0, 0);
        if (extras) {
            GlStateManager.enableTextures();
            // GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.cullFace(GL11.GL_BACK);
        }
        GlStateManager.popMatrix();
    }

    private void func_180437_a(RenderGlobal p_180437_1_, float partialTicks, int pass) {
        if (this.mc.gameSettings.shouldRenderClouds()) {
            this.mc.mcProfiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            p_180437_1_.func_180447_b(partialTicks, pass);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.field_180189_a);
            GlStateManager.matrixMode(5888);
        }
    }

    private void addRainParticles() {
        float var1 = this.mc.theWorld.getRainStrength(1.0F);

        if (!Config.isRainFancy()) {
            var1 /= 2.0F;
        }

        if (var1 != 0.0F && Config.isRainSplash()) {
            this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
            Entity var2 = this.mc.func_175606_aa();
            WorldClient var3 = this.mc.theWorld;
            BlockPos var4 = new BlockPos(var2);
            byte var5 = 10;
            double var6 = 0.0D;
            double var8 = 0.0D;
            double var10 = 0.0D;
            int var12 = 0;
            int var13 = (int) (100.0F * var1 * var1);

            if (this.mc.gameSettings.particleSetting == 1) {
                var13 >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                var13 = 0;
            }

            for (int var14 = 0; var14 < var13; ++var14) {
                BlockPos var15 = var3.func_175725_q(var4.add(this.random.nextInt(var5) - this.random.nextInt(var5), 0, this.random.nextInt(var5) - this.random.nextInt(var5)));
                BiomeGenBase var16 = var3.getBiomeGenForCoords(var15);
                BlockPos var17 = var15.offsetDown();
                Block var18 = var3.getBlockState(var17).getBlock();

                if (var15.getY() <= var4.getY() + var5 && var15.getY() >= var4.getY() - var5 && var16.canSpawnLightningBolt() && var16.func_180626_a(var15) >= 0.15F) {
                    float var19 = this.random.nextFloat();
                    float var20 = this.random.nextFloat();

                    if (var18.getMaterial() == Material.lava) {
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) ((float) var15.getX() + var19), (double) ((float) var15.getY() + 0.1F) - var18.getBlockBoundsMinY(), (double) ((float) var15.getZ() + var20), 0.0D, 0.0D, 0.0D, new int[0]);
                    } else if (var18.getMaterial() != Material.air) {
                        var18.setBlockBoundsBasedOnState(var3, var17);
                        ++var12;

                        if (this.random.nextInt(var12) == 0) {
                            var6 = (double) ((float) var17.getX() + var19);
                            var8 = (double) ((float) var17.getY() + 0.1F) + var18.getBlockBoundsMaxY() - 1.0D;
                            var10 = (double) ((float) var17.getZ() + var20);
                        }

                        this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double) ((float) var17.getX() + var19), (double) ((float) var17.getY() + 0.1F) + var18.getBlockBoundsMaxY(), (double) ((float) var17.getZ() + var20), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
            }

            if (var12 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;

                if (var8 > (double) (var4.getY() + 1) && var3.func_175725_q(var4).getY() > MathHelper.floor_float((float) var4.getY())) {
                    this.mc.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.1F, 0.5F, false);
                } else {
                    this.mc.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.2F, 1.0F, false);
                }
            }
        }
    }

    /**
     * Render rain and snow
     */
    protected void renderRainSnow(float partialTicks) {
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
            WorldProvider var2 = this.mc.theWorld.provider;
            Object var3 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);

            if (var3 != null) {
                Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[]{Float.valueOf(partialTicks), this.mc.theWorld, this.mc});
                return;
            }
        }

        float var421 = this.mc.theWorld.getRainStrength(partialTicks);

        if (var421 > 0.0F) {
            if (Config.isRainOff()) {
                return;
            }

            this.func_180436_i();
            Entity var431 = this.mc.func_175606_aa();
            WorldClient var4 = this.mc.theWorld;
            int var5 = MathHelper.floor_double(var431.posX);
            int var6 = MathHelper.floor_double(var431.posY);
            int var7 = MathHelper.floor_double(var431.posZ);
            Tessellator var8 = Tessellator.getInstance();
            WorldRenderer var9 = var8.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1F);
            double var10 = var431.lastTickPosX + (var431.posX - var431.lastTickPosX) * (double) partialTicks;
            double var12 = var431.lastTickPosY + (var431.posY - var431.lastTickPosY) * (double) partialTicks;
            double var14 = var431.lastTickPosZ + (var431.posZ - var431.lastTickPosZ) * (double) partialTicks;
            int var16 = MathHelper.floor_double(var12);
            byte var17 = 5;

            if (Config.isRainFancy()) {
                var17 = 10;
            }

            byte var18 = -1;
            float var19 = (float) this.rendererUpdateCount + partialTicks;

            if (Config.isRainFancy()) {
                var17 = 10;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            for (int var20 = var7 - var17; var20 <= var7 + var17; ++var20) {
                for (int var21 = var5 - var17; var21 <= var5 + var17; ++var21) {
                    int var22 = (var20 - var7 + 16) * 32 + var21 - var5 + 16;
                    float var23 = this.field_175076_N[var22] * 0.5F;
                    float var24 = this.field_175077_O[var22] * 0.5F;
                    BlockPos var25 = new BlockPos(var21, 0, var20);
                    BiomeGenBase var26 = var4.getBiomeGenForCoords(var25);

                    if (var26.canSpawnLightningBolt() || var26.getEnableSnow()) {
                        int var27 = var4.func_175725_q(var25).getY();
                        int var28 = var6 - var17;
                        int var29 = var6 + var17;

                        if (var28 < var27) {
                            var28 = var27;
                        }

                        if (var29 < var27) {
                            var29 = var27;
                        }

                        float var30 = 1.0F;
                        int var31 = var27;

                        if (var27 < var16) {
                            var31 = var16;
                        }

                        if (var28 != var29) {
                            this.random.setSeed((long) (var21 * var21 * 3121 + var21 * 45238971 ^ var20 * var20 * 418711 + var20 * 13761));
                            float var32 = var26.func_180626_a(new BlockPos(var21, var28, var20));
                            float var33;
                            double var36;

                            if (var4.getWorldChunkManager().getTemperatureAtHeight(var32, var27) >= 0.15F) {
                                if (var18 != 0) {
                                    if (var18 >= 0) {
                                        var8.draw();
                                    }

                                    var18 = 0;
                                    this.mc.getTextureManager().bindTexture(locationRainPng);
                                    var9.startDrawingQuads();
                                }

                                var33 = ((float) (this.rendererUpdateCount + var21 * var21 * 3121 + var21 * 45238971 + var20 * var20 * 418711 + var20 * 13761 & 31) + partialTicks) / 32.0F * (3.0F + this.random.nextFloat());
                                double var42 = (double) ((float) var21 + 0.5F) - var431.posX;
                                var36 = (double) ((float) var20 + 0.5F) - var431.posZ;
                                float var43 = MathHelper.sqrt_double(var42 * var42 + var36 * var36) / (float) var17;
                                float var39 = 1.0F;
                                var9.func_178963_b(var4.getCombinedLight(new BlockPos(var21, var31, var20), 0));
                                var9.setColorRGBA(var39, var39, var39, ((1.0F - var43 * var43) * 0.5F + 0.5F) * var421);
                                var9.setTranslation(-var10 * 1.0D, -var12 * 1.0D, -var14 * 1.0D);
                                var9.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var30), (double) ((float) var28 * var30 / 4.0F + var33 * var30));
                                var9.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var30), (double) ((float) var28 * var30 / 4.0F + var33 * var30));
                                var9.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var29, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var30), (double) ((float) var29 * var30 / 4.0F + var33 * var30));
                                var9.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var29, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var30), (double) ((float) var29 * var30 / 4.0F + var33 * var30));
                                var9.setTranslation(0.0D, 0.0D, 0.0D);
                            } else {
                                if (var18 != 1) {
                                    if (var18 >= 0) {
                                        var8.draw();
                                    }

                                    var18 = 1;
                                    this.mc.getTextureManager().bindTexture(locationSnowPng);
                                    var9.startDrawingQuads();
                                }

                                var33 = ((float) (this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
                                float var44 = this.random.nextFloat() + var19 * 0.01F * (float) this.random.nextGaussian();
                                float var35 = this.random.nextFloat() + var19 * (float) this.random.nextGaussian() * 0.001F;
                                var36 = (double) ((float) var21 + 0.5F) - var431.posX;
                                double var45 = (double) ((float) var20 + 0.5F) - var431.posZ;
                                float var40 = MathHelper.sqrt_double(var36 * var36 + var45 * var45) / (float) var17;
                                float var41 = 1.0F;
                                var9.func_178963_b((var4.getCombinedLight(new BlockPos(var21, var31, var20), 0) * 3 + 15728880) / 4);
                                var9.setColorRGBA(var41, var41, var41, ((1.0F - var40 * var40) * 0.3F + 0.5F) * var421);
                                var9.setTranslation(-var10 * 1.0D, -var12 * 1.0D, -var14 * 1.0D);
                                var9.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var30 + var44), (double) ((float) var28 * var30 / 4.0F + var33 * var30 + var35));
                                var9.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var30 + var44), (double) ((float) var28 * var30 / 4.0F + var33 * var30 + var35));
                                var9.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var29, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var30 + var44), (double) ((float) var29 * var30 / 4.0F + var33 * var30 + var35));
                                var9.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var29, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var30 + var44), (double) ((float) var29 * var30 / 4.0F + var33 * var30 + var35));
                                var9.setTranslation(0.0D, 0.0D, 0.0D);
                            }
                        }
                    }
                }
            }

            if (var18 >= 0) {
                var8.draw();
            }

            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            this.func_175072_h();
        }
    }

    /**
     * Setup orthogonal projection for rendering GUI screen overlays
     */
    public void setupOverlayRendering() {
        ScaledResolution var1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    /**
     * calculates fog and calls glClearColor
     */
    private void updateFogColor(float partialTicks) {
        WorldClient var2 = this.mc.theWorld;
        Entity var3 = this.mc.func_175606_aa();
        float var4 = 0.25F + 0.75F * (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
        var4 = 1.0F - (float) Math.pow((double) var4, 0.25D);
        Vec3 var5 = var2.getSkyColor(this.mc.func_175606_aa(), partialTicks);
        var5 = CustomColorizer.getWorldSkyColor(var5, var2, this.mc.func_175606_aa(), partialTicks);
        float var6 = (float) var5.xCoord;
        float var7 = (float) var5.yCoord;
        float var8 = (float) var5.zCoord;
        Vec3 var9 = var2.getFogColor(partialTicks);
        var9 = CustomColorizer.getWorldFogColor(var9, var2, this.mc.func_175606_aa(), partialTicks);
        this.field_175080_Q = (float) var9.xCoord;
        this.field_175082_R = (float) var9.yCoord;
        this.field_175081_S = (float) var9.zCoord;
        float var13;

        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            double var19 = -1.0D;
            Vec3 var20 = MathHelper.sin(var2.getCelestialAngleRadians(partialTicks)) > 0.0F ? new Vec3(var19, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
            var13 = (float) var3.getLook(partialTicks).dotProduct(var20);

            if (var13 < 0.0F) {
                var13 = 0.0F;
            }

            if (var13 > 0.0F) {
                float[] var21 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(partialTicks), partialTicks);

                if (var21 != null) {
                    var13 *= var21[3];
                    this.field_175080_Q = this.field_175080_Q * (1.0F - var13) + var21[0] * var13;
                    this.field_175082_R = this.field_175082_R * (1.0F - var13) + var21[1] * var13;
                    this.field_175081_S = this.field_175081_S * (1.0F - var13) + var21[2] * var13;
                }
            }
        }

        this.field_175080_Q += (var6 - this.field_175080_Q) * var4;
        this.field_175082_R += (var7 - this.field_175082_R) * var4;
        this.field_175081_S += (var8 - this.field_175081_S) * var4;
        float var191 = var2.getRainStrength(partialTicks);
        float var11;
        float var201;

        if (var191 > 0.0F) {
            var11 = 1.0F - var191 * 0.5F;
            var201 = 1.0F - var191 * 0.4F;
            this.field_175080_Q *= var11;
            this.field_175082_R *= var11;
            this.field_175081_S *= var201;
        }

        var11 = var2.getWeightedThunderStrength(partialTicks);

        if (var11 > 0.0F) {
            var201 = 1.0F - var11 * 0.5F;
            this.field_175080_Q *= var201;
            this.field_175082_R *= var201;
            this.field_175081_S *= var201;
        }

        Block var211 = ActiveRenderInfo.func_180786_a(this.mc.theWorld, var3, partialTicks);
        Vec3 fogYFactor;

        if (this.cloudFog) {
            fogYFactor = var2.getCloudColour(partialTicks);
            this.field_175080_Q = (float) fogYFactor.xCoord;
            this.field_175082_R = (float) fogYFactor.yCoord;
            this.field_175081_S = (float) fogYFactor.zCoord;
        } else if (var211.getMaterial() == Material.water) {
            var13 = (float) EnchantmentHelper.func_180319_a(var3) * 0.2F;

            if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).isPotionActive(Potion.waterBreathing)) {
                var13 = var13 * 0.3F + 0.6F;
            }

            this.field_175080_Q = 0.02F + var13;
            this.field_175082_R = 0.02F + var13;
            this.field_175081_S = 0.2F + var13;
            fogYFactor = CustomColorizer.getUnderwaterColor(this.mc.theWorld, this.mc.func_175606_aa().posX, this.mc.func_175606_aa().posY + 1.0D, this.mc.func_175606_aa().posZ);

            if (fogYFactor != null) {
                this.field_175080_Q = (float) fogYFactor.xCoord;
                this.field_175082_R = (float) fogYFactor.yCoord;
                this.field_175081_S = (float) fogYFactor.zCoord;
            }
        } else if (var211.getMaterial() == Material.lava) {
            this.field_175080_Q = 0.6F;
            this.field_175082_R = 0.1F;
            this.field_175081_S = 0.0F;
        }

        var13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.field_175080_Q *= var13;
        this.field_175082_R *= var13;
        this.field_175081_S *= var13;
        double fogYFactor1 = var2.provider.getVoidFogYFactor();
        double var23 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double) partialTicks) * fogYFactor1;

        if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).isPotionActive(Potion.blindness)) {
            int var24 = ((EntityLivingBase) var3).getActivePotionEffect(Potion.blindness).getDuration();

            if (var24 < 20) {
                var23 *= (double) (1.0F - (float) var24 / 20.0F);
            } else {
                var23 = 0.0D;
            }
        }

        if (var23 < 1.0D) {
            if (var23 < 0.0D) {
                var23 = 0.0D;
            }

            var23 *= var23;
            this.field_175080_Q = (float) ((double) this.field_175080_Q * var23);
            this.field_175082_R = (float) ((double) this.field_175082_R * var23);
            this.field_175081_S = (float) ((double) this.field_175081_S * var23);
        }

        float var241;

        if (this.bossColorModifier > 0.0F) {
            var241 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.field_175080_Q = this.field_175080_Q * (1.0F - var241) + this.field_175080_Q * 0.7F * var241;
            this.field_175082_R = this.field_175082_R * (1.0F - var241) + this.field_175082_R * 0.6F * var241;
            this.field_175081_S = this.field_175081_S * (1.0F - var241) + this.field_175081_S * 0.6F * var241;
        }

        float var17;

        if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).isPotionActive(Potion.nightVision)) {
            var241 = this.func_180438_a((EntityLivingBase) var3, partialTicks);
            var17 = 1.0F / this.field_175080_Q;

            if (var17 > 1.0F / this.field_175082_R) {
                var17 = 1.0F / this.field_175082_R;
            }

            if (var17 > 1.0F / this.field_175081_S) {
                var17 = 1.0F / this.field_175081_S;
            }

            this.field_175080_Q = this.field_175080_Q * (1.0F - var241) + this.field_175080_Q * var17 * var241;
            this.field_175082_R = this.field_175082_R * (1.0F - var241) + this.field_175082_R * var17 * var241;
            this.field_175081_S = this.field_175081_S * (1.0F - var241) + this.field_175081_S * var17 * var241;
        }

        if (this.mc.gameSettings.anaglyph) {
            var241 = (this.field_175080_Q * 30.0F + this.field_175082_R * 59.0F + this.field_175081_S * 11.0F) / 100.0F;
            var17 = (this.field_175080_Q * 30.0F + this.field_175082_R * 70.0F) / 100.0F;
            float event = (this.field_175080_Q * 30.0F + this.field_175081_S * 70.0F) / 100.0F;
            this.field_175080_Q = var241;
            this.field_175082_R = var17;
            this.field_175081_S = event;
        }

        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
            Object event1 = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[]{this, var3, var211, Float.valueOf(partialTicks), Float.valueOf(this.field_175080_Q), Float.valueOf(this.field_175082_R), Float.valueOf(this.field_175081_S)});
            Reflector.postForgeBusEvent(event1);
            this.field_175080_Q = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_red, this.field_175080_Q);
            this.field_175082_R = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_green, this.field_175082_R);
            this.field_175081_S = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_blue, this.field_175081_S);
        }

        GlStateManager.clearColor(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 0.0F);
    }

    /**
     * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts
     * at 0 and goes to 80% of far plane distance and is used for sky rendering.
     */
    private void setupFog(int p_78468_1_, float partialTicks) {
        Entity var3 = this.mc.func_175606_aa();
        boolean var4 = false;
        this.fogStandard = false;

        if (var3 instanceof EntityPlayer) {
            var4 = ((EntityPlayer) var3).capabilities.isCreativeMode;
        }

        GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Block var5 = ActiveRenderInfo.func_180786_a(this.mc.theWorld, var3, partialTicks);
        Object event = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogDensity_Constructor, new Object[]{this, var3, var5, Float.valueOf(partialTicks), Float.valueOf(0.1F)});

        if (Reflector.postForgeBusEvent(event)) {
            float var7 = Reflector.getFieldValueFloat(event, Reflector.EntityViewRenderEvent_FogDensity_density, 0.0F);
            GL11.glFogf(GL11.GL_FOG_DENSITY, var7);
        } else {
            float var6;

            if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).isPotionActive(Potion.blindness)) {
                var6 = 5.0F;
                int var71 = ((EntityLivingBase) var3).getActivePotionEffect(Potion.blindness).getDuration();

                if (var71 < 20) {
                    var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) var71 / 20.0F);
                }

                GlStateManager.setFog(9729);

                if (p_78468_1_ == -1) {
                    GlStateManager.setFogStart(0.0F);
                    GlStateManager.setFogEnd(var6 * 0.8F);
                } else {
                    GlStateManager.setFogStart(var6 * 0.25F);
                    GlStateManager.setFogEnd(var6);
                }

                if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                    GL11.glFogi(34138, 34139);
                }
            } else if (this.cloudFog) {
                GlStateManager.setFog(2048);
                GlStateManager.setFogDensity(0.1F);
            } else if (var5.getMaterial() == Material.water) {
                GlStateManager.setFog(2048);

                if (var3 instanceof EntityLivingBase && ((EntityLivingBase) var3).isPotionActive(Potion.waterBreathing)) {
                    GlStateManager.setFogDensity(0.01F);
                } else {
                    GlStateManager.setFogDensity(0.1F - (float) EnchantmentHelper.func_180319_a(var3) * 0.03F);
                }

                if (Config.isClearWater()) {
                    GL11.glFogf(GL11.GL_FOG_DENSITY, 0.02F);
                }
            } else if (var5.getMaterial() == Material.lava) {
                GlStateManager.setFog(2048);
                GlStateManager.setFogDensity(2.0F);
            } else {
                var6 = this.farPlaneDistance;
                this.fogStandard = true;
                GlStateManager.setFog(9729);

                if (p_78468_1_ == -1) {
                    GlStateManager.setFogStart(0.0F);
                    GlStateManager.setFogEnd(var6);
                } else {
                    GlStateManager.setFogStart(var6 * Config.getFogStart());
                    GlStateManager.setFogEnd(var6);
                }

                if (GLContext.getCapabilities().GL_NV_fog_distance) {
                    if (Config.isFogFancy()) {
                        GL11.glFogi(34138, 34139);
                    }

                    if (Config.isFogFast()) {
                        GL11.glFogi(34138, 34140);
                    }
                }

                if (this.mc.theWorld.provider.doesXZShowFog((int) var3.posX, (int) var3.posZ)) {
                    GlStateManager.setFogStart(var6 * 0.05F);
                    GlStateManager.setFogEnd(var6);
                }

                Reflector.postForgeBusEvent(Reflector.newInstance(Reflector.EntityViewRenderEvent_RenderFogEvent_Constructor, new Object[]{this, var3, var5, Float.valueOf(partialTicks), Integer.valueOf(p_78468_1_), Float.valueOf(var6)}));
            }
        }

        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    /**
     * Update and return fogColorBuffer with the RGBA values passed as arguments
     */
    private FloatBuffer setFogColorBuffer(float p_78469_1_, float p_78469_2_, float p_78469_3_, float p_78469_4_) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public MapItemRenderer getMapItemRenderer() {
        return this.theMapItemRenderer;
    }

    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;

        if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
            if (this.mc.isIntegratedServerRunning()) {
                IntegratedServer srv = this.mc.getIntegratedServer();

                if (srv != null) {
                    boolean paused = this.mc.isGamePaused();

                    if (!paused && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                        if (this.serverWaitTime > 0) {
                            Lagometer.timerServer.start();
                            Config.sleep((long) this.serverWaitTime);
                            Lagometer.timerServer.end();
                            this.serverWaitTimeCurrent = this.serverWaitTime;
                        }

                        long timeNow = System.nanoTime() / 1000000L;

                        if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                            long timeDiff = timeNow - this.lastServerTime;

                            if (timeDiff < 0L) {
                                this.lastServerTime = timeNow;
                                timeDiff = 0L;
                            }

                            if (timeDiff >= 50L) {
                                this.lastServerTime = timeNow;
                                int ticks = srv.getTickCounter();
                                int tickDiff = ticks - this.lastServerTicks;

                                if (tickDiff < 0) {
                                    this.lastServerTicks = ticks;
                                    tickDiff = 0;
                                }

                                if (tickDiff < 1 && this.serverWaitTime < 100) {
                                    this.serverWaitTime += 2;
                                }

                                if (tickDiff > 1 && this.serverWaitTime > 0) {
                                    --this.serverWaitTime;
                                }

                                this.lastServerTicks = ticks;
                            }
                        } else {
                            this.lastServerTime = timeNow;
                            this.lastServerTicks = srv.getTickCounter();
                            this.avgServerTickDiff = 1.0F;
                            this.avgServerTimeDiff = 50.0F;
                        }
                    } else {
                        if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                            Config.sleep(20L);
                        }

                        this.lastServerTime = 0L;
                        this.lastServerTicks = 0;
                    }
                }
            }
        } else {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        }
    }

    private void frameInit() {
        if (!this.initialized) {
            TextureUtils.registerResourceListener();
            RenderPlayerOF.register();
            this.initialized = true;
        }

        Config.isActing();
        Config.checkDisplayMode();
        WorldClient world = this.mc.theWorld;

        if (this.mc.currentScreen instanceof ClientMainMenu) {
            this.updateMainMenu((ClientMainMenu) this.mc.currentScreen);
        }

        if (this.updatedWorld != world) {
            RandomMobs.worldChanged(this.updatedWorld, world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }
    }

    private void updateMainMenu(ClientMainMenu mainGui) {
        try {
            String e = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(5);
            int month = calendar.get(2) + 1;

            if (day == 8 && month == 4) {
                e = "Happy birthday, OptiFine!";
            }

            if (day == 14 && month == 8) {
                e = "Happy birthday, sp614x!";
            }

            if (e == null) {
                return;
            }

            Field[] fs = ClientMainMenu.class.getDeclaredFields();

            for (int i = 0; i < fs.length; ++i) {
                if (fs[i].getType() == String.class) {
                    fs[i].setAccessible(true);
                    fs[i].set(mainGui, e);
                    break;
                }
            }
        } catch (Throwable var8) {
            ;
        }
    }
}
