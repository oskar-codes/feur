package src.cs107;
import java.util.ArrayList;

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
    public static byte[] qoiOpRGB(byte[] pixel) {
      assert pixel.length == 4: "Invalid pixel length";
      return new byte[]{QOISpecification.QOI_OP_RGB_TAG, pixel[0], pixel[1], pixel[2]};
    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel) {
      assert pixel.length == 4 : "Invalid pixel length";
      return new byte[]{QOISpecification.QOI_OP_RGBA_TAG, pixel[0], pixel[1], pixel[2], pixel[3]};
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index) {
      assert index >= 0 && index < 64 : "Invalid index";
      return ArrayUtils.wrap(index);
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_DIFF schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpDiff(byte[] diff) {
      assert diff != null : "Diff cannot be null";
      assert diff.length == 3 : "Diff length must be 3";
      byte[] encoding = new byte[1];
      for (int i = 0; i < diff.length; i++) {
        assert diff[i] < 2 && diff[i] > -3;
        encoding[0] += (diff[2-i] + 2) << (2 * i);
      }
      encoding[0] += QOISpecification.QOI_OP_DIFF_TAG;
      return encoding;
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
    public static byte[] qoiOpRun(byte count) {
      assert count > 0 && count < 63 : "Invalid count length";
      return ArrayUtils.wrap((byte) (count + QOISpecification.QOI_OP_RUN_TAG - 1));
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
    public static byte[] encodeData(byte[][] image) {
      byte[] previousPixel = QOISpecification.START_PIXEL;
      byte[][] hashTable = new byte[64][4];
      byte compteur = 0;

      ArrayList<byte[]> compressedImage = new ArrayList<byte[]>();
      
      for (int i = 0; i < image.length; i++) {
        byte[] pixel = image[i];

        // QOI_OP_RUN
        if (ArrayUtils.equals(pixel, previousPixel)) {
          compteur += 1;
          if (compteur >= 62) {
            compressedImage.add(qoiOpRun(compteur));
            compteur = 0;
          }
          previousPixel = ArrayUtils.copy(pixel);
          continue;
        }
        if (compteur > 0) {
          compressedImage.add(qoiOpRun(compteur));
          compteur = 0;
        }

        // QOI_OP_INDEX
        byte hash = QOISpecification.hash(pixel);
        byte[] reference = hashTable[hash];
        if (ArrayUtils.equals(reference, pixel) && (!reference.equals(new byte[]{0, 0, 0, 0}) || hash != 0)) {
          compressedImage.add(qoiOpIndex(hash));
          previousPixel = ArrayUtils.copy(pixel);
          continue;
        }
        hashTable[hash] = ArrayUtils.copy(pixel);
        
        // QOI_OP_RGBA
        if (pixel[3] != previousPixel[3]) {
          compressedImage.add(qoiOpRGBA(pixel));
          previousPixel = ArrayUtils.copy(pixel);
          continue;
        }

        // QOI_OP_DIFF
        byte dr = (byte) (pixel[0] - previousPixel[0]);
        byte dg = (byte) (pixel[1] - previousPixel[1]);
        byte db = (byte) (pixel[2] - previousPixel[2]);

        if (ArrayUtils.inRange(-3,2, dr)
        &&  ArrayUtils.inRange(-3, 2, dg)
        &&  ArrayUtils.inRange(-3, 2, db)) {
          compressedImage.add(qoiOpDiff(new byte[]{dr, dg, db}));
          previousPixel = ArrayUtils.copy(pixel);
          continue;
        }

        // QOI_OP_LUMA
        byte drdg = (byte) (dr - dg);
        byte dbdg = (byte) (db - dg);
        if (ArrayUtils.inRange(-33,32, dg)
        &&  ArrayUtils.inRange(-9, 8, drdg)
        &&  ArrayUtils.inRange(-9, 8, dbdg)) {
          compressedImage.add(qoiOpLuma(new byte[]{dr, dg, db}));
          previousPixel = ArrayUtils.copy(pixel);
          continue;
        }
        
        // QOI_OP_RGB
        compressedImage.add(qoiOpRGB(pixel));
        previousPixel = ArrayUtils.copy(pixel);
      }
      if (compteur > 0) {
        compressedImage.add(qoiOpRun(compteur));
      }

      int size = 0;
      for (int i = 0; i < compressedImage.size(); i++) {
        size += compressedImage.get(i).length;
      }

      byte[] result = new byte[size];
      int k = 0;
      for (int i = 0; i < compressedImage.size(); i++) {
        for (int j = 0; j < compressedImage.get(i).length; j++) {
          result[k++] = compressedImage.get(i)[j];
        }
      }

      return result;
    }

    /**
     * Creates the representation in memory of the "Quite Ok Image" file.
     * @apiNote THE FILE IS NOT CREATED YET, THIS IS JUST ITS REPRESENTATION.
     * TO CREATE THE FILE, YOU'LL NEED TO CALL Helper::write
     * @param image (Helper.Image) - Image to encode
     * @return (byte[]) - Binary representation of the "Quite Ok File" of the image
     * @throws AssertionError if the image is null
     */
    public static byte[] qoiFile(Helper.Image image) {
      assert image != null : "Image is null";
      byte[] header = qoiHeader(image);

      byte[][] data = ArrayUtils.imageToChannels(image.data());

      byte[] encodedData = encodeData(data);
      byte[] EOF = QOISpecification.QOI_EOF;
      return ArrayUtils.concat(header, encodedData, EOF);
    }

}