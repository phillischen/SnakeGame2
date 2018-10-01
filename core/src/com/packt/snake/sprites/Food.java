package com.packt.snake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;
import java.util.Arrays;


public class Food implements Disposable{
    private Texture food,background;
    private ArrayList<int[]> foodlist = new ArrayList<int[]>();
    private int foodX, foodY;
    private static final int padding = 300;
    private java.util.Random randomX = new java.util.Random(1);
    private java.util.Random randomY = new java.util.Random(1);
    private int maxWidth;
    private int maxHeigh;
    private SnakeGame mygame;
    private MyAssetsManager myAm;

    public Food (SnakeGame game){
        mygame = game;
        myAm = mygame.getAm();
        myAm.loadMap();
        myAm.manager.finishLoading();
        background = myAm.manager.get(myAm.MAP1);
        maxWidth = background.getWidth() - padding;
        maxHeigh = background.getHeight() - padding;
        food = new Texture("apple.png");
        placeFood();

    }

    public void placeFood(){ //randomly put food on screen
        if (foodlist.size()<20){ //no food no screen
            do{
                //need to change to unified canvas size
//                foodX = randomX.nextInt() + padding;
//                foodY = randomY.nextInt() + padding;
                foodX = padding+randomX.nextInt(maxWidth);
                foodY = padding+randomY.nextInt(maxHeigh);
                System.out.print(foodX+"  ");
                System.out.print(foodY);
                System.out.println();
                //need a method to avoid food on snake!!
                int[] x = {foodX,foodY};
                foodlist.add(x);
            }while (foodlist.size() < 20);
        }
    }


    public ArrayList<int[]> getFoodlist() {
        return foodlist;
    }

    public Texture getFood() {
        return food;
    }

    public void removeFood(int[] headpostion){
        arraylistRemove(foodlist,headpostion);
    }

    private ArrayList<int[]> arraylistRemove(ArrayList<int[]> al, int[] lst){
        for (int[] x: al){
            if (Arrays.equals(x,lst)){
                al.remove(x);
                return al;
            }
        }
        return al;
    }

    @Override
    public void dispose() {
        food.dispose();
    }
}
