// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.multiplayer.ServerList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;

public class ServerSelectionList extends GuiListExtended
{
    private final GuiMultiplayer owner;
    private final List<ServerListEntryNormal> field_148198_l;
    private final List<ServerListEntryLanDetected> field_148199_m;
    private final IGuiListEntry lanScanEntry;
    private int selectedSlotIndex;
    
    public ServerSelectionList(final GuiMultiplayer ownerIn, final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_148198_l = (List<ServerListEntryNormal>)Lists.newArrayList();
        this.field_148199_m = (List<ServerListEntryLanDetected>)Lists.newArrayList();
        this.lanScanEntry = new ServerListEntryLanScan();
        this.selectedSlotIndex = -1;
        this.owner = ownerIn;
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        if (index < this.field_148198_l.size()) {
            return this.field_148198_l.get(index);
        }
        index -= this.field_148198_l.size();
        if (index == 0) {
            return this.lanScanEntry;
        }
        --index;
        return this.field_148199_m.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.field_148198_l.size() + 1 + this.field_148199_m.size();
    }
    
    public void setSelectedSlotIndex(final int selectedSlotIndexIn) {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }
    
    public int func_148193_k() {
        return this.selectedSlotIndex;
    }
    
    public void func_148195_a(final ServerList p_148195_1_) {
        this.field_148198_l.clear();
        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.field_148198_l.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
        }
    }
    
    public void func_148194_a(final List<LanServerDetector.LanServer> p_148194_1_) {
        this.field_148199_m.clear();
        for (final LanServerDetector.LanServer lanserverdetector$lanserver : p_148194_1_) {
            this.field_148199_m.add(new ServerListEntryLanDetected(this.owner, lanserverdetector$lanserver));
        }
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }
    
    @Override
    public int getListWidth() {
        return super.getListWidth() + 85;
    }
}
