package com.packt.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class Score_Activity extends AppCompatActivity {
    private Button shareButton,restartButton;
    private TextView scoreText;
    private MyAssetsManager myAm;
    private SocketConnect myConnect;
    private String score = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_);

        myConnect = SocketConnect.get();
        myAm = myConnect.getMyAm();

        getSupportActionBar().hide();

        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do sth to share
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
