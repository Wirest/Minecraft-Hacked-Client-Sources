// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.profiler;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class Profiler
{
    private static final Logger logger;
    private final List sectionList;
    private final List timestampList;
    public boolean profilingEnabled;
    private String profilingSection;
    private final Map profilingMap;
    private static final String __OBFID = "CL_00001497";
    public boolean profilerGlobalEnabled;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final String RENDER = "render";
    private static final String DISPLAY = "display";
    private static final int HASH_SCHEDULED_EXECUTABLES;
    private static final int HASH_TICK;
    private static final int HASH_PRE_RENDER_ERRORS;
    private static final int HASH_RENDER;
    private static final int HASH_DISPLAY;
    
    static {
        logger = LogManager.getLogger();
        HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
        HASH_TICK = "tick".hashCode();
        HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
        HASH_RENDER = "render".hashCode();
        HASH_DISPLAY = "display".hashCode();
    }
    
    public Profiler() {
        this.sectionList = Lists.newArrayList();
        this.timestampList = Lists.newArrayList();
        this.profilingSection = "";
        this.profilingMap = Maps.newHashMap();
        this.profilerGlobalEnabled = true;
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }
    
    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }
    
    public void startSection(final String name) {
        if (Lagometer.isActive()) {
            final int i = name.hashCode();
            if (i == Profiler.HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            }
            else if (i == Profiler.HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            }
            else if (i == Profiler.HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }
        if (Config.isFastRender()) {
            final int j = name.hashCode();
            if (j == Profiler.HASH_RENDER && name.equals("render")) {
                GlStateManager.clearEnabled = false;
            }
            else if (j == Profiler.HASH_DISPLAY && name.equals("display")) {
                GlStateManager.clearEnabled = true;
            }
        }
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            if (this.profilingSection.length() > 0) {
                this.profilingSection = String.valueOf(this.profilingSection) + ".";
            }
            this.profilingSection = String.valueOf(this.profilingSection) + name;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(System.nanoTime());
        }
    }
    
    public void endSection() {
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            final long i = System.nanoTime();
            final long j = this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            final long k = i - j;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + k);
            }
            else {
                this.profilingMap.put(this.profilingSection, k);
            }
            if (k > 100000000L) {
                Profiler.logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + k / 1000000.0 + " ms");
            }
            this.profilingSection = (this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1));
        }
    }
    
    public List getProfilingData(String p_76321_1_) {
        if (!(this.profilerLocalEnabled = this.profilerGlobalEnabled)) {
            return new ArrayList(Arrays.asList(new Result("root", 0.0, 0.0)));
        }
        if (!this.profilingEnabled) {
            return null;
        }
        long i = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
        final long j = this.profilingMap.containsKey(p_76321_1_) ? this.profilingMap.get(p_76321_1_) : -1L;
        final ArrayList arraylist = Lists.newArrayList();
        if (p_76321_1_.length() > 0) {
            p_76321_1_ = String.valueOf(p_76321_1_) + ".";
        }
        long k = 0L;
        for (final Object s : this.profilingMap.keySet()) {
            if (((String)s).length() > p_76321_1_.length() && ((String)s).startsWith(p_76321_1_) && ((String)s).indexOf(".", p_76321_1_.length() + 1) < 0) {
                k += this.profilingMap.get(s);
            }
        }
        final float f = (float)k;
        if (k < j) {
            k = j;
        }
        if (i < k) {
            i = k;
        }
        for (final Object s2 : this.profilingMap.keySet()) {
            final String s3 = (String)s2;
            if (s3.length() > p_76321_1_.length() && s3.startsWith(p_76321_1_) && s3.indexOf(".", p_76321_1_.length() + 1) < 0) {
                final long l = this.profilingMap.get(s3);
                final double d0 = l * 100.0 / k;
                final double d2 = l * 100.0 / i;
                final String s4 = s3.substring(p_76321_1_.length());
                arraylist.add(new Result(s4, d0, d2));
            }
        }
        for (final Object s5 : this.profilingMap.keySet()) {
            this.profilingMap.put(s5, this.profilingMap.get(s5) * 950L / 1000L);
        }
        if (k > f) {
            arraylist.add(new Result("unspecified", (k - f) * 100.0 / k, (k - f) * 100.0 / i));
        }
        Collections.sort((List<Comparable>)arraylist);
        arraylist.add(0, new Result(p_76321_1_, 100.0, k * 100.0 / i));
        return arraylist;
    }
    
    public void endStartSection(final String name) {
        if (this.profilerLocalEnabled) {
            this.endSection();
            this.startSection(name);
        }
    }
    
    public String getNameOfLastSection() {
        return (this.sectionList.size() == 0) ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
    }
    
    public static final class Result implements Comparable
    {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        private static final String __OBFID = "CL_00001498";
        
        public Result(final String p_i1554_1_, final double p_i1554_2_, final double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }
        
        public int compareTo(final Result p_compareTo_1_) {
            return (p_compareTo_1_.field_76332_a < this.field_76332_a) ? -1 : ((p_compareTo_1_.field_76332_a > this.field_76332_a) ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
        }
        
        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 4473924;
        }
        
        @Override
        public int compareTo(final Object p_compareTo_1_) {
            return this.compareTo((Result)p_compareTo_1_);
        }
    }
}
