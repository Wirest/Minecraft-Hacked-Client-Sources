// 
// Decompiled by Procyon v0.5.36
// 

package ibxm;

public class Sample
{
    public String name;
    public boolean set_panning;
    public int volume;
    public int panning;
    public int c2_rate;
    public int relative_note;
    public int fine_tune;
    private int loop_start;
    private int loop_length;
    private short[] sample_data;
    private static final int POINT_SHIFT = 4;
    private static final int POINTS = 16;
    private static final int OVERLAP = 8;
    private static final int INTERP_SHIFT = 11;
    private static final int INTERP_BITMASK = 2047;
    private static final short[] sinc_table;
    
    static {
        sinc_table = new short[] { 0, -7, 27, -71, 142, -227, 299, 32439, 299, -227, 142, -71, 27, -7, 0, 0, 0, 0, -5, 36, -142, 450, -1439, 32224, 2302, -974, 455, -190, 64, -15, 2, 0, 0, 6, -33, 128, -391, 1042, -2894, 31584, 4540, -1765, 786, -318, 105, -25, 3, 0, 0, 10, -55, 204, -597, 1533, -4056, 30535, 6977, -2573, 1121, -449, 148, -36, 5, 0, -1, 13, -71, 261, -757, 1916, -4922, 29105, 9568, -3366, 1448, -578, 191, -47, 7, 0, -1, 15, -81, 300, -870, 2185, -5498, 27328, 12263, -4109, 1749, -698, 232, -58, 9, 0, -1, 15, -86, 322, -936, 2343, -5800, 25249, 15006, -4765, 2011, -802, 269, -68, 10, 0, -1, 15, -87, 328, -957, 2394, -5849, 22920, 17738, -5298, 2215, -885, 299, -77, 12, 0, 0, 14, -83, 319, -938, 2347, -5671, 20396, 20396, -5671, 2347, -938, 319, -83, 14, 0, 0, 12, -77, 299, -885, 2215, -5298, 17738, 22920, -5849, 2394, -957, 328, -87, 15, -1, 0, 10, -68, 269, -802, 2011, -4765, 15006, 25249, -5800, 2343, -936, 322, -86, 15, -1, 0, 9, -58, 232, -698, 1749, -4109, 12263, 27328, -5498, 2185, -870, 300, -81, 15, -1, 0, 7, -47, 191, -578, 1448, -3366, 9568, 29105, -4922, 1916, -757, 261, -71, 13, -1, 0, 5, -36, 148, -449, 1121, -2573, 6977, 30535, -4056, 1533, -597, 204, -55, 10, 0, 0, 3, -25, 105, -318, 786, -1765, 4540, 31584, -2894, 1042, -391, 128, -33, 6, 0, 0, 2, -15, 64, -190, 455, -974, 2302, 32224, -1439, 450, -142, 36, -5, 0, 0, 0, 0, -7, 27, -71, 142, -227, 299, 32439, 299, -227, 142, -71, 27, -7, 0 };
    }
    
    public Sample() {
        this.name = "";
        this.c2_rate = 8363;
        this.set_sample_data(new short[0], 0, 0, false);
    }
    
