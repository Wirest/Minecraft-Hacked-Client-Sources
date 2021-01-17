// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import java.io.RandomAccessFile;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import java.io.IOException;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.InputStream;

public class VorbisFile
{
    static final int CHUNKSIZE = 8500;
    static final int SEEK_SET = 0;
    static final int SEEK_CUR = 1;
    static final int SEEK_END = 2;
    static final int OV_FALSE = -1;
    static final int OV_EOF = -2;
    static final int OV_HOLE = -3;
    static final int OV_EREAD = -128;
    static final int OV_EFAULT = -129;
    static final int OV_EIMPL = -130;
    static final int OV_EINVAL = -131;
    static final int OV_ENOTVORBIS = -132;
    static final int OV_EBADHEADER = -133;
    static final int OV_EVERSION = -134;
    static final int OV_ENOTAUDIO = -135;
    static final int OV_EBADPACKET = -136;
    static final int OV_EBADLINK = -137;
    static final int OV_ENOSEEK = -138;
    InputStream datasource;
    boolean seekable;
    long offset;
    long end;
    SyncState oy;
    int links;
    long[] offsets;
    long[] dataoffsets;
    int[] serialnos;
    long[] pcmlengths;
    Info[] vi;
    Comment[] vc;
    long pcm_offset;
    boolean decode_ready;
    int current_serialno;
    int current_link;
    float bittrack;
    float samptrack;
    StreamState os;
    DspState vd;
    Block vb;
    
