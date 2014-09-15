package assist.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MongoActivity extends Activity {

    // Points to Database stored on mongolabs
    private String USER_BASE_URL = "https://api.mongolab.com/api/1/databases/readassist/collections/";

    // "String" is the name of the document stored in each collection
    public final static String USER_HISTORY = "String";
    
    String message;
    
    String username;

    EditText textToBeUploaded;
    Button mButtonCancel;
    Button mButtonSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mongo_layout);
        
        Intent intent = getIntent();
        
        // Get the text and set in field
        message = intent.getStringExtra(PerformOCR.EXTRA_MESSAGE2);
        
        //username = readUsername();
        username = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("MYLABEL", "defaultStringIfNothingFound"); 
        USER_BASE_URL += username+ "?apiKey=" + getResources().getString(R.string.mongolab_api_key);

        textToBeUploaded = (EditText) findViewById(R.id.textToUpload);
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) 
            {
                new Thread(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        JSONObject user = new JSONObject();

                        try 
                        {
                            user.put(USER_HISTORY, textToBeUploaded.getText().toString());
                                                     
                            if(postData(user)) {
                                clearFields();
                            }
                        }
                        catch(JSONException e) {}
                    }
                }).start();
            }
        });

        textToBeUploaded.setText(message);
    }

    public void clearFields() 
    {
        runOnUiThread(new Runnable() 
        {
            @Override
            public void run() 
            {
            	textToBeUploaded.setText("");
            }
        });
    }

    public String getJSONStringFromUrl(final String url) 
    {

        final StringBuffer sb = new StringBuffer("");

        try 
        {
            BufferedReader in = null;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            final int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == 200) 
            {

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                String NL = System.getProperty("line.separator");

                while ((line = in.readLine()) != null) 
                {
                    sb.append(line + NL);
                }

                in.close();
            }
        }
        catch(Exception e) {}

        return sb.toString();
    }

    public boolean postData(final JSONObject data) 
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(USER_BASE_URL);

        boolean posted = false;

        try 
        {
            HttpResponse response;

            StringEntity params = new StringEntity(data.toString());

            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(params);

            response = httpclient.execute(httppost);
            @SuppressWarnings("unused")
            String k = response.getStatusLine().toString();

            posted = true;

        }
        catch (ClientProtocolException e) 
        {
            Toast.makeText(getApplicationContext(), "There was an with the connection error", Toast.LENGTH_LONG).show();
        } 
        catch (Exception e) 
        {
            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_LONG).show();
        }

        return posted;
    }
    
    // Overwrite the standard back button method 
    @Override
    public void onBackPressed()
    {
    	// Call the PerformOCR activity when back is pressed
        super.onBackPressed(); 
        startActivity(new Intent(MongoActivity.this, PerformOCR.class));
        finish();
    }
}