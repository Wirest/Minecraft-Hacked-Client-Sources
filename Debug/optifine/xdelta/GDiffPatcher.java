package optifine.xdelta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class GDiffPatcher
{
    public GDiffPatcher(File sourceFile, File patchFile, File outputFile) throws IOException, PatchException
    {
        RandomAccessFileSeekableSource randomaccessfileseekablesource = new RandomAccessFileSeekableSource(new RandomAccessFile(sourceFile, "r"));
        InputStream inputstream = new FileInputStream(patchFile);
        OutputStream outputstream = new FileOutputStream(outputFile);

        try
        {
            runPatch(randomaccessfileseekablesource, inputstream, outputstream);
        }
        catch (IOException ioexception)
        {
            throw ioexception;
        }
        catch (PatchException patchexception)
        {
            throw patchexception;
        }
        finally
        {
            randomaccessfileseekablesource.close();
            inputstream.close();
            outputstream.close();
        }
    }

    public GDiffPatcher(byte[] source, InputStream patch, OutputStream output) throws IOException, PatchException
    {
        this((SeekableSource)(new ByteArraySeekableSource(source)), (InputStream)patch, (OutputStream)output);
    }

    public GDiffPatcher(SeekableSource source, InputStream patch, OutputStream out) throws IOException, PatchException
    {
        runPatch(source, patch, out);
    }

    private static void runPatch(SeekableSource source, InputStream patch, OutputStream out) throws IOException, PatchException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(out);
        DataInputStream datainputstream = new DataInputStream(patch);

        try
        {
            byte[] abyte = new byte[256];
            int i = 0;

            if (datainputstream.readUnsignedByte() == 209 && datainputstream.readUnsignedByte() == 255 && datainputstream.readUnsignedByte() == 209 && datainputstream.readUnsignedByte() == 255 && datainputstream.readUnsignedByte() == 4)
            {
                i = i + 5;

                while (datainputstream.available() > 0)
                {
                    int j = datainputstream.readUnsignedByte();
                    int k = 0;
                    int l = 0;

                    switch (j)
                    {
                        case 0:
                            break;

                        case 1:
                            append(1, datainputstream, dataoutputstream);
                            i += 2;
                            break;

                        case 2:
                            append(2, datainputstream, dataoutputstream);
                            i += 3;
                            break;

                        case 246:
                            append(246, datainputstream, dataoutputstream);
                            i += 247;
                            break;

                        case 247:
                            k = datainputstream.readUnsignedShort();
                            append(k, datainputstream, dataoutputstream);
                            i += k + 3;
                            break;

                        case 248:
                            k = datainputstream.readInt();
                            append(k, datainputstream, dataoutputstream);
                            i += k + 5;
                            break;

                        case 249:
                            l = datainputstream.readUnsignedShort();
                            k = datainputstream.readUnsignedByte();
                            copy((long)l, k, source, dataoutputstream);
                            i += 4;
                            break;

                        case 250:
                            l = datainputstream.readUnsignedShort();
                            k = datainputstream.readUnsignedShort();
                            copy((long)l, k, source, dataoutputstream);
                            i += 5;
                            break;

                        case 251:
                            l = datainputstream.readUnsignedShort();
                            k = datainputstream.readInt();
                            copy((long)l, k, source, dataoutputstream);
                            i += 7;
                            break;

                        case 252:
                            l = datainputstream.readInt();
                            k = datainputstream.readUnsignedByte();
                            copy((long)l, k, source, dataoutputstream);
                            i += 8;
                            break;

                        case 253:
                            l = datainputstream.readInt();
                            k = datainputstream.readUnsignedShort();
                            copy((long)l, k, source, dataoutputstream);
                            i += 7;
                            break;

                        case 254:
                            l = datainputstream.readInt();
                            k = datainputstream.readInt();
                            copy((long)l, k, source, dataoutputstream);
                            i += 9;
                            break;

                        case 255:
                            long i1 = datainputstream.readLong();
                            k = datainputstream.readInt();
                            copy(i1, k, source, dataoutputstream);
                            i += 13;
                            break;

                        default:
                            append(j, datainputstream, dataoutputstream);
                            i += j + 1;
                    }
                }

                return;
            }

            System.err.println("magic string not found, aborting!");
        }
        catch (PatchException patchexception)
        {
            throw patchexception;
        }
        finally
        {
            dataoutputstream.flush();
        }
    }

    protected static void copy(long offset, int length, SeekableSource source, OutputStream output) throws IOException, PatchException
    {
        if (offset + (long)length > source.length())
        {
            throw new PatchException("truncated source file, aborting");
        }
        else
        {
            byte[] abyte = new byte[256];
            source.seek(offset);

            while (length > 0)
            {
                int i = length > 256 ? 256 : length;
                int j = source.read(abyte, 0, i);
                output.write(abyte, 0, j);
                length -= j;
            }
        }
    }

    protected static void append(int length, InputStream patch, OutputStream output) throws IOException
    {
        int j;

        for (byte[] abyte = new byte[256]; length > 0; length -= j)
        {
            int i = length > 256 ? 256 : length;
            j = patch.read(abyte, 0, i);
            output.write(abyte, 0, j);
        }
    }

    public static void main(String[] argv)
    {
        if (argv.length != 3)
        {
            System.err.println("usage GDiffPatch source patch output");
            System.err.println("aborting..");
        }
        else
        {
            try
            {
                File file1 = new File(argv[0]);
                File file2 = new File(argv[1]);
                File file3 = new File(argv[2]);

                if (file1.length() > 2147483647L || file2.length() > 2147483647L)
                {
                    System.err.println("source or patch is too large, max length is 2147483647");
                    System.err.println("aborting..");
                    return;
                }

                new GDiffPatcher(file1, file2, file3);
                System.out.println("finished patching file");
            }
            catch (Exception exception)
            {
                System.err.println("error while patching: " + exception);
            }
        }
    }
}
