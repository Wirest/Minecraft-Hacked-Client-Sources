// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.model.IBakedModel;

public class SmartLeaves
{
    private static IBakedModel modelLeavesCullAcacia;
    private static IBakedModel modelLeavesCullBirch;
    private static IBakedModel modelLeavesCullDarkOak;
    private static IBakedModel modelLeavesCullJungle;
    private static IBakedModel modelLeavesCullOak;
    private static IBakedModel modelLeavesCullSpruce;
    private static List generalQuadsCullAcacia;
    private static List generalQuadsCullBirch;
    private static List generalQuadsCullDarkOak;
    private static List generalQuadsCullJungle;
    private static List generalQuadsCullOak;
    private static List generalQuadsCullSpruce;
    private static IBakedModel modelLeavesDoubleAcacia;
    private static IBakedModel modelLeavesDoubleBirch;
    private static IBakedModel modelLeavesDoubleDarkOak;
    private static IBakedModel modelLeavesDoubleJungle;
    private static IBakedModel modelLeavesDoubleOak;
    private static IBakedModel modelLeavesDoubleSpruce;
    
    static {
        SmartLeaves.modelLeavesCullAcacia = null;
        SmartLeaves.modelLeavesCullBirch = null;
        SmartLeaves.modelLeavesCullDarkOak = null;
        SmartLeaves.modelLeavesCullJungle = null;
        SmartLeaves.modelLeavesCullOak = null;
        SmartLeaves.modelLeavesCullSpruce = null;
        SmartLeaves.generalQuadsCullAcacia = null;
        SmartLeaves.generalQuadsCullBirch = null;
        SmartLeaves.generalQuadsCullDarkOak = null;
        SmartLeaves.generalQuadsCullJungle = null;
        SmartLeaves.generalQuadsCullOak = null;
        SmartLeaves.generalQuadsCullSpruce = null;
        SmartLeaves.modelLeavesDoubleAcacia = null;
        SmartLeaves.modelLeavesDoubleBirch = null;
        SmartLeaves.modelLeavesDoubleDarkOak = null;
        SmartLeaves.modelLeavesDoubleJungle = null;
        SmartLeaves.modelLeavesDoubleOak = null;
        SmartLeaves.modelLeavesDoubleSpruce = null;
    }
    
    public static IBakedModel getLeavesModel(final IBakedModel p_getLeavesModel_0_) {
        if (!Config.isTreesSmart()) {
            return p_getLeavesModel_0_;
        }
        final List list = p_getLeavesModel_0_.getGeneralQuads();
        return (list == SmartLeaves.generalQuadsCullAcacia) ? SmartLeaves.modelLeavesDoubleAcacia : ((list == SmartLeaves.generalQuadsCullBirch) ? SmartLeaves.modelLeavesDoubleBirch : ((list == SmartLeaves.generalQuadsCullDarkOak) ? SmartLeaves.modelLeavesDoubleDarkOak : ((list == SmartLeaves.generalQuadsCullJungle) ? SmartLeaves.modelLeavesDoubleJungle : ((list == SmartLeaves.generalQuadsCullOak) ? SmartLeaves.modelLeavesDoubleOak : ((list == SmartLeaves.generalQuadsCullSpruce) ? SmartLeaves.modelLeavesDoubleSpruce : p_getLeavesModel_0_)))));
    }
    
