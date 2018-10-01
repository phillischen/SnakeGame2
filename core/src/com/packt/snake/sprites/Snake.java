package com.packt.snake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;


public class Snake implements Disposable{
    public static int getInitSize() {
        return initSize;
    }

    /**---Initialize Parameters---**/
    private static final int initSize = 50;
    private static final int growRatio = 50;
    private static final int interval = 15;
    private static final int step = 15;
    private static final double maxRotate = 20.0;

    private int headPosX = 100, headPosY = 100;

    private Array<SnakeBody> body = new Array<SnakeBody>();
    private Texture headTexture;
    private Texture bodyTexture;
    private boolean directionSet = false;

    private double currentDirection = 0.0;
    private double settingDirection = 0.0;
    private int score = 0;
    private int size = 50;
    private SnakeGame game;
    private String myUsername;


    private MyAssetsManager myAm;

    public Snake(SnakeGame game){
        this.game = game;
        myAm = this.game.getAm();
        myUsername = myAm.myUsername;
        myAm.loadSnake(1);
        myAm.manager.finishLoading();
        headTexture = myAm.manager.get(myAm.SNAKEHEAD1);
        bodyTexture = myAm.manager.get(myAm.SNAKEBODY1);

        resizeBody(initSize);
        for(int i=1;i<=3;i++){
            body.add(new SnakeBody(headPosX -i*interval, headPosY -i*interval));
        }
    }

    public Snake(SnakeGame game, int headPosX, int headPosY, String name){
        this.game = game;
        myAm = this.game.getAm();
        myUsername = name;
        this.headPosX = headPosX;
        this.headPosY = headPosY;

        //load skin based on name!!!
        myAm.loadSnake(1);
        myAm.manager.finishLoading();
        headTexture = myAm.manager.get(myAm.SNAKEHEAD1);
        bodyTexture = myAm.manager.get(myAm.SNAKEBODY1);

        resizeBody(initSize);
        for(int i=1;i<=3;i++){
            body.add(new SnakeBody(this.headPosX -i*interval, this.headPosY -i*interval));
        }
    }

    public class SnakeBody {
        private int x, y;
        //private Texture texture = new Texture("snake.png");