    public VorbisFile(final String file) throws JOrbisException {
        this.seekable = false;
        this.oy = new SyncState();
        this.decode_ready = false;
        this.os = new StreamState();
        this.vd = new DspState();
        this.vb = new Block(this.vd);
        InputStream is = null;
        try {
            is = new SeekableInputStream(file);
            final int ret = this.open(is, null, 0);
            if (ret == -1) {
                throw new JOrbisException("VorbisFile: open return -1");
            }
        }
        catch (Exception e) {
            throw new JOrbisException("VorbisFile: " + e.toString());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public VorbisFile(final InputStream is, final byte[] initial, final int ibytes) throws JOrbisException {
        this.seekable = false;
        this.oy = new SyncState();
        this.decode_ready = false;
        this.os = new StreamState();
        this.vd = new DspState();
        this.vb = new Block(this.vd);
        final int ret = this.open(is, initial, ibytes);
        if (ret == -1) {}
    }
    
    private int get_data() {
        final int index = this.oy.buffer(8500);
        final byte[] buffer = this.oy.data;
        int bytes = 0;
        try {
            bytes = this.datasource.read(buffer, index, 8500);
        }
        catch (Exception e) {
            return -128;
        }
        this.oy.wrote(bytes);
        if (bytes == -1) {
            bytes = 0;
        }
        return bytes;
    }
    
    private void seek_helper(final long offst) {
        fseek(this.datasource, offst, 0);
        this.offset = offst;
        this.oy.reset();
    }
    
    private int get_next_page(final Page page, long boundary) {
        if (boundary > 0L) {
            boundary += this.offset;
        }
        while (boundary <= 0L || this.offset < boundary) {
            final int more = this.oy.pageseek(page);
            if (more < 0) {
                this.offset -= more;
            }
            else {
                if (more != 0) {
                    final int ret = (int)this.offset;
                    this.offset += more;
                    return ret;
                }
                if (boundary == 0L) {
                    return -1;
                }
                final int ret = this.get_data();
                if (ret == 0) {
                    return -2;
                }
                if (ret < 0) {
                    return -128;
                }
                continue;
            }
        }
        return -1;
    }
    
    private int get_prev_page(final Page page) throws JOrbisException {
        long begin = this.offset;
        int offst = -1;
        while (offst == -1) {
            begin -= 8500L;
            if (begin < 0L) {
                begin = 0L;
            }
            this.seek_helper(begin);
            while (this.offset < begin + 8500L) {
                final int ret = this.get_next_page(page, begin + 8500L - this.offset);
                if (ret == -128) {
                    return -128;
                }
                if (ret < 0) {
                    if (offst == -1) {
                        throw new JOrbisException();
                    }
                    break;
                }
                else {
                    offst = ret;
                }
            }
        }
        this.seek_helper(offst);
        final int ret = this.get_next_page(page, 8500L);
        if (ret < 0) {
            return -129;
        }
        return offst;
    }
    
    int bisect_forward_serialno(final long begin, long searched, final long end, final int currentno, final int m) {
        long endsearched = end;
        long next = end;
        final Page page = new Page();
        while (searched < endsearched) {
            long bisect;
            if (endsearched - searched < 8500L) {
                bisect = searched;
            }
            else {
                bisect = (searched + endsearched) / 2L;
            }
            this.seek_helper(bisect);
            final int ret = this.get_next_page(page, -1L);
            if (ret == -128) {
                return -128;
            }
            if (ret < 0 || page.serialno() != currentno) {
                endsearched = bisect;
                if (ret < 0) {
                    continue;
                }
                next = ret;
            }
            else {
                searched = ret + page.header_len + page.body_len;
            }
        }
        this.seek_helper(next);
        int ret = this.get_next_page(page, -1L);
        if (ret == -128) {
            return -128;
        }
        if (searched >= end || ret == -1) {
            this.links = m + 1;
            (this.offsets = new long[m + 2])[m + 1] = searched;
        }
        else {
            ret = this.bisect_forward_serialno(next, this.offset, end, page.serialno(), m + 1);
            if (ret == -128) {
                return -128;
            }
        }
        this.offsets[m] = begin;
        return 0;
    }
    
    int fetch_headers(final Info vi, final Comment vc, final int[] serialno, Page og_ptr) {
        final Page og = new Page();
        final Packet op = new Packet();
        if (og_ptr == null) {
            final int ret = this.get_next_page(og, 8500L);
            if (ret == -128) {
                return -128;
            }
            if (ret < 0) {
                return -132;
            }
            og_ptr = og;
        }
        if (serialno != null) {
            serialno[0] = og_ptr.serialno();
        }
        this.os.init(og_ptr.serialno());
        vi.init();
        vc.init();
        int i = 0;
        while (i < 3) {
            this.os.pagein(og_ptr);
            while (i < 3) {
                final int result = this.os.packetout(op);
                if (result == 0) {
                    break;
                }
                if (result == -1) {
                    vi.clear();
                    vc.clear();
                    this.os.clear();
                    return -1;
                }
                if (vi.synthesis_headerin(vc, op) != 0) {
                    vi.clear();
                    vc.clear();
                    this.os.clear();
                    return -1;
                }
                ++i;
            }
            if (i < 3 && this.get_next_page(og_ptr, 1L) < 0) {
                vi.clear();
                vc.clear();
                this.os.clear();
                return -1;
            }
        }
        return 0;
    }
    
    void prefetch_all_headers(final Info first_i, final Comment first_c, final int dataoffset) throws JOrbisException {
        final Page og = new Page();
        this.vi = new Info[this.links];
        this.vc = new Comment[this.links];
        this.dataoffsets = new long[this.links];
        this.pcmlengths = new long[this.links];
        this.serialnos = new int[this.links];
        int i = 0;
    Label_0064:
        while (i < this.links) {
            if (first_i != null && first_c != null && i == 0) {
                this.vi[i] = first_i;
                this.vc[i] = first_c;
                this.dataoffsets[i] = dataoffset;
            }
            else {
                this.seek_helper(this.offsets[i]);
                this.vi[i] = new Info();
                this.vc[i] = new Comment();
                if (this.fetch_headers(this.vi[i], this.vc[i], null, null) == -1) {
                    this.dataoffsets[i] = -1L;
                }
                else {
                    this.dataoffsets[i] = this.offset;
                    this.os.clear();
                }
            }
            final long end = this.offsets[i + 1];
            this.seek_helper(end);
            while (true) {
                do {
                    final int ret = this.get_prev_page(og);
                    if (ret == -1) {
                        this.vi[i].clear();
                        this.vc[i].clear();
                        ++i;
                        continue Label_0064;
                    }
                } while (og.granulepos() == -1L);
                this.serialnos[i] = og.serialno();
                this.pcmlengths[i] = og.granulepos();
                continue;
            }
        }
    }
    
    private int make_decode_ready() {
        if (this.decode_ready) {
            System.exit(1);
        }
        this.vd.synthesis_init(this.vi[0]);
        this.vb.init(this.vd);
        this.decode_ready = true;
        return 0;
    }
    
    int open_seekable() throws JOrbisException {
        final Info initial_i = new Info();
        final Comment initial_c = new Comment();
        final Page og = new Page();
        final int[] foo = { 0 };
        final int ret = this.fetch_headers(initial_i, initial_c, foo, null);
        final int serialno = foo[0];
        final int dataoffset = (int)this.offset;
        this.os.clear();
        if (ret == -1) {
            return -1;
        }
        if (ret < 0) {
            return ret;
        }
        this.seekable = true;
        fseek(this.datasource, 0L, 2);
        this.offset = ftell(this.datasource);
        long end = this.offset;
        end = this.get_prev_page(og);
        if (og.serialno() != serialno) {
            if (this.bisect_forward_serialno(0L, 0L, end + 1L, serialno, 0) < 0) {
                this.clear();
                return -128;
            }
        }
        else if (this.bisect_forward_serialno(0L, end, end + 1L, serialno, 0) < 0) {
            this.clear();
            return -128;
        }
        this.prefetch_all_headers(initial_i, initial_c, dataoffset);
        return 0;
    }
    
    int open_nonseekable() {
        this.links = 1;
        (this.vi = new Info[this.links])[0] = new Info();
        (this.vc = new Comment[this.links])[0] = new Comment();
        final int[] foo = { 0 };
        if (this.fetch_headers(this.vi[0], this.vc[0], foo, null) == -1) {
            return -1;
        }
        this.current_serialno = foo[0];
        this.make_decode_ready();
        return 0;
    }
    
    void decode_clear() {
        this.os.clear();
        this.vd.clear();
        this.vb.clear();
        this.decode_ready = false;
        this.bittrack = 0.0f;
        this.samptrack = 0.0f;
    }
    
    int process_packet(final int readp) {
        final Page og = new Page();
        while (true) {
            if (this.decode_ready) {
                final Packet op = new Packet();
                final int result = this.os.packetout(op);
                if (result > 0) {
                    long granulepos = op.granulepos;
                    if (this.vb.synthesis(op) == 0) {
                        final int oldsamples = this.vd.synthesis_pcmout(null, null);
                        this.vd.synthesis_blockin(this.vb);
                        this.samptrack += this.vd.synthesis_pcmout(null, null) - oldsamples;
                        this.bittrack += op.bytes * 8;
                        if (granulepos != -1L && op.e_o_s == 0) {
                            final int link = this.seekable ? this.current_link : 0;
                            final int samples = this.vd.synthesis_pcmout(null, null);
                            granulepos -= samples;
                            for (int i = 0; i < link; ++i) {
                                granulepos += this.pcmlengths[i];
                            }
                            this.pcm_offset = granulepos;
                        }
                        return 1;
                    }
                }
            }
            if (readp == 0) {
                return 0;
            }
            if (this.get_next_page(og, -1L) < 0) {
                return 0;
            }
            this.bittrack += og.header_len * 8;
            if (this.decode_ready && this.current_serialno != og.serialno()) {
                this.decode_clear();
            }
            if (!this.decode_ready) {
                if (this.seekable) {
                    this.current_serialno = og.serialno();
                    int j;
                    for (j = 0; j < this.links && this.serialnos[j] != this.current_serialno; ++j) {}
                    if (j == this.links) {
                        return -1;
                    }
                    this.current_link = j;
                    this.os.init(this.current_serialno);
                    this.os.reset();
                }
                else {
                    final int[] foo = { 0 };
                    final int ret = this.fetch_headers(this.vi[0], this.vc[0], foo, og);
                    this.current_serialno = foo[0];
                    if (ret != 0) {
                        return ret;
                    }
                    ++this.current_link;
                    final int j = 0;
                }
                this.make_decode_ready();
            }
            this.os.pagein(og);
        }
    }
    
    int clear() {
        this.vb.clear();
        this.vd.clear();
        this.os.clear();
        if (this.vi != null && this.links != 0) {
            for (int i = 0; i < this.links; ++i) {
                this.vi[i].clear();
                this.vc[i].clear();
            }
            this.vi = null;
            this.vc = null;
        }
        if (this.dataoffsets != null) {
            this.dataoffsets = null;
        }
        if (this.pcmlengths != null) {
            this.pcmlengths = null;
        }
        if (this.serialnos != null) {
            this.serialnos = null;
        }
        if (this.offsets != null) {
            this.offsets = null;
        }
        this.oy.clear();
        return 0;
    }
    
    static int fseek(final InputStream fis, final long off, final int whence) {
        if (fis instanceof SeekableInputStream) {
            final SeekableInputStream sis = (SeekableInputStream)fis;
            try {
                if (whence == 0) {
                    sis.seek(off);
                }
                else if (whence == 2) {
                    sis.seek(sis.getLength() - off);
                }
            }
            catch (Exception ex) {}
            return 0;
        }
        try {
            if (whence == 0) {
                fis.reset();
            }
            fis.skip(off);
        }
        catch (Exception e) {
            return -1;
        }
        return 0;
    }
    
    static long ftell(final InputStream fis) {
        try {
            if (fis instanceof SeekableInputStream) {
                final SeekableInputStream sis = (SeekableInputStream)fis;
                return sis.tell();
            }
        }
        catch (Exception ex) {}
        return 0L;
    }
    
    int open(final InputStream is, final byte[] initial, final int ibytes) throws JOrbisException {
        return this.open_callbacks(is, initial, ibytes);
    }
    
    int open_callbacks(final InputStream is, final byte[] initial, final int ibytes) throws JOrbisException {
        this.datasource = is;
        this.oy.init();
        if (initial != null) {
            final int index = this.oy.buffer(ibytes);
            System.arraycopy(initial, 0, this.oy.data, index, ibytes);
            this.oy.wrote(ibytes);
        }
        int ret;
        if (is instanceof SeekableInputStream) {
            ret = this.open_seekable();
        }
        else {
            ret = this.open_nonseekable();
        }
        if (ret != 0) {
            this.datasource = null;
            this.clear();
        }
        return ret;
    }
    
    public int streams() {
        return this.links;
    }
    
    public boolean seekable() {
        return this.seekable;
    }
    
    public int bitrate(final int i) {
        if (i >= this.links) {
            return -1;
        }
        if (!this.seekable && i != 0) {
            return this.bitrate(0);
        }
        if (i < 0) {
            long bits = 0L;
            for (int j = 0; j < this.links; ++j) {
                bits += (this.offsets[j + 1] - this.dataoffsets[j]) * 8L;
            }
            return (int)Math.rint(bits / this.time_total(-1));
        }
        if (this.seekable) {
            return (int)Math.rint((this.offsets[i + 1] - this.dataoffsets[i]) * 8L / this.time_total(i));
        }
        if (this.vi[i].bitrate_nominal > 0) {
            return this.vi[i].bitrate_nominal;
        }
        if (this.vi[i].bitrate_upper <= 0) {
            return -1;
        }
        if (this.vi[i].bitrate_lower > 0) {
            return (this.vi[i].bitrate_upper + this.vi[i].bitrate_lower) / 2;
        }
        return this.vi[i].bitrate_upper;
    }
    
    public int bitrate_instant() {
        final int _link = this.seekable ? this.current_link : 0;
        if (this.samptrack == 0.0f) {
            return -1;
        }
        final int ret = (int)(this.bittrack / this.samptrack * this.vi[_link].rate + 0.5);
        this.bittrack = 0.0f;
        this.samptrack = 0.0f;
        return ret;
    }
    
    public int serialnumber(final int i) {
        if (i >= this.links) {
            return -1;
        }
        if (!this.seekable && i >= 0) {
            return this.serialnumber(-1);
        }
        if (i < 0) {
            return this.current_serialno;
        }
        return this.serialnos[i];
    }
    
    public long raw_total(final int i) {
        if (!this.seekable || i >= this.links) {
            return -1L;
        }
        if (i < 0) {
            long acc = 0L;
            for (int j = 0; j < this.links; ++j) {
                acc += this.raw_total(j);
            }
            return acc;
        }
        return this.offsets[i + 1] - this.offsets[i];
    }
    
    public long pcm_total(final int i) {
        if (!this.seekable || i >= this.links) {
            return -1L;
        }
        if (i < 0) {
            long acc = 0L;
            for (int j = 0; j < this.links; ++j) {
                acc += this.pcm_total(j);
            }
            return acc;
        }
        return this.pcmlengths[i];
    }
    
    public float time_total(final int i) {
        if (!this.seekable || i >= this.links) {
            return -1.0f;
        }
        if (i < 0) {
            float acc = 0.0f;
            for (int j = 0; j < this.links; ++j) {
                acc += this.time_total(j);
            }
            return acc;
        }
        return this.pcmlengths[i] / (float)this.vi[i].rate;
    }
    
    public int raw_seek(final int pos) {
        if (!this.seekable) {
            return -1;
        }
        if (pos < 0 || pos > this.offsets[this.links]) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        this.pcm_offset = -1L;
        this.decode_clear();
        this.seek_helper(pos);
        switch (this.process_packet(1)) {
            case 0: {
                this.pcm_offset = this.pcm_total(-1);
                return 0;
            }
            case -1: {
                this.pcm_offset = -1L;
                this.decode_clear();
                return -1;
            }
            default: {
                while (true) {
                    switch (this.process_packet(0)) {
                        case 0: {
                            return 0;
                        }
                        case -1: {
                            this.pcm_offset = -1L;
                            this.decode_clear();
                            return -1;
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public int pcm_seek(final long pos) {
        int link = -1;
        long total = this.pcm_total(-1);
        if (!this.seekable) {
            return -1;
        }
        if (pos < 0L || pos > total) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        for (link = this.links - 1; link >= 0; --link) {
            total -= this.pcmlengths[link];
            if (pos >= total) {
                break;
            }
        }
        final long target = pos - total;
        long end = this.offsets[link + 1];
        long begin = this.offsets[link];
        int best = (int)begin;
        final Page og = new Page();
        while (begin < end) {
            long bisect;
            if (end - begin < 8500L) {
                bisect = begin;
            }
            else {
                bisect = (end + begin) / 2L;
            }
            this.seek_helper(bisect);
            final int ret = this.get_next_page(og, end - bisect);
            if (ret == -1) {
                end = bisect;
            }
            else {
                final long granulepos = og.granulepos();
                if (granulepos < target) {
                    best = ret;
                    begin = this.offset;
                }
                else {
                    end = bisect;
                }
            }
        }
        if (this.raw_seek(best) != 0) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        if (this.pcm_offset >= pos) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        if (pos > this.pcm_total(-1)) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        while (this.pcm_offset < pos) {
            final int target2 = (int)(pos - this.pcm_offset);
            final float[][][] _pcm = { null };
            final int[] _index = new int[this.getInfo(-1).channels];
            int samples = this.vd.synthesis_pcmout(_pcm, _index);
            if (samples > target2) {
                samples = target2;
            }
            this.vd.synthesis_read(samples);
            this.pcm_offset += samples;
            if (samples < target2 && this.process_packet(1) == 0) {
                this.pcm_offset = this.pcm_total(-1);
            }
        }
        return 0;
    }
    
    int time_seek(final float seconds) {
        int link = -1;
        long pcm_total = this.pcm_total(-1);
        float time_total = this.time_total(-1);
        if (!this.seekable) {
            return -1;
        }
        if (seconds < 0.0f || seconds > time_total) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        for (link = this.links - 1; link >= 0; --link) {
            pcm_total -= this.pcmlengths[link];
            time_total -= this.time_total(link);
            if (seconds >= time_total) {
                break;
            }
        }
        final long target = (long)(pcm_total + (seconds - time_total) * this.vi[link].rate);
        return this.pcm_seek(target);
    }
    
    public long raw_tell() {
        return this.offset;
    }
    
    public long pcm_tell() {
        return this.pcm_offset;
    }
    
    public float time_tell() {
        int link = -1;
        long pcm_total = 0L;
        float time_total = 0.0f;
        if (this.seekable) {
            pcm_total = this.pcm_total(-1);
            time_total = this.time_total(-1);
            for (link = this.links - 1; link >= 0; --link) {
                pcm_total -= this.pcmlengths[link];
                time_total -= this.time_total(link);
                if (this.pcm_offset >= pcm_total) {
                    break;
                }
            }
        }
        return time_total + (this.pcm_offset - pcm_total) / (float)this.vi[link].rate;
    }
    
    public Info getInfo(final int link) {
        if (this.seekable) {
            if (link < 0) {
                if (this.decode_ready) {
                    return this.vi[this.current_link];
                }
                return null;
            }
            else {
                if (link >= this.links) {
                    return null;
                }
                return this.vi[link];
            }
        }
        else {
            if (this.decode_ready) {
                return this.vi[0];
            }
            return null;
        }
    }
    
    public Comment getComment(final int link) {
        if (this.seekable) {
            if (link < 0) {
                if (this.decode_ready) {
                    return this.vc[this.current_link];
                }
                return null;
            }
            else {
                if (link >= this.links) {
                    return null;
                }
                return this.vc[link];
            }
        }
        else {
            if (this.decode_ready) {
                return this.vc[0];
            }
            return null;
        }
    }
    
    int host_is_big_endian() {
        return 1;
    }
    
    int read(final byte[] buffer, final int length, final int bigendianp, final int word, final int sgned, final int[] bitstream) {
        final int host_endian = this.host_is_big_endian();
        int index = 0;
        while (true) {
            if (this.decode_ready) {
                final float[][][] _pcm = { null };
                final int[] _index = new int[this.getInfo(-1).channels];
                int samples = this.vd.synthesis_pcmout(_pcm, _index);
                final float[][] pcm = _pcm[0];
                if (samples != 0) {
                    final int channels = this.getInfo(-1).channels;
                    final int bytespersample = word * channels;
                    if (samples > length / bytespersample) {
                        samples = length / bytespersample;
                    }
                    if (word == 1) {
                        final int off = (sgned != 0) ? 0 : 128;
                        for (int j = 0; j < samples; ++j) {
                            for (int i = 0; i < channels; ++i) {
                                int val = (int)(pcm[i][_index[i] + j] * 128.0 + 0.5);
                                if (val > 127) {
                                    val = 127;
                                }
                                else if (val < -128) {
                                    val = -128;
                                }
                                buffer[index++] = (byte)(val + off);
                            }
                        }
                    }
                    else {
                        final int off = (sgned != 0) ? 0 : 32768;
                        if (host_endian == bigendianp) {
                            if (sgned != 0) {
                                for (int k = 0; k < channels; ++k) {
                                    final int src = _index[k];
                                    int dest = k;
                                    for (int l = 0; l < samples; ++l) {
                                        int val = (int)(pcm[k][src + l] * 32768.0 + 0.5);
                                        if (val > 32767) {
                                            val = 32767;
                                        }
                                        else if (val < -32768) {
                                            val = -32768;
                                        }
                                        buffer[dest] = (byte)(val >>> 8);
                                        buffer[dest + 1] = (byte)val;
                                        dest += channels * 2;
                                    }
                                }
                            }
                            else {
                                for (int k = 0; k < channels; ++k) {
                                    final float[] src2 = pcm[k];
                                    int dest = k;
                                    for (int l = 0; l < samples; ++l) {
                                        int val = (int)(src2[l] * 32768.0 + 0.5);
                                        if (val > 32767) {
                                            val = 32767;
                                        }
                                        else if (val < -32768) {
                                            val = -32768;
                                        }
                                        buffer[dest] = (byte)(val + off >>> 8);
                                        buffer[dest + 1] = (byte)(val + off);
                                        dest += channels * 2;
                                    }
                                }
                            }
                        }
                        else if (bigendianp != 0) {
                            for (int j = 0; j < samples; ++j) {
                                for (int i = 0; i < channels; ++i) {
                                    int val = (int)(pcm[i][j] * 32768.0 + 0.5);
                                    if (val > 32767) {
                                        val = 32767;
                                    }
                                    else if (val < -32768) {
                                        val = -32768;
                                    }
                                    val += off;
                                    buffer[index++] = (byte)(val >>> 8);
                                    buffer[index++] = (byte)val;
                                }
                            }
                        }
                        else {
                            for (int j = 0; j < samples; ++j) {
                                for (int i = 0; i < channels; ++i) {
                                    int val = (int)(pcm[i][j] * 32768.0 + 0.5);
                                    if (val > 32767) {
                                        val = 32767;
                                    }
                                    else if (val < -32768) {
                                        val = -32768;
                                    }
                                    val += off;
                                    buffer[index++] = (byte)val;
                                    buffer[index++] = (byte)(val >>> 8);
                                }
                            }
                        }
                    }
                    this.vd.synthesis_read(samples);
                    this.pcm_offset += samples;
                    if (bitstream != null) {
                        bitstream[0] = this.current_link;
                    }
                    return samples * bytespersample;
                }
            }
            switch (this.process_packet(1)) {
                case 0: {
                    return 0;
                }
                case -1: {
                    return -1;
                }
                default: {
                    continue;
                }
            }
        }
    }
    
    public Info[] getInfo() {
        return this.vi;
    }
    
    public Comment[] getComment() {
        return this.vc;
    }
    
    public void close() throws IOException {
        this.datasource.close();
    }
    
    class SeekableInputStream extends InputStream
    {
        RandomAccessFile raf;
        final String mode = "r";
        
        SeekableInputStream(final String file) throws IOException {
            this.raf = null;
            this.raf = new RandomAccessFile(file, "r");
        }
        
        @Override
        public int read() throws IOException {
            return this.raf.read();
        }
        
        @Override
        public int read(final byte[] buf) throws IOException {
            return this.raf.read(buf);
        }
        
        @Override
        public int read(final byte[] buf, final int s, final int len) throws IOException {
            return this.raf.read(buf, s, len);
        }
        
        @Override
        public long skip(final long n) throws IOException {
            return this.raf.skipBytes((int)n);
        }
        
        public long getLength() throws IOException {
            return this.raf.length();
        }
        
        public long tell() throws IOException {
            return this.raf.getFilePointer();
        }
        
        @Override
        public int available() throws IOException {
            return (this.raf.length() != this.raf.getFilePointer()) ? 1 : 0;
        }
        
        @Override
        public void close() throws IOException {
            this.raf.close();
        }
        
        @Override
        public synchronized void mark(final int m) {
        }
        
        @Override
        public synchronized void reset() throws IOException {
        }
        
        @Override
        public boolean markSupported() {
            return false;
        }
        
        public void seek(final long pos) throws IOException {
            this.raf.seek(pos);
        }
    }
}
