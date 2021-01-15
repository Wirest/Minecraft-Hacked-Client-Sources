/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.Rotations;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ 
/*     */ 
/*     */ public class DataWatcher
/*     */ {
/*     */   private final Entity owner;
/*  24 */   private boolean isBlank = true;
/*  25 */   private static final Map<Class<?>, Integer> dataTypes = ;
/*  26 */   private final Map<Integer, WatchableObject> watchedObjects = Maps.newHashMap();
/*     */   
/*     */   private boolean objectChanged;
/*     */   
/*  30 */   private ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */   
/*     */   public DataWatcher(Entity owner)
/*     */   {
/*  34 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public <T> void addObject(int id, T object)
/*     */   {
/*  39 */     Integer integer = (Integer)dataTypes.get(object.getClass());
/*     */     
/*  41 */     if (integer == null)
/*     */     {
/*  43 */       throw new IllegalArgumentException("Unknown data type: " + object.getClass());
/*     */     }
/*  45 */     if (id > 31)
/*     */     {
/*  47 */       throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
/*     */     }
/*  49 */     if (this.watchedObjects.containsKey(Integer.valueOf(id)))
/*     */     {
/*  51 */       throw new IllegalArgumentException("Duplicate id value for " + id + "!");
/*     */     }
/*     */     
/*     */ 
/*  55 */     WatchableObject datawatcher$watchableobject = new WatchableObject(integer.intValue(), id, object);
/*  56 */     this.lock.writeLock().lock();
/*  57 */     this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
/*  58 */     this.lock.writeLock().unlock();
/*  59 */     this.isBlank = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addObjectByDataType(int id, int type)
/*     */   {
/*  68 */     WatchableObject datawatcher$watchableobject = new WatchableObject(type, id, null);
/*  69 */     this.lock.writeLock().lock();
/*  70 */     this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
/*  71 */     this.lock.writeLock().unlock();
/*  72 */     this.isBlank = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getWatchableObjectByte(int id)
/*     */   {
/*  80 */     return ((Byte)getWatchedObject(id).getObject()).byteValue();
/*     */   }
/*     */   
/*     */   public short getWatchableObjectShort(int id)
/*     */   {
/*  85 */     return ((Short)getWatchedObject(id).getObject()).shortValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getWatchableObjectInt(int id)
/*     */   {
/*  93 */     return ((Integer)getWatchedObject(id).getObject()).intValue();
/*     */   }
/*     */   
/*     */   public float getWatchableObjectFloat(int id)
/*     */   {
/*  98 */     return ((Float)getWatchedObject(id).getObject()).floatValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWatchableObjectString(int id)
/*     */   {
/* 106 */     return (String)getWatchedObject(id).getObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getWatchableObjectItemStack(int id)
/*     */   {
/* 114 */     return (ItemStack)getWatchedObject(id).getObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private WatchableObject getWatchedObject(int id)
/*     */   {
/* 122 */     this.lock.readLock().lock();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 127 */       datawatcher$watchableobject = (WatchableObject)this.watchedObjects.get(Integer.valueOf(id));
/*     */     }
/*     */     catch (Throwable throwable) {
/*     */       WatchableObject datawatcher$watchableobject;
/* 131 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
/* 132 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
/* 133 */       crashreportcategory.addCrashSection("Data ID", Integer.valueOf(id));
/* 134 */       throw new ReportedException(crashreport);
/*     */     }
/*     */     WatchableObject datawatcher$watchableobject;
/* 137 */     this.lock.readLock().unlock();
/* 138 */     return datawatcher$watchableobject;
/*     */   }
/*     */   
/*     */   public Rotations getWatchableObjectRotations(int id)
/*     */   {
/* 143 */     return (Rotations)getWatchedObject(id).getObject();
/*     */   }
/*     */   
/*     */   public <T> void updateObject(int id, T newData)
/*     */   {
/* 148 */     WatchableObject datawatcher$watchableobject = getWatchedObject(id);
/*     */     
/* 150 */     if (ObjectUtils.notEqual(newData, datawatcher$watchableobject.getObject()))
/*     */     {
/* 152 */       datawatcher$watchableobject.setObject(newData);
/* 153 */       this.owner.onDataWatcherUpdate(id);
/* 154 */       datawatcher$watchableobject.setWatched(true);
/* 155 */       this.objectChanged = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setObjectWatched(int id)
/*     */   {
/* 161 */     getWatchedObject(id).watched = true;
/* 162 */     this.objectChanged = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasObjectChanged()
/*     */   {
/* 170 */     return this.objectChanged;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeWatchedListToPacketBuffer(List<WatchableObject> objectsList, PacketBuffer buffer)
/*     */     throws IOException
/*     */   {
/* 179 */     if (objectsList != null)
/*     */     {
/* 181 */       for (WatchableObject datawatcher$watchableobject : objectsList)
/*     */       {
/* 183 */         writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
/*     */       }
/*     */     }
/*     */     
/* 187 */     buffer.writeByte(127);
/*     */   }
/*     */   
/*     */   public List<WatchableObject> getChanged()
/*     */   {
/* 192 */     List<WatchableObject> list = null;
/*     */     
/* 194 */     if (this.objectChanged)
/*     */     {
/* 196 */       this.lock.readLock().lock();
/*     */       
/* 198 */       for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values())
/*     */       {
/* 200 */         if (datawatcher$watchableobject.isWatched())
/*     */         {
/* 202 */           datawatcher$watchableobject.setWatched(false);
/*     */           
/* 204 */           if (list == null)
/*     */           {
/* 206 */             list = Lists.newArrayList();
/*     */           }
/*     */           
/* 209 */           list.add(datawatcher$watchableobject);
/*     */         }
/*     */       }
/*     */       
/* 213 */       this.lock.readLock().unlock();
/*     */     }
/*     */     
/* 216 */     this.objectChanged = false;
/* 217 */     return list;
/*     */   }
/*     */   
/*     */   public void writeTo(PacketBuffer buffer) throws IOException
/*     */   {
/* 222 */     this.lock.readLock().lock();
/*     */     
/* 224 */     for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values())
/*     */     {
/* 226 */       writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
/*     */     }
/*     */     
/* 229 */     this.lock.readLock().unlock();
/* 230 */     buffer.writeByte(127);
/*     */   }
/*     */   
/*     */   public List<WatchableObject> getAllWatched()
/*     */   {
/* 235 */     List<WatchableObject> list = null;
/* 236 */     this.lock.readLock().lock();
/*     */     
/* 238 */     for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values())
/*     */     {
/* 240 */       if (list == null)
/*     */       {
/* 242 */         list = Lists.newArrayList();
/*     */       }
/*     */       
/* 245 */       list.add(datawatcher$watchableobject);
/*     */     }
/*     */     
/* 248 */     this.lock.readLock().unlock();
/* 249 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, WatchableObject object)
/*     */     throws IOException
/*     */   {
/* 258 */     int i = (object.getObjectType() << 5 | object.getDataValueId() & 0x1F) & 0xFF;
/* 259 */     buffer.writeByte(i);
/*     */     
/* 261 */     switch (object.getObjectType())
/*     */     {
/*     */     case 0: 
/* 264 */       buffer.writeByte(((Byte)object.getObject()).byteValue());
/* 265 */       break;
/*     */     
/*     */     case 1: 
/* 268 */       buffer.writeShort(((Short)object.getObject()).shortValue());
/* 269 */       break;
/*     */     
/*     */     case 2: 
/* 272 */       buffer.writeInt(((Integer)object.getObject()).intValue());
/* 273 */       break;
/*     */     
/*     */     case 3: 
/* 276 */       buffer.writeFloat(((Float)object.getObject()).floatValue());
/* 277 */       break;
/*     */     
/*     */     case 4: 
/* 280 */       buffer.writeString((String)object.getObject());
/* 281 */       break;
/*     */     
/*     */     case 5: 
/* 284 */       ItemStack itemstack = (ItemStack)object.getObject();
/* 285 */       buffer.writeItemStackToBuffer(itemstack);
/* 286 */       break;
/*     */     
/*     */     case 6: 
/* 289 */       BlockPos blockpos = (BlockPos)object.getObject();
/* 290 */       buffer.writeInt(blockpos.getX());
/* 291 */       buffer.writeInt(blockpos.getY());
/* 292 */       buffer.writeInt(blockpos.getZ());
/* 293 */       break;
/*     */     
/*     */     case 7: 
/* 296 */       Rotations rotations = (Rotations)object.getObject();
/* 297 */       buffer.writeFloat(rotations.getX());
/* 298 */       buffer.writeFloat(rotations.getY());
/* 299 */       buffer.writeFloat(rotations.getZ());
/*     */     }
/*     */   }
/*     */   
/*     */   public static List<WatchableObject> readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException
/*     */   {
/* 305 */     List<WatchableObject> list = null;
/*     */     
/* 307 */     for (int i = buffer.readByte(); i != 127; i = buffer.readByte())
/*     */     {
/* 309 */       if (list == null)
/*     */       {
/* 311 */         list = Lists.newArrayList();
/*     */       }
/*     */       
/* 314 */       int j = (i & 0xE0) >> 5;
/* 315 */       int k = i & 0x1F;
/* 316 */       WatchableObject datawatcher$watchableobject = null;
/*     */       
/* 318 */       switch (j)
/*     */       {
/*     */       case 0: 
/* 321 */         datawatcher$watchableobject = new WatchableObject(j, k, Byte.valueOf(buffer.readByte()));
/* 322 */         break;
/*     */       
/*     */       case 1: 
/* 325 */         datawatcher$watchableobject = new WatchableObject(j, k, Short.valueOf(buffer.readShort()));
/* 326 */         break;
/*     */       
/*     */       case 2: 
/* 329 */         datawatcher$watchableobject = new WatchableObject(j, k, Integer.valueOf(buffer.readInt()));
/* 330 */         break;
/*     */       
/*     */       case 3: 
/* 333 */         datawatcher$watchableobject = new WatchableObject(j, k, Float.valueOf(buffer.readFloat()));
/* 334 */         break;
/*     */       
/*     */       case 4: 
/* 337 */         datawatcher$watchableobject = new WatchableObject(j, k, buffer.readStringFromBuffer(32767));
/* 338 */         break;
/*     */       
/*     */       case 5: 
/* 341 */         datawatcher$watchableobject = new WatchableObject(j, k, buffer.readItemStackFromBuffer());
/* 342 */         break;
/*     */       
/*     */       case 6: 
/* 345 */         int l = buffer.readInt();
/* 346 */         int i1 = buffer.readInt();
/* 347 */         int j1 = buffer.readInt();
/* 348 */         datawatcher$watchableobject = new WatchableObject(j, k, new BlockPos(l, i1, j1));
/* 349 */         break;
/*     */       
/*     */       case 7: 
/* 352 */         float f = buffer.readFloat();
/* 353 */         float f1 = buffer.readFloat();
/* 354 */         float f2 = buffer.readFloat();
/* 355 */         datawatcher$watchableobject = new WatchableObject(j, k, new Rotations(f, f1, f2));
/*     */       }
/*     */       
/* 358 */       list.add(datawatcher$watchableobject);
/*     */     }
/*     */     
/* 361 */     return list;
/*     */   }
/*     */   
/*     */   public void updateWatchedObjectsFromList(List<WatchableObject> p_75687_1_)
/*     */   {
/* 366 */     this.lock.writeLock().lock();
/*     */     
/* 368 */     for (WatchableObject datawatcher$watchableobject : p_75687_1_)
/*     */     {
/* 370 */       WatchableObject datawatcher$watchableobject1 = (WatchableObject)this.watchedObjects.get(Integer.valueOf(datawatcher$watchableobject.getDataValueId()));
/*     */       
/* 372 */       if (datawatcher$watchableobject1 != null)
/*     */       {
/* 374 */         datawatcher$watchableobject1.setObject(datawatcher$watchableobject.getObject());
/* 375 */         this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
/*     */       }
/*     */     }
/*     */     
/* 379 */     this.lock.writeLock().unlock();
/* 380 */     this.objectChanged = true;
/*     */   }
/*     */   
/*     */   public boolean getIsBlank()
/*     */   {
/* 385 */     return this.isBlank;
/*     */   }
/*     */   
/*     */   public void func_111144_e()
/*     */   {
/* 390 */     this.objectChanged = false;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 395 */     dataTypes.put(Byte.class, Integer.valueOf(0));
/* 396 */     dataTypes.put(Short.class, Integer.valueOf(1));
/* 397 */     dataTypes.put(Integer.class, Integer.valueOf(2));
/* 398 */     dataTypes.put(Float.class, Integer.valueOf(3));
/* 399 */     dataTypes.put(String.class, Integer.valueOf(4));
/* 400 */     dataTypes.put(ItemStack.class, Integer.valueOf(5));
/* 401 */     dataTypes.put(BlockPos.class, Integer.valueOf(6));
/* 402 */     dataTypes.put(Rotations.class, Integer.valueOf(7));
/*     */   }
/*     */   
/*     */   public static class WatchableObject
/*     */   {
/*     */     private final int objectType;
/*     */     private final int dataValueId;
/*     */     private Object watchedObject;
/*     */     private boolean watched;
/*     */     
/*     */     public WatchableObject(int type, int id, Object object)
/*     */     {
/* 414 */       this.dataValueId = id;
/* 415 */       this.watchedObject = object;
/* 416 */       this.objectType = type;
/* 417 */       this.watched = true;
/*     */     }
/*     */     
/*     */     public int getDataValueId()
/*     */     {
/* 422 */       return this.dataValueId;
/*     */     }
/*     */     
/*     */     public void setObject(Object object)
/*     */     {
/* 427 */       this.watchedObject = object;
/*     */     }
/*     */     
/*     */     public Object getObject()
/*     */     {
/* 432 */       return this.watchedObject;
/*     */     }
/*     */     
/*     */     public int getObjectType()
/*     */     {
/* 437 */       return this.objectType;
/*     */     }
/*     */     
/*     */     public boolean isWatched()
/*     */     {
/* 442 */       return this.watched;
/*     */     }
/*     */     
/*     */     public void setWatched(boolean watched)
/*     */     {
/* 447 */       this.watched = watched;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\DataWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */