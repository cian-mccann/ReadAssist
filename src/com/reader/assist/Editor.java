package com.reader.assist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import assist.reader.R;

public class Editor extends Activity implements OnSeekBarChangeListener 
{
	private EditText editTextInput; // Text field
	ClipboardManager clipboard; // For copying and pasting
	
    private SeekBar SizeBar; // Seek bar object variable
    private TextView SizrProgress; // Text label objects
	
 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState); // Store the state
        setContentView(R.layout.editor_layout); // Set the layout
        
        // Initialize up the seekbar
        SizeBar = (SeekBar)findViewById(R.id.PRICEseekBarID); 
        SizeBar.setOnSeekBarChangeListener(this); 
        
        SizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
        {
        	// Methods the seekbar must implement
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) 
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) 
            {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
            {
            	// Set up the TextView to be modified by the seekbar
                SizrProgress = (TextView)findViewById(R.id.PRICEtextViewProgressID);
                // Set the new text size
                SizrProgress.setText("Text Size: "+progress);
                EditText editTextInput = (EditText)findViewById(R.id.editTextInput);
                editTextInput.setTextSize(progress);
            }
        });
        
        // Set up the TextView with the OCR text
        editTextInput = (EditText) findViewById(R.id.editTextInput);
        Intent intent = getIntent();
        
        // Get the text and set in field
        String message = intent.getStringExtra(PerformOCR.EXTRA_MESSAGE2);
		editTextInput.setText(message, TextView.BufferType.EDITABLE);    
    }
    
    // Google search button
    public void onSearchClick(View v)
    {
    	try 
    	{
    		// Call the intent and pass the text
    		 Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
             String term = editTextInput.getText().toString();
             intent.putExtra(SearchManager.QUERY, term);
             startActivity(intent);
		} 
    	catch (Exception e) 
    	{
		}
    }
    	
    // Called by copy button
    public void copyToClipboard(View v)
    {
    	// Convert the edited text to a string
    	EditText editText = (EditText)findViewById(R.id.editTextInput);
    	String message = editText.getText().toString();
    	
    	// Move the string to the clip board
    	clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	ClipData clip = ClipData.newPlainText("label",message);
    	clipboard.setPrimaryClip(clip);
    }
        
    // Paste into editor from clip board
    public void paste(View v)
    {	
    	try // Make sure its a string
    	{
	    	// Get item from clip board
	    	CharSequence pasteData="";
	    	ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
	    	pasteData = item.getText();
	    	
	    	// The new text to paste
	    	String newS = pasteData.toString();
	 
	    	// The original text which should be kept
	    	EditText editText = (EditText)findViewById(R.id.editTextInput);
	    	String oldS = editText.getText().toString();
	
	    	// Update the text field
	    	editText.setText(newS+oldS, TextView.BufferType.EDITABLE);	
    	}
    	catch(Exception e)
    	{
    	}
    }
      
    // Return and replace the ocr text with the edited text
    public void finishEdit(View v)
    {
    	// Take the edited text
    	EditText editText = (EditText)findViewById(R.id.editTextInput);
    	String message = editText.getText().toString();
    	
    	// Set up indent to store the text
        Intent intent=new Intent();  
        intent.putExtra("MESSAGE",message);  
        
        // Call the indent with result code 5 so the activity knows how to behave
        setResult(5,intent);  
          
        finish();// Finish activity
    }

    // Update from seek bar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
    {
        if (seekBar == SizeBar)
        {
            SizrProgress.setText(""+progress);
        }
    }
    
    // Methods which must be implemented which we dont edit
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) 
    {
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) 
    {
    }
        
    // Overwrite the standard back button method 
    @Override
    public void onBackPressed()
    {
    	// Call the PerformOCR activity when back is pressed
        super.onBackPressed(); 
        startActivity(new Intent(Editor.this, PerformOCR.class));
        finish();
    }
}


