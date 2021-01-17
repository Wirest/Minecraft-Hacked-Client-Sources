// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import java.io.IOException;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyHash;
import java.io.InputStream;

class b implements XMLParser
{
    private InputStream try;
    private XMLHandler for;
    private int int;
    private int void;
    private int byte;
    private char[] c;
    private int d;
    private boolean case;
    private int new;
    private char[] long;
    private int null;
    private int char;
    private char[] if;
    private int do;
    private char[] goto;
    private char[] else;
    private TinyHash b;
    private boolean a;
    
    public b() {
        this.byte = 4096;
        this.c = new char[this.byte];
        this.new = 4096;
        this.long = new char[this.new];
        this.char = 16;
        this.if = new char[this.char];
        this.goto = "CDATA".toCharArray();
        this.else = "ENTITY".toCharArray();
        (this.b = new TinyHash(1, 11)).put(new TinyString("lt".toCharArray()), new TinyString("<".toCharArray()));
        this.b.put(new TinyString("gt".toCharArray()), new TinyString(">".toCharArray()));
        this.b.put(new TinyString("apos".toCharArray()), new TinyString("'".toCharArray()));
        this.b.put(new TinyString("amp".toCharArray()), new TinyString("&".toCharArray()));
        this.b.put(new TinyString("quot".toCharArray()), new TinyString("\"".toCharArray()));
    }
    
    public void setInputStream(final InputStream try1) {
        this.try = try1;
    }
    
    public void setXMLHandler(final XMLHandler for1) {
        this.for = for1;
    }
    
    public int getType() {
        return this.int;
    }
    
    public int getError() {
        return this.void;
    }
    
    public void init() {
        final int n = 0;
        this.null = n;
        this.d = n;
        final int n2 = 4096;
        this.new = n2;
        this.byte = n2;
        for (int i = 0; i < this.byte; ++i) {
            this.c[i] = '\0';
            this.long[i] = '\0';
        }
        this.do = 0;
        this.char = 16;
        for (int j = 0; j < this.char; ++j) {
            this.if[j] = '\0';
        }
        this.try = null;
        this.case = false;
        this.void = 0;
    }
    
