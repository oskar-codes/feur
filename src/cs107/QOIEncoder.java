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
    public static byte[] qoiHeader(Helper.Image image) {
      assert image != null : "Input is null";
      assert image.channels() == QOISpecification.RGB || image.channels() == QOISpecification.RGBA : "Number of channels is corrupted";
      assert image.color_space() == QOISpecification.sRGB || image.color_space() == QOISpecification.ALL : "Color space is corrupted";

      byte[] header = new byte[14];
      header[0] = 'q';
      header[1] = 'o';
      header[2] = 'i';
      header[3] = 'f';

      int width = image.data()[0].length;
      header[4] = (byte) (width >> 24);
      header[5] = (byte) (width >> 16);
      header[6] = (byte) (width >> 8);
      header[7] = (byte) (width);

      int height = image.data().length;
      header[8] = (byte) (height >> 24);
      header[9] = (byte) (height >> 16);
      header[10] = (byte) (height >> 8);
      header[11] = (byte) (height);

      header[12] = image.channels();
      header[13] = image.color_space();

      return header;
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
    public static byte[] qoiOpRGBA(byte[] pixel) {
      assert pixel.length == 4 : "Invalid pixel length";
      return ArrayUtils.concat(
        ArrayUtils.wrap(QOISpecification.QOI_OP_RGBA_TAG),
        pixel
      );
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index){
      assert index < 64 : "Invalid index";
      return ArrayUtils.wrap((byte) (QOISpecification.QOI_OP_INDEX_TAG | index));
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
    public static byte[] qoiOpLuma(byte[] diff) {
      assert diff.length == 3;
      assert diff[1] > -33 && diff[1] < 32 : "Invalid DG value";

      assert diff[2] > -33 && diff[2] < 32 : "Invalid DB value";

      byte drdg = (byte) (diff[0] - diff[1]);
      assert drdg > -9 && drdg < 8 : "Invalid DR-DG value";

      byte dbdg = (byte) (diff[2] - diff[1]);
      assert dbdg > -9 && dbdg < 8 : "Invalid DB-DG value";

      
      byte[] encoded = new byte[2];
      encoded[0] = (byte) (QOISpecification.QOI_OP_LUMA_TAG | (diff[1] + 32));
      encoded[1] = (byte) (((drdg + 8) << 4) + (dbdg + 8));

      return encoded;
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