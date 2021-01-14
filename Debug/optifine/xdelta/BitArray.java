package optifine.xdelta;

public class BitArray
{
    int[] implArray;
    int size;
    static final int INT_SIZE = 32;

    public BitArray(int size)
    {
        int i = size / 32 + 1;
        this.implArray = new int[i];
    }

    public void set(int pos, boolean value)
    {
        int i = pos / 32;
        int j = 1 << (pos & 31);

        if (value)
        {
            this.implArray[i] |= j;
        }
        else
        {
            this.implArray[i] &= ~j;
        }
    }

    public boolean get(int pos)
    {
        int i = pos / 32;
        int j = 1 << (pos & 31);
        return (this.implArray[i] & j) != 0;
    }
}
