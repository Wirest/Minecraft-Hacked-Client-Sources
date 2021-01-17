package javazoom.jl.converter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Obuffer;

public class Converter {
   public synchronized void convert(String sourceName, String destName) throws JavaLayerException {
      this.convert((String)sourceName, destName, (Converter.ProgressListener)null, (Decoder.Params)null);
   }

   public synchronized void convert(String sourceName, String destName, Converter.ProgressListener progressListener) throws JavaLayerException {
      this.convert((String)sourceName, destName, progressListener, (Decoder.Params)null);
   }

   public void convert(String sourceName, String destName, Converter.ProgressListener progressListener, Decoder.Params decoderParams) throws JavaLayerException {
      if (destName.length() == 0) {
         destName = null;
      }

      try {
         InputStream in = this.openInput(sourceName);
         this.convert(in, destName, progressListener, decoderParams);
         in.close();
      } catch (IOException var6) {
         throw new JavaLayerException(var6.getLocalizedMessage(), var6);
      }
   }

   public synchronized void convert(InputStream sourceStream, String destName, Converter.ProgressListener progressListener, Decoder.Params decoderParams) throws JavaLayerException {
      if (progressListener == null) {
         progressListener = Converter.PrintWriterProgressListener.newStdOut(0);
      }

      try {
         if (!(sourceStream instanceof BufferedInputStream)) {
            sourceStream = new BufferedInputStream((InputStream)sourceStream);
         }

         int frameCount = -1;
         if (((InputStream)sourceStream).markSupported()) {
            ((InputStream)sourceStream).mark(-1);
            frameCount = this.countFrames((InputStream)sourceStream);
            ((InputStream)sourceStream).reset();
         }

         ((Converter.ProgressListener)progressListener).converterUpdate(1, frameCount, 0);
         Obuffer output = null;
         Decoder decoder = new Decoder(decoderParams);
         Bitstream stream = new Bitstream((InputStream)sourceStream);
         if (frameCount == -1) {
            frameCount = Integer.MAX_VALUE;
         }

         int frame = 0;
         long startTime = System.currentTimeMillis();

         try {
            for(; frame < frameCount; ++frame) {
               try {
                  Header header = stream.readFrame();
                  if (header == null) {
                     break;
                  }

                  ((Converter.ProgressListener)progressListener).readFrame(frame, header);
                  if (output == null) {
                     int channels = header.mode() == 3 ? 1 : 2;
                     int freq = header.frequency();
                     output = new WaveFileObuffer(channels, freq, destName);
                     decoder.setOutputBuffer(output);
                  }

                  Obuffer decoderOutput = decoder.decodeFrame(header, stream);
                  if (decoderOutput != output) {
                     throw new InternalError("Output buffers are different.");
                  }

                  ((Converter.ProgressListener)progressListener).decodedFrame(frame, header, output);
                  stream.closeFrame();
               } catch (Exception var19) {
                  boolean stop = !((Converter.ProgressListener)progressListener).converterException(var19);
                  if (stop) {
                     throw new JavaLayerException(var19.getLocalizedMessage(), var19);
                  }
               }
            }
         } finally {
            if (output != null) {
               output.close();
            }

         }

         int time = (int)(System.currentTimeMillis() - startTime);
         ((Converter.ProgressListener)progressListener).converterUpdate(2, time, frame);
      } catch (IOException var21) {
         throw new JavaLayerException(var21.getLocalizedMessage(), var21);
      }
   }

   protected int countFrames(InputStream in) {
      return -1;
   }

   protected InputStream openInput(String fileName) throws IOException {
      File file = new File(fileName);
      InputStream fileIn = new FileInputStream(file);
      BufferedInputStream bufIn = new BufferedInputStream(fileIn);
      return bufIn;
   }

   public static class PrintWriterProgressListener implements Converter.ProgressListener {
      public static final int NO_DETAIL = 0;
      public static final int EXPERT_DETAIL = 1;
      public static final int VERBOSE_DETAIL = 2;
      public static final int DEBUG_DETAIL = 7;
      public static final int MAX_DETAIL = 10;
      private PrintWriter pw;
      private int detailLevel;

      public static Converter.PrintWriterProgressListener newStdOut(int detail) {
         return new Converter.PrintWriterProgressListener(new PrintWriter(System.out, true), detail);
      }

      public PrintWriterProgressListener(PrintWriter writer, int detailLevel) {
         this.pw = writer;
         this.detailLevel = detailLevel;
      }

      public boolean isDetail(int detail) {
         return this.detailLevel >= detail;
      }

      public void converterUpdate(int updateID, int param1, int param2) {
         if (this.isDetail(2)) {
            switch(updateID) {
            case 2:
               if (param2 == 0) {
                  param2 = 1;
               }

               this.pw.println();
               this.pw.println("Converted " + param2 + " frames in " + param1 + " ms (" + param1 / param2 + " ms per frame.)");
            }
         }

      }

      public void parsedFrame(int frameNo, Header header) {
         String headerString;
         if (frameNo == 0 && this.isDetail(2)) {
            headerString = header.toString();
            this.pw.println("File is a " + headerString);
         } else if (this.isDetail(10)) {
            headerString = header.toString();
            this.pw.println("Prased frame " + frameNo + ": " + headerString);
         }

      }

      public void readFrame(int frameNo, Header header) {
         String headerString;
         if (frameNo == 0 && this.isDetail(2)) {
            headerString = header.toString();
            this.pw.println("File is a " + headerString);
         } else if (this.isDetail(10)) {
            headerString = header.toString();
            this.pw.println("Read frame " + frameNo + ": " + headerString);
         }

      }

      public void decodedFrame(int frameNo, Header header, Obuffer o) {
         if (this.isDetail(10)) {
            String headerString = header.toString();
            this.pw.println("Decoded frame " + frameNo + ": " + headerString);
            this.pw.println("Output: " + o);
         } else if (this.isDetail(2)) {
            if (frameNo == 0) {
               this.pw.print("Converting.");
               this.pw.flush();
            }

            if (frameNo % 10 == 0) {
               this.pw.print('.');
               this.pw.flush();
            }
         }

      }

      public boolean converterException(Throwable t) {
         if (this.detailLevel > 0) {
            t.printStackTrace(this.pw);
            this.pw.flush();
         }

         return false;
      }
   }

   public interface ProgressListener {
      int UPDATE_FRAME_COUNT = 1;
      int UPDATE_CONVERT_COMPLETE = 2;

      void converterUpdate(int var1, int var2, int var3);

      void parsedFrame(int var1, Header var2);

      void readFrame(int var1, Header var2);

      void decodedFrame(int var1, Header var2, Obuffer var3);

      boolean converterException(Throwable var1);
   }
}
