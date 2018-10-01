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
import android.widget.TextView;

import java.util.Map;

public class Score_Activity extends AppCompatActivity {
    private Button shareButton,restartButton, shareImageButton;
    private TextView scoreText;
    private MyAssetsManager myAm;
    private SocketConnect myConnect;
    private String score = "";
    private ImageView imageView;
    private View main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_);
        main = findViewById(R.id.main);

        myConnect = SocketConnect.get();
        myAm = myConnect.getMyAm();

        getSupportActionBar().hide();

//        shareButton = findViewById(R.id.share_button);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("text/plain");
//                String shareBody = "Hey guys I've got xx score on Slither Game! Awesome!";
//                String shareSub = "Slither Game Score!";
//                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(myIntent, "Share using"));
//
//            }
//        });

        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.parse("");
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("image/*");
                myIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Hey I just shared the score of the Slither Game! Look at what I Have got!");
                startActivity(Intent.createChooser(myIntent, "Send Image"));

            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        shareImageButton = findViewById(R.id.share_image_button);
        shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = Screenshot.takescreenshotRootView(imageView);
                imageView.setImageBitmap(b);
                main.setBackgroundColor(Color.parseColor("#999999"));

            }
        });

        restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myAm.resetData();
                myConnect.quit = true;
                myAm.manager.clear();
                myConnect.renewSocket();
                Intent intent = new Intent(Score_Activity.this, Welcome_Activity.class);
                startActivity(intent);
                Score_Activity.this.finish();
            }
        });

        scoreText = findViewById(R.id.score_text);
        for (Map.Entry<String, int[]> entry : myAm.userdata.entrySet()) {
            score = score+ entry.getKey() + "\t" + entry.getValue()[2]+"\n";
        }
        scoreText.setText(score);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //do nothing
    }
}
