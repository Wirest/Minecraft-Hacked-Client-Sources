package optifine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockModelUtils
{
    private static final float VERTEX_COORD_ACCURACY = 1.0E-6F;

    public static IBakedModel makeModelCube(String p_makeModelCube_0_, int p_makeModelCube_1_)
    {
        TextureAtlasSprite textureatlassprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(p_makeModelCube_0_);
        return makeModelCube(textureatlassprite, p_makeModelCube_1_);
    }

    public static IBakedModel makeModelCube(TextureAtlasSprite p_makeModelCube_0_, int p_makeModelCube_1_)
    {
        List list = new ArrayList();
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        List<List<BakedQuad>> list1 = new ArrayList();

        for (int i = 0; i < aenumfacing.length; ++i)
        {
            EnumFacing enumfacing = aenumfacing[i];
            List list2 = new ArrayList();
            list2.add(makeBakedQuad(enumfacing, p_makeModelCube_0_, p_makeModelCube_1_));
            list1.add(list2);
        }

        IBakedModel ibakedmodel = new SimpleBakedModel(list, list1, true, true, p_makeModelCube_0_, ItemCameraTransforms.DEFAULT);
        return ibakedmodel;
    }

    public static IBakedModel joinModelsCube(IBakedModel p_joinModelsCube_0_, IBakedModel p_joinModelsCube_1_)
    {
        List<BakedQuad> list = new ArrayList();
        list.addAll(p_joinModelsCube_0_.getGeneralQuads());
        list.addAll(p_joinModelsCube_1_.getGeneralQuads());
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        List list1 = new ArrayList();

        for (int i = 0; i < aenumfacing.length; ++i)
        {
            EnumFacing enumfacing = aenumfacing[i];
            List list2 = new ArrayList();
            list2.addAll(p_joinModelsCube_0_.getFaceQuads(enumfacing));
            list2.addAll(p_joinModelsCube_1_.getFaceQuads(enumfacing));
            list1.add(list2);
        }

        boolean flag = p_joinModelsCube_0_.isAmbientOcclusion();
        boolean flag1 = p_joinModelsCube_0_.isBuiltInRenderer();
        TextureAtlasSprite textureatlassprite = p_joinModelsCube_0_.getParticleTexture();
        ItemCameraTransforms itemcameratransforms = p_joinModelsCube_0_.getItemCameraTransforms();
        IBakedModel ibakedmodel = new SimpleBakedModel(list, list1, flag, flag1, textureatlassprite, itemcameratransforms);
        return ibakedmodel;
    }

    public static BakedQuad makeBakedQuad(EnumFacing p_makeBakedQuad_0_, TextureAtlasSprite p_makeBakedQuad_1_, int p_makeBakedQuad_2_)
    {
        Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f vector3f1 = new Vector3f(16.0F, 16.0F, 16.0F);
        BlockFaceUV blockfaceuv = new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace blockpartface = new BlockPartFace(p_makeBakedQuad_0_, p_makeBakedQuad_2_, "#" + p_makeBakedQuad_0_.getName(), blockfaceuv);
        ModelRotation modelrotation = ModelRotation.X0_Y0;
        BlockPartRotation blockpartrotation = null;
        boolean flag = false;
        boolean flag1 = true;
        FaceBakery facebakery = new FaceBakery();
        BakedQuad bakedquad = facebakery.makeBakedQuad(vector3f, vector3f1, blockpartface, p_makeBakedQuad_1_, p_makeBakedQuad_0_, modelrotation, blockpartrotation, flag, flag1);
        return bakedquad;
    }

    public static IBakedModel makeModel(String p_makeModel_0_, String p_makeModel_1_, String p_makeModel_2_)
    {
        TextureMap texturemap = Config.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(p_makeModel_1_);
        TextureAtlasSprite textureatlassprite1 = texturemap.getSpriteSafe(p_makeModel_2_);
        return makeModel(p_makeModel_0_, textureatlassprite, textureatlassprite1);
    }

    public static IBakedModel makeModel(String p_makeModel_0_, TextureAtlasSprite p_makeModel_1_, TextureAtlasSprite p_makeModel_2_)
    {
        if (p_makeModel_1_ != null && p_makeModel_2_ != null)
        {
            ModelManager modelmanager = Config.getModelManager();

            if (modelmanager == null)
            {
                return null;
            }
            else
            {
                ModelResourceLocation modelresourcelocation = new ModelResourceLocation(p_makeModel_0_, "normal");
                IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);

                if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel())
                {
                    IBakedModel ibakedmodel1 = ModelUtils.duplicateModel(ibakedmodel);
                    EnumFacing[] aenumfacing = EnumFacing.VALUES;

                    for (int i = 0; i < aenumfacing.length; ++i)
                    {
                        EnumFacing enumfacing = aenumfacing[i];
                        List<BakedQuad> list = ibakedmodel1.getFaceQuads(enumfacing);
                        replaceTexture(list, p_makeModel_1_, p_makeModel_2_);
                    }

                    List<BakedQuad> list1 = ibakedmodel1.getGeneralQuads();
                    replaceTexture(list1, p_makeModel_1_, p_makeModel_2_);
                    return ibakedmodel1;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }

    private static void replaceTexture(List<BakedQuad> p_replaceTexture_0_, TextureAtlasSprite p_replaceTexture_1_, TextureAtlasSprite p_replaceTexture_2_)
    {
        List<BakedQuad> list = new ArrayList();

        for (BakedQuad bakedquad : p_replaceTexture_0_)
        {
            if (bakedquad.getSprite() != p_replaceTexture_1_)
            {
                list.add(bakedquad);
                break;
            }

            BakedQuad bakedquad1 = new BreakingFour(bakedquad, p_replaceTexture_2_);
            list.add(bakedquad1);
        }

        p_replaceTexture_0_.clear();
        p_replaceTexture_0_.addAll(list);
    }

    public static void snapVertexPosition(Vector3f p_snapVertexPosition_0_)
    {
        p_snapVertexPosition_0_.setX(snapVertexCoord(p_snapVertexPosition_0_.getX()));
        p_snapVertexPosition_0_.setY(snapVertexCoord(p_snapVertexPosition_0_.getY()));
        p_snapVertexPosition_0_.setZ(snapVertexCoord(p_snapVertexPosition_0_.getZ()));
    }

    private static float snapVertexCoord(float p_snapVertexCoord_0_)
    {
        return p_snapVertexCoord_0_ > -1.0E-6F && p_snapVertexCoord_0_ < 1.0E-6F ? 0.0F : (p_snapVertexCoord_0_ > 0.999999F && p_snapVertexCoord_0_ < 1.000001F ? 1.0F : p_snapVertexCoord_0_);
    }

    public static AxisAlignedBB getOffsetBoundingBox(AxisAlignedBB p_getOffsetBoundingBox_0_, Block.EnumOffsetType p_getOffsetBoundingBox_1_, BlockPos p_getOffsetBoundingBox_2_)
    {
        int i = p_getOffsetBoundingBox_2_.getX();
        int j = p_getOffsetBoundingBox_2_.getZ();
        long k = (long)(i * 3129871) ^ (long)j * 116129781L;
        k = k * k * 42317861L + k * 11L;
        double d0 = ((double)((float)(k >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
        double d1 = ((double)((float)(k >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        double d2 = 0.0D;

        if (p_getOffsetBoundingBox_1_ == Block.EnumOffsetType.XYZ)
        {
            d2 = ((double)((float)(k >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
        }

        return p_getOffsetBoundingBox_0_.offset(d0, d2, d1);
    }
}
