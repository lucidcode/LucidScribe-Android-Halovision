package lucidcode.LucidScribe.HaloVision;

import lucidcode.LucidScribe.HaloVision.Store.IabHelper;
import lucidcode.LucidScribe.HaloVision.Store.IabResult;
import lucidcode.LucidScribe.HaloVision.Store.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LucidScribeStoreActivity extends Activity {
	
	IabHelper mHelper;
	String callbackId;
	
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		final String sku = getIntent().getStringExtra("SKU");
		callbackId = getIntent().getStringExtra("callbackId");
		
		String key1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2z29LXhOBYhRb+yYk7FCOKpfZzmkvtGUD/TJj4HlmapmpPtu";
		String key2 = "ZbGUc4dncTbfX9Q6xu8/EnG5F214qYpDnP5SrwjaJGiq/DxTkd/lr47LOSq/HbtMLX6HbJbq2sL7uAxs7Q/75xy8Nnpt";
		String key3 = "Tf8fZtasznVJ9EM8z1q7LH0P+S6B9qlZW8gdaMZ6RsdrO6iJv38bwKzKoxb+88HCtlZgj0SKsq4GktM+zkXrSxXCqa2y";
		String key4 = "ccnf5pRW5DEUSKZezI1YoGK5KhkNkkmzsz3cW9VOm0zj/qyoPGTQp9ejufjAS16gXJy6jm3G8u4qSMHAoRjbIZ4LQx49";
		String key5 = "ky7udBU9kvLlO2YqnwIDAQAB";

		mHelper = new IabHelper(getApplicationContext(), key1 + key2 + key3 + key4 + key5);
		
		final Activity activity = this;
		
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d("LucidScribe.Halovision", "Problem setting up In-app Billing: " + result);
	                Intent broadcast = new Intent();
	                broadcast.setAction("com.lucidcode.LucidScribe.HaloVision.Store");
	                broadcast.putExtra("response", "error");
	                broadcast.putExtra("error", result.getMessage());
	                sendBroadcast(broadcast);
				}
				mHelper.launchPurchaseFlow(activity, sku, 10001, mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
			}
		});
	}

	@Override
	protected void onPause() {
        super.onPause();
	}
	
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
    	super.onDestroy();
	}

	public void onResume() {
        super.onResume();
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				Log.d("Lucid Scribe Halovision Plugin", "Error purchasing: " + result);

                Intent broadcast = new Intent();
                broadcast.setAction("com.lucidcode.LucidScribe.HaloVision.Store");
                
				if (result.getMessage().startsWith("User canceled"))
				{
	                broadcast.putExtra("response", "success");
	                broadcast.putExtra("sku", "User canceled");	
				}
				else
				{
	                broadcast.putExtra("response", "error");
	                broadcast.putExtra("error", result.getMessage());
				}

                sendBroadcast(broadcast);
			} else {
	            Intent broadcast = new Intent();
                broadcast.setAction("com.lucidcode.LucidScribe.HaloVision.Store");
                broadcast.putExtra("response", "success");
                broadcast.putExtra("sku", purchase.getSku());
                sendBroadcast(broadcast);
			}
			
			if (mHelper != null)
				mHelper.dispose();
			mHelper = null;
			finish();
		}
	};
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass on the activity result to the helper for handling
		//error(new PluginResult(Status.JSON_EXCEPTION, "Calling ar"), callbackId);
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
        }
    }

}
