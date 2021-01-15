/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagShort;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class MapStorage
/*     */ {
/*     */   private ISaveHandler saveHandler;
/*  21 */   protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
/*  22 */   private List<WorldSavedData> loadedDataList = com.google.common.collect.Lists.newArrayList();
/*  23 */   private Map<String, Short> idCounts = Maps.newHashMap();
/*     */   
/*     */   public MapStorage(ISaveHandler saveHandlerIn)
/*     */   {
/*  27 */     this.saveHandler = saveHandlerIn;
/*  28 */     loadIdCounts();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String dataIdentifier)
/*     */   {
/*  37 */     WorldSavedData worldsaveddata = (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
/*     */     
/*  39 */     if (worldsaveddata != null)
/*     */     {
/*  41 */       return worldsaveddata;
/*     */     }
/*     */     
/*     */ 
/*  45 */     if (this.saveHandler != null)
/*     */     {
/*     */       try
/*     */       {
/*  49 */         File file1 = this.saveHandler.getMapFileFromName(dataIdentifier);
/*     */         
/*  51 */         if ((file1 != null) && (file1.exists()))
/*     */         {
/*     */           try
/*     */           {
/*  55 */             worldsaveddata = (WorldSavedData)clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { dataIdentifier });
/*     */           }
/*     */           catch (Exception exception)
/*     */           {
/*  59 */             throw new RuntimeException("Failed to instantiate " + clazz.toString(), exception);
/*     */           }
/*     */           
/*  62 */           FileInputStream fileinputstream = new FileInputStream(file1);
/*  63 */           NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
/*  64 */           fileinputstream.close();
/*  65 */           worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
/*     */         }
/*     */       }
/*     */       catch (Exception exception1)
/*     */       {
/*  70 */         exception1.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*  74 */     if (worldsaveddata != null)
/*     */     {
/*  76 */       this.loadedDataMap.put(dataIdentifier, worldsaveddata);
/*  77 */       this.loadedDataList.add(worldsaveddata);
/*     */     }
/*     */     
/*  80 */     return worldsaveddata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setData(String dataIdentifier, WorldSavedData data)
/*     */   {
/*  89 */     if (this.loadedDataMap.containsKey(dataIdentifier))
/*     */     {
/*  91 */       this.loadedDataList.remove(this.loadedDataMap.remove(dataIdentifier));
/*     */     }
/*     */     
/*  94 */     this.loadedDataMap.put(dataIdentifier, data);
/*  95 */     this.loadedDataList.add(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveAllData()
/*     */   {
/* 103 */     for (int i = 0; i < this.loadedDataList.size(); i++)
/*     */     {
/* 105 */       WorldSavedData worldsaveddata = (WorldSavedData)this.loadedDataList.get(i);
/*     */       
/* 107 */       if (worldsaveddata.isDirty())
/*     */       {
/* 109 */         saveData(worldsaveddata);
/* 110 */         worldsaveddata.setDirty(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void saveData(WorldSavedData p_75747_1_)
/*     */   {
/* 120 */     if (this.saveHandler != null)
/*     */     {
/*     */       try
/*     */       {
/* 124 */         File file1 = this.saveHandler.getMapFileFromName(p_75747_1_.mapName);
/*     */         
/* 126 */         if (file1 != null)
/*     */         {
/* 128 */           NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 129 */           p_75747_1_.writeToNBT(nbttagcompound);
/* 130 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 131 */           nbttagcompound1.setTag("data", nbttagcompound);
/* 132 */           FileOutputStream fileoutputstream = new FileOutputStream(file1);
/* 133 */           CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
/* 134 */           fileoutputstream.close();
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 139 */         exception.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void loadIdCounts()
/*     */   {
/*     */     try
/*     */     {
/* 151 */       this.idCounts.clear();
/*     */       
/* 153 */       if (this.saveHandler == null)
/*     */       {
/* 155 */         return;
/*     */       }
/*     */       
/* 158 */       File file1 = this.saveHandler.getMapFileFromName("idcounts");
/*     */       
/* 160 */       if ((file1 != null) && (file1.exists()))
/*     */       {
/* 162 */         DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
/* 163 */         NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
/* 164 */         datainputstream.close();
/*     */         
/* 166 */         for (String s : nbttagcompound.getKeySet())
/*     */         {
/* 168 */           NBTBase nbtbase = nbttagcompound.getTag(s);
/*     */           
/* 170 */           if ((nbtbase instanceof NBTTagShort))
/*     */           {
/* 172 */             NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
/* 173 */             short short1 = nbttagshort.getShort();
/* 174 */             this.idCounts.put(s, Short.valueOf(short1));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 181 */       exception.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getUniqueDataId(String key)
/*     */   {
/* 190 */     Short oshort = (Short)this.idCounts.get(key);
/*     */     
/* 192 */     if (oshort == null)
/*     */     {
/* 194 */       oshort = Short.valueOf((short)0);
/*     */     }
/*     */     else
/*     */     {
/* 198 */       oshort = Short.valueOf((short)(oshort.shortValue() + 1));
/*     */     }
/*     */     
/* 201 */     this.idCounts.put(key, oshort);
/*     */     
/* 203 */     if (this.saveHandler == null)
/*     */     {
/* 205 */       return oshort.shortValue();
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 211 */       File file1 = this.saveHandler.getMapFileFromName("idcounts");
/*     */       
/* 213 */       if (file1 != null)
/*     */       {
/* 215 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */         
/* 217 */         for (String s : this.idCounts.keySet())
/*     */         {
/* 219 */           short short1 = ((Short)this.idCounts.get(s)).shortValue();
/* 220 */           nbttagcompound.setShort(s, short1);
/*     */         }
/*     */         
/* 223 */         DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
/* 224 */         CompressedStreamTools.write(nbttagcompound, dataoutputstream);
/* 225 */         dataoutputstream.close();
/*     */       }
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 230 */       exception.printStackTrace();
/*     */     }
/*     */     
/* 233 */     return oshort.shortValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\MapStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */