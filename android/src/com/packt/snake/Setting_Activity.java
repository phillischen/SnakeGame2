package com.packt.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Setting_Activity extends AppCompatActivity {
    private Button backbutton,submitButton;
    private EditText usernameText;
    private String username = "player";
    private RadioGroup skins;
    private RadioGroup controls;
    private Switch adsswitch;
    private int skinno;
    private int controlno;
    private boolean adsOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        getSupportActionBar().hide();

        adsswitch = (Switch) findViewById(R.id.switch_noads);
        adsswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                adsOff = isChecked;
            }
        });


        backbutton = findViewById(R.id.back_botton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting_Activity.this,Welcome_Activity.class);
                startActivity(intent);
            }
        });

        usernameText = findViewById(R.id.text_name);

        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
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


        controls = findViewById(R.id.controlgroup);
        controls.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_gravity:
                        controlno = 3;
                        break;
                    case R.id.radio_handle:
                        controlno = 2;
                        break;
                    case R.id.radio_touch:
                        controlno = 1;
                        break;
                    default:
                        controlno = 1;
                        break;
                }
            }

        });





    }


}
