package com.packt.snake;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Score_Activity extends AppCompatActivity {
    private Button shareButton, restartButton, shareImageButton;
    public static final int REQUEST_VIDEO_CODE = 1000;
    private TextView scoreText;
    private MyAssetsManager myAm;
    private SocketConnect myConnect;
    private String score = "";
    private ImageView imageView;
    private View main;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplication());
        setContentView(R.layout.activity_score_);
        main = findViewById(R.id.main);

        myConnect = SocketConnect.get();
        myAm = myConnect.getMyAm();
        myConnect.quit = true;

        getSupportActionBar().hide();

        scoreText = findViewById(R.id.score_text);
        for (Map.Entry<String, int[]> entry : myAm.userdata.entrySet()) {
            score = score + entry.getKey() + "\t" + entry.getValue()[2] + "\n";
        }
        scoreText.setText(score);

        if (!myAm.adsOff) {
            Intent intent = new Intent(Score_Activity.this, AdvertiseActivity.class);
            startActivity(intent);
        }
        setupButtons();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE && data != null && data.getData() != null) {
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(sharePhotoContent);
                }
            }
        }
    }
//
//    private void setupButtons(){
//        shareButton = findViewById(R.id.share_button);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                myAm.incrementSkin();//for unlocking more skin
//                myConnect.saveData();
//                Uri imageUri = Uri.parse("");
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("image/*");
//                myIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                myIntent.putExtra(Intent.EXTRA_TEXT, "Hey I just shared the score of the Slither Game! Look at what I Have got!");
//                startActivity(Intent.createChooser(myIntent, "Send Image"));
//
//            }
//        });

    private void setupButtons() {
            imageView = (ImageView) findViewById(R.id.imageView);
            shareButton = findViewById(R.id.share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAm.incrementSkin();//for unlocking more skin
                    myConnect.saveData();
                    capture(imageView);

                }

            });
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);

            shareImageButton = findViewById(R.id.share_image_button);
            shareImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(Score_Activity.this, "Share successul!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(Score_Activity.this, "Share cancel!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Toast.makeText(Score_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_VIDEO_CODE);
                }
            });

            restartButton = findViewById(R.id.restart_button);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //myAm.resetData();

                    myAm.manager.clear();
                    //myConnect.renewSocket();
                    Intent intent = new Intent(Score_Activity.this, Welcome_Activity.class);
                    startActivity(intent);
                    Score_Activity.this.finish();
                }
            });
        }

        @Override
        public void onBackPressed () {
            //super.onBackPressed();
            //do nothing
        }

        public void capture(View v){
            ActivityCompat.requestPermissions(Score_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YY-MM-DD-HH-MM-SS");

            String filePathName= Environment.getExternalStorageDirectory()+"/"+simpleDateFormat.format(new Date())+".png";
            View view=v.getRootView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bitmap=view.getDrawingCache();
            try{
                System.out.println("File Name"+ filePathName);
                FileOutputStream fileOutputStream=new FileOutputStream(filePathName);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this,"Capture Succeed",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,"Capture Failed",Toast.LENGTH_SHORT).show();
            }

        }


    }

