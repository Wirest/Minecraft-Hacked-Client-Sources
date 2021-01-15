package io.netty.handler.codec.http.multipart;

public abstract interface FileUpload
  extends HttpData
{
  public abstract String getFilename();
  
  public abstract void setFilename(String paramString);
  
  public abstract void setContentType(String paramString);
  
  public abstract String getContentType();
  
  public abstract void setContentTransferEncoding(String paramString);
  
  public abstract String getContentTransferEncoding();
  
  public abstract FileUpload copy();
  
  public abstract FileUpload duplicate();
  
  public abstract FileUpload retain();
  
  public abstract FileUpload retain(int paramInt);
  
  public abstract FileUpload touch();
  
  public abstract FileUpload touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\FileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */