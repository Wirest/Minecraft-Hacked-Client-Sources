// 
// Decompiled by Procyon v0.5.36
// 

package ibxm;

import java.io.UnsupportedEncodingException;
import java.io.EOFException;
import java.io.IOException;
import java.io.DataInput;

public class ScreamTracker3
{
    private static final int[] effect_map;
    private static final int[] effect_s_map;
    
    static {
        effect_map = new int[] { 255, 37, 11, 13, 10, 2, 1, 3, 4, 29, 0, 6, 5, 255, 255, 9, 255, 27, 7, 14, 15, 36, 16, 255, 255, 255, 255, 255, 255, 255, 255, 255 };
        effect_s_map = new int[] { 0, 3, 5, 4, 7, 255, 255, 255, 8, 255, 9, 6, 12, 13, 14, 15 };
    }
    
    public static boolean is_s3m(final byte[] header_96_bytes) {
        final String s3m_identifier = ascii_text(header_96_bytes, 44, 4);
        return s3m_identifier.equals("SCRM");
    }
    
    public static Module load_s3m(final byte[] header_96_bytes, final DataInput data_input) throws IOException {
        final byte[] s3m_file = read_s3m_file(header_96_bytes, data_input);
        final Module module = new Module();
        module.song_title = ascii_text(s3m_file, 0, 28);
        final int num_pattern_orders = get_num_pattern_orders(s3m_file);
        final int num_instruments = get_num_instruments(s3m_file);
        final int num_patterns = get_num_patterns(s3m_file);
        int flags = unsigned_short_le(s3m_file, 38);
        final int tracker_version = unsigned_short_le(s3m_file, 40);
        if ((flags & 0x40) == 0x40 || tracker_version == 4864) {
            module.fast_volume_slides = true;
        }
        boolean signed_samples = false;
        if (unsigned_short_le(s3m_file, 42) == 1) {
            signed_samples = true;
        }
        module.global_volume = (s3m_file[48] & 0xFF);
        module.default_speed = (s3m_file[49] & 0xFF);
        module.default_tempo = (s3m_file[50] & 0xFF);
        final int master_volume = s3m_file[51] & 0x7F;
        module.channel_gain = master_volume << 15 >> 7;
        final boolean stereo_mode = (s3m_file[51] & 0x80) == 0x80;
        final boolean default_panning = (s3m_file[53] & 0xFF) == 0xFC;
        final int[] channel_map = new int[32];
        int num_channels = 0;
        for (int channel_idx = 0; channel_idx < 32; ++channel_idx) {
            final int channel_config = s3m_file[64 + channel_idx] & 0xFF;
            channel_map[channel_idx] = -1;
            if (channel_config < 16) {
                channel_map[channel_idx] = num_channels;
                ++num_channels;
            }
        }
        module.set_num_channels(num_channels);
        final int panning_offset = 96 + num_pattern_orders + num_instruments * 2 + num_patterns * 2;
        for (int channel_idx = 0; channel_idx < 32; ++channel_idx) {
            final int channel_config = s3m_file[64 + channel_idx] & 0xFF;
            if (channel_map[channel_idx] >= 0) {
                int panning = 128;
                if (stereo_mode) {
                    if (channel_config < 8) {
                        panning = 64;
                    }
                    else {
                        panning = 192;
                    }
                }
                if (default_panning) {
                    flags = (s3m_file[panning_offset + channel_idx] & 0xFF);
                    if ((flags & 0x20) == 0x20) {
                        panning = (flags & 0xF) << 4;
                    }
                }
                module.set_initial_panning(channel_map[channel_idx], panning);
            }
        }
        final int[] sequence = read_s3m_sequence(s3m_file);
        module.set_sequence_length(sequence.length);
        for (int order_idx = 0; order_idx < sequence.length; ++order_idx) {
            module.set_sequence(order_idx, sequence[order_idx]);
        }
        module.set_num_instruments(num_instruments);
        for (int instrument_idx = 0; instrument_idx < num_instruments; ++instrument_idx) {
            final Instrument instrument = read_s3m_instrument(s3m_file, instrument_idx, signed_samples);
            module.set_instrument(instrument_idx + 1, instrument);
        }
        module.set_num_patterns(num_patterns);
        for (int pattern_idx = 0; pattern_idx < num_patterns; ++pattern_idx) {
            module.set_pattern(pattern_idx, read_s3m_pattern(s3m_file, pattern_idx, channel_map));
        }
        return module;
    }
    
