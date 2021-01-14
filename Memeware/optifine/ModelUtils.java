package optifine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;

public class ModelUtils {
    public static void dbgModel(IBakedModel model) {
        if (model != null) {
            Config.dbg("Model: " + model + ", ao: " + model.isGui3d() + ", gui3d: " + model.isAmbientOcclusionEnabled() + ", builtIn: " + model.isBuiltInRenderer() + ", particle: " + model.getTexture());
            EnumFacing[] faces = EnumFacing.VALUES;

            for (int generalQuads = 0; generalQuads < faces.length; ++generalQuads) {
                EnumFacing face = faces[generalQuads];
                List faceQuads = model.func_177551_a(face);
                dbgQuads(face.getName(), faceQuads, "  ");
            }

            List var5 = model.func_177550_a();
            dbgQuads("General", var5, "  ");
        }
    }

    private static void dbgQuads(String name, List quads, String prefix) {
        Iterator it = quads.iterator();

        while (it.hasNext()) {
            BakedQuad quad = (BakedQuad) it.next();
            dbgQuad(name, quad, prefix);
        }
    }

    public static void dbgQuad(String name, BakedQuad quad, String prefix) {
        Config.dbg(prefix + "Quad: " + quad.getClass().getName() + ", type: " + name + ", face: " + quad.getFace() + ", tint: " + quad.func_178211_c() + ", sprite: " + quad.getSprite());
        dbgVertexData(quad.func_178209_a(), "  " + prefix);
    }

    public static void dbgVertexData(int[] vd, String prefix) {
        int step = vd.length / 4;
        Config.dbg(prefix + "Length: " + vd.length + ", step: " + step);

        for (int i = 0; i < 4; ++i) {
            int pos = i * step;
            float x = Float.intBitsToFloat(vd[pos + 0]);
            float y = Float.intBitsToFloat(vd[pos + 1]);
            float z = Float.intBitsToFloat(vd[pos + 2]);
            int col = vd[pos + 3];
            float u = Float.intBitsToFloat(vd[pos + 4]);
            float v = Float.intBitsToFloat(vd[pos + 5]);
            Config.dbg(prefix + i + " xyz: " + x + "," + y + "," + z + " col: " + col + " u,v: " + u + "," + v);
        }
    }

    public static IBakedModel duplicateModel(IBakedModel model) {
        List generalQuads2 = duplicateQuadList(model.func_177550_a());
        EnumFacing[] faces = EnumFacing.VALUES;
        ArrayList faceQuads2 = new ArrayList();

        for (int model2 = 0; model2 < faces.length; ++model2) {
            EnumFacing face = faces[model2];
            List quads = model.func_177551_a(face);
            List quads2 = duplicateQuadList(quads);
            faceQuads2.add(quads2);
        }

        SimpleBakedModel var8 = new SimpleBakedModel(generalQuads2, faceQuads2, model.isGui3d(), model.isAmbientOcclusionEnabled(), model.getTexture(), model.getItemCameraTransforms());
        return var8;
    }

    public static List duplicateQuadList(List list) {
        ArrayList list2 = new ArrayList();
        Iterator it = list.iterator();

        while (it.hasNext()) {
            BakedQuad quad = (BakedQuad) it.next();
            BakedQuad quad2 = duplicateQuad(quad);
            list2.add(quad2);
        }

        return list2;
    }

    public static BakedQuad duplicateQuad(BakedQuad quad) {
        BakedQuad quad2 = new BakedQuad((int[]) quad.func_178209_a().clone(), quad.func_178211_c(), quad.getFace(), quad.getSprite());
        return quad2;
    }
}
