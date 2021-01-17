// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class IntArray
{
    private int[] array;
    private int position;
    private int limit;
    
    public IntArray(final int p_i62_1_) {
        this.array = null;
        this.position = 0;
        this.limit = 0;
        this.array = new int[p_i62_1_];
    }
    
    public void put(final int p_put_1_) {
        this.array[this.position] = p_put_1_;
        ++this.position;
        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }
    
    public void put(final int p_put_1_, final int p_put_2_) {
        this.array[p_put_1_] = p_put_2_;
        if (this.limit < p_put_1_) {
            this.limit = p_put_1_;
        }
    }
    
    public void position(final int p_position_1_) {
        this.position = p_position_1_;
    }
    
    public void put(final int[] p_put_1_) {
        for (int i = p_put_1_.length, j = 0; j < i; ++j) {
            this.array[this.position] = p_put_1_[j];
            ++this.position;
        }
        if (this.limit < this.position) {
            this.limit = this.position;
        }
    }
    
    public int get(final int p_get_1_) {
        return this.array[p_get_1_];
    }
    
    public int[] getArray() {
        return this.array;
    }
    
    public void clear() {
        this.position = 0;
        this.limit = 0;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public int getPosition() {
        return this.position;
    }
}
