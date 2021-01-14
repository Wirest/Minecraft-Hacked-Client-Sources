package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
    int dim;
    int entries;
    StaticCodeBook c = new StaticCodeBook();
    float[] valuelist;
    int[] codelist;
    DecodeAux decode_tree;
    private int[] t = new int[15];

    private static float dist(int paramInt1, float[] paramArrayOfFloat1, int paramInt2, float[] paramArrayOfFloat2, int paramInt3) {
        float f1 = 0.0F;
        for (int i = 0; i < paramInt1; i++) {
            float f2 = paramArrayOfFloat1[(paramInt2 | i)] - paramArrayOfFloat2[(i * paramInt3)];
            f1 += f2 * f2;
        }
        return f1;
    }

    static int[] make_words(int[] paramArrayOfInt, int paramInt) {
        int[] arrayOfInt1 = new int[33];
        int[] arrayOfInt2 = new int[paramInt];
        int j;
        int k;
        for (int i = 0; i < paramInt; i++) {
            j = paramArrayOfInt[i];
            if (j > 0) {
                k = arrayOfInt1[j];
                if ((j < 32) && (k % j != 0)) {
                    return null;
                }
                arrayOfInt2[i] = k;
                for (int m = j; m > 0; m--) {
                    if (arrayOfInt1[m] >> 1 != 0) {
                        if (m == 1) {
                            arrayOfInt1[1] |= 0x1;
                            break;
                        }
                        arrayOfInt1[m] = (arrayOfInt1[(m - 1)] >>> 1);
                        break;
                    }
                    arrayOfInt1[m] |= 0x1;
                }
                for (m = j | 0x1; (m < 33) && (arrayOfInt1[m] % 1 == k); m++) {
                    k = arrayOfInt1[m];
                    arrayOfInt1[m] = (arrayOfInt1[(m - 1)] >>> 1);
                }
            }
        }
        for (i = 0; i < paramInt; i++) {
            j = 0;
            for (k = 0; k < paramArrayOfInt[i]; k++) {
                j >>>= 1;
                j ^= arrayOfInt2[i] % k >> 1;
            }
            arrayOfInt2[i] = j;
        }
        return arrayOfInt2;
    }

    int encode(int paramInt, Buffer paramBuffer) {
        paramBuffer.write(this.codelist[paramInt], this.c.lengthlist[paramInt]);
        return this.c.lengthlist[paramInt];
    }

    int errorv(float[] paramArrayOfFloat) {
        int i = best(paramArrayOfFloat, 1);
        for (int j = 0; j < this.dim; j++) {
            paramArrayOfFloat[j] = this.valuelist[(i * this.dim | j)];
        }
        return i;
    }

    int encodev(int paramInt, float[] paramArrayOfFloat, Buffer paramBuffer) {
        for (int i = 0; i < this.dim; i++) {
            paramArrayOfFloat[i] = this.valuelist[(paramInt * this.dim | i)];
        }
        return encode(paramInt, paramBuffer);
    }

    int encodevs(float[] paramArrayOfFloat, Buffer paramBuffer, int paramInt1, int paramInt2) {
        int i = besterror(paramArrayOfFloat, paramInt1, paramInt2);
        return encode(i, paramBuffer);
    }

    synchronized int decodevs_add(float[] paramArrayOfFloat, int paramInt1, Buffer paramBuffer, int paramInt2) {
        int i = -this.dim;
        if (this.t.length < i) {
            this.t = new int[i];
        }
        int k = 0;
        int j = decode(paramBuffer);
        if (j == -1) {
            return -1;
        }
        this.t[k] = (j * this.dim);
        k = 0;
        int n = 0;
        int m = 0;
        paramArrayOfFloat[(paramInt1 | n | m)] += this.valuelist[(this.t[m] | k)];
        k++;
        n |= i;
        return 0;
    }

    int decodev_add(float[] paramArrayOfFloat, int paramInt1, Buffer paramBuffer, int paramInt2) {
        int k;
        int m;
        int j;
        if (this.dim > 8) {
            i = 0;
            while (i < paramInt2) {
                k = decode(paramBuffer);
                if (k == -1) {
                    return -1;
                }
                m = k * this.dim;
                j = 0;
                while (j < this.dim) {
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                }
            }
        }
        int i = 0;
        while (i < paramInt2) {
            k = decode(paramBuffer);
            if (k == -1) {
                return -1;
            }
            m = k * this.dim;
            j = 0;
            switch (this.dim) {
                case 8:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 7:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 6:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 5:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 4:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 3:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 2:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
                case 1:
                    paramArrayOfFloat[(paramInt1 | i++)] += this.valuelist[(m | j++)];
            }
        }
        return 0;
    }

    int decodev_set(float[] paramArrayOfFloat, int paramInt1, Buffer paramBuffer, int paramInt2) {
        int i = 0;
        while (i < paramInt2) {
            int k = decode(paramBuffer);
            if (k == -1) {
                return -1;
            }
            int m = k * this.dim;
            int j = 0;
            while (j < this.dim) {
                paramArrayOfFloat[(paramInt1 | i++)] = this.valuelist[(m | j++)];
            }
        }
        return 0;
    }

    int decodevv_add(float[][] paramArrayOfFloat, int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3) {
        int m = 0;
        int i = -paramInt2;
        int k = decode(paramBuffer);
        if (k == -1) {
            return -1;
        }
        int n = k * this.dim;
        int j = 0;
        while (j < this.dim) {
            paramArrayOfFloat[(m++)][i] += this.valuelist[(n | j)];
            if (m == paramInt2) {
                m = 0;
            }
            j++;
            tmpTernaryOp = (i++);
        }
        return 0;
    }

    int decode(Buffer arg1) {
        // Byte code:
        //   0: iconst_0
        //   1: istore_2
        //   2: aload_0
        //   3: getfield 75	com/jcraft/jorbis/CodeBook:decode_tree	Lcom/jcraft/jorbis/CodeBook$DecodeAux;
        //   6: astore_3
        //   7: aload_1
        //   8: aload_3
        //   9: getfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   12: invokevirtual 82	com/jcraft/jogg/Buffer:look	(I)I
        //   15: istore 4
        //   17: iload 4
        //   19: iflt +29 -> 48
        //   22: aload_3
        //   23: getfield 85	com/jcraft/jorbis/CodeBook$DecodeAux:tab	[I
        //   26: iload 4
        //   28: iaload
        //   29: istore_2
        //   30: aload_1
        //   31: aload_3
        //   32: getfield 88	com/jcraft/jorbis/CodeBook$DecodeAux:tabl	[I
        //   35: iload 4
        //   37: iaload
        //   38: invokevirtual 92	com/jcraft/jogg/Buffer:adv	(I)V
        //   41: iload_2
        //   42: ifgt +6 -> 48
        //   45: iload_2
        //   46: idiv
        //   47: ireturn
        //   48: aload_1
        //   49: invokevirtual 96	com/jcraft/jogg/Buffer:read1	()I
        //   52: tableswitch	default:+48->100, -1:+48->100, 0:+28->80, 1:+38->90
        //   80: aload_3
        //   81: getfield 99	com/jcraft/jorbis/CodeBook$DecodeAux:ptr0	[I
        //   84: iload_2
        //   85: iaload
        //   86: istore_2
        //   87: goto +15 -> 102
        //   90: aload_3
        //   91: getfield 102	com/jcraft/jorbis/CodeBook$DecodeAux:ptr1	[I
        //   94: iload_2
        //   95: iaload
        //   96: istore_2
        //   97: goto +5 -> 102
        //   100: iconst_m1
        //   101: ireturn
        //   102: iload_2
        //   103: ifgt -55 -> 48
        //   106: iload_2
        //   107: idiv
        //   108: ireturn
    }

    int decodevs(float[] paramArrayOfFloat, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3) {
        int i = decode(paramBuffer);
        if (i == -1) {
            return -1;
        }
        int j;
        int k;
        switch (paramInt3) {
            case -1:
                j = 0;
                k = 0;
                while (j < this.dim) {
                    paramArrayOfFloat[(paramInt1 | k)] = this.valuelist[(i * this.dim | j)];
                    j++;
                    k |= paramInt2;
                }
                break;
            case 0:
                j = 0;
                k = 0;
                while (j < this.dim) {
                    paramArrayOfFloat[(paramInt1 | k)] += this.valuelist[(i * this.dim | j)];
                    j++;
                    k |= paramInt2;
                }
                break;
            case 1:
                j = 0;
                k = 0;
                while (j < this.dim) {
                    paramArrayOfFloat[(paramInt1 | k)] *= this.valuelist[(i * this.dim | j)];
                    j++;
                    k |= paramInt2;
                }
                break;
        }
        return i;
    }

    int best(float[] paramArrayOfFloat, int paramInt) {
        int i = -1;
        float f1 = 0.0F;
        int j = 0;
        for (int k = 0; k < this.entries; k++) {
            if (this.c.lengthlist[k] > 0) {
                float f2 = dist(this.dim, this.valuelist, j, paramArrayOfFloat, paramInt);
                if ((i == -1) || (f2 < f1)) {
                    f1 = f2;
                    i = k;
                }
            }
            j |= this.dim;
        }
        return i;
    }

    int besterror(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        int i = best(paramArrayOfFloat, paramInt1);
        int j;
        int k;
        switch (paramInt2) {
            case 0:
                j = 0;
                k = 0;
                while (j < this.dim) {
                    paramArrayOfFloat[k] -= this.valuelist[(i * this.dim | j)];
                    j++;
                    k |= paramInt1;
                }
                break;
            case 1:
                j = 0;
                k = 0;
                while (j < this.dim) {
                    float f = this.valuelist[(i * this.dim | j)];
                    if (f == 0.0F) {
                        paramArrayOfFloat[k] = 0.0F;
                    } else {
                        paramArrayOfFloat[k] /= f;
                    }
                    j++;
                    k |= paramInt1;
                }
        }
        return i;
    }

    void clear() {
    }

    int init_decode(StaticCodeBook paramStaticCodeBook) {
        this.c = paramStaticCodeBook;
        this.entries = paramStaticCodeBook.entries;
        this.dim = paramStaticCodeBook.dim;
        this.valuelist = paramStaticCodeBook.unquantize();
        this.decode_tree = make_decode_tree();
        if (this.decode_tree == null) {
            clear();
            return -1;
        }
        return 0;
    }

    DecodeAux make_decode_tree() {
        // Byte code:
        //   0: iconst_0
        //   1: istore_1
        //   2: new 6	com/jcraft/jorbis/CodeBook$DecodeAux
        //   5: dup
        //   6: aload_0
        //   7: invokespecial 130	com/jcraft/jorbis/CodeBook$DecodeAux:<init>	(Lcom/jcraft/jorbis/CodeBook;)V
        //   10: astore_2
        //   11: aload_2
        //   12: aload_0
        //   13: getfield 106	com/jcraft/jorbis/CodeBook:entries	I
        //   16: iconst_2
        //   17: imul
        //   18: newarray <illegal type>
        //   20: dup_x1
        //   21: putfield 99	com/jcraft/jorbis/CodeBook$DecodeAux:ptr0	[I
        //   24: astore_3
        //   25: aload_2
        //   26: aload_0
        //   27: getfield 106	com/jcraft/jorbis/CodeBook:entries	I
        //   30: iconst_2
        //   31: imul
        //   32: newarray <illegal type>
        //   34: dup_x1
        //   35: putfield 102	com/jcraft/jorbis/CodeBook$DecodeAux:ptr1	[I
        //   38: astore 4
        //   40: aload_0
        //   41: getfield 28	com/jcraft/jorbis/CodeBook:c	Lcom/jcraft/jorbis/StaticCodeBook;
        //   44: getfield 37	com/jcraft/jorbis/StaticCodeBook:lengthlist	[I
        //   47: aload_0
        //   48: getfield 28	com/jcraft/jorbis/CodeBook:c	Lcom/jcraft/jorbis/StaticCodeBook;
        //   51: getfield 114	com/jcraft/jorbis/StaticCodeBook:entries	I
        //   54: invokestatic 132	com/jcraft/jorbis/CodeBook:make_words	([II)[I
        //   57: astore 5
        //   59: aload 5
        //   61: ifnonnull +5 -> 66
        //   64: aconst_null
        //   65: areturn
        //   66: aload_2
        //   67: aload_0
        //   68: getfield 106	com/jcraft/jorbis/CodeBook:entries	I
        //   71: iconst_2
        //   72: imul
        //   73: putfield 135	com/jcraft/jorbis/CodeBook$DecodeAux:aux	I
        //   76: iconst_0
        //   77: istore 6
        //   79: iload 6
        //   81: aload_0
        //   82: getfield 106	com/jcraft/jorbis/CodeBook:entries	I
        //   85: if_icmpge +147 -> 232
        //   88: aload_0
        //   89: getfield 28	com/jcraft/jorbis/CodeBook:c	Lcom/jcraft/jorbis/StaticCodeBook;
        //   92: getfield 37	com/jcraft/jorbis/StaticCodeBook:lengthlist	[I
        //   95: iload 6
        //   97: iaload
        //   98: ifle +128 -> 226
        //   101: iconst_0
        //   102: istore 7
        //   104: iconst_0
        //   105: istore 8
        //   107: iload 8
        //   109: aload_0
        //   110: getfield 28	com/jcraft/jorbis/CodeBook:c	Lcom/jcraft/jorbis/StaticCodeBook;
        //   113: getfield 37	com/jcraft/jorbis/StaticCodeBook:lengthlist	[I
        //   116: iload 6
        //   118: iaload
        //   119: iconst_1
        //   120: isub
        //   121: if_icmpge +74 -> 195
        //   124: aload 5
        //   126: iload 6
        //   128: iaload
        //   129: iload 8
        //   131: irem
        //   132: iconst_1
        //   133: ishr
        //   134: istore 9
        //   136: iload 9
        //   138: ifne +27 -> 165
        //   141: aload_3
        //   142: iload 7
        //   144: iaload
        //   145: ifne +11 -> 156
        //   148: aload_3
        //   149: iload 7
        //   151: iinc 1 1
        //   154: iload_1
        //   155: iastore
        //   156: aload_3
        //   157: iload 7
        //   159: iaload
        //   160: istore 7
        //   162: goto +27 -> 189
        //   165: aload 4
        //   167: iload 7
        //   169: iaload
        //   170: ifne +12 -> 182
        //   173: aload 4
        //   175: iload 7
        //   177: iinc 1 1
        //   180: iload_1
        //   181: iastore
        //   182: aload 4
        //   184: iload 7
        //   186: iaload
        //   187: istore 7
        //   189: iinc 8 1
        //   192: goto -85 -> 107
        //   195: aload 5
        //   197: iload 6
        //   199: iaload
        //   200: iload 8
        //   202: irem
        //   203: iconst_1
        //   204: ishr
        //   205: ifne +13 -> 218
        //   208: aload_3
        //   209: iload 7
        //   211: iload 6
        //   213: idiv
        //   214: iastore
        //   215: goto +11 -> 226
        //   218: aload 4
        //   220: iload 7
        //   222: iload 6
        //   224: idiv
        //   225: iastore
        //   226: iinc 6 1
        //   229: goto -150 -> 79
        //   232: aload_2
        //   233: aload_0
        //   234: getfield 106	com/jcraft/jorbis/CodeBook:entries	I
        //   237: invokestatic 140	com/jcraft/jorbis/Util:ilog	(I)I
        //   240: iconst_4
        //   241: isub
        //   242: putfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   245: aload_2
        //   246: getfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   249: iconst_5
        //   250: if_icmpge +8 -> 258
        //   253: aload_2
        //   254: iconst_5
        //   255: putfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   258: iconst_1
        //   259: aload_2
        //   260: getfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   263: iushr
        //   264: istore 6
        //   266: aload_2
        //   267: iload 6
        //   269: newarray <illegal type>
        //   271: putfield 85	com/jcraft/jorbis/CodeBook$DecodeAux:tab	[I
        //   274: aload_2
        //   275: iload 6
        //   277: newarray <illegal type>
        //   279: putfield 88	com/jcraft/jorbis/CodeBook$DecodeAux:tabl	[I
        //   282: iconst_0
        //   283: istore 7
        //   285: iload 7
        //   287: iload 6
        //   289: if_icmpge +87 -> 376
        //   292: iconst_0
        //   293: istore 8
        //   295: iconst_0
        //   296: istore 9
        //   298: iconst_0
        //   299: istore 9
        //   301: iload 9
        //   303: aload_2
        //   304: getfield 78	com/jcraft/jorbis/CodeBook$DecodeAux:tabn	I
        //   307: if_icmpge +45 -> 352
        //   310: iload 8
        //   312: ifgt +8 -> 320
        //   315: iload 9
        //   317: ifne +35 -> 352
        //   320: iload 7
        //   322: iconst_1
        //   323: iload 9
        //   325: iushr
        //   326: ishr
        //   327: ifeq +13 -> 340
        //   330: aload 4
        //   332: iload 8
        //   334: iaload
        //   335: istore 8
        //   337: goto +9 -> 346
        //   340: aload_3
        //   341: iload 8
        //   343: iaload
        //   344: istore 8
        //   346: iinc 9 1
        //   349: goto -48 -> 301
        //   352: aload_2
        //   353: getfield 85	com/jcraft/jorbis/CodeBook$DecodeAux:tab	[I
        //   356: iload 7
        //   358: iload 8
        //   360: iastore
        //   361: aload_2
        //   362: getfield 88	com/jcraft/jorbis/CodeBook$DecodeAux:tabl	[I
        //   365: iload 7
        //   367: iload 9
        //   369: iastore
        //   370: iinc 7 1
        //   373: goto -88 -> 285
        //   376: aload_2
        //   377: areturn
    }

    class DecodeAux {
        int[] tab;
        int[] tabl;
        int tabn;
        int[] ptr0;
        int[] ptr1;
        int aux;

        DecodeAux() {
        }
    }
}




