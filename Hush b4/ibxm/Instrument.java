// 
// Decompiled by Procyon v0.5.36
// 

package ibxm;

public class Instrument
{
    public String name;
    public int vibrato_type;
    public int vibrato_sweep;
    public int vibrato_depth;
    public int vibrato_rate;
    public boolean volume_envelope_active;
    public boolean panning_envelope_active;
    public int volume_fade_out;
    private Envelope volume_envelope;
    private Envelope panning_envelope;
    private int[] key_to_sample;
    private Sample[] samples;
    
    public Instrument() {
        this.name = "";
        this.set_volume_envelope(new Envelope());
        this.set_panning_envelope(new Envelope());
        this.key_to_sample = new int[96];
        this.set_num_samples(1);
    }
    
    public Envelope get_volume_envelope() {
        return this.volume_envelope;
    }
    
    public void set_volume_envelope(final Envelope envelope) {
        if (envelope != null) {
            this.volume_envelope = envelope;
        }
    }
    
    public Envelope get_panning_envelope() {
        return this.panning_envelope;
    }
    
    public void set_panning_envelope(final Envelope envelope) {
        if (envelope != null) {
            this.panning_envelope = envelope;
        }
    }
    
    public Sample get_sample_from_key(final int key) {
        int sample_idx = 0;
        if (key > 0 && key <= this.key_to_sample.length) {
            sample_idx = this.key_to_sample[key - 1];
        }
        return this.get_sample(sample_idx);
    }
    
    public void set_key_to_sample(final int key, final int sample) {
        if (key > 0 && key <= this.key_to_sample.length) {
            this.key_to_sample[key - 1] = sample;
        }
    }
    
    public int get_num_samples() {
        return this.samples.length;
    }
    
    public void set_num_samples(int num_samples) {
        if (num_samples < 1) {
            num_samples = 1;
        }
        this.samples = new Sample[num_samples];
        this.set_sample(0, null);
    }
    
    public Sample get_sample(final int sample_index) {
        Sample sample = null;
        if (sample_index >= 0 && sample_index < this.samples.length) {
            sample = this.samples[sample_index];
        }
        if (sample == null) {
            sample = this.samples[0];
        }
        return sample;
    }
    
    public void set_sample(final int sample_index, final Sample sample) {
        if (sample_index >= 0 && sample_index < this.samples.length) {
            this.samples[sample_index] = sample;
        }
        if (this.samples[0] == null) {
            this.samples[0] = new Sample();
        }
    }
}
