package com.packt.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Setting_Activity extends AppCompatActivity {
    private Button backbutton;
    private RadioGroup skins;
    private int skinno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        getSupportActionBar().hide();

        backbutton = findViewById(R.id.back_botton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting_Activity.this,Welcome_Activity.class);
                startActivity(intent);
            }
        });

        skins = findViewById(R.id.skingroup);
        skins.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_Skin1:
                        skinno = 1;
                        break;
                    case R.id.radio_Skin2:
                        skinno = 2;
                        break;
                    case R.id.radio_Skin3:
                        skinno = 3;
                        break;
                    case R.id.radio_Skin4:
                        skinno = 4;
                        break;
                    case R.id.radio_Skin5:
                        skinno = 5;
                        break;
                    case R.id.radio_Skin6:
                        skinno = 6;
                        break;
                    default:
                        skinno = 1;
                        break;
                }
            }

        });

    }



}