    private static int[] read_s3m_sequence(final byte[] s3m_file) {
        final int num_pattern_orders = get_num_pattern_orders(s3m_file);
        int sequence_length = 0;
        for (int order_idx = 0; order_idx < num_pattern_orders; ++order_idx) {
            final int pattern_order = s3m_file[96 + order_idx] & 0xFF;
            if (pattern_order == 255) {
                break;
            }
            if (pattern_order < 254) {
                ++sequence_length;
            }
        }
        final int[] sequence = new int[sequence_length];
        int sequence_idx = 0;
        for (int order_idx = 0; order_idx < num_pattern_orders; ++order_idx) {
            final int pattern_order = s3m_file[96 + order_idx] & 0xFF;
            if (pattern_order == 255) {
                break;
            }
            if (pattern_order < 254) {
                sequence[sequence_idx] = pattern_order;
                ++sequence_idx;
            }
        }
        return sequence;
    }
    
    private static Instrument read_s3m_instrument(final byte[] s3m_file, final int instrument_idx, final boolean signed_samples) {
        final int instrument_offset = get_instrument_offset(s3m_file, instrument_idx);
        final Instrument instrument = new Instrument();
        instrument.name = ascii_text(s3m_file, instrument_offset + 48, 28);
        final Sample sample = new Sample();
        if (s3m_file[instrument_offset] == 1) {
            final int sample_data_length = get_sample_data_length(s3m_file, instrument_offset);
            final int loop_start = unsigned_short_le(s3m_file, instrument_offset + 20);
            int loop_length = unsigned_short_le(s3m_file, instrument_offset + 24) - loop_start;
            sample.volume = (s3m_file[instrument_offset + 28] & 0xFF);
            if (s3m_file[instrument_offset + 30] != 0) {
                throw new IllegalArgumentException("ScreamTracker3: Packed samples not supported!");
            }
            if ((s3m_file[instrument_offset + 31] & 0x1) == 0x0) {
                loop_length = 0;
            }
            sample.c2_rate = unsigned_short_le(s3m_file, instrument_offset + 32);
            final short[] sample_data = new short[sample_data_length];
            final int sample_data_offset = get_sample_data_offset(s3m_file, instrument_offset);
            if (signed_samples) {
                for (int sample_idx = 0; sample_idx < sample_data_length; ++sample_idx) {
                    final int amplitude = s3m_file[sample_data_offset + sample_idx] << 8;
                    sample_data[sample_idx] = (short)(amplitude << 8);
                }
            }
            else {
                for (int sample_idx = 0; sample_idx < sample_data_length; ++sample_idx) {
                    final int amplitude = (s3m_file[sample_data_offset + sample_idx] & 0xFF) - 128;
                    sample_data[sample_idx] = (short)(amplitude << 8);
                }
            }
            sample.set_sample_data(sample_data, loop_start, loop_length, false);
        }
        instrument.set_num_samples(1);
        instrument.set_sample(0, sample);
        return instrument;
    }
    
