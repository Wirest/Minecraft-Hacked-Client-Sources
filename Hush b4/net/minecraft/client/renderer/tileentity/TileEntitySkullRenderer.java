// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.renderer.GlStateManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntitySkull;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull>
{
    private static final ResourceLocation SKELETON_TEXTURES;
    private static final ResourceLocation WITHER_SKELETON_TEXTURES;
    private static final ResourceLocation ZOMBIE_TEXTURES;
    private static final ResourceLocation CREEPER_TEXTURES;
    public static TileEntitySkullRenderer instance;
    private final ModelSkeletonHead skeletonHead;
    private final ModelSkeletonHead humanoidHead;
    
    static {
        SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
        WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
        ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
        CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    }
    
    public TileEntitySkullRenderer() {
        this.skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
        this.humanoidHead = new ModelHumanoidHead();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntitySkull te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final EnumFacing enumfacing = EnumFacing.getFront(te.getBlockMetadata() & 0x7);
        this.renderSkull((float)x, (float)y, (float)z, enumfacing, te.getSkullRotation() * 360 / 16.0f, te.getSkullType(), te.getPlayerProfile(), destroyStage);
    }
    
    @Override
    public void setRendererDispatcher(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super.setRendererDispatcher(rendererDispatcherIn);
        TileEntitySkullRenderer.instance = this;
    }
    
    public void renderSkull(final float p_180543_1_, final float p_180543_2_, final float p_180543_3_, final EnumFacing p_180543_4_, float p_180543_5_, final int p_180543_6_, final GameProfile p_180543_7_, final int p_180543_8_) {
        ModelBase modelbase = this.skeletonHead;
        if (p_180543_8_ >= 0) {
            this.bindTexture(TileEntitySkullRenderer.DESTROY_STAGES[p_180543_8_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            switch (p_180543_6_) {
                default: {
                    this.bindTexture(TileEntitySkullRenderer.SKELETON_TEXTURES);
                    break;
                }
                case 1: {
                    this.bindTexture(TileEntitySkullRenderer.WITHER_SKELETON_TEXTURES);
                    break;
                }
                case 2: {
                    this.bindTexture(TileEntitySkullRenderer.ZOMBIE_TEXTURES);
                    modelbase = this.humanoidHead;
                    break;
                }
                case 3: {
                    modelbase = this.humanoidHead;
                    ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
                    if (p_180543_7_ != null) {
                        final Minecraft minecraft = Minecraft.getMinecraft();
                        final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(p_180543_7_);
                        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                            resourcelocation = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                        }
                        else {
                            final UUID uuid = EntityPlayer.getUUID(p_180543_7_);
                            resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
                        }
                    }
                    this.bindTexture(resourcelocation);
                    break;
                }
                case 4: {
                    this.bindTexture(TileEntitySkullRenderer.CREEPER_TEXTURES);
                    break;
                }
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        if (p_180543_4_ != EnumFacing.UP) {
            switch (p_180543_4_) {
                case NORTH: {
                    GlStateManager.translate(p_180543_1_ + 0.5f, p_180543_2_ + 0.25f, p_180543_3_ + 0.74f);
                    break;
                }
                case SOUTH: {
                    GlStateManager.translate(p_180543_1_ + 0.5f, p_180543_2_ + 0.25f, p_180543_3_ + 0.26f);
                    p_180543_5_ = 180.0f;
                    break;
                }
                case WEST: {
                    GlStateManager.translate(p_180543_1_ + 0.74f, p_180543_2_ + 0.25f, p_180543_3_ + 0.5f);
                    p_180543_5_ = 270.0f;
                    break;
                }
                default: {
                    GlStateManager.translate(p_180543_1_ + 0.26f, p_180543_2_ + 0.25f, p_180543_3_ + 0.5f);
                    p_180543_5_ = 90.0f;
                    break;
                }
            }
        }
        else {
            GlStateManager.translate(p_180543_1_ + 0.5f, p_180543_2_, p_180543_3_ + 0.5f);
        }
        final float f = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        modelbase.render(null, 0.0f, 0.0f, 0.0f, p_180543_5_, 0.0f, f);
        GlStateManager.popMatrix();
        if (p_180543_8_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
