package com.everestadvanced.frantcameraimage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class MainActivity extends AppCompatActivity {

    public ImageView imgback;
    public String dir;
    public  File newdir;
    public Button btnback;
    private static final int CAMERA_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Back camera image
        imgback= (ImageView) findViewById(R.id.myImage);
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/iPay_Images/";
        newdir = new File(dir);
        newdir.mkdirs();

        btnback = (Button) findViewById(R.id.btnCapture);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            imgback.setImageBitmap(thumbnail);

            //Store images to internal storage
            storeImage(thumbnail);
        }
    }

    private void storeImage(Bitmap image) {

        File pictureFile = getOutputMediaFile();

        if (pictureFile == null)
        {
            Toast.makeText(getApplicationContext(),"Error creating media file, check storage permissions:",Toast.LENGTH_LONG).show();
            return;
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(getApplicationContext(),"File not found:" + e.getMessage(),Toast.LENGTH_LONG).show();

        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),"Error accessing file:"+ e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private  File getOutputMediaFile() {


        if (! newdir.exists())
        {
            if (! newdir.mkdirs())
            {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName=timeStamp +".jpg";
        mediaFile = new File(newdir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
