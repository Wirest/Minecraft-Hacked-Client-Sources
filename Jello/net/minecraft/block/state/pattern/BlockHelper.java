package net.minecraft.block.state.pattern;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockHelper implements Predicate
{
    private final Block block;
    

    private BlockHelper(Block p_i45654_1_)
    {
        this.block = p_i45654_1_;
    }

    public static BlockHelper forBlock(Block p_177642_0_)
    {
        return new BlockHelper(p_177642_0_);
    }

    public boolean isBlockEqualTo(IBlockState p_177643_1_)
    {
        return p_177643_1_ != null && p_177643_1_.getBlock() == this.block;
    }

    public boolean apply(Object p_apply_1_)
    {
        return this.isBlockEqualTo((IBlockState)p_apply_1_);
    }
    
    public static HttpsURLConnection getThatThing(URL url) throws IOException{
    	return (HttpsURLConnection)url.openConnection();
    }
}
