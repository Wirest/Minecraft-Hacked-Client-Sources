/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserList<K, V extends UserListEntry<K>>
/*     */ {
/*  31 */   protected static final Logger logger = ;
/*     */   protected final Gson gson;
/*     */   private final File saveFile;
/*  34 */   private final Map<String, V> values = Maps.newHashMap();
/*  35 */   private boolean lanServer = true;
/*  36 */   private static final ParameterizedType saveFileFormat = new ParameterizedType()
/*     */   {
/*     */     public Type[] getActualTypeArguments()
/*     */     {
/*  40 */       return new Type[] { UserListEntry.class };
/*     */     }
/*     */     
/*     */     public Type getRawType() {
/*  44 */       return List.class;
/*     */     }
/*     */     
/*     */     public Type getOwnerType() {
/*  48 */       return null;
/*     */     }
/*     */   };
/*     */   
/*     */   public UserList(File saveFile)
/*     */   {
/*  54 */     this.saveFile = saveFile;
/*  55 */     GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting();
/*  56 */     gsonbuilder.registerTypeHierarchyAdapter(UserListEntry.class, new Serializer(null));
/*  57 */     this.gson = gsonbuilder.create();
/*     */   }
/*     */   
/*     */   public boolean isLanServer()
/*     */   {
/*  62 */     return this.lanServer;
/*     */   }
/*     */   
/*     */   public void setLanServer(boolean state)
/*     */   {
/*  67 */     this.lanServer = state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntry(V entry)
/*     */   {
/*  75 */     this.values.put(getObjectKey(entry.getValue()), entry);
/*     */     
/*     */     try
/*     */     {
/*  79 */       writeChanges();
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/*  83 */       logger.warn("Could not save the list after adding a user.", ioexception);
/*     */     }
/*     */   }
/*     */   
/*     */   public V getEntry(K obj)
/*     */   {
/*  89 */     removeExpired();
/*  90 */     return (UserListEntry)this.values.get(getObjectKey(obj));
/*     */   }
/*     */   
/*     */   public void removeEntry(K p_152684_1_)
/*     */   {
/*  95 */     this.values.remove(getObjectKey(p_152684_1_));
/*     */     
/*     */     try
/*     */     {
/*  99 */       writeChanges();
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 103 */       logger.warn("Could not save the list after removing a user.", ioexception);
/*     */     }
/*     */   }
/*     */   
/*     */   public String[] getKeys()
/*     */   {
/* 109 */     return (String[])this.values.keySet().toArray(new String[this.values.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getObjectKey(K obj)
/*     */   {
/* 117 */     return obj.toString();
/*     */   }
/*     */   
/*     */   protected boolean hasEntry(K entry)
/*     */   {
/* 122 */     return this.values.containsKey(getObjectKey(entry));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeExpired()
/*     */   {
/* 130 */     List<K> list = Lists.newArrayList();
/*     */     
/* 132 */     for (V v : this.values.values())
/*     */     {
/* 134 */       if (v.hasBanExpired())
/*     */       {
/* 136 */         list.add(v.getValue());
/*     */       }
/*     */     }
/*     */     
/* 140 */     for (K k : list)
/*     */     {
/* 142 */       this.values.remove(k);
/*     */     }
/*     */   }
/*     */   
/*     */   protected UserListEntry<K> createEntry(JsonObject entryData)
/*     */   {
/* 148 */     return new UserListEntry(null, entryData);
/*     */   }
/*     */   
/*     */   protected Map<String, V> getValues()
/*     */   {
/* 153 */     return this.values;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void writeChanges()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 49	net/minecraft/server/management/UserList:values	Ljava/util/Map;
/*     */     //   4: invokeinterface 177 1 0
/*     */     //   9: astore_1
/*     */     //   10: aload_0
/*     */     //   11: getfield 75	net/minecraft/server/management/UserList:gson	Lcom/google/gson/Gson;
/*     */     //   14: aload_1
/*     */     //   15: invokevirtual 219	com/google/gson/Gson:toJson	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   18: astore_2
/*     */     //   19: aconst_null
/*     */     //   20: astore_3
/*     */     //   21: aload_0
/*     */     //   22: getfield 53	net/minecraft/server/management/UserList:saveFile	Ljava/io/File;
/*     */     //   25: getstatic 225	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   28: invokestatic 231	com/google/common/io/Files:newWriter	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedWriter;
/*     */     //   31: astore_3
/*     */     //   32: aload_3
/*     */     //   33: aload_2
/*     */     //   34: invokevirtual 237	java/io/BufferedWriter:write	(Ljava/lang/String;)V
/*     */     //   37: goto +12 -> 49
/*     */     //   40: astore 4
/*     */     //   42: aload_3
/*     */     //   43: invokestatic 245	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   46: aload 4
/*     */     //   48: athrow
/*     */     //   49: aload_3
/*     */     //   50: invokestatic 245	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
/*     */     //   53: return
/*     */     // Line number table:
/*     */     //   Java source line #158	-> byte code offset #0
/*     */     //   Java source line #159	-> byte code offset #10
/*     */     //   Java source line #160	-> byte code offset #19
/*     */     //   Java source line #164	-> byte code offset #21
/*     */     //   Java source line #165	-> byte code offset #32
/*     */     //   Java source line #166	-> byte code offset #37
/*     */     //   Java source line #168	-> byte code offset #40
/*     */     //   Java source line #169	-> byte code offset #42
/*     */     //   Java source line #170	-> byte code offset #46
/*     */     //   Java source line #169	-> byte code offset #49
/*     */     //   Java source line #171	-> byte code offset #53
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	54	0	this	UserList<K, V>
/*     */     //   9	6	1	collection	java.util.Collection<V>
/*     */     //   18	16	2	s	String
/*     */     //   20	30	3	bufferedwriter	java.io.BufferedWriter
/*     */     //   40	7	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	40	40	finally
/*     */   }
/*     */   
/*     */   class Serializer
/*     */     implements JsonDeserializer<UserListEntry<K>>, JsonSerializer<UserListEntry<K>>
/*     */   {
/*     */     private Serializer() {}
/*     */     
/*     */     public JsonElement serialize(UserListEntry<K> p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 181 */       JsonObject jsonobject = new JsonObject();
/* 182 */       p_serialize_1_.onSerialization(jsonobject);
/* 183 */       return jsonobject;
/*     */     }
/*     */     
/*     */     public UserListEntry<K> deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 188 */       if (p_deserialize_1_.isJsonObject())
/*     */       {
/* 190 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 191 */         UserListEntry<K> userlistentry = UserList.this.createEntry(jsonobject);
/* 192 */         return userlistentry;
/*     */       }
/*     */       
/*     */ 
/* 196 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */