package assist.reader;

// import libraries
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Button;

public class MainActivity extends Activity 
{
	// Camera button
	Button btnCapture;
	
	public static final String lang = "eng";
	
	final int sizeOfRandomString = 10;
	
	// Where to store the image
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ReadAssist/";
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); // Store state
		
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return;
				} else {}
			}
		}
		
		// lang.traineddata file with the app (in assets folder)
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				
				//gin.close();
				out.close();
				
			} catch (IOException e) {}
		}
		

		String name = getRandomString(sizeOfRandomString);
		
		final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		if (settings.getBoolean("my_first_time", true)) 
		{
			//the app is being launched for first time, do something        
			// record the fact that the app has been started at least once
	        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("MYLABEL", name).commit();
		    settings.edit().putBoolean("my_first_time", false).commit(); 
		}

		// Set the layout
		setContentView(R.layout.activity_main);
		
		// Create an instance of intent 
		// Pass action android.media.action.IMAGE_CAPTUREt to launch camera
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		
		// Create instance of File
		File file = new File(DATA_PATH + "img.jpg");
		
		// Put uri as extra in intent object
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		
		// Start activity for result, pass intent as argument and request code
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}


	@Override // Method to deal with returns
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) 
	    {
	        if (resultCode == RESULT_OK) 
	        {
	        	// Perform the crop
				Intent intent = new Intent(this, Crop.class);
				startActivity(intent);
	        } 
	        else if (resultCode == RESULT_CANCELED) 
	        {
	            // User cancelled the image capture
	        	Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
	        } 
	    }
	}
	
	private static String getRandomString(final int sizeOfRandomString)
	{
		final Random random=new Random();
		final StringBuilder sb=new StringBuilder();
		for(int i=0;i<sizeOfRandomString;++i)
		{
			sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
		}
		return sb.toString();
	}
	
	
}