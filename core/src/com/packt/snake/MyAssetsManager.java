package com.packt.snake;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAssetsManager{
    public final AssetManager manager = new AssetManager();

    //resource
    public final String SNAKEHEAD1 = "snakehead.png";
    public final String SNAKEBODY1 = "skin1.png";
    public final String SNAKEHEAD2 = "snakehead.png";
    public final String SNAKEBODY2 = "skin2.png";
    public final String SNAKEHEAD3 = "snakehead.png";
    public final String SNAKEBODY3 = "skin3.png";
    public final String SNAKEHEAD4 = "snakehead.png";
    public final String SNAKEBODY4 = "skin4.png";
    public final String SNAKEHEAD5 = "snakehead.png";
    public final String SNAKEBODY5 = "skin5.png";
    public final String SNAKEHEAD6 = "snakehead.png";
    public final String SNAKEBODY6 = "skin6.png";

    public final String MAP1 = "map5001.png";
    public final String SKIN = "skin/comic-ui.json";
    public final String SKIN2 = "skin2/flat-earth-ui.json";
    public final String BACKGROUND1 = "bg.png";
    public final String SPEEDUP = "speedup.png";

    //parameters
    public static final int V_WIDTH = 960;
    public static final int V_HEIGHT = 560;
    private static final int POINTS_PER_FOOD = 10;
    public SpriteBatch batch;//only need one, allow all screen to acces
    //public int score = 0;
    //public SocketConnect myConnect;
    public Map<String,int[]> userdata = new HashMap<String,int[]>(); //<username, [skin,speed,score,direction,ready]>
    public String[] userlist;
    public int mapsize;
    public boolean play = false;
    //public SnakeGame game;
    public String mode = "single";
    public String myUsername = "player";
    public boolean newDirection = false;
    public int direction = 0;
    public boolean disconnect = false;
    public String disconnectP = "";
    public int controlMode = 2; //1 - touch; 2 - joystick; 3 - gravity
    public String myheadskin = SNAKEHEAD1, mybodyskin = SNAKEBODY1;
    public boolean serverError = false;


    /////////////////resource loader//////////////////////

    //need to change: load specific texture based on player choice
    public void loadMap(){
        manager.load(MAP1, Texture.class);
    }

    public void loadSnake(int skincode){//load based on skincode
        switch (skincode){
            case 1:
                manager.load(SNAKEHEAD1, Texture.class);
                manager.load(SNAKEBODY1,Texture.class);
                myheadskin = SNAKEHEAD1;
                mybodyskin = SNAKEBODY1;
                break;
            case 2:
                manager.load(SNAKEHEAD2, Texture.class);
                manager.load(SNAKEBODY2,Texture.class);
                myheadskin = SNAKEHEAD2;
                mybodyskin = SNAKEBODY2;
                break;
            case 3:
                manager.load(SNAKEHEAD3, Texture.class);
                manager.load(SNAKEBODY3,Texture.class);
                myheadskin = SNAKEHEAD3;
                mybodyskin = SNAKEBODY3;
                break;
            case 4:
                manager.load(SNAKEHEAD4, Texture.class);
                manager.load(SNAKEBODY4,Texture.class);
                myheadskin = SNAKEHEAD4;
                mybodyskin = SNAKEBODY4;
                break;
            case 5:
                manager.load(SNAKEHEAD5, Texture.class);
                manager.load(SNAKEBODY5,Texture.class);
                myheadskin = SNAKEHEAD5;
                mybodyskin = SNAKEBODY5;
                break;
            case 6:
                manager.load(SNAKEHEAD6, Texture.class);
                manager.load(SNAKEBODY6,Texture.class);
                myheadskin = SNAKEHEAD6;
                mybodyskin = SNAKEBODY6;
                break;
        }

    }

    public void loadSkin(){
        manager.load(SKIN, Skin.class);
        //manager.load(BACKGROUND1,Texture.class);
    }

    public void loadSkin2(){
        manager.load(SKIN2, Skin.class);
    }

    public void loadHubResource(){
        manager.load(SPEEDUP,Texture.class);
    }

    ////////////////parameter modifier////////////////
    public void addScore(String name){
        int[] param = userdata.get(name);
        param[2] = param[2]+POINTS_PER_FOOD;
        userdata.put(name,param);
        //System.out.println(name + "get score! "+param[2]);
    }


    public void setNewDirection(int newd){
        newDirection = true;
        int[] param = userdata.get(myUsername);
        param[3] = newd;
        userdata.put(myUsername,param);
        direction = newd;
    }

    public int getMyDirection(){
        return userdata.get(myUsername)[3];
    }

    public int getRemoteDirection(String name){
        return userdata.get(name)[3];
    }

    public int getvWidth() {
        return V_WIDTH;
    }

    public int getvHeight() {
        return V_HEIGHT;
    }
}
