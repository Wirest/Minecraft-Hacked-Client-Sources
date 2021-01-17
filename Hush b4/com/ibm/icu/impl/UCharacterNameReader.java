// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;

final class UCharacterNameReader implements ICUBinary.Authenticate
{
    private DataInputStream m_dataInputStream_;
    private static final int GROUP_INFO_SIZE_ = 3;
    private int m_tokenstringindex_;
    private int m_groupindex_;
    private int m_groupstringindex_;
    private int m_algnamesindex_;
    private static final int ALG_INFO_SIZE_ = 12;
    private static final byte[] DATA_FORMAT_VERSION_;
    private static final byte[] DATA_FORMAT_ID_;
    
    public boolean isDataVersionAcceptable(final byte[] version) {
        return version[0] == UCharacterNameReader.DATA_FORMAT_VERSION_[0];
    }
    
    protected UCharacterNameReader(final InputStream inputStream) throws IOException {
        ICUBinary.readHeader(inputStream, UCharacterNameReader.DATA_FORMAT_ID_, this);
        this.m_dataInputStream_ = new DataInputStream(inputStream);
    }
    
    protected void read(final UCharacterName data) throws IOException {
        this.m_tokenstringindex_ = this.m_dataInputStream_.readInt();
        this.m_groupindex_ = this.m_dataInputStream_.readInt();
        this.m_groupstringindex_ = this.m_dataInputStream_.readInt();
        this.m_algnamesindex_ = this.m_dataInputStream_.readInt();
        int count = this.m_dataInputStream_.readChar();
        final char[] token = new char[count];
        for (char i = '\0'; i < count; ++i) {
            token[i] = this.m_dataInputStream_.readChar();
        }
        int size = this.m_groupindex_ - this.m_tokenstringindex_;
        final byte[] tokenstr = new byte[size];
        this.m_dataInputStream_.readFully(tokenstr);
        data.setToken(token, tokenstr);
        count = this.m_dataInputStream_.readChar();
        data.setGroupCountSize(count, 3);
        count *= 3;
        final char[] group = new char[count];
        for (int j = 0; j < count; ++j) {
            group[j] = this.m_dataInputStream_.readChar();
        }
        size = this.m_algnamesindex_ - this.m_groupstringindex_;
        final byte[] groupstring = new byte[size];
        this.m_dataInputStream_.readFully(groupstring);
        data.setGroup(group, groupstring);
        count = this.m_dataInputStream_.readInt();
        final UCharacterName.AlgorithmName[] alg = new UCharacterName.AlgorithmName[count];
        for (int k = 0; k < count; ++k) {
            final UCharacterName.AlgorithmName an = this.readAlg();
            if (an == null) {
                throw new IOException("unames.icu read error: Algorithmic names creation error");
            }
            alg[k] = an;
        }
        data.setAlgorithm(alg);
    }
    
    protected boolean authenticate(final byte[] dataformatid, final byte[] dataformatversion) {
        return Arrays.equals(UCharacterNameReader.DATA_FORMAT_ID_, dataformatid) && Arrays.equals(UCharacterNameReader.DATA_FORMAT_VERSION_, dataformatversion);
    }
    
    private UCharacterName.AlgorithmName readAlg() throws IOException {
        final UCharacterName.AlgorithmName result = new UCharacterName.AlgorithmName();
        final int rangestart = this.m_dataInputStream_.readInt();
        final int rangeend = this.m_dataInputStream_.readInt();
        final byte type = this.m_dataInputStream_.readByte();
        final byte variant = this.m_dataInputStream_.readByte();
        if (!result.setInfo(rangestart, rangeend, type, variant)) {
            return null;
        }
        int size = this.m_dataInputStream_.readChar();
        if (type == 1) {
            final char[] factor = new char[variant];
            for (int j = 0; j < variant; ++j) {
                factor[j] = this.m_dataInputStream_.readChar();
            }
            result.setFactor(factor);
            size -= variant << 1;
        }
        final StringBuilder prefix = new StringBuilder();
        for (char c = (char)(this.m_dataInputStream_.readByte() & 0xFF); c != '\0'; c = (char)(this.m_dataInputStream_.readByte() & 0xFF)) {
            prefix.append(c);
        }
        result.setPrefix(prefix.toString());
        size -= 12 + prefix.length() + 1;
        if (size > 0) {
            final byte[] string = new byte[size];
            this.m_dataInputStream_.readFully(string);
            result.setFactorString(string);
        }
        return result;
    }
    
    static {
        DATA_FORMAT_VERSION_ = new byte[] { 1, 0, 0, 0 };
        DATA_FORMAT_ID_ = new byte[] { 117, 110, 97, 109 };
    }
}
