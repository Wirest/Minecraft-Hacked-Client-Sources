package net.minecraft.util;

public abstract interface IProgressUpdate
{
  public abstract void displaySavingString(String paramString);
  
  public abstract void resetProgressAndMessage(String paramString);
  
  public abstract void displayLoadingString(String paramString);
  
  public abstract void setLoadingProgress(int paramInt);
  
  public abstract void setDoneWorking();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IProgressUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */