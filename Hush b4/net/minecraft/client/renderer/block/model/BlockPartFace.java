// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.EnumFacing;

public class BlockPartFace
{
    public static final EnumFacing FACING_DEFAULT;
    public final EnumFacing cullFace;
    public final int tintIndex;
    public final String texture;
    public final BlockFaceUV blockFaceUV;
    
    static {
        FACING_DEFAULT = null;
    }
    
    public BlockPartFace(final EnumFacing cullFaceIn, final int tintIndexIn, final String textureIn, final BlockFaceUV blockFaceUVIn) {
        this.cullFace = cullFaceIn;
        this.tintIndex = tintIndexIn;
        this.texture = textureIn;
        this.blockFaceUV = blockFaceUVIn;
    }
    
    static class Deserializer implements JsonDeserializer<BlockPartFace>
    {
        @Override
        public BlockPartFace deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final EnumFacing enumfacing = this.parseCullFace(jsonobject);
            final int i = this.parseTintIndex(jsonobject);
            final String s = this.parseTexture(jsonobject);
            final BlockFaceUV blockfaceuv = p_deserialize_3_.deserialize(jsonobject, BlockFaceUV.class);
            return new BlockPartFace(enumfacing, i, s, blockfaceuv);
        }
        
        protected int parseTintIndex(final JsonObject p_178337_1_) {
            return JsonUtils.getInt(p_178337_1_, "tintindex", -1);
        }
        
        private String parseTexture(final JsonObject p_178340_1_) {
            return JsonUtils.getString(p_178340_1_, "texture");
        }
        
        private EnumFacing parseCullFace(final JsonObject p_178339_1_) {
            final String s = JsonUtils.getString(p_178339_1_, "cullface", "");
            return EnumFacing.byName(s);
        }
    }
}
