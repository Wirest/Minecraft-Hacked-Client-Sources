// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.stream;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.gson.Gson;

public class Metadata
{
    private static final Gson field_152811_a;
    private final String name;
    private String description;
    private Map<String, String> payload;
    
    static {
        field_152811_a = new Gson();
    }
    
    public Metadata(final String p_i46345_1_, final String p_i46345_2_) {
        this.name = p_i46345_1_;
        this.description = p_i46345_2_;
    }
    
    public Metadata(final String p_i1030_1_) {
        this(p_i1030_1_, null);
    }
    
    public void func_152807_a(final String p_152807_1_) {
        this.description = p_152807_1_;
    }
    
    public String func_152809_a() {
        return (this.description == null) ? this.name : this.description;
    }
    
    public void func_152808_a(final String p_152808_1_, final String p_152808_2_) {
        if (this.payload == null) {
            this.payload = (Map<String, String>)Maps.newHashMap();
        }
        if (this.payload.size() > 50) {
            throw new IllegalArgumentException("Metadata payload is full, cannot add more to it!");
        }
        if (p_152808_1_ == null) {
            throw new IllegalArgumentException("Metadata payload key cannot be null!");
        }
        if (p_152808_1_.length() > 255) {
            throw new IllegalArgumentException("Metadata payload key is too long!");
        }
        if (p_152808_2_ == null) {
            throw new IllegalArgumentException("Metadata payload value cannot be null!");
        }
        if (p_152808_2_.length() > 255) {
            throw new IllegalArgumentException("Metadata payload value is too long!");
        }
        this.payload.put(p_152808_1_, p_152808_2_);
    }
    
    public String func_152806_b() {
        return (this.payload != null && !this.payload.isEmpty()) ? Metadata.field_152811_a.toJson(this.payload) : null;
    }
    
    public String func_152810_c() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name", this.name).add("description", this.description).add("data", this.func_152806_b()).toString();
    }
}
