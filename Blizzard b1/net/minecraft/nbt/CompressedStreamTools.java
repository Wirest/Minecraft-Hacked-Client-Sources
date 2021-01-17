package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

public class CompressedStreamTools
{
    private static final String __OBFID = "CL_00001226";

    /**
     * Load the gzipped compound from the inputstream.
     */
    public static NBTTagCompound readCompressed(InputStream p_74796_0_) throws IOException
    {
        DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(p_74796_0_)));
        NBTTagCompound var2;

        try
        {
            var2 = func_152456_a(var1, NBTSizeTracker.INFINITE);
        }
        finally
        {
            var1.close();
        }

        return var2;
    }

    /**
     * Write the compound, gzipped, to the outputstream.
     */
    public static void writeCompressed(NBTTagCompound p_74799_0_, OutputStream p_74799_1_) throws IOException
    {
        DataOutputStream var2 = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(p_74799_1_)));

        try
        {
            write(p_74799_0_, var2);
        }
        finally
        {
            var2.close();
        }
    }

    public static void safeWrite(NBTTagCompound p_74793_0_, File p_74793_1_) throws IOException
    {
        File var2 = new File(p_74793_1_.getAbsolutePath() + "_tmp");

        if (var2.exists())
        {
            var2.delete();
        }

        write(p_74793_0_, var2);

        if (p_74793_1_.exists())
        {
            p_74793_1_.delete();
        }

        if (p_74793_1_.exists())
        {
            throw new IOException("Failed to delete " + p_74793_1_);
        }
        else
        {
            var2.renameTo(p_74793_1_);
        }
    }

    public static void write(NBTTagCompound p_74795_0_, File p_74795_1_) throws IOException
    {
        DataOutputStream var2 = new DataOutputStream(new FileOutputStream(p_74795_1_));

        try
        {
            write(p_74795_0_, var2);
        }
        finally
        {
            var2.close();
        }
    }

    public static NBTTagCompound read(File p_74797_0_) throws IOException
    {
        if (!p_74797_0_.exists())
        {
            return null;
        }
        else
        {
            DataInputStream var1 = new DataInputStream(new FileInputStream(p_74797_0_));
            NBTTagCompound var2;

            try
            {
                var2 = func_152456_a(var1, NBTSizeTracker.INFINITE);
            }
            finally
            {
                var1.close();
            }

            return var2;
        }
    }

    /**
     * Reads from a CompressedStream.
     */
    public static NBTTagCompound read(DataInputStream p_74794_0_) throws IOException
    {
        return func_152456_a(p_74794_0_, NBTSizeTracker.INFINITE);
    }

    public static NBTTagCompound func_152456_a(DataInput p_152456_0_, NBTSizeTracker p_152456_1_) throws IOException
    {
        NBTBase var2 = func_152455_a(p_152456_0_, 0, p_152456_1_);

        if (var2 instanceof NBTTagCompound)
        {
            return (NBTTagCompound)var2;
        }
        else
        {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound p_74800_0_, DataOutput p_74800_1_) throws IOException
    {
        writeTag(p_74800_0_, p_74800_1_);
    }

    private static void writeTag(NBTBase p_150663_0_, DataOutput p_150663_1_) throws IOException
    {
        p_150663_1_.writeByte(p_150663_0_.getId());

        if (p_150663_0_.getId() != 0)
        {
            p_150663_1_.writeUTF("");
            p_150663_0_.write(p_150663_1_);
        }
    }

    private static NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_) throws IOException
    {
        byte var3 = p_152455_0_.readByte();

        if (var3 == 0)
        {
            return new NBTTagEnd();
        }
        else
        {
            p_152455_0_.readUTF();
            NBTBase var4 = NBTBase.createNewByType(var3);

            try
            {
                var4.read(p_152455_0_, p_152455_1_, p_152455_2_);
                return var4;
            }
            catch (IOException var8)
            {
                CrashReport var6 = CrashReport.makeCrashReport(var8, "Loading NBT data");
                CrashReportCategory var7 = var6.makeCategory("NBT Tag");
                var7.addCrashSection("Tag name", "[UNNAMED TAG]");
                var7.addCrashSection("Tag type", Byte.valueOf(var3));
                throw new ReportedException(var6);
            }
        }
    }
}
