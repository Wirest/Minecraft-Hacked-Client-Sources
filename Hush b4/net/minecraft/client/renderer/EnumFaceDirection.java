// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection
{
    DOWN("DOWN", 0, new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null) }), 
    UP("UP", 1, new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }), 
    NORTH("NORTH", 2, new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }), 
    SOUTH("SOUTH", 3, new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }), 
    WEST("WEST", 4, new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }), 
    EAST("EAST", 5, new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) });
    
    private static final EnumFaceDirection[] facings;
    private final VertexInformation[] vertexInfos;
    
    static {
        (facings = new EnumFaceDirection[6])[Constants.DOWN_INDEX] = EnumFaceDirection.DOWN;
        EnumFaceDirection.facings[Constants.UP_INDEX] = EnumFaceDirection.UP;
        EnumFaceDirection.facings[Constants.NORTH_INDEX] = EnumFaceDirection.NORTH;
        EnumFaceDirection.facings[Constants.SOUTH_INDEX] = EnumFaceDirection.SOUTH;
        EnumFaceDirection.facings[Constants.WEST_INDEX] = EnumFaceDirection.WEST;
        EnumFaceDirection.facings[Constants.EAST_INDEX] = EnumFaceDirection.EAST;
    }
    
    public static EnumFaceDirection getFacing(final EnumFacing facing) {
        return EnumFaceDirection.facings[facing.getIndex()];
    }
    
    private EnumFaceDirection(final String name, final int ordinal, final VertexInformation[] vertexInfosIn) {
        this.vertexInfos = vertexInfosIn;
    }
    
    public VertexInformation func_179025_a(final int p_179025_1_) {
        return this.vertexInfos[p_179025_1_];
    }
    
    public static final class Constants
    {
        public static final int SOUTH_INDEX;
        public static final int UP_INDEX;
        public static final int EAST_INDEX;
        public static final int NORTH_INDEX;
        public static final int DOWN_INDEX;
        public static final int WEST_INDEX;
        
        static {
            SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
            UP_INDEX = EnumFacing.UP.getIndex();
            EAST_INDEX = EnumFacing.EAST.getIndex();
            NORTH_INDEX = EnumFacing.NORTH.getIndex();
            DOWN_INDEX = EnumFacing.DOWN.getIndex();
            WEST_INDEX = EnumFacing.WEST.getIndex();
        }
    }
    
    public static class VertexInformation
    {
        public final int field_179184_a;
        public final int field_179182_b;
        public final int field_179183_c;
        
        private VertexInformation(final int p_i46270_1_, final int p_i46270_2_, final int p_i46270_3_) {
            this.field_179184_a = p_i46270_1_;
            this.field_179182_b = p_i46270_2_;
            this.field_179183_c = p_i46270_3_;
        }
    }
}
