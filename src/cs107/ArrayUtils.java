package cs107;
/**
 * Utility class to manipulate arrays.
 * 
 * @apiNote First Task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class ArrayUtils {

  /**
   * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
   */
  private ArrayUtils() {
  }

  // ==================================================================================
  // =========================== ARRAY EQUALITY METHODS
  // ===========================
  // ==================================================================================

  /**
   * Check if the content of both arrays is the same
   * 
   * @param a1 (byte[]) - First array
   * @param a2 (byte[]) - Second array
   * @return (boolean) - true if both arrays have the same content (or both null),
   *         false otherwise
   * @throws AssertionError if one of the parameters is null
   */
  public static boolean equals(byte[] a1, byte[] a2) {
    if (a1 == null && a2 == null) return true;
    assert !(a1 == null && a2 != null
        || a1 != null && a2 == null) : "One of the arrays is null";
    if (a1.length != a2.length) return false;

    for (int i = 0; i < a1.length; i++) {
      if (a1[i] != a2[i]) return false;
    }

    return true;
  }

  /**
   * Check if the content of both arrays is the same
   * 
   * @param a1 (byte[][]) - First array
   * @param a2 (byte[][]) - Second array
   * @return (boolean) - true if both arrays have the same content (or both null),
   *         false otherwise
   * @throws AssertionError if one of the parameters is null
   */
  @SuppressWarnings("all")
  public static boolean equals(byte[][] a1, byte[][] a2) {
    if (a1.length != a2.length) return false;
    if (a1 == null && a2 == null) return true;
    assert !(a1 == null && a2 != null
        || a1 != null && a2 == null) : "One of the arrays is null";

    for (int i = 0; i < a1.length; i++) {
      if (!equals(a1[i], a2[i])) return false;
    }

    return true;
  }

  // ==================================================================================
  // ============================ ARRAY WRAPPING METHODS
  // ============================
  // ==================================================================================

  /**
   * Wrap the given value in an array
   * 
   * @param value (byte) - value to wrap
   * @return (byte[]) - array with one element (value)
   */
  public static byte[] wrap(byte value) {
    return new byte[] { value };
  }

  // ==================================================================================
  // ========================== INTEGER MANIPULATION METHODS
  // ==========================
  // ==================================================================================

  /**
   * Create an Integer using the given array. The input needs to be considered
   * as "Big Endian"
   * (See handout for the definition of "Big Endian")
   * 
   * @param bytes (byte[]) - Array of 4 bytes
   * @return (int) - Integer representation of the array
   * @throws AssertionError if the input is null or the input's length is
   *                        different from 4
   */
  public static int toInt(byte[] bytes) {
    assert bytes != null : "Input is null";
    assert bytes.length == 4 : "Invalid length of byte array";

    return (bytes[0] & 0xFF) << 24 |
           (bytes[1] & 0xFF) << 16 |
           (bytes[2] & 0xFF) << 8  |
           (bytes[3] & 0xFF);
  }

  /**
   * Separate the Integer (word) to 4 bytes. The Memory layout of this integer is
   * "Big Endian"
   * (See handout for the definition of "Big Endian")
   * 
   * @param value (int) - The integer
   * @return (byte[]) - Big Endian representation of the integer
   */
  public static byte[] fromInt(int value) {
    return new byte[] {
      (byte) (value >> 24),
      (byte) ((value << 8) >> 24),
      (byte) ((value << 16) >> 24),
      (byte) ((value << 24) >> 24)
    };
  }

  // ==================================================================================
  // ========================== ARRAY CONCATENATION METHODS
  // ==========================
  // ==================================================================================

  /**
   * Concatenate a given sequence of bytes and stores them in an array
   * 
   * @param bytes (byte ...) - Sequence of bytes to store in the array
   * @return (byte[]) - Array representation of the sequence
   * @throws AssertionError if the input is null
   */
  public static byte[] concat(byte... bytes) {
    assert bytes != null : "Input is null";

    byte[] output = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      output[i] = bytes[i];
    }
    return output;
  }

  /**
   * Concatenate a given sequence of arrays into one array
   * 
   * @param tabs (byte[] ...) - Sequence of arrays
   * @return (byte[]) - Array representation of the sequence
   * @throws AssertionError if the input is null
   *                        or one of the inner arrays of input is null.
   */
  public static byte[] concat(byte[]... tabs) {
    assert tabs != null : "Input is null";

    int size = 0;
    for (byte[] tab : tabs) {
      assert tab != null : "One of the inner arrays is null";
      size += tab.length;
    }

    int i = 0;
    byte[] output = new byte[size];
    for (byte[] tab : tabs) {
      for (byte c : tab) {
        output[i++] = c;
      }
    }

    return output;

  }

  // ==================================================================================
  // =========================== ARRAY EXTRACTION METHODS
  // ===========================
  // ==================================================================================

  /**
   * Extract an array from another array
   * 
   * @param input  (byte[]) - Array to extract from
   * @param start  (int) - Index in the input array to start the extract from
   * @param length (int) - The number of bytes to extract
   * @return (byte[]) - The extracted array
   * @throws AssertionError if the input is null or start and length are invalid.
   *                        start + length should also be smaller than the input's
   *                        length
   */
  public static byte[] extract(byte[] input, int start, int length) {
    assert input != null : "Input is null";
    assert start >= 0 && length >= 0 && start + length <= input.length
        : "Invalid start and length";

    byte[] output = new byte[length];
    for (int i = 0; i < length; i++) {
      output[i] = input[start + i];
    }
    return output;
  }

  /**
   * Create a partition of the input array.
   * (See handout for more information on how this method works)
   * 
   * @param input (byte[]) - The original array
   * @param sizes (int ...) - Sizes of the partitions
   * @return (byte[][]) - Array of input's partitions.
   *         The order of the partition is the same as the order in sizes
   * @throws AssertionError if one of the parameters is null
   *                        or the sum of the elements in sizes is different from
   *                        the input's length
   */
  public static byte[][] partition(byte[] input, int... sizes) {
    assert input != null : "Input is null";
    assert sizes != null : "Sizes is null";

    int sum = 0;
    for (int size : sizes) {
      sum += size;
    }
    assert sum == input.length : "Sum of sizes is different from input's length";

    byte[][] output = new byte[sizes.length][];
    int start = 0;
    for (int i = 0; i < sizes.length; i++) {
      output[i] = extract(input, start, sizes[i]);
      start += sizes[i];
    }
    return output;
  }

  // ==================================================================================
  // ============================== ARRAY FORMATTING METHODS
  // ==============================
  // ==================================================================================

  /**
   * Format a 2-dim integer array
   * where each dimension is a direction in the image to
   * a 2-dim byte array where the first dimension is the pixel
   * and the second dimension is the channel.
   * See handouts for more information on the format.
   * 
   * @param input (int[][]) - image data
   * @return (byte [][]) - formatted image data
   * @throws AssertionError if the input is null
   *                        or one of the inner arrays of input is null
   */
  public static byte[][] imageToChannels(int[][] input) {
    assert input != null : "Input is null";

    int width = input.length;
    int height = input[0].length;
    byte[][] output = new byte[width * height][4];

    int i = 0;
    for (int[] line : input) {
      assert line != null : "One of the inner arrays is null";
      for (int pixel : line) {
        byte[] tab = fromInt(pixel);
        output[i++] = new byte[]{
          tab[1],
          tab[2],
          tab[3],
          tab[0]
        };
      }
    }

    return output;
  }

  /**
   * Format a 2-dim byte array where the first dimension is the pixel
   * and the second is the channel to a 2-dim int array where the first
   * dimension is the height and the second is the width
   * 
   * @param input  (byte[][]) : linear representation of the image
   * @param height (int) - Height of the resulting image
   * @param width  (int) - Width of the resulting image
   * @return (int[][]) - the image data
   * @throws AssertionError if the input is null
   *                        or one of the inner arrays of input is null
   *                        or input's length differs from width * height
   *                        or height is invalid
   *                        or width is invalid
   */
  public static int[][] channelsToImage(byte[][] input, int height, int width) {
    assert input != null : "Input is null";
    assert height > 0 && width > 0 : "Invalid height or width";
    assert input.length == height * width : "Input's length differs from width * height";
    for (byte[] tab : input) {
      assert tab != null : "One of the inner arrays is null";
    }

    int[][] output = new int[height][width];
    int index = 0;
    for (int y = 0; y < height; y++) {
      int[] line = new int[width];
      for (int x = 0; x < width; x++) {
        byte[] pixel = new byte[]{
          input[index][3],
          input[index][0],
          input[index][1],
          input[index][2]
        };
        line[x] = toInt(pixel);
        index++;
      }
      output[y] = copy(line);
    }
    return output;
  }

  // ==================================================================================
  // ============================== ADDITIONAL METHODS
  // ==============================
  // ==================================================================================

  /**
   * Return a string representation of the input array
   * @param input (byte[]) - Array to represent
   * @return (String) - String representation of the array
   * @throws AssertionError if the input is null
   */
  public static String toString(byte[] input) {
    assert input != null : "Input is null";

    String output = "";
    for (byte b : input) {
      output += b + " ";
    }
    return output;
  }

  /**
   * Return a string representation of the input array
   * @param input (int[]) - Array to represent
   * @return (String) - String representation of the array
   * @throws AssertionError if the input is null
   */
  public static String toString(int[] input) {
    assert input != null : "Input is null";

    String output = "";
    for (int b : input) {
      output += b + " ";
    }
    return output;
  }

  /**
   * Recursively return a string representation of the input array
   * @param input (int[][]) - Array to represent
   * @return (String) - String representation of the array
   * @throws AssertionError if the input is null
   */
  public static String toString(int[][] input) {
    assert input != null : "Input is null";

    String output = "";
    for (int[] tab : input) {
      output += toString(tab) + "\n";
    }
    return output;
  }

  /**
   * Recursively return a string representation of the input array
   * @param input (byte[][]) - Array to represent
   * @return (String) - String representation of the array
   * @throws AssertionError if the input is null
   */
  public static String toString(byte[][] input) {
    assert input != null : "Input is null";

    String output = "";
    for (byte[] tab : input) {
      output += toString(tab) + "\n";
    }

    return output;
  }

  /**
   * Returns a boolean value indicating whether the given byte is between min and max (non-inclusive)
   * @param min (int) - Minimum value
   * @param max (int) - Maximum value
   * @param val (byte) - Value
   * @return (boolean) - Result
   */
  public static boolean inRange(int min, int max, byte val) {
    return ((int)val > min && (int)val < max);
  }

  /**
   * Returns a copy of the given array
   * @param input (byte[]) - Array to copy
   * @return (byte[]) - Copy of the array
   * @throws AssertionError if the input is null
   */
  public static byte[] copy(byte[] input) {
    assert input != null : "Input is null";

    byte[] output = new byte[input.length];
    for (int i = 0; i < input.length; i++) {
      output[i] = input[i];
    }
    return output;
  }

  /**
   * Returns a copy of the given array
   * @param input (int[]) - Array to copy
   * @return (int[]) - Copy of the array
   * @throws AssertionError if the input is null
   */
  public static int[] copy(int[] input) {
    assert input != null : "Input is null";

    int[] output = new int[input.length];
    for (int i = 0; i < input.length; i++) {
      output[i] = input[i];
    }
    return output;
  }
}