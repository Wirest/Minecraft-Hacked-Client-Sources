package me.slowly.client.util;

public class ColorObject {
	public int red;
	  public int green;
	  public int blue;
	  public int alpha;
	  
	  public ColorObject(int red, int green, int blue, int alpha)
	  {
	    this.red = red;
	    this.green = green;
	    this.blue = blue;
	    this.alpha = alpha;
	  }
	  
	  public int getRed()
	  {
	    return this.red;
	  }
	  
	  public int getGreen()
	  {
	    return this.green;
	  }
	  
	  public int getBlue()
	  {
	    return this.blue;
	  }
	  
	  public int getAlpha()
	  {
	    return this.alpha;
	  }
	  
	  public void setRed(int red)
	  {
	    this.red = red;
	  }
	  
	  public void setGreen(int green)
	  {
	    this.green = green;
	  }
	  
	  public void setBlue(int blue)
	  {
	    this.blue = blue;
	  }
	  
	  public void setAlpha(int alpha)
	  {
	    this.alpha = alpha;
	  }
	  
	  public int getColorInt()
	  {
	    return Colors2.getColor(this.red, this.green, this.blue, this.alpha);
	  }
	  
	  public void updateColors(int red, int green, int blue, int alpha)
	  {
	    this.red = red;
	    this.green = green;
	    this.blue = blue;
	    this.alpha = alpha;
	  }

}
