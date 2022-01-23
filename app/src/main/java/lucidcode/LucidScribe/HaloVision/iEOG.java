package lucidcode.LucidScribe.HaloVision;

public interface iEOG {

    /**
     * Get the previous image in integer array format
     * 
     * @return int array of previous image.
     */
    public int[] getPrevious();

    /**
     * Detect motion.
     * 
     * @param data
     *            integer array representing an image.
     * @param width
     *            Width of the image.
     * @param height
     *            Height of the image.
     * @return boolean True is there is motion.
     * @throws NullPointerException
     *             if data integer array is NULL.
     */
    public int detect(int[] data, int width, int height, int highlight, int pixelThreshold, int PixelsInARow, int Amplification);
}
