package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetPic extends Activity {

    private static final int    SELECT_PICTURE = 1;
    String selectedImagePath;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private int FLAG = 0;
    public static final int REQUEST_CAMERA = 9000;
    public static final int REQUEST_GALLERY = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_pic);

        imageView1 = (ImageView)findViewById(R.id.setPicimageView1);
        imageView2 = (ImageView)findViewById(R.id.setPicimageView2);
        imageView3 = (ImageView)findViewById(R.id.setPicimageView3);
        imageView4 = (ImageView)findViewById(R.id.setPicimageView4);
        imageView5 = (ImageView)findViewById(R.id.setPicimageView5);

        //ImageView 버튼 처리
        View mViewPic1 = findViewById(R.id.setPicimageView1);
        mViewPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 1;
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);    //only 1 get image
                startActivityForResult(intent1, SELECT_PICTURE);
            }
        });

        View mViewPic2 = findViewById(R.id.setPicimageView2);
        mViewPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 2;
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);    //only 1 get image
                startActivityForResult(intent2, SELECT_PICTURE);
            }
        });

        View mViewPic3 = findViewById(R.id.setPicimageView3);
        mViewPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 3;
                Intent intent3 = new Intent();
                intent3.setType("image/*");
                intent3.setAction(Intent.ACTION_GET_CONTENT);    //only 1 get image
                startActivityForResult(intent3, SELECT_PICTURE);
            }
        });

        View mViewPic4 = findViewById(R.id.setPicimageView4);
        mViewPic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 4;
                Intent intent4 = new Intent();
                intent4.setType("image/*");
                intent4.setAction(Intent.ACTION_GET_CONTENT);    //only 1 get image
                startActivityForResult(intent4, SELECT_PICTURE);
            }
        });

        View mViewPic5 = findViewById(R.id.setPicimageView5);
        mViewPic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 5;
                Intent intent5 = new Intent();
                intent5.setType("image/*");
                intent5.setAction(Intent.ACTION_GET_CONTENT);    //only 1 get image
                startActivityForResult(intent5, SELECT_PICTURE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);

                    if(FLAG == 1) {
                        imageView1.setImageBitmap(bitmap);
                    } else if(FLAG == 2){
                        imageView2.setImageBitmap(bitmap);
                    } else if(FLAG == 3){
                        imageView3.setImageBitmap(bitmap);
                    } else if(FLAG == 4){
                        imageView4.setImageBitmap(bitmap);
                    } else if(FLAG == 5){
                        imageView5.setImageBitmap(bitmap);
                    }

                    String sjTag = "xxxxxxxxxxxxxxxxxxxxxx";
                    Log.i(sjTag, selectedImagePath.toString());
                }
                else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        /*
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        */

                        AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(selectedImageUri, "r");
                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inSampleSize = 4;
                        Bitmap image = BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, opt);

                        //rotate image////////////////////////////////////////////////////////////////////////////////////////////////////////
                        selectedImagePath = selectedImageUri.getPath();
                        ExifInterface exif = new ExifInterface(selectedImagePath);
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegree(exifOrientation);
                        image = rotate(image, exifDegree);
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                        if(FLAG == 1) {
                            imageView1.setImageBitmap(image);
                        } else if(FLAG == 2){
                            imageView2.setImageBitmap(image);
                        } else if(FLAG == 3){
                            imageView3.setImageBitmap(image);
                        } else if(FLAG == 4){
                            imageView4.setImageBitmap(image);
                        } else if(FLAG == 5){
                            imageView5.setImageBitmap(image);
                        }

                        String sjTag = "yyyyyyyyyyyyyyyyyyyyyy";
                        Log.i(sjTag, selectedImagePath.toString());



                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static int exifOrientationToDegree(int exifOrientation){
        int value = 0;
        String TAG = "HELLO WORLD";
        Log.d(TAG, Integer.toString(exifOrientation));
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            Log.i(TAG, "ORIENTATION_ROTATE_90");
            value = 90;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            Log.i(TAG, "ORIENTATION_ROTATE_180");
            value = 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            Log.i(TAG, "ORIENTATION_ROTATE_270");
            value = 270;
        }
        return value;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree){
        if(degree != 0 && bitmap != null){
            Matrix m = new Matrix();
            m.setRotate(degree, (float)bitmap.getWidth() / 2, (float)bitmap.getHeight() / 2);

            try{
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted){
                    bitmap.recycle();;
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex){
                ex.printStackTrace();
            }
        }
        return bitmap;
    }
}
