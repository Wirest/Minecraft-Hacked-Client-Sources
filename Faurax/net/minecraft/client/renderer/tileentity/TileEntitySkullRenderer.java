package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147537_c = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation field_147534_d = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    private static final ResourceLocation field_147535_e = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation field_147532_f = new ResourceLocation("textures/entity/creeper/creeper.png");
    public static TileEntitySkullRenderer instance;
    private final ModelSkeletonHead field_178467_h = new ModelSkeletonHead(0, 0, 64, 32);
    private final ModelSkeletonHead field_178468_i = new ModelHumanoidHead();
    private static final String __OBFID = "CL_00000971";

    public void func_180542_a(TileEntitySkull p_180542_1_, double p_180542_2_, double p_180542_4_, double p_180542_6_, float p_180542_8_, int p_180542_9_)
    {
        EnumFacing var10 = EnumFacing.getFront(p_180542_1_.getBlockMetadata() & 7);
        this.renderSkull((float)p_180542_2_, (float)p_180542_4_, (float)p_180542_6_, var10, (float)(p_180542_1_.getSkullRotation() * 360) / 16.0F, p_180542_1_.getSkullType(), p_180542_1_.getPlayerProfile(), p_180542_9_);
    }

    public void setRendererDispatcher(TileEntityRendererDispatcher p_147497_1_)
    {
        super.setRendererDispatcher(p_147497_1_);
        instance = this;
    }

    public void renderSkull(float p_180543_1_, float p_180543_2_, float p_180543_3_, EnumFacing p_180543_4_, float p_180543_5_, int p_180543_6_, GameProfile p_180543_7_, int p_180543_8_)
    {
        ModelSkeletonHead var9 = this.field_178467_h;

        if (p_180543_8_ >= 0)
        {
            this.bindTexture(DESTROY_STAGES[p_180543_8_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            switch (p_180543_6_)
            {
                case 0:
                default:
                    this.bindTexture(field_147537_c);
                    break;

                case 1:
                    this.bindTexture(field_147534_d);
                    break;

                case 2:
                    this.bindTexture(field_147535_e);
                    var9 = this.field_178468_i;
                    break;

                case 3:
                    var9 = this.field_178468_i;
                    ResourceLocation var10 = DefaultPlayerSkin.func_177335_a();

                    if (p_180543_7_ != null)
                    {
                        Minecraft var11 = Minecraft.getMinecraft();
                        Map var12 = var11.getSkinManager().loadSkinFromCache(p_180543_7_);

                        if (var12.containsKey(Type.SKIN))
                        {
                            var10 = var11.getSkinManager().loadSkin((MinecraftProfileTexture)var12.get(Type.SKIN), Type.SKIN);
                        }
                        else
                        {
                            UUID var13 = EntityPlayer.getUUID(p_180543_7_);
                            var10 = DefaultPlayerSkin.func_177334_a(var13);
                        }
                    }

                    this.bindTexture(var10);
                    break;

                case 4:
                    this.bindTexture(field_147532_f);
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (p_180543_4_ != EnumFacing.UP)
        {
            switch (TileEntitySkullRenderer.SwitchEnumFacing.field_178458_a[p_180543_4_.ordinal()])
            {
                case 1:
                    GlStateManager.translate(p_180543_1_ + 0.5F, p_180543_2_ + 0.25F, p_180543_3_ + 0.74F);
                    break;

                case 2:
                    GlStateManager.translate(p_180543_1_ + 0.5F, p_180543_2_ + 0.25F, p_180543_3_ + 0.26F);
                    p_180543_5_ = 180.0F;
                    break;

                case 3:
                    GlStateManager.translate(p_180543_1_ + 0.74F, p_180543_2_ + 0.25F, p_180543_3_ + 0.5F);
                    p_180543_5_ = 270.0F;
                    break;

                case 4:
                default:
                    GlStateManager.translate(p_180543_1_ + 0.26F, p_180543_2_ + 0.25F, p_180543_3_ + 0.5F);
                    p_180543_5_ = 90.0F;
            }
        }
        else
        {
            GlStateManager.translate(p_180543_1_ + 0.5F, p_180543_2_, p_180543_3_ + 0.5F);
        }

        float var14 = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        var9.render((Entity)null, 0.0F, 0.0F, 0.0F, p_180543_5_, 0.0F, var14);
        GlStateManager.popMatrix();

        if (p_180543_8_ >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double p_180535_2_, double p_180535_4_, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180542_a((TileEntitySkull)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_178458_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002468";

        static
        {
            try
            {
                field_178458_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_178458_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_178458_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_178458_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
