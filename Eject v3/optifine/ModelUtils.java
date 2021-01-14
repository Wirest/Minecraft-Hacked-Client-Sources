package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelUtils {
    public static void dbgModel(IBakedModel paramIBakedModel) {
        if (paramIBakedModel != null) {
            Config.dbg("Model: " + paramIBakedModel + ", ao: " + paramIBakedModel.isAmbientOcclusion() + ", gui3d: " + paramIBakedModel.isGui3d() + ", builtIn: " + paramIBakedModel.isBuiltInRenderer() + ", particle: " + paramIBakedModel.getParticleTexture());
            EnumFacing[] arrayOfEnumFacing = EnumFacing.VALUES;
            for (int i = 0; i < arrayOfEnumFacing.length; i++) {
                EnumFacing localEnumFacing = arrayOfEnumFacing[i];
                List localList2 = paramIBakedModel.getFaceQuads(localEnumFacing);
                dbgQuads(localEnumFacing.getName(), localList2, "  ");
            }
            List localList1 = paramIBakedModel.getGeneralQuads();
            dbgQuads("General", localList1, "  ");
        }
    }

    private static void dbgQuads(String paramString1, List paramList, String paramString2) {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            dbgQuad(paramString1, (BakedQuad) localObject, paramString2);
        }
    }

    public static void dbgQuad(String paramString1, BakedQuad paramBakedQuad, String paramString2) {
        Config.dbg(paramString2 + "Quad: " + paramBakedQuad.getClass().getName() + ", type: " + paramString1 + ", face: " + paramBakedQuad.getFace() + ", tint: " + paramBakedQuad.getTintIndex() + ", sprite: " + paramBakedQuad.getSprite());
        dbgVertexData(paramBakedQuad.getVertexData(), "  " + paramString2);
    }

    public static void dbgVertexData(int[] paramArrayOfInt, String paramString) {
        int i = -4;
        Config.dbg(paramString + "Length: " + paramArrayOfInt.length + ", step: " + i);
        int j = 0;
        int k = j * i;
        float f1 = Float.intBitsToFloat(paramArrayOfInt[(k | 0x0)]);
        float f2 = Float.intBitsToFloat(paramArrayOfInt[(k | 0x1)]);
        float f3 = Float.intBitsToFloat(paramArrayOfInt[(k | 0x2)]);
        int m = paramArrayOfInt[(k | 0x3)];
        float f4 = Float.intBitsToFloat(paramArrayOfInt[(k | 0x4)]);
        float f5 = Float.intBitsToFloat(paramArrayOfInt[(k | 0x5)]);
        Config.dbg(paramString + j + " xyz: " + f1 + "," + f2 + "," + f3 + " col: " + m + " u,v: " + f4 + "," + f5);
    }

    public static IBakedModel duplicateModel(IBakedModel paramIBakedModel) {
        List localList1 = duplicateQuadList(paramIBakedModel.getGeneralQuads());
        EnumFacing[] arrayOfEnumFacing = EnumFacing.VALUES;
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < arrayOfEnumFacing.length; i++) {
            EnumFacing localEnumFacing = arrayOfEnumFacing[i];
            List localList2 = paramIBakedModel.getFaceQuads(localEnumFacing);
            List localList3 = duplicateQuadList(localList2);
            localArrayList.add(localList3);
        }
        SimpleBakedModel localSimpleBakedModel = new SimpleBakedModel(localList1, localArrayList, paramIBakedModel.isAmbientOcclusion(), paramIBakedModel.isGui3d(), paramIBakedModel.getParticleTexture(), paramIBakedModel.getItemCameraTransforms());
        return localSimpleBakedModel;
    }

    public static List duplicateQuadList(List paramList) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            BakedQuad localBakedQuad = duplicateQuad((BakedQuad) localObject);
            localArrayList.add(localBakedQuad);
        }
        return localArrayList;
    }

    public static BakedQuad duplicateQuad(BakedQuad paramBakedQuad) {
        BakedQuad localBakedQuad = new BakedQuad((int[]) paramBakedQuad.getVertexData().clone(), paramBakedQuad.getTintIndex(), paramBakedQuad.getFace(), paramBakedQuad.getSprite());
        return localBakedQuad;
    }
}




