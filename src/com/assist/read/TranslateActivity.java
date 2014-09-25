package com.assist.read;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.assist.read.R;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TranslateActivity extends Activity implements OnInitListener{

	Language[] languages = Language.values();
	private TextToSpeech tts;
	
	Spinner EnterLan, TransLan;
	ImageView send, speakEntered, speakTranslated;
	TextView languageEntered, textEntered, languageTranslated, textTranslated;
	TextView userText;
	ProgressBar loading;
	
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setContentView(R.layout.activity_translate);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(PerformOCR.EXTRA_MESSAGE);
		
		TextView textToBeTranslated = (TextView)findViewById(R.id.etEnteredText);
		textToBeTranslated.setText(message);
		
		tts = new TextToSpeech(this, this);
		
		initViews();
	}
	
	public void initViews(){
		//work with spinners
		EnterLan = (Spinner) findViewById(R.id.sTobTranslated);
		TransLan = (Spinner) findViewById(R.id.sTranslateto);
		
		EnterLan.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, GetAllValues()));
		
		TransLan.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, GetAllValues()));
		TransLan.setSelection(13);
		
		send = (ImageView) findViewById(R.id.ivSend);
		speakEntered = (ImageView) findViewById(R.id.ivSpeakEntered);
		speakTranslated = (ImageView) findViewById(R.id.ivSpeakTranslated);
		
		languageEntered = (TextView) findViewById(R.id.tvLanguageEntered);
		textEntered = (TextView) findViewById(R.id.tvTextEntered);
		languageTranslated = (TextView) findViewById(R.id.tvLanguageTranslated);
		textTranslated = (TextView) findViewById(R.id.tvTextTranslated);
		
		userText = (TextView) findViewById(R.id.etEnteredText);
	
		speakEntered.setVisibility(ImageView.INVISIBLE);
		speakTranslated.setVisibility(ImageView.INVISIBLE);
		
		languageEntered.setVisibility(TextView.INVISIBLE);
		languageTranslated.setVisibility(TextView.INVISIBLE);
		textEntered.setVisibility(TextView.INVISIBLE);
		textTranslated.setVisibility(TextView.INVISIBLE);
		
		loading = (ProgressBar) findViewById(R.id.pbLoading);
		loading.setVisibility(ProgressBar.INVISIBLE);
		
		speakEntered.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				SpeakText(textEntered.getText().toString());
			}
		});
		
		speakTranslated.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				SpeakText(textTranslated.getText().toString());
			}
		});

		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
					class bgStuff extends AsyncTask<Void, Void, Void>{
						
						String translatedText = "";
						
						
						@Override
						protected void onPreExecute() {
							// Auto-generated method stub
							loading.setVisibility(ProgressBar.VISIBLE);
							super.onPreExecute();
						}

						@Override
						protected Void doInBackground(Void... params) {
							// Auto-generated method stub
							try {
								translatedText = translateText();
							} catch (Exception e) {
								// Auto-generated catch block
								translatedText = "Unable to translate";
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							// Auto-generated method stub
							speakEntered.setVisibility(ImageView.VISIBLE);
							speakTranslated.setVisibility(ImageView.VISIBLE);
							
							languageEntered.setVisibility(TextView.VISIBLE);
							languageTranslated.setVisibility(TextView.VISIBLE);
							textEntered.setVisibility(TextView.VISIBLE);
							textTranslated.setVisibility(TextView.VISIBLE);
							
							textEntered.setText(userText.getText().toString());
							textTranslated.setText(translatedText);
							
							languageEntered.setText(detectedLanguage);
							languageTranslated.setText(languages[TransLan.getSelectedItemPosition()].name());
							loading.setVisibility(ProgressBar.INVISIBLE);
							super.onPostExecute(result);
						}
						
					}
					
					new bgStuff().execute();
			}
		});
	}
	
	String detectedLanguage = "";
	
	public String translateText() throws Exception{
		// Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
	       Translate.setClientId("ReadAssist");//CHANGE THIS
	       Translate.setClientSecret("xUPpjgdm9xfmWT/I3vtyHjjT+sibh5GroJCBgZnkuIE="); //CHANGE THIS
	       
	       String translatedText = Translate.execute(userText.getText().toString(),languages[EnterLan.getSelectedItemPosition()], languages[TransLan.getSelectedItemPosition()]);
		   
	       Language detectedLanguage = Detect.execute(userText.getText().toString());
	       this.detectedLanguage = detectedLanguage.getName(Language.ENGLISH);
	       
			return translatedText;
	}
	public String[] GetAllValues(){
		String lan[] = new String[languages.length];
		for(int i = 0; i < languages.length; i++){
			lan[i] = languages[i].name();
		}
		return lan;
	}
	
	public void playSound(){
		MediaPlayer player = new MediaPlayer();
		try {
			player.setVolume(10, 10);
			player.setDataSource("http://api.microsofttranslator.com/V2/http.svc/Speak?appId=Bearer+http%253a%252f%252fschemas.xmlsoap.org%252fws%252f2005%252f05%252fidentity%252fclaims%252fnameidentifier%3dgilokimu%26http%253a%252f%252fschemas.microsoft.com%252faccesscontrolservice%252f2010%252f07%252fclaims%252fidentityprovider%3dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26Audience%3dhttp%253a%252f%252fapi.microsofttranslator.com%26ExpiresOn%3d1360142778%26Issuer%3dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26HMACSHA256%3dBzK2I18ZSFu0lkV88oCNZUDZzt9QwmVaaDLQKyhhpjs%253d&text=Did+you+enjoy+the+2011+Cricket+World+Cup%3f&language=en-in");
			player.start();
		} catch (IllegalArgumentException e) {
			// Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		} catch (SecurityException e) {
			// Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IllegalStateException e) {
			// Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(TranslateActivity.this, PerformOCR.class));
        finish();
    }
	
	public void SpeakText(String text){
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			 
	        int result = tts.setLanguage(Locale.US);

	        if (result == TextToSpeech.LANG_MISSING_DATA
	                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	            Log.e("TTS", "This Language is not supported");
	        } else {
	            
	            //speakOut("Ich");
	        }

	    } else {
	        Log.e("TTS", "Initilization Failed!");
	    }
	}
}
