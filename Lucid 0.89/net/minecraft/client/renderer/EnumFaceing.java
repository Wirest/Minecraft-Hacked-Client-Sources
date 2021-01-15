package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceing
{
    DOWN("DOWN", 0, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null)}),
    UP("UP", 1, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null)}),
    NORTH("NORTH", 2, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null)}),
    SOUTH("SOUTH", 3, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null)}),
    WEST("WEST", 4, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.WEST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null)}),
    EAST("EAST", 5, new EnumFaceing.VertexInformation[]{new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.SOUTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.DOWN_INDEX, EnumFaceing.Constants.NORTH_INDEX, null), new EnumFaceing.VertexInformation(EnumFaceing.Constants.EAST_INDEX, EnumFaceing.Constants.UP_INDEX, EnumFaceing.Constants.NORTH_INDEX, null)});
    private static final EnumFaceing[] facings = new EnumFaceing[6];
    private final EnumFaceing.VertexInformation[] vertexInfos; 

    public static EnumFaceing getFacing(EnumFacing facing)
    {
        return facings[facing.getIndex()];
    }

    private EnumFaceing(String p_i46272_1_, int p_i46272_2_, EnumFaceing.VertexInformation ... vertexInfosIn)
    {
        this.vertexInfos = vertexInfosIn;
    }

    public EnumFaceing.VertexInformation func_179025_a(int p_179025_1_)
    {
        return this.vertexInfos[p_179025_1_];
    }

    static {
        facings[EnumFaceing.Constants.DOWN_INDEX] = DOWN;
        facings[EnumFaceing.Constants.UP_INDEX] = UP;
        facings[EnumFaceing.Constants.NORTH_INDEX] = NORTH;
        facings[EnumFaceing.Constants.SOUTH_INDEX] = SOUTH;
        facings[EnumFaceing.Constants.WEST_INDEX] = WEST;
        facings[EnumFaceing.Constants.EAST_INDEX] = EAST;
    }

    public static final class Constants {
        public static final int SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
        public static final int UP_INDEX = EnumFacing.UP.getIndex();
        public static final int EAST_INDEX = EnumFacing.EAST.getIndex();
        public static final int NORTH_INDEX = EnumFacing.NORTH.getIndex();
        public static final int DOWN_INDEX = EnumFacing.DOWN.getIndex();
        public static final int WEST_INDEX = EnumFacing.WEST.getIndex();
    }

    public static class VertexInformation {
        public final int field_179184_a;
        public final int field_179182_b;
        public final int field_179183_c;

        private VertexInformation(int p_i46270_1_, int p_i46270_2_, int p_i46270_3_)
        {
            this.field_179184_a = p_i46270_1_;
            this.field_179182_b = p_i46270_2_;
            this.field_179183_c = p_i46270_3_;
        }

        VertexInformation(int p_i46271_1_, int p_i46271_2_, int p_i46271_3_, Object p_i46271_4_)
        {
            this(p_i46271_1_, p_i46271_2_, p_i46271_3_);
        }
    }
}
