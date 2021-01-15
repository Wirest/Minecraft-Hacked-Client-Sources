/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.slf4j.Marker;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicMarker
/*     */   implements Marker
/*     */ {
/*     */   private static final long serialVersionUID = 1803952589649545191L;
/*     */   private final String name;
/*     */   private List<Marker> refereceList;
/*     */   
/*     */   BasicMarker(String name)
/*     */   {
/*  48 */     if (name == null) {
/*  49 */       throw new IllegalArgumentException("A marker name cannot be null");
/*     */     }
/*  51 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */   public synchronized void add(Marker reference) {
/*  59 */     if (reference == null) {
/*  60 */       throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  65 */     if (contains(reference)) {
/*  66 */       return;
/*     */     }
/*  68 */     if (reference.contains(this))
/*     */     {
/*  70 */       return;
/*     */     }
/*     */     
/*  73 */     if (this.refereceList == null) {
/*  74 */       this.refereceList = new Vector();
/*     */     }
/*  76 */     this.refereceList.add(reference);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized boolean hasReferences()
/*     */   {
/*  82 */     return (this.refereceList != null) && (this.refereceList.size() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/*  86 */     return hasReferences();
/*     */   }
/*     */   
/*     */   public synchronized Iterator<Marker> iterator() {
/*  90 */     if (this.refereceList != null) {
/*  91 */       return this.refereceList.iterator();
/*     */     }
/*  93 */     return Collections.EMPTY_LIST.iterator();
/*     */   }
/*     */   
/*     */   public synchronized boolean remove(Marker referenceToRemove)
/*     */   {
/*  98 */     if (this.refereceList == null) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     int size = this.refereceList.size();
/* 103 */     for (int i = 0; i < size; i++) {
/* 104 */       Marker m = (Marker)this.refereceList.get(i);
/* 105 */       if (referenceToRemove.equals(m)) {
/* 106 */         this.refereceList.remove(i);
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Marker other) {
/* 114 */     if (other == null) {
/* 115 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/* 118 */     if (equals(other)) {
/* 119 */       return true;
/*     */     }
/*     */     
/* 122 */     if (hasReferences()) {
/* 123 */       for (int i = 0; i < this.refereceList.size(); i++) {
/* 124 */         Marker ref = (Marker)this.refereceList.get(i);
/* 125 */         if (ref.contains(other)) {
/* 126 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(String name)
/*     */   {
/* 137 */     if (name == null) {
/* 138 */       throw new IllegalArgumentException("Other cannot be null");
/*     */     }
/*     */     
/* 141 */     if (this.name.equals(name)) {
/* 142 */       return true;
/*     */     }
/*     */     
/* 145 */     if (hasReferences()) {
/* 146 */       for (int i = 0; i < this.refereceList.size(); i++) {
/* 147 */         Marker ref = (Marker)this.refereceList.get(i);
/* 148 */         if (ref.contains(name)) {
/* 149 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/* 156 */   private static String OPEN = "[ ";
/* 157 */   private static String CLOSE = " ]";
/* 158 */   private static String SEP = ", ";
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 162 */     if (this == obj)
/* 163 */       return true;
/* 164 */     if (obj == null)
/* 165 */       return false;
/* 166 */     if (!(obj instanceof Marker)) {
/* 167 */       return false;
/*     */     }
/* 169 */     Marker other = (Marker)obj;
/* 170 */     return this.name.equals(other.getName());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 174 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 178 */     if (!hasReferences()) {
/* 179 */       return getName();
/*     */     }
/* 181 */     Iterator<Marker> it = iterator();
/*     */     
/* 183 */     StringBuffer sb = new StringBuffer(getName());
/* 184 */     sb.append(' ').append(OPEN);
/* 185 */     while (it.hasNext()) {
/* 186 */       Marker reference = (Marker)it.next();
/* 187 */       sb.append(reference.getName());
/* 188 */       if (it.hasNext()) {
/* 189 */         sb.append(SEP);
/*     */       }
/*     */     }
/* 192 */     sb.append(CLOSE);
/*     */     
/* 194 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\BasicMarker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */