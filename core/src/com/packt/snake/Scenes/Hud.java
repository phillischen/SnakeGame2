package com.packt.snake.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;
import java.util.Map;

public class Hud implements Disposable{
    public Stage stage;//a box to put things in
    private Viewport viewport;//an independent vp for hud

    private Integer worldTimer;
    private float timeCount;
    //private int score;
    private OrthographicCamera camera;
    private Skin myskin;
    private MyAssetsManager myAm;
    private int roomsize;
    private ArrayList<Label> nameLabelList = new ArrayList<Label>();
    private ArrayList<Label> scoreLabelList = new ArrayList<Label>();
    private ArrayList<String> nameList = new ArrayList<String>();


    //Label scoreLable;
    //Label snakeLabel;

    public Hud(MyAssetsManager manager){
        //worldTimer = 300;
        //timeCount = 0;
        myAm = manager;
        roomsize = myAm.userdata.size();

        //myskin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        myAm.loadSkin();
        myAm.manager.finishLoading();
        myskin = myAm.manager.get(myAm.SKIN);

        viewport = new FitViewport(myAm.V_WIDTH,myAm.V_HEIGHT,new OrthographicCamera());
        //camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        //viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        stage = new Stage(viewport, myAm.batch);

        Table table = new Table();//organize things in stage
        table.top();
        table.setFillParent(true);//set table the same size of stage

        //scoreLable = new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        //snakeLabel = new Label("Player 1",new Label.LabelStyle(new BitmapFont(),Color.BLACK));
        for (Map.Entry<String, int[]> entry : myAm.userdata.entrySet()) {
            Label nameLabel = new Label(entry.getKey(),myskin,"big");
            nameList.add(entry.getKey());
            nameLabelList.add(nameLabel);

            Label scoreLable = new Label(String.format("%06d",0),myskin,"big");
            //Object value = entry.getValue();
            scoreLabelList.add(scoreLable);
            table.add(nameLabel).expandX();
            table.add(scoreLable).expandX();
            table.row();
        }
        //table.add(snakeLabel).expandX().padTop(10);


        stage.addActor(table);
    }

    public void updateScore(){
        for (int i = 0; i < nameList.size(); i++){
            int newscore = myAm.userdata.get(nameList.get(i))[2];
            Label scoreLabel = scoreLabelList.get(i);
            scoreLabel.setText(String.format("%06d",newscore));
        }
        //System.out.println("update score = "+this.score);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
