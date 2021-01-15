/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.network.LanServerDetector.LanServer;
/*     */ 
/*     */ public class ServerSelectionList extends GuiListExtended
/*     */ {
/*     */   private final GuiMultiplayer owner;
/*  12 */   private final List<ServerListEntryNormal> field_148198_l = Lists.newArrayList();
/*  13 */   private final List<ServerListEntryLanDetected> field_148199_m = Lists.newArrayList();
/*  14 */   private final GuiListExtended.IGuiListEntry lanScanEntry = new ServerListEntryLanScan();
/*  15 */   private int selectedSlotIndex = -1;
/*     */   
/*     */   public ServerSelectionList(GuiMultiplayer ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
/*     */   {
/*  19 */     super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
/*  20 */     this.owner = ownerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiListExtended.IGuiListEntry getListEntry(int index)
/*     */   {
/*  28 */     if (index < this.field_148198_l.size())
/*     */     {
/*  30 */       return (GuiListExtended.IGuiListEntry)this.field_148198_l.get(index);
/*     */     }
/*     */     
/*     */ 
/*  34 */     index -= this.field_148198_l.size();
/*     */     
/*  36 */     if (index == 0)
/*     */     {
/*  38 */       return this.lanScanEntry;
/*     */     }
/*     */     
/*     */ 
/*  42 */     index--;
/*  43 */     return (GuiListExtended.IGuiListEntry)this.field_148199_m.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getSize()
/*     */   {
/*  50 */     return this.field_148198_l.size() + 1 + this.field_148199_m.size();
/*     */   }
/*     */   
/*     */   public void setSelectedSlotIndex(int selectedSlotIndexIn)
/*     */   {
/*  55 */     this.selectedSlotIndex = selectedSlotIndexIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isSelected(int slotIndex)
/*     */   {
/*  63 */     return slotIndex == this.selectedSlotIndex;
/*     */   }
/*     */   
/*     */   public int func_148193_k()
/*     */   {
/*  68 */     return this.selectedSlotIndex;
/*     */   }
/*     */   
/*     */   public void func_148195_a(ServerList p_148195_1_)
/*     */   {
/*  73 */     this.field_148198_l.clear();
/*     */     
/*  75 */     for (int i = 0; i < p_148195_1_.countServers(); i++)
/*     */     {
/*  77 */       this.field_148198_l.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_148194_a(List<LanServerDetector.LanServer> p_148194_1_)
/*     */   {
/*  83 */     this.field_148199_m.clear();
/*     */     
/*  85 */     for (LanServerDetector.LanServer lanserverdetector$lanserver : p_148194_1_)
/*     */     {
/*  87 */       this.field_148199_m.add(new ServerListEntryLanDetected(this.owner, lanserverdetector$lanserver));
/*     */     }
/*     */   }
/*     */   
/*     */   protected int getScrollBarX()
/*     */   {
/*  93 */     return super.getScrollBarX() + 30;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getListWidth()
/*     */   {
/* 101 */     return super.getListWidth() + 85;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\ServerSelectionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */