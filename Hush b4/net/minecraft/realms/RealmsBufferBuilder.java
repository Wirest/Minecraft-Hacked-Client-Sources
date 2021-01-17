// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormat;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.WorldRenderer;

public class RealmsBufferBuilder
{
    private WorldRenderer b;
    
    public RealmsBufferBuilder(final WorldRenderer p_i46442_1_) {
        this.b = p_i46442_1_;
    }
    
    public RealmsBufferBuilder from(final WorldRenderer p_from_1_) {
        this.b = p_from_1_;
        return this;
    }
    
    public void sortQuads(final float p_sortQuads_1_, final float p_sortQuads_2_, final float p_sortQuads_3_) {
        this.b.func_181674_a(p_sortQuads_1_, p_sortQuads_2_, p_sortQuads_3_);
    }
    
    public void fixupQuadColor(final int p_fixupQuadColor_1_) {
        this.b.putColor4(p_fixupQuadColor_1_);
    }
    
    public ByteBuffer getBuffer() {
        return this.b.getByteBuffer();
    }
    
    public void postNormal(final float p_postNormal_1_, final float p_postNormal_2_, final float p_postNormal_3_) {
        this.b.putNormal(p_postNormal_1_, p_postNormal_2_, p_postNormal_3_);
    }
    
    public int getDrawMode() {
        return this.b.getDrawMode();
    }
    
    public void offset(final double p_offset_1_, final double p_offset_3_, final double p_offset_5_) {
        this.b.setTranslation(p_offset_1_, p_offset_3_, p_offset_5_);
    }
    
    public void restoreState(final WorldRenderer.State p_restoreState_1_) {
        this.b.setVertexState(p_restoreState_1_);
    }
    
    public void endVertex() {
        this.b.endVertex();
    }
    
    public RealmsBufferBuilder normal(final float p_normal_1_, final float p_normal_2_, final float p_normal_3_) {
        return this.from(this.b.normal(p_normal_1_, p_normal_2_, p_normal_3_));
    }
    
    public void end() {
        this.b.finishDrawing();
    }
    
    public void begin(final int p_begin_1_, final VertexFormat p_begin_2_) {
        this.b.begin(p_begin_1_, p_begin_2_);
    }
    
    public RealmsBufferBuilder color(final int p_color_1_, final int p_color_2_, final int p_color_3_, final int p_color_4_) {
        return this.from(this.b.color(p_color_1_, p_color_2_, p_color_3_, p_color_4_));
    }
    
    public void faceTex2(final int p_faceTex2_1_, final int p_faceTex2_2_, final int p_faceTex2_3_, final int p_faceTex2_4_) {
        this.b.putBrightness4(p_faceTex2_1_, p_faceTex2_2_, p_faceTex2_3_, p_faceTex2_4_);
    }
    
    public void postProcessFacePosition(final double p_postProcessFacePosition_1_, final double p_postProcessFacePosition_3_, final double p_postProcessFacePosition_5_) {
        this.b.putPosition(p_postProcessFacePosition_1_, p_postProcessFacePosition_3_, p_postProcessFacePosition_5_);
    }
    
    public void fixupVertexColor(final float p_fixupVertexColor_1_, final float p_fixupVertexColor_2_, final float p_fixupVertexColor_3_, final int p_fixupVertexColor_4_) {
        this.b.putColorRGB_F(p_fixupVertexColor_1_, p_fixupVertexColor_2_, p_fixupVertexColor_3_, p_fixupVertexColor_4_);
    }
    
    public RealmsBufferBuilder color(final float p_color_1_, final float p_color_2_, final float p_color_3_, final float p_color_4_) {
        return this.from(this.b.color(p_color_1_, p_color_2_, p_color_3_, p_color_4_));
    }
    
    public RealmsVertexFormat getVertexFormat() {
        return new RealmsVertexFormat(this.b.getVertexFormat());
    }
    
    public void faceTint(final float p_faceTint_1_, final float p_faceTint_2_, final float p_faceTint_3_, final int p_faceTint_4_) {
        this.b.putColorMultiplier(p_faceTint_1_, p_faceTint_2_, p_faceTint_3_, p_faceTint_4_);
    }
    
    public RealmsBufferBuilder tex2(final int p_tex2_1_, final int p_tex2_2_) {
        return this.from(this.b.lightmap(p_tex2_1_, p_tex2_2_));
    }
    
    public void putBulkData(final int[] p_putBulkData_1_) {
        this.b.addVertexData(p_putBulkData_1_);
    }
    
    public RealmsBufferBuilder tex(final double p_tex_1_, final double p_tex_3_) {
        return this.from(this.b.tex(p_tex_1_, p_tex_3_));
    }
    
    public int getVertexCount() {
        return this.b.getVertexCount();
    }
    
    public void clear() {
        this.b.reset();
    }
    
    public RealmsBufferBuilder vertex(final double p_vertex_1_, final double p_vertex_3_, final double p_vertex_5_) {
        return this.from(this.b.pos(p_vertex_1_, p_vertex_3_, p_vertex_5_));
    }
    
    public void fixupQuadColor(final float p_fixupQuadColor_1_, final float p_fixupQuadColor_2_, final float p_fixupQuadColor_3_) {
        this.b.putColorRGB_F4(p_fixupQuadColor_1_, p_fixupQuadColor_2_, p_fixupQuadColor_3_);
    }
    
    public void noColor() {
        this.b.markDirty();
    }
}