        public SnakeBody(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void updateBodyPosition(int x, int y){
            this.x = x; this.y = y;
        }

        public void draw(Batch batch){
            if (!(x == headPosX && y == headPosY))
                batch.draw(bodyTexture, x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public void updateBodyPartsPosition(int headXBeforeUpdate,int headYBeforeUpdate){
        if(body.size > 0){
            body.removeIndex(body.size-1);
            SnakeBody tempPart = new SnakeBody(headXBeforeUpdate,headYBeforeUpdate);
            body.insert(0,tempPart);
        }
    }

    public void drawSnake(SpriteBatch sb){
        for(int i = body.size-1;i>=0;i--){
            body.get(i).draw(sb);
        }
        sb.draw(headTexture, headPosX, headPosY);
    }

    public void lengthenBody(int foodX, int foodY){
        SnakeBody newBodyPart = new SnakeBody(foodX, foodY);
        body.insert(0,newBodyPart); //insert at front of array - body of the snake
    }

    public void moveSnake(){
        double deviation = settingDirection - currentDirection;
        double deltaX;
        double deltaY;
        //System.out.println("settingDirection "+settingDirection);
        //System.out.println("currentDirection "+currentDirection);
        //System.out.print("deviation ");
       // System.out.println(deviation);
        if(Math.abs(deviation) <= maxRotate){
            deltaX = step*Math.cos(settingDirection/180*Math.PI);
            deltaY = step*Math.sin(settingDirection/180*Math.PI);
            this.currentDirection = this.settingDirection;
        }else {
            if(deviation>0){
                if(deviation <= 180){
                    this.currentDirection = this.currentDirection+this.maxRotate;
                    if(this.currentDirection >= 360){
                        this.currentDirection -= 360;
                    }
                }else{
                    this.currentDirection = this.currentDirection-this.maxRotate;
                    if(this.currentDirection < 0){
                        this.currentDirection += 360;
                    }
                }

            }else{
                if(deviation >= -180){
                    this.currentDirection = this.currentDirection-this.maxRotate;
                    if(this.currentDirection < 0){
                        this.currentDirection += 360;
                    }
                }else{
                    this.currentDirection = this.currentDirection+this.maxRotate;
                    if(this.currentDirection >= 360){
                        this.currentDirection -= 360;
                    }
                }

            }
            deltaX = step*Math.cos(this.currentDirection/180*Math.PI);
            deltaY = step*Math.sin(this.currentDirection/180*Math.PI);
        }
        this.headPosX += deltaX;
        this.headPosY += deltaY;
    }

    public boolean checkEdge(){ //need to change
        if (headPosX +50 >= myAm.mapsize){
            //headPosX = 0;
            return true;
        }
        if (headPosX -50 <= 0){
            //headPosX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
            return true;
        }
        if (headPosY +50 >= myAm.mapsize){
            //headPosY = 0;
            return true;
        }
        if (headPosY -50 <= 0){
            //headPosY = Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
            return true;
        }
        return false;
    }

    public boolean checkSnakeBodyCollision(){
        for (SnakeBody bodypart : body){
            if (bodypart.x == headPosX && bodypart.y == headPosY)
                return true;
        }
        return false;
    }

    public void restartSnake(){
        body.clear();
        currentDirection = 0.0;
        directionSet = false;
        headPosX = 0;
        headPosY = 0;
    }

    private void resizeBody(int size){
        Pixmap pixmapOrigin = new Pixmap(Gdx.files.internal("snakehead.png"));

        Pixmap newPixmap = new Pixmap(size, size, pixmapOrigin.getFormat());
        newPixmap.drawPixmap(pixmapOrigin,
                0, 0, pixmapOrigin.getWidth(), pixmapOrigin.getHeight(),
                0, 0, newPixmap.getWidth(), newPixmap.getHeight()
        );
        headTexture = new Texture(newPixmap);

        pixmapOrigin = new Pixmap(Gdx.files.internal("snakebody.png"));
        newPixmap = new Pixmap(size, size, pixmapOrigin.getFormat());
        newPixmap.drawPixmap(pixmapOrigin,
                0, 0, pixmapOrigin.getWidth(), pixmapOrigin.getHeight(),
                0, 0, newPixmap.getWidth(), newPixmap.getHeight()
        );
        bodyTexture = new Texture(newPixmap);
        pixmapOrigin.dispose();
        newPixmap.dispose();
    }

    public ArrayList<Array<Integer>> getDeadSnake(){
        ArrayList<Array<Integer>> deadSnake = new ArrayList<Array<Integer>>();
        Array<Integer> headPos = new Array<Integer>();
        headPos.add(headPosX);
        headPos.add(headPosX);
        deadSnake.add(headPos);
        for (SnakeBody sb: body){
            Array<Integer> nodePos = new Array<Integer>();
            nodePos.add(sb.x);
            nodePos.add(sb.y);
            deadSnake.add(nodePos);
        }
        return deadSnake;
    }

    @Override
    public void dispose() {
        headTexture.dispose();
//        bodyTexture.dispose();
    }

    /**--Setters and Getters--**/

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void updateSize(){
        size = initSize + this.score/growRatio;
        resizeBody(size);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScore(){
        myAm.addScore(myUsername);
    }

    public double getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(double currentDirection) {
        this.currentDirection = currentDirection;
    }

    public double getSettingDirection() {
        return settingDirection;
    }

    public void setSettingDirection(double settingDirection) {
        this.settingDirection = settingDirection;
    }

    public int getHeadPosX() {
        return headPosX;
    }

    public int getHeadPosY() {
        return headPosY;
    }

    public void setHeadPosX(int i) {
        this.headPosX = i;
    }

    public void setHeadPosY(int i) {
        this.headPosY = i;
    }

    public Array<SnakeBody> getBody() {
        return body;
    }

    public void setDirectionSet(boolean directionSet) {
        this.directionSet = directionSet;
    }

    public String getMyUsername() {
        return myUsername;
    }
}
