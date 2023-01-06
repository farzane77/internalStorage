package com.example.espinas.eminternals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    Bitmap yourSelectedImage;
    private static final int select_photo=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission();
        imageView=(ImageView)findViewById(R.id.imageView);
        button=(Button)findViewById(R.id.btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent,select_photo);
                }catch (Exception x){
                    Toast.makeText(getApplicationContext(),"error choose image",Toast.LENGTH_LONG).show();
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path= Environment.getExternalStorageDirectory();
                File dir=new File(path+"/AppChooseImage");
                dir.mkdir();
                File file=new File(dir,"Ax.png");

                try {
                    OutputStream outputStream=new FileOutputStream(file);
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode){
            case select_photo:
                if(resultCode==RESULT_OK){
                    Uri selectImage=imageReturnedIntent.getData();
                    InputStream inputStream=null;
                    try{
                        inputStream=getContentResolver().openInputStream(selectImage);
                    }catch (Exception x){
                        Toast.makeText(getApplicationContext(),"error get file",Toast.LENGTH_LONG).show();
                    }
                    yourSelectedImage= BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(yourSelectedImage);
                }
        }
    }
    final private int request_code=123;
    public void permission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},request_code);
            }
        }
    }
}
