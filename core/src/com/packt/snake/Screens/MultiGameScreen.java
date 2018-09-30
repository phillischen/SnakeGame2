package com.packt.snake.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.Scenes.Hud;
import com.packt.snake.SnakeGame;
import com.packt.snake.sprites.Food;
import com.packt.snake.sprites.Snake;

import java.util.ArrayList;

public class MultiGameScreen implements Screen{

    private static final float MOVE_TIME = 0.03F;

    private static float speed = 0.1f;
    private float timer = speed;
    private static final float roomOutRatio = 0.2f;

    private enum STATE {
        NORMAL, SPEEDUP, GAME_OVER
    }

    private STATE state = STATE.NORMAL;
    private STATE previousState = STATE.NORMAL;

    private Texture background;
    private float screenWidth;
    private float screenHeight;

    private SnakeGame game;
    private MyAssetsManager myAM;
    private Hud hud;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Food myfood;
    private Snake mySnake;
    private ArrayList<Snake> snakeList = new ArrayList<Snake>();
    private int directionDegree = 0, lastdirection = directionDegree;

    private float stateTimer;//count how many seconds the current state last
    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();
    FlingDirection myFlingDirection;

    public MultiGameScreen(SnakeGame game) {
        this.game = game;
        myAM = this.game.getAm();
        myfood = new Food();
        //initiate the snake controlled my device owner
        int position = getInitialPosition(myAM.myUsername);
        mySnake = new Snake(this.game,position,position,myAM.myUsername);
        //initial snakes for remote users
        for (String name : myAM.userdata.keySet()){
            if (!name.equals(myAM.myUsername)){
                int position2 = getInitialPosition(name);
                snakeList.add(new Snake(this.game,position2,position2,name));
            }
        }

        viewport = new FitViewport(myAM.V_WIDTH, myAM.V_HEIGHT);
        camera = new OrthographicCamera(screenWidth, screenHeight);

        camera.position.set(mySnake.getHeadPosX(), mySnake.getHeadPosY(), 0);
        screenWidth = myAM.V_WIDTH*3;
        screenHeight = myAM.V_HEIGHT*3;
        camera.viewportWidth = screenWidth;
        camera.viewportHeight = screenHeight;

        hud = new Hud(myAM);

    }

    @Override
    public void show() {
        camera.update();
        myAM.loadMap();
        myAM.manager.finishLoading();
        background = myAM.manager.get(myAM.MAP1);
        //background = new Texture("map10000.png");
        myAM.mapsize = background.getWidth();
        bitmapFont = new BitmapFont();
        myFlingDirection = new FlingDirection();
        Gdx.input.setInputProcessor(new GestureDetector(myFlingDirection));
    }

    private int getInitialPosition(String name){
        for (int i = 0; i < myAM.userlist.length;i++){
            if (name.equals(myAM.userlist[i]))
                return (i+1)*500;
        }
        return 500;
    }

    @Override
    public void resize(int width, int height) {
        //not much use for android phone, bcs they cannot resize the window
        //camera.viewportWidth = width/2;
        //camera.viewportHeight = height/2;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //background.dispose();
        mySnake.dispose();
        hud.dispose();
    }

