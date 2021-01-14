package net.minecraft.optifine;

public class IntArray {
    private int[] array = null;
    private int position = 0;
    private int limit = 0;

    public IntArray(int size) {
        array = new int[size];
    }

    public void put(int x) {
        array[position] = x;
        ++position;

        if (limit < position) {
            limit = position;
        }
    }

    public void put(int pos, int x) {
        array[pos] = x;

        if (limit < pos) {
            limit = pos;
        }
    }

    public void position(int pos) {
        position = pos;
    }

    public void put(int[] ints) {
        int len = ints.length;

        for (int i = 0; i < len; ++i) {
            array[position] = ints[i];
            ++position;
        }

        if (limit < position) {
            limit = position;
        }
    }

    public int get(int pos) {
        return array[pos];
    }

    public int[] getArray() {
        return array;
    }

    public void clear() {
        position = 0;
        limit = 0;
    }

    public int getLimit() {
        return limit;
    }

    public int getPosition() {
        return position;
    }
}
