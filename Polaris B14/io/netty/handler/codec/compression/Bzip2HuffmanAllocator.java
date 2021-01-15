/*     */ package io.netty.handler.codec.compression;
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
/*     */ final class Bzip2HuffmanAllocator
/*     */ {
/*     */   private static int first(int[] array, int i, int nodesToMove)
/*     */   {
/*  34 */     int length = array.length;
/*  35 */     int limit = i;
/*  36 */     int k = array.length - 2;
/*     */     
/*  38 */     while ((i >= nodesToMove) && (array[i] % length > limit)) {
/*  39 */       k = i;
/*  40 */       i -= limit - i + 1;
/*     */     }
/*  42 */     i = Math.max(nodesToMove - 1, i);
/*     */     
/*  44 */     while (k > i + 1) {
/*  45 */       int temp = i + k >>> 1;
/*  46 */       if (array[temp] % length > limit) {
/*  47 */         k = temp;
/*     */       } else {
/*  49 */         i = temp;
/*     */       }
/*     */     }
/*  52 */     return k;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void setExtendedParentPointers(int[] array)
/*     */   {
/*  60 */     int length = array.length;
/*  61 */     array[0] += array[1];
/*     */     
/*  63 */     int headNode = 0;int tailNode = 1; for (int topNode = 2; tailNode < length - 1; tailNode++) {
/*     */       int temp;
/*  65 */       if ((topNode >= length) || (array[headNode] < array[topNode])) {
/*  66 */         int temp = array[headNode];
/*  67 */         array[(headNode++)] = tailNode;
/*     */       } else {
/*  69 */         temp = array[(topNode++)];
/*     */       }
/*     */       
/*  72 */       if ((topNode >= length) || ((headNode < tailNode) && (array[headNode] < array[topNode]))) {
/*  73 */         temp += array[headNode];
/*  74 */         array[(headNode++)] = (tailNode + length);
/*     */       } else {
/*  76 */         temp += array[(topNode++)];
/*     */       }
/*  78 */       array[tailNode] = temp;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findNodesToRelocate(int[] array, int maximumLength)
/*     */   {
/*  89 */     int currentNode = array.length - 2;
/*  90 */     for (int currentDepth = 1; (currentDepth < maximumLength - 1) && (currentNode > 1); currentDepth++) {
/*  91 */       currentNode = first(array, currentNode - 1, 0);
/*     */     }
/*  93 */     return currentNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void allocateNodeLengths(int[] array)
/*     */   {
/* 101 */     int firstNode = array.length - 2;
/* 102 */     int nextNode = array.length - 1;
/*     */     
/* 104 */     int currentDepth = 1; for (int availableNodes = 2; availableNodes > 0; currentDepth++) {
/* 105 */       int lastNode = firstNode;
/* 106 */       firstNode = first(array, lastNode - 1, 0);
/*     */       
/* 108 */       for (int i = availableNodes - (lastNode - firstNode); i > 0; i--) {
/* 109 */         array[(nextNode--)] = currentDepth;
/*     */       }
/*     */       
/* 112 */       availableNodes = lastNode - firstNode << 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void allocateNodeLengthsWithRelocation(int[] array, int nodesToMove, int insertDepth)
/*     */   {
/* 124 */     int firstNode = array.length - 2;
/* 125 */     int nextNode = array.length - 1;
/* 126 */     int currentDepth = insertDepth == 1 ? 2 : 1;
/* 127 */     int nodesLeftToMove = insertDepth == 1 ? nodesToMove - 2 : nodesToMove;
/*     */     
/* 129 */     for (int availableNodes = currentDepth << 1; availableNodes > 0; currentDepth++) {
/* 130 */       int lastNode = firstNode;
/* 131 */       firstNode = firstNode <= nodesToMove ? firstNode : first(array, lastNode - 1, nodesToMove);
/*     */       
/* 133 */       int offset = 0;
/* 134 */       if (currentDepth >= insertDepth) {
/* 135 */         offset = Math.min(nodesLeftToMove, 1 << currentDepth - insertDepth);
/* 136 */       } else if (currentDepth == insertDepth - 1) {
/* 137 */         offset = 1;
/* 138 */         if (array[firstNode] == lastNode) {
/* 139 */           firstNode++;
/*     */         }
/*     */       }
/*     */       
/* 143 */       for (int i = availableNodes - (lastNode - firstNode + offset); i > 0; i--) {
/* 144 */         array[(nextNode--)] = currentDepth;
/*     */       }
/*     */       
/* 147 */       nodesLeftToMove -= offset;
/* 148 */       availableNodes = lastNode - firstNode + offset << 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void allocateHuffmanCodeLengths(int[] array, int maximumLength)
/*     */   {
/* 159 */     switch (array.length) {
/*     */     case 2: 
/* 161 */       array[1] = 1;
/*     */     case 1: 
/* 163 */       array[0] = 1;
/* 164 */       return;
/*     */     }
/*     */     
/*     */     
/* 168 */     setExtendedParentPointers(array);
/*     */     
/*     */ 
/* 171 */     int nodesToRelocate = findNodesToRelocate(array, maximumLength);
/*     */     
/*     */ 
/* 174 */     if (array[0] % array.length >= nodesToRelocate) {
/* 175 */       allocateNodeLengths(array);
/*     */     } else {
/* 177 */       int insertDepth = maximumLength - (32 - Integer.numberOfLeadingZeros(nodesToRelocate - 1));
/* 178 */       allocateNodeLengthsWithRelocation(array, nodesToRelocate, insertDepth);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2HuffmanAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */