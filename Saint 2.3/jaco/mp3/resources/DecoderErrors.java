package jaco.mp3.resources;

public interface DecoderErrors extends JavaLayerErrors {
   int UNKNOWN_ERROR = 512;
   int UNSUPPORTED_LAYER = 513;
   int ILLEGAL_SUBBAND_ALLOCATION = 514;
}
