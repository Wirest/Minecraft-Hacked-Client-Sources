/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ 
/*     */ public class CompressedStreamTools
/*     */ {
/*     */   /* Error */
/*     */   public static NBTTagCompound readCompressed(java.io.InputStream is)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 17	java/io/DataInputStream
/*     */     //   3: dup
/*     */     //   4: new 19	java/io/BufferedInputStream
/*     */     //   7: dup
/*     */     //   8: new 21	java/util/zip/GZIPInputStream
/*     */     //   11: dup
/*     */     //   12: aload_0
/*     */     //   13: invokespecial 24	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   16: invokespecial 25	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   19: invokespecial 26	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   22: astore_1
/*     */     //   23: aload_1
/*     */     //   24: getstatic 32	net/minecraft/nbt/NBTSizeTracker:INFINITE	Lnet/minecraft/nbt/NBTSizeTracker;
/*     */     //   27: invokestatic 36	net/minecraft/nbt/CompressedStreamTools:read	(Ljava/io/DataInput;Lnet/minecraft/nbt/NBTSizeTracker;)Lnet/minecraft/nbt/NBTTagCompound;
/*     */     //   30: astore_2
/*     */     //   31: goto +10 -> 41
/*     */     //   34: astore_3
/*     */     //   35: aload_1
/*     */     //   36: invokevirtual 43	java/io/DataInputStream:close	()V
/*     */     //   39: aload_3
/*     */     //   40: athrow
/*     */     //   41: aload_1
/*     */     //   42: invokevirtual 43	java/io/DataInputStream:close	()V
/*     */     //   45: aload_2
/*     */     //   46: areturn
/*     */     // Line number table:
/*     */     //   Java source line #28	-> byte code offset #0
/*     */     //   Java source line #33	-> byte code offset #23
/*     */     //   Java source line #34	-> byte code offset #31
/*     */     //   Java source line #36	-> byte code offset #34
/*     */     //   Java source line #37	-> byte code offset #35
/*     */     //   Java source line #38	-> byte code offset #39
/*     */     //   Java source line #37	-> byte code offset #41
/*     */     //   Java source line #40	-> byte code offset #45
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	47	0	is	java.io.InputStream
/*     */     //   22	20	1	datainputstream	DataInputStream
/*     */     //   30	2	2	nbttagcompound	NBTTagCompound
/*     */     //   41	5	2	nbttagcompound	NBTTagCompound
/*     */     //   34	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   23	34	34	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void writeCompressed(NBTTagCompound p_74799_0_, java.io.OutputStream outputStream)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 55	java/io/DataOutputStream
/*     */     //   3: dup
/*     */     //   4: new 57	java/io/BufferedOutputStream
/*     */     //   7: dup
/*     */     //   8: new 59	java/util/zip/GZIPOutputStream
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: invokespecial 62	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   16: invokespecial 63	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   19: invokespecial 64	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   22: astore_2
/*     */     //   23: aload_0
/*     */     //   24: aload_2
/*     */     //   25: invokestatic 68	net/minecraft/nbt/CompressedStreamTools:write	(Lnet/minecraft/nbt/NBTTagCompound;Ljava/io/DataOutput;)V
/*     */     //   28: goto +10 -> 38
/*     */     //   31: astore_3
/*     */     //   32: aload_2
/*     */     //   33: invokevirtual 71	java/io/DataOutputStream:close	()V
/*     */     //   36: aload_3
/*     */     //   37: athrow
/*     */     //   38: aload_2
/*     */     //   39: invokevirtual 71	java/io/DataOutputStream:close	()V
/*     */     //   42: return
/*     */     // Line number table:
/*     */     //   Java source line #48	-> byte code offset #0
/*     */     //   Java source line #52	-> byte code offset #23
/*     */     //   Java source line #53	-> byte code offset #28
/*     */     //   Java source line #55	-> byte code offset #31
/*     */     //   Java source line #56	-> byte code offset #32
/*     */     //   Java source line #57	-> byte code offset #36
/*     */     //   Java source line #56	-> byte code offset #38
/*     */     //   Java source line #58	-> byte code offset #42
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	43	0	p_74799_0_	NBTTagCompound
/*     */     //   0	43	1	outputStream	java.io.OutputStream
/*     */     //   22	17	2	dataoutputstream	java.io.DataOutputStream
/*     */     //   31	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   23	31	31	finally
/*     */   }
/*     */   
/*     */   public static void safeWrite(NBTTagCompound p_74793_0_, File p_74793_1_)
/*     */     throws IOException
/*     */   {
/*  62 */     File file1 = new File(p_74793_1_.getAbsolutePath() + "_tmp");
/*     */     
/*  64 */     if (file1.exists())
/*     */     {
/*  66 */       file1.delete();
/*     */     }
/*     */     
/*  69 */     write(p_74793_0_, file1);
/*     */     
/*  71 */     if (p_74793_1_.exists())
/*     */     {
/*  73 */       p_74793_1_.delete();
/*     */     }
/*     */     
/*  76 */     if (p_74793_1_.exists())
/*     */     {
/*  78 */       throw new IOException("Failed to delete " + p_74793_1_);
/*     */     }
/*     */     
/*     */ 
/*  82 */     file1.renameTo(p_74793_1_);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void write(NBTTagCompound p_74795_0_, File p_74795_1_)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 55	java/io/DataOutputStream
/*     */     //   3: dup
/*     */     //   4: new 130	java/io/FileOutputStream
/*     */     //   7: dup
/*     */     //   8: aload_1
/*     */     //   9: invokespecial 133	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   12: invokespecial 64	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   15: astore_2
/*     */     //   16: aload_0
/*     */     //   17: aload_2
/*     */     //   18: invokestatic 68	net/minecraft/nbt/CompressedStreamTools:write	(Lnet/minecraft/nbt/NBTTagCompound;Ljava/io/DataOutput;)V
/*     */     //   21: goto +10 -> 31
/*     */     //   24: astore_3
/*     */     //   25: aload_2
/*     */     //   26: invokevirtual 71	java/io/DataOutputStream:close	()V
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     //   31: aload_2
/*     */     //   32: invokevirtual 71	java/io/DataOutputStream:close	()V
/*     */     //   35: return
/*     */     // Line number table:
/*     */     //   Java source line #88	-> byte code offset #0
/*     */     //   Java source line #92	-> byte code offset #16
/*     */     //   Java source line #93	-> byte code offset #21
/*     */     //   Java source line #95	-> byte code offset #24
/*     */     //   Java source line #96	-> byte code offset #25
/*     */     //   Java source line #97	-> byte code offset #29
/*     */     //   Java source line #96	-> byte code offset #31
/*     */     //   Java source line #98	-> byte code offset #35
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	36	0	p_74795_0_	NBTTagCompound
/*     */     //   0	36	1	p_74795_1_	File
/*     */     //   15	17	2	dataoutputstream	java.io.DataOutputStream
/*     */     //   24	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	24	24	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static NBTTagCompound read(File p_74797_0_)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 109	java/io/File:exists	()Z
/*     */     //   4: ifne +5 -> 9
/*     */     //   7: aconst_null
/*     */     //   8: areturn
/*     */     //   9: new 17	java/io/DataInputStream
/*     */     //   12: dup
/*     */     //   13: new 138	java/io/FileInputStream
/*     */     //   16: dup
/*     */     //   17: aload_0
/*     */     //   18: invokespecial 139	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   21: invokespecial 26	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   24: astore_1
/*     */     //   25: aload_1
/*     */     //   26: getstatic 32	net/minecraft/nbt/NBTSizeTracker:INFINITE	Lnet/minecraft/nbt/NBTSizeTracker;
/*     */     //   29: invokestatic 36	net/minecraft/nbt/CompressedStreamTools:read	(Ljava/io/DataInput;Lnet/minecraft/nbt/NBTSizeTracker;)Lnet/minecraft/nbt/NBTTagCompound;
/*     */     //   32: astore_2
/*     */     //   33: goto +10 -> 43
/*     */     //   36: astore_3
/*     */     //   37: aload_1
/*     */     //   38: invokevirtual 43	java/io/DataInputStream:close	()V
/*     */     //   41: aload_3
/*     */     //   42: athrow
/*     */     //   43: aload_1
/*     */     //   44: invokevirtual 43	java/io/DataInputStream:close	()V
/*     */     //   47: aload_2
/*     */     //   48: areturn
/*     */     // Line number table:
/*     */     //   Java source line #102	-> byte code offset #0
/*     */     //   Java source line #104	-> byte code offset #7
/*     */     //   Java source line #108	-> byte code offset #9
/*     */     //   Java source line #113	-> byte code offset #25
/*     */     //   Java source line #114	-> byte code offset #33
/*     */     //   Java source line #116	-> byte code offset #36
/*     */     //   Java source line #117	-> byte code offset #37
/*     */     //   Java source line #118	-> byte code offset #41
/*     */     //   Java source line #117	-> byte code offset #43
/*     */     //   Java source line #120	-> byte code offset #47
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	49	0	p_74797_0_	File
/*     */     //   24	20	1	datainputstream	DataInputStream
/*     */     //   32	2	2	nbttagcompound	NBTTagCompound
/*     */     //   43	5	2	nbttagcompound	NBTTagCompound
/*     */     //   36	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   25	36	36	finally
/*     */   }
/*     */   
/*     */   public static NBTTagCompound read(DataInputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 129 */     return read(inputStream, NBTSizeTracker.INFINITE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static NBTTagCompound read(DataInput p_152456_0_, NBTSizeTracker p_152456_1_)
/*     */     throws IOException
/*     */   {
/* 137 */     NBTBase nbtbase = func_152455_a(p_152456_0_, 0, p_152456_1_);
/*     */     
/* 139 */     if ((nbtbase instanceof NBTTagCompound))
/*     */     {
/* 141 */       return (NBTTagCompound)nbtbase;
/*     */     }
/*     */     
/*     */ 
/* 145 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */   
/*     */   public static void write(NBTTagCompound p_74800_0_, DataOutput p_74800_1_)
/*     */     throws IOException
/*     */   {
/* 151 */     writeTag(p_74800_0_, p_74800_1_);
/*     */   }
/*     */   
/*     */   private static void writeTag(NBTBase p_150663_0_, DataOutput p_150663_1_) throws IOException
/*     */   {
/* 156 */     p_150663_1_.writeByte(p_150663_0_.getId());
/*     */     
/* 158 */     if (p_150663_0_.getId() != 0)
/*     */     {
/* 160 */       p_150663_1_.writeUTF("");
/* 161 */       p_150663_0_.write(p_150663_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   private static NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_) throws IOException
/*     */   {
/* 167 */     byte b0 = p_152455_0_.readByte();
/*     */     
/* 169 */     if (b0 == 0)
/*     */     {
/* 171 */       return new NBTTagEnd();
/*     */     }
/*     */     
/*     */ 
/* 175 */     p_152455_0_.readUTF();
/* 176 */     NBTBase nbtbase = NBTBase.createNewByType(b0);
/*     */     
/*     */     try
/*     */     {
/* 180 */       nbtbase.read(p_152455_0_, p_152455_1_, p_152455_2_);
/* 181 */       return nbtbase;
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 185 */       CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
/* 186 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
/* 187 */       crashreportcategory.addCrashSection("Tag name", "[UNNAMED TAG]");
/* 188 */       crashreportcategory.addCrashSection("Tag type", Byte.valueOf(b0));
/* 189 */       throw new ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\CompressedStreamTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */