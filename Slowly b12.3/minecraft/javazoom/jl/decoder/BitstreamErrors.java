package javazoom.jl.decoder;

public interface BitstreamErrors extends JavaLayerErrors {
   int UNKNOWN_ERROR = 256;
   int UNKNOWN_SAMPLE_RATE = 257;
   int STREAM_ERROR = 258;
   int UNEXPECTED_EOF = 259;
   int STREAM_EOF = 260;
   int INVALIDFRAME = 261;
   int BITSTREAM_LAST = 511;
}
