package com.tathink.brooch.brooch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetPic extends Activity {

    private static final int SELECT_PICTURE = 1;
    String selectedImagePath;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private int FLAG = 0;
    public static final int REQUEST_CODE_PICKALBUM = 101;
    private String mImgPath;
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    String TAG = "HELLO SEJIN~~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_pic);

        imageView1 = (ImageView) findViewById(R.id.setPicimageView1);
        imageView2 = (ImageView) findViewById(R.id.setPicimageView2);
        imageView3 = (ImageView) findViewById(R.id.setPicimageView3);
        imageView4 = (ImageView) findViewById(R.id.setPicimageView4);
        imageView5 = (ImageView) findViewById(R.id.setPicimageView5);

        //mashowmallow permission////////////////////////////////////////////////////////////////////
        checkPermission();
        //mashowmallow permission////////////////////////////////////////////////////////////////////

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetPic.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //ImageView 버튼 처리
        View mViewPic1 = findViewById(R.id.setPicimageView1);
        mViewPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 1;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);
            }
        });

        //ImageView 버튼 처리
        View mViewPic2 = findViewById(R.id.setPicimageView2);
        mViewPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 2;
                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent2, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);
            }
        });

        //ImageView 버튼 처리
        View mViewPic3 = findViewById(R.id.setPicimageView3);
        mViewPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 3;
                Intent intent3 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent3.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent3, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);
            }
        });

        //ImageView 버튼 처리
        View mViewPic4 = findViewById(R.id.setPicimageView4);
        mViewPic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 4;
                Intent intent4 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent4.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent4, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);
            }
        });

        //ImageView 버튼 처리
        View mViewPic5 = findViewById(R.id.setPicimageView5);
        mViewPic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                FLAG = 5;
                Intent intent5 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent5.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(intent5, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);
            }
        });

        ((Button)findViewById(R.id.setpic_btn_nexttime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetPic.this, SetText.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.setpic_btn_reg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetPic.this, SetText.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKALBUM) {
            if (resultCode == RESULT_OK) {
                // 앨범인 경우
                Uri mImageUri = data.getData();

                // 이미지 Path 취득
                mImgPath = getPath(mImageUri);
                updateImageView();
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void updateImageView() {
        int degree = ImageUtil.GetExifOrientation(mImgPath);
        Bitmap resizeBitmap = ImageUtil.loadBackgroundBitmap(SetPic.this, mImgPath);
        Bitmap rotateBitmap = ImageUtil.GetRotatedBitmap(resizeBitmap, degree);
        Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(rotateBitmap);

        if(FLAG == 1) {
            imageView1.setImageBitmap(roundBitmap);
        } else if(FLAG == 2) {
            imageView2.setImageBitmap(roundBitmap);
        } else if(FLAG == 3) {
            imageView3.setImageBitmap(roundBitmap);
        } else if(FLAG == 4) {
            imageView4.setImageBitmap(roundBitmap);
        } else if(FLAG == 5) {
            imageView5.setImageBitmap(roundBitmap);
        }

        resizeBitmap.recycle();
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 사용자가 임의로 권한을 취소시킨 경우
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                // 권한 요청 (최초 요청)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 동의 및 로직 처리
                    Log.e(TAG, ">>> 동의함.");
                } else {
                    // 동의 안함
                    Log.e(TAG, ">>> 동의를 해주셔야 합니다.");
                }
                return;
            }
            // 예외 케이스
        }
    }

}
