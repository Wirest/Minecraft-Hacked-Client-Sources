// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Buffer;

public class Comment
{
    private static byte[] _vorbis;
    private static byte[] _vendor;
    private static final int OV_EIMPL = -130;
    public byte[][] user_comments;
    public int[] comment_lengths;
    public int comments;
    public byte[] vendor;
    
    public void init() {
        this.user_comments = null;
        this.comments = 0;
        this.vendor = null;
    }
    
    public void add(final String comment) {
        this.add(comment.getBytes());
    }
    
    private void add(final byte[] comment) {
        final byte[][] foo = new byte[this.comments + 2][];
        if (this.user_comments != null) {
            System.arraycopy(this.user_comments, 0, foo, 0, this.comments);
        }
        this.user_comments = foo;
        final int[] goo = new int[this.comments + 2];
        if (this.comment_lengths != null) {
            System.arraycopy(this.comment_lengths, 0, goo, 0, this.comments);
        }
        this.comment_lengths = goo;
        final byte[] bar = new byte[comment.length + 1];
        System.arraycopy(comment, 0, bar, 0, comment.length);
        this.user_comments[this.comments] = bar;
        this.comment_lengths[this.comments] = comment.length;
        ++this.comments;
        this.user_comments[this.comments] = null;
    }
    
    public void add_tag(final String tag, String contents) {
        if (contents == null) {
            contents = "";
        }
        this.add(tag + "=" + contents);
    }
    
    static boolean tagcompare(final byte[] s1, final byte[] s2, final int n) {
        for (int c = 0; c < n; ++c) {
            byte u1 = s1[c];
            byte u2 = s2[c];
            if (90 >= u1 && u1 >= 65) {
                u1 = (byte)(u1 - 65 + 97);
            }
            if (90 >= u2 && u2 >= 65) {
                u2 = (byte)(u2 - 65 + 97);
            }
            if (u1 != u2) {
                return false;
            }
        }
        return true;
    }
    
    public String query(final String tag) {
        return this.query(tag, 0);
    }
    
    public String query(final String tag, final int count) {
        final int foo = this.query(tag.getBytes(), count);
        if (foo == -1) {
            return null;
        }
        final byte[] comment = this.user_comments[foo];
        for (int i = 0; i < this.comment_lengths[foo]; ++i) {
            if (comment[i] == 61) {
                return new String(comment, i + 1, this.comment_lengths[foo] - (i + 1));
            }
        }
        return null;
    }
    
    private int query(final byte[] tag, final int count) {
        int i = 0;
        int found = 0;
        final int fulltaglen = tag.length + 1;
        final byte[] fulltag = new byte[fulltaglen];
        System.arraycopy(tag, 0, fulltag, 0, tag.length);
        fulltag[tag.length] = 61;
        for (i = 0; i < this.comments; ++i) {
            if (tagcompare(this.user_comments[i], fulltag, fulltaglen)) {
                if (count == found) {
                    return i;
                }
                ++found;
            }
        }
        return -1;
    }
    
    int unpack(final Buffer opb) {
        final int vendorlen = opb.read(32);
        if (vendorlen < 0) {
            this.clear();
            return -1;
        }
        opb.read(this.vendor = new byte[vendorlen + 1], vendorlen);
        this.comments = opb.read(32);
        if (this.comments < 0) {
            this.clear();
            return -1;
        }
        this.user_comments = new byte[this.comments + 1][];
        this.comment_lengths = new int[this.comments + 1];
        for (int i = 0; i < this.comments; ++i) {
            final int len = opb.read(32);
            if (len < 0) {
                this.clear();
                return -1;
            }
            this.comment_lengths[i] = len;
            opb.read(this.user_comments[i] = new byte[len + 1], len);
        }
        if (opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    int pack(final Buffer opb) {
        opb.write(3, 8);
        opb.write(Comment._vorbis);
        opb.write(Comment._vendor.length, 32);
        opb.write(Comment._vendor);
        opb.write(this.comments, 32);
        if (this.comments != 0) {
            for (int i = 0; i < this.comments; ++i) {
                if (this.user_comments[i] != null) {
                    opb.write(this.comment_lengths[i], 32);
                    opb.write(this.user_comments[i]);
                }
                else {
                    opb.write(0, 32);
                }
            }
        }
        opb.write(1, 1);
        return 0;
    }
    
    public int header_out(final Packet op) {
        final Buffer opb = new Buffer();
        opb.writeinit();
        if (this.pack(opb) != 0) {
            return -130;
        }
        op.packet_base = new byte[opb.bytes()];
        op.packet = 0;
        op.bytes = opb.bytes();
        System.arraycopy(opb.buffer(), 0, op.packet_base, 0, op.bytes);
        op.b_o_s = 0;
        op.e_o_s = 0;
        op.granulepos = 0L;
        return 0;
    }
    
    void clear() {
        for (int i = 0; i < this.comments; ++i) {
            this.user_comments[i] = null;
        }
        this.user_comments = null;
        this.vendor = null;
    }
    
    public String getVendor() {
        return new String(this.vendor, 0, this.vendor.length - 1);
    }
    
    public String getComment(final int i) {
        if (this.comments <= i) {
            return null;
        }
        return new String(this.user_comments[i], 0, this.user_comments[i].length - 1);
    }
    
    @Override
    public String toString() {
        String foo = "Vendor: " + new String(this.vendor, 0, this.vendor.length - 1);
        for (int i = 0; i < this.comments; ++i) {
            foo = foo + "\nComment: " + new String(this.user_comments[i], 0, this.user_comments[i].length - 1);
        }
        foo += "\n";
        return foo;
    }
    
    static {
        Comment._vorbis = "vorbis".getBytes();
        Comment._vendor = "Xiphophorus libVorbis I 20000508".getBytes();
    }
}
