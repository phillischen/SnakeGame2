package com.packt.snake;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import java.util.Map;

public class Score_Activity extends AppCompatActivity {
    private Button shareButton,restartButton, shareImageButton;
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
            score = score+ entry.getKey() + "\t" + entry.getValue()[2]+"\n";
        }
        scoreText.setText(score);

        if (!myAm.adsOff){
            Intent intent = new Intent(Score_Activity.this,AdvertiseActivity.class);
            startActivity(intent);
        }
        setupButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_VIDEO_CODE){
                Uri selectedPhoto = data.getData();
                SharePhoto photo = new SharePhoto.Builder()
                        .setImageUrl(selectedPhoto)
                        .build();
            }
        }
    }

    private void setupButtons(){
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAm.incrementSkin();//for unlocking more skin
                myConnect.saveData();
                Uri imageUri = Uri.parse("");
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("image/*");
                myIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Hey I just shared the score of the Slither Game! Look at what I Have got!");
                startActivity(Intent.createChooser(myIntent, "Send Image"));

            }
        });

//        imageView = (ImageView) findViewById(R.id.imageView);
//        shareImageButton = findViewById(R.id.share_image_button);
//        shareImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap b = Screenshot.takescreenshotRootView(imageView);
//                imageView.setImageBitmap(b);
//                main.setBackgroundColor(Color.parseColor("#999999"));
//
//            }
//        });

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


//        shareImageButton = findViewById(R.id.share_image_button);
//        shareImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                        .setQuote("I have got a super high Score on this Slither.io game!")
//                        .setContentUrl(Uri.parse("http://slither.io/"))
//                        .build();
//                if(ShareDialog.canShow(ShareLinkContent.class)){
//                    shareDialog.show(linkContent);
//                }
//
//
//            }
//        });


        shareImageButton = findViewById(R.id.share_image_button);
        shareImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select video"),REQUEST_VIDEO_CODE);

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
    public void onBackPressed() {
        //super.onBackPressed();
        //do nothing
    }
}
