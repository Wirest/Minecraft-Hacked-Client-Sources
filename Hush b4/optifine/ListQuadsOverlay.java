// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.init.Blocks;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

public class ListQuadsOverlay
{
    private List<BakedQuad> listQuads;
    private List<IBlockState> listBlockStates;
    private List<BakedQuad> listQuadsSingle;
    
    public ListQuadsOverlay() {
        this.listQuads = new ArrayList<BakedQuad>();
        this.listBlockStates = new ArrayList<IBlockState>();
        this.listQuadsSingle = Arrays.asList(new BakedQuad[1]);
    }
    
    public void addQuad(final BakedQuad p_addQuad_1_, final IBlockState p_addQuad_2_) {
        this.listQuads.add(p_addQuad_1_);
        this.listBlockStates.add(p_addQuad_2_);
    }
    
    public int size() {
        return this.listQuads.size();
    }
    
    public BakedQuad getQuad(final int p_getQuad_1_) {
        return this.listQuads.get(p_getQuad_1_);
    }
    
    public IBlockState getBlockState(final int p_getBlockState_1_) {
        return (p_getBlockState_1_ >= 0 && p_getBlockState_1_ < this.listBlockStates.size()) ? this.listBlockStates.get(p_getBlockState_1_) : Blocks.air.getDefaultState();
    }
    
    public List<BakedQuad> getListQuadsSingle(final BakedQuad p_getListQuadsSingle_1_) {
        this.listQuadsSingle.set(0, p_getListQuadsSingle_1_);
        return this.listQuadsSingle;
    }
    
    public void clear() {
        this.listQuads.clear();
        this.listBlockStates.clear();
    }
}
