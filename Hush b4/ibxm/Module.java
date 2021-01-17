// 
// Decompiled by Procyon v0.5.36
// 

package ibxm;

public class Module
{
    public String song_title;
    public boolean linear_periods;
    public boolean fast_volume_slides;
    public int global_volume;
    public int channel_gain;
    public int default_speed;
    public int default_tempo;
    public int restart_sequence_index;
    private int[] initial_panning;
    private int[] sequence;
    private Pattern[] patterns;
    private Instrument[] instruments;
    private Pattern default_pattern;
    private Instrument default_instrument;
    
    public Module() {
        this.song_title = "ibxm alpha 45 (c)2006 mumart@gmail.com";
        this.set_num_channels(1);
        this.set_sequence_length(1);
        this.set_num_patterns(0);
        this.set_num_instruments(0);
        this.default_pattern = new Pattern();
        this.default_instrument = new Instrument();
    }
    
    public int get_num_channels() {
        return this.initial_panning.length;
    }
    
    public void set_num_channels(int num_channels) {
        if (num_channels < 1) {
            num_channels = 1;
        }
        this.initial_panning = new int[num_channels];
    }
    
    public int get_initial_panning(final int channel) {
        int panning = 128;
        if (channel >= 0 && channel < this.initial_panning.length) {
            panning = this.initial_panning[channel];
        }
        return panning;
    }
    
    public void set_initial_panning(final int channel, final int panning) {
        if (channel >= 0 && channel < this.initial_panning.length) {
            this.initial_panning[channel] = panning;
        }
    }
    
    public int get_sequence_length() {
        return this.sequence.length;
    }
    
    public void set_sequence_length(int sequence_length) {
        if (sequence_length < 0) {
            sequence_length = 0;
        }
        this.sequence = new int[sequence_length];
    }
    
    public void set_sequence(final int sequence_index, final int pattern_index) {
        if (sequence_index >= 0 && sequence_index < this.sequence.length) {
            this.sequence[sequence_index] = pattern_index;
        }
    }
    
    public int get_num_patterns() {
        return this.patterns.length;
    }
    
    public void set_num_patterns(int num_patterns) {
        if (num_patterns < 0) {
            num_patterns = 0;
        }
        this.patterns = new Pattern[num_patterns];
    }
    
    public Pattern get_pattern_from_sequence(final int sequence_index) {
        Pattern pattern = this.default_pattern;
        if (sequence_index >= 0 && sequence_index < this.sequence.length) {
            pattern = this.get_pattern(this.sequence[sequence_index]);
        }
        return pattern;
    }
    
    public Pattern get_pattern(final int pattern_index) {
        Pattern pattern = null;
        if (pattern_index >= 0 && pattern_index < this.patterns.length) {
            pattern = this.patterns[pattern_index];
        }
        if (pattern == null) {
            pattern = this.default_pattern;
        }
        return pattern;
    }
    
    public void set_pattern(final int pattern_index, final Pattern pattern) {
        if (pattern_index >= 0 && pattern_index < this.patterns.length) {
            this.patterns[pattern_index] = pattern;
        }
    }
    
    public int get_num_instruments() {
        return this.instruments.length;
    }
    
    public void set_num_instruments(int num_instruments) {
        if (num_instruments < 0) {
            num_instruments = 0;
        }
        this.instruments = new Instrument[num_instruments];
    }
    
    public Instrument get_instrument(final int instrument_index) {
        Instrument instrument = null;
        if (instrument_index > 0 && instrument_index <= this.instruments.length) {
            instrument = this.instruments[instrument_index - 1];
        }
        if (instrument == null) {
            instrument = this.default_instrument;
        }
        return instrument;
    }
    
    public void set_instrument(final int instrument_index, final Instrument instrument) {
        if (instrument_index > 0 && instrument_index <= this.instruments.length) {
            this.instruments[instrument_index - 1] = instrument;
        }
    }
}
