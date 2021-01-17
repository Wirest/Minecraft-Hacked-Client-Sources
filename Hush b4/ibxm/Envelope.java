// 
// Decompiled by Procyon v0.5.36
// 

package ibxm;

public class Envelope
{
    public boolean sustain;
    public boolean looped;
    private int sustain_tick;
    private int loop_start_tick;
    private int loop_end_tick;
    private int[] ticks;
    private int[] ampls;
    
    public Envelope() {
        this.set_num_points(1);
    }
    
    public void set_num_points(int num_points) {
        if (num_points <= 0) {
            num_points = 1;
        }
        this.ticks = new int[num_points];
        this.ampls = new int[num_points];
        this.set_point(0, 0, 0);
    }
    
    public void set_point(int point, int tick, final int ampl) {
        if (point >= 0 && point < this.ticks.length) {
            if (point == 0) {
                tick = 0;
            }
            if (point > 0) {
                if (tick < this.ticks[point - 1]) {
                    tick += 256;
                }
                if (tick <= this.ticks[point - 1]) {
                    System.out.println("Envelope: Point not valid (" + tick + " <= " + this.ticks[point - 1] + ")");
                    tick = this.ticks[point - 1] + 1;
                }
            }
            this.ticks[point] = tick;
            this.ampls[point] = ampl;
            ++point;
            while (point < this.ticks.length) {
                this.ticks[point] = this.ticks[point - 1] + 1;
                this.ampls[point] = 0;
                ++point;
            }
        }
    }
    
    public void set_sustain_point(int point) {
        if (point < 0) {
            point = 0;
        }
        if (point >= this.ticks.length) {
            point = this.ticks.length - 1;
        }
        this.sustain_tick = this.ticks[point];
    }
    
    public void set_loop_points(int start, int end) {
        if (start < 0) {
            start = 0;
        }
        if (start >= this.ticks.length) {
            start = this.ticks.length - 1;
        }
        if (end < start || end >= this.ticks.length) {
            end = start;
        }
        this.loop_start_tick = this.ticks[start];
        this.loop_end_tick = this.ticks[end];
    }
    
    public int next_tick(int tick, final boolean key_on) {
        ++tick;
        if (this.looped && tick >= this.loop_end_tick) {
            tick = this.loop_start_tick;
        }
        if (this.sustain && key_on && tick >= this.sustain_tick) {
            tick = this.sustain_tick;
        }
        return tick;
    }
    
    public int calculate_ampl(final int tick) {
        int ampl = this.ampls[this.ticks.length - 1];
        if (tick < this.ticks[this.ticks.length - 1]) {
            int point = 0;
            for (int idx = 1; idx < this.ticks.length; ++idx) {
                if (this.ticks[idx] <= tick) {
                    point = idx;
                }
            }
            final int delta_t = this.ticks[point + 1] - this.ticks[point];
            final int delta_a = this.ampls[point + 1] - this.ampls[point];
            ampl = (delta_a << 15) / delta_t;
            ampl = ampl * (tick - this.ticks[point]) >> 15;
            ampl += this.ampls[point];
        }
        return ampl;
    }
    
    public void dump() {
        for (int idx = 0; idx < this.ticks.length; ++idx) {
            System.out.println(String.valueOf(this.ticks[idx]) + ", " + this.ampls[idx]);
        }
        for (int tick = 0; tick < 222; ++tick) {
            System.out.print(String.valueOf(this.calculate_ampl(tick)) + ", ");
        }
    }
}
