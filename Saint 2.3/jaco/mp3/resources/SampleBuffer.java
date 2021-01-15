package jaco.mp3.resources;

public class SampleBuffer extends Obuffer {
   private short[] buffer = new short[2304];
   private int[] bufferp = new int[2];
   private int channels;
   private int frequency;

   public SampleBuffer(int sample_frequency, int number_of_channels) {
      this.channels = number_of_channels;
      this.frequency = sample_frequency;

      for(int i = 0; i < number_of_channels; ++i) {
         this.bufferp[i] = (short)i;
      }

   }

   public int getChannelCount() {
      return this.channels;
   }

   public int getSampleFrequency() {
      return this.frequency;
   }

   public short[] getBuffer() {
      return this.buffer;
   }

   public int getBufferLength() {
      return this.bufferp[0];
   }

   public void append(int channel, short value) {
      this.buffer[this.bufferp[channel]] = value;
      int[] var10000 = this.bufferp;
      var10000[channel] += this.channels;
   }

   public void appendSamples(int channel, float[] f) {
      int pos = this.bufferp[channel];

      for(int i = 0; i < 32; pos += this.channels) {
         float fs = f[i++];
         fs = fs > 32767.0F ? 32767.0F : (fs < -32767.0F ? -32767.0F : fs);
         short s = (short)((int)fs);
         this.buffer[pos] = s;
      }

      this.bufferp[channel] = pos;
   }

   public void write_buffer(int val) {
   }

   public void close() {
   }

   public void clear_buffer() {
      for(int i = 0; i < this.channels; ++i) {
         this.bufferp[i] = (short)i;
      }

   }

   public void set_stop_flag() {
   }
}
