package com.assist.read;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.assist.read.R;

import com.edmodo.cropper.CropImageView;

public class Crop extends Activity {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    Bitmap croppedImage;
    String _path;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) 
    {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }
      
    static final String PATH = Environment.getExternalStorageDirectory().toString() + "/ReadAssist/";    

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        
        _path = PATH + "/img.jpg";
        CropImageView cropView = (CropImageView)findViewById(R.id.cropImageView);
        Bitmap bitmap = ShrinkBitmap(_path, 2047, 2047);

        cropView.setImageBitmap(bitmap);
        
        try
        {
		   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		   bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

		   //you can create a new file name "test.jpg" in sdcard folder.
		   File f = new File(PATH + "resized.jpg");
		   f.createNewFile();
		   //write the bytes in file
		   FileOutputStream fo = new FileOutputStream(f);
		   fo.write(bytes.toByteArray());

		   // close the FileOutput
		   fo.close();
        }
		catch(IOException e)
		{		   
		}

        
        //Sets the rotate button
        final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	 final CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });
    }
        

	// Method for the re-take button at the top
	public void crop(View v)
	{
		CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
		croppedImage = cropImageView.getCroppedImage();
        
        try
        {
		   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		   croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

		   //you can create a new file name "test.jpg" in sdcard folder.
		   File f = new File(PATH + "img2.jpg");
		   f.createNewFile();
		   //write the bytes in file
		   FileOutputStream fo = new FileOutputStream(f);
		   fo.write(bytes.toByteArray());

		   // close the FileOutput
		   fo.close();
        }
		catch(IOException e)
		{		   
		}
        
		Intent intent = new Intent(this, PerformOCR.class);
		startActivity(intent);

	}

    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(Crop.this, MainActivity.class));
        finish();
    }
    
    Bitmap ShrinkBitmap(String file, int width, int height)
    {   
       BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
       bmpFactoryOptions.inJustDecodeBounds = true;
       Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        
       int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
       int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
        
       if (heightRatio > 1 || widthRatio > 1)
       {
		    if (heightRatio > widthRatio)
		    {
		    	bmpFactoryOptions.inSampleSize = heightRatio;
		    } 
		    else 
		    {
		    	bmpFactoryOptions.inSampleSize = widthRatio; 
		    }
        }
        
        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
      }
}
