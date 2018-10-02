package com.packt.snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

public class Welcome_Activity extends AppCompatActivity {
    private Button startButton, testButton, multiButton,submitButton,settingButton;
    private SocketConnect myConnect;
    private EditText usernameText;
    private String username = "player";
    //AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
    //AlertDialog alert;

    private boolean waiting = false;
    private String alertMessage = "";
    private int connectResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);
        myConnect = SocketConnect.get();

        getSupportActionBar().hide();
        initiateUI();

    }

    private void initiateUI(){
        usernameText = findViewById(R.id.username_text);

        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                myConnect.setUsername(username);
            }
        });
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, AndroidLauncher.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode","single");
                intent.putExtras(bundle);
                myConnect.dumpSingleData();
                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        });

        testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, Score_Activity.class);

                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        });

        multiButton = findViewById(R.id.multi_button);
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Join().execute();
                //while(true){ alert.setMessage("Connection Failed"); }
            }
        });

        settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_Activity.this, Setting_Activity.class);
                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        });


    }

    class Join extends AsyncTask<String,Void,Integer>{
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Welcome_Activity.this);
        AlertDialog alert;
        CountDownTimer MyTimer;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(String... strings) {


            int response = myConnect.initConnect();
            System.out.println("doinbackground "+response);
            return response;
        }
        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            //alert.cancel();
            MyTimer = new CountDownTimer(1000000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    alert.setMessage(myConnect.getUserlist());
                    //System.out.println("00:"+ (millisUntilFinished/1000));
                }

                @Override
                public void onFinish() {
                    //info.setVisibility(View.GONE);
                }
            };
            mBuilder.setTitle("Waiting for join").setMessage("")
                    .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Welcome_Activity.this, AndroidLauncher.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mode","multi");
                            intent.putExtras(bundle);
                            startActivity(intent);

                            System.out.println("===========quit = "+myConnect.quit);
                            myConnect.setWaiting(true);
                            myConnect.dumpMultiData();
                            //myConnect.dumpSingleData();
                            Welcome_Activity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyTimer.cancel();
                            myConnect.setQuit(true);
                            System.out.println("===========cancel is triggerd");
                            dialog.dismiss();
                            myConnect.renewSocket();
                            myConnect = SocketConnect.get();
                            myConnect.setUsername(username);

                        }
                    }).setCancelable(false);
            alert = mBuilder.create();
            alert.show();
            //System.out.println("dialog show");
            //alert.setMessage(myConnect.getUserlist());

            if (response == 1){
                //alertMessage = ;
                System.out.println("response = 1");
                alert.setMessage(myConnect.getUserlist());
                alertMessage = myConnect.getUserlist();
                //myConnect.setWaiting(true);
                myConnect.start();
                MyTimer.start();
                System.out.println("thread started");
            } else {
                //alertMessage = "Connection Failed";
                alert.setMessage("Connection Failed");
                System.out.println("response != 1");
            }


        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //do nothing
    }

    @Override
    protected void onStop() {
        //System.out.println("on stop is called!");
        //myConnect.setQuit(true);
        super.onStop();
    }

}