    public void set_sample_data(final short[] data, int loop_start, int loop_length, final boolean ping_pong) {
        if (loop_start < 0) {
            loop_start = 0;
        }
        if (loop_start >= data.length) {
            loop_start = data.length - 1;
        }
        if (loop_start + loop_length > data.length) {
            loop_length = data.length - loop_start;
        }
        if (loop_length <= 1) {
            System.arraycopy(data, 0, this.sample_data = new short[8 + data.length + 24], 8, data.length);
            for (int offset = 0; offset < 8; ++offset) {
                short sample = this.sample_data[8 + data.length - 1];
                sample = (short)(sample * (8 - offset) / 8);
                this.sample_data[8 + data.length + offset] = sample;
            }
            loop_start = 8 + data.length + 8;
            loop_length = 1;
        }
        else {
            if (ping_pong) {
                System.arraycopy(data, 0, this.sample_data = new short[8 + loop_start + loop_length * 2 + 16], 8, loop_start + loop_length);
                for (int offset = 0; offset < loop_length; ++offset) {
                    final short sample = data[loop_start + loop_length - offset - 1];
                    this.sample_data[8 + loop_start + loop_length + offset] = sample;
                }
                loop_start += 8;
                loop_length *= 2;
            }
            else {
                System.arraycopy(data, 0, this.sample_data = new short[8 + loop_start + loop_length + 16], 8, loop_start + loop_length);
                loop_start += 8;
            }
            for (int offset = 0; offset < 16; ++offset) {
                final short sample = this.sample_data[loop_start + offset];
                this.sample_data[loop_start + loop_length + offset] = sample;
            }
        }
        this.loop_start = loop_start;
        this.loop_length = loop_length;
    }
    
    public void resample_nearest(int sample_idx, int sample_frac, final int step, final int left_gain, final int right_gain, final int[] mix_buffer, final int frame_offset, final int frames) {
        sample_idx += 8;
        final int loop_end = this.loop_start + this.loop_length - 1;
        for (int offset = frame_offset << 1, end = frame_offset + frames - 1 << 1; offset <= end; offset += 2, sample_frac += step, sample_idx += sample_frac >> 15, sample_frac &= 0x7FFF) {
            if (sample_idx > loop_end) {
                if (this.loop_length <= 1) {
                    break;
                }
                sample_idx = this.loop_start + (sample_idx - this.loop_start) % this.loop_length;
            }
            final int n = offset;
            mix_buffer[n] += this.sample_data[sample_idx] * left_gain >> 15;
            final int n2 = offset + 1;
            mix_buffer[n2] += this.sample_data[sample_idx] * right_gain >> 15;
        }
    }
    
    public void resample_linear(int sample_idx, int sample_frac, final int step, final int left_gain, final int right_gain, final int[] mix_buffer, final int frame_offset, final int frames) {
        sample_idx += 8;
        final int loop_end = this.loop_start + this.loop_length - 1;
        for (int offset = frame_offset << 1, end = frame_offset + frames - 1 << 1; offset <= end; offset += 2, sample_frac += step, sample_idx += sample_frac >> 15, sample_frac &= 0x7FFF) {
            if (sample_idx > loop_end) {
                if (this.loop_length <= 1) {
                    break;
                }
                sample_idx = this.loop_start + (sample_idx - this.loop_start) % this.loop_length;
            }
            int amplitude = this.sample_data[sample_idx];
            amplitude += (this.sample_data[sample_idx + 1] - amplitude) * sample_frac >> 15;
            final int n = offset;
            mix_buffer[n] += amplitude * left_gain >> 15;
            final int n2 = offset + 1;
            mix_buffer[n2] += amplitude * right_gain >> 15;
        }
    }
    
