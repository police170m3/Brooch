package com.tathink.brooch.brooch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import kr.mint.testbluetoothspp.BTService;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetPic extends Activity {
    public boolean prefSave = false;
    public String pic1 = "", pic2 = "", pic3 = "", pic4 = "", pic5 = "";

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

        //프리퍼런스 값 읽어오기
        getPreferences();
        if(pic1 != "" || pic2 != "" || pic3 != "" || pic4 != "" || pic5 != ""){
            loadImageView();
        }

        //mashowmallow permission////////////////////////////////////////////////////////////////////
        checkPermission();
        //mashowmallow permission////////////////////////////////////////////////////////////////////

        //이전, 다음 버튼 처리////////////////////////////////////////////////////////////////////// sejin 2016.11.02
        Button previousButton = (Button) findViewById(R.id.previous_btn);
        Button nextButton = (Button) findViewById(R.id.next_btn);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        //이전, 다음 버튼 처리//////////////////////////////////////////////////////////////////////

        //home 버튼 처리
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetPic.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

            //이전, 다음 버튼 활성화 및 이벤트 처리///////////////////////// sejin 2016.11.02
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            //이전 이벤트 처리
            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    BTService.FREE_PASS = true;
                    Intent i = new Intent(SetPic.this, SetRageVoice.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //다음 이벤트 처리
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetPic.this, SetText.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //이전, 다음 버튼 활성화 및 이벤트 처리/////////////////////////
        }

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
                savePreferences(pic1, pic2, pic3, pic4, pic5);  //프리퍼런스 저장
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

                //이미지 Path 저장
                switch (FLAG){
                    case 1:
                        pic1 = mImgPath;
                        break;
                    case 2:
                        pic2 = mImgPath;
                        break;
                    case 3:
                        pic3 = mImgPath;
                        break;
                    case 4:
                        pic4 = mImgPath;
                        break;
                    case 5:
                        pic5 = mImgPath;
                        break;
                }
                //이미지 Path 저장

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

    private  void loadImageView(){
        if(pic1 != ""){
            int degree1 = ImageUtil.GetExifOrientation(pic1);
            Bitmap resizeBitmap1 = ImageUtil.loadBackgroundBitmap(SetPic.this, pic1);
            Bitmap rotateBitmap1 = ImageUtil.GetRotatedBitmap(resizeBitmap1, degree1);
            Bitmap roundBitmap1 = ImageUtil.getRoundedCornerBitmap(rotateBitmap1);
            imageView1.setImageBitmap(roundBitmap1);
        }
        if(pic2 != ""){
            int degree2 = ImageUtil.GetExifOrientation(pic2);
            Bitmap resizeBitmap2 = ImageUtil.loadBackgroundBitmap(SetPic.this, pic2);
            Bitmap rotateBitmap2 = ImageUtil.GetRotatedBitmap(resizeBitmap2, degree2);
            Bitmap roundBitmap2 = ImageUtil.getRoundedCornerBitmap(rotateBitmap2);
            imageView2.setImageBitmap(roundBitmap2);
        }
        if(pic3 != ""){
            int degree3 = ImageUtil.GetExifOrientation(pic3);
            Bitmap resizeBitmap3 = ImageUtil.loadBackgroundBitmap(SetPic.this, pic3);
            Bitmap rotateBitmap3 = ImageUtil.GetRotatedBitmap(resizeBitmap3, degree3);
            Bitmap roundBitmap3 = ImageUtil.getRoundedCornerBitmap(rotateBitmap3);
            imageView3.setImageBitmap(roundBitmap3);
        }
        if(pic4 != ""){
            int degree4 = ImageUtil.GetExifOrientation(pic4);
            Bitmap resizeBitmap4 = ImageUtil.loadBackgroundBitmap(SetPic.this, pic4);
            Bitmap rotateBitmap4 = ImageUtil.GetRotatedBitmap(resizeBitmap4, degree4);
            Bitmap roundBitmap4 = ImageUtil.getRoundedCornerBitmap(rotateBitmap4);
            imageView4.setImageBitmap(roundBitmap4);
        }
        if(pic5 != ""){
            int degree5 = ImageUtil.GetExifOrientation(pic5);
            Bitmap resizeBitmap5 = ImageUtil.loadBackgroundBitmap(SetPic.this, pic5);
            Bitmap rotateBitmap5 = ImageUtil.GetRotatedBitmap(resizeBitmap5, degree5);
            Bitmap roundBitmap5 = ImageUtil.getRoundedCornerBitmap(rotateBitmap5);
            imageView5.setImageBitmap(roundBitmap5);
        }
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

    private void savePreferences(String pic1, String pic2, String pic3, String pic4, String pic5){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pic1", pic1);
        editor.putString("pic2", pic2);
        editor.putString("pic3", pic3);
        editor.putString("pic4", pic4);
        editor.putString("pic5", pic5);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pic1 = pref.getString("pic1", "");
        pic2 = pref.getString("pic2", "");
        pic3 = pref.getString("pic3", "");
        pic4 = pref.getString("pic4", "");
        pic5 = pref.getString("pic5", "");
        prefSave = pref.getBoolean("prefSave", false);
    }

}
