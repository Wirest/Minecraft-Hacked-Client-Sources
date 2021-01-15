/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public final class NBTUtil
/*     */ {
/*     */   public static GameProfile readGameProfileFromNBT(NBTTagCompound compound)
/*     */   {
/*  15 */     String s = null;
/*  16 */     String s1 = null;
/*     */     
/*  18 */     if (compound.hasKey("Name", 8))
/*     */     {
/*  20 */       s = compound.getString("Name");
/*     */     }
/*     */     
/*  23 */     if (compound.hasKey("Id", 8))
/*     */     {
/*  25 */       s1 = compound.getString("Id");
/*     */     }
/*     */     
/*  28 */     if ((StringUtils.isNullOrEmpty(s)) && (StringUtils.isNullOrEmpty(s1)))
/*     */     {
/*  30 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     UUID uuid;
/*     */     
/*     */     try
/*     */     {
/*  38 */       uuid = UUID.fromString(s1);
/*     */     }
/*     */     catch (Throwable var12) {
/*     */       UUID uuid;
/*  42 */       uuid = null;
/*     */     }
/*     */     
/*  45 */     GameProfile gameprofile = new GameProfile(uuid, s);
/*     */     
/*  47 */     if (compound.hasKey("Properties", 10))
/*     */     {
/*  49 */       NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
/*     */       NBTTagList nbttaglist;
/*  51 */       int i; for (Iterator localIterator = nbttagcompound.getKeySet().iterator(); localIterator.hasNext(); 
/*     */           
/*     */ 
/*     */ 
/*  55 */           i < nbttaglist.tagCount())
/*     */       {
/*  51 */         String s2 = (String)localIterator.next();
/*     */         
/*  53 */         nbttaglist = nbttagcompound.getTagList(s2, 10);
/*     */         
/*  55 */         i = 0; continue;
/*     */         
/*  57 */         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/*  58 */         String s3 = nbttagcompound1.getString("Value");
/*     */         
/*  60 */         if (nbttagcompound1.hasKey("Signature", 8))
/*     */         {
/*  62 */           gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound1.getString("Signature")));
/*     */         }
/*     */         else
/*     */         {
/*  66 */           gameprofile.getProperties().put(s2, new Property(s2, s3));
/*     */         }
/*  55 */         i++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */     return gameprofile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile)
/*     */   {
/*  81 */     if (!StringUtils.isNullOrEmpty(profile.getName()))
/*     */     {
/*  83 */       tagCompound.setString("Name", profile.getName());
/*     */     }
/*     */     
/*  86 */     if (profile.getId() != null)
/*     */     {
/*  88 */       tagCompound.setString("Id", profile.getId().toString());
/*     */     }
/*     */     
/*  91 */     if (!profile.getProperties().isEmpty())
/*     */     {
/*  93 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */       
/*  95 */       for (String s : profile.getProperties().keySet())
/*     */       {
/*  97 */         NBTTagList nbttaglist = new NBTTagList();
/*     */         
/*  99 */         for (Property property : profile.getProperties().get(s))
/*     */         {
/* 101 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 102 */           nbttagcompound1.setString("Value", property.getValue());
/*     */           
/* 104 */           if (property.hasSignature())
/*     */           {
/* 106 */             nbttagcompound1.setString("Signature", property.getSignature());
/*     */           }
/*     */           
/* 109 */           nbttaglist.appendTag(nbttagcompound1);
/*     */         }
/*     */         
/* 112 */         nbttagcompound.setTag(s, nbttaglist);
/*     */       }
/*     */       
/* 115 */       tagCompound.setTag("Properties", nbttagcompound);
/*     */     }
/*     */     
/* 118 */     return tagCompound;
/*     */   }
/*     */   
/*     */   public static boolean func_181123_a(NBTBase p_181123_0_, NBTBase p_181123_1_, boolean p_181123_2_)
/*     */   {
/* 123 */     if (p_181123_0_ == p_181123_1_)
/*     */     {
/* 125 */       return true;
/*     */     }
/* 127 */     if (p_181123_0_ == null)
/*     */     {
/* 129 */       return true;
/*     */     }
/* 131 */     if (p_181123_1_ == null)
/*     */     {
/* 133 */       return false;
/*     */     }
/* 135 */     if (!p_181123_0_.getClass().equals(p_181123_1_.getClass()))
/*     */     {
/* 137 */       return false;
/*     */     }
/* 139 */     if ((p_181123_0_ instanceof NBTTagCompound))
/*     */     {
/* 141 */       NBTTagCompound nbttagcompound = (NBTTagCompound)p_181123_0_;
/* 142 */       NBTTagCompound nbttagcompound1 = (NBTTagCompound)p_181123_1_;
/*     */       
/* 144 */       for (String s : nbttagcompound.getKeySet())
/*     */       {
/* 146 */         NBTBase nbtbase1 = nbttagcompound.getTag(s);
/*     */         
/* 148 */         if (!func_181123_a(nbtbase1, nbttagcompound1.getTag(s), p_181123_2_))
/*     */         {
/* 150 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 154 */       return true;
/*     */     }
/* 156 */     if (((p_181123_0_ instanceof NBTTagList)) && (p_181123_2_))
/*     */     {
/* 158 */       NBTTagList nbttaglist = (NBTTagList)p_181123_0_;
/* 159 */       NBTTagList nbttaglist1 = (NBTTagList)p_181123_1_;
/*     */       
/* 161 */       if (nbttaglist.tagCount() == 0)
/*     */       {
/* 163 */         return nbttaglist1.tagCount() == 0;
/*     */       }
/*     */       
/*     */ 
/* 167 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 169 */         NBTBase nbtbase = nbttaglist.get(i);
/* 170 */         boolean flag = false;
/*     */         
/* 172 */         for (int j = 0; j < nbttaglist1.tagCount(); j++)
/*     */         {
/* 174 */           if (func_181123_a(nbtbase, nbttaglist1.get(j), p_181123_2_))
/*     */           {
/* 176 */             flag = true;
/* 177 */             break;
/*     */           }
/*     */         }
/*     */         
/* 181 */         if (!flag)
/*     */         {
/* 183 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 187 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 192 */     return p_181123_0_.equals(p_181123_1_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\NBTUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */