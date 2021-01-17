// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;

class References extends BaseReferences
{
    Buffer ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
    Buffer ARB_vertex_blend_glWeightPointerARB_pPointer;
    Buffer EXT_fog_coord_glFogCoordPointerEXT_data;
    Buffer EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
    Buffer EXT_vertex_shader_glVariantPointerEXT_pAddr;
    Buffer EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
    Buffer GL11_glColorPointer_pointer;
    Buffer GL11_glEdgeFlagPointer_pointer;
    Buffer GL11_glNormalPointer_pointer;
    Buffer GL11_glVertexPointer_pointer;
    Buffer GL14_glFogCoordPointer_data;
    
    References(final ContextCapabilities caps) {
        super(caps);
    }
    
    void copy(final References references, final int mask) {
        super.copy(references, mask);
        if ((mask & 0x2) != 0x0) {
            this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = references.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
            this.ARB_vertex_blend_glWeightPointerARB_pPointer = references.ARB_vertex_blend_glWeightPointerARB_pPointer;
            this.EXT_fog_coord_glFogCoordPointerEXT_data = references.EXT_fog_coord_glFogCoordPointerEXT_data;
            this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = references.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
            this.EXT_vertex_shader_glVariantPointerEXT_pAddr = references.EXT_vertex_shader_glVariantPointerEXT_pAddr;
            this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = references.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
            this.GL11_glColorPointer_pointer = references.GL11_glColorPointer_pointer;
            this.GL11_glEdgeFlagPointer_pointer = references.GL11_glEdgeFlagPointer_pointer;
            this.GL11_glNormalPointer_pointer = references.GL11_glNormalPointer_pointer;
            this.GL11_glVertexPointer_pointer = references.GL11_glVertexPointer_pointer;
            this.GL14_glFogCoordPointer_data = references.GL14_glFogCoordPointer_data;
        }
    }
    
    @Override
    void clear() {
        super.clear();
        this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = null;
        this.ARB_vertex_blend_glWeightPointerARB_pPointer = null;
        this.EXT_fog_coord_glFogCoordPointerEXT_data = null;
        this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = null;
        this.EXT_vertex_shader_glVariantPointerEXT_pAddr = null;
        this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = null;
        this.GL11_glColorPointer_pointer = null;
        this.GL11_glEdgeFlagPointer_pointer = null;
        this.GL11_glNormalPointer_pointer = null;
        this.GL11_glVertexPointer_pointer = null;
        this.GL14_glFogCoordPointer_data = null;
    }
}
