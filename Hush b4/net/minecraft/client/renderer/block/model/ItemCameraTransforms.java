// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.client.renderer.GlStateManager;

public class ItemCameraTransforms
{
    public static final ItemCameraTransforms DEFAULT;
    public static float field_181690_b;
    public static float field_181691_c;
    public static float field_181692_d;
    public static float field_181693_e;
    public static float field_181694_f;
    public static float field_181695_g;
    public static float field_181696_h;
    public static float field_181697_i;
    public static float field_181698_j;
    public final ItemTransformVec3f thirdPerson;
    public final ItemTransformVec3f firstPerson;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;
    public final ItemTransformVec3f ground;
    public final ItemTransformVec3f fixed;
    
    static {
        DEFAULT = new ItemCameraTransforms();
        ItemCameraTransforms.field_181690_b = 0.0f;
        ItemCameraTransforms.field_181691_c = 0.0f;
        ItemCameraTransforms.field_181692_d = 0.0f;
        ItemCameraTransforms.field_181693_e = 0.0f;
        ItemCameraTransforms.field_181694_f = 0.0f;
        ItemCameraTransforms.field_181695_g = 0.0f;
        ItemCameraTransforms.field_181696_h = 0.0f;
        ItemCameraTransforms.field_181697_i = 0.0f;
        ItemCameraTransforms.field_181698_j = 0.0f;
    }
    
    private ItemCameraTransforms() {
        this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }
    
    public ItemCameraTransforms(final ItemCameraTransforms p_i46443_1_) {
        this.thirdPerson = p_i46443_1_.thirdPerson;
        this.firstPerson = p_i46443_1_.firstPerson;
        this.head = p_i46443_1_.head;
        this.gui = p_i46443_1_.gui;
        this.ground = p_i46443_1_.ground;
        this.fixed = p_i46443_1_.fixed;
    }
    
    public ItemCameraTransforms(final ItemTransformVec3f p_i46444_1_, final ItemTransformVec3f p_i46444_2_, final ItemTransformVec3f p_i46444_3_, final ItemTransformVec3f p_i46444_4_, final ItemTransformVec3f p_i46444_5_, final ItemTransformVec3f p_i46444_6_) {
        this.thirdPerson = p_i46444_1_;
        this.firstPerson = p_i46444_2_;
        this.head = p_i46444_3_;
        this.gui = p_i46444_4_;
        this.ground = p_i46444_5_;
        this.fixed = p_i46444_6_;
    }
    
    public void applyTransform(final TransformType p_181689_1_) {
        final ItemTransformVec3f itemtransformvec3f = this.getTransform(p_181689_1_);
        if (itemtransformvec3f != ItemTransformVec3f.DEFAULT) {
            GlStateManager.translate(itemtransformvec3f.translation.x + ItemCameraTransforms.field_181690_b, itemtransformvec3f.translation.y + ItemCameraTransforms.field_181691_c, itemtransformvec3f.translation.z + ItemCameraTransforms.field_181692_d);
            GlStateManager.rotate(itemtransformvec3f.rotation.y + ItemCameraTransforms.field_181694_f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(itemtransformvec3f.rotation.x + ItemCameraTransforms.field_181693_e, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(itemtransformvec3f.rotation.z + ItemCameraTransforms.field_181695_g, 0.0f, 0.0f, 1.0f);
            GlStateManager.scale(itemtransformvec3f.scale.x + ItemCameraTransforms.field_181696_h, itemtransformvec3f.scale.y + ItemCameraTransforms.field_181697_i, itemtransformvec3f.scale.z + ItemCameraTransforms.field_181698_j);
        }
    }
    
    public ItemTransformVec3f getTransform(final TransformType p_181688_1_) {
        switch (p_181688_1_) {
            case THIRD_PERSON: {
                return this.thirdPerson;
            }
            case FIRST_PERSON: {
                return this.firstPerson;
            }
            case HEAD: {
                return this.head;
            }
            case GUI: {
                return this.gui;
            }
            case GROUND: {
                return this.ground;
            }
            case FIXED: {
                return this.fixed;
            }
            default: {
                return ItemTransformVec3f.DEFAULT;
            }
        }
    }
    
    public boolean func_181687_c(final TransformType p_181687_1_) {
        return !this.getTransform(p_181687_1_).equals(ItemTransformVec3f.DEFAULT);
    }
    
    static class Deserializer implements JsonDeserializer<ItemCameraTransforms>
    {
        @Override
        public ItemCameraTransforms deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final ItemTransformVec3f itemtransformvec3f = this.func_181683_a(p_deserialize_3_, jsonobject, "thirdperson");
            final ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(p_deserialize_3_, jsonobject, "firstperson");
            final ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(p_deserialize_3_, jsonobject, "head");
            final ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(p_deserialize_3_, jsonobject, "gui");
            final ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(p_deserialize_3_, jsonobject, "ground");
            final ItemTransformVec3f itemtransformvec3f6 = this.func_181683_a(p_deserialize_3_, jsonobject, "fixed");
            return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6);
        }
        
        private ItemTransformVec3f func_181683_a(final JsonDeserializationContext p_181683_1_, final JsonObject p_181683_2_, final String p_181683_3_) {
            return p_181683_2_.has(p_181683_3_) ? p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
        }
    }
    
    public enum TransformType
    {
        NONE("NONE", 0), 
        THIRD_PERSON("THIRD_PERSON", 1), 
        FIRST_PERSON("FIRST_PERSON", 2), 
        HEAD("HEAD", 3), 
        GUI("GUI", 4), 
        GROUND("GROUND", 5), 
        FIXED("FIXED", 6);
        
        private TransformType(final String name, final int ordinal) {
        }
    }
}
