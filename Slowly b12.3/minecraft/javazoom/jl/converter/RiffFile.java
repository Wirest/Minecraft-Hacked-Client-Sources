package javazoom.jl.converter;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RiffFile {
   public static final int DDC_SUCCESS = 0;
   public static final int DDC_FAILURE = 1;
   public static final int DDC_OUT_OF_MEMORY = 2;
   public static final int DDC_FILE_ERROR = 3;
   public static final int DDC_INVALID_CALL = 4;
   public static final int DDC_USER_ABORT = 5;
   public static final int DDC_INVALID_FILE = 6;
   public static final int RFM_UNKNOWN = 0;
   public static final int RFM_WRITE = 1;
   public static final int RFM_READ = 2;
   private RiffFile.RiffChunkHeader riff_header = new RiffFile.RiffChunkHeader();
   protected int fmode = 0;
   protected RandomAccessFile file = null;

   public RiffFile() {
      this.riff_header.ckID = FourCC("RIFF");
      this.riff_header.ckSize = 0;
   }

   public int CurrentFileMode() {
      return this.fmode;
   }

   public int Open(String Filename, int NewMode) {
      int retcode = 0;
      if (this.fmode != 0) {
         retcode = this.Close();
      }

      if (retcode == 0) {
         byte[] br;
         switch(NewMode) {
         case 1:
            try {
               this.file = new RandomAccessFile(Filename, "rw");

               try {
                  br = new byte[8];
                  br[0] = (byte)(this.riff_header.ckID >>> 24 & 255);
                  br[1] = (byte)(this.riff_header.ckID >>> 16 & 255);
                  br[2] = (byte)(this.riff_header.ckID >>> 8 & 255);
                  br[3] = (byte)(this.riff_header.ckID & 255);
                  byte br4 = (byte)(this.riff_header.ckSize >>> 24 & 255);
                  byte br5 = (byte)(this.riff_header.ckSize >>> 16 & 255);
                  byte br6 = (byte)(this.riff_header.ckSize >>> 8 & 255);
                  byte br7 = (byte)(this.riff_header.ckSize & 255);
                  br[4] = br7;
                  br[5] = br6;
                  br[6] = br5;
                  br[7] = br4;
                  this.file.write(br, 0, 8);
                  this.fmode = 1;
               } catch (IOException var11) {
                  this.file.close();
                  this.fmode = 0;
               }
            } catch (IOException var12) {
               this.fmode = 0;
               retcode = 3;
            }
            break;
         case 2:
            try {
               this.file = new RandomAccessFile(Filename, "r");

               try {
                  br = new byte[8];
                  this.file.read(br, 0, 8);
                  this.fmode = 2;
                  this.riff_header.ckID = br[0] << 24 & -16777216 | br[1] << 16 & 16711680 | br[2] << 8 & '\uff00' | br[3] & 255;
                  this.riff_header.ckSize = br[4] << 24 & -16777216 | br[5] << 16 & 16711680 | br[6] << 8 & '\uff00' | br[7] & 255;
               } catch (IOException var9) {
                  this.file.close();
                  this.fmode = 0;
               }
            } catch (IOException var10) {
               this.fmode = 0;
               retcode = 3;
            }
            break;
         default:
            retcode = 4;
         }
      }

      return retcode;
   }

   public int Write(byte[] Data, int NumBytes) {
      if (this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(Data, 0, NumBytes);
            this.fmode = 1;
         } catch (IOException var4) {
            return 3;
         }

         this.riff_header.ckSize += NumBytes;
         return 0;
      }
   }

   public int Write(short[] Data, int NumBytes) {
      byte[] theData = new byte[NumBytes];
      int yc = 0;

      for(int y = 0; y < NumBytes; y += 2) {
         theData[y] = (byte)(Data[yc] & 255);
         theData[y + 1] = (byte)(Data[yc++] >>> 8 & 255);
      }

      if (this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(theData, 0, NumBytes);
            this.fmode = 1;
         } catch (IOException var6) {
            return 3;
         }

         this.riff_header.ckSize += NumBytes;
         return 0;
      }
   }

   public int Write(RiffFile.RiffChunkHeader Triff_header, int NumBytes) {
      byte[] br = new byte[8];
      br[0] = (byte)(Triff_header.ckID >>> 24 & 255);
      br[1] = (byte)(Triff_header.ckID >>> 16 & 255);
      br[2] = (byte)(Triff_header.ckID >>> 8 & 255);
      br[3] = (byte)(Triff_header.ckID & 255);
      byte br4 = (byte)(Triff_header.ckSize >>> 24 & 255);
      byte br5 = (byte)(Triff_header.ckSize >>> 16 & 255);
      byte br6 = (byte)(Triff_header.ckSize >>> 8 & 255);
      byte br7 = (byte)(Triff_header.ckSize & 255);
      br[4] = br7;
      br[5] = br6;
      br[6] = br5;
      br[7] = br4;
      if (this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(br, 0, NumBytes);
            this.fmode = 1;
         } catch (IOException var9) {
            return 3;
         }

         this.riff_header.ckSize += NumBytes;
         return 0;
      }
   }

   public int Write(short Data, int NumBytes) {
      short theData = (short)(Data >>> 8 & 255 | Data << 8 & '\uff00');
      if (this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.writeShort(theData);
            this.fmode = 1;
         } catch (IOException var5) {
            return 3;
         }

         this.riff_header.ckSize += NumBytes;
         return 0;
      }
   }

   public int Write(int Data, int NumBytes) {
      short theDataL = (short)(Data >>> 16 & '\uffff');
      short theDataR = (short)(Data & '\uffff');
      short theDataLI = (short)(theDataL >>> 8 & 255 | theDataL << 8 & '\uff00');
      short theDataRI = (short)(theDataR >>> 8 & 255 | theDataR << 8 & '\uff00');
      int theData = theDataRI << 16 & -65536 | theDataLI & '\uffff';
      if (this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.writeInt(theData);
            this.fmode = 1;
         } catch (IOException var9) {
            return 3;
         }

         this.riff_header.ckSize += NumBytes;
         return 0;
      }
   }

   public int Read(byte[] Data, int NumBytes) {
      byte retcode = 0;

      try {
         this.file.read(Data, 0, NumBytes);
      } catch (IOException var5) {
         retcode = 3;
      }

      return retcode;
   }

   public int Expect(String Data, int NumBytes )
   {
      byte	target = 0;
	  int	cnt = 0;
   	  try
 	  {
	  	   while ((NumBytes--) != 0)
      	   {
    	 		target = file.readByte();
				if (target != Data.charAt(cnt++)) return DDC_FILE_ERROR;
		   }			
      } catch (IOException ioe)
 		 {
 		   return DDC_FILE_ERROR;
		 }   
   		return DDC_SUCCESS;
   }

   public int Close() {
      int retcode = 0;
      switch(this.fmode) {
      case 1:
         try {
            this.file.seek(0L);

            try {
               byte[] br = new byte[]{(byte)(this.riff_header.ckID >>> 24 & 255), (byte)(this.riff_header.ckID >>> 16 & 255), (byte)(this.riff_header.ckID >>> 8 & 255), (byte)(this.riff_header.ckID & 255), (byte)(this.riff_header.ckSize & 255), (byte)(this.riff_header.ckSize >>> 8 & 255), (byte)(this.riff_header.ckSize >>> 16 & 255), (byte)(this.riff_header.ckSize >>> 24 & 255)};
               this.file.write(br, 0, 8);
               this.file.close();
            } catch (IOException var4) {
               retcode = 3;
            }
         } catch (IOException var5) {
            retcode = 3;
         }
         break;
      case 2:
         try {
            this.file.close();
         } catch (IOException var3) {
            retcode = 3;
         }
      }

      this.file = null;
      this.fmode = 0;
      return retcode;
   }

   public long CurrentFilePosition() {
      long position;
      try {
         position = this.file.getFilePointer();
      } catch (IOException var4) {
         position = -1L;
      }

      return position;
   }

   public int Backpatch(long FileOffset, RiffFile.RiffChunkHeader Data, int NumBytes) {
      if (this.file == null) {
         return 4;
      } else {
         try {
            this.file.seek(FileOffset);
         } catch (IOException var6) {
            return 3;
         }

         return this.Write(Data, NumBytes);
      }
   }

   public int Backpatch(long FileOffset, byte[] Data, int NumBytes) {
      if (this.file == null) {
         return 4;
      } else {
         try {
            this.file.seek(FileOffset);
         } catch (IOException var6) {
            return 3;
         }

         return this.Write(Data, NumBytes);
      }
   }

   protected int Seek(long offset) {
      byte rc;
      try {
         this.file.seek(offset);
         rc = 0;
      } catch (IOException var5) {
         rc = 3;
      }

      return rc;
   }

   private String DDCRET_String(int retcode) {
      switch(retcode) {
      case 0:
         return "DDC_SUCCESS";
      case 1:
         return "DDC_FAILURE";
      case 2:
         return "DDC_OUT_OF_MEMORY";
      case 3:
         return "DDC_FILE_ERROR";
      case 4:
         return "DDC_INVALID_CALL";
      case 5:
         return "DDC_USER_ABORT";
      case 6:
         return "DDC_INVALID_FILE";
      default:
         return "Unknown Error";
      }
   }

   public static int FourCC(String ChunkName) {
      byte[] p = new byte[]{32, 32, 32, 32};
      ChunkName.getBytes(0, 4, p, 0);
      int ret = p[0] << 24 & -16777216 | p[1] << 16 & 16711680 | p[2] << 8 & '\uff00' | p[3] & 255;
      return ret;
   }

   class RiffChunkHeader {
      public int ckID = 0;
      public int ckSize = 0;
   }
}