    public void getNext() {
        if (this.a) {
            this.int = 16;
            this.a = false;
        }
        else {
            switch (this.byte()) {
                case 60: {
                    this.if();
                    this.a();
                    break;
                }
                case -1: {
                    this.int = 8;
                    this.for.endDocument();
                    break;
                }
                default: {
                    this.int = this.for('<');
                    if (this.int == 128) {
                        this.for.charData(this.long, 0, this.null);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private final void case() {
        if (this.if() != 45) {
            this.case = true;
            this.void |= 0x8;
            return;
        }
        while (true) {
            this.a('-');
            if (this.if() == -1) {
                this.void |= 0x10;
                return;
            }
            int n = 0;
            int i;
            do {
                i = this.if();
                ++n;
            } while (i == 45);
            if (i == 62 && n >= 2) {
                this.int = 1;
            }
        }
    }
    
    private final void char() {
        int n = 1;
        while (true) {
            switch (this.if()) {
                case -1: {
                    this.void |= 0x10;
                }
                case 60: {
                    ++n;
                    continue;
                }
                case 62: {
                    if (--n == 0) {
                        this.int = 2;
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    private final void new() {
        this.a('?');
        this.if();
        while (this.byte() != 62) {
            if (this.if() == -1) {
                this.void |= 0x8;
                return;
            }
            this.a('?');
            this.if();
        }
        this.if();
        this.int = 32;
    }
    
    private final void do(final char c) {
        if (c == -1) {
            return;
        }
        if (this.null == this.new) {
            this.new *= 2;
            final char[] long1 = new char[this.new];
            System.arraycopy(this.long, 0, long1, 0, this.null);
            this.long = long1;
        }
        this.long[this.null++] = c;
    }
    
    private final void do() {
        final int if1 = this.if();
        if (if1 == -1) {
            return;
        }
        if (if1 < 128 && if1 != 95 && if1 != 58 && (if1 < 97 || if1 > 122) && (if1 < 65 || if1 > 90)) {
            this.case = true;
            this.void |= 0x20;
            return;
        }
        this.null = 0;
        this.do((char)if1);
        while (!this.case) {
            final int byte1 = this.byte();
            if (byte1 < 128 && byte1 != 95 && byte1 != 45 && byte1 != 58 && byte1 != 46 && (byte1 < 48 || byte1 > 57) && (byte1 < 97 || byte1 > 122) && (byte1 < 65 || byte1 > 90)) {
                break;
            }
            this.do((char)this.if());
        }
    }
    
    private final void for() {
        this.try();
        this.do();
        this.try();
        if (this.if() != 62) {
            this.case = true;
            this.void |= 0x40;
            return;
        }
        this.int = 16;
        this.for.endElement();
    }
    
    private final void if(final char c) {
        while (!this.case && this.byte() != c) {
            this.do((char)this.if());
        }
    }
    
    private final void int(final char c) {
        this.do = 0;
        while (!this.case && this.byte() != c) {
            final char c2 = (char)this.if();
            if (this.do == this.char) {
                this.char *= 2;
                final char[] if1 = new char[this.char];
                System.arraycopy(this.if, 0, if1, 0, this.do);
                this.if = if1;
            }
            this.if[this.do++] = c2;
        }
    }
    
    private final void goto() {
        this.null = 0;
        this.if('[');
        if (0 != TinyString.compareTo(this.long, 0, this.goto.length, this.goto, 0, this.goto.length)) {
            this.case = true;
            this.void |= 0x80;
            return;
        }
        this.null = 0;
        this.if();
        int if1 = this.if();
        int if2 = this.if();
        while (true) {
            final int if3 = this.if();
            if (if3 == -1) {
                this.void |= 0x10;
                return;
            }
            if (if1 == 93 && if2 == 93 && if3 == 62) {
                this.int = 128;
                this.for.charData(this.long, 0, this.null);
                return;
            }
            this.do((char)if1);
            if1 = if2;
            if2 = if3;
        }
    }
    
    private final void a() {
        switch (this.byte()) {
            case -1: {
                this.void |= 0x10;
            }
            case 33: {
                this.if();
                switch (this.byte()) {
                    case 45: {
                        this.if();
                        this.case();
                        break;
                    }
                    case 91: {
                        this.if();
                        this.goto();
                        break;
                    }
                    default: {
                        this.char();
                        break;
                    }
                }
                break;
            }
            case 63: {
                this.if();
                this.new();
                break;
            }
            case 47: {
                this.if();
                this.for();
                break;
            }
            default: {
                this.int();
                break;
            }
        }
    }
    
    private final int for(final char c) {
        int n = 256;
        this.null = 0;
        while (true) {
            final int byte1 = this.byte();
            if (byte1 == -1 || byte1 == c || (c == ' ' && (byte1 == '>' || byte1 < ' '))) {
                break;
            }
            ++this.d;
            if (byte1 == '&') {
                this.int(';');
                this.if();
                if (this.if[0] == '#') {
                    final int n2 = (this.if[1] == 'x') ? TinyNumber.parseInt(this.if, 2, this.do, 16) : TinyNumber.parseInt(this.if, 1, this.do, 10);
                    if (n2 > 32) {
                        n = 128;
                    }
                    this.do((char)n2);
                }
                else {
                    final TinyString tinyString = (TinyString)this.b.get(new TinyString(this.if, 0, this.do));
                    if (tinyString != null && tinyString.count > 0) {
                        for (int i = 0; i < tinyString.count; ++i) {
                            this.do(tinyString.data[i]);
                        }
                    }
                    else {
                        this.do('&');
                        for (int j = 0; j < this.char; ++j) {
                            this.do(this.if[j]);
                        }
                        this.do(';');
                    }
                    n = 128;
                }
            }
            else {
                if (byte1 > ' ') {
                    n = 128;
                }
                this.do((char)byte1);
            }
        }
        return n;
    }
    
    private final void int() {
        this.do();
        this.int = 64;
        this.for.startElement(this.long, 0, this.null);
        while (true) {
            this.try();
            final int byte1 = this.byte();
            if (byte1 == 47) {
                this.a = true;
                this.if();
                this.try();
                if (this.if() != 62) {
                    this.case = true;
                    this.void |= 0x200;
                    return;
                }
                break;
            }
            else {
                if (byte1 == 62) {
                    this.if();
                    break;
                }
                if (byte1 == -1) {
                    this.void |= 0x10;
                    return;
                }
                this.do();
                if (this.null == 0) {
                    this.case = true;
                    this.void |= 0x100;
                    return;
                }
                this.for.attributeName(this.long, 0, this.null);
                this.try();
                if (this.if() != 61) {
                    this.case = true;
                    this.void |= 0x400;
                    return;
                }
                this.try();
                int if1 = this.if();
                if (if1 != 39 && if1 != 34) {
                    if1 = 32;
                }
                this.for((char)if1);
                this.for.attributeValue(this.long, 0, this.null);
                if (if1 == 32) {
                    continue;
                }
                this.if();
            }
        }
        if (this.a) {
            this.for.endElement();
        }
    }
    
    private final int byte() {
        if (this.case) {
            return -1;
        }
        if (this.d >= this.byte) {
            this.byte = this.a(this.c, 0, this.c.length);
            this.d = 0;
            if (this.byte == -1) {
                this.case = true;
                return -1;
            }
        }
        return this.c[this.d];
    }
    
    private final int if() {
        if (this.case) {
            return -1;
        }
        if (this.d >= this.byte) {
            this.byte = this.a(this.c, 0, this.c.length);
            this.d = 0;
            if (this.byte == -1) {
                this.case = true;
                return -1;
            }
        }
        return this.c[this.d++];
    }
    
    private final void try() {
        while (!this.case && this.byte() <= 32) {
            this.if();
        }
    }
    
    private final void a(final char c) {
        while (!this.case && this.byte() != c) {
            this.if();
        }
    }
    
    private final int a(final char[] array, int n, final int n2) {
        int i;
        for (i = 0; i < n2; ++i) {
            final int else1 = this.else();
            if (else1 < 0) {
                return (i == 0) ? -1 : i;
            }
            switch (else1 & 0xF0) {
                case 192:
                case 208: {
                    final int else2 = this.else();
                    if (else2 < 0) {
                        return -1;
                    }
                    array[n++] = (char)((else1 & 0x1F) << 6 | (else2 & 0x3F));
                    break;
                }
                case 224: {
                    final int else3 = this.else();
                    if (else3 < 0) {
                        return -1;
                    }
                    final int else4 = this.else();
                    if (else4 < 0) {
                        return -1;
                    }
                    array[n++] = (char)((else1 & 0xF) << 12 | (else3 & 0x3F) << 6 | (else4 & 0x3F));
                    break;
                }
                case 240: {
                    this.void |= 0x2;
                    return -1;
                }
                default: {
                    array[n++] = (char)else1;
                    break;
                }
            }
        }
        return i;
    }
    
    private final int else() {
        try {
            return this.try.read();
        }
        catch (IOException ex) {
            final int n = -1;
            this.void |= 0x1;
            return n;
        }
    }
}
