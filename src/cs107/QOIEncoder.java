package src.cs107;

/**
 * "Quite Ok Image" Encoder
 * @apiNote Second task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIEncoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIEncoder(){}

    // ==================================================================================
    // ============================ QUITE OK IMAGE HEADER ===============================
    // ==================================================================================

    /**
     * Generate a "Quite Ok Image" header using the following parameters
     * @param image (Helper.Image) - Image to use
     * @throws AssertionError if the colorspace or the number of channels is corrupted or if the image is null.
     *  (See the "Quite Ok Image" Specification or the handouts of the project for more information)
     * @return (byte[]) - Corresponding "Quite Ok Image" Header
     */
    public static byte[] qoiHeader(Helper.Image image){

      /*
      // define a QOI image format header and return it
      // the header is a byte array of 16 bytes
      // the first 4 bytes are the magic number 0x51 0x4F 0x49 0x01
      // the next 4 bytes are the width of the image
      // the next 4 bytes are the height of the image
      // the next 4 bytes are the number of channels in the image
      // ingore the colorspace

      // check if the image is null
      if (image == null) {
        throw new AssertionError("Image is null");
      }

      // check if the number of channels is corrupted
      if (image.channels() < 1 || image.channels() > 4) {
        throw new AssertionError("Number of channels is corrupted");
      }

      // check if the color space is corrupted
      if (image.color_space() != 0) {
        throw new AssertionError("Color space is corrupted");
      }

      // create the header
      byte[] header = new byte[16];
      header[0] = 0x51;
      header[1] = 0x4F;
      header[2] = 0x49;
      header[3] = 0x01;

      // width
      header[4] = (byte) (image.width >> 24);
      header[5] = (byte) (image.width >> 16);
      header[6] = (byte) (image.width >> 8);
      header[7] = (byte) (image.width);

      // height
      header[8] = (byte) (image.height >> 24);
      header[9] = (byte) (image.height >> 16);
      header[10] = (byte) (image.height >> 8);
      header[11] = (byte) (image.height);

      // channels
      header[12] = (byte) (image.channels() >> 24);
      header[13] = (byte) (image.channels() >> 16);
      header[14] = (byte) (image.channels() >> 8);
      header[15] = (byte) (image.channels());

      return header;
      */

      return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ============================ ATOMIC ENCODING METHODS =============================
    // ==================================================================================

    /**
     * Encode the given pixel using the QOI_OP_RGB schema
     * @param pixel (byte[]) - The Pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) - Encoding of the pixel using the QOI_OP_RGB schema
     */
    public static byte[] qoiOpRGB(byte[] pixel){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_DIFF schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpDiff(byte[] diff){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_LUMA schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints
     * or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpLuma(byte[] diff){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the number of similar pixels using the QOI_OP_RUN schema
     * @param count (byte) - Number of similar pixels
     * @throws AssertionError if count is not between 0 (exclusive) and 63 (exclusive)
     * @return (byte[]) - Encoding of count
     */
    public static byte[] qoiOpRun(byte count){
        return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ============================== GLOBAL ENCODING METHODS  ==========================
    // ==================================================================================

    /**
     * Encode the given image using the "Quite Ok Image" Protocol
     * (See handout for more information about the "Quite Ok Image" protocol)
     * @param image (byte[][]) - Formatted image to encode
     * @return (byte[]) - "Quite Ok Image" representation of the image
     */
    public static byte[] encodeData(byte[][] image){
        return Helper.fail("Not Implemented");
    }

    /**
     * Creates the representation in memory of the "Quite Ok Image" file.
     * @apiNote THE FILE IS NOT CREATED YET, THIS IS JUST ITS REPRESENTATION.
     * TO CREATE THE FILE, YOU'LL NEED TO CALL Helper::write
     * @param image (Helper.Image) - Image to encode
     * @return (byte[]) - Binary representation of the "Quite Ok File" of the image
     * @throws AssertionError if the image is null
     */
    public static byte[] qoiFile(Helper.Image image){
        return Helper.fail("Not Implemented");
    }

}