    public void resample_sinc(int sample_idx, int sample_frac, final int step, final int left_gain, final int right_gain, final int[] mix_buffer, final int frame_offset, final int frames) {
        final int loop_end = this.loop_start + this.loop_length - 1;
        for (int offset = frame_offset << 1, end = frame_offset + frames - 1 << 1; offset <= end; offset += 2, sample_frac += step, sample_idx += sample_frac >> 15, sample_frac &= 0x7FFF) {
            if (sample_idx > loop_end) {
                if (this.loop_length <= 1) {
                    break;
                }
                sample_idx = this.loop_start + (sample_idx - this.loop_start) % this.loop_length;
            }
            final int table_idx = sample_frac >> 11 << 4;
            int a1 = Sample.sinc_table[table_idx + 0] * this.sample_data[sample_idx + 0] >> 15;
            a1 += Sample.sinc_table[table_idx + 1] * this.sample_data[sample_idx + 1] >> 15;
            a1 += Sample.sinc_table[table_idx + 2] * this.sample_data[sample_idx + 2] >> 15;
            a1 += Sample.sinc_table[table_idx + 3] * this.sample_data[sample_idx + 3] >> 15;
            a1 += Sample.sinc_table[table_idx + 4] * this.sample_data[sample_idx + 4] >> 15;
            a1 += Sample.sinc_table[table_idx + 5] * this.sample_data[sample_idx + 5] >> 15;
            a1 += Sample.sinc_table[table_idx + 6] * this.sample_data[sample_idx + 6] >> 15;
            a1 += Sample.sinc_table[table_idx + 7] * this.sample_data[sample_idx + 7] >> 15;
            a1 += Sample.sinc_table[table_idx + 8] * this.sample_data[sample_idx + 8] >> 15;
            a1 += Sample.sinc_table[table_idx + 9] * this.sample_data[sample_idx + 9] >> 15;
            a1 += Sample.sinc_table[table_idx + 10] * this.sample_data[sample_idx + 10] >> 15;
            a1 += Sample.sinc_table[table_idx + 11] * this.sample_data[sample_idx + 11] >> 15;
            a1 += Sample.sinc_table[table_idx + 12] * this.sample_data[sample_idx + 12] >> 15;
            a1 += Sample.sinc_table[table_idx + 13] * this.sample_data[sample_idx + 13] >> 15;
            a1 += Sample.sinc_table[table_idx + 14] * this.sample_data[sample_idx + 14] >> 15;
            a1 += Sample.sinc_table[table_idx + 15] * this.sample_data[sample_idx + 15] >> 15;
            int a2 = Sample.sinc_table[table_idx + 16] * this.sample_data[sample_idx + 0] >> 15;
            a2 += Sample.sinc_table[table_idx + 17] * this.sample_data[sample_idx + 1] >> 15;
            a2 += Sample.sinc_table[table_idx + 18] * this.sample_data[sample_idx + 2] >> 15;
            a2 += Sample.sinc_table[table_idx + 19] * this.sample_data[sample_idx + 3] >> 15;
            a2 += Sample.sinc_table[table_idx + 20] * this.sample_data[sample_idx + 4] >> 15;
            a2 += Sample.sinc_table[table_idx + 21] * this.sample_data[sample_idx + 5] >> 15;
            a2 += Sample.sinc_table[table_idx + 22] * this.sample_data[sample_idx + 6] >> 15;
            a2 += Sample.sinc_table[table_idx + 23] * this.sample_data[sample_idx + 7] >> 15;
            a2 += Sample.sinc_table[table_idx + 24] * this.sample_data[sample_idx + 8] >> 15;
            a2 += Sample.sinc_table[table_idx + 25] * this.sample_data[sample_idx + 9] >> 15;
            a2 += Sample.sinc_table[table_idx + 26] * this.sample_data[sample_idx + 10] >> 15;
            a2 += Sample.sinc_table[table_idx + 27] * this.sample_data[sample_idx + 11] >> 15;
            a2 += Sample.sinc_table[table_idx + 28] * this.sample_data[sample_idx + 12] >> 15;
            a2 += Sample.sinc_table[table_idx + 29] * this.sample_data[sample_idx + 13] >> 15;
            a2 += Sample.sinc_table[table_idx + 30] * this.sample_data[sample_idx + 14] >> 15;
            a2 += Sample.sinc_table[table_idx + 31] * this.sample_data[sample_idx + 15] >> 15;
            final int amplitude = a1 + ((a2 - a1) * (sample_frac & 0x7FF) >> 11);
            final int n = offset;
            mix_buffer[n] += amplitude * left_gain >> 15;
            final int n2 = offset + 1;
            mix_buffer[n2] += amplitude * right_gain >> 15;
        }
    }
    
    public boolean has_finished(final int sample_idx) {
        boolean finished = false;
        if (this.loop_length <= 1 && sample_idx > this.loop_start) {
            finished = true;
        }
        return finished;
    }
}