    public static void updateLeavesModels() {
        final List list = new ArrayList();
        SmartLeaves.modelLeavesCullAcacia = getModelCull("acacia", list);
        SmartLeaves.modelLeavesCullBirch = getModelCull("birch", list);
        SmartLeaves.modelLeavesCullDarkOak = getModelCull("dark_oak", list);
        SmartLeaves.modelLeavesCullJungle = getModelCull("jungle", list);
        SmartLeaves.modelLeavesCullOak = getModelCull("oak", list);
        SmartLeaves.modelLeavesCullSpruce = getModelCull("spruce", list);
        SmartLeaves.generalQuadsCullAcacia = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullAcacia);
        SmartLeaves.generalQuadsCullBirch = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullBirch);
        SmartLeaves.generalQuadsCullDarkOak = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullDarkOak);
        SmartLeaves.generalQuadsCullJungle = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullJungle);
        SmartLeaves.generalQuadsCullOak = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullOak);
        SmartLeaves.generalQuadsCullSpruce = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullSpruce);
        SmartLeaves.modelLeavesDoubleAcacia = getModelDoubleFace(SmartLeaves.modelLeavesCullAcacia);
        SmartLeaves.modelLeavesDoubleBirch = getModelDoubleFace(SmartLeaves.modelLeavesCullBirch);
        SmartLeaves.modelLeavesDoubleDarkOak = getModelDoubleFace(SmartLeaves.modelLeavesCullDarkOak);
        SmartLeaves.modelLeavesDoubleJungle = getModelDoubleFace(SmartLeaves.modelLeavesCullJungle);
        SmartLeaves.modelLeavesDoubleOak = getModelDoubleFace(SmartLeaves.modelLeavesCullOak);
        SmartLeaves.modelLeavesDoubleSpruce = getModelDoubleFace(SmartLeaves.modelLeavesCullSpruce);
        if (list.size() > 0) {
            Config.dbg("Enable face culling: " + Config.arrayToString(list.toArray()));
        }
    }
    
    private static List getGeneralQuadsSafe(final IBakedModel p_getGeneralQuadsSafe_0_) {
        return (p_getGeneralQuadsSafe_0_ == null) ? null : p_getGeneralQuadsSafe_0_.getGeneralQuads();
    }
    
    static IBakedModel getModelCull(final String p_getModelCull_0_, final List p_getModelCull_1_) {
        final ModelManager modelmanager = Config.getModelManager();
        if (modelmanager == null) {
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation("blockstates/" + p_getModelCull_0_ + "_leaves.json");
        if (Config.getDefiningResourcePack(resourcelocation) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ResourceLocation resourcelocation2 = new ResourceLocation("models/block/" + p_getModelCull_0_ + "_leaves.json");
        if (Config.getDefiningResourcePack(resourcelocation2) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(String.valueOf(p_getModelCull_0_) + "_leaves", "normal");
        final IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
        if (ibakedmodel == null || ibakedmodel == modelmanager.getMissingModel()) {
            return null;
        }
        final List list = ibakedmodel.getGeneralQuads();
        if (list.size() == 0) {
            return ibakedmodel;
        }
        if (list.size() != 6) {
            return null;
        }
        for (final Object bakedquad : list) {
            final List list2 = ibakedmodel.getFaceQuads(((BakedQuad)bakedquad).getFace());
            if (list2.size() > 0) {
                return null;
            }
            list2.add(bakedquad);
        }
        list.clear();
        p_getModelCull_1_.add(String.valueOf(p_getModelCull_0_) + "_leaves");
        return ibakedmodel;
    }
    
    private static IBakedModel getModelDoubleFace(final IBakedModel p_getModelDoubleFace_0_) {
        if (p_getModelDoubleFace_0_ == null) {
            return null;
        }
        if (p_getModelDoubleFace_0_.getGeneralQuads().size() > 0) {
            Config.warn("SmartLeaves: Model is not cube, general quads: " + p_getModelDoubleFace_0_.getGeneralQuads().size() + ", model: " + p_getModelDoubleFace_0_);
            return p_getModelDoubleFace_0_;
        }
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List<BakedQuad> list = p_getModelDoubleFace_0_.getFaceQuads(enumfacing);
            if (list.size() != 1) {
                Config.warn("SmartLeaves: Model is not cube, side: " + enumfacing + ", quads: " + list.size() + ", model: " + p_getModelDoubleFace_0_);
                return p_getModelDoubleFace_0_;
            }
        }
        final IBakedModel ibakedmodel = ModelUtils.duplicateModel(p_getModelDoubleFace_0_);
        final List[] alist = new List[aenumfacing.length];
        for (int k = 0; k < aenumfacing.length; ++k) {
            final EnumFacing enumfacing2 = aenumfacing[k];
            final List<BakedQuad> list2 = ibakedmodel.getFaceQuads(enumfacing2);
            final BakedQuad bakedquad = list2.get(0);
            final BakedQuad bakedquad2 = new BakedQuad(bakedquad.getVertexData().clone(), bakedquad.getTintIndex(), bakedquad.getFace(), bakedquad.getSprite());
            final int[] aint = bakedquad2.getVertexData();
            final int[] aint2 = aint.clone();
            final int j = aint.length / 4;
            System.arraycopy(aint, 0 * j, aint2, 3 * j, j);
            System.arraycopy(aint, 1 * j, aint2, 2 * j, j);
            System.arraycopy(aint, 2 * j, aint2, 1 * j, j);
            System.arraycopy(aint, 3 * j, aint2, 0 * j, j);
            System.arraycopy(aint2, 0, aint, 0, aint2.length);
            list2.add(bakedquad2);
        }
        return ibakedmodel;
    }
}
