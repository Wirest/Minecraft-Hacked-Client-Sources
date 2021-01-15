/*      */ package io.netty.handler.codec.compression;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class Bzip2DivSufSort
/*      */ {
/*      */   private static final int STACK_SIZE = 64;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int BUCKET_A_SIZE = 256;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int BUCKET_B_SIZE = 65536;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int SS_BLOCKSIZE = 1024;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int INSERTIONSORT_THRESHOLD = 8;
/*      */   
/*      */ 
/*      */ 
/*   33 */   private static final int[] LOG_2_TABLE = { -1, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int[] SA;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final byte[] T;
/*      */   
/*      */ 
/*      */ 
/*      */   private final int n;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Bzip2DivSufSort(byte[] block, int[] bwtBlock, int blockLength)
/*      */   {
/*   54 */     this.T = block;
/*   55 */     this.SA = bwtBlock;
/*   56 */     this.n = blockLength;
/*      */   }
/*      */   
/*      */   private static void swapElements(int[] array1, int idx1, int[] array2, int idx2) {
/*   60 */     int temp = array1[idx1];
/*   61 */     array1[idx1] = array2[idx2];
/*   62 */     array2[idx2] = temp;
/*      */   }
/*      */   
/*      */   private int ssCompare(int p1, int p2, int depth) {
/*   66 */     int[] SA = this.SA;
/*   67 */     byte[] T = this.T;
/*      */     
/*      */ 
/*   70 */     int U1n = SA[(p1 + 1)] + 2;
/*   71 */     int U2n = SA[(p2 + 1)] + 2;
/*      */     
/*   73 */     int U1 = depth + SA[p1];
/*   74 */     int U2 = depth + SA[p2];
/*      */     
/*   76 */     while ((U1 < U1n) && (U2 < U2n) && (T[U1] == T[U2])) {
/*   77 */       U1++;
/*   78 */       U2++;
/*      */     }
/*      */     
/*   81 */     return U2 < U2n ? -1 : U1 < U1n ? 1 : U2 < U2n ? (T[U1] & 0xFF) - (T[U2] & 0xFF) : 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private int ssCompareLast(int pa, int p1, int p2, int depth, int size)
/*      */   {
/*   87 */     int[] SA = this.SA;
/*   88 */     byte[] T = this.T;
/*      */     
/*   90 */     int U1 = depth + SA[p1];
/*   91 */     int U2 = depth + SA[p2];
/*   92 */     int U1n = size;
/*   93 */     int U2n = SA[(p2 + 1)] + 2;
/*      */     
/*   95 */     while ((U1 < U1n) && (U2 < U2n) && (T[U1] == T[U2])) {
/*   96 */       U1++;
/*   97 */       U2++;
/*      */     }
/*      */     
/*  100 */     if (U1 < U1n) {
/*  101 */       return U2 < U2n ? (T[U1] & 0xFF) - (T[U2] & 0xFF) : 1;
/*      */     }
/*  103 */     if (U2 == U2n) {
/*  104 */       return 1;
/*      */     }
/*      */     
/*  107 */     U1 %= size;
/*  108 */     U1n = SA[pa] + 2;
/*  109 */     while ((U1 < U1n) && (U2 < U2n) && (T[U1] == T[U2])) {
/*  110 */       U1++;
/*  111 */       U2++;
/*      */     }
/*      */     
/*  114 */     return U2 < U2n ? -1 : U1 < U1n ? 1 : U2 < U2n ? (T[U1] & 0xFF) - (T[U2] & 0xFF) : 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private void ssInsertionSort(int pa, int first, int last, int depth)
/*      */   {
/*  120 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */     for (int i = last - 2; first <= i; i--) {
/*  127 */       int t = SA[i]; int r; for (int j = i + 1; 0 < (r = ssCompare(pa + t, pa + SA[j], depth));) {
/*      */         do {
/*  129 */           SA[(j - 1)] = SA[j];
/*  130 */           j++; } while ((j < last) && (SA[j] < 0));
/*  131 */         if (last <= j) {
/*      */           break;
/*      */         }
/*      */       }
/*  135 */       if (r == 0) {
/*  136 */         SA[j] ^= 0xFFFFFFFF;
/*      */       }
/*  138 */       SA[(j - 1)] = t;
/*      */     }
/*      */   }
/*      */   
/*      */   private void ssFixdown(int td, int pa, int sa, int i, int size) {
/*  143 */     int[] SA = this.SA;
/*  144 */     byte[] T = this.T;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */     int v = SA[(sa + i)]; int j; int k; for (int c = T[(td + SA[(pa + v)])] & 0xFF; (j = 2 * i + 1) < size; i = k) {
/*  151 */       int d = T[(td + SA[(pa + SA[(sa + (k = j++))])])] & 0xFF;
/*  152 */       int e; if (d < (e = T[(td + SA[(pa + SA[(sa + j)])])] & 0xFF)) {
/*  153 */         k = j;
/*  154 */         d = e;
/*      */       }
/*  156 */       if (d <= c) {
/*      */         break;
/*      */       }
/*  150 */       SA[(sa + i)] = SA[(sa + k)];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */     SA[(sa + i)] = v;
/*      */   }
/*      */   
/*      */   private void ssHeapSort(int td, int pa, int sa, int size) {
/*  164 */     int[] SA = this.SA;
/*  165 */     byte[] T = this.T;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  170 */     int m = size;
/*  171 */     if (size % 2 == 0) {
/*  172 */       m--;
/*  173 */       if ((T[(td + SA[(pa + SA[(sa + m / 2)])])] & 0xFF) < (T[(td + SA[(pa + SA[(sa + m)])])] & 0xFF)) {
/*  174 */         swapElements(SA, sa + m, SA, sa + m / 2);
/*      */       }
/*      */     }
/*      */     
/*  178 */     for (int i = m / 2 - 1; 0 <= i; i--) {
/*  179 */       ssFixdown(td, pa, sa, i, m);
/*      */     }
/*      */     
/*  182 */     if (size % 2 == 0) {
/*  183 */       swapElements(SA, sa, SA, sa + m);
/*  184 */       ssFixdown(td, pa, sa, 0, m);
/*      */     }
/*      */     
/*  187 */     for (i = m - 1; 0 < i; i--) {
/*  188 */       int t = SA[sa];
/*  189 */       SA[sa] = SA[(sa + i)];
/*  190 */       ssFixdown(td, pa, sa, 0, i);
/*  191 */       SA[(sa + i)] = t;
/*      */     }
/*      */   }
/*      */   
/*      */   private int ssMedian3(int td, int pa, int v1, int v2, int v3) {
/*  196 */     int[] SA = this.SA;
/*  197 */     byte[] T = this.T;
/*      */     
/*  199 */     int T_v1 = T[(td + SA[(pa + SA[v1])])] & 0xFF;
/*  200 */     int T_v2 = T[(td + SA[(pa + SA[v2])])] & 0xFF;
/*  201 */     int T_v3 = T[(td + SA[(pa + SA[v3])])] & 0xFF;
/*      */     
/*  203 */     if (T_v1 > T_v2) {
/*  204 */       int temp = v1;
/*  205 */       v1 = v2;
/*  206 */       v2 = temp;
/*  207 */       int T_vtemp = T_v1;
/*  208 */       T_v1 = T_v2;
/*  209 */       T_v2 = T_vtemp;
/*      */     }
/*  211 */     if (T_v2 > T_v3) {
/*  212 */       if (T_v1 > T_v3) {
/*  213 */         return v1;
/*      */       }
/*  215 */       return v3;
/*      */     }
/*  217 */     return v2;
/*      */   }
/*      */   
/*      */   private int ssMedian5(int td, int pa, int v1, int v2, int v3, int v4, int v5) {
/*  221 */     int[] SA = this.SA;
/*  222 */     byte[] T = this.T;
/*      */     
/*  224 */     int T_v1 = T[(td + SA[(pa + SA[v1])])] & 0xFF;
/*  225 */     int T_v2 = T[(td + SA[(pa + SA[v2])])] & 0xFF;
/*  226 */     int T_v3 = T[(td + SA[(pa + SA[v3])])] & 0xFF;
/*  227 */     int T_v4 = T[(td + SA[(pa + SA[v4])])] & 0xFF;
/*  228 */     int T_v5 = T[(td + SA[(pa + SA[v5])])] & 0xFF;
/*      */     
/*      */ 
/*      */ 
/*  232 */     if (T_v2 > T_v3) {
/*  233 */       int temp = v2;
/*  234 */       v2 = v3;
/*  235 */       v3 = temp;
/*  236 */       int T_vtemp = T_v2;
/*  237 */       T_v2 = T_v3;
/*  238 */       T_v3 = T_vtemp;
/*      */     }
/*  240 */     if (T_v4 > T_v5) {
/*  241 */       int temp = v4;
/*  242 */       v4 = v5;
/*  243 */       v5 = temp;
/*  244 */       int T_vtemp = T_v4;
/*  245 */       T_v4 = T_v5;
/*  246 */       T_v5 = T_vtemp;
/*      */     }
/*  248 */     if (T_v2 > T_v4) {
/*  249 */       int temp = v2;
/*  250 */       v4 = temp;
/*  251 */       int T_vtemp = T_v2;
/*  252 */       T_v4 = T_vtemp;
/*  253 */       temp = v3;
/*  254 */       v3 = v5;
/*  255 */       v5 = temp;
/*  256 */       T_vtemp = T_v3;
/*  257 */       T_v3 = T_v5;
/*  258 */       T_v5 = T_vtemp;
/*      */     }
/*  260 */     if (T_v1 > T_v3) {
/*  261 */       int temp = v1;
/*  262 */       v1 = v3;
/*  263 */       v3 = temp;
/*  264 */       int T_vtemp = T_v1;
/*  265 */       T_v1 = T_v3;
/*  266 */       T_v3 = T_vtemp;
/*      */     }
/*  268 */     if (T_v1 > T_v4) {
/*  269 */       int temp = v1;
/*  270 */       v4 = temp;
/*  271 */       int T_vtemp = T_v1;
/*  272 */       T_v4 = T_vtemp;
/*  273 */       v3 = v5;
/*  274 */       T_v3 = T_v5;
/*      */     }
/*  276 */     if (T_v3 > T_v4) {
/*  277 */       return v4;
/*      */     }
/*  279 */     return v3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int ssPivot(int td, int pa, int first, int last)
/*      */   {
/*  286 */     int t = last - first;
/*  287 */     int middle = first + t / 2;
/*      */     
/*  289 */     if (t <= 512) {
/*  290 */       if (t <= 32) {
/*  291 */         return ssMedian3(td, pa, first, middle, last - 1);
/*      */       }
/*  293 */       t >>= 2;
/*  294 */       return ssMedian5(td, pa, first, first + t, middle, last - 1 - t, last - 1);
/*      */     }
/*  296 */     t >>= 3;
/*  297 */     return ssMedian3(td, pa, ssMedian3(td, pa, first, first + t, first + (t << 1)), ssMedian3(td, pa, middle - t, middle, middle + t), ssMedian3(td, pa, last - 1 - (t << 1), last - 1 - t, last - 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int ssLog(int n)
/*      */   {
/*  306 */     return (n & 0xFF00) != 0 ? 8 + LOG_2_TABLE[(n >> 8 & 0xFF)] : LOG_2_TABLE[(n & 0xFF)];
/*      */   }
/*      */   
/*      */ 
/*      */   private int ssSubstringPartition(int pa, int first, int last, int depth)
/*      */   {
/*  312 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  317 */     int a = first - 1;int b = last;
/*  318 */     for (;;) { a++; if ((a < b) && (SA[(pa + SA[a])] + depth >= SA[(pa + SA[a] + 1)] + 1)) {
/*  319 */         SA[a] ^= 0xFFFFFFFF;
/*      */       } else {
/*  321 */         b--;
/*  322 */         while ((a < b) && (SA[(pa + SA[b])] + depth < SA[(pa + SA[b] + 1)] + 1)) {
/*  323 */           b--;
/*      */         }
/*      */         
/*  326 */         if (b <= a) {
/*      */           break;
/*      */         }
/*  329 */         int t = SA[b] ^ 0xFFFFFFFF;
/*  330 */         SA[b] = SA[a];
/*  331 */         SA[a] = t;
/*      */       } }
/*  333 */     if (first < a) {
/*  334 */       SA[first] ^= 0xFFFFFFFF;
/*      */     }
/*  336 */     return a;
/*      */   }
/*      */   
/*      */   private static class StackEntry {
/*      */     final int a;
/*      */     final int b;
/*      */     final int c;
/*      */     final int d;
/*      */     
/*      */     StackEntry(int a, int b, int c, int d) {
/*  346 */       this.a = a;
/*  347 */       this.b = b;
/*  348 */       this.c = c;
/*  349 */       this.d = d;
/*      */     }
/*      */   }
/*      */   
/*      */   private void ssMultiKeyIntroSort(int pa, int first, int last, int depth) {
/*  354 */     int[] SA = this.SA;
/*  355 */     byte[] T = this.T;
/*      */     
/*  357 */     StackEntry[] stack = new StackEntry[64];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  364 */     int x = 0;
/*      */     
/*  366 */     int ssize = 0;int limit = ssLog(last - first);
/*  367 */     for (;;) { if (last - first <= 8) {
/*  368 */         if (1 < last - first) {
/*  369 */           ssInsertionSort(pa, first, last, depth);
/*      */         }
/*  371 */         if (ssize == 0) {
/*  372 */           return;
/*      */         }
/*  374 */         StackEntry entry = stack[(--ssize)];
/*  375 */         first = entry.a;
/*  376 */         last = entry.b;
/*  377 */         depth = entry.c;
/*  378 */         limit = entry.d;
/*      */       }
/*      */       else
/*      */       {
/*  382 */         int Td = depth;
/*  383 */         if (limit-- == 0) {
/*  384 */           ssHeapSort(Td, pa, first, last - first);
/*      */         }
/*  386 */         if (limit < 0) {
/*  387 */           int a = first + 1; for (int v = T[(Td + SA[(pa + SA[first])])] & 0xFF; a < last; a++) {
/*  388 */             if ((x = T[(Td + SA[(pa + SA[a])])] & 0xFF) != v) {
/*  389 */               if (1 < a - first) {
/*      */                 break;
/*      */               }
/*  392 */               v = x;
/*  393 */               first = a;
/*      */             }
/*      */           }
/*  396 */           if ((T[(Td + SA[(pa + SA[first])] - 1)] & 0xFF) < v) {
/*  397 */             first = ssSubstringPartition(pa, first, a, depth);
/*      */           }
/*  399 */           if (a - first <= last - a) {
/*  400 */             if (1 < a - first) {
/*  401 */               stack[(ssize++)] = new StackEntry(a, last, depth, -1);
/*  402 */               last = a;
/*  403 */               depth++;
/*  404 */               limit = ssLog(a - first);
/*      */             } else {
/*  406 */               first = a;
/*  407 */               limit = -1;
/*      */             }
/*      */           }
/*  410 */           else if (1 < last - a) {
/*  411 */             stack[(ssize++)] = new StackEntry(first, a, depth + 1, ssLog(a - first));
/*  412 */             first = a;
/*  413 */             limit = -1;
/*      */           } else {
/*  415 */             last = a;
/*  416 */             depth++;
/*  417 */             limit = ssLog(a - first);
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/*  423 */           int a = ssPivot(Td, pa, first, last);
/*  424 */           int v = T[(Td + SA[(pa + SA[a])])] & 0xFF;
/*  425 */           swapElements(SA, first, SA, a);
/*      */           
/*  427 */           int b = first + 1;
/*  428 */           while ((b < last) && ((x = T[(Td + SA[(pa + SA[b])])] & 0xFF) == v)) {
/*  429 */             b++;
/*      */           }
/*  431 */           if (((a = b) < last) && (x < v)) {
/*  432 */             for (;;) { b++; if ((b >= last) || ((x = T[(Td + SA[(pa + SA[b])])] & 0xFF) > v)) break;
/*  433 */               if (x == v) {
/*  434 */                 swapElements(SA, b, SA, a);
/*  435 */                 a++;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  440 */           int c = last - 1;
/*  441 */           while ((b < c) && ((x = T[(Td + SA[(pa + SA[c])])] & 0xFF) == v))
/*  442 */             c--;
/*      */           int d;
/*  444 */           if ((b < (d = c)) && (x > v)) {
/*  445 */             while ((b < --c) && ((x = T[(Td + SA[(pa + SA[c])])] & 0xFF) >= v)) {
/*  446 */               if (x == v) {
/*  447 */                 swapElements(SA, c, SA, d);
/*  448 */                 d--;
/*      */               }
/*      */             }
/*      */           }
/*  452 */           for (; b < c; 
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  463 */               goto 752)
/*      */           {
/*  453 */             swapElements(SA, b, SA, c);
/*  454 */             for (;;) { b++; if ((b >= c) || ((x = T[(Td + SA[(pa + SA[b])])] & 0xFF) > v)) break;
/*  455 */               if (x == v) {
/*  456 */                 swapElements(SA, b, SA, a);
/*  457 */                 a++;
/*      */               }
/*      */             }
/*  460 */             while ((b < --c) && ((x = T[(Td + SA[(pa + SA[c])])] & 0xFF) >= v)) {
/*  461 */               if (x == v) {
/*  462 */                 swapElements(SA, c, SA, d);
/*  463 */                 d--;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  468 */           if (a <= d) {
/*  469 */             c = b - 1;
/*      */             int s;
/*  471 */             int t; if ((s = a - first) > (t = b - a)) {
/*  472 */               s = t;
/*      */             }
/*  474 */             int e = first; for (int f = b - s; 0 < s; f++) {
/*  475 */               swapElements(SA, e, SA, f);s--;e++;
/*      */             }
/*  477 */             if ((s = d - c) > (t = last - d - 1)) {
/*  478 */               s = t;
/*      */             }
/*  480 */             e = b; for (f = last - s; 0 < s; f++) {
/*  481 */               swapElements(SA, e, SA, f);s--;e++;
/*      */             }
/*      */             
/*  484 */             a = first + (b - a);
/*  485 */             c = last - (d - c);
/*  486 */             b = v <= (T[(Td + SA[(pa + SA[a])] - 1)] & 0xFF) ? a : ssSubstringPartition(pa, a, c, depth);
/*      */             
/*  488 */             if (a - first <= last - c) {
/*  489 */               if (last - c <= c - b) {
/*  490 */                 stack[(ssize++)] = new StackEntry(b, c, depth + 1, ssLog(c - b));
/*  491 */                 stack[(ssize++)] = new StackEntry(c, last, depth, limit);
/*  492 */                 last = a;
/*  493 */               } else if (a - first <= c - b) {
/*  494 */                 stack[(ssize++)] = new StackEntry(c, last, depth, limit);
/*  495 */                 stack[(ssize++)] = new StackEntry(b, c, depth + 1, ssLog(c - b));
/*  496 */                 last = a;
/*      */               } else {
/*  498 */                 stack[(ssize++)] = new StackEntry(c, last, depth, limit);
/*  499 */                 stack[(ssize++)] = new StackEntry(first, a, depth, limit);
/*  500 */                 first = b;
/*  501 */                 last = c;
/*  502 */                 depth++;
/*  503 */                 limit = ssLog(c - b);
/*      */               }
/*      */             }
/*  506 */             else if (a - first <= c - b) {
/*  507 */               stack[(ssize++)] = new StackEntry(b, c, depth + 1, ssLog(c - b));
/*  508 */               stack[(ssize++)] = new StackEntry(first, a, depth, limit);
/*  509 */               first = c;
/*  510 */             } else if (last - c <= c - b) {
/*  511 */               stack[(ssize++)] = new StackEntry(first, a, depth, limit);
/*  512 */               stack[(ssize++)] = new StackEntry(b, c, depth + 1, ssLog(c - b));
/*  513 */               first = c;
/*      */             } else {
/*  515 */               stack[(ssize++)] = new StackEntry(first, a, depth, limit);
/*  516 */               stack[(ssize++)] = new StackEntry(c, last, depth, limit);
/*  517 */               first = b;
/*  518 */               last = c;
/*  519 */               depth++;
/*  520 */               limit = ssLog(c - b);
/*      */             }
/*      */           }
/*      */           else {
/*  524 */             limit++;
/*  525 */             if ((T[(Td + SA[(pa + SA[first])] - 1)] & 0xFF) < v) {
/*  526 */               first = ssSubstringPartition(pa, first, last, depth);
/*  527 */               limit = ssLog(last - first);
/*      */             }
/*  529 */             depth++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void ssBlockSwap(int[] array1, int first1, int[] array2, int first2, int size)
/*      */   {
/*  538 */     int i = size;int a = first1; for (int b = first2; 0 < i; b++) {
/*  539 */       swapElements(array1, a, array2, b);i--;a++;
/*      */     }
/*      */   }
/*      */   
/*      */   private void ssMergeForward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth)
/*      */   {
/*  545 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  552 */     int bufend = bufoffset + (middle - first) - 1;
/*  553 */     ssBlockSwap(buf, bufoffset, SA, first, middle - first);
/*      */     
/*  555 */     int t = SA[first];int i = first;int j = bufoffset;int k = middle;
/*  556 */     for (;;) { int r = ssCompare(pa + buf[j], pa + SA[k], depth);
/*  557 */       if (r < 0) {
/*      */         do {
/*  559 */           SA[(i++)] = buf[j];
/*  560 */           if (bufend <= j) {
/*  561 */             buf[j] = t;
/*  562 */             return;
/*      */           }
/*  564 */           buf[(j++)] = SA[i];
/*  565 */         } while (buf[j] < 0);
/*  566 */       } else if (r > 0) {
/*      */         do {
/*  568 */           SA[(i++)] = SA[k];
/*  569 */           SA[(k++)] = SA[i];
/*  570 */           if (last <= k) {
/*  571 */             for (; j < bufend; buf[(j++)] = SA[i]) SA[(i++)] = buf[j];
/*  572 */             SA[i] = buf[j];buf[j] = t;
/*  573 */             return;
/*      */           }
/*  575 */         } while (SA[k] < 0);
/*      */       } else {
/*  577 */         SA[k] ^= 0xFFFFFFFF;
/*      */         do {
/*  579 */           SA[(i++)] = buf[j];
/*  580 */           if (bufend <= j) {
/*  581 */             buf[j] = t;
/*  582 */             return;
/*      */           }
/*  584 */           buf[(j++)] = SA[i];
/*  585 */         } while (buf[j] < 0);
/*      */         do
/*      */         {
/*  588 */           SA[(i++)] = SA[k];
/*  589 */           SA[(k++)] = SA[i];
/*  590 */           if (last <= k) {
/*  591 */             while (j < bufend) {
/*  592 */               SA[(i++)] = buf[j];
/*  593 */               buf[(j++)] = SA[i];
/*      */             }
/*  595 */             SA[i] = buf[j];buf[j] = t;
/*  596 */             return;
/*      */           }
/*  598 */         } while (SA[k] < 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void ssMergeBackward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth)
/*      */   {
/*  605 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  614 */     int bufend = bufoffset + (last - middle);
/*  615 */     ssBlockSwap(buf, bufoffset, SA, middle, last - middle);
/*      */     
/*  617 */     int x = 0;
/*  618 */     int p1; int p1; if (buf[(bufend - 1)] < 0) {
/*  619 */       x |= 0x1;
/*  620 */       p1 = pa + (buf[(bufend - 1)] ^ 0xFFFFFFFF);
/*      */     } else {
/*  622 */       p1 = pa + buf[(bufend - 1)]; }
/*      */     int p2;
/*  624 */     int p2; if (SA[(middle - 1)] < 0) {
/*  625 */       x |= 0x2;
/*  626 */       p2 = pa + (SA[(middle - 1)] ^ 0xFFFFFFFF);
/*      */     } else {
/*  628 */       p2 = pa + SA[(middle - 1)];
/*      */     }
/*  630 */     int t = SA[(last - 1)];int i = last - 1;int j = bufend - 1;int k = middle - 1;
/*      */     for (;;) {
/*  632 */       int r = ssCompare(p1, p2, depth);
/*  633 */       if (r > 0) {
/*  634 */         if ((x & 0x1) != 0) {
/*      */           do {
/*  636 */             SA[(i--)] = buf[j];
/*  637 */             buf[(j--)] = SA[i];
/*  638 */           } while (buf[j] < 0);
/*  639 */           x ^= 0x1;
/*      */         }
/*  641 */         SA[(i--)] = buf[j];
/*  642 */         if (j <= bufoffset) {
/*  643 */           buf[j] = t;
/*  644 */           return;
/*      */         }
/*  646 */         buf[(j--)] = SA[i];
/*      */         
/*  648 */         if (buf[j] < 0) {
/*  649 */           x |= 0x1;
/*  650 */           p1 = pa + (buf[j] ^ 0xFFFFFFFF);
/*      */         } else {
/*  652 */           p1 = pa + buf[j];
/*      */         }
/*  654 */       } else if (r < 0) {
/*  655 */         if ((x & 0x2) != 0) {
/*      */           do {
/*  657 */             SA[(i--)] = SA[k];
/*  658 */             SA[(k--)] = SA[i];
/*  659 */           } while (SA[k] < 0);
/*  660 */           x ^= 0x2;
/*      */         }
/*  662 */         SA[(i--)] = SA[k];
/*  663 */         SA[(k--)] = SA[i];
/*  664 */         if (k < first) {
/*  665 */           while (bufoffset < j) {
/*  666 */             SA[(i--)] = buf[j];
/*  667 */             buf[(j--)] = SA[i];
/*      */           }
/*  669 */           SA[i] = buf[j];
/*  670 */           buf[j] = t;
/*  671 */           return;
/*      */         }
/*      */         
/*  674 */         if (SA[k] < 0) {
/*  675 */           x |= 0x2;
/*  676 */           p2 = pa + (SA[k] ^ 0xFFFFFFFF);
/*      */         } else {
/*  678 */           p2 = pa + SA[k];
/*      */         }
/*      */       } else {
/*  681 */         if ((x & 0x1) != 0) {
/*      */           do {
/*  683 */             SA[(i--)] = buf[j];
/*  684 */             buf[(j--)] = SA[i];
/*  685 */           } while (buf[j] < 0);
/*  686 */           x ^= 0x1;
/*      */         }
/*  688 */         SA[(i--)] = (buf[j] ^ 0xFFFFFFFF);
/*  689 */         if (j <= bufoffset) {
/*  690 */           buf[j] = t;
/*  691 */           return;
/*      */         }
/*  693 */         buf[(j--)] = SA[i];
/*      */         
/*  695 */         if ((x & 0x2) != 0) {
/*      */           do {
/*  697 */             SA[(i--)] = SA[k];
/*  698 */             SA[(k--)] = SA[i];
/*  699 */           } while (SA[k] < 0);
/*  700 */           x ^= 0x2;
/*      */         }
/*  702 */         SA[(i--)] = SA[k];
/*  703 */         SA[(k--)] = SA[i];
/*  704 */         if (k < first) {
/*  705 */           while (bufoffset < j) {
/*  706 */             SA[(i--)] = buf[j];
/*  707 */             buf[(j--)] = SA[i];
/*      */           }
/*  709 */           SA[i] = buf[j];
/*  710 */           buf[j] = t;
/*  711 */           return;
/*      */         }
/*      */         
/*  714 */         if (buf[j] < 0) {
/*  715 */           x |= 0x1;
/*  716 */           p1 = pa + (buf[j] ^ 0xFFFFFFFF);
/*      */         } else {
/*  718 */           p1 = pa + buf[j];
/*      */         }
/*  720 */         if (SA[k] < 0) {
/*  721 */           x |= 0x2;
/*  722 */           p2 = pa + (SA[k] ^ 0xFFFFFFFF);
/*      */         } else {
/*  724 */           p2 = pa + SA[k];
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static int getIDX(int a) {
/*  731 */     return 0 <= a ? a : a ^ 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   private void ssMergeCheckEqual(int pa, int depth, int a) {
/*  735 */     int[] SA = this.SA;
/*      */     
/*  737 */     if ((0 <= SA[a]) && (ssCompare(pa + getIDX(SA[(a - 1)]), pa + SA[a], depth) == 0)) {
/*  738 */       SA[a] ^= 0xFFFFFFFF;
/*      */     }
/*      */   }
/*      */   
/*      */   private void ssMerge(int pa, int first, int middle, int last, int[] buf, int bufoffset, int bufsize, int depth)
/*      */   {
/*  744 */     int[] SA = this.SA;
/*      */     
/*  746 */     StackEntry[] stack = new StackEntry[64];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  753 */     int check = 0;int ssize = 0;
/*      */     for (;;) {
/*  755 */       if (last - middle <= bufsize) {
/*  756 */         if ((first < middle) && (middle < last)) {
/*  757 */           ssMergeBackward(pa, buf, bufoffset, first, middle, last, depth);
/*      */         }
/*      */         
/*  760 */         if ((check & 0x1) != 0) {
/*  761 */           ssMergeCheckEqual(pa, depth, first);
/*      */         }
/*  763 */         if ((check & 0x2) != 0) {
/*  764 */           ssMergeCheckEqual(pa, depth, last);
/*      */         }
/*  766 */         if (ssize == 0) {
/*  767 */           return;
/*      */         }
/*  769 */         StackEntry entry = stack[(--ssize)];
/*  770 */         first = entry.a;
/*  771 */         middle = entry.b;
/*  772 */         last = entry.c;
/*  773 */         check = entry.d;
/*      */ 
/*      */ 
/*      */       }
/*  777 */       else if (middle - first <= bufsize) {
/*  778 */         if (first < middle) {
/*  779 */           ssMergeForward(pa, buf, bufoffset, first, middle, last, depth);
/*      */         }
/*  781 */         if ((check & 0x1) != 0) {
/*  782 */           ssMergeCheckEqual(pa, depth, first);
/*      */         }
/*  784 */         if ((check & 0x2) != 0) {
/*  785 */           ssMergeCheckEqual(pa, depth, last);
/*      */         }
/*  787 */         if (ssize == 0) {
/*  788 */           return;
/*      */         }
/*  790 */         StackEntry entry = stack[(--ssize)];
/*  791 */         first = entry.a;
/*  792 */         middle = entry.b;
/*  793 */         last = entry.c;
/*  794 */         check = entry.d;
/*      */       }
/*      */       else
/*      */       {
/*  798 */         int m = 0;int len = Math.min(middle - first, last - middle);int half = len >> 1;
/*  799 */         for (; 0 < len; 
/*  800 */             half >>= 1)
/*      */         {
/*  802 */           if (ssCompare(pa + getIDX(SA[(middle + m + half)]), pa + getIDX(SA[(middle - m - half - 1)]), depth) < 0)
/*      */           {
/*  804 */             m += half + 1;
/*  805 */             half -= (len & 0x1 ^ 0x1);
/*      */           }
/*  800 */           len = half;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  809 */         if (0 < m) {
/*  810 */           ssBlockSwap(SA, middle - m, SA, middle, m);
/*  811 */           int j; int i = j = middle;
/*  812 */           int next = 0;
/*  813 */           if (middle + m < last) {
/*  814 */             if (SA[(middle + m)] < 0) {
/*  815 */               while (SA[(i - 1)] < 0) {
/*  816 */                 i--;
/*      */               }
/*  818 */               SA[(middle + m)] ^= 0xFFFFFFFF;
/*      */             }
/*  820 */             for (j = middle; SA[j] < 0;) {
/*  821 */               j++;
/*      */             }
/*  823 */             next = 1;
/*      */           }
/*  825 */           if (i - first <= last - j) {
/*  826 */             stack[(ssize++)] = new StackEntry(j, middle + m, last, check & 0x2 | next & 0x1);
/*  827 */             middle -= m;
/*  828 */             last = i;
/*  829 */             check &= 0x1;
/*      */           } else {
/*  831 */             if ((i == middle) && (middle == j)) {
/*  832 */               next <<= 1;
/*      */             }
/*  834 */             stack[(ssize++)] = new StackEntry(first, middle - m, i, check & 0x1 | next & 0x2);
/*  835 */             first = j;
/*  836 */             middle += m;
/*  837 */             check = check & 0x2 | next & 0x1;
/*      */           }
/*      */         } else {
/*  840 */           if ((check & 0x1) != 0) {
/*  841 */             ssMergeCheckEqual(pa, depth, first);
/*      */           }
/*  843 */           ssMergeCheckEqual(pa, depth, middle);
/*  844 */           if ((check & 0x2) != 0) {
/*  845 */             ssMergeCheckEqual(pa, depth, last);
/*      */           }
/*  847 */           if (ssize == 0) {
/*  848 */             return;
/*      */           }
/*  850 */           StackEntry entry = stack[(--ssize)];
/*  851 */           first = entry.a;
/*  852 */           middle = entry.b;
/*  853 */           last = entry.c;
/*  854 */           check = entry.d;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void subStringSort(int pa, int first, int last, int[] buf, int bufoffset, int bufsize, int depth, boolean lastsuffix, int size)
/*      */   {
/*  862 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  870 */     if (lastsuffix) {
/*  871 */       first++;
/*      */     }
/*  873 */     int a = first; for (int i = 0; a + 1024 < last; i++) {
/*  874 */       ssMultiKeyIntroSort(pa, a, a + 1024, depth);
/*  875 */       int[] curbuf = SA;
/*  876 */       int curbufoffset = a + 1024;
/*  877 */       int curbufsize = last - (a + 1024);
/*  878 */       if (curbufsize <= bufsize) {
/*  879 */         curbufsize = bufsize;
/*  880 */         curbuf = buf;
/*  881 */         curbufoffset = bufoffset;
/*      */       }
/*  883 */       int b = a;int k = 1024; for (int j = i; (j & 0x1) != 0; j >>>= 1) {
/*  884 */         ssMerge(pa, b - k, b, b + k, curbuf, curbufoffset, curbufsize, depth);b -= k;k <<= 1;
/*      */       }
/*  873 */       a += 1024;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  888 */     ssMultiKeyIntroSort(pa, a, last, depth);
/*      */     
/*  890 */     for (int k = 1024; i != 0; i >>= 1) {
/*  891 */       if ((i & 0x1) != 0) {
/*  892 */         ssMerge(pa, a - k, a, last, buf, bufoffset, bufsize, depth);
/*  893 */         a -= k;
/*      */       }
/*  890 */       k <<= 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  897 */     if (lastsuffix)
/*      */     {
/*  899 */       a = first;i = SA[(first - 1)];int r = 1;
/*  900 */       for (; (a < last) && ((SA[a] < 0) || (0 < (r = ssCompareLast(pa, pa + i, pa + SA[a], depth, size)))); 
/*  901 */           a++) {
/*  902 */         SA[(a - 1)] = SA[a];
/*      */       }
/*  904 */       if (r == 0) {
/*  905 */         SA[a] ^= 0xFFFFFFFF;
/*      */       }
/*  907 */       SA[(a - 1)] = i;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private int trGetC(int isa, int isaD, int isaN, int p)
/*      */   {
/*  914 */     return isaD + p < isaN ? this.SA[(isaD + p)] : this.SA[(isa + (isaD - isa + p) % (isaN - isa))];
/*      */   }
/*      */   
/*      */ 
/*      */   private void trFixdown(int isa, int isaD, int isaN, int sa, int i, int size)
/*      */   {
/*  920 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  926 */     int v = SA[(sa + i)]; int j; int k; for (int c = trGetC(isa, isaD, isaN, v); (j = 2 * i + 1) < size; i = k) {
/*  927 */       k = j++;
/*  928 */       int d = trGetC(isa, isaD, isaN, SA[(sa + k)]);
/*  929 */       int e; if (d < (e = trGetC(isa, isaD, isaN, SA[(sa + j)]))) {
/*  930 */         k = j;
/*  931 */         d = e;
/*      */       }
/*  933 */       if (d <= c) {
/*      */         break;
/*      */       }
/*  926 */       SA[(sa + i)] = SA[(sa + k)];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  937 */     SA[(sa + i)] = v;
/*      */   }
/*      */   
/*      */   private void trHeapSort(int isa, int isaD, int isaN, int sa, int size) {
/*  941 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  946 */     int m = size;
/*  947 */     if (size % 2 == 0) {
/*  948 */       m--;
/*  949 */       if (trGetC(isa, isaD, isaN, SA[(sa + m / 2)]) < trGetC(isa, isaD, isaN, SA[(sa + m)])) {
/*  950 */         swapElements(SA, sa + m, SA, sa + m / 2);
/*      */       }
/*      */     }
/*      */     
/*  954 */     for (int i = m / 2 - 1; 0 <= i; i--) {
/*  955 */       trFixdown(isa, isaD, isaN, sa, i, m);
/*      */     }
/*      */     
/*  958 */     if (size % 2 == 0) {
/*  959 */       swapElements(SA, sa, SA, sa + m);
/*  960 */       trFixdown(isa, isaD, isaN, sa, 0, m);
/*      */     }
/*      */     
/*  963 */     for (i = m - 1; 0 < i; i--) {
/*  964 */       int t = SA[sa];
/*  965 */       SA[sa] = SA[(sa + i)];
/*  966 */       trFixdown(isa, isaD, isaN, sa, 0, i);
/*  967 */       SA[(sa + i)] = t;
/*      */     }
/*      */   }
/*      */   
/*      */   private void trInsertionSort(int isa, int isaD, int isaN, int first, int last) {
/*  972 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  977 */     for (int a = first + 1; a < last; a++) {
/*  978 */       int t = SA[a]; int r; for (int b = a - 1; 0 > (r = trGetC(isa, isaD, isaN, t) - trGetC(isa, isaD, isaN, SA[b]));) {
/*      */         do {
/*  980 */           SA[(b + 1)] = SA[b];
/*  981 */         } while ((first <= --b) && (SA[b] < 0));
/*  982 */         if (b < first) {
/*      */           break;
/*      */         }
/*      */       }
/*  986 */       if (r == 0) {
/*  987 */         SA[b] ^= 0xFFFFFFFF;
/*      */       }
/*  989 */       SA[(b + 1)] = t;
/*      */     }
/*      */   }
/*      */   
/*      */   private static int trLog(int n) {
/*  994 */     return (n & 0xFF00) != 0 ? 8 + LOG_2_TABLE[(n >> 8 & 0xFF)] : (n & 0xFFFF0000) != 0 ? LOG_2_TABLE[(n >> 16 & 0x10F)] : (n & 0xFF000000) != 0 ? 24 + LOG_2_TABLE[(n >> 24 & 0xFF)] : LOG_2_TABLE[(n & 0xFF)];
/*      */   }
/*      */   
/*      */ 
/*      */   private int trMedian3(int isa, int isaD, int isaN, int v1, int v2, int v3)
/*      */   {
/* 1000 */     int[] SA = this.SA;
/*      */     
/* 1002 */     int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
/* 1003 */     int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
/* 1004 */     int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
/*      */     
/* 1006 */     if (SA_v1 > SA_v2) {
/* 1007 */       int temp = v1;
/* 1008 */       v1 = v2;
/* 1009 */       v2 = temp;
/* 1010 */       int SA_vtemp = SA_v1;
/* 1011 */       SA_v1 = SA_v2;
/* 1012 */       SA_v2 = SA_vtemp;
/*      */     }
/* 1014 */     if (SA_v2 > SA_v3) {
/* 1015 */       if (SA_v1 > SA_v3) {
/* 1016 */         return v1;
/*      */       }
/* 1018 */       return v3;
/*      */     }
/*      */     
/* 1021 */     return v2;
/*      */   }
/*      */   
/*      */   private int trMedian5(int isa, int isaD, int isaN, int v1, int v2, int v3, int v4, int v5) {
/* 1025 */     int[] SA = this.SA;
/*      */     
/* 1027 */     int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
/* 1028 */     int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
/* 1029 */     int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
/* 1030 */     int SA_v4 = trGetC(isa, isaD, isaN, SA[v4]);
/* 1031 */     int SA_v5 = trGetC(isa, isaD, isaN, SA[v5]);
/*      */     
/*      */ 
/*      */ 
/* 1035 */     if (SA_v2 > SA_v3) {
/* 1036 */       int temp = v2;
/* 1037 */       v2 = v3;
/* 1038 */       v3 = temp;
/* 1039 */       int SA_vtemp = SA_v2;
/* 1040 */       SA_v2 = SA_v3;
/* 1041 */       SA_v3 = SA_vtemp;
/*      */     }
/* 1043 */     if (SA_v4 > SA_v5) {
/* 1044 */       int temp = v4;
/* 1045 */       v4 = v5;
/* 1046 */       v5 = temp;
/* 1047 */       int SA_vtemp = SA_v4;
/* 1048 */       SA_v4 = SA_v5;
/* 1049 */       SA_v5 = SA_vtemp;
/*      */     }
/* 1051 */     if (SA_v2 > SA_v4) {
/* 1052 */       int temp = v2;
/* 1053 */       v4 = temp;
/* 1054 */       int SA_vtemp = SA_v2;
/* 1055 */       SA_v4 = SA_vtemp;
/* 1056 */       temp = v3;
/* 1057 */       v3 = v5;
/* 1058 */       v5 = temp;
/* 1059 */       SA_vtemp = SA_v3;
/* 1060 */       SA_v3 = SA_v5;
/* 1061 */       SA_v5 = SA_vtemp;
/*      */     }
/* 1063 */     if (SA_v1 > SA_v3) {
/* 1064 */       int temp = v1;
/* 1065 */       v1 = v3;
/* 1066 */       v3 = temp;
/* 1067 */       int SA_vtemp = SA_v1;
/* 1068 */       SA_v1 = SA_v3;
/* 1069 */       SA_v3 = SA_vtemp;
/*      */     }
/* 1071 */     if (SA_v1 > SA_v4) {
/* 1072 */       int temp = v1;
/* 1073 */       v4 = temp;
/* 1074 */       int SA_vtemp = SA_v1;
/* 1075 */       SA_v4 = SA_vtemp;
/* 1076 */       v3 = v5;
/* 1077 */       SA_v3 = SA_v5;
/*      */     }
/* 1079 */     if (SA_v3 > SA_v4) {
/* 1080 */       return v4;
/*      */     }
/* 1082 */     return v3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int trPivot(int isa, int isaD, int isaN, int first, int last)
/*      */   {
/* 1089 */     int t = last - first;
/* 1090 */     int middle = first + t / 2;
/*      */     
/* 1092 */     if (t <= 512) {
/* 1093 */       if (t <= 32) {
/* 1094 */         return trMedian3(isa, isaD, isaN, first, middle, last - 1);
/*      */       }
/* 1096 */       t >>= 2;
/* 1097 */       return trMedian5(isa, isaD, isaN, first, first + t, middle, last - 1 - t, last - 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1104 */     t >>= 3;
/* 1105 */     return trMedian3(isa, isaD, isaN, trMedian3(isa, isaD, isaN, first, first + t, first + (t << 1)), trMedian3(isa, isaD, isaN, middle - t, middle, middle + t), trMedian3(isa, isaD, isaN, last - 1 - (t << 1), last - 1 - t, last - 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void lsUpdateGroup(int isa, int first, int last)
/*      */   {
/* 1116 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1121 */     for (int a = first; a < last; a++) {
/* 1122 */       if (0 <= SA[a]) {
/* 1123 */         int b = a;
/*      */         do {
/* 1125 */           SA[(isa + SA[a])] = a;
/* 1126 */           a++; } while ((a < last) && (0 <= SA[a]));
/* 1127 */         SA[b] = (b - a);
/* 1128 */         if (last <= a) {
/*      */           break;
/*      */         }
/*      */       }
/* 1132 */       int b = a;
/*      */       do {
/* 1134 */         SA[a] ^= 0xFFFFFFFF;
/* 1135 */       } while (SA[(++a)] < 0);
/* 1136 */       int t = a;
/*      */       do {
/* 1138 */         SA[(isa + SA[b])] = t;
/* 1139 */         b++; } while (b <= a);
/*      */     }
/*      */   }
/*      */   
/*      */   private void lsIntroSort(int isa, int isaD, int isaN, int first, int last) {
/* 1144 */     int[] SA = this.SA;
/*      */     
/* 1146 */     StackEntry[] stack = new StackEntry[64];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1151 */     int x = 0;
/*      */     
/*      */ 
/* 1154 */     int ssize = 0;int limit = trLog(last - first);
/* 1155 */     for (;;) { if (last - first <= 8) {
/* 1156 */         if (1 < last - first) {
/* 1157 */           trInsertionSort(isa, isaD, isaN, first, last);
/* 1158 */           lsUpdateGroup(isa, first, last);
/* 1159 */         } else if (last - first == 1) {
/* 1160 */           SA[first] = -1;
/*      */         }
/* 1162 */         if (ssize == 0) {
/* 1163 */           return;
/*      */         }
/* 1165 */         StackEntry entry = stack[(--ssize)];
/* 1166 */         first = entry.a;
/* 1167 */         last = entry.b;
/* 1168 */         limit = entry.c;
/*      */ 
/*      */ 
/*      */       }
/* 1172 */       else if (limit-- == 0) {
/* 1173 */         trHeapSort(isa, isaD, isaN, first, last - first);
/* 1174 */         int b; for (int a = last - 1; first < a; a = b) {
/* 1175 */           x = trGetC(isa, isaD, isaN, SA[a]); for (b = a - 1; 
/* 1176 */               (first <= b) && (trGetC(isa, isaD, isaN, SA[b]) == x); 
/* 1177 */               b--) {
/* 1178 */             SA[b] ^= 0xFFFFFFFF;
/*      */           }
/*      */         }
/* 1181 */         lsUpdateGroup(isa, first, last);
/* 1182 */         if (ssize == 0) {
/* 1183 */           return;
/*      */         }
/* 1185 */         StackEntry entry = stack[(--ssize)];
/* 1186 */         first = entry.a;
/* 1187 */         last = entry.b;
/* 1188 */         limit = entry.c;
/*      */       }
/*      */       else
/*      */       {
/* 1192 */         int a = trPivot(isa, isaD, isaN, first, last);
/* 1193 */         swapElements(SA, first, SA, a);
/* 1194 */         int v = trGetC(isa, isaD, isaN, SA[first]);
/*      */         
/* 1196 */         int b = first + 1;
/* 1197 */         while ((b < last) && ((x = trGetC(isa, isaD, isaN, SA[b])) == v)) {
/* 1198 */           b++;
/*      */         }
/* 1200 */         if (((a = b) < last) && (x < v)) {
/* 1201 */           for (;;) { b++; if ((b >= last) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1202 */             if (x == v) {
/* 1203 */               swapElements(SA, b, SA, a);
/* 1204 */               a++;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1209 */         int c = last - 1;
/* 1210 */         while ((b < c) && ((x = trGetC(isa, isaD, isaN, SA[c])) == v))
/* 1211 */           c--;
/*      */         int d;
/* 1213 */         if ((b < (d = c)) && (x > v)) {
/* 1214 */           while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1215 */             if (x == v) {
/* 1216 */               swapElements(SA, c, SA, d);
/* 1217 */               d--;
/*      */             }
/*      */           }
/*      */         }
/* 1221 */         for (; b < c; 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1232 */             goto 609)
/*      */         {
/* 1222 */           swapElements(SA, b, SA, c);
/* 1223 */           for (;;) { b++; if ((b >= c) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1224 */             if (x == v) {
/* 1225 */               swapElements(SA, b, SA, a);
/* 1226 */               a++;
/*      */             }
/*      */           }
/* 1229 */           while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1230 */             if (x == v) {
/* 1231 */               swapElements(SA, c, SA, d);
/* 1232 */               d--;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1237 */         if (a <= d) {
/* 1238 */           c = b - 1;
/*      */           int s;
/* 1240 */           int t; if ((s = a - first) > (t = b - a)) {
/* 1241 */             s = t;
/*      */           }
/* 1243 */           int e = first; for (int f = b - s; 0 < s; f++) {
/* 1244 */             swapElements(SA, e, SA, f);s--;e++;
/*      */           }
/* 1246 */           if ((s = d - c) > (t = last - d - 1)) {
/* 1247 */             s = t;
/*      */           }
/* 1249 */           e = b; for (f = last - s; 0 < s; f++) {
/* 1250 */             swapElements(SA, e, SA, f);s--;e++;
/*      */           }
/*      */           
/* 1253 */           a = first + (b - a);
/* 1254 */           b = last - (d - c);
/*      */           
/* 1256 */           c = first; for (v = a - 1; c < a; c++) {
/* 1257 */             SA[(isa + SA[c])] = v;
/*      */           }
/* 1259 */           if (b < last) {
/* 1260 */             c = a; for (v = b - 1; c < b; c++) {
/* 1261 */               SA[(isa + SA[c])] = v;
/*      */             }
/*      */           }
/* 1264 */           if (b - a == 1) {
/* 1265 */             SA[a] = -1;
/*      */           }
/*      */           
/* 1268 */           if (a - first <= last - b) {
/* 1269 */             if (first < a) {
/* 1270 */               stack[(ssize++)] = new StackEntry(b, last, limit, 0);
/* 1271 */               last = a;
/*      */             } else {
/* 1273 */               first = b;
/*      */             }
/*      */           }
/* 1276 */           else if (b < last) {
/* 1277 */             stack[(ssize++)] = new StackEntry(first, a, limit, 0);
/* 1278 */             first = b;
/*      */           } else {
/* 1280 */             last = a;
/*      */           }
/*      */         }
/*      */         else {
/* 1284 */           if (ssize == 0) {
/* 1285 */             return;
/*      */           }
/* 1287 */           StackEntry entry = stack[(--ssize)];
/* 1288 */           first = entry.a;
/* 1289 */           last = entry.b;
/* 1290 */           limit = entry.c;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/* 1296 */   private void lsSort(int isa, int n, int depth) { int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1302 */     for (int isaD = isa + depth; -n < SA[0]; isaD += isaD - isa) {
/* 1303 */       int first = 0;
/* 1304 */       int skip = 0;
/*      */       int t;
/* 1306 */       do { if ((t = SA[first]) < 0) {
/* 1307 */           first -= t;
/* 1308 */           skip += t;
/*      */         } else {
/* 1310 */           if (skip != 0) {
/* 1311 */             SA[(first + skip)] = skip;
/* 1312 */             skip = 0;
/*      */           }
/* 1314 */           int last = SA[(isa + t)] + 1;
/* 1315 */           lsIntroSort(isa, isaD, isa + n, first, last);
/* 1316 */           first = last;
/*      */         }
/* 1318 */       } while (first < n);
/* 1319 */       if (skip != 0) {
/* 1320 */         SA[(first + skip)] = skip;
/*      */       }
/* 1322 */       if (n < isaD - isa) {
/* 1323 */         first = 0;
/*      */         do {
/* 1325 */           if ((t = SA[first]) < 0) {
/* 1326 */             first -= t;
/*      */           } else {
/* 1328 */             int last = SA[(isa + t)] + 1;
/* 1329 */             for (int i = first; i < last; i++) {
/* 1330 */               SA[(isa + SA[i])] = i;
/*      */             }
/* 1332 */             first = last;
/*      */           }
/* 1334 */         } while (first < n);
/* 1335 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PartitionResult
/*      */   {
/*      */     final int first;
/*      */     final int last;
/*      */     
/*      */     PartitionResult(int first, int last)
/*      */     {
/* 1347 */       this.first = first;
/* 1348 */       this.last = last;
/*      */     }
/*      */   }
/*      */   
/*      */   private PartitionResult trPartition(int isa, int isaD, int isaN, int first, int last, int v)
/*      */   {
/* 1354 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/* 1358 */     int x = 0;
/*      */     
/* 1360 */     int b = first;
/* 1361 */     while ((b < last) && ((x = trGetC(isa, isaD, isaN, SA[b])) == v))
/* 1362 */       b++;
/*      */     int a;
/* 1364 */     if (((a = b) < last) && (x < v)) {
/* 1365 */       for (;;) { b++; if ((b >= last) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1366 */         if (x == v) {
/* 1367 */           swapElements(SA, b, SA, a);
/* 1368 */           a++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1373 */     int c = last - 1;
/* 1374 */     while ((b < c) && ((x = trGetC(isa, isaD, isaN, SA[c])) == v))
/* 1375 */       c--;
/*      */     int d;
/* 1377 */     if ((b < (d = c)) && (x > v)) {
/* 1378 */       while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1379 */         if (x == v) {
/* 1380 */           swapElements(SA, c, SA, d);
/* 1381 */           d--;
/*      */         }
/*      */       }
/*      */     }
/* 1385 */     for (; b < c; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1396 */         goto 299)
/*      */     {
/* 1386 */       swapElements(SA, b, SA, c);
/* 1387 */       for (;;) { b++; if ((b >= c) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1388 */         if (x == v) {
/* 1389 */           swapElements(SA, b, SA, a);
/* 1390 */           a++;
/*      */         }
/*      */       }
/* 1393 */       while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1394 */         if (x == v) {
/* 1395 */           swapElements(SA, c, SA, d);
/* 1396 */           d--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1401 */     if (a <= d) {
/* 1402 */       c = b - 1;
/* 1403 */       int s; int t; if ((s = a - first) > (t = b - a)) {
/* 1404 */         s = t;
/*      */       }
/* 1406 */       int e = first; for (int f = b - s; 0 < s; f++) {
/* 1407 */         swapElements(SA, e, SA, f);s--;e++;
/*      */       }
/* 1409 */       if ((s = d - c) > (t = last - d - 1)) {
/* 1410 */         s = t;
/*      */       }
/* 1412 */       e = b; for (f = last - s; 0 < s; f++) {
/* 1413 */         swapElements(SA, e, SA, f);s--;e++;
/*      */       }
/* 1415 */       first += b - a;
/* 1416 */       last -= d - c;
/*      */     }
/* 1418 */     return new PartitionResult(first, last);
/*      */   }
/*      */   
/*      */   private void trCopy(int isa, int isaN, int first, int a, int b, int last, int depth)
/*      */   {
/* 1423 */     int[] SA = this.SA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1428 */     int v = b - 1;
/*      */     
/* 1430 */     int c = first; for (int d = a - 1; c <= d; c++) { int s;
/* 1431 */       if ((s = SA[c] - depth) < 0) {
/* 1432 */         s += isaN - isa;
/*      */       }
/* 1434 */       if (SA[(isa + s)] == v) {
/* 1435 */         SA[(++d)] = s;
/* 1436 */         SA[(isa + s)] = d;
/*      */       }
/*      */     }
/* 1439 */     c = last - 1;int e = d + 1; for (d = b; e < d; c--) { int s;
/* 1440 */       if ((s = SA[c] - depth) < 0) {
/* 1441 */         s += isaN - isa;
/*      */       }
/* 1443 */       if (SA[(isa + s)] == v) {
/* 1444 */         SA[(--d)] = s;
/* 1445 */         SA[(isa + s)] = d;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void trIntroSort(int isa, int isaD, int isaN, int first, int last, TRBudget budget, int size)
/*      */   {
/* 1452 */     int[] SA = this.SA;
/*      */     
/* 1454 */     StackEntry[] stack = new StackEntry[64];
/*      */     
/*      */ 
/*      */ 
/* 1458 */     int x = 0;
/*      */     
/*      */ 
/*      */ 
/* 1462 */     int ssize = 0;int limit = trLog(last - first);
/* 1463 */     for (;;) { if (limit < 0) {
/* 1464 */         if (limit == -1) {
/* 1465 */           if (!budget.update(size, last - first)) {
/*      */             break;
/*      */           }
/* 1468 */           PartitionResult result = trPartition(isa, isaD - 1, isaN, first, last, last - 1);
/* 1469 */           int a = result.first;
/* 1470 */           int b = result.last;
/* 1471 */           if ((first < a) || (b < last)) {
/* 1472 */             if (a < last) {
/* 1473 */               int c = first; for (int v = a - 1; c < a; c++) {
/* 1474 */                 SA[(isa + SA[c])] = v;
/*      */               }
/*      */             }
/* 1477 */             if (b < last) {
/* 1478 */               int c = a; for (int v = b - 1; c < b; c++) {
/* 1479 */                 SA[(isa + SA[c])] = v;
/*      */               }
/*      */             }
/*      */             
/* 1483 */             stack[(ssize++)] = new StackEntry(0, a, b, 0);
/* 1484 */             stack[(ssize++)] = new StackEntry(isaD - 1, first, last, -2);
/* 1485 */             if (a - first <= last - b) {
/* 1486 */               if (1 < a - first) {
/* 1487 */                 stack[(ssize++)] = new StackEntry(isaD, b, last, trLog(last - b));
/* 1488 */                 last = a;limit = trLog(a - first);
/* 1489 */               } else if (1 < last - b) {
/* 1490 */                 first = b;limit = trLog(last - b);
/*      */               } else {
/* 1492 */                 if (ssize == 0) {
/* 1493 */                   return;
/*      */                 }
/* 1495 */                 StackEntry entry = stack[(--ssize)];
/* 1496 */                 isaD = entry.a;
/* 1497 */                 first = entry.b;
/* 1498 */                 last = entry.c;
/* 1499 */                 limit = entry.d;
/*      */               }
/*      */             }
/* 1502 */             else if (1 < last - b) {
/* 1503 */               stack[(ssize++)] = new StackEntry(isaD, first, a, trLog(a - first));
/* 1504 */               first = b;
/* 1505 */               limit = trLog(last - b);
/* 1506 */             } else if (1 < a - first) {
/* 1507 */               last = a;
/* 1508 */               limit = trLog(a - first);
/*      */             } else {
/* 1510 */               if (ssize == 0) {
/* 1511 */                 return;
/*      */               }
/* 1513 */               StackEntry entry = stack[(--ssize)];
/* 1514 */               isaD = entry.a;
/* 1515 */               first = entry.b;
/* 1516 */               last = entry.c;
/* 1517 */               limit = entry.d;
/*      */             }
/*      */           }
/*      */           else {
/* 1521 */             for (int c = first; c < last; c++) {
/* 1522 */               SA[(isa + SA[c])] = c;
/*      */             }
/* 1524 */             if (ssize == 0) {
/* 1525 */               return;
/*      */             }
/* 1527 */             StackEntry entry = stack[(--ssize)];
/* 1528 */             isaD = entry.a;
/* 1529 */             first = entry.b;
/* 1530 */             last = entry.c;
/* 1531 */             limit = entry.d;
/*      */           }
/* 1533 */         } else if (limit == -2) {
/* 1534 */           int a = stack[(--ssize)].b;
/* 1535 */           int b = stack[ssize].c;
/* 1536 */           trCopy(isa, isaN, first, a, b, last, isaD - isa);
/* 1537 */           if (ssize == 0) {
/* 1538 */             return;
/*      */           }
/* 1540 */           StackEntry entry = stack[(--ssize)];
/* 1541 */           isaD = entry.a;
/* 1542 */           first = entry.b;
/* 1543 */           last = entry.c;
/* 1544 */           limit = entry.d;
/*      */         } else {
/* 1546 */           if (0 <= SA[first]) {
/* 1547 */             int a = first;
/*      */             do {
/* 1549 */               SA[(isa + SA[a])] = a;
/* 1550 */               a++; } while ((a < last) && (0 <= SA[a]));
/* 1551 */             first = a;
/*      */           }
/* 1553 */           if (first < last) {
/* 1554 */             int a = first;
/*      */             do {
/* 1556 */               SA[a] ^= 0xFFFFFFFF;
/* 1557 */             } while (SA[(++a)] < 0);
/* 1558 */             int next = SA[(isa + SA[a])] != SA[(isaD + SA[a])] ? trLog(a - first + 1) : -1;
/* 1559 */             a++; if (a < last) {
/* 1560 */               int b = first; for (int v = a - 1; b < a; b++) {
/* 1561 */                 SA[(isa + SA[b])] = v;
/*      */               }
/*      */             }
/*      */             
/* 1565 */             if (a - first <= last - a) {
/* 1566 */               stack[(ssize++)] = new StackEntry(isaD, a, last, -3);
/* 1567 */               isaD++;last = a;limit = next;
/*      */             }
/* 1569 */             else if (1 < last - a) {
/* 1570 */               stack[(ssize++)] = new StackEntry(isaD + 1, first, a, next);
/* 1571 */               first = a;limit = -3;
/*      */             } else {
/* 1573 */               isaD++;last = a;limit = next;
/*      */             }
/*      */           }
/*      */           else {
/* 1577 */             if (ssize == 0) {
/* 1578 */               return;
/*      */             }
/* 1580 */             StackEntry entry = stack[(--ssize)];
/* 1581 */             isaD = entry.a;
/* 1582 */             first = entry.b;
/* 1583 */             last = entry.c;
/* 1584 */             limit = entry.d;
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/* 1590 */       else if (last - first <= 8) {
/* 1591 */         if (!budget.update(size, last - first)) {
/*      */           break;
/*      */         }
/* 1594 */         trInsertionSort(isa, isaD, isaN, first, last);
/* 1595 */         limit = -3;
/*      */ 
/*      */ 
/*      */       }
/* 1599 */       else if (limit-- == 0) {
/* 1600 */         if (!budget.update(size, last - first)) {
/*      */           break;
/*      */         }
/* 1603 */         trHeapSort(isa, isaD, isaN, first, last - first);
/* 1604 */         int b; for (int a = last - 1; first < a; a = b) {
/* 1605 */           x = trGetC(isa, isaD, isaN, SA[a]); for (b = a - 1; 
/* 1606 */               (first <= b) && (trGetC(isa, isaD, isaN, SA[b]) == x); 
/* 1607 */               b--) {
/* 1608 */             SA[b] ^= 0xFFFFFFFF;
/*      */           }
/*      */         }
/* 1611 */         limit = -3;
/*      */       }
/*      */       else
/*      */       {
/* 1615 */         int a = trPivot(isa, isaD, isaN, first, last);
/*      */         
/* 1617 */         swapElements(SA, first, SA, a);
/* 1618 */         int v = trGetC(isa, isaD, isaN, SA[first]);
/*      */         
/* 1620 */         int b = first + 1;
/* 1621 */         while ((b < last) && ((x = trGetC(isa, isaD, isaN, SA[b])) == v)) {
/* 1622 */           b++;
/*      */         }
/* 1624 */         if (((a = b) < last) && (x < v)) {
/* 1625 */           for (;;) { b++; if ((b >= last) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1626 */             if (x == v) {
/* 1627 */               swapElements(SA, b, SA, a);
/* 1628 */               a++;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1633 */         int c = last - 1;
/* 1634 */         while ((b < c) && ((x = trGetC(isa, isaD, isaN, SA[c])) == v))
/* 1635 */           c--;
/*      */         int d;
/* 1637 */         if ((b < (d = c)) && (x > v)) {
/* 1638 */           while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1639 */             if (x == v) {
/* 1640 */               swapElements(SA, c, SA, d);
/* 1641 */               d--;
/*      */             }
/*      */           }
/*      */         }
/* 1645 */         for (; b < c; 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1656 */             goto 1491)
/*      */         {
/* 1646 */           swapElements(SA, b, SA, c);
/* 1647 */           for (;;) { b++; if ((b >= c) || ((x = trGetC(isa, isaD, isaN, SA[b])) > v)) break;
/* 1648 */             if (x == v) {
/* 1649 */               swapElements(SA, b, SA, a);
/* 1650 */               a++;
/*      */             }
/*      */           }
/* 1653 */           while ((b < --c) && ((x = trGetC(isa, isaD, isaN, SA[c])) >= v)) {
/* 1654 */             if (x == v) {
/* 1655 */               swapElements(SA, c, SA, d);
/* 1656 */               d--;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1661 */         if (a <= d) {
/* 1662 */           c = b - 1;
/*      */           int s;
/* 1664 */           int t; if ((s = a - first) > (t = b - a)) {
/* 1665 */             s = t;
/*      */           }
/* 1667 */           int e = first; for (int f = b - s; 0 < s; f++) {
/* 1668 */             swapElements(SA, e, SA, f);s--;e++;
/*      */           }
/* 1670 */           if ((s = d - c) > (t = last - d - 1)) {
/* 1671 */             s = t;
/*      */           }
/* 1673 */           e = b; for (f = last - s; 0 < s; f++) {
/* 1674 */             swapElements(SA, e, SA, f);s--;e++;
/*      */           }
/*      */           
/* 1677 */           a = first + (b - a);
/* 1678 */           b = last - (d - c);
/* 1679 */           int next = SA[(isa + SA[a])] != v ? trLog(b - a) : -1;
/*      */           
/* 1681 */           c = first; for (v = a - 1; c < a; c++) {
/* 1682 */             SA[(isa + SA[c])] = v;
/*      */           }
/* 1684 */           if (b < last) {
/* 1685 */             c = a; for (v = b - 1; c < b; c++) {
/* 1686 */               SA[(isa + SA[c])] = v;
/*      */             }
/*      */           }
/* 1689 */           if (a - first <= last - b) {
/* 1690 */             if (last - b <= b - a) {
/* 1691 */               if (1 < a - first) {
/* 1692 */                 stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1693 */                 stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1694 */                 last = a;
/* 1695 */               } else if (1 < last - b) {
/* 1696 */                 stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1697 */                 first = b;
/* 1698 */               } else if (1 < b - a) {
/* 1699 */                 isaD++;
/* 1700 */                 first = a;
/* 1701 */                 last = b;
/* 1702 */                 limit = next;
/*      */               } else {
/* 1704 */                 if (ssize == 0) {
/* 1705 */                   return;
/*      */                 }
/* 1707 */                 StackEntry entry = stack[(--ssize)];
/* 1708 */                 isaD = entry.a;
/* 1709 */                 first = entry.b;
/* 1710 */                 last = entry.c;
/* 1711 */                 limit = entry.d;
/*      */               }
/* 1713 */             } else if (a - first <= b - a) {
/* 1714 */               if (1 < a - first) {
/* 1715 */                 stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1716 */                 stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1717 */                 last = a;
/* 1718 */               } else if (1 < b - a) {
/* 1719 */                 stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1720 */                 isaD++;
/* 1721 */                 first = a;
/* 1722 */                 last = b;
/* 1723 */                 limit = next;
/*      */               } else {
/* 1725 */                 first = b;
/*      */               }
/*      */             }
/* 1728 */             else if (1 < b - a) {
/* 1729 */               stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1730 */               stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1731 */               isaD++;
/* 1732 */               first = a;
/* 1733 */               last = b;
/* 1734 */               limit = next;
/*      */             } else {
/* 1736 */               stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1737 */               last = a;
/*      */             }
/*      */             
/*      */           }
/* 1741 */           else if (a - first <= b - a) {
/* 1742 */             if (1 < last - b) {
/* 1743 */               stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1744 */               stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1745 */               first = b;
/* 1746 */             } else if (1 < a - first) {
/* 1747 */               stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1748 */               last = a;
/* 1749 */             } else if (1 < b - a) {
/* 1750 */               isaD++;
/* 1751 */               first = a;
/* 1752 */               last = b;
/* 1753 */               limit = next;
/*      */             } else {
/* 1755 */               stack[(ssize++)] = new StackEntry(isaD, first, last, limit);
/*      */             }
/* 1757 */           } else if (last - b <= b - a) {
/* 1758 */             if (1 < last - b) {
/* 1759 */               stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1760 */               stack[(ssize++)] = new StackEntry(isaD + 1, a, b, next);
/* 1761 */               first = b;
/* 1762 */             } else if (1 < b - a) {
/* 1763 */               stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1764 */               isaD++;
/* 1765 */               first = a;
/* 1766 */               last = b;
/* 1767 */               limit = next;
/*      */             } else {
/* 1769 */               last = a;
/*      */             }
/*      */           }
/* 1772 */           else if (1 < b - a) {
/* 1773 */             stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1774 */             stack[(ssize++)] = new StackEntry(isaD, b, last, limit);
/* 1775 */             isaD++;
/* 1776 */             first = a;
/* 1777 */             last = b;
/* 1778 */             limit = next;
/*      */           } else {
/* 1780 */             stack[(ssize++)] = new StackEntry(isaD, first, a, limit);
/* 1781 */             first = b;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1786 */           if (!budget.update(size, last - first)) {
/*      */             break;
/*      */           }
/* 1789 */           limit++;isaD++;
/*      */         }
/*      */       }
/*      */     }
/* 1793 */     for (int s = 0; s < ssize; s++) {
/* 1794 */       if (stack[s].d == -3) {
/* 1795 */         lsUpdateGroup(isa, stack[s].b, stack[s].c);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TRBudget {
/*      */     int budget;
/*      */     int chance;
/*      */     
/*      */     TRBudget(int budget, int chance) {
/* 1805 */       this.budget = budget;
/* 1806 */       this.chance = chance;
/*      */     }
/*      */     
/*      */     boolean update(int size, int n) {
/* 1810 */       this.budget -= n;
/* 1811 */       if (this.budget <= 0) {
/* 1812 */         if (--this.chance == 0) {
/* 1813 */           return false;
/*      */         }
/* 1815 */         this.budget += size;
/*      */       }
/* 1817 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void trSort(int isa, int n, int depth) {
/* 1822 */     int[] SA = this.SA;
/*      */     
/* 1824 */     int first = 0;
/*      */     
/*      */ 
/* 1827 */     if (-n < SA[0]) {
/* 1828 */       TRBudget budget = new TRBudget(n, trLog(n) * 2 / 3 + 1);
/*      */       do { int t;
/* 1830 */         if ((t = SA[first]) < 0) {
/* 1831 */           first -= t;
/*      */         } else {
/* 1833 */           int last = SA[(isa + t)] + 1;
/* 1834 */           if (1 < last - first) {
/* 1835 */             trIntroSort(isa, isa + depth, isa + n, first, last, budget, n);
/* 1836 */             if (budget.chance == 0)
/*      */             {
/* 1838 */               if (0 < first) {
/* 1839 */                 SA[0] = (-first);
/*      */               }
/* 1841 */               lsSort(isa, n, depth);
/* 1842 */               break;
/*      */             }
/*      */           }
/* 1845 */           first = last;
/*      */         }
/* 1847 */       } while (first < n);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static int BUCKET_B(int c0, int c1)
/*      */   {
/* 1854 */     return c1 << 8 | c0;
/*      */   }
/*      */   
/*      */   private static int BUCKET_BSTAR(int c0, int c1) {
/* 1858 */     return c0 << 8 | c1;
/*      */   }
/*      */   
/*      */   private int sortTypeBstar(int[] bucketA, int[] bucketB) {
/* 1862 */     byte[] T = this.T;
/* 1863 */     int[] SA = this.SA;
/* 1864 */     int n = this.n;
/* 1865 */     int[] tempbuf = new int['Ā'];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1873 */     int i = 1; for (int flag = 1; i < n; i++) {
/* 1874 */       if (T[(i - 1)] != T[i]) {
/* 1875 */         if ((T[(i - 1)] & 0xFF) <= (T[i] & 0xFF)) break;
/* 1876 */         flag = 0; break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1881 */     i = n - 1;
/* 1882 */     int m = n;
/*      */     int ti;
/*      */     int t0;
/* 1885 */     if (((ti = T[i] & 0xFF) < (t0 = T[0] & 0xFF)) || ((T[i] == T[0]) && (flag != 0))) {
/* 1886 */       if (flag == 0) {
/* 1887 */         bucketB[BUCKET_BSTAR(ti, t0)] += 1;
/* 1888 */         SA[(--m)] = i;
/*      */       } else {
/* 1890 */         bucketB[BUCKET_B(ti, t0)] += 1; }
/*      */       int ti1;
/* 1892 */       for (i--; (0 <= i) && ((ti = T[i] & 0xFF) <= (ti1 = T[(i + 1)] & 0xFF)); i--) {
/* 1893 */         bucketB[BUCKET_B(ti, ti1)] += 1;
/*      */       }
/*      */     }
/*      */     
/* 1897 */     while (0 <= i) {
/*      */       do {
/* 1899 */         bucketA[(T[i] & 0xFF)] += 1;
/* 1900 */       } while ((0 <= --i) && ((T[i] & 0xFF) >= (T[(i + 1)] & 0xFF)));
/* 1901 */       if (0 <= i) {
/* 1902 */         bucketB[BUCKET_BSTAR(T[i] & 0xFF, T[(i + 1)] & 0xFF)] += 1;
/* 1903 */         SA[(--m)] = i;
/* 1904 */         int ti1; for (i--; (0 <= i) && ((ti = T[i] & 0xFF) <= (ti1 = T[(i + 1)] & 0xFF)); i--) {
/* 1905 */           bucketB[BUCKET_B(ti, ti1)] += 1;
/*      */         }
/*      */       }
/*      */     }
/* 1909 */     m = n - m;
/* 1910 */     if (m == 0) {
/* 1911 */       for (i = 0; i < n; i++) {
/* 1912 */         SA[i] = i;
/*      */       }
/* 1914 */       return 0;
/*      */     }
/*      */     
/* 1917 */     int c0 = 0;i = -1; for (int j = 0; c0 < 256; c0++) {
/* 1918 */       int t = i + bucketA[c0];
/* 1919 */       bucketA[c0] = (i + j);
/* 1920 */       i = t + bucketB[BUCKET_B(c0, c0)];
/* 1921 */       for (int c1 = c0 + 1; c1 < 256; c1++) {
/* 1922 */         j += bucketB[BUCKET_BSTAR(c0, c1)];
/* 1923 */         bucketB[(c0 << 8 | c1)] = j;
/* 1924 */         i += bucketB[BUCKET_B(c0, c1)];
/*      */       }
/*      */     }
/*      */     
/* 1928 */     int PAb = n - m;
/* 1929 */     int ISAb = m;
/* 1930 */     for (i = m - 2; 0 <= i; i--) {
/* 1931 */       int t = SA[(PAb + i)];
/* 1932 */       c0 = T[t] & 0xFF;
/* 1933 */       int c1 = T[(t + 1)] & 0xFF;
/* 1934 */       SA[(bucketB[BUCKET_BSTAR(c0, c1)] -= 1)] = i;
/*      */     }
/* 1936 */     int t = SA[(PAb + m - 1)];
/* 1937 */     c0 = T[t] & 0xFF;
/* 1938 */     int c1 = T[(t + 1)] & 0xFF;
/* 1939 */     SA[(bucketB[BUCKET_BSTAR(c0, c1)] -= 1)] = (m - 1);
/*      */     
/* 1941 */     int[] buf = SA;
/* 1942 */     int bufoffset = m;
/* 1943 */     int bufsize = n - 2 * m;
/* 1944 */     if (bufsize <= 256) {
/* 1945 */       buf = tempbuf;
/* 1946 */       bufoffset = 0;
/* 1947 */       bufsize = 256;
/*      */     }
/*      */     
/* 1950 */     c0 = 255; for (j = m; 0 < j; c0--) {
/* 1951 */       for (c1 = 255; c0 < c1; c1--) {
/* 1952 */         i = bucketB[BUCKET_BSTAR(c0, c1)];
/* 1953 */         if (1 < j - i) {
/* 1954 */           subStringSort(PAb, i, j, buf, bufoffset, bufsize, 2, SA[i] == m - 1, n);
/*      */         }
/* 1951 */         j = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1959 */     for (i = m - 1; 0 <= i; i--) {
/* 1960 */       if (0 <= SA[i]) {
/* 1961 */         j = i;
/*      */         do {
/* 1963 */           SA[(ISAb + SA[i])] = i;
/* 1964 */         } while ((0 <= --i) && (0 <= SA[i]));
/* 1965 */         SA[(i + 1)] = (i - j);
/* 1966 */         if (i <= 0) {
/*      */           break;
/*      */         }
/*      */       }
/* 1970 */       j = i;
/*      */       do {
/* 1972 */         SA[(ISAb + (SA[i] ^= 0xFFFFFFFF))] = j;
/* 1973 */       } while (SA[(--i)] < 0);
/* 1974 */       SA[(ISAb + SA[i])] = j;
/*      */     }
/*      */     
/* 1977 */     trSort(ISAb, m, 1);
/*      */     
/* 1979 */     i = n - 1;j = m;
/* 1980 */     if (((T[i] & 0xFF) < (T[0] & 0xFF)) || ((T[i] == T[0]) && (flag != 0))) {
/* 1981 */       if (flag == 0) {
/* 1982 */         SA[SA[(ISAb + --j)]] = i;
/*      */       }
/* 1984 */       for (i--; (0 <= i) && ((T[i] & 0xFF) <= (T[(i + 1)] & 0xFF));) {
/* 1985 */         i--;
/*      */       }
/*      */     }
/* 1988 */     while (0 <= i) {
/* 1989 */       for (i--; (0 <= i) && ((T[i] & 0xFF) >= (T[(i + 1)] & 0xFF));) {
/* 1990 */         i--;
/*      */       }
/* 1992 */       if (0 <= i) {
/* 1993 */         SA[SA[(ISAb + --j)]] = i;
/* 1994 */         for (i--; (0 <= i) && ((T[i] & 0xFF) <= (T[(i + 1)] & 0xFF));) {
/* 1995 */           i--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2000 */     c0 = 255;i = n - 1; for (int k = m - 1; 0 <= c0; c0--) {
/* 2001 */       for (c1 = 255; c0 < c1; c1--) {
/* 2002 */         t = i - bucketB[BUCKET_B(c0, c1)];
/* 2003 */         bucketB[BUCKET_B(c0, c1)] = (i + 1);
/*      */         
/* 2005 */         i = t; for (j = bucketB[BUCKET_BSTAR(c0, c1)]; j <= k; k--) {
/* 2006 */           SA[i] = SA[k];i--;
/*      */         }
/*      */       }
/* 2009 */       t = i - bucketB[BUCKET_B(c0, c0)];
/* 2010 */       bucketB[BUCKET_B(c0, c0)] = (i + 1);
/* 2011 */       if (c0 < 255) {
/* 2012 */         bucketB[BUCKET_BSTAR(c0, c0 + 1)] = (t + 1);
/*      */       }
/* 2014 */       i = bucketA[c0];
/*      */     }
/* 2016 */     return m;
/*      */   }
/*      */   
/*      */   private int constructBWT(int[] bucketA, int[] bucketB) {
/* 2020 */     byte[] T = this.T;
/* 2021 */     int[] SA = this.SA;
/* 2022 */     int n = this.n;
/*      */     
/* 2024 */     int t = 0;
/*      */     
/* 2026 */     int c2 = 0;
/* 2027 */     int orig = -1;
/*      */     
/* 2029 */     for (int c1 = 254; 0 <= c1; c1--) {
/* 2030 */       int i = bucketB[BUCKET_BSTAR(c1, c1 + 1)];int j = bucketA[(c1 + 1)];t = 0;c2 = -1;
/* 2031 */       for (; i <= j; 
/* 2032 */           j--) { int s;
/* 2033 */         int s1; if (0 <= (s1 = s = SA[j])) {
/* 2034 */           s--; if (s < 0)
/* 2035 */             s = n - 1;
/*      */           int c0;
/* 2037 */           if ((c0 = T[s] & 0xFF) <= c1) {
/* 2038 */             SA[j] = (s1 ^ 0xFFFFFFFF);
/* 2039 */             if ((0 < s) && ((T[(s - 1)] & 0xFF) > c0)) {
/* 2040 */               s ^= 0xFFFFFFFF;
/*      */             }
/* 2042 */             if (c2 == c0) {
/* 2043 */               SA[(--t)] = s;
/*      */             } else {
/* 2045 */               if (0 <= c2) {
/* 2046 */                 bucketB[BUCKET_B(c2, c1)] = t;
/*      */               }
/* 2048 */               SA[(t = bucketB[BUCKET_B(c2 = c0, c1)] - 1)] = s;
/*      */             }
/*      */           }
/*      */         } else {
/* 2052 */           SA[j] = (s ^ 0xFFFFFFFF);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2057 */     for (int i = 0; i < n; i++) { int s;
/* 2058 */       int s1; if (0 <= (s1 = s = SA[i])) {
/* 2059 */         s--; if (s < 0)
/* 2060 */           s = n - 1;
/*      */         int c0;
/* 2062 */         if ((c0 = T[s] & 0xFF) >= (T[(s + 1)] & 0xFF)) {
/* 2063 */           if ((0 < s) && ((T[(s - 1)] & 0xFF) < c0)) {
/* 2064 */             s ^= 0xFFFFFFFF;
/*      */           }
/* 2066 */           if (c0 == c2) {
/* 2067 */             SA[(++t)] = s;
/*      */           } else {
/* 2069 */             if (c2 != -1) {
/* 2070 */               bucketA[c2] = t;
/*      */             }
/* 2072 */             SA[(t = bucketA[(c2 = c0)] + 1)] = s;
/*      */           }
/*      */         }
/*      */       } else {
/* 2076 */         s1 ^= 0xFFFFFFFF;
/*      */       }
/*      */       
/* 2079 */       if (s1 == 0) {
/* 2080 */         SA[i] = T[(n - 1)];
/* 2081 */         orig = i;
/*      */       } else {
/* 2083 */         SA[i] = T[(s1 - 1)];
/*      */       }
/*      */     }
/* 2086 */     return orig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int bwt()
/*      */   {
/* 2094 */     int[] SA = this.SA;
/* 2095 */     byte[] T = this.T;
/* 2096 */     int n = this.n;
/*      */     
/* 2098 */     int[] bucketA = new int['Ā'];
/* 2099 */     int[] bucketB = new int[65536];
/*      */     
/* 2101 */     if (n == 0) {
/* 2102 */       return 0;
/*      */     }
/* 2104 */     if (n == 1) {
/* 2105 */       SA[0] = T[0];
/* 2106 */       return 0;
/*      */     }
/*      */     
/* 2109 */     int m = sortTypeBstar(bucketA, bucketB);
/* 2110 */     if (0 < m) {
/* 2111 */       return constructBWT(bucketA, bucketB);
/*      */     }
/* 2113 */     return 0;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2DivSufSort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */