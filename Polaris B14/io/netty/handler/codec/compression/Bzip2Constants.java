package io.netty.handler.codec.compression;

final class Bzip2Constants
{
  static final int MAGIC_NUMBER = 4348520;
  static final int BLOCK_HEADER_MAGIC_1 = 3227993;
  static final int BLOCK_HEADER_MAGIC_2 = 2511705;
  static final int END_OF_STREAM_MAGIC_1 = 1536581;
  static final int END_OF_STREAM_MAGIC_2 = 3690640;
  static final int BASE_BLOCK_SIZE = 100000;
  static final int MIN_BLOCK_SIZE = 1;
  static final int MAX_BLOCK_SIZE = 9;
  static final int HUFFMAN_MAX_ALPHABET_SIZE = 258;
  static final int HUFFMAN_ENCODE_MAX_CODE_LENGTH = 20;
  static final int HUFFMAN_DECODE_MAX_CODE_LENGTH = 23;
  static final int HUFFMAN_SYMBOL_RUNA = 0;
  static final int HUFFMAN_SYMBOL_RUNB = 1;
  static final int HUFFMAN_SYMBOL_RANGE_SIZE = 16;
  static final int HUFFMAN_SELECTOR_LIST_MAX_LENGTH = 6;
  static final int HUFFMAN_GROUP_RUN_LENGTH = 50;
  static final int MAX_SELECTORS = 18002;
  static final int HUFFMAN_MINIMUM_TABLES = 2;
  static final int HUFFMAN_MAXIMUM_TABLES = 6;
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\Bzip2Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */