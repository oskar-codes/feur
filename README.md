# QOI file Encoder / Decoder
A simple QOI file encoder/decoder built for the first semester of the Computer Science course at EPFL.
The project is fully functional, and follows the guidelines given in [this](https://proginsc.epfl.ch/wwwhiver/mini-projet1/QOIcompression-en.pdf) project description, along with a few additional methods described below to facilitate the development.

## Additional methods
All of the following methods are in the ArrayUtils class.

### `toString`
Signature:<br>
<pre>public static String toString(byte[] input)
public static String toString(byte[][] input)
public static String toString(int[] input)
public static String toString(int[][] input)</pre>
Recursively returns a string representation of the given array of dimension 1 or 2, and with inner elements of type `int` or `byte`.
This function is not referenced anywhere in the code, but was used for debugging purposes.

### `inRange`
Signature:
<pre>
public static boolean inRange(int min, int max, byte val)
</pre>
Returns a boolean value indicating whether the given byte is between min and max (non-inclusive).

### `copy`
Signature:
<pre>
public static int[] copy(int[] input)
public static byte[] copy(byte[] input)
</pre>
Returns a copy of the given array. Used to assign arrays by value instead of reference.