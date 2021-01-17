// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.MathHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class ItemTransformVec3f
{
    public static final ItemTransformVec3f DEFAULT;
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;
    
    static {
        DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    public ItemTransformVec3f(final Vector3f rotation, final Vector3f translation, final Vector3f scale) {
        this.rotation = new Vector3f(rotation);
        this.translation = new Vector3f(translation);
        this.scale = new Vector3f(scale);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (this.getClass() != p_equals_1_.getClass()) {
            return false;
        }
        final ItemTransformVec3f itemtransformvec3f = (ItemTransformVec3f)p_equals_1_;
        return this.rotation.equals(itemtransformvec3f.rotation) && this.scale.equals(itemtransformvec3f.scale) && this.translation.equals(itemtransformvec3f.translation);
    }
    
    @Override
    public int hashCode() {
        int i = this.rotation.hashCode();
        i = 31 * i + this.translation.hashCode();
        i = 31 * i + this.scale.hashCode();
        return i;
    }
    
    static class Deserializer implements JsonDeserializer<ItemTransformVec3f>
    {
        private static final Vector3f ROTATION_DEFAULT;
        private static final Vector3f TRANSLATION_DEFAULT;
        private static final Vector3f SCALE_DEFAULT;
        
        static {
            ROTATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
            TRANSLATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
            SCALE_DEFAULT = new Vector3f(1.0f, 1.0f, 1.0f);
        }
        
        @Override
        public ItemTransformVec3f deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", Deserializer.ROTATION_DEFAULT);
            final Vector3f vector3f2 = this.parseVector3f(jsonobject, "translation", Deserializer.TRANSLATION_DEFAULT);
            vector3f2.scale(0.0625f);
            vector3f2.x = MathHelper.clamp_float(vector3f2.x, -1.5f, 1.5f);
            vector3f2.y = MathHelper.clamp_float(vector3f2.y, -1.5f, 1.5f);
            vector3f2.z = MathHelper.clamp_float(vector3f2.z, -1.5f, 1.5f);
            final Vector3f vector3f3 = this.parseVector3f(jsonobject, "scale", Deserializer.SCALE_DEFAULT);
            vector3f3.x = MathHelper.clamp_float(vector3f3.x, -4.0f, 4.0f);
            vector3f3.y = MathHelper.clamp_float(vector3f3.y, -4.0f, 4.0f);
            vector3f3.z = MathHelper.clamp_float(vector3f3.z, -4.0f, 4.0f);
            return new ItemTransformVec3f(vector3f, vector3f2, vector3f3);
        }
        
        private Vector3f parseVector3f(final JsonObject jsonObject, final String key, final Vector3f defaultValue) {
            if (!jsonObject.has(key)) {
                return defaultValue;
            }
            final JsonArray jsonarray = JsonUtils.getJsonArray(jsonObject, key);
            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
            }
            final float[] afloat = new float[3];
            for (int i = 0; i < afloat.length; ++i) {
                afloat[i] = JsonUtils.getFloat(jsonarray.get(i), String.valueOf(key) + "[" + i + "]");
            }
            return new Vector3f(afloat[0], afloat[1], afloat[2]);
        }
    }
}
