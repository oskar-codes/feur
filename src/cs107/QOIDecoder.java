package src.cs107;

import static src.cs107.Helper.Image;

/**
 * "Quite Ok Image" Decoder
 * @apiNote Third task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIDecoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIDecoder(){}

    // ==================================================================================
    // =========================== QUITE OK IMAGE HEADER ================================
    // ==================================================================================

    /**
     * Extract useful information from the "Quite Ok Image" header
     * @param header (byte[]) - A "Quite Ok Image" header
     * @return (int[]) - Array such as its content is {width, height, channels, color space}
     * @throws AssertionError See handouts section 6.1
     */
    public static int[] decodeHeader(byte[] header){
      assert header != null : "Empty Header";
      assert header.length == QOISpecification.HEADER_SIZE : "Invalid header size";
      
      byte[][] parts = ArrayUtils.partition(header, 4,4,4,1,1);
      assert ArrayUtils.equals(QOISpecification.QOI_MAGIC, parts[0]) : "Invalid magic number";

      byte channels = parts[3][0];
      byte colorSpace = parts[4][0];

      assert channels == QOISpecification.RGB || channels == QOISpecification.RGBA;
      assert colorSpace == QOISpecification.sRGB || colorSpace == QOISpecification.ALL;
      
      int[] decodedHeader =  new int[4];
      decodedHeader[0] = ArrayUtils.toInt(parts[1]);
      decodedHeader[1] = ArrayUtils.toInt(parts[2]);
      decodedHeader[2] += (int)channels;
      decodedHeader[3] += (int)colorSpace;
      
      return decodedHeader;
    }

    // ==================================================================================
    // =========================== ATOMIC DECODING METHODS ==============================
    // ==================================================================================

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param alpha (byte) - Alpha component of the pixel
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.1
     */
    public static int decodeQoiOpRGB(byte[][] buffer, byte[] input, byte alpha, int position, int idx){
      assert buffer != null : "Invalid buffer size";
      assert input != null : "Invalid input size";
      assert position >= 0 && position < buffer.length : "Invalid position in buffers";
      assert idx >= 0 && idx < input.length - 3 : "Invalid position in input";
      for (int i = 0; i < 3; i++) {
          buffer[position][i] = input[idx + i]; 
      }
      buffer[position][3] = alpha;
      return 3;
    }

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.2
     */
    public static int decodeQoiOpRGBA(byte[][] buffer, byte[] input, int position, int idx){
      assert buffer != null : "Empty buffer";
      assert input != null : "Empty input";
      assert position >= 0 && position < buffer.length : "Invalid position";
      assert idx >= 0 && idx < input.length - 4 : "Invalid index or input too short";

      buffer[position] = ArrayUtils.extract(input, idx, 4);

      return 4;
    }

    /**
     * Create a new pixel following the "QOI_OP_DIFF" schema.
     * @param previousPixel (byte[]) - The previous pixel
     * @param chunk (byte) - A "QOI_OP_DIFF" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.4
     */
    public static byte[] decodeQoiOpDiff(byte[] previousPixel, byte chunk){
      assert previousPixel != null : "previousPixel is null";
      assert previousPixel.length == 4 : "Invalid previousPixel length";
      assert (chunk >> 6) << 6 == QOISpecification.QOI_OP_DIFF_TAG : "Invalid method";
      byte dr = (byte) (((chunk & 0b00_11_00_00) >> 4) - 2);
      byte dg = (byte) (((chunk & 0b00_00_11_00) >> 2) - 2);
      byte db = (byte) ((chunk & 0b00_00_00_11) - 2);
      previousPixel[0] += dr;
      previousPixel[1] += dg;
      previousPixel[2] += db;
      return previousPixel;
    }

    /**
     * Create a new pixel following the "QOI_OP_LUMA" schema
     * @param previousPixel (byte[]) - The previous pixel
     * @param data (byte[]) - A "QOI_OP_LUMA" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.5
     */
    public static byte[] decodeQoiOpLuma(byte[] previousPixel, byte[] data){
      assert previousPixel != null : "previousPixel is null";
      assert data != null : "data is null";
      assert previousPixel.length == 4 : "Invalid previousPixel length";
      assert (data[0] >> 6) << 6 == QOISpecification.QOI_OP_LUMA_TAG : "Invalid method";
      byte dg = (byte) ((data[0] & 0b00_11_11_11) - 32) ;
      byte dr =  (byte) (((data[1] & 0b11_11_00_00) >> 4) + dg - 8);
      byte db =  (byte) ((data[1] & 0b00_00_11_11) + dg - 8);

      previousPixel[0] += dr;
      previousPixel[1] += dg; 
      previousPixel[2] += db;

      return previousPixel;
    }

    /**
     * Store the given pixel in the buffer multiple times
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param pixel (byte[]) - The pixel to store
     * @param chunk (byte) - a QOI_OP_RUN data chunk
     * @param position (int) - Index in buffer to start writing from
     * @return (int) - number of written pixels in buffer
     * @throws AssertionError See handouts section 6.2.6
     */
    public static int decodeQoiOpRun(byte[][] buffer, byte[] pixel, byte chunk, int position){
      int repetitions = (chunk & 0b00_11_11_11) + 1;
      
      for(int i = 0; i < repetitions; i++) {
        buffer[position + i] = pixel;
      }

      return repetitions - 1;
    }

    // ==================================================================================
    // ========================= GLOBAL DECODING METHODS ================================
    // ==================================================================================

    /**
     * Decode the given data using the "Quite Ok Image" Protocol
     * @param data (byte[]) - Data to decode
     * @param width (int) - The width of the expected output
     * @param height (int) - The height of the expected output
     * @return (byte[][]) - Decoded "Quite Ok Image"
     * @throws AssertionError See handouts section 6.3
     */
    public static byte[][] decodeData(byte[] data, int width, int height){
      byte[] previousPixel = QOISpecification.START_PIXEL;
      byte[][] buffer = new byte[width * height][4];
      byte[][] hashTable = new byte[64][4];
      int position = 0;

      for (byte chunk : data) {
        switch (chunk) {
          case QOISpecification.QOI_OP_RGB_TAG:
            position += decodeQoiOpRGB(buffer, data, (byte) 0xFF, position, 0);
            break;
          case QOISpecification.QOI_OP_RGBA_TAG:
            position += decodeQoiOpRGBA(buffer, data, position, 0);
            break;
          case QOISpecification.QOI_OP_DIFF_TAG:
            previousPixel = decodeQoiOpDiff(previousPixel, chunk);
            buffer[position] = previousPixel;
            position++;
            break;
          case QOISpecification.QOI_OP_LUMA_TAG:
            previousPixel = decodeQoiOpLuma(previousPixel, ArrayUtils.extract(data, 0, 2));
            buffer[position] = previousPixel;
            position++;
            break;
          case QOISpecification.QOI_OP_RUN_TAG:
            position += decodeQoiOpRun(buffer, previousPixel, chunk, position);
            break;
          default:
            if (chunk >= 0 && chunk <= 63) {
              buffer[position] = hashTable[chunk];
              position++;
            } else if (chunk >= 64 && chunk <= 127) {
              byte[] pixel = hashTable[chunk - 64];
              position += decodeQoiOpRun(buffer, pixel, chunk, position);
            } else if (chunk >= -64 && chunk <= -1) {
              byte[] pixel = hashTable[chunk + 64];
              position += decodeQoiOpRun(buffer, pixel, chunk, position);
            } else {
              assert false : "Invalid chunk";
            }
        }
      }




      return buffer;
    }

    /**
     * Decode a file using the "Quite Ok Image" Protocol
     * @param content (byte[]) - Content of the file to decode
     * @return (Image) - Decoded image
     * @throws AssertionError if content is null
     */
    public static Image decodeQoiFile(byte[] content){
      return Helper.fail("Not Implemented");
    }

}