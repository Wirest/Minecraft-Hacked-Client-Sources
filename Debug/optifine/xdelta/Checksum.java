package optifine.xdelta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Checksum
{
    public static final int BASE = 65521;
    public static final int S = 16;
    public static boolean debug = false;
    protected int[] hashtable;
    protected long[] checksums;
    protected int prime;
    protected static final char[] single_hash = new char[] {48337, 47973, 17090, 57342, 38502, 17179, 34052, 60230, 25465, 54368, 53012, 21455, 56145, 56072, 4808, 62978, 59238, 9108, 9485, 56507, 42616, 687, 42438, 32422, 46661, 52045, 50251, 58844, 40934, 23388, 13813, 28698, 8719, 27704, 6742, 19619, 65478, 45394, 36193, 31320, 36901, 35645, 48911, 38307, 58868, 49447, 15341, 12811, 47091, 24660, 13116, 54147, 33108, 21058, 19981, 2708, 28712, 34441, 14882, 2432, 6215, 45297, 39772, 16758, 47192, 54594, 8044, 9367, 27226, 40873, 35930, 30531, 43177, 39426, 18712, 17292, 50056, 40491, 19629, 438, 43801, 63351, 13919, 7858, 2334, 31736, 31374, 21031, 60081, 8308, 17699, 59265, 419, 5693, 15150, 10365, 24191, 41059, 45364, 36782, 24206, 47031, 17736, 8026, 64086, 31268, 36879, 17116, 52329, 672, 2850, 56113, 29182, 3197, 5938, 4441, 51977, 57810, 4945, 21225, 62774, 23119, 49942, 27641, 35220, 46964, 24382, 63190, 14945, 63532, 52258, 40198, 10652, 2533, 7916, 20815, 36179, 42576, 23662, 50551, 31064, 29100, 35094, 39759, 11273, 21009, 63192, 51882, 63471, 10367, 31380, 43849, 64044, 29218, 58455, 55066, '\u00c3', 6774, 59788, 49207, 33288, 23597, 57306, 58869, 2885, 5582, 35454, 64685, 43565, 19292, 54318, 45649, 36990, 39495, 51622, 55615, 2142, 13774, 41299, 32379, 40715, 9642, 23967, 49229, 35342, 10357, 18972, 10591, 5011, 63328, 37240, 3931, 64125, 33716, 8322, 29213, 25698, 872, 26594, 34340, 6477, 8950, 30971, 26513, 45624, 45874, 29302, 62066, 18412, 17668, 43361, 40904, 16348, 46099, 'z', 2054, 29784, 38342, 52394, 6358, 58030, 6918, 62454, 20560, 51432, 62636, 49228, 62492, 39215, 44612, 24347, 4371, 5944, 55720, 6634, 11571, 38552, 12265, 12863, 52706, 28017, 58237, 46743, 11343, 17267, 37122, 1885, 36389, 5746, 60456, 27339, 34508, 6254, 37908, 54900, 53669};

    public static long queryChecksum(byte[] buf, int len)
    {
        int i = 0;
        int j = 0;

        for (int k = 0; k < len; ++k)
        {
            j += single_hash[buf[k] + 128];
            i += j;
        }

        return (long)((i & 65535) << 16 | j & 65535);
    }

    public static long incrementChecksum(long checksum, byte out, byte in)
    {
        char c0 = single_hash[out + 128];
        char c1 = single_hash[in + 128];
        int i = (int)(checksum & 65535L) - c0 + c1 & 65535;
        int j = (int)(checksum >> 16) - c0 * 16 + i & 65535;
        return (long)(j << 16 | i & 65535);
    }

    public static int generateHash(long checksum)
    {
        long i = checksum >> 16 & 65535L;
        long j = checksum & 65535L;
        long k = (i >> 2) + (j << 3) + (i << 16);
        int l = (int)(k ^ i ^ j);
        return l > 0 ? l : -l;
    }

    public void generateChecksums(File sourceFile, int length) throws IOException
    {
        FileInputStream fileinputstream = new FileInputStream(sourceFile);

        try
        {
            this.generateChecksums((InputStream)fileinputstream, length);
        }
        catch (IOException ioexception)
        {
            throw ioexception;
        }
        finally
        {
            fileinputstream.close();
        }
    }

    public void generateChecksums(InputStream sis, int length) throws IOException
    {
        InputStream inputstream = new BufferedInputStream(sis);
        int i = length / 16;

        if (debug)
        {
            System.out.println("generating checksum array of size " + i);
        }

        this.checksums = new long[i];
        this.hashtable = new int[i];
        this.prime = findClosestPrime(i);

        if (debug)
        {
            System.out.println("using prime " + this.prime);
        }

        for (int j = 0; j < i; ++j)
        {
            byte[] abyte = new byte[16];
            inputstream.read(abyte, 0, 16);
            this.checksums[j] = queryChecksum(abyte, 16);
        }

        for (int k = 0; k < i; ++k)
        {
            this.hashtable[k] = -1;
        }

        for (int l = 0; l < i; ++l)
        {
            int i1 = generateHash(this.checksums[l]) % this.prime;

            if (debug)
            {
                System.out.println("checking with hash: " + i1);
            }

            if (this.hashtable[i1] != -1)
            {
                if (debug)
                {
                    System.out.println("hash table collision for index " + l);
                }
            }
            else
            {
                this.hashtable[i1] = l;
            }
        }
    }

    public int findChecksumIndex(long checksum)
    {
        return this.hashtable[generateHash(checksum) % this.prime];
    }

    private static int findClosestPrime(int size)
    {
        int i = (int)SimplePrime.belowOrEqual((long)(size - 1));
        return i < 2 ? 1 : i;
    }

    private String printIntArray(int[] a)
    {
        String s = "";
        s = s + "[";

        for (int i = 0; i < a.length; ++i)
        {
            s = s + a[i];

            if (i != a.length - 1)
            {
                s = s + ",";
            }
            else
            {
                s = s + "]";
            }
        }

        return s;
    }

    private String printLongArray(long[] a)
    {
        String s = "";
        s = s + "[";

        for (int i = 0; i < a.length; ++i)
        {
            s = s + a[i];

            if (i != a.length - 1)
            {
                s = s + ",";
            }
            else
            {
                s = s + "]";
            }
        }

        return s;
    }
}
