package optifine;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

public class BlockModelUtils {
    public static IBakedModel makeModelCube(String paramString, int paramInt) {
        TextureAtlasSprite localTextureAtlasSprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(paramString);
        return makeModelCube(localTextureAtlasSprite, paramInt);
    }

    public static IBakedModel makeModelCube(TextureAtlasSprite paramTextureAtlasSprite, int paramInt) {
        ArrayList localArrayList1 = new ArrayList();
        EnumFacing[] arrayOfEnumFacing = EnumFacing.VALUES;
        ArrayList localArrayList2 = new ArrayList(arrayOfEnumFacing.length);
        for (int i = 0; i < arrayOfEnumFacing.length; i++) {
            EnumFacing localEnumFacing = arrayOfEnumFacing[i];
            ArrayList localArrayList3 = new ArrayList();
            localArrayList3.add(makeBakedQuad(localEnumFacing, paramTextureAtlasSprite, paramInt));
            localArrayList2.add(localArrayList3);
        }
        SimpleBakedModel localSimpleBakedModel = new SimpleBakedModel(localArrayList1, localArrayList2, true, true, paramTextureAtlasSprite, ItemCameraTransforms.DEFAULT);
        return localSimpleBakedModel;
    }

    private static BakedQuad makeBakedQuad(EnumFacing paramEnumFacing, TextureAtlasSprite paramTextureAtlasSprite, int paramInt) {
        Vector3f localVector3f1 = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f localVector3f2 = new Vector3f(16.0F, 16.0F, 16.0F);
        BlockFaceUV localBlockFaceUV = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace localBlockPartFace = new BlockPartFace(paramEnumFacing, paramInt, "#" + paramEnumFacing.getName(), localBlockFaceUV);
        ModelRotation localModelRotation = ModelRotation.X0_Y0;
        BlockPartRotation localBlockPartRotation = null;
        boolean bool1 = false;
        boolean bool2 = true;
        FaceBakery localFaceBakery = new FaceBakery();
        BakedQuad localBakedQuad = localFaceBakery.makeBakedQuad(localVector3f1, localVector3f2, localBlockPartFace, paramTextureAtlasSprite, paramEnumFacing, localModelRotation, localBlockPartRotation, bool1, bool2);
        return localBakedQuad;
    }
}