    @Override
    public void render(float delta) {
        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = state == previousState ? stateTimer + delta : 0;
        //update previous state
        previousState = state;
        switch (state) {
            case NORMAL: {
//                queryInput();
                updateSnake(delta);
            }
            break;
            case GAME_OVER: {
                if (stateTimer >= 1) { //wait for 1 second after died
                    //game.setScreen(new GameOverScreen(game));
                    game.startGameOver();
                    dispose();
                }
            }
            break;
        }
        clearScreen();
        draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b,Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        myAM.batch.setProjectionMatrix(camera.projection);
        myAM.batch.setTransformMatrix(camera.view);

        game.batch.begin();
        // game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(background, 0, 0);
        mySnake.drawSnake(game.batch);
        for(Snake snake:snakeList){
            snake.drawSnake(game.batch);
        }
        //draw food
        for (int[] x : myfood.getFoodlist()) {
            myAM.batch.draw(myfood.getFood(), x[0], x[1]);
        }
        myAM.batch.end();

        //draw the score hub
        myAM.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private void checkFoodCollision(Snake snake) {
        int[] head = {snake.getHeadPosX(), snake.getHeadPosY()};
        //Gdx.app.log("MYTAG","head = "+Arrays.toString(head));
        if (checkContain(myfood.getFoodlist(), head)) {
            //Gdx.app.log("MYTAG","yes, hit the food!");
            snake.lengthenBody(head[0], head[1]);
            snake.updateScore();
        }
    }

    private boolean checkContain(ArrayList<int[]> al, int[] lst) {
        for (int[] x : al) {
            float collisionRadius = (x[0] - lst[0]) * (x[0] - lst[0]) + (x[1] - lst[1]) * (x[1] - lst[1]);
            //Gdx.app.log("collisionRadius", "asd");
            if (collisionRadius <= (mySnake.getSize()/2+16)*(mySnake.getSize()/2+16)) {
                myfood.removeFood(x);
                return true;
            }
        }
        return false;
    }

    private void updateSnake(float delta){
        timer-=delta;
        if(timer<=0){
            timer=speed;

            int snakeXBeforeUpdate = mySnake.getHeadPosX();
            int snakeYBeforeUpdate = mySnake.getHeadPosY();
            if (lastdirection != directionDegree){
                lastdirection = directionDegree;
                myAM.setNewDirection(directionDegree);
            }
            mySnake.setSettingDirection(myAM.getMyDirection());

            mySnake.moveSnake();

            if (mySnake.checkEdge()){
                mySnake.setHeadPosX(snakeXBeforeUpdate);
                mySnake.setHeadPosY(snakeYBeforeUpdate);
                state = STATE.GAME_OVER;
            } else {
                mySnake.updateBodyPartsPosition(snakeXBeforeUpdate,snakeYBeforeUpdate);
            }

            checkFoodCollision(mySnake);
            myfood.placeFood();
            mySnake.updateSize();

            for (int i = snakeList.size()-1;i>=0;i--){
                Snake snake = snakeList.get(i);
                int snkXB4Update = snake.getHeadPosX();
                int snkYB4Update = snake .getHeadPosY();
                snake.setSettingDirection(myAM.getRemoteDirection(snake.getMyUsername()));
                snake.moveSnake();
                if(snake.checkEdge()){
                    snake.setHeadPosX(snakeXBeforeUpdate);
                    snake.setHeadPosY(snakeYBeforeUpdate);
                    snakeList.remove(i);//really needed?
                    state = STATE.GAME_OVER;
                }else{
                    snake.updateBodyPartsPosition(snkXB4Update,snkYB4Update);
                }
                checkFoodCollision(snake);
                myfood.placeFood();
                snake.updateSize();
            }

            camera.position.set(mySnake.getHeadPosX(), mySnake.getHeadPosY(), 0);
            int i = mySnake.getScore()/100;
            camera.viewportWidth = screenWidth * (1+i*roomOutRatio);
            camera.viewportHeight = screenHeight * (1+i*roomOutRatio);

            camera.update();

            hud.updateScore();

        }
    }



    public class FlingDirection implements GestureDetector.GestureListener {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            float velocityX = x - mySnake.getHeadPosX();
            System.out.println("velocity x = "+mySnake.getHeadPosX()+ ", "+x);
            float velocityY = mySnake.getHeadPosY() - y;
            System.out.println("velocity y = "+mySnake.getHeadPosY()+ ", "+y);
            if (stateTimer < 0.5)
                return false;
            if (velocityX >= 0 && velocityY >= 0) {
                directionDegree = 360 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            } else if (velocityX >= 0 && velocityY <= 0) {
                directionDegree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            } else if (velocityX <= 0 && velocityY >= 0) {
                directionDegree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX)) + 180;
                //System.out.println("degree = " + directionDegree);
            } else {
                directionDegree = 180 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            }
            //mySnake.setSettingDirection(directionDegree);
            System.out.println("TAP called! "+directionDegree);
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            //System.out.println("fling called!");
            if (stateTimer < 0.5)
                return false;
            if (velocityX >= 0 && velocityY >= 0) {
                directionDegree = 360 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            } else if (velocityX >= 0 && velocityY <= 0) {
                directionDegree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            } else if (velocityX <= 0 && velocityY >= 0) {
                directionDegree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX)) + 180;
                //System.out.println("degree = " + directionDegree);
            } else {
                directionDegree = 180 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
                //System.out.println("degree = " + directionDegree);
            }
            //mySnake.setSettingDirection(directionDegree);
            System.out.println("fling called! "+directionDegree);
            return false;
        }


    }

}

