// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import java.io.IOException;
import com.ibm.icu.lang.UCharacter;
import java.util.ArrayList;
import java.util.List;
import java.io.Reader;

public class XMLRecordReader implements RecordReader
{
    private Reader r;
    private List<String> nameStack;
    private boolean atTag;
    private String tag;
    
    public XMLRecordReader(final Reader r) {
        this.r = r;
        this.nameStack = new ArrayList<String>();
        if (this.getTag().startsWith("?xml")) {
            this.advance();
        }
        if (this.getTag().startsWith("!--")) {
            this.advance();
        }
    }
    
    public boolean open(final String title) {
        if (this.getTag().equals(title)) {
            this.nameStack.add(title);
            this.advance();
            return true;
        }
        return false;
    }
    
    public boolean close() {
        final int ix = this.nameStack.size() - 1;
        final String name = this.nameStack.get(ix);
        if (this.getTag().equals("/" + name)) {
            this.nameStack.remove(ix);
            this.advance();
            return true;
        }
        return false;
    }
    
    public boolean bool(final String name) {
        final String s = this.string(name);
        return s != null && "true".equals(s);
    }
    
    public boolean[] boolArray(final String name) {
        final String[] sa = this.stringArray(name);
        if (sa != null) {
            final boolean[] result = new boolean[sa.length];
            for (int i = 0; i < sa.length; ++i) {
                result[i] = "true".equals(sa[i]);
            }
            return result;
        }
        return null;
    }
    
    public char character(final String name) {
        final String s = this.string(name);
        if (s != null) {
            return s.charAt(0);
        }
        return '\uffff';
    }
    
    public char[] characterArray(final String name) {
        final String[] sa = this.stringArray(name);
        if (sa != null) {
            final char[] result = new char[sa.length];
            for (int i = 0; i < sa.length; ++i) {
                result[i] = sa[i].charAt(0);
            }
            return result;
        }
        return null;
    }
    
    public byte namedIndex(final String name, final String[] names) {
        final String sa = this.string(name);
        if (sa != null) {
            for (int i = 0; i < names.length; ++i) {
                if (sa.equals(names[i])) {
                    return (byte)i;
                }
            }
        }
        return -1;
    }
    
    public byte[] namedIndexArray(final String name, final String[] names) {
        final String[] sa = this.stringArray(name);
        if (sa != null) {
            final byte[] result = new byte[sa.length];
            int i = 0;
        Label_0019:
            while (i < sa.length) {
                final String s = sa[i];
                while (true) {
                    for (int j = 0; j < names.length; ++j) {
                        if (names[j].equals(s)) {
                            result[i] = (byte)j;
                            ++i;
                            continue Label_0019;
                        }
                    }
                    result[i] = -1;
                    continue;
                }
            }
            return result;
        }
        return null;
    }
    
    public String string(final String name) {
        if (this.match(name)) {
            final String result = this.readData();
            if (this.match("/" + name)) {
                return result;
            }
        }
        return null;
    }
    
    public String[] stringArray(final String name) {
        if (this.match(name + "List")) {
            final List<String> list = new ArrayList<String>();
            String s;
            while (null != (s = this.string(name))) {
                if ("Null".equals(s)) {
                    s = null;
                }
                list.add(s);
            }
            if (this.match("/" + name + "List")) {
                return list.toArray(new String[list.size()]);
            }
        }
        return null;
    }
    
    public String[][] stringTable(final String name) {
        if (this.match(name + "Table")) {
            final List<String[]> list = new ArrayList<String[]>();
            String[] sa;
            while (null != (sa = this.stringArray(name))) {
                list.add(sa);
            }
            if (this.match("/" + name + "Table")) {
                return list.toArray(new String[list.size()][]);
            }
        }
        return null;
    }
    
    private boolean match(final String target) {
        if (this.getTag().equals(target)) {
            this.advance();
            return true;
        }
        return false;
    }
    
    private String getTag() {
        if (this.tag == null) {
            this.tag = this.readNextTag();
        }
        return this.tag;
    }
    
    private void advance() {
        this.tag = null;
    }
    
    private String readData() {
        final StringBuilder sb = new StringBuilder();
        boolean inWhitespace = false;
        int c;
        while (true) {
            c = this.readChar();
            if (c == -1 || c == 60) {
                break;
            }
            if (c == 38) {
                c = this.readChar();
                if (c == 35) {
                    final StringBuilder numBuf = new StringBuilder();
                    int radix = 10;
                    c = this.readChar();
                    if (c == 120) {
                        radix = 16;
                        c = this.readChar();
                    }
                    while (c != 59 && c != -1) {
                        numBuf.append((char)c);
                        c = this.readChar();
                    }
                    try {
                        final int num = Integer.parseInt(numBuf.toString(), radix);
                        c = (char)num;
                    }
                    catch (NumberFormatException ex) {
                        System.err.println("numbuf: " + numBuf.toString() + " radix: " + radix);
                        throw ex;
                    }
                }
                else {
                    final StringBuilder charBuf = new StringBuilder();
                    while (c != 59 && c != -1) {
                        charBuf.append((char)c);
                        c = this.readChar();
                    }
                    final String charName = charBuf.toString();
                    if (charName.equals("lt")) {
                        c = 60;
                    }
                    else if (charName.equals("gt")) {
                        c = 62;
                    }
                    else if (charName.equals("quot")) {
                        c = 34;
                    }
                    else if (charName.equals("apos")) {
                        c = 39;
                    }
                    else {
                        if (!charName.equals("amp")) {
                            System.err.println("unrecognized character entity: '" + charName + "'");
                            continue;
                        }
                        c = 38;
                    }
                }
            }
            if (UCharacter.isWhitespace(c)) {
                if (inWhitespace) {
                    continue;
                }
                c = 32;
                inWhitespace = true;
            }
            else {
                inWhitespace = false;
            }
            sb.append((char)c);
        }
        this.atTag = (c == 60);
        return sb.toString();
    }
    
    private String readNextTag() {
        int c = 0;
        while (!this.atTag) {
            c = this.readChar();
            if (c == 60 || c == -1) {
                if (c == 60) {
                    this.atTag = true;
                    break;
                }
                break;
            }
            else {
                if (!UCharacter.isWhitespace(c)) {
                    System.err.println("Unexpected non-whitespace character " + Integer.toHexString(c));
                    break;
                }
                continue;
            }
        }
        if (this.atTag) {
            this.atTag = false;
            final StringBuilder sb = new StringBuilder();
            while (true) {
                c = this.readChar();
                if (c == 62 || c == -1) {
                    break;
                }
                sb.append((char)c);
            }
            return sb.toString();
        }
        return null;
    }
    
    int readChar() {
        try {
            return this.r.read();
        }
        catch (IOException e) {
            return -1;
        }
    }
}
