package lucidcode.LucidScribe.HaloVision;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Color;

public class HaloVisionPlugin extends Plugin  {
	
	private Context context;
    protected String callbackId = null;

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId)
    {
        this.callbackId = callbackId;
        context = this.cordova.getContext();
    	
    	try
    	{  
    		Intent visionIntent; 
    		if (data.getString(8).equals("true"))
    		{
    			visionIntent = new Intent(this.cordova.getContext(), VisionActivity.class);
    		}
    		else
    		{
    			//https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/themes.xml
    			visionIntent = new Intent(this.cordova.getContext(), VisionActivitySmall.class);
    		}
    		
    		visionIntent.putExtra("Algorithm", data.getString(0));
    		visionIntent.putExtra("Amplification", Integer.parseInt(data.getString(1)));
    		visionIntent.putExtra("PixelThreshold", Integer.parseInt(data.getString(2)));
    		visionIntent.putExtra("PixelsInARow", Integer.parseInt(data.getString(3)));
    		visionIntent.putExtra("FrameThreshold", Integer.parseInt(data.getString(4)));
    		visionIntent.putExtra("Color", GetColor(data.getString(5)));
    		visionIntent.putExtra("Track", data.getString(6));
    		visionIntent.putExtra("Researcher", data.getString(7));
    		
			this.cordova.getActivity().startActivity(visionIntent);
			
			IntentFilter filter = new IntentFilter();
	        filter.addAction("com.lucidcode.LucidScribe.HaloVision.Data");
	        this.cordova.getContext().registerReceiver(receiver, filter);
	        
			//IntentFilter filterHide = new IntentFilter();
			//filterHide.addAction("com.lucidcode.LucidScribe.HaloVision.Hide");
	        //this.cordova.getContext().registerReceiver(receiverHide, filterHide);
    	}
    	catch (Exception ex)
        {
			Toast.makeText(this.cordova.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    		Log.e("Lucid Scribe Halovision Plugin", ex.getMessage());
            error(new PluginResult(Status.JSON_EXCEPTION, ex.getMessage()), callbackId);
        }
    	
        PluginResult result = new PluginResult(Status.NO_RESULT);
        result.setKeepCallback(true);
        return result;
    }
    
    private int GetColor(String colorName)
    {
		if (colorName.equals("White")) { return Color.WHITE; }
		else if (colorName.equals("AliceBlue")) { return Color.argb(255, 240, 248, 255); }
		else if (colorName.equals("AntiqueWhite")) { return Color.argb(255, 250, 235, 215); }
		else if (colorName.equals("Aqua")) { return Color.argb(255, 0, 255, 255); }
		else if (colorName.equals("Aquamarine")) { return Color.argb(255, 127, 255, 212); }
		else if (colorName.equals("Azure")) { return Color.argb(255, 240, 255, 255); }
		else if (colorName.equals("Beige")) { return Color.argb(255, 245, 245, 220); }
		else if (colorName.equals("Bisque")) { return Color.argb(255, 255, 228, 196); }
		else if (colorName.equals("Black")) { return Color.argb(255, 0, 0, 0); }
		else if (colorName.equals("BlanchedAlmond")) { return Color.argb(255, 255, 235, 205); }
		else if (colorName.equals("Blue")) { return Color.argb(255, 0, 0, 255); }
		else if (colorName.equals("BlueViolet")) { return Color.argb(255, 138, 43, 226); }
		else if (colorName.equals("Brown")) { return Color.argb(255, 165, 42, 42); }
		else if (colorName.equals("BurlyWood")) { return Color.argb(255, 222, 184, 135); }
		else if (colorName.equals("CadetBlue")) { return Color.argb(255, 95, 158, 160); }
		else if (colorName.equals("Chartreuse")) { return Color.argb(255, 127, 255, 0); }
		else if (colorName.equals("Chocolate")) { return Color.argb(255, 210, 105, 30); }
		else if (colorName.equals("Coral")) { return Color.argb(255, 255, 127, 80); }
		else if (colorName.equals("CornflowerBlue")) { return Color.argb(255, 100, 149, 237); }
		else if (colorName.equals("Cornsilk")) { return Color.argb(255, 255, 248, 220); }
		else if (colorName.equals("Crimson")) { return Color.argb(255, 220, 20, 60); }
		else if (colorName.equals("Cyan")) { return Color.argb(255, 0, 255, 255); }
		else if (colorName.equals("DarkBlue")) { return Color.argb(255, 0, 0, 139); }
		else if (colorName.equals("DarkCyan")) { return Color.argb(255, 0, 139, 139); }
		else if (colorName.equals("DarkGoldenrod")) { return Color.argb(255, 184, 134, 11); }
		else if (colorName.equals("DarkGray")) { return Color.argb(255, 169, 169, 169); }
		else if (colorName.equals("DarkGreen")) { return Color.argb(255, 0, 100, 0); }
		else if (colorName.equals("DarkKhaki")) { return Color.argb(255, 189, 183, 107); }
		else if (colorName.equals("DarkMagenta")) { return Color.argb(255, 139, 0, 139); }
		else if (colorName.equals("DarkOliveGreen")) { return Color.argb(255, 85, 107, 47); }
		else if (colorName.equals("DarkOrange")) { return Color.argb(255, 255, 140, 0); }
		else if (colorName.equals("DarkOrchid")) { return Color.argb(255, 153, 50, 204); }
		else if (colorName.equals("DarkRed")) { return Color.argb(255, 139, 0, 0); }
		else if (colorName.equals("DarkSalmon")) { return Color.argb(255, 233, 150, 122); }
		else if (colorName.equals("DarkSeaGreen")) { return Color.argb(255, 143, 188, 139); }
		else if (colorName.equals("DarkSlateBlue")) { return Color.argb(255, 72, 61, 139); }
		else if (colorName.equals("DarkSlateGray")) { return Color.argb(255, 47, 79, 79); }
		else if (colorName.equals("DarkTurquoise")) { return Color.argb(255, 0, 206, 209); }
		else if (colorName.equals("DarkViolet")) { return Color.argb(255, 148, 0, 211); }
		else if (colorName.equals("DeepPink")) { return Color.argb(255, 255, 20, 147); }
		else if (colorName.equals("DeepSkyBlue")) { return Color.argb(255, 0, 191, 255); }
		else if (colorName.equals("DimGray")) { return Color.argb(255, 105, 105, 105); }
		else if (colorName.equals("DodgerBlue")) { return Color.argb(255, 30, 144, 255); }
		else if (colorName.equals("Firebrick")) { return Color.argb(255, 178, 34, 34); }
		else if (colorName.equals("FloralWhite")) { return Color.argb(255, 255, 250, 240); }
		else if (colorName.equals("ForestGreen")) { return Color.argb(255, 34, 139, 34); }
		else if (colorName.equals("Fuchsia")) { return Color.argb(255, 255, 0, 255); }
		else if (colorName.equals("Gainsboro")) { return Color.argb(255, 220, 220, 220); }
		else if (colorName.equals("GhostWhite")) { return Color.argb(255, 248, 248, 255); }
		else if (colorName.equals("Gold")) { return Color.argb(255, 255, 215, 0); }
		else if (colorName.equals("Goldenrod")) { return Color.argb(255, 218, 165, 32); }
		else if (colorName.equals("Gray")) { return Color.argb(255, 128, 128, 128); }
		else if (colorName.equals("Green")) { return Color.argb(255, 0, 128, 0); }
		else if (colorName.equals("GreenYellow")) { return Color.argb(255, 173, 255, 47); }
		else if (colorName.equals("Honeydew")) { return Color.argb(255, 240, 255, 240); }
		else if (colorName.equals("HotPink")) { return Color.argb(255, 255, 105, 180); }
		else if (colorName.equals("IndianRed")) { return Color.argb(255, 205, 92, 92); }
		else if (colorName.equals("Indigo")) { return Color.argb(255, 75, 0, 130); }
		else if (colorName.equals("Ivory")) { return Color.argb(255, 255, 255, 240); }
		else if (colorName.equals("Khaki")) { return Color.argb(255, 240, 230, 140); }
		else if (colorName.equals("Lavender")) { return Color.argb(255, 230, 230, 250); }
		else if (colorName.equals("LavenderBlush")) { return Color.argb(255, 255, 240, 245); }
		else if (colorName.equals("LawnGreen")) { return Color.argb(255, 124, 252, 0); }
		else if (colorName.equals("LemonChiffon")) { return Color.argb(255, 255, 250, 205); }
		else if (colorName.equals("LightBlue")) { return Color.argb(255, 173, 216, 230); }
		else if (colorName.equals("LightCoral")) { return Color.argb(255, 240, 128, 128); }
		else if (colorName.equals("LightCyan")) { return Color.argb(255, 224, 255, 255); }
		else if (colorName.equals("LightGoldenrodYellow")) { return Color.argb(255, 250, 250, 210); }
		else if (colorName.equals("LightGreen")) { return Color.argb(255, 144, 238, 144); }
		else if (colorName.equals("LightGray")) { return Color.argb(255, 211, 211, 211); }
		else if (colorName.equals("LightPink")) { return Color.argb(255, 255, 182, 193); }
		else if (colorName.equals("LightSalmon")) { return Color.argb(255, 255, 160, 122); }
		else if (colorName.equals("LightSeaGreen")) { return Color.argb(255, 32, 178, 170); }
		else if (colorName.equals("LightSkyBlue")) { return Color.argb(255, 135, 206, 250); }
		else if (colorName.equals("LightSlateGray")) { return Color.argb(255, 119, 136, 153); }
		else if (colorName.equals("LightSteelBlue")) { return Color.argb(255, 176, 196, 222); }
		else if (colorName.equals("LightYellow")) { return Color.argb(255, 255, 255, 224); }
		else if (colorName.equals("Lime")) { return Color.argb(255, 0, 255, 0); }
		else if (colorName.equals("LimeGreen")) { return Color.argb(255, 50, 205, 50); }
		else if (colorName.equals("Linen")) { return Color.argb(255, 250, 240, 230); }
		else if (colorName.equals("Magenta")) { return Color.argb(255, 255, 0, 255); }
		else if (colorName.equals("Maroon")) { return Color.argb(255, 128, 0, 0); }
		else if (colorName.equals("MediumAquamarine")) { return Color.argb(255, 102, 205, 170); }
		else if (colorName.equals("MediumBlue")) { return Color.argb(255, 0, 0, 205); }
		else if (colorName.equals("MediumOrchid")) { return Color.argb(255, 186, 85, 211); }
		else if (colorName.equals("MediumPurple")) { return Color.argb(255, 147, 112, 219); }
		else if (colorName.equals("MediumSeaGreen")) { return Color.argb(255, 60, 179, 113); }
		else if (colorName.equals("MediumSlateBlue")) { return Color.argb(255, 123, 104, 238); }
		else if (colorName.equals("MediumSpringGreen")) { return Color.argb(255, 0, 250, 154); }
		else if (colorName.equals("MediumTurquoise")) { return Color.argb(255, 72, 209, 204); }
		else if (colorName.equals("MediumVioletRed")) { return Color.argb(255, 199, 21, 133); }
		else if (colorName.equals("MidnightBlue")) { return Color.argb(255, 25, 25, 112); }
		else if (colorName.equals("MintCream")) { return Color.argb(255, 245, 255, 250); }
		else if (colorName.equals("MistyRose")) { return Color.argb(255, 255, 228, 225); }
		else if (colorName.equals("Moccasin")) { return Color.argb(255, 255, 228, 181); }
		else if (colorName.equals("NavajoWhite")) { return Color.argb(255, 255, 222, 173); }
		else if (colorName.equals("Navy")) { return Color.argb(255, 0, 0, 128); }
		else if (colorName.equals("OldLace")) { return Color.argb(255, 253, 245, 230); }
		else if (colorName.equals("Olive")) { return Color.argb(255, 128, 128, 0); }
		else if (colorName.equals("OliveDrab")) { return Color.argb(255, 107, 142, 35); }
		else if (colorName.equals("Orange")) { return Color.argb(255, 255, 165, 0); }
		else if (colorName.equals("OrangeRed")) { return Color.argb(255, 255, 69, 0); }
		else if (colorName.equals("Orchid")) { return Color.argb(255, 218, 112, 214); }
		else if (colorName.equals("PaleGoldenrod")) { return Color.argb(255, 238, 232, 170); }
		else if (colorName.equals("PaleGreen")) { return Color.argb(255, 152, 251, 152); }
		else if (colorName.equals("PaleTurquoise")) { return Color.argb(255, 175, 238, 238); }
		else if (colorName.equals("PaleVioletRed")) { return Color.argb(255, 219, 112, 147); }
		else if (colorName.equals("PapayaWhip")) { return Color.argb(255, 255, 239, 213); }
		else if (colorName.equals("PeachPuff")) { return Color.argb(255, 255, 218, 185); }
		else if (colorName.equals("Peru")) { return Color.argb(255, 205, 133, 63); }
		else if (colorName.equals("Pink")) { return Color.argb(255, 255, 192, 203); }
		else if (colorName.equals("Plum")) { return Color.argb(255, 221, 160, 221); }
		else if (colorName.equals("PowderBlue")) { return Color.argb(255, 176, 224, 230); }
		else if (colorName.equals("Purple")) { return Color.argb(255, 128, 0, 128); }
		else if (colorName.equals("Red")) { return Color.argb(255, 255, 0, 0); }
		else if (colorName.equals("RosyBrown")) { return Color.argb(255, 188, 143, 143); }
		else if (colorName.equals("RoyalBlue")) { return Color.argb(255, 65, 105, 225); }
		else if (colorName.equals("SaddleBrown")) { return Color.argb(255, 139, 69, 19); }
		else if (colorName.equals("Salmon")) { return Color.argb(255, 250, 128, 114); }
		else if (colorName.equals("SandyBrown")) { return Color.argb(255, 244, 164, 96); }
		else if (colorName.equals("SeaGreen")) { return Color.argb(255, 46, 139, 87); }
		else if (colorName.equals("SeaShell")) { return Color.argb(255, 255, 245, 238); }
		else if (colorName.equals("Sienna")) { return Color.argb(255, 160, 82, 45); }
		else if (colorName.equals("Silver")) { return Color.argb(255, 192, 192, 192); }
		else if (colorName.equals("SkyBlue")) { return Color.argb(255, 135, 206, 235); }
		else if (colorName.equals("SlateBlue")) { return Color.argb(255, 106, 90, 205); }
		else if (colorName.equals("SlateGray")) { return Color.argb(255, 112, 128, 144); }
		else if (colorName.equals("Snow")) { return Color.argb(255, 255, 250, 250); }
		else if (colorName.equals("SpringGreen")) { return Color.argb(255, 0, 255, 127); }
		else if (colorName.equals("SteelBlue")) { return Color.argb(255, 70, 130, 180); }
		else if (colorName.equals("Tan")) { return Color.argb(255, 210, 180, 140); }
		else if (colorName.equals("Teal")) { return Color.argb(255, 0, 128, 128); }
		else if (colorName.equals("Thistle")) { return Color.argb(255, 216, 191, 216); }
		else if (colorName.equals("Tomato")) { return Color.argb(255, 255, 99, 71); }
		else if (colorName.equals("Turquoise")) { return Color.argb(255, 64, 224, 208); }
		else if (colorName.equals("Violet")) { return Color.argb(255, 238, 130, 238); }
		else if (colorName.equals("Wheat")) { return Color.argb(255, 245, 222, 179); }
		else if (colorName.equals("White")) { return Color.argb(255, 255, 255, 255); }
		else if (colorName.equals("WhiteSmoke")) { return Color.argb(255, 245, 245, 245); }
		else if (colorName.equals("Yellow")) { return Color.argb(255, 255, 255, 0); }
		else if (colorName.equals("YellowGreen")) { return Color.argb(255, 154, 205, 50); }

    	return Color.WHITE;
    }
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	try
	        {        		
	            JSONObject geoPosition = new JSONObject();
	            int diff = intent.getIntExtra("DIFF", 0);
	            geoPosition.put("x", diff);

		        PluginResult result = new PluginResult(Status.OK, geoPosition);
		    	result.setKeepCallback(true);
	            success(result, callbackId);
	        }
	        catch (Exception ex)
	        {
	            error(new PluginResult(Status.JSON_EXCEPTION, ex.getMessage()), callbackId);
	        }
        }
    };
    
    //private BroadcastReceiver receiverHide = new BroadcastReceiver() {
        //@Override
        //public void onReceive(Context context, Intent intent) {
        	//Intent i = new Intent(context, context.getApplicationContext().geta);
        	//i.setAction(Intent.ACTION_MAIN);
        	//i.addCategory(Intent.CATEGORY_LAUNCHER);
        	//startActivity(i);
        //}
    //};
    
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
    	context.unregisterReceiver(receiver);	
    	//context.unregisterReceiver(receiverHide);
    	super.onDestroy();
	}
}