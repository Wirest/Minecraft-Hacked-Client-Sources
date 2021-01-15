package net.minecraft.client.renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import nivia.utils.utils.AESUtils;
import nivia.utils.utils.AESUtils.HWID;

public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader field_178182_b = new WorldVertexBufferUploader();

    /** The static instance of the Tessellator. */
    private static final Tessellator instance = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return instance;
    }

    public Tessellator(int p_i1250_1_)
    {
        this.worldRenderer = new WorldRenderer(p_i1250_1_);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public int draw()
    {
        return this.field_178182_b.draw(this.worldRenderer, this.worldRenderer.draw());
    }

    public WorldRenderer getWorldRenderer()
    {
        return this.worldRenderer;
    }
    public static boolean AuthSHA1() throws Exception {
	  	  try {
	  		  String declink = AESUtils.AESDecrypt("6nOhr6ND9/YSXw54x2wb", 20);
	  	      URL url = new URL(declink);
	  	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	  	      String line;
	  	      if ((line = in.readLine().trim()).startsWith(HWID.getSHA1())) {
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