    private static Pattern read_s3m_pattern(final byte[] s3m_file, final int pattern_idx, final int[] channel_map) {
        int num_channels = 0;
        for (int channel_idx = 0; channel_idx < 32; ++channel_idx) {
            if (channel_map[channel_idx] >= num_channels) {
                num_channels = channel_idx + 1;
            }
        }
        final int num_notes = num_channels * 64;
        final byte[] pattern_data = new byte[num_notes * 5];
        int row_idx = 0;
        int pattern_offset = get_pattern_offset(s3m_file, pattern_idx) + 2;
        while (row_idx < 64) {
            final int token = s3m_file[pattern_offset] & 0xFF;
            ++pattern_offset;
            if (token > 0) {
                final int channel_idx = channel_map[token & 0x1F];
                final int note_idx = (num_channels * row_idx + channel_idx) * 5;
                if ((token & 0x20) == 0x20) {
                    if (channel_idx >= 0) {
                        int key = s3m_file[pattern_offset] & 0xFF;
                        if (key == 255) {
                            key = 0;
                        }
                        else if (key == 254) {
                            key = 97;
                        }
                        else {
                            for (key = ((key & 0xF0) >> 4) * 12 + (key & 0xF) + 1; key > 96; key -= 12) {}
                        }
                        pattern_data[note_idx] = (byte)key;
                        pattern_data[note_idx + 1] = s3m_file[pattern_offset + 1];
                    }
                    pattern_offset += 2;
                }
                if ((token & 0x40) == 0x40) {
                    if (channel_idx >= 0) {
                        final int volume_column = (s3m_file[pattern_offset] & 0xFF) + 16;
                        pattern_data[note_idx + 2] = (byte)volume_column;
                    }
                    ++pattern_offset;
                }
                if ((token & 0x80) != 0x80) {
                    continue;
                }
                if (channel_idx >= 0) {
                    int effect = s3m_file[pattern_offset] & 0xFF;
                    int effect_param = s3m_file[pattern_offset + 1] & 0xFF;
                    effect = ScreamTracker3.effect_map[effect & 0x1F];
                    if (effect == 255) {
                        effect = 0;
                        effect_param = 0;
                    }
                    if (effect == 14) {
                        effect = ScreamTracker3.effect_s_map[(effect_param & 0xF0) >> 4];
                        effect_param &= 0xF;
                        switch (effect) {
                            case 8: {
                                effect = 8;
                                effect_param <<= 4;
                                break;
                            }
                            case 9: {
                                effect = 8;
                                if (effect_param > 7) {
                                    effect_param -= 8;
                                    break;
                                }
                                effect_param += 8;
                                break;
                            }
                            case 255: {
                                effect = 0;
                                effect_param = 0;
                                break;
                            }
                            default: {
                                effect_param = ((effect & 0xF) << 4 | (effect_param & 0xF));
                                effect = 14;
                                break;
                            }
                        }
                    }
                    pattern_data[note_idx + 3] = (byte)effect;
                    pattern_data[note_idx + 4] = (byte)effect_param;
                }
                pattern_offset += 2;
            }
            else {
                ++row_idx;
            }
        }
        final Pattern pattern = new Pattern();
        pattern.num_rows = 64;
        pattern.set_pattern_data(pattern_data);
        return pattern;
    }
    
    private static byte[] read_s3m_file(final byte[] header_96_bytes, final DataInput data_input) throws IOException {
        if (!is_s3m(header_96_bytes)) {
            throw new IllegalArgumentException("ScreamTracker3: Not an S3M file!");
        }
        byte[] s3m_file = header_96_bytes;
        int s3m_file_length = header_96_bytes.length;
        final int num_pattern_orders = get_num_pattern_orders(s3m_file);
        final int num_instruments = get_num_instruments(s3m_file);
        final int num_patterns = get_num_patterns(s3m_file);
        s3m_file_length += num_pattern_orders;
        s3m_file_length += num_instruments * 2;
        s3m_file_length += num_patterns * 2;
        s3m_file = read_more(s3m_file, s3m_file_length, data_input);
        for (int instrument_idx = 0; instrument_idx < num_instruments; ++instrument_idx) {
            int instrument_offset = get_instrument_offset(s3m_file, instrument_idx);
            instrument_offset += 80;
            if (instrument_offset > s3m_file_length) {
                s3m_file_length = instrument_offset;
            }
        }
        for (int pattern_idx = 0; pattern_idx < num_patterns; ++pattern_idx) {
            int pattern_offset = get_pattern_offset(s3m_file, pattern_idx);
            pattern_offset += 2;
            if (pattern_offset > s3m_file_length) {
                s3m_file_length = pattern_offset;
            }
        }
        s3m_file = read_more(s3m_file, s3m_file_length, data_input);
        for (int instrument_idx = 0; instrument_idx < num_instruments; ++instrument_idx) {
            final int instrument_offset = get_instrument_offset(s3m_file, instrument_idx);
            int sample_data_offset = get_sample_data_offset(s3m_file, instrument_offset);
            sample_data_offset += get_sample_data_length(s3m_file, instrument_offset);
            if (sample_data_offset > s3m_file_length) {
                s3m_file_length = sample_data_offset;
            }
        }
        for (int pattern_idx = 0; pattern_idx < num_patterns; ++pattern_idx) {
            int pattern_offset = get_pattern_offset(s3m_file, pattern_idx);
            pattern_offset += get_pattern_length(s3m_file, pattern_offset);
            pattern_offset += 2;
            if (pattern_offset > s3m_file_length) {
                s3m_file_length = pattern_offset;
            }
        }
        s3m_file = read_more(s3m_file, s3m_file_length, data_input);
        return s3m_file;
    }
    
