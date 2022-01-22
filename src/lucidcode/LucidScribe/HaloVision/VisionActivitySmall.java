package lucidcode.LucidScribe.HaloVision;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class VisionActivitySmall extends Activity {
	private static TextView txtConsole;
	private SurfaceView svCamera;
    private static Camera camera = null;
    private static boolean inPreview = false;
    private static SurfaceHolder previewHolder = null;

	static final public String LOG_TAG = "LucidScribe.HaloVision";
	
    private static Electrooculograh detector = null;
    private static volatile AtomicBoolean processing = new AtomicBoolean(false);
    
    private static MediaPlayer mediaPlayer;
    
    private String Track;
    private String Algorithm;
    private int Amplification;
    private int PixelThreshold;
    private int PixelsInARow;
    private int FrameThreshold;
    private int HaloColor;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			Display display = getWindow().getWindowManager().getDefaultDisplay();
			
			WindowManager.LayoutParams params = getWindow().getAttributes();  
			//params.x = -20;  
			params.height = (display.getHeight()) / 2;
			params.width = (display.getWidth()) / 2;
			params.gravity = Gravity.CENTER;
			//params.y = -10;  
//http://stackoverflow.com/questions/2176922/how-to-create-transparent-activity-in-android
			this.getWindow().setAttributes(params); 
			
			setContentView(R.layout.video);
    		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

			Track = getIntent().getStringExtra("Track");
			Algorithm = getIntent().getStringExtra("Algorithm");
			Amplification = getIntent().getIntExtra("Amplification", 32);
			PixelThreshold = getIntent().getIntExtra("PixelThreshold", 16);
			PixelsInARow = getIntent().getIntExtra("PixelsInARow", 4);
			FrameThreshold = getIntent().getIntExtra("FrameThreshold", 960);
			HaloColor = getIntent().getIntExtra("Color", Color.WHITE);
			
			if (Track.equals("Astral Projection - People Can Fly.mp3"))
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.people_can_fly);
        	}
        	else if (Track.equals("DT8 Project – Breathe.mp3"))
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.breathe);
        	}
        	else if (Track.equals("Kai Tracid - Trance And Acid.mp3"))
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.trance_and_acid);
        	}
        	else if (Track.equals("Lange - Follow Me.mp3"))
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.follow_me);
        	}
        	else if (Track.equals("Prodigy - Voodoo People.mp3"))
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.voodoo_people);
        	}
        	else
        	{
              	mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.voodoo_people);
        	}

			// Get the controls for easy access
			txtConsole = (TextView) findViewById(R.id.console);
			svCamera = (SurfaceView) findViewById(R.id.preview);

			txtConsole.setOnClickListener(startSteamClicked);
			
	        previewHolder = svCamera.getHolder();
	        previewHolder.addCallback(surfaceCallback);
	        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            detector = new Electrooculograh();
            
	        log("Lucid Scribe - Halovision");
		} catch (Exception e) {
			Log.e(VisionActivitySmall.LOG_TAG, "onCreate: " + e.getMessage());
		}
	}
	
	private OnClickListener startSteamClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			txtConsole.setBackgroundResource(R.drawable.ic_eye);
			
            Intent broadcast = new Intent();
            broadcast.setAction("com.lucidcode.LucidScribe.HaloVision.Hide");
            sendBroadcast(broadcast);
		}
	};

	@Override
	protected void onPause() {
        super.onPause();

        camera.setPreviewCallback(null);
        if (inPreview) camera.stopPreview();
        inPreview = false;
        camera.release();
        camera = null;
	}
	
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
    	super.onDestroy();
	}

	public void onResume() {
        super.onResume();

        camera = Camera.open();
    	if (camera == null)
    	{
    		camera = Camera.open(0);
    	}
	}
	
	List<Integer> m_arrHistory = new ArrayList<Integer>();
    private PreviewCallback previewCallback = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) return;
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) return;
            
            try {
                // Current frame (with changes)
                int[] img = ImageProcessing.decodeYUV420SPtoRGB(data, size.width, size.height);
                int diff = detector.detect(img, size.width, size.height, HaloColor, PixelThreshold, PixelsInARow, Amplification);
                txtConsole.setText("");
                
                Intent broadcast = new Intent();
                broadcast.setAction("com.lucidcode.LucidScribe.HaloVision.Data");
                broadcast.putExtra("DIFF", diff);
                sendBroadcast(broadcast);
                                
                log("Lucid Scribe - Halovision: " + diff);
                log(Algorithm + " (" + Amplification + ", " + PixelThreshold + ", " + PixelsInARow + ", " + FrameThreshold + ")");
                
                if (Algorithm.equals("Motion Detector"))
                {
                	if (diff >= FrameThreshold)
                	{
                  		if (Track.equals("Lucid Scribe Trigger"))
                		{
		                	if ( TaskerIntent.testStatus( getApplicationContext() ).equals( TaskerIntent.Status.OK ) ) {
		                		TaskerIntent i = new TaskerIntent( "Lucid Scribe Trigger" );
		                		sendBroadcast( i );
		                	}
	                	}
                		else
                		{
                			mediaPlayer.start();
                		}
                	}
                }
                else if (Algorithm.equals("REM Detector"))
                {
                	m_arrHistory.add(diff);
                    if (m_arrHistory.size() > 128) { m_arrHistory.remove(0); }

                    int intBlinks = 0;
                    Boolean boolBlinking = false;

                    int intBelow = 0;
                    int intAbove = 0;

                    for (int index = 0; index < m_arrHistory.size(); index++)
                    {
                      if (m_arrHistory.get(index) > FrameThreshold)
                      {
                        intAbove += 1;
                        intBelow = 0;
                      }
                      else
                      {
                        intBelow += 1;
                        intAbove = 0;
                      }

                      if (!boolBlinking)
                      {
                        if (intAbove >= 2)
                        {
                          boolBlinking = true;
                          intBlinks += 1;
                          intAbove = 0;
                          intBelow = 0;
                        }
                      }
                      else
                      {
                        if (intBelow >= 8)
                        {
                          boolBlinking = false;
                          intBlinks += 1;
                          intBelow = 0;
                          intAbove = 0;
                        }
                        else
                        {
                          if (intAbove >= 12)
                          {
                            // reset
                            boolBlinking = false;
                            intBlinks = 0;
                            intBelow = 0;
                            intAbove = 0;
                          }
                        }
                      }

                      if (intBlinks > 10)
                      {
                		if (Track.equals("Lucid Scribe Trigger"))
                		{
		                	if ( TaskerIntent.testStatus( getApplicationContext() ).equals( TaskerIntent.Status.OK ) ) {
		                		TaskerIntent i = new TaskerIntent( "Lucid Scribe Trigger" );
		                		sendBroadcast( i );
		                	}
	                	}
                		else
                		{
                			mediaPlayer.start();
                		}
                        break;
                      }

                      if (intAbove > 12)
                      { // reset
                        boolBlinking = false;
                        intBlinks = 0;
                        intBelow = 0;
                        intAbove = 0; ;
                      }
                      if (intBelow > 40)
                      { // reset
                        boolBlinking = false;
                        intBlinks = 0;
                        intBelow = 0;
                        intAbove = 0; ;
                      }
                    }
                }
                
                if (img != null && (diff > 0)) {
	                Bitmap bitmap = ImageProcessing.rgbToBitmap(img, size.width, size.height);
	                logBG(new BitmapDrawable(bitmap));  
                }
            } catch (Exception e) {
                log("E: " + e.getMessage());
                e.printStackTrace();
            } finally {
                processing.set(false);
            }
            processing.set(false);
        }
    };
	
	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
            	//previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = getBestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(LOG_TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
            inPreview = true;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };
    
    private static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) result = size;
                }
            }
        }

        return result;
    }

	public void log(String s) {
		if (txtConsole != null) {
			txtConsole.append(Html.fromHtml("<font color=\"#B0C4DE\">" + s + "</font><br />"));
		} else {
			Toast.makeText(this, "Console is null", Toast.LENGTH_SHORT).show();
		}
	}

	public void logBG(Drawable dr) {
		if (txtConsole != null) {
            txtConsole.setBackgroundDrawable(dr);
		} else {
			Toast.makeText(this, "Console is null", Toast.LENGTH_SHORT).show();
		}
	}
}