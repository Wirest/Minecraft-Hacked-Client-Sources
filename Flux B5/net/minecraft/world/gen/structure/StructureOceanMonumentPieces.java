package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class StructureOceanMonumentPieces
{
    private static final String __OBFID = "CL_00001994";

    public static void func_175970_a()
    {
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentBuilding.class, "OMB");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentCoreRoom.class, "OMCR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXRoom.class, "OMDXR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXYRoom.class, "OMDXYR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYRoom.class, "OMDYR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYZRoom.class, "OMDYZR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleZRoom.class, "OMDZR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.EntryRoom.class, "OMEntry");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.Penthouse.class, "OMPenthouse");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleRoom.class, "OMSimple");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleTopRoom.class, "OMSimpleT");
    }

    public static class DoubleXRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001983";

        public DoubleXRoom() {}

        public DoubleXRoom(EnumFacing p_i45597_1_, StructureOceanMonumentPieces.RoomDefinition p_i45597_2_, Random p_i45597_3_)
        {
            super(1, p_i45597_1_, p_i45597_2_, 2, 1, 1);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            StructureOceanMonumentPieces.RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var5 = this.field_175830_k;

            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 8, 0, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (var5.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 7, 4, 6, field_175828_a);
            }

            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 8, 4, 1, 14, 4, 6, field_175828_a);
            }

            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 3, 0, 15, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 15, 3, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 14, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 2, 0, 15, 2, 7, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 15, 2, 0, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 14, 2, 7, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, 1, 0, 15, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 15, 1, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 10, 1, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 2, 0, 9, 2, 3, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 10, 3, 4, field_175826_b, field_175826_b, false);
            this.func_175811_a(worldIn, field_175825_e, 6, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 9, 2, 3, p_74875_3_);

            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 0, 12, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 7, 12, 2, 7, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 3, 15, 2, 4, field_175822_f, field_175822_f, false);
            }

            return true;
        }
    }

    public static class DoubleXYRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001982";

        public DoubleXYRoom() {}

        public DoubleXYRoom(EnumFacing p_i45596_1_, StructureOceanMonumentPieces.RoomDefinition p_i45596_2_, Random p_i45596_3_)
        {
            super(1, p_i45596_1_, p_i45596_2_, 2, 2, 1);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            StructureOceanMonumentPieces.RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var5 = this.field_175830_k;
            StructureOceanMonumentPieces.RoomDefinition var6 = var5.field_175965_b[EnumFacing.UP.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var7 = var4.field_175965_b[EnumFacing.UP.getIndex()];

            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 8, 0, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (var6.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 7, 8, 6, field_175828_a);
            }

            if (var7.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 8, 8, 1, 14, 8, 6, field_175828_a);
            }

            for (int var8 = 1; var8 <= 7; ++var8)
            {
                IBlockState var9 = field_175826_b;

                if (var8 == 2 || var8 == 6)
                {
                    var9 = field_175828_a;
                }

                this.func_175804_a(worldIn, p_74875_3_, 0, var8, 0, 0, var8, 7, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, var8, 0, 15, var8, 7, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 0, 15, var8, 0, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 7, 14, var8, 7, var9, var9, false);
            }

            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 3, 2, 7, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 4, 7, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 5, 4, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 3, 13, 7, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 11, 1, 2, 12, 7, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 11, 1, 5, 12, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 3, 5, 3, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 3, 10, 3, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 2, 10, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 5, 2, 5, 7, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 5, 2, 10, 7, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 5, 5, 5, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 5, 5, 10, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175811_a(worldIn, field_175826_b, 6, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 9, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 6, 6, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 9, 6, 5, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 3, 6, 4, 4, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 4, 3, 10, 4, 4, field_175826_b, field_175826_b, false);
            this.func_175811_a(worldIn, field_175825_e, 5, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 5, 4, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 10, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 10, 4, 5, p_74875_3_);

            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 0, 12, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 1, 7, 12, 2, 7, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 3, 15, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var6.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 0, 4, 6, 0, field_175822_f, field_175822_f, false);
            }

            if (var6.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 7, 4, 6, 7, field_175822_f, field_175822_f, false);
            }

            if (var6.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 3, 0, 6, 4, field_175822_f, field_175822_f, false);
            }

            if (var7.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 5, 0, 12, 6, 0, field_175822_f, field_175822_f, false);
            }

            if (var7.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 11, 5, 7, 12, 6, 7, field_175822_f, field_175822_f, false);
            }

            if (var7.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 15, 5, 3, 15, 6, 4, field_175822_f, field_175822_f, false);
            }

            return true;
        }
    }

    public static class DoubleYRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001981";

        public DoubleYRoom() {}

        public DoubleYRoom(EnumFacing p_i45595_1_, StructureOceanMonumentPieces.RoomDefinition p_i45595_2_, Random p_i45595_3_)
        {
            super(1, p_i45595_1_, p_i45595_2_, 1, 2, 1);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            StructureOceanMonumentPieces.RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()];

            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 6, 8, 6, field_175828_a);
            }

            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 0, 0, 4, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 4, 0, 7, 4, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 0, 6, 4, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 7, 6, 4, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 4, 1, 2, 4, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 2, 1, 4, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 1, 5, 4, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 4, 2, 6, 4, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 4, 5, 2, 4, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 5, 1, 4, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 4, 5, 5, 4, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 4, 5, 6, 4, 5, field_175826_b, field_175826_b, false);
            StructureOceanMonumentPieces.RoomDefinition var5 = this.field_175830_k;

            for (int var6 = 1; var6 <= 5; var6 += 4)
            {
                byte var7 = 0;

                if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 2, var6, var7, 2, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, var6, var7, 5, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, var6 + 2, var7, 4, var6 + 2, var7, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6, var7, 7, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6 + 1, var7, 7, var6 + 1, var7, field_175828_a, field_175828_a, false);
                }

                var7 = 7;

                if (var5.field_175966_c[EnumFacing.NORTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 2, var6, var7, 2, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, var6, var7, 5, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, var6 + 2, var7, 4, var6 + 2, var7, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6, var7, 7, var6 + 2, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, var6 + 1, var7, 7, var6 + 1, var7, field_175828_a, field_175828_a, false);
                }

                byte var8 = 0;

                if (var5.field_175966_c[EnumFacing.WEST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 2, var8, var6 + 2, 2, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 5, var8, var6 + 2, 5, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 2, 3, var8, var6 + 2, 4, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 0, var8, var6 + 2, 7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 1, 0, var8, var6 + 1, 7, field_175828_a, field_175828_a, false);
                }

                var8 = 7;

                if (var5.field_175966_c[EnumFacing.EAST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 2, var8, var6 + 2, 2, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 5, var8, var6 + 2, 5, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 2, 3, var8, var6 + 2, 4, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6, 0, var8, var6 + 2, 7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, var6 + 1, 0, var8, var6 + 1, 7, field_175828_a, field_175828_a, false);
                }

                var5 = var4;
            }

            return true;
        }
    }

    public static class DoubleYZRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001980";

        public DoubleYZRoom() {}

        public DoubleYZRoom(EnumFacing p_i45594_1_, StructureOceanMonumentPieces.RoomDefinition p_i45594_2_, Random p_i45594_3_)
        {
            super(1, p_i45594_1_, p_i45594_2_, 1, 2, 2);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            StructureOceanMonumentPieces.RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var5 = this.field_175830_k;
            StructureOceanMonumentPieces.RoomDefinition var6 = var4.field_175965_b[EnumFacing.UP.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var7 = var5.field_175965_b[EnumFacing.UP.getIndex()];

            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 0, 8, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (var7.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 1, 6, 8, 7, field_175828_a);
            }

            if (var6.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 8, 8, 6, 8, 14, field_175828_a);
            }

            int var8;
            IBlockState var9;

            for (var8 = 1; var8 <= 7; ++var8)
            {
                var9 = field_175826_b;

                if (var8 == 2 || var8 == 6)
                {
                    var9 = field_175828_a;
                }

                this.func_175804_a(worldIn, p_74875_3_, 0, var8, 0, 0, var8, 15, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, var8, 0, 7, var8, 15, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 0, 6, var8, 0, var9, var9, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var8, 15, 6, var8, 15, var9, var9, false);
            }

            for (var8 = 1; var8 <= 7; ++var8)
            {
                var9 = field_175827_c;

                if (var8 == 2 || var8 == 6)
                {
                    var9 = field_175825_e;
                }

                this.func_175804_a(worldIn, p_74875_3_, 3, var8, 7, 4, var8, 8, var9, var9, false);
            }

            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 15, 4, 2, 15, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 11, 0, 2, 12, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 11, 7, 2, 12, field_175822_f, field_175822_f, false);
            }

            if (var7.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 0, 4, 6, 0, field_175822_f, field_175822_f, false);
            }

            if (var7.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 5, 3, 7, 6, 4, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 4, 2, 6, 4, 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 2, 6, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 5, 6, 3, 5, field_175826_b, field_175826_b, false);
            }

            if (var7.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 3, 0, 6, 4, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 4, 2, 2, 4, 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 2, 1, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 5, 1, 3, 5, field_175826_b, field_175826_b, false);
            }

            if (var6.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 5, 15, 4, 6, 15, field_175822_f, field_175822_f, false);
            }

            if (var6.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 11, 0, 6, 12, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 4, 10, 2, 4, 13, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 10, 1, 3, 10, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 13, 1, 3, 13, field_175826_b, field_175826_b, false);
            }

            if (var6.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 5, 11, 7, 6, 12, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 4, 10, 6, 4, 13, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 10, 6, 3, 10, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 13, 6, 3, 13, field_175826_b, field_175826_b, false);
            }

            return true;
        }
    }

    public static class DoubleZRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001979";

        public DoubleZRoom() {}

        public DoubleZRoom(EnumFacing p_i45593_1_, StructureOceanMonumentPieces.RoomDefinition p_i45593_2_, Random p_i45593_3_)
        {
            super(1, p_i45593_1_, p_i45593_2_, 1, 1, 2);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            StructureOceanMonumentPieces.RoomDefinition var4 = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition var5 = this.field_175830_k;

            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 0, 8, var4.field_175966_c[EnumFacing.DOWN.getIndex()]);
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, var5.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (var5.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 7, field_175828_a);
            }

            if (var4.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 8, 6, 4, 14, field_175828_a);
            }

            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 15, 6, 3, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 15, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 15, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 7, 2, 0, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 15, 6, 2, 15, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 7, 1, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 15, 6, 1, 15, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 1, 1, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 1, 6, 1, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 1, 1, 3, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 1, 6, 3, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 13, 1, 1, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 13, 6, 1, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 13, 1, 3, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 13, 6, 3, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 6, 2, 3, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 6, 5, 3, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 9, 2, 3, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 9, 5, 3, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 2, 6, 4, 2, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 2, 9, 4, 2, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 2, 7, 2, 2, 8, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 7, 5, 2, 8, field_175826_b, field_175826_b, false);
            this.func_175811_a(worldIn, field_175825_e, 2, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 5, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 2, 2, 10, p_74875_3_);
            this.func_175811_a(worldIn, field_175825_e, 5, 2, 10, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 2, 3, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 5, 3, 5, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 2, 3, 10, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 5, 3, 10, p_74875_3_);

            if (var5.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var5.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 15, 4, 2, 15, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 11, 0, 2, 12, field_175822_f, field_175822_f, false);
            }

            if (var4.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 11, 7, 2, 12, field_175822_f, field_175822_f, false);
            }

            return true;
        }
    }

    public static class EntryRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001978";

        public EntryRoom() {}

        public EntryRoom(EnumFacing p_i45592_1_, StructureOceanMonumentPieces.RoomDefinition p_i45592_2_)
        {
            super(1, p_i45592_1_, p_i45592_2_, 1, 1, 1);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 2, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 1, 2, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 2, 0, 7, 2, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 2, 3, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 6, 3, 0, field_175826_b, field_175826_b, false);

            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175822_f, field_175822_f, false);
            }

            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 1, 2, 4, field_175822_f, field_175822_f, false);
            }

            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 3, 7, 2, 4, field_175822_f, field_175822_f, false);
            }

            return true;
        }
    }

    static class FitSimpleRoomHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001987";

        private FitSimpleRoomHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            return true;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            return new StructureOceanMonumentPieces.SimpleRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        FitSimpleRoomHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45601_1_)
        {
            this();
        }
    }

    static class FitSimpleRoomTopHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001986";

        private FitSimpleRoomTopHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            return !p_175969_1_.field_175966_c[EnumFacing.WEST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.SOUTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()];
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            return new StructureOceanMonumentPieces.SimpleTopRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        FitSimpleRoomTopHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45600_1_)
        {
            this();
        }
    }

    public static class MonumentBuilding extends StructureOceanMonumentPieces.Piece
    {
        private StructureOceanMonumentPieces.RoomDefinition field_175845_o;
        private StructureOceanMonumentPieces.RoomDefinition field_175844_p;
        private List field_175843_q = Lists.newArrayList();
        private static final String __OBFID = "CL_00001985";

        public MonumentBuilding() {}

        public MonumentBuilding(Random p_i45599_1_, int p_i45599_2_, int p_i45599_3_, EnumFacing p_i45599_4_)
        {
            super(0);
            this.coordBaseMode = p_i45599_4_;

            switch (StructureOceanMonumentPieces.SwitchEnumFacing.field_175971_a[this.coordBaseMode.ordinal()])
            {
                case 1:
                case 2:
                    this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
                    break;

                default:
                    this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
            }

            List var5 = this.func_175836_a(p_i45599_1_);
            this.field_175845_o.field_175963_d = true;
            this.field_175843_q.add(new StructureOceanMonumentPieces.EntryRoom(this.coordBaseMode, this.field_175845_o));
            this.field_175843_q.add(new StructureOceanMonumentPieces.MonumentCoreRoom(this.coordBaseMode, this.field_175844_p, p_i45599_1_));
            ArrayList var6 = Lists.newArrayList();
            var6.add(new StructureOceanMonumentPieces.XYDoubleRoomFitHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.YZDoubleRoomFitHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.ZDoubleRoomFitHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.XDoubleRoomFitHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.YDoubleRoomFitHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.FitSimpleRoomTopHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            var6.add(new StructureOceanMonumentPieces.FitSimpleRoomHelper((StructureOceanMonumentPieces.SwitchEnumFacing)null));
            Iterator var7 = var5.iterator();

            while (var7.hasNext())
            {
                StructureOceanMonumentPieces.RoomDefinition var8 = (StructureOceanMonumentPieces.RoomDefinition)var7.next();

                if (!var8.field_175963_d && !var8.func_175961_b())
                {
                    Iterator var9 = var6.iterator();

                    while (var9.hasNext())
                    {
                        StructureOceanMonumentPieces.MonumentRoomFitHelper var10 = (StructureOceanMonumentPieces.MonumentRoomFitHelper)var9.next();

                        if (var10.func_175969_a(var8))
                        {
                            this.field_175843_q.add(var10.func_175968_a(this.coordBaseMode, var8, p_i45599_1_));
                            break;
                        }
                    }
                }
            }

            int var14 = this.boundingBox.minY;
            int var15 = this.getXWithOffset(9, 22);
            int var16 = this.getZWithOffset(9, 22);
            Iterator var17 = this.field_175843_q.iterator();

            while (var17.hasNext())
            {
                StructureOceanMonumentPieces.Piece var11 = (StructureOceanMonumentPieces.Piece)var17.next();
                var11.getBoundingBox().offset(var15, var14, var16);
            }

            StructureBoundingBox var18 = StructureBoundingBox.func_175899_a(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1), this.getXWithOffset(23, 21), this.getYWithOffset(8), this.getZWithOffset(23, 21));
            StructureBoundingBox var19 = StructureBoundingBox.func_175899_a(this.getXWithOffset(34, 1), this.getYWithOffset(1), this.getZWithOffset(34, 1), this.getXWithOffset(56, 21), this.getYWithOffset(8), this.getZWithOffset(56, 21));
            StructureBoundingBox var12 = StructureBoundingBox.func_175899_a(this.getXWithOffset(22, 22), this.getYWithOffset(13), this.getZWithOffset(22, 22), this.getXWithOffset(35, 35), this.getYWithOffset(17), this.getZWithOffset(35, 35));
            int var13 = p_i45599_1_.nextInt();
            this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, var18, var13++));
            this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, var19, var13++));
            this.field_175843_q.add(new StructureOceanMonumentPieces.Penthouse(this.coordBaseMode, var12));
        }

        private List func_175836_a(Random p_175836_1_)
        {
            StructureOceanMonumentPieces.RoomDefinition[] var2 = new StructureOceanMonumentPieces.RoomDefinition[75];
            int var3;
            int var4;
            byte var5;
            int var6;

            for (var3 = 0; var3 < 5; ++var3)
            {
                for (var4 = 0; var4 < 4; ++var4)
                {
                    var5 = 0;
                    var6 = func_175820_a(var3, var5, var4);
                    var2[var6] = new StructureOceanMonumentPieces.RoomDefinition(var6);
                }
            }

            for (var3 = 0; var3 < 5; ++var3)
            {
                for (var4 = 0; var4 < 4; ++var4)
                {
                    var5 = 1;
                    var6 = func_175820_a(var3, var5, var4);
                    var2[var6] = new StructureOceanMonumentPieces.RoomDefinition(var6);
                }
            }

            for (var3 = 1; var3 < 4; ++var3)
            {
                for (var4 = 0; var4 < 2; ++var4)
                {
                    var5 = 2;
                    var6 = func_175820_a(var3, var5, var4);
                    var2[var6] = new StructureOceanMonumentPieces.RoomDefinition(var6);
                }
            }

            this.field_175845_o = var2[field_175823_g];
            int var8;
            int var9;
            int var11;
            int var12;
            int var13;

            for (var3 = 0; var3 < 5; ++var3)
            {
                for (var4 = 0; var4 < 5; ++var4)
                {
                    for (int var17 = 0; var17 < 3; ++var17)
                    {
                        var6 = func_175820_a(var3, var17, var4);

                        if (var2[var6] != null)
                        {
                            EnumFacing[] var7 = EnumFacing.values();
                            var8 = var7.length;

                            for (var9 = 0; var9 < var8; ++var9)
                            {
                                EnumFacing var10 = var7[var9];
                                var11 = var3 + var10.getFrontOffsetX();
                                var12 = var17 + var10.getFrontOffsetY();
                                var13 = var4 + var10.getFrontOffsetZ();

                                if (var11 >= 0 && var11 < 5 && var13 >= 0 && var13 < 5 && var12 >= 0 && var12 < 3)
                                {
                                    int var14 = func_175820_a(var11, var12, var13);

                                    if (var2[var14] != null)
                                    {
                                        if (var13 != var4)
                                        {
                                            var2[var6].func_175957_a(var10.getOpposite(), var2[var14]);
                                        }
                                        else
                                        {
                                            var2[var6].func_175957_a(var10, var2[var14]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StructureOceanMonumentPieces.RoomDefinition var15;
            StructureOceanMonumentPieces.RoomDefinition var16;
            StructureOceanMonumentPieces.RoomDefinition var18;
            var2[field_175831_h].func_175957_a(EnumFacing.UP, var15 = new StructureOceanMonumentPieces.RoomDefinition(1003));
            var2[field_175832_i].func_175957_a(EnumFacing.SOUTH, var16 = new StructureOceanMonumentPieces.RoomDefinition(1001));
            var2[field_175829_j].func_175957_a(EnumFacing.SOUTH, var18 = new StructureOceanMonumentPieces.RoomDefinition(1002));
            var15.field_175963_d = true;
            var16.field_175963_d = true;
            var18.field_175963_d = true;
            this.field_175845_o.field_175964_e = true;
            this.field_175844_p = var2[func_175820_a(p_175836_1_.nextInt(4), 0, 2)];
            this.field_175844_p.field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            ArrayList var19 = Lists.newArrayList();
            StructureOceanMonumentPieces.RoomDefinition[] var20 = var2;
            var8 = var2.length;

            for (var9 = 0; var9 < var8; ++var9)
            {
                StructureOceanMonumentPieces.RoomDefinition var24 = var20[var9];

                if (var24 != null)
                {
                    var24.func_175958_a();
                    var19.add(var24);
                }
            }

            var15.func_175958_a();
            Collections.shuffle(var19, p_175836_1_);
            int var21 = 1;
            Iterator var22 = var19.iterator();

            while (var22.hasNext())
            {
                StructureOceanMonumentPieces.RoomDefinition var23 = (StructureOceanMonumentPieces.RoomDefinition)var22.next();
                int var25 = 0;
                var11 = 0;

                while (var25 < 2 && var11 < 5)
                {
                    ++var11;
                    var12 = p_175836_1_.nextInt(6);

                    if (var23.field_175966_c[var12])
                    {
                        var13 = EnumFacing.getFront(var12).getOpposite().getIndex();
                        var23.field_175966_c[var12] = false;
                        var23.field_175965_b[var12].field_175966_c[var13] = false;

                        if (var23.func_175959_a(var21++) && var23.field_175965_b[var12].func_175959_a(var21++))
                        {
                            ++var25;
                        }
                        else
                        {
                            var23.field_175966_c[var12] = true;
                            var23.field_175965_b[var12].field_175966_c[var13] = true;
                        }
                    }
                }
            }

            var19.add(var15);
            var19.add(var16);
            var19.add(var18);
            return var19;
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            this.func_175840_a(false, 0, worldIn, p_74875_2_, p_74875_3_);
            this.func_175840_a(true, 33, worldIn, p_74875_2_, p_74875_3_);
            this.func_175839_b(worldIn, p_74875_2_, p_74875_3_);
            this.func_175837_c(worldIn, p_74875_2_, p_74875_3_);
            this.func_175841_d(worldIn, p_74875_2_, p_74875_3_);
            this.func_175835_e(worldIn, p_74875_2_, p_74875_3_);
            this.func_175842_f(worldIn, p_74875_2_, p_74875_3_);
            this.func_175838_g(worldIn, p_74875_2_, p_74875_3_);
            int var4;

            for (var4 = 0; var4 < 7; ++var4)
            {
                int var5 = 0;

                while (var5 < 7)
                {
                    if (var5 == 0 && var4 == 3)
                    {
                        var5 = 6;
                    }

                    int var6 = var4 * 9;
                    int var7 = var5 * 9;

                    for (int var8 = 0; var8 < 4; ++var8)
                    {
                        for (int var9 = 0; var9 < 4; ++var9)
                        {
                            this.func_175811_a(worldIn, field_175826_b, var6 + var8, 0, var7 + var9, p_74875_3_);
                            this.func_175808_b(worldIn, field_175826_b, var6 + var8, -1, var7 + var9, p_74875_3_);
                        }
                    }

                    if (var4 != 0 && var4 != 6)
                    {
                        var5 += 6;
                    }
                    else
                    {
                        ++var5;
                    }
                }
            }

            for (var4 = 0; var4 < 5; ++var4)
            {
                this.func_175804_a(worldIn, p_74875_3_, -1 - var4, 0 + var4 * 2, -1 - var4, -1 - var4, 23, 58 + var4, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 58 + var4, 0 + var4 * 2, -1 - var4, 58 + var4, 23, 58 + var4, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 0 - var4, 0 + var4 * 2, -1 - var4, 57 + var4, 23, -1 - var4, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_74875_3_, 0 - var4, 0 + var4 * 2, 58 + var4, 57 + var4, 23, 58 + var4, field_175822_f, field_175822_f, false);
            }

            Iterator var10 = this.field_175843_q.iterator();

            while (var10.hasNext())
            {
                StructureOceanMonumentPieces.Piece var11 = (StructureOceanMonumentPieces.Piece)var10.next();

                if (var11.getBoundingBox().intersectsWith(p_74875_3_))
                {
                    var11.addComponentParts(worldIn, p_74875_2_, p_74875_3_);
                }
            }

            return true;
        }

        private void func_175840_a(boolean p_175840_1_, int p_175840_2_, World worldIn, Random p_175840_4_, StructureBoundingBox p_175840_5_)
        {
            boolean var6 = true;

            if (this.func_175818_a(p_175840_5_, p_175840_2_, 0, p_175840_2_ + 23, 20))
            {
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 0, 0, 0, p_175840_2_ + 24, 0, 20, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 0, 1, 0, p_175840_2_ + 24, 10, 20, field_175822_f, field_175822_f, false);
                int var7;

                for (var7 = 0; var7 < 4; ++var7)
                {
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7, var7 + 1, var7, p_175840_2_ + var7, var7 + 1, 20, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 7, var7 + 5, var7 + 7, p_175840_2_ + var7 + 7, var7 + 5, 20, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 17 - var7, var7 + 5, var7 + 7, p_175840_2_ + 17 - var7, var7 + 5, 20, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 24 - var7, var7 + 1, var7, p_175840_2_ + 24 - var7, var7 + 1, 20, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 1, var7 + 1, var7, p_175840_2_ + 23 - var7, var7 + 1, var7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + var7 + 8, var7 + 5, var7 + 7, p_175840_2_ + 16 - var7, var7 + 5, var7 + 7, field_175826_b, field_175826_b, false);
                }

                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 4, 4, 4, p_175840_2_ + 6, 4, 20, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 7, 4, 4, p_175840_2_ + 17, 4, 6, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 18, 4, 4, p_175840_2_ + 20, 4, 20, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 11, 8, 11, p_175840_2_ + 13, 8, 20, field_175828_a, field_175828_a, false);
                this.func_175811_a(worldIn, field_175824_d, p_175840_2_ + 12, 9, 12, p_175840_5_);
                this.func_175811_a(worldIn, field_175824_d, p_175840_2_ + 12, 9, 15, p_175840_5_);
                this.func_175811_a(worldIn, field_175824_d, p_175840_2_ + 12, 9, 18, p_175840_5_);
                var7 = p_175840_1_ ? p_175840_2_ + 19 : p_175840_2_ + 5;
                int var8 = p_175840_1_ ? p_175840_2_ + 5 : p_175840_2_ + 19;
                int var9;

                for (var9 = 20; var9 >= 5; var9 -= 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, var7, 5, var9, p_175840_5_);
                }

                for (var9 = 19; var9 >= 7; var9 -= 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, var8, 5, var9, p_175840_5_);
                }

                for (var9 = 0; var9 < 4; ++var9)
                {
                    int var10 = p_175840_1_ ? p_175840_2_ + (24 - (17 - var9 * 3)) : p_175840_2_ + 17 - var9 * 3;
                    this.func_175811_a(worldIn, field_175824_d, var10, 5, 5, p_175840_5_);
                }

                this.func_175811_a(worldIn, field_175824_d, var8, 5, 5, p_175840_5_);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 11, 1, 12, p_175840_2_ + 13, 7, 12, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175840_5_, p_175840_2_ + 12, 1, 11, p_175840_2_ + 12, 7, 13, field_175828_a, field_175828_a, false);
            }
        }

        private void func_175839_b(World worldIn, Random p_175839_2_, StructureBoundingBox p_175839_3_)
        {
            if (this.func_175818_a(p_175839_3_, 22, 5, 35, 17))
            {
                this.func_175804_a(worldIn, p_175839_3_, 25, 0, 0, 32, 8, 20, field_175822_f, field_175822_f, false);

                for (int var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175839_3_, 24, 2, 5 + var4 * 4, 24, 4, 5 + var4 * 4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175839_3_, 22, 4, 5 + var4 * 4, 23, 4, 5 + var4 * 4, field_175826_b, field_175826_b, false);
                    this.func_175811_a(worldIn, field_175826_b, 25, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, field_175826_b, 26, 6, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, field_175825_e, 26, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175804_a(worldIn, p_175839_3_, 33, 2, 5 + var4 * 4, 33, 4, 5 + var4 * 4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175839_3_, 34, 4, 5 + var4 * 4, 35, 4, 5 + var4 * 4, field_175826_b, field_175826_b, false);
                    this.func_175811_a(worldIn, field_175826_b, 32, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, field_175826_b, 31, 6, 5 + var4 * 4, p_175839_3_);
                    this.func_175811_a(worldIn, field_175825_e, 31, 5, 5 + var4 * 4, p_175839_3_);
                    this.func_175804_a(worldIn, p_175839_3_, 27, 6, 5 + var4 * 4, 30, 6, 5 + var4 * 4, field_175828_a, field_175828_a, false);
                }
            }
        }

        private void func_175837_c(World worldIn, Random p_175837_2_, StructureBoundingBox p_175837_3_)
        {
            if (this.func_175818_a(p_175837_3_, 15, 20, 42, 21))
            {
                this.func_175804_a(worldIn, p_175837_3_, 15, 0, 21, 42, 0, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 26, 1, 21, 31, 3, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 12, 21, 36, 12, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 11, 21, 40, 11, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 10, 21, 41, 10, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 15, 7, 21, 42, 9, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 6, 21, 41, 6, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 5, 21, 40, 5, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 4, 21, 36, 4, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 22, 3, 21, 26, 3, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 31, 3, 21, 35, 3, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 23, 2, 21, 25, 2, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 32, 2, 21, 34, 2, 21, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175837_3_, 28, 4, 20, 29, 4, 21, field_175826_b, field_175826_b, false);
                this.func_175811_a(worldIn, field_175826_b, 27, 3, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175826_b, 30, 3, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175826_b, 26, 2, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175826_b, 31, 2, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175826_b, 25, 1, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175826_b, 32, 1, 21, p_175837_3_);
                int var4;

                for (var4 = 0; var4 < 7; ++var4)
                {
                    this.func_175811_a(worldIn, field_175827_c, 28 - var4, 6 + var4, 21, p_175837_3_);
                    this.func_175811_a(worldIn, field_175827_c, 29 + var4, 6 + var4, 21, p_175837_3_);
                }

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175811_a(worldIn, field_175827_c, 28 - var4, 9 + var4, 21, p_175837_3_);
                    this.func_175811_a(worldIn, field_175827_c, 29 + var4, 9 + var4, 21, p_175837_3_);
                }

                this.func_175811_a(worldIn, field_175827_c, 28, 12, 21, p_175837_3_);
                this.func_175811_a(worldIn, field_175827_c, 29, 12, 21, p_175837_3_);

                for (var4 = 0; var4 < 3; ++var4)
                {
                    this.func_175811_a(worldIn, field_175827_c, 22 - var4 * 2, 8, 21, p_175837_3_);
                    this.func_175811_a(worldIn, field_175827_c, 22 - var4 * 2, 9, 21, p_175837_3_);
                    this.func_175811_a(worldIn, field_175827_c, 35 + var4 * 2, 8, 21, p_175837_3_);
                    this.func_175811_a(worldIn, field_175827_c, 35 + var4 * 2, 9, 21, p_175837_3_);
                }

                this.func_175804_a(worldIn, p_175837_3_, 15, 13, 21, 42, 15, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 15, 1, 21, 15, 6, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 16, 1, 21, 16, 5, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 17, 1, 21, 20, 4, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 21, 1, 21, 21, 3, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 22, 1, 21, 22, 2, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 23, 1, 21, 24, 1, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 42, 1, 21, 42, 6, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 41, 1, 21, 41, 5, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 37, 1, 21, 40, 4, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 36, 1, 21, 36, 3, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 35, 1, 21, 35, 2, 21, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175837_3_, 33, 1, 21, 34, 1, 21, field_175822_f, field_175822_f, false);
            }
        }

        private void func_175841_d(World worldIn, Random p_175841_2_, StructureBoundingBox p_175841_3_)
        {
            if (this.func_175818_a(p_175841_3_, 21, 21, 36, 36))
            {
                this.func_175804_a(worldIn, p_175841_3_, 21, 0, 22, 36, 0, 36, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 21, 1, 22, 36, 23, 36, field_175822_f, field_175822_f, false);

                for (int var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 21 + var4, 36 - var4, 13 + var4, 21 + var4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 36 - var4, 36 - var4, 13 + var4, 36 - var4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 21 + var4, 13 + var4, 22 + var4, 21 + var4, 13 + var4, 35 - var4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_175841_3_, 36 - var4, 13 + var4, 22 + var4, 36 - var4, 13 + var4, 35 - var4, field_175826_b, field_175826_b, false);
                }

                this.func_175804_a(worldIn, p_175841_3_, 25, 16, 25, 32, 16, 32, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 25, 17, 25, 25, 19, 25, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 32, 17, 25, 32, 19, 25, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 25, 17, 32, 25, 19, 32, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175841_3_, 32, 17, 32, 32, 19, 32, field_175826_b, field_175826_b, false);
                this.func_175811_a(worldIn, field_175826_b, 26, 20, 26, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 27, 21, 27, p_175841_3_);
                this.func_175811_a(worldIn, field_175825_e, 27, 20, 27, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 26, 20, 31, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 27, 21, 30, p_175841_3_);
                this.func_175811_a(worldIn, field_175825_e, 27, 20, 30, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 31, 20, 31, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 30, 21, 30, p_175841_3_);
                this.func_175811_a(worldIn, field_175825_e, 30, 20, 30, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 31, 20, 26, p_175841_3_);
                this.func_175811_a(worldIn, field_175826_b, 30, 21, 27, p_175841_3_);
                this.func_175811_a(worldIn, field_175825_e, 30, 20, 27, p_175841_3_);
                this.func_175804_a(worldIn, p_175841_3_, 28, 21, 27, 29, 21, 27, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 27, 21, 28, 27, 21, 29, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 28, 21, 30, 29, 21, 30, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175841_3_, 30, 21, 28, 30, 21, 29, field_175828_a, field_175828_a, false);
            }
        }

        private void func_175835_e(World worldIn, Random p_175835_2_, StructureBoundingBox p_175835_3_)
        {
            int var4;

            if (this.func_175818_a(p_175835_3_, 0, 21, 6, 58))
            {
                this.func_175804_a(worldIn, p_175835_3_, 0, 0, 21, 6, 0, 57, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 0, 1, 21, 6, 7, 57, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175835_3_, 4, 4, 21, 6, 4, 53, field_175828_a, field_175828_a, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175835_3_, var4, var4 + 1, 21, var4, var4 + 1, 57 - var4, field_175826_b, field_175826_b, false);
                }

                for (var4 = 23; var4 < 53; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 5, 5, var4, p_175835_3_);
                }

                this.func_175811_a(worldIn, field_175824_d, 5, 5, 52, p_175835_3_);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175835_3_, var4, var4 + 1, 21, var4, var4 + 1, 57 - var4, field_175826_b, field_175826_b, false);
                }

                this.func_175804_a(worldIn, p_175835_3_, 4, 1, 52, 6, 3, 52, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 5, 1, 51, 5, 3, 53, field_175828_a, field_175828_a, false);
            }

            if (this.func_175818_a(p_175835_3_, 51, 21, 58, 58))
            {
                this.func_175804_a(worldIn, p_175835_3_, 51, 0, 21, 57, 0, 57, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 51, 1, 21, 57, 7, 57, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175835_3_, 51, 4, 21, 53, 4, 53, field_175828_a, field_175828_a, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175835_3_, 57 - var4, var4 + 1, 21, 57 - var4, var4 + 1, 57 - var4, field_175826_b, field_175826_b, false);
                }

                for (var4 = 23; var4 < 53; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 52, 5, var4, p_175835_3_);
                }

                this.func_175811_a(worldIn, field_175824_d, 52, 5, 52, p_175835_3_);
                this.func_175804_a(worldIn, p_175835_3_, 51, 1, 52, 53, 3, 52, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 52, 1, 51, 52, 3, 53, field_175828_a, field_175828_a, false);
            }

            if (this.func_175818_a(p_175835_3_, 0, 51, 57, 57))
            {
                this.func_175804_a(worldIn, p_175835_3_, 7, 0, 51, 50, 0, 57, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175835_3_, 7, 1, 51, 50, 10, 57, field_175822_f, field_175822_f, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175835_3_, var4 + 1, var4 + 1, 57 - var4, 56 - var4, var4 + 1, 57 - var4, field_175826_b, field_175826_b, false);
                }
            }
        }

        private void func_175842_f(World worldIn, Random p_175842_2_, StructureBoundingBox p_175842_3_)
        {
            int var4;

            if (this.func_175818_a(p_175842_3_, 7, 21, 13, 50))
            {
                this.func_175804_a(worldIn, p_175842_3_, 7, 0, 21, 13, 0, 50, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 7, 1, 21, 13, 10, 50, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175842_3_, 11, 8, 21, 13, 8, 53, field_175828_a, field_175828_a, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175842_3_, var4 + 7, var4 + 5, 21, var4 + 7, var4 + 5, 54, field_175826_b, field_175826_b, false);
                }

                for (var4 = 21; var4 <= 45; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 12, 9, var4, p_175842_3_);
                }
            }

            if (this.func_175818_a(p_175842_3_, 44, 21, 50, 54))
            {
                this.func_175804_a(worldIn, p_175842_3_, 44, 0, 21, 50, 0, 50, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 44, 1, 21, 50, 10, 50, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175842_3_, 44, 8, 21, 46, 8, 53, field_175828_a, field_175828_a, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175842_3_, 50 - var4, var4 + 5, 21, 50 - var4, var4 + 5, 54, field_175826_b, field_175826_b, false);
                }

                for (var4 = 21; var4 <= 45; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 45, 9, var4, p_175842_3_);
                }
            }

            if (this.func_175818_a(p_175842_3_, 8, 44, 49, 54))
            {
                this.func_175804_a(worldIn, p_175842_3_, 14, 0, 44, 43, 0, 50, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175842_3_, 14, 1, 44, 43, 10, 50, field_175822_f, field_175822_f, false);

                for (var4 = 12; var4 <= 45; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, var4, 9, 45, p_175842_3_);
                    this.func_175811_a(worldIn, field_175824_d, var4, 9, 52, p_175842_3_);

                    if (var4 == 12 || var4 == 18 || var4 == 24 || var4 == 33 || var4 == 39 || var4 == 45)
                    {
                        this.func_175811_a(worldIn, field_175824_d, var4, 9, 47, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 9, 50, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 10, 45, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 10, 46, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 10, 51, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 10, 52, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 11, 47, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 11, 50, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 12, 48, p_175842_3_);
                        this.func_175811_a(worldIn, field_175824_d, var4, 12, 49, p_175842_3_);
                    }
                }

                for (var4 = 0; var4 < 3; ++var4)
                {
                    this.func_175804_a(worldIn, p_175842_3_, 8 + var4, 5 + var4, 54, 49 - var4, 5 + var4, 54, field_175828_a, field_175828_a, false);
                }

                this.func_175804_a(worldIn, p_175842_3_, 11, 8, 54, 46, 8, 54, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175842_3_, 14, 8, 44, 43, 8, 53, field_175828_a, field_175828_a, false);
            }
        }

        private void func_175838_g(World worldIn, Random p_175838_2_, StructureBoundingBox p_175838_3_)
        {
            int var4;

            if (this.func_175818_a(p_175838_3_, 14, 21, 20, 43))
            {
                this.func_175804_a(worldIn, p_175838_3_, 14, 0, 21, 20, 0, 43, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 14, 1, 22, 20, 14, 43, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 18, 12, 22, 20, 12, 39, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 18, 12, 21, 20, 12, 21, field_175826_b, field_175826_b, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175838_3_, var4 + 14, var4 + 9, 21, var4 + 14, var4 + 9, 43 - var4, field_175826_b, field_175826_b, false);
                }

                for (var4 = 23; var4 <= 39; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 19, 13, var4, p_175838_3_);
                }
            }

            if (this.func_175818_a(p_175838_3_, 37, 21, 43, 43))
            {
                this.func_175804_a(worldIn, p_175838_3_, 37, 0, 21, 43, 0, 43, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 1, 22, 43, 14, 43, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 12, 22, 39, 12, 39, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 37, 12, 21, 39, 12, 21, field_175826_b, field_175826_b, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175838_3_, 43 - var4, var4 + 9, 21, 43 - var4, var4 + 9, 43 - var4, field_175826_b, field_175826_b, false);
                }

                for (var4 = 23; var4 <= 39; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, 38, 13, var4, p_175838_3_);
                }
            }

            if (this.func_175818_a(p_175838_3_, 15, 37, 42, 43))
            {
                this.func_175804_a(worldIn, p_175838_3_, 21, 0, 37, 36, 0, 43, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175838_3_, 21, 1, 37, 36, 14, 43, field_175822_f, field_175822_f, false);
                this.func_175804_a(worldIn, p_175838_3_, 21, 12, 37, 36, 12, 39, field_175828_a, field_175828_a, false);

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_175838_3_, 15 + var4, var4 + 9, 43 - var4, 42 - var4, var4 + 9, 43 - var4, field_175826_b, field_175826_b, false);
                }

                for (var4 = 21; var4 <= 36; var4 += 3)
                {
                    this.func_175811_a(worldIn, field_175824_d, var4, 13, 38, p_175838_3_);
                }
            }
        }
    }

    public static class MonumentCoreRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001984";

        public MonumentCoreRoom() {}

        public MonumentCoreRoom(EnumFacing p_i45598_1_, StructureOceanMonumentPieces.RoomDefinition p_i45598_2_, Random p_i45598_3_)
        {
            super(1, p_i45598_1_, p_i45598_2_, 2, 2, 2);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            this.func_175819_a(worldIn, p_74875_3_, 1, 8, 0, 14, 8, 14, field_175828_a);
            byte var4 = 7;
            IBlockState var5 = field_175826_b;
            this.func_175804_a(worldIn, p_74875_3_, 0, var4, 0, 0, var4, 15, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 15, var4, 0, 15, var4, 15, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, var4, 0, 15, var4, 0, var5, var5, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, var4, 15, 14, var4, 15, var5, var5, false);
            int var7;

            for (var7 = 1; var7 <= 6; ++var7)
            {
                var5 = field_175826_b;

                if (var7 == 2 || var7 == 6)
                {
                    var5 = field_175828_a;
                }

                for (int var6 = 0; var6 <= 15; var6 += 15)
                {
                    this.func_175804_a(worldIn, p_74875_3_, var6, var7, 0, var6, var7, 1, var5, var5, false);
                    this.func_175804_a(worldIn, p_74875_3_, var6, var7, 6, var6, var7, 9, var5, var5, false);
                    this.func_175804_a(worldIn, p_74875_3_, var6, var7, 14, var6, var7, 15, var5, var5, false);
                }

                this.func_175804_a(worldIn, p_74875_3_, 1, var7, 0, 1, var7, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, var7, 0, 9, var7, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, var7, 0, 14, var7, 0, var5, var5, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, var7, 15, 14, var7, 15, var5, var5, false);
            }

            this.func_175804_a(worldIn, p_74875_3_, 6, 3, 6, 9, 6, 9, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 4, 7, 8, 5, 8, Blocks.gold_block.getDefaultState(), Blocks.gold_block.getDefaultState(), false);

            for (var7 = 3; var7 <= 6; var7 += 3)
            {
                for (int var8 = 6; var8 <= 9; var8 += 3)
                {
                    this.func_175811_a(worldIn, field_175825_e, var8, var7, 6, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, var8, var7, 9, p_74875_3_);
                }
            }

            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 6, 5, 2, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 9, 5, 2, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 6, 10, 2, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 1, 9, 10, 2, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 5, 6, 2, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 1, 5, 9, 2, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 10, 6, 2, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 1, 10, 9, 2, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 5, 5, 6, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 2, 10, 5, 6, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 2, 5, 10, 6, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 2, 10, 10, 6, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 1, 5, 7, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 7, 1, 10, 7, 6, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 7, 9, 5, 7, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 7, 9, 10, 7, 14, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 7, 5, 6, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 7, 10, 6, 7, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 7, 5, 14, 7, 5, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 7, 10, 14, 7, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 2, 2, 1, 3, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 3, 1, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 2, 13, 1, 3, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, 1, 2, 12, 1, 2, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 12, 2, 1, 13, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 13, 3, 1, 13, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 1, 12, 13, 1, 13, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, 1, 13, 12, 1, 13, field_175826_b, field_175826_b, false);
            return true;
        }
    }

    interface MonumentRoomFitHelper
    {
        boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition var1);

        StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3);
    }

    public static class Penthouse extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001977";

        public Penthouse() {}

        public Penthouse(EnumFacing p_i45591_1_, StructureBoundingBox p_i45591_2_)
        {
            super(p_i45591_1_, p_i45591_2_);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 2, 11, -1, 11, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, -1, 0, 1, -1, 11, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 12, -1, 0, 13, -1, 11, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 0, 11, -1, 1, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 2, -1, 12, 11, -1, 13, field_175828_a, field_175828_a, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 0, 13, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 13, 0, 0, 13, 0, 13, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 12, 0, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 13, 12, 0, 13, field_175826_b, field_175826_b, false);

            for (int var4 = 2; var4 <= 11; var4 += 3)
            {
                this.func_175811_a(worldIn, field_175825_e, 0, 0, var4, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 13, 0, var4, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, var4, 0, 0, p_74875_3_);
            }

            this.func_175804_a(worldIn, p_74875_3_, 2, 0, 3, 4, 0, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 0, 3, 11, 0, 9, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 0, 9, 9, 0, 11, field_175826_b, field_175826_b, false);
            this.func_175811_a(worldIn, field_175826_b, 5, 0, 8, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 8, 0, 8, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 10, 0, 10, p_74875_3_);
            this.func_175811_a(worldIn, field_175826_b, 3, 0, 10, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 3, 0, 3, 3, 0, 7, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 0, 3, 10, 0, 7, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 0, 10, 7, 0, 10, field_175827_c, field_175827_c, false);
            byte var7 = 3;

            for (int var5 = 0; var5 < 2; ++var5)
            {
                for (int var6 = 2; var6 <= 8; var6 += 3)
                {
                    this.func_175804_a(worldIn, p_74875_3_, var7, 0, var6, var7, 2, var6, field_175826_b, field_175826_b, false);
                }

                var7 = 10;
            }

            this.func_175804_a(worldIn, p_74875_3_, 5, 0, 10, 5, 2, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 0, 10, 8, 2, 10, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, -1, 7, 7, -1, 8, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 6, -1, 3, 7, -1, 4, field_175822_f, field_175822_f, false);
            this.func_175817_a(worldIn, p_74875_3_, 6, 1, 6);
            return true;
        }
    }

    public abstract static class Piece extends StructureComponent
    {
        protected static final IBlockState field_175828_a = Blocks.prismarine.getStateFromMeta(BlockPrismarine.ROUGHMETA);
        protected static final IBlockState field_175826_b = Blocks.prismarine.getStateFromMeta(BlockPrismarine.BRICKSMETA);
        protected static final IBlockState field_175827_c = Blocks.prismarine.getStateFromMeta(BlockPrismarine.DARKMETA);
        protected static final IBlockState field_175824_d = field_175826_b;
        protected static final IBlockState field_175825_e = Blocks.sea_lantern.getDefaultState();
        protected static final IBlockState field_175822_f = Blocks.water.getDefaultState();
        protected static final int field_175823_g = func_175820_a(2, 0, 0);
        protected static final int field_175831_h = func_175820_a(2, 2, 0);
        protected static final int field_175832_i = func_175820_a(0, 1, 0);
        protected static final int field_175829_j = func_175820_a(4, 1, 0);
        protected StructureOceanMonumentPieces.RoomDefinition field_175830_k;
        private static final String __OBFID = "CL_00001976";

        protected static final int func_175820_a(int p_175820_0_, int p_175820_1_, int p_175820_2_)
        {
            return p_175820_1_ * 25 + p_175820_2_ * 5 + p_175820_0_;
        }

        public Piece()
        {
            super(0);
        }

        public Piece(int p_i45588_1_)
        {
            super(p_i45588_1_);
        }

        public Piece(EnumFacing p_i45589_1_, StructureBoundingBox p_i45589_2_)
        {
            super(1);
            this.coordBaseMode = p_i45589_1_;
            this.boundingBox = p_i45589_2_;
        }

        protected Piece(int p_i45590_1_, EnumFacing p_i45590_2_, StructureOceanMonumentPieces.RoomDefinition p_i45590_3_, int p_i45590_4_, int p_i45590_5_, int p_i45590_6_)
        {
            super(p_i45590_1_);
            this.coordBaseMode = p_i45590_2_;
            this.field_175830_k = p_i45590_3_;
            int var7 = p_i45590_3_.field_175967_a;
            int var8 = var7 % 5;
            int var9 = var7 / 5 % 5;
            int var10 = var7 / 25;

            if (p_i45590_2_ != EnumFacing.NORTH && p_i45590_2_ != EnumFacing.SOUTH)
            {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_6_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_4_ * 8 - 1);
            }
            else
            {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_4_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_6_ * 8 - 1);
            }

            switch (StructureOceanMonumentPieces.SwitchEnumFacing.field_175971_a[p_i45590_2_.ordinal()])
            {
                case 1:
                    this.boundingBox.offset(var8 * 8, var10 * 4, -(var9 + p_i45590_6_) * 8 + 1);
                    break;

                case 2:
                    this.boundingBox.offset(var8 * 8, var10 * 4, var9 * 8);
                    break;

                case 3:
                    this.boundingBox.offset(-(var9 + p_i45590_6_) * 8 + 1, var10 * 4, var8 * 8);
                    break;

                default:
                    this.boundingBox.offset(var9 * 8, var10 * 4, var8 * 8);
            }
        }

        protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {}

        protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {}

        protected void func_175821_a(World worldIn, StructureBoundingBox p_175821_2_, int p_175821_3_, int p_175821_4_, boolean p_175821_5_)
        {
            if (p_175821_5_)
            {
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 2, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 0, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 2, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 2, 0, p_175821_4_ + 3, p_175821_3_ + 2, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 3, p_175821_3_ + 5, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
            }
            else
            {
                this.func_175804_a(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
            }
        }

        protected void func_175819_a(World worldIn, StructureBoundingBox p_175819_2_, int p_175819_3_, int p_175819_4_, int p_175819_5_, int p_175819_6_, int p_175819_7_, int p_175819_8_, IBlockState p_175819_9_)
        {
            for (int var10 = p_175819_4_; var10 <= p_175819_7_; ++var10)
            {
                for (int var11 = p_175819_3_; var11 <= p_175819_6_; ++var11)
                {
                    for (int var12 = p_175819_5_; var12 <= p_175819_8_; ++var12)
                    {
                        if (this.func_175807_a(worldIn, var11, var10, var12, p_175819_2_) == field_175822_f)
                        {
                            this.func_175811_a(worldIn, p_175819_9_, var11, var10, var12, p_175819_2_);
                        }
                    }
                }
            }
        }

        protected boolean func_175818_a(StructureBoundingBox p_175818_1_, int p_175818_2_, int p_175818_3_, int p_175818_4_, int p_175818_5_)
        {
            int var6 = this.getXWithOffset(p_175818_2_, p_175818_3_);
            int var7 = this.getZWithOffset(p_175818_2_, p_175818_3_);
            int var8 = this.getXWithOffset(p_175818_4_, p_175818_5_);
            int var9 = this.getZWithOffset(p_175818_4_, p_175818_5_);
            return p_175818_1_.intersectsWith(Math.min(var6, var8), Math.min(var7, var9), Math.max(var6, var8), Math.max(var7, var9));
        }

        protected boolean func_175817_a(World worldIn, StructureBoundingBox p_175817_2_, int p_175817_3_, int p_175817_4_, int p_175817_5_)
        {
            int var6 = this.getXWithOffset(p_175817_3_, p_175817_5_);
            int var7 = this.getYWithOffset(p_175817_4_);
            int var8 = this.getZWithOffset(p_175817_3_, p_175817_5_);

            if (p_175817_2_.func_175898_b(new BlockPos(var6, var7, var8)))
            {
                EntityGuardian var9 = new EntityGuardian(worldIn);
                var9.func_175467_a(true);
                var9.heal(var9.getMaxHealth());
                var9.setLocationAndAngles((double)var6 + 0.5D, (double)var7, (double)var8 + 0.5D, 0.0F, 0.0F);
                var9.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var9)), (IEntityLivingData)null);
                worldIn.spawnEntityInWorld(var9);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    static class RoomDefinition
    {
        int field_175967_a;
        StructureOceanMonumentPieces.RoomDefinition[] field_175965_b = new StructureOceanMonumentPieces.RoomDefinition[6];
        boolean[] field_175966_c = new boolean[6];
        boolean field_175963_d;
        boolean field_175964_e;
        int field_175962_f;
        private static final String __OBFID = "CL_00001972";

        public RoomDefinition(int p_i45584_1_)
        {
            this.field_175967_a = p_i45584_1_;
        }

        public void func_175957_a(EnumFacing p_175957_1_, StructureOceanMonumentPieces.RoomDefinition p_175957_2_)
        {
            this.field_175965_b[p_175957_1_.getIndex()] = p_175957_2_;
            p_175957_2_.field_175965_b[p_175957_1_.getOpposite().getIndex()] = this;
        }

        public void func_175958_a()
        {
            for (int var1 = 0; var1 < 6; ++var1)
            {
                this.field_175966_c[var1] = this.field_175965_b[var1] != null;
            }
        }

        public boolean func_175959_a(int p_175959_1_)
        {
            if (this.field_175964_e)
            {
                return true;
            }
            else
            {
                this.field_175962_f = p_175959_1_;

                for (int var2 = 0; var2 < 6; ++var2)
                {
                    if (this.field_175965_b[var2] != null && this.field_175966_c[var2] && this.field_175965_b[var2].field_175962_f != p_175959_1_ && this.field_175965_b[var2].func_175959_a(p_175959_1_))
                    {
                        return true;
                    }
                }

                return false;
            }
        }

        public boolean func_175961_b()
        {
            return this.field_175967_a >= 75;
        }

        public int func_175960_c()
        {
            int var1 = 0;

            for (int var2 = 0; var2 < 6; ++var2)
            {
                if (this.field_175966_c[var2])
                {
                    ++var1;
                }
            }

            return var1;
        }
    }

    public static class SimpleRoom extends StructureOceanMonumentPieces.Piece
    {
        private int field_175833_o;
        private static final String __OBFID = "CL_00001975";

        public SimpleRoom() {}

        public SimpleRoom(EnumFacing p_i45587_1_, StructureOceanMonumentPieces.RoomDefinition p_i45587_2_, Random p_i45587_3_)
        {
            super(1, p_i45587_1_, p_i45587_2_, 1, 1, 1);
            this.field_175833_o = p_i45587_3_.nextInt(3);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 6, field_175828_a);
            }

            boolean var4 = this.field_175833_o != 0 && p_74875_2_.nextBoolean() && !this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()] && !this.field_175830_k.field_175966_c[EnumFacing.UP.getIndex()] && this.field_175830_k.func_175960_c() > 1;

            if (this.field_175833_o == 0)
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 2, 1, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 2, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 2, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 2, 2, 0, field_175828_a, field_175828_a, false);
                this.func_175811_a(worldIn, field_175825_e, 1, 2, 1, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 7, 1, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 3, 0, 7, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 2, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
                this.func_175811_a(worldIn, field_175825_e, 6, 2, 1, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 5, 2, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 5, 2, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 5, 0, 2, 7, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 2, 2, 7, field_175828_a, field_175828_a, false);
                this.func_175811_a(worldIn, field_175825_e, 1, 2, 6, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 5, 7, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 3, 5, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 5, 7, 2, 7, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
                this.func_175811_a(worldIn, field_175825_e, 6, 2, 6, p_74875_3_);

                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 0, 4, 3, 0, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 0, 4, 3, 1, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 2, 0, 4, 2, 0, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 1, 1, field_175826_b, field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 7, 4, 3, 7, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 3, 6, 4, 3, 7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 2, 7, 4, 2, 7, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 6, 4, 1, 7, field_175826_b, field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 3, 0, 3, 4, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 3, 1, 3, 4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, 3, 0, 2, 4, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 1, 1, 4, field_175826_b, field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 6, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 2, 3, 7, 2, 4, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 6, 1, 3, 7, 1, 4, field_175826_b, field_175826_b, false);
                }
            }
            else if (this.field_175833_o == 1)
            {
                this.func_175804_a(worldIn, p_74875_3_, 2, 1, 2, 2, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 2, 1, 5, 2, 3, 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 5, 5, 3, 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 1, 2, 5, 3, 2, field_175826_b, field_175826_b, false);
                this.func_175811_a(worldIn, field_175825_e, 2, 2, 2, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 2, 2, 5, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 5, 2, 5, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 5, 2, 2, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 1, 3, 0, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 1, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 7, 1, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 6, 0, 3, 6, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 6, 7, 3, 6, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 1, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 1, 7, 3, 1, field_175826_b, field_175826_b, false);
                this.func_175811_a(worldIn, field_175828_a, 1, 2, 0, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 0, 2, 1, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 1, 2, 7, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 0, 2, 6, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 6, 2, 7, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 7, 2, 6, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 6, 2, 0, p_74875_3_);
                this.func_175811_a(worldIn, field_175828_a, 7, 2, 1, p_74875_3_);

                if (!this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, 1, 0, 3, 6, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 2, 1, 0, 2, 6, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 1, 6, field_175826_b, field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 3, 1, 7, 3, 6, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 2, 1, 7, 2, 6, field_175828_a, field_175828_a, false);
                    this.func_175804_a(worldIn, p_74875_3_, 7, 1, 1, 7, 1, 6, field_175826_b, field_175826_b, false);
                }
            }
            else if (this.field_175833_o == 2)
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);

                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175822_f, field_175822_f, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175822_f, field_175822_f, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()])
                {
                    this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, field_175822_f, field_175822_f, false);
                }
            }

            if (var4)
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 3, 4, 1, 4, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 2, 3, 4, 2, 4, field_175828_a, field_175828_a, false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 3, 3, 4, 3, 4, field_175826_b, field_175826_b, false);
            }

            return true;
        }
    }

    public static class SimpleTopRoom extends StructureOceanMonumentPieces.Piece
    {
        private static final String __OBFID = "CL_00001974";

        public SimpleTopRoom() {}

        public SimpleTopRoom(EnumFacing p_i45586_1_, StructureOceanMonumentPieces.RoomDefinition p_i45586_2_, Random p_i45586_3_)
        {
            super(1, p_i45586_1_, p_i45586_2_, 1, 1, 1);
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_175830_k.field_175967_a / 25 > 0)
            {
                this.func_175821_a(worldIn, p_74875_3_, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
            }

            if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null)
            {
                this.func_175819_a(worldIn, p_74875_3_, 1, 4, 1, 6, 4, 6, field_175828_a);
            }

            for (int var4 = 1; var4 <= 6; ++var4)
            {
                for (int var5 = 1; var5 <= 6; ++var5)
                {
                    if (p_74875_2_.nextInt(3) != 0)
                    {
                        int var6 = 2 + (p_74875_2_.nextInt(4) == 0 ? 0 : 1);
                        this.func_175804_a(worldIn, p_74875_3_, var4, var6, var5, var4, 3, var5, Blocks.sponge.getStateFromMeta(1), Blocks.sponge.getStateFromMeta(1), false);
                    }
                }
            }

            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);

            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()])
            {
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 4, 2, 0, field_175822_f, field_175822_f, false);
            }

            return true;
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_175971_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00001993";

        static
        {
            try
            {
                field_175971_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_175971_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_175971_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    public static class WingRoom extends StructureOceanMonumentPieces.Piece
    {
        private int field_175834_o;
        private static final String __OBFID = "CL_00001973";

        public WingRoom() {}

        public WingRoom(EnumFacing p_i45585_1_, StructureBoundingBox p_i45585_2_, int p_i45585_3_)
        {
            super(p_i45585_1_, p_i45585_2_);
            this.field_175834_o = p_i45585_3_ & 1;
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_175834_o == 0)
            {
                int var4;

                for (var4 = 0; var4 < 4; ++var4)
                {
                    this.func_175804_a(worldIn, p_74875_3_, 10 - var4, 3 - var4, 20 - var4, 12 + var4, 3 - var4, 20, field_175826_b, field_175826_b, false);
                }

                this.func_175804_a(worldIn, p_74875_3_, 7, 0, 6, 15, 0, 16, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 0, 6, 6, 3, 20, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 16, 0, 6, 16, 3, 20, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 7, 7, 1, 20, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, 1, 7, 15, 1, 20, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 7, 1, 6, 9, 3, 6, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 1, 6, 15, 3, 6, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 1, 7, 9, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 0, 5, 13, 0, 5, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 10, 0, 7, 12, 0, 7, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 0, 10, 8, 0, 12, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, 0, 10, 14, 0, 12, field_175827_c, field_175827_c, false);

                for (var4 = 18; var4 >= 7; var4 -= 3)
                {
                    this.func_175811_a(worldIn, field_175825_e, 6, 3, var4, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, 16, 3, var4, p_74875_3_);
                }

                this.func_175811_a(worldIn, field_175825_e, 10, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 12, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 10, 0, 12, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 12, 0, 12, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 8, 3, 6, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 14, 3, 6, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 4, 2, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 4, 1, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 4, 0, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 18, 2, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 18, 1, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 18, 0, 4, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 4, 2, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 4, 1, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 4, 0, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 18, 2, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175825_e, 18, 1, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 18, 0, 18, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 9, 7, 20, p_74875_3_);
                this.func_175811_a(worldIn, field_175826_b, 13, 7, 20, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 6, 0, 21, 7, 4, 21, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 15, 0, 21, 16, 4, 21, field_175826_b, field_175826_b, false);
                this.func_175817_a(worldIn, p_74875_3_, 11, 2, 16);
            }
            else if (this.field_175834_o == 1)
            {
                this.func_175804_a(worldIn, p_74875_3_, 9, 3, 18, 13, 3, 20, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 0, 18, 9, 2, 18, field_175826_b, field_175826_b, false);
                this.func_175804_a(worldIn, p_74875_3_, 13, 0, 18, 13, 2, 18, field_175826_b, field_175826_b, false);
                byte var8 = 9;
                byte var5 = 20;
                byte var6 = 5;
                int var7;

                for (var7 = 0; var7 < 2; ++var7)
                {
                    this.func_175811_a(worldIn, field_175826_b, var8, var6 + 1, var5, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, var8, var6, var5, p_74875_3_);
                    this.func_175811_a(worldIn, field_175826_b, var8, var6 - 1, var5, p_74875_3_);
                    var8 = 13;
                }

                this.func_175804_a(worldIn, p_74875_3_, 7, 3, 7, 15, 3, 14, field_175826_b, field_175826_b, false);
                var8 = 10;

                for (var7 = 0; var7 < 2; ++var7)
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, 0, 10, var8, 6, 10, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, 0, 12, var8, 6, 12, field_175826_b, field_175826_b, false);
                    this.func_175811_a(worldIn, field_175825_e, var8, 0, 10, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, var8, 0, 12, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, var8, 4, 10, p_74875_3_);
                    this.func_175811_a(worldIn, field_175825_e, var8, 4, 12, p_74875_3_);
                    var8 = 12;
                }

                var8 = 8;

                for (var7 = 0; var7 < 2; ++var7)
                {
                    this.func_175804_a(worldIn, p_74875_3_, var8, 0, 7, var8, 2, 7, field_175826_b, field_175826_b, false);
                    this.func_175804_a(worldIn, p_74875_3_, var8, 0, 14, var8, 2, 14, field_175826_b, field_175826_b, false);
                    var8 = 14;
                }

                this.func_175804_a(worldIn, p_74875_3_, 8, 3, 8, 8, 3, 13, field_175827_c, field_175827_c, false);
                this.func_175804_a(worldIn, p_74875_3_, 14, 3, 8, 14, 3, 13, field_175827_c, field_175827_c, false);
                this.func_175817_a(worldIn, p_74875_3_, 11, 5, 13);
            }

            return true;
        }
    }

    static class XDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001992";

        private XDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            return p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleXRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        XDoubleRoomFitHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45606_1_)
        {
            this();
        }
    }

    static class XYDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001991";

        private XYDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            if (p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d && p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d)
            {
                StructureOceanMonumentPieces.RoomDefinition var2 = p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()];
                return var2.field_175966_c[EnumFacing.UP.getIndex()] && !var2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            else
            {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleXYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        XYDoubleRoomFitHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45605_1_)
        {
            this();
        }
    }

    static class YDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001990";

        private YDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            return p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        YDoubleRoomFitHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45604_1_)
        {
            this();
        }
    }

    static class YZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001989";

        private YZDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            if (p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d && p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d)
            {
                StructureOceanMonumentPieces.RoomDefinition var2 = p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()];
                return var2.field_175966_c[EnumFacing.UP.getIndex()] && !var2.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
            }
            else
            {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            p_175968_2_.field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleYZRoom(p_175968_1_, p_175968_2_, p_175968_3_);
        }

        YZDoubleRoomFitHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45603_1_)
        {
            this();
        }
    }

    static class ZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper
    {
        private static final String __OBFID = "CL_00001988";

        private ZDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
        {
            return p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
        {
            StructureOceanMonumentPieces.RoomDefinition var4 = p_175968_2_;

            if (!p_175968_2_.field_175966_c[EnumFacing.NORTH.getIndex()] || p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d)
            {
                var4 = p_175968_2_.field_175965_b[EnumFacing.SOUTH.getIndex()];
            }

            var4.field_175963_d = true;
            var4.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleZRoom(p_175968_1_, var4, p_175968_3_);
        }

        ZDoubleRoomFitHelper(StructureOceanMonumentPieces.SwitchEnumFacing p_i45602_1_)
        {
            this();
        }
    }
}
