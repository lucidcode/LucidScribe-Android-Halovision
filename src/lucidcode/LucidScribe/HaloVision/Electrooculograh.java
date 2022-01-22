package lucidcode.LucidScribe.HaloVision;


public class Electrooculograh implements iEOG {
    private static int[] mPrevious = null;
    private static int mPreviousWidth = 0;
    private static int mPreviousHeight = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getPrevious() {
        return ((mPrevious != null) ? mPrevious.clone() : null);
    }

    protected static int isDifferent(int[] first, int width, int height, int highlight, int pixelThreshold, int PixelsInARow, int Amplification) {
        if (first == null) throw new NullPointerException();

        if (mPrevious == null) return 0;
        if (first.length != mPrevious.length) return 1;
        if (mPreviousWidth != width || mPreviousHeight != height) return 1;

        int totDifferentPixels = 0;
        for (int i = 0, ij = 0; i < height; i++) {
        	int rowDifferences = 0;
            for (int j = 0; j < width; j++, ij++) {
                int pix = (0xff & ((int) first[ij]));
                int otherPix = (0xff & ((int) mPrevious[ij]));

                if (Math.abs(pix - otherPix) >= pixelThreshold) {
                	rowDifferences++;
                }
                else
                {
                	rowDifferences = 0;
                }
                
                if (rowDifferences >= PixelsInARow)
                {
                    totDifferentPixels++;
                    // Paint different pixel 
                    first[ij] = highlight;
                }
            }
        }
        if (totDifferentPixels <= 0) totDifferentPixels = 1;
        
          //int size = height * width;
          //int pergrand = 1000/(size/totDifferentPixels);  
        if (totDifferentPixels > 1000) {totDifferentPixels= 1000;}
        return totDifferentPixels;
    }

    /**
     * Detect motion comparing RGB pixel values. {@inheritDoc}
     */
    @Override
    public int detect(int[] rgb, int width, int height, int highlight, int pixelThreshold, int PixelsInARow, int Amplification) {
        if (rgb == null) throw new NullPointerException();

        int[] original = rgb.clone();

        // Create the "mPrevious" picture, the one that will be used to check
        // the next frame against.
        if (mPrevious == null) {
            mPrevious = original;
            mPreviousWidth = width;
            mPreviousHeight = height;
            // Log.i(TAG, "Creating background image");
            return 0;
        }

        // long bDetection = System.currentTimeMillis();
        int motionDetected = isDifferent(rgb, width, height, highlight, pixelThreshold, PixelsInARow, Amplification);
        // long aDetection = System.currentTimeMillis();
        // Log.d(TAG, "Detection "+(aDetection-bDetection));

        // Replace the current image with the previous.
        mPrevious = original;
        mPreviousWidth = width;
        mPreviousHeight = height;

        return motionDetected;
    }
}
