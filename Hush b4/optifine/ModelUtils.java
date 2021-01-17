// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.resources.model.SimpleBakedModel;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.resources.model.IBakedModel;

public class ModelUtils
{
    public static void dbgModel(final IBakedModel p_dbgModel_0_) {
        if (p_dbgModel_0_ != null) {
            Config.dbg("Model: " + p_dbgModel_0_ + ", ao: " + p_dbgModel_0_.isAmbientOcclusion() + ", gui3d: " + p_dbgModel_0_.isGui3d() + ", builtIn: " + p_dbgModel_0_.isBuiltInRenderer() + ", particle: " + p_dbgModel_0_.getParticleTexture());
            final EnumFacing[] aenumfacing = EnumFacing.VALUES;
            for (int i = 0; i < aenumfacing.length; ++i) {
                final EnumFacing enumfacing = aenumfacing[i];
                final List list = p_dbgModel_0_.getFaceQuads(enumfacing);
                dbgQuads(enumfacing.getName(), list, "  ");
            }
            final List list2 = p_dbgModel_0_.getGeneralQuads();
            dbgQuads("General", list2, "  ");
        }
    }
    
    private static void dbgQuads(final String p_dbgQuads_0_, final List p_dbgQuads_1_, final String p_dbgQuads_2_) {
        for (final Object bakedquad : p_dbgQuads_1_) {
            dbgQuad(p_dbgQuads_0_, (BakedQuad)bakedquad, p_dbgQuads_2_);
        }
    }
    
    public static void dbgQuad(final String p_dbgQuad_0_, final BakedQuad p_dbgQuad_1_, final String p_dbgQuad_2_) {
        Config.dbg(String.valueOf(p_dbgQuad_2_) + "Quad: " + p_dbgQuad_1_.getClass().getName() + ", type: " + p_dbgQuad_0_ + ", face: " + p_dbgQuad_1_.getFace() + ", tint: " + p_dbgQuad_1_.getTintIndex() + ", sprite: " + p_dbgQuad_1_.getSprite());
        dbgVertexData(p_dbgQuad_1_.getVertexData(), "  " + p_dbgQuad_2_);
    }
    
    public static void dbgVertexData(final int[] p_dbgVertexData_0_, final String p_dbgVertexData_1_) {
        final int i = p_dbgVertexData_0_.length / 4;
        Config.dbg(String.valueOf(p_dbgVertexData_1_) + "Length: " + p_dbgVertexData_0_.length + ", step: " + i);
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f = Float.intBitsToFloat(p_dbgVertexData_0_[k + 0]);
            final float f2 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 1]);
            final float f3 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 2]);
            final int l = p_dbgVertexData_0_[k + 3];
            final float f4 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 4]);
            final float f5 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 5]);
            Config.dbg(String.valueOf(p_dbgVertexData_1_) + j + " xyz: " + f + "," + f2 + "," + f3 + " col: " + l + " u,v: " + f4 + "," + f5);
        }
    }
    
    public static IBakedModel duplicateModel(final IBakedModel p_duplicateModel_0_) {
        final List list = duplicateQuadList(p_duplicateModel_0_.getGeneralQuads());
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final List list2 = new ArrayList();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list3 = p_duplicateModel_0_.getFaceQuads(enumfacing);
            final List list4 = duplicateQuadList(list3);
            list2.add(list4);
        }
        final SimpleBakedModel simplebakedmodel = new SimpleBakedModel(list, list2, p_duplicateModel_0_.isAmbientOcclusion(), p_duplicateModel_0_.isGui3d(), p_duplicateModel_0_.getParticleTexture(), p_duplicateModel_0_.getItemCameraTransforms());
        return simplebakedmodel;
    }
    
    public static List duplicateQuadList(final List p_duplicateQuadList_0_) {
        final List list = new ArrayList();
        for (final Object bakedquad : p_duplicateQuadList_0_) {
            final BakedQuad bakedquad2 = duplicateQuad((BakedQuad)bakedquad);
            list.add(bakedquad2);
        }
        return list;
    }
    
    public static BakedQuad duplicateQuad(final BakedQuad p_duplicateQuad_0_) {
        final BakedQuad bakedquad = new BakedQuad(p_duplicateQuad_0_.getVertexData().clone(), p_duplicateQuad_0_.getTintIndex(), p_duplicateQuad_0_.getFace(), p_duplicateQuad_0_.getSprite());
        return bakedquad;
    }
}
