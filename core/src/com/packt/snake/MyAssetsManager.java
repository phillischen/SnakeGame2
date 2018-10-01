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
    public final String SNAKEBODY1 = "snakebody.png";
    public final String MAP1 = "border.png";
    public final String SKIN = "skin/comic-ui.json";
    public final String BACKGROUND1 = "bg.png";
    public final String SPEEDUP = "speedup.png";

    //parameters
    public static final int V_WIDTH = 960;
    public static final int V_HEIGHT = 520;
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


    /////////////////resource loader//////////////////////

    //need to change: load specific texture based on player choice
    public void loadMap(){
        manager.load(MAP1, Texture.class);
    }

    public void loadSnake(int skincode){//load based on skincode
        manager.load(SNAKEHEAD1, Texture.class);
        manager.load(SNAKEBODY1,Texture.class);
    }

    public void loadSkin(){
        manager.load(SKIN, Skin.class);
        manager.load(BACKGROUND1,Texture.class);
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


}
