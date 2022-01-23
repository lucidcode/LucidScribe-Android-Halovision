package lucidcode.LucidScribe.HaloVision;

import lucidcode.LucidScribe.HaloVision.Store.IabHelper;
import lucidcode.LucidScribe.HaloVision.Store.IabResult;
import lucidcode.LucidScribe.HaloVision.Store.Inventory;
import lucidcode.LucidScribe.HaloVision.Store.Purchase;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

public class LucidScribeStorePlugin extends Plugin {

	private Context context;
	protected String callbackId = null;
	IabHelper mHelper;
	String SKU_COLORS = "lucidcode_lucid_scribe_colors";
	String objectSKU = "";
	
	@Override
	public PluginResult execute(String action, JSONArray data, String internalCallbackId) {
		this.callbackId = internalCallbackId;
		context = this.cordova.getContext();

		try {
			final String command = data.getString(0);
			final Activity cordovaActivity = this.cordova.getActivity();

			try {
				objectSKU = data.getString(1);
			} catch (Exception ex) {
			}

			String key1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2z29LXhOBYhRb+yYk7FCOKpfZzmkvtGUD/TJj4HlmapmpPtu";
			String key2 = "ZbGUc4dncTbfX9Q6xu8/EnG5F214qYpDnP5SrwjaJGiq/DxTkd/lr47LOSq/HbtMLX6HbJbq2sL7uAxs7Q/75xy8Nnpt";
			String key3 = "Tf8fZtasznVJ9EM8z1q7LH0P+S6B9qlZW8gdaMZ6RsdrO6iJv38bwKzKoxb+88HCtlZgj0SKsq4GktM+zkXrSxXCqa2y";
			String key4 = "ccnf5pRW5DEUSKZezI1YoGK5KhkNkkmzsz3cW9VOm0zj/qyoPGTQp9ejufjAS16gXJy6jm3G8u4qSMHAoRjbIZ4LQx49";
			String key5 = "ky7udBU9kvLlO2YqnwIDAQAB";
			
			if (command.equals("Purchase")) {
				if (objectSKU.equals("Colors")) {
					Intent storeActivityIntent = new Intent(context, LucidScribeStoreActivity.class);
		    		storeActivityIntent.putExtra("SKU", SKU_COLORS);
		    		storeActivityIntent.putExtra("callbackId", callbackId);
		    		cordovaActivity.startActivity(storeActivityIntent);
		    		
					IntentFilter filter = new IntentFilter();
			        filter.addAction("com.lucidcode.LucidScribe.HaloVision.Store");
			        this.cordova.getContext().registerReceiver(receiver, filter);
		    		
		    		PluginResult result = new PluginResult(Status.NO_RESULT);
		    		result.setKeepCallback(true);
		    		return result;
				}
			}
			
			mHelper = new IabHelper(context, key1 + key2 + key3 + key4 + key5);
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					if (!result.isSuccess()) {
						// Oh noes, there was a problem.
						Log.d("LucidScribe.Halovision", "Problem setting up In-app Billing: " + result);
						if (!result.getMessage().startsWith("Billing service unavailable on device"))
						{
							error(new PluginResult(Status.JSON_EXCEPTION, result.getMessage()), callbackId);
						}
						
						if (mHelper != null)
							mHelper.dispose();
						mHelper = null;
						
						return;
					}

					if (command.equals("GetPurchases")) {
						mHelper.queryInventoryAsync(mGotInventoryListener);
						//return;
					}
						else if (command.equals("Consume"))
						{
							mHelper.queryInventoryAsync(mGotInventoryListenerConsume);
							//return;
							
						
					}
					//error(new PluginResult(Status.JSON_EXCEPTION, "Unknown store command"), callbackId);
				}
			});
		} catch (Exception ex) {
			Toast.makeText(this.cordova.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("Lucid Scribe Halovision Plugin", ex.getMessage());
			if (!ex.getMessage().startsWith("Billing service unavailable on device"))
			{
				error(new PluginResult(Status.JSON_EXCEPTION, ex.getMessage()), callbackId);
			}
		}

		PluginResult result = new PluginResult(Status.NO_RESULT);
		result.setKeepCallback(true);
		return result;
	}
	
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	try
	        {        		
        		String response = intent.getStringExtra("response");
        		
        		if (response.equals("success"))
        		{
					JSONObject geoPosition = new JSONObject();
					try {
						geoPosition.put("Command", "NewPurchase");
						String sku = intent.getStringExtra("sku");
						if (sku.equals(SKU_COLORS))
						{
							geoPosition.put("ObjectSKU", "Colors");
						}
						else
						{
							geoPosition.put("ObjectSKU", sku);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PluginResult pluginResult = new PluginResult(Status.OK, geoPosition);
					success(pluginResult, callbackId);
				}
        		else
        		{
            		String error = intent.getStringExtra("error");
    	            error(new PluginResult(Status.JSON_EXCEPTION, error), callbackId);
        		}
	        }
	        catch (Exception ex)
	        {
	            error(new PluginResult(Status.JSON_EXCEPTION, ex.getMessage()), callbackId);
	        }
        }
    };

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if (result.isFailure()) {
				// handle error here
				if (!result.getMessage().startsWith("Billing service unavailable on device"))
				{
					error(new PluginResult(Status.JSON_EXCEPTION, result.getMessage()), callbackId);
				}
			} else {
				// does the user have the premium upgrade?
				boolean hasColors = inventory.hasPurchase(SKU_COLORS);
				// update UI accordingly

				JSONObject geoPosition = new JSONObject();
				try {
					geoPosition.put("Command", "Purchases");
					if (hasColors) {
						geoPosition.put("Colors", 1);
					} else {
						geoPosition.put("Colors", 0);
					}
					geoPosition.put("Result", result.getMessage());
					geoPosition.put("Inventory", inventory.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PluginResult pluginResult = new PluginResult(Status.OK, geoPosition);
				success(pluginResult, callbackId);
				
				if (mHelper != null)
					mHelper.dispose();
				mHelper = null;
			}
		}
	};
	

	IabHelper.QueryInventoryFinishedListener mGotInventoryListenerConsume = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if (result.isFailure()) {
				// handle error here
				error(new PluginResult(Status.JSON_EXCEPTION, result.getMessage()), callbackId);
			} else 
			{
				mHelper.consumeAsync(inventory.getPurchase(SKU_COLORS), mConsumeFinishedListener);
			}
		}
	};
	
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
		   new IabHelper.OnConsumeFinishedListener() {
		   public void onConsumeFinished(Purchase purchase, IabResult result) {
		      if (result.isSuccess()) {
		         // provision the in-app purchase to the user
		         // (for example, credit 50 gold coins to player's character)
		      }
		      else {
		         // handle error 
	
				JSONObject geoPosition = new JSONObject();
				try {
					geoPosition.put("Command", "Consume");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PluginResult pluginResult = new PluginResult(Status.OK, geoPosition);
				success(pluginResult, callbackId);
				
				if (mHelper != null)
					mHelper.dispose();
				mHelper = null;
		   }
		}
	};	
}
