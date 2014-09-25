package com.reader.assist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import assist.reader.R;

import com.googlecode.tesseract.android.TessBaseAPI;

public class PerformOCR extends Activity implements OnClickListener, OnInitListener 
{
	// For passing the ocr message to new activities
	public final static String EXTRA_MESSAGE = "assist.reader";
	public final static String EXTRA_MESSAGE2 = "assist.reader";
	
	static final String Edited_text = "playerScore";
	String text;
	
	// Set up variables
	private TextToSpeech tts;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	
	// The default language
	public static final String lang = "eng";
	
	// Path to the image to be converted to text
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ReadAssist/";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		
		// Add the necessary ocr files to the folder if they are not there
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) 
		{
			File dir = new File(path);
			if (!dir.exists()) 
			{
				if (!dir.mkdirs()) 
				{
					return;
				} 
				else 
				{
				}
			}
		}
		// Add tessdata to ReadAssist the first time the app is run
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) 
		{
			// Get the data required for ocr
			try 
			{
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) 
				{
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				
			} 
			catch (IOException e) 
			{
			}
		}

		// Set the layout
		setContentView(R.layout.activity_perform_ocr);
		
		// Buttons for long clicks
		btn1 = (Button) findViewById(R.id.buttonTranslate);
		btn2 = (Button) findViewById(R.id.buttonEdit);
		btn3 = (Button) findViewById(R.id.buttonRead);
		btn4 = (Button) findViewById(R.id.buttonRetake);
		btn5 = (Button) findViewById(R.id.buttonHistory);
		btn6 = (Button) findViewById(R.id.buttonUpload);
		
		// For long press on buttons
		btn1.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   // Long click for translte button
			   @Override
			   public boolean onLongClick(View v) 
			   {
				    // Indicate a long click
				   Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
					if (tts!=null) 
					{	
						// Read Translate
						String text = "Translate";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		// Long Click for editor button
		btn2.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   @Override
			   public boolean onLongClick(View v) 
			   {
				    // Indicate a long click
				    Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
					if (tts!=null) 
					{	
						// Read Editor out loud
						String text = "Editor";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		btn3.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   @Override
			   public boolean onLongClick(View v) 
			   {
				    // Indicate a long click
				    Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
					if (tts!=null) 
					{	
						// Read out loud
						String text = "Read";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		// long click method for retake button
		btn4.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   @Override
			   public boolean onLongClick(View v) 
			   {
				    // Indicate a long click
				    Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
					if (tts!=null) 
					{	
						// Read retake out loud
						String text = "Retake";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		// Long click method for history
		btn5.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   @Override
			   public boolean onLongClick(View v) 
			   {	
				    // indicate a long click
				    Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
					if (tts!=null) 
					{	
						// Read History out loud
						String text = "History";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		// Long click method for upload
		btn6.setOnLongClickListener(new OnLongClickListener() 
		{	   
			   @Override
			   public boolean onLongClick(View v) 
			   {	
				    // indicate a long click
				    Toast.makeText(PerformOCR.this,"Button long click", Toast.LENGTH_SHORT).show();
				    
					if (tts!=null) 
					{	
						// Read History out loud
						String text = "Upload";
						if (text!=null) 
						{
							if (!tts.isSpeaking()) 
							{
								tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
							}
						}
					}
				   return true;
			   }
		 });
		
		// Initialize text to speech button
		tts = new TextToSpeech(this, this);
		findViewById(R.id.buttonRead).setOnClickListener(this);

		// The actual path of the image
		_path = DATA_PATH + "/img2.jpg";
		
		// Call the method to start ocr
		onPhotoTaken();
	}
	
	// Call Back method to get the Message from Editor activity  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
    	super.onActivityResult(requestCode, resultCode, data);  
                     
        // Check if the request code is same as what is passed to Editor
        if(requestCode==5)  
        {  
        	// Set the TextView to the edited text
        	TextView myAwesomeTextView = (TextView)findViewById(R.id.myAwesomeTextView);
        	myAwesomeTextView.setMovementMethod(new ScrollingMovementMethod());	
        	
        	
            String message=data.getStringExtra("MESSAGE");   
            myAwesomeTextView.setText(message);                  
        }  
    } 
    
    // Start the translate activity
	public void callTranslate(View view) 
	{
		Intent intent = new Intent(this, TranslateActivity.class);
		
		// Pass the text to the translator in the intent
		TextView theOCR = (TextView) findViewById(R.id.myAwesomeTextView);
		String text = theOCR.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, text);
		
		// Start the translator
		startActivity(intent);
	}
	
    // Start the translate activity
	public void upload(View view) 
	{
		Intent intent = new Intent(this, MongoActivity.class);
		
		// Pass the text to the translator in the intent
		TextView theOCR = (TextView) findViewById(R.id.myAwesomeTextView);
		String text = theOCR.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, text);
		
		// Start the translator
		startActivity(intent);
	}
	
    // Start the translate activity
	public void history(View view) 
	{
		Intent intent = new Intent(this, History.class);
		// Start the translator
		startActivity(intent);
	}
	
	// Called when the user clicks the Editor button
	public void openEditor(View view) 
	{
		Intent intent = new Intent(this, Editor.class);
		
		// Pass the text from the picture to the editor
		TextView theOCR = (TextView) findViewById(R.id.myAwesomeTextView);
		String text = theOCR.getText().toString();
		intent.putExtra(EXTRA_MESSAGE2, text);
		
		// Call the method to update TextView with the edited text
		startActivityForResult(intent, 5);
	}
	
	// Method for the retake button at the top
	public void retake(View v)
	{
		// Start the main activity again
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	// Carry out ocr on the stored cropped image
	protected void onPhotoTaken() 
	{
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try 
		{
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

			// Rotate the image if necessary
			int rotate = 0;

			switch (exifOrientation) 
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
			}

			if (rotate != 0) 
			{
				// Getting width and height of the image
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotate Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by the ocr
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} 
		catch (IOException e) 
		{
		}
		
		// Get the text from the image
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		if ( lang.equalsIgnoreCase("eng")) 
		{
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		TextView myAwesomeTextView = (TextView)findViewById(R.id.myAwesomeTextView);

		// If letters were found
		if ( recognizedText.length() != 0 ) 
		{		
			// Set the TextView to display the text
			myAwesomeTextView.setText(recognizedText);
		}
		else
		{
			// If it was unsuccessful
			myAwesomeTextView.setText("Could not generate text from image..");
		}
	}
	
	// Overwrite the default method for the back button
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        // Call the main activity and start again
        startActivity(new Intent(PerformOCR.this, MainActivity.class));
        finish();
    }

	// Method for text to speech button
	@Override
	public void onClick(View v) 
	{
		if (tts!=null) 
		{	
			TextView theOCR = (TextView) findViewById(R.id.myAwesomeTextView);
			String text = theOCR.getText().toString();
			if (text!=null) 
			{
				if (!tts.isSpeaking()) 
				{
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
		}
	}
	
    // Start text to speach engine
	@Override
	public void onInit(int code) 
	{
		if (code==TextToSpeech.SUCCESS) 
		{
			tts.setLanguage(Locale.getDefault());
		} 
		else 
		{
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		// End the text to speech
		if (tts!=null) 
		{
			tts.stop();
            tts.shutdown();
		}
		super.onDestroy();
	}

}
