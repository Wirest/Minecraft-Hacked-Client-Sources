package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmartLeaves {
    private static IBakedModel modelLeavesCullAcacia = null;
    private static IBakedModel modelLeavesCullBirch = null;
    private static IBakedModel modelLeavesCullDarkOak = null;
    private static IBakedModel modelLeavesCullJungle = null;
    private static IBakedModel modelLeavesCullOak = null;
    private static IBakedModel modelLeavesCullSpruce = null;
    private static List generalQuadsCullAcacia = null;
    private static List generalQuadsCullBirch = null;
    private static List generalQuadsCullDarkOak = null;
    private static List generalQuadsCullJungle = null;
    private static List generalQuadsCullOak = null;
    private static List generalQuadsCullSpruce = null;
    private static IBakedModel modelLeavesDoubleAcacia = null;
    private static IBakedModel modelLeavesDoubleBirch = null;
    private static IBakedModel modelLeavesDoubleDarkOak = null;
    private static IBakedModel modelLeavesDoubleJungle = null;
    private static IBakedModel modelLeavesDoubleOak = null;
    private static IBakedModel modelLeavesDoubleSpruce = null;

    public static IBakedModel getLeavesModel(IBakedModel paramIBakedModel) {
        if (!Config.isTreesSmart()) {
            return paramIBakedModel;
        }
        List localList = paramIBakedModel.getGeneralQuads();
        return localList == generalQuadsCullSpruce ? modelLeavesDoubleSpruce : localList == generalQuadsCullOak ? modelLeavesDoubleOak : localList == generalQuadsCullJungle ? modelLeavesDoubleJungle : localList == generalQuadsCullDarkOak ? modelLeavesDoubleDarkOak : localList == generalQuadsCullBirch ? modelLeavesDoubleBirch : localList == generalQuadsCullAcacia ? modelLeavesDoubleAcacia : paramIBakedModel;
    }

    public static void updateLeavesModels() {
        ArrayList localArrayList = new ArrayList();
        modelLeavesCullAcacia = getModelCull("acacia", localArrayList);
        modelLeavesCullBirch = getModelCull("birch", localArrayList);
        modelLeavesCullDarkOak = getModelCull("dark_oak", localArrayList);
        modelLeavesCullJungle = getModelCull("jungle", localArrayList);
        modelLeavesCullOak = getModelCull("oak", localArrayList);
        modelLeavesCullSpruce = getModelCull("spruce", localArrayList);
        generalQuadsCullAcacia = getGeneralQuadsSafe(modelLeavesCullAcacia);
        generalQuadsCullBirch = getGeneralQuadsSafe(modelLeavesCullBirch);
        generalQuadsCullDarkOak = getGeneralQuadsSafe(modelLeavesCullDarkOak);
        generalQuadsCullJungle = getGeneralQuadsSafe(modelLeavesCullJungle);
        generalQuadsCullOak = getGeneralQuadsSafe(modelLeavesCullOak);
        generalQuadsCullSpruce = getGeneralQuadsSafe(modelLeavesCullSpruce);
        modelLeavesDoubleAcacia = getModelDoubleFace(modelLeavesCullAcacia);
        modelLeavesDoubleBirch = getModelDoubleFace(modelLeavesCullBirch);
        modelLeavesDoubleDarkOak = getModelDoubleFace(modelLeavesCullDarkOak);
        modelLeavesDoubleJungle = getModelDoubleFace(modelLeavesCullJungle);
        modelLeavesDoubleOak = getModelDoubleFace(modelLeavesCullOak);
        modelLeavesDoubleSpruce = getModelDoubleFace(modelLeavesCullSpruce);
        if (localArrayList.size() > 0) {
            Config.dbg("Enable face culling: " + Config.arrayToString(localArrayList.toArray()));
        }
    }

    private static List getGeneralQuadsSafe(IBakedModel paramIBakedModel) {
        return paramIBakedModel == null ? null : paramIBakedModel.getGeneralQuads();
    }

    static IBakedModel getModelCull(String paramString, List paramList) {
        ModelManager localModelManager = Config.getModelManager();
        if (localModelManager == null) {
            return null;
        }
        ResourceLocation localResourceLocation1 = new ResourceLocation("blockstates/" + paramString + "_leaves.json");
        if (Config.getDefiningResourcePack(localResourceLocation1) != Config.getDefaultResourcePack()) {
            return null;
        }
        ResourceLocation localResourceLocation2 = new ResourceLocation("models/block/" + paramString + "_leaves.json");
        if (Config.getDefiningResourcePack(localResourceLocation2) != Config.getDefaultResourcePack()) {
            return null;
        }
        ModelResourceLocation localModelResourceLocation = new ModelResourceLocation(paramString + "_leaves", "normal");
        IBakedModel localIBakedModel = localModelManager.getModel(localModelResourceLocation);
        if ((localIBakedModel != null) && (localIBakedModel != localModelManager.getMissingModel())) {
            List localList1 = localIBakedModel.getGeneralQuads();
            if (localList1.size() == 0) {
                return localIBakedModel;
            }
            if (localList1.size() != 6) {
                return null;
            }
            Iterator localIterator = localList1.iterator();
            while (localIterator.hasNext()) {
                Object localObject = localIterator.next();
                List localList2 = localIBakedModel.getFaceQuads(((BakedQuad) localObject).getFace());
                if (localList2.size() > 0) {
                    return null;
                }
                localList2.add(localObject);
            }
            localList1.clear();
            paramList.add(paramString + "_leaves");
            return localIBakedModel;
        }
        return null;
    }

    private static IBakedModel getModelDoubleFace(IBakedModel paramIBakedModel) {
        if (paramIBakedModel == null) {
            return null;
        }
        if (paramIBakedModel.getGeneralQuads().size() > 0) {
            Config.warn("SmartLeaves: Model is not cube, general quads: " + paramIBakedModel.getGeneralQuads().size() + ", model: " + paramIBakedModel);
            return paramIBakedModel;
        }
        EnumFacing[] arrayOfEnumFacing = EnumFacing.VALUES;
        for (int i = 0; i < arrayOfEnumFacing.length; i++) {
            localObject = arrayOfEnumFacing[i];
            List localList1 = paramIBakedModel.getFaceQuads((EnumFacing) localObject);
            if (localList1.size() != 1) {
                Config.warn("SmartLeaves: Model is not cube, side: " + localObject + ", quads: " + localList1.size() + ", model: " + paramIBakedModel);
                return paramIBakedModel;
            }
        }
        IBakedModel localIBakedModel = ModelUtils.duplicateModel(paramIBakedModel);
        Object localObject = new List[arrayOfEnumFacing.length];
        int j = 0;
        EnumFacing localEnumFacing = arrayOfEnumFacing[j];
        List localList2 = localIBakedModel.getFaceQuads(localEnumFacing);
        BakedQuad localBakedQuad1 = (BakedQuad) localList2.get(0);
        BakedQuad localBakedQuad2 = new BakedQuad((int[]) localBakedQuad1.getVertexData().clone(), localBakedQuad1.getTintIndex(), localBakedQuad1.getFace(), localBakedQuad1.getSprite());
        int[] arrayOfInt1 = localBakedQuad2.getVertexData();
        int[] arrayOfInt2 = (int[]) arrayOfInt1.clone();
        int k = -4;
        System.arraycopy(arrayOfInt1, 0 * k, arrayOfInt2, 3 * k, k);
        System.arraycopy(arrayOfInt1, 1 * k, arrayOfInt2, 2 * k, k);
        System.arraycopy(arrayOfInt1, 2 * k, arrayOfInt2, 1 * k, k);
        System.arraycopy(arrayOfInt1, 3 * k, arrayOfInt2, 0 * k, k);
        System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, arrayOfInt2.length);
        localList2.add(localBakedQuad2);
        return localIBakedModel;
    }
}




