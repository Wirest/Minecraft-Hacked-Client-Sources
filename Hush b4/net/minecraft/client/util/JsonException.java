// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.IOException;

public class JsonException extends IOException
{
    private final List<Entry> field_151383_a;
    private final String field_151382_b;
    
    public JsonException(final String p_i45279_1_) {
        (this.field_151383_a = (List<Entry>)Lists.newArrayList()).add(new Entry(null));
        this.field_151382_b = p_i45279_1_;
    }
    
    public JsonException(final String p_i45280_1_, final Throwable p_i45280_2_) {
        super(p_i45280_2_);
        (this.field_151383_a = (List<Entry>)Lists.newArrayList()).add(new Entry(null));
        this.field_151382_b = p_i45280_1_;
    }
    
    public void func_151380_a(final String p_151380_1_) {
        this.field_151383_a.get(0).func_151373_a(p_151380_1_);
    }
    
    public void func_151381_b(final String p_151381_1_) {
        Entry.access$2(this.field_151383_a.get(0), p_151381_1_);
        this.field_151383_a.add(0, new Entry(null));
    }
    
    @Override
    public String getMessage() {
        return "Invalid " + this.field_151383_a.get(this.field_151383_a.size() - 1).toString() + ": " + this.field_151382_b;
    }
    
    public static JsonException func_151379_a(final Exception p_151379_0_) {
        if (p_151379_0_ instanceof JsonException) {
            return (JsonException)p_151379_0_;
        }
        String s = p_151379_0_.getMessage();
        if (p_151379_0_ instanceof FileNotFoundException) {
            s = "File not found";
        }
        return new JsonException(s, p_151379_0_);
    }
    
    public static class Entry
    {
        private String field_151376_a;
        private final List<String> field_151375_b;
        
        private Entry() {
            this.field_151376_a = null;
            this.field_151375_b = (List<String>)Lists.newArrayList();
        }
        
        private void func_151373_a(final String p_151373_1_) {
            this.field_151375_b.add(0, p_151373_1_);
        }
        
        public String func_151372_b() {
            return StringUtils.join(this.field_151375_b, "->");
        }
        
        @Override
        public String toString() {
            return (this.field_151376_a != null) ? (this.field_151375_b.isEmpty() ? this.field_151376_a : (String.valueOf(this.field_151376_a) + " " + this.func_151372_b())) : (this.field_151375_b.isEmpty() ? "(Unknown file)" : ("(Unknown file) " + this.func_151372_b()));
        }
        
        static /* synthetic */ void access$2(final Entry entry, final String field_151376_a) {
            entry.field_151376_a = field_151376_a;
        }
    }
}
