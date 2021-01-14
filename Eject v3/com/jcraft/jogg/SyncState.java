package com.jcraft.jogg;

public class SyncState {
    public byte[] data;
    int storage;
    int fill;
    int returned;
    int unsynced;
    int headerbytes;
    int bodybytes;
    private Page pageseek = new Page();
    private byte[] chksum = new byte[4];

    public int clear() {
        this.data = null;
        return 0;
    }

    public int buffer(int paramInt) {
        if (this.returned != 0) {
            this.fill -= this.returned;
            if (this.fill > 0) {
                System.arraycopy(this.data, this.returned, this.data, 0, this.fill);
            }
            this.returned = 0;
        }
        if (paramInt > this.storage - this.fill) {
            int i = paramInt | this.fill | 0x1000;
            if (this.data != null) {
                byte[] arrayOfByte = new byte[i];
                System.arraycopy(this.data, 0, arrayOfByte, 0, this.data.length);
                this.data = arrayOfByte;
            } else {
                this.data = new byte[i];
            }
            this.storage = i;
        }
        return this.fill;
    }

    public int wrote(int paramInt) {
        if ((this.fill | paramInt) > this.storage) {
            return -1;
        }
        this.fill |= paramInt;
        return 0;
    }

    public int pageseek(Page arg1) {
        // Byte code:
        //   0: aload_0
        //   1: getfield 35	com/jcraft/jogg/SyncState:returned	I
        //   4: istore_2
        //   5: aload_0
        //   6: getfield 37	com/jcraft/jogg/SyncState:fill	I
        //   9: aload_0
        //   10: getfield 35	com/jcraft/jogg/SyncState:returned	I
        //   13: isub
        //   14: istore 4
        //   16: aload_0
        //   17: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   20: ifne +222 -> 242
        //   23: iload 4
        //   25: bipush 27
        //   27: if_icmpge +5 -> 32
        //   30: iconst_0
        //   31: ireturn
        //   32: aload_0
        //   33: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   36: iload_2
        //   37: baload
        //   38: bipush 79
        //   40: if_icmpne +42 -> 82
        //   43: aload_0
        //   44: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   47: iload_2
        //   48: iconst_1
        //   49: ior
        //   50: baload
        //   51: bipush 103
        //   53: if_icmpne +29 -> 82
        //   56: aload_0
        //   57: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   60: iload_2
        //   61: iconst_2
        //   62: ior
        //   63: baload
        //   64: bipush 103
        //   66: if_icmpne +16 -> 82
        //   69: aload_0
        //   70: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   73: iload_2
        //   74: iconst_3
        //   75: ior
        //   76: baload
        //   77: bipush 83
        //   79: if_icmpeq +78 -> 157
        //   82: aload_0
        //   83: iconst_0
        //   84: putfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   87: aload_0
        //   88: iconst_0
        //   89: putfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   92: iconst_0
        //   93: istore_3
        //   94: iconst_0
        //   95: istore 7
        //   97: iload 7
        //   99: iload 4
        //   101: iconst_1
        //   102: isub
        //   103: if_icmpge +35 -> 138
        //   106: aload_0
        //   107: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   110: iload_2
        //   111: iconst_1
        //   112: ior
        //   113: iload 7
        //   115: ior
        //   116: baload
        //   117: bipush 79
        //   119: if_icmpne +13 -> 132
        //   122: iload_2
        //   123: iconst_1
        //   124: ior
        //   125: iload 7
        //   127: ior
        //   128: istore_3
        //   129: goto +9 -> 138
        //   132: iinc 7 1
        //   135: goto -38 -> 97
        //   138: iload_3
        //   139: ifne +8 -> 147
        //   142: aload_0
        //   143: getfield 37	com/jcraft/jogg/SyncState:fill	I
        //   146: istore_3
        //   147: aload_0
        //   148: iload_3
        //   149: putfield 35	com/jcraft/jogg/SyncState:returned	I
        //   152: iload_3
        //   153: iload_2
        //   154: isub
        //   155: idiv
        //   156: ireturn
        //   157: aload_0
        //   158: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   161: iload_2
        //   162: bipush 26
        //   164: ior
        //   165: baload
        //   166: sipush 255
        //   169: ishr
        //   170: bipush 27
        //   172: ior
        //   173: istore 5
        //   175: iload 4
        //   177: iload 5
        //   179: if_icmpge +5 -> 184
        //   182: iconst_0
        //   183: ireturn
        //   184: iconst_0
        //   185: istore 6
        //   187: iload 6
        //   189: aload_0
        //   190: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   193: iload_2
        //   194: bipush 26
        //   196: ior
        //   197: baload
        //   198: sipush 255
        //   201: ishr
        //   202: if_icmpge +34 -> 236
        //   205: aload_0
        //   206: dup
        //   207: getfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   210: aload_0
        //   211: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   214: iload_2
        //   215: bipush 27
        //   217: ior
        //   218: iload 6
        //   220: ior
        //   221: baload
        //   222: sipush 255
        //   225: ishr
        //   226: ior
        //   227: putfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   230: iinc 6 1
        //   233: goto -46 -> 187
        //   236: aload_0
        //   237: iload 5
        //   239: putfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   242: aload_0
        //   243: getfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   246: aload_0
        //   247: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   250: ior
        //   251: iload 4
        //   253: if_icmple +5 -> 258
        //   256: iconst_0
        //   257: ireturn
        //   258: aload_0
        //   259: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   262: dup
        //   263: astore 5
        //   265: monitorenter
        //   266: aload_0
        //   267: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   270: iload_2
        //   271: bipush 22
        //   273: ior
        //   274: aload_0
        //   275: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   278: iconst_0
        //   279: iconst_4
        //   280: invokestatic 43	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   283: aload_0
        //   284: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   287: iload_2
        //   288: bipush 22
        //   290: ior
        //   291: iconst_0
        //   292: bastore
        //   293: aload_0
        //   294: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   297: iload_2
        //   298: bipush 23
        //   300: ior
        //   301: iconst_0
        //   302: bastore
        //   303: aload_0
        //   304: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   307: iload_2
        //   308: bipush 24
        //   310: ior
        //   311: iconst_0
        //   312: bastore
        //   313: aload_0
        //   314: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   317: iload_2
        //   318: bipush 25
        //   320: ior
        //   321: iconst_0
        //   322: bastore
        //   323: aload_0
        //   324: getfield 25	com/jcraft/jogg/SyncState:pageseek	Lcom/jcraft/jogg/Page;
        //   327: astore 6
        //   329: aload 6
        //   331: aload_0
        //   332: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   335: putfield 54	com/jcraft/jogg/Page:header_base	[B
        //   338: aload 6
        //   340: iload_2
        //   341: putfield 57	com/jcraft/jogg/Page:header	I
        //   344: aload 6
        //   346: aload_0
        //   347: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   350: putfield 60	com/jcraft/jogg/Page:header_len	I
        //   353: aload 6
        //   355: aload_0
        //   356: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   359: putfield 63	com/jcraft/jogg/Page:body_base	[B
        //   362: aload 6
        //   364: iload_2
        //   365: aload_0
        //   366: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   369: ior
        //   370: putfield 66	com/jcraft/jogg/Page:body	I
        //   373: aload 6
        //   375: aload_0
        //   376: getfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   379: putfield 69	com/jcraft/jogg/Page:body_len	I
        //   382: aload 6
        //   384: invokevirtual 72	com/jcraft/jogg/Page:checksum	()V
        //   387: aload_0
        //   388: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   391: iconst_0
        //   392: baload
        //   393: aload_0
        //   394: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   397: iload_2
        //   398: bipush 22
        //   400: ior
        //   401: baload
        //   402: if_icmpne +57 -> 459
        //   405: aload_0
        //   406: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   409: iconst_1
        //   410: baload
        //   411: aload_0
        //   412: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   415: iload_2
        //   416: bipush 23
        //   418: ior
        //   419: baload
        //   420: if_icmpne +39 -> 459
        //   423: aload_0
        //   424: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   427: iconst_2
        //   428: baload
        //   429: aload_0
        //   430: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   433: iload_2
        //   434: bipush 24
        //   436: ior
        //   437: baload
        //   438: if_icmpne +21 -> 459
        //   441: aload_0
        //   442: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   445: iconst_3
        //   446: baload
        //   447: aload_0
        //   448: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   451: iload_2
        //   452: bipush 25
        //   454: ior
        //   455: baload
        //   456: if_icmpeq +98 -> 554
        //   459: aload_0
        //   460: getfield 27	com/jcraft/jogg/SyncState:chksum	[B
        //   463: iconst_0
        //   464: aload_0
        //   465: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   468: iload_2
        //   469: bipush 22
        //   471: ior
        //   472: iconst_4
        //   473: invokestatic 43	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
        //   476: aload_0
        //   477: iconst_0
        //   478: putfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   481: aload_0
        //   482: iconst_0
        //   483: putfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   486: iconst_0
        //   487: istore_3
        //   488: iconst_0
        //   489: istore 7
        //   491: iload 7
        //   493: iload 4
        //   495: iconst_1
        //   496: isub
        //   497: if_icmpge +35 -> 532
        //   500: aload_0
        //   501: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   504: iload_2
        //   505: iconst_1
        //   506: ior
        //   507: iload 7
        //   509: ior
        //   510: baload
        //   511: bipush 79
        //   513: if_icmpne +13 -> 526
        //   516: iload_2
        //   517: iconst_1
        //   518: ior
        //   519: iload 7
        //   521: ior
        //   522: istore_3
        //   523: goto +9 -> 532
        //   526: iinc 7 1
        //   529: goto -38 -> 491
        //   532: iload_3
        //   533: ifne +8 -> 541
        //   536: aload_0
        //   537: getfield 37	com/jcraft/jogg/SyncState:fill	I
        //   540: istore_3
        //   541: aload_0
        //   542: iload_3
        //   543: putfield 35	com/jcraft/jogg/SyncState:returned	I
        //   546: iload_3
        //   547: iload_2
        //   548: isub
        //   549: idiv
        //   550: aload 5
        //   552: monitorexit
        //   553: ireturn
        //   554: aload 5
        //   556: monitorexit
        //   557: goto +11 -> 568
        //   560: astore 8
        //   562: aload 5
        //   564: monitorexit
        //   565: aload 8
        //   567: athrow
        //   568: aload_0
        //   569: getfield 35	com/jcraft/jogg/SyncState:returned	I
        //   572: istore_2
        //   573: aload_1
        //   574: ifnull +50 -> 624
        //   577: aload_1
        //   578: aload_0
        //   579: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   582: putfield 54	com/jcraft/jogg/Page:header_base	[B
        //   585: aload_1
        //   586: iload_2
        //   587: putfield 57	com/jcraft/jogg/Page:header	I
        //   590: aload_1
        //   591: aload_0
        //   592: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   595: putfield 60	com/jcraft/jogg/Page:header_len	I
        //   598: aload_1
        //   599: aload_0
        //   600: getfield 31	com/jcraft/jogg/SyncState:data	[B
        //   603: putfield 63	com/jcraft/jogg/Page:body_base	[B
        //   606: aload_1
        //   607: iload_2
        //   608: aload_0
        //   609: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   612: ior
        //   613: putfield 66	com/jcraft/jogg/Page:body	I
        //   616: aload_1
        //   617: aload_0
        //   618: getfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   621: putfield 69	com/jcraft/jogg/Page:body_len	I
        //   624: aload_0
        //   625: iconst_0
        //   626: putfield 74	com/jcraft/jogg/SyncState:unsynced	I
        //   629: aload_0
        //   630: dup
        //   631: getfield 35	com/jcraft/jogg/SyncState:returned	I
        //   634: aload_0
        //   635: getfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   638: aload_0
        //   639: getfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   642: ior
        //   643: dup
        //   644: istore 4
        //   646: ior
        //   647: putfield 35	com/jcraft/jogg/SyncState:returned	I
        //   650: aload_0
        //   651: iconst_0
        //   652: putfield 49	com/jcraft/jogg/SyncState:headerbytes	I
        //   655: aload_0
        //   656: iconst_0
        //   657: putfield 51	com/jcraft/jogg/SyncState:bodybytes	I
        //   660: iload 4
        //   662: ireturn
        // Exception table:
        //   from	to	target	type
        //   266	553	560	finally
        //   554	557	560	finally
        //   560	565	560	finally
    }

    public int pageout(Page paramPage) {
        for (; ; ) {
            int i = pageseek(paramPage);
            if (i > 0) {
                return 1;
            }
            if (i == 0) {
                return 0;
            }
            if (this.unsynced == 0) {
                this.unsynced = 1;
                return -1;
            }
        }
    }

    public int reset() {
        this.fill = 0;
        this.returned = 0;
        this.unsynced = 0;
        this.headerbytes = 0;
        this.bodybytes = 0;
        return 0;
    }

    public void init() {
    }

    public int getDataOffset() {
        return this.returned;
    }

    public int getBufferOffset() {
        return this.fill;
    }
}




