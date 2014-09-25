package com.reader.assist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import assist.reader.R;

public class History extends Activity 
{

    // YOU NEED TO SETUP YOUR OWN MONGODB DATABASE ON MONGOLAB.COM
    // PUT YOUR API KEY IN STRING RESOURCES
    private String USER_BASE_URL = "https://api.mongolab.com/api/1/databases/readassist/collections/";

    // YOU NEED TO SETUP A TABLE IN MONGOLAB WITH SOME STRUCTURE, LIKE THIS
    public final static String USER_FIRST_NAME = "String";
    
    String message;		
    String username;
    String history;
    
    String print;
    Button mButtonSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        
        Intent intent = getIntent();
        
        // Get the text and set in field
        message = intent.getStringExtra(PerformOCR.EXTRA_MESSAGE2);

        username = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("MYLABEL", "defaultStringIfNothingFound"); 

        
        USER_BASE_URL = USER_BASE_URL + username + "?apiKey=tsisfUKNMc_Iud0sLsZbyZUXHe5rTYNl";
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
                    	
                    	print = getHistory();
                    	
                    	runOnUiThread(new Runnable() 
                    	{  
                            @Override
                            public void run() 
                            {
                            	TextView view = (TextView)findViewById(R.id.displayhistory);
                        		view.setText(print);                           	
                            }
                        });
                    }
                }).start();
            }
        });
    }



    // searches through database adding string to history variable 
    public String getHistory() 
    {
        String uname;
        
    	try 
    	{
            JSONArray users = new JSONArray(getJSONStringFromUrl(USER_BASE_URL));
            
            for(int i = 0; i < users.length(); i++) 
            {
                uname = users.getJSONObject(i).getString(USER_FIRST_NAME);
                history = history + "\n" + "-" + uname.trim();
            }
            
            history = history.replaceFirst("null","");
            return history;
        }
        catch(JSONException e) 
        {
            Toast.makeText(this, "There was an error", Toast.LENGTH_LONG).show();
        }

        return history;
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
    
    // Overwrite the standard back button method 
    @Override
    public void onBackPressed()
    {
    	// Call the PerformOCR activity when back is pressed
        super.onBackPressed(); 
        startActivity(new Intent(History.this, PerformOCR.class));
        finish();
    }


}