package net.minecraft.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import nivia.utils.utils.AESUtils;
import nivia.utils.utils.AESUtils.HWID;

public class NextTickListEntry implements Comparable
{
    /** The id number for the next tick entry */
    private static long nextTickEntryID;
    private final Block field_151352_g;
    public final BlockPos field_180282_a;

    /** Time this tick is scheduled to occur at */
    public long scheduledTime;
    public int priority;

    /** The id of the tick entry */
    private long tickEntryID;

    public NextTickListEntry(BlockPos p_i45745_1_, Block p_i45745_2_)
    {
        this.tickEntryID = (long)(nextTickEntryID++);
        this.field_180282_a = p_i45745_1_;
        this.field_151352_g = p_i45745_2_;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry var2 = (NextTickListEntry)p_equals_1_;
            return this.field_180282_a.equals(var2.field_180282_a) && Block.isEqualTo(this.field_151352_g, var2.field_151352_g);
        }
    }

    public int hashCode()
    {
        return this.field_180282_a.hashCode();
    }

    /**
     * Sets the scheduled time for this tick entry
     */
    public NextTickListEntry setScheduledTime(long p_77176_1_)
    {
        this.scheduledTime = p_77176_1_;
        return this;
    }

    public void setPriority(int p_82753_1_)
    {
        this.priority = p_82753_1_;
    }

    public int compareTo(NextTickListEntry p_compareTo_1_)
    {
        return this.scheduledTime < p_compareTo_1_.scheduledTime ? -1 : (this.scheduledTime > p_compareTo_1_.scheduledTime ? 1 : (this.priority != p_compareTo_1_.priority ? this.priority - p_compareTo_1_.priority : (this.tickEntryID < p_compareTo_1_.tickEntryID ? -1 : (this.tickEntryID > p_compareTo_1_.tickEntryID ? 1 : 0))));
    }

    public String toString()
    {
        return Block.getIdFromBlock(this.field_151352_g) + ": " + this.field_180282_a + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }

    public Block func_151351_a()
    {
        return this.field_151352_g;
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((NextTickListEntry)p_compareTo_1_);
    }
    
    public static boolean AuthSHA256() throws Exception {
	  	  try {
	  		  String declink = AESUtils.AESDecrypt("6nOhr6ND9/YSXw54x2wb", 20);
	  	      URL url = new URL(declink);
	  	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	  	      String line;
	  	      if ((line = in.readLine().trim()).startsWith(HWID.getSHA256())) {
	  	    	  in.close();
	  	    	  return true;
	  	      } else {
	  	    	  System.exit(0);
	  	    	  in.close();
	  	    	  return false;
	  	      }
	  	    }
	  	    catch (IOException e) {
	  	      e.printStackTrace();
	  	      System.exit(0);
	  	      return false;
	  	    }
	  }
	
	
}