    private static int get_num_pattern_orders(final byte[] s3m_file) {
        final int num_pattern_orders = unsigned_short_le(s3m_file, 32);
        return num_pattern_orders;
    }
    
    private static int get_num_instruments(final byte[] s3m_file) {
        final int num_instruments = unsigned_short_le(s3m_file, 34);
        return num_instruments;
    }
    
    private static int get_num_patterns(final byte[] s3m_file) {
        final int num_patterns = unsigned_short_le(s3m_file, 36);
        return num_patterns;
    }
    
    private static int get_instrument_offset(final byte[] s3m_file, final int instrument_idx) {
        final int pointer_offset = 96 + get_num_pattern_orders(s3m_file);
        final int instrument_offset = unsigned_short_le(s3m_file, pointer_offset + instrument_idx * 2) << 4;
        return instrument_offset;
    }
    
    private static int get_sample_data_offset(final byte[] s3m_file, final int instrument_offset) {
        int sample_data_offset = 0;
        if (s3m_file[instrument_offset] == 1) {
            sample_data_offset = (s3m_file[instrument_offset + 13] & 0xFF) << 20;
            sample_data_offset |= unsigned_short_le(s3m_file, instrument_offset + 14) << 4;
        }
        return sample_data_offset;
    }
    
    private static int get_sample_data_length(final byte[] s3m_file, final int instrument_offset) {
        int sample_data_length = 0;
        if (s3m_file[instrument_offset] == 1) {
            sample_data_length = unsigned_short_le(s3m_file, instrument_offset + 16);
        }
        return sample_data_length;
    }
    
    private static int get_pattern_offset(final byte[] s3m_file, final int pattern_idx) {
        int pointer_offset = 96 + get_num_pattern_orders(s3m_file);
        pointer_offset += get_num_instruments(s3m_file) * 2;
        final int pattern_offset = unsigned_short_le(s3m_file, pointer_offset + pattern_idx * 2) << 4;
        return pattern_offset;
    }
    
    private static int get_pattern_length(final byte[] s3m_file, final int pattern_offset) {
        final int pattern_length = unsigned_short_le(s3m_file, pattern_offset);
        return pattern_length;
    }
    
    private static byte[] read_more(final byte[] old_data, final int new_length, final DataInput data_input) throws IOException {
        byte[] new_data = old_data;
        if (new_length > old_data.length) {
            new_data = new byte[new_length];
            System.arraycopy(old_data, 0, new_data, 0, old_data.length);
            try {
                data_input.readFully(new_data, old_data.length, new_data.length - old_data.length);
            }
            catch (EOFException e) {
                System.out.println("ScreamTracker3: Module has been truncated!");
            }
        }
        return new_data;
    }
    
    private static int unsigned_short_le(final byte[] buffer, final int offset) {
        int value = buffer[offset] & 0xFF;
        value |= (buffer[offset + 1] & 0xFF) << 8;
        return value;
    }
    
    private static String ascii_text(final byte[] buffer, final int offset, final int length) {
        final byte[] string_buffer = new byte[length];
        for (int idx = 0; idx < length; ++idx) {
            int chr = buffer[offset + idx];
            if (chr < 32) {
                chr = 32;
            }
            string_buffer[idx] = (byte)chr;
        }
        String string;
        try {
            string = new String(string_buffer, 0, length, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e) {
            string = "";
        }
        return string;
    }
}
