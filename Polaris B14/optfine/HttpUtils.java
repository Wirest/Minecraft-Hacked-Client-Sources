package optfine;

public class HttpUtils
{
  public static final String SERVER_URL = "http://s.optifine.net";
  public static final String POST_URL = "http://optifine.net";
  
  /* Error */
  public static byte[] get(String p_get_0_)
    throws java.io.IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: new 24	java/net/URL
    //   5: dup
    //   6: aload_0
    //   7: invokespecial 27	java/net/URL:<init>	(Ljava/lang/String;)V
    //   10: astore_3
    //   11: aload_3
    //   12: invokestatic 33	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
    //   15: invokevirtual 37	net/minecraft/client/Minecraft:getProxy	()Ljava/net/Proxy;
    //   18: invokevirtual 41	java/net/URL:openConnection	(Ljava/net/Proxy;)Ljava/net/URLConnection;
    //   21: checkcast 43	java/net/HttpURLConnection
    //   24: astore_1
    //   25: aload_1
    //   26: iconst_1
    //   27: invokevirtual 47	java/net/HttpURLConnection:setDoInput	(Z)V
    //   30: aload_1
    //   31: iconst_0
    //   32: invokevirtual 50	java/net/HttpURLConnection:setDoOutput	(Z)V
    //   35: aload_1
    //   36: invokevirtual 53	java/net/HttpURLConnection:connect	()V
    //   39: aload_1
    //   40: invokevirtual 57	java/net/HttpURLConnection:getResponseCode	()I
    //   43: bipush 100
    //   45: idiv
    //   46: iconst_2
    //   47: if_icmpeq +30 -> 77
    //   50: new 22	java/io/IOException
    //   53: dup
    //   54: new 59	java/lang/StringBuilder
    //   57: dup
    //   58: ldc 61
    //   60: invokespecial 62	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   63: aload_1
    //   64: invokevirtual 57	java/net/HttpURLConnection:getResponseCode	()I
    //   67: invokevirtual 66	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   70: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   73: invokespecial 71	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   76: athrow
    //   77: aload_1
    //   78: invokevirtual 77	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   81: astore 4
    //   83: aload_1
    //   84: invokevirtual 80	java/net/HttpURLConnection:getContentLength	()I
    //   87: newarray <illegal type>
    //   89: astore 5
    //   91: iconst_0
    //   92: istore 6
    //   94: aload 4
    //   96: aload 5
    //   98: iload 6
    //   100: aload 5
    //   102: arraylength
    //   103: iload 6
    //   105: isub
    //   106: invokevirtual 88	java/io/InputStream:read	([BII)I
    //   109: istore 7
    //   111: iload 7
    //   113: ifge +27 -> 140
    //   116: new 22	java/io/IOException
    //   119: dup
    //   120: new 59	java/lang/StringBuilder
    //   123: dup
    //   124: ldc 90
    //   126: invokespecial 62	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   129: aload_0
    //   130: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   136: invokespecial 71	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   139: athrow
    //   140: iload 6
    //   142: iload 7
    //   144: iadd
    //   145: istore 6
    //   147: iload 6
    //   149: aload 5
    //   151: arraylength
    //   152: if_icmplt -58 -> 94
    //   155: aload 5
    //   157: astore_2
    //   158: goto +16 -> 174
    //   161: astore 8
    //   163: aload_1
    //   164: ifnull +7 -> 171
    //   167: aload_1
    //   168: invokevirtual 98	java/net/HttpURLConnection:disconnect	()V
    //   171: aload 8
    //   173: athrow
    //   174: aload_1
    //   175: ifnull +7 -> 182
    //   178: aload_1
    //   179: invokevirtual 98	java/net/HttpURLConnection:disconnect	()V
    //   182: aload_2
    //   183: areturn
    // Line number table:
    //   Java source line #20	-> byte code offset #0
    //   Java source line #25	-> byte code offset #2
    //   Java source line #26	-> byte code offset #11
    //   Java source line #27	-> byte code offset #25
    //   Java source line #28	-> byte code offset #30
    //   Java source line #29	-> byte code offset #35
    //   Java source line #31	-> byte code offset #39
    //   Java source line #33	-> byte code offset #50
    //   Java source line #36	-> byte code offset #77
    //   Java source line #37	-> byte code offset #83
    //   Java source line #38	-> byte code offset #91
    //   Java source line #42	-> byte code offset #94
    //   Java source line #44	-> byte code offset #111
    //   Java source line #46	-> byte code offset #116
    //   Java source line #49	-> byte code offset #140
    //   Java source line #51	-> byte code offset #147
    //   Java source line #57	-> byte code offset #155
    //   Java source line #58	-> byte code offset #158
    //   Java source line #60	-> byte code offset #161
    //   Java source line #61	-> byte code offset #163
    //   Java source line #63	-> byte code offset #167
    //   Java source line #65	-> byte code offset #171
    //   Java source line #61	-> byte code offset #174
    //   Java source line #63	-> byte code offset #178
    //   Java source line #67	-> byte code offset #182
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	p_get_0_	String
    //   1	178	1	httpurlconnection	java.net.HttpURLConnection
    //   157	2	2	abyte1	byte[]
    //   174	9	2	abyte1	byte[]
    //   10	2	3	url	java.net.URL
    //   81	14	4	inputstream	java.io.InputStream
    //   89	67	5	abyte	byte[]
    //   92	56	6	i	int
    //   109	34	7	j	int
    //   161	11	8	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	161	161	finally
  }
  
  /* Error */
  public static String post(String p_post_0_, java.util.Map p_post_1_, byte[] p_post_2_)
    throws java.io.IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 24	java/net/URL
    //   5: dup
    //   6: aload_0
    //   7: invokespecial 27	java/net/URL:<init>	(Ljava/lang/String;)V
    //   10: astore 5
    //   12: aload 5
    //   14: invokestatic 33	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
    //   17: invokevirtual 37	net/minecraft/client/Minecraft:getProxy	()Ljava/net/Proxy;
    //   20: invokevirtual 41	java/net/URL:openConnection	(Ljava/net/Proxy;)Ljava/net/URLConnection;
    //   23: checkcast 43	java/net/HttpURLConnection
    //   26: astore_3
    //   27: aload_3
    //   28: ldc 114
    //   30: invokevirtual 117	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
    //   33: aload_1
    //   34: ifnull +72 -> 106
    //   37: aload_1
    //   38: invokeinterface 123 1 0
    //   43: invokeinterface 129 1 0
    //   48: astore 7
    //   50: goto +46 -> 96
    //   53: aload 7
    //   55: invokeinterface 135 1 0
    //   60: astore 6
    //   62: new 59	java/lang/StringBuilder
    //   65: dup
    //   66: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   69: aload_1
    //   70: aload 6
    //   72: invokeinterface 139 2 0
    //   77: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   80: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   83: astore 8
    //   85: aload_3
    //   86: aload 6
    //   88: checkcast 73	java/lang/String
    //   91: aload 8
    //   93: invokevirtual 146	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   96: aload 7
    //   98: invokeinterface 150 1 0
    //   103: ifne -50 -> 53
    //   106: aload_3
    //   107: ldc -104
    //   109: ldc -102
    //   111: invokevirtual 146	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   114: aload_3
    //   115: ldc -100
    //   117: new 59	java/lang/StringBuilder
    //   120: dup
    //   121: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   124: aload_2
    //   125: arraylength
    //   126: invokevirtual 66	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   129: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: invokevirtual 146	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   135: aload_3
    //   136: ldc -98
    //   138: ldc -96
    //   140: invokevirtual 146	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   143: aload_3
    //   144: iconst_0
    //   145: invokevirtual 163	java/net/HttpURLConnection:setUseCaches	(Z)V
    //   148: aload_3
    //   149: iconst_1
    //   150: invokevirtual 47	java/net/HttpURLConnection:setDoInput	(Z)V
    //   153: aload_3
    //   154: iconst_1
    //   155: invokevirtual 50	java/net/HttpURLConnection:setDoOutput	(Z)V
    //   158: aload_3
    //   159: invokevirtual 167	java/net/HttpURLConnection:getOutputStream	()Ljava/io/OutputStream;
    //   162: astore 6
    //   164: aload 6
    //   166: aload_2
    //   167: invokevirtual 173	java/io/OutputStream:write	([B)V
    //   170: aload 6
    //   172: invokevirtual 176	java/io/OutputStream:flush	()V
    //   175: aload 6
    //   177: invokevirtual 179	java/io/OutputStream:close	()V
    //   180: aload_3
    //   181: invokevirtual 77	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   184: astore 7
    //   186: new 181	java/io/InputStreamReader
    //   189: dup
    //   190: aload 7
    //   192: ldc -73
    //   194: invokespecial 186	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   197: astore 8
    //   199: new 188	java/io/BufferedReader
    //   202: dup
    //   203: aload 8
    //   205: invokespecial 191	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   208: astore 9
    //   210: new 193	java/lang/StringBuffer
    //   213: dup
    //   214: invokespecial 194	java/lang/StringBuffer:<init>	()V
    //   217: astore 10
    //   219: goto +19 -> 238
    //   222: aload 10
    //   224: aload 11
    //   226: invokevirtual 197	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   229: pop
    //   230: aload 10
    //   232: bipush 13
    //   234: invokevirtual 200	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
    //   237: pop
    //   238: aload 9
    //   240: invokevirtual 203	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   243: dup
    //   244: astore 11
    //   246: ifnonnull -24 -> 222
    //   249: aload 9
    //   251: invokevirtual 204	java/io/BufferedReader:close	()V
    //   254: aload 10
    //   256: invokevirtual 205	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   259: astore 4
    //   261: goto +16 -> 277
    //   264: astore 12
    //   266: aload_3
    //   267: ifnull +7 -> 274
    //   270: aload_3
    //   271: invokevirtual 98	java/net/HttpURLConnection:disconnect	()V
    //   274: aload 12
    //   276: athrow
    //   277: aload_3
    //   278: ifnull +7 -> 285
    //   281: aload_3
    //   282: invokevirtual 98	java/net/HttpURLConnection:disconnect	()V
    //   285: aload 4
    //   287: areturn
    // Line number table:
    //   Java source line #72	-> byte code offset #0
    //   Java source line #77	-> byte code offset #2
    //   Java source line #78	-> byte code offset #12
    //   Java source line #79	-> byte code offset #27
    //   Java source line #81	-> byte code offset #33
    //   Java source line #83	-> byte code offset #37
    //   Java source line #85	-> byte code offset #62
    //   Java source line #86	-> byte code offset #85
    //   Java source line #83	-> byte code offset #96
    //   Java source line #90	-> byte code offset #106
    //   Java source line #91	-> byte code offset #114
    //   Java source line #92	-> byte code offset #135
    //   Java source line #93	-> byte code offset #143
    //   Java source line #94	-> byte code offset #148
    //   Java source line #95	-> byte code offset #153
    //   Java source line #96	-> byte code offset #158
    //   Java source line #97	-> byte code offset #164
    //   Java source line #98	-> byte code offset #170
    //   Java source line #99	-> byte code offset #175
    //   Java source line #100	-> byte code offset #180
    //   Java source line #101	-> byte code offset #186
    //   Java source line #102	-> byte code offset #199
    //   Java source line #103	-> byte code offset #210
    //   Java source line #106	-> byte code offset #219
    //   Java source line #108	-> byte code offset #222
    //   Java source line #109	-> byte code offset #230
    //   Java source line #106	-> byte code offset #238
    //   Java source line #112	-> byte code offset #249
    //   Java source line #113	-> byte code offset #254
    //   Java source line #114	-> byte code offset #261
    //   Java source line #116	-> byte code offset #264
    //   Java source line #117	-> byte code offset #266
    //   Java source line #119	-> byte code offset #270
    //   Java source line #121	-> byte code offset #274
    //   Java source line #117	-> byte code offset #277
    //   Java source line #119	-> byte code offset #281
    //   Java source line #123	-> byte code offset #285
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	p_post_0_	String
    //   0	288	1	p_post_1_	java.util.Map
    //   0	288	2	p_post_2_	byte[]
    //   1	281	3	httpurlconnection	java.net.HttpURLConnection
    //   259	3	4	s3	String
    //   277	9	4	s3	String
    //   10	3	5	url	java.net.URL
    //   60	27	6	s	Object
    //   162	14	6	outputstream	java.io.OutputStream
    //   48	49	7	localIterator	java.util.Iterator
    //   184	7	7	inputstream	java.io.InputStream
    //   83	9	8	s1	String
    //   197	7	8	inputstreamreader	java.io.InputStreamReader
    //   208	42	9	bufferedreader	java.io.BufferedReader
    //   217	38	10	stringbuffer	StringBuffer
    //   222	3	11	s2	String
    //   244	3	11	s2	String
    //   264	11	12	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   2	264	264	finally
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\HttpUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */