package lucidcode.LucidScribe.HaloVision;

import android.os.Bundle;
import org.apache.cordova.*;

public class MotionMonitor extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setIntegerProperty("loadUrlTimeoutValue", 380000);
        
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == 2) {
            super.setIntegerProperty("splashscreen", R.drawable.splashlandscape);
        }
        else {
            super.setIntegerProperty("splashscreen", R.drawable.splashportrait);
        }
        
        super.loadUrl("file:///android_asset/www/index.html", 4096);
    }
